---
title: Examples
nav_order: 6
---

# Examples
{: .no_toc }

Complete, runnable patterns covering the most common use cases.

<details open markdown="block">
  <summary>Contents</summary>
  {: .text-delta }
- TOC
{:toc}
</details>

---

## Example 1 — Employee management (MySQL, basic CRUD)

### Entity

```java
package com.example.hr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity @Table(name = "employees")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "department", length = 80)
    private String department;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "hired_at")
    private LocalDateTime hiredAt;
}
```

### DAO

```java
package com.example.hr.dao;

import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.example.hr.entity.Employee;
import java.util.Collection;

public interface IEmployeeDao extends InterCrud<Employee> {
    Collection<Employee> findByDepartment(String dept) throws Exception;
    Collection<Employee> findActive(int page, int size) throws Exception;
}

public class EmployeeDao extends AbstractJpaDaoV2<Employee> implements IEmployeeDao {

    public EmployeeDao() { setClazz(Employee.class); }

    @Override
    public Collection<Employee> findByDepartment(String dept) throws Exception {
        return customFieldsFilterAnd(
            "base.id, base.fullName, base.email, base.salary",
            new String[]{"equal:base.department:" + dept, "=:base.active:true"},
            new String[]{"base.fullName:asc"}
        );
    }

    @Override
    public Collection<Employee> findActive(int page, int size) throws Exception {
        return findCollectionByFilterAndPaginated(
            new String[]{"=:base.active:true"}, page, size
        );
    }
}
```

### Database configuration

```java
package com.example.hr.config;

import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesMysql;
import org.hibernate.SessionFactory;
import java.util.List;

public class HrDatabaseConfig {

    private static SessionFactory sf;

    public static SessionFactory get() {
        if (sf == null) {
            sf = PSF.getInstance().buildPSF(
                "hr_mysql",
                new ConnectionPropertiesMysql("localhost", 3306, "hr_db", "root", "pass"),
                List.of("com.example.hr.entity")
            );
        }
        return sf;
    }
}
```

### Usage

```java
EmployeeDao dao = new EmployeeDao();
dao.setSessionFactory(HrDatabaseConfig.get());

// Create
Employee emp = Employee.builder()
    .fullName("Alice Johnson")
    .email("alice@example.com")
    .department("Engineering")
    .salary(75000.0)
    .active(true)
    .hiredAt(LocalDateTime.now())
    .build();
dao.save(emp);

// Read
Employee found = dao.findById(1L);

// Update
found.setSalary(80000.0);
dao.update(found);

// Delete
dao.delete(1L);

// Query by department
Collection<Employee> engineers = dao.findByDepartment("Engineering");
engineers.forEach(e -> System.out.println(e.getFullName() + " – " + e.getSalary()));

// Paginated list
Collection<Employee> page1 = dao.findActive(1, 10);
```

---

## Example 2 — Product catalog with categories (PostgreSQL, hierarchical tree)

A common pattern for menus, settings, tags, or any tree-structured entity.

### Entities

```java
@Entity @Table(name = "categories")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 20)
    private String code;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    @Column(name = "active")
    private Boolean active = true;
}

@Entity @Table(name = "products")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "active")
    private Boolean active = true;
}
```

### CategoryDao — tree operations

```java
public interface ICategoryDao extends InterCrud<Category> {
    Collection<Category> roots() throws Exception;
    Collection<Category> roots(int page, int size) throws Exception;
    Collection<Category> childrenOf(Long parentId) throws Exception;
    Long countChildren(Long parentId) throws Exception;
    ArrayNode listPaged(Long limit, Long offset) throws Exception;
}

public class CategoryDao extends AbstractJpaDaoV2<Category> implements ICategoryDao {

    public CategoryDao() { setClazz(Category.class); }

    @Override
    public Collection<Category> roots() throws Exception {
        return customFieldsJoinFilterAnd(
            "base.id as id, base.name as name, base.code as code",
            "left:parent",
            new String[]{"isnull:parent.id", "=:base.active:true"},
            new String[]{"base.name:asc"}
        );
    }

    @Override
    public Collection<Category> roots(int page, int size) throws Exception {
        return customFieldsJoinFilterAnd(
            "base.id as id, base.name as name",
            "left:parent",
            new String[]{"isnull:parent.id", "=:base.active:true"},
            new String[]{"base.name:asc"},
            page, size
        );
    }

    @Override
    public Collection<Category> childrenOf(Long parentId) throws Exception {
        return customFieldsJoinFilterAnd(
            "base.id as id, base.name as name, " +
            "parent.id as parentId, parent.name as parentName",
            "left:parent",
            new String[]{"=:parent.id:" + parentId, "=:base.active:true"},
            new String[]{"base.name:asc"}
        );
    }

    @Override
    public Long countChildren(Long parentId) throws Exception {
        return rowCountJoinFilterAnd(
            "left:parent",
            new String[]{"=:parent.id:" + parentId, "=:base.active:true"}
        );
    }

    @Override
    public ArrayNode listPaged(Long limit, Long offset) throws Exception {
        return allFieldsLimitOffsetPostgres(
            "public.categories as base",
            "base.id, base.name, base.code",
            new String[]{"=:base.active:true"},
            new String[]{"id:asc"},
            limit, offset
        );
    }
}
```

### Service using the tree

```java
@Service
public class CategoryService {

    @Autowired private ICategoryDao categoryDao;

    public void createRoot(String name, String code) throws Exception {
        Category cat = new Category();
        cat.setName(name);
        cat.setCode(code);
        cat.setActive(true);
        categoryDao.save(cat);
    }

    public void createChild(Long parentId, String name) throws Exception {
        Category parent = categoryDao.findById(parentId);
        if (parent == null) throw new RuntimeException("Parent not found: " + parentId);

        Category child = new Category();
        child.setName(name);
        child.setParent(parent);
        child.setActive(true);
        categoryDao.save(child);
    }

    public void printTree() throws Exception {
        Collection<Category> roots = categoryDao.roots();
        for (Category root : roots) {
            System.out.println("+ " + root.getName());
            Collection<Category> children = categoryDao.childrenOf(root.getId());
            children.forEach(c -> System.out.println("  └─ " + c.getName()));
        }
    }
}
```

---

## Example 3 — Order management with JOINs and aggregations (PostgreSQL)

```java
@Entity @Table(name = "orders")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "status", length = 30)
    private String status;         // OPEN, CLOSED, CANCELLED

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

### JOIN query across tables

```java
// Report: orders with customer name and total
String table  = "jofrantoba.sales.orders as base";
String fields = "base.id as orderId, c.name as customer, " +
                "base.status, base.total_amount as total";
String[] joins   = {
    "inner:jofrantoba.sales.customers as c:on:base.customer_id:c.id"
};
String[] filters = {"equal:base.status:CLOSED"};
String[] order   = {"base.total_amount:desc"};

ArrayNode result = orderDao.allFieldsJoinPostgres(
    joins, table, fields, filters, order, "and"
);
System.out.println(result.toPrettyString());
```

### GROUP BY — revenue by status

```java
String table   = "jofrantoba.sales.orders as base";
String fields  = "base.status, COUNT(*) as qty, SUM(base.total_amount) as revenue";
String[] joins    = {};
String[] filters  = {"isnotnull:base.created_at"};
String[] order    = {"revenue:desc"};
String groupBy = "base.status";

ArrayNode stats = orderDao.allFieldsJoinPostgresGroupBy(
    joins, table, fields, filters, order, groupBy
);
```

### Count open orders

```java
Long openCount = orderDao.rowCountJoinFilterAnd(
    "left:customer",
    new String[]{"equal:base.status:OPEN", "isnotnull:base.created_at"}
);
System.out.println("Open orders: " + openCount);
```

---

## Example 4 — Stored procedure (SQL Server)

```java
// SP signature:  SP_APPLY_DISCOUNT @p_product_id INT, @p_discount FLOAT, @p_reason VARCHAR
public Long applyDiscount(Long productId, Double discount, String reason) throws Exception {
    String json = String.format(
        "{\"p_product_id\":%d,\"p_discount\":%.2f,\"p_reason\":\"%s\"}",
        productId, discount, reason
    );
    return dao.iudProcedureJson("SP_APPLY_DISCOUNT", json);
}
```

---

## Example 5 — Spring Boot full integration

```java
// DaoConfig.java
@Configuration
@ComponentScan("com.example.myapp.dao")
public class DaoConfig {
    private static SessionFactory sf;

    @Bean(name = "sessionFactory")
    public SessionFactory sessionFactory() {
        if (sf == null) {
            PSF.getInstance().buildPSF(
                "main",
                new ConnectionPropertiesPostgre("db-host", 5432, "myapp", "user", "pass"),
                List.of("com.example.myapp.entity")
            );
            sf = PSF.getInstance().getPSF("main");
        }
        return sf;
    }
}

// ProductDao.java
@Repository
public class ProductDao extends AbstractJpaDaoV2<Product> implements IProductDao {

    @Autowired @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        if (getSessionFactory() == null && sessionFactory != null)
            setSessionFactory(sessionFactory);
    }

    public ProductDao() { super(); setClazz(Product.class); }
}

// ProductService.java
@Service
public class ProductService {

    @Autowired private IProductDao productDao;

    @Transactional
    public void create(Product p) { productDao.save(p); }

    public Collection<Product> active() throws Exception {
        return productDao.findCollectionByFilterAnd(
            new String[]{"=:base.active:true"}
        );
    }
}
```

---

## Example 6 — Unit test with JUnit 5

```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDaoTest {

    private ProductDao dao;

    @BeforeAll
    void setUp() {
        SessionFactory sf = PSF.getInstance().buildPSF(
            "test_pg",
            new ConnectionPropertiesPostgre("localhost", 5432, "test_db", "u", "p"),
            List.of("com.example.myapp.entity")
        );
        dao = new ProductDao();
        dao.setSessionFactory(sf);
    }

    @Test
    void saveAndFindById() {
        Product p = Product.builder().name("Widget").sku("WGT-01").price(9.99).active(true).build();
        dao.save(p);
        assertNotNull(p.getId());

        Product found = dao.findById(p.getId());
        assertEquals("Widget", found.getName());
    }

    @Test
    void filterActive() throws Exception {
        Collection<Product> list = dao.findCollectionByFilterAnd(
            new String[]{"=:base.active:true"}
        );
        list.forEach(item -> assertTrue(item.getActive()));
    }
}
```

---

## Troubleshooting

| Symptom | Cause | Fix |
|---------|-------|-----|
| `NullPointerException` on first DAO call | `setSessionFactory` not called | Call it after constructing the DAO |
| `LazyInitializationException` | Accessing a relation outside session | Use `@Transactional` or `FetchType.EAGER` |
| Duplicate rows with JOIN + pagination | Missing DISTINCT | Add `DISTINCT` to the `fields` string |
| NULL filter not working | Using `"=:field:null"` | Use `"isnull:field"` |
| Wrong results with `equal` vs `=` | Text vs numeric | `equal`/`notequal` for strings; `=`/`!=` for numbers |
| SessionFactory already closed | `destroyPSF` called too early | Only call `destroyPSF` on application shutdown |
