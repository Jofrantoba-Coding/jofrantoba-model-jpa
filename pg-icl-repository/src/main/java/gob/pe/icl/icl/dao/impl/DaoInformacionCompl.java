/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoInformacionCompl;
import gob.pe.icl.icl.dto.beans.FilterInformacionCompl;
import gob.pe.icl.icl.entity.InformacionCompl;
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
public class DaoInformacionCompl extends AbstractJpaDao<InformacionCompl> implements InterDaoInformacionCompl {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoInformacionCompl() {
        super();
        this.setClazz(InformacionCompl.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<InformacionCompl> listarFilter(FilterInformacionCompl filter) throws Exception {
        String[] joinTables = {"left:condicionDeclarante", "left:estadoLlenado", "left:tipoDocumentoDecla", "left:mantenimiento", "inner:unidadCatastral"};
        List<String> filters = new ArrayList();
        filters.add("=:base.isPersistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdCondicionDeclarante() != null) {
            filters.add("=:condicionDeclarante.id:" + filter.getIdCondicionDeclarante());
        }
        if (filter.getIdEstadoLlenado() != null) {
            filters.add("=:estadoLlenado.id:" + filter.getIdEstadoLlenado());
        }
        if (filter.getIdTipoDocumentoDecla() != null) {
            filters.add("=:tipoDocumentoDecla.id:" + filter.getIdTipoDocumentoDecla());
        }
        if (filter.getIdMantenimiento() != null) {
            filters.add("=:mantenimiento.id:" + filter.getIdMantenimiento());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("condicionDeclarante.id as idCondicionDeclarante,"));
        strFileds.append(share.append("condicionDeclarante.descripcion as condicionDeclarante,"));
        strFileds.append(share.append("estadoLlenado.id as idEstadoLlenado,"));
        strFileds.append(share.append("estadoLlenado.descripcion as estadoLlenado,"));
        strFileds.append(share.append("tipoDocumentoDecla.id as idTipoDocumentoDecla,"));
        strFileds.append(share.append("tipoDocumentoDecla.descripcion as tipoDocumentoDecla,"));
        strFileds.append(share.append("mantenimiento.id as idMantenimiento,"));
        strFileds.append(share.append("mantenimiento.descripcion as mantenimiento"));
        String fields = strFileds.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                InformacionCompl bean = new InformacionCompl();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<InformacionCompl> lista = (Collection<InformacionCompl>) this.customFieldsJoinFilterAnd(rt, fields, joinTables, mapFilterField, mapOrder);
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table = "icl.catastro.tgv_informacion_compl as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("condicionDeclarante.id as idCondicionDeclarante,"));
        strFileds.append(share.append("condicionDeclarante.descripcion as condicionDeclarante,"));
        strFileds.append(share.append("estadoLlenado.id as idEstadoLlenado,"));
        strFileds.append(share.append("estadoLlenado.descripcion as estadoLlenado,"));
        strFileds.append(share.append("tipoDocumentoDecla.id as idTipoDocumentoDecla,"));
        strFileds.append(share.append("tipoDocumentoDecla.descripcion as tipoDocumentoDecla,"));
        strFileds.append(share.append("mantenimiento.id as idMantenimiento,"));
        strFileds.append(share.append("mantenimiento.descripcion as mantenimiento"));
        String fields = strFileds.toString();
        String[] joinTables = new String[5];
        joinTables[0] = "inner:icl.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "left:icl.catastro.tm_parametrias as condicionDeclarante:on:base.id_condicion_declarante:condicionDeclarante.id";
        joinTables[2] = "left:icl.catastro.tm_parametrias as estadoLlenado:on:base.id_estado_llenado:estadoLlenado.id";
        joinTables[3] = "left:icl.catastro.tm_parametrias as tipoDocumentoDecla:on:base.id_tipo_documento_decla:tipoDocumentoDecla.id";
        joinTables[4] = "left:icl.catastro.tm_parametrias as mantenimiento:on:base.id_mantenimiento:mantenimiento.id";

        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "icl.catastro.tgv_informacion_compl as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("condicionDeclarante.id as idCondicionDeclarante,"));
        strFileds.append(share.append("condicionDeclarante.descripcion as condicionDeclarante,"));
        strFileds.append(share.append("estadoLlenado.id as idEstadoLlenado,"));
        strFileds.append(share.append("estadoLlenado.descripcion as estadoLlenado,"));
        strFileds.append(share.append("tipoDocumentoDecla.id as idTipoDocumentoDecla,"));
        strFileds.append(share.append("tipoDocumentoDecla.descripcion as tipoDocumentoDecla,"));
        strFileds.append(share.append("mantenimiento.id as idMantenimiento,"));
        strFileds.append(share.append("mantenimiento.descripcion as mantenimiento"));
        String fields = strFileds.toString();
        String[] joinTables = new String[5];
        joinTables[0] = "inner:icl.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "left:icl.catastro.tm_parametrias as condicionDeclarante:on:base.id_condicion_declarante:condicionDeclarante.id";
        joinTables[2] = "left:icl.catastro.tm_parametrias as estadoLlenado:on:base.id_estado_llenado:estadoLlenado.id";
        joinTables[3] = "left:icl.catastro.tm_parametrias as tipoDocumentoDecla:on:base.id_tipo_documento_decla:tipoDocumentoDecla.id";
        joinTables[4] = "left:icl.catastro.tm_parametrias as mantenimiento:on:base.id_mantenimiento:mantenimiento.id";

        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
    
      @Override
    public Long countestadoLlenado(Long idEstadoLlenado) throws Exception {
        String joinTable = "inner:estadoLlenado";
        String[] mapFilterField = {"=:estadoLlenado.id:" + idEstadoLlenado, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }


    @Override
    public Long countCondicionDeclarante(Long idCondicionDeclarante) throws Exception {
        String joinTable = "inner:condicionDeclarante";
        String[] mapFilterField = {"=:condicionDeclarante.id:" + idCondicionDeclarante, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countDocumentoDecla(Long idDocumentoDecla) throws Exception {
        String joinTable = "inner:tipoDocumentoDecla";
        String[] mapFilterField = {"=:tipoDocumentoDecla.id:" + idDocumentoDecla, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countMantenimiento(Long idMantenimiento) throws Exception {
        String joinTable = "inner:mantenimiento";
        String[] mapFilterField = {"=:mantenimiento.id:" + idMantenimiento, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
