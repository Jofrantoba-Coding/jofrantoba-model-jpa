/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import gob.pe.icl.icl.dao.inter.InterDaoParametrias;
import gob.pe.icl.icl.entity.Parametrias;
import java.util.Collection;
import java.util.List;
import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoParametrias extends AbstractJpaDao<Parametrias> implements InterDaoParametrias {
    
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoParametrias() {
        super();
        this.setClazz(Parametrias.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<Parametrias> parents()throws Exception{
        String joinTable="left:parent";
        String[] mapFilterField={"isnull:parent.id","=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        String fields="base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura, base.codigo as codigo, base.orden as orden";
        Collection<Parametrias> lista=this.customFieldsJoinFilterAnd(fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }
    
    @Override
    public Collection<Parametrias> parents(int pageNumber, int pageSize)throws Exception{
        String joinTable="left:parent";
        String[] mapFilterField={"isnull:parent.id","=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        String fields="base.id as id,base.descripcion as descripcion";
        Collection<Parametrias> lista=this.customFieldsJoinFilterAnd(fields,joinTable,mapFilterField, mapOrder,pageNumber,pageSize);        
        return lista;
    }
    
    @Override
    public Collection<Parametrias> childrens()throws Exception{        
        String joinTable="left:parent";
        String[] mapFilterField={"isnotnull:parent.id","=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        String fields="base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo,base.orden as orden,parent.id as idParent,parent.descripcion as descripcionParent";
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Parametrias bean=new Parametrias();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Parametrias> lista=(Collection<Parametrias>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public Collection<Parametrias> childrensByParents(Long idParent) throws Exception {
        String joinTable="left:parent";
        String[] mapFilterField={"=:parent.id:"+idParent,"=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        String fields="base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo,base.orden as orden,parent.id as idParent,parent.descripcion as descripcionParent";
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Parametrias bean=new Parametrias();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Parametrias> lista=(Collection<Parametrias>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public Long countChildrens(Long idParent) throws Exception {
        String joinTable="left:parent";
        String[] mapFilterField={"=:parent.id:"+idParent,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="icl.catastro.tm_parametrias as base";
        String fields="base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"id:asc"};        
        return this.allFieldsLimitOffsetPostgres(table,fields,mapFilterField,mapOrder, limit, offSet);
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="icl.catastro.tm_parametrias as base";
        String fields="base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"id:asc"};        
        return this.allFieldsPostgres(table,fields,mapFilterField,mapOrder);
    }
}
