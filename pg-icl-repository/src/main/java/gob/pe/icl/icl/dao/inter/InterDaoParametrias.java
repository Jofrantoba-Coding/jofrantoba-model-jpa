/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.entity.Parametrias;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoParametrias extends InterCrud<Parametrias>{
    Collection<Parametrias> parents()throws Exception;
    Collection<Parametrias> parents(int pageNumber, int pageSize)throws Exception;
    Collection<Parametrias> childrens()throws Exception;
    Collection<Parametrias> childrensByParents(Long idParent)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long countChildrens(Long idParent)throws Exception;
}
