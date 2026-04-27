/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoClienteSistema;
import gob.pe.icl.icl.entity.ClienteSistema;
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
public class DaoClienteSistema extends AbstractJpaDao<ClienteSistema> implements InterDaoClienteSistema {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoClienteSistema() {
        super();
        this.setClazz(ClienteSistema.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<ClienteSistema> listar(Long idSistema)throws Exception {
        String joinTable="inner:sistema";
        String[] mapFilterField={"=:sistema.id:"+idSistema,"=:base.isPersistente:true","=:base.isAppIcl:true"};
        String[] mapOrder={"base.descripcion:asc"};
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("sistema.id as idSistema,"));
        strFields.append(share.append("sistema.descripcion as descripcionSistema"));
        String fields=strFields.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                ClienteSistema bean=new ClienteSistema();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<ClienteSistema> lista=(Collection<ClienteSistema>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }
  
    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="icl.seguridad.tg_cliente_sistema as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));                
        strFields.append(share.append("sistema.id as idSistema,"));
        strFields.append(share.append("sistema.descripcion as descripcionSistema"));
        String fields=strFields.toString();
        StringBuilder join=new StringBuilder();
        String[] joinTables=new String[1];
        joinTables[0]="inner:icl.seguridad.tg_sistema as sistema:on:base.id_sistema:sistema.id";        
        String[] mapFilterField={"=:base.is_persistente:true","=:base.is_app_icl:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet);
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="icl.seguridad.tg_cliente_sistema as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));        
        strFields.append(share.append("sistema.id as idSistema,"));
        strFields.append(share.append("sistema.descripcion as descripcionSistema"));
        String fields=strFields.toString();
        String[] mapFilterField={"=:base.is_persistente:true","=:base.is_app_icl:true"};
        String[] mapOrder={"base.id:asc"};
        String[] joinTable=new String[1];
        joinTable[0]="inner:icl.seguridad.tg_sistema as sistema:on:base.id_sistema:sistema.id";        
        return this.allFieldsJoinPostgres(joinTable,table,fields,mapFilterField,mapOrder);
    }    
    
    @Override
    public Long count(Long idSistema) throws Exception {
        String joinTable="inner:sistema";
        String[] mapFilterField={"=:sistema.id:"+idSistema,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
