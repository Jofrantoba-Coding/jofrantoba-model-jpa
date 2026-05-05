/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoclientesistema;

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
public class TestUpdateClienteSistema extends TestAbstract {

    @Test
    void updateDescripcion() throws Exception {
        InterDaoClienteSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.updateDescripcion(1L, "APP ICL ACTUALIZADO");
            tx.commit();
            log.info("Descripcion actualizada en cliente sistema 1");
        } catch (Exception e) {
            log.error("Error al actualizar descripcion de cliente sistema", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void updateClientId() throws Exception {
        InterDaoClienteSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.updateClientId(1L, "app-icl-actualizado");
            tx.commit();
            log.info("Client ID actualizado en cliente sistema 1");
        } catch (Exception e) {
            log.error("Error al actualizar client id de cliente sistema", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void marcarClienteSistemaInactivo() throws Exception {
        InterDaoClienteSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.marcarInactivo(2L);
            tx.commit();
            log.info("Cliente sistema 2 marcado como inactivo");
        } catch (Exception e) {
            log.error("Error al marcar cliente sistema inactivo", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoClienteSistema buildDao() {
        DaoClienteSistema dao = new DaoClienteSistema();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daosistema");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoclientesistema");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
