/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoActividadEconomica;
import gob.pe.icl.icl.dto.beans.FilterActividadEconomica;
import gob.pe.icl.icl.entity.ActividadEconomica;
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
public class DaoActividadEconomica extends AbstractJpaDao<ActividadEconomica> implements InterDaoActividadEconomica {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoActividadEconomica() {
        super();
        this.setClazz(ActividadEconomica.class);
        //this.setSessionFactory(sessionFactory);
    }

     @Override
    public ArrayNode listarFilter(FilterActividadEconomica filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterActividadEconomica filter,Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder,limit,offSet);
    }

    @Override
    public ArrayNode listar(FilterActividadEconomica filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    private Map<String, Object> buildQueryList(FilterActividadEconomica filter) {
        Map<String, Object> map = new HashMap();
          String table = "icl.catastro.tgv_actividad_economica as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("tipoDocIdent.id as idTipoDocIdent,"));
        strFileds.append(share.append("tipoDocIdent.descripcion as descripcionTipoDocIdent,"));
        strFileds.append(share.append("tipoConductor.id as idTipoConductor,"));
        strFileds.append(share.append("tipoConductor.descripcion as descripcionTipoConductor,"));
        strFileds.append(share.append("condicionConductor.id as idCondicionConductor,"));
        strFileds.append(share.append("condicionConductor.descripcion as descripcionCondicionConductor,"));
        strFileds.append(share.append("base.nombre_comercial as nombreComercial,"));
        strFileds.append(share.append("base.numero_ruc as numeroRuc,"));
        strFileds.append(share.append("base.numero_documento as numeroDocumento,"));
        strFileds.append(share.append("base.nombre as nombre,"));
        strFileds.append(share.append("base.numero_expediente as numero,"));
        strFileds.append(share.append("base.numero_licencia as numeroLicencia,"));
        strFileds.append(share.append("base.fecha_expedicion as fechaExpedicion,"));
        strFileds.append(share.append("base.fecha_vencimiento as fechaVencimiento,"));
        strFileds.append(share.append("base.inicio_actividad as inicioActividad,"));
        strFileds.append(share.append("base.predio_catastral_aa as predioCatastralAa,"));
        strFileds.append(share.append("base.predio_catastral_av as predioCatastralAv,"));
        strFileds.append(share.append("base.via_publica_aa as viaPublicaAa,"));
        strFileds.append(share.append("base.via_publica_av as viaPublicaAv,"));
        strFileds.append(share.append("base.bien_comun_aa as bienComunAa,"));
        strFileds.append(share.append("base.bien_comun_av as bienComunAv"));
        String fields = strFileds.toString();
        String[] joinTables = new String[4];
        joinTables[0] = "inner:icl.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "left:icl.catastro.tm_parametrias as tipoDocIdent:on:base.id_tipo_doc_ident:tipoDocIdent.id";
        joinTables[2] = "inner:icl.catastro.tm_parametrias as tipoConductor:on:base.id_tipo_conductor:tipoConductor.id";
        joinTables[3] = "left:icl.catastro.tm_parametrias as condicionConductor:on:base.id_condicion_conductor:condicionConductor.id";
        
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");

        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdTipoDocIdent() != null) {
            filters.add("=:tipoDocIdent.id:" + filter.getIdTipoDocIdent());
        }
        if (filter.getIdTipoConductor() != null) {
            filters.add("=:tipoConductor.id:" + filter.getIdTipoConductor());
        }   
        if (filter.getIdCondicionConductor() != null) {
            filters.add("=:condicionConductor.id:" + filter.getIdCondicionConductor());
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
    public Long couhtTipoDocIdent(Long idTipoDocIdent) throws Exception {
        String joinTable = "inner:tipoDocIdent";
        String[] mapFilterField = {"=:tipoDocIdent.id:" + idTipoDocIdent, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoConductor(Long idTipoConductor) throws Exception {
        String joinTable = "inner:tipoConductor";
        String[] mapFilterField = {"=:tipoConductor.id:" + idTipoConductor, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countCondicionConductor(Long idCondicionConductor) throws Exception {
        String joinTable = "inner:condicionConductor";
        String[] mapFilterField = {"=:condicionConductor.id:" + idCondicionConductor, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

}
