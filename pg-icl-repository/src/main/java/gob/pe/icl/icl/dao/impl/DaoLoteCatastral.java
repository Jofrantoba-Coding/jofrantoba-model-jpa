/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoLoteCatastral;
import gob.pe.icl.icl.dto.beans.CodigoLoteCatastral;
import gob.pe.icl.icl.dto.beans.FilterLoteCatastral;
import gob.pe.icl.icl.entity.LoteCatastral;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Slf4j
@Repository
public class DaoLoteCatastral extends AbstractJpaDao<LoteCatastral> implements InterDaoLoteCatastral {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoLoteCatastral() {
        super();
        this.setClazz(LoteCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }
    
    @Override
    public ArrayNode listarFilter(FilterLoteCatastral filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String groupBy = (String) map.get("groupBy");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String fieldsSq = (String) map.get("fieldsSq");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBySq = (String) map.get("groupBySq");
        return this.allFieldsJoinPostgresGroupBySubQuery(fields, groupBy, mapOrder, joinTables, table, fieldsSq, mapFilterField, groupBySq);
    }
    
    private boolean strValid(String value) {
        return (value != null && !value.trim().isEmpty());
    }
    
    @Override
    public ArrayNode listar(FilterLoteCatastral filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String groupBy = (String) map.get("groupBy");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String fieldsSq = (String) map.get("fieldsSq");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBySq = (String) map.get("groupBySq");
        return this.allFieldsLimitJoinPostgresGroupBySubQuery(fields, groupBy, mapOrder, joinTables, table, fieldsSq, mapFilterField, groupBySq, limit, offSet);
    }
    
    @Override
    public ArrayNode listar(FilterLoteCatastral filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String groupBy = (String) map.get("groupBy");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String fieldsSq = (String) map.get("fieldsSq");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBySq = (String) map.get("groupBySq");
        return this.allFieldsJoinPostgresGroupBySubQuery(fields, groupBy, mapOrder, joinTables, table, fieldsSq, mapFilterField, groupBySq);
    }
    
    private Map<String, Object> buildQueryList(FilterLoteCatastral filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tgv_lote_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("id,"));
        strFileds.append(share.append("ubigeo,"));
        strFileds.append(share.append("codigoSector,"));
        strFileds.append(share.append("codigoManzana,"));
        strFileds.append(share.append("codigoLote,"));
        strFileds.append(share.append("idManzana,"));
        strFileds.append(share.append("descripcionManzana,"));
        //strFileds.append(share.append("descripcionHabilitacionUrbana,"));        
        strFileds.append(share.append("string_agg(direccion, '; ') AS direccion"));
        String fields = strFileds.toString();
        
        StringBuilder strFiledsSq = new StringBuilder();
        strFiledsSq.append(share.append("base.id as id,"));
        strFiledsSq.append(share.append("concat(base.codigo_departamento,base.codigo_provincia,base.codigo_distrito) as ubigeo,"));
        strFiledsSq.append(share.append("base.codigo_sector as codigoSector,"));
        strFiledsSq.append(share.append("base.codigo_manzana as codigoManzana,"));
        strFiledsSq.append(share.append("base.codigo_lote as codigoLote,"));
        strFiledsSq.append(share.append("manzana.id as idManzana,"));
        strFiledsSq.append(share.append("manzana.descripcion as descripcionManzana,"));
        //strFiledsSq.append(share.append("habilitacionUrbana.id as idHabilitacionUrbana,"));
        //strFiledsSq.append(share.append("habilitacionUrbana.descripcion as descripcionHabilitacionUrbana,"));
        strFiledsSq.append(share.append("concat(tipoVia.descripcion,' ',via.descripcion,' N° ',string_agg(distinct numeracion.numero,',')) as direccion"));
        String fieldsSq = strFiledsSq.toString();
        
        String[] joinTables = new String[7];
        joinTables[0] = "inner:icl.catastro.tg_manzana as manzana:on:base.id_manzana:manzana.id";
        joinTables[1] = "inner:icl.catastro.tg_sector as sector:on:manzana.id_sector:sector.id";
        //joinTables[2] = "inner:icl.catastro.tm_habilitacion_urbana as habilitacionUrbana:on:base.id_habilitacion_urbana:habilitacionUrbana.id";
        joinTables[2] = "left:(select * from icl.catastro.tgv_numeracion where is_persistente=true) as numeracion:on:base.id:numeracion.id_lote_catastral";
        joinTables[3] = "left:(select * from icl.catastro.tg_manzana_via where is_persistente=true) as manzanaVia:on:numeracion.id_manzana_via:manzanaVia.id";
        joinTables[4] = "left:(select * from icl.catastro.tg_via_cuadra where is_persistente=true) as viaCuadra:on:manzanaVia.id_via_cuadra:viaCuadra.id";
        joinTables[5] = "left:(select * from icl.catastro.tm_via where is_persistente=true) as via:on:viaCuadra.id_via:via.id";
        joinTables[6] = "left:(select * from icl.catastro.tm_parametrias where id_parametria_padre=1 and is_persistente=true) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        filters.add("=:manzana.is_persistente:true");
        filters.add("=:sector.is_persistente:true");
        if (strValid(filter.getCodigoDepartamento())) {
            filters.add("equal:base.codigo_departamento:" + filter.getCodigoDepartamento());
        }
        if (strValid(filter.getCodigoProvincia())) {
            filters.add("equal:base.codigo_provincia:" + filter.getCodigoProvincia());
        }
        if (strValid(filter.getCodigoDistrito())) {
            filters.add("equal:base.codigo_distrito:" + filter.getCodigoDistrito());
        }
        if (strValid(filter.getCodigoSector())) {
            filters.add("equal:base.codigo_sector:" + filter.getCodigoSector());
        }
        if (strValid(filter.getCodigoManzana())) {
            filters.add("equal:base.codigo_manzana:" + filter.getCodigoManzana());
        }
        if (strValid(filter.getCodigoLote())) {
            filters.add("equal:base.codigo_lote:" + filter.getCodigoLote());
        }
        if (filter.getIdManzana() != null) {
            filters.add("=:manzana.id:" + filter.getIdManzana());
        }
        /*if (filter.getIdHabilitacionUrbana() != null) {
            filters.add("=:habilitacionUrbana.id:" + filter.getIdHabilitacionUrbana());
        }*/
        if (filter.getIdDistrito() != null) {
            filters.add("=:sector.id_distrito:" + filter.getIdDistrito());
        }
        if (filter.getIdVia() != null) {
            filters.add("=:via.id:" + filter.getIdVia());
        }
        if (strValid(filter.getNumero())) {
            filters.add("equal:numeracion.numero:" + filter.getNumero());
        }
        if (strValid(filter.getLetra())) {
            filters.add("equal:numeracion.letra:" + filter.getLetra());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        StringBuilder groupBySq = new StringBuilder();
        groupBySq.append(share.append("base.id,"));        
        groupBySq.append(share.append("base.codigo_departamento,"));
        groupBySq.append(share.append("base.codigo_provincia,"));
        groupBySq.append(share.append("base.codigo_distrito,"));
        groupBySq.append(share.append("base.codigo_sector,"));
        groupBySq.append(share.append("base.codigo_manzana,"));
        groupBySq.append(share.append("base.codigo_lote,"));
        groupBySq.append(share.append("manzana.id,"));
        groupBySq.append(share.append("manzana.descripcion,"));
        //groupBySq.append(share.append("habilitacionUrbana.id,"));
        //groupBySq.append(share.append("habilitacionUrbana.descripcion,"));
        groupBySq.append(share.append("tipoVia.descripcion,"));
        groupBySq.append(share.append("via.descripcion"));
        
        StringBuilder groupBy = new StringBuilder();
        groupBy.append(share.append("id,"));
        groupBy.append(share.append("ubigeo,"));
        groupBy.append(share.append("codigoSector,"));
        groupBy.append(share.append("codigoManzana,"));
        groupBy.append(share.append("codigoLote,"));
        groupBy.append(share.append("idManzana,"));
        groupBy.append(share.append("descripcionManzana"));
        //groupBy.append(share.append("descripcionHabilitacionUrbana"));
        String[] mapOrder = {"ubigeo:asc","codigoSector:asc","codigoManzana:asc","codigoLote:asc"};
        map.put("fields", fields);
        map.put("groupBy", groupBy.toString());
        map.put("mapOrder", mapOrder);
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fieldsSq", fieldsSq);
        map.put("mapFilterField", mapFilterField);
        map.put("groupBySq", groupBySq.toString());
        return map;
    }
    
    @Override
    public Long countManzana(Long idManzana) throws Exception {
        String joinTable = "inner:manzana";
        String[] mapFilterField = {"=:manzana.id:" + idManzana, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
    
    /*@Override
    public Long countHabilitacionUrbana(Long idHabilitacionUrbana) throws Exception {
        String joinTable = "inner:habilitacionUrbana";
        String[] mapFilterField = {"=:habilitacionUrbana.id:" + idHabilitacionUrbana, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }*/
    
    @Override
    public String newValueCodigoLote(CodigoLoteCatastral codigoLoteCatastral) throws Exception {
        String fields = "cast(coalesce(max(base.codigoLote),'000') as long)+1";
        String[] mapFilterField = new String[5];        
        mapFilterField[0] = "equal:base.codigoDepartamento:" + codigoLoteCatastral.getCodigoDepartamento();
        mapFilterField[1] = "equal:base.codigoProvincia:" + codigoLoteCatastral.getCodigoProvincia();
        mapFilterField[2] = "equal:base.codigoDistrito:" + codigoLoteCatastral.getCodigoDistrito();
        mapFilterField[3] = "equal:base.codigoSector:" + codigoLoteCatastral.getCodigoSector();
        mapFilterField[4] = "equal:base.codigoManzana:" + codigoLoteCatastral.getCodigoManzana();
        Long valueNew = this.aggregateJoinFilterAndGroupBy(fields, null, mapFilterField, null);
        String codigo = "000" + valueNew;
        codigo = codigo.substring(codigo.length() - 3, codigo.length());
        return codigo;
    }
    
    @Override
    public ArrayNode listarLotes(Long idManzana) throws Exception {
        String table = "icl.catastro.tgv_lote_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("base.codigo_lote as codigoLote"));        
        String fieldsSq = strFileds.toString();        
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (idManzana != null) {
            filters.add("=:base.id_manzana:" + idManzana);
        }        
        String[] mapFilterField = filters.toArray(new String[0]);  
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsPostgres(table, fieldsSq, mapFilterField, mapOrder);
    }
}
