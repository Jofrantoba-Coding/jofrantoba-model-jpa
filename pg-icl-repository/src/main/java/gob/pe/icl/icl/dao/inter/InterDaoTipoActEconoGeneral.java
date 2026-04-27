/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.entity.TipoActEconoGeneral;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoTipoActEconoGeneral extends InterCrud<TipoActEconoGeneral>{
    Collection<TipoActEconoGeneral> listar() throws Exception;
    Collection<TipoActEconoGeneral> listar(int pageNumber, int pageSize) throws Exception;
}
