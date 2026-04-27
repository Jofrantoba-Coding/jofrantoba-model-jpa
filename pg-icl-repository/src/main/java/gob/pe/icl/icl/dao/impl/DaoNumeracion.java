/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoNumeracion;
import gob.pe.icl.icl.entity.Numeracion;
import java.util.ArrayList;
import java.util.List;
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
public class DaoNumeracion extends AbstractJpaDao<Numeracion> implements InterDaoNumeracion {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoNumeracion() {
        super();
        this.setClazz(Numeracion.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public ArrayNode listarFilter(Long idLoteCatastral,Long idManzanaVia,Long idTipoPuerta, Long idCondicionNumeracion) throws Exception {
        String table="icl.catastro.tgv_numeracion as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("tipoVia.descripcion as tipoVia,"));
        strFileds.append(share.append("via.descripcion as via,")); 
        strFileds.append(share.append("base.numero as numero,"));        
        strFileds.append(share.append("base.letra as letra,"));        
        strFileds.append(share.append("via.codigo_via as codigoVia,"));      
        strFileds.append(share.append("viaCuadra.cuadra as cuadra,"));      
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("manzanaVia.id as idManzanaVia,"));       
        strFileds.append(share.append("tipoPuerta.id as idTipoPuerta,"));
        strFileds.append(share.append("tipoPuerta.descripcion as descripcionPuerta,"));
        strFileds.append(share.append("condicionNumeracion.id as idCondicionNumeracion,"));
        strFileds.append(share.append("condicionNumeracion.descripcion as descripcionCondicionNumeracion,"));       
        strFileds.append(share.append("trim(concat(coalesce(tipoVia.descripcion,''),' ',coalesce(via.descripcion,''),' Cda. ',coalesce(cast(viaCuadra.cuadra as varchar),''))) as direccionNumeracion"));       
        String fields=strFileds.toString();
        String[] joinTables=new String[7];
        joinTables[0]="inner:icl.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        joinTables[1]="inner:icl.catastro.tg_manzana_via as manzanaVia:on:base.id_manzana_via:manzanaVia.id";
        joinTables[2]="left:icl.catastro.tg_via_cuadra as viaCuadra:on:manzanaVia.id_via_cuadra:viaCuadra.id";
        joinTables[3]="left:icl.catastro.tm_via as via:on:viaCuadra.id_via:via.id"; 
        joinTables[4]="left:(select * from icl.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        joinTables[5]="left:icl.catastro.tm_parametrias as condicionNumeracion:on:base.id_condicion_numeracion:condicionNumeracion.id";
        joinTables[6]="left:icl.catastro.tm_parametrias as tipoPuerta:on:base.id_tipo_puerta:tipoPuerta.id";
        List<String> filter=new ArrayList();
        filter.add("=:base.is_persistente:true");
        if(idLoteCatastral!=null){
            filter.add("=:loteCatastral.id:"+idLoteCatastral);
        }
        if(idManzanaVia!=null){
            filter.add("=:manzanaVia.id:"+idManzanaVia);
        }
        if(idTipoPuerta!=null){
            filter.add("=:tipoPuerta.id:"+idTipoPuerta);
        }
        if(idCondicionNumeracion!=null){
            filter.add("=:condicionNumeracion.id:"+idCondicionNumeracion);
        }
        String[] mapFilterField=filter.toArray(new String[0]);
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="icl.catastro.tgv_numeracion as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("tipoVia.descripcion as tipoVia,"));
        strFileds.append(share.append("via.descripcion as via,")); 
        strFileds.append(share.append("base.numero as numero,"));        
        strFileds.append(share.append("base.letra as letra,"));       
        strFileds.append(share.append("via.codigo_via as codigoVia,"));      
        strFileds.append(share.append("viaCuadra.cuadra as cuadra,"));        
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("manzanaVia.id as idManzanaVia,"));       
        strFileds.append(share.append("tipoPuerta.id as idTipoPuerta,"));
        strFileds.append(share.append("tipoPuerta.descripcion as descripcionPuerta,"));
        strFileds.append(share.append("condicionNumeracion.id as idCondicionNumeracion,"));
        strFileds.append(share.append("condicionNumeracion.descripcion as descripcionCondicionNumeracion"));       
        String fields=strFileds.toString();
        String[] joinTables=new String[7];
        joinTables[0]="inner:icl.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        joinTables[1]="inner:icl.catastro.tg_manzana_via as manzanaVia:on:base.id_manzana_via:manzanaVia.id";
        joinTables[2]="left:icl.catastro.tg_via_cuadra as viaCuadra:on:manzanaVia.id_via_cuadra:viaCuadra.id";
        joinTables[3]="left:icl.catastro.tm_via as via:on:viaCuadra.id_via:via.id"; 
        joinTables[4]="left:(select * from icl.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        joinTables[5]="left:icl.catastro.tm_parametrias as condicionNumeracion:on:base.id_condicion_numeracion:condicionNumeracion.id";
        joinTables[6]="left:icl.catastro.tm_parametrias as tipoPuerta:on:base.id_tipo_puerta:tipoPuerta.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);               
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="icl.catastro.tgv_numeracion as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("tipoVia.descripcion as tipoVia,"));
        strFileds.append(share.append("via.descripcion as via,")); 
        strFileds.append(share.append("base.numero as numero,"));        
        strFileds.append(share.append("base.letra as letra,"));     
        strFileds.append(share.append("via.codigo_via as codigoVia,"));      
        strFileds.append(share.append("viaCuadra.cuadra as cuadra,"));            
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("manzanaVia.id as idManzanaVia,"));       
        strFileds.append(share.append("tipoPuerta.id as idTipoPuerta,"));
        strFileds.append(share.append("tipoPuerta.descripcion as descripcionPuerta,"));
        strFileds.append(share.append("condicionNumeracion.id as idCondicionNumeracion,"));
        strFileds.append(share.append("condicionNumeracion.descripcion as descripcionCondicionNumeracion"));       
        String fields=strFileds.toString();
        String[] joinTables=new String[7];
        joinTables[0]="inner:icl.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        joinTables[1]="inner:icl.catastro.tg_manzana_via as manzanaVia:on:base.id_manzana_via:manzanaVia.id";
        joinTables[2]="left:icl.catastro.tg_via_cuadra as viaCuadra:on:manzanaVia.id_via_cuadra:viaCuadra.id";
        joinTables[3]="left:icl.catastro.tm_via as via:on:viaCuadra.id_via:via.id"; 
        joinTables[4]="left:(select * from icl.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        joinTables[5]="left:icl.catastro.tm_parametrias as condicionNumeracion:on:base.id_condicion_numeracion:condicionNumeracion.id";
        joinTables[6]="left:icl.catastro.tm_parametrias as tipoPuerta:on:base.id_tipo_puerta:tipoPuerta.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
    
    @Override
    public Long countLoteCatastral(Long idLoteCatastral) throws Exception {
        String joinTable="inner:loteCatastral";
        String[] mapFilterField={"=:loteCatastral.id:"+idLoteCatastral,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
    
    @Override
    public Long countManzanaVia(Long idManzanaVia) throws Exception {
        String joinTable="inner:manzanaVia";
        String[] mapFilterField={"=:manzanaVia.id:"+idManzanaVia,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public Long countTipoPuerta(Long idTipoPuerta) throws Exception {
        String joinTable="inner:tipoPuerta";
        String[] mapFilterField={"=:tipoPuerta.id:"+idTipoPuerta,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public Long countCondicionNumeracion(Long idCondicionNumeracion) throws Exception {
        String joinTable="inner:condicionNumeracion";
        String[] mapFilterField={"=:condicionNumeracion.id:"+idCondicionNumeracion,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
