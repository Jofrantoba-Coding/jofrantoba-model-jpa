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
public class TestDeleteParametrias extends TestAbstract {

    @Test
    void deleteParametriaById() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Parametrias p = dao.findById(1L);
            if (p != null) {
                dao.delete(p);
                log.info("Parametria eliminada: {} - {}", p.getCodigo(), p.getDescripcion());
            } else {
                log.warn("Parametria no encontrada para eliminar");
            }
            tx.commit();
        } catch (Exception e) {
            log.error("Error al eliminar parametria", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteMultiplesParametrias() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(2L);
            ids.add(3L);
            dao.deleteByIds(ids);
            tx.commit();
            log.info("Parametrias eliminadas: {}", ids);
        } catch (Exception e) {
            log.error("Error al eliminar múltiples parametrias", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteParametriasInactivas() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminadas = dao.deleteInactivos();
            tx.commit();
            log.info("Se eliminaron {} parametrias inactivas", eliminadas);
        } catch (Exception e) {
            log.error("Error al eliminar parametrias inactivas", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void truncateParametrias() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminadas = dao.deleteAll();
            tx.commit();
            log.info("Tabla parametrias truncada. Se eliminaron {} registros", eliminadas);
        } catch (Exception e) {
            log.error("Error al truncar parametrias", e);
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
