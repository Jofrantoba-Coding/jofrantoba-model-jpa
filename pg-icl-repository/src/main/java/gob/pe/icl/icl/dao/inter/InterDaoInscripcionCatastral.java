/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterInscripcionCatastral;
import gob.pe.icl.icl.entity.InscripcionCatastral;

/**
 *
 * @author jtorresb
 */
public interface InterDaoInscripcionCatastral extends InterCrud<InscripcionCatastral>{    
    ArrayNode listarFilter(FilterInscripcionCatastral filter)throws Exception;
    ArrayNode listar(FilterInscripcionCatastral filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterInscripcionCatastral filter)throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countTipoPartidaRegistral(Long idTipoPartidaCatastral) throws Exception;
    Long countDeclaratoriaFabrica(Long idDeclaratoriaFabrica) throws Exception;

}
