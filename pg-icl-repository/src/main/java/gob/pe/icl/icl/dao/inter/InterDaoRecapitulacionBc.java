/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterRecapitulacionBc;
import gob.pe.icl.icl.entity.RecapitulacionBc;

/**
 *
 * @author jtorresb
 */
public interface InterDaoRecapitulacionBc extends InterCrud<RecapitulacionBc>{    
    ArrayNode listarFilter(FilterRecapitulacionBc filter)throws Exception;
    ArrayNode listar(FilterRecapitulacionBc filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterRecapitulacionBc filter)throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countUnidadCatastralRef(Long idUnidadCatastralBc) throws Exception;
}
