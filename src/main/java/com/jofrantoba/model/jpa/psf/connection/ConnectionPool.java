/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.jofrantoba.model.jpa.psf.connection;

import java.util.Properties;

/**
 *
 * @author JOFRANTOBA
 */
public enum ConnectionPool {
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

      public abstract void apply(Properties props);

}
