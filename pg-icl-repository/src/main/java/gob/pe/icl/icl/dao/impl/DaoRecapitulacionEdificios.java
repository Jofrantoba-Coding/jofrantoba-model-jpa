/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoRecapitulacionEdificios;
import gob.pe.icl.icl.dto.beans.FilterRecapitulacionEdificios;
import gob.pe.icl.icl.entity.RecapitulacionEdificios;
import gob.pe.icl.icl.entity.UnidadCatastral;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoRecapitulacionEdificios extends AbstractJpaDao<RecapitulacionEdificios> implements InterDaoRecapitulacionEdificios {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoRecapitulacionEdificios() {
        super();
        this.setClazz(RecapitulacionEdificios.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterRecapitulacionEdificios filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    @Override
    public ArrayNode listar(FilterRecapitulacionEdificios filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }

    @Override
    public ArrayNode listar(FilterRecapitulacionEdificios filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }
    
    @Override
    public Collection<UnidadCatastral> udsCastSinAsociar() throws Exception{
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        
        sql.append(sharedUtil.append("select * from"));
        sql.append("catastro.tgv_unidad_catastral as uc");
        sql.append(sharedUtil.append("left join"));
        sql.append("catastro.tgv_recapitulacion_edificios as r_ed");
        sql.append(sharedUtil.append("on"));
        sql.append("uc.id = r_ed.id_unidad_catastral");
        sql.append(sharedUtil.append("where"));
        sql.append("r_ed.id is null");
        
        Query query = this.getCurrentSession().createQuery(sql.toString());
        return query.list();
    }

    private Map<String, Object> buildQueryList(FilterRecapitulacionEdificios filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tgv_recapitulacion_edificios as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.porcentaje as numeroDocumento,"));
        strFileds.append(share.append("base.area_terreno_comun as areaTerrenoComun,"));
        strFileds.append(share.append("base.area_construida_comun as areaConstruidaComun,"));
        strFileds.append(share.append("base.area_otras_instalacion_comun as areaOtrasInstalacionComun,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("unidadCatastral.codigo_departamento as codigoDepartamento,"));
        strFileds.append(share.append("unidadCatastral.codigo_provincia as codigoProvincia,"));
        strFileds.append(share.append("unidadCatastral.codigo_distrito as codigoDistrito,"));
        strFileds.append(share.append("unidadCatastral.codigo_sector as codigoSector,"));
        strFileds.append(share.append("unidadCatastral.codigo_manzana as codigoManzana,"));
        strFileds.append(share.append("unidadCatastral.codigo_lote as codigoLote,"));
        strFileds.append(share.append("unidadCatastral.codigo_edificacion as codigoEdificacion,"));
        strFileds.append(share.append("unidadCatastral.codigo_entrada as codigoEntrada,"));
        strFileds.append(share.append("unidadCatastral.codigo_piso as codigoPiso,"));
        strFileds.append(share.append("unidadCatastral.codigo_unidad as codigoUnidad,"));
        strFileds.append(share.append("unidadCatastral.area_terreno_adquirida as areaTerrenoAdquirida,"));
        strFileds.append(share.append("unidadCatastral.area_terreno_verificada as areaTerrenoVerificada,"));
        strFileds.append(share.append("unidadCatastral.area_libre as areaLibre,"));
        strFileds.append(share.append("unidadCatastral.area_suma_const_ver as areaSumaConstVer,"));
        strFileds.append(share.append("unidadCatastral.area_ocupada as areaOcupada,"));
        strFileds.append(share.append("unidadCatastral.porc_bc_terreno_legal as porcBcTerrenoLegal,"));
        strFileds.append(share.append("unidadCatastral.porc_bc_terreno_fisico as porcBcTerrenoFisico,"));
        strFileds.append(share.append("unidadCatastral.porc_bc_construccion_legal as porcBcConstruccionLegal,"));
        strFileds.append(share.append("unidadCatastral.porc_bc_construccion_fisico as porcBcConstruccionFisico,"));        
        strFileds.append(share.append("unidadCatastralref.id as idBienComun,"));
        strFileds.append(share.append("unidadCatastralref.codigo_departamento as codigoDepartamentoBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_provincia as codigoProvinciaBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_distrito as codigoDistritoBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_sector as codigoSectorBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_manzana as codigoManzanaBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_lote as codigoLoteBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_edificacion as codigoEdificacionBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_entrada as codigoEntradaBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_piso as codigoPisoBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_unidad as codigoUnidadBc,"));
        strFileds.append(share.append("unidadCatastralref.area_terreno_adquirida as areaTerrenoAdquiridaBc,"));
        strFileds.append(share.append("unidadCatastralref.area_terreno_verificada as areaTerrenoVerificadaBc,"));
        strFileds.append(share.append("unidadCatastralref.area_libre as areaLibreBc,"));
        strFileds.append(share.append("unidadCatastralref.area_suma_const_ver as areaSumaConstVerBc,"));
        strFileds.append(share.append("unidadCatastralref.area_ocupada as areaOcupadaBc,"));
        strFileds.append(share.append("unidadCatastralref.porc_bc_terreno_legal as porcBcTerrenoLegalBc,"));
        strFileds.append(share.append("unidadCatastralref.porc_bc_terreno_fisico as porcBcTerrenoFisicoBc,"));
        strFileds.append(share.append("unidadCatastralref.porc_bc_construccion_legal as porcBcConstruccionLegalBc,"));
        strFileds.append(share.append("unidadCatastralref.porc_bc_construccion_fisico as porcBcConstruccionFisicoBc"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:icl.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:icl.catastro.tgv_unidad_catastral as unidadCatastralref:on:base.id_unidad_catastral_ref:unidadCatastralref.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdUnidadCatastralRef()!= null) {
            filters.add("=:unidadCatastralref.id:" + filter.getIdUnidadCatastralRef());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countUnidadCatastralRef(Long idUnidadCatastralRef) throws Exception {
        String joinTable = "inner:unidadCatastralRef";
        String[] mapFilterField = {"=:unidadCatastralRef.id:" + idUnidadCatastralRef, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
