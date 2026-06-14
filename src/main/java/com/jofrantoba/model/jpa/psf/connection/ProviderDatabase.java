/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.psf.connection;

/**
 * Catálogo de los motores de base de datos soportados por la librería.
 * <p>
 * Cada constante asocia un nombre legible del proveedor con el nombre
 * totalmente cualificado de la clase del driver JDBC correspondiente. Las
 * subclases de {@link AbstractConnectionProperties} usan estos valores para
 * rellenar la propiedad {@code jakarta.persistence.jdbc.driver} y construir la
 * URL de conexión adecuada para cada motor.
 *
 * @author jona
 */
public enum ProviderDatabase {
    /** Oracle Database (driver {@code oracle.jdbc.driver.OracleDriver}). */
    ORACLE("Oracle","oracle.jdbc.driver.OracleDriver"),
    /** PostgreSQL (driver {@code org.postgresql.Driver}). */
    POSTGRES("Postgres","org.postgresql.Driver"),
    /** MySQL 8+ (driver {@code com.mysql.cj.jdbc.Driver}). */
    MYSQL("MySql","com.mysql.cj.jdbc.Driver"),
    /** Microsoft SQL Server (driver {@code com.microsoft.sqlserver.jdbc.SQLServerDriver}). */
    SQLSERVER("SqlServer","com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    /** HyperSQL / HSQLDB, útil para pruebas en memoria (driver {@code org.hsqldb.jdbc.JDBCDriver}). */
    HYPERSQL("HyperSql","org.hsqldb.jdbc.JDBCDriver");

    /** Nombre legible del proveedor (p.&nbsp;ej. {@code "Postgres"}). */
    private String nameProvider;
    /** Nombre totalmente cualificado de la clase del driver JDBC. */
    private String driver;

    /**
     * Crea una constante del enum asociando el nombre del proveedor con su driver.
     *
     * @param nameProvider nombre legible del motor de base de datos
     * @param driver       clase del driver JDBC totalmente cualificada
     */
    private ProviderDatabase(String nameProvider,String driver){
        this.nameProvider=nameProvider;
        this.driver=driver;
    }

    /**
     * Devuelve el nombre legible del proveedor.
     *
     * @return el nombre del motor (p.&nbsp;ej. {@code "Oracle"}, {@code "Postgres"})
     */
    public String getNameProvider() {
        return nameProvider;
    }

    /**
     * Devuelve la clase del driver JDBC asociada a este proveedor.
     *
     * @return nombre totalmente cualificado de la clase del driver JDBC
     */
    public String getDriver() {
        return driver;
    }


}
