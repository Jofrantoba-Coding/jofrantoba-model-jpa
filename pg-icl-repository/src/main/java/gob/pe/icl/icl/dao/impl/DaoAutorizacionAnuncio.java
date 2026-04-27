/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoAutorizacionAnuncio;
import gob.pe.icl.icl.dto.beans.FilterAutorizacionAnuncio;
import gob.pe.icl.icl.entity.AutorizacionAnuncio;
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
public class DaoAutorizacionAnuncio extends AbstractJpaDao<AutorizacionAnuncio> implements InterDaoAutorizacionAnuncio {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoAutorizacionAnuncio() {
        super();
        this.setClazz(AutorizacionAnuncio.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterAutorizacionAnuncio filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterAutorizacionAnuncio filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }

    @Override
    public ArrayNode listar(FilterAutorizacionAnuncio filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    private Map<String, Object> buildQueryList(FilterAutorizacionAnuncio filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tgv_autorizacion_anuncio as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("actividadEconomica.id as idActividadEconomica,"));
        strFileds.append(share.append("actividadEconomica.nombre_comercial as nombreComercialActividadEconomica,"));
        strFileds.append(share.append("anuncio.id as idAnuncio,"));
        strFileds.append(share.append("anuncio.descripcion as descripcionAnuncio,"));
        strFileds.append(share.append("base.nro_lados as nroLados,"));
        strFileds.append(share.append("base.area_autorizada_anuncio as areaAutorizadaAnuncio,"));
        strFileds.append(share.append("base.area_verificada_anuncio as areaVerificadaAnuncio,"));
        strFileds.append(share.append("base.nro_expediente as nroExpediente,"));
        strFileds.append(share.append("base.nro_licencia as nroLicencia,"));
        strFileds.append(share.append("base.fecha_expedicion as fechExpedicion,"));
        strFileds.append(share.append("base.fecha_vencimiento as fechaVencimiento"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:icl.catastro.tgv_actividad_economica as actividadEconomica:on:base.id_actividad_economica:actividadEconomica.id";
        joinTables[1] = "left:icl.catastro.tm_parametrias as anuncio:on:base.id_anuncio:anuncio.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");

        if (filter.getIdActividadEconomica() != null) {
            filters.add("=:actividadEconomica.id:" + filter.getIdActividadEconomica());
        }
        if (filter.getIdAnuncio() != null) {
            filters.add("=:anuncio.id:" + filter.getIdAnuncio());
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
    public Long countAnuncio(Long idAnuncio) throws Exception {
        String joinTable = "inner:anuncio";
        String[] mapFilterField = {"=:anuncio.id:" + idAnuncio, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
