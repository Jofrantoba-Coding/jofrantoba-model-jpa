/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterInterior;
import gob.pe.icl.icl.entity.Interior;

/**
 *
 * @author jtorresb
 */
public interface InterDaoInterior extends InterCrud<Interior>{    
    ArrayNode listarFilter(FilterInterior filter)throws Exception;
    ArrayNode listar(FilterInterior filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterInterior filter)throws Exception;;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countNumeracion(Long idNumeracion) throws Exception;
    Long countTipoEdificacion(Long idTipoEdificacion) throws Exception;
    Long countTipoInterior(Long idTipoInterior) throws Exception;
}
