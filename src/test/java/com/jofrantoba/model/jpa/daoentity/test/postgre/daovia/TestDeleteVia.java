/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daovia;

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
public class TestDeleteVia extends TestAbstract {

    @Test
    void deleteViaById() throws Exception {
        InterDaoVia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Via v = dao.findById(1L);
            if (v != null) {
                dao.delete(v);
                log.info("Via eliminada: {} - {}", v.getCodigoVia(), v.getDescripcion());
            } else {
                log.warn("Via no encontrada para eliminar");
            }
            tx.commit();
        } catch (Exception e) {
            log.error("Error al eliminar via", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteMultiplesVias() throws Exception {
        InterDaoVia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(2L);
            ids.add(3L);
            dao.deleteByIds(ids);
            tx.commit();
            log.info("Vias eliminadas: {}", ids);
        } catch (Exception e) {
            log.error("Error al eliminar múltiples vias", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteViasInactivas() throws Exception {
        InterDaoVia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminadas = dao.deleteInactivos();
            tx.commit();
            log.info("Se eliminaron {} vias inactivas", eliminadas);
        } catch (Exception e) {
            log.error("Error al eliminar vias inactivas", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void truncateVias() throws Exception {
        InterDaoVia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminadas = dao.deleteAll();
            tx.commit();
            log.info("Tabla vias truncada. Se eliminaron {} registros", eliminadas);
        } catch (Exception e) {
            log.error("Error al truncar vias", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoVia buildDao() {
        DaoVia dao = new DaoVia();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoparametrias");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daovia");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
