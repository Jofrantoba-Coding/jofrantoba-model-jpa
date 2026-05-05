/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daosistema;

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
public class TestSelectSistema extends TestAbstract {

    @Test
    void listarTodos() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Sistema> resultado = dao.listar();
            tx.commit();
            log.info("Total sistemas: {}", resultado.size());
            for (Sistema sistema : resultado) {
                log.info("  Sistema: {} - {}", sistema.getId(), sistema.getDescripcion());
            }
        } catch (Exception e) {
            log.error("Error al listar sistemas", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarPaginado() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Sistema> resultado = dao.listar(1, 10);
            tx.commit();
            log.info("Sistemas (pagina 1, tamano 10): {}", resultado.size());
        } catch (Exception e) {
            log.error("Error al listar sistemas paginado", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void buscarPorDescripcion() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Sistema> resultado = dao.findByDescripcion("ICL");
            tx.commit();
            log.info("Sistemas encontrados con 'ICL': {}", resultado.size());
        } catch (Exception e) {
            log.error("Error al buscar sistema por descripcion", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void contarTodos() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Long total = dao.countAll();
            tx.commit();
            log.info("Total sistemas activos: {}", total);
        } catch (Exception e) {
            log.error("Error al contar sistemas", e);
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
