/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
public class TestInsertClienteIcl extends TestAbstract {

    @Test
    void insertClienteTitular() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Cliente cliente = new Cliente();
            cliente.setNombres("Juan");
            cliente.setApellidos("Pérez García");
            cliente.setTipoDocumento("DNI");
            cliente.setNumeroDocumento("12345678");
            cliente.setFechaNacimiento(convertToDate(LocalDate.of(1980, 5, 15)));
            cliente.setFechaVinculacion(new Date());
            cliente.setCargo("Titular Catastral");
            cliente.setSalario(new BigDecimal("3500.00"));
            cliente.setIsPersistente(Boolean.TRUE);
            cliente.setVersion(System.currentTimeMillis());
            dao.save(cliente);
            tx.commit();
            log.info("Cliente Titular insertado: {} {}", cliente.getNombres(), cliente.getApellidos());
        } catch (Exception e) {
            log.error("Error al insertar cliente titular", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertClienteRepresentante() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Cliente cliente = new Cliente();
            cliente.setNombres("María");
            cliente.setApellidos("López Rodríguez");
            cliente.setTipoDocumento("DNI");
            cliente.setNumeroDocumento("87654321");
            cliente.setFechaNacimiento(convertToDate(LocalDate.of(1985, 10, 22)));
            cliente.setFechaVinculacion(new Date());
            cliente.setCargo("Representante Legal");
            cliente.setSalario(new BigDecimal("4200.00"));
            cliente.setIsPersistente(Boolean.TRUE);
            cliente.setVersion(System.currentTimeMillis());
            dao.save(cliente);
            tx.commit();
            log.info("Cliente Representante insertado: {} {}", cliente.getNombres(), cliente.getApellidos());
        } catch (Exception e) {
            log.error("Error al insertar cliente representante", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertClienteContacto() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Cliente cliente = new Cliente();
            cliente.setNombres("Carlos");
            cliente.setApellidos("Martínez Sánchez");
            cliente.setTipoDocumento("DNI");
            cliente.setNumeroDocumento("11223344");
            cliente.setFechaNacimiento(convertToDate(LocalDate.of(1990, 3, 8)));
            cliente.setFechaVinculacion(new Date());
            cliente.setCargo("Persona de Contacto");
            cliente.setSalario(new BigDecimal("2800.00"));
            cliente.setIsPersistente(Boolean.TRUE);
            cliente.setVersion(System.currentTimeMillis());
            dao.save(cliente);
            tx.commit();
            log.info("Cliente Contacto insertado: {} {}", cliente.getNombres(), cliente.getApellidos());
        } catch (Exception e) {
            log.error("Error al insertar cliente contacto", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertMultiplesClientes() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Cliente> clientes = new ArrayList<>();

            Cliente cliente1 = new Cliente();
            cliente1.setNombres("Ana");
            cliente1.setApellidos("Gutiérrez Morales");
            cliente1.setTipoDocumento("DNI");
            cliente1.setNumeroDocumento("55667788");
            cliente1.setFechaNacimiento(convertToDate(LocalDate.of(1982, 7, 30)));
            cliente1.setFechaVinculacion(new Date());
            cliente1.setCargo("Supervisor");
            cliente1.setSalario(new BigDecimal("5000.00"));
            cliente1.setIsPersistente(Boolean.TRUE);
            cliente1.setVersion(System.currentTimeMillis());
            clientes.add(cliente1);

            Cliente cliente2 = new Cliente();
            cliente2.setNombres("Roberto");
            cliente2.setApellidos("Fernández Díaz");
            cliente2.setTipoDocumento("DNI");
            cliente2.setNumeroDocumento("99887766");
            cliente2.setFechaNacimiento(convertToDate(LocalDate.of(1975, 12, 5)));
            cliente2.setFechaVinculacion(new Date());
            cliente2.setCargo("Coordinador");
            cliente2.setSalario(new BigDecimal("4500.00"));
            cliente2.setIsPersistente(Boolean.TRUE);
            cliente2.setVersion(System.currentTimeMillis());
            clientes.add(cliente2);

            Cliente cliente3 = new Cliente();
            cliente3.setNombres("Patricia");
            cliente3.setApellidos("Ramírez López");
            cliente3.setTipoDocumento("DNI");
            cliente3.setNumeroDocumento("44332211");
            cliente3.setFechaNacimiento(convertToDate(LocalDate.of(1988, 9, 18)));
            cliente3.setFechaVinculacion(new Date());
            cliente3.setCargo("Asistente");
            cliente3.setSalario(new BigDecimal("2500.00"));
            cliente3.setIsPersistente(Boolean.TRUE);
            cliente3.setVersion(System.currentTimeMillis());
            clientes.add(cliente3);

            dao.saveAll(clientes);
            tx.commit();
            log.info("Se insertaron {} clientes de ejemplo", clientes.size());
        } catch (Exception e) {
            log.error("Error al insertar múltiples clientes", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private InterDaoCliente buildDao() {
        DaoCliente dao = new DaoCliente();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private Date convertToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
