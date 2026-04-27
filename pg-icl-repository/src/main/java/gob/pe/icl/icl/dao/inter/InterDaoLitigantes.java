/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterLitigantes;
import gob.pe.icl.icl.entity.Litigantes;

/**
 *
 * @author jtorresb
 */
public interface InterDaoLitigantes extends InterCrud<Litigantes>{    
    ArrayNode listarFilter(FilterLitigantes filter)throws Exception;
    ArrayNode listar(FilterLitigantes filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterLitigantes filter)throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countTipoDocumento(Long idTipoDocumento) throws Exception;
}
