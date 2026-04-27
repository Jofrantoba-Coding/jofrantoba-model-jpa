/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterPerfilMenu;
import gob.pe.icl.icl.entity.PerfilMenu;

/**
 *
 * @author jtorresb
 */
public interface InterDaoPerfilMenu extends InterCrud<PerfilMenu>{   
    ArrayNode listar(FilterPerfilMenu filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterPerfilMenu filter)throws Exception;
    Long countPerfil(Long idPerfil) throws Exception;
    Long countMenu(Long idMenu) throws Exception;
    int deletePerfilMenu(Long idPerfil)throws Exception;
    ArrayNode createTreeMenu(FilterPerfilMenu filter) throws Exception;
    ArrayNode createTreeMenuPerfiles(FilterPerfilMenu filter) throws Exception;
}
