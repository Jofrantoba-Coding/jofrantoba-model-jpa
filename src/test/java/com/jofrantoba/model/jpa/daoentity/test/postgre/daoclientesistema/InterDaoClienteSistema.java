/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoclientesistema;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jofrantoba
 */
public interface InterDaoClienteSistema extends InterCrud<ClienteSistema> {

    // --- select ---
    Collection<ClienteSistema> listar(Long idSistema) throws Exception;
    ArrayNode listar(Long limit, Long offSet) throws Exception;
    ArrayNode listar() throws Exception;
    Long count(Long idSistema) throws Exception;

    // --- insert ---
    void saveAll(List<ClienteSistema> lista) throws UnknownException;

    // --- update ---
    void updateDescripcion(Long id, String descripcion) throws Exception;
    void updateClientId(Long id, String clientId) throws Exception;
    void marcarInactivo(Long id) throws Exception;

    // --- delete ---
    void deleteByIds(List<Long> ids) throws Exception;
    int deleteInactivos() throws Exception;
    int deleteAll() throws Exception;
}
