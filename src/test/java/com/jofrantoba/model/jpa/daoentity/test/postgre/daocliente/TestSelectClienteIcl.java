/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente;

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
public class TestSelectClienteIcl extends TestAbstract {

    @Test
    void findAllClientes() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Cliente> clientes = dao.allFields();
            log.info("Total de clientes encontrados: {}", clientes.size());
            clientes.forEach(c -> log.info("Cliente: {} {}, DNI: {}",
                c.getNombres(), c.getApellidos(), c.getNumeroDocumento()));
            tx.commit();
        } catch (Exception e) {
            log.error("Error al buscar clientes", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void findClienteById() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Cliente cliente = dao.findById(1L);
            if (cliente != null) {
                log.info("Cliente encontrado: {} {}", cliente.getNombres(), cliente.getApellidos());
                log.info("Tipo doc: {}, Número: {}", cliente.getTipoDocumento(), cliente.getNumeroDocumento());
            } else {
                log.info("Cliente no encontrado");
            }
            tx.commit();
        } catch (Exception e) {
            log.error("Error al buscar cliente por ID", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void findClientesByNombre() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Cliente> clientes = dao.findByNombre("Jonathan");
            log.info("Clientes encontrados con nombre 'Jonathan': {}", clientes.size());
            clientes.forEach(c -> log.info("- {} {}", c.getNombres(), c.getApellidos()));
            tx.commit();
        } catch (Exception e) {
            log.error("Error al buscar clientes por nombre", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void findClientesByTipoDocumento() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Collection<Cliente> clientes = dao.findByTipoDocumento("DNI");
            log.info("Clientes con DNI encontrados: {}", clientes.size());
            clientes.forEach(c -> log.info("- {} {} ({})",
                c.getNombres(), c.getApellidos(), c.getNumeroDocumento()));
            tx.commit();
        } catch (Exception e) {
            log.error("Error al buscar clientes por tipo de documento", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void countClientes() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Long totalClientes = dao.countAll();
            log.info("Total de clientes en base de datos: {}", totalClientes);
            tx.commit();
        } catch (Exception e) {
            log.error("Error al contar clientes", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoCliente buildDao() {
        DaoCliente dao = new DaoCliente();
        List<String> packages = new ArrayList<>();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
