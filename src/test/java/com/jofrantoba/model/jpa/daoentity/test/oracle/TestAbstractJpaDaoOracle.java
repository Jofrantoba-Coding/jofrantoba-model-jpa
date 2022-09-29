/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.oracle;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.psf.PSF;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.Test;

/**
 *
 * @author jona
 */
@Log4j2
public class TestAbstractJpaDaoOracle extends TestAbstract {
    
    /*@Test
    public void testOracleConnection() {        
        Session ss = PSF.getClassPSF().getPSFStatic().getCurrentSession();
        ss.beginTransaction();
        ss.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                log.info("Servidor:{} ", connection.getMetaData().getDatabaseProductVersion());                
            }
        });        
    }*/
    
}
