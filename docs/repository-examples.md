---
title: Repository Examples
nav_order: 8
---

# Repository Examples
{: .no_toc }

This guide explains the complex repository patterns used by the `.examples` projects, sanitized to neutral `jofrantoba.*` schemas and `com.jofrantoba.examples.*` packages.

It does not copy every file line by line. Instead, it teaches how the examples are structured and how repository classes use `AbstractJpaDaoV2` through the DSL for joins, subqueries, grouping, pagination, DTO projections, counts, and trusted SQL builders.

<details open markdown="block">
  <summary>Contents</summary>
  {: .text-delta }
- TOC
{:toc}
</details>

---

## The Pattern

Every complex example follows the same structure:

1. A DAO interface extends `InterCrud<T>` and declares domain-specific methods.
2. A repository class extends `AbstractJpaDaoV2<T>`.
3. The constructor calls `setClazz(Entity.class)`.
4. Public repository methods build DSL arrays: `fields`, `table`, `joinTables`, `mapFilterField`, `mapOrder`, `groupBy`.
5. The repository delegates execution to inherited `AbstractJpaDaoV2` methods.

```java
package com.jofrantoba.examples.catalog.dao.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.examples.catalog.dao.inter.InterDaoProduct;
import com.jofrantoba.examples.catalog.entity.Product;
import com.jofrantoba.examples.catalog.filter.FilterProduct;
import java.util.Collection;
import java.util.HashMap;

public class DaoProduct extends AbstractJpaDaoV2<Product> implements InterDaoProduct {

    public DaoProduct() {
        setClazz(Product.class);
    }

    @Override
    public ArrayNode listNative(FilterProduct filter) throws Exception {
        HashMap<String, Object> map = filterNativeMap(filter);
        String table = (String) map.get("table");
        String fields = (String) map.get("fields");
        String[] joinTables = (String[]) map.get("joinTables");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String[] mapOrder = (String[]) map.get("mapOrder");

        return allFieldsJoinPostgres(
            joinTables, table, fields, mapFilterField, mapOrder, "and"
        );
    }
}
```

The interface keeps the application contract separate from the reusable DAO implementation:

```java
package com.jofrantoba.examples.catalog.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.catalog.entity.Product;
import com.jofrantoba.examples.catalog.filter.FilterProduct;
import java.util.Collection;

public interface InterDaoProduct extends InterCrud<Product> {
    ArrayNode listNative(FilterProduct filter) throws Exception;
    ArrayNode listNativePaged(FilterProduct filter, Long limit, Long offset) throws Exception;
    ArrayNode listGrouped(FilterProduct filter) throws Exception;
    Collection<Product> listByEntityDsl(Long categoryId) throws Exception;
}
```

---

## DSL Parts

### Filter DSL

`mapFilterField` values are parsed by `AbstractJpaDaoV2` and bound as query parameters.

```java
String[] mapFilterField = {
    "=:base.is_persistente:true",
    "=:category.id:10",
    "like:base.name:%phone%",
    "between:base.price:100:500",
    "isnotnull:base.created_at"
};
```

Common operators:

| Operator | Example | Meaning |
|----------|---------|---------|
| `=` | `"=:base.active:true"` | equality for parsed values |
| `equal` | `"equal:base.status:PUBLISHED"` | text equality |
| `like` | `"like:base.name:%phone%"` | SQL/HQL LIKE |
| `between` | `"between:base.price:100:500"` | range |
| `in` | `"in:base.id:1:2:3"` | collection |
| `isnull` | `"isnull:base.deleted_at"` | null check |
| `isnotnull` | `"isnotnull:base.created_at"` | not-null check |

### HQL Join DSL

Use HQL joins for entity relationships.

```java
String joinTable = "left:category";
String[] joinTables = {"inner:category", "left:brand"};
```

This maps to entity paths such as `base.category category`.

### Native Join DSL

Use native joins for table-based methods that return `ArrayNode`.

```java
String[] joinTables = {
    "inner:jofrantoba.catalog.tm_category as category:on:base.id_category:category.id",
    "left:jofrantoba.catalog.tm_brand as brand:on:base.id_brand:brand.id"
};
```

Native joins support multiple conditions:

```java
String[] joinTables = {
    "left:jofrantoba.catalog.tgv_stock as stock:on:stock.id_product:base.id:and:stock.active:true"
};
```

Native joins also support trusted internal subqueries:

```java
String[] joinTables = {
    "left:(select * from jofrantoba.catalog.tm_parameter where parent_id=1 and active=true) as type:on:base.id_type:type.id"
};
```

Only `mapFilterField` values are bound as parameters. `table`, `fields`, `groupBy`, and subquery bodies must come from constants or server-side code.

---

## Example 1: Native List With Complex Joins

This is the most common pattern in the examples: build a map, then call `allFieldsJoinPostgres`.

### Interface

```java
public interface InterDaoActivity extends InterCrud<Activity> {
    ArrayNode list(FilterActivity filter) throws Exception;
}
```

### Repository

```java
public class DaoActivity extends AbstractJpaDaoV2<Activity> implements InterDaoActivity {

    public DaoActivity() {
        setClazz(Activity.class);
    }

    @Override
    public ArrayNode list(FilterActivity filter) throws Exception {
        HashMap<String, Object> map = nativeMap(filter);
        return allFieldsJoinPostgres(
            (String[]) map.get("joinTables"),
            (String) map.get("table"),
            (String) map.get("fields"),
            (String[]) map.get("mapFilterField"),
            (String[]) map.get("mapOrder"),
            "and"
        );
    }

    private HashMap<String, Object> nativeMap(FilterActivity filter) {
        HashMap<String, Object> map = new HashMap<>();

        String table = "jofrantoba.catalog.tgv_activity as base";
        String fields =
            "base.id, " +
            "base.code, " +
            "base.name, " +
            "unit.code as unitCode, " +
            "docType.description as documentType, " +
            "condition.description as condition";

        String[] joinTables = {
            "inner:jofrantoba.catalog.tgv_unit as unit:on:base.id_unit:unit.id",
            "left:jofrantoba.catalog.tm_parameter as docType:on:base.id_doc_type:docType.id",
            "left:jofrantoba.catalog.tm_parameter as condition:on:base.id_condition:condition.id"
        };

        String[] filters = {
            "=:base.is_persistente:true",
            filter.getUnitId() == null ? "isnotnull:base.id" : "=:unit.id:" + filter.getUnitId()
        };

        map.put("table", table);
        map.put("fields", fields);
        map.put("joinTables", joinTables);
        map.put("mapFilterField", filters);
        map.put("mapOrder", new String[]{"base.id:asc"});
        return map;
    }
}
```

### Usage

```java
FilterActivity filter = new FilterActivity();
filter.setUnitId(25L);

ArrayNode rows = daoActivity.list(filter);
```

---

## Example 2: Native Pagination With Joins

Pagination uses the same DSL pieces, but delegates to `allFieldsJoinLimitOffsetPostgres`.

```java
public ArrayNode listPaged(FilterActivity filter, Long limit, Long offset) throws Exception {
    HashMap<String, Object> map = nativeMap(filter);

    return allFieldsJoinLimitOffsetPostgres(
        (String[]) map.get("joinTables"),
        (String) map.get("table"),
        (String) map.get("fields"),
        (String[]) map.get("mapFilterField"),
        (String[]) map.get("mapOrder"),
        limit,
        offset,
        "and"
    );
}
```

Usage:

```java
ArrayNode page = daoActivity.listPaged(filter, 20L, 40L);
```

---

## Example 3: Subquery Joins

The examples use subqueries to pre-filter parameter tables or security tables before joining them.

```java
String[] joinTables = {
    "inner:jofrantoba.security.tg_system as system:on:base.id_system:system.id",
    "left:jofrantoba.security.tg_user_role as userRole:on:base.id:userRole.id_user:and:userRole.active:true",
    "left:(select * from jofrantoba.security.tg_role where internal=true and active=true) as role:on:userRole.id_role:role.id",
    "left:jofrantoba.security.tg_user_profile as userProfile:on:base.id:userProfile.id_user:and:userProfile.active:true",
    "left:(select * from jofrantoba.security.tg_profile where internal=true and active=true) as profile:on:userProfile.id_profile:profile.id"
};
```

The query method does not change:

```java
ArrayNode users = allFieldsJoinPostgres(
    joinTables,
    "jofrantoba.security.tg_user as base",
    "base.id, base.username, role.name as roleName, profile.name as profileName",
    new String[]{"=:base.active:true"},
    new String[]{"base.username:asc"},
    "and"
);
```

---

## Example 4: Multiple Join Conditions

Some examples need two or more conditions in the same join. Use `:and:` or `:or:` tokens.

```java
String[] joinTables = {
    "inner:jofrantoba.auth.user_entity as userEntity:on:userEntity.realm_id:system.realm_id:and:userEntity.realm_id:base.realm_id:and:userEntity.id:base.user_entity_id"
};
```

That produces the equivalent of:

```sql
inner join jofrantoba.auth.user_entity as userEntity
  on userEntity.realm_id = system.realm_id
 and userEntity.realm_id = base.realm_id
 and userEntity.id = base.user_entity_id
```

---

## Example 5: Group By With Subquery

The most complex examples build a subquery first, group it, and then group again in the outer query.
This pattern delegates to `allFieldsJoinPostgresGroupBySubQuery`.

### Repository Method

```java
public ArrayNode listGroupedUnits(FilterUnit filter) throws Exception {
    HashMap<String, Object> map = unitGroupedMap(filter);

    return allFieldsJoinPostgresGroupBySubQuery(
        (String) map.get("fields"),
        (String) map.get("groupBy"),
        (String[]) map.get("mapOrder"),
        (String[]) map.get("joinTables"),
        (String) map.get("table"),
        (String) map.get("fieldsSq"),
        (String[]) map.get("mapFilterField"),
        (String) map.get("groupBySq")
    );
}
```

### DSL Map Builder

```java
private HashMap<String, Object> unitGroupedMap(FilterUnit filter) {
    HashMap<String, Object> map = new HashMap<>();

    String table = "jofrantoba.catalog.tgv_unit as base";

    String fieldsSq =
        "base.id, " +
        "base.code as unitCode, " +
        "lot.code as lotCode, " +
        "district.description as districtName, " +
        "string_agg(owner.full_name, ', ') as owners";

    String[] joinTables = {
        "inner:jofrantoba.catalog.tgv_lot as lot:on:base.id_lot:lot.id",
        "inner:jofrantoba.catalog.tm_district as district:on:lot.id_district:district.id",
        "left:jofrantoba.catalog.tgv_ownership as ownership:on:base.id:ownership.id_unit",
        "left:jofrantoba.catalog.tg_owner as owner:on:ownership.id_owner:owner.id"
    };

    String groupBySq =
        "base.id, " +
        "base.code, " +
        "lot.code, " +
        "district.description";

    String fields =
        "id, " +
        "unitCode, " +
        "lotCode, " +
        "districtName, " +
        "owners";

    String groupBy =
        "id, unitCode, lotCode, districtName, owners";

    map.put("table", table);
    map.put("fieldsSq", fieldsSq);
    map.put("joinTables", joinTables);
    map.put("mapFilterField", new String[]{"=:base.is_persistente:true"});
    map.put("groupBySq", groupBySq);
    map.put("fields", fields);
    map.put("groupBy", groupBy);
    map.put("mapOrder", new String[]{"unitCode:asc"});
    return map;
}
```

### Usage

```java
ArrayNode rows = daoUnit.listGroupedUnits(filter);
```

---

## Example 6: Paged Group By With Subquery

Use `allFieldsLimitJoinPostgresGroupBySubQuery` when the grouped result needs pagination.

```java
public ArrayNode listGroupedUnitsPaged(FilterUnit filter, Long limit, Long offset) throws Exception {
    HashMap<String, Object> map = unitGroupedMap(filter);

    return allFieldsLimitJoinPostgresGroupBySubQuery(
        (String) map.get("fields"),
        (String) map.get("groupBy"),
        (String[]) map.get("mapOrder"),
        (String[]) map.get("joinTables"),
        (String) map.get("table"),
        (String) map.get("fieldsSq"),
        (String[]) map.get("mapFilterField"),
        (String) map.get("groupBySq"),
        limit,
        offset
    );
}
```

---

## Example 7: SQL String Builder For Trusted Internal Queries

Some examples build a SQL string first using inherited `str*` methods, then pass it to another internal component.
This is useful for reports, exports, or compatibility with existing SQL execution paths.

```java
public StringBuilder buildUserSql(FilterUser filter, Long limit, Long offset) {
    HashMap<String, Object> map = userMap(filter);

    return strAllFieldsJoinGroupByLimitOffsetPostgres(
        (String[]) map.get("joinTables"),
        (String) map.get("table"),
        (String) map.get("fields"),
        (String[]) map.get("mapFilterField"),
        (String[]) map.get("mapOrder"),
        (String) map.get("groupBy"),
        limit,
        offset
    );
}
```

The returned SQL is text. Treat it as trusted internal SQL, not as an endpoint for raw user input.

---

## Example 8: HQL Entity Joins

Not every example uses native SQL. When relationships are mapped in JPA, use HQL join methods.

```java
public Collection<Product> listByCategory(Long categoryId) throws Exception {
    return customFieldsJoinFilterAnd(
        "base.id as id, base.name as name, category.name as categoryName",
        "inner:category",
        new String[]{
            "=:category.id:" + categoryId,
            "=:base.active:true"
        },
        new String[]{"base.name:asc"}
    );
}
```

For multiple entity joins:

```java
public Collection<Product> listWithBrandAndCategory() throws Exception {
    return customFieldsJoinFilterAnd(
        "base.id as id, base.name as name, category.name as categoryName, brand.name as brandName",
        new String[]{"inner:category", "left:brand"},
        new String[]{"=:base.active:true"},
        new String[]{"base.name:asc"}
    );
}
```

---

## Example 9: Counts And Aggregates

### Count With One Entity Join

```java
public Long countByCategory(Long categoryId) throws Exception {
    return rowCountJoinFilterAnd(
        "inner:category",
        new String[]{
            "=:category.id:" + categoryId,
            "=:base.active:true"
        }
    );
}
```

### Count With Multiple Entity Joins

```java
public Long countByBrandAndCategory(Long brandId, Long categoryId) throws Exception {
    return rowCountJoinsFilterAnd(
        new String[]{"inner:brand", "inner:category"},
        new String[]{
            "=:brand.id:" + brandId,
            "=:category.id:" + categoryId,
            "=:base.active:true"
        }
    );
}
```

### Aggregate

```java
public Long countGroupedByCategory(Long categoryId) throws Exception {
    return aggregateJoinFilterAndGroupBy(
        "count(base.id)",
        "inner:category",
        new String[]{"=:category.id:" + categoryId},
        null
    );
}
```

---

## Example 10: Raw SQL Export

The examples also include report-style SQL builders. Use this only for trusted internal SQL.

```java
public Map<Integer, Object[]> exportActiveProducts() throws Exception {
    return sqlExportTOExcel(
        "select base.id, base.name, base.price " +
        "from jofrantoba.catalog.tm_product base " +
        "where base.active = true " +
        "order by base.name asc"
    );
}
```

---

## How To Choose The Method

| Need | Use |
|------|-----|
| Entity results with simple filters | `allFieldsFilterAnd`, `allFieldsFilterOr` |
| DTO-like HQL projection | `customFieldsFilterAnd`, `customFieldsJoinFilterAnd` |
| Entity relationship joins | HQL join methods with `"inner:relation"` |
| Native table joins returning JSON | `allFieldsJoinPostgres` |
| Native joins with pagination | `allFieldsJoinLimitOffsetPostgres` |
| Native aggregation | `allFieldsJoinPostgresGroupBy` |
| Aggregation over a subquery | `allFieldsJoinPostgresGroupBySubQuery` |
| Paged aggregation over a subquery | `allFieldsLimitJoinPostgresGroupBySubQuery` |
| Count with entity join | `rowCountJoinFilterAnd`, `rowCountJoinsFilterAnd` |
| Export/report SQL | `sqlExportTOExcel` with trusted SQL |

---

## Security Rules Preserved From The Examples

- Values in `mapFilterField` are parameterized by `AbstractJpaDaoV2`.
- `limit` and `offset` are bound as JDBC parameters.
- Native join types and join identifiers are validated.
- `table`, `fields`, `groupBy`, and subquery bodies are SQL structure; keep them in repository code or trusted server configuration.
- Do not pass raw request parameters into `table`, `fields`, `groupBy`, or raw SQL methods.
