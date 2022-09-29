/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.oracle.daoaduana;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.Date;
import java.util.UUID;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jona
 */
public class TestInsertAduana extends TestAbstract{
    /*@Test                
    void createEntity1() throws UnknownException {        
        Aduana entity = new Aduana();
        UUID uuid = UUID.randomUUID(); 
        entity.setId(uuid.toString());
        entity.setDescripcion("ACTIVO");
        entity.setEstado("A");
        entity.setDateCrea(new Date());        
        DaoAduana dao = new DaoAduana();
        SessionFactory sesionFactory=PSF.getClassPSF().getPSFStatic();
        dao.setSessionFactory(sesionFactory);
        Transaction tx=sesionFactory.getCurrentSession().beginTransaction();
        dao.save(entity);
        tx.commit();
    }*/
}
