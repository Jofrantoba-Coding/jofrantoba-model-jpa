/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.Collection;
import java.util.List;
import org.hibernate.transform.ResultTransformer;

/**
 *
 * @author jona
 */
public class DaoProvincia extends AbstractJpaDaoV2<Provincia>
        implements InterDaoProvincia {

    public DaoProvincia() {
        super();
        setClazz(Provincia.class);
    }

    // --- select ---

    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    @Override
    public Collection<Provincia> listar(Long idDepartamento) throws Exception {
        String joinTable = "inner:departamento";
        String[] mapFilterField = {"=:departamento.id:" + idDepartamento, "=:base.isPersistente:true"};
        String[] mapOrder = {"base.descripcion:asc"};
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigoProvincia as codigoProvincia,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento,"));
        strFields.append(share.append("departamento.codigoDepartamento as codigoDepartamento"));
        String fields = strFields.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Provincia bean = new Provincia();
                bean.setTransformer(os, strings);
                return bean;
            }
            @Override
            public List transformList(List list) {
                return list;
            }
        };
        return (Collection<Provincia>) customFieldsJoinFilterAnd(rt, fields, joinTable, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table = "catastro.tm_provincia as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFields.append(share.append("base.codigo_departamento as codigoDepartamento,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields = strFields.toString();
        String[] joinTables = {"inner:catastro.tm_departamento as departamento:on:base.id_departamento:departamento.id"};
        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "catastro.tm_provincia as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFields.append(share.append("base.codigo_departamento as codigoDepartamento,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields = strFields.toString();
        String[] joinTables = {"inner:catastro.tm_departamento as departamento:on:base.id_departamento:departamento.id"};
        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public Long count(Long idDepartamento) throws Exception {
        String joinTable = "inner:departamento";
        String[] mapFilterField = {"=:departamento.id:" + idDepartamento, "=:base.isPersistente:true"};
        return rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    // --- insert ---

    @Override
    public void saveAll(List<Provincia> lista) throws UnknownException {
        for (Provincia p : lista) {
            save(p);
        }
    }

    // --- update ---

    @Override
    public void updateDescripcion(Long id, String descripcion) throws Exception {
        Provincia p = findById(id);
        if (p != null) {
            p.setDescripcion(descripcion);
            p.setVersion(System.currentTimeMillis());
            update(p);
        }
    }

    @Override
    public void marcarInactivo(Long id) throws Exception {
        Provincia p = findById(id);
        if (p != null) {
            p.setIsPersistente(Boolean.FALSE);
            p.setVersion(System.currentTimeMillis());
            update(p);
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
