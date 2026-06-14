/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento;

import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jona
 */
public class DaoDepartamento extends AbstractJpaDao<Departamento>
        implements InterDaoDepartamento {

    public DaoDepartamento() {
        super();
        setClazz(Departamento.class);
    }

    // --- select ---

    @Override
    public Collection<Departamento> listar() throws Exception {
        String mapFilterField = "istrue:isPersistente";
        String[] mapOrder = {"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder);
    }

    @Override
    public Collection<Departamento> listar(int pageNumber, int pageSize) throws Exception {
        String mapFilterField = "istrue:isPersistente";
        String[] mapOrder = {"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder, pageNumber, pageSize);
    }

    @Override
    public Collection<Departamento> findByDescripcion(String descripcion) throws Exception {
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
    public void saveAll(List<Departamento> lista) throws UnknownException {
        for (Departamento d : lista) {
            save(d);
        }
    }

    // --- update ---

    @Override
    public void updateDescripcion(Long id, String descripcion) throws Exception {
        Departamento d = findById(id);
        if (d != null) {
            d.setDescripcion(descripcion);
            d.setVersion(System.currentTimeMillis());
            update(d);
        }
    }

    @Override
    public void updateOrden(Long id, Long orden) throws Exception {
        Departamento d = findById(id);
        if (d != null) {
            d.setOrden(orden);
            d.setVersion(System.currentTimeMillis());
            update(d);
        }
    }

    @Override
    public void marcarInactivo(Long id) throws Exception {
        Departamento d = findById(id);
        if (d != null) {
            d.setIsPersistente(Boolean.FALSE);
            d.setVersion(System.currentTimeMillis());
            update(d);
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
