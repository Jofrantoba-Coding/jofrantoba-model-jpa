/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.mysql.daoempleado;

import com.jofrantoba.model.jpa.daoentity.test.TestAbstract;
import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.math.BigDecimal;
import java.util.Date;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jona
 */
public class TestInsertEmpleado extends TestAbstract{
    @Test                
    void createEntity1() throws UnknownException {
        Empleado emp=new Empleado();
        //emp.setId(1l);
        emp.setNombres("Jonathan");
        emp.setApellidos("Torres");
        emp.setCargo("Developer");
        emp.setFechaNacimiento(new Date());
        emp.setFechaVinculacion(new Date());
        emp.setIsPersistente(Boolean.TRUE);
        emp.setNumeroDocumento("45329234");
        emp.setSalario(BigDecimal.ZERO);
        emp.setTipoDocumento("DNI");
        emp.setVersion(Long.MIN_VALUE);
        DaoEmpleado dao=new DaoEmpleado();
        SessionFactory sesionFactory=PSF.getClassPSF().getPSFStatic();
        dao.setSessionFactory(sesionFactory);
        Transaction tx=sesionFactory.getCurrentSession().beginTransaction();
        dao.save(emp);
        tx.commit();
    }
}
