/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.entity.Titularidad;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoTitularidad extends InterCrud<Titularidad>{    
    Collection<Titularidad> listarFilter(Long idUnidadCatastral,Long idCondicionTitular,Long idFormaAdquisicion)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countCondicionTitular(Long idCondicionTitular) throws Exception;
    Long countFormaAdquisicion(Long idFormaAdquisicion) throws Exception;
}
