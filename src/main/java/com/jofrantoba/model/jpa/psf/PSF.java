/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.psf;

import com.jofrantoba.model.jpa.psf.connection.AbstractConnectionProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;

/**
 *
 * @author jona
 */
public class PSF {

    private static final PSF psf = new PSF();
    private final HashMap<String, SessionFactory> mapPSF = new HashMap();

    private PSF() {
    }

    public static PSF getInstance() {
        return psf;
    }

    public SessionFactory buildPSF(String publickey, AbstractConnectionProperties cnxProperties, List<String> packages) {
        if (mapPSF.get(publickey) == null) {
            Configuration cfg = new Configuration();
            cfg.setProperties(cnxProperties.getProperties());
            for (String pack : packages) {
                Reflections reflections = new Reflections(pack);
                Set<Class<?>> classes = reflections.getTypesAnnotatedWith(jakarta.persistence.Entity.class);
                for (Class<?> clazz : classes) {
                    cfg.addAnnotatedClass(clazz);
                }
            }
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
            SessionFactory psf = cfg.buildSessionFactory(serviceRegistry);
            mapPSF.put(publickey, psf);
        }
        return mapPSF.get(publickey);
    }

    public SessionFactory getPSF(String publickey) {
        return mapPSF.get(publickey);
    }

    public void destroyPSF(String publickey) {
        if (mapPSF.get(publickey) != null) {
            mapPSF.get(publickey).close();
            mapPSF.remove(publickey);
        }

    }

    public HashMap<String, SessionFactory> getMapPSF() {
        return mapPSF;
    }
}
