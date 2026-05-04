/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoparametrias;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
public class TestInsertParametrias extends TestAbstract {

    @Test
    void insertParametriaPadre() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Parametrias p = new Parametrias();
            p.setDescripcion("TIPO DE DOCUMENTO");
            p.setAbreviatura("TDOC");
            p.setCodigo("001");
            p.setOrden(1L);
            p.setIsPersistente(Boolean.TRUE);
            p.setVersion(System.currentTimeMillis());
            marcarTiempo(p);
            dao.save(p);
            tx.commit();
            log.info("Parametria padre insertada: {} - {}", p.getCodigo(), p.getDescripcion());
        } catch (Exception e) {
            log.error("Error al insertar parametria padre", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertParametriaHijo() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Parametrias padre = dao.findById(1L);
            if (padre != null) {
                Parametrias hijo = new Parametrias();
                hijo.setDescripcion("DNI");
                hijo.setAbreviatura("DNI");
                hijo.setCodigo("001001");
                hijo.setOrden(1L);
                hijo.setParent(padre);
                hijo.setIsPersistente(Boolean.TRUE);
                hijo.setVersion(System.currentTimeMillis());
                marcarTiempo(hijo);
                dao.save(hijo);
                tx.commit();
                log.info("Parametria hijo insertada: {} bajo padre: {}", hijo.getDescripcion(), padre.getDescripcion());
            } else {
                log.warn("Padre no encontrado para insertar hijo");
                tx.rollback();
            }
        } catch (Exception e) {
            log.error("Error al insertar parametria hijo", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertMultiplesParametrias() throws Exception {
        InterDaoParametrias dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Parametrias> lista = new ArrayList<>();

            Parametrias p1 = new Parametrias();
            p1.setDescripcion("ESTADO CIVIL");
            p1.setAbreviatura("ECIV");
            p1.setCodigo("002");
            p1.setOrden(2L);
            p1.setIsPersistente(Boolean.TRUE);
            p1.setVersion(System.currentTimeMillis());
            marcarTiempo(p1);
            lista.add(p1);

            Parametrias p2 = new Parametrias();
            p2.setDescripcion("GÉNERO");
            p2.setAbreviatura("GEN");
            p2.setCodigo("003");
            p2.setOrden(3L);
            p2.setIsPersistente(Boolean.TRUE);
            p2.setVersion(System.currentTimeMillis());
            marcarTiempo(p2);
            lista.add(p2);

            Parametrias p3 = new Parametrias();
            p3.setDescripcion("NIVEL EDUCATIVO");
            p3.setAbreviatura("NEDU");
            p3.setCodigo("004");
            p3.setOrden(4L);
            p3.setIsPersistente(Boolean.TRUE);
            p3.setVersion(System.currentTimeMillis());
            marcarTiempo(p3);
            lista.add(p3);

            dao.saveAll(lista);
            tx.commit();
            log.info("Se insertaron {} parametrias", lista.size());
        } catch (Exception e) {
            log.error("Error al insertar múltiples parametrias", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Asigna la marca de tiempo actual en zona horaria de Lima
     */
    private void marcarTiempo(Parametrias entity) {
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        entity.setMarcaTiempo(now);
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
