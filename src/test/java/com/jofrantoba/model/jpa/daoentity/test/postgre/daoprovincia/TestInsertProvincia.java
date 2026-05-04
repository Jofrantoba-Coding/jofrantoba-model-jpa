/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento.Departamento;
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
public class TestInsertProvincia extends TestAbstract {

    @Test
    void insertProvincia() throws Exception {
        InterDaoProvincia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Departamento dep = new Departamento();
            dep.setId(1L);

            Provincia p = new Provincia();
            p.setDescripcion("LIMA");
            p.setCodigoProvincia("99");
            p.setCodigoDepartamento("15");
            p.setOrden(1L);
            p.setDepartamento(dep);
            p.setIsPersistente(Boolean.TRUE);
            p.setVersion(System.currentTimeMillis());
            marcarTiempo(p);
            dao.save(p);
            tx.commit();
            log.info("Provincia insertada: {} - {}", p.getCodigoProvincia(), p.getDescripcion());
        } catch (Exception e) {
            log.error("Error al insertar provincia", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertMultiplesProvincias() throws Exception {
        InterDaoProvincia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Departamento dep = new Departamento();
            dep.setId(1L);

            List<Provincia> lista = new ArrayList<>();

            Provincia p1 = new Provincia();
            p1.setDescripcion("CALLAO");
            p1.setCodigoProvincia("98");
            p1.setCodigoDepartamento("07");
            p1.setOrden(2L);
            p1.setDepartamento(dep);
            p1.setIsPersistente(Boolean.TRUE);
            p1.setVersion(System.currentTimeMillis());
            marcarTiempo(p1);
            lista.add(p1);

            Provincia p2 = new Provincia();
            p2.setDescripcion("AREQUIPA");
            p2.setCodigoProvincia("97");
            p2.setCodigoDepartamento("04");
            p2.setOrden(3L);
            p2.setDepartamento(dep);
            p2.setIsPersistente(Boolean.TRUE);
            p2.setVersion(System.currentTimeMillis());
            marcarTiempo(p2);
            lista.add(p2);

            dao.saveAll(lista);
            tx.commit();
            log.info("Se insertaron {} provincias", lista.size());
        } catch (Exception e) {
            log.error("Error al insertar múltiples provincias", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private void marcarTiempo(Provincia entity) {
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        entity.setMarcaTiempo(now);
    }

    private InterDaoProvincia buildDao() {
        DaoProvincia dao = new DaoProvincia();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
