/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import gob.pe.icl.icl.dao.inter.InterDaoDepartamento;
import gob.pe.icl.icl.entity.Departamento;
import java.util.Collection;
import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoDepartamento extends AbstractJpaDao<Departamento> implements InterDaoDepartamento {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoDepartamento() {
        super();
        this.setClazz(Departamento.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<Departamento> listar() throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder);
    }

    @Override
    public Collection<Departamento> listar(int pageNumber, int pageSize) throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder,pageNumber,pageSize);
    }
    
}

