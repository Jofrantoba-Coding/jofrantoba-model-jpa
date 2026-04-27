/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoCucUnidad;
import gob.pe.icl.icl.entity.CucUnidad;
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
public class DaoCucUnidad extends AbstractJpaDao<CucUnidad> implements InterDaoCucUnidad{
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoCucUnidad(){
        super();
        this.setClazz(CucUnidad.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<CucUnidad> listar(Long idLoteCatastral) throws Exception {
        String joinTable = "inner:loteCatastral";
        String[] mapFilterField = {"=:loteCatastral.id:" + idLoteCatastral, "=:base.isPersistente:true"};
        StringBuilder strFields = new StringBuilder();
        Shared share = new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.actual as actual,"));
        strFields.append(share.append("loteCatastral.id as idLoteCatastral"));
        String fields = strFields.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                CucUnidad bean = new CucUnidad();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<CucUnidad> lista=(Collection<CucUnidad>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, null);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offset) throws Exception {
        String table = "icl.catastro.tm_cuc_unidad as base";
        StringBuilder strFields = new StringBuilder();
        Shared share = new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.actual as actual,"));
        strFields.append(share.append("loteCatastral.id as idLoteCatastral"));
        String fields = strFields.toString();
        String[] joinTables = new String[1];
        joinTables[0] = "inner:icl.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField,mapOrder,limit,offset);
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "icl.catastro.tm_cuc_unidad as base";
        StringBuilder strFields = new StringBuilder();
        Shared share = new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.actual as actual,"));
        strFields.append(share.append("loteCatastral.id as idLoteCatastral"));
        String fields = strFields.toString();
        String[] joinTables = new String[1];
        joinTables[0] = "inner:icl.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }

    @Override
    public Long count(Long idLoteCatastral) throws Exception {
        String joinTable = "inner:loteCatastral";
        String[] mapFilterField = {"=:loteCatastral.id:" + idLoteCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
