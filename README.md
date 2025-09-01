# jofrantoba-model-jpa

Proyecto Java para acceso a datos genérico y flexible usando JPA/Hibernate, con soporte para múltiples motores de base de datos (MySQL, Oracle, PostgreSQL).

## Características principales
- Arquitectura DAO genérica basada en JPA/Hibernate.
- Métodos CRUD y consultas avanzadas (joins, filtros, group by, paginación, ordenamiento).
- Soporte para consultas nativas y exportación de resultados a Excel.
- Configuración desacoplada para diferentes motores de base de datos.
- Utilidades para construcción dinámica de sentencias SQL/HQL.
- Manejo centralizado de excepciones personalizadas.
- Pruebas unitarias organizadas por motor de base de datos.

## Estructura del proyecto
```
src/
	main/
		java/
			com/jofrantoba/model/jpa/
				daoentity/        # DAOs genéricos y entidades
				psf/              # Configuración y utilidades de conexión
				shared/           # Utilidades y excepciones
		resources/
			log4j2.xml          # Configuración de logging
	test/
		java/
			com/jofrantoba/model/jpa/daoentity/test/  # Pruebas unitarias
```

## Clases principales
- **AbstractJpaDao<T>**: DAO genérico con métodos CRUD y consultas avanzadas.
- **InterCrud<T>**: Interfaz que define operaciones CRUD y de consulta.
- **PSF**: Singleton para gestión de `SessionFactory` y configuración de conexiones.
- **ConnectionProperties[Motor]**: Configuración específica para cada base de datos.
- **Shared**: Utilidades para manipulación de SQL y cadenas.
- **UnknownException**: Excepción personalizada para manejo de errores.

## Ejemplo de uso
```java
// Definir un DAO concreto extendiendo AbstractJpaDao
public class EmpleadoDao extends AbstractJpaDao<Empleado> {
	public EmpleadoDao() {
		setClazz(Empleado.class);
	}
}

// Configuración de la SessionFactory (ejemplo simplificado)
SessionFactory sessionFactory = PSF.getInstance().buildPSF("mysql", new ConnectionPropertiesMysql(), List.of("com.jofrantoba.model.jpa.daoentity"));

// Uso en aplicación
EmpleadoDao dao = new EmpleadoDao();
dao.setSessionFactory(sessionFactory);

// Operaciones CRUD
Empleado emp = new Empleado();
emp.setNombre("Juan");
dao.save(emp);

Empleado encontrado = dao.findById(emp.getId());
encontrado.setNombre("Juan Actualizado");
dao.update(encontrado);

dao.delete(encontrado);
```

## Pruebas
Las pruebas unitarias están organizadas por motor de base de datos en `src/test/java/com/jofrantoba/model/jpa/daoentity/test/`.

## Dependencias principales
- Java 8+
- Hibernate Core 5.4.32.Final
- Hibernate c3p0 5.5.3.Final
- JPA (implementado vía Hibernate)
- Lombok 1.18.12
- JUnit 5.6.2
- log4j2 2.13.3

## Configuración
La configuración de conexión se realiza mediante las clases `ConnectionProperties[Motor]` y el singleton `PSF`. Ver ejemplos en las pruebas unitarias.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Puedes usarlo, modificarlo y distribuirlo libremente.

Consulta el archivo LICENSE para más detalles.