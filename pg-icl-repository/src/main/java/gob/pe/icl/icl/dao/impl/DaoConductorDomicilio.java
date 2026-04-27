/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoConductorDomicilio;
import gob.pe.icl.icl.dto.beans.FilterConductorDomicilio;
import gob.pe.icl.icl.entity.ConductorDomicilio;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoConductorDomicilio extends AbstractJpaDao<ConductorDomicilio> implements InterDaoConductorDomicilio {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoConductorDomicilio() {
        super();
        this.setClazz(ConductorDomicilio.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<ConductorDomicilio> listarFilter(FilterConductorDomicilio filter) throws Exception {
        String[] joinTables = new String[5];
        joinTables[0] = "inner:actividadEconomica";
        joinTables[1] = "left:departamento";
        joinTables[2] = "left:provincia";
        joinTables[3] = "left:distrito";
        joinTables[4] = "left:tipoVia";
        List<String> filters = new ArrayList();
        filters.add("=:base.isPersistente:true");
        if (filter.getIdActividadEconomica()!= null) {
            filters.add("=:actividadEconomica.id:" + filter.getIdActividadEconomica());
        }
        if (filter.getIdDepartamento()!= null) {
            filters.add("=:departamento.id:" + filter.getIdDepartamento());
        }
        if (filter.getIdProvincia()!= null) {
            filters.add("=:provincia.id:" + filter.getIdProvincia());
        }
        if (filter.getIdTipoVia()!= null) {
            filters.add("=:tipoVia.id:" + filter.getIdTipoVia());
        }
        
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as departamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as provincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as distrito,"));
        strFileds.append(share.append("tipoVia.id as idTipoVia,"));
        strFileds.append(share.append("tipoVia.descripcion as tipoVia"));
        String fields = strFileds.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                ConductorDomicilio bean = new ConductorDomicilio();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<ConductorDomicilio> lista = (Collection<ConductorDomicilio>) this.customFieldsJoinFilterAnd(rt, fields, joinTables, mapFilterField, mapOrder);
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table = "icl.catastro.tgv_conductor_domicilio as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.codigo_via as codigoVia,"));
        strFileds.append(share.append("base.nombre_via as nombreVia,"));
        strFileds.append(share.append("base.numero_municipal as numeroMunicipal,"));
        strFileds.append(share.append("base.numero_edificcion as nombreEdificcion,"));
        strFileds.append(share.append("base.numero_interior as numerorInterior,"));
        strFileds.append(share.append("base.numero_edificcion as nombreEdificcion,"));
        strFileds.append(share.append("base.numero_interior as numeroInterior,"));
        strFileds.append(share.append("base.codigo_hu as codigoHu,"));
        strFileds.append(share.append("base.nombre_hu as nombreHu,"));
        strFileds.append(share.append("base.zona_sector_etapa as zonaSectorEtapa,"));
        strFileds.append(share.append("base.manzana as manzana,"));
        strFileds.append(share.append("base.lote as lote,"));
        strFileds.append(share.append("base.sub_lote as subLote,"));
        strFileds.append(share.append("base.telefono as telefono,"));
        strFileds.append(share.append("base.anexo as anexo,"));
        strFileds.append(share.append("base.fax as fax,"));
        strFileds.append(share.append("base.correo_electronico as correoElectronico,"));
        strFileds.append(share.append("actividadEconomica.id as idActividadEconomica,"));
        strFileds.append(share.append("actividadEconomica.nombre as nombreActividadEconomica,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as departamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as provincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as distrito,"));
        strFileds.append(share.append("tipoVia.id as idTipoVia,"));
        strFileds.append(share.append("tipoVia.descripcion as tipoVia"));
        String fields = strFileds.toString();
        String[] joinTables = new String[5];

        joinTables[0] = "inner:icl.catastro.tgv_actividad_economica as actividadEconomica:on:base.id_actividad_economica:actividadEconomica.id";
        joinTables[1] = "left:icl.catastro.tm_departamento as departamento:on:base.id_departamento:departamento.id";
        joinTables[2] = "left:icl.catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";
        joinTables[3] = "left:icl.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";
        joinTables[4] = "left:(select * from icl.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:base.id_tipo_via:tipoVia.id";

        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "icl.catastro.tgv_conductor_domicilio as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.codigo_via as codigoVia,"));
        strFileds.append(share.append("base.nombre_via as nombreVia,"));
        strFileds.append(share.append("base.numero_municipal as numeroMunicipal,"));
        strFileds.append(share.append("base.numero_edificcion as nombreEdificcion,"));
        strFileds.append(share.append("base.numero_interior as numerorInterior,"));
        strFileds.append(share.append("base.numero_edificcion as nombreEdificcion,"));
        strFileds.append(share.append("base.numero_interior as numeroInterior,"));
        strFileds.append(share.append("base.codigo_hu as codigoHu,"));
        strFileds.append(share.append("base.nombre_hu as nombreHu,"));
        strFileds.append(share.append("base.zona_sector_etapa as zonaSectorEtapa,"));
        strFileds.append(share.append("base.manzana as manzana,"));
        strFileds.append(share.append("base.lote as lote,"));
        strFileds.append(share.append("base.sub_lote as subLote,"));
        strFileds.append(share.append("base.telefono as telefono,"));
        strFileds.append(share.append("base.anexo as anexo,"));
        strFileds.append(share.append("base.fax as fax,"));
        strFileds.append(share.append("base.correo_electronico as correoElectronico,"));
        strFileds.append(share.append("actividadEconomica.id as idActividadEconomica,"));
        strFileds.append(share.append("actividadEconomica.nombre as nombreActividadEconomica,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as departamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as provincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as distrito,"));
        strFileds.append(share.append("tipoVia.id as idTipoVia,"));
        strFileds.append(share.append("tipoVia.descripcion as tipoVia"));
        String fields = strFileds.toString();
        String[] joinTables = new String[5];

        joinTables[0] = "inner:icl.catastro.tgv_actividad_economica as actividadEconomica:on:base.id_actividad_economica:actividadEconomica.id";
        joinTables[1] = "left:icl.catastro.tm_departamento as departamento:on:base.id_departamento:departamento.id";
        joinTables[2] = "left:icl.catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";
        joinTables[3] = "left:icl.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";
        joinTables[4] = "left:(select * from icl.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:base.id_tipo_via:tipoVia.id";
        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public Long countActividadEconomica(Long idActividadEconomica) throws Exception {
        String joinTable = "inner:actividadEconomica";
        String[] mapFilterField = {"=:actividadEconomica.id:" + idActividadEconomica, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countDepartamento(Long idDepartamento) throws Exception {
        String joinTable = "inner:departamento";
        String[] mapFilterField = {"=:departamento.id:" + idDepartamento, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countProvincia(Long idProvincia) throws Exception {
        String joinTable = "inner:provincia";
        String[] mapFilterField = {"=:provincia.id:" + idProvincia, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countDistrito(Long idDistrito) throws Exception {
        String joinTable = "inner:distrito";
        String[] mapFilterField = {"=:distrito.id:" + idDistrito, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoVia(Long idTipoVia) throws Exception {
        String joinTable = "inner:tipoVia";
        String[] mapFilterField = {"=:tipoVia.id:" + idTipoVia, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

}
