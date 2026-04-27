/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoUsuarioProyecto;
import gob.pe.icl.icl.dto.beans.FilterUsuarioProyecto;
import gob.pe.icl.icl.entity.UsuarioProyecto;
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
public class DaoUsuarioProyecto extends AbstractJpaDao<UsuarioProyecto> implements InterDaoUsuarioProyecto {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoUsuarioProyecto() {
        super();
        this.setClazz(UsuarioProyecto.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterUsuarioProyecto filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterUsuarioProyecto filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }

    @Override
    public ArrayNode listar(FilterUsuarioProyecto filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    private Map<String, Object> buildQueryList(FilterUsuarioProyecto filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.seguridad.tg_usuario_proyecto as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento,"));
        strFileds.append(share.append("departamento.codigo_departamento as codigoDepartamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFileds.append(share.append("provincia.codigo_provincia as codigoProvincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("distrito.codigo_distrito as codigoDistrito,"));
        strFileds.append(share.append("proyecto.id as idProyecto,"));
        strFileds.append(share.append("proyecto.descripcion as descripcionProyecto,"));
        strFileds.append(share.append("usuario.id as idUsuario,"));        
        strFileds.append(share.append("usuario.username as userName"));                
        String fields = strFileds.toString();
        String[] joinTables = new String[5];
        joinTables[0] = "inner:icl.catastro.tg_proyecto as proyecto:on:base.id_proyecto:proyecto.id";
        joinTables[1] = "inner:icl.seguridad.tg_usuario as usuario:on:base.id_usuario:usuario.id";
        joinTables[2] = "inner:icl.catastro.tm_distrito as distrito:on:proyecto.id_distrito:distrito.id";
        joinTables[3] = "inner:icl.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";
        joinTables[4] = "inner:icl.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdDistrito()!= null) {
            filters.add("=:distrito.id:" + filter.getIdDistrito());
        }
        if (filter.getIdProyecto()!= null) {
            filters.add("=:proyecto.id:" + filter.getIdProyecto());
        }
        if (filter.getIdUsuario()!= null) {
            filters.add("=:usuario.id:" + filter.getIdUsuario());
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
    public Long countProyecto(Long idProyecto) throws Exception {
        String joinTable = "inner:proyecto";
        String[] mapFilterField = {"=:proyecto.id:" + idProyecto, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countUsuario(Long idUsuario) throws Exception {
        String joinTable = "inner:user";
        String[] mapFilterField = {"=:user.id:" + idUsuario, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countUsuarioProyecto(Long idProyecto, Long idUsuario) throws Exception {
        String[] joinTables = new String[2];
        joinTables[0] = "inner:proyecto";
        joinTables[1] = "inner:user";
        String[] mapFilterField={"=:proyecto.id:"+idProyecto,"=:user.id:"+idUsuario,"=:base.isPersistente:true"};        
        return this.rowCountJoinsFilterAnd(joinTables,mapFilterField); 
    }
}
