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
 * Registro <em>singleton</em> de {@link SessionFactory} de Hibernate indexadas
 * por una clave pública.
 * <p>
 * {@code PSF} (<em>Provider of Session Factory</em>) mantiene un mapa en memoria
 * de fábricas de sesión ya construidas, de modo que cada combinación
 * lógica de base de datos se inicializa una sola vez y se reutiliza en
 * sucesivas peticiones. Esto evita el coste de reconstruir el
 * {@code SessionFactory} (operación costosa) y permite trabajar con varias
 * bases de datos simultáneamente identificándolas por su clave.
 * <p>
 * El escaneo de entidades se hace con la librería <em>Reflections</em>:
 * por cada paquete indicado se detectan todas las clases anotadas con
 * {@link jakarta.persistence.Entity @Entity} y se registran en la
 * {@link Configuration}.
 * <p>
 * <strong>Uso típico:</strong>
 * <pre>{@code
 * AbstractConnectionProperties cnx =
 *     new ConnectionPropertiesPostgre("localhost", 5432, "midb", "user", "pass");
 * SessionFactory sf = PSF.getInstance()
 *     .buildPSF("midb", cnx, List.of("com.jofrantoba.app.entidades"));
 * }</pre>
 *
 * @author jona
 */
public class PSF {

    /** Única instancia del singleton. */
    private static final PSF psf = new PSF();
    /** Mapa de fábricas de sesión ya construidas, indexadas por clave pública. */
    private final HashMap<String, SessionFactory> mapPSF = new HashMap();

    /** Constructor privado: el acceso se realiza siempre vía {@link #getInstance()}. */
    private PSF() {
    }

    /**
     * Devuelve la instancia única (singleton) de {@code PSF}.
     *
     * @return la instancia compartida de {@code PSF}
     */
    public static PSF getInstance() {
        return psf;
    }

    /**
     * Obtiene —construyéndola la primera vez— la {@link SessionFactory}
     * asociada a la clave dada.
     * <p>
     * Si ya existe una fábrica registrada bajo {@code publickey} se devuelve tal
     * cual. En caso contrario se crea una {@link Configuration} a partir de las
     * propiedades de conexión, se escanean los paquetes indicados para registrar
     * todas las entidades {@code @Entity}, se construye el
     * {@link ServiceRegistry} y la {@link SessionFactory}, y se almacena en el
     * mapa para futuros accesos.
     *
     * @param publickey     clave lógica con la que se indexa y recupera la fábrica
     * @param cnxProperties propiedades de conexión del motor de base de datos
     * @param packages      lista de paquetes Java a escanear en busca de entidades
     * @return la {@link SessionFactory} asociada a {@code publickey} (nueva o cacheada)
     */
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

    /**
     * Recupera una {@link SessionFactory} ya registrada sin construir nada.
     *
     * @param publickey clave lógica de la fábrica
     * @return la fábrica asociada, o {@code null} si no existe
     */
    public SessionFactory getPSF(String publickey) {
        return mapPSF.get(publickey);
    }

    /**
     * Cierra y elimina del registro la {@link SessionFactory} asociada a la
     * clave dada, liberando sus recursos (conexiones del pool, cachés, etc.).
     * No hace nada si no existe ninguna fábrica con esa clave.
     *
     * @param publickey clave lógica de la fábrica a destruir
     */
    public void destroyPSF(String publickey) {
        if (mapPSF.get(publickey) != null) {
            mapPSF.get(publickey).close();
            mapPSF.remove(publickey);
        }

    }

    /**
     * Devuelve el mapa interno de fábricas registradas.
     * <p>
     * Pensado para inspección/diagnóstico; modificarlo directamente afecta al
     * estado del singleton.
     *
     * @return el mapa de {@link SessionFactory} indexadas por clave pública
     */
    public HashMap<String, SessionFactory> getMapPSF() {
        return mapPSF;
    }
}
