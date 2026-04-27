/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoSector;
import gob.pe.icl.icl.entity.Sector;
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
public class DaoSector extends AbstractJpaDao<Sector> implements InterDaoSector {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoSector() {
        super();
        this.setClazz(Sector.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<Sector> listar(Long idDistrito)throws Exception {        
        String joinTable="inner:distrito";
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};
        String[] mapOrder={"base.codigoDepartamento:asc","base.codigoProvincia:asc","base.codigoDistrito:asc","base.codigoSector:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.codigoSector as codigoSector,"));
        strFileds.append(share.append("base.codigoDistrito as codigoDistrito,"));
        strFileds.append(share.append("base.codigoProvincia as codigoProvincia,"));
        strFileds.append(share.append("base.codigoDepartamento as codigoDepartamento,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Sector bean=new Sector();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Sector> lista=(Collection<Sector>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="icl.catastro.tg_sector as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.codigo_sector as codigoSector,"));
        strFileds.append(share.append("base.codigo_distrito as codigoDistrito,"));
        strFileds.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFileds.append(share.append("base.codigo_Departamento as codigoDepartamento,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields=strFileds.toString();
        String[] joinTables=new String[3];
        joinTables[0]="inner:icl.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";        
        joinTables[1]="inner:icl.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[2]="inner:icl.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.codigo_departamento:asc","base.codigo_provincia:asc","base.codigo_distrito:asc","base.codigo_sector:asc"};                
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet);
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="icl.catastro.tg_sector as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.codigo_sector as codigoSector,"));
        strFileds.append(share.append("base.codigo_distrito as codigoDistrito,"));
        strFileds.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFileds.append(share.append("base.codigo_Departamento as codigoDepartamento,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String[] joinTables=new String[3];
        joinTables[0]="inner:icl.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";        
        joinTables[1]="inner:icl.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[2]="inner:icl.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String fields=strFileds.toString();
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.codigo_departamento:asc","base.codigo_provincia:asc","base.codigo_distrito:asc","base.codigo_sector:asc"};                
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
    
    @Override
    public Long count(Long idDistrito) throws Exception {
        String joinTable="inner:distrito";
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
