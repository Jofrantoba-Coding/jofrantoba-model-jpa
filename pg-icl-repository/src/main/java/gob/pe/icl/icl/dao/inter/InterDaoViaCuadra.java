/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.entity.ViaCuadra;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoViaCuadra extends InterCrud<ViaCuadra>{    
    Collection<ViaCuadra> listar(Long idVia)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idDistrito) throws Exception;
}
