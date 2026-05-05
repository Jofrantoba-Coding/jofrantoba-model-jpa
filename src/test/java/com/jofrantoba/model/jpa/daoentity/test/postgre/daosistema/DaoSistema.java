/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daosistema;

import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jofrantoba
 */
public class DaoSistema extends AbstractJpaDaoV2<Sistema>
        implements InterDaoSistema {

    public DaoSistema() {
        super();
        setClazz(Sistema.class);
    }

    // --- select ---

    @Override
    public Collection<Sistema> listar() throws Exception {
        String mapFilterField = "istrue:isPersistente";
        String[] mapOrder = {"descripcion:asc"};
        return allFields(mapFilterField, mapOrder);
    }

    @Override
    public Collection<Sistema> listar(int pageNumber, int pageSize) throws Exception {
        String mapFilterField = "istrue:isPersistente";
        String[] mapOrder = {"descripcion:asc"};
        return allFields(mapFilterField, mapOrder, pageNumber, pageSize);
    }

    @Override
    public Collection<Sistema> findByDescripcion(String descripcion) throws Exception {
        String[] filters = {"like:descripcion:" + descripcion, "istrue:isPersistente"};
        String[] order = {"descripcion:asc"};
        return allFieldsFilterAnd(filters, order);
    }

    @Override
    public Long countAll() throws Exception {
        return (long) listar().size();
    }

    // --- insert ---

    @Override
    public void saveAll(List<Sistema> lista) throws UnknownException {
        for (Sistema sistema : lista) {
            save(sistema);
        }
    }

    // --- update ---

    @Override
    public void updateDescripcion(Long id, String descripcion) throws Exception {
        Sistema sistema = findById(id);
        if (sistema != null) {
            sistema.setDescripcion(descripcion);
            sistema.setVersion(System.currentTimeMillis());
            update(sistema);
        }
    }

    @Override
    public void updateRealmId(Long id, String realmId) throws Exception {
        Sistema sistema = findById(id);
        if (sistema != null) {
            sistema.setRealmId(realmId);
            sistema.setVersion(System.currentTimeMillis());
            update(sistema);
        }
    }

    @Override
    public void marcarInactivo(Long id) throws Exception {
        Sistema sistema = findById(id);
        if (sistema != null) {
            sistema.setIsPersistente(Boolean.FALSE);
            sistema.setVersion(System.currentTimeMillis());
            update(sistema);
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
