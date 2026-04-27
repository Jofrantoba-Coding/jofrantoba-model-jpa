/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.entity.CucUnidad;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoCucUnidad extends InterCrud<CucUnidad>{
    Collection<CucUnidad> listar(Long idLoteCatastral) throws Exception;
    ArrayNode listar(Long limit, Long offset) throws Exception;
    ArrayNode listar() throws Exception;
    Long count(Long idLoteCatastral) throws Exception;
}
