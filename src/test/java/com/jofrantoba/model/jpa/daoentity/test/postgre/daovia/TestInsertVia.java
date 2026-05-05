/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daovia;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito.Distrito;
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
public class TestInsertVia extends TestAbstract {

    @Test
    void insertVia() throws Exception {
        InterDaoVia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Distrito dist = new Distrito();
            dist.setId(1L);

            Parametrias tipoVia = new Parametrias();
            tipoVia.setId(1L);

            Via v = new Via();
            v.setDescripcion("AVENIDA ABANCAY");
            v.setCodigoVia("150101001");
            v.setOrden(1L);
            v.setDistrito(dist);
            v.setTipoVia(tipoVia);
            v.setIsPersistente(Boolean.TRUE);
            v.setVersion(System.currentTimeMillis());
            marcarTiempo(v);
            dao.save(v);
            tx.commit();
            log.info("Via insertada: {} - {}", v.getCodigoVia(), v.getDescripcion());
        } catch (Exception e) {
            log.error("Error al insertar via", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Test
    void insertMultiplesVias() throws Exception {
        InterDaoVia dao = buildDao();
        SessionFactory sesionFactory = PSF.getInstance().getPSF("postgre");
        Transaction tx = sesionFactory.getCurrentSession().beginTransaction();
        try {
            Distrito dist = new Distrito();
            dist.setId(1L);

            Parametrias tipoVia = new Parametrias();
            tipoVia.setId(1L);

            List<Via> lista = new ArrayList<>();

            Via v1 = new Via();
            v1.setDescripcion("JIRON HUALLAGA");
            v1.setCodigoVia("150101002");
            v1.setOrden(2L);
            v1.setDistrito(dist);
            v1.setTipoVia(tipoVia);
            v1.setIsPersistente(Boolean.TRUE);
            v1.setVersion(System.currentTimeMillis());
            marcarTiempo(v1);
            lista.add(v1);

            Via v2 = new Via();
            v2.setDescripcion("CALLE UNION");
            v2.setCodigoVia("150101003");
            v2.setOrden(3L);
            v2.setDistrito(dist);
            v2.setTipoVia(tipoVia);
            v2.setIsPersistente(Boolean.TRUE);
            v2.setVersion(System.currentTimeMillis());
            marcarTiempo(v2);
            lista.add(v2);

            dao.saveAll(lista);
            tx.commit();
            log.info("Se insertaron {} vias", lista.size());
        } catch (Exception e) {
            log.error("Error al insertar múltiples vias", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    private void marcarTiempo(Via entity) {
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        entity.setMarcaTiempo(now);
    }

    private InterDaoVia buildDao() {
        DaoVia dao = new DaoVia();
        List<String> packages = new ArrayList();
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daoparametrias");
        packages.add("com.jofrantoba.model.jpa.daoentity.test.postgre.daovia");
        PSF.getInstance().buildPSF("postgre", getCnx(), packages);
        dao.setSessionFactory(PSF.getInstance().getPSF("postgre"));
        return dao;
    }

    private ConnectionPropertiesPostgre getCnx() {
        return new ConnectionPropertiesPostgre("localhost", 5432, "icl", "jofrantoba", "J0fr4nt0b4");
    }
}
