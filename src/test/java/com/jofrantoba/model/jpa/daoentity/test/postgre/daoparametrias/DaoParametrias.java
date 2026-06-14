/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoparametrias;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.Collection;
import java.util.List;
import org.hibernate.transform.ResultTransformer;

/**
 *
 * @author jona
 */
public class DaoParametrias extends AbstractJpaDao<Parametrias>
        implements InterDaoParametrias {

    public DaoParametrias() {
        super();
        setClazz(Parametrias.class);
    }

    // --- select ---

    @Override
    public Collection<Parametrias> parents() throws Exception {
        String joinTable = "left:parent";
        String[] filters = {"isnull:parent.id", "=:base.isPersistente:true"};
        String[] order   = {"base.descripcion:asc"};
        String fields    = "base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo,base.orden as orden";
        return customFieldsJoinFilterAnd(fields, joinTable, filters, order);
    }

    @Override
    public Collection<Parametrias> parents(int pageNumber, int pageSize) throws Exception {
        String joinTable = "left:parent";
        String[] filters = {"isnull:parent.id", "=:base.isPersistente:true"};
        String[] order   = {"base.descripcion:asc"};
        String fields    = "base.id as id,base.descripcion as descripcion";
        return customFieldsJoinFilterAnd(fields, joinTable, filters, order, pageNumber, pageSize);
    }

    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    @Override
    public Collection<Parametrias> childrens() throws Exception {
        String joinTable = "left:parent";
        String[] filters = {"isnotnull:parent.id", "=:base.isPersistente:true"};
        String[] order   = {"base.descripcion:asc"};
        String fields    = "base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo,base.orden as orden,parent.id as idParent,parent.descripcion as descripcionParent";
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Parametrias bean = new Parametrias();
                bean.setTransformer(os, strings);
                return bean;
            }
            @Override
            public List transformList(List list) {
                return list;
            }
        };
        return (Collection<Parametrias>) customFieldsJoinFilterAnd(rt, fields, joinTable, filters, order);
    }

    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    @Override
    public Collection<Parametrias> childrensByParents(Long idParent) throws Exception {
        String joinTable = "left:parent";
        String[] filters = {"=:parent.id:" + idParent, "=:base.isPersistente:true"};
        String[] order   = {"base.descripcion:asc"};
        String fields    = "base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo,base.orden as orden,parent.id as idParent,parent.descripcion as descripcionParent";
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Parametrias bean = new Parametrias();
                bean.setTransformer(os, strings);
                return bean;
            }
            @Override
            public List transformList(List list) {
                return list;
            }
        };
        return (Collection<Parametrias>) customFieldsJoinFilterAnd(rt, fields, joinTable, filters, order);
    }

    @Override
    public Long countChildrens(Long idParent) throws Exception {
        String joinTable = "left:parent";
        String[] filters = {"=:parent.id:" + idParent, "=:base.isPersistente:true"};
        return rowCountJoinFilterAnd(joinTable, filters);
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table     = "catastro.tm_parametrias as base";
        String fields    = "base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo";
        String[] filters = {"=:base.is_persistente:true"};
        String[] order   = {"id:asc"};
        return allFieldsLimitOffsetPostgres(table, fields, filters, order, limit, offSet);
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table     = "catastro.tm_parametrias as base";
        String fields    = "base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo";
        String[] filters = {"=:base.is_persistente:true"};
        String[] order   = {"id:asc"};
        return allFieldsPostgres(table, fields, filters, order);
    }

    // --- insert ---

    @Override
    public void saveAll(List<Parametrias> lista) throws UnknownException {
        for (Parametrias p : lista) {
            save(p);
        }
    }

    // --- update ---

    @Override
    public void updateDescripcion(Long id, String descripcion) throws Exception {
        Parametrias p = findById(id);
        if (p != null) {
            p.setDescripcion(descripcion);
            p.setVersion(System.currentTimeMillis());
            update(p);
        }
    }

    @Override
    public void updateOrden(Long id, Long orden) throws Exception {
        Parametrias p = findById(id);
        if (p != null) {
            p.setOrden(orden);
            p.setVersion(System.currentTimeMillis());
            update(p);
        }
    }

    @Override
    public void marcarInactivo(Long id) throws Exception {
        Parametrias p = findById(id);
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
