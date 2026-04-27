/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.sqlserver;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesSqlServer;
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
 * @author apoyo1953
 */
@Log4j2
public class TestAbstractJpaDaoSqlServer extends TestAbstract {
    
    @Test
    public void testOracleConnection() {      
        List<String> packages=new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.sqlserver.daoparametria");
        PSF.getInstance().buildPSF("sqlserver", getCnx(), packages);
        SessionFactory sesionFactory=PSF.getInstance().getPSF("sqlserver");
        Session ss = sesionFactory.getCurrentSession();
        ss.beginTransaction();
        ss.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                log.info("Servidor:{} ", connection.getMetaData().getDatabaseProductVersion());                
            }
        });        
    }
    
    private ConnectionPropertiesSqlServer getCnx(){
        ConnectionPropertiesSqlServer cnx=new ConnectionPropertiesSqlServer("localhost",1433,"DEMO","UsrDemoSiat","AppPassword123!");
        return cnx;
    }
    
}