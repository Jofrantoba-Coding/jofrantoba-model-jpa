/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daosector;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito.Distrito;
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
public class TestInsertSector extends TestAbstract {

    @Test
    void insertSector() throws Exception {
        InterDaoSector dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Distrito dist = new Distrito();
            dist.setId(1L);

            Sector s = new Sector();
            s.setDescripcion("SECTOR 01");
            s.setCodigoSector("150101001");
            s.setCodigoDistrito("150101");
            s.setCodigoProvincia("1501");
            s.setCodigoDepartamento("15");
            s.setOrden(1L);
            s.setDistrito(dist);
            s.setIsPersistente(Boolean.TRUE);
            s.setVersion(System.currentTimeMillis());
            marcarTiempo(s);
            dao.save(s);
            tx.commit();
            log.info("Sector insertado: {} - {}", s.getCodigoSector(), s.getDescripcion());
        } catch (Exception e) {
            log.error("Error al insertar sector", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertMultiplesSectores() throws Exception {
        InterDaoSector dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Distrito dist = new Distrito();
            dist.setId(1L);

            List<Sector> lista = new ArrayList<>();

            Sector s1 = new Sector();
            s1.setDescripcion("SECTOR 02");
            s1.setCodigoSector("150101002");
            s1.setCodigoDistrito("150101");
            s1.setCodigoProvincia("1501");
            s1.setCodigoDepartamento("15");
            s1.setOrden(2L);
            s1.setDistrito(dist);
            s1.setIsPersistente(Boolean.TRUE);
            s1.setVersion(System.currentTimeMillis());
            marcarTiempo(s1);
            lista.add(s1);

            Sector s2 = new Sector();
            s2.setDescripcion("SECTOR 03");
            s2.setCodigoSector("150101003");
            s2.setCodigoDistrito("150101");
            s2.setCodigoProvincia("1501");
            s2.setCodigoDepartamento("15");
            s2.setOrden(3L);
            s2.setDistrito(dist);
            s2.setIsPersistente(Boolean.TRUE);
            s2.setVersion(System.currentTimeMillis());
            marcarTiempo(s2);
            lista.add(s2);

            dao.saveAll(lista);
            tx.commit();
            log.info("Se insertaron {} sectores", lista.size());
        } catch (Exception e) {
            log.error("Error al insertar múltiples sectores", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private void marcarTiempo(Sector entity) {
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        entity.setMarcaTiempo(now);
    }

    private InterDaoSector buildDao() {
        DaoSector dao = new DaoSector();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daosector");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
