/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterLoteHabilitacionUrbana;
import gob.pe.icl.icl.entity.LoteHabilitacionUrbana;

/**
 *
 * @author jtorresb
 */
public interface InterDaoLoteHabilitacionUrbana extends InterCrud<LoteHabilitacionUrbana> {

    ArrayNode listarFilter(FilterLoteHabilitacionUrbana filter) throws Exception;

    ArrayNode listar(FilterLoteHabilitacionUrbana filter, Long limit, Long offSet) throws Exception;

    ArrayNode listar(FilterLoteHabilitacionUrbana filter) throws Exception;

    Long countLoteCatastral(Long idCountLoteCatastral) throws Exception;

    Long countHabilitacionUrbana(Long idHabilitacionUrbana) throws Exception;
    
    Long countLoteHabilitacionUrbana(Long idLoteCatastral,Long idHabilitacionUrbana) throws Exception;
}
