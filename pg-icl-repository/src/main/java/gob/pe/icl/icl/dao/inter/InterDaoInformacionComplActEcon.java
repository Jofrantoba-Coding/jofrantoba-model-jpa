/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterInformacionComplActEcon;
import gob.pe.icl.icl.entity.InformacionComplActEcon;

/**
 *
 * @author jtorresb
 */
public interface InterDaoInformacionComplActEcon extends InterCrud<InformacionComplActEcon> {

    ArrayNode listarFilter(FilterInformacionComplActEcon filter) throws Exception;

    ArrayNode listar(FilterInformacionComplActEcon filter, Long limit, Long offSet) throws Exception;

    ArrayNode listar(FilterInformacionComplActEcon filter) throws Exception;

    Long countActividadEconomica(Long idActividadEconomica) throws Exception;

    Long countCondicionDeclarante(Long idCondicionDeclarante) throws Exception;

    Long countestadoLlenado(Long idEstadoLlenado) throws Exception;

    Long countTipoDocumentoDecla(Long idTipoDocumentoDecla) throws Exception;

    Long countMantenimiento(Long idMantenimiento) throws Exception;

    Long countDocumentoPresentado(Long idDocumentoPresentado) throws Exception;

;
}
