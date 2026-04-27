/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoRangoCuc;
import gob.pe.icl.icl.entity.RangoCuc;
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
public class DaoRangoCuc extends AbstractJpaDao<RangoCuc> implements InterDaoRangoCuc {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoRangoCuc() {
        super();
        this.setClazz(RangoCuc.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<RangoCuc> listar(Long idDistrito)throws Exception {        
        String joinTable="inner:distrito";
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};
        String[] mapOrder={"distrito.descripcion:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.inicio as inicio,"));
        strFileds.append(share.append("base.fin as fin,"));
        strFileds.append(share.append("base.actual as actual,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                RangoCuc bean=new RangoCuc();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<RangoCuc> lista=(Collection<RangoCuc>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="icl.catastro.tm_rango_cuc as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.inicio as inicio,"));
        strFields.append(share.append("base.final as fin,"));
        strFields.append(share.append("base.actual as actual,"));
        strFields.append(share.append("distrito.id as idDistrito,"));
        strFields.append(share.append("distrito.descripcion as descripcionDistrito"));                
        String fields=strFields.toString();
        String[] joinTables=new String[1];
        joinTables[0]="inner:icl.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";                
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet);
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="icl.catastro.tm_rango_cuc as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.inicio as inicio,"));
        strFields.append(share.append("base.final as fin,"));
        strFields.append(share.append("base.actual as actual,"));
        strFields.append(share.append("distrito.id as idDistrito,"));
        strFields.append(share.append("distrito.descripcion as descripcionDistrito"));                
        String fields=strFields.toString();
        String[] joinTables=new String[1];
        joinTables[0]="inner:icl.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";                
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
    
    @Override
    public Long count(Long idDistrito) throws Exception {
        String joinTable="inner:distrito";
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
