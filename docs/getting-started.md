---
title: Getting Started
nav_order: 2
---

# Getting Started
{: .no_toc }

<details open markdown="block">
  <summary>Contents</summary>
  {: .text-delta }
- TOC
{:toc}
</details>

---

## Prerequisites

- Java 17, 21 or later
- Maven 3.8+
- A running database (MySQL, PostgreSQL, Oracle or SQL Server)

---

## Step 1 — Add the dependency

```xml
<dependency>
    <groupId>com.jofrantoba.model.jpa</groupId>
    <artifactId>jofrantoba-model-jpa</artifactId>
    <version>2.0.0</version>
</dependency>
```

---

## Step 2 — Define a JPA entity

```java
package com.example.myapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "sku", unique = true, length = 50)
    private String sku;

    @Column(name = "price")
    private Double price;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

---

## Step 3 — Create a DAO interface and implementation

**Interface** — extends `InterCrud<T>` to inherit all built-in methods, then add custom ones:

```java
package com.example.myapp.dao;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.example.myapp.entity.Product;
import java.util.Collection;

public interface IProductDao extends InterCrud<Product> {
    Collection<Product> findActive() throws Exception;
    Collection<Product> findBySku(String sku) throws Exception;
}
```

**Implementation** — extends `AbstractJpaDaoV2<T>`:

```java
package com.example.myapp.dao;

import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.example.myapp.entity.Product;
import java.util.Collection;

public class ProductDao extends AbstractJpaDaoV2<Product> implements IProductDao {

    public ProductDao() {
        setClazz(Product.class);
    }

    @Override
    public Collection<Product> findActive() throws Exception {
        return customFieldsFilterAnd(
            "base.id as id, base.name as name, base.price as price",
            new String[]{"=:base.active:true"},
            new String[]{"base.name:asc"}
        );
    }

    @Override
    public Collection<Product> findBySku(String sku) throws Exception {
        return customFieldsFilterAnd(
            "base.id, base.name, base.sku, base.price",
            new String[]{"equal:base.sku:" + sku},
            new String[]{}
        );
    }
}
```

---

## Step 4 — Configure the database connection

```java
package com.example.myapp.config;

import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import org.hibernate.SessionFactory;
import java.util.List;

public class DatabaseConfig {

    private static SessionFactory sessionFactory;

    public static SessionFactory get() {
        if (sessionFactory == null) {
            sessionFactory = PSF.getInstance().buildPSF(
                "main",                                       // cache key (any string)
                new ConnectionPropertiesPostgre(
                    "localhost", 5432, "myapp_db",
                    "dbuser", "dbpassword"
                ),
                List.of("com.example.myapp.entity")           // packages to scan for @Entity
            );
        }
        return sessionFactory;
    }
}
```

---

## Step 5 — Use the DAO

```java
package com.example.myapp.service;

import com.example.myapp.dao.ProductDao;
import com.example.myapp.entity.Product;
import org.hibernate.Transaction;
import java.time.LocalDateTime;
import java.util.Collection;

public class ProductService {

    private final ProductDao dao;

    public ProductService() {
        dao = new ProductDao();
        dao.setSessionFactory(DatabaseConfig.get());
    }

    public void create(Product product) {
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        dao.save(product);
    }

    public Product findById(long id) {
        return dao.findById(id);
    }

    public void update(Product product) {
        dao.update(product);
    }

    public void delete(long id) {
        dao.delete(id);
    }

    public Collection<Product> listActive() throws Exception {
        return dao.findActive();
    }

    public Collection<Product> search(String name) throws Exception {
        return dao.customFieldsFilterAnd(
            "base.id, base.name, base.sku, base.price",
            new String[]{"like:base.name:%" + name + "%", "=:base.active:true"},
            new String[]{"base.name:asc"}
        );
    }

    public Collection<Product> listPaged(int page, int size) throws Exception {
        return dao.findCollectionByFilterAndPaginated(
            new String[]{"=:base.active:true"},
            page, size
        );
    }
}
```

---

## Next steps

- [Database Configuration](configuration) — MySQL, Oracle, SQL Server and Spring Boot wiring
- [DSL Guide](dsl-guide) — full operator reference
- [Examples](examples) — production patterns: hierarchical data, JOINs, aggregations, stored procedures
