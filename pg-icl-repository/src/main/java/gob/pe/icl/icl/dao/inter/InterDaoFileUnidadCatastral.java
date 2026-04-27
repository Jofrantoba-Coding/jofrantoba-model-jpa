/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterFile;
import gob.pe.icl.icl.entity.FileUnidadCatastral;

/**
 *
 * @author jtorresb
 */


public interface InterDaoFileUnidadCatastral extends InterCrud<FileUnidadCatastral>{
    ArrayNode listar(FilterFile filter) throws Exception;
    ArrayNode listar(FilterFile filter, Long limit, Long offSet) throws Exception;
    void asignarPrincipal(Long id) throws Exception;
    void limpiarPrincipal(Long idUnidadCatastral) throws Exception ;
    //Long count(Long idCategoria) throws Exception;
}