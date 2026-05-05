/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daomenu;

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
public class TestDeleteMenu extends TestAbstract {

    @Test
    void deleteMenuById() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Menu menu = dao.findById(1L);
            if (menu != null) {
                dao.delete(menu);
                log.info("Menu eliminado: {} - {}", menu.getOrden(), menu.getDescripcion());
            } else {
                log.warn("Menu no encontrado para eliminar");
            }
            tx.commit();
        } catch (Exception e) {
            log.error("Error al eliminar menu", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteMultiplesMenus() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(2L);
            ids.add(3L);
            dao.deleteByIds(ids);
            tx.commit();
            log.info("Menus eliminados: {}", ids);
        } catch (Exception e) {
            log.error("Error al eliminar multiples menus", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void deleteMenusInactivos() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminados = dao.deleteInactivos();
            tx.commit();
            log.info("Se eliminaron {} menus inactivos", eliminados);
        } catch (Exception e) {
            log.error("Error al eliminar menus inactivos", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void truncateMenus() throws Exception {
        InterDaoMenu dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            int eliminados = dao.deleteAll();
            tx.commit();
            log.info("Tabla menus truncada. Se eliminaron {} registros", eliminados);
        } catch (Exception e) {
            log.error("Error al truncar menus", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
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
