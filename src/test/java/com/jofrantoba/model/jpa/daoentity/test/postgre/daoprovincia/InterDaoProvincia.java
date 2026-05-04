/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jofrantoba
 */
public interface InterDaoProvincia extends InterCrud<Provincia> {

    // --- select ---
    Collection<Provincia> listar(Long idDepartamento) throws Exception;
    ArrayNode listar(Long limit, Long offSet) throws Exception;
    ArrayNode listar() throws Exception;
    Long count(Long idDepartamento) throws Exception;

    // --- insert ---
    void saveAll(List<Provincia> lista) throws UnknownException;

    // --- update ---
    void updateDescripcion(Long id, String descripcion) throws Exception;
    void marcarInactivo(Long id) throws Exception;

    // --- delete ---
    void deleteByIds(List<Long> ids) throws Exception;
    int deleteInactivos() throws Exception;
    int deleteAll() throws Exception;
}
