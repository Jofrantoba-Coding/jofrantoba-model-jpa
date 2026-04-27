# jofrantoba-model-jpa

Framework ORM genérico y flexible basado en **JPA/Hibernate 6** con soporte para múltiples motores de base de datos (MySQL, Oracle, PostgreSQL, SQL Server). Proporciona una arquitectura DAO robusta y reutilizable con operaciones complejas como JOINs, GROUP BY, paginación, procedimientos almacenados y consultas nativas.

**Requisitos mínimos:** Java 17+ | **Versiones soportadas:** Java 17, 21 y superiores

📚 **[Documentación completa en GitHub Pages](https://jofrantoba.github.io/jofrantoba-model-jpa)**

## 🎯 Características principales

- ✅ **DAO Genérico**: `AbstractJpaDao<T>` reutilizable para cualquier entidad JPA
- ✅ **Operaciones CRUD**: Save, update, delete, findById con soporte para PKs numéricas y de texto
- ✅ **Consultas Avanzadas**: JOINs, GROUP BY, filtros dinámicos, ordenamiento, paginación
- ✅ **Procedimientos Almacenados**: Ejecución de SPs con parámetros de entrada/salida
- ✅ **Consultas Nativas SQL/HQL**: Con transformación automática de resultados a JSON
- ✅ **Multi-BD**: Configuración desacoplada para MySQL, Oracle, PostgreSQL, SQL Server
- ✅ **Manejo de Conexiones**: Pool de conexiones C3P0 con caché de SessionFactory
- ✅ **Logging Avanzado**: SLF4J + Log4j2 2.21.1
- ✅ **Zero Configuration**: Auto-discovery de entidades con anotaciones `@Entity`

## 📦 Estructura del proyecto

```
src/
├── main/java/com/jofrantoba/model/jpa/
│   ├── daoentity/
│   │   ├── AbstractJpaDao.java      # DAO genérico base
│   │   ├── InterCrud.java           # Interfaz CRUD
│   │   ├── ParameterProcedure.java  # Parámetros para SPs
│   │   └── test/                    # Entidades de prueba
│   ├── psf/
│   │   ├── PSF.java                 # Singleton SessionFactory
│   │   └── connection/              # Configuraciones BD
│   │       ├── ConnectionProperties.java (interfaz)
│   │       ├── ConnectionPropertiesMysql.java
│   │       ├── ConnectionPropertiesOracle.java
│   │       ├── ConnectionPropertiesPostgre.java
│   │       └── ConnectionPropertiesSqlServer.java
│   └── shared/
│       ├── Shared.java              # Utilidades SQL
│       └── UnknownException.java    # Excepciones custom
├── resources/
│   └── log4j2.xml                   # Configuración logging
└── test/java/com/jofrantoba/model/jpa/  # Suite de pruebas
```

## 🚀 Inicio rápido

### 1. Definir una Entidad JPA

```java
package com.miempresa.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "empleados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "salario")
    private Double salario;

    @Column(name = "departamento", length = 50)
    private String departamento;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
```

### 2. Crear un DAO específico

```java
package com.miempresa.dao;

import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.miempresa.modelos.Empleado;

public class EmpleadoDao extends AbstractJpaDao<Empleado> {
    
    public EmpleadoDao() {
        setClazz(Empleado.class);
    }
}
```

### 3. Configurar la Conexión a BD

```java
import org.hibernate.SessionFactory;
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesMysql;
import java.util.List;

public class DatabaseConfig {
    
    private static SessionFactory sessionFactory;
    
    public static SessionFactory initializeDatabase() {
        if (sessionFactory == null) {
            sessionFactory = PSF.getInstance().buildPSF(
                "mysql",  // Clave para cachear la SessionFactory
                new ConnectionPropertiesMysql(
                    "localhost",      // host
                    3306,              // puerto
                    "mi_base_datos",  // database
                    "root",           // usuario
                    "password"        // contraseña
                ),
                List.of("com.miempresa.modelos")  // Paquete(s) con entidades
            );
        }
        return sessionFactory;
    }
    
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            return initializeDatabase();
        }
        return sessionFactory;
    }
}
```

### 4. Usar el DAO en tu aplicación

```java
import org.hibernate.SessionFactory;

public class GestorEmpleados {
    
    private EmpleadoDao empleadoDao;
    
    public GestorEmpleados() {
        this.empleadoDao = new EmpleadoDao();
        this.empleadoDao.setSessionFactory(
            DatabaseConfig.getSessionFactory()
        );
    }
    
    // ========== CRUD BÁSICO ==========
    
    public void crearEmpleado(Empleado empleado) {
        empleado.setFechaIngreso(LocalDateTime.now());
        empleado.setActivo(true);
        empleadoDao.save(empleado);
        System.out.println("✓ Empleado creado: " + empleado.getNombre());
    }
    
    public Empleado buscarPorId(long id) {
        return empleadoDao.findById(id);
    }
    
    public void actualizarEmpleado(Empleado empleado) {
        empleado.setFechaActualizacion(LocalDateTime.now());
        empleadoDao.update(empleado);
        System.out.println("✓ Empleado actualizado: " + empleado.getNombre());
    }
    
    public void eliminarEmpleado(long id) {
        empleadoDao.delete(id);
        System.out.println("✓ Empleado eliminado");
    }
    
    // ========== OPERACIONES AVANZADAS ==========
    
    public void listarTodos() {
        try {
            Collection<Empleado> empleados = empleadoDao.allFields();
            empleados.forEach(e -> 
                System.out.println(e.getId() + " - " + e.getNombre() + " - " + e.getDepartamento())
            );
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    // Filtros dinámicos (AND)
    public void buscarPorDepartamento(String departamento) {
        String[] filtros = {
            "departamento:=:" + departamento
        };
        try {
            Collection<Empleado> empleados = empleadoDao.findCollectionByFilterAnd(filtros);
            System.out.println("Encontrados: " + empleados.size() + " empleados");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    // Consultas con paginación
    public void listarConPaginacion(int pagina, int registrosPorPagina) {
        try {
            Collection<Empleado> empleados = empleadoDao.findCollectionByFilterAndPaginated(
                new String[]{},  // filtros vacíos (sin filtro)
                pagina,
                registrosPorPagina
            );
            System.out.println("Página " + pagina + ": " + empleados.size() + " registros");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

## 📚 Casos de Uso Avanzados

### Ejemplo 1: Consultas con JOINs (PostgreSQL/Oracle)

```java
public void reporteEmpleadosPorDepartamento() {
    String[] joinTables = {
        "INNER:t_empleado e:t_departamento d:e.departamento:d.nombre"
    };
    String table = "t_empleado e";
    String fields = "e.id_empleado, e.nombre, d.nombre as departamento, e.salario";
    String[] filtros = {
        "e.activo:=:true"
    };
    String[] orden = {"e.nombre:ASC"};
    
    try {
        ArrayNode resultado = empleadoDao.allFieldsJoinPostgres(
            joinTables,
            table,
            fields,
            filtros,
            orden,
            "AND"
        );
        System.out.println(resultado.toPrettyString());
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
    }
}
```

### Ejemplo 2: GROUP BY y Agregaciones

```java
public void salarioPromedioPorDepartamento() {
    String[] joinTables = {};
    String table = "t_empleado";
    String fields = "departamento, AVG(salario) as salario_promedio, COUNT(*) as total_empleados";
    String[] filtros = {"activo:=:true"};
    String groupBy = "departamento";
    String[] orden = {"salario_promedio:DESC"};
    
    try {
        ArrayNode resultado = empleadoDao.allFieldsJoinPostgresGroupBy(
            joinTables,
            table,
            fields,
            filtros,
            orden,
            groupBy
        );
        System.out.println("Reporte de Salarios:\n" + resultado.toPrettyString());
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
    }
}
```

### Ejemplo 3: Procedimientos Almacenados

```java
public void ejecutarProcedimientoActualizacion() {
    String nombreProcedimiento = "SP_ACTUALIZAR_DATOS";
    String jsonParametros = """
        {
            "id_empleado": 1,
            "nuevo_salario": 45000,
            "motivo": "Promoción"
        }
        """;
    
    try {
        Long registrosAfectados = empleadoDao.iudProcedureJson(
            nombreProcedimiento,
            jsonParametros
        );
        System.out.println("Registros afectados: " + registrosAfectados);
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
    }
}
```

### Ejemplo 4: Consultas Nativas SQL

```java
public void consultaNativaSql() {
    String sql = """
        SELECT e.id_empleado, e.nombre, e.salario, COUNT(d.id) as total_departamentos
        FROM empleados e
        LEFT JOIN departamentos d ON e.id_departamento = d.id
        WHERE e.activo = 1
        GROUP BY e.id_empleado
        HAVING total_departamentos > 0
        ORDER BY e.salario DESC
        """;
    
    try {
        List<Empleado> resultados = empleadoDao.findCollectionByNativeQuery(sql);
        resultados.forEach(e -> 
            System.out.println(e.getNombre() + " - Salario: $" + e.getSalario())
        );
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
    }
}
```

## Ejemplo de Producción: Catálogo con árbol de categorías (PostgreSQL)

Patrón completo para gestionar entidades jerárquicas (categorías, menús, configuraciones). Ver más ejemplos en la [documentación web](https://jofrantoba.github.io/jofrantoba-model-jpa/examples).

### Configuración Spring Boot

```java
@Configuration
@ComponentScan(basePackages = {"com.example.myapp.dao"})
public class DaoConfig {

    private static SessionFactory sessionFactory;

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            PSF.getInstance().buildPSF(
                "main",
                new ConnectionPropertiesPostgre(
                    "localhost", 5432, "myapp_db", "user", "password"
                ),
                List.of("com.example.myapp.entity")
            );
            sessionFactory = PSF.getInstance().getPSF("main");
        }
        return sessionFactory;
    }
}
```

### Entidad jerárquica (self-reference)

```java
@Entity
@Table(name = "categories")
@Data @NoArgsConstructor @AllArgsConstructor
public class Category implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 20)
    private String code;

    @ManyToOne @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    @Column private Boolean active = true;
}
```

### DAO con operaciones de árbol

```java
@Repository
public class CategoryDao extends AbstractJpaDao<Category> implements ICategoryDao {

    @Autowired(required = false) @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        if (getSessionFactory() == null && sessionFactory != null)
            setSessionFactory(sessionFactory);
    }

    public CategoryDao() { super(); setClazz(Category.class); }

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
    public Collection<Category> childrenOf(Long parentId) throws Exception {
        return customFieldsJoinFilterAnd(
            "base.id as id, base.name as name, parent.id as parentId",
            "left:parent",
            new String[]{"=:parent.id:" + parentId, "=:base.active:true"},
            new String[]{"base.name:asc"}
        );
    }

    @Override
    public Long countChildren(Long parentId) throws Exception {
        return rowCountJoinFilterAnd(
            "left:parent",
            new String[]{"=:parent.id:" + parentId}
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

### Ejemplo 4: El DSL en Detalle (Lenguaje Intermedio)

El framework usa un **DSL declarativo basado en `:`** que se traduce internamente a SQL/HQL:

```java
// ===== OPERADORES DE COMPARACIÓN =====
String[] filtros1 = {
    "=:base.isPersistente:true"           // WHERE isPersistente = true
};

String[] filtros2 = {
    "!=:base.estado:cancelado",           // WHERE estado != 'cancelado'
    ">:base.version:1000",                // AND version > 1000
    "<:base.version:2000"                 // AND version < 2000
};

// ===== OPERADORES NULL =====
String[] filtros3 = {
    "isnull:parent.id",                   // WHERE parent.id IS NULL
    "isnotnull:descripcion"               // AND descripcion IS NOT NULL
};

// ===== OPERADORES DE RANGO =====
String[] filtros4 = {
    "between:version:1000:2000",          // WHERE version BETWEEN 1000 AND 2000
    "in:id:1:2:3:4:5"                    // AND id IN (1,2,3,4,5)
};

// ===== JOINS =====
String joinTable = "left:parent";        // LEFT JOIN parent
String[] joinTables = {
    "left:parent",                        // LEFT JOIN parent
    "inner:departamento"                  // INNER JOIN departamento
};

// ===== ORDENAMIENTO =====
String[] orden = {
    "base.descripcion:asc",               // ORDER BY descripcion ASC
    "base.version:desc"                   // , version DESC
};

// ===== CAMPOS CON ALIAS =====
String fields = "base.id as id, " +
                "base.descripcion as descripcion, " +
                "parent.id as idParent, " +
                "parent.descripcion as descripcionParent";

// ===== RESULTADO FINAL (Se traduce a HQL internamente) =====
Collection<Parametrias> resultado = daoParametrias.customFieldsJoinFilterAnd(
    fields,           // SELECT base.id as id, ...
    joinTable,        // LEFT JOIN parent
    filtros3,         // WHERE parent.id IS NULL AND descripcion IS NOT NULL
    orden             // ORDER BY descripcion ASC
);
```

Como en el ejemplo anterior, el DSL anterior se traduce a:
```sql
SELECT 
    base.id as id,
    base.descripcion as descripcion,
    parent.id as idParent,
    parent.descripcion as descripcionParent
FROM Parametrias base
LEFT JOIN Parametrias.parent parent
WHERE 1=1
    AND parent.id IS NULL
    AND descripcion IS NOT NULL
ORDER BY base.descripcion ASC
```

### Ejemplo 5: Test Unitario (TestSelectDaoParametria)

```java
@Log4j2
public class TestSelectDaoParametria extends TestBaseDao {

    @Test
    public void selectByID() throws Exception {
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        Transaction tx = dao.getSession().beginTransaction();
        
        Parametrias entidad = dao.findById(6L);
        log.info("Registro encontrado: {}", entidad);
        
        tx.commit();
    }

    @Test
    public void selectPadres() throws Exception {
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        Transaction tx = dao.getSession().beginTransaction();
        
        List<Parametrias> lista = (List<Parametrias>) dao.parents(1, 5);
        log.info("Total padres (página 1, 5 por página): {}", lista.size());
        lista.forEach(p -> 
            log.info("ID: {}, Descripción: {}", p.getId(), p.getDescripcion())
        );
        
        tx.commit();
    }

    @Test
    public void selectHijosByPadre() throws Exception {
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        Transaction tx = dao.getSession().beginTransaction();
        
        Collection<Parametrias> hijos = dao.childrensByParents(2L);
        log.info("Total hijos del padre 2: {}", hijos.size());
        hijos.forEach(h -> 
            log.info("Hijo: {} (Parent: {})", 
                h.getDescripcion(), h.getParent().getDescripcion())
        );
        
        tx.commit();
    }

    @Test
    public void rowCountChildren() throws Exception {
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        Transaction tx = dao.getSession().beginTransaction();
        
        Long count = dao.countChildrens(3L);
        log.info("El padre 3 tiene {} hijos", count);
        
        tx.commit();
    }

    @Test
    public void listarConPaginacion() throws Exception {
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        Transaction tx = dao.getSession().beginTransaction();
        
        ArrayNode lista = dao.listar(10L, 0L);  // 10 registros, offset 0
        log.info("Registros (formato JSON):\n{}", lista.toPrettyString());
        
        tx.commit();
    }
}
```

---
## 🔤 El DSL Declarativo (Domain Specific Language)

El framework incluye un **DSL único basado en cadenas de texto** que permite escribir consultas complejas sin SQL/HQL manual. El separador es **`:`** (dos-puntos).

### Ventajas
- ✅ **Agnóstico a Base de Datos**: El mismo código funciona en MySQL, PostgreSQL, Oracle, SQL Server
- ✅ **Legible**: Sintaxis clara y autodocumentada
- ✅ **Seguro**: Previene inyección SQL automáticamente
- ✅ **No requiere SQL manual**: Generación dinámica de sentencias

### Componentes del DSL

#### 1. Operadores de Comparación
```java
"=:campo:valor"              // = 
"!=:campo:valor"             // !=
">:campo:valor"              // >
"<:campo:valor"              // <
">=:campo:valor"             // >=
"<=:campo:valor"             // <=
```

#### 2. Operadores Especiales
```java
"equal:campo:valor"          // = (con comillas: 'valor')
"notequal:campo:valor"       // !=
"like:campo:%patron%"        // LIKE
"between:campo:min:max"      // BETWEEN
"in:campo:val1:val2:val3"   // IN
"isnull:campo"               // IS NULL
"isnotnull:campo"            // IS NOT NULL
```

#### 3. JOINs
```java
"left:nombreEntidad"         // LEFT JOIN
"inner:nombreEntidad"        // INNER JOIN
"right:nombreEntidad"        // RIGHT JOIN
```

#### 4. Uso del DSL en Métodos
```java
// Filtros (array de strings con operadores)
String[] filtros = {
    "=:base.activo:true",
    ">:base.salario:30000",
    "isnull:base.fecha_baja"
};

// Orden (array con "campo:dirección")
String[] orden = {
    "base.apellido:asc",
    "base.nombre:asc"
};

// Campos SELECT (con alias)
String fields = "base.id as id, base.nombre as nombre, base.salario as salario";

// Ejecutar
Collection<Empleado> resultados = dao.customFieldsFilterAnd(
    fields, filtros, orden
);
```

**📖 Para referencia completa, ver [DSL_CHEATSHEET.md](DSL_CHEATSHEET.md) con ejemplos de cada operador.**

---

## 📚 Referencia de Métodos DAO Principales

| Método | DSL | Descripción |
|--------|-----|------------|
| `save(T entity)` | N/A | Inserta o actualiza |
| `update(T entity)` | N/A | Actualiza existente (merge) |
| `delete(T entity)` | N/A | Elimina entidad |
| `delete(long id)` | N/A | Elimina por ID |
| `findById(long id)` | N/A | Busca por ID |
| `allFields()` | N/A | Obtiene todas |
| `customFieldsFilterAnd(String fields, String[] filters, String[] order)` | ✅ | Filtros AND con DSL |
| `customFieldsFilterOr(String fields, String[] filters, String[] order)` | ✅ | Filtros OR con DSL |
| `customFieldsJoinFilterAnd(String fields, String joinTable, String[] filters, String[] order)` | ✅ | JOINs con DSL |
| `allFieldsJoinPostgres(String[] joins, String table, String fields, String[] filters, String[] order)` | ✅ | JOINs PostgreSQL con DSL |
| `allFieldsJoinPostgresGroupBy(String[] joins, String table, String fields, String[] filters, String[] order, String groupBy)` | ✅ | GROUP BY con DSL |
| `rowCountJoinFilterAnd(String joinTable, String[] filters)` | ✅ | COUNT con filtros |
| `iudProcedureJson(String name, String json)` | N/A | Ejecuta procedimiento |
| `allFieldsLimitOffsetPostgres(String table, String fields, String[] filters, String[] order, Long limit, Long offset)` | ✅ | Paginación con DSL |

---
## �🔧 Configuración de diferentes Bases de Datos

### MySQL
```java
ConnectionPropertiesMysql config = new ConnectionPropertiesMysql(
    "localhost",      // host
    3306,             // puerto (por defecto)
    "mi_db",          // base de datos
    "usuario",        // usuario
    "contraseña"      // contraseña
);
SessionFactory sf = PSF.getInstance().buildPSF("mysql_prod", config, 
    List.of("com.miapp.modelos"));
```

### PostgreSQL
```java
ConnectionPropertiesPostgre config = new ConnectionPropertiesPostgre(
    "localhost",
    5432,
    "mi_db",
    "usuario",
    "contraseña"
);
SessionFactory sf = PSF.getInstance().buildPSF("postgres_prod", config, 
    List.of("com.miapp.modelos"));
```

### Oracle
```java
ConnectionPropertiesOracle config = new ConnectionPropertiesOracle(
    "localhost",
    1521,
    "ORCL",
    "usuario",
    "contraseña"
);
SessionFactory sf = PSF.getInstance().buildPSF("oracle_prod", config, 
    List.of("com.miapp.modelos"));
```

### SQL Server
```java
ConnectionPropertiesSqlServer config = new ConnectionPropertiesSqlServer(
    "localhost",
    1433,
    "mi_db",
    "usuario",
    "contraseña"
);
SessionFactory sf = PSF.getInstance().buildPSF("sqlserver_prod", config, 
    List.of("com.miapp.modelos"));
```

---

## 🔗 Recursos

- **[Documentación web](https://jofrantoba.github.io/jofrantoba-model-jpa)** - Guía completa con ejemplos, referencia de API y DSL
- **[jofrantoba-model-jpa en GitHub](https://github.com/jofrantoba/jofrantoba-model-jpa)** - Código fuente y tests

---

## 📋 Métodos Principales Removida - Ver Tabla de Referencia

La tabla completa de métodos se ha integrado arriba en la sección "📚 Referencia de Métodos DAO Principales" con información de soporte para DSL.

## 📊 Dependencias principales

```xml
<!-- Java/Jakarta -->
<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
    <version>3.2.0</version>
</dependency>

<!-- Hibernate 6 Core -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.5.2.Final</version>
</dependency>

<!-- Pool de Conexiones -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-c3p0</artifactId>
    <version>6.5.2.Final</version>
</dependency>

<!-- Drivers JDBC -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.28</version>
</dependency>

<!-- Lombok (anotaciones) -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.32</version>
    <scope>provided</scope>
</dependency>

<!-- Logging -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.21.1</version>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.6.2</version>
    <scope>test</scope>
</dependency>
```

## 🧪 Ejecución de Pruebas

```bash
# Ejecutar todos los tests
mvn clean test

# Ejecutar tests de una base de datos específica
mvn test -Dtest=TestAbstractJpaDaoMysql

# Ejecutar con cobertura
mvn clean test jacoco:report
```

## 🐛 Troubleshooting y Debugging

### Problema: SessionFactory no inicializado

**Síntoma:** `NullPointerException` al usar el DAO

**Solución:**
```java
@PostConstruct
public void init() {
    if (this.getSessionFactory() == null && sessionFactory != null) {
        this.setSessionFactory(sessionFactory);
    }
}
```

### Problema: Lazy Loading en Relaciones

**Síntoma:** `LazyInitializationException` al acceder a relacionados fuera de transacción

**Solución:**
```java
// Usar Eager Loading en la relación
@ManyToOne(fetch = FetchType.EAGER)
private Padre padre;

// O cargar dentro de transacción
@Transactional
public void procesar() {
    Entidad e = dao.findById(1);
    // Acceder a relacionados aquí está permitido
}
```

### Problema: Valores NULL en el DSL

**Síntoma:** Filtros con NULL no funcionan correctamente

**Solución:**
```java
// Usar isnull / isnotnull en lugar de "=:campo:null"
String[] filtrosCorrecto = {
    "isnull:base.fecha_baja"        // ✅ Correcto
};

String[] filtrosIncorrecto = {
    "=:base.fecha_baja:null"        // ❌ Incorrecto
};
```

### Problema: Paginación con JOINs

**Síntoma:** Resultados duplicados en paginación

**Solución:**
```java
// Usar DISTINCT en los campos
String fields = "DISTINCT base.id as id, base.nombre as nombre";

// O usar GROUP BY
String groupBy = "base.id";
```

### Problema: Caché de SessionFactory

**Síntoma:** La conexión anterior se mantiene después de cambiar configuración

**Solución:**
```java
// Limpiar caché si necesitas cambiar configuración
PSF.getInstance().buildPSF("nueva_clave", newConfig, packages);

// Ahora usar la nueva clave
SessionFactory sf = PSF.getInstance().getPSF("nueva_clave");
```

### Debugging: Activar logging SQL

```xml
<!-- En log4j2.xml -->
<Logger name="org.hibernate.SQL" level="DEBUG"/>
<Logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE"/>
```

En aplicación:
```java
log.debug("Query: {}", dao.strAllFieldsJoinPostgres(...));
```

---

## ✅ Requisitos del Sistema

- **Java:** 17, 21 o superior (módulos JVMRNA soportados)
- **Maven:** 3.8.1+
- **BD Soportadas:** MySQL 5.7+, Oracle 11g+, PostgreSQL 10+, SQL Server 2016+
- **Memoria:** Mínimo 512 MB (recomendado 1 GB+)

## 🎓 Mejores Prácticas

1. **Siempre usar transacciones**: Envuelve operaciones críticas en `@Transactional`
2. **Lazy loading**: Cuidado con inicialización de relaciones fuera de sesión
3. **Validación de entidades**: Usa `@NotNull`, `@NotEmpty` antes de `save()`
4. **Caché de SessionFactory**: PSF cachea automáticamente por clave
5. **Cierre de recursos**: Llama a `clear()` en operaciones batch
6. **Índices en BD**: Crea índices en columnas de filtrado frecuente

## 📄 Licencia

Este proyecto está licenciado bajo la **Licencia MIT**. Eres libre de usarlo, modificarlo y distribuirlo.

Ver el archivo [LICENSE](LICENSE) para más detalles.

## 👨‍💻 Autor

**Jofrantoba** - Framework JPA/Hibernate de código abierto para acceso a datos genérico.

---

**Versión:** 2.0.0 | **Java:** 17+ | **Hibernate:** 6.5.2 | **Jakarta Persistence:** 3.2.0
