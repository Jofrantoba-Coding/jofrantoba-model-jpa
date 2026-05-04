/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jona
 */
public interface InterDaoCliente extends InterCrud<Cliente> {

    // --- select ---
    Collection<Cliente> findByNombre(String nombre) throws Exception;

    Collection<Cliente> findByTipoDocumento(String tipoDocumento) throws Exception;

    List<Cliente> findByCargo(String cargo) throws UnknownException;

    Long countAll() throws Exception;

    // --- insert ---
    void saveAll(List<Cliente> clientes) throws UnknownException;

    // --- update ---
    void updateCargo(Long id, String cargo) throws Exception;
    void incrementarSalario(Long id, BigDecimal incremento) throws Exception;
    void updateDatos(Long id, String cargo, BigDecimal salario) throws Exception;
    void marcarInactivo(Long id) throws Exception;
    int incrementarSalarioByCargo(String cargo, BigDecimal factor) throws Exception;

    // --- delete ---
    void deleteByIds(List<Long> ids) throws Exception;

    int deleteInactivos() throws Exception;

    int deleteBySalarioMenorA(double limite) throws Exception;

    int deleteAll() throws Exception;
}
