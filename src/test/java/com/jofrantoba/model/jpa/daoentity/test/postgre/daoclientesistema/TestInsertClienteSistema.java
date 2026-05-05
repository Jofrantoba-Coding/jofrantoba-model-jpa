/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoclientesistema;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daosistema.Sistema;
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
public class TestInsertClienteSistema extends TestAbstract {

    @Test
    void insertClienteSistema() throws Exception {
        InterDaoClienteSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            ClienteSistema clienteSistema = new ClienteSistema();
            clienteSistema.setDescripcion("APP ICL");
            clienteSistema.setClientId("app-icl");
            clienteSistema.setRealmId("icl");
            clienteSistema.setIsAppIcl(Boolean.TRUE);
            clienteSistema.setSistema(sistema(1L));
            clienteSistema.setIsPersistente(Boolean.TRUE);
            clienteSistema.setVersion(System.currentTimeMillis());
            marcarTiempo(clienteSistema);
            dao.save(clienteSistema);
            tx.commit();
            log.info("Cliente sistema insertado: {} - {}", clienteSistema.getId(), clienteSistema.getDescripcion());
        } catch (Exception e) {
            log.error("Error al insertar cliente sistema", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertMultiplesClientesSistema() throws Exception {
        InterDaoClienteSistema dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<ClienteSistema> lista = new ArrayList<>();

            ClienteSistema cs1 = new ClienteSistema();
            cs1.setDescripcion("APP CATASTRO");
            cs1.setClientId("app-catastro");
            cs1.setRealmId("catastro");
            cs1.setIsAppIcl(Boolean.TRUE);
            cs1.setSistema(sistema(1L));
            cs1.setIsPersistente(Boolean.TRUE);
            cs1.setVersion(System.currentTimeMillis());
            marcarTiempo(cs1);
            lista.add(cs1);

            ClienteSistema cs2 = new ClienteSistema();
            cs2.setDescripcion("APP SEGURIDAD");
            cs2.setClientId("app-seguridad");
            cs2.setRealmId("seguridad");
            cs2.setIsAppIcl(Boolean.TRUE);
            cs2.setSistema(sistema(1L));
            cs2.setIsPersistente(Boolean.TRUE);
            cs2.setVersion(System.currentTimeMillis());
            marcarTiempo(cs2);
            lista.add(cs2);

            dao.saveAll(lista);
            tx.commit();
            log.info("Se insertaron {} clientes sistema", lista.size());
        } catch (Exception e) {
            log.error("Error al insertar multiples clientes sistema", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private Sistema sistema(Long id) {
        Sistema sistema = new Sistema();
        sistema.setId(id);
        return sistema;
    }

    private void marcarTiempo(ClienteSistema entity) {
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        entity.setMarcaTiempo(now);
    }

    private InterDaoClienteSistema buildDao() {
        DaoClienteSistema dao = new DaoClienteSistema();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daosistema");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoclientesistema");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
