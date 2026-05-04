/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoparametrias;

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
public class TestUpdateParametrias extends TestAbstract {

    @Test
    void updateDescripcion() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.updateDescripcion(1L, "TIPO DE DOCUMENTO IDENTIDAD");
            tx.commit();
            log.info("Descripcion actualizada en parametria 1");
        } catch (Exception e) {
            log.error("Error al actualizar descripcion", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void updateOrden() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.updateOrden(2L, 10L);
            tx.commit();
            log.info("Orden actualizado en parametria 2");
        } catch (Exception e) {
            log.error("Error al actualizar orden", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void marcarParametriaInactiva() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.marcarInactivo(3L);
            tx.commit();
            log.info("Parametria 3 marcada como inactiva");
        } catch (Exception e) {
            log.error("Error al marcar parametria inactiva", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoParametrias buildDao() {
        DaoParametrias dao = new DaoParametrias();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoparametrias");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
