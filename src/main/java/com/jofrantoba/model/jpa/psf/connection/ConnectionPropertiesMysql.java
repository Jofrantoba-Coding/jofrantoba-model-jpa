/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.psf.connection;

import java.util.Properties;
import org.hibernate.cfg.Environment;

/**
 *
 * @author jona
 */
public class ConnectionPropertiesMysql extends AbstractConnectionProperties{

    public ConnectionPropertiesMysql(
            String host,
            int port,
            String nameDatabase,
            String userDatabase,
            String passwordDatabase){
        this(host, port, nameDatabase, userDatabase, passwordDatabase, ConnectionPool.C3P0);
    }

    public ConnectionPropertiesMysql(
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
        super.setDriver(ProviderDatabase.MYSQL.getDriver());
        super.setProviderDatabase(ProviderDatabase.MYSQL.getNameProvider());
        super.setUrlConnection("jdbc:mysql://"+host+":"+port+"/"+nameDatabase);   
        super.setConnectionPool(pool);
    }
    
    @Override
    public Properties getProperties() {
        Properties props=new Properties();             
        props.setProperty("jakarta.persistence.jdbc.driver", super.getDriver());
        props.setProperty("jakarta.persistence.jdbc.url", super.getUrlConnection());
        props.setProperty("jakarta.persistence.jdbc.user", super.getUserDatabase());        
        props.setProperty("jakarta.persistence.jdbc.password", super.getPasswordDatabase());            
        //props.setProperty(Environment.DIALECT,"org.hibernate.dialect.MySQL8Dialect");                 
        props.setProperty(Environment.SHOW_SQL,"true");                 
        props.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS,"thread");   
        props.setProperty("hibernate.connection.release_mode", "after_transaction");
        props.setProperty("hibernate.connection.useUnicode", "true");
        props.setProperty("hibernate.connection.charSet", "utf8mb4");
        props.setProperty("hibernate.connection.characterEncoding", "utf8");
        props.setProperty("hibernate.connection.isolation","2");
        /*props.setProperty("hibernate.connection.provider_class","org.hibernate.c3p0.internal.C3P0ConnectionProvider");
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
