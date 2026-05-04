/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento;

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
public class TestUpdateDepartamento extends TestAbstract {

    @Test
    void updateDescripcion() throws Exception {
        InterDaoDepartamento dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.updateDescripcion(1L, "LIMA METROPOLITANA");
            tx.commit();
            log.info("Descripcion actualizada en departamento 1");
        } catch (Exception e) {
            log.error("Error al actualizar descripcion", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void updateOrden() throws Exception {
        InterDaoDepartamento dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.updateOrden(2L, 10L);
            tx.commit();
            log.info("Orden actualizado en departamento 2");
        } catch (Exception e) {
            log.error("Error al actualizar orden", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void marcarDepartamentoInactivo() throws Exception {
        InterDaoDepartamento dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.marcarInactivo(3L);
            tx.commit();
            log.info("Departamento 3 marcado como inactivo");
        } catch (Exception e) {
            log.error("Error al marcar departamento inactivo", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoDepartamento buildDao() {
        DaoDepartamento dao = new DaoDepartamento();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
