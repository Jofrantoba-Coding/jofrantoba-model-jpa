---
title: Database Configuration
nav_order: 3
---

# Database Configuration
{: .no_toc }

<details open markdown="block">
  <summary>Contents</summary>
  {: .text-delta }
- TOC
{:toc}
</details>

---

## PSF — SessionFactory cache

`PSF` is a singleton that creates and caches Hibernate `SessionFactory` instances by an arbitrary string key.
A single application can hold multiple open factories (one per database).

```java
// Build and cache
SessionFactory sf = PSF.getInstance().buildPSF(key, connectionProperties, packages);

// Retrieve a cached factory
SessionFactory sf = PSF.getInstance().getPSF(key);

// Close and remove from cache
PSF.getInstance().destroyPSF(key);
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `key` | `String` | Any unique identifier — e.g. `"postgres_main"` |
| `connectionProperties` | `ConnectionProperties` | Database-specific config object |
| `packages` | `List<String>` | Java packages to scan for `@Entity` classes |

---

## MySQL

```java
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesMysql;

SessionFactory sf = PSF.getInstance().buildPSF(
    "mysql_main",
    new ConnectionPropertiesMysql(
        "localhost",    // host
        3306,           // port
        "myapp_db",     // database name
        "root",         // username
        "password"      // password
    ),
    List.of("com.example.myapp.entity")
);
```

Driver version included: `mysql-connector-java:8.0.28`  
Character set: `utf8mb4`

---

## PostgreSQL

```java
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;

SessionFactory sf = PSF.getInstance().buildPSF(
    "postgres_main",
    new ConnectionPropertiesPostgre(
        "localhost",
        5432,
        "myapp_db",
        "postgres",
        "password"
    ),
    List.of("com.example.myapp.entity")
);
```

Driver version included: `postgresql:42.6.0`

---

## Oracle

```java
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesOracle;

SessionFactory sf = PSF.getInstance().buildPSF(
    "oracle_main",
    new ConnectionPropertiesOracle(
        "localhost",
        1521,
        "ORCL",       // SID / service name
        "appuser",
        "password"
    ),
    List.of("com.example.myapp.entity")
);
```

Driver version included: `ojdbc6:11.2.0.4`

---

## SQL Server

```java
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesSqlServer;

SessionFactory sf = PSF.getInstance().buildPSF(
    "sqlserver_main",
    new ConnectionPropertiesSqlServer(
        "localhost",
        1433,
        "MyAppDB",
        "sa",
        "password"
    ),
    List.of("com.example.myapp.entity")
);
```

Driver version included: `mssql-jdbc:12.8.1.jre11`

---

## Multiple databases

```java
// Connect to two databases simultaneously
PSF.getInstance().buildPSF("pg_orders",
    new ConnectionPropertiesPostgre("host1", 5432, "orders_db", "u", "p"),
    List.of("com.example.orders.entity"));

PSF.getInstance().buildPSF("mysql_catalog",
    new ConnectionPropertiesMysql("host2", 3306, "catalog_db", "u", "p"),
    List.of("com.example.catalog.entity"));

// Each DAO receives its own SessionFactory
orderDao.setSessionFactory(PSF.getInstance().getPSF("pg_orders"));
productDao.setSessionFactory(PSF.getInstance().getPSF("mysql_catalog"));
```

---

## Spring Boot integration

```java
package com.example.myapp.config;

import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Configuration
@ComponentScan(basePackages = {"com.example.myapp.dao"})
public class DaoConfig {

    private static final Logger log = LoggerFactory.getLogger(DaoConfig.class);
    private static SessionFactory sessionFactory;

    @Bean(name = "sessionFactory")
    public SessionFactory sessionFactory() {
        if (sessionFactory == null) {
            try {
                PSF.getInstance().buildPSF(
                    "main",
                    new ConnectionPropertiesPostgre(
                        "localhost", 5432, "myapp_db", "user", "pass"
                    ),
                    List.of("com.example.myapp.entity")
                );
                sessionFactory = PSF.getInstance().getPSF("main");
            } catch (Exception ex) {
                log.error("SessionFactory initialization failed: {}", ex.getMessage(), ex);
            }
        }
        return sessionFactory;
    }
}
```

**DAO auto-wired with Spring:**

```java
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

@Repository
public class ProductDao extends AbstractJpaDaoV2<Product> implements IProductDao {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        if (this.getSessionFactory() == null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }

    public ProductDao() {
        super();
        setClazz(Product.class);
    }
}
```

---

## C3P0 connection pool defaults

All connection classes configure C3P0 with the following defaults:

| Property | Default | Description |
|----------|---------|-------------|
| `minPoolSize` | 5 | Minimum idle connections |
| `maxPoolSize` | 20 | Maximum active connections |
| `maxIdleTime` | 1800 s | Connection idle timeout |
| `checkoutTimeout` | 30000 ms | Timeout waiting for a connection |
| `idleConnectionTestPeriod` | 3600 s | Health-check interval |

---

## Enable SQL logging

```xml
<!-- log4j2.xml -->
<Logger name="org.hibernate.SQL" level="DEBUG" additivity="false">
    <AppenderRef ref="Console"/>
</Logger>
<Logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE" additivity="false">
    <AppenderRef ref="Console"/>
</Logger>
```
