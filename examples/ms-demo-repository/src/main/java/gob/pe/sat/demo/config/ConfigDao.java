/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.sat.demo.config;

import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesSqlServer;
import gob.pe.sat.demo.dto.beans.DtoConnection;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
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
@ComponentScan(basePackages = {"gob.pe.sat.demo.dao"})
public class ConfigDao {

    public static boolean isSessionFactoryInicializado = false;

    private static SessionFactory sesionFactory=null;    
    
    @Primary
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() {
        try {
            if (!isSessionFactoryInicializado && sesionFactory==null) {
                List<String> packages = new ArrayList();
                packages.add("gob.pe.sat.demo.entity");
                PSF.getInstance().buildPSF("sqlserver", getCnx(), packages);                
                sesionFactory = PSF.getInstance().getPSF("sqlserver");
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

    private ConnectionPropertiesSqlServer getCnx() {
        String host = DtoConnection.host;//localhost
        Integer port = DtoConnection.port;//1433
        String nameDatabase = DtoConnection.nameDatabase;//demosiat
        String userDatabase = DtoConnection.userDatabase;//usrdemosiat
        String passwordDatabase = DtoConnection.passwordDatabase;//AppPassword123!
        ConnectionPropertiesSqlServer cnx = new ConnectionPropertiesSqlServer(host, port.intValue(), nameDatabase, userDatabase, passwordDatabase);
        return cnx;
    }
}
