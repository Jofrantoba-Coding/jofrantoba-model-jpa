/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente;

import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jona
 */
public class DaoCliente extends AbstractJpaDao<Cliente>
        implements InterDaoCliente {

    public DaoCliente() {
        super();
        setClazz(Cliente.class);
    }

    // --- select ---

    @Override
    public Collection<Cliente> findByNombre(String nombre) throws Exception {
        String[] filters = {"like:base.nombres:%" + nombre + "%"};
        String[] order   = {"base.apellidos:asc"};
        return customFieldsFilterAnd("base", filters, order);
    }

    @Override
    public Collection<Cliente> findByTipoDocumento(String tipoDocumento) throws Exception {
        String[] filters = {"equal:base.tipoDocumento:" + tipoDocumento};
        String[] order   = {"base.nombres:asc"};
        return customFieldsFilterAnd("base", filters, order);
    }

    @Override
    public List<Cliente> findByCargo(String cargo) throws UnknownException {
        String[] filters = {"like:base.cargo:%" + cargo + "%"};
        String[] order   = {"base.apellidos:asc"};
        return new ArrayList<>(customFieldsFilterAnd("base", filters, order));
    }

    @Override
    public Long countAll() throws Exception {
        return rowCountJoinFilterAnd(null, new String[]{"isnotnull:base.id"});
    }

    // --- insert ---

    @Override
    public void saveAll(List<Cliente> clientes) throws UnknownException {
        for (Cliente cliente : clientes) {
            save(cliente);
        }
    }

    // --- update ---

    @Override
    public void updateCargo(Long id, String cargo) throws Exception {
        Cliente cliente = findById(id);
        if (cliente != null) {
            cliente.setCargo(cargo);
            cliente.setVersion(System.currentTimeMillis());
            update(cliente);
        }
    }

    @Override
    public void incrementarSalario(Long id, BigDecimal incremento) throws Exception {
        Cliente cliente = findById(id);
        if (cliente != null) {
            cliente.setSalario(cliente.getSalario().add(incremento));
            cliente.setVersion(System.currentTimeMillis());
            update(cliente);
        }
    }

    @Override
    public void updateDatos(Long id, String cargo, BigDecimal salario) throws Exception {
        Cliente cliente = findById(id);
        if (cliente != null) {
            cliente.setCargo(cargo);
            cliente.setSalario(salario);
            cliente.setVersion(System.currentTimeMillis());
            update(cliente);
        }
    }

    @Override
    public void marcarInactivo(Long id) throws Exception {
        Cliente cliente = findById(id);
        if (cliente != null) {
            cliente.setIsPersistente(Boolean.FALSE);
            cliente.setVersion(System.currentTimeMillis());
            update(cliente);
        }
    }

    @Override
    public int incrementarSalarioByCargo(String cargo, BigDecimal factor) throws Exception {
        List<Cliente> clientes = findByCargo(cargo);
        int actualizado = 0;
        for (Cliente cliente : clientes) {
            cliente.setSalario(cliente.getSalario().multiply(factor));
            cliente.setVersion(System.currentTimeMillis());
            update(cliente);
            actualizado++;
        }
        return actualizado;
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
    public int deleteBySalarioMenorA(double limite) throws Exception {
        return deleteFilterAnd(new String[]{"<:base.salario:" + limite});
    }

    @Override
    public int deleteAll() throws Exception {
        return deleteFilterAnd(new String[]{"isnotnull:base.id"});
    }
}
