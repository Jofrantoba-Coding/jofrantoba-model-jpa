/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia.Provincia;
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
public class TestInsertDistrito extends TestAbstract {

    @Test
    void insertDistrito() throws Exception {
        InterDaoDistrito dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Provincia provincia = new Provincia();
            provincia.setId(1L);

            Distrito d = new Distrito();
            d.setDescripcion("LIMA");
            d.setCodigoDistrito("150101");
            d.setCodigoProvincia("1501");
            d.setCodigoDepartamento("15");
            d.setOrden(1L);
            d.setProvincia(provincia);
            d.setIsPersistente(Boolean.TRUE);
            d.setVersion(System.currentTimeMillis());
            marcarTiempo(d);
            dao.save(d);
            tx.commit();
            log.info("Distrito insertado: {} - {}", d.getCodigoDistrito(), d.getDescripcion());
        } catch (Exception e) {
            log.error("Error al insertar distrito", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertMultiplesDistritos() throws Exception {
        InterDaoDistrito dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Provincia provincia = new Provincia();
            provincia.setId(1L);

            List<Distrito> lista = new ArrayList<>();

            Distrito d1 = new Distrito();
            d1.setDescripcion("ANCON");
            d1.setCodigoDistrito("150102");
            d1.setCodigoProvincia("1501");
            d1.setCodigoDepartamento("15");
            d1.setOrden(2L);
            d1.setProvincia(provincia);
            d1.setIsPersistente(Boolean.TRUE);
            d1.setVersion(System.currentTimeMillis());
            marcarTiempo(d1);
            lista.add(d1);

            Distrito d2 = new Distrito();
            d2.setDescripcion("ATE");
            d2.setCodigoDistrito("150103");
            d2.setCodigoProvincia("1501");
            d2.setCodigoDepartamento("15");
            d2.setOrden(3L);
            d2.setProvincia(provincia);
            d2.setIsPersistente(Boolean.TRUE);
            d2.setVersion(System.currentTimeMillis());
            marcarTiempo(d2);
            lista.add(d2);

            dao.saveAll(lista);
            tx.commit();
            log.info("Se insertaron {} distritos", lista.size());
        } catch (Exception e) {
            log.error("Error al insertar multiples distritos", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private void marcarTiempo(Distrito entity) {
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        entity.setMarcaTiempo(now);
    }

    private InterDaoDistrito buildDao() {
        DaoDistrito dao = new DaoDistrito();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
