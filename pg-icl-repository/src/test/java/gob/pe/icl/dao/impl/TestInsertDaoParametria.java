/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.dao.impl;

import gob.pe.icl.icl.dao.impl.DaoLogsEntityOperation;
import gob.pe.icl.icl.dao.impl.DaoParametrias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gob.pe.icl.icl.dao.inter.InterDaoLogsEntityOperation;
import gob.pe.icl.icl.dao.inter.InterDaoParametrias;
import gob.pe.icl.icl.entity.Distrito;
import gob.pe.icl.icl.entity.LogsEntityOperation;
import gob.pe.icl.icl.entity.Parametrias;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.hibernate.Transaction;
import org.junit.Test;

/**
 *
 * @author jtorresb
 */
public class TestInsertDaoParametria extends TestBaseDao {
    
     private void marcarTiempo(Parametrias entity){
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        entity.setMarcaTiempo(now);
    }
          
    @Test
    public void createEntity1() throws Exception {
        Parametrias entity = contextEntity.getBean(Parametrias.class);
        LogsEntityOperation logEntity=contextEntity.getBean(LogsEntityOperation.class);
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        InterDaoLogsEntityOperation daoLog = contextDao.getBean(DaoLogsEntityOperation.class);        
        entity.setIsPersistente(Boolean.TRUE);
        entity.setVersion((new Date()).getTime());        
        entity.setDescripcion("TITULAR CATASTRAL"); 
        marcarTiempo(entity);        
        Transaction tx = dao.getSession().beginTransaction();
        dao.save(entity);
        loadLogsEntityOperation(logEntity,entity);
        daoLog.save(logEntity);
        tx.commit();
    }
    
    /*@Test
    public void createEntity2() throws Exception {
        Parametrias entity = contextEntity.getBean(Parametrias.class);
        LogsEntityOperation logEntity=contextEntity.getBean(LogsEntityOperation.class);
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        InterDaoLogsEntityOperation daoLog = contextDao.getBean(DaoLogsEntityOperation.class);        
        entity.setIsPersistente(Boolean.TRUE);
        entity.setVersion((new Date()).getTime());        
        entity.setDescripcion("TITULAR CATASTRAL");           
        loadLogsEntityOperation(logEntity,entity);
        Transaction tx = dao.getSession().beginTransaction();
        dao.save(entity);
        entity.setParent(dao.findById(3));
        daoLog.save(logEntity);
        tx.commit();
    }
    
    
    @Test
    public void createEntity3() throws Exception {
        Parametrias entity = contextEntity.getBean(Parametrias.class);
        LogsEntityOperation logEntity=contextEntity.getBean(LogsEntityOperation.class);
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        InterDaoLogsEntityOperation daoLog = contextDao.getBean(DaoLogsEntityOperation.class);        
        entity.setIsPersistente(Boolean.TRUE);
        entity.setVersion((new Date()).getTime());        
        entity.setDescripcion("TITULAR CATASTRAL");           
        loadLogsEntityOperation(logEntity,entity);
        Transaction tx = dao.getSession().beginTransaction();
        dao.save(entity);
        entity.setParent(dao.findById(1));
        daoLog.save(logEntity);
        tx.commit();
    }
    
    @Test
    public void createEntity4() throws Exception {
        Parametrias entity = contextEntity.getBean(Parametrias.class);
        LogsEntityOperation logEntity=contextEntity.getBean(LogsEntityOperation.class);
        InterDaoParametrias dao = contextDao.getBean(DaoParametrias.class);
        InterDaoLogsEntityOperation daoLog = contextDao.getBean(DaoLogsEntityOperation.class);        
        entity.setIsPersistente(Boolean.TRUE);
        entity.setVersion((new Date()).getTime());        
        entity.setDescripcion("TITULAR CATASTRAL");           
        loadLogsEntityOperation(logEntity,entity);
        Transaction tx = dao.getSession().beginTransaction();        
        dao.save(entity);
        entity.setParent(dao.findById(2));
        daoLog.save(logEntity);
        tx.commit();
    }*/
    
    private LogsEntityOperation loadLogsEntityOperation(LogsEntityOperation logEntity,Parametrias entity) throws JsonProcessingException{
        logEntity.setIdEntity(entity.getId().toString());
        logEntity.setVersion((new Date()).getTime());
        logEntity.setIsPersistente(Boolean.TRUE);
        logEntity.setTipoOperacion("INSERT");
        ZoneId zone=ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        logEntity.setFecha(now);
        logEntity.setUsuario("chescot2302@gmail.com");
        logEntity.setTabla("catastro.tm_habilitacion_urbana");
        logEntity.setClazz(Parametrias.class.getCanonicalName());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.convertValue(entity, JsonNode.class);        
        logEntity.setJsonEntity(json);
        return logEntity;
    }
}
