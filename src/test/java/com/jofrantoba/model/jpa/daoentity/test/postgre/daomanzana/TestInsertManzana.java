/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daomanzana;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daosector.Sector;
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
public class TestInsertManzana extends TestAbstract {

    @Test
    void insertManzana() throws Exception {
        InterDaoManzana dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Sector sec = new Sector();
            sec.setId(1L);

            Manzana m = new Manzana();
            m.setDescripcion("MANZANA A");
            m.setCodigoManzana("150101001A");
            m.setCodigoSector("150101001");
            m.setCodigoDistrito("150101");
            m.setCodigoProvincia("1501");
            m.setCodigoDepartamento("15");
            m.setOrden(1L);
            m.setSector(sec);
            m.setIsPersistente(Boolean.TRUE);
            m.setVersion(System.currentTimeMillis());
            marcarTiempo(m);
            dao.save(m);
            tx.commit();
            log.info("Manzana insertada: {} - {}", m.getCodigoManzana(), m.getDescripcion());
        } catch (Exception e) {
            log.error("Error al insertar manzana", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertMultiplesManzanas() throws Exception {
        InterDaoManzana dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Sector sec = new Sector();
            sec.setId(1L);

            List<Manzana> lista = new ArrayList<>();

            Manzana m1 = new Manzana();
            m1.setDescripcion("MANZANA B");
            m1.setCodigoManzana("150101001B");
            m1.setCodigoSector("150101001");
            m1.setCodigoDistrito("150101");
            m1.setCodigoProvincia("1501");
            m1.setCodigoDepartamento("15");
            m1.setOrden(2L);
            m1.setSector(sec);
            m1.setIsPersistente(Boolean.TRUE);
            m1.setVersion(System.currentTimeMillis());
            marcarTiempo(m1);
            lista.add(m1);

            Manzana m2 = new Manzana();
            m2.setDescripcion("MANZANA C");
            m2.setCodigoManzana("150101001C");
            m2.setCodigoSector("150101001");
            m2.setCodigoDistrito("150101");
            m2.setCodigoProvincia("1501");
            m2.setCodigoDepartamento("15");
            m2.setOrden(3L);
            m2.setSector(sec);
            m2.setIsPersistente(Boolean.TRUE);
            m2.setVersion(System.currentTimeMillis());
            marcarTiempo(m2);
            lista.add(m2);

            dao.saveAll(lista);
            tx.commit();
            log.info("Se insertaron {} manzanas", lista.size());
        } catch (Exception e) {
            log.error("Error al insertar múltiples manzanas", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private void marcarTiempo(Manzana entity) {
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        entity.setMarcaTiempo(now);
    }

    private InterDaoManzana buildDao() {
        DaoManzana dao = new DaoManzana();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daosector");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daomanzana");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
