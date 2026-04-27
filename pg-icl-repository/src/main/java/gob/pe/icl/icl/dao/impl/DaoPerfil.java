/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoPerfil;
import gob.pe.icl.icl.dto.beans.FilterPerfil;
import gob.pe.icl.icl.entity.Perfil;
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
public class DaoPerfil extends AbstractJpaDao<Perfil> implements InterDaoPerfil {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoPerfil(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(Perfil.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listar(FilterPerfil filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterPerfil filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }

    private Map<String, Object> buildQueryList(FilterPerfil filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.seguridad.tg_perfil as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("sistema.id as idSistema"));
        String fields = strFileds.toString();
        String[] joinTables = new String[1];
        joinTables[0] = "inner:icl.seguridad.tg_sistema as sistema:on:base.id_sistema:sistema.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdPerfil()!= null) {
            filters.add("=:base.id:" + filter.getIdPerfil());
        }
        if (filter.getIdSistema() != null) {
            filters.add("=:sistema.id:" + filter.getIdSistema());
        }
        if (filter.getDescripcion() != null) {
            filters.add("equal:base.descripcion:" + filter.getDescripcion());
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
}
