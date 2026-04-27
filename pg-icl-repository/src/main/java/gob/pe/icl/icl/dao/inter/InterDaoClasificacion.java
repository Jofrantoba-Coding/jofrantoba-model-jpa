/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterClasificacion;
import gob.pe.icl.icl.entity.Clasificacion;

/**
 *
 * @author jtorresb
 */


public interface InterDaoClasificacion extends InterCrud<Clasificacion>{
    ArrayNode listar(FilterClasificacion filter) throws Exception;
    ArrayNode listar(FilterClasificacion filter, Long limit, Long offSet) throws Exception;
    Long count(Long idCategoria) throws Exception;
}