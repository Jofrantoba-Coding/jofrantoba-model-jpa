# jofrantoba-model-jpa

Generic DAO toolkit for **JPA / Hibernate 6** applications. It provides a reusable repository base class, `AbstractJpaDaoV2<T>`, with CRUD operations, a string-based DSL for dynamic filters, HQL/entity joins, native relational joins, pagination, grouping, stored procedures, and JSON-style native query results.

**Requirements:** Java 17+ | Maven 3.8+ | Hibernate 6.5+ | Jakarta Persistence 3.2+

Full documentation:

- [Home](https://jofrantoba-coding.github.io/jofrantoba-model-jpa)
- [Getting Started](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/getting-started)
- [DSL Guide](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/dsl-guide)
- [API Reference](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/api-reference)
- [Method Examples](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/method-examples)
- [Repository Examples](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/repository-examples)

## What This Library Solves

Most enterprise persistence layers repeat the same patterns:

- CRUD for JPA entities.
- Dynamic filters from screens or reports.
- HQL joins across entity relationships.
- Native SQL joins for reporting-style result sets.
- Direct JDBC/ResultSet execution for native JSON results without hydrating JPA entities.
- Pagination with `limit` / `offset`.
- `group by`, subqueries, and aggregate projections.
- Stored procedure calls.
- Export-style SQL queries.

`AbstractJpaDaoV2<T>` centralizes those patterns so each repository only declares its entity type and its domain-specific methods.

## Core Concepts

### Repository Shape

```java
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.daoentity.InterCrud;

public interface InterDaoProduct extends InterCrud<Product> {
    Collection<Product> activeProducts() throws Exception;
}

public class DaoProduct extends AbstractJpaDaoV2<Product> implements InterDaoProduct {

    public DaoProduct() {
        setClazz(Product.class);
    }

    @Override
    public Collection<Product> activeProducts() throws Exception {
        return allFieldsFilterAnd(
            new String[]{"=:base.active:true"},
            new String[]{"base.name:asc"}
        );
    }
}
```

### Entity `T` And The `base` Alias

In HQL/entity methods, `T` is the root entity and `base` is the alias created by `AbstractJpaDaoV2`.

```java
public class DaoProduct extends AbstractJpaDaoV2<Product> {
    public DaoProduct() {
        setClazz(Product.class);
    }
}

Collection<Product> rows = dao.allFieldsFilterAnd(
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

Here `active` and `name` are Java properties of `Product`.

In native SQL methods, `base` must be declared explicitly:

```java
String table = "jofrantoba.catalog.products as base";
```

If a native `fields`, `joinTables`, `filters`, `order`, or `groupBy` entry uses `base.*`, then `table` must include `as base`.

## DSL At A Glance

Filters are strings in this form:

```text
operator:field:value
```

Examples:

```java
String[] filters = {
    "=:base.active:true",
    "equal:base.status:PUBLISHED",
    "like:base.name:%phone%",
    "between:base.price:100:500",
    "in:base.categoryId:10:11:12",
    "isnotnull:base.createdAt"
};

String[] order = {
    "base.name:asc",
    "base.createdAt:desc"
};
```

Common operators:

| Operator | Use |
|----------|-----|
| `=` / `!=` / `>` / `<` / `>=` / `<=` | Parsed numeric, boolean, or string values |
| `equal` / `notequal` | Text equality |
| `like` | Pattern matching |
| `between` | Ranges |
| `in` | Multiple values |
| `isnull` / `isnotnull` | Null checks |

See the [DSL Guide](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/dsl-guide) for the full contract.

## HQL Entity Joins

Use HQL joins when the relationship is mapped in your entity model.

```java
Collection<Product> rows = dao.customFieldsJoinFilterAnd(
    "base.id as id, base.name as name, category.name as categoryName",
    "left:category",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

Multiple entity joins:

```java
Collection<Product> rows = dao.customFieldsJoinFilterAnd(
    "base.id as id, base.name as name, category.name as categoryName, brand.name as brandName",
    new String[]{"inner:category", "left:brand"},
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

## Native Relational Joins

Use native joins when you need table-level SQL and `ArrayNode` results.
These methods bypass Hibernate entity hydration for the result rows: `AbstractJpaDaoV2` executes the SQL through JDBC `PreparedStatement`, reads the `ResultSet`, and maps each row directly to Jackson `ObjectNode` / `ArrayNode`.

That is useful for high-volume reports, grids, exports, dashboards, and multi-table projections where creating managed JPA entities would add unnecessary overhead.

```java
ArrayNode rows = dao.allFieldsJoinPostgres(
    new String[]{
        "inner:jofrantoba.catalog.categories as category:on:base.category_id:category.id",
        "left:jofrantoba.catalog.brands as brand:on:base.brand_id:brand.id"
    },
    "jofrantoba.catalog.products as base",
    "base.id, base.name, category.name as categoryName, brand.name as brandName",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"},
    "and"
);
```

The optimized native-to-JSON path is used by methods such as:

- `allFieldsPostgres`
- `allFieldsLimitOffsetPostgres`
- `allFieldsJoinPostgres`
- `allFieldsJoinLimitOffsetPostgres`
- `allFieldsJoinPostgresGroupBy`
- `allFieldsJoinPostgresGroupBySubQuery`
- `allFieldsLimitJoinPostgresGroupBySubQuery`

Native join format:

```text
joinType:schema.table as alias:on:leftColumn:rightColumn
```

Multiple join conditions:

```java
"inner:jofrantoba.auth.user_entity as userEntity:on:userEntity.realm_id:system.realm_id:and:userEntity.id:base.user_entity_id"
```

Trusted internal subquery:

```java
"left:(select * from jofrantoba.catalog.stock where active=true) as stock:on:base.id:stock.product_id"
```

## Grouping And Subqueries

Complex report repositories can build a subquery, group it, and then group the outer result:

```java
ArrayNode rows = dao.allFieldsJoinPostgresGroupBySubQuery(
    "subquery.categoryName, sum(subquery.total) as total",
    "subquery.categoryName",
    new String[]{"subquery.categoryName:asc"},
    new String[]{
        "inner:jofrantoba.catalog.categories as category:on:base.category_id:category.id",
        "left:(select * from jofrantoba.catalog.stock where active=true) as stock:on:base.id:stock.product_id"
    },
    "jofrantoba.catalog.products as base",
    "category.name as categoryName, count(base.id) as total",
    new String[]{"=:base.active:true"},
    "category.name"
);
```

For the full repository pattern, see [Repository Examples](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/repository-examples).

## SQL Safety Contract

`AbstractJpaDaoV2` parameterizes:

- DSL filter values.
- HQL named parameters.
- Native `PreparedStatement` values.
- `limit` and `offset`.

It validates:

- DSL field identifiers.
- Order fields and directions.
- Native join types.
- Native table references.
- Join condition identifiers.

These fragments are SQL structure and must come from constants or trusted server-side code:

- `table`
- `fields`
- `groupBy`
- subquery bodies inside `joinTables`
- raw SQL passed to `sqlExportTOExcel(sql)` or `iudNativeQuery(sql)`

Do not build those fragments from raw user input.

## Database Configuration

```java
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import org.hibernate.SessionFactory;
import java.util.List;

SessionFactory sf = PSF.getInstance().buildPSF(
    "main",
    new ConnectionPropertiesPostgre(
        "localhost",
        5432,
        "myapp_db",
        "user",
        "password"
    ),
    List.of("com.example.myapp.entity")
);
```

Supported connection property classes:

- `ConnectionPropertiesMysql`
- `ConnectionPropertiesPostgre`
- `ConnectionPropertiesOracle`
- `ConnectionPropertiesSqlServer`

Bundled driver compatibility:

| Database | Driver | Minimum database version by driver |
|----------|--------|------------------------------------|
| MySQL | `mysql-connector-java:8.0.28` | MySQL 5.7 |
| PostgreSQL | `postgresql:42.7.2` | PostgreSQL 8.4 |
| Oracle Database | `ojdbc6:11.2.0.4` | Oracle Database 11.2.0.4 |
| SQL Server | `mssql-jdbc:12.8.2.jre11` | SQL Server 2016 |

See [Database Configuration](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/configuration).

## Maven

```xml
<dependency>
    <groupId>com.jofrantoba.model.jpa</groupId>
    <artifactId>jofrantoba-model-jpa</artifactId>
    <version>2.0.0</version>
</dependency>
```

Build from source:

```bash
git clone https://github.com/Jofrantoba-Coding/jofrantoba-model-jpa.git
cd jofrantoba-model-jpa
mvn clean install -DskipTests
```

## Documentation Map

| Page | Purpose |
|------|---------|
| [Getting Started](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/getting-started) | First DAO and database connection |
| [DSL Guide](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/dsl-guide) | Full DSL contract |
| [API Reference](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/api-reference) | Public methods and support classes |
| [Method Examples](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/method-examples) | Example for each public method |
| [Repository Examples](https://jofrantoba-coding.github.io/jofrantoba-model-jpa/repository-examples) | Complex patterns from `.examples` |

## License

MIT. See [LICENSE](LICENSE).
