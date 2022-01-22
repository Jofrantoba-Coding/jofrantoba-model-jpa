/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.oracle.daoaduana;

import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;

/**
 *
 * @author jona
 */
public class DaoAduana extends AbstractJpaDao<Aduana> 
        implements InterDaoAduana{
    public DaoAduana(){
        super();
        setClazz(Aduana.class);        
    }
    
}
