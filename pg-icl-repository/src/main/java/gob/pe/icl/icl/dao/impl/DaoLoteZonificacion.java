/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoLoteZonificacion;
import gob.pe.icl.icl.entity.LoteZonificacion;
import java.util.ArrayList;
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
public class DaoLoteZonificacion extends AbstractJpaDao<LoteZonificacion> implements InterDaoLoteZonificacion {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoLoteZonificacion() {
        super();
        this.setClazz(LoteZonificacion.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<LoteZonificacion> listarFilter(Long idLoteCatastral,Long idZonficacion)throws Exception {        
        String[] joinTables={"inner:zonificacion","inner:loteCatastral"};
        List<String> filter=new ArrayList();
        filter.add("=:base.isPersistente:true");
        if(idLoteCatastral!=null){
            filter.add("=:loteCatastral.id:"+idLoteCatastral);
        }
        if(idZonficacion!=null){
            filter.add("=:zonificacion.id:"+idZonficacion);
        }
        String[] mapFilterField=filter.toArray(new String[0]);        
        String[] mapOrder={"base.id:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));              
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));        
        strFileds.append(share.append("zonificacion.id as idZonificacion,"));
        strFileds.append(share.append("zonificacion.descripcion as descripcionZonificacion"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                LoteZonificacion bean=new LoteZonificacion();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<LoteZonificacion> lista=(Collection<LoteZonificacion>)this.customFieldsJoinFilterAnd(rt,fields,joinTables,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="icl.catastro.tgv_lote_zonificacion as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("zonificacion.id as idZonificacion,"));
        strFileds.append(share.append("zonificacion.descripcion as descripcionZonificacion"));
        String fields=strFileds.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:icl.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";        
        joinTables[1]="inner:icl.catastro.tm_zonificacion as zonificacion:on:base.id_zonificacion:zonificacion.id";                
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="icl.catastro.tgv_lote_zonificacion as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("zonificacion.id as idZonificacion,"));
        strFileds.append(share.append("zonificacion.descripcion as descripcionZonificacion"));
        String fields=strFileds.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:icl.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";        
        joinTables[1]="inner:icl.catastro.tm_zonificacion as zonificacion:on:base.id_zonificacion:zonificacion.id";                
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
    
    @Override
    public Long countLoteCatastral(Long idLoteCatastral) throws Exception {
        String joinTable="inner:loteCatastral";
        String[] mapFilterField={"=:loteCatastral.id:"+idLoteCatastral,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
    
    @Override
    public Long countZonificacion(Long idZonificacion) throws Exception {
        String joinTable="inner:zonificacion";
        String[] mapFilterField={"=:zonificacion.id:"+idZonificacion,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public Long countLoteZonificacion(Long idLoteCatastral, Long idZonificacion) throws Exception {
        String[] joinTables = new String[2];
        joinTables[0] = "inner:zonificacion";
        joinTables[1] = "inner:loteCatastral";
        String[] mapFilterField={"=:zonificacion.id:"+idZonificacion,"=:loteCatastral.id:"+idLoteCatastral,"=:base.isPersistente:true"};        
        return this.rowCountJoinsFilterAnd(joinTables,mapFilterField); 
    }
}
