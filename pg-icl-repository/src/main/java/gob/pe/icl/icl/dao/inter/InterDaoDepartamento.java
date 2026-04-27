/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.entity.Departamento;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoDepartamento extends InterCrud<Departamento>{
    Collection<Departamento> listar() throws Exception;
    Collection<Departamento> listar(int pageNumber, int pageSize) throws Exception;
}
