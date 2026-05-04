/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoparametrias;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jona
 */
public interface InterDaoParametrias extends InterCrud<Parametrias> {

    // --- select ---
    Collection<Parametrias> parents() throws Exception;

    Collection<Parametrias> parents(int pageNumber, int pageSize) throws Exception;

    Collection<Parametrias> childrens() throws Exception;

    Collection<Parametrias> childrensByParents(Long idParent) throws Exception;

    Long countChildrens(Long idParent) throws Exception;

    ArrayNode listar(Long limit, Long offSet) throws Exception;

    ArrayNode listar() throws Exception;

    // --- insert ---
    void saveAll(List<Parametrias> lista) throws UnknownException;

    // --- update ---
    void updateDescripcion(Long id, String descripcion) throws Exception;

    void updateOrden(Long id, Long orden) throws Exception;

    void marcarInactivo(Long id) throws Exception;

    // --- delete ---
    void deleteByIds(List<Long> ids) throws Exception;

    int deleteInactivos() throws Exception;

    int deleteAll() throws Exception;
}
