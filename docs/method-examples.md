---
title: Method Examples
nav_order: 7
---

# Method Examples
{: .no_toc }

This page shows one minimal usage example for each public method exposed by `AbstractJpaDaoV2`.
The snippets use neutral schemas such as `jofrantoba.sales` and `jofrantoba.catalog`; replace them with constants from your own application.

<details open markdown="block">
  <summary>Contents</summary>
  {: .text-delta }
- TOC
{:toc}
</details>

---

## Base Classes Used By The Examples

```java
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

public interface IProductDao extends InterCrud<Product> {
}

public class ProductDao extends AbstractJpaDaoV2<Product> implements IProductDao {
    public ProductDao(SessionFactory sessionFactory) {
        setClazz(Product.class);
        setSessionFactory(sessionFactory);
    }
}

public class ProductSummaryDto {
    private Long id;
    private String name;
    private String categoryName;
    // getters and setters
}
```

The examples below assume:

```java
ProductDao dao = new ProductDao(sessionFactory);
String[] activeFilter = {"=:base.active:true"};
String[] nameOrder = {"base.name:asc"};
```

---

## DSL Language Quick Reference

### Entity `T` and `base`

`ProductDao extends AbstractJpaDaoV2<Product>` means `Product` is the root entity `T`.
For HQL/entity methods, `base` is the alias of that entity:

```java
Collection<Product> rows = dao.allFieldsFilterAnd(
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

Here `active` and `name` are Java properties of `Product`.

For native SQL methods, define `base` explicitly:

```java
String table = "jofrantoba.catalog.products as base";
```

Then `base.id`, `base.name`, and `base.active` are SQL columns or table aliases from the native query.

### Filters

```java
String[] filters = {
    "=:base.active:true",
    "equal:base.status:PUBLISHED",
    "like:base.name:%phone%",
    "between:base.price:100:500",
    "in:base.categoryId:10:11:12",
    "isnotnull:base.createdAt"
};
```

`=` / `!=` / `>` / `<` / `>=` / `<=` are for numeric, boolean, and values that can be parsed safely.
Use `equal` and `notequal` for text. Use `isnull` and `isnotnull` for null checks.

### HQL joins

Use HQL joins with entity relationships:

```java
String joinTable = "left:category";
String joinFetch = "inner:customer:fetch";
String[] joinTables = {"inner:category", "left:brand"};
```

### Native joins

Use native joins with SQL table references:

```java
String[] joinTables = {
    "inner:jofrantoba.catalog.categories as category:on:base.category_id:category.id",
    "left:jofrantoba.catalog.brands as brand:on:base.brand_id:brand.id",
    "left:(select * from jofrantoba.catalog.stock where active=true) as stock:on:base.id:stock.product_id"
};
```

Multiple join conditions use `:and:` or `:or:`:

```java
String[] joinTables = {
    "left:jofrantoba.catalog.stock as stock:on:base.id:stock.product_id:and:stock.active:true"
};
```

---

## Setup And Session Methods

### setSessionFactory

```java
ProductDao dao = new ProductDao(sessionFactory);
dao.setSessionFactory(sessionFactory);
```

### getSession

```java
Session session = dao.getSession();
```

### getNewSession

```java
try (Session session = dao.getNewSession()) {
    Product product = session.get(Product.class, 1L);
}
```

### flush

```java
dao.save(product);
dao.flush();
```

### detach

```java
Product product = dao.findById(1L);
dao.detach(product);
```

### clear

```java
dao.clear();
```

### evict

```java
Product product = dao.findById(1L);
dao.evict(product);
```

---

## CRUD Methods

### save

```java
Product product = new Product();
product.setName("Phone");
product.setActive(true);
dao.save(product);
```

### update

```java
Product product = dao.findById(1L);
product.setName("Phone Pro");
dao.update(product);
```

### findById(long)

```java
Product product = dao.findById(1L);
```

### findById(String)

```java
Product product = dao.findById("SKU-001");
```

### delete(T entity)

```java
Product product = dao.findById(1L);
dao.delete(product);
```

### delete(long)

```java
dao.delete(1L);
```

### deleteFilterAnd

```java
int deleted = dao.deleteFilterAnd(new String[]{
    "=:base.active:false",
    "<:base.stock:1"
});
```

---

## Entity Query Methods

### allFields()

```java
Collection<Product> products = dao.allFields();
```

### allFields(HashMap<String, String>)

```java
HashMap<String, String> order = new HashMap<>();
order.put("name", "asc");
Collection<Product> products = dao.allFields(order);
```

### allFields(String[])

```java
Collection<Product> products = dao.allFields(new String[]{"base.name:asc"});
```

### allFields(String, String[])

```java
Collection<Product> products = dao.allFields(
    "=:active:true",
    new String[]{"name:asc"}
);
```

### allFields(String, String[], int, int)

```java
Collection<Product> page = dao.allFields(
    "=:active:true",
    new String[]{"name:asc"},
    1,
    20
);
```

### allFieldsFilterAnd

```java
Collection<Product> products = dao.allFieldsFilterAnd(
    new String[]{"=:base.active:true", ">:base.stock:0"},
    new String[]{"base.name:asc"}
);
```

### allFieldsFilterOr

```java
Collection<Product> products = dao.allFieldsFilterOr(
    new String[]{"equal:base.status:DRAFT", "equal:base.status:PUBLISHED"},
    new String[]{"base.name:asc"}
);
```

---

## Custom Field HQL Methods

### customFields(String)

```java
Collection<Product> rows = dao.customFields(
    "base.id as id, base.name as name"
);
```

### customFields(ResultTransformer, String)

```java
ResultTransformer rt = Transformers.aliasToBean(ProductSummaryDto.class);
Collection<Product> rows = dao.customFields(
    rt,
    "base.id as id, base.name as name"
);
```

### customFieldsFilterAnd(String, String[], String[])

```java
Collection<Product> rows = dao.customFieldsFilterAnd(
    "base.id as id, base.name as name",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

### customFieldsFilterAnd(Class, String, String[], String[])

```java
Collection<?> rows = dao.customFieldsFilterAnd(
    ProductSummaryDto.class,
    "base.id as id, base.name as name",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

### customFieldsFilterOr

```java
Collection<Product> rows = dao.customFieldsFilterOr(
    "base.id as id, base.name as name",
    new String[]{"equal:base.status:DRAFT", "equal:base.status:PUBLISHED"},
    new String[]{"base.name:asc"}
);
```

---

## HQL Join Methods

### allFieldsJoinFilter(String, String, String[])

```java
Collection<Product> products = dao.allFieldsJoinFilter(
    "left:category",
    "=:category.id:10",
    new String[]{"base.name:asc"}
);
```

### allFieldsJoinFilter(String, String, String[], int, int)

```java
Collection<Product> page = dao.allFieldsJoinFilter(
    "left:category",
    "=:category.id:10",
    new String[]{"base.name:asc"},
    1,
    20
);
```

### allFieldsJoinFilterAnd(String, String[], String[])

```java
Collection<Product> products = dao.allFieldsJoinFilterAnd(
    "left:category",
    new String[]{"=:category.id:10", "=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

### allFieldsJoinFilterAnd(String[], String[], String[])

```java
Collection<Product> products = dao.allFieldsJoinFilterAnd(
    new String[]{"inner:category", "left:brand"},
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

### allFieldsJoinFilterAnd(String, String[], String[], int, int)

```java
Collection<Product> page = dao.allFieldsJoinFilterAnd(
    "left:category",
    new String[]{"=:category.id:10", "=:base.active:true"},
    new String[]{"base.name:asc"},
    1,
    20
);
```

### customFieldsJoinFilterAnd(String, String, String[], String[])

```java
Collection<Product> rows = dao.customFieldsJoinFilterAnd(
    "base.id as id, base.name as name, category.name as categoryName",
    "left:category",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

### customFieldsJoinFilterAnd(String, String, String[], String[], int, int)

```java
Collection<Product> page = dao.customFieldsJoinFilterAnd(
    "base.id as id, base.name as name, category.name as categoryName",
    "left:category",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"},
    1,
    20
);
```

### customFieldsJoinFilterAnd(String, String[], String[], String[])

```java
Collection<Product> rows = dao.customFieldsJoinFilterAnd(
    "base.id as id, base.name as name, category.name as categoryName, brand.name as brandName",
    new String[]{"left:category", "left:brand"},
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

### customFieldsJoinFilterAnd(String, String[], String[], String[], int, int)

```java
Collection<Product> page = dao.customFieldsJoinFilterAnd(
    "base.id as id, base.name as name, category.name as categoryName",
    new String[]{"left:category"},
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"},
    1,
    20
);
```

### customFieldsJoinFilterAnd(Class, String, String, String[], String[])

```java
Collection<?> rows = dao.customFieldsJoinFilterAnd(
    ProductSummaryDto.class,
    "base.id as id, base.name as name, category.name as categoryName",
    "left:category",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

### customFieldsJoinFilterAnd(ResultTransformer, String, String, String[], String[])

```java
ResultTransformer rt = Transformers.aliasToBean(ProductSummaryDto.class);
Collection<?> rows = dao.customFieldsJoinFilterAnd(
    rt,
    "base.id as id, base.name as name, category.name as categoryName",
    "left:category",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

### customFieldsJoinFilterAnd(ResultTransformer, String, String[], String[], String[])

```java
ResultTransformer rt = Transformers.aliasToBean(ProductSummaryDto.class);
Collection<?> rows = dao.customFieldsJoinFilterAnd(
    rt,
    "base.id as id, base.name as name, category.name as categoryName, brand.name as brandName",
    new String[]{"left:category", "left:brand"},
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

### rowCountJoinFilterAnd

```java
Long count = dao.rowCountJoinFilterAnd(
    "left:category",
    new String[]{"=:category.id:10", "=:base.active:true"}
);
```

### rowCountJoinsFilterAnd

```java
Long count = dao.rowCountJoinsFilterAnd(
    new String[]{"left:category", "left:brand"},
    new String[]{"=:base.active:true"}
);
```

### maxValueJoinFilterAnd

```java
Object maxPrice = dao.maxValueJoinFilterAnd(
    "base.price",
    "left:category",
    new String[]{"=:category.id:10"}
);
```

### aggregateJoinFilterAndGroupBy

```java
Long count = dao.aggregateJoinFilterAndGroupBy(
    "count(base.id)",
    "left:category",
    new String[]{"=:category.id:10"},
    null
);
```

---

## Native Relational Methods

### allFieldsPostgres

```java
ArrayNode rows = dao.allFieldsPostgres(
    "jofrantoba.catalog.products as base",
    "base.id, base.name, base.price",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

### allFieldsLimitOffsetPostgres

```java
ArrayNode page = dao.allFieldsLimitOffsetPostgres(
    "jofrantoba.catalog.products as base",
    "base.id, base.name, base.price",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"},
    20L,
    0L
);
```

### allFieldsJoinPostgres

```java
ArrayNode rows = dao.allFieldsJoinPostgres(
    new String[]{
        "inner:jofrantoba.catalog.categories as category:on:base.category_id:category.id"
    },
    "jofrantoba.catalog.products as base",
    "base.id, base.name, category.name as categoryName",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"},
    "and"
);
```

### allFieldsJoinLimitOffsetPostgres

```java
ArrayNode page = dao.allFieldsJoinLimitOffsetPostgres(
    new String[]{
        "inner:jofrantoba.catalog.categories as category:on:base.category_id:category.id"
    },
    "jofrantoba.catalog.products as base",
    "base.id, base.name, category.name as categoryName",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"},
    20L,
    0L,
    "and"
);
```

### allFieldsJoinPostgresGroupBy

```java
ArrayNode stats = dao.allFieldsJoinPostgresGroupBy(
    new String[]{
        "inner:jofrantoba.catalog.categories as category:on:base.category_id:category.id"
    },
    "jofrantoba.catalog.products as base",
    "category.name as categoryName, count(base.id) as total",
    new String[]{"=:base.active:true"},
    new String[]{"category.name:asc"},
    "category.name"
);
```

### allFieldsJoinPostgresGroupBySubQuery

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

### allFieldsLimitJoinPostgresGroupBySubQuery

```java
ArrayNode page = dao.allFieldsLimitJoinPostgresGroupBySubQuery(
    "subquery.categoryName, sum(subquery.total) as total",
    "subquery.categoryName",
    new String[]{"subquery.categoryName:asc"},
    new String[]{
        "inner:jofrantoba.catalog.categories as category:on:base.category_id:category.id"
    },
    "jofrantoba.catalog.products as base",
    "category.name as categoryName, count(base.id) as total",
    new String[]{"=:base.active:true"},
    "category.name",
    20L,
    0L
);
```

---

## SQL String Builder Methods

These methods return SQL text only. They are useful for debugging or composing a trusted internal query.

### strAllFieldsJoinPostgres

```java
StringBuilder sql = dao.strAllFieldsJoinPostgres(
    new String[]{"inner:jofrantoba.catalog.categories as category:on:base.category_id:category.id"},
    "jofrantoba.catalog.products as base",
    "base.id, base.name, category.name as categoryName",
    new String[]{"=:base.active:true"},
    new String[]{"base.name:asc"}
);
```

### strAllFieldsJoinPostgresGroupBy

```java
StringBuilder sql = dao.strAllFieldsJoinPostgresGroupBy(
    new String[]{"inner:jofrantoba.catalog.categories as category:on:base.category_id:category.id"},
    "jofrantoba.catalog.products as base",
    "category.name, count(base.id) as total",
    new String[]{"=:base.active:true"},
    new String[]{"category.name:asc"},
    "category.name"
);
```

### strAllFieldsJoinGroupByLimitOffsetPostgres

```java
StringBuilder sql = dao.strAllFieldsJoinGroupByLimitOffsetPostgres(
    new String[]{"inner:jofrantoba.catalog.categories as category:on:base.category_id:category.id"},
    "jofrantoba.catalog.products as base",
    "category.name, count(base.id) as total",
    new String[]{"=:base.active:true"},
    new String[]{"category.name:asc"},
    "category.name",
    20L,
    0L
);
```

---

## Native SQL, Export, And Stored Procedures

### saveNativeQuery

```java
int inserted = dao.saveNativeQuery(
    "jofrantoba.catalog.products",
    new String[]{
        "name:String:Phone",
        "price:Double:799.90",
        "stock:Integer:20"
    }
);
```

### iudNativeQuery(String)

Use only with trusted internal SQL:

```java
int updated = dao.iudNativeQuery(
    "update jofrantoba.catalog.products set active=false where discontinued=true"
);
```

### iudNativeQuery(String, String[])

```java
int updated = dao.iudNativeQuery(
    "update jofrantoba.catalog.products set price = :price where id = :id",
    new String[]{"price:Double:749.90", "id:Long:1"}
);
```

### sqlExportTOExcel

```java
Map<Integer, Object[]> data = dao.sqlExportTOExcel(
    "select id, name, price from jofrantoba.catalog.products where active=true"
);
```

### iudProcedureJson

```java
Long affected = dao.iudProcedureJson(
    "jofrantoba_update_product",
    "{\"id\":1,\"name\":\"Phone Pro\",\"active\":true}"
);
```

### listProcedureMsql

```java
Map<String, Object> params = new HashMap<>();
params.put("categoryId", 10L);
List<Product> products = dao.listProcedureMsql(
    "exec jofrantoba_list_products_by_category :categoryId",
    params
);
```
