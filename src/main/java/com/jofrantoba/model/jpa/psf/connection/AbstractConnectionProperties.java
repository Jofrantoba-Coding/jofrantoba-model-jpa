/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.psf.connection;

import java.io.Serializable;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * Base común para las clases que describen los parámetros de conexión a una
 * base de datos concreta.
 * <p>
 * Centraliza los atributos compartidos por todos los motores (host, puerto,
 * nombre de la base, credenciales, URL JDBC, driver, proveedor y estrategia de
 * pool) y deja a cada subclase la responsabilidad de construir la URL adecuada
 * e implementar {@link #getProperties()} con el dialecto y ajustes propios del
 * motor.
 * <p>
 * Gracias a Lombok ({@link lombok.Data @Data}) se generan automáticamente los
 * <em>getters</em>, <em>setters</em>, {@code equals}, {@code hashCode} y
 * {@code toString} de todos los campos.
 *
 * @author jona
 * @see ConnectionPropertiesPostgre
 * @see ConnectionPropertiesMysql
 * @see ConnectionPropertiesOracle
 * @see ConnectionPropertiesSqlServer
 */

@Log4j2
@Data
public abstract class AbstractConnectionProperties implements ConnectionProperties,Serializable{
    /** Nombre o IP del servidor de base de datos. */
    private String host;
    /** Puerto TCP en el que escucha el motor de base de datos. */
    private int port;
    /** Nombre del esquema / base de datos al que conectarse. */
    private String nameDatabase;
    /** Usuario con el que autenticarse contra la base de datos. */
    private String userDatabase;
    /** Contraseña del usuario de base de datos. */
    private String passwordDatabase;
    /** URL JDBC completa, construida por la subclase a partir de host/puerto/base. */
    private String urlConnection;
    /** Clase del driver JDBC totalmente cualificada. */
    private String driver;
    /** Nombre legible del proveedor de base de datos (ver {@link ProviderDatabase}). */
    private String providerDatabase;
    /** Estrategia de pool de conexiones a aplicar; por defecto {@link ConnectionPool#C3P0}. */
    private ConnectionPool connectionPool = ConnectionPool.C3P0;
}
