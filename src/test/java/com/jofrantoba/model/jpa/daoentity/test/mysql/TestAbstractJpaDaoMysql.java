/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.mysql;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesMysql;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.junit.Test;

/**
 *
 * @author jona
 */
@Log4j2
public class TestAbstractJpaDaoMysql extends TestAbstract {
    
    @Test
    public void testOracleConnection() {      
        List<String> packages=new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.mysql.daoempleado");
        PSF.getInstance().buildPSF("mysql", getCnx(), packages);
        SessionFactory sesionFactory=PSF.getInstance().getPSF("mysql");
        Session ss = sesionFactory.getCurrentSession();
        ss.beginTransaction();
        ss.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                log.info("Servidor:{} ", connection.getMetaData().getDatabaseProductVersion());                
            }
        });        
    }
    
    private ConnectionPropertiesMysql getCnx(){
        ConnectionPropertiesMysql cnx=new ConnectionPropertiesMysql("172.16.1.10",3306,"demotiktok","jofrantoba","F1l0s0f0");
        return cnx;
    }
    
}
