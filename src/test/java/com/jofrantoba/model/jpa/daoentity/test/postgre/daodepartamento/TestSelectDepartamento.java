/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento;

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
public class TestSelectDepartamento extends TestAbstract {

    @Test
    void listarTodos() throws Exception {
        InterDaoDepartamento dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Departamento> resultado = dao.listar();
            tx.commit();
            log.info("Total departamentos: {}", resultado.size());
            for (Departamento d : resultado) {
                log.info("  Departamento: {} - {}", d.getCodigoDepartamento(), d.getDescripcion());
            }
        } catch (Exception e) {
            log.error("Error al listar departamentos", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void listarPaginado() throws Exception {
        InterDaoDepartamento dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Departamento> resultado = dao.listar(1, 5);
            tx.commit();
            log.info("Departamentos (pagina 1, tamaño 5): {}", resultado.size());
            for (Departamento d : resultado) {
                log.info("  Departamento: {}", d.getDescripcion());
            }
        } catch (Exception e) {
            log.error("Error al listar departamentos paginado", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void buscarPorDescripcion() throws Exception {
        InterDaoDepartamento dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Departamento> resultado = dao.findByDescripcion("LIMA");
            tx.commit();
            log.info("Departamentos encontrados con 'LIMA': {}", resultado.size());
            for (Departamento d : resultado) {
                log.info("  Departamento: {} - {}", d.getCodigoDepartamento().toString(), d.getDescripcion().toString());
            }
        } catch (Exception e) {
            log.error("Error al buscar departamento por descripcion", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void contarTodos() throws Exception {
        InterDaoDepartamento dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Long total = dao.countAll();
            tx.commit();
            log.info("Total departamentos activos: {}", total);
        } catch (Exception e) {
            log.error("Error al contar departamentos", e);
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
