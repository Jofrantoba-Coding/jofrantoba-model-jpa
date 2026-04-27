/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gob.pe.icl.icl.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import gob.pe.icl.icl.dto.beans.FilterTitular;
import gob.pe.icl.icl.entity.Titular;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoTitular extends InterCrud<Titular> {

    Collection<Titular> listarFilter(FilterTitular filter) throws Exception;

    ArrayNode listar(Long limit, Long offSet) throws Exception;

    ArrayNode listar() throws Exception;

    Long countTitularidad(Long idTitularidad) throws Exception;

    Long countTipoTitular(Long idTipoTitular) throws Exception;

    Long countEstadoCivil(Long idEstadoCivil) throws Exception;

    Long countTipoDocumento(Long idCountTipoDocumento) throws Exception;

    Long countTipoPersonaJuridica(Long idTipoPersonaJuridica) throws Exception;

    Long countTipoUbicacion(Long idTipoUbicacion) throws Exception;

    Long countDepartamento(Long idDepartamento) throws Exception;

    Long countProvincia(Long idProvincia) throws Exception;

    Long countDistrito(Long idDistrito) throws Exception;

    Long countTipoVia(Long idTipoVia) throws Exception;

}
