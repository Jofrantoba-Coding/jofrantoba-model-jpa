/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoInterior;
import gob.pe.icl.icl.dto.beans.FilterInterior;
import gob.pe.icl.icl.entity.Interior;
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
public class DaoInterior extends AbstractJpaDao<Interior> implements InterDaoInterior {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoInterior() {
        super();
        this.setClazz(Interior.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterInterior filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");        
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterInterior filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterInterior filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countNumeracion(Long idNumeracion) throws Exception {
        String joinTable = "inner:numeracion";
        String[] mapFilterField = {"=:numeracion.id:" + idNumeracion, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoEdificacion(Long idTipoEdificacion) throws Exception {
        String joinTable = "inner:tipoEdificacion";
        String[] mapFilterField = {"=:tipoEdificacion.id:" + idTipoEdificacion, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoInterior(Long idTipoInterior) throws Exception {
        String joinTable = "inner:tipoInterior";
        String[] mapFilterField = {"=:tipoInterior.id:" + idTipoInterior, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    private Map<String, Object> buildQueryList(FilterInterior filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tgv_interior as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        //strFields.append(share.append("base.id_numeracion as idNumeracion,"));
        //strFields.append(share.append("base.id_tipo_edificacion as idTipoEdificacion,"));
        //strFields.append(share.append("base.id_tipo_interior as idTipoInterior,"));
        strFields.append(share.append("base.interior as interior,"));
        strFields.append(share.append("base.letra_interior as letraInterior,"));
        strFields.append(share.append("base.nombre_edificacion as nombreEdificacion,"));
        //strFields.append(share.append("base.codigo_unidad_catastral as codigoUnidadCatastral,"));
        //strFields.append(share.append("loteCatastral.codigo as codigoLoteCatastral,"));
        strFields.append(share.append("loteCatastral.id as idLoteCatastral,"));
        strFields.append(share.append("concat(unidadCatastral.codigo_departamento,unidadCatastral.codigo_provincia,unidadCatastral.codigo_distrito) as ubigeo,"));
        strFields.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFields.append(share.append("unidadCatastral.codigo_sector as codigoSector,"));
        strFields.append(share.append("unidadCatastral.codigo_manzana as codigoManzana,"));
        strFields.append(share.append("unidadCatastral.codigo_lote as codigoLote,"));
        strFields.append(share.append("unidadCatastral.codigo_edificacion as codigoEdificacion,"));
        strFields.append(share.append("unidadCatastral.codigo_entrada as codigoEntrada,"));
        strFields.append(share.append("unidadCatastral.codigo_piso as codigoPiso,"));
        strFields.append(share.append("unidadCatastral.codigo_unidad as codigoUnidad,"));
        //strFields.append(share.append("manzana.id as idManzana,"));
        strFields.append(share.append("manzana.descripcion as descripcionManzana,"));
        //strFields.append(share.append("habilitacionUrbana.id as idHabilitacionUrbana,"));
        //strFields.append(share.append("habilitacionUrbana.descripcion as descripcionHabilitacionUrbana,"));
        strFields.append(share.append("tipoVia.descripcion as descripcionTipoVia,"));
        strFields.append(share.append("via.descripcion as descripcionVia,"));
        strFields.append(share.append("via.codigo_via as codigoVia,"));
        strFields.append(share.append("viaCuadra.cuadra as cuadra,"));
        strFields.append(share.append("numeracion.numero as numero,"));
        strFields.append(share.append("tipoEdificacion.descripcion as descripcionTipoEdificacion,"));
        strFields.append(share.append("tipoInterior.descripcion as descripcionTipoInterior,"));
        strFields.append(share.append("tipoPuerta.descripcion as descripcionTipoPuerta,"));
        strFields.append(share.append("condicionNumeracion.descripcion as descripcionCondicionNumeracion"));
        String fields = strFields.toString();
        String[] joinTables = new String[13];
        joinTables[0] = "inner:icl.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:icl.catastro.tgv_lote_catastral as loteCatastral:on:unidadCatastral.id_lote_catastral:loteCatastral.id";
        joinTables[2] = "inner:icl.catastro.tg_manzana as manzana:on:loteCatastral.id_manzana:manzana.id";
        joinTables[3] = "inner:icl.catastro.tg_sector as sector:on:manzana.id_sector:sector.id";
        //joinTables[4] = "inner:icl.catastro.tm_habilitacion_urbana as habilitacionUrbana:on:loteCatastral.id_habilitacion_urbana:habilitacionUrbana.id";
        joinTables[4] = "inner:icl.catastro.tgv_numeracion as numeracion:on:base.id_numeracion:numeracion.id";
        joinTables[5] = "left:icl.catastro.tm_parametrias as condicionNumeracion:on:numeracion.id_condicion_numeracion:condicionNumeracion.id";
        joinTables[6] = "left:icl.catastro.tm_parametrias as tipoPuerta:on:numeracion.id_tipo_puerta:tipoPuerta.id";
        joinTables[7] = "left:icl.catastro.tm_parametrias as tipoEdificacion:on:base.id_tipo_edificacion:tipoEdificacion.id";
        joinTables[8] = "left:icl.catastro.tg_manzana_via as manzanaVia:on:numeracion.id_manzana_via:manzanaVia.id";
        joinTables[9] = "left:icl.catastro.tg_via_cuadra as viaCuadra:on:manzanaVia.id_via_cuadra:viaCuadra.id";
        joinTables[10] = "left:icl.catastro.tm_via as via:on:viaCuadra.id_via:via.id";
        joinTables[11] = "left:(select * from icl.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        joinTables[12] = "left:icl.catastro.tm_parametrias as tipoInterior:on:base.id_tipo_interior:tipoInterior.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:base.id_unidad_catastral:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdNumeracion() != null) {
            filters.add("=:base.id_numeracion:" + filter.getIdNumeracion());
        }
        if (filter.getIdTipoEdificacion() != null) {
            filters.add("=:base.id_tipo_edificacion:" + filter.getIdTipoEdificacion());
        }
        if (filter.getIdTipoInterior() != null) {
            filters.add("=:base.id_tipo_interior:" + filter.getIdTipoInterior());
        }
        if (strValid(filter.getLetraInterior())) {
            filters.add("equal:base.letra_interior:" + filter.getLetraInterior());
        }
        if (strValid(filter.getCodigoDepartamento())) {
            filters.add("equal:unidadCatastral.codigo_departamento:" + filter.getCodigoDepartamento());
        }
        if (strValid(filter.getCodigoProvincia())) {
            filters.add("equal:unidadCatastral.codigo_provincia:" + filter.getCodigoProvincia());
        }
        if (strValid(filter.getCodigoDistrito())) {
            filters.add("equal:unidadCatastral.codigo_distrito:" + filter.getCodigoDistrito());
        }
        if (strValid(filter.getCodigoSector())) {
            filters.add("equal:unidadCatastral.codigo_sector:" + filter.getCodigoSector());
        }
        if (strValid(filter.getCodigoManzana())) {
            filters.add("equal:unidadCatastral.codigo_manzana:" + filter.getCodigoManzana());
        }
        if (strValid(filter.getCodigoLote())) {
            filters.add("equal:unidadCatastral.codigo_lote:" + filter.getCodigoLote());
        }
        if (strValid(filter.getCodigoEdificacion())) {
            filters.add("equal:unidadCatastral.codigo_edificacion:" + filter.getCodigoEdificacion());
        }
        if (strValid(filter.getCodigoEntrada())) {
            filters.add("equal:unidadCatastral.codigo_entrada:" + filter.getCodigoEntrada());
        }
        if (strValid(filter.getCodigoPiso())) {
            filters.add("equal:unidadCatastral.codigo_piso:" + filter.getCodigoPiso());
        }
        if (strValid(filter.getCodigoUnidad())) {
            filters.add("equal:unidadCatastral.codigo_unidad:" + filter.getCodigoUnidad());
        }
        if (filter.getIdLoteCatastral() != null) {
            filters.add("=:unidadCatastral.id_lote_catastral:" + filter.getIdLoteCatastral());
        }
        if (filter.getIdUsoEspecifico() != null) {
            filters.add("=:unidadCatastral.id_uso_especifico:" + filter.getIdUsoEspecifico());
        }
        if (filter.getIdClasificacionPredio() != null) {
            filters.add("=:unidadCatastral.id_clasificacion_predio:" + filter.getIdClasificacionPredio());
        }
        if (filter.getIdPredioEn() != null) {
            filters.add("=:unidadCatastral.id_predio_en:" + filter.getIdPredioEn());
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
        String[] mapOrder = {"id:asc"};
        map.put("fields", fields);
        map.put("mapOrder", mapOrder);
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("mapFilterField", mapFilterField);
        return map;
    }

    private boolean strValid(String value) {
        return (value != null && !value.trim().isEmpty());
    }
}
