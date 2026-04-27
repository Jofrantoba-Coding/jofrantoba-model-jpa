/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoManzanaVia;
import gob.pe.icl.icl.dto.beans.FilterManzanaVia;
import gob.pe.icl.icl.entity.ManzanaVia;
import java.util.ArrayList;
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
public class DaoManzanaVia extends AbstractJpaDao<ManzanaVia> implements InterDaoManzanaVia {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoManzanaVia() {
        super();
        this.setClazz(ManzanaVia.class);
        //this.setSessionFactory(sessionFactory);
    }           
    
    @Override
    public ArrayNode listarFilter(FilterManzanaVia filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);      
    }

    @Override
    public ArrayNode listar(FilterManzanaVia filter,Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }
    
    @Override
    public ArrayNode listar(FilterManzanaVia filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);        
    }
    
    @Override
    public Long countManzana(Long idManzana) throws Exception {
        String joinTable="inner:manzana";
        String[] mapFilterField={"=:manzana.id:"+idManzana,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
    
    @Override
    public Long countViaCuadra(Long idViaCuadra) throws Exception {
        String joinTable="inner:viaCuadra";
        String[] mapFilterField={"=:via.viaCuadra:"+idViaCuadra,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
    
    private Map<String, Object> buildQueryList(FilterManzanaVia filters) {
        Map<String, Object> map = new HashMap();
        String table="icl.catastro.tg_manzana_via as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));  
        strFileds.append(share.append("tipoVia.descripcion as tipoVia,"));
        strFileds.append(share.append("via.descripcion as via,")); 
        strFileds.append(share.append("via.codigo_via as codigoVia,"));      
        strFileds.append(share.append("viaCuadra.cuadra as cuadra,"));            
        strFileds.append(share.append("manzana.id as idManzana,"));
        strFileds.append(share.append("manzana.descripcion as descripcionManzana,"));        
        strFileds.append(share.append("viaCuadra.id as idViaCuadra,"));
        strFileds.append(share.append("viaCuadra.cuadra as cuadra,"));
        strFileds.append(share.append("trim(concat(coalesce(tipoVia.descripcion,''),' ',coalesce(via.descripcion,''),' Cda. ',coalesce(cast(viaCuadra.cuadra as varchar),''))) as direccion"));
        String fields=strFileds.toString();
        String[] joinTables=new String[4];
        joinTables[0]="inner:icl.catastro.tg_manzana as manzana:on:base.id_manzana:manzana.id";        
        joinTables[1]="inner:icl.catastro.tg_via_cuadra as viaCuadra:on:base.id_via_cuadra:viaCuadra.id";      
        joinTables[2]="left:icl.catastro.tm_via as via:on:viaCuadra.id_via:via.id"; 
        joinTables[3]="left:(select * from icl.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        List<String> filter=new ArrayList();
        filter.add("=:base.is_persistente:true");
        if(filters.getIdManzana()!=null){
            filter.add("=:manzana.id:"+filters.getIdManzana());
        }
        if(filters.getIdViaCuadra()!=null){
            filter.add("=:viaCuadra.id:"+filters.getIdViaCuadra());
        }
        String[] mapFilterField=filter.toArray(new String[0]); 
        String[] mapOrder={"base.id:asc"};  
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }
}
