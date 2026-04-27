/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoUsoEspecifico;
import gob.pe.icl.icl.entity.UsoEspecifico;
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
public class DaoUsoEspecifico extends AbstractJpaDao<UsoEspecifico> implements InterDaoUsoEspecifico {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoUsoEspecifico(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(UsoEspecifico.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<UsoEspecifico> listar(Long idUsoIntermedio)throws Exception {        
        String joinTable="inner:usoIntermedio";
        String[] mapFilterField={"=:usoIntermedio.id:"+idUsoIntermedio,"=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("usoIntermedio.id as idUsoIntermedio,"));
        strFileds.append(share.append("usoIntermedio.descripcion as descripcionUsoIntermedio"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                UsoEspecifico bean=new UsoEspecifico();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<UsoEspecifico> lista=(Collection<UsoEspecifico>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="icl.catastro.tm_uso_especifico as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("usoIntermedio.id as idUsoIntermedio,"));
        strFields.append(share.append("usoIntermedio.descripcion as descripcionUsoIntermedio,"));
        strFields.append(share.append("usoGeneral.id as idUsoGeneral,"));
        strFields.append(share.append("usoGeneral.descripcion as descripcionUsoGeneral"));
        String fields=strFields.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:icl.catastro.tm_uso_intermedio as usoIntermedio:on:base.id_uso_intermedio:usoIntermedio.id";        
        joinTables[1]="inner:icl.catastro.tm_uso_general as usoGeneral:on:usoIntermedio.id_uso_general:usoGeneral.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet);
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="icl.catastro.tm_uso_especifico as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("usoIntermedio.id as idUsoIntermedio,"));
        strFields.append(share.append("usoIntermedio.descripcion as descripcionUsoIntermedio,"));
        strFields.append(share.append("usoGeneral.id as idUsoGeneral,"));
        strFields.append(share.append("usoGeneral.descripcion as descripcionUsoGeneral"));
        String fields=strFields.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:icl.catastro.tm_uso_intermedio as usoIntermedio:on:base.id_uso_intermedio:usoIntermedio.id";        
        joinTables[1]="inner:icl.catastro.tm_uso_general as usoGeneral:on:usoIntermedio.id_uso_general:usoGeneral.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
    
    @Override
    public Long count(Long idUsoIntermedio) throws Exception {
        String joinTable="inner:usoIntermedio";
        String[] mapFilterField={"=:usoIntermedio.id:"+idUsoIntermedio,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
