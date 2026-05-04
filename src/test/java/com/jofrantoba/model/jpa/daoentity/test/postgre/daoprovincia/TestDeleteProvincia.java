/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia;

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
public class TestDeleteProvincia extends TestAbstract {

    @Test
    void deleteProvinciaById() throws Exception {
        InterDaoProvincia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Provincia p = dao.findById(1L);
            if (p != null) {
                dao.delete(p);
                log.info("Provincia eliminada: {} - {}", p.getCodigoProvincia(), p.getDescripcion());
            } else {
                log.warn("Provincia no encontrada para eliminar");
            }
            tx.commit();
        } catch (Exception e) {
            log.error("Error al eliminar provincia", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteMultiplesProvincias() throws Exception {
        InterDaoProvincia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(2L);
            ids.add(3L);
            dao.deleteByIds(ids);
            tx.commit();
            log.info("Provincias eliminadas: {}", ids);
        } catch (Exception e) {
            log.error("Error al eliminar múltiples provincias", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteProvinciasInactivas() throws Exception {
        InterDaoProvincia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminadas = dao.deleteInactivos();
            tx.commit();
            log.info("Se eliminaron {} provincias inactivas", eliminadas);
        } catch (Exception e) {
            log.error("Error al eliminar provincias inactivas", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void truncateProvincias() throws Exception {
        InterDaoProvincia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminadas = dao.deleteAll();
            tx.commit();
            log.info("Tabla provincias truncada. Se eliminaron {} registros", eliminadas);
        } catch (Exception e) {
            log.error("Error al truncar provincias", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoProvincia buildDao() {
        DaoProvincia dao = new DaoProvincia();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
