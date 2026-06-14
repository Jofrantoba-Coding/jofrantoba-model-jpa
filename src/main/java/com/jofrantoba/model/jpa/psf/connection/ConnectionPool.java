/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.jofrantoba.model.jpa.psf.connection;

import java.util.Properties;

/**
 * Estrategia de <em>pool</em> de conexiones JDBC aplicable a la configuración
 * de Hibernate.
 * <p>
 * Cada constante encapsula el ajuste de las propiedades necesarias para activar
 * el proveedor de conexiones correspondiente y sus parámetros de tamaño y
 * tiempos de vida. Se aplica mediante el método plantilla {@link #apply(Properties)},
 * que invocan las subclases de {@link AbstractConnectionProperties} al final de
 * su método {@code getProperties()}.
 * <p>
 * Valor por defecto en {@link AbstractConnectionProperties}: {@link #C3P0}.
 *
 * @author JOFRANTOBA
 */
public enum ConnectionPool {
      /**
       * Pool C3P0. Configura {@code C3P0ConnectionProvider} con incremento de
       * adquisición de 5, tamaño mínimo 5, máximo 20, hasta 50 sentencias en
       * caché y un timeout de inactividad de 1800&nbsp;s.
       */
      C3P0 {
          @Override
          public void apply(Properties props) {
              props.setProperty("hibernate.connection.provider_class",
                  "org.hibernate.c3p0.internal.C3P0ConnectionProvider");
              props.setProperty("hibernate.c3p0.acquire_increment", "5");
              props.setProperty("hibernate.c3p0.idle_test_period", "3000");
              props.setProperty("hibernate.c3p0.min_size", "5");
              props.setProperty("hibernate.c3p0.max_size", "20");
              props.setProperty("hibernate.c3p0.max_statements", "50");
              props.setProperty("hibernate.c3p0.timeout", "1800");
              props.setProperty("hibernate.c3p0.acquireRetryAttempts", "1");
              props.setProperty("hibernate.c3p0.acquireRetryDelay", "250");
          }
      },
      /**
       * Pool HikariCP. Configura {@code HikariCPConnectionProvider} con un
       * mínimo de 5 conexiones en reposo, un máximo de 20 en el pool, timeout
       * de inactividad de 5&nbsp;min, timeout de conexión de 30&nbsp;s y tiempo
       * de vida máximo de 30&nbsp;min por conexión.
       */
      HIKARI {
          @Override
          public void apply(Properties props) {
              props.setProperty("hibernate.connection.provider_class",
                  "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
              props.setProperty("hibernate.hikari.minimumIdle", "5");
              props.setProperty("hibernate.hikari.maximumPoolSize", "20");
              props.setProperty("hibernate.hikari.idleTimeout", "300000");      // 5 min
              props.setProperty("hibernate.hikari.connectionTimeout", "30000"); // 30 s
              props.setProperty("hibernate.hikari.maxLifetime", "1800000");     // 30 min
          }
      };

      /**
       * Aplica sobre el objeto de propiedades dado los ajustes de Hibernate
       * propios de esta estrategia de pool (clase del proveedor de conexiones
       * y parámetros de tamaño/tiempos).
       *
       * @param props propiedades de Hibernate a las que se añadirá la
       *              configuración del pool; se modifican <em>in situ</em>
       */
      public abstract void apply(Properties props);

}
