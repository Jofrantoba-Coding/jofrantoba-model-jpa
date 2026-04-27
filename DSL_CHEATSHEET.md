# 🔤 DSL Cheat Sheet - jofrantoba-model-jpa

Referencia rápida para usar el **Domain Specific Language (DSL)** del framework.

## ⚡ Uso Rápido

```java
// El DSL es una cadena separada por ":"
"operador:campo:valor"

// Se usa en arrays de String
String[] filtros = {"=:edad:30", ">:salario:5000"};

// Se pasa a métodos del DAO
Collection<T> resultados = dao.customFieldsFilterAnd(
    "campo1, campo2",              // SELECT
    filtros,                        // WHERE con DSL
    new String[]{"campo1:asc"}      // ORDER BY
);
```

---

## 📋 Operadores del DSL

### Comparación Numérica/Valor

| Operador | Ejemplo | SQL Equivalente |
|----------|---------|-----------------|
| `=` | `"=:edad:30"` | `edad = 30` |
| `!=` | `"!=:estado:activo"` | `estado != 'activo'` |
| `>` | `">:salario:5000"` | `salario > 5000` |
| `<` | `"<:categoria:5"` | `categoria < 5` |
| `>=` | `">=:edad:18"` | `edad >= 18` |
| `<=` | `"<=:precio:1000"` | `precio <= 1000` |

### Comparación de Texto (con comillas)

| Operador | Ejemplo | SQL Equivalente |
|----------|---------|-----------------|
| `equal` | `"equal:ciudad:Madrid"` | `ciudad = 'Madrid'` |
| `notequal` | `"notequal:estado:INACTIVO"` | `estado != 'INACTIVO'` |
| `like` | `"like:nombre:%Juan%"` | `nombre LIKE '%Juan%'` |

### Operadores de Rango y Colecciones

| Operador | Ejemplo | SQL Equivalente |
|----------|---------|-----------------|
| `between` | `"between:edad:18:65"` | `edad BETWEEN 18 AND 65` |
| `in` | `"in:id:1:2:3:4"` | `id IN (1,2,3,4)` |

### Operadores NULL

| Operador | Ejemplo | SQL Equivalente |
|----------|---------|-----------------|
| `isnull` | `"isnull:fecha_baja"` | `fecha_baja IS NULL` |
| `isnotnull` | `"isnotnull:email"` | `email IS NOT NULL` |

---

## 🔗 JOINs

### Sintaxis Básica
```java
// Un JOIN
String joinTable = "left:nombreEntidad";

// Múltiples JOINs
String[] joinTables = {
    "left:parent",
    "inner:departamento",
    "right:provincia"
};
```

### Tipos de JOIN

| Tipo | Uso de DSL | SQL |
|------|------------|-----|
| LEFT | `"left:tabla"` | LEFT JOIN tabla |
| INNER | `"inner:tabla"` | INNER JOIN tabla |
| RIGHT | `"right:tabla"` | RIGHT JOIN tabla |

---

## 📊 Campos y Aliases

```java
// Sintaxis: "tabla.campo as alias"

String fields = 
    "base.id as id, " +
    "base.nombre as nombre, " +
    "base.email as email, " +
    "departamento.nombre as nombre_departamento, " +
    "COUNT(*) as total";
```

---

## 📈 Ordenamiento

```java
// Sintaxis: "campo:dirección"

String[] orden = {
    "base.nombre:asc",              // ORDER BY nombre ASC
    "base.fecha_creacion:desc",     // , fecha_creacion DESC
    "base.id:asc"
};
```

---

## 🔍 Ejemplos Prácticos

### Ejemplo 1: Búsqueda Simple

```java
String[] filtros = {"=:estado:activo"};
String[] orden = {"apellido:asc"};
String fields = "id as id, nombre as nombre, email as email";

Collection<Usuario> usuarios = dao.customFieldsFilterAnd(
    fields, filtros, orden
);
```

**SQL Generado:**
```sql
SELECT id, nombre, email 
FROM Usuario base 
WHERE estado = 'activo'
ORDER BY apellido ASC
```

---

### Ejemplo 2: Múltiples Filtros (AND)

```java
String[] filtros = {
    "=:base.activo:true",
    ">:base.salario:30000",
    "<:base.salario:100000",
    "like:base.nombre:%Juan%"
};
String[] orden = {"base.salario:desc"};

Collection<Empleado> resultados = dao.customFieldsFilterAnd(
    "base.id, base.nombre, base.salario", filtros, orden
);
```

**SQL Generado:**
```sql
SELECT base.id, base.nombre, base.salario
FROM Empleado base
WHERE 1=1
  AND base.activo = true
  AND base.salario > 30000
  AND base.salario < 100000
  AND base.nombre LIKE '%Juan%'
ORDER BY base.salario DESC
```

---

### Ejemplo 3: JOIN y Filtros

```java
String joinTable = "left:departamento";
String[] filtros = {
    "=:base.activo:true",
    "isnotnull:departamento.id"
};
String fields = 
    "base.id as id, " +
    "base.nombre as nombre, " +
    "departamento.nombre as departamento";
String[] orden = {"base.nombre:asc"};

Collection<Empleado> resultados = dao.customFieldsJoinFilterAnd(
    fields, joinTable, filtros, orden
);
```

**SQL Generado:**
```sql
SELECT 
  base.id as id,
  base.nombre as nombre,
  departamento.nombre as departamento
FROM Empleado base
LEFT JOIN departamento ON base.departamento_id = departamento.id
WHERE 1=1
  AND base.activo = true
  AND departamento.id IS NOT NULL
ORDER BY base.nombre ASC
```

---

### Ejemplo 4: Relaciones Jerárquicas

```java
// Obtener hijos de un nodo padre específico
String joinTable = "left:parent";
String[] filtros = {
    "=:parent.id:5",              // Parent ID = 5
    "=:base.active:true"          // Y activos
};
String fields =
    "base.id as id, " +
    "base.name as name, " +
    "parent.id as parentId, " +
    "parent.name as parentName";
String[] orden = {"base.name:asc"};

Collection<Category> children = dao.customFieldsJoinFilterAnd(
    fields, joinTable, filtros, orden
);
```

---

### Ejemplo 5: Rango e IN

```java
String[] filtros = {
    "between:fecha_creacion:2025-01-01:2025-12-31",
    "in:categoria:A:B:C"
};

Collection<Producto> productos = dao.customFieldsFilterAnd(
    "id, nombre, categoria, precio", filtros, null
);
```

**SQL Generado:**
```sql
SELECT id, nombre, categoria, precio
FROM Producto base
WHERE 1=1
  AND fecha_creacion BETWEEN 2025-01-01 AND 2025-12-31
  AND categoria IN ('A','B','C')
```

---

### Ejemplo 6: Paginación

```java
String table = "empleados as base";
String fields = "base.id, base.nombre, base.email";
String[] filtros = {"=:base.activo:true"};
String[] orden = {"base.nombre:asc"};

long limit = 10;      // 10 registros por página
long offset = 0;      // Página 1: offset = 0

ArrayNode resultado = dao.allFieldsLimitOffsetPostgres(
    table, fields, filtros, orden, limit, offset
);
```

**Para página 2:** `offset = 10` (1 * 10)  
**Para página 3:** `offset = 20` (2 * 10)

---

### Ejemplo 7: COUNT y Agregaciones

```java
String[] filtros = {
    "=:empleados.departamento_id:5"
};

Long total = dao.rowCountJoinFilterAnd("left:departamento", filtros);
log.info("Total empleados en departamento 5: {}", total);
```

---

### Ejemplo 8: NULL vs NOT NULL

```java
// CORRECTO - Usar isnull / isnotnull
String[] filtrosOK = {
    "isnull:fecha_baja",                // fecha_baja IS NULL
    "isnotnull:fecha_contratacion"      // fecha_contratacion IS NOT NULL
};

// INCORRECTO - No usar "=" con NULL
String[] filtrosError = {
    "=:fecha_baja:null"                 // ❌ NO funciona
};
```

---

## 💡 Tips de Uso

### 1. Alias en WHERE con PostgreSQL
```java
// Usar tabla.campo en WHERE
String[] filtros = {"=:base.estado:activo"};  // OK

// NO usar alias en WHERE
String[] filtros = {"=:estado:activo"};       // ❌ Podría fallar
```

### 2. Múltiples JOINs
```java
String[] joinTables = {
    "left:departamento",    // Primer JOIN
    "inner:empresa"         // Segundo JOIN
};
```

### 3. LIKE con Patrones
```java
"like:nombre:%Juan%"         // Contiene Juan
"like:apellido:Garcia%"      // Comienza con Garcia
"like:codigo:%ICL%"          // Contiene ICL
```

### 4. IN con Muchos Valores
```java
"in:id:1:2:3:4:5:6:7:8:9:10"   // Múltiples valores separados por :
```

### 5. Ordering Complejo
```java
String[] orden = {
    "departamento:asc",     // Primera prioridad
    "apellido:asc",         // Segunda prioridad
    "nombre:asc"            // Tercera prioridad
};
```

---

## 🚫 Errores Comunes

| Error | Causa | Arreglar |
|-------|-------|---------|
| `NullPointerException` en filtro | Comparador `:` faltante | `"=:campo:valor"` no `"=campo:valor"` |
| Resultados inesperados | Filtros desconocidos | Usar operadores válidos de la tabla |
| `LazyInitializationException` | Acceso a relación fuera de tx | Usar `@Transactional` o Eager Loading |
| `ClassCastException` | Tipo retornado incorrecto | Verificar clase genérica `<T>` |
| Caracteres especiales en LIKE | Escaping faltante | Usar comillas: `"%juan%"` |

---

## 📚 Métodos que Soportan DSL

```java
// Todos estos soportan DSL:
- customFieldsFilterAnd(fields, filtros, orden)
- customFieldsFilterOr(fields, filtros, orden)
- customFieldsJoinFilterAnd(fields, join, filtros, orden)
- allFieldsJoinPostgres(joins, table, fields, filtros, orden)
- allFieldsJoinPostgresGroupBy(joins, table, fields, filtros, orden, groupBy)
- allFieldsLimitOffsetPostgres(table, fields, filtros, orden, limit, offset)
- rowCountJoinFilterAnd(join, filtros)
```

---

## 🔗 Ver También

- [README.md](README.md) - Documentación completa
- [Documentación web](https://jofrantoba.github.io/jofrantoba-model-jpa/dsl-guide) - Guía DSL online con ejemplos adicionales
