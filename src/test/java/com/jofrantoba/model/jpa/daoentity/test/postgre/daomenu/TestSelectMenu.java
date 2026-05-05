/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daomenu;

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
public class TestSelectMenu extends TestAbstract {

    @Test
    void listarPadres() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Menu> resultado = dao.parents(filter());
            tx.commit();
            log.info("Total menus padre: {}", resultado.size());
            for (Menu menu : resultado) {
                log.info("  Menu padre: {} - {}", menu.getOrden(), menu.getDescripcion());
            }
        } catch (Exception e) {
            log.error("Error al listar menus padre", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarPadresPaginado() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Menu> resultado = dao.parents(filter(), 1, 10);
            tx.commit();
            log.info("Menus padre (pagina 1, tamano 10): {}", resultado.size());
        } catch (Exception e) {
            log.error("Error al listar menus padre paginado", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarHijos() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Menu> resultado = dao.childrens(filter());
            tx.commit();
            log.info("Total menus hijos: {}", resultado.size());
            for (Menu menu : resultado) {
                log.info("  Menu hijo: {} - {} (padre: {})",
                    menu.getOrden(), menu.getDescripcion(),
                    menu.getParent() != null ? menu.getParent().getDescripcion() : "N/A");
            }
        } catch (Exception e) {
            log.error("Error al listar menus hijos", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarHijosPorPadre() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Menu> resultado = dao.childrensByParents(1L);
            tx.commit();
            log.info("Total menus hijos del padre 1: {}", resultado.size());
        } catch (Exception e) {
            log.error("Error al listar menus hijos por padre", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarTodos() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            ArrayNode resultado = dao.listar(filter());
            tx.commit();
            log.info("Total menus: {}", resultado.size());
            log.info("Resultado: {}", resultado.toPrettyString());
        } catch (Exception e) {
            log.error("Error al listar menus", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarPaginado() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            ArrayNode resultado = dao.listar(filter(), 10L, 0L);
            tx.commit();
            log.info("Menus (limit 10, offset 0): {}", resultado.size());
            log.info("Resultado: {}", resultado.toPrettyString());
        } catch (Exception e) {
            log.error("Error al listar menus paginado", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void crearTreeMenu() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            ArrayNode resultado = dao.createTreeMenu(filter());
            tx.commit();
            log.info("Tree menu: {}", resultado.toPrettyString());
        } catch (Exception e) {
            log.error("Error al crear tree menu", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void contarHijosYOrden() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Long total = dao.countChildrens(1L);
            Long maxOrdenHijos = dao.maxOrdenChildrens(1L);
            Long maxOrdenNivel = dao.maxOrdenNivel(1L, 1L);
            tx.commit();
            log.info("Hijos del menu 1: {}", total);
            log.info("Max orden hijos del menu 1: {}", maxOrdenHijos);
            log.info("Max orden cliente sistema 1 nivel 1: {}", maxOrdenNivel);
        } catch (Exception e) {
            log.error("Error al contar menus hijos y orden", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private FilterMenu filter() {
        FilterMenu filter = new FilterMenu();
        filter.setIdClienteSistema(1L);
        filter.setIdSistema(1L);
        return filter;
    }

    private InterDaoMenu buildDao() {
        DaoMenu dao = new DaoMenu();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daomenu");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
