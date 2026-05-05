/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daomanzana;

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
public class TestDeleteManzana extends TestAbstract {

    @Test
    void deleteManzanaById() throws Exception {
        InterDaoManzana dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Manzana m = dao.findById(1L);
            if (m != null) {
                dao.delete(m);
                log.info("Manzana eliminada: {} - {}", m.getCodigoManzana(), m.getDescripcion());
            } else {
                log.warn("Manzana no encontrada para eliminar");
            }
            tx.commit();
        } catch (Exception e) {
            log.error("Error al eliminar manzana", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteMultiplesManzanas() throws Exception {
        InterDaoManzana dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(2L);
            ids.add(3L);
            dao.deleteByIds(ids);
            tx.commit();
            log.info("Manzanas eliminadas: {}", ids);
        } catch (Exception e) {
            log.error("Error al eliminar múltiples manzanas", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteManzanasInactivas() throws Exception {
        InterDaoManzana dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminadas = dao.deleteInactivos();
            tx.commit();
            log.info("Se eliminaron {} manzanas inactivas", eliminadas);
        } catch (Exception e) {
            log.error("Error al eliminar manzanas inactivas", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void truncateManzanas() throws Exception {
        InterDaoManzana dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminadas = dao.deleteAll();
            tx.commit();
            log.info("Tabla manzanas truncada. Se eliminaron {} registros", eliminadas);
        } catch (Exception e) {
            log.error("Error al truncar manzanas", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoManzana buildDao() {
        DaoManzana dao = new DaoManzana();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daosector");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daomanzana");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
