---
title: Home
nav_order: 1
---

# jofrantoba-model-jpa

Generic ORM framework built on **JPA / Hibernate 6** with multi-database support (MySQL, Oracle, PostgreSQL, SQL Server). Provides a reusable DAO layer, a declarative DSL for dynamic queries, stored-procedure execution and native SQL with automatic JSON mapping.

**Requirements:** Java 17+ &nbsp;|&nbsp; Maven 3.8+ &nbsp;|&nbsp; Hibernate 6.5 &nbsp;|&nbsp; Jakarta Persistence 3.2

---

## Features

| Feature | Details |
|---------|---------|
| Generic DAO | `AbstractJpaDaoV2<T>` works for any JPA entity and parameterizes DSL filter values |
| CRUD | save, update, delete, findById — numeric and string PKs |
| Dynamic queries | DSL filters, JOINs, GROUP BY, ORDER BY, pagination |
| Stored procedures | Input/output parameters, JSON-based calling convention |
| Native SQL / HQL | Auto-transforms results to `ArrayNode` JSON |
| Multi-database | One config class per DB engine — no code changes needed |
| Connection pooling | C3P0, SessionFactory cached by key via `PSF` singleton |
| Logging | SLF4J + Log4j2 2.21.1 |
| Zero config | Entity auto-discovery via `@Entity` + reflection |

---

## Maven coordinates

```xml
<dependency>
    <groupId>com.jofrantoba.model.jpa</groupId>
    <artifactId>jofrantoba-model-jpa</artifactId>
    <version>2.0.0</version>
</dependency>
```

Build from source:

```bash
git clone https://github.com/jofrantoba/jofrantoba-model-jpa.git
cd jofrantoba-model-jpa
mvn clean install -DskipTests
```

---

## Quick navigation

| Section | Description |
|---------|-------------|
| [Getting Started](getting-started) | First entity, DAO and database connection in 4 steps |
| [Database Configuration](configuration) | MySQL, PostgreSQL, Oracle, SQL Server, Spring Boot |
| [DSL Guide](dsl-guide) | Complete reference for the declarative query DSL |
| [API Reference](api-reference) | All classes and methods |
| [Examples](examples) | Full working patterns for real-world scenarios |
| [Method Examples](method-examples) | Minimal usage example for each `AbstractJpaDaoV2` public method |

---

## Technology stack

| Component | Version |
|-----------|---------|
| Java (compiler target) | 21 (supports 17+) |
| Hibernate ORM | 6.5.2.Final |
| Jakarta Persistence | 3.2.0 |
| Log4j | 2.21.1 |
| SLF4J | 2.0.13 |
| Lombok | 1.18.32 |
| Jackson | 2.17.1 |
| C3P0 pool | 0.9.5.5 |
| MySQL driver | 8.0.28 |
| PostgreSQL driver | 42.6.0 |
| Oracle JDBC | 11.2.0.4 |
| SQL Server JDBC | 12.8.1 |

## License

MIT — free to use, modify and distribute.
See [LICENSE](https://github.com/jofrantoba/jofrantoba-model-jpa/blob/master/LICENSE).
