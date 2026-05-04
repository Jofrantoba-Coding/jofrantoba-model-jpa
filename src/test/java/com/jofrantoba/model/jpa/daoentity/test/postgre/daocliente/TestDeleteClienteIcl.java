/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
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
public class TestDeleteClienteIcl extends TestAbstract {

    @Test
    void deleteClienteById() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Cliente cliente = dao.findById(1L);
            if (cliente != null) {
                dao.delete(cliente);
                log.info("Cliente eliminado: {} {}", cliente.getNombres(), cliente.getApellidos());
            } else {
                log.warn("Cliente no encontrado para eliminar");
            }
            tx.commit();
        } catch (Exception e) {
            log.error("Error al eliminar cliente", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteMultiplesClientes() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(2L);
            ids.add(3L);
            dao.deleteByIds(ids);
            tx.commit();
            log.info("Clientes eliminados: {}", ids);
        } catch (Exception e) {
            log.error("Error al eliminar múltiples clientes", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteClientesInactivos() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminados = dao.deleteInactivos();
            tx.commit();
            log.info("Se eliminaron {} clientes inactivos", eliminados);
        } catch (Exception e) {
            log.error("Error al eliminar clientes inactivos", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteClientesPorSalario() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminados = dao.deleteBySalarioMenorA(3000.0);
            tx.commit();
            log.info("Se eliminaron {} clientes con salario menor a 3000", eliminados);
        } catch (Exception e) {
            log.error("Error al eliminar clientes por salario", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void truncateTablaClientes() throws Exception {
        InterDaoCliente dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminados = dao.deleteAll();
            tx.commit();
            log.info("Tabla de clientes truncada. Se eliminaron {} registros", eliminados);
        } catch (Exception e) {
            log.error("Error al truncar tabla de clientes", e);
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
