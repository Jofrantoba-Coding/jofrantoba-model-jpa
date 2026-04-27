/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterUsuario;
import gob.pe.icl.icl.entity.Usuario;

/**
 *
 * @author jtorresb
 */


public interface InterDaoUsuario extends InterCrud<Usuario>{
    ArrayNode listar(FilterUsuario filter) throws Exception;
    ArrayNode listar(FilterUsuario filter, Long limit, Long offSet) throws Exception;
    ArrayNode idPerfiles(FilterUsuario filter) throws Exception;
}