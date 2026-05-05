/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoclientesistema;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import java.util.ArrayList;
import java.util.Collection;
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
public class TestSelectClienteSistema extends TestAbstract {

    @Test
    void listarPorSistema() throws Exception {
        InterDaoClienteSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<ClienteSistema> resultado = dao.listar(1L);
            tx.commit();
            log.info("Total clientes sistema del sistema 1: {}", resultado.size());
            for (ClienteSistema clienteSistema : resultado) {
                log.info("  Cliente sistema: {} - {} (sistema: {})",
                    clienteSistema.getId(), clienteSistema.getDescripcion(),
                    clienteSistema.getSistema() != null ? clienteSistema.getSistema().getDescripcion() : "N/A");
            }
        } catch (Exception e) {
            log.error("Error al listar clientes sistema por sistema", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarTodos() throws Exception {
        InterDaoClienteSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            ArrayNode resultado = dao.listar();
            tx.commit();
            log.info("Total clientes sistema: {}", resultado.size());
            log.info("Resultado: {}", resultado.toPrettyString());
        } catch (Exception e) {
            log.error("Error al listar clientes sistema", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarPaginado() throws Exception {
        InterDaoClienteSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            ArrayNode resultado = dao.listar(10L, 0L);
            tx.commit();
            log.info("Clientes sistema (limit 10, offset 0): {}", resultado.size());
            log.info("Resultado: {}", resultado.toPrettyString());
        } catch (Exception e) {
            log.error("Error al listar clientes sistema paginado", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void contarPorSistema() throws Exception {
        InterDaoClienteSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Long total = dao.count(1L);
            tx.commit();
            log.info("Total clientes sistema del sistema 1: {}", total);
        } catch (Exception e) {
            log.error("Error al contar clientes sistema por sistema", e);
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
