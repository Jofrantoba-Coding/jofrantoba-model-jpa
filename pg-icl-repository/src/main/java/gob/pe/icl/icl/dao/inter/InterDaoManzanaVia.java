/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterManzanaVia;
import gob.pe.icl.icl.entity.ManzanaVia;

/**
 *
 * @author jtorresb
 */
public interface InterDaoManzanaVia extends InterCrud<ManzanaVia>{    
    ArrayNode listarFilter(FilterManzanaVia filter)throws Exception;
    ArrayNode listar(FilterManzanaVia filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterManzanaVia filter)throws Exception;
    Long countManzana(Long idManzana) throws Exception;
    Long countViaCuadra(Long idViaCuadra) throws Exception;
}
