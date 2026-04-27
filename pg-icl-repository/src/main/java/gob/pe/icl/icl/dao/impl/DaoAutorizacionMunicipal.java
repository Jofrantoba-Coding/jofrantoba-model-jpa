/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoAutorizacionMunicipal;
import gob.pe.icl.icl.dto.beans.FilterAutorizacionMunicipal;
import gob.pe.icl.icl.entity.AutorizacionMunicipal;
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
public class DaoAutorizacionMunicipal extends AbstractJpaDao<AutorizacionMunicipal> implements InterDaoAutorizacionMunicipal {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoAutorizacionMunicipal() {
        super();
        this.setClazz(AutorizacionMunicipal.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterAutorizacionMunicipal filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterAutorizacionMunicipal filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }

    @Override
    public ArrayNode listar(FilterAutorizacionMunicipal filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    private Map<String, Object> buildQueryList(FilterAutorizacionMunicipal filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tgv_autorizacion_municipal as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("actividadEconomica.id as idActividadEconomica,"));
        strFileds.append(share.append("actividadEconomica.nombre_comercial as nombreComercialActividadEconomica,"));
        strFileds.append(share.append("tipoActEconoEspecifico.id as idTipoActEconoEspecifico,"));
        strFileds.append(share.append("tipoActEconoEspecifico.descripcion as descripcionTipoActEconoEspecifico"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:icl.catastro.tgv_actividad_economica as actividadEconomica:on:base.id_actividad_economica:actividadEconomica.id";
        joinTables[1] = "inner:icl.catastro.tm_tipo_act_econo_especifico as tipoActEconoEspecifico:on:base.id_tipo_act_econo_especifico:tipoActEconoEspecifico.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");

        if (filter.getIdActividadEconomica() != null) {
            filters.add("=:actividadEconomica.id:" + filter.getIdActividadEconomica());
        }
        if (filter.getIdTipoActEconoEspecifico() != null) {
            filters.add("=:tipoActEconoEspecifico.id:" + filter.getIdTipoActEconoEspecifico());
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
    public Long countActividadEconomica(Long idActividadEconomica) throws Exception {
        String joinTable = "inner:actividadEconomica";
        String[] mapFilterField = {"=:actividadEconomica.id:" + idActividadEconomica, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoActEconoEspecifico(Long idTipoActEconoEspecifico) throws Exception {
        String joinTable = "inner:tipoActEconoEspecifico";
        String[] mapFilterField = {"=:tipoActEconoEspecifico.id:" + idTipoActEconoEspecifico, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
