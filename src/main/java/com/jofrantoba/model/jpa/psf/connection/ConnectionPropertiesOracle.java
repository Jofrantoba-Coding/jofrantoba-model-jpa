/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.psf.connection;

import java.util.Properties;
import org.hibernate.cfg.Environment;

/**
 * Parámetros de conexión específicos para <strong>Oracle Database</strong>.
 * <p>
 * Construye la URL JDBC con el formato
 * {@code jdbc:oracle:thin:@//host:puerto/servicio} y fija el driver y proveedor
 * de {@link ProviderDatabase#ORACLE}. El método {@link #getProperties()} añade
 * los ajustes de Hibernate (codificación UTF-8, nivel de aislamiento
 * {@code READ_COMMITTED}, liberación de conexión tras la transacción) y aplica
 * la estrategia de pool seleccionada.
 *
 * @author jona
 */
public class ConnectionPropertiesOracle extends AbstractConnectionProperties{

    /**
     * Crea la configuración de conexión a Oracle usando el pool por defecto
     * ({@link ConnectionPool#C3P0}).
     *
     * @param host             servidor de base de datos (host o IP)
     * @param port             puerto del listener de Oracle (por defecto 1521)
     * @param nameDatabase     nombre del servicio / SID de Oracle
     * @param userDatabase     usuario de conexión
     * @param passwordDatabase contraseña del usuario
     */
    public ConnectionPropertiesOracle(
            String host,
            int port,
            String nameDatabase,
            String userDatabase,
            String passwordDatabase){
        this(host, port, nameDatabase, userDatabase, passwordDatabase, ConnectionPool.C3P0);
    }

    /**
     * Crea la configuración de conexión a Oracle indicando explícitamente la
     * estrategia de pool de conexiones.
     *
     * @param host             servidor de base de datos (host o IP)
     * @param port             puerto del listener de Oracle (por defecto 1521)
     * @param nameDatabase     nombre del servicio / SID de Oracle
     * @param userDatabase     usuario de conexión
     * @param passwordDatabase contraseña del usuario
     * @param pool             estrategia de pool de conexiones a utilizar
     */
    public ConnectionPropertiesOracle(
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
        super.setDriver(ProviderDatabase.ORACLE.getDriver());
        super.setProviderDatabase(ProviderDatabase.ORACLE.getNameProvider());
        super.setUrlConnection("jdbc:oracle:thin:@//"+host+":"+port+"/"+nameDatabase);   
        super.setConnectionPool(pool);
    }
    
    /**
     * Construye las propiedades de Hibernate para Oracle: driver, URL,
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
        //props.setProperty(Environment.DIALECT,"org.hibernate.dialect.Oracle12cDialect");                 
        //props.setProperty(Environment.DIALECT,"org.hibernate.dialect.Oracle10gDialect");                 
        props.setProperty(Environment.SHOW_SQL,"true");                 
        props.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS,"thread");   
        props.setProperty("hibernate.connection.release_mode", "after_transaction");
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
