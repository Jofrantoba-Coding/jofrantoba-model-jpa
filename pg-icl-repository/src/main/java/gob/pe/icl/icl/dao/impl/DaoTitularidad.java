/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoTitularidad;
import gob.pe.icl.icl.entity.Titularidad;
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
public class DaoTitularidad extends AbstractJpaDao<Titularidad> implements InterDaoTitularidad {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoTitularidad(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(Titularidad.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<Titularidad> listarFilter(Long idUnidadCatastral,Long idCondicionTitular,Long idFormaAdquisicion)throws Exception {        
        String[] joinTables={"inner:unidadCatastral","left:condicionTitularidad","left:formaAdquisicion"};
        List<String> filter=new ArrayList();
        filter.add("=:base.isPersistente:true");
        if(idUnidadCatastral!=null){
            filter.add("=:unidadCatastral.id:"+idUnidadCatastral);
        }
        if(idCondicionTitular!=null){
            filter.add("=:condicionTitularidad.id:"+idCondicionTitular);
        }
        if(idFormaAdquisicion!=null){
            filter.add("=:formaAdquisicion.id:"+idFormaAdquisicion);
        }
        String[] mapFilterField=filter.toArray(new String[0]);        
        String[] mapOrder={"base.id:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));              
        strFileds.append(share.append("base.fechaAdquisicion as fechaAdquisicion,"));              
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));        
        strFileds.append(share.append("condicionTitularidad.id as idCondicionTitularidad,"));
        strFileds.append(share.append("condicionTitularidad.descripcion as descripcionCondicionTitular,"));
        strFileds.append(share.append("formaAdquisicion.id as idFormaAdquisicion,"));
        strFileds.append(share.append("formaAdquisicion.descripcion as descripcionFormaAdquisicion"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Titularidad bean=new Titularidad();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Titularidad> lista=(Collection<Titularidad>)this.customFieldsJoinFilterAnd(rt,fields,joinTables,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="icl.catastro.tgv_titularidad as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));      
        strFileds.append(share.append("condicionTitularidad.id as idCondicionTitularidad,"));
        strFileds.append(share.append("condicionTitularidad.descripcion as descripcionCondicionTitularidad,"));
        strFileds.append(share.append("formaAdquisicion.id as idFormaAdquisicion,"));
        strFileds.append(share.append("formaAdquisicion.descripcion as descripcionFormaAdquisicion"));
        String fields=strFileds.toString();
        String[] joinTables=new String[3];
        joinTables[0]="inner:icl.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";        
        joinTables[1]="left:icl.catastro.tm_parametrias as condicionTitularidad:on:base.id_condicion_titularidad:condicionTitularidad.id";
        joinTables[2]="left:icl.catastro.tm_parametrias as formaAdquisicion:on:base.id_forma_adquisicion:formaAdquisicion.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="icl.catastro.tgv_titularidad as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("base.fecha_adquisicion as fechaAdquisicion,")); 
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));      
        strFileds.append(share.append("condicionTitularidad.id as idCondicionTitularidad,"));
        strFileds.append(share.append("condicionTitularidad.descripcion as descripcionCondicionTitularidad,"));
        strFileds.append(share.append("formaAdquisicion.id as idFormaAdquisicion,"));
        strFileds.append(share.append("formaAdquisicion.descripcion as descripcionFormaAdquisicion"));
        String fields=strFileds.toString();
        String[] joinTables=new String[3];
        joinTables[0]="inner:icl.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";        
        joinTables[1]="left:icl.catastro.tm_parametrias as condicionTitularidad:on:base.id_condicion_titularidad:condicionTitularidad.id";
        joinTables[2]="left:icl.catastro.tm_parametrias as formaAdquisicion:on:base.id_forma_adquisicion:formaAdquisicion.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
    
    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable="inner:unidadCatastral";
        String[] mapFilterField={"=:unidadCatastral.id:"+idUnidadCatastral,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
    
    @Override
    public Long countCondicionTitular(Long idCondicionTitular) throws Exception {
        String joinTable="inner:condicionTitular";
        String[] mapFilterField={"=:condicionTitular.id:"+idCondicionTitular,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public Long countFormaAdquisicion(Long idFormaAdquisicion) throws Exception {
        String joinTable="inner:formaAdquisicion";
        String[] mapFilterField={"=:formaAdquisicion.id:"+idFormaAdquisicion,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
