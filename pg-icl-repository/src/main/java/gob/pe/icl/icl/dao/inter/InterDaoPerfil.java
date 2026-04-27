/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterPerfil;
import gob.pe.icl.icl.entity.Perfil;

/**
 *
 * @author jtorresb
 */


public interface InterDaoPerfil extends InterCrud<Perfil>{
    ArrayNode listar(FilterPerfil filter) throws Exception;
    ArrayNode listar(FilterPerfil filter, Long limit, Long offSet) throws Exception;
}