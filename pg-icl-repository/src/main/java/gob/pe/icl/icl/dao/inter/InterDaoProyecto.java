/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterProyecto;
import gob.pe.icl.icl.entity.Proyecto;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoProyecto extends InterCrud<Proyecto>{
    Collection<Proyecto> listar(FilterProyecto filter) throws Exception;
    Collection<Proyecto> listar(FilterProyecto filter, int pageNumber, int pageSize) throws Exception;
}
