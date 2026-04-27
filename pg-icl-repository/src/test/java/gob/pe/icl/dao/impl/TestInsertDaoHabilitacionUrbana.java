/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.dao.impl;

import gob.pe.icl.icl.dao.impl.DaoHabilitacionUrbana;
import gob.pe.icl.icl.dao.impl.DaoLogsEntityOperation;
import gob.pe.icl.icl.dao.impl.DaoParametrias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gob.pe.icl.icl.dao.inter.InterDaoLogsEntityOperation;
import gob.pe.icl.icl.dao.inter.InterDaoHabilitacionUrbana;
import gob.pe.icl.icl.dao.inter.InterDaoParametrias;
import gob.pe.icl.icl.entity.LogsEntityOperation;
import gob.pe.icl.icl.entity.HabilitacionUrbana;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.hibernate.Transaction;
import org.junit.Test;

/**
 *
 * @author jtorresb
 */
public class TestInsertDaoHabilitacionUrbana extends TestBaseDao {
    
    @Test
    public void createEntity1() throws Exception {
        HabilitacionUrbana entity = contextEntity.getBean(HabilitacionUrbana.class);
        LogsEntityOperation logEntity=contextEntity.getBean(LogsEntityOperation.class);
        InterDaoHabilitacionUrbana dao = contextDao.getBean(DaoHabilitacionUrbana.class);
        InterDaoParametrias daoParametria = contextDao.getBean(DaoParametrias.class);
        InterDaoLogsEntityOperation daoLog = contextDao.getBean(DaoLogsEntityOperation.class);        
        entity.setIsPersistente(Boolean.TRUE);
        entity.setVersion((new Date()).getTime());        
        entity.setDescripcion("TITULAR CATASTRAL");   
        loadLogsEntityOperation(logEntity,entity);
        Transaction tx = dao.getSession().beginTransaction();
        entity.setTipoHabilitacionUrbana(daoParametria.findById(1l));
        dao.save(entity);
        daoLog.save(logEntity);
        tx.commit();
    }
    
    private LogsEntityOperation loadLogsEntityOperation(LogsEntityOperation logEntity,HabilitacionUrbana entity) throws JsonProcessingException{
        logEntity.setVersion((new Date()).getTime());
        logEntity.setIsPersistente(Boolean.TRUE);
        logEntity.setTipoOperacion("INSERT");
        ZoneId zone=ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        logEntity.setFecha(now);
        logEntity.setUsuario("chescot2302@gmail.com");
        logEntity.setTabla("catastro.tm_parametrias");
        logEntity.setClazz(HabilitacionUrbana.class.getCanonicalName());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(entity);
        //logEntity.setJsonEntity(json);
        return logEntity;
    }
    
}
