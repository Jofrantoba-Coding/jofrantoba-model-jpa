# 📚 pg-icl-repository - Módulo de Persistencia JPA

Módulo de repositorio de datos para el **Sistema de Catastro Digital de la Municipalidad Provincial de Chincha (PG-ICL)**, construido sobre el framework **jofrantoba-model-jpa** v2.1.0. Proporciona una capa de acceso a datos completa con más de **60 entidades** y patrones avanzados de consultas mediante un **DSL declarativo personalizado**.

## 🎯 Descripción General

Este módulo implementa una arquitectura de **DAO (Data Access Object)** robusta y escalable para gestionar toda la persistencia del sistema de catastro digital. Incluye:

- ✅ **60+ DAOs especializados** para diferentes entidades del dominio catastral
- ✅ **Interfaz CRUD completa** con operaciones avanzadas
- ✅ **Soporte para relaciones jerárquicas** (padre-hijo)
- ✅ **DSL nativo** para consultas complejas sin raw SQL
- ✅ **Serialización automática a JSON** de resultados
- ✅ **Integración con Spring Boot 3.3** y autoconfiguration

## 📦 Dependencias Principales

```xml
<dependency>
    <groupId>com.jofrantoba.model.jpa</groupId>
    <artifactId>jofrantoba-model-jpa</artifactId>
    <version>2.1.0</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure</artifactId>
    <version>3.3.0</version>
</dependency>

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.1</version>
</dependency>
```

## 🚀 Inicio Rápido

### 1. Configuración de Conexión a Base de Datos

El proyecto utiliza **PostgreSQL** como motor principal. La configuración se realiza a través de un bean Spring:

```java
@Configuration
@ComponentScan(basePackages = {"gob.pe.icl.icl.dao"})
public class ConfigDao {

    @Primary
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() {
        List<String> packages = new ArrayList();
        packages.add("gob.pe.icl.icl.entity");
        
        // Construir SessionFactory para PostgreSQL
        PSF.getInstance().buildPSF(
            "postgre",                    // Clave para caché
            new ConnectionPropertiesPostgre(
                "localhost",              // Host
                5432,                     // Puerto
                "icl",                    // Base de datos
                "postgres",               // Usuario
                "jofrantoba"              // Contraseña
            ),
            packages
        );
        
        return PSF.getInstance().getPSF("postgre");
    }
}
```

### 2. Estructura de una Entidad

Las entidades se definen como JPA estándar:

```java
@Entity
@Table(name = "tm_parametrias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parametrias implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(length = 50)
    private String abreviatura;

    @Column(length = 50)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parametrias parent;

    @OneToMany(mappedBy = "parent")
    private List<Parametrias> childrens = new ArrayList<>();

    @Column(name = "is_persistente")
    private Boolean isPersistente = true;

    @Column(name = "marca_tiempo")
    private LocalDateTime marcaTiempo;

    @Column(name = "version")
    private Long version;

    @Column(name = "orden")
    private Integer orden;
}
```

### 3. Implementación de un DAO

Los DAOs extienden `AbstractJpaDao` y definen operaciones personalizadas:

```java
@Repository
public class DaoParametrias extends AbstractJpaDao<Parametrias> 
    implements InterDaoParametrias {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        if (this.getSessionFactory() == null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }

    public DaoParametrias() {
        super();
        this.setClazz(Parametrias.class);
    }

    @Override
    public Collection<Parametrias> parents() throws Exception {
        String joinTable = "left:parent";
        String[] mapFilterField = {
            "isnull:parent.id",
            "=:base.isPersistente:true"
        };
        String[] mapOrder = {"base.descripcion:asc"};
        String fields = "base.id as id, base.descripcion as descripcion";
        
        return this.customFieldsJoinFilterAnd(
            fields, joinTable, mapFilterField, mapOrder
        );
    }
}
```

## 🔤 El Lenguaje Intermedio (DSL)

### Descripción

El framework utiliza un **Domain Specific Language (DSL) declarativo** basado en **cadenas de texto con separadores `:`** para describir consultas de manera agnóstica a la base de datos. Este DSL se interpreta internamente y se traduce a consultas **HQL/SQL válidas**.

### Ventajas del DSL

✨ **Advantages:**
- 📝 Sintaxis clara y legible
- 🔁 Reutilizable entre diferentes BD
- ⚡ Sin necesidad de escribir SQL/HQL manualmente
- 🛡️ Protección contra inyección SQL
- 📦 Fácil de mantener y debuquear

### Componentes del DSL

#### 1️⃣ **Operadores de Comparación**

```java
// Formato: "operador:campo:valor"

"=:base.edad:30"                  // edad = 30
"!=:base.estado:inactivo"         // estado != 'inactivo'
">:base.salario:5000"             // salario > 5000
"<:base.fecha_creacion:2025-01-01" // fecha_creacion < 2025-01-01
">=:base.edad:18"                 // edad >= 18
"<=:base.precio:1000"             // precio <= 1000
```

#### 2️⃣ **Operadores de Igualdad (con comillas)**

```java
"equal:base.descripcion:TITULAR"    // 'TITULAR' (con comillas)
"notequal:base.estado:CANCELADO"    // != 'CANCELADO'
```

#### 3️⃣ **Operador LIKE**

```java
"like:base.nombre:%Juan%"             // nombre like '%Juan%'
"like:base.apellido:^Garcia"          // apellido like '^Garcia'
```

#### 4️⃣ **Operador BETWEEN**

```java
"between:base.edad:18:65"             // edad between 18 and 65
"between:base.fecha:2025-01-01:2025-12-31"
```

#### 5️⃣ **Operador IN**

```java
"in:base.id:1:2:3:4:5"               // id in (1,2,3,4,5)
"in:base.estado:activo:pendiente:cancelado"
```

#### 6️⃣ **Operadores NULL**

```java
"isnull:base.parent_id"              // parent_id is null
"isnotnull:base.descripcion"         // descripcion is not null
```

### Joins

```java
// Formato: "tipoJoin:nombreEntidad"
"left:parent"                        // LEFT JOIN parent
"inner:departamento"                 // INNER JOIN departamento
"right:provincia"                    // RIGHT JOIN provincia

// Con multiple joins:
String[] joinTables = {
    "left:parent",
    "left:departamento"
};
```

### Campos y Aliases

```java
// Formato: "tabla.campo as alias"
String fields = "base.id as id, " +
                "base.descripcion as descripcion, " +
                "parent.id as idParent, " +
                "parent.descripcion as descripcionParent";
```

### Ordenamiento

```java
// Formato: "campo:dirección"
String[] mapOrder = {
    "base.descripcion:asc",          // ORDER BY descripcion ASC
    "base.fecha_creacion:desc"       // ORDER BY fecha_creacion DESC
};
```

### Ejemplo Completo de DSL

```java
// Obtener hijos de un padre específico
public Collection<Parametrias> childrensByParents(Long idParent) throws Exception {
    
    // 1. Definir JOIN
    String joinTable = "left:parent";
    
    // 2. Definir FILTROS (WHERE)
    String[] mapFilterField = {
        "=:parent.id:" + idParent,
        "=:base.isPersistente:true"
    };
    
    // 3. Definir CAMPOS
    String fields = "base.id as id, " +
                   "base.descripcion as descripcion, " +
                   "parent.id as idParent, " +
                   "parent.descripcion as descripcionParent";
    
    // 4. Definir ORDEN
    String[] mapOrder = {"base.descripcion:asc"};
    
    // 5. Ejecutar (se traduce internamente a HQL)
    return this.customFieldsJoinFilterAnd(
        fields, joinTable, mapFilterField, mapOrder
    );
}
```

**SQL Generado (equivalente):**
```sql
SELECT 
    base.id as id,
    base.descripcion as descripcion,
    parent.id as idParent,
    parent.descripcion as descripcionParent
FROM Parametrias as base
LEFT JOIN Parametrias.parent as parent
WHERE 1=1
    AND parent.id = :parentId
    AND base.isPersistente = true
ORDER BY base.descripcion ASC
```

## 🔍 Métodos DAO Principales

### CRUD Básico

```java
// Crear/Actualizar
dao.save(entity);
dao.update(entity);

// Leer
Parametrias p = dao.findById(1L);
Collection<Parametrias> todos = dao.allFields();

// Eliminar
dao.delete(entity);
dao.delete(1L);
```

### Consultas Especializadas

```java
// Obtener padres (sin padre)
Collection<Parametrias> padres = dao.parents();

// Obtener padres con paginación
Collection<Parametrias> padres = dao.parents(1, 20);

// Obtener todos los hijos
Collection<Parametrias> hijos = dao.childrens();

// Obtener hijos de un padre específico
Collection<Parametrias> hijosPadre = dao.childrensByParents(5L);

// Contar hijos
Long cantidad = dao.countChildrens(5L);

// Listar con paginación (retorna JSON)
ArrayNode lista = dao.listar(10L, 0L);  // limit=10, offset=0
```

### Consultas con Transformación de Resultados

```java
// ResultTransformer para mapeo personalizado
ResultTransformer rt = new ResultTransformer() {
    @Override
    public Object transformTuple(Object[] os, String[] strings) {
        Parametrias bean = new Parametrias();
        bean.setTransformer(os, strings);  // Mapeo manual
        return bean;
    }

    @Override
    public List transformList(List list) {
        return list;
    }
};

Collection<Parametrias> resultado = dao.customFieldsJoinFilterAnd(
    rt, fields, joinTable, mapFilterField, mapOrder
);
```

## 📊 Entidades Principales

El módulo incluye **60+ entidades**, agrupadas en:

| Categoría | Entidades |
|-----------|-----------|
| **Catastro Base** | Distrito, Provincia, Departamento, Sector, Manzana, Lote, Unidad Catastral |
| **Habilitación** | HabilitacionUrbana, LoteHabilitacionUrbana, ViaHabilitacion |
| **Actividades** | ActividadEconomica, TipoActEconoGeneral, TipoActEconoIntermedio, TipoActEconoEspecifico |
| **Usos de Suelo** | UsoGeneral, UsoIntermedio, UsoEspecifico |
| **Construcciones** | Construcciones, Interiores, OtrasInstalaciones |
| **Administración** | Usuario, Proyecto, Menu, Perfil, Parametrias |
| **Transacciones** | Titularidad, Litigantes, LogsEntityOperation, Documentos |

## 🧪 Ejemplos de Pruebas

### Test de Inserción

```java
@Test
public void createEntity1() throws Exception {
    Parametrias entity = contextEntity.getBean(Parametrias.class);
    LogsEntityOperation logEntity = contextEntity.getBean(LogsEntityOperation.class);
    InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
    
    entity.setIsPersistente(Boolean.TRUE);
    entity.setVersion(new Date().getTime());
    entity.setDescripcion("TITULAR CATASTRAL");
    entity.setMarcaTiempo(LocalDateTime.now(ZoneId.of("America/Lima")));
    
    Transaction tx = dao.getSession().beginTransaction();
    dao.save(entity);
    
    // Registrar operación
    loadLogsEntityOperation(logEntity, entity);
    daoLog.save(logEntity);
    
    tx.commit();
}
```

### Test de Consultas

```java
@Test
public void selectHijosByPadre() throws Exception {
    InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
    Transaction tx = dao.getSession().beginTransaction();
    
    // Obtener hijos del padre con ID=2
    Collection<Parametrias> lista = dao.childrensByParents(2L);
    
    log.info("Total hijos encontrados: {}", lista.size());
    lista.forEach(p -> log.info("- {}: {}", p.getId(), p.getDescripcion()));
    
    tx.commit();
}

@Test
public void selecLimit() throws Exception {
    InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
    Transaction tx = dao.getSession().beginTransaction();
    
    // Listar 10 registros desde el offset 0
    ArrayNode lista = dao.listar(10L, 0L);
    
    log.info("Registros (JSON): {}", lista);
    tx.commit();
}
```

## 🏗️ Arquitectura

```
pg-icl-repository/
├── src/main/java/gob/pe/icl/icl/
│   ├── config/
│   │   └── ConfigDao.java              # Configuración Spring + SessionFactory
│   │       └── ConfigEntity.java
│   ├── dao/
│   │   ├── impl/                       # 60+ DAOs concretos
│   │   │   ├── DaoParametrias.java
│   │   │   ├── DaoDistrito.java
│   │   │   ├── DaoDepartamento.java
│   │   │   └── ... (más DAOs)
│   │   └── inter/                      # 60+ Interfaces DAO
│   │       ├── InterDaoParametrias.java
│   │       ├── InterDaoDistrito.java
│   │       └── ... (más interfaces)
│   └── entity/
│       └── ... (60+ entidades JPA)
├── src/test/java/gob/pe/icl/dao/impl/
│   ├── TestBaseDao.java                # Base para tests
│   ├── TestInsertDaoParametria.java
│   ├── TestSelectDaoParametria.java
│   ├── TestInsertDaoHabilitacionUrbana.java
│   └── ... (más tests)
└── pom.xml
```

## 📝 Patrón Interfaz + Implementación

Cada entidad tiene un patrón consistente:

```java
// 1. Interfaz que extiende InterCrud<T>
public interface InterDaoParametrias extends InterCrud<Parametrias> {
    Collection<Parametrias> parents() throws Exception;
    Collection<Parametrias> childrens() throws Exception;
    Collection<Parametrias> childrensByParents(Long idParent) throws Exception;
    Long countChildrens(Long idParent) throws Exception;
    ArrayNode listar(Long limit, Long offset) throws Exception;
}

// 2. Implementación que extiende AbstractJpaDao<T>
@Repository
public class DaoParametrias extends AbstractJpaDao<Parametrias> 
    implements InterDaoParametrias {
    // Implementar métodos específicos usando DSL
}
```

## 🔧 Manejo de Transacciones

```java
Session session = dao.getSession();
Transaction tx = session.beginTransaction();

try {
    // Operaciones CRUD
    dao.save(entity);
    dao.update(entity);
    
    // Commit si todo OK
    tx.commit();
} catch (Exception e) {
    // Rollback si hay error
    if (tx.isActive()) {
        tx.rollback();
    }
    log.error("Error en transacción: {}", e.getMessage());
}
```

## 📦 Serialización a JSON

El framework auto-convierte resultados a JSON:

```java
// Las consultas Postgres retornan ArrayNode (JSON)
ArrayNode resultados = dao.allFieldsPostgres(
    "icl.catastro.tm_parametrias as base",
    "base.id, base.descripcion, base.abreviatura",
    new String[]{"=:base.is_persistente:true"},
    new String[]{"id:asc"}
);

// resultados es un JsonNode que puede serializar directamente
String json = resultados.toPrettyString();
```

## 🔐 Seguridad y Buenas Prácticas

✅ **Recomendaciones:**
- Use transacciones explícitas para operaciones críticas
- Valide entrada de usuarios antes de usar en filtros
- Implemente auditoría con `LogsEntityOperation`
- Use paginación para resultados grandes
- Cachee SessionFactory (ya implementado con PSF Singleton)

## 🐛 Troubleshooting

### SessionFactory no inicializado
```
if (this.getSessionFactory() == null && sessionFactory != null) {
    this.setSessionFactory(sessionFactory);
}
```

### Excepciones de Transacción
Asegúrese de:
- Abrir transacción antes de modificar datos
- Hacer commit() o rollback() siempre
- Cerrar recursos después de usar

## 📚 Referencias

- [jofrantoba-model-jpa](../README.md) - Framework base
- [Hibernate 6 Docs](https://hibernate.org/orm/documentation/)
- [Jakarta Persistence API](https://jakarta.ee/specifications/persistence/3.1/)
- [Spring Boot 3.3](https://spring.io/projects/spring-boot)

## 👨‍💻 Autor

**jtorresb** - Arquitecto de Persistencia

---

**Última actualización:** Abril 2026  
**Versión:** 1.0.0  
**Estado:** Producción ✅
