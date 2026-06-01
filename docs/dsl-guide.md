---
title: DSL Guide
nav_order: 4
---

# DSL Guide
{: .no_toc }

The framework includes a **declarative, string-based DSL** that translates plain arrays of strings into HQL/SQL at runtime. No manual query writing required; the same syntax works across all supported databases.

<details open markdown="block">
  <summary>Contents</summary>
  {: .text-delta }
- TOC
{:toc}
</details>

---

## Syntax overview

```
"operator:field:value"
```

- Separator is always **`:`** (colon).
- All expressions are passed as a `String[]` array.
- Arrays combine with `AND` (or `OR`) depending on the method called.

```java
String[] filters = {
    "=:base.active:true",
    ">:base.price:10.0",
    "like:base.name:%shirt%"
};
String[] order  = {"base.name:asc"};
String   fields = "base.id as id, base.name as name, base.price as price";

Collection<Product> results = dao.customFieldsFilterAnd(fields, filters, order);
```

---

## Entity `T` and the `base` alias

`AbstractJpaDaoV2<T>` always treats `T` as the root entity for HQL/entity methods.
In those methods, the DAO builds queries using `base` as the alias for `T`.

```java
public class ProductDao extends AbstractJpaDaoV2<Product> {
    public ProductDao() {
        setClazz(Product.class);
    }
}

Collection<Product> rows = dao.allFieldsFilterAnd(
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

For HQL methods, `base.active` and `base.name` are Java entity properties of `Product`, not database column names.
HQL joins also start from `base`:

```java
String joinTable = "left:category";
```

This means:

```sql
from Product as base
left join base.category category
```

For native SQL methods, `base` is not created from `T`. You must define it in the `table` argument:

```java
String table = "jofrantoba.catalog.products as base";
String[] filters = {"=:base.active:true"};
```

If a native filter, field, join, order, or group uses `base.*`, then `table` must include `as base`.

---

## Comparison operators

### Numeric / boolean

| Operator | Example | Generated SQL |
|----------|---------|---------------|
| `=`  | `"=:price:50"` | `price = 50` |
| `!=` | `"!=:status:0"` | `status != 0` |
| `>`  | `">:stock:0"` | `stock > 0` |
| `<`  | `"<:price:100"` | `price < 100` |
| `>=` | `">=:age:18"` | `age >= 18` |
| `<=` | `"<=:score:10"` | `score <= 10` |

### Text (adds quotes in the query)

| Operator | Example | Generated SQL |
|----------|---------|---------------|
| `equal`    | `"equal:city:Madrid"` | `city = 'Madrid'` |
| `notequal` | `"notequal:status:CANCELLED"` | `status != 'CANCELLED'` |
| `like`     | `"like:name:%john%"` | `name LIKE '%john%'` |

---

## NULL operators

```java
"isnull:base.deletedAt"       // deletedAt IS NULL
"isnotnull:base.email"        // email IS NOT NULL
```

{: .warning }
Never use `"=:field:null"` — it does not work. Always use `isnull` / `isnotnull`.

---

## Range and collection operators

```java
"between:price:10:500"        // price BETWEEN 10 AND 500
"in:categoryId:1:2:3:4"      // categoryId IN (1,2,3,4)
```

---

## ORDER BY

```java
String[] order = {
    "base.lastName:asc",
    "base.firstName:asc",
    "base.createdAt:desc"
};
```

---

## SELECT fields with aliases

```java
String fields =
    "base.id as id, "              +
    "base.name as name, "          +
    "dept.name as department, "    +
    "COUNT(*) as total";
```

---

## HQL JOIN syntax

Use this format with HQL/entity methods such as `customFieldsJoinFilterAnd`, `allFieldsJoinFilterAnd`, and `rowCountJoinFilterAnd`.

### Single JOIN

```java
String joinTable = "left:department";    // LEFT JOIN department
String joinFetch = "inner:customer:fetch";
```

### Multiple JOINs

```java
String[] joinTables = {
    "left:department",    // LEFT JOIN department
    "inner:company",      // INNER JOIN company
    "right:region"        // RIGHT JOIN region
};
```

| DSL token | SQL keyword |
|-----------|-------------|
| `left`    | LEFT JOIN   |
| `inner`   | INNER JOIN  |
| `right`   | RIGHT JOIN  |

---

## Native JOIN syntax

Use this format with native relational methods such as `allFieldsJoinPostgres`, `allFieldsJoinLimitOffsetPostgres`, and the `*GroupBySubQuery` variants.
These methods use an optimized native-to-JSON path: Hibernate provides the JDBC connection, but the query runs through `PreparedStatement`, reads a `ResultSet`, and maps rows directly to `ArrayNode`.

This avoids hydrating JPA entities and is intended for reports, dashboards, exports, and complex projections.

```java
String[] joinTables = {
    "inner:jofrantoba.catalog.categories as category:on:base.category_id:category.id",
    "inner:jofrantoba.catalog.departments as department:on:category.department_id:department.id",
    "left:(select * from jofrantoba.catalog.stock where active=true) as stock:on:base.id:stock.product_id"
};
```

Multiple join conditions are expressed as additional colon-separated tokens:

```java
String[] joinTables = {
    "left:jofrantoba.catalog.warehouse_stock as wstock:on:wstock.stock_id:stock.id:and:wstock.product_id:base.id"
};
```

Valid native join types are `left`, `inner`, `right`, `cross`, and `full`.
Normal table references must use `schema.table` or `schema.table as alias`.
Subqueries are allowed only for trusted internal SQL.

---

## SQL safety contract

`AbstractJpaDaoV2` parameterizes DSL filter values, `limit`, and `offset`.
It also validates filter/order identifiers, native join types, table references, and join condition identifiers.

### What is safe to accept from user input

| Parameter | Safe from user input? | Reason |
|-----------|----------------------|--------|
| DSL filter **values** — `"=:field:VALUE"` | ✅ Yes | Bound as JDBC/HQL named parameter |
| `limit`, `offset` | ✅ Yes | Bound as JDBC parameter |
| DSL filter **field names** — `"=:FIELD:value"` | ⚠️ Validated | Regex-checked (`schema.field` pattern); still use server-side constants |
| `mapOrder` field names | ⚠️ Validated | Regex-checked + direction forced to `asc`/`desc`; still use server-side constants |
| `fields` (SELECT clause) | ❌ No | Concatenated directly into SQL |
| `table` | ❌ No | Concatenated directly into SQL |
| `groupBy` | ❌ No | Concatenated directly into SQL |
| `joinTables` | ❌ No | Join type and identifier tokens are validated, subquery bodies are not |
| `sql` in `iudNativeQuery(sql)` | ❌ No | Executes raw SQL |
| `sql` in `sqlExportTOExcel(sql)` | ❌ No | Executes raw SQL |

**Rule of thumb:** filter *values* can come from the user; SQL *structure* (table names, column lists, joins, subqueries) must come from server-side constants.

---

## Choosing the right method

| Goal | Returns | Method family |
|------|---------|---------------|
| Fetch entity objects, AND filters | `Collection<T>` | `customFieldsFilterAnd`, `allFieldsFilterAnd` |
| Fetch entity objects, OR filters | `Collection<T>` | `customFieldsFilterOr`, `allFieldsFilterOr` |
| Entity objects + JOIN | `Collection<T>` | `customFieldsJoinFilterAnd`, `allFieldsJoinFilterAnd` |
| Paginated entity objects | `Collection<T>` | Any of the above with `page, size` overload |
| JSON results, native SQL (PostgreSQL) | `ArrayNode` | `allFieldsJoinPostgres`, `allFieldsLimitOffsetPostgres` |
| JSON with GROUP BY | `ArrayNode` | `allFieldsJoinPostgresGroupBy` |
| Paginated JSON | `ArrayNode` | `allFieldsLimitOffsetPostgres`, `allFieldsJoinLimitOffsetPostgres` |
| COUNT | `Long` | `rowCountJoinFilterAnd`, `rowCountJoinsFilterAnd` |
| MAX / aggregate | `Object` / `Long` | `maxValueJoinFilterAnd`, `aggregateJoinFilterAndGroupBy` |
| Stored procedure | varies | `iudProcedureJson`, `listProcedureMsql` |
| Native INSERT | `int` | `saveNativeQuery` |

---

## Methods that support the DSL

```java
// Basic filters
dao.customFieldsFilterAnd(fields, filters, order)
dao.customFieldsFilterOr(fields, filters, order)

// WITH JOIN (single join)
dao.customFieldsJoinFilterAnd(fields, joinTable, filters, order)
dao.customFieldsJoinFilterAnd(fields, joinTable, filters, order, pageNumber, pageSize)

// Native PostgreSQL / relational queries (returns ArrayNode JSON)
dao.allFieldsJoinPostgres(joins, table, fields, filters, order, "and")
dao.allFieldsJoinPostgresGroupBy(joins, table, fields, filters, order, groupBy)
dao.allFieldsLimitOffsetPostgres(table, fields, filters, order, limit, offset)

// Aggregates
dao.rowCountJoinFilterAnd(joinTable, filters)
dao.aggregateJoinFilterAndGroupBy(joinTable, filters, groupBy)
```

---

## Practical examples

### Filter active records with pagination

```java
Collection<Product> page = dao.customFieldsFilterAnd(
    "base.id, base.name, base.price",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"},
    1,    // pageNumber
    20    // pageSize
);
```

### JOIN with two tables

```java
String joinTable = "left:category";
String[] filters = {
    "=:base.active:true",
    "isnotnull:category.id"
};
String fields =
    "base.id as id, base.name as name, " +
    "category.name as categoryName, base.price as price";

Collection<Product> results = dao.customFieldsJoinFilterAnd(
    fields, joinTable, filters, new String[]{"base.name:asc"}
);
```

Generated HQL equivalent:
```sql
SELECT base.id as id, base.name as name,
       category.name as categoryName, base.price as price
FROM Product base
LEFT JOIN base.category category
WHERE 1=1
  AND base.active = true
  AND category.id IS NOT NULL
ORDER BY base.name ASC
```

### Hierarchical parent-child data

A common pattern for tree-structured entities (menus, categories, settings):

```java
// Entity with self-reference
@ManyToOne  @JoinColumn(name = "parent_id")
private Category parent;

@OneToMany(mappedBy = "parent")
private List<Category> children;

// DAO: get all root nodes (no parent)
public Collection<Category> roots() throws Exception {
    return customFieldsJoinFilterAnd(
        "base.id, base.name, base.code",
        "left:parent",
        new String[]{"isnull:parent.id", "=:base.active:true"},
        new String[]{"base.name:asc"}
    );
}

// DAO: get children of a specific parent
public Collection<Category> childrenOf(Long parentId) throws Exception {
    return customFieldsJoinFilterAnd(
        "base.id, base.name, base.code, parent.id as parentId",
        "left:parent",
        new String[]{"=:parent.id:" + parentId, "=:base.active:true"},
        new String[]{"base.name:asc"}
    );
}
```

### GROUP BY with aggregation (PostgreSQL)

```java
String table  = "orders as base";
String fields = "base.status, COUNT(*) as total, SUM(base.amount) as revenue";
String[] filters = {"isnotnull:base.closedAt"};
String[] order   = {"revenue:desc"};
String groupBy   = "base.status";

ArrayNode result = dao.allFieldsJoinPostgresGroupBy(
    new String[]{}, table, fields, filters, order, groupBy
);
```

### Pagination with limit/offset (PostgreSQL)

```java
long limit  = 10;
long offset = 20;  // page 3 = (page-1) * limit

ArrayNode page = dao.allFieldsLimitOffsetPostgres(
    "public.products as base",
    "base.id, base.name, base.price",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"},
    limit, offset
);
```

### COUNT query

```java
Long count = dao.rowCountJoinFilterAnd(
    "left:department",
    new String[]{"=:department.id:5", "=:base.active:true"}
);
```

---

## Common mistakes

| Mistake | Fix |
|---------|-----|
| `"=campo:valor"` missing first `:` | `"=:campo:valor"` |
| `"=:field:null"` for NULL check | `"isnull:field"` |
| Alias in WHERE clause (PostgreSQL) | Use `table.field` not the alias |
| Duplicate results with JOIN + pagination | Add `DISTINCT` or `GROUP BY base.id` |
| LIKE without wildcards | `"like:name:%value%"` not `"like:name:value"` |
