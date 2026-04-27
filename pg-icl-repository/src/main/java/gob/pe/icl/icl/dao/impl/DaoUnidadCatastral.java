/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoUnidadCatastral;
import gob.pe.icl.icl.dto.beans.CodigoUnidadCatastral;
import gob.pe.icl.icl.dto.beans.FilterUnidadCatastral;
import gob.pe.icl.icl.entity.UnidadCatastral;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoUnidadCatastral extends AbstractJpaDao<UnidadCatastral> implements InterDaoUnidadCatastral {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoUnidadCatastral(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(UnidadCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public ArrayNode listarFilter(FilterUnidadCatastral filter)throws Exception {        
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String groupBy = (String) map.get("groupBy");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String fieldsSq = (String) map.get("fieldsSq");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBySq = (String) map.get("groupBySq");
        return this.allFieldsJoinPostgresGroupBySubQuery(fields, groupBy, mapOrder, joinTables, table, fieldsSq, mapFilterField, groupBySq);
    }

    @Override
    public ArrayNode listar(FilterUnidadCatastral filter,Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String groupBy = (String) map.get("groupBy");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String fieldsSq = (String) map.get("fieldsSq");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBySq = (String) map.get("groupBySq");
        return this.allFieldsLimitJoinPostgresGroupBySubQuery(fields, groupBy, mapOrder, joinTables, table, fieldsSq, mapFilterField, groupBySq, limit, offSet);
    }
    
    @Override
    public ArrayNode listar(FilterUnidadCatastral filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String groupBy = (String) map.get("groupBy");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String fieldsSq = (String) map.get("fieldsSq");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBySq = (String) map.get("groupBySq");
        return this.allFieldsJoinPostgresGroupBySubQuery(fields, groupBy, mapOrder, joinTables, table, fieldsSq, mapFilterField, groupBySq);
    }
    
    @Override
    public Long countLoteCatastral(Long idLoteCatastral) throws Exception {
        String joinTable="inner:loteCatastral";
        String[] mapFilterField={"=:loteCatastral.id:"+idLoteCatastral,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
    
    @Override
    public Long countUsoEspecifico(Long idUsoEspecifico) throws Exception {
        String joinTable="inner:usoEspecifico";
        String[] mapFilterField={"=:usoEspecifico.id:"+idUsoEspecifico,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public Long countClasificacionPredio(Long idClasificacionPredio) throws Exception {
        String joinTable="inner:clasificacionPredio";
        String[] mapFilterField={"=:clasificacionPredio.id:"+idClasificacionPredio,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public Long countPredioEn(Long idPredioEn) throws Exception {
        String joinTable="inner:predioEn";
        String[] mapFilterField={"=:predioEn.id:"+idPredioEn,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public String newValueCodigoUnidad(CodigoUnidadCatastral codigoUnidadCatastral) throws Exception {
        String fields = "cast(coalesce(max(base.codigoUnidad),'000') as long)+1";
        String[] mapFilterField = new String[10];
        //mapFilterField[0] = "=:base.isPersistente:true";
        mapFilterField[0] = "equal:base.codigoDepartamento:"+codigoUnidadCatastral.getCodigoDepartamento();
        mapFilterField[1] = "equal:base.codigoProvincia:"+codigoUnidadCatastral.getCodigoProvincia();
        mapFilterField[2] = "equal:base.codigoDistrito:"+codigoUnidadCatastral.getCodigoDistrito();
        mapFilterField[3] = "equal:base.codigoSector:"+codigoUnidadCatastral.getCodigoSector();
        mapFilterField[4] = "equal:base.codigoManzana:"+codigoUnidadCatastral.getCodigoManzana();
        mapFilterField[5] = "equal:base.codigoLote:"+codigoUnidadCatastral.getCodigoLote();
        mapFilterField[6] = "equal:base.codigoEdificacion:"+codigoUnidadCatastral.getCodigoEdificacion();
        mapFilterField[7] = "equal:base.codigoEntrada:"+codigoUnidadCatastral.getCodigoEntrada();
        mapFilterField[8] = "equal:base.codigoPiso:"+codigoUnidadCatastral.getCodigoPiso();
        mapFilterField[9] = "equal:base.tipo:"+"UNIDADCATASTRAL";
        
        Long valueNew = this.aggregateJoinFilterAndGroupBy(fields, null, mapFilterField, null);
        String codigo="000"+valueNew;
        codigo=codigo.substring(codigo.length()-3, codigo.length());
        return codigo;
    }
    
    private Map<String, Object> buildQueryList(FilterUnidadCatastral filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.catastro.tgv_unidad_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("id,"));
        strFileds.append(share.append("idLoteCatastral,"));
        strFileds.append(share.append("codigoUnidadCatastral,"));
        strFileds.append(share.append("codigoLoteCatastral,"));
        strFileds.append(share.append("ubigeo,"));
        strFileds.append(share.append("codigoSector,"));
        strFileds.append(share.append("codigoManzana,"));
        strFileds.append(share.append("codigoLote,"));
        strFileds.append(share.append("codigoEdificacion,"));
        strFileds.append(share.append("codigoEntrada,"));
        strFileds.append(share.append("codigoPiso,"));
        strFileds.append(share.append("codigoUnidad,"));
        strFileds.append(share.append("idManzana,"));
        strFileds.append(share.append("descripcionManzana,"));
        strFileds.append(share.append("idCondicionTitularidad,"));
        strFileds.append(share.append("descripcionCondicionTitularidad,"));
        strFileds.append(share.append("case when"));
        strFileds.append(share.append("(select count(*) from icl.catastro.tgv_interior ti where ti.id_unidad_catastral =subquery.id and ti.is_persistente =true)>0 then"));
        strFileds.append(share.append("string_agg(direccion, '; ')"));
                strFileds.append(share.append("else '' end as direccion"));
        String fields = strFileds.toString();
        
        StringBuilder strFiledsSq = new StringBuilder();
        strFiledsSq.append(share.append("base.id as id,"));
        strFiledsSq.append(share.append("base.codigo as codigoUnidadCatastral,"));
        strFiledsSq.append(share.append("loteCatastral.codigo as codigoLoteCatastral,"));
        strFiledsSq.append(share.append("loteCatastral.id as idLoteCatastral,"));
        strFiledsSq.append(share.append("concat(base.codigo_departamento,base.codigo_provincia,base.codigo_distrito) as ubigeo,"));
        strFiledsSq.append(share.append("base.codigo_sector as codigoSector,"));
        strFiledsSq.append(share.append("base.codigo_manzana as codigoManzana,"));
        strFiledsSq.append(share.append("base.codigo_lote as codigoLote,"));
        strFiledsSq.append(share.append("base.codigo_edificacion as codigoEdificacion,"));
        strFiledsSq.append(share.append("base.codigo_entrada as codigoEntrada,"));
        strFiledsSq.append(share.append("base.codigo_piso as codigoPiso,"));
        strFiledsSq.append(share.append("base.codigo_unidad as codigoUnidad,"));
        strFiledsSq.append(share.append("manzana.id as idManzana,"));
        strFiledsSq.append(share.append("manzana.descripcion as descripcionManzana,"));
        strFiledsSq.append(share.append("condicionTitularidad.id as idCondicionTitularidad,"));
        strFiledsSq.append(share.append("condicionTitularidad.descripcion as descripcionCondicionTitularidad,"));
        strFiledsSq.append(share.append("concat(tipoVia.descripcion,' ',via.descripcion,' N° ',string_agg(distinct numeracion.numero,','),' ',tipoEdificacion.descripcion,' ',interior.interior,' ',interior.letra_interior) as direccion"));
        String fieldsSq = strFiledsSq.toString();
        
        String[] joinTables = new String[12];
        joinTables[0] = "inner:icl.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        joinTables[1] = "inner:icl.catastro.tg_manzana as manzana:on:loteCatastral.id_manzana:manzana.id";
        joinTables[2] = "inner:icl.catastro.tg_sector as sector:on:manzana.id_sector:sector.id";
        joinTables[3] = "left:icl.catastro.tgv_titularidad as titularidad:on:base.id:titularidad.id_unidad_catastral";
        joinTables[4] = "left:(select * from icl.catastro.tm_parametrias where id_parametria_padre=96) as condicionTitularidad:on:titularidad.id_condicion_titularidad:condicionTitularidad.id";
        joinTables[5] = "left:icl.catastro.tgv_numeracion as numeracion:on:loteCatastral.id:numeracion.id_lote_catastral";
        joinTables[6] = "left:icl.catastro.tgv_interior as interior:on:interior.id_numeracion:numeracion.id and interior.id_unidad_catastral=base.id";
        joinTables[7] = "left:icl.catastro.tm_parametrias as tipoEdificacion:on:interior.id_tipo_edificacion:tipoEdificacion.id";
        joinTables[8] = "left:icl.catastro.tg_manzana_via as manzanaVia:on:numeracion.id_manzana_via:manzanaVia.id";
        joinTables[9] = "left:icl.catastro.tg_via_cuadra as viaCuadra:on:manzanaVia.id_via_cuadra:viaCuadra.id";
        joinTables[10] = "left:icl.catastro.tm_via as via:on:viaCuadra.id_via:via.id";
        joinTables[11] = "left:(select * from icl.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (strValid(filter.getCodigoDepartamento())) {
            filters.add("equal:base.codigo_departamento:" + filter.getCodigoDepartamento());
        }
        if (strValid(filter.getCodigoProvincia())) {
            filters.add("equal:base.codigo_provincia:" + filter.getCodigoProvincia());
        }
        if (strValid(filter.getCodigoDistrito())) {
            filters.add("equal:base.codigo_distrito:" + filter.getCodigoDistrito());
        }
        if (strValid(filter.getCodigoSector())) {
            filters.add("equal:base.codigo_sector:" + filter.getCodigoSector());
        }
        if (strValid(filter.getCodigoManzana())) {
            filters.add("equal:base.codigo_manzana:" + filter.getCodigoManzana());
        }
        if (strValid(filter.getCodigoLote())) {
            filters.add("equal:base.codigo_lote:" + filter.getCodigoLote());
        }
        if (strValid(filter.getCodigoEdificacion())) {
            filters.add("equal:base.codigo_edificacion:" + filter.getCodigoEdificacion());
        }
        if (strValid(filter.getCodigoEntrada())) {
            filters.add("equal:base.codigo_entrada:" + filter.getCodigoEntrada());
        }
        if (strValid(filter.getCodigoPiso())) {
            filters.add("equal:base.codigo_piso:" + filter.getCodigoPiso());
        }
        if (strValid(filter.getCodigoUnidad())) {
            filters.add("equal:base.codigo_unidad:" + filter.getCodigoUnidad());
        }
        if (filter.getIdLoteCatastral() != null) {
            filters.add("=:base.id_lote_catastral:" + filter.getIdLoteCatastral());
        }
        if (filter.getIdUsoEspecifico() != null) {
            filters.add("=:base.id_uso_especifico:" + filter.getIdUsoEspecifico());
        }
        if (filter.getIdClasificacionPredio() != null) {
            filters.add("=:base.id_clasificacion_predio:" + filter.getIdClasificacionPredio());
        }
        if (filter.getIdPredioEn() != null) {
            filters.add("=:base.id_predio_en:" + filter.getIdPredioEn());
        }
        if (filter.getIdManzana() != null) {
            filters.add("=:manzana.id:" + filter.getIdManzana());
        }
       // if (filter.getIdHabilitacionUrbana() != null) {
        //    filters.add("=:habilitacionUrbana.id:" + filter.getIdHabilitacionUrbana());
       // }
        if (filter.getIdDistrito() != null) {
            filters.add("=:sector.id_distrito:" + filter.getIdDistrito());
        }
        if (filter.getIdVia() != null) {
            filters.add("=:via.id:" + filter.getIdVia());
        }
        if (strValid(filter.getNumero())) {
            filters.add("equal:numeracion.numero:" + filter.getNumero());
        }
        if (strValid(filter.getLetra())) {
            filters.add("equal:numeracion.letra:" + filter.getLetra());
        }
        if (strValid(filter.getTipo())) {
            filters.add("equal:base.tipo:" + filter.getTipo());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        StringBuilder groupBySq = new StringBuilder();
        groupBySq.append(share.append("base.id,"));
        groupBySq.append(share.append("base.codigo,"));
        groupBySq.append(share.append("loteCatastral.codigo,"));
        groupBySq.append(share.append("loteCatastral.id,"));
        groupBySq.append(share.append("base.codigo_departamento,"));
        groupBySq.append(share.append("base.codigo_provincia,"));
        groupBySq.append(share.append("base.codigo_distrito,"));
        groupBySq.append(share.append("base.codigo_sector,"));
        groupBySq.append(share.append("base.codigo_manzana,"));
        groupBySq.append(share.append("base.codigo_lote,"));
        groupBySq.append(share.append("base.codigo_edificacion,"));
        groupBySq.append(share.append("base.codigo_entrada,"));
        groupBySq.append(share.append("base.codigo_piso,"));
        groupBySq.append(share.append("base.codigo_unidad,"));
        groupBySq.append(share.append("manzana.id,"));
        groupBySq.append(share.append("manzana.descripcion,"));
        groupBySq.append(share.append("condicionTitularidad.id,"));
        groupBySq.append(share.append("condicionTitularidad.descripcion,"));
        groupBySq.append(share.append("tipoVia.descripcion,"));
        groupBySq.append(share.append("via.descripcion,"));
        groupBySq.append(share.append("tipoEdificacion.descripcion,"));
        groupBySq.append(share.append("interior.interior,"));
        groupBySq.append(share.append("interior.letra_interior"));
        
        StringBuilder groupBy = new StringBuilder();
        groupBy.append(share.append("id,"));
        groupBy.append(share.append("codigoUnidadCatastral,"));        
        groupBy.append(share.append("codigoLoteCatastral,"));        
        groupBy.append(share.append("idLoteCatastral,"));        
        groupBy.append(share.append("ubigeo,"));
        groupBy.append(share.append("codigoSector,"));
        groupBy.append(share.append("codigoManzana,"));
        groupBy.append(share.append("codigoLote,"));
        groupBy.append(share.append("codigoEdificacion,"));
        groupBy.append(share.append("codigoEntrada,"));
        groupBy.append(share.append("codigoPiso,"));
        groupBy.append(share.append("codigoUnidad,"));
        groupBy.append(share.append("idManzana,"));
        groupBy.append(share.append("descripcionManzana,"));
        groupBy.append(share.append("idCondicionTitularidad,"));
        groupBy.append(share.append("descripcionCondicionTitularidad"));
        String[] mapOrder = {"id:asc"};
        map.put("fields", fields);
        map.put("groupBy", groupBy.toString());
        map.put("mapOrder", mapOrder);
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fieldsSq", fieldsSq);
        map.put("mapFilterField", mapFilterField);
        map.put("groupBySq", groupBySq.toString());
        return map;
    }
    
    private boolean strValid(String value) {
        return (value != null && !value.trim().isEmpty());
    }

    @Override
    public Collection<UnidadCatastral> listarIdsBc(String codigoLoteCatastral) throws Exception {
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.codigo as codigo"));
        String fields=strFields.toString();
        String[] mapFilterField={"equal:base.tipo:UNIDADCATASTRAL","equal:base.codigoLoteCatastral:"+codigoLoteCatastral.trim(),"=:base.isPersistente:true"};
        String[] mapOrder={"base.id:asc"};
        Collection<UnidadCatastral> lista=(Collection<UnidadCatastral>)this.customFieldsFilterAnd(UnidadCatastral.class,fields,mapFilterField,mapOrder);        
        return lista;
    }

    @Override
    public Collection<UnidadCatastral> listarIdsEdi(String codigoLoteCatastral) throws Exception {
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.codigo as codigo"));
        String fields=strFields.toString();
        String[] mapFilterField={"equal:base.tipo:BIENCOMUN","equal:base.codigoLoteCatastral:"+codigoLoteCatastral,"notequal:base.codigoEdificacion:99","equal:base.codigoEntrada:99","=:base.isPersistente:true"};
        String[] mapOrder={"base.id:asc"};
        Collection<UnidadCatastral> lista=(Collection<UnidadCatastral>)this.customFieldsFilterAnd(UnidadCatastral.class,fields,mapFilterField,mapOrder);        
        return lista;
    }
    
    @Override
    public ArrayNode listarBienesComunesSinAsociar(String codigoLoteCatastral) throws Exception{
        String table = "icl.catastro.tgv_unidad_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.codigo_lote_catastral as codigoLoteCatastral"));
        String fields=strFileds.toString();
        String[] joinTables=new String[1];
        joinTables[0]="left:icl.catastro.tgv_recapitulacion_bc as recapitulacion_bc:on:base.id:recapitulacion_bc.id_unidad_catastral_ref";
        String[] mapFilterField={"isnull:recapitulacion_bc.id","=:base.is_persistente:true","equal:base.tipo:UNIDADCATASTRAL","equal:base.codigo_lote_catastral:"+codigoLoteCatastral};
        String[] mapOrder={"base.id:asc"};                
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
    
    @Override
    public ArrayNode listarEdificionesSinAsociar(String codigoLoteCatastral) throws Exception{
        String table = "icl.catastro.tgv_unidad_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.codigo_lote_catastral as codigoLoteCatastral"));
        String fields=strFileds.toString();
        String[] joinTables=new String[1];
        joinTables[0]="left:icl.catastro.tgv_recapitulacion_edificios as recapitulacion_ed:on:base.id:recapitulacion_ed.id_unidad_catastral_ref";
        String[] mapFilterField={"notequal:base.codigo_edificacion:99","isnull:recapitulacion_ed.id","equal:base.tipo:BIENCOMUN","equal:base.codigo_lote_catastral:"+codigoLoteCatastral,"equal:base.codigo_entrada:99","=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};                
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
}
