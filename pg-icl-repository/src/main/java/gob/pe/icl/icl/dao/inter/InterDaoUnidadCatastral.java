/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.CodigoUnidadCatastral;
import gob.pe.icl.icl.dto.beans.FilterUnidadCatastral;
import gob.pe.icl.icl.entity.UnidadCatastral;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoUnidadCatastral extends InterCrud<UnidadCatastral>{    
    ArrayNode listarFilter(FilterUnidadCatastral filter)throws Exception;
    ArrayNode listar(FilterUnidadCatastral filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterUnidadCatastral filter)throws Exception;
    Long countLoteCatastral(Long idLoteCatastral) throws Exception;
    Long countUsoEspecifico(Long idUsoEspecifico) throws Exception;
    Long countClasificacionPredio(Long idClasificionPredio) throws Exception;
    Long countPredioEn(Long idPredioEn) throws Exception;
    String newValueCodigoUnidad(CodigoUnidadCatastral codigoUnidadCatastral)throws Exception;
    Collection<UnidadCatastral> listarIdsBc(String codigoLoteCatastral)throws Exception;
    Collection<UnidadCatastral> listarIdsEdi(String codigoLoteCatastral)throws Exception;
    ArrayNode listarBienesComunesSinAsociar(String codigoLoteCatastral) throws Exception;
    ArrayNode listarEdificionesSinAsociar(String codigoLoteCatastral) throws Exception;
}
