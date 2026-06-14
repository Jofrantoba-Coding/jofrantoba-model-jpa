/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.psf.connection;

import java.util.Properties;
import org.hibernate.cfg.Environment;

/**
 * Parámetros de conexión específicos para <strong>Microsoft SQL Server</strong>.
 * <p>
 * Construye la URL JDBC con el formato
 * {@code jdbc:sqlserver://host:puerto;databasename=base;encrypt=true;trustServerCertificate=true;}
 * y fija el driver y proveedor de {@link ProviderDatabase#SQLSERVER}. El método
 * {@link #getProperties()} añade los ajustes de Hibernate (codificación UTF-8,
 * nivel de aislamiento {@code READ_COMMITTED}, manejo de conexión diferido por
 * transacción) y aplica la estrategia de pool seleccionada.
 *
 * @author apoyo1953
 */
public class ConnectionPropertiesSqlServer extends AbstractConnectionProperties{

    /**
     * Crea la configuración de conexión a SQL Server usando el pool por
     * defecto ({@link ConnectionPool#C3P0}).
     *
     * @param host             servidor de base de datos (host o IP)
     * @param port             puerto del servidor SQL Server (por defecto 1433)
     * @param nameDatabase     nombre de la base de datos
     * @param userDatabase     usuario de conexión
     * @param passwordDatabase contraseña del usuario
     */
    public ConnectionPropertiesSqlServer(
            String host,
            int port,
            String nameDatabase,
            String userDatabase,
            String passwordDatabase){
        this(host, port, nameDatabase, userDatabase, passwordDatabase, ConnectionPool.C3P0);
    }

    /**
     * Crea la configuración de conexión a SQL Server indicando explícitamente
     * la estrategia de pool de conexiones.
     *
     * @param host             servidor de base de datos (host o IP)
     * @param port             puerto del servidor SQL Server (por defecto 1433)
     * @param nameDatabase     nombre de la base de datos
     * @param userDatabase     usuario de conexión
     * @param passwordDatabase contraseña del usuario
     * @param pool             estrategia de pool de conexiones a utilizar
     */
    public ConnectionPropertiesSqlServer(
            String host,
            int port,
            String nameDatabase,
            String userDatabase,
            String passwordDatabase,
            ConnectionPool pool){
        super.setHost(host);
        super.setPort(port);
        super.setNameDatabase(nameDatabase);
        super.setUserDatabase(userDatabase);
        super.setPasswordDatabase(passwordDatabase);    
        super.setDriver(ProviderDatabase.SQLSERVER.getDriver());
        super.setProviderDatabase(ProviderDatabase.SQLSERVER.getNameProvider());
        super.setUrlConnection("jdbc:sqlserver://"+host+":"+port+";databasename="+nameDatabase+";encrypt=true;trustServerCertificate=true;");        
        super.setConnectionPool(pool);        
    }
    
    /**
     * Construye las propiedades de Hibernate para SQL Server: driver, URL,
     * credenciales, visualización de SQL, contexto de sesión por hilo,
     * codificación UTF-8 y nivel de aislamiento {@code READ_COMMITTED} (2).
     * Finalmente aplica la estrategia de pool configurada.
     *
     * @return las propiedades de conexión listas para Hibernate
     */
    @Override
    public Properties getProperties() {
        Properties props=new Properties();
        props.setProperty("jakarta.persistence.jdbc.driver", super.getDriver());
        props.setProperty("jakarta.persistence.jdbc.url", super.getUrlConnection());
        props.setProperty("jakarta.persistence.jdbc.user", super.getUserDatabase());        
        props.setProperty("jakarta.persistence.jdbc.password", super.getPasswordDatabase());                   
        //props.setProperty(Environment.DIALECT,"org.hibernate.dialect.PostgreSQLDialect");                 
        props.setProperty(Environment.SHOW_SQL,"true");                 
        props.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS,"thread");           
        //props.setProperty("hibernate.type_contributors", "com.fasterxml.jackson.datatype.hibernate6.Hibernate6ModuleTypeContributor");
        //props.setProperty("hibernate.connection.release_mode", "after_transaction");
        props.setProperty("hibernate.connection.handling_mode", "delayed_acquisition_and_release_after_transaction");        
        props.setProperty("hibernate.connection.useUnicode", "true");
        props.setProperty("hibernate.connection.charSet", "utf8mb4");
        props.setProperty("hibernate.connection.characterEncoding", "utf8");
        props.setProperty("hibernate.connection.isolation","2");
        /*props.setProperty("hibernate.connection.provider_class","org.hibernate.connection.C3P0ConnectionProvider");
        props.setProperty("hibernate.c3p0.acquire_increment","5");
        props.setProperty("hibernate.c3p0.idle_test_period","3000");
        props.setProperty("hibernate.c3p0.min_size","5");
        props.setProperty("hibernate.c3p0.max_size","20");
        props.setProperty("hibernate.c3p0.max_statements","50");
        props.setProperty("hibernate.c3p0.timeout","1800");
        props.setProperty("hibernate.c3p0.acquireRetryAttempts","1");
        props.setProperty("hibernate.c3p0.acquireRetryDelay","250");*/       
        super.getConnectionPool().apply(props);
        return props;
    }
    
}