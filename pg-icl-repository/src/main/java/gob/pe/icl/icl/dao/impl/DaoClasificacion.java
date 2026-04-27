/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoClasificacion;
import gob.pe.icl.icl.dto.beans.FilterClasificacion;
import gob.pe.icl.icl.entity.Clasificacion;
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
public class DaoClasificacion extends AbstractJpaDao<Clasificacion> implements InterDaoClasificacion {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoClasificacion() {
        super();
        this.setClazz(Clasificacion.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listar(FilterClasificacion filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }
  
    @Override
    public ArrayNode listar(FilterClasificacion filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }
    
    private Map<String, Object> buildQueryList(FilterClasificacion filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tm_clasificacion as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("categoria.id as idCategoria,"));
        strFileds.append(share.append("categoria.descripcion as descripcionCategoria"));
        String fields = strFileds.toString();
        String[] joinTables = new String[1];
        joinTables[0] = "inner:icl.catastro.tm_categoria as categoria:on:base.id_categoria:categoria.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdClasificacion()!= null) {
            filters.add("=:base.id:" + filter.getIdClasificacion());
        }
        if (filter.getIdCategoria() != null) {
            filters.add("=:categoria.id:" + filter.getIdCategoria());
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
    
    
    @Override
    public Long count(Long idCategoria) throws Exception {
        String joinTable="inner:categoria";
        String[] mapFilterField={"=:categoria.id:"+idCategoria,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
   }


}
