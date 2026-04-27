/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoTitular;
import gob.pe.icl.icl.dto.beans.FilterTitular;
import gob.pe.icl.icl.entity.Titular;
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
public class DaoTitular extends AbstractJpaDao<Titular> implements InterDaoTitular {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoTitular(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(Titular.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<Titular> listarFilter(FilterTitular filter) throws Exception {
        String[] joinTables = {"inner:titularidad", "left:tipoTitular", "left:estadoCivil", "left:tipoDocumento", "left:tipoPersonaJuridica",
            "left:tipoUbicacion", "left:departamento", "left:provincia", "left:distrito", "left:tipoVia"};

        List<String> filters = new ArrayList();

        filters.add("=:base.isPersistente:true");
        if (filter.getIdTitularidad() != null) {
            filters.add("=:titularidad.id:" + filter.getIdTitularidad());
        }
        if (filter.getIdTipoTitular() != null) {
            filters.add("=:tipoTitular.id:" + filter.getIdTipoTitular());
        }
        if (filter.getIdEstadoCivil() != null) {
            filters.add("=:estadoCivil.id:" + filter.getIdEstadoCivil());
        }
        if (filter.getIdTipoDocumento() != null) {
            filters.add("=:tipoDocumento.id:" + filter.getIdTipoDocumento());
        }
        if (filter.getIdTipoPersonaJuridica() != null) {
            filters.add("=:tipoPersonaJuridica.id:" + filter.getIdTipoPersonaJuridica());
        }
        if (filter.getIdTipoUbicacion() != null) {
            filters.add("=:tipoUbicacion.id:" + filter.getIdTipoUbicacion());
        }
        if (filter.getIdDepartamento() != null) {
            filters.add("=:departamento.id:" + filter.getIdDepartamento());
        }
        if (filter.getIdProvincia() != null) {
            filters.add("=:provincia.id:" + filter.getIdProvincia());
        }
        if (filter.getIdDistrito() != null) {
            filters.add("=:distrito.id:" + filter.getIdDistrito());
        }
        if (filter.getIdTipoVia() != null) {
            filters.add("=:tipoVia.id:" + filter.getIdTipoVia());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.nombreRuc as nombreRuc,"));
        strFileds.append(share.append("base.apellidoPaterno as apellidoPaterno,"));
        strFileds.append(share.append("base.apellidoMaterno as apellidoMaterno,"));
        strFileds.append(share.append("base.porcentaje as porcentaje,"));
        strFileds.append(share.append("titularidad.id as idTitularidad,"));
        strFileds.append(share.append("tipoTitular.id as idTipoTitular,"));
        strFileds.append(share.append("tipoTitular.descripcion as tipoTitular,"));
        strFileds.append(share.append("estadoCivil.id as idEstadoCivil,"));
        strFileds.append(share.append("estadoCivil.descripcion as estadoCivil,"));
        strFileds.append(share.append("tipoDocumento.id as idTipoDocumento,"));
        strFileds.append(share.append("tipoDocumento.descripcion as tipoDocumento,"));
        strFileds.append(share.append("base.codigoContribuyente as codigoContribuyente,"));
        strFileds.append(share.append("base.numeroDocumento as numeroDocumento,"));
        strFileds.append(share.append("tipoPersonaJuridica.id as idTipoPersonaJuridica,"));
        strFileds.append(share.append("tipoPersonaJuridica.descripcion as tipoPersonaJuridica,"));
        strFileds.append(share.append("tipoUbicacion.id as idTipoUbicacion,"));
        strFileds.append(share.append("tipoUbicacion.descripcion as tipoUbicacion,"));
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
                Titular bean = new Titular();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Titular> lista = (Collection<Titular>) this.customFieldsJoinFilterAnd(rt, fields, joinTables, mapFilterField, mapOrder);
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table = "icl.catastro.tgv_titular as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.nombre_ruc as nombreRuc,"));
        strFileds.append(share.append("base.porcentaje as porcentaje,"));
        strFileds.append(share.append("base.codigo_contribuyente as codigoContribuyente,"));
        strFileds.append(share.append("base.numero_documento as numeroDocumento,"));
        strFileds.append(share.append("titularidad.id as idTitularidad,"));
        strFileds.append(share.append("tipoTitular.id as idTipoTitular,"));
        strFileds.append(share.append("tipoTitular.descripcion as tipoTitular,"));
        strFileds.append(share.append("estadoCivil.id as idEstadoCivil,"));
        strFileds.append(share.append("estadoCivil.descripcion as estadoCivil,"));
        strFileds.append(share.append("tipoDocumento.id as idTipoDocumento,"));
        strFileds.append(share.append("tipoDocumento.descripcion as tipoDocumento,"));
        strFileds.append(share.append("tipoPersonaJuridica.id as idTipoPersonaJuridica,"));
        strFileds.append(share.append("tipoPersonaJuridica.descripcion as tipoPersonaJuridica,"));
        strFileds.append(share.append("tipoUbicacion.id as idTipoUbicacion,"));
        strFileds.append(share.append("tipoUbicacion.descripcion as tipoUbicacion,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as departamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as provincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as distrito,"));
        strFileds.append(share.append("tipoVia.id as idTipoVia,"));
        strFileds.append(share.append("tipoVia.descripcion as tipoVia"));
        String fields = strFileds.toString();
        String[] joinTables = new String[10];

        joinTables[0] = "inner:icl.catastro.tgv_titularidad as titularidad:on:base.id_titularidad:titularidad.id";
        joinTables[1] = "left:icl.catastro.tm_parametrias as tipoTitular:on:base.id_tipo_titular:tipoTitular.id";
        joinTables[2] = "left:icl.catastro.tm_parametrias as estadoCivil:on:base.id_estado_civil:estadoCivil.id";
        joinTables[3] = "left:icl.catastro.tm_parametrias as tipoDocumento:on:base.id_tipo_documento:tipoDocumento.id";
        joinTables[4] = "left:icl.catastro.tm_parametrias as tipoPersonaJuridica:on:base.id_tipo_persona_juridica:tipoPersonaJuridica.id";
        joinTables[5] = "left:icl.catastro.tm_parametrias as tipoUbicacion:on:base.id_tipo_ubicacion:tipoUbicacion.id";
        joinTables[6] = "left:icl.catastro.tm_departamento as departamento:on:base.id_departamento:departamento.id";
        joinTables[7] = "left:icl.catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";
        joinTables[8] = "left:icl.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";
        joinTables[9] = "left:(select * from icl.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:base.id_tipo_via:tipoVia.id";

        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "icl.catastro.tgv_titular as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.nombre_ruc as nombreRuc,"));
        strFileds.append(share.append("base.porcentaje as porcentaje,"));
        strFileds.append(share.append("base.codigo_contribuyente as codigoContribuyente,"));
        strFileds.append(share.append("base.numero_documento as numeroDocumento,"));
        strFileds.append(share.append("titularidad.id as idTitularidad,"));
        strFileds.append(share.append("tipoTitular.id as idTipoTitular,"));
        strFileds.append(share.append("tipoTitular.descripcion as tipoTitular,"));
        strFileds.append(share.append("estadoCivil.id as idEstadoCivil,"));
        strFileds.append(share.append("estadoCivil.descripcion as estadoCivil,"));
        strFileds.append(share.append("tipoDocumento.id as idTipoDocumento,"));
        strFileds.append(share.append("tipoDocumento.descripcion as tipoDocumento,"));
        strFileds.append(share.append("tipoPersonaJuridica.id as idTipoPersonaJuridica,"));
        strFileds.append(share.append("tipoPersonaJuridica.descripcion as tipoPersonaJuridica,"));
        strFileds.append(share.append("tipoUbicacion.id as idTipoUbicacion,"));
        strFileds.append(share.append("tipoUbicacion.descripcion as tipoUbicacion,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as departamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as provincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as distrito,"));
        strFileds.append(share.append("tipoVia.id as idTipoVia,"));
        strFileds.append(share.append("tipoVia.descripcion as tipoVia"));
        String fields = strFileds.toString();
        String[] joinTables = new String[10];
          joinTables[0] = "inner:icl.catastro.tgv_titularidad as titularidad:on:base.id_titularidad:titularidad.id";
        joinTables[1] = "left:icl.catastro.tm_parametrias as tipoTitular:on:base.id_tipo_titular:tipoTitular.id";
        joinTables[2] = "left:icl.catastro.tm_parametrias as estadoCivil:on:base.id_estado_civil:estadoCivil.id";
        joinTables[3] = "left:icl.catastro.tm_parametrias as tipoDocumento:on:base.id_tipo_documento:tipoDocumento.id";
        joinTables[4] = "left:icl.catastro.tm_parametrias as tipoPersonaJuridica:on:base.id_tipo_persona_juridica:tipoPersonaJuridica.id";
        joinTables[5] = "left:icl.catastro.tm_parametrias as tipoUbicacion:on:base.id_tipo_ubicacion:tipoUbicacion.id";
        joinTables[6] = "left:icl.catastro.tm_departamento as departamento:on:base.id_departamento:departamento.id";
        joinTables[7] = "left:icl.catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";
        joinTables[8] = "left:icl.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";
        joinTables[9] = "left:(select * from icl.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:base.id_tipo_via:tipoVia.id";
        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public Long countTipoTitular(Long idTipoTitular) throws Exception {
        String joinTable = "inner:tipoTitular";
        String[] mapFilterField = {"=:tipoTitular.id:" + idTipoTitular, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTitularidad(Long idTitularidad) throws Exception {
        String joinTable = "inner:titularidad";
        String[] mapFilterField = {"=:titularidad.id:" + idTitularidad, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countEstadoCivil(Long idEstadoCivil) throws Exception {
        String joinTable = "inner:estadoCivil";
        String[] mapFilterField = {"=:estadoCivil.id:" + idEstadoCivil, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoDocumento(Long idCountTipoDocumento) throws Exception {
        String joinTable = "inner:tipoDocumento";
        String[] mapFilterField = {"=:tipoDocumento.id:" + idCountTipoDocumento, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoPersonaJuridica(Long idTipoPersonaJuridica) throws Exception {
        String joinTable = "inner:tipoPersonaJuridica";
        String[] mapFilterField = {"=:tipoPersonaJuridica.id:" + idTipoPersonaJuridica, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoUbicacion(Long idTipoUbicacion) throws Exception {
        String joinTable = "inner:tipoTitular";
        String[] mapFilterField = {"=:tipoTitular.id:" + idTipoUbicacion, "=:base.isPersistente:true"};
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
