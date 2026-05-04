/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jofrantoba
 */
@Log4j2
public class TestUpdateDistrito extends TestAbstract {

    @Test
    void updateDescripcion() throws Exception {
        InterDaoDistrito dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.updateDescripcion(1L, "LIMA DISTRITO");
            tx.commit();
            log.info("Descripcion actualizada en distrito 1");
        } catch (Exception e) {
            log.error("Error al actualizar descripcion", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void marcarDistritoInactivo() throws Exception {
        InterDaoDistrito dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.marcarInactivo(2L);
            tx.commit();
            log.info("Distrito 2 marcado como inactivo");
        } catch (Exception e) {
            log.error("Error al marcar distrito inactivo", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoDistrito buildDao() {
        DaoDistrito dao = new DaoDistrito();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
