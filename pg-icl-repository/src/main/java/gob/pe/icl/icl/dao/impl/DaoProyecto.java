/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import gob.pe.icl.icl.dao.inter.InterDaoProyecto;
import gob.pe.icl.icl.dto.beans.FilterProyecto;
import gob.pe.icl.icl.entity.Proyecto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class DaoProyecto extends AbstractJpaDao<Proyecto> implements InterDaoProyecto {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoProyecto() {
        super();
        this.setClazz(Proyecto.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<Proyecto> listar(FilterProyecto filter) throws Exception {
        String[] joinTables = {"inner:distrito:fetch","inner:provincia:fetch","inner:departamento:fetch"};
        List<String> filters = new ArrayList();
        filters.add("=:base.isPersistente:true");
        if (filter.getIdDistrito() != null) {
            filters.add("=:distrito.id:" + filter.getIdDistrito());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder={"base.descripcion:asc"};
        return this.allFieldsJoinFilterAnd(joinTables,mapFilterField, mapOrder);
    }

    @Override
    public Collection<Proyecto> listar(FilterProyecto filter, int pageNumber, int pageSize) throws Exception {
        String joinTable = "inner:distrito:fetch";
        List<String> filters = new ArrayList();
        filters.add("=:base.isPersistente:true");
        if (filter.getIdDistrito() != null) {
            filters.add("=:distrito.id:" + filter.getIdDistrito());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder={"base.descripcion:asc"};
        return this.allFieldsJoinFilterAnd(joinTable,mapFilterField, mapOrder,pageNumber,pageSize);
    }
    
}

