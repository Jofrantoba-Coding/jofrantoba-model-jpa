/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.config;

import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import gob.pe.icl.icl.dto.beans.DtoConnection;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @author jtorresb
 */

@Slf4j
@Configuration
@ComponentScan(basePackages = {"gob.pe.icl.icl.dao"})
public class ConfigDao {

    public static boolean isSessionFactoryInicializado = false;

    private static SessionFactory sesionFactory=null;    
    
    @Primary
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() {
        try {
            if (!isSessionFactoryInicializado && sesionFactory==null) {
                List<String> packages = new ArrayList();
                packages.add("gob.pe.icl.icl.entity");
                PSF.getInstance().buildPSF("postgre", getCnx(), packages);                
                sesionFactory = PSF.getInstance().getPSF("postgre");
                isSessionFactoryInicializado = true;
                log.info("sessionFactory inicializado");
            }else{
                log.info("sessionFactory ya fue inicializado");
            }
           
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Warning: sessionFactory no puede inicializarse:{}",ex.getMessage());
        }
        return sesionFactory;
    }

    private ConnectionPropertiesPostgre getCnx() {
        String host = DtoConnection.host;//localhost
        Integer port = DtoConnection.port;//5432
        String nameDatabase = DtoConnection.nameDatabase;//icl
        String userDatabase = DtoConnection.userDatabase;//postgres
        String passwordDatabase = DtoConnection.passwordDatabase;//jofrantoba
        ConnectionPropertiesPostgre cnx = new ConnectionPropertiesPostgre(host, port.intValue(), nameDatabase, userDatabase, passwordDatabase);
        return cnx;
    }
}
