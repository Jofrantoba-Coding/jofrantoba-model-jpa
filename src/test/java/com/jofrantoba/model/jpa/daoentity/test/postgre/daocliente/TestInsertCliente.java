/*
 * Click nbfs://nbhost/SystemFileSystem/Tentitylates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Tentitylates/Classes/Class.java to edit this tentitylate
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Usuario
 */
public class TestInsertCliente extends TestAbstract{
    @Test                
    void createEntity1() throws UnknownException {
        Cliente entity=new Cliente();
        //entity.setId(1l);
        entity.setNombres("Jonathan");
        entity.setApellidos("Torres");
        entity.setCargo("Developer");
        entity.setFechaNacimiento(new Date());
        entity.setFechaVinculacion(new Date());
        entity.setIsPersistente(Boolean.TRUE);
        entity.setNumeroDocumento("45329234");
        entity.setSalario(BigDecimal.ZERO);
        entity.setTipoDocumento("DNI");
        entity.setVersion(Long.MIN_VALUE);
        DaoCliente dao=new DaoCliente();        
        List<String> packages=new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        SessionFactory sesionFactory=PSF.getInstance().getPSF("postgre");
        dao.setSessionFactory(sesionFactory);
        Transaction tx=sesionFactory.getCurrentSession().beginTransaction();
        dao.save(entity);
        tx.commit();
    }
    
    private ConnectionPropertiesPostgre getCnx(){
        ConnectionPropertiesPostgre cnx=new ConnectionPropertiesPostgre("localhost",5432,"icl","usercatastro","icl2023");
        return cnx;
    }
}
