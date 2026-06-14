/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daovia;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.Collection;
import java.util.List;
import org.hibernate.transform.ResultTransformer;

/**
 *
 * @author jona
 */
public class DaoVia extends AbstractJpaDao<Via>
        implements InterDaoVia {

    public DaoVia() {
        super();
        setClazz(Via.class);
    }

    // --- select ---

    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    @Override
    public Collection<Via> listar(Long idDistrito) throws Exception {
        String[] joinTables = {"inner:distrito", "inner:tipoVia"};
        String[] mapFilterField = {"=:distrito.id:" + idDistrito, "=:base.isPersistente:true"};
        String[] mapOrder = {"base.descripcion:asc"};
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigoVia as codigoVia,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("distrito.id as idDistrito,"));
        strFields.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFields.append(share.append("tipoVia.id as idTipoVia,"));
        strFields.append(share.append("tipoVia.descripcion as descripcionTipoVia"));
        String fields = strFields.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Via bean = new Via();
                bean.setTransformer(os, strings);
                return bean;
            }
            @Override
            public List transformList(List list) {
                return list;
            }
        };
        return (Collection<Via>) customFieldsJoinFilterAnd(rt, fields, joinTables, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table = "catastro.tm_via as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("base.codigo_via as codigoVia,"));
        strFields.append(share.append("distrito.id as idDistrito,"));
        strFields.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFields.append(share.append("provincia.id as idProvincia,"));
        strFields.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields = strFields.toString();
        String[] joinTables = {
            "inner:catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id",
            "inner:catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id",
            "inner:catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id"
        };
        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "catastro.tm_via as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("base.codigo_via as codigoVia,"));
        strFields.append(share.append("distrito.id as idDistrito,"));
        strFields.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFields.append(share.append("provincia.id as idProvincia,"));
        strFields.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields = strFields.toString();
        String[] joinTables = {
            "inner:catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id",
            "inner:catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id",
            "inner:catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id"
        };
        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public Long count(Long idDistrito) throws Exception {
        String joinTable = "inner:distrito";
        String[] mapFilterField = {"=:distrito.id:" + idDistrito, "=:base.isPersistente:true"};
        return rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    // --- insert ---

    @Override
    public void saveAll(List<Via> lista) throws UnknownException {
        for (Via v : lista) {
            save(v);
        }
    }

    // --- update ---

    @Override
    public void updateDescripcion(Long id, String descripcion) throws Exception {
        Via v = findById(id);
        if (v != null) {
            v.setDescripcion(descripcion);
            v.setVersion(System.currentTimeMillis());
            update(v);
        }
    }

    @Override
    public void marcarInactivo(Long id) throws Exception {
        Via v = findById(id);
        if (v != null) {
            v.setIsPersistente(Boolean.FALSE);
            v.setVersion(System.currentTimeMillis());
            update(v);
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
