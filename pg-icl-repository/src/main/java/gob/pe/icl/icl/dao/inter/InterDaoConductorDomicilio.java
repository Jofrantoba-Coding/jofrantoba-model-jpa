/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterConductorDomicilio;
import gob.pe.icl.icl.entity.ConductorDomicilio;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoConductorDomicilio extends InterCrud<ConductorDomicilio> {

    Collection<ConductorDomicilio> listarFilter(FilterConductorDomicilio filter) throws Exception;

    ArrayNode listar(Long limit, Long offSet) throws Exception;

    ArrayNode listar() throws Exception;

    Long countActividadEconomica(Long idActividadEconomica) throws Exception;

    Long countDepartamento(Long idDepartamento) throws Exception;

    Long countProvincia(Long idProvincia) throws Exception;

    Long countDistrito(Long idDistrito) throws Exception;

    Long countTipoVia(Long idTipoVia) throws Exception;

}
