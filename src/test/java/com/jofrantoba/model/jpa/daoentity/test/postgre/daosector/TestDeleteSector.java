/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daosector;

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
public class TestDeleteSector extends TestAbstract {

    @Test
    void deleteSectorById() throws Exception {
        InterDaoSector dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Sector s = dao.findById(1L);
            if (s != null) {
                dao.delete(s);
                log.info("Sector eliminado: {} - {}", s.getCodigoSector(), s.getDescripcion());
            } else {
                log.warn("Sector no encontrado para eliminar");
            }
            tx.commit();
        } catch (Exception e) {
            log.error("Error al eliminar sector", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteMultiplesSectores() throws Exception {
        InterDaoSector dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(2L);
            ids.add(3L);
            dao.deleteByIds(ids);
            tx.commit();
            log.info("Sectores eliminados: {}", ids);
        } catch (Exception e) {
            log.error("Error al eliminar múltiples sectores", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteSectoresInactivos() throws Exception {
        InterDaoSector dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminados = dao.deleteInactivos();
            tx.commit();
            log.info("Se eliminaron {} sectores inactivos", eliminados);
        } catch (Exception e) {
            log.error("Error al eliminar sectores inactivos", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void truncateSectores() throws Exception {
        InterDaoSector dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminados = dao.deleteAll();
            tx.commit();
            log.info("Tabla sectores truncada. Se eliminaron {} registros", eliminados);
        } catch (Exception e) {
            log.error("Error al truncar sectores", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoSector buildDao() {
        DaoSector dao = new DaoSector();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daosector");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
