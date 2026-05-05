/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daosistema;

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
public class TestInsertSistema extends TestAbstract {

    @Test
    void insertSistema() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Sistema sistema = new Sistema();
            sistema.setDescripcion("ICL");
            sistema.setRealmId("icl");
            sistema.setIsPersistente(Boolean.TRUE);
            sistema.setVersion(System.currentTimeMillis());
            marcarTiempo(sistema);
            dao.save(sistema);
            tx.commit();
            log.info("Sistema insertado: {} - {}", sistema.getId(), sistema.getDescripcion());
        } catch (Exception e) {
            log.error("Error al insertar sistema", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertMultiplesSistemas() throws Exception {
        InterDaoSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Sistema> lista = new ArrayList<>();

            Sistema s1 = new Sistema();
            s1.setDescripcion("CATASTRO");
            s1.setRealmId("catastro");
            s1.setIsPersistente(Boolean.TRUE);
            s1.setVersion(System.currentTimeMillis());
            marcarTiempo(s1);
            lista.add(s1);

            Sistema s2 = new Sistema();
            s2.setDescripcion("SEGURIDAD");
            s2.setRealmId("seguridad");
            s2.setIsPersistente(Boolean.TRUE);
            s2.setVersion(System.currentTimeMillis());
            marcarTiempo(s2);
            lista.add(s2);

            dao.saveAll(lista);
            tx.commit();
            log.info("Se insertaron {} sistemas", lista.size());
        } catch (Exception e) {
            log.error("Error al insertar multiples sistemas", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private void marcarTiempo(Sistema entity) {
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        entity.setMarcaTiempo(now);
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
