/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoclientesistema;

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
public class DaoClienteSistema extends AbstractJpaDao<ClienteSistema>
        implements InterDaoClienteSistema {

    public DaoClienteSistema() {
        super();
        setClazz(ClienteSistema.class);
    }

    // --- select ---

    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    @Override
    public Collection<ClienteSistema> listar(Long idSistema) throws Exception {
        String joinTable = "inner:sistema";
        String[] mapFilterField = {
            "=:sistema.id:" + idSistema,
            "=:base.isPersistente:true",
            "=:base.isAppIcl:true"
        };
        String[] mapOrder = {"base.descripcion:asc"};
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("sistema.id as idSistema,"));
        strFields.append(share.append("sistema.descripcion as descripcionSistema"));
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                ClienteSistema bean = new ClienteSistema();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        return (Collection<ClienteSistema>) customFieldsJoinFilterAnd(rt, strFields.toString(), joinTable, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table = "seguridad.tg_cliente_sistema as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("sistema.id as idSistema,"));
        strFields.append(share.append("sistema.descripcion as descripcionSistema"));
        String[] joinTables = new String[1];
        joinTables[0] = "inner:seguridad.tg_sistema as sistema:on:base.id_sistema:sistema.id";
        String[] mapFilterField = {"=:base.is_persistente:true", "=:base.is_app_icl:true"};
        String[] mapOrder = {"base.id:asc"};
        return allFieldsJoinLimitOffsetPostgres(joinTables, table, strFields.toString(), mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "seguridad.tg_cliente_sistema as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("sistema.id as idSistema,"));
        strFields.append(share.append("sistema.descripcion as descripcionSistema"));
        String[] joinTables = new String[1];
        joinTables[0] = "inner:seguridad.tg_sistema as sistema:on:base.id_sistema:sistema.id";
        String[] mapFilterField = {"=:base.is_persistente:true", "=:base.is_app_icl:true"};
        String[] mapOrder = {"base.id:asc"};
        return allFieldsJoinPostgres(joinTables, table, strFields.toString(), mapFilterField, mapOrder, "and");
    }

    @Override
    public Long count(Long idSistema) throws Exception {
        String joinTable = "inner:sistema";
        String[] mapFilterField = {"=:sistema.id:" + idSistema, "=:base.isPersistente:true"};
        return rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    // --- insert ---

    @Override
    public void saveAll(List<ClienteSistema> lista) throws UnknownException {
        for (ClienteSistema clienteSistema : lista) {
            save(clienteSistema);
        }
    }

    // --- update ---

    @Override
    public void updateDescripcion(Long id, String descripcion) throws Exception {
        ClienteSistema clienteSistema = findById(id);
        if (clienteSistema != null) {
            clienteSistema.setDescripcion(descripcion);
            clienteSistema.setVersion(System.currentTimeMillis());
            update(clienteSistema);
        }
    }

    @Override
    public void updateClientId(Long id, String clientId) throws Exception {
        ClienteSistema clienteSistema = findById(id);
        if (clienteSistema != null) {
            clienteSistema.setClientId(clientId);
            clienteSistema.setVersion(System.currentTimeMillis());
            update(clienteSistema);
        }
    }

    @Override
    public void marcarInactivo(Long id) throws Exception {
        ClienteSistema clienteSistema = findById(id);
        if (clienteSistema != null) {
            clienteSistema.setIsPersistente(Boolean.FALSE);
            clienteSistema.setVersion(System.currentTimeMillis());
            update(clienteSistema);
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
