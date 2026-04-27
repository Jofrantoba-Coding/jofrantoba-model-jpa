---
title: API Reference
nav_order: 5
---

# API Reference
{: .no_toc }

<details open markdown="block">
  <summary>Contents</summary>
  {: .text-delta }
- TOC
{:toc}
</details>

---

## AbstractJpaDao\<T\>

`com.jofrantoba.model.jpa.daoentity.AbstractJpaDao<T extends Serializable>`

Generic base class for all DAOs. Extend it and call `setClazz(YourEntity.class)` in the constructor.

```java
public class ProductDao extends AbstractJpaDao<Product> {
    public ProductDao() { setClazz(Product.class); }
}
```

### Required setup

| Method | When to call |
|--------|-------------|
| `setClazz(Class<T>)` | Constructor |
| `setSessionFactory(SessionFactory)` | Before first use |

---

## Core CRUD methods

| Method | Returns | Description |
|--------|---------|-------------|
| `save(T entity)` | `void` | Persist new entity |
| `update(T entity)` | `void` | Merge (update) existing entity |
| `delete(T entity)` | `void` | Remove entity object |
| `delete(long id)` | `void` | Remove by numeric PK |
| `delete(String id)` | `void` | Remove by string PK |
| `findById(long id)` | `T` | Find by numeric PK |
| `findById(String id)` | `T` | Find by string PK |
| `allFields()` | `Collection<T>` | Fetch all rows |

---

## DSL filter methods

All methods below accept DSL string arrays. See the [DSL Guide](dsl-guide) for syntax.

### AND filters (HQL-based)

| Method | Description |
|--------|-------------|
| `customFieldsFilterAnd(fields, filters, order)` | SELECT with AND filters |
| `customFieldsFilterAnd(fields, filters, order, page, size)` | Paginated version |
| `findCollectionByFilterAnd(filters)` | Returns full entity objects |
| `findCollectionByFilterAndPaginated(filters, page, size)` | Paginated full objects |

### OR filters (HQL-based)

| Method | Description |
|--------|-------------|
| `customFieldsFilterOr(fields, filters, order)` | SELECT with OR filters |
| `findCollectionByFilterOr(filters)` | Returns full entity objects |

### JOIN methods (HQL-based, single join)

| Method | Description |
|--------|-------------|
| `customFieldsJoinFilterAnd(fields, joinTable, filters, order)` | SELECT + single JOIN |
| `customFieldsJoinFilterAnd(fields, joinTable, filters, order, page, size)` | Paginated |
| `customFieldsJoinFilterAnd(ResultTransformer, fields, joinTable, filters, order)` | Custom transformer |
| `allFieldsJoinFilterAnd(joinTable, filters, order)` | Full entity + single JOIN |
| `rowCountJoinFilterAnd(joinTable, filters)` | COUNT with single JOIN |

### PostgreSQL / relational methods (returns `ArrayNode`)

| Method | Description |
|--------|-------------|
| `allFieldsJoinPostgres(joins, table, fields, filters, order, logicOp)` | Multiple JOINs → JSON |
| `allFieldsJoinPostgresGroupBy(joins, table, fields, filters, order, groupBy)` | GROUP BY → JSON |
| `allFieldsLimitOffsetPostgres(table, fields, filters, order, limit, offset)` | Limit/offset pagination → JSON |
| `allFieldsJoinPostgresCount(joins, table, fields, filters)` | COUNT → JSON |

---

## Native SQL / HQL

| Method | Description |
|--------|-------------|
| `iudNativeQuery(sql)` | Execute INSERT/UPDATE/DELETE native SQL |
| `saveNativeQuery(sql)` | Execute native INSERT returning generated key |
| `findCollectionByNativeQuery(sql)` | SELECT native SQL → `List<T>` |
| `findCollectionByHql(hql)` | Execute arbitrary HQL |

---

## Stored procedures

| Method | Description |
|--------|-------------|
| `iudProcedureJson(name, json)` | Call SP; JSON string maps to IN params |
| `listProcedureMsql(name, params)` | Call SP returning result set (SQL Server) |

**`iudProcedureJson` example:**

```java
String json = """
    {
        "p_user_id": 42,
        "p_status": "ACTIVE",
        "p_updated_by": "admin"
    }
    """;

Long affected = dao.iudProcedureJson("SP_UPDATE_USER_STATUS", json);
```

The framework extracts the JSON keys as ordered stored-procedure parameters.

---

## Session / Transaction access

| Method | Returns | Description |
|--------|---------|-------------|
| `getSession()` | `Session` | Current Hibernate session |
| `getSessionFactory()` | `SessionFactory` | Configured factory |
| `setSessionFactory(sf)` | `void` | Inject session factory |

---

## InterCrud\<T\>

`com.jofrantoba.model.jpa.daoentity.InterCrud<T extends Serializable>`

Interface that declares all ~135 methods. Extend it in your own DAO interfaces to add custom method signatures while retaining the built-in contract:

```java
public interface IOrderDao extends InterCrud<Order> {
    Collection<Order> findByCustomer(Long customerId) throws Exception;
    Long countOpenOrders() throws Exception;
}
```

---

## PSF

`com.jofrantoba.model.jpa.psf.PSF`

Singleton SessionFactory cache. Thread-safe.

| Method | Description |
|--------|-------------|
| `PSF.getInstance()` | Get singleton |
| `buildPSF(key, props, packages)` | Build + cache a SessionFactory |
| `getPSF(key)` | Retrieve cached SessionFactory |
| `destroyPSF(key)` | Close and remove from cache |

---

## ConnectionProperties implementations

All extend `AbstractConnectionProperties` and implement `ConnectionProperties`.

| Class | Database | Default port |
|-------|----------|-------------|
| `ConnectionPropertiesMysql` | MySQL 5.7+ | 3306 |
| `ConnectionPropertiesPostgre` | PostgreSQL 10+ | 5432 |
| `ConnectionPropertiesOracle` | Oracle 11g+ | 1521 |
| `ConnectionPropertiesSqlServer` | SQL Server 2016+ | 1433 |

Constructor signature (all four):
```java
new ConnectionPropertiesXxx(String host, int port, String database,
                             String username, String password)
```

---

## ParameterProcedure

`com.jofrantoba.model.jpa.daoentity.ParameterProcedure`

Represents a single parameter for a stored-procedure call.

| Property | Description |
|----------|-------------|
| `name` | Parameter name |
| `value` | Parameter value |
| `mode` | `IN`, `OUT`, or `INOUT` |
| `sqlType` | `java.sql.Types` constant |

---

## Shared utilities

`com.jofrantoba.model.jpa.shared.Shared`

| Method | Description |
|--------|-------------|
| `strApostrofe(String)` | Wraps a string in single quotes |
| `getClean(String)` | Removes potentially dangerous characters |
| `convertValueLong(Object)` | Safe cast to `Long` |
| `getUnixTime()` | Current epoch millis as `Long` |
| `closeResultSet(ResultSet)` | Null-safe close |
| `closeCallableStatement(CallableStatement)` | Null-safe close |
