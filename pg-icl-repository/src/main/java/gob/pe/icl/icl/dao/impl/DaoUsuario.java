/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoUsuario;
import gob.pe.icl.icl.dto.beans.FilterUsuario;
import gob.pe.icl.icl.entity.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Slf4j
@Repository
public class DaoUsuario extends AbstractJpaDao<Usuario> implements InterDaoUsuario {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoUsuario(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(Usuario.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listar(FilterUsuario filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBy = (String) map.get("groupBy");
        StringBuilder sql=this.strAllFieldsJoinPostgresGroupBy(joinTables, table, fields, mapFilterField, mapOrder,groupBy);
        Shared sharedUtil = new Shared();
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                customExecuteStatement(cnctn, sql.toString(), sharedUtil, array,null,null);
            }
        });
        return array;
    }

    @Override
    public ArrayNode listar(FilterUsuario filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBy = (String) map.get("groupBy");
        StringBuilder sql=this.strAllFieldsJoinGroupByLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder,groupBy,limit,offSet);
        Shared sharedUtil = new Shared();
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                customExecuteStatement(cnctn, sql.toString(), sharedUtil, array,limit,offSet);
            }
        });
        return array;
    }

    private Map<String, Object> buildQueryList(FilterUsuario filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.seguridad.tg_usuario as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("distinct"));
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("userEntity.first_name as firstname,"));
        strFileds.append(share.append("userEntity.last_name as lastname,"));
        strFileds.append(share.append("userEntity.username as username,"));
        strFileds.append(share.append("userEntity.email as email,"));
        strFileds.append(share.append("base.fecha_alta as fechaalta,"));
        strFileds.append(share.append("base.fecha_baja as fechabaja,"));
        strFileds.append(share.append("base.estado as estado,"));
        strFileds.append(share.append("base.is_usuario_icl as isusuarioicl,"));
        strFileds.append(share.append("sistema.id as idsistema,"));
        strFileds.append(share.append("sistema.realm_id as idrealm,"));
        strFileds.append(share.append("base.user_entity_id as iduserentity,"));
        strFileds.append(share.append("string_agg(distinct concat('id:',usuarioRol.id_rol,',descripcion:',rol.descripcion,',isrolicl:',rol.is_rol_icl::int),';') as arrayroles,"));        
        strFileds.append(share.append("string_agg(distinct concat('id:',usuarioperfil.id_perfil,',descripcion:',perfil.descripcion,',idperfilicl:',perfil.is_perfil_icl::int),';') as arrayperfiles"));        
        String fields = strFileds.toString();
        String[] joinTables = new String[6];
        joinTables[0] = "inner:icl.seguridad.tg_sistema as sistema:on:base.id_sistema:sistema.id";
        joinTables[1] = "inner:icl.auth.user_entity as userEntity:on:userEntity.realm_id:sistema.realm_id:and:userEntity.realm_id:base.realm_id:and:userEntity.id:base.user_entity_id";
        joinTables[2] = "left:icl.seguridad.tg_usuario_rol as usuarioRol:on:base.id:usuarioRol.id_usuario:and:usuarioRol.is_persistente:true";
        joinTables[3] = "left:(select * from icl.seguridad.tg_rol where is_rol_icl=true and is_persistente=true) as rol:on:usuarioRol.id_rol:rol.id";
        joinTables[4] = "left:icl.seguridad.tg_usuario_perfil usuarioPerfil:on:base.id:usuarioPerfil.id_usuario:and:usuarioPerfil.is_persistente:true";
        joinTables[5] = "left:(select * from icl.seguridad.tg_perfil where is_perfil_icl=true and is_persistente=true) as perfil:on:usuarioPerfil.id_perfil:perfil.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        filters.add("=:base.is_usuario_icl:true");
        filters.add("=:base.estado:true");
        if(filter.getIdUserEntity()!=null){
            filters.add("equal:base.user_entity_id:" + filter.getIdUserEntity());
        }
        if (filter.getIdUsuario()!= null) {
            filters.add("=:base.id:" + filter.getIdUsuario());
        }
        if (filter.getIdSistema() != null) {
            filters.add("=:sistema.id:" + filter.getIdSistema());
        }
        if (filter.getEmail() != null) {
            filters.add("equal:base.email:" + filter.getEmail());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        StringBuilder groupBy = new StringBuilder();
        groupBy.append(share.append("base.id,"));
        groupBy.append(share.append("userEntity.first_name,"));
        groupBy.append(share.append("userEntity.last_name,"));
        groupBy.append(share.append("userEntity.username,"));
        groupBy.append(share.append("userEntity.email,"));
        groupBy.append(share.append("base.fecha_alta,"));
        groupBy.append(share.append("base.fecha_baja,"));
        groupBy.append(share.append("base.estado,"));
        groupBy.append(share.append("base.is_usuario_icl,"));
        groupBy.append(share.append("sistema.id,"));
        groupBy.append(share.append("sistema.realm_id,"));
        groupBy.append(share.append("base.user_entity_id"));
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("groupBy", groupBy.toString());
        map.put("mapOrder", mapOrder);
        return map;
    }
    
    private void customExecuteStatement(Connection cnctn, String sql, Shared sharedUtil, ArrayNode array, Long limit, Long offset) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = cnctn.prepareStatement(sql);
            if (limit != null && offset != null) {
                ps.setLong(1, limit);
                ps.setLong(2, offset);
            }
            rs = ps.executeQuery();
            ResultSetMetaData metadata = rs.getMetaData();
            int size = metadata.getColumnCount();
            while (rs.next()) {
                ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
                for (int i = 1; i <= size; i++) {
                    typesSet(node, rs, metadata, i);                    
                }
                array.add(node);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            sharedUtil.closePreparedStatement(ps);
            sharedUtil.closeResultSet(rs);
        }
    }
    
    private void typesSet(ObjectNode node, ResultSet rs, ResultSetMetaData metadata, int i) throws SQLException {
        log.error(metadata.getColumnLabel(i));
        log.error(metadata.getColumnClassName(i));
        log.error(metadata.getColumnName(i));
        log.error(metadata.getColumnTypeName(i));        
        if (metadata.getColumnTypeName(i).equals("numeric")) {
            node.put(metadata.getColumnName(i), rs.getBigDecimal(metadata.getColumnName(i)));
        }
        if (metadata.getColumnTypeName(i).equals("text")) {
            String value=rs.getString(metadata.getColumnName(i));
            String columName=metadata.getColumnName(i);
            if(columName.equals("arrayroles")){
                arrayField(value,node,"roles");
            }else if(columName.equals("arrayperfiles")){
                arrayField(value,node,"perfiles");
            }else{                
             node.put(columName, value);               
            }            
        }
        if (metadata.getColumnTypeName(i).equals("varchar")) {
            node.put(metadata.getColumnName(i), rs.getString(metadata.getColumnName(i)));
        }
        if (metadata.getColumnTypeName(i).equals("serial")) {
            node.put(metadata.getColumnName(i), rs.getLong(metadata.getColumnName(i)));
        }
        if (metadata.getColumnTypeName(i).equals("int8") || metadata.getColumnTypeName(i).equals("int4")) {
            node.put(metadata.getColumnName(i), rs.getLong(metadata.getColumnName(i)));
        }
        if (metadata.getColumnTypeName(i).equals("bool")) {
            node.put(metadata.getColumnName(i), rs.getBoolean(metadata.getColumnName(i)));
        }
        if (metadata.getColumnTypeName(i).equals("date")) {
            java.sql.Date fecha = rs.getDate(metadata.getColumnName(i));
            if (fecha != null) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String fechaStr = df.format(new java.util.Date(fecha.getTime()));
                node.put(metadata.getColumnName(i) + "longtime", fecha.getTime());
                node.put(metadata.getColumnName(i), fechaStr);
            } else {
                node.put(metadata.getColumnName(i) + "longtime", 0);
                node.put(metadata.getColumnName(i), "");
            }
        }
    }
    
    private void arrayField(String value,ObjectNode node,String nameField){
                String[] listado=value.split(";");
                ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
                for(int j=0;j<listado.length;j++){
                    String[] campos=listado[j].split(",");
                    if(campos.length==3 && campos[2].split(":").length==2 && campos[2].split(":")[1].equals("1")){
                        ObjectNode nodeArray = new ObjectNode(JsonNodeFactory.instance);                                        
                        String[] id=campos[0].split(":");
                        String[] descripcion=campos[1].split(":");
                        nodeArray.put(id[0],id[1]);
                        nodeArray.put(descripcion[0],descripcion[1]);
                        array.add(nodeArray);
                    }                    
                }
                node.putArray(nameField).addAll(array);
    }

    @Override
    public ArrayNode idPerfiles(FilterUsuario filter) throws Exception {
        String table = "icl.seguridad.tg_usuario as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("string_agg(distinct cast(perfil.id as varchar),':') as idperfiles"));
        String fields = strFileds.toString();
        String[] joinTables = new String[3];
        joinTables[0] = "inner:icl.seguridad.tg_sistema as sistema:on:base.id_sistema:sistema.id";
        joinTables[1] = "inner:icl.seguridad.tg_usuario_perfil as userPerfil:on:base.id:userPerfil.id_usuario";
        joinTables[2] = "inner:icl.seguridad.tg_perfil as perfil:on:userPerfil.id_perfil:perfil.id";        
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        filters.add("=:base.is_usuario_icl:true");
        filters.add("=:base.estado:true");
        if(filter.getIdUserEntity()!=null){
            filters.add("equal:base.user_entity_id:" + filter.getIdUserEntity());
        }
        if (filter.getIdUsuario()!= null) {
            filters.add("=:base.id:" + filter.getIdUsuario());
        }
        if (filter.getIdSistema() != null) {
            filters.add("=:sistema.id:" + filter.getIdSistema());
        }
        if (filter.getEmail() != null) {
            filters.add("equal:base.email:" + filter.getEmail());
        }
        String[] mapFilterField = filters.toArray(new String[0]);        
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, null);
    }
        
}
