/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoDistrito;
import gob.pe.icl.icl.entity.Distrito;
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
public class DaoDistrito extends AbstractJpaDao<Distrito> implements InterDaoDistrito {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoDistrito() {
        super();
        this.setClazz(Distrito.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<Distrito> listar(Long idProvincia)throws Exception {        
        String joinTable="inner:provincia";
        String[] mapFilterField={"=:provincia.id:"+idProvincia,"=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.codigoDistrito as codigoDistrito,"));
        strFileds.append(share.append("base.codigoProvincia as codigoProvincia,"));
        strFileds.append(share.append("base.codigoDepartamento as codigoDepartamento,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Distrito bean=new Distrito();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Distrito> lista=(Collection<Distrito>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="icl.catastro.tm_distrito as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigo_distrito as codigoDistrito,"));
        strFields.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFields.append(share.append("base.codigo_departamento as codigoDepartamento,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("provincia.id as idProvincia,"));
        strFields.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields=strFields.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:icl.catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";        
        joinTables[1]="inner:icl.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet);
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="icl.catastro.tm_distrito as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigo_distrito as codigoDistrito,"));
        strFields.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFields.append(share.append("base.codigo_departamento as codigoDepartamento,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("provincia.id as idProvincia,"));
        strFields.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields=strFields.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:icl.catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";        
        joinTables[1]="inner:icl.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
    
    @Override
    public Long count(Long idProvincia) throws Exception {
        String joinTable="inner:provincia";
        String[] mapFilterField={"=:provincia.id:"+idProvincia,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
