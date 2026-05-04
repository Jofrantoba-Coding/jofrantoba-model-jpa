/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daoparametrias.Parametrias;
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
public class TestInsertDepartamento extends TestAbstract {

    @Test
    void insertDepartamento() throws Exception {
        InterDaoDepartamento dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Departamento d = new Departamento();
            d.setDescripcion("LIMA");
            d.setCodigoDepartamento("15");
            d.setOrden(1L);
            d.setIsPersistente(Boolean.TRUE);
            d.setVersion(System.currentTimeMillis());
            marcarTiempo(d);
            dao.save(d);
            tx.commit();
            log.info("Departamento insertado: {} - {}", d.getCodigoDepartamento(), d.getDescripcion());
        } catch (Exception e) {
            log.error("Error al insertar departamento", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertMultiplesDepartamentos() throws Exception {
        InterDaoDepartamento dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            List<Departamento> lista = new ArrayList<>();

            Departamento d1 = new Departamento();
            d1.setDescripcion("AREQUIPA");
            d1.setCodigoDepartamento("04");
            d1.setOrden(2L);
            d1.setIsPersistente(Boolean.TRUE);
            d1.setVersion(System.currentTimeMillis());
            marcarTiempo(d1);
            lista.add(d1);

            Departamento d2 = new Departamento();
            d2.setDescripcion("CUSCO");
            d2.setCodigoDepartamento("08");
            d2.setOrden(3L);
            d2.setIsPersistente(Boolean.TRUE);
            d2.setVersion(System.currentTimeMillis());
            marcarTiempo(d2);
            lista.add(d2);

            Departamento d3 = new Departamento();
            d3.setDescripcion("PIURA");
            d3.setCodigoDepartamento("20");
            d3.setOrden(4L);
            d3.setIsPersistente(Boolean.TRUE);
            d3.setVersion(System.currentTimeMillis());
            marcarTiempo(d3);
            lista.add(d3);

            dao.saveAll(lista);
            tx.commit();
            log.info("Se insertaron {} departamentos", lista.size());
        } catch (Exception e) {
            log.error("Error al insertar múltiples departamentos", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    private void marcarTiempo(Departamento entity) {
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        entity.setMarcaTiempo(now);
    }

    private InterDaoDepartamento buildDao() {
        DaoDepartamento dao = new DaoDepartamento();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
