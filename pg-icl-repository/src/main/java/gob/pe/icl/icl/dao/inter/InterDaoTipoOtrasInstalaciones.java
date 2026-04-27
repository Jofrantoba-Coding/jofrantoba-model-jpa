/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.entity.TipoOtrasInstalaciones;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoTipoOtrasInstalaciones extends InterCrud<TipoOtrasInstalaciones>{
    Collection<TipoOtrasInstalaciones> listar() throws Exception;
    Collection<TipoOtrasInstalaciones> listar(int pageNumber, int pageSize) throws Exception;
}
