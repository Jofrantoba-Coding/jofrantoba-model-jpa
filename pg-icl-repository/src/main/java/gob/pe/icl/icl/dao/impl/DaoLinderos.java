/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoLinderos;
import gob.pe.icl.icl.entity.Linderos;
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
public class DaoLinderos extends AbstractJpaDao<Linderos> implements InterDaoLinderos {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoLinderos() {
        super();
        this.setClazz(Linderos.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<Linderos> listarFilter(Long idLoteCatastral,Long idTipoLindero)throws Exception {        
        String[] joinTables={"left:tipoLindero","inner:loteCatastral"};
        List<String> filter=new ArrayList();
        filter.add("=:base.isPersistente:true");
        if(idLoteCatastral!=null){
            filter.add("=:loteCatastral.id:"+idLoteCatastral);
        }
        if(idTipoLindero!=null){
            filter.add("=:tipoLindero.id:"+idTipoLindero);
        }
        String[] mapFilterField=filter.toArray(new String[0]);        
        String[] mapOrder={"base.id:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));              
        strFileds.append(share.append("base.colindancia as colindancia,"));              
        strFileds.append(share.append("base.medidaCampo as medidaCampo,"));                      
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));        
        strFileds.append(share.append("tipoLindero.id as idTipoLindero,"));
        strFileds.append(share.append("tipoLindero.descripcion as descripcionTipoLindero"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Linderos bean=new Linderos();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Linderos> lista=(Collection<Linderos>)this.customFieldsJoinFilterAnd(rt,fields,joinTables,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="icl.catastro.tgv_linderos as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("base.colindancia as colindancia,"));              
        strFileds.append(share.append("base.medida_campo as medidaCampo,"));                      
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("tipoLindero.id as idTipoLindero,"));
        strFileds.append(share.append("tipoLindero.descripcion as descripcionTipoLindero"));
        String fields=strFileds.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:icl.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";        
        joinTables[1]="left:icl.catastro.tm_parametrias as tipoLindero:on:base.id_tipo_lindero:tipoLindero.id";                
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="icl.catastro.tgv_linderos as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));   
        strFileds.append(share.append("base.colindancia as colindancia,"));              
        strFileds.append(share.append("base.medida_campo as medidaCampo,")); 
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("tipoLindero.id as idTipoLindero,"));
        strFileds.append(share.append("tipoLindero.descripcion as descripcionTipoLindero"));
        String fields=strFileds.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:icl.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";        
        joinTables[1]="left:icl.catastro.tm_parametrias as tipoLindero:on:base.id_tipo_lindero:tipoLindero.id";                
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
    public Long countTipoLindero(Long idTipoLindero) throws Exception {
        String joinTable="inner:tipoLindero";
        String[] mapFilterField={"=:tipoLindero.id:"+idTipoLindero,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
