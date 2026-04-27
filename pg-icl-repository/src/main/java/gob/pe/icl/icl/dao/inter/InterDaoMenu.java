/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterMenu;
import gob.pe.icl.icl.entity.Menu;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoMenu extends InterCrud<Menu>{
    Collection<Menu> parents(FilterMenu filter)throws Exception;
    Collection<Menu> parents(FilterMenu filter,int pageNumber, int pageSize)throws Exception;
    Collection<Menu> childrens(FilterMenu filter)throws Exception;
    Collection<Menu> childrensByParents(Long idParent)throws Exception;
    ArrayNode listar(FilterMenu filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterMenu filter)throws Exception;
    Long countChildrens(Long idParent)throws Exception;
    Long maxOrdenChildrens(Long idParent) throws Exception;
    Long maxOrdenNivel(Long idClienteSistema,Long nivel) throws Exception;
    ArrayNode createTreeMenu(FilterMenu filter)throws Exception;
}
