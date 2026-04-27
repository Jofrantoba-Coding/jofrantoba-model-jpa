# jofrantoba-model-jpa

Framework ORM genérico y flexible basado en **JPA/Hibernate 6** con soporte para múltiples motores de base de datos (MySQL, Oracle, PostgreSQL, SQL Server). Proporciona una arquitectura DAO robusta y reutilizable con operaciones complejas como JOINs, GROUP BY, paginación, procedimientos almacenados y consultas nativas.

**Requisitos mínimos:** Java 17+ | **Versiones soportadas:** Java 17, 21 y superiores

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

## 🔧 Configuración de diferentes Bases de Datos

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

## 📋 Métodos principales de AbstractJpaDao<T>

| Método | Descripción |
|--------|------------|
| `save(T entity)` | Inserta o actualiza una entidad |
| `update(T entity)` | Actualiza una entidad existente usando `merge()` |
| `delete(T entity)` | Elimina una entidad |
| `delete(long id)` | Elimina por ID numérico |
| `findById(long id)` | Busca por ID numérico |
| `findById(String id)` | Busca por ID texto |
| `allFields()` | Obtiene todas las entidades |
| `findCollectionByFilterAnd(String[] filters)` | Busca con filtros AND |
| `allFieldsJoinPostgres(...)` | Consulta con JOINs dinámicos |
| `allFieldsJoinPostgresGroupBy(...)` | Consulta con JOINs y GROUP BY |
| `iudProcedureJson(name, json)` | Ejecuta procedimiento con JSON |
| `findCollectionByNativeQuery(sql)` | Ejecuta consulta SQL nativa |
| `flush()` | Sincroniza cambios con BD |
| `clear()` | Limpia la sesión de Hibernate |
| `detach(T entity)` | Desasocia una entidad |

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