/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.entity.Linderos;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoLinderos extends InterCrud<Linderos>{    
    Collection<Linderos> listarFilter(Long idLoteCatastral,Long idTipoLindero)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long countLoteCatastral(Long idLoteCatastral) throws Exception;
    Long countTipoLindero(Long idTipoLindero) throws Exception;
}
