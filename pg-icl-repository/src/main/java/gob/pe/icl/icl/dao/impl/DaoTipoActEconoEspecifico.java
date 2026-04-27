/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoTipoActEconoEspecifico;
import gob.pe.icl.icl.entity.TipoActEconoEspecifico;
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
public class DaoTipoActEconoEspecifico extends AbstractJpaDao<TipoActEconoEspecifico> implements InterDaoTipoActEconoEspecifico {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoTipoActEconoEspecifico() {
        super();
        this.setClazz(TipoActEconoEspecifico.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<TipoActEconoEspecifico> listar(Long idTipoActEconoIntermedio)throws Exception {        
        String joinTable="inner:tipoActEconoIntermedio";
        String[] mapFilterField={"=:tipoActEconoIntermedio.id:"+idTipoActEconoIntermedio,"=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("tipoActEconoIntermedio.id as idTipoActEconoIntermedio,"));
        strFileds.append(share.append("tipoActEconoIntermedio.descripcion as descripcionTipoActEconoIntermedio"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                TipoActEconoEspecifico bean=new TipoActEconoEspecifico();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<TipoActEconoEspecifico> lista=(Collection<TipoActEconoEspecifico>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="icl.catastro.tm_tipo_act_econo_especifico as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("tipoActEconoIntermedio.id as idTipoActEconoIntermedio,"));
        strFields.append(share.append("tipoActEconoIntermedio.descripcion as descripcionTipoActEconoIntermedio,"));
        strFields.append(share.append("tipoActEconoGeneral.id as idTipoActEconoGeneral,"));
        strFields.append(share.append("tipoActEconoGeneral.descripcion as descripcionTipoActEconoGeneral"));
        String fields=strFields.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:icl.catastro.tm_tipo_act_econo_intermedio as tipoActEconoIntermedio:on:base.id_tipo_act_econo_intermedio:tipoActEconoIntermedio.id";        
        joinTables[1]="inner:icl.catastro.tm_tipo_act_econo_general as tipoActEconoGeneral:on:tipoActEconoIntermedio.id_tipo_act_econo_general:tipoActEconoGeneral.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet);
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="icl.catastro.tm_tipo_act_econo_especifico as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("tipoActEconoIntermedio.id as idTipoActEconoIntermedio,"));
        strFields.append(share.append("tipoActEconoIntermedio.descripcion as descripcionTipoActEconoIntermedio,"));
        strFields.append(share.append("tipoActEconoGeneral.id as idTipoActEconoGeneral,"));
        strFields.append(share.append("tipoActEconoGeneral.descripcion as descripcionTipoActEconoGeneral"));
        String fields=strFields.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:icl.catastro.tm_tipo_act_econo_intermedio as tipoActEconoIntermedio:on:base.id_tipo_act_econo_intermedio:tipoActEconoIntermedio.id";        
        joinTables[1]="inner:icl.catastro.tm_tipo_act_econo_general as tipoActEconoGeneral:on:tipoActEconoIntermedio.id_tipo_act_econo_general:tipoActEconoGeneral.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder);
    }
    
    @Override
    public Long count(Long idTipoActEconoIntermedio) throws Exception {
        String joinTable="inner:tipoActEconoIntermedio";
        String[] mapFilterField={"=:tipoActEconoIntermedio.id:"+idTipoActEconoIntermedio,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
