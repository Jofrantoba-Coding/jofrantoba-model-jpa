/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.dao.impl;

import gob.pe.icl.icl.dao.impl.DaoParametrias;
import com.fasterxml.jackson.databind.node.ArrayNode;
import gob.pe.icl.icl.dao.inter.InterDaoParametrias;
import gob.pe.icl.icl.entity.Parametrias;
import java.util.Collection;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Transaction;
import org.junit.Test;

/**
 *
 * @author jtorresb
 */
@Log4j2
public class TestSelectDaoParametria extends TestBaseDao {
    
    @Test
    public void selectByID() throws Exception {
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        Transaction tx = dao.getSession().beginTransaction();
        Parametrias entidad=dao.findById(6);
        log.info(entidad);
        tx.commit();
    }
    
    @Test
    public void selectPadres() throws Exception {
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        Transaction tx = dao.getSession().beginTransaction();        
        List<Parametrias> lista=(List<Parametrias>)dao.parents(1,5);
        log.info(lista);
        log.info(lista.get(0).getId());
        log.info(lista.get(0).getDescripcion());
        tx.commit();
    }
    
    @Test
    public void selectHijos() throws Exception {
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        Transaction tx = dao.getSession().beginTransaction();
        List<Parametrias> lista=(List<Parametrias>)dao.childrens();
        log.info(lista.get(0).getId());
        tx.commit();
    }
    
    
    @Test
    public void selectHijosByPadre() throws Exception {
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        Transaction tx = dao.getSession().beginTransaction();
        Collection<Parametrias> lista=dao.childrensByParents(2l);
        log.info(lista);
        tx.commit();
    }
    
    @Test
    public void rowCountChildren() throws Exception {
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        Transaction tx = dao.getSession().beginTransaction();
        Long count=dao.countChildrens(3l);
        log.info(count);
        tx.commit();
    }
    
    @Test
    public void selecLimit() throws Exception {
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        Transaction tx = dao.getSession().beginTransaction();
        ArrayNode lista=dao.listar(2l, 0l);
        log.info(lista);
        tx.commit();
    }
    
}
