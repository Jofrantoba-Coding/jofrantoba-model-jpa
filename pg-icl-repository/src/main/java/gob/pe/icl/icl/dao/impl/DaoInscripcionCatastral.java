/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoInscripcionCatastral;
import gob.pe.icl.icl.dto.beans.FilterInscripcionCatastral;
import gob.pe.icl.icl.entity.InscripcionCatastral;
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
public class DaoInscripcionCatastral extends AbstractJpaDao<InscripcionCatastral> implements InterDaoInscripcionCatastral {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoInscripcionCatastral() {
        super();
        this.setClazz(InscripcionCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterInscripcionCatastral filter) throws Exception {
         Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterInscripcionCatastral filter,Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }

    @Override
    public ArrayNode listar(FilterInscripcionCatastral filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    private Map<String, Object> buildQueryList(FilterInscripcionCatastral filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tgv_inscripcion_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.numero as numero,"));
        strFileds.append(share.append("base.fojas as fojas,"));
        strFileds.append(share.append("base.asiento as asiento,"));
        strFileds.append(share.append("base.fecha_inscripcion as fechaInscripcion,"));
        strFileds.append(share.append("base.inscripcion_fabrica as inscripcionFabrica,"));
        strFileds.append(share.append("base.fecha_inscripcion_fabrica as fechaInscripcionFabrica,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("tipoPartidaCatastral.id as idTipoPartidaCatastral,"));
        strFileds.append(share.append("tipoPartidaCatastral.descripcion as descripcionTipoPartidaCatastral,"));
        strFileds.append(share.append("declaratoriaFabrica.id as idDeclaratoriaFabrica,"));
        strFileds.append(share.append("declaratoriaFabrica.descripcion as descripcionDeclaratoriaFabrica"));
        String fields = strFileds.toString();
        String[] joinTables = new String[3];
        joinTables[0] = "inner:icl.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:icl.catastro.tm_parametrias as tipoPartidaCatastral:on:base.id_tipo_partida_registral:tipoPartidaCatastral.id";
        joinTables[2] = "left:icl.catastro.tm_parametrias as declaratoriaFabrica:on:base.id_declaratoria_fabrica:declaratoriaFabrica.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");

        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdTipoPartidaCatastral() != null) {
            filters.add("=:tipoPartidaCatastral.id:" + filter.getIdTipoPartidaCatastral());
        }
        if (filter.getIdDeclaratoriaFabrica() != null) {
            filters.add("=:declaratoriaFabrica.id:" + filter.getIdDeclaratoriaFabrica());
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
    public Long countTipoPartidaRegistral(Long idTipoPartidaCatastral) throws Exception {
        String joinTable = "inner:tipoPartidaCatastral";
        String[] mapFilterField = {"=:tipoPartidaCatastral.id:" + idTipoPartidaCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countDeclaratoriaFabrica(Long idDeclaratoriaFabrica) throws Exception {
        String joinTable = "inner:declaratoriaFabrica";
        String[] mapFilterField = {"=:declaratoriaFabrica.id:" + idDeclaratoriaFabrica, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
