/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.entity.Numeracion;

/**
 *
 * @author jtorresb
 */
public interface InterDaoNumeracion extends InterCrud<Numeracion>{    
    ArrayNode listarFilter(Long idLoteCatastral,Long idManzanaVia,Long idTipoPuerta, Long idCondicionNumeracion)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long countLoteCatastral(Long idLoteCatastral) throws Exception;
    Long countManzanaVia(Long idManzanaVia) throws Exception;
    Long countTipoPuerta(Long idTipoPuerta) throws Exception;
    Long countCondicionNumeracion(Long idCondicionNumeracion) throws Exception;
}
