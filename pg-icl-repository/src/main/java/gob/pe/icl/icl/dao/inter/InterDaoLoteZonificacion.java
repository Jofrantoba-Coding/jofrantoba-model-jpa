/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.entity.LoteZonificacion;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoLoteZonificacion extends InterCrud<LoteZonificacion>{    
    Collection<LoteZonificacion> listarFilter(Long idLoteCatastral,Long idZonificacion)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long countLoteCatastral(Long idLoteCatastral) throws Exception;
    Long countZonificacion(Long idZonificacion) throws Exception;
    Long countLoteZonificacion(Long idLoteCatastral,Long idZonificacion)throws Exception;
}
