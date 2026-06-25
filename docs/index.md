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
| Generic DAO | `AbstractJpaDao<T>` works for any JPA entity and parameterizes DSL filter values |
| CRUD | save, update, delete, findById - numeric and string PKs |
| Dynamic queries | DSL filters, JOINs, GROUP BY, ORDER BY, pagination |
| Stored procedures | Input/output parameters, JSON-based calling convention |
| Native SQL / HQL | Auto-transforms results to `ArrayNode` JSON |
| Multi-database | One config class per DB engine - no code changes needed |
| Connection pooling | Selectable C3P0 (default) or HikariCP; SessionFactory cached by key via `PSF` singleton |
| Logging | SLF4J + Log4j2 2.26.0 |
| Zero config | Entity auto-discovery via `@Entity` + reflection |

---

## Maven coordinates

```xml
<dependency>
    <groupId>com.jofrantoba.model.jpa</groupId>
    <artifactId>jofrantoba-model-jpa</artifactId>
    <version>2.0.4</version>
</dependency>
```

Build from source:

```bash
git clone https://github.com/Jofrantoba-Coding/jofrantoba-model-jpa.git
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
| [Method Examples](method-examples) | Minimal usage example for each `AbstractJpaDao` public method |
| [Repository Examples](repository-examples) | Sanitized examples generated from every Java file in `.examples` |

---

## Technology stack

| Component | Version |
|-----------|---------|
| Java (compiler target) | 21 (supports 17+) |
| Hibernate ORM | 6.5.2.Final |
| Jakarta Persistence | 3.2.0 |
| Log4j | 2.26.0 |
| SLF4J | 2.0.13 |
| Lombok | 1.18.46 |
| Jackson | 2.17.1 |
| Guava | 33.6.0-jre |
| Apache Commons Lang | 3.20.0 |
| C3P0 pool | 0.12.0 |
| HikariCP (via `hibernate-hikaricp`) | 6.5.2.Final |
| MySQL driver | 9.7.0 - MySQL 8.0+ |
| PostgreSQL driver | 42.7.11 - PostgreSQL 8.4+ |
| Oracle JDBC | 11.2.0.4 - Oracle Database 11.2.0.4+ |
| SQL Server JDBC | 12.8.2 - SQL Server 2016+ |

## License

MIT - free to use, modify and distribute.
See [LICENSE](https://github.com/Jofrantoba-Coding/jofrantoba-model-jpa/blob/master/LICENSE).
