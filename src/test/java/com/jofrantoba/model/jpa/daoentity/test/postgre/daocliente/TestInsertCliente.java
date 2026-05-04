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
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Usuario
 */
@Log4j2
public class TestInsertCliente extends TestAbstract {

    @Test
    void createEntity1() throws UnknownException {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {            
            Cliente entity = new Cliente();
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
            dao.save(entity);
            tx.commit();
        } catch (Exception e) {
            log.error("Error al guardar cliente", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoCliente buildDao() {
        DaoCliente dao = new DaoCliente();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        ConnectionPropertiesPostgre cnx = new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
        return cnx;
    }
}
