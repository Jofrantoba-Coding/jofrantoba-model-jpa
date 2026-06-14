/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daomanzana;

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
public class DaoManzana extends AbstractJpaDao<Manzana>
        implements InterDaoManzana {

    public DaoManzana() {
        super();
        setClazz(Manzana.class);
    }

    // --- select ---

    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    @Override
    public Collection<Manzana> listar(Long idSector) throws Exception {
        String joinTable = "inner:sector";
        String[] mapFilterField = {"=:sector.id:" + idSector, "=:base.isPersistente:true"};
        String[] mapOrder = {"base.codigoDepartamento:asc", "base.codigoProvincia:asc", "base.codigoDistrito:asc", "base.codigoSector:asc", "base.codigoManzana:asc"};
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigoManzana as codigoManzana,"));
        strFields.append(share.append("base.codigoSector as codigoSector,"));
        strFields.append(share.append("base.codigoDistrito as codigoDistrito,"));
        strFields.append(share.append("base.codigoProvincia as codigoProvincia,"));
        strFields.append(share.append("base.codigoDepartamento as codigoDepartamento,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("sector.id as idSector,"));
        strFields.append(share.append("sector.descripcion as descripcionSector"));
        String fields = strFields.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Manzana bean = new Manzana();
                bean.setTransformer(os, strings);
                return bean;
            }
            @Override
            public List transformList(List list) {
                return list;
            }
        };
        return (Collection<Manzana>) customFieldsJoinFilterAnd(rt, fields, joinTable, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table = "catastro.tg_manzana as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("base.codigo_manzana as codigoManzana,"));
        strFields.append(share.append("base.codigo_sector as codigoSector,"));
        strFields.append(share.append("base.codigo_distrito as codigoDistrito,"));
        strFields.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFields.append(share.append("base.codigo_departamento as codigoDepartamento,"));
        strFields.append(share.append("sector.id as idSector,"));
        strFields.append(share.append("sector.descripcion as descripcionSector,"));
        strFields.append(share.append("distrito.id as idDistrito,"));
        strFields.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFields.append(share.append("provincia.id as idProvincia,"));
        strFields.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields = strFields.toString();
        String[] joinTables = {
            "inner:catastro.tg_sector as sector:on:base.id_sector:sector.id",
            "inner:catastro.tm_distrito as distrito:on:sector.id_distrito:distrito.id",
            "inner:catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id",
            "inner:catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id"
        };
        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.codigo_departamento:asc", "base.codigo_provincia:asc", "base.codigo_distrito:asc", "base.codigo_sector:asc", "base.codigo_manzana:asc"};
        return allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "catastro.tg_manzana as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("base.codigo_manzana as codigoManzana,"));
        strFields.append(share.append("base.codigo_sector as codigoSector,"));
        strFields.append(share.append("base.codigo_distrito as codigoDistrito,"));
        strFields.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFields.append(share.append("base.codigo_departamento as codigoDepartamento,"));
        strFields.append(share.append("sector.id as idSector,"));
        strFields.append(share.append("sector.descripcion as descripcionSector,"));
        strFields.append(share.append("distrito.id as idDistrito,"));
        strFields.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFields.append(share.append("provincia.id as idProvincia,"));
        strFields.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields = strFields.toString();
        String[] joinTables = {
            "inner:catastro.tg_sector as sector:on:base.id_sector:sector.id",
            "inner:catastro.tm_distrito as distrito:on:sector.id_distrito:distrito.id",
            "inner:catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id",
            "inner:catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id"
        };
        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.codigo_departamento:asc", "base.codigo_provincia:asc", "base.codigo_distrito:asc", "base.codigo_sector:asc", "base.codigo_manzana:asc"};
        return allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public Long count(Long idSector) throws Exception {
        String joinTable = "inner:sector";
        String[] mapFilterField = {"=:sector.id:" + idSector, "=:base.isPersistente:true"};
        return rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    // --- insert ---

    @Override
    public void saveAll(List<Manzana> lista) throws UnknownException {
        for (Manzana m : lista) {
            save(m);
        }
    }

    // --- update ---

    @Override
    public void updateDescripcion(Long id, String descripcion) throws Exception {
        Manzana m = findById(id);
        if (m != null) {
            m.setDescripcion(descripcion);
            m.setVersion(System.currentTimeMillis());
            update(m);
        }
    }

    @Override
    public void marcarInactivo(Long id) throws Exception {
        Manzana m = findById(id);
        if (m != null) {
            m.setIsPersistente(Boolean.FALSE);
            m.setVersion(System.currentTimeMillis());
            update(m);
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
