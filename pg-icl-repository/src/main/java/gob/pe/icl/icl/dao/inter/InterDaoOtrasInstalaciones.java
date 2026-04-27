/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterOtrasInstalaciones;
import gob.pe.icl.icl.entity.OtrasInstalaciones;

/**
 *
 * @author jtorresb
 */
public interface InterDaoOtrasInstalaciones extends InterCrud<OtrasInstalaciones> {

    ArrayNode listarFilter(FilterOtrasInstalaciones filter) throws Exception;  
    
    ArrayNode listar(FilterOtrasInstalaciones filter,Long limit, Long offSet) throws Exception;
    
    ArrayNode listar(FilterOtrasInstalaciones filter) throws Exception;

    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;

    Long countTipoOtrasInstalaciones(Long idTipoOtrasInstalaciones) throws Exception;

    Long counntMes(Long counntMes) throws Exception;

    Long countMaterialEstructural(Long idMaterialEstructural) throws Exception;

    Long countEstadoConservacion(Long idEstadoConservacion) throws Exception;

    Long countEstadoConstruccion(Long idEstadoConstruccion) throws Exception;

    Long countUca(Long idUca) throws Exception;

}
