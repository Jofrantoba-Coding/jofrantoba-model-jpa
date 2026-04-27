/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterInformacionCompl;
import gob.pe.icl.icl.entity.InformacionCompl;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoInformacionCompl extends InterCrud<InformacionCompl> {

    Collection<InformacionCompl> listarFilter(FilterInformacionCompl filter) throws Exception;

    ArrayNode listar(Long limit, Long offSet) throws Exception;

    ArrayNode listar() throws Exception;

    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;

    Long countCondicionDeclarante(Long idCondicionDeclarante) throws Exception;

    Long countestadoLlenado(Long idEstadoLlenado) throws Exception;

    Long countDocumentoDecla(Long idDocumentoDecla) throws Exception;

    Long countMantenimiento(Long idMantenimiento) throws Exception;

}
