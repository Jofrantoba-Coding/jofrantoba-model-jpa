/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daomenu;

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
public class TestInsertMenu extends TestAbstract {

    @Test
    void insertMenuPadre() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Menu menu = new Menu();
            menu.setDescripcion("CONFIGURACION");
            menu.setIcono("settings");
            menu.setRuta("/configuracion");
            menu.setTipo("MENU");
            menu.setOrden(1L);
            menu.setNivel(1L);
            menu.setNumeroSubmenu(0L);
            menu.setClienteSistema(clienteSistema(1L));
            menu.setIsPersistente(Boolean.TRUE);
            menu.setVersion(System.currentTimeMillis());
            marcarTiempo(menu);
            dao.save(menu);
            tx.commit();
            log.info("Menu padre insertado: {} - {}", menu.getOrden(), menu.getDescripcion());
        } catch (Exception e) {
            log.error("Error al insertar menu padre", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertMenuHijo() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Menu parent = new Menu();
            parent.setId(1L);

            Menu menu = new Menu();
            menu.setDescripcion("USUARIOS");
            menu.setIcono("users");
            menu.setRuta("/configuracion/usuarios");
            menu.setTipo("MENU");
            menu.setOrden(1L);
            menu.setNivel(2L);
            menu.setNumeroSubmenu(0L);
            menu.setParent(parent);
            menu.setClienteSistema(clienteSistema(1L));
            menu.setIsPersistente(Boolean.TRUE);
            menu.setVersion(System.currentTimeMillis());
            marcarTiempo(menu);
            dao.save(menu);
            tx.commit();
            log.info("Menu hijo insertado: {} - {}", menu.getOrden(), menu.getDescripcion());
        } catch (Exception e) {
            log.error("Error al insertar menu hijo", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertMultiplesMenus() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Menu> lista = new ArrayList<>();

            Menu m1 = new Menu();
            m1.setDescripcion("REPORTES");
            m1.setIcono("bar-chart");
            m1.setRuta("/reportes");
            m1.setTipo("MENU");
            m1.setOrden(2L);
            m1.setNivel(1L);
            m1.setNumeroSubmenu(0L);
            m1.setClienteSistema(clienteSistema(1L));
            m1.setIsPersistente(Boolean.TRUE);
            m1.setVersion(System.currentTimeMillis());
            marcarTiempo(m1);
            lista.add(m1);

            Menu m2 = new Menu();
            m2.setDescripcion("MANTENIMIENTO");
            m2.setIcono("tool");
            m2.setRuta("/mantenimiento");
            m2.setTipo("MENU");
            m2.setOrden(3L);
            m2.setNivel(1L);
            m2.setNumeroSubmenu(0L);
            m2.setClienteSistema(clienteSistema(1L));
            m2.setIsPersistente(Boolean.TRUE);
            m2.setVersion(System.currentTimeMillis());
            marcarTiempo(m2);
            lista.add(m2);

            dao.saveAll(lista);
            tx.commit();
            log.info("Se insertaron {} menus", lista.size());
        } catch (Exception e) {
            log.error("Error al insertar multiples menus", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private ClienteSistema clienteSistema(Long id) {
        ClienteSistema clienteSistema = new ClienteSistema();
        clienteSistema.setId(id);
        return clienteSistema;
    }

    private void marcarTiempo(Menu entity) {
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        entity.setMarcaTiempo(now);
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
