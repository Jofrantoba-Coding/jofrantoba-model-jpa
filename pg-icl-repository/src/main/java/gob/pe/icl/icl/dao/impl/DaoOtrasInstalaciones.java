/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoOtrasInstalaciones;
import gob.pe.icl.icl.dto.beans.FilterOtrasInstalaciones;
import gob.pe.icl.icl.entity.OtrasInstalaciones;
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
public class DaoOtrasInstalaciones extends AbstractJpaDao<OtrasInstalaciones> implements InterDaoOtrasInstalaciones {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoOtrasInstalaciones() {
        super();
        this.setClazz(OtrasInstalaciones.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterOtrasInstalaciones filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterOtrasInstalaciones filter,Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder,limit,offSet);
    }

    @Override
    public ArrayNode listar(FilterOtrasInstalaciones filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }
    
    private Map<String, Object> buildQueryList(FilterOtrasInstalaciones filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tgv_otras_instalaciones as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.anio as anio,"));
        strFileds.append(share.append("base.producto_total as productoTotal,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("tipoOtrasInstalaciones.id as idTipoOtrasInstalaciones,"));
        strFileds.append(share.append("tipoOtrasInstalaciones.descripcion as tipoOtrasInstalaciones,"));
        strFileds.append(share.append("tipoOtrasInstalaciones.unidad_medida as unidadTipoOtrasInstalaciones,"));
        strFileds.append(share.append("mes.id as idMes,"));
        strFileds.append(share.append("mes.descripcion as descripcionMes,"));
        strFileds.append(share.append("materialEstructural.id as idMaterialEstructural,"));
        strFileds.append(share.append("materialEstructural.descripcion as descripcionMaterialEstructural,"));
        strFileds.append(share.append("estadoConservacion.id as idEstadoConservacion,"));
        strFileds.append(share.append("estadoConservacion.descripcion as descripcionEstadoConservacion,"));
        strFileds.append(share.append("estadoConstruccion.id as idEstadoConstruccion,"));
        strFileds.append(share.append("estadoConstruccion.descripcion as descripcionEstadoConstruccion,"));
        strFileds.append(share.append("uca.id as idUca,"));
        strFileds.append(share.append("uca.descripcion as descripcionUca"));
        String fields = strFileds.toString();
        String[] joinTables = new String[7];
        joinTables[0] = "inner:icl.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:icl.catastro.tm_tipo_otras_instalaciones as tipoOtrasInstalaciones:on:base.id_tipo_otras_instalaciones:tipoOtrasInstalaciones.id";
        joinTables[2] = "inner:icl.catastro.tm_parametrias as mes:on:base.id_mes:mes.id";
        joinTables[3] = "left:icl.catastro.tm_parametrias as materialEstructural:on:base.id_material_estructural:materialEstructural.id";
        joinTables[4] = "left:icl.catastro.tm_parametrias as estadoConservacion:on:base.id_estado_conservacion:estadoConservacion.id";
        joinTables[5] = "left:icl.catastro.tm_parametrias as estadoConstruccion:on:base.id_estado_construccion:estadoConstruccion.id";
        joinTables[6] = "left:icl.catastro.tm_parametrias as uca:on:base.id_uca:uca.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdTipoOtrasInstalaciones() != null) {
            filters.add("=:tipoOtrasInstalaciones.id:" + filter.getIdTipoOtrasInstalaciones());
        }
        if (filter.getIdEstadoConservacion() != null) {
            filters.add("=:estadoConservacion.id:" + filter.getIdEstadoConservacion());
        }
        if (filter.getIdEstadoConstruccion() != null) {
            filters.add("=:estadoConstruccion.id:" + filter.getIdEstadoConstruccion());
        }
        if (filter.getIdMaterialEstructural() != null) {
            filters.add("=:materialEstructural.id:" + filter.getIdMaterialEstructural());
        }
        if (filter.getIdMes() != null) {
            filters.add("=:mes.id:" + filter.getIdMes());
        }
        if (filter.getIdUca() != null) {
            filters.add("=:uca.id:" + filter.getIdUca());
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
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoOtrasInstalaciones(Long idTipoOtrasInstalaciones) throws Exception {
        String joinTable = "inner:tipoOtrasInstalaciones";
        String[] mapFilterField = {"=:tipoOtrasInstalaciones.id:" + idTipoOtrasInstalaciones, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long counntMes(Long counntMes) throws Exception {
        String joinTable = "inner:mes";
        String[] mapFilterField = {"=:mes.id:" + counntMes, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countMaterialEstructural(Long idMaterialEstructural) throws Exception {
        String joinTable = "inner:materialEstructural";
        String[] mapFilterField = {"=:materialEstructural.id:" + idMaterialEstructural, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countEstadoConservacion(Long idEstadoConservacion) throws Exception {
        String joinTable = "inner:estadoConservacion";
        String[] mapFilterField = {"=:estadoConservacion.id:" + idEstadoConservacion, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countEstadoConstruccion(Long idEstadoConstruccion) throws Exception {
        String joinTable = "inner:estadoConstruccion";
        String[] mapFilterField = {"=:estadoConstruccion.id:" + idEstadoConstruccion, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countUca(Long idUca) throws Exception {
        String joinTable = "inner:uca";
        String[] mapFilterField = {"=:uca.id:" + idUca, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

}
