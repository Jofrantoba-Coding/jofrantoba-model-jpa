/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoparametrias;

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
public class TestSelectParametrias extends TestAbstract {

    @Test
    void listarTodos() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            ArrayNode resultado = dao.listar();
            tx.commit();
            log.info("Total parametrias: {}", resultado.size());
            log.info("Resultado: {}", resultado.toPrettyString());
        } catch (Exception e) {
            log.error("Error al listar parametrias", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarPaginado() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            ArrayNode resultado = dao.listar(10L, 0L);
            tx.commit();
            log.info("Parametrias (pagina 1, tamaño 10): {}", resultado.size());
            log.info("Resultado: {}", resultado.toPrettyString());
        } catch (Exception e) {
            log.error("Error al listar parametrias paginado", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarPadres() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Parametrias> padres = dao.parents();
            tx.commit();
            log.info("Total parametrias padre: {}", padres.size());
            for (Parametrias p : padres) {
                log.info("  Padre: {} - {}", p.getCodigo(), p.getDescripcion());
            }
        } catch (Exception e) {
            log.error("Error al listar padres", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarPadresPaginado() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Parametrias> padres = dao.parents(1, 5);
            tx.commit();
            log.info("Parametrias padre (pagina 1, tamaño 5): {}", padres.size());
            for (Parametrias p : padres) {
                log.info("  Padre: {}", p.getDescripcion());
            }
        } catch (Exception e) {
            log.error("Error al listar padres paginado", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarHijos() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Parametrias> hijos = dao.childrens();
            tx.commit();
            log.info("Total parametrias hijo: {}", hijos.size());
            for (Parametrias p : hijos) {
                log.info("  Hijo: {} - padre: {}", p.getDescripcion(),
                    p.getParent() != null ? p.getParent().getDescripcion() : "N/A");
            }
        } catch (Exception e) {
            log.error("Error al listar hijos", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarHijosPorPadre() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Parametrias> hijos = dao.childrensByParents(1L);
            tx.commit();
            log.info("Hijos del padre 1: {}", hijos.size());
            for (Parametrias p : hijos) {
                log.info("  Hijo: {} - {}", p.getCodigo(), p.getDescripcion());
            }
        } catch (Exception e) {
            log.error("Error al listar hijos por padre", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void contarHijosPorPadre() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Long total = dao.countChildrens(1L);
            tx.commit();
            log.info("Total hijos del padre 1: {}", total);
        } catch (Exception e) {
            log.error("Error al contar hijos por padre", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoParametrias buildDao() {
        DaoParametrias dao = new DaoParametrias();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoparametrias");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
