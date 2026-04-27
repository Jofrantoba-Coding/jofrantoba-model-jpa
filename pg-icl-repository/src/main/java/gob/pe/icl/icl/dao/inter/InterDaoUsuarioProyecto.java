/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterUsuarioProyecto;
import gob.pe.icl.icl.entity.UsuarioProyecto;

/**
 *
 * @author jtorresb
 */
public interface InterDaoUsuarioProyecto extends InterCrud<UsuarioProyecto> {

    ArrayNode listarFilter(FilterUsuarioProyecto filter) throws Exception;

    ArrayNode listar(FilterUsuarioProyecto filter, Long limit, Long offSet) throws Exception;

    ArrayNode listar(FilterUsuarioProyecto filter) throws Exception;

    Long countProyecto(Long idCountLoteCatastral) throws Exception;

    Long countUsuario(Long idHabilitacionUrbana) throws Exception;
    
    Long countUsuarioProyecto(Long idLoteCatastral,Long idHabilitacionUrbana) throws Exception;
}
