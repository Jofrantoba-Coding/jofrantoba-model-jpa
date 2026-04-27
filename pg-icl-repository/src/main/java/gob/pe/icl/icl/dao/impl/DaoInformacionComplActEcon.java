/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoInformacionComplActEcon;
import gob.pe.icl.icl.dto.beans.FilterInformacionComplActEcon;
import gob.pe.icl.icl.entity.InformacionComplActEcon;
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
public class DaoInformacionComplActEcon extends AbstractJpaDao<InformacionComplActEcon> implements InterDaoInformacionComplActEcon {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoInformacionComplActEcon() {
        super();
        this.setClazz(InformacionComplActEcon.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterInformacionComplActEcon filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterInformacionComplActEcon filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }

    @Override
    public ArrayNode listar(FilterInformacionComplActEcon filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    private Map<String, Object> buildQueryList(FilterInformacionComplActEcon filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tgv_informacion_compl_act_econ as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.condicion_declarante_otro as condicionDeclaranteOtro,"));
        strFileds.append(share.append("base.numero_documento_decla as numeroDocumentoDecla,"));
        strFileds.append(share.append("base.nombre_declarante as nombreDeclarante,"));
        strFileds.append(share.append("base.apellido_declarante as apellidoDeclarante,"));
        strFileds.append(share.append("base.fecha_declarante as fechaDeclarante,"));
        strFileds.append(share.append("base.documento_presentado_otro as documentoPresentadoOtro,"));
        strFileds.append(share.append("base.observacion as observacion,"));
        strFileds.append(share.append("actividadEconomica.id as idActividadEconomica,"));
        strFileds.append(share.append("condicionDeclarante.id as idCondicionDeclarante,"));
        strFileds.append(share.append("condicionDeclarante.descripcion as condicionDeclarante,"));
        strFileds.append(share.append("estadoLlenado.id as idEstadoLlenado,"));
        strFileds.append(share.append("estadoLlenado.descripcion as estadoLlenado,"));
        strFileds.append(share.append("tipoDocumentoDecla.id as idTipoDocumentoDecla,"));
        strFileds.append(share.append("tipoDocumentoDecla.descripcion as tipoDocumentoDecla,"));
        strFileds.append(share.append("mantenimiento.id as idMantenimiento,"));
        strFileds.append(share.append("mantenimiento.descripcion as mantenimiento,"));
        strFileds.append(share.append("documentoPresentado.id as idDocumentoPresentado,"));
        strFileds.append(share.append("documentoPresentado.descripcion as documentoPresentado"));
        String fields = strFileds.toString();
        String[] joinTables = new String[6];
        joinTables[0] = "inner:icl.catastro.tgv_actividad_economica as actividadEconomica:on:base.id_actividad_economica:actividadEconomica.id";
        joinTables[1] = "left:icl.catastro.tm_parametrias as condicionDeclarante:on:base.id_condicion_declarante:condicionDeclarante.id";
        joinTables[2] = "left:icl.catastro.tm_parametrias as estadoLlenado:on:base.id_estado_llenado:estadoLlenado.id";
        joinTables[3] = "left:icl.catastro.tm_parametrias as tipoDocumentoDecla:on:base.id_tipo_documento_decla:tipoDocumentoDecla.id";
        joinTables[4] = "left:icl.catastro.tm_parametrias as mantenimiento:on:base.id_mantenimiento:mantenimiento.id";
        joinTables[5] = "left:icl.catastro.tm_parametrias as documentoPresentado:on:base.id_documento_presentado:documentoPresentado.id";

        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");

        if (filter.getIdActividadEconomica() != null) {
            filters.add("=:actividadEconomica.id:" + filter.getIdActividadEconomica());
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
    public Long countestadoLlenado(Long idEstadoLlenado) throws Exception {
        String joinTable = "inner:estadoLlenado";
        String[] mapFilterField = {"=:estadoLlenado.id:" + idEstadoLlenado, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countCondicionDeclarante(Long idCondicionDeclarante) throws Exception {
        String joinTable = "inner:condicionDeclarante";
        String[] mapFilterField = {"=:condicionDeclarante.id:" + idCondicionDeclarante, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoDocumentoDecla(Long idDocumentoDecla) throws Exception {
        String joinTable = "inner:tipoDocumentoDecla";
        String[] mapFilterField = {"=:tipoDocumentoDecla.id:" + idDocumentoDecla, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countMantenimiento(Long idMantenimiento) throws Exception {
        String joinTable = "inner:mantenimiento";
        String[] mapFilterField = {"=:mantenimiento.id:" + idMantenimiento, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countDocumentoPresentado(Long idDocumentoPresentado) throws Exception {
        String joinTable = "inner:documentoPresentado";
        String[] mapFilterField = {"=:documentoPresentado.id:" + idDocumentoPresentado, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
