/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dto.beans.FilterFile;
import gob.pe.icl.icl.entity.FileUnidadCatastral;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import gob.pe.icl.icl.dao.inter.InterDaoFileUnidadCatastral;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoFileUnidadCatastral extends AbstractJpaDao<FileUnidadCatastral> implements InterDaoFileUnidadCatastral {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoFileUnidadCatastral() {
        super();
        this.setClazz(FileUnidadCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listar(FilterFile filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }
  
    @Override
    public ArrayNode listar(FilterFile filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }
    
    private Map<String, Object> buildQueryList(FilterFile filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tgv_file_unidad_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.tipo_file as tipoFile,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("base.principal as principal,"));        
        strFileds.append(share.append("base.anio as anio,"));
        strFileds.append(share.append("base.url_file as urlFile,"));
        strFileds.append(share.append("clasificacion.id as idClasificacion,"));
        strFileds.append(share.append("clasificacion.descripcion as descripcionClasificacion,"));
        strFileds.append(share.append("categoria.id as idCategoria,"));
        strFileds.append(share.append("categoria.descripcion as descripcionCategoria,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito"));
        String fields = strFileds.toString();
        String[] joinTables = new String[4];
        joinTables[0] = "inner:icl.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:icl.catastro.tm_clasificacion as clasificacion:on:base.id_clasificacion:clasificacion.id";
        joinTables[2] = "inner:icl.catastro.tm_categoria as categoria:on:clasificacion.id_categoria:categoria.id";
        joinTables[3] = "inner:icl.catastro.tm_distrito as distrito:on:categoria.id_distrito:distrito.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdFile()!= null) {
            filters.add("=:base.id:" + filter.getIdFile());
        }
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdClasificacion() != null) {
            filters.add("=:clasificacion.id:" + filter.getIdClasificacion());
        }
        if (filter.getIdCategoria() != null) {
            filters.add("=:categoria.id:" + filter.getIdCategoria());
        }
        if (filter.getTipoFile() != null) {
            filters.add("equal:base.tipo_file:" + filter.getTipoFile());
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
    public void asignarPrincipal(Long id) throws Exception {
        Shared sharedUtil = new Shared(); 
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("update icl.catastro.tgv_file_unidad_catastral set"));
        sql.append(sharedUtil.append("principal=:principal"));
        sql.append(sharedUtil.append("where id=:id"));
        String[] paramValues={"principal:String:S","id:Long:"+id};
        this.iudNativeQuery(sql.toString(),paramValues);
    }

    @Override
    public void limpiarPrincipal(Long idUnidadCatastral) throws Exception {
        Shared sharedUtil = new Shared();  
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("update icl.catastro.tgv_file_unidad_catastral set"));
        sql.append(sharedUtil.append("principal=:principal"));
        sql.append(sharedUtil.append("where id_unidad_catastral=:idUnidadCatastral"));
        String[] paramValues={"principal:String:N","idUnidadCatastral:Long:"+idUnidadCatastral};
        this.iudNativeQuery(sql.toString(),paramValues);
    }
    

}
