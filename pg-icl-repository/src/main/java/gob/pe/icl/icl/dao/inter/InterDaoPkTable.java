/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterPkTable;
import gob.pe.icl.icl.entity.PkTable;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoPkTable extends InterCrud<PkTable>{
    Collection<PkTable> listar() throws Exception;
    Collection<PkTable> listar(int pageNumber, int pageSize) throws Exception;
    Collection<PkTable> listar(FilterPkTable filter)throws Exception;
    Long count(String tabla, String codigo) throws Exception;
    int deletePkbyLikeCodigo(String codigo) throws Exception;
}
