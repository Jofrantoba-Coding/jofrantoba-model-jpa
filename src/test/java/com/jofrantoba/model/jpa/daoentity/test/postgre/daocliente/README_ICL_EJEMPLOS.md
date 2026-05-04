# Ejemplos de Pruebas ICL para PostgreSQL

Este directorio contiene ejemplos de pruebas completas para realizar operaciones CRUD (Create, Read, Update, Delete) en la base de datos PostgreSQL con la entidad Cliente.

## Estructura de Ejemplos

### TestInsertClienteIcl.java
Ejemplos para insertar clientes en la base de datos:
- `insertClienteTitular()` - Inserta un cliente de ejemplo con rol de titular
- `insertClienteRepresentante()` - Inserta un cliente con rol de representante legal
- `insertClienteContacto()` - Inserta un cliente como persona de contacto
- `insertMultiplesClientes()` - Inserta múltiples clientes en una sola transacción

### TestSelectClienteIcl.java
Ejemplos para consultar clientes de la base de datos:
- `findAllClientes()` - Obtiene todos los clientes registrados
- `findClienteById()` - Busca un cliente específico por ID
- `findClientesByNombre()` - Busca clientes por nombre
- `findClientesByTipoDocumento()` - Busca clientes por tipo de documento
- `countClientes()` - Cuenta el total de clientes

### TestUpdateClienteIcl.java
Ejemplos para actualizar clientes:
- `updateClienteCargo()` - Actualiza el cargo de un cliente
- `updateClienteSalario()` - Actualiza el salario de un cliente
- `updateClienteDatos()` - Actualiza múltiples datos de un cliente
- `marcarClienteInactivo()` - Marca un cliente como inactivo
- `incrementarSalarioSupervisores()` - Incrementa salario a todos los supervisores

### TestDeleteClienteIcl.java
Ejemplos para eliminar clientes:
- `deleteClienteById()` - Elimina un cliente específico
- `deleteMultiplesClientes()` - Elimina varios clientes
- `deleteClientesInactivos()` - Elimina todos los clientes inactivos
- `deleteClientesPorSalario()` - Elimina clientes con salario menor a un monto
- `truncateTablaClientes()` - Elimina todos los clientes

## Requisitos

1. **Base de datos PostgreSQL** ejecutándose en `localhost:5432`
2. **Base de datos ICL** creada con:
   - Usuario: `jofrantoba`
   - Contraseña: `J0fr4nt0b4`
   - Catálogo: `catastro`
   - Schema: `catastro`
   - Tabla: `cliente`

3. **Dependencias del proyecto**:
   - Hibernate 6.5.2
   - Jakarta Persistence API 3.2.0
   - PostgreSQL Driver 42.7.2

## Estructura de la tabla cliente

```sql
CREATE TABLE catastro.cliente (
    id BIGSERIAL PRIMARY KEY,
    nombres VARCHAR(255),
    apellidos VARCHAR(255),
    tipo_documento VARCHAR(50),
    numero_documento VARCHAR(50),
    fecha_nacimiento DATE,
    fecha_vinculacion DATE,
    cargo VARCHAR(255),
    salario DECIMAL(10,2),
    is_persistente BOOLEAN,
    version BIGINT
);
```

## Cómo ejecutar los ejemplos

### Ejecutar un test específico
```bash
mvn test -Dtest=TestInsertClienteIcl#insertClienteTitular
```

### Ejecutar todos los tests de inserción
```bash
mvn test -Dtest=TestInsertClienteIcl
```

### Ejecutar todos los tests de ICL PostgreSQL
```bash
mvn test -Dtest=TestInsertClienteIcl,TestSelectClienteIcl,TestUpdateClienteIcl,TestDeleteClienteIcl
```

### Ejecutar todos los tests del proyecto
```bash
mvn clean test
```

## Datos de Ejemplo

Los ejemplos de inserción crean clientes con la siguiente información:

### Cliente 1 - Titular
- Nombres: Juan
- Apellidos: Pérez García
- DNI: 12345678
- Cargo: Titular Catastral
- Salario: $3,500.00

### Cliente 2 - Representante
- Nombres: María
- Apellidos: López Rodríguez
- DNI: 87654321
- Cargo: Representante Legal
- Salario: $4,200.00

### Cliente 3 - Contacto
- Nombres: Carlos
- Apellidos: Martínez Sánchez
- DNI: 11223344
- Cargo: Persona de Contacto
- Salario: $2,800.00

## Configuración de Conexión

La conexión a PostgreSQL se configura en la clase con los siguientes parámetros:

```java
ConnectionPropertiesPostgre cnx = new ConnectionPropertiesPostgre(
    "localhost",        // Host
    5432,              // Puerto
    "icl",             // Base de datos
    "jofrantoba",      // Usuario
    "J0fr4nt0b4"       // Contraseña
);
```

Para cambiar los parámetros, edita el método `getCnx()` en cada clase de test.

## Manejo de Transacciones

Todos los ejemplos siguen el patrón de transacciones:

```java
Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
try {
    // Operaciones
    tx.commit();
} catch (Exception e) {
    tx.rollback();
    log.error("Error", e);
}
```

## Logs

Los ejemplos utilizan Log4j2 para registrar las operaciones. Configura el nivel de log en `log4j2.xml`:

```xml
<Logger name="com.jofrantoba" level="info"/>
```

## Notas Importantes

1. **Orden de ejecución**: Se recomienda ejecutar primero `TestInsertClienteIcl` para crear datos, luego `TestSelectClienteIcl`, `TestUpdateClienteIcl` y finalmente `TestDeleteClienteIcl`.

2. **Base de datos limpia**: Los ejemplos de eliminación pueden limpiar la base de datos. Usa `truncateTablaClientes()` con cuidado.

3. **Datos persistentes**: Los datos insertados permanecerán en la base de datos hasta que sean eliminados explícitamente.

4. **Validación de datos**: Verifica que los datos existan antes de ejecutar operaciones de actualización o eliminación.

## Troubleshooting

### Error: Connection refused
- Verifica que PostgreSQL esté ejecutándose en `localhost:5432`
- Comprueba que la base de datos `icl` existe

### Error: Authentication failed
- Verifica las credenciales en el método `getCnx()`
- Asegúrate de que el usuario `jofrantoba` tiene permisos en la base de datos

### Error: Table not found
- Verifica que la tabla `cliente` existe en el schema `catastro`
- Comprueba que tienes permisos para leer/escribir en la tabla

## Recursos Adicionales

- [Documentación de Hibernate](https://hibernate.org/orm/documentation/)
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/)
- [Jakarta Persistence API](https://jakarta.ee/specifications/persistence/)
