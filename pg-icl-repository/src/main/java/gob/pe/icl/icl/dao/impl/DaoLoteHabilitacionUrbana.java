/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoLoteHabilitacionUrbana;
import gob.pe.icl.icl.dto.beans.FilterLoteHabilitacionUrbana;
import gob.pe.icl.icl.entity.LoteHabilitacionUrbana;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class DaoLoteHabilitacionUrbana extends AbstractJpaDao<LoteHabilitacionUrbana> implements InterDaoLoteHabilitacionUrbana {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoLoteHabilitacionUrbana() {
        super();
        this.setClazz(LoteHabilitacionUrbana.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterLoteHabilitacionUrbana filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterLoteHabilitacionUrbana filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }

    @Override
    public ArrayNode listar(FilterLoteHabilitacionUrbana filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    private Map<String, Object> buildQueryList(FilterLoteHabilitacionUrbana filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tgv_lote_habilitacion_urbana as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));
        strFileds.append(share.append("habilitacionUrbana.id as idHabilitacionUrbana,"));
        strFileds.append(share.append("habilitacionUrbana.descripcion as descripcionHabilitacionUrbana,"));
        strFileds.append(share.append("base.zona_sector_etapa as zonaSectorEtapa,"));
        strFileds.append(share.append("base.manzana_urbana as manzanaUrbana,"));
        strFileds.append(share.append("base.lote_urbano as loteUrbana,"));
        strFileds.append(share.append("base.sub_lote_urbano as subLote,"));
        strFileds.append(share.append("base.codigo_lote_catastral as codigoLoteCatastral"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:icl.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        joinTables[1] = "left:icl.catastro.tm_habilitacion_urbana as habilitacionUrbana:on:base.id_habilitacion_urbana:habilitacionUrbana.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");

        if (filter.getIdLoteCatastral()!= null) {
            filters.add("=:loteCatastral.id:" + filter.getIdLoteCatastral());
        }
        if (filter.getIdHabilitacionUrbana()!= null) {
            filters.add("=:habilitacionUrbana.id:" + filter.getIdHabilitacionUrbana());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countLoteCatastral(Long idLoteCatastral) throws Exception {
        String joinTable = "inner:loteCatastral";
        String[] mapFilterField = {"=:loteCatastral.id:" + idLoteCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countHabilitacionUrbana(Long idHabilitacionUrbana) throws Exception {
        String joinTable = "inner:habilitacionUrbana";
        String[] mapFilterField = {"=:habilitacionUrbana.id:" + idHabilitacionUrbana, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countLoteHabilitacionUrbana(Long idLoteCatastral, Long idHabilitacionUrbana) throws Exception {
        String[] joinTables = new String[2];
        joinTables[0] = "inner:habilitacionUrbana";
        joinTables[1] = "inner:loteCatastral";
        String[] mapFilterField={"=:habilitacionUrbana.id:"+idHabilitacionUrbana,"=:loteCatastral.id:"+idLoteCatastral,"=:base.isPersistente:true"};        
        return this.rowCountJoinsFilterAnd(joinTables,mapFilterField); 
    }
}
