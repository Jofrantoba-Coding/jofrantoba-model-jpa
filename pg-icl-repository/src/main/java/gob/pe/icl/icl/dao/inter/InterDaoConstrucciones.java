/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterConstrucciones;
import gob.pe.icl.icl.entity.Construcciones;

/**
 *
 * @author jtorresb
 */
public interface InterDaoConstrucciones extends InterCrud<Construcciones>{    
    ArrayNode listarFilter(FilterConstrucciones filter)throws Exception;
    ArrayNode listar(FilterConstrucciones filter,Long limit, Long offSet)throws Exception;
    ArrayNode listar(FilterConstrucciones filter) throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countNivel(Long idNivel) throws Exception;
    Long countMes(Long idMes) throws Exception;
    Long countMaterialEstructural(Long idMaterialEstructural) throws Exception;
    Long countEstadoConservacion(Long idEstadoConservacion) throws Exception;
    Long countEstadoConstruccion(Long idEstadoConstruccion) throws Exception;
    Long countUca(Long idUca) throws Exception;
}
