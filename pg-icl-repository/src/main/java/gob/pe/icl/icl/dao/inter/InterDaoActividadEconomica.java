/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterActividadEconomica;
import gob.pe.icl.icl.entity.ActividadEconomica;

/**
 *
 * @author jtorresb
 */
public interface InterDaoActividadEconomica extends InterCrud<ActividadEconomica> {

    ArrayNode listarFilter(FilterActividadEconomica filter) throws Exception;

    ArrayNode listar(FilterActividadEconomica filter, Long limit, Long offSet) throws Exception;

    ArrayNode listar(FilterActividadEconomica filter) throws Exception;

    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;

    Long couhtTipoDocIdent(Long idTipoDocIdent) throws Exception;

    Long countTipoConductor(Long idTipoConductor) throws Exception;

    Long countCondicionConductor(Long idCondicionConductor) throws Exception;
}
