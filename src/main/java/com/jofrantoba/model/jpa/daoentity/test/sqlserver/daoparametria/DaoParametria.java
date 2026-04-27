/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.sqlserver.daoparametria;

import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;

/**
 *
 * @author apoyo1953
 */
public class DaoParametria extends AbstractJpaDao<Parametria>
        implements InterDaoParametria {

    public DaoParametria() {
        super();
        setClazz(Parametria.class);
    }

}