/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jofrantoba
 */
public interface InterDaoDepartamento extends InterCrud<Departamento> {

    // --- select ---
    Collection<Departamento> listar() throws Exception;
    Collection<Departamento> listar(int pageNumber, int pageSize) throws Exception;
    Collection<Departamento> findByDescripcion(String descripcion) throws Exception;
    Long countAll() throws Exception;

    // --- insert ---
    void saveAll(List<Departamento> lista) throws UnknownException;

    // --- update ---
    void updateDescripcion(Long id, String descripcion) throws Exception;
    void updateOrden(Long id, Long orden) throws Exception;
    void marcarInactivo(Long id) throws Exception;

    // --- delete ---
    void deleteByIds(List<Long> ids) throws Exception;
    int deleteInactivos() throws Exception;
    int deleteAll() throws Exception;
}
