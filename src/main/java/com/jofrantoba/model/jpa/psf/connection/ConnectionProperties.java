/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.psf.connection;

import java.util.Properties;

/**
 * Contrato para los objetos que aportan la configuración de conexión de
 * Hibernate en forma de {@link Properties}.
 * <p>
 * Implementar esta interfaz permite desacoplar la creación del
 * {@link org.hibernate.SessionFactory} (ver {@link com.jofrantoba.model.jpa.psf.PSF})
 * del motor concreto: cada base de datos provee sus propias propiedades
 * (driver, URL, credenciales, dialecto, pool de conexiones, etc.).
 *
 * @author jona
 * @see AbstractConnectionProperties
 */
public interface ConnectionProperties {

    /**
     * Construye y devuelve las propiedades de configuración listas para
     * entregarse a Hibernate (típicamente vía
     * {@link org.hibernate.cfg.Configuration#setProperties(Properties)}).
     *
     * @return el conjunto de propiedades de conexión de Hibernate
     */
    Properties getProperties();
}
