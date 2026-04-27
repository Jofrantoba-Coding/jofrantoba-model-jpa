/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterRecapitulacionEdificios;
import gob.pe.icl.icl.entity.RecapitulacionEdificios;
import gob.pe.icl.icl.entity.UnidadCatastral;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoRecapitulacionEdificios extends InterCrud<RecapitulacionEdificios>{    
    ArrayNode listarFilter(FilterRecapitulacionEdificios filter)throws Exception;
    ArrayNode listar(FilterRecapitulacionEdificios filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterRecapitulacionEdificios filter)throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countUnidadCatastralRef(Long idUnidadCatastralBc) throws Exception;
    Collection<UnidadCatastral> udsCastSinAsociar() throws Exception;
}
