/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daomenu;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jofrantoba
 */
public interface InterDaoMenu extends InterCrud<Menu> {

    // --- select ---
    Collection<Menu> parents(FilterMenu filter) throws Exception;
    Collection<Menu> parents(FilterMenu filter, int pageNumber, int pageSize) throws Exception;
    Collection<Menu> childrens(FilterMenu filter) throws Exception;
    Collection<Menu> childrensByParents(Long idParent) throws Exception;
    ArrayNode listar(FilterMenu filter, Long limit, Long offSet) throws Exception;
    ArrayNode listar(FilterMenu filter) throws Exception;
    Long countChildrens(Long idParent) throws Exception;
    Long maxOrdenChildrens(Long idParent) throws Exception;
    Long maxOrdenNivel(Long idClienteSistema, Long nivel) throws Exception;
    ArrayNode createTreeMenu(FilterMenu filter) throws Exception;

    // --- insert ---
    void saveAll(List<Menu> lista) throws UnknownException;

    // --- update ---
    void updateDescripcion(Long id, String descripcion) throws Exception;
    void updateOrden(Long id, Long orden) throws Exception;
    void marcarInactivo(Long id) throws Exception;

    // --- delete ---
    void deleteByIds(List<Long> ids) throws Exception;
    int deleteInactivos() throws Exception;
    int deleteAll() throws Exception;
}
