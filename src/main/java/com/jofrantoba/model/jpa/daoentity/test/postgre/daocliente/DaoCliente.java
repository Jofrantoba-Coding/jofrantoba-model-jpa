/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente;

import com.jofrantoba.model.jpa.daoentity.test.mysql.daoempleado.*;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;

/**
 *
 * @author jona
 */
public class DaoCliente extends AbstractJpaDao<Cliente>
        implements InterDaoCliente {

    public DaoCliente() {
        super();
        setClazz(Cliente.class);
    }

}
