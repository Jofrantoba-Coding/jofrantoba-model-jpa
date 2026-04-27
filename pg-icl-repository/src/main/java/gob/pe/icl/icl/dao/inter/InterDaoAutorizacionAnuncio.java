/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterAutorizacionAnuncio;
import gob.pe.icl.icl.entity.AutorizacionAnuncio;

/**
 *
 * @author jtorresb
 */
public interface InterDaoAutorizacionAnuncio extends InterCrud<AutorizacionAnuncio> {

    ArrayNode listarFilter(FilterAutorizacionAnuncio filter) throws Exception;

    ArrayNode listar(FilterAutorizacionAnuncio filter, Long limit, Long offSet) throws Exception;

    ArrayNode listar(FilterAutorizacionAnuncio filter) throws Exception;

    Long countActividadEconomica(Long idActividadEconomica) throws Exception;

    Long countAnuncio(Long idAnuncio) throws Exception;
}
