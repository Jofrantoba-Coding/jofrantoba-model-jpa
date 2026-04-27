/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterCategoria;
import gob.pe.icl.icl.entity.Categoria;

/**
 *
 * @author jtorresb
 */


public interface InterDaoCategoria extends InterCrud<Categoria>{
    ArrayNode listar(FilterCategoria filter) throws Exception;
    ArrayNode listar(FilterCategoria filter, Long limit, Long offSet) throws Exception;
    Long count(Long idDistrito) throws Exception;
}