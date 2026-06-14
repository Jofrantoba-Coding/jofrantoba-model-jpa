/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;

/**
 * Contrato genérico de operaciones de persistencia (CRUD y consulta avanzada)
 * para una entidad de tipo {@code T}.
 * <p>
 * Define el conjunto completo de operaciones que ofrece la librería sobre una
 * entidad: alta/baja/modificación, búsquedas por clave, consultas con filtros y
 * ordenamiento, <em>joins</em>, paginación, agregados, ejecución de SQL nativo,
 * procedimientos almacenados y exportación de resultados. La implementación de
 * referencia es {@link AbstractJpaDao}.
 *
 * <h2>Convenciones del DSL de cadenas</h2>
 * Muchos métodos aceptan filtros, ordenamientos y <em>joins</em> expresados como
 * cadenas con campos separados por dos puntos ({@code :}). Las convenciones son:
 *
 * <h3>Filtros — {@code "operador:campo[:valor...]"}</h3>
 * <ul>
 *   <li>Comparadores: {@code =}, {@code !=}, {@code >}, {@code <}, {@code >=},
 *       {@code <=} → p.&nbsp;ej. {@code ">:edad:18"}.</li>
 *   <li>{@code equal} / {@code notequal}: igualdad/desigualdad tratando el valor
 *       como texto → {@code "equal:nombre:Juan"}.</li>
 *   <li>{@code like}: coincidencia parcial → {@code "like:nombre:%an%"}.</li>
 *   <li>{@code between}: rango → {@code "between:fecha:1000:2000"}.</li>
 *   <li>{@code in}: pertenencia a lista → {@code "in:id:1:2:3"}.</li>
 *   <li>{@code isnull} / {@code isnotnull}: nulidad → {@code "isnull:fechaBaja"}.</li>
 *   <li>{@code istrue} / {@code isfalse}: valores booleanos.</li>
 * </ul>
 * Todos los valores se enlazan como parámetros (named en HQL, posicionales en
 * SQL nativo), por lo que el DSL es seguro frente a inyección SQL. Los nombres
 * de campo se validan contra una expresión regular de identificadores seguros.
 *
 * <h3>Ordenamiento — {@code "campo:direccion"}</h3>
 * Donde {@code direccion} es {@code asc} o {@code desc} → {@code "nombre:asc"}.
 *
 * <h3>Joins</h3>
 * <ul>
 *   <li><strong>HQL</strong> (métodos {@code *JoinFilter*}):
 *       {@code "tipo:asociacion[:fetch]"} → {@code "left:cliente"}. El {@code tipo}
 *       es {@code inner}/{@code left}/etc.; {@code asociacion} es la propiedad de
 *       navegación de la entidad; el tercer segmento opcional habilita
 *       {@code fetch}.</li>
 *   <li><strong>SQL nativo Postgres</strong> (métodos {@code *Postgres}):
 *       {@code "tipo:tabla:on:colIzq:colDer"} con tipos {@code left}/{@code inner}/
 *       {@code right}/{@code cross}/{@code full}.</li>
 * </ul>
 *
 * @author jona
 * @param <T> tipo de la entidad gestionada; debe ser {@link Serializable}
 * @see AbstractJpaDao
 */
public interface InterCrud<T extends Serializable> {
    /**
     * Recupera una entidad por su clave primaria numérica.
     *
     * @param id valor de la clave primaria
     * @return la entidad encontrada, o {@code null} si no existe
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    T findById(final long id)throws UnknownException;

    /**
     * Recupera una entidad por su clave primaria de tipo {@link String}.
     *
     * @param id valor de la clave primaria
     * @return la entidad encontrada, o {@code null} si no existe
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    T findById(final String id)throws UnknownException;

    /**
     * Persiste la entidad (alta o actualización) mediante {@code merge}.
     * <p>
     * Si la entidad no existe se inserta; si existe, se sincronizan sus cambios.
     *
     * @param entity entidad a guardar; no puede ser {@code null}
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    void save(final T entity)throws UnknownException;

    /**
     * Invoca un procedimiento almacenado de inserción/actualización/borrado que
     * recibe un parámetro de entrada {@code json} (texto) y devuelve un
     * parámetro de salida {@code count} de tipo {@link Long}.
     *
     * @param nameProcedure nombre del procedimiento almacenado
     * @param json          carga JSON a pasar como parámetro de entrada
     * @return el valor del parámetro de salida {@code count} (p.&nbsp;ej. nº de filas afectadas)
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Long iudProcedureJson(String nameProcedure, String json) throws UnknownException;

    /**
     * Ejecuta una consulta SQL nativa (o llamada a procedimiento) cuyos
     * resultados se mapean directamente a la entidad {@code T}, enlazando los
     * parámetros con nombre indicados en el mapa.
     *
     * @param nameProcedure sentencia SQL nativa con parámetros con nombre
     * @param mapParameter  mapa nombre→valor de parámetros a enlazar
     * @return la lista de entidades resultante
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    List<T> listProcedureMsql(String nameProcedure, Map<String, Object> mapParameter) throws UnknownException;

    /**
     * Construye y ejecuta un {@code INSERT} nativo sobre la tabla indicada a
     * partir de descriptores de campo.
     * <p>
     * Cada elemento de {@code fieldValue} tiene el formato
     * {@code "campo:valor:tipo"}, donde {@code tipo} es uno de los soportados
     * por {@link com.jofrantoba.model.jpa.shared.Shared#StringToObject(String, String)}
     * ({@code String}, {@code Date}, {@code Integer}, {@code Double},
     * {@code Long}, {@code BigDecimal}). El nombre de tabla y los campos se
     * validan como identificadores seguros y los valores se enlazan como
     * parámetros.
     *
     * @param table      nombre de la tabla destino
     * @param fieldValue descriptores de campo en formato {@code "campo:valor:tipo"}
     * @return número de filas insertadas
     * @throws UnknownException si ocurre un error de acceso a datos o un identificador no es válido
     */
    int saveNativeQuery(String table,String[] fieldValue)throws UnknownException;

    /**
     * Ejecuta una sentencia DML (INSERT/UPDATE/DELETE) en SQL nativo.
     *
     * @param sql sentencia SQL nativa a ejecutar
     * @return número de filas afectadas
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    int iudNativeQuery(String sql)throws UnknownException;

    /**
     * Actualiza la entidad mediante {@code merge}.
     *
     * @param entity entidad a actualizar; no puede ser {@code null}
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    void update(final T entity)throws UnknownException;

    /**
     * Elimina la entidad dada.
     *
     * @param entity entidad a eliminar; no puede ser {@code null}
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    void delete(final T entity)throws UnknownException;

    /**
     * Elimina la entidad identificada por su clave primaria numérica.
     * <p>
     * Primero la recupera con {@link #findById(long)} y luego la borra; falla si
     * no existe.
     *
     * @param id clave primaria de la entidad a eliminar
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    void delete(final long id)throws UnknownException;

    /**
     * Borra en bloque, mediante HQL, todas las filas que cumplan
     * <em>todos</em> los filtros indicados (unidos con {@code AND}).
     *
     * @param filters filtros en formato DSL (ver {@linkplain InterCrud convenciones})
     * @return número de filas eliminadas
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    int deleteFilterAnd(String[] filters)throws UnknownException;

    /**
     * Sincroniza con la base de datos los cambios pendientes de la sesión actual.
     *
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    void flush()throws UnknownException;

    /**
     * Desvincula (<em>detach</em>) la entidad del contexto de persistencia, de
     * forma que sus cambios posteriores dejen de rastrearse.
     *
     * @param entity entidad a desvincular
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    void detach(T entity)throws UnknownException;

    /**
     * Vacía por completo el contexto de persistencia de la sesión actual,
     * desvinculando todas las entidades gestionadas.
     *
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    void clear()throws UnknownException;

    /**
     * Expulsa (<em>evict</em>) una entidad concreta de la caché de primer nivel.
     *
     * @param entity entidad a expulsar
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    void evict(T entity)throws UnknownException;

    /**
     * Devuelve la {@link Session} actual asociada al hilo.
     *
     * @return la sesión de Hibernate vigente
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Session getSession()throws UnknownException;

    /**
     * Abre y devuelve una {@link Session} nueva e independiente de la del hilo.
     * <p>
     * El llamante es responsable de cerrarla.
     *
     * @return una nueva sesión de Hibernate
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Session getNewSession()throws UnknownException;

    /**
     * Devuelve todas las entidades de tipo {@code T}.
     *
     * @return la colección completa de entidades
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> allFields()throws UnknownException;

    /**
     * Devuelve todas las entidades ordenadas según el mapa campo→dirección.
     *
     * @param mapOrder mapa de campo a dirección ({@code "asc"}/{@code "desc"})
     * @return la colección de entidades ordenada
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> allFields(HashMap<String,String> mapOrder)throws UnknownException;

    /**
     * Devuelve todas las entidades ordenadas según las cláusulas indicadas.
     *
     * @param mapOrder ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de entidades ordenada
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> allFields(String[] mapOrder)throws UnknownException;

    /**
     * Devuelve las entidades que cumplen un único filtro, ordenadas.
     *
     * @param mapFilter filtro en formato DSL (ver {@linkplain InterCrud convenciones})
     * @param mapOrder  ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de entidades filtrada y ordenada
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> allFields(String mapFilter,String[] mapOrder) throws UnknownException;

    /**
     * Devuelve las entidades que cumplen <em>todos</em> los filtros (unidos con
     * {@code AND}), ordenadas. Implementado con la API Criteria de JPA.
     *
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @return la colección filtrada (AND) y ordenada
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> allFieldsFilterAnd(String[] mapFilterField, String[] mapOrder) throws UnknownException;

    /**
     * Devuelve las entidades que cumplen <em>alguno</em> de los filtros (unidos
     * con {@code OR}), ordenadas. Implementado con la API Criteria de JPA.
     *
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @return la colección filtrada (OR) y ordenada
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> allFieldsFilterOr(String[] mapFilterField, String[] mapOrder) throws UnknownException;

    /**
     * Ejecuta una proyección HQL de los campos indicados sobre la entidad.
     *
     * @param fields lista de campos a seleccionar (cláusula {@code select} de HQL)
     * @return la colección de resultados
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> customFields(String fields) throws UnknownException;

    /**
     * Ejecuta una proyección HQL aplicando un {@link ResultTransformer} para
     * transformar cada fila del resultado.
     *
     * @param rt     transformador de resultados a aplicar
     * @param fields lista de campos a seleccionar
     * @return la colección de resultados transformada
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> customFields(ResultTransformer rt,String fields) throws UnknownException;

    /**
     * Proyección HQL de campos con filtros {@code AND} y ordenamiento.
     *
     * @param fields         campos a seleccionar
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de resultados
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> customFieldsFilterAnd(String fields,String[] mapFilterField, String[] mapOrder) throws UnknownException;

    /**
     * Proyección HQL de campos con filtros {@code OR} y ordenamiento.
     *
     * @param fields         campos a seleccionar
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de resultados
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> customFieldsFilterOr(String fields,String[] mapFilterField, String[] mapOrder) throws UnknownException;

    /**
     * Ejecuta una consulta SQL nativa y la devuelve preparada para exportar a
     * Excel: un mapa indexado por número de fila donde la fila {@code 1} es la
     * cabecera (etiquetas de columna) y las siguientes son los datos.
     *
     * @param sql sentencia SQL nativa de consulta
     * @return mapa {@code fila → valores}, con la cabecera en la posición 1
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Map<Integer, Object[]> sqlExportTOExcel(String sql)throws UnknownException;

    /**
     * Consulta con un <em>join</em> HQL y un único filtro, ordenada.
     *
     * @param joinTable     definición del join HQL ({@code "tipo:asociacion[:fetch]"})
     * @param mapJoinFilter filtro en formato DSL
     * @param mapOrder      ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de entidades resultante
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> allFieldsJoinFilter(String joinTable,String mapJoinFilter,String[] mapOrder) throws UnknownException;

    /**
     * Consulta con un <em>join</em> HQL y varios filtros unidos con {@code AND}.
     *
     * @param joinTable definición del join HQL ({@code "tipo:asociacion[:fetch]"})
     * @param mapFilter filtros en formato DSL
     * @param mapOrder  ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de entidades resultante
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> allFieldsJoinFilterAnd(String joinTable,String[] mapFilter, String[] mapOrder) throws UnknownException;

    /**
     * Consulta con <em>varios joins</em> HQL encadenados y filtros {@code AND}.
     * <p>
     * Los joins se encadenan: cada uno navega desde la asociación del anterior.
     *
     * @param joinTable definiciones de join HQL en cadena
     * @param mapFilter filtros en formato DSL
     * @param mapOrder  ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de entidades resultante
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> allFieldsJoinFilterAnd(String[] joinTable,String[] mapFilter, String[] mapOrder) throws UnknownException;

    /**
     * Proyección HQL con join y filtros {@code AND}, mapeando cada fila al DTO
     * indicado mediante alias→propiedad.
     *
     * @param dto            clase destino a la que mapear cada fila
     * @param fields         campos a seleccionar (con alias coincidentes con el DTO)
     * @param joinTable      definición del join HQL
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de DTOs resultante
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<?> customFieldsJoinFilterAnd(Class<?> dto,String fields,String joinTable, String[] mapFilterField, String[] mapOrder) throws UnknownException;

    /**
     * Proyección HQL con join y filtros {@code AND}, mapeada a la propia entidad.
     *
     * @param fields         campos a seleccionar
     * @param joinTable      definición del join HQL
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de resultados
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> customFieldsJoinFilterAnd(String fields,String joinTable, String[] mapFilterField, String[] mapOrder) throws UnknownException;

    /**
     * Proyección HQL con join y filtros {@code AND}, aplicando un
     * {@link ResultTransformer} personalizado.
     *
     * @param rt             transformador de resultados a aplicar
     * @param fields         campos a seleccionar
     * @param joinTable      definición del join HQL
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de resultados transformada
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<?> customFieldsJoinFilterAnd(ResultTransformer rt,String fields,String joinTable, String[] mapFilterField, String[] mapOrder) throws UnknownException;

    /**
     * Cuenta las filas que cumplen los filtros {@code AND}, con un join opcional.
     *
     * @param joinTable      definición del join HQL, o {@code null} para no aplicar join
     * @param mapFilterField filtros en formato DSL
     * @return el número de filas que cumplen las condiciones
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Long rowCountJoinFilterAnd(String joinTable, String[] mapFilterField) throws UnknownException;

    /**
     * Cuenta las filas que cumplen los filtros {@code AND}, con varios joins.
     *
     * @param joinTables     definiciones de join HQL
     * @param mapFilterField filtros en formato DSL
     * @return el número de filas que cumplen las condiciones
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Long rowCountJoinsFilterAnd(String[] joinTables, String[] mapFilterField) throws UnknownException;

    /**
     * Consulta nativa de PostgreSQL con filtros {@code AND}, ordenamiento y
     * paginación por {@code LIMIT}/{@code OFFSET}, devuelta como JSON.
     *
     * @param table          tabla (o vista) de la cláusula {@code from}
     * @param fields         columnas a seleccionar
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @param limit          número máximo de filas a devolver
     * @param offset         número de filas a omitir
     * @return un {@link ArrayNode} con una fila JSON por registro
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    ArrayNode allFieldsLimitOffsetPostgres(String table,String fields,String[] mapFilterField,String[] mapOrder,Long limit, Long offset)throws UnknownException;

    /**
     * Consulta nativa de PostgreSQL con filtros {@code AND} y ordenamiento,
     * devuelta como JSON.
     *
     * @param table          tabla (o vista) de la cláusula {@code from}
     * @param fields         columnas a seleccionar
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @return un {@link ArrayNode} con una fila JSON por registro
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    ArrayNode allFieldsPostgres(String table, String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException;

    /**
     * Proyección HQL con join, filtros {@code AND}, ordenamiento y paginación.
     *
     * @param fields         campos a seleccionar
     * @param joinTable      definición del join HQL
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @param pageNumber     número de página (empezando en 1)
     * @param pageSize       tamaño de página
     * @return la página de resultados
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> customFieldsJoinFilterAnd(String fields, String joinTable, String[] mapFilterField, String[] mapOrder,int pageNumber, int pageSize) throws UnknownException;

    /**
     * Consulta paginada con un único filtro y ordenamiento, vía Criteria.
     *
     * @param mapFilterField filtro en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @param pageNumber     número de página (empezando en 1)
     * @param pageSize       tamaño de página
     * @return la página de entidades
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> allFields(String mapFilterField, String[] mapOrder,int pageNumber, int pageSize) throws UnknownException;

    /**
     * Consulta nativa de PostgreSQL con varios joins, filtros y ordenamiento,
     * devuelta como JSON.
     *
     * @param joinTables                  definiciones de join SQL nativo
     *                                    ({@code "tipo:tabla:on:colIzq:colDer"})
     * @param table                       tabla (o vista) de la cláusula {@code from}
     * @param fields                      columnas a seleccionar
     * @param mapFilterField              filtros en formato DSL
     * @param mapOrder                    ordenamientos en formato {@code "campo:direccion"}
     * @param filterConectorLogicoDefault conector lógico por defecto entre filtros ({@code "and"}/{@code "or"})
     * @return un {@link ArrayNode} con una fila JSON por registro
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    ArrayNode allFieldsJoinPostgres(String[] joinTables,String table, String fields, String[] mapFilterField, String[] mapOrder,String filterConectorLogicoDefault) throws UnknownException;

    /**
     * Consulta nativa de PostgreSQL con joins, filtros {@code AND},
     * agrupamiento y ordenamiento, devuelta como JSON.
     *
     * @param joinTables     definiciones de join SQL nativo
     * @param table          tabla (o vista) de la cláusula {@code from}
     * @param fields         columnas a seleccionar (con funciones de agregado)
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @param groupBy        cláusula {@code group by}
     * @return un {@link ArrayNode} con una fila JSON por grupo
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    ArrayNode allFieldsJoinPostgresGroupBy(String[] joinTables,String table, String fields, String[] mapFilterField, String[] mapOrder,String groupBy) throws UnknownException;

    /**
     * Consulta nativa de PostgreSQL sobre una <em>subconsulta</em> con joins,
     * filtros y agrupamiento, agregando un segundo nivel de agrupamiento,
     * ordenamiento y paginación, devuelta como JSON.
     *
     * @param fields           columnas a seleccionar en la consulta externa
     * @param groupBy          {@code group by} de la consulta externa
     * @param mapOrder         ordenamientos de la consulta externa
     * @param joinTablesSq     joins de la subconsulta
     * @param tableSq          tabla de la subconsulta
     * @param fieldsSq         columnas de la subconsulta
     * @param mapFilterFieldSq filtros (DSL) de la subconsulta
     * @param groupBySq        {@code group by} de la subconsulta
     * @param limit            número máximo de filas a devolver
     * @param offset           número de filas a omitir
     * @return un {@link ArrayNode} con una fila JSON por registro
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    ArrayNode allFieldsLimitJoinPostgresGroupBySubQuery(String fields,String groupBy,String[] mapOrder,String[] joinTablesSq, String tableSq, String fieldsSq, String[] mapFilterFieldSq,String groupBySq,Long limit, Long offset) throws UnknownException;

    /**
     * Igual que {@link #allFieldsLimitJoinPostgresGroupBySubQuery} pero sin
     * paginación (sin {@code LIMIT}/{@code OFFSET}).
     *
     * @param fields           columnas a seleccionar en la consulta externa
     * @param groupBy          {@code group by} de la consulta externa
     * @param mapOrder         ordenamientos de la consulta externa
     * @param joinTablesSq     joins de la subconsulta
     * @param tableSq          tabla de la subconsulta
     * @param fieldsSq         columnas de la subconsulta
     * @param mapFilterFieldSq filtros (DSL) de la subconsulta
     * @param groupBySq        {@code group by} de la subconsulta
     * @return un {@link ArrayNode} con una fila JSON por registro
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    ArrayNode allFieldsJoinPostgresGroupBySubQuery(String fields,String groupBy,String[] mapOrder,String[] joinTablesSq, String tableSq, String fieldsSq, String[] mapFilterFieldSq,String groupBySq) throws UnknownException;

    /**
     * Consulta nativa de PostgreSQL con varios joins, filtros, ordenamiento y
     * paginación por {@code LIMIT}/{@code OFFSET}, devuelta como JSON.
     *
     * @param joinTables                  definiciones de join SQL nativo
     * @param table                       tabla (o vista) de la cláusula {@code from}
     * @param fields                      columnas a seleccionar
     * @param mapFilterField              filtros en formato DSL
     * @param mapOrder                    ordenamientos en formato {@code "campo:direccion"}
     * @param limit                       número máximo de filas a devolver
     * @param offset                      número de filas a omitir
     * @param filterConectorLogicoDefault conector lógico por defecto entre filtros ({@code "and"}/{@code "or"})
     * @return un {@link ArrayNode} con una fila JSON por registro
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    ArrayNode allFieldsJoinLimitOffsetPostgres(String[] joinTables,String table, String fields, String[] mapFilterField, String[] mapOrder, Long limit, Long offset,String filterConectorLogicoDefault) throws UnknownException;

    /**
     * Proyección HQL con varios joins y filtros {@code AND}, aplicando un
     * {@link ResultTransformer} personalizado.
     *
     * @param rt             transformador de resultados a aplicar
     * @param fields         campos a seleccionar
     * @param joinTables     definiciones de join HQL
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de resultados transformada
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<?> customFieldsJoinFilterAnd(ResultTransformer rt, String fields, String[] joinTables, String[] mapFilterField, String[] mapOrder) throws UnknownException;

    /**
     * Calcula un agregado HQL (p.&nbsp;ej. {@code sum}, {@code count}) con un
     * join opcional, filtros {@code AND} y agrupamiento.
     *
     * @param fields         expresión de agregado a seleccionar
     * @param joinTable      definición del join HQL, o {@code null} para no aplicar join
     * @param mapFilterField filtros en formato DSL
     * @param groupBy        cláusula {@code group by}, o {@code null}
     * @return el valor agregado como {@link Long}
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Long aggregateJoinFilterAndGroupBy(String fields,String joinTable, String[] mapFilterField,String groupBy) throws UnknownException;

    /**
     * Proyección HQL con filtros {@code AND} y ordenamiento, mapeando cada fila
     * al DTO indicado.
     *
     * @param dto            clase destino a la que mapear cada fila
     * @param fields         campos a seleccionar (con alias coincidentes con el DTO)
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de DTOs resultante
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<?> customFieldsFilterAnd(Class<?> dto,String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException;

    /**
     * Obtiene el valor máximo ({@code max}) de un campo con un join opcional y
     * filtros {@code AND}.
     *
     * @param field          campo del que obtener el máximo
     * @param joinTable      definición del join HQL, o {@code null} para no aplicar join
     * @param mapFilterField filtros en formato DSL
     * @return el valor máximo del campo
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Object maxValueJoinFilterAnd(String field,String joinTable, String[] mapFilterField)throws UnknownException;

    /**
     * Proyección HQL con varios joins encadenados y filtros {@code AND},
     * mapeada a la propia entidad.
     *
     * @param fields         campos a seleccionar
     * @param joinTables     definiciones de join HQL
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @return la colección de resultados
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> customFieldsJoinFilterAnd(String fields, String[] joinTables, String[] mapFilterField, String[] mapOrder) throws UnknownException;

    /**
     * Proyección HQL con varios joins, filtros {@code AND}, ordenamiento y
     * paginación, mapeada a la propia entidad.
     *
     * @param fields         campos a seleccionar
     * @param joinTables     definiciones de join HQL
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @param pageNumber     número de página (empezando en 1)
     * @param pageSize       tamaño de página
     * @return la página de resultados
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> customFieldsJoinFilterAnd(String fields, String[] joinTables, String[] mapFilterField, String[] mapOrder, int pageNumber, int pageSize) throws UnknownException;

    /**
     * Consulta paginada con un join HQL y un único filtro, ordenada.
     *
     * @param joinTable  definición del join HQL
     * @param mapFilter  filtro en formato DSL
     * @param mapOrder   ordenamientos en formato {@code "campo:direccion"}
     * @param pageNumber número de página (empezando en 1)
     * @param pageSize   tamaño de página
     * @return la página de entidades
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> allFieldsJoinFilter(String joinTable, String mapFilter, String[] mapOrder,int pageNumber, int pageSize) throws UnknownException;

    /**
     * Consulta paginada con un join HQL y filtros {@code AND}, ordenada.
     *
     * @param joinTable  definición del join HQL
     * @param mapFilter  filtros en formato DSL
     * @param mapOrder   ordenamientos en formato {@code "campo:direccion"}
     * @param pageNumber número de página (empezando en 1)
     * @param pageSize   tamaño de página
     * @return la página de entidades
     * @throws UnknownException si ocurre un error de acceso a datos
     */
    Collection<T> allFieldsJoinFilterAnd(String joinTable, String[] mapFilter, String[] mapOrder, int pageNumber, int pageSize) throws UnknownException;

    /**
     * Construye y devuelve (sin ejecutarla) la sentencia SQL nativa de una
     * consulta Postgres con joins, filtros y ordenamiento.
     * <p>
     * Método de apoyo/depuración para inspeccionar el SQL generado.
     *
     * @param joinTables     definiciones de join SQL nativo
     * @param table          tabla de la cláusula {@code from}
     * @param fields         columnas a seleccionar
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @return el SQL generado como {@link StringBuilder}
     */
    StringBuilder strAllFieldsJoinPostgres(String[] joinTables, String table, String fields, String[] mapFilterField, String[] mapOrder);

    /**
     * Construye y devuelve (sin ejecutarla) la sentencia SQL nativa de una
     * consulta Postgres con joins, filtros, agrupamiento y ordenamiento.
     * <p>
     * Método de apoyo/depuración para inspeccionar el SQL generado.
     *
     * @param joinTables     definiciones de join SQL nativo
     * @param table          tabla de la cláusula {@code from}
     * @param fields         columnas a seleccionar
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @param groupBy        cláusula {@code group by}
     * @return el SQL generado como {@link StringBuilder}
     */
    StringBuilder strAllFieldsJoinPostgresGroupBy(String[] joinTables, String table, String fields, String[] mapFilterField, String[] mapOrder, String groupBy);

    /**
     * Construye y devuelve (sin ejecutarla) la sentencia SQL nativa de una
     * consulta Postgres con joins, filtros, agrupamiento, ordenamiento y
     * paginación por {@code LIMIT}/{@code OFFSET}.
     * <p>
     * Método de apoyo/depuración para inspeccionar el SQL generado.
     *
     * @param joinTables     definiciones de join SQL nativo
     * @param table          tabla de la cláusula {@code from}
     * @param fields         columnas a seleccionar
     * @param mapFilterField filtros en formato DSL
     * @param mapOrder       ordenamientos en formato {@code "campo:direccion"}
     * @param groupBy        cláusula {@code group by}
     * @param limit          número máximo de filas a devolver
     * @param offset         número de filas a omitir
     * @return el SQL generado como {@link StringBuilder}
     */
    StringBuilder strAllFieldsJoinGroupByLimitOffsetPostgres(String[] joinTables, String table, String fields, String[] mapFilterField, String[] mapOrder, String groupBy, Long limit, Long offset);
}
