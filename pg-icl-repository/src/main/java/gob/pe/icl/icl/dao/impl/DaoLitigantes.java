/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoLitigantes;
import gob.pe.icl.icl.dto.beans.FilterLitigantes;
import gob.pe.icl.icl.entity.Litigantes;
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
public class DaoLitigantes extends AbstractJpaDao<Litigantes> implements InterDaoLitigantes {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoLitigantes() {
        super();
        this.setClazz(Litigantes.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterLitigantes filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterLitigantes filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }

    @Override
    public ArrayNode listar(FilterLitigantes filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    private Map<String, Object> buildQueryList(FilterLitigantes filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tgv_litigantes as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.numero_documento as numeroDocumento,"));
        strFileds.append(share.append("base.nombres as nombres,"));
        strFileds.append(share.append("base.codigo_contribuyente as codigoContribuyente,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("tipoDocumento.id as idTipoDocumento,"));
        strFileds.append(share.append("tipoDocumento.descripcion as descripcionTipoDocumento"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:icl.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:icl.catastro.tm_parametrias as tipoDocumento:on:base.id_tipo_documento:tipoDocumento.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdTipoDocumento() != null) {
            filters.add("=:tipoDocumento.id:" + filter.getIdTipoDocumento());
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
    public Long countTipoDocumento(Long idTipoDocumento) throws Exception {
        String joinTable = "inner:tipoDocumento";
        String[] mapFilterField = {"=:tipoDocumento.id:" + idTipoDocumento, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
