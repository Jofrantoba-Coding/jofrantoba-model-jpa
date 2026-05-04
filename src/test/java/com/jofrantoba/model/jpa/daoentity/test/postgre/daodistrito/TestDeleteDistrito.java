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
public class TestDeleteDistrito extends TestAbstract {

    @Test
    void deleteDistritoById() throws Exception {
        InterDaoDistrito dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Distrito d = dao.findById(1L);
            if (d != null) {
                dao.delete(d);
                log.info("Distrito eliminado: {} - {}", d.getCodigoDistrito(), d.getDescripcion());
            } else {
                log.warn("Distrito no encontrado para eliminar");
            }
            tx.commit();
        } catch (Exception e) {
            log.error("Error al eliminar distrito", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteMultiplesDistritos() throws Exception {
        InterDaoDistrito dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(2L);
            ids.add(3L);
            dao.deleteByIds(ids);
            tx.commit();
            log.info("Distritos eliminados: {}", ids);
        } catch (Exception e) {
            log.error("Error al eliminar multiples distritos", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteDistritosInactivos() throws Exception {
        InterDaoDistrito dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminados = dao.deleteInactivos();
            tx.commit();
            log.info("Se eliminaron {} distritos inactivos", eliminados);
        } catch (Exception e) {
            log.error("Error al eliminar distritos inactivos", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void truncateDistritos() throws Exception {
        InterDaoDistrito dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminados = dao.deleteAll();
            tx.commit();
            log.info("Tabla distritos truncada. Se eliminaron {} registros", eliminados);
        } catch (Exception e) {
            log.error("Error al truncar distritos", e);
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
