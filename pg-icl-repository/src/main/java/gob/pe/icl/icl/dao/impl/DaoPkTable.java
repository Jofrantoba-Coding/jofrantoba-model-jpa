/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoPkTable;
import gob.pe.icl.icl.dto.beans.FilterPkTable;
import gob.pe.icl.icl.entity.PkTable;
import java.util.ArrayList;
import java.util.Collection;
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
public class DaoPkTable extends AbstractJpaDao<PkTable> implements InterDaoPkTable {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoPkTable() {
        super();
        this.setClazz(PkTable.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<PkTable> listar() throws Exception {        
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"titulo:asc"};
        return this.allFields(mapFilterField, mapOrder);
    }

    @Override
    public Collection<PkTable> listar(int pageNumber, int pageSize) throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"titulo:asc"};
        return this.allFields(mapFilterField, mapOrder,pageNumber,pageSize);
    }
    
    @Override
    public Long count(String tabla, String codigo) throws Exception {
        String[] mapFilterField={"equal:base.tabla:"+tabla,"equal:base.codigo:"+codigo,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(null,mapFilterField);                
    }

    @Override
    public Collection<PkTable> listar(FilterPkTable filter) throws Exception {
        List<String> filterList = new ArrayList();
        filterList.add("=:base.isPersistente:true");
        if (filter.getTabla()!= null) {
            filterList.add("equal:base.tabla:" + filter.getTabla());
        }
        if (filter.getIdTabla()!= null) {
            filterList.add("=:base.idTabla:" + filter.getIdTabla());
        }
        if (filter.getCodigo()!= null) {
            filterList.add("equal:base.codigo:" + filter.getCodigo());
        }
        String[] mapFilterField = filterList.toArray(new String[0]);
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.tabla as tabla,"));
        strFileds.append(share.append("base.idTabla as idTabla"));
        String fields = strFileds.toString();
        String[] mapOrder = {"base.id:asc"};
        Collection<PkTable> lista = (Collection<PkTable>)this.customFieldsFilterAnd(PkTable.class,fields, mapFilterField, mapOrder);
        return lista;
    }

    @Override
    public int deletePkbyLikeCodigo(String codigo)throws Exception {
        String[] mapFilterField={"like:base.codigo:"+"\'"+codigo+"%\'"};
        return this.deleteFilterAnd(mapFilterField);
    }
}
