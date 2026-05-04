/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import java.math.BigDecimal;
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
public class TestUpdateClienteIcl extends TestAbstract {

    @Test
    void updateClienteCargo() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.updateCargo(1L, "Gerente General");
            tx.commit();
            log.info("Cargo actualizado al cliente 1");
        } catch (Exception e) {
            log.error("Error al actualizar cargo del cliente", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void updateClienteSalario() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.incrementarSalario(2L, new BigDecimal("500.00"));
            tx.commit();
            log.info("Salario incrementado en 500 al cliente 2");
        } catch (Exception e) {
            log.error("Error al actualizar salario del cliente", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void updateClienteDatos() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.updateDatos(3L, "Especialista Senior", new BigDecimal("6000.00"));
            tx.commit();
            log.info("Datos actualizados al cliente 3");
        } catch (Exception e) {
            log.error("Error al actualizar datos del cliente", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void marcarClienteInactivo() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            dao.marcarInactivo(1L);
            tx.commit();
            log.info("Cliente 1 marcado como inactivo");
        } catch (Exception e) {
            log.error("Error al marcar cliente como inactivo", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void incrementarSalarioSupervisores() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int actualizado = dao.incrementarSalarioByCargo("Supervisor", new BigDecimal("1.10"));
            tx.commit();
            log.info("Se actualizaron {} supervisores con incremento de salario", actualizado);
        } catch (Exception e) {
            log.error("Error al incrementar salario de supervisores", e);
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

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
