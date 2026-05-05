/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daosistema;

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
public class TestDeleteSistema extends TestAbstract {

    @Test
    void deleteSistemaById() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Sistema sistema = dao.findById(1L);
            if (sistema != null) {
                dao.delete(sistema);
                log.info("Sistema eliminado: {} - {}", sistema.getId(), sistema.getDescripcion());
            } else {
                log.warn("Sistema no encontrado para eliminar");
            }
            tx.commit();
        } catch (Exception e) {
            log.error("Error al eliminar sistema", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteMultiplesSistemas() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(2L);
            ids.add(3L);
            dao.deleteByIds(ids);
            tx.commit();
            log.info("Sistemas eliminados: {}", ids);
        } catch (Exception e) {
            log.error("Error al eliminar multiples sistemas", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteSistemasInactivos() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminados = dao.deleteInactivos();
            tx.commit();
            log.info("Se eliminaron {} sistemas inactivos", eliminados);
        } catch (Exception e) {
            log.error("Error al eliminar sistemas inactivos", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void truncateSistemas() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminados = dao.deleteAll();
            tx.commit();
            log.info("Tabla sistemas truncada. Se eliminaron {} registros", eliminados);
        } catch (Exception e) {
            log.error("Error al truncar sistemas", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoSistema buildDao() {
        DaoSistema dao = new DaoSistema();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daosistema");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
