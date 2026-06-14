/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.Collection;
import java.util.List;
import org.hibernate.transform.ResultTransformer;

/**
 *
 * @author jofrantoba
 */
public class DaoDistrito extends AbstractJpaDao<Distrito>
        implements InterDaoDistrito {

    public DaoDistrito() {
        super();
        setClazz(Distrito.class);
    }

    // --- select ---

    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    @Override
    public Collection<Distrito> listar(Long idProvincia) throws Exception {
        String joinTable = "inner:provincia";
        String[] mapFilterField = {"=:provincia.id:" + idProvincia, "=:base.isPersistente:true"};
        String[] mapOrder = {"base.descripcion:asc"};
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigoDistrito as codigoDistrito,"));
        strFields.append(share.append("base.codigoProvincia as codigoProvincia,"));
        strFields.append(share.append("base.codigoDepartamento as codigoDepartamento,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("provincia.id as idProvincia,"));
        strFields.append(share.append("provincia.descripcion as descripcionProvincia"));
        String fields = strFields.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Distrito bean = new Distrito();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        return (Collection<Distrito>) customFieldsJoinFilterAnd(rt, fields, joinTable, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table = "catastro.tm_distrito as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigo_distrito as codigoDistrito,"));
        strFields.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFields.append(share.append("base.codigo_departamento as codigoDepartamento,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("provincia.id as idProvincia,"));
        strFields.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields = strFields.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";
        joinTables[1] = "inner:catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "catastro.tm_distrito as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigo_distrito as codigoDistrito,"));
        strFields.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFields.append(share.append("base.codigo_departamento as codigoDepartamento,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("provincia.id as idProvincia,"));
        strFields.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields = strFields.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";
        joinTables[1] = "inner:catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public Long count(Long idProvincia) throws Exception {
        String joinTable = "inner:provincia";
        String[] mapFilterField = {"=:provincia.id:" + idProvincia, "=:base.isPersistente:true"};
        return rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    // --- insert ---

    @Override
    public void saveAll(List<Distrito> lista) throws UnknownException {
        for (Distrito distrito : lista) {
            save(distrito);
        }
    }

    // --- update ---

    @Override
    public void updateDescripcion(Long id, String descripcion) throws Exception {
        Distrito distrito = findById(id);
        if (distrito != null) {
            distrito.setDescripcion(descripcion);
            distrito.setVersion(System.currentTimeMillis());
            update(distrito);
        }
    }

    @Override
    public void marcarInactivo(Long id) throws Exception {
        Distrito distrito = findById(id);
        if (distrito != null) {
            distrito.setIsPersistente(Boolean.FALSE);
            distrito.setVersion(System.currentTimeMillis());
            update(distrito);
        }
    }

    // --- delete ---

    @Override
    public void deleteByIds(List<Long> ids) throws Exception {
        for (Long id : ids) {
            delete(id);
        }
    }

    @Override
    public int deleteInactivos() throws Exception {
        return deleteFilterAnd(new String[]{"=:base.isPersistente:false"});
    }

    @Override
    public int deleteAll() throws Exception {
        return deleteFilterAnd(new String[]{"isnotnull:base.id"});
    }
}
