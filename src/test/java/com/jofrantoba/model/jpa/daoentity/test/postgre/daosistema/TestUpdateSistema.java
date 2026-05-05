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
public class TestUpdateSistema extends TestAbstract {

    @Test
    void updateDescripcion() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.updateDescripcion(1L, "ICL ACTUALIZADO");
            tx.commit();
            log.info("Descripcion actualizada en sistema 1");
        } catch (Exception e) {
            log.error("Error al actualizar descripcion de sistema", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void updateRealmId() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.updateRealmId(1L, "icl-actualizado");
            tx.commit();
            log.info("Realm ID actualizado en sistema 1");
        } catch (Exception e) {
            log.error("Error al actualizar realm id de sistema", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void marcarSistemaInactivo() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.marcarInactivo(2L);
            tx.commit();
            log.info("Sistema 2 marcado como inactivo");
        } catch (Exception e) {
            log.error("Error al marcar sistema inactivo", e);
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
