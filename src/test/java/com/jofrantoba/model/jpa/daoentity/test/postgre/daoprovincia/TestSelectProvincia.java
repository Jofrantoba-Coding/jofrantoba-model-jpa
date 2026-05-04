/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia;

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
public class TestSelectProvincia extends TestAbstract {

    @Test
    void listarPorDepartamento() throws Exception {
        InterDaoProvincia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Provincia> resultado = dao.listar(27L);
            tx.commit();
            log.info("Total provincias del departamento 27: {}", resultado.size());
            for (Provincia p : resultado) {
                log.info("  Provincia: {} - {} (dpto: {})",
                    p.getCodigoProvincia(), p.getDescripcion(),
                    p.getDepartamento() != null ? p.getDepartamento().getDescripcion() : "N/A");
            }
        } catch (Exception e) {
            log.error("Error al listar provincias por departamento", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarTodos() throws Exception {
        InterDaoProvincia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            ArrayNode resultado = dao.listar();
            tx.commit();
            log.info("Total provincias: {}", resultado.size());
            log.info("Resultado: {}", resultado.toPrettyString());
        } catch (Exception e) {
            log.error("Error al listar provincias", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarPaginado() throws Exception {
        InterDaoProvincia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            ArrayNode resultado = dao.listar(10L, 0L);
            tx.commit();
            log.info("Provincias (limit 10, offset 0): {}", resultado.size());
            log.info("Resultado: {}", resultado.toPrettyString());
        } catch (Exception e) {
            log.error("Error al listar provincias paginado", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void contarPorDepartamento() throws Exception {
        InterDaoProvincia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Long total = dao.count(26L);
            tx.commit();
            log.info("Total provincias del departamento 26: {}", total);
        } catch (Exception e) {
            log.error("Error al contar provincias por departamento", e);
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
