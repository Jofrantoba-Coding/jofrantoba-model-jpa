/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterDocumentos;
import gob.pe.icl.icl.entity.Documentos;

/**
 *
 * @author jtorresb
 */
public interface InterDaoDocumentos extends InterCrud<Documentos>{    
    ArrayNode listarFilter(FilterDocumentos filter)throws Exception;
    ArrayNode listar(FilterDocumentos filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterDocumentos filter)throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countTipoDocumento(Long idTipoDocumento) throws Exception;
}
