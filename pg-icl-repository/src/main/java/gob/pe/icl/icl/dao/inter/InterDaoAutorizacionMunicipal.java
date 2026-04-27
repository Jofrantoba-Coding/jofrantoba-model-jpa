/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterAutorizacionMunicipal;
import gob.pe.icl.icl.entity.AutorizacionMunicipal;

/**
 *
 * @author jtorresb
 */
public interface InterDaoAutorizacionMunicipal extends InterCrud<AutorizacionMunicipal> {

    ArrayNode listarFilter(FilterAutorizacionMunicipal filter) throws Exception;

    ArrayNode listar(FilterAutorizacionMunicipal filter, Long limit, Long offSet) throws Exception;

    ArrayNode listar(FilterAutorizacionMunicipal filter) throws Exception;

    Long countActividadEconomica(Long idActividadEconomica) throws Exception;

    Long countTipoActEconoEspecifico(Long idTipoActEconoEspecifico) throws Exception;
}
