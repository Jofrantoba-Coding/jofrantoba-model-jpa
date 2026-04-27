/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.CodigoLoteCatastral;
import gob.pe.icl.icl.dto.beans.FilterLoteCatastral;
import gob.pe.icl.icl.entity.LoteCatastral;

/**
 *
 * @author jtorresb
 */
public interface InterDaoLoteCatastral extends InterCrud<LoteCatastral>{    
    ArrayNode listarFilter(FilterLoteCatastral filter)throws Exception;
    ArrayNode listar(FilterLoteCatastral filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterLoteCatastral filter)throws Exception;
    ArrayNode listarLotes(Long idManzana)throws Exception;
    Long countManzana(Long idManzana) throws Exception;
    //Long countHabilitacionUrbana(Long idHabilitacionUrbana) throws Exception;
    String newValueCodigoLote(CodigoLoteCatastral codigoLoteCatastral)throws Exception;
}
