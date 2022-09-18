/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.psf;

import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesMysql;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesOracle;
import java.util.HashMap;
import java.util.Properties;
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

    //private static final ConnectionPropertiesOracle propsStatic = new ConnectionPropertiesOracle("172.16.1.4", 1521, "orcl", "user", "pass");
    private static final ConnectionPropertiesMysql propsStatic = new ConnectionPropertiesMysql("172.16.1.10", 3306, "schema", "user", "pass");
    private static final PSF psf = new PSF();
    private final HashMap<String, SessionFactory> mapPSF = new HashMap();
    private static final String SCHEMASTATIC = "database";

    private PSF() {
        getPSF(SCHEMASTATIC, propsStatic.getProperties());
    }

    public SessionFactory getPSFStatic() {
        return mapPSF.get(SCHEMASTATIC);
    }

    public void destroyPSFStatic() {
        if (mapPSF.get(SCHEMASTATIC) != null) {
            mapPSF.get(SCHEMASTATIC).close();
            mapPSF.remove(SCHEMASTATIC);
        }

    }

    public static PSF getClassPSF() {
        return psf;
    }

    public SessionFactory getPSF(String publickey, Properties props) {
        if (mapPSF.get(publickey) == null) {
            Configuration cfg = new Configuration();
            cfg.setProperties(props);
            //cfg.addAnnotatedClass(Aduana.class);
            //cfg.addPackage("com.jofrantoba.model.jpa.daoentity");
            //Reflections reflections = new Reflections("com.jofrantoba.model.jpa.daoentity");
            Reflections reflections = new Reflections("com.parameter.entity");
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);
            for (Class<?> clazz : classes) {
                cfg.addAnnotatedClass(clazz);
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
