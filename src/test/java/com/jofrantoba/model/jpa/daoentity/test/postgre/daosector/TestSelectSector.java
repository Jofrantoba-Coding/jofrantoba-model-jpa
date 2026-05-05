/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daosector;

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
public class TestSelectSector extends TestAbstract {

    @Test
    void listarPorDistrito() throws Exception {
        InterDaoSector dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Sector> resultado = dao.listar(1L);
            tx.commit();
            log.info("Total sectores del distrito 1: {}", resultado.size());
            for (Sector s : resultado) {
                log.info("  Sector: {} - {} (dist: {})",
                    s.getCodigoSector(), s.getDescripcion(),
                    s.getDistrito() != null ? s.getDistrito().getDescripcion() : "N/A");
            }
        } catch (Exception e) {
            log.error("Error al listar sectores por distrito", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarTodos() throws Exception {
        InterDaoSector dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            ArrayNode resultado = dao.listar();
            tx.commit();
            log.info("Total sectores: {}", resultado.size());
            log.info("Resultado: {}", resultado.toPrettyString());
        } catch (Exception e) {
            log.error("Error al listar sectores", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarPaginado() throws Exception {
        InterDaoSector dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            ArrayNode resultado = dao.listar(10L, 0L);
            tx.commit();
            log.info("Sectores (limit 10, offset 0): {}", resultado.size());
            log.info("Resultado: {}", resultado.toPrettyString());
        } catch (Exception e) {
            log.error("Error al listar sectores paginado", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void contarPorDistrito() throws Exception {
        InterDaoSector dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Long total = dao.count(1L);
            tx.commit();
            log.info("Total sectores del distrito 1: {}", total);
        } catch (Exception e) {
            log.error("Error al contar sectores por distrito", e);
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
