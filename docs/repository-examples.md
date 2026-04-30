---
title: Repository Examples
nav_order: 8
---

# Repository Examples
{: .no_toc }

This page is generated from every Java example under `.examples`. Names from real public-sector sample projects are sanitized to neutral `jofrantoba` packages, schemas, and class names while preserving the DAO patterns, DSL calls, joins, subqueries, pagination, and aggregation structure.

<details open markdown="block">
  <summary>Contents</summary>
  {: .text-delta }
- TOC
{:toc}
</details>

---

## ms-demo-repository


### ms-demo-repository\src\main\java\com/jofrantoba/examples\demo\config\ConfigDao.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.demo.config;

import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesSqlServer;
import com.jofrantoba.examples.demo.dto.beans.DtoConnection;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @author jtorresb
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.jofrantoba.examples.demo.dao"})
public class ConfigDao {

    public static boolean isSessionFactoryInicializado = false;

    private static SessionFactory sesionFactory = null;

    @Primary
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() {
        try {
            if (!isSessionFactoryInicializado && sesionFactory == null) {
                List<String> packages = new ArrayList();
                packages.add("com.jofrantoba.examples.demo.entity");
                PSF.getInstance().buildPSF("sqlserver", getCnx(), packages);
                sesionFactory = PSF.getInstance().getPSF("sqlserver");
                isSessionFactoryInicializado = true;
                log.info("sessionFactory inicializado");
            } else {
                log.info("sessionFactory ya fue inicializado");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Warning: sessionFactory no puede inicializarse:{}", ex.getMessage());
        }
        return sesionFactory;
    }

    private ConnectionPropertiesSqlServer getCnx() {
        String host = DtoConnection.host;//localhost
        Integer port = DtoConnection.port;//1433
        String nameDatabase = DtoConnection.nameDatabase;//demosiat
        String userDatabase = DtoConnection.userDatabase;//usrdemosiat
        String passwordDatabase = DtoConnection.passwordDatabase;//AppPassword123!
        /*String host = "";//localhost
        Integer port = 1433;//1433
        String nameDatabase = "DEMOSIAT";//demosiat
        String userDatabase = "sa";//usrdemosiat
        String passwordDatabase = "";//AppPassword123!*/
        ConnectionPropertiesSqlServer cnx = new ConnectionPropertiesSqlServer(host, port.intValue(), nameDatabase, userDatabase, passwordDatabase);
        return cnx;
    }
}
```

### ms-demo-repository\src\main\java\com/jofrantoba/examples\demo\dao\impl\DaoParametria.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.demo.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.examples.demo.dao.inter.InterDaoParametria;
import com.jofrantoba.examples.demo.dto.beans.DtoParametria;
import com.jofrantoba.examples.demo.entity.Parametria;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author apoyo1953
 */
@Slf4j
@Repository
public class DaoParametria extends AbstractJpaDaoV2<Parametria> implements InterDaoParametria {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        if (this.getSessionFactory() == null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }

    public DaoParametria() {
        super();
        this.setClazz(Parametria.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public DtoParametria insertProcedureJson(DtoParametria bean) throws Exception {
        String jsonString = (new ObjectMapper()).writeValueAsString(bean);
        log.info("insertProcedureJson: " + jsonString);
        Long id = this.iudProcedureJson("InsertParametriaJson", jsonString);
        bean.setId(id);
        return bean;
    }

    @Override
    public DtoParametria updateProcedureJson(DtoParametria bean) throws Exception {
        String jsonString = (new ObjectMapper()).writeValueAsString(bean);
        log.info("updateProcedureJson: " + jsonString);
        this.iudProcedureJson("updateParametriaJson", jsonString);
        return bean;
    }
    
    @Override
    public void deleteProcedureJson(DtoParametria bean) throws Exception {
        String jsonString = (new ObjectMapper()).writeValueAsString(bean);
        log.info("deleteProcedureJson: " + jsonString);
        this.iudProcedureJson("deleteParametriaJson", jsonString);
    }

    @Override
    public List<Parametria> listProcedure() throws Exception {
        return this.listProcedureMsql("EXEC ListParametria", new HashMap());
    }

    @Override
    public List<Parametria> listProcedurePaginacion(Long limit, Long offSet) throws Exception {
        Map<String, Object> mapParameter = new HashMap();
        mapParameter.put("limit", limit);
        mapParameter.put("offset", offSet);
        return this.listProcedureMsql("EXEC ListParametriaLimit :limit,:offset", mapParameter);
    }

}
```

### ms-demo-repository\src\main\java\com/jofrantoba/examples\demo\dao\inter\InterDaoParametria.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.demo.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.demo.dto.beans.DtoParametria;
import com.jofrantoba.examples.demo.entity.Parametria;
import java.util.List;

/**
 *
 * @author apoyo1953
 */
public interface InterDaoParametria extends InterCrud<Parametria> {

    DtoParametria insertProcedureJson(DtoParametria bean) throws Exception;

    DtoParametria updateProcedureJson(DtoParametria bean) throws Exception;
    
    void deleteProcedureJson(DtoParametria bean) throws Exception;

    List<Parametria> listProcedure() throws Exception;

    List<Parametria> listProcedurePaginacion(Long limit, Long offSet) throws Exception;
}
```

### ms-demo-repository\src\test\java\com/jofrantoba/examples\dao\impl\TestBaseDao.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.dao.impl;

import com.jofrantoba.examples.demo.config.ConfigDao;
import com.jofrantoba.examples.demo.config.ConfigEntity;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Sergio
 */
public class TestBaseDao {
    protected AnnotationConfigApplicationContext contextEntity = new AnnotationConfigApplicationContext(ConfigEntity.class);
    protected AnnotationConfigApplicationContext contextDao = new AnnotationConfigApplicationContext(ConfigDao.class);
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
```

### ms-demo-repository\src\test\java\com/jofrantoba/examples\dao\impl\TestInsertDaoParametria.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.dao.impl;

import com.jofrantoba.examples.demo.dao.impl.DaoParametria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jofrantoba.model.jpa.daoentity.ParameterProcedure;
import com.jofrantoba.examples.demo.dao.inter.InterDaoParametria;
import com.jofrantoba.examples.demo.dto.beans.DtoParametria;
import com.jofrantoba.examples.demo.entity.Parametria;
import jakarta.persistence.ParameterMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Transaction;
import org.junit.Test;

/**
 *
 * @author Sergio
 */
public class TestInsertDaoParametria extends TestBaseDao {

    /*private void marcarTiempo(Parametria entity) {
        ZoneId zone = ZoneId.of("America/Lima");
        LocalDateTime now = LocalDateTime.now(zone);
        entity.setMarcaTiempo(now);
    }

    @Test
    public void createEntity1() throws Exception {
        Parametria entity = contextEntity.getBean(Parametria.class);
        InterDaoParametria dao = contextDao.getBean(DaoParametria.class);
        entity.setIsPersistente(Boolean.TRUE);
        entity.setVersion((new Date()).getTime());        
        entity.setDescripcion("TITULAR CATASTRAL"); 
        marcarTiempo(entity);        
        Transaction tx = dao.getSession().beginTransaction();
        //dao.save(entity);
        dao.iudProcedure(entity);
        tx.commit();
    }*/
 /*@Test
    public void execProcedureJson() throws Exception {
        InterDaoParametria dao = contextDao.getBean(DaoParametria.class);
        Transaction tx = dao.getSession().beginTransaction();
        //dao.save(entity);
        dao.iudProcedureJson("InsertParametriaJson", "{\"biVersion\": 1726903649875,\"bIsPersistente\": 1,\"vDescripcion\": \"prueba json Hibernate\",\"vAbreviatura\": \"abrJson\",\"dt2MarcaTiempo\": \"2024-09-23 01:10:57.2625285\"}");
        tx.commit();
    }*/
 /*@Test
    public void execProcedure() throws Exception {
        InterDaoParametria dao = contextDao.getBean(DaoParametria.class);
        Transaction tx = dao.getSession().beginTransaction();
        List<ParameterProcedure> lstParametria = new ArrayList();
        lstParametria.add(new ParameterProcedure("id", Long.class, ParameterMode.IN, 5));
        lstParametria.add(new ParameterProcedure("descripcion", String.class, ParameterMode.IN, "test update spDin"));
        //lstParametria.add(new ParameterProcedure("id_out", Long.class, ParameterMode.OUT));
        dao.iudProcedure("UpdateParametria", lstParametria);
        tx.commit();
    }*/
    @Test
    public void execProcedureList() throws Exception {
        InterDaoParametria dao = contextDao.getBean(DaoParametria.class);
        Transaction tx = dao.getSession().beginTransaction();
        Map<String, Object> mapParameter = new HashMap();
        mapParameter.put("id", 1L);
        List<Parametria> result = dao.listProcedureMsql("EXEC ListParametriaFilter :id", mapParameter);
        for (int i = 0; i < result.size(); i++) {
            Parametria stock = result.get(i);
            System.out.println(stock.getId());
        }
        tx.commit();
    }
}
```

## pg-jofrantobafiles-repository


### pg-jofrantobafiles-repository\src\main\java\com/jofrantoba/examples\jofrantobafiles\config\ConfigDaojofrantobaFiles.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantobafiles.config;

import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import com.jofrantoba.examples.jofrantobafiles.dto.beans.DtoConnection;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @author jtorresb
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.jofrantoba.examples.jofrantobafiles.dao"})
public class ConfigDaojofrantobafiles {

    public static boolean isSessionFactoryInicializado = false;

    private static SessionFactory sesionFactory = null;

    @Autowired
    @Primary
    @Bean(name = "sessionFactoryjofrantobafiles")
    public SessionFactory getSessionFactory() {
        try {
            if (!isSessionFactoryInicializado && sesionFactory==null) {
                List<String> packages = new ArrayList();
                packages.add("com.jofrantoba.examples.jofrantobafiles.entity");
                PSF.getInstance().buildPSF("postgre", getCnx(), packages);
                sesionFactory = PSF.getInstance().getPSF("postgre");
                log.info("sessionFactoryjofrantobafiles inicializado");
            } else {
                log.info("sessionFactoryjofrantobafiles ya fue inicializado");
            }
        } catch (Exception ex) {
            log.error("Warning: sessionFactoryjofrantobafiles no puede inicializarse");
        }
        return sesionFactory;
    }

    private ConnectionPropertiesPostgre getCnx() {
        String host = DtoConnection.host;//localhost
        Integer port = DtoConnection.port;//5432
        String nameDatabase = DtoConnection.nameDatabase;//Jofrantoba
        String userDatabase = DtoConnection.userDatabase;//usercatastro
        String passwordDatabase = DtoConnection.passwordDatabase;//Jofrantoba2023
        ConnectionPropertiesPostgre cnx = new ConnectionPropertiesPostgre(host, port.intValue(), nameDatabase, userDatabase, passwordDatabase);

        return cnx;
    }
}
```

### pg-jofrantobafiles-repository\src\main\java\com/jofrantoba/examples\jofrantobafiles\dao\impl\DaoChunkFiles.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantobafiles.dao.impl;

import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.examples.jofrantobafiles.dao.inter.InterDaoChunkFiles;
import com.jofrantoba.examples.jofrantobafiles.entity.ChunkFiles;
import com.jofrantoba.examples.jofrantobafiles.entity.MetadataFiles;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */
@Slf4j
@Repository
public class DaoChunkFiles extends AbstractJpaDaoV2<ChunkFiles> implements InterDaoChunkFiles {
    
    @Autowired(required = false)
    @Qualifier("sessionFactoryjofrantobafiles")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }

    public DaoChunkFiles() {
        super();
        this.setClazz(ChunkFiles.class);
        //this.setSessionFactory(sessionFactory);
    }       

}
```

### pg-jofrantobafiles-repository\src\main\java\com/jofrantoba/examples\jofrantobafiles\dao\impl\DaoMetadataFiles.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantobafiles.dao.impl;

import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.examples.jofrantobafiles.dao.inter.InterDaoMetadataFiles;
import com.jofrantoba.examples.jofrantobafiles.entity.MetadataFiles;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */
@Slf4j
@Repository
public class DaoMetadataFiles extends AbstractJpaDaoV2<MetadataFiles> implements InterDaoMetadataFiles {
    
    @Autowired(required = false)
    @Qualifier("sessionFactoryjofrantobafiles")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }

    public DaoMetadataFiles() {
        super();
        this.setClazz(MetadataFiles.class);
        //this.setSessionFactory(sessionFactory);
    }       

}
```

### pg-jofrantobafiles-repository\src\main\java\com/jofrantoba/examples\jofrantobafiles\dao\inter\InterDaoChunkFiles.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantobafiles.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantobafiles.entity.ChunkFiles;

/**
 *
 * @author jtorresb
 */
public interface InterDaoChunkFiles extends InterCrud<ChunkFiles>{
    
}
```

### pg-jofrantobafiles-repository\src\main\java\com/jofrantoba/examples\jofrantobafiles\dao\inter\InterDaoMetadataFiles.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantobafiles.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantobafiles.entity.MetadataFiles;

/**
 *
 * @author jtorresb
 */
public interface InterDaoMetadataFiles extends InterCrud<MetadataFiles>{
    
}
```

## pg-jofrantoba-repository


### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\config\ConfigDao.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.config;

import com.jofrantoba.model.jpa.psf.PSF;
import com.jofrantoba.model.jpa.psf.connection.ConnectionPropertiesPostgre;
import com.jofrantoba.examples.jofrantoba.dto.beans.DtoConnection;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 * @author jtorresb
 */

@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.jofrantoba.examples.jofrantoba.dao"})
public class ConfigDao {

    public static boolean isSessionFactoryInicializado = false;

    private static SessionFactory sesionFactory=null;    
    
    @Primary
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() {
        try {
            if (!isSessionFactoryInicializado && sesionFactory==null) {
                List<String> packages = new ArrayList();
                packages.add("com.jofrantoba.examples.jofrantoba.entity");
                PSF.getInstance().buildPSF("postgre", getCnx(), packages);                
                sesionFactory = PSF.getInstance().getPSF("postgre");
                isSessionFactoryInicializado = true;
                log.info("sessionFactory inicializado");
            }else{
                log.info("sessionFactory ya fue inicializado");
            }
           
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Warning: sessionFactory no puede inicializarse:{}",ex.getMessage());
        }
        return sesionFactory;
    }

    private ConnectionPropertiesPostgre getCnx() {
        String host = DtoConnection.host;//localhost
        Integer port = DtoConnection.port;//5432
        String nameDatabase = DtoConnection.nameDatabase;//Jofrantoba
        String userDatabase = DtoConnection.userDatabase;//postgres
        String passwordDatabase = DtoConnection.passwordDatabase;//jofrantoba
        ConnectionPropertiesPostgre cnx = new ConnectionPropertiesPostgre(host, port.intValue(), nameDatabase, userDatabase, passwordDatabase);
        return cnx;
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoActividadEconomica.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoActividadEconomica;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterActividadEconomica;
import com.jofrantoba.examples.jofrantoba.entity.ActividadEconomica;
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
public class DaoActividadEconomica extends AbstractJpaDaoV2<ActividadEconomica> implements InterDaoActividadEconomica {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoActividadEconomica() {
        super();
        this.setClazz(ActividadEconomica.class);
        //this.setSessionFactory(sessionFactory);
    }

     @Override
    public ArrayNode listarFilter(FilterActividadEconomica filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterActividadEconomica filter,Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder,limit,offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterActividadEconomica filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private Map<String, Object> buildQueryList(FilterActividadEconomica filter) {
        Map<String, Object> map = new HashMap();
          String table = "jofrantoba.catastro.tgv_actividad_economica as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("tipoDocIdent.id as idTipoDocIdent,"));
        strFileds.append(share.append("tipoDocIdent.descripcion as descripcionTipoDocIdent,"));
        strFileds.append(share.append("tipoConductor.id as idTipoConductor,"));
        strFileds.append(share.append("tipoConductor.descripcion as descripcionTipoConductor,"));
        strFileds.append(share.append("condicionConductor.id as idCondicionConductor,"));
        strFileds.append(share.append("condicionConductor.descripcion as descripcionCondicionConductor,"));
        strFileds.append(share.append("base.nombre_comercial as nombreComercial,"));
        strFileds.append(share.append("base.numero_ruc as numeroRuc,"));
        strFileds.append(share.append("base.numero_documento as numeroDocumento,"));
        strFileds.append(share.append("base.nombre as nombre,"));
        strFileds.append(share.append("base.numero_expediente as numero,"));
        strFileds.append(share.append("base.numero_licencia as numeroLicencia,"));
        strFileds.append(share.append("base.fecha_expedicion as fechaExpedicion,"));
        strFileds.append(share.append("base.fecha_vencimiento as fechaVencimiento,"));
        strFileds.append(share.append("base.inicio_actividad as inicioActividad,"));
        strFileds.append(share.append("base.predio_catastral_aa as predioCatastralAa,"));
        strFileds.append(share.append("base.predio_catastral_av as predioCatastralAv,"));
        strFileds.append(share.append("base.via_publica_aa as viaPublicaAa,"));
        strFileds.append(share.append("base.via_publica_av as viaPublicaAv,"));
        strFileds.append(share.append("base.bien_comun_aa as bienComunAa,"));
        strFileds.append(share.append("base.bien_comun_av as bienComunAv"));
        String fields = strFileds.toString();
        String[] joinTables = new String[4];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "left:jofrantoba.catastro.tm_parametrias as tipoDocIdent:on:base.id_tipo_doc_ident:tipoDocIdent.id";
        joinTables[2] = "inner:jofrantoba.catastro.tm_parametrias as tipoConductor:on:base.id_tipo_conductor:tipoConductor.id";
        joinTables[3] = "left:jofrantoba.catastro.tm_parametrias as condicionConductor:on:base.id_condicion_conductor:condicionConductor.id";
        
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");

        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdTipoDocIdent() != null) {
            filters.add("=:tipoDocIdent.id:" + filter.getIdTipoDocIdent());
        }
        if (filter.getIdTipoConductor() != null) {
            filters.add("=:tipoConductor.id:" + filter.getIdTipoConductor());
        }   
        if (filter.getIdCondicionConductor() != null) {
            filters.add("=:condicionConductor.id:" + filter.getIdCondicionConductor());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long couhtTipoDocIdent(Long idTipoDocIdent) throws Exception {
        String joinTable = "inner:tipoDocIdent";
        String[] mapFilterField = {"=:tipoDocIdent.id:" + idTipoDocIdent, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoConductor(Long idTipoConductor) throws Exception {
        String joinTable = "inner:tipoConductor";
        String[] mapFilterField = {"=:tipoConductor.id:" + idTipoConductor, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countCondicionConductor(Long idCondicionConductor) throws Exception {
        String joinTable = "inner:condicionConductor";
        String[] mapFilterField = {"=:condicionConductor.id:" + idCondicionConductor, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoAutorizacionAnuncio.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoAutorizacionAnuncio;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterAutorizacionAnuncio;
import com.jofrantoba.examples.jofrantoba.entity.AutorizacionAnuncio;
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
public class DaoAutorizacionAnuncio extends AbstractJpaDaoV2<AutorizacionAnuncio> implements InterDaoAutorizacionAnuncio {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoAutorizacionAnuncio() {
        super();
        this.setClazz(AutorizacionAnuncio.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterAutorizacionAnuncio filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterAutorizacionAnuncio filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterAutorizacionAnuncio filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private Map<String, Object> buildQueryList(FilterAutorizacionAnuncio filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_autorizacion_anuncio as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("actividadEconomica.id as idActividadEconomica,"));
        strFileds.append(share.append("actividadEconomica.nombre_comercial as nombreComercialActividadEconomica,"));
        strFileds.append(share.append("anuncio.id as idAnuncio,"));
        strFileds.append(share.append("anuncio.descripcion as descripcionAnuncio,"));
        strFileds.append(share.append("base.nro_lados as nroLados,"));
        strFileds.append(share.append("base.area_autorizada_anuncio as areaAutorizadaAnuncio,"));
        strFileds.append(share.append("base.area_verificada_anuncio as areaVerificadaAnuncio,"));
        strFileds.append(share.append("base.nro_expediente as nroExpediente,"));
        strFileds.append(share.append("base.nro_licencia as nroLicencia,"));
        strFileds.append(share.append("base.fecha_expedicion as fechExpedicion,"));
        strFileds.append(share.append("base.fecha_vencimiento as fechaVencimiento"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_actividad_economica as actividadEconomica:on:base.id_actividad_economica:actividadEconomica.id";
        joinTables[1] = "left:jofrantoba.catastro.tm_parametrias as anuncio:on:base.id_anuncio:anuncio.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");

        if (filter.getIdActividadEconomica() != null) {
            filters.add("=:actividadEconomica.id:" + filter.getIdActividadEconomica());
        }
        if (filter.getIdAnuncio() != null) {
            filters.add("=:anuncio.id:" + filter.getIdAnuncio());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countActividadEconomica(Long idActividadEconomica) throws Exception {
        String joinTable = "inner:actividadEconomica";
        String[] mapFilterField = {"=:actividadEconomica.id:" + idActividadEconomica, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countAnuncio(Long idAnuncio) throws Exception {
        String joinTable = "inner:anuncio";
        String[] mapFilterField = {"=:anuncio.id:" + idAnuncio, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoAutorizacionMunicipal.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoAutorizacionMunicipal;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterAutorizacionMunicipal;
import com.jofrantoba.examples.jofrantoba.entity.AutorizacionMunicipal;
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
public class DaoAutorizacionMunicipal extends AbstractJpaDaoV2<AutorizacionMunicipal> implements InterDaoAutorizacionMunicipal {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoAutorizacionMunicipal() {
        super();
        this.setClazz(AutorizacionMunicipal.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterAutorizacionMunicipal filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterAutorizacionMunicipal filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterAutorizacionMunicipal filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private Map<String, Object> buildQueryList(FilterAutorizacionMunicipal filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_autorizacion_municipal as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("actividadEconomica.id as idActividadEconomica,"));
        strFileds.append(share.append("actividadEconomica.nombre_comercial as nombreComercialActividadEconomica,"));
        strFileds.append(share.append("tipoActEconoEspecifico.id as idTipoActEconoEspecifico,"));
        strFileds.append(share.append("tipoActEconoEspecifico.descripcion as descripcionTipoActEconoEspecifico"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_actividad_economica as actividadEconomica:on:base.id_actividad_economica:actividadEconomica.id";
        joinTables[1] = "inner:jofrantoba.catastro.tm_tipo_act_econo_especifico as tipoActEconoEspecifico:on:base.id_tipo_act_econo_especifico:tipoActEconoEspecifico.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");

        if (filter.getIdActividadEconomica() != null) {
            filters.add("=:actividadEconomica.id:" + filter.getIdActividadEconomica());
        }
        if (filter.getIdTipoActEconoEspecifico() != null) {
            filters.add("=:tipoActEconoEspecifico.id:" + filter.getIdTipoActEconoEspecifico());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countActividadEconomica(Long idActividadEconomica) throws Exception {
        String joinTable = "inner:actividadEconomica";
        String[] mapFilterField = {"=:actividadEconomica.id:" + idActividadEconomica, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoActEconoEspecifico(Long idTipoActEconoEspecifico) throws Exception {
        String joinTable = "inner:tipoActEconoEspecifico";
        String[] mapFilterField = {"=:tipoActEconoEspecifico.id:" + idTipoActEconoEspecifico, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoCategoria.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoCategoria;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterCategoria;
import com.jofrantoba.examples.jofrantoba.entity.Categoria;
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
public class DaoCategoria extends AbstractJpaDaoV2<Categoria> implements InterDaoCategoria {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoCategoria() {
        super();
        this.setClazz(Categoria.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listar(FilterCategoria filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }
  @Override
    public ArrayNode listar(FilterCategoria filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }
    
    private Map<String, Object> buildQueryList(FilterCategoria filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tm_categoria as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripciondistrito,"));
        strFileds.append(share.append("distrito.codigo as codigodistrito"));
        String fields = strFileds.toString();
        String[] joinTables = new String[1];
        joinTables[0] = "inner:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdCategoria()!= null) {
            filters.add("=:base.id:" + filter.getIdCategoria());
        }
        if (filter.getIdDistrito() != null) {
            filters.add("=:distrito.id:" + filter.getIdDistrito());
        }
        if (filter.getDescripcion() != null) {
            filters.add("equal:base.descripcion:" + filter.getDescripcion());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }
    
    
    @Override
    public Long count(Long idDistrito) throws Exception {
        String joinTable="inner:distrito";
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoClasificacion.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoClasificacion;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterClasificacion;
import com.jofrantoba.examples.jofrantoba.entity.Clasificacion;
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
public class DaoClasificacion extends AbstractJpaDaoV2<Clasificacion> implements InterDaoClasificacion {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoClasificacion() {
        super();
        this.setClazz(Clasificacion.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listar(FilterClasificacion filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }
  
    @Override
    public ArrayNode listar(FilterClasificacion filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }
    
    private Map<String, Object> buildQueryList(FilterClasificacion filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tm_clasificacion as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("categoria.id as idCategoria,"));
        strFileds.append(share.append("categoria.descripcion as descripcionCategoria"));
        String fields = strFileds.toString();
        String[] joinTables = new String[1];
        joinTables[0] = "inner:jofrantoba.catastro.tm_categoria as categoria:on:base.id_categoria:categoria.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdClasificacion()!= null) {
            filters.add("=:base.id:" + filter.getIdClasificacion());
        }
        if (filter.getIdCategoria() != null) {
            filters.add("=:categoria.id:" + filter.getIdCategoria());
        }
        if (filter.getDescripcion() != null) {
            filters.add("equal:base.descripcion:" + filter.getDescripcion());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }
    
    
    @Override
    public Long count(Long idCategoria) throws Exception {
        String joinTable="inner:categoria";
        String[] mapFilterField={"=:categoria.id:"+idCategoria,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
   }


}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoClienteSistema.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoClienteSistema;
import com.jofrantoba.examples.jofrantoba.entity.ClienteSistema;
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
public class DaoClienteSistema extends AbstractJpaDaoV2<ClienteSistema> implements InterDaoClienteSistema {

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
        String[] mapFilterField={"=:sistema.id:"+idSistema,"=:base.isPersistente:true","=:base.isAppJofrantoba:true"};
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
        String table="jofrantoba.seguridad.tg_cliente_sistema as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));                
        strFields.append(share.append("sistema.id as idSistema,"));
        strFields.append(share.append("sistema.descripcion as descripcionSistema"));
        String fields=strFields.toString();
        StringBuilder join=new StringBuilder();
        String[] joinTables=new String[1];
        joinTables[0]="inner:jofrantoba.seguridad.tg_sistema as sistema:on:base.id_sistema:sistema.id";        
        String[] mapFilterField={"=:base.is_persistente:true","=:base.is_app_jofrantoba:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.seguridad.tg_cliente_sistema as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));        
        strFields.append(share.append("sistema.id as idSistema,"));
        strFields.append(share.append("sistema.descripcion as descripcionSistema"));
        String fields=strFields.toString();
        String[] mapFilterField={"=:base.is_persistente:true","=:base.is_app_jofrantoba:true"};
        String[] mapOrder={"base.id:asc"};
        String[] joinTable=new String[1];
        joinTable[0]="inner:jofrantoba.seguridad.tg_sistema as sistema:on:base.id_sistema:sistema.id";        
        return this.allFieldsJoinPostgres(joinTable,table,fields,mapFilterField,mapOrder, "and");
    }    
    
    @Override
    public Long count(Long idSistema) throws Exception {
        String joinTable="inner:sistema";
        String[] mapFilterField={"=:sistema.id:"+idSistema,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoConductorDomicilio.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoConductorDomicilio;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterConductorDomicilio;
import com.jofrantoba.examples.jofrantoba.entity.ConductorDomicilio;
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
public class DaoConductorDomicilio extends AbstractJpaDaoV2<ConductorDomicilio> implements InterDaoConductorDomicilio {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoConductorDomicilio() {
        super();
        this.setClazz(ConductorDomicilio.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<ConductorDomicilio> listarFilter(FilterConductorDomicilio filter) throws Exception {
        String[] joinTables = new String[5];
        joinTables[0] = "inner:actividadEconomica";
        joinTables[1] = "left:departamento";
        joinTables[2] = "left:provincia";
        joinTables[3] = "left:distrito";
        joinTables[4] = "left:tipoVia";
        List<String> filters = new ArrayList();
        filters.add("=:base.isPersistente:true");
        if (filter.getIdActividadEconomica()!= null) {
            filters.add("=:actividadEconomica.id:" + filter.getIdActividadEconomica());
        }
        if (filter.getIdDepartamento()!= null) {
            filters.add("=:departamento.id:" + filter.getIdDepartamento());
        }
        if (filter.getIdProvincia()!= null) {
            filters.add("=:provincia.id:" + filter.getIdProvincia());
        }
        if (filter.getIdTipoVia()!= null) {
            filters.add("=:tipoVia.id:" + filter.getIdTipoVia());
        }
        
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as departamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as provincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as distrito,"));
        strFileds.append(share.append("tipoVia.id as idTipoVia,"));
        strFileds.append(share.append("tipoVia.descripcion as tipoVia"));
        String fields = strFileds.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                ConductorDomicilio bean = new ConductorDomicilio();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<ConductorDomicilio> lista = (Collection<ConductorDomicilio>) this.customFieldsJoinFilterAnd(rt, fields, joinTables, mapFilterField, mapOrder);
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table = "jofrantoba.catastro.tgv_conductor_domicilio as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.codigo_via as codigoVia,"));
        strFileds.append(share.append("base.nombre_via as nombreVia,"));
        strFileds.append(share.append("base.numero_municipal as numeroMunicipal,"));
        strFileds.append(share.append("base.numero_edificcion as nombreEdificcion,"));
        strFileds.append(share.append("base.numero_interior as numerorInterior,"));
        strFileds.append(share.append("base.numero_edificcion as nombreEdificcion,"));
        strFileds.append(share.append("base.numero_interior as numeroInterior,"));
        strFileds.append(share.append("base.codigo_hu as codigoHu,"));
        strFileds.append(share.append("base.nombre_hu as nombreHu,"));
        strFileds.append(share.append("base.zona_sector_etapa as zonaSectorEtapa,"));
        strFileds.append(share.append("base.manzana as manzana,"));
        strFileds.append(share.append("base.lote as lote,"));
        strFileds.append(share.append("base.sub_lote as subLote,"));
        strFileds.append(share.append("base.telefono as telefono,"));
        strFileds.append(share.append("base.anexo as anexo,"));
        strFileds.append(share.append("base.fax as fax,"));
        strFileds.append(share.append("base.correo_electronico as correoElectronico,"));
        strFileds.append(share.append("actividadEconomica.id as idActividadEconomica,"));
        strFileds.append(share.append("actividadEconomica.nombre as nombreActividadEconomica,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as departamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as provincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as distrito,"));
        strFileds.append(share.append("tipoVia.id as idTipoVia,"));
        strFileds.append(share.append("tipoVia.descripcion as tipoVia"));
        String fields = strFileds.toString();
        String[] joinTables = new String[5];

        joinTables[0] = "inner:jofrantoba.catastro.tgv_actividad_economica as actividadEconomica:on:base.id_actividad_economica:actividadEconomica.id";
        joinTables[1] = "left:jofrantoba.catastro.tm_departamento as departamento:on:base.id_departamento:departamento.id";
        joinTables[2] = "left:jofrantoba.catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";
        joinTables[3] = "left:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";
        joinTables[4] = "left:(select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:base.id_tipo_via:tipoVia.id";

        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "jofrantoba.catastro.tgv_conductor_domicilio as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.codigo_via as codigoVia,"));
        strFileds.append(share.append("base.nombre_via as nombreVia,"));
        strFileds.append(share.append("base.numero_municipal as numeroMunicipal,"));
        strFileds.append(share.append("base.numero_edificcion as nombreEdificcion,"));
        strFileds.append(share.append("base.numero_interior as numerorInterior,"));
        strFileds.append(share.append("base.numero_edificcion as nombreEdificcion,"));
        strFileds.append(share.append("base.numero_interior as numeroInterior,"));
        strFileds.append(share.append("base.codigo_hu as codigoHu,"));
        strFileds.append(share.append("base.nombre_hu as nombreHu,"));
        strFileds.append(share.append("base.zona_sector_etapa as zonaSectorEtapa,"));
        strFileds.append(share.append("base.manzana as manzana,"));
        strFileds.append(share.append("base.lote as lote,"));
        strFileds.append(share.append("base.sub_lote as subLote,"));
        strFileds.append(share.append("base.telefono as telefono,"));
        strFileds.append(share.append("base.anexo as anexo,"));
        strFileds.append(share.append("base.fax as fax,"));
        strFileds.append(share.append("base.correo_electronico as correoElectronico,"));
        strFileds.append(share.append("actividadEconomica.id as idActividadEconomica,"));
        strFileds.append(share.append("actividadEconomica.nombre as nombreActividadEconomica,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as departamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as provincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as distrito,"));
        strFileds.append(share.append("tipoVia.id as idTipoVia,"));
        strFileds.append(share.append("tipoVia.descripcion as tipoVia"));
        String fields = strFileds.toString();
        String[] joinTables = new String[5];

        joinTables[0] = "inner:jofrantoba.catastro.tgv_actividad_economica as actividadEconomica:on:base.id_actividad_economica:actividadEconomica.id";
        joinTables[1] = "left:jofrantoba.catastro.tm_departamento as departamento:on:base.id_departamento:departamento.id";
        joinTables[2] = "left:jofrantoba.catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";
        joinTables[3] = "left:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";
        joinTables[4] = "left:(select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:base.id_tipo_via:tipoVia.id";
        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public Long countActividadEconomica(Long idActividadEconomica) throws Exception {
        String joinTable = "inner:actividadEconomica";
        String[] mapFilterField = {"=:actividadEconomica.id:" + idActividadEconomica, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countDepartamento(Long idDepartamento) throws Exception {
        String joinTable = "inner:departamento";
        String[] mapFilterField = {"=:departamento.id:" + idDepartamento, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countProvincia(Long idProvincia) throws Exception {
        String joinTable = "inner:provincia";
        String[] mapFilterField = {"=:provincia.id:" + idProvincia, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countDistrito(Long idDistrito) throws Exception {
        String joinTable = "inner:distrito";
        String[] mapFilterField = {"=:distrito.id:" + idDistrito, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoVia(Long idTipoVia) throws Exception {
        String joinTable = "inner:tipoVia";
        String[] mapFilterField = {"=:tipoVia.id:" + idTipoVia, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoConstrucciones.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoConstrucciones;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterConstrucciones;
import com.jofrantoba.examples.jofrantoba.entity.Construcciones;
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
public class DaoConstrucciones extends AbstractJpaDaoV2<Construcciones> implements InterDaoConstrucciones {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoConstrucciones() {
        super();
        this.setClazz(Construcciones.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterConstrucciones filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterConstrucciones filter,Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder,limit,offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterConstrucciones filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }
    
    private Map<String, Object> buildQueryList(FilterConstrucciones filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_construcciones as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.anio as anio,"));
        strFileds.append(share.append("base.muros as muros,"));
        strFileds.append(share.append("base.techos as techos,"));
        strFileds.append(share.append("base.pisos as pisos,"));
        strFileds.append(share.append("base.puertas_ventanas as puertasVentanas,"));
        strFileds.append(share.append("base.revestimiento revestimiento,"));
        strFileds.append(share.append("base.banios as banios,"));
        strFileds.append(share.append("base.instalaciones_electricas as instalacionesElectricas,"));
        strFileds.append(share.append("base.area_verificada as areaVerificada,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("nivel.id as idNivel,"));
        strFileds.append(share.append("nivel.descripcion as descripcionNivel,"));
        strFileds.append(share.append("mes.id as idMes,"));
        strFileds.append(share.append("mes.descripcion as descripcionMes,"));
        strFileds.append(share.append("materialEstructural.id as idMaterialEstructural,"));
        strFileds.append(share.append("materialEstructural.descripcion as descripcionMaterialEstructural,"));
        strFileds.append(share.append("estadoConservacion.id as idEstadoConservacion,"));
        strFileds.append(share.append("estadoConservacion.descripcion as descripcionEstadoConservacion,"));
        strFileds.append(share.append("estadoConstruccion.id as idEstadoConstruccion,"));
        strFileds.append(share.append("estadoConstruccion.descripcion as descripcionEstadoConstruccion,"));
        strFileds.append(share.append("uca.id as idUca,"));
        strFileds.append(share.append("uca.descripcion as descripcionUca"));
        String fields = strFileds.toString();
        String[] joinTables = new String[7];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:jofrantoba.catastro.tm_parametrias as nivel:on:base.id_nivel:nivel.id";
        joinTables[2] = "inner:jofrantoba.catastro.tm_parametrias as mes:on:base.id_mes:mes.id";
        joinTables[3] = "left:jofrantoba.catastro.tm_parametrias as materialEstructural:on:base.id_material_estructural:materialEstructural.id";
        joinTables[4] = "left:jofrantoba.catastro.tm_parametrias as estadoConservacion:on:base.id_estado_conservacion:estadoConservacion.id";
        joinTables[5] = "left:jofrantoba.catastro.tm_parametrias as estadoConstruccion:on:base.id_estado_construccion:estadoConstruccion.id";
        joinTables[6] = "left:jofrantoba.catastro.tm_parametrias as uca:on:base.id_uca:uca.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdNivel() != null) {
            filters.add("=:nivel.id:" + filter.getIdNivel());
        }
        if (filter.getIdMes() != null) {
            filters.add("=:mes.id:" + filter.getIdMes());
        }
        if (filter.getIdMaterialEstructural() != null) {
            filters.add("=:materialEstructural.id:" + filter.getIdMaterialEstructural());
        }
        if (filter.getIdEstadoConservacion() != null) {
            filters.add("=:estadoConservacion.id:" + filter.getIdEstadoConservacion());
        }
        if (filter.getIdEstadoConstruccion() != null) {
            filters.add("=:estadoConstruccion.id:" + filter.getIdEstadoConstruccion());
        }
        if (filter.getIdUca() != null) {
            filters.add("=:uca.id:" + filter.getIdUca());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);        
        return map;
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countNivel(Long idNivel) throws Exception {
        String joinTable = "inner:condicionTitular";
        String[] mapFilterField = {"=:nivel.id:" + idNivel, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countMes(Long idMes) throws Exception {
        String joinTable = "inner:mes";
        String[] mapFilterField = {"=:mes.id:" + idMes, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countMaterialEstructural(Long idMaterialEstructural) throws Exception {
        String joinTable = "inner:materialEstructural";
        String[] mapFilterField = {"=:materialEstructural.id:" + idMaterialEstructural, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countEstadoConservacion(Long idEstadoConservacion) throws Exception {
        String joinTable = "inner:estadoConservacion";
        String[] mapFilterField = {"=:estadoConservacion.id:" + idEstadoConservacion, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countEstadoConstruccion(Long idEstadoConstruccion) throws Exception {
        String joinTable = "inner:estadoConstruccion";
        String[] mapFilterField = {"=:estadoConstruccion.id:" + idEstadoConstruccion, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countUca(Long idUca) throws Exception {
        String joinTable = "inner:uca";
        String[] mapFilterField = {"=:uca.id:" + idUca, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoCucUnidad.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoCucUnidad;
import com.jofrantoba.examples.jofrantoba.entity.CucUnidad;
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
public class DaoCucUnidad extends AbstractJpaDaoV2<CucUnidad> implements InterDaoCucUnidad{
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoCucUnidad(){
        super();
        this.setClazz(CucUnidad.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<CucUnidad> listar(Long idLoteCatastral) throws Exception {
        String joinTable = "inner:loteCatastral";
        String[] mapFilterField = {"=:loteCatastral.id:" + idLoteCatastral, "=:base.isPersistente:true"};
        StringBuilder strFields = new StringBuilder();
        Shared share = new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.actual as actual,"));
        strFields.append(share.append("loteCatastral.id as idLoteCatastral"));
        String fields = strFields.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                CucUnidad bean = new CucUnidad();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<CucUnidad> lista=(Collection<CucUnidad>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, null);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offset) throws Exception {
        String table = "jofrantoba.catastro.tm_cuc_unidad as base";
        StringBuilder strFields = new StringBuilder();
        Shared share = new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.actual as actual,"));
        strFields.append(share.append("loteCatastral.id as idLoteCatastral"));
        String fields = strFields.toString();
        String[] joinTables = new String[1];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField,mapOrder,limit,offset, "and");
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "jofrantoba.catastro.tm_cuc_unidad as base";
        StringBuilder strFields = new StringBuilder();
        Shared share = new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.actual as actual,"));
        strFields.append(share.append("loteCatastral.id as idLoteCatastral"));
        String fields = strFields.toString();
        String[] joinTables = new String[1];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }

    @Override
    public Long count(Long idLoteCatastral) throws Exception {
        String joinTable = "inner:loteCatastral";
        String[] mapFilterField = {"=:loteCatastral.id:" + idLoteCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoDepartamento.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoDepartamento;
import com.jofrantoba.examples.jofrantoba.entity.Departamento;
import java.util.Collection;
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
public class DaoDepartamento extends AbstractJpaDaoV2<Departamento> implements InterDaoDepartamento {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoDepartamento() {
        super();
        this.setClazz(Departamento.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<Departamento> listar() throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder);
    }

    @Override
    public Collection<Departamento> listar(int pageNumber, int pageSize) throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder,pageNumber,pageSize);
    }
    
}

```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoDistrito.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoDistrito;
import com.jofrantoba.examples.jofrantoba.entity.Distrito;
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
public class DaoDistrito extends AbstractJpaDaoV2<Distrito> implements InterDaoDistrito {

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
        String table="jofrantoba.catastro.tm_distrito as base";
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
        joinTables[0]="inner:jofrantoba.catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tm_distrito as base";
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
        joinTables[0]="inner:jofrantoba.catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long count(Long idProvincia) throws Exception {
        String joinTable="inner:provincia";
        String[] mapFilterField={"=:provincia.id:"+idProvincia,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoDocumentos.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoDocumentos;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterDocumentos;
import com.jofrantoba.examples.jofrantoba.entity.Documentos;
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
public class DaoDocumentos extends AbstractJpaDaoV2<Documentos> implements InterDaoDocumentos {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoDocumentos() {
        super();
        this.setClazz(Documentos.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterDocumentos filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterDocumentos filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterDocumentos filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private Map<String, Object> buildQueryList(FilterDocumentos filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_documentos as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.numero_documento as numeroDocumento,"));
        strFileds.append(share.append("base.fecha_documento as fechaDocumento,"));
        strFileds.append(share.append("base.area_autorizada as areaAutorizada,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("tipoDocumento.id as idTipoDocumento,"));
        strFileds.append(share.append("tipoDocumento.descripcion as descripcionTipoDocumento"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:jofrantoba.catastro.tm_parametrias as tipoDocumento:on:base.id_tipo_documento:tipoDocumento.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdTipoDocumento() != null) {
            filters.add("=:tipoDocumento.id:" + filter.getIdTipoDocumento());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoDocumento(Long idTipoDocumento) throws Exception {
        String joinTable = "inner:tipoDocumento";
        String[] mapFilterField = {"=:tipoDocumento.id:" + idTipoDocumento, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoFileLoteCatastral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterFile;
import com.jofrantoba.examples.jofrantoba.entity.FileLoteCatastral;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoFileLoteCatastral;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoFileLoteCatastral extends AbstractJpaDaoV2<FileLoteCatastral> implements InterDaoFileLoteCatastral {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoFileLoteCatastral() {
        super();
        this.setClazz(FileLoteCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listar(FilterFile filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }
  
    @Override
    public ArrayNode listar(FilterFile filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }
    
    private Map<String, Object> buildQueryList(FilterFile filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_file_lote_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.tipo_file as tipoFile,"));
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));
        strFileds.append(share.append("base.principal as principal,"));        
        strFileds.append(share.append("base.anio as anio,"));
        strFileds.append(share.append("base.url_file as urlFile,"));
        strFileds.append(share.append("clasificacion.id as idClasificacion,"));
        strFileds.append(share.append("clasificacion.descripcion as descripcionClasificacion,"));
        strFileds.append(share.append("categoria.id as idCategoria,"));
        strFileds.append(share.append("categoria.descripcion as descripcionCategoria,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito"));
        String fields = strFileds.toString();
        String[] joinTables = new String[4];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        joinTables[1] = "inner:jofrantoba.catastro.tm_clasificacion as clasificacion:on:base.id_clasificacion:clasificacion.id";
        joinTables[2] = "inner:jofrantoba.catastro.tm_categoria as categoria:on:clasificacion.id_categoria:categoria.id";
        joinTables[3] = "inner:jofrantoba.catastro.tm_distrito as distrito:on:categoria.id_distrito:distrito.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdFile()!= null) {
            filters.add("=:base.id:" + filter.getIdFile());
        }
        if (filter.getIdLoteCatastral() != null) {
            filters.add("=:loteCatastral.id:" + filter.getIdLoteCatastral());
        }
        if (filter.getIdClasificacion() != null) {
            filters.add("=:clasificacion.id:" + filter.getIdClasificacion());
        }
        if (filter.getIdCategoria() != null) {
            filters.add("=:categoria.id:" + filter.getIdCategoria());
        }
        if (filter.getTipoFile() != null) {
            filters.add("equal:base.tipo_file:" + filter.getTipoFile());
        }
       
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public void asignarPrincipal(Long id)throws Exception { 
        Shared sharedUtil = new Shared(); 
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("update jofrantoba.catastro.tgv_file_lote_catastral set"));
        sql.append(sharedUtil.append("principal=:principal"));
        sql.append(sharedUtil.append("where id=:id"));
        String[] paramValues={"principal:String:S","id:Long:"+id};
        this.iudNativeQuery(sql.toString(),paramValues);
    }

    @Override
    public void limpiarPrincipal(Long idLoteCatastral)throws Exception {
        Shared sharedUtil = new Shared();  
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("update jofrantoba.catastro.tgv_file_lote_catastral set"));
        sql.append(sharedUtil.append("principal=:principal"));
        sql.append(sharedUtil.append("where id_lote_catastral=:idLoteCatastral"));
        String[] paramValues={"principal:String:N","idLoteCatastral:Long:"+idLoteCatastral};
        this.iudNativeQuery(sql.toString(),paramValues);
    }
    

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoFileUnidadCatastral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterFile;
import com.jofrantoba.examples.jofrantoba.entity.FileUnidadCatastral;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoFileUnidadCatastral;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoFileUnidadCatastral extends AbstractJpaDaoV2<FileUnidadCatastral> implements InterDaoFileUnidadCatastral {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoFileUnidadCatastral() {
        super();
        this.setClazz(FileUnidadCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listar(FilterFile filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }
  
    @Override
    public ArrayNode listar(FilterFile filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }
    
    private Map<String, Object> buildQueryList(FilterFile filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_file_unidad_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.tipo_file as tipoFile,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("base.principal as principal,"));        
        strFileds.append(share.append("base.anio as anio,"));
        strFileds.append(share.append("base.url_file as urlFile,"));
        strFileds.append(share.append("clasificacion.id as idClasificacion,"));
        strFileds.append(share.append("clasificacion.descripcion as descripcionClasificacion,"));
        strFileds.append(share.append("categoria.id as idCategoria,"));
        strFileds.append(share.append("categoria.descripcion as descripcionCategoria,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito"));
        String fields = strFileds.toString();
        String[] joinTables = new String[4];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:jofrantoba.catastro.tm_clasificacion as clasificacion:on:base.id_clasificacion:clasificacion.id";
        joinTables[2] = "inner:jofrantoba.catastro.tm_categoria as categoria:on:clasificacion.id_categoria:categoria.id";
        joinTables[3] = "inner:jofrantoba.catastro.tm_distrito as distrito:on:categoria.id_distrito:distrito.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdFile()!= null) {
            filters.add("=:base.id:" + filter.getIdFile());
        }
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdClasificacion() != null) {
            filters.add("=:clasificacion.id:" + filter.getIdClasificacion());
        }
        if (filter.getIdCategoria() != null) {
            filters.add("=:categoria.id:" + filter.getIdCategoria());
        }
        if (filter.getTipoFile() != null) {
            filters.add("equal:base.tipo_file:" + filter.getTipoFile());
        }
       
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public void asignarPrincipal(Long id) throws Exception {
        Shared sharedUtil = new Shared(); 
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("update jofrantoba.catastro.tgv_file_unidad_catastral set"));
        sql.append(sharedUtil.append("principal=:principal"));
        sql.append(sharedUtil.append("where id=:id"));
        String[] paramValues={"principal:String:S","id:Long:"+id};
        this.iudNativeQuery(sql.toString(),paramValues);
    }

    @Override
    public void limpiarPrincipal(Long idUnidadCatastral) throws Exception {
        Shared sharedUtil = new Shared();  
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("update jofrantoba.catastro.tgv_file_unidad_catastral set"));
        sql.append(sharedUtil.append("principal=:principal"));
        sql.append(sharedUtil.append("where id_unidad_catastral=:idUnidadCatastral"));
        String[] paramValues={"principal:String:N","idUnidadCatastral:Long:"+idUnidadCatastral};
        this.iudNativeQuery(sql.toString(),paramValues);
    }
    

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoHabilitacionUrbana.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoHabilitacionUrbana;
import com.jofrantoba.examples.jofrantoba.entity.HabilitacionUrbana;
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
public class DaoHabilitacionUrbana extends AbstractJpaDaoV2<HabilitacionUrbana> implements InterDaoHabilitacionUrbana {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoHabilitacionUrbana() {
        super();
        this.setClazz(HabilitacionUrbana.class);
        //this.setSessionFactory(sessionFactory);
    }
    
    @Override
    public Collection<HabilitacionUrbana> listar(Long idDistrito,Long idTipoHabilitacionUrbana,String descripcion)throws Exception {        
        String[] joinTables={"inner:distrito","inner:tipoHabilitacionUrbana"};
        List<String> filter=new ArrayList();
        filter.add("=:base.isPersistente:true");
        if(idDistrito!=null){
            filter.add("=:distrito.id:"+idDistrito);
        }
        if(idTipoHabilitacionUrbana!=null){
            filter.add("=:tipoHabilitacionUrbana.id:"+idTipoHabilitacionUrbana);
        }
        if(descripcion!=null && !descripcion.isEmpty()){
            filter.add("like:base.descripcion:"+"\'%"+descripcion+"%\'");
        }
        String[] mapFilterField=filter.toArray(new String[0]);
        String[] mapOrder={"base.descripcion:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));        
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("tipoHabilitacionUrbana.id as idTipoHabilitacionUrbana,"));
        strFileds.append(share.append("tipoHabilitacionUrbana.descripcion as descripcionTipoHabilitacionUrbana"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                HabilitacionUrbana bean=new HabilitacionUrbana();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<HabilitacionUrbana> lista=(Collection<HabilitacionUrbana>)this.customFieldsJoinFilterAnd(rt,fields,joinTables,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tm_habilitacion_urbana as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));   
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento,"));
        strFileds.append(share.append("distrito.codigo_distrito as codigoDistrito,"));
        strFileds.append(share.append("provincia.codigo_provincia as codigoProvincia,"));
        strFileds.append(share.append("departamento.codigo_departamento as codigoDepartamento"));
        String fields=strFileds.toString();
        String[] joinTables=new String[3];
        joinTables[0]="inner:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[2]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tm_habilitacion_urbana as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));   
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));        
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));        
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento,"));
        strFileds.append(share.append("distrito.codigo_distrito as codigoDistrito,"));
        strFileds.append(share.append("provincia.codigo_provincia as codigoProvincia,"));
        strFileds.append(share.append("departamento.codigo_departamento as codigoDepartamento"));
        String fields=strFileds.toString();
        String[] joinTables=new String[3];
        joinTables[0]="inner:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[2]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long count(Long idDistrito) throws Exception {
        String joinTable="inner:distrito";
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
    
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoInformacionCompl.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoInformacionCompl;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterInformacionCompl;
import com.jofrantoba.examples.jofrantoba.entity.InformacionCompl;
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
public class DaoInformacionCompl extends AbstractJpaDaoV2<InformacionCompl> implements InterDaoInformacionCompl {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoInformacionCompl() {
        super();
        this.setClazz(InformacionCompl.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<InformacionCompl> listarFilter(FilterInformacionCompl filter) throws Exception {
        String[] joinTables = {"left:condicionDeclarante", "left:estadoLlenado", "left:tipoDocumentoDecla", "left:mantenimiento", "inner:unidadCatastral"};
        List<String> filters = new ArrayList();
        filters.add("=:base.isPersistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdCondicionDeclarante() != null) {
            filters.add("=:condicionDeclarante.id:" + filter.getIdCondicionDeclarante());
        }
        if (filter.getIdEstadoLlenado() != null) {
            filters.add("=:estadoLlenado.id:" + filter.getIdEstadoLlenado());
        }
        if (filter.getIdTipoDocumentoDecla() != null) {
            filters.add("=:tipoDocumentoDecla.id:" + filter.getIdTipoDocumentoDecla());
        }
        if (filter.getIdMantenimiento() != null) {
            filters.add("=:mantenimiento.id:" + filter.getIdMantenimiento());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("condicionDeclarante.id as idCondicionDeclarante,"));
        strFileds.append(share.append("condicionDeclarante.descripcion as condicionDeclarante,"));
        strFileds.append(share.append("estadoLlenado.id as idEstadoLlenado,"));
        strFileds.append(share.append("estadoLlenado.descripcion as estadoLlenado,"));
        strFileds.append(share.append("tipoDocumentoDecla.id as idTipoDocumentoDecla,"));
        strFileds.append(share.append("tipoDocumentoDecla.descripcion as tipoDocumentoDecla,"));
        strFileds.append(share.append("mantenimiento.id as idMantenimiento,"));
        strFileds.append(share.append("mantenimiento.descripcion as mantenimiento"));
        String fields = strFileds.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                InformacionCompl bean = new InformacionCompl();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<InformacionCompl> lista = (Collection<InformacionCompl>) this.customFieldsJoinFilterAnd(rt, fields, joinTables, mapFilterField, mapOrder);
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table = "jofrantoba.catastro.tgv_informacion_compl as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("condicionDeclarante.id as idCondicionDeclarante,"));
        strFileds.append(share.append("condicionDeclarante.descripcion as condicionDeclarante,"));
        strFileds.append(share.append("estadoLlenado.id as idEstadoLlenado,"));
        strFileds.append(share.append("estadoLlenado.descripcion as estadoLlenado,"));
        strFileds.append(share.append("tipoDocumentoDecla.id as idTipoDocumentoDecla,"));
        strFileds.append(share.append("tipoDocumentoDecla.descripcion as tipoDocumentoDecla,"));
        strFileds.append(share.append("mantenimiento.id as idMantenimiento,"));
        strFileds.append(share.append("mantenimiento.descripcion as mantenimiento"));
        String fields = strFileds.toString();
        String[] joinTables = new String[5];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "left:jofrantoba.catastro.tm_parametrias as condicionDeclarante:on:base.id_condicion_declarante:condicionDeclarante.id";
        joinTables[2] = "left:jofrantoba.catastro.tm_parametrias as estadoLlenado:on:base.id_estado_llenado:estadoLlenado.id";
        joinTables[3] = "left:jofrantoba.catastro.tm_parametrias as tipoDocumentoDecla:on:base.id_tipo_documento_decla:tipoDocumentoDecla.id";
        joinTables[4] = "left:jofrantoba.catastro.tm_parametrias as mantenimiento:on:base.id_mantenimiento:mantenimiento.id";

        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "jofrantoba.catastro.tgv_informacion_compl as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("condicionDeclarante.id as idCondicionDeclarante,"));
        strFileds.append(share.append("condicionDeclarante.descripcion as condicionDeclarante,"));
        strFileds.append(share.append("estadoLlenado.id as idEstadoLlenado,"));
        strFileds.append(share.append("estadoLlenado.descripcion as estadoLlenado,"));
        strFileds.append(share.append("tipoDocumentoDecla.id as idTipoDocumentoDecla,"));
        strFileds.append(share.append("tipoDocumentoDecla.descripcion as tipoDocumentoDecla,"));
        strFileds.append(share.append("mantenimiento.id as idMantenimiento,"));
        strFileds.append(share.append("mantenimiento.descripcion as mantenimiento"));
        String fields = strFileds.toString();
        String[] joinTables = new String[5];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "left:jofrantoba.catastro.tm_parametrias as condicionDeclarante:on:base.id_condicion_declarante:condicionDeclarante.id";
        joinTables[2] = "left:jofrantoba.catastro.tm_parametrias as estadoLlenado:on:base.id_estado_llenado:estadoLlenado.id";
        joinTables[3] = "left:jofrantoba.catastro.tm_parametrias as tipoDocumentoDecla:on:base.id_tipo_documento_decla:tipoDocumentoDecla.id";
        joinTables[4] = "left:jofrantoba.catastro.tm_parametrias as mantenimiento:on:base.id_mantenimiento:mantenimiento.id";

        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
    
      @Override
    public Long countestadoLlenado(Long idEstadoLlenado) throws Exception {
        String joinTable = "inner:estadoLlenado";
        String[] mapFilterField = {"=:estadoLlenado.id:" + idEstadoLlenado, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }


    @Override
    public Long countCondicionDeclarante(Long idCondicionDeclarante) throws Exception {
        String joinTable = "inner:condicionDeclarante";
        String[] mapFilterField = {"=:condicionDeclarante.id:" + idCondicionDeclarante, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countDocumentoDecla(Long idDocumentoDecla) throws Exception {
        String joinTable = "inner:tipoDocumentoDecla";
        String[] mapFilterField = {"=:tipoDocumentoDecla.id:" + idDocumentoDecla, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countMantenimiento(Long idMantenimiento) throws Exception {
        String joinTable = "inner:mantenimiento";
        String[] mapFilterField = {"=:mantenimiento.id:" + idMantenimiento, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoInformacionComplActEcon.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoInformacionComplActEcon;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterInformacionComplActEcon;
import com.jofrantoba.examples.jofrantoba.entity.InformacionComplActEcon;
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
public class DaoInformacionComplActEcon extends AbstractJpaDaoV2<InformacionComplActEcon> implements InterDaoInformacionComplActEcon {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoInformacionComplActEcon() {
        super();
        this.setClazz(InformacionComplActEcon.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterInformacionComplActEcon filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterInformacionComplActEcon filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterInformacionComplActEcon filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private Map<String, Object> buildQueryList(FilterInformacionComplActEcon filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_informacion_compl_act_econ as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.condicion_declarante_otro as condicionDeclaranteOtro,"));
        strFileds.append(share.append("base.numero_documento_decla as numeroDocumentoDecla,"));
        strFileds.append(share.append("base.nombre_declarante as nombreDeclarante,"));
        strFileds.append(share.append("base.apellido_declarante as apellidoDeclarante,"));
        strFileds.append(share.append("base.fecha_declarante as fechaDeclarante,"));
        strFileds.append(share.append("base.documento_presentado_otro as documentoPresentadoOtro,"));
        strFileds.append(share.append("base.observacion as observacion,"));
        strFileds.append(share.append("actividadEconomica.id as idActividadEconomica,"));
        strFileds.append(share.append("condicionDeclarante.id as idCondicionDeclarante,"));
        strFileds.append(share.append("condicionDeclarante.descripcion as condicionDeclarante,"));
        strFileds.append(share.append("estadoLlenado.id as idEstadoLlenado,"));
        strFileds.append(share.append("estadoLlenado.descripcion as estadoLlenado,"));
        strFileds.append(share.append("tipoDocumentoDecla.id as idTipoDocumentoDecla,"));
        strFileds.append(share.append("tipoDocumentoDecla.descripcion as tipoDocumentoDecla,"));
        strFileds.append(share.append("mantenimiento.id as idMantenimiento,"));
        strFileds.append(share.append("mantenimiento.descripcion as mantenimiento,"));
        strFileds.append(share.append("documentoPresentado.id as idDocumentoPresentado,"));
        strFileds.append(share.append("documentoPresentado.descripcion as documentoPresentado"));
        String fields = strFileds.toString();
        String[] joinTables = new String[6];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_actividad_economica as actividadEconomica:on:base.id_actividad_economica:actividadEconomica.id";
        joinTables[1] = "left:jofrantoba.catastro.tm_parametrias as condicionDeclarante:on:base.id_condicion_declarante:condicionDeclarante.id";
        joinTables[2] = "left:jofrantoba.catastro.tm_parametrias as estadoLlenado:on:base.id_estado_llenado:estadoLlenado.id";
        joinTables[3] = "left:jofrantoba.catastro.tm_parametrias as tipoDocumentoDecla:on:base.id_tipo_documento_decla:tipoDocumentoDecla.id";
        joinTables[4] = "left:jofrantoba.catastro.tm_parametrias as mantenimiento:on:base.id_mantenimiento:mantenimiento.id";
        joinTables[5] = "left:jofrantoba.catastro.tm_parametrias as documentoPresentado:on:base.id_documento_presentado:documentoPresentado.id";

        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");

        if (filter.getIdActividadEconomica() != null) {
            filters.add("=:actividadEconomica.id:" + filter.getIdActividadEconomica());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countActividadEconomica(Long idActividadEconomica) throws Exception {
        String joinTable = "inner:actividadEconomica";
        String[] mapFilterField = {"=:actividadEconomica.id:" + idActividadEconomica, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countestadoLlenado(Long idEstadoLlenado) throws Exception {
        String joinTable = "inner:estadoLlenado";
        String[] mapFilterField = {"=:estadoLlenado.id:" + idEstadoLlenado, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countCondicionDeclarante(Long idCondicionDeclarante) throws Exception {
        String joinTable = "inner:condicionDeclarante";
        String[] mapFilterField = {"=:condicionDeclarante.id:" + idCondicionDeclarante, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoDocumentoDecla(Long idDocumentoDecla) throws Exception {
        String joinTable = "inner:tipoDocumentoDecla";
        String[] mapFilterField = {"=:tipoDocumentoDecla.id:" + idDocumentoDecla, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countMantenimiento(Long idMantenimiento) throws Exception {
        String joinTable = "inner:mantenimiento";
        String[] mapFilterField = {"=:mantenimiento.id:" + idMantenimiento, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countDocumentoPresentado(Long idDocumentoPresentado) throws Exception {
        String joinTable = "inner:documentoPresentado";
        String[] mapFilterField = {"=:documentoPresentado.id:" + idDocumentoPresentado, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoInscripcionCatastral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoInscripcionCatastral;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterInscripcionCatastral;
import com.jofrantoba.examples.jofrantoba.entity.InscripcionCatastral;
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
public class DaoInscripcionCatastral extends AbstractJpaDaoV2<InscripcionCatastral> implements InterDaoInscripcionCatastral {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoInscripcionCatastral() {
        super();
        this.setClazz(InscripcionCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterInscripcionCatastral filter) throws Exception {
         Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterInscripcionCatastral filter,Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterInscripcionCatastral filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private Map<String, Object> buildQueryList(FilterInscripcionCatastral filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_inscripcion_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.numero as numero,"));
        strFileds.append(share.append("base.fojas as fojas,"));
        strFileds.append(share.append("base.asiento as asiento,"));
        strFileds.append(share.append("base.fecha_inscripcion as fechaInscripcion,"));
        strFileds.append(share.append("base.inscripcion_fabrica as inscripcionFabrica,"));
        strFileds.append(share.append("base.fecha_inscripcion_fabrica as fechaInscripcionFabrica,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("tipoPartidaCatastral.id as idTipoPartidaCatastral,"));
        strFileds.append(share.append("tipoPartidaCatastral.descripcion as descripcionTipoPartidaCatastral,"));
        strFileds.append(share.append("declaratoriaFabrica.id as idDeclaratoriaFabrica,"));
        strFileds.append(share.append("declaratoriaFabrica.descripcion as descripcionDeclaratoriaFabrica"));
        String fields = strFileds.toString();
        String[] joinTables = new String[3];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:jofrantoba.catastro.tm_parametrias as tipoPartidaCatastral:on:base.id_tipo_partida_registral:tipoPartidaCatastral.id";
        joinTables[2] = "left:jofrantoba.catastro.tm_parametrias as declaratoriaFabrica:on:base.id_declaratoria_fabrica:declaratoriaFabrica.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");

        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdTipoPartidaCatastral() != null) {
            filters.add("=:tipoPartidaCatastral.id:" + filter.getIdTipoPartidaCatastral());
        }
        if (filter.getIdDeclaratoriaFabrica() != null) {
            filters.add("=:declaratoriaFabrica.id:" + filter.getIdDeclaratoriaFabrica());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoPartidaRegistral(Long idTipoPartidaCatastral) throws Exception {
        String joinTable = "inner:tipoPartidaCatastral";
        String[] mapFilterField = {"=:tipoPartidaCatastral.id:" + idTipoPartidaCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countDeclaratoriaFabrica(Long idDeclaratoriaFabrica) throws Exception {
        String joinTable = "inner:declaratoriaFabrica";
        String[] mapFilterField = {"=:declaratoriaFabrica.id:" + idDeclaratoriaFabrica, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoInterior.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoInterior;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterInterior;
import com.jofrantoba.examples.jofrantoba.entity.Interior;
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
public class DaoInterior extends AbstractJpaDaoV2<Interior> implements InterDaoInterior {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoInterior() {
        super();
        this.setClazz(Interior.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterInterior filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");        
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterInterior filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterInterior filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countNumeracion(Long idNumeracion) throws Exception {
        String joinTable = "inner:numeracion";
        String[] mapFilterField = {"=:numeracion.id:" + idNumeracion, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoEdificacion(Long idTipoEdificacion) throws Exception {
        String joinTable = "inner:tipoEdificacion";
        String[] mapFilterField = {"=:tipoEdificacion.id:" + idTipoEdificacion, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoInterior(Long idTipoInterior) throws Exception {
        String joinTable = "inner:tipoInterior";
        String[] mapFilterField = {"=:tipoInterior.id:" + idTipoInterior, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    private Map<String, Object> buildQueryList(FilterInterior filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_interior as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        //strFields.append(share.append("base.id_numeracion as idNumeracion,"));
        //strFields.append(share.append("base.id_tipo_edificacion as idTipoEdificacion,"));
        //strFields.append(share.append("base.id_tipo_interior as idTipoInterior,"));
        strFields.append(share.append("base.interior as interior,"));
        strFields.append(share.append("base.letra_interior as letraInterior,"));
        strFields.append(share.append("base.nombre_edificacion as nombreEdificacion,"));
        //strFields.append(share.append("base.codigo_unidad_catastral as codigoUnidadCatastral,"));
        //strFields.append(share.append("loteCatastral.codigo as codigoLoteCatastral,"));
        strFields.append(share.append("loteCatastral.id as idLoteCatastral,"));
        strFields.append(share.append("concat(unidadCatastral.codigo_departamento,unidadCatastral.codigo_provincia,unidadCatastral.codigo_distrito) as ubigeo,"));
        strFields.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFields.append(share.append("unidadCatastral.codigo_sector as codigoSector,"));
        strFields.append(share.append("unidadCatastral.codigo_manzana as codigoManzana,"));
        strFields.append(share.append("unidadCatastral.codigo_lote as codigoLote,"));
        strFields.append(share.append("unidadCatastral.codigo_edificacion as codigoEdificacion,"));
        strFields.append(share.append("unidadCatastral.codigo_entrada as codigoEntrada,"));
        strFields.append(share.append("unidadCatastral.codigo_piso as codigoPiso,"));
        strFields.append(share.append("unidadCatastral.codigo_unidad as codigoUnidad,"));
        //strFields.append(share.append("manzana.id as idManzana,"));
        strFields.append(share.append("manzana.descripcion as descripcionManzana,"));
        //strFields.append(share.append("habilitacionUrbana.id as idHabilitacionUrbana,"));
        //strFields.append(share.append("habilitacionUrbana.descripcion as descripcionHabilitacionUrbana,"));
        strFields.append(share.append("tipoVia.descripcion as descripcionTipoVia,"));
        strFields.append(share.append("via.descripcion as descripcionVia,"));
        strFields.append(share.append("via.codigo_via as codigoVia,"));
        strFields.append(share.append("viaCuadra.cuadra as cuadra,"));
        strFields.append(share.append("numeracion.numero as numero,"));
        strFields.append(share.append("tipoEdificacion.descripcion as descripcionTipoEdificacion,"));
        strFields.append(share.append("tipoInterior.descripcion as descripcionTipoInterior,"));
        strFields.append(share.append("tipoPuerta.descripcion as descripcionTipoPuerta,"));
        strFields.append(share.append("condicionNumeracion.descripcion as descripcionCondicionNumeracion"));
        String fields = strFields.toString();
        String[] joinTables = new String[13];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:unidadCatastral.id_lote_catastral:loteCatastral.id";
        joinTables[2] = "inner:jofrantoba.catastro.tg_manzana as manzana:on:loteCatastral.id_manzana:manzana.id";
        joinTables[3] = "inner:jofrantoba.catastro.tg_sector as sector:on:manzana.id_sector:sector.id";
        //joinTables[4] = "inner:jofrantoba.catastro.tm_habilitacion_urbana as habilitacionUrbana:on:loteCatastral.id_habilitacion_urbana:habilitacionUrbana.id";
        joinTables[4] = "inner:jofrantoba.catastro.tgv_numeracion as numeracion:on:base.id_numeracion:numeracion.id";
        joinTables[5] = "left:jofrantoba.catastro.tm_parametrias as condicionNumeracion:on:numeracion.id_condicion_numeracion:condicionNumeracion.id";
        joinTables[6] = "left:jofrantoba.catastro.tm_parametrias as tipoPuerta:on:numeracion.id_tipo_puerta:tipoPuerta.id";
        joinTables[7] = "left:jofrantoba.catastro.tm_parametrias as tipoEdificacion:on:base.id_tipo_edificacion:tipoEdificacion.id";
        joinTables[8] = "left:jofrantoba.catastro.tg_manzana_via as manzanaVia:on:numeracion.id_manzana_via:manzanaVia.id";
        joinTables[9] = "left:jofrantoba.catastro.tg_via_cuadra as viaCuadra:on:manzanaVia.id_via_cuadra:viaCuadra.id";
        joinTables[10] = "left:jofrantoba.catastro.tm_via as via:on:viaCuadra.id_via:via.id";
        joinTables[11] = "left:(select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        joinTables[12] = "left:jofrantoba.catastro.tm_parametrias as tipoInterior:on:base.id_tipo_interior:tipoInterior.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:base.id_unidad_catastral:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdNumeracion() != null) {
            filters.add("=:base.id_numeracion:" + filter.getIdNumeracion());
        }
        if (filter.getIdTipoEdificacion() != null) {
            filters.add("=:base.id_tipo_edificacion:" + filter.getIdTipoEdificacion());
        }
        if (filter.getIdTipoInterior() != null) {
            filters.add("=:base.id_tipo_interior:" + filter.getIdTipoInterior());
        }
        if (strValid(filter.getLetraInterior())) {
            filters.add("equal:base.letra_interior:" + filter.getLetraInterior());
        }
        if (strValid(filter.getCodigoDepartamento())) {
            filters.add("equal:unidadCatastral.codigo_departamento:" + filter.getCodigoDepartamento());
        }
        if (strValid(filter.getCodigoProvincia())) {
            filters.add("equal:unidadCatastral.codigo_provincia:" + filter.getCodigoProvincia());
        }
        if (strValid(filter.getCodigoDistrito())) {
            filters.add("equal:unidadCatastral.codigo_distrito:" + filter.getCodigoDistrito());
        }
        if (strValid(filter.getCodigoSector())) {
            filters.add("equal:unidadCatastral.codigo_sector:" + filter.getCodigoSector());
        }
        if (strValid(filter.getCodigoManzana())) {
            filters.add("equal:unidadCatastral.codigo_manzana:" + filter.getCodigoManzana());
        }
        if (strValid(filter.getCodigoLote())) {
            filters.add("equal:unidadCatastral.codigo_lote:" + filter.getCodigoLote());
        }
        if (strValid(filter.getCodigoEdificacion())) {
            filters.add("equal:unidadCatastral.codigo_edificacion:" + filter.getCodigoEdificacion());
        }
        if (strValid(filter.getCodigoEntrada())) {
            filters.add("equal:unidadCatastral.codigo_entrada:" + filter.getCodigoEntrada());
        }
        if (strValid(filter.getCodigoPiso())) {
            filters.add("equal:unidadCatastral.codigo_piso:" + filter.getCodigoPiso());
        }
        if (strValid(filter.getCodigoUnidad())) {
            filters.add("equal:unidadCatastral.codigo_unidad:" + filter.getCodigoUnidad());
        }
        if (filter.getIdLoteCatastral() != null) {
            filters.add("=:unidadCatastral.id_lote_catastral:" + filter.getIdLoteCatastral());
        }
        if (filter.getIdUsoEspecifico() != null) {
            filters.add("=:unidadCatastral.id_uso_especifico:" + filter.getIdUsoEspecifico());
        }
        if (filter.getIdClasificacionPredio() != null) {
            filters.add("=:unidadCatastral.id_clasificacion_predio:" + filter.getIdClasificacionPredio());
        }
        if (filter.getIdPredioEn() != null) {
            filters.add("=:unidadCatastral.id_predio_en:" + filter.getIdPredioEn());
        }
        if (filter.getIdManzana() != null) {
            filters.add("=:manzana.id:" + filter.getIdManzana());
        }
        /*if (filter.getIdHabilitacionUrbana() != null) {
            filters.add("=:habilitacionUrbana.id:" + filter.getIdHabilitacionUrbana());
        }*/
        if (filter.getIdDistrito() != null) {
            filters.add("=:sector.id_distrito:" + filter.getIdDistrito());
        }
        if (filter.getIdVia() != null) {
            filters.add("=:via.id:" + filter.getIdVia());
        }
        if (strValid(filter.getNumero())) {
            filters.add("equal:numeracion.numero:" + filter.getNumero());
        }
        if (strValid(filter.getLetra())) {
            filters.add("equal:numeracion.letra:" + filter.getLetra());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"id:asc"};
        map.put("fields", fields);
        map.put("mapOrder", mapOrder);
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("mapFilterField", mapFilterField);
        return map;
    }

    private boolean strValid(String value) {
        return (value != null && !value.trim().isEmpty());
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoLinderos.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoLinderos;
import com.jofrantoba.examples.jofrantoba.entity.Linderos;
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
public class DaoLinderos extends AbstractJpaDaoV2<Linderos> implements InterDaoLinderos {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoLinderos() {
        super();
        this.setClazz(Linderos.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<Linderos> listarFilter(Long idLoteCatastral,Long idTipoLindero)throws Exception {        
        String[] joinTables={"left:tipoLindero","inner:loteCatastral"};
        List<String> filter=new ArrayList();
        filter.add("=:base.isPersistente:true");
        if(idLoteCatastral!=null){
            filter.add("=:loteCatastral.id:"+idLoteCatastral);
        }
        if(idTipoLindero!=null){
            filter.add("=:tipoLindero.id:"+idTipoLindero);
        }
        String[] mapFilterField=filter.toArray(new String[0]);        
        String[] mapOrder={"base.id:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));              
        strFileds.append(share.append("base.colindancia as colindancia,"));              
        strFileds.append(share.append("base.medidaCampo as medidaCampo,"));                      
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));        
        strFileds.append(share.append("tipoLindero.id as idTipoLindero,"));
        strFileds.append(share.append("tipoLindero.descripcion as descripcionTipoLindero"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Linderos bean=new Linderos();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Linderos> lista=(Collection<Linderos>)this.customFieldsJoinFilterAnd(rt,fields,joinTables,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tgv_linderos as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("base.colindancia as colindancia,"));              
        strFileds.append(share.append("base.medida_campo as medidaCampo,"));                      
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("tipoLindero.id as idTipoLindero,"));
        strFileds.append(share.append("tipoLindero.descripcion as descripcionTipoLindero"));
        String fields=strFileds.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";        
        joinTables[1]="left:jofrantoba.catastro.tm_parametrias as tipoLindero:on:base.id_tipo_lindero:tipoLindero.id";                
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tgv_linderos as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));   
        strFileds.append(share.append("base.colindancia as colindancia,"));              
        strFileds.append(share.append("base.medida_campo as medidaCampo,")); 
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("tipoLindero.id as idTipoLindero,"));
        strFileds.append(share.append("tipoLindero.descripcion as descripcionTipoLindero"));
        String fields=strFileds.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";        
        joinTables[1]="left:jofrantoba.catastro.tm_parametrias as tipoLindero:on:base.id_tipo_lindero:tipoLindero.id";                
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long countLoteCatastral(Long idLoteCatastral) throws Exception {
        String joinTable="inner:loteCatastral";
        String[] mapFilterField={"=:loteCatastral.id:"+idLoteCatastral,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
    
    @Override
    public Long countTipoLindero(Long idTipoLindero) throws Exception {
        String joinTable="inner:tipoLindero";
        String[] mapFilterField={"=:tipoLindero.id:"+idTipoLindero,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoLitigantes.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoLitigantes;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterLitigantes;
import com.jofrantoba.examples.jofrantoba.entity.Litigantes;
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
public class DaoLitigantes extends AbstractJpaDaoV2<Litigantes> implements InterDaoLitigantes {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoLitigantes() {
        super();
        this.setClazz(Litigantes.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterLitigantes filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterLitigantes filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterLitigantes filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private Map<String, Object> buildQueryList(FilterLitigantes filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_litigantes as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.numero_documento as numeroDocumento,"));
        strFileds.append(share.append("base.nombres as nombres,"));
        strFileds.append(share.append("base.codigo_contribuyente as codigoContribuyente,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("tipoDocumento.id as idTipoDocumento,"));
        strFileds.append(share.append("tipoDocumento.descripcion as descripcionTipoDocumento"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:jofrantoba.catastro.tm_parametrias as tipoDocumento:on:base.id_tipo_documento:tipoDocumento.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdTipoDocumento() != null) {
            filters.add("=:tipoDocumento.id:" + filter.getIdTipoDocumento());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoDocumento(Long idTipoDocumento) throws Exception {
        String joinTable = "inner:tipoDocumento";
        String[] mapFilterField = {"=:tipoDocumento.id:" + idTipoDocumento, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoLogsEntityOperation.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoLogsEntityOperation;
import com.jofrantoba.examples.jofrantoba.entity.LogsEntityOperation;
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
public class DaoLogsEntityOperation extends AbstractJpaDaoV2<LogsEntityOperation> implements InterDaoLogsEntityOperation {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoLogsEntityOperation() {
        super();
        this.setClazz(LogsEntityOperation.class);
        //this.setSessionFactory(sessionFactory);
    }
}

```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoLoteCatastral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoLoteCatastral;
import com.jofrantoba.examples.jofrantoba.dto.beans.CodigoLoteCatastral;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterLoteCatastral;
import com.jofrantoba.examples.jofrantoba.entity.LoteCatastral;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Slf4j
@Repository
public class DaoLoteCatastral extends AbstractJpaDaoV2<LoteCatastral> implements InterDaoLoteCatastral {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoLoteCatastral() {
        super();
        this.setClazz(LoteCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }
    
    @Override
    public ArrayNode listarFilter(FilterLoteCatastral filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String groupBy = (String) map.get("groupBy");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String fieldsSq = (String) map.get("fieldsSq");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBySq = (String) map.get("groupBySq");
        return this.allFieldsJoinPostgresGroupBySubQuery(fields, groupBy, mapOrder, joinTables, table, fieldsSq, mapFilterField, groupBySq);
    }
    
    private boolean strValid(String value) {
        return (value != null && !value.trim().isEmpty());
    }
    
    @Override
    public ArrayNode listar(FilterLoteCatastral filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String groupBy = (String) map.get("groupBy");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String fieldsSq = (String) map.get("fieldsSq");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBySq = (String) map.get("groupBySq");
        return this.allFieldsLimitJoinPostgresGroupBySubQuery(fields, groupBy, mapOrder, joinTables, table, fieldsSq, mapFilterField, groupBySq, limit, offSet);
    }
    
    @Override
    public ArrayNode listar(FilterLoteCatastral filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String groupBy = (String) map.get("groupBy");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String fieldsSq = (String) map.get("fieldsSq");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBySq = (String) map.get("groupBySq");
        return this.allFieldsJoinPostgresGroupBySubQuery(fields, groupBy, mapOrder, joinTables, table, fieldsSq, mapFilterField, groupBySq);
    }
    
    private Map<String, Object> buildQueryList(FilterLoteCatastral filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_lote_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("id,"));
        strFileds.append(share.append("ubigeo,"));
        strFileds.append(share.append("codigoSector,"));
        strFileds.append(share.append("codigoManzana,"));
        strFileds.append(share.append("codigoLote,"));
        strFileds.append(share.append("idManzana,"));
        strFileds.append(share.append("descripcionManzana,"));
        //strFileds.append(share.append("descripcionHabilitacionUrbana,"));        
        strFileds.append(share.append("string_agg(direccion, '; ') AS direccion"));
        String fields = strFileds.toString();
        
        StringBuilder strFiledsSq = new StringBuilder();
        strFiledsSq.append(share.append("base.id as id,"));
        strFiledsSq.append(share.append("concat(base.codigo_departamento,base.codigo_provincia,base.codigo_distrito) as ubigeo,"));
        strFiledsSq.append(share.append("base.codigo_sector as codigoSector,"));
        strFiledsSq.append(share.append("base.codigo_manzana as codigoManzana,"));
        strFiledsSq.append(share.append("base.codigo_lote as codigoLote,"));
        strFiledsSq.append(share.append("manzana.id as idManzana,"));
        strFiledsSq.append(share.append("manzana.descripcion as descripcionManzana,"));
        //strFiledsSq.append(share.append("habilitacionUrbana.id as idHabilitacionUrbana,"));
        //strFiledsSq.append(share.append("habilitacionUrbana.descripcion as descripcionHabilitacionUrbana,"));
        strFiledsSq.append(share.append("concat(tipoVia.descripcion,' ',via.descripcion,' NÂ° ',string_agg(distinct numeracion.numero,',')) as direccion"));
        String fieldsSq = strFiledsSq.toString();
        
        String[] joinTables = new String[7];
        joinTables[0] = "inner:jofrantoba.catastro.tg_manzana as manzana:on:base.id_manzana:manzana.id";
        joinTables[1] = "inner:jofrantoba.catastro.tg_sector as sector:on:manzana.id_sector:sector.id";
        //joinTables[2] = "inner:jofrantoba.catastro.tm_habilitacion_urbana as habilitacionUrbana:on:base.id_habilitacion_urbana:habilitacionUrbana.id";
        joinTables[2] = "left:(select * from jofrantoba.catastro.tgv_numeracion where is_persistente=true) as numeracion:on:base.id:numeracion.id_lote_catastral";
        joinTables[3] = "left:(select * from jofrantoba.catastro.tg_manzana_via where is_persistente=true) as manzanaVia:on:numeracion.id_manzana_via:manzanaVia.id";
        joinTables[4] = "left:(select * from jofrantoba.catastro.tg_via_cuadra where is_persistente=true) as viaCuadra:on:manzanaVia.id_via_cuadra:viaCuadra.id";
        joinTables[5] = "left:(select * from jofrantoba.catastro.tm_via where is_persistente=true) as via:on:viaCuadra.id_via:via.id";
        joinTables[6] = "left:(select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre=1 and is_persistente=true) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        filters.add("=:manzana.is_persistente:true");
        filters.add("=:sector.is_persistente:true");
        if (strValid(filter.getCodigoDepartamento())) {
            filters.add("equal:base.codigo_departamento:" + filter.getCodigoDepartamento());
        }
        if (strValid(filter.getCodigoProvincia())) {
            filters.add("equal:base.codigo_provincia:" + filter.getCodigoProvincia());
        }
        if (strValid(filter.getCodigoDistrito())) {
            filters.add("equal:base.codigo_distrito:" + filter.getCodigoDistrito());
        }
        if (strValid(filter.getCodigoSector())) {
            filters.add("equal:base.codigo_sector:" + filter.getCodigoSector());
        }
        if (strValid(filter.getCodigoManzana())) {
            filters.add("equal:base.codigo_manzana:" + filter.getCodigoManzana());
        }
        if (strValid(filter.getCodigoLote())) {
            filters.add("equal:base.codigo_lote:" + filter.getCodigoLote());
        }
        if (filter.getIdManzana() != null) {
            filters.add("=:manzana.id:" + filter.getIdManzana());
        }
        /*if (filter.getIdHabilitacionUrbana() != null) {
            filters.add("=:habilitacionUrbana.id:" + filter.getIdHabilitacionUrbana());
        }*/
        if (filter.getIdDistrito() != null) {
            filters.add("=:sector.id_distrito:" + filter.getIdDistrito());
        }
        if (filter.getIdVia() != null) {
            filters.add("=:via.id:" + filter.getIdVia());
        }
        if (strValid(filter.getNumero())) {
            filters.add("equal:numeracion.numero:" + filter.getNumero());
        }
        if (strValid(filter.getLetra())) {
            filters.add("equal:numeracion.letra:" + filter.getLetra());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        StringBuilder groupBySq = new StringBuilder();
        groupBySq.append(share.append("base.id,"));        
        groupBySq.append(share.append("base.codigo_departamento,"));
        groupBySq.append(share.append("base.codigo_provincia,"));
        groupBySq.append(share.append("base.codigo_distrito,"));
        groupBySq.append(share.append("base.codigo_sector,"));
        groupBySq.append(share.append("base.codigo_manzana,"));
        groupBySq.append(share.append("base.codigo_lote,"));
        groupBySq.append(share.append("manzana.id,"));
        groupBySq.append(share.append("manzana.descripcion,"));
        //groupBySq.append(share.append("habilitacionUrbana.id,"));
        //groupBySq.append(share.append("habilitacionUrbana.descripcion,"));
        groupBySq.append(share.append("tipoVia.descripcion,"));
        groupBySq.append(share.append("via.descripcion"));
        
        StringBuilder groupBy = new StringBuilder();
        groupBy.append(share.append("id,"));
        groupBy.append(share.append("ubigeo,"));
        groupBy.append(share.append("codigoSector,"));
        groupBy.append(share.append("codigoManzana,"));
        groupBy.append(share.append("codigoLote,"));
        groupBy.append(share.append("idManzana,"));
        groupBy.append(share.append("descripcionManzana"));
        //groupBy.append(share.append("descripcionHabilitacionUrbana"));
        String[] mapOrder = {"ubigeo:asc","codigoSector:asc","codigoManzana:asc","codigoLote:asc"};
        map.put("fields", fields);
        map.put("groupBy", groupBy.toString());
        map.put("mapOrder", mapOrder);
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fieldsSq", fieldsSq);
        map.put("mapFilterField", mapFilterField);
        map.put("groupBySq", groupBySq.toString());
        return map;
    }
    
    @Override
    public Long countManzana(Long idManzana) throws Exception {
        String joinTable = "inner:manzana";
        String[] mapFilterField = {"=:manzana.id:" + idManzana, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
    
    /*@Override
    public Long countHabilitacionUrbana(Long idHabilitacionUrbana) throws Exception {
        String joinTable = "inner:habilitacionUrbana";
        String[] mapFilterField = {"=:habilitacionUrbana.id:" + idHabilitacionUrbana, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }*/
    
    @Override
    public String newValueCodigoLote(CodigoLoteCatastral codigoLoteCatastral) throws Exception {
        String fields = "cast(coalesce(max(base.codigoLote),'000') as long)+1";
        String[] mapFilterField = new String[5];        
        mapFilterField[0] = "equal:base.codigoDepartamento:" + codigoLoteCatastral.getCodigoDepartamento();
        mapFilterField[1] = "equal:base.codigoProvincia:" + codigoLoteCatastral.getCodigoProvincia();
        mapFilterField[2] = "equal:base.codigoDistrito:" + codigoLoteCatastral.getCodigoDistrito();
        mapFilterField[3] = "equal:base.codigoSector:" + codigoLoteCatastral.getCodigoSector();
        mapFilterField[4] = "equal:base.codigoManzana:" + codigoLoteCatastral.getCodigoManzana();
        Long valueNew = this.aggregateJoinFilterAndGroupBy(fields, null, mapFilterField, null);
        String codigo = "000" + valueNew;
        codigo = codigo.substring(codigo.length() - 3, codigo.length());
        return codigo;
    }
    
    @Override
    public ArrayNode listarLotes(Long idManzana) throws Exception {
        String table = "jofrantoba.catastro.tgv_lote_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("base.codigo_lote as codigoLote"));        
        String fieldsSq = strFileds.toString();        
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (idManzana != null) {
            filters.add("=:base.id_manzana:" + idManzana);
        }        
        String[] mapFilterField = filters.toArray(new String[0]);  
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsPostgres(table, fieldsSq, mapFilterField, mapOrder);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoLoteHabilitacionUrbana.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoLoteHabilitacionUrbana;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterLoteHabilitacionUrbana;
import com.jofrantoba.examples.jofrantoba.entity.LoteHabilitacionUrbana;
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
public class DaoLoteHabilitacionUrbana extends AbstractJpaDaoV2<LoteHabilitacionUrbana> implements InterDaoLoteHabilitacionUrbana {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoLoteHabilitacionUrbana() {
        super();
        this.setClazz(LoteHabilitacionUrbana.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterLoteHabilitacionUrbana filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterLoteHabilitacionUrbana filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterLoteHabilitacionUrbana filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private Map<String, Object> buildQueryList(FilterLoteHabilitacionUrbana filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_lote_habilitacion_urbana as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));
        strFileds.append(share.append("habilitacionUrbana.id as idHabilitacionUrbana,"));
        strFileds.append(share.append("habilitacionUrbana.descripcion as descripcionHabilitacionUrbana,"));
        strFileds.append(share.append("base.zona_sector_etapa as zonaSectorEtapa,"));
        strFileds.append(share.append("base.manzana_urbana as manzanaUrbana,"));
        strFileds.append(share.append("base.lote_urbano as loteUrbana,"));
        strFileds.append(share.append("base.sub_lote_urbano as subLote,"));
        strFileds.append(share.append("base.codigo_lote_catastral as codigoLoteCatastral"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        joinTables[1] = "left:jofrantoba.catastro.tm_habilitacion_urbana as habilitacionUrbana:on:base.id_habilitacion_urbana:habilitacionUrbana.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");

        if (filter.getIdLoteCatastral()!= null) {
            filters.add("=:loteCatastral.id:" + filter.getIdLoteCatastral());
        }
        if (filter.getIdHabilitacionUrbana()!= null) {
            filters.add("=:habilitacionUrbana.id:" + filter.getIdHabilitacionUrbana());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countLoteCatastral(Long idLoteCatastral) throws Exception {
        String joinTable = "inner:loteCatastral";
        String[] mapFilterField = {"=:loteCatastral.id:" + idLoteCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countHabilitacionUrbana(Long idHabilitacionUrbana) throws Exception {
        String joinTable = "inner:habilitacionUrbana";
        String[] mapFilterField = {"=:habilitacionUrbana.id:" + idHabilitacionUrbana, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countLoteHabilitacionUrbana(Long idLoteCatastral, Long idHabilitacionUrbana) throws Exception {
        String[] joinTables = new String[2];
        joinTables[0] = "inner:habilitacionUrbana";
        joinTables[1] = "inner:loteCatastral";
        String[] mapFilterField={"=:habilitacionUrbana.id:"+idHabilitacionUrbana,"=:loteCatastral.id:"+idLoteCatastral,"=:base.isPersistente:true"};        
        return this.rowCountJoinsFilterAnd(joinTables,mapFilterField); 
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoLoteZonificacion.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoLoteZonificacion;
import com.jofrantoba.examples.jofrantoba.entity.LoteZonificacion;
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
public class DaoLoteZonificacion extends AbstractJpaDaoV2<LoteZonificacion> implements InterDaoLoteZonificacion {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoLoteZonificacion() {
        super();
        this.setClazz(LoteZonificacion.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<LoteZonificacion> listarFilter(Long idLoteCatastral,Long idZonficacion)throws Exception {        
        String[] joinTables={"inner:zonificacion","inner:loteCatastral"};
        List<String> filter=new ArrayList();
        filter.add("=:base.isPersistente:true");
        if(idLoteCatastral!=null){
            filter.add("=:loteCatastral.id:"+idLoteCatastral);
        }
        if(idZonficacion!=null){
            filter.add("=:zonificacion.id:"+idZonficacion);
        }
        String[] mapFilterField=filter.toArray(new String[0]);        
        String[] mapOrder={"base.id:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));              
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));        
        strFileds.append(share.append("zonificacion.id as idZonificacion,"));
        strFileds.append(share.append("zonificacion.descripcion as descripcionZonificacion"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                LoteZonificacion bean=new LoteZonificacion();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<LoteZonificacion> lista=(Collection<LoteZonificacion>)this.customFieldsJoinFilterAnd(rt,fields,joinTables,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tgv_lote_zonificacion as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("zonificacion.id as idZonificacion,"));
        strFileds.append(share.append("zonificacion.descripcion as descripcionZonificacion"));
        String fields=strFileds.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_zonificacion as zonificacion:on:base.id_zonificacion:zonificacion.id";                
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tgv_lote_zonificacion as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("zonificacion.id as idZonificacion,"));
        strFileds.append(share.append("zonificacion.descripcion as descripcionZonificacion"));
        String fields=strFileds.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_zonificacion as zonificacion:on:base.id_zonificacion:zonificacion.id";                
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long countLoteCatastral(Long idLoteCatastral) throws Exception {
        String joinTable="inner:loteCatastral";
        String[] mapFilterField={"=:loteCatastral.id:"+idLoteCatastral,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
    
    @Override
    public Long countZonificacion(Long idZonificacion) throws Exception {
        String joinTable="inner:zonificacion";
        String[] mapFilterField={"=:zonificacion.id:"+idZonificacion,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public Long countLoteZonificacion(Long idLoteCatastral, Long idZonificacion) throws Exception {
        String[] joinTables = new String[2];
        joinTables[0] = "inner:zonificacion";
        joinTables[1] = "inner:loteCatastral";
        String[] mapFilterField={"=:zonificacion.id:"+idZonificacion,"=:loteCatastral.id:"+idLoteCatastral,"=:base.isPersistente:true"};        
        return this.rowCountJoinsFilterAnd(joinTables,mapFilterField); 
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoManzana.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoManzana;
import com.jofrantoba.examples.jofrantoba.entity.Manzana;
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
public class DaoManzana extends AbstractJpaDaoV2<Manzana> implements InterDaoManzana {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoManzana() {
        super();
        this.setClazz(Manzana.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<Manzana> listar(Long idSector)throws Exception {        
        String joinTable="inner:sector";
        String[] mapFilterField={"=:sector.id:"+idSector,"=:base.isPersistente:true"};
        String[] mapOrder={"base.codigoDepartamento:asc","base.codigoProvincia:asc","base.codigoDistrito:asc","base.codigoSector:asc","base.codigoManzana:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.codigoManzana as codigoManzana,"));
        strFileds.append(share.append("base.codigoSector as codigoSector,"));
        strFileds.append(share.append("base.codigoDistrito as codigoDistrito,"));
        strFileds.append(share.append("base.codigoProvincia as codigoProvincia,"));
        strFileds.append(share.append("base.codigoDepartamento as codigoDepartamento,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("sector.id as idSector,"));
        strFileds.append(share.append("sector.descripcion as descripcionSector"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Manzana bean=new Manzana();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Manzana> lista=(Collection<Manzana>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tg_manzana as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.codigo_manzana as codigoManzana,"));
        strFileds.append(share.append("base.codigo_sector as codigoSector,"));
        strFileds.append(share.append("base.codigo_distrito as codigoDistrito,"));
        strFileds.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFileds.append(share.append("base.codigo_Departamento as codigoDepartamento,"));
        strFileds.append(share.append("sector.id as idSector,"));
        strFileds.append(share.append("sector.descripcion as descripcionSector,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields=strFileds.toString();
        String[] joinTables=new String[4];
        joinTables[0]="inner:jofrantoba.catastro.tg_sector as sector:on:base.id_sector:sector.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_distrito as distrito:on:sector.id_distrito:distrito.id";        
        joinTables[2]="inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[3]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.codigo_departamento:asc","base.codigo_provincia:asc","base.codigo_distrito:asc","base.codigo_sector:asc","base.codigo_manzana:asc"};                
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tg_manzana as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.codigo_manzana as codigoManzana,"));
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
        String[] joinTables=new String[4];
        joinTables[0]="inner:jofrantoba.catastro.tg_sector as sector:on:base.id_sector:sector.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_distrito as distrito:on:sector.id_distrito:distrito.id";        
        joinTables[2]="inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[3]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String fields=strFileds.toString();
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.codigo_departamento:asc","base.codigo_provincia:asc","base.codigo_distrito:asc","base.codigo_sector:asc","base.codigo_manzana:asc"};                
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long count(Long idSector) throws Exception {
        String joinTable="inner:sector";
        String[] mapFilterField={"=:sector.id:"+idSector,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoManzanaVia.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoManzanaVia;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterManzanaVia;
import com.jofrantoba.examples.jofrantoba.entity.ManzanaVia;
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
public class DaoManzanaVia extends AbstractJpaDaoV2<ManzanaVia> implements InterDaoManzanaVia {

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
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");      
    }

    @Override
    public ArrayNode listar(FilterManzanaVia filter,Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }
    
    @Override
    public ArrayNode listar(FilterManzanaVia filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");        
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
        String table="jofrantoba.catastro.tg_manzana_via as base";
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
        joinTables[0]="inner:jofrantoba.catastro.tg_manzana as manzana:on:base.id_manzana:manzana.id";        
        joinTables[1]="inner:jofrantoba.catastro.tg_via_cuadra as viaCuadra:on:base.id_via_cuadra:viaCuadra.id";      
        joinTables[2]="left:jofrantoba.catastro.tm_via as via:on:viaCuadra.id_via:via.id"; 
        joinTables[3]="left:(select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:via.id_tipo_via:tipoVia.id";
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
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoMenu.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoMenu;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterMenu;
import com.jofrantoba.examples.jofrantoba.entity.Menu;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */
@Slf4j
@Repository
public class DaoMenu extends AbstractJpaDaoV2<Menu> implements InterDaoMenu {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }

    public DaoMenu() {
        super();
        this.setClazz(Menu.class);
        //this.setSessionFactory(sessionFactory);
    }    

    @Override
    public Collection<Menu> parents(FilterMenu filter) throws Exception {
        String[] joinTable = {"left:parent", "inner:clienteSistema", "inner:clienteSistema.sistema"};
        //String[] mapFilterField={"isnull:parent.id","=:base.isPersistente:true"};
        List<String> filterList = new ArrayList();
        filterList.add("isnull:parent.id");
        filterList.add("=:base.isPersistente:true");
        if (filter.getIdClienteSistema() != null) {
            filterList.add("=:clienteSistema.id:" + filter.getIdClienteSistema());
        }
        if (filter.getIdSistema() != null) {
            filterList.add("=:sistema.id:" + filter.getIdSistema());
        }
        String[] mapFilterField = filterList.toArray(new String[0]);
        String[] mapOrder = {"base.orden:asc"};
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.icono as icono,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.nivel as nivel,"));
        strFileds.append(share.append("base.tipo as tipo,"));
        strFileds.append(share.append("base.ruta as ruta,"));
        strFileds.append(share.append("base.numeroSubmenu as numeroSubmenu"));
        String fields = strFileds.toString();
        Collection<Menu> lista = this.customFieldsJoinFilterAnd(fields, joinTable, mapFilterField, mapOrder);
        return lista;
    }

    @Override
    public Collection<Menu> parents(FilterMenu filter, int pageNumber, int pageSize) throws Exception {
        String[] joinTable = {"left:parent", "inner:clienteSistema", "inner:clienteSistema.sistema"};
        //String[] mapFilterField={"isnull:parent.id","=:base.isPersistente:true"};
        List<String> filterList = new ArrayList();
        filterList.add("isnull:parent.id");
        filterList.add("=:base.isPersistente:true");
        if (filter.getIdClienteSistema() != null) {
            filterList.add("=:clienteSistema.id:" + filter.getIdClienteSistema());
        }
        if (filter.getIdSistema() != null) {
            filterList.add("=:sistema.id:" + filter.getIdSistema());
        }
        String[] mapFilterField = filterList.toArray(new String[0]);
        String[] mapOrder = {"base.orden:asc"};
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.nivel as nivel,"));
        strFileds.append(share.append("base.tipo as tipo,"));
        strFileds.append(share.append("base.ruta as ruta,"));
        strFileds.append(share.append("base.numeroSubmenu as numeroSubmenu"));
        String fields = strFileds.toString();
        Collection<Menu> lista = this.customFieldsJoinFilterAnd(fields, joinTable, mapFilterField, mapOrder, pageNumber, pageSize);
        return lista;
    }

    @Override
    public Collection<Menu> childrens(FilterMenu filter) throws Exception {
        String[] joinTable = {"left:parent", "inner:clienteSistema", "inner:clienteSistema.sistema"};
        //String joinTable="left:parent";
        //String[] mapFilterField={"isnotnull:parent.id","=:base.isPersistente:true"};
        List<String> filterList = new ArrayList();
        filterList.add("isnotnull:parent.id");
        filterList.add("=:base.isPersistente:true");
        if (filter.getIdClienteSistema() != null) {
            filterList.add("=:clienteSistema.id:" + filter.getIdClienteSistema());
        }
        if (filter.getIdSistema() != null) {
            filterList.add("=:sistema.id:" + filter.getIdSistema());
        }
        String[] mapFilterField = filterList.toArray(new String[0]);
        String[] mapOrder = {"base.orden:asc"};
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.nivel as nivel,"));
        strFileds.append(share.append("base.tipo as tipo,"));
        strFileds.append(share.append("base.ruta as ruta,"));
        strFileds.append(share.append("base.numeroSubmenu as numeroSubmenu,"));
        strFileds.append(share.append("parent.id as idParent,"));
        strFileds.append(share.append("parent.descripcion as descripcionParent"));
        String fields = strFileds.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Menu bean = new Menu();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Menu> lista = (Collection<Menu>) this.customFieldsJoinFilterAnd(rt, fields, joinTable, mapFilterField, mapOrder);
        return lista;
    }

    @Override
    public Collection<Menu> childrensByParents(Long idParent) throws Exception {
        String joinTable = "left:parent";
        String[] mapFilterField = {"=:parent.id:" + idParent, "=:base.isPersistente:true"};
        String[] mapOrder = {"base.orden:asc"};
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.nivel as nivel,"));
        strFileds.append(share.append("base.tipo as tipo,"));
        strFileds.append(share.append("base.ruta as ruta,"));
        strFileds.append(share.append("base.numeroSubmenu as numeroSubmenu,"));
        strFileds.append(share.append("parent.id as idParent,"));
        strFileds.append(share.append("parent.descripcion as descripcionParent"));
        String fields = strFileds.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Menu bean = new Menu();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Menu> lista = (Collection<Menu>) this.customFieldsJoinFilterAnd(rt, fields, joinTable, mapFilterField, mapOrder);
        return lista;
    }

    @Override
    public Long countChildrens(Long idParent) throws Exception {
        String joinTable = "left:parent";
        String[] mapFilterField = {"=:parent.id:" + idParent, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long maxOrdenChildrens(Long idParent) throws Exception {
        String fieldMax = "base.orden";
        String joinTable = "left:parent";
        String[] mapFilterField = {"=:parent.id:" + idParent, "=:base.isPersistente:true"};
        Long maxOrden = (Long) this.maxValueJoinFilterAnd(fieldMax, joinTable, mapFilterField);
        return maxOrden != null ? maxOrden : 0l;
    }

    @Override
    public Long maxOrdenNivel(Long idClienteSistema, Long nivel) throws Exception {
        String fieldMax = "base.orden";
        String joinTable = "inner:clienteSistema";
        String[] mapFilterField = {"=:clienteSistema.id:" + idClienteSistema, "=:base.isPersistente:true", "=:base.nivel:" + nivel};
        Long maxOrden = (Long) this.maxValueJoinFilterAnd(fieldMax, joinTable, mapFilterField);
        return maxOrden != null ? maxOrden : 0l;
    }

    @Override
    public ArrayNode listar(FilterMenu filter, Long limit, Long offSet) throws Exception {
        String table = "jofrantoba.seguridad.tg_menu as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.icono as icono,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("menuPadre.id as idMenuPadre,"));
        strFileds.append(share.append("menuPadre.descripcion as descripcionMenuPadre,"));
        strFileds.append(share.append("clienteSistema.descripcion as descripcionApp,"));
        strFileds.append(share.append("sistema.descripcion as descripcionSistema"));
        String fields = strFileds.toString();
        String[] joinTables = new String[3];
        joinTables[0] = "left:jofrantoba.seguridad.tg_menu as menuPadre:on:base.id_menu_padre:menuPadre.id";
        joinTables[1] = "inner:jofrantoba.seguridad.tg_cliente_sistema as clienteSistema:on:base.id_cliente_sistema:clienteSistema.id";
        joinTables[2] = "inner:jofrantoba.seguridad.tg_sistema as sistema:on:clienteSistema.id_sistema:sistema.id";
        //String[] mapFilterField={"=:base.is_persistente:true"};
        List<String> filterList = new ArrayList();
        filterList.add("=:base.is_persistente:true");
        if (filter.getIdClienteSistema() != null) {
            filterList.add("=:clienteSistema.id:" + filter.getIdClienteSistema());
        }
        if (filter.getIdSistema() != null) {
            filterList.add("=:sistema.id:" + filter.getIdSistema());
        }
        String[] mapFilterField = filterList.toArray(new String[0]);
        String[] mapOrder = {"orden:asc"};
        //return this.allFieldsLimitOffsetPostgres(table,fields,mapFilterField,mapOrder, limit, offSet);
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterMenu filter) throws Exception {
        String table = "jofrantoba.seguridad.tg_menu as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.icono as icono,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("menuPadre.id as idMenuPadre,"));
        strFileds.append(share.append("menuPadre.descripcion as descripcionMenuPadre,"));
        strFileds.append(share.append("clienteSistema.descripcion as descripcionApp,"));
        strFileds.append(share.append("sistema.descripcion as descripcionSistema"));
        String fields = strFileds.toString();
        String[] joinTables = new String[3];
        joinTables[0] = "left:jofrantoba.seguridad.tg_menu as menuPadre:on:base.id_menu_padre:menuPadre.id";
        joinTables[1] = "inner:jofrantoba.seguridad.tg_cliente_sistema as clienteSistema:on:base.id_cliente_sistema:clienteSistema.id";
        joinTables[2] = "inner:jofrantoba.seguridad.tg_sistema as sistema:on:clienteSistema.id_sistema:sistema.id";
        //String[] mapFilterField={"=:base.is_persistente:true"};
        List<String> filterList = new ArrayList();
        filterList.add("=:base.is_persistente:true");
        if (filter.getIdClienteSistema() != null) {
            filterList.add("=:clienteSistema.id:" + filter.getIdClienteSistema());
        }
        if (filter.getIdSistema() != null) {
            filterList.add("=:sistema.id:" + filter.getIdSistema());
        }
        String[] mapFilterField = filterList.toArray(new String[0]);
        String[] mapOrder = {"orden:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    public ArrayNode listarChildrens(FilterMenu filter) throws Exception {
        String table = "jofrantoba.seguridad.tg_menu as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.icono as icono,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.nivel as nivel,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.ruta as path,"));
        strFileds.append(share.append("base.tipo as tipo,"));
        strFileds.append(share.append("base.numero_submenu as numerosubmenu,"));
        strFileds.append(share.append("menuPadre.id as idmenupadre"));
        String fields = strFileds.toString();
        String[] joinTables = new String[3];
        joinTables[0] = "inner:jofrantoba.seguridad.tg_menu as menuPadre:on:base.id_menu_padre:menuPadre.id";
        joinTables[1] = "inner:jofrantoba.seguridad.tg_cliente_sistema as clienteSistema:on:base.id_cliente_sistema:clienteSistema.id";
        joinTables[2] = "inner:jofrantoba.seguridad.tg_sistema as sistema:on:clienteSistema.id_sistema:sistema.id";
        //String[] mapFilterField={"=:base.is_persistente:true"};
        List<String> filterList = new ArrayList();
        filterList.add("=:base.is_persistente:true");
        if (filter.getIdClienteSistema() != null) {
            filterList.add("=:clienteSistema.id:" + filter.getIdClienteSistema());
        }
        if (filter.getIdSistema() != null) {
            filterList.add("=:sistema.id:" + filter.getIdSistema());
        }
        String[] mapFilterField = filterList.toArray(new String[0]);
        String[] mapOrder = {"orden:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode createTreeMenu(FilterMenu filter) throws Exception {
        Collection<Menu> listParents = this.parents(filter);
        Iterator<Menu> iterador = listParents.iterator();
        ArrayNode arrayChildrens = listarChildrens(filter);
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        while (iterador.hasNext()) {
            Menu menuPadre = iterador.next();
            ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
            node.put("id", menuPadre.getId());
            node.put("descripcion", menuPadre.getDescripcion());
            node.put("nivel", menuPadre.getNivel());
            node.put("orden", menuPadre.getOrden());
            node.put("path", menuPadre.getRuta());
            node.put("tipo", menuPadre.getTipo());
            node.put("numerosubmenu", menuPadre.getNumeroSubmenu());
            node.put("icono", menuPadre.getIcono());
            createTree(node, arrayChildrens);
            array.add(node);
        }
        return array;
    }

    private void createTree(ObjectNode nodePadre, ArrayNode arrayChildrens) throws Exception {
        int cont = 0;
        for (int j = 0; j < arrayChildrens.size(); j++) {
            JsonNode beanHijo = arrayChildrens.get(j);
            if (beanHijo.isObject()) {
                ObjectNode nodeHijo = (ObjectNode) beanHijo;
                if (Long.parseLong(nodePadre.get("id").toString()) == Long.parseLong(nodeHijo.get("idmenupadre").toString())) {
                    ArrayNode arraySubmenu;
                    if (nodePadre.get("submenus") == null) {
                        arraySubmenu = new ArrayNode(JsonNodeFactory.instance);
                        nodePadre.set("submenus", arraySubmenu);
                    }
                    arraySubmenu = (ArrayNode) nodePadre.get("submenus");
                    arraySubmenu.add(nodeHijo);
                    createTree(nodeHijo, arrayChildrens);
                    cont = cont + 1;
                }
                if (cont == Integer.parseInt(nodePadre.get("numerosubmenu").toString())) {
                    break;
                }
            }
        }
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoNumeracion.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoNumeracion;
import com.jofrantoba.examples.jofrantoba.entity.Numeracion;
import java.util.ArrayList;
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
public class DaoNumeracion extends AbstractJpaDaoV2<Numeracion> implements InterDaoNumeracion {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoNumeracion() {
        super();
        this.setClazz(Numeracion.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public ArrayNode listarFilter(Long idLoteCatastral,Long idManzanaVia,Long idTipoPuerta, Long idCondicionNumeracion) throws Exception {
        String table="jofrantoba.catastro.tgv_numeracion as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("tipoVia.descripcion as tipoVia,"));
        strFileds.append(share.append("via.descripcion as via,")); 
        strFileds.append(share.append("base.numero as numero,"));        
        strFileds.append(share.append("base.letra as letra,"));        
        strFileds.append(share.append("via.codigo_via as codigoVia,"));      
        strFileds.append(share.append("viaCuadra.cuadra as cuadra,"));      
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("manzanaVia.id as idManzanaVia,"));       
        strFileds.append(share.append("tipoPuerta.id as idTipoPuerta,"));
        strFileds.append(share.append("tipoPuerta.descripcion as descripcionPuerta,"));
        strFileds.append(share.append("condicionNumeracion.id as idCondicionNumeracion,"));
        strFileds.append(share.append("condicionNumeracion.descripcion as descripcionCondicionNumeracion,"));       
        strFileds.append(share.append("trim(concat(coalesce(tipoVia.descripcion,''),' ',coalesce(via.descripcion,''),' Cda. ',coalesce(cast(viaCuadra.cuadra as varchar),''))) as direccionNumeracion"));       
        String fields=strFileds.toString();
        String[] joinTables=new String[7];
        joinTables[0]="inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        joinTables[1]="inner:jofrantoba.catastro.tg_manzana_via as manzanaVia:on:base.id_manzana_via:manzanaVia.id";
        joinTables[2]="left:jofrantoba.catastro.tg_via_cuadra as viaCuadra:on:manzanaVia.id_via_cuadra:viaCuadra.id";
        joinTables[3]="left:jofrantoba.catastro.tm_via as via:on:viaCuadra.id_via:via.id"; 
        joinTables[4]="left:(select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        joinTables[5]="left:jofrantoba.catastro.tm_parametrias as condicionNumeracion:on:base.id_condicion_numeracion:condicionNumeracion.id";
        joinTables[6]="left:jofrantoba.catastro.tm_parametrias as tipoPuerta:on:base.id_tipo_puerta:tipoPuerta.id";
        List<String> filter=new ArrayList();
        filter.add("=:base.is_persistente:true");
        if(idLoteCatastral!=null){
            filter.add("=:loteCatastral.id:"+idLoteCatastral);
        }
        if(idManzanaVia!=null){
            filter.add("=:manzanaVia.id:"+idManzanaVia);
        }
        if(idTipoPuerta!=null){
            filter.add("=:tipoPuerta.id:"+idTipoPuerta);
        }
        if(idCondicionNumeracion!=null){
            filter.add("=:condicionNumeracion.id:"+idCondicionNumeracion);
        }
        String[] mapFilterField=filter.toArray(new String[0]);
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tgv_numeracion as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("tipoVia.descripcion as tipoVia,"));
        strFileds.append(share.append("via.descripcion as via,")); 
        strFileds.append(share.append("base.numero as numero,"));        
        strFileds.append(share.append("base.letra as letra,"));       
        strFileds.append(share.append("via.codigo_via as codigoVia,"));      
        strFileds.append(share.append("viaCuadra.cuadra as cuadra,"));        
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("manzanaVia.id as idManzanaVia,"));       
        strFileds.append(share.append("tipoPuerta.id as idTipoPuerta,"));
        strFileds.append(share.append("tipoPuerta.descripcion as descripcionPuerta,"));
        strFileds.append(share.append("condicionNumeracion.id as idCondicionNumeracion,"));
        strFileds.append(share.append("condicionNumeracion.descripcion as descripcionCondicionNumeracion"));       
        String fields=strFileds.toString();
        String[] joinTables=new String[7];
        joinTables[0]="inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        joinTables[1]="inner:jofrantoba.catastro.tg_manzana_via as manzanaVia:on:base.id_manzana_via:manzanaVia.id";
        joinTables[2]="left:jofrantoba.catastro.tg_via_cuadra as viaCuadra:on:manzanaVia.id_via_cuadra:viaCuadra.id";
        joinTables[3]="left:jofrantoba.catastro.tm_via as via:on:viaCuadra.id_via:via.id"; 
        joinTables[4]="left:(select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        joinTables[5]="left:jofrantoba.catastro.tm_parametrias as condicionNumeracion:on:base.id_condicion_numeracion:condicionNumeracion.id";
        joinTables[6]="left:jofrantoba.catastro.tm_parametrias as tipoPuerta:on:base.id_tipo_puerta:tipoPuerta.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");               
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tgv_numeracion as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));        
        strFileds.append(share.append("tipoVia.descripcion as tipoVia,"));
        strFileds.append(share.append("via.descripcion as via,")); 
        strFileds.append(share.append("base.numero as numero,"));        
        strFileds.append(share.append("base.letra as letra,"));     
        strFileds.append(share.append("via.codigo_via as codigoVia,"));      
        strFileds.append(share.append("viaCuadra.cuadra as cuadra,"));            
        strFileds.append(share.append("loteCatastral.id as idLoteCatastral,"));      
        strFileds.append(share.append("manzanaVia.id as idManzanaVia,"));       
        strFileds.append(share.append("tipoPuerta.id as idTipoPuerta,"));
        strFileds.append(share.append("tipoPuerta.descripcion as descripcionPuerta,"));
        strFileds.append(share.append("condicionNumeracion.id as idCondicionNumeracion,"));
        strFileds.append(share.append("condicionNumeracion.descripcion as descripcionCondicionNumeracion"));       
        String fields=strFileds.toString();
        String[] joinTables=new String[7];
        joinTables[0]="inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        joinTables[1]="inner:jofrantoba.catastro.tg_manzana_via as manzanaVia:on:base.id_manzana_via:manzanaVia.id";
        joinTables[2]="left:jofrantoba.catastro.tg_via_cuadra as viaCuadra:on:manzanaVia.id_via_cuadra:viaCuadra.id";
        joinTables[3]="left:jofrantoba.catastro.tm_via as via:on:viaCuadra.id_via:via.id"; 
        joinTables[4]="left:(select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        joinTables[5]="left:jofrantoba.catastro.tm_parametrias as condicionNumeracion:on:base.id_condicion_numeracion:condicionNumeracion.id";
        joinTables[6]="left:jofrantoba.catastro.tm_parametrias as tipoPuerta:on:base.id_tipo_puerta:tipoPuerta.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long countLoteCatastral(Long idLoteCatastral) throws Exception {
        String joinTable="inner:loteCatastral";
        String[] mapFilterField={"=:loteCatastral.id:"+idLoteCatastral,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
    
    @Override
    public Long countManzanaVia(Long idManzanaVia) throws Exception {
        String joinTable="inner:manzanaVia";
        String[] mapFilterField={"=:manzanaVia.id:"+idManzanaVia,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public Long countTipoPuerta(Long idTipoPuerta) throws Exception {
        String joinTable="inner:tipoPuerta";
        String[] mapFilterField={"=:tipoPuerta.id:"+idTipoPuerta,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public Long countCondicionNumeracion(Long idCondicionNumeracion) throws Exception {
        String joinTable="inner:condicionNumeracion";
        String[] mapFilterField={"=:condicionNumeracion.id:"+idCondicionNumeracion,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoOtrasInstalaciones.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoOtrasInstalaciones;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterOtrasInstalaciones;
import com.jofrantoba.examples.jofrantoba.entity.OtrasInstalaciones;
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
public class DaoOtrasInstalaciones extends AbstractJpaDaoV2<OtrasInstalaciones> implements InterDaoOtrasInstalaciones {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoOtrasInstalaciones() {
        super();
        this.setClazz(OtrasInstalaciones.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterOtrasInstalaciones filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterOtrasInstalaciones filter,Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder,limit,offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterOtrasInstalaciones filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }
    
    private Map<String, Object> buildQueryList(FilterOtrasInstalaciones filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_otras_instalaciones as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.anio as anio,"));
        strFileds.append(share.append("base.producto_total as productoTotal,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("tipoOtrasInstalaciones.id as idTipoOtrasInstalaciones,"));
        strFileds.append(share.append("tipoOtrasInstalaciones.descripcion as tipoOtrasInstalaciones,"));
        strFileds.append(share.append("tipoOtrasInstalaciones.unidad_medida as unidadTipoOtrasInstalaciones,"));
        strFileds.append(share.append("mes.id as idMes,"));
        strFileds.append(share.append("mes.descripcion as descripcionMes,"));
        strFileds.append(share.append("materialEstructural.id as idMaterialEstructural,"));
        strFileds.append(share.append("materialEstructural.descripcion as descripcionMaterialEstructural,"));
        strFileds.append(share.append("estadoConservacion.id as idEstadoConservacion,"));
        strFileds.append(share.append("estadoConservacion.descripcion as descripcionEstadoConservacion,"));
        strFileds.append(share.append("estadoConstruccion.id as idEstadoConstruccion,"));
        strFileds.append(share.append("estadoConstruccion.descripcion as descripcionEstadoConstruccion,"));
        strFileds.append(share.append("uca.id as idUca,"));
        strFileds.append(share.append("uca.descripcion as descripcionUca"));
        String fields = strFileds.toString();
        String[] joinTables = new String[7];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:jofrantoba.catastro.tm_tipo_otras_instalaciones as tipoOtrasInstalaciones:on:base.id_tipo_otras_instalaciones:tipoOtrasInstalaciones.id";
        joinTables[2] = "inner:jofrantoba.catastro.tm_parametrias as mes:on:base.id_mes:mes.id";
        joinTables[3] = "left:jofrantoba.catastro.tm_parametrias as materialEstructural:on:base.id_material_estructural:materialEstructural.id";
        joinTables[4] = "left:jofrantoba.catastro.tm_parametrias as estadoConservacion:on:base.id_estado_conservacion:estadoConservacion.id";
        joinTables[5] = "left:jofrantoba.catastro.tm_parametrias as estadoConstruccion:on:base.id_estado_construccion:estadoConstruccion.id";
        joinTables[6] = "left:jofrantoba.catastro.tm_parametrias as uca:on:base.id_uca:uca.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdTipoOtrasInstalaciones() != null) {
            filters.add("=:tipoOtrasInstalaciones.id:" + filter.getIdTipoOtrasInstalaciones());
        }
        if (filter.getIdEstadoConservacion() != null) {
            filters.add("=:estadoConservacion.id:" + filter.getIdEstadoConservacion());
        }
        if (filter.getIdEstadoConstruccion() != null) {
            filters.add("=:estadoConstruccion.id:" + filter.getIdEstadoConstruccion());
        }
        if (filter.getIdMaterialEstructural() != null) {
            filters.add("=:materialEstructural.id:" + filter.getIdMaterialEstructural());
        }
        if (filter.getIdMes() != null) {
            filters.add("=:mes.id:" + filter.getIdMes());
        }
        if (filter.getIdUca() != null) {
            filters.add("=:uca.id:" + filter.getIdUca());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);        
        return map;
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoOtrasInstalaciones(Long idTipoOtrasInstalaciones) throws Exception {
        String joinTable = "inner:tipoOtrasInstalaciones";
        String[] mapFilterField = {"=:tipoOtrasInstalaciones.id:" + idTipoOtrasInstalaciones, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long counntMes(Long counntMes) throws Exception {
        String joinTable = "inner:mes";
        String[] mapFilterField = {"=:mes.id:" + counntMes, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countMaterialEstructural(Long idMaterialEstructural) throws Exception {
        String joinTable = "inner:materialEstructural";
        String[] mapFilterField = {"=:materialEstructural.id:" + idMaterialEstructural, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countEstadoConservacion(Long idEstadoConservacion) throws Exception {
        String joinTable = "inner:estadoConservacion";
        String[] mapFilterField = {"=:estadoConservacion.id:" + idEstadoConservacion, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countEstadoConstruccion(Long idEstadoConstruccion) throws Exception {
        String joinTable = "inner:estadoConstruccion";
        String[] mapFilterField = {"=:estadoConstruccion.id:" + idEstadoConstruccion, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countUca(Long idUca) throws Exception {
        String joinTable = "inner:uca";
        String[] mapFilterField = {"=:uca.id:" + idUca, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoParametrias.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoParametrias;
import com.jofrantoba.examples.jofrantoba.entity.Parametrias;
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
public class DaoParametrias extends AbstractJpaDaoV2<Parametrias> implements InterDaoParametrias {
    
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoParametrias() {
        super();
        this.setClazz(Parametrias.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<Parametrias> parents()throws Exception{
        String joinTable="left:parent";
        String[] mapFilterField={"isnull:parent.id","=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        String fields="base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura, base.codigo as codigo, base.orden as orden";
        Collection<Parametrias> lista=this.customFieldsJoinFilterAnd(fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }
    
    @Override
    public Collection<Parametrias> parents(int pageNumber, int pageSize)throws Exception{
        String joinTable="left:parent";
        String[] mapFilterField={"isnull:parent.id","=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        String fields="base.id as id,base.descripcion as descripcion";
        Collection<Parametrias> lista=this.customFieldsJoinFilterAnd(fields,joinTable,mapFilterField, mapOrder,pageNumber,pageSize);        
        return lista;
    }
    
    @Override
    public Collection<Parametrias> childrens()throws Exception{        
        String joinTable="left:parent";
        String[] mapFilterField={"isnotnull:parent.id","=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        String fields="base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo,base.orden as orden,parent.id as idParent,parent.descripcion as descripcionParent";
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Parametrias bean=new Parametrias();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Parametrias> lista=(Collection<Parametrias>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public Collection<Parametrias> childrensByParents(Long idParent) throws Exception {
        String joinTable="left:parent";
        String[] mapFilterField={"=:parent.id:"+idParent,"=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        String fields="base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo,base.orden as orden,parent.id as idParent,parent.descripcion as descripcionParent";
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Parametrias bean=new Parametrias();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Parametrias> lista=(Collection<Parametrias>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public Long countChildrens(Long idParent) throws Exception {
        String joinTable="left:parent";
        String[] mapFilterField={"=:parent.id:"+idParent,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tm_parametrias as base";
        String fields="base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"id:asc"};        
        return this.allFieldsLimitOffsetPostgres(table,fields,mapFilterField,mapOrder, limit, offSet);
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tm_parametrias as base";
        String fields="base.id as id,base.descripcion as descripcion,base.abreviatura as abreviatura,base.codigo as codigo";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"id:asc"};        
        return this.allFieldsPostgres(table,fields,mapFilterField,mapOrder);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoPerfil.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoPerfil;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterPerfil;
import com.jofrantoba.examples.jofrantoba.entity.Perfil;
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
public class DaoPerfil extends AbstractJpaDaoV2<Perfil> implements InterDaoPerfil {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoPerfil(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(Perfil.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listar(FilterPerfil filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterPerfil filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    private Map<String, Object> buildQueryList(FilterPerfil filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.seguridad.tg_perfil as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("sistema.id as idSistema"));
        String fields = strFileds.toString();
        String[] joinTables = new String[1];
        joinTables[0] = "inner:jofrantoba.seguridad.tg_sistema as sistema:on:base.id_sistema:sistema.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdPerfil()!= null) {
            filters.add("=:base.id:" + filter.getIdPerfil());
        }
        if (filter.getIdSistema() != null) {
            filters.add("=:sistema.id:" + filter.getIdSistema());
        }
        if (filter.getDescripcion() != null) {
            filters.add("equal:base.descripcion:" + filter.getDescripcion());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoPerfilMenu.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoPerfilMenu;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterPerfilMenu;
import com.jofrantoba.examples.jofrantoba.entity.PerfilMenu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
public class DaoPerfilMenu extends AbstractJpaDaoV2<PerfilMenu> implements InterDaoPerfilMenu {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoPerfilMenu() {
        super();
        this.setClazz(PerfilMenu.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listar(FilterPerfilMenu filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterPerfilMenu filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private Map<String, Object> buildQueryList(FilterPerfilMenu filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.seguridad.tg_perfil_menu as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("perfil.id as idPerfil,"));
        strFileds.append(share.append("perfil.descripcion as descripcionPerfil,"));
        strFileds.append(share.append("menu.id as idMenu,"));
        strFileds.append(share.append("menu.descripcion as descripcionMenu,"));
        strFileds.append(share.append("sistema.id as idSistema,"));
        strFileds.append(share.append("sistema.descripcion as descripcionSistema"));
        String fields = strFileds.toString();
        String[] joinTables = new String[3];
        joinTables[0] = "inner:jofrantoba.seguridad.tg_perfil as perfil:on:base.id_perfil:perfil.id";
        joinTables[1] = "inner:jofrantoba.seguridad.tg_menu as menu:on:base.id_menu:menu.id";
        joinTables[2] = "inner:jofrantoba.seguridad.tg_sistema as sistema:on:perfil.id_sistema:sistema.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdPerfil() != null) {
            filters.add("=:perfil.id:" + Long.parseLong(filter.getIdPerfil()));
        }
        if (filter.getIdMenu() != null) {
            filters.add("=:menu.id:" + filter.getIdMenu());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countPerfil(Long idPerfil) throws Exception {
        String joinTable = "inner:perfil";
        String[] mapFilterField = {"=:perfil.id:" + idPerfil, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countMenu(Long idMenu) throws Exception {
        String joinTable = "inner:menu";
        String[] mapFilterField = {"=:menu.id:" + idMenu, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public int deletePerfilMenu(Long idPerfil) throws Exception {
        String[] mapFilterField = {"=:perfil.id:" + idPerfil};
        return this.deleteFilterAnd(mapFilterField);
    }

    @Override
    public ArrayNode createTreeMenu(FilterPerfilMenu filter) throws Exception {
        ArrayNode arrayParents = this.menuParents(filter);
        ArrayNode arrayChildrens = this.menuChildrens(filter);
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        Iterator<JsonNode> iterador = arrayParents.iterator();
        while (iterador.hasNext()) {
            ObjectNode node = (ObjectNode) iterador.next();
            createTree(node, arrayChildrens);
            array.add(node);
        }
        return array;
    }

    @Override
    public ArrayNode createTreeMenuPerfiles(FilterPerfilMenu filter) throws Exception {
        ArrayNode arrayParents = this.menuParentsPerfiles(filter);
        ArrayNode arrayChildrens = this.menuChildrensPerfiles(filter);
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        Iterator<JsonNode> iterador = arrayParents.iterator();
        while (iterador.hasNext()) {
            ObjectNode node = (ObjectNode) iterador.next();
            createTree(node, arrayChildrens);
            array.add(node);
        }
        return array;
    }

    private void createTree(ObjectNode nodePadre, ArrayNode arrayChildrens) throws Exception {
        int cont = 0;
        for (int j = 0; j < arrayChildrens.size(); j++) {
            JsonNode beanHijo = arrayChildrens.get(j);
            if (beanHijo.isObject()) {
                ObjectNode nodeHijo = (ObjectNode) beanHijo;
                if (Long.parseLong(nodePadre.get("id").toString()) == Long.parseLong(nodeHijo.get("idmenupadre").toString())) {
                    ArrayNode arraySubmenu;
                    if (nodePadre.get("submenus") == null) {
                        arraySubmenu = new ArrayNode(JsonNodeFactory.instance);
                        nodePadre.set("submenus", arraySubmenu);
                    }
                    arraySubmenu = (ArrayNode) nodePadre.get("submenus");
                    arraySubmenu.add(nodeHijo);
                    createTree(nodeHijo, arrayChildrens);
                    cont = cont + 1;
                }
                if (cont == Integer.parseInt(nodePadre.get("numerosubmenu").toString())) {
                    break;
                }
            }
        }
    }

    private ArrayNode menuParents(FilterPerfilMenu filter) throws Exception {
        String table = "(select * from jofrantoba.seguridad.tg_perfil_menu where id_perfil=" + filter.getIdPerfil() + " and is_persistente=true) as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id_perfil_menu,"));
        strFileds.append(share.append("menu.id as id,"));
        strFileds.append(share.append("menu.descripcion as descripcion,"));
        strFileds.append(share.append("menu.nivel as nivel,"));
        strFileds.append(share.append("menu.orden as orden,"));
        strFileds.append(share.append("menu.ruta as path,"));
        strFileds.append(share.append("menu.tipo as tipo,"));
        strFileds.append(share.append("menu.numero_submenu as numerosubmenu,"));
        strFileds.append(share.append("case when not base.id is null then true else false end selected,"));
        strFileds.append(share.append(filter.getIdPerfil().toString())).append(" as idperfil,");
        strFileds.append(share.append("menu.id_cliente_sistema as idclientesistema"));
        String fields = strFileds.toString();
        String[] joinTables = new String[1];
        joinTables[0] = "right:jofrantoba.seguridad.tg_menu as menu:on:base.id_menu:menu.id";
        List<String> filters = new ArrayList();
        filters.add("isnull:menu.id_menu_padre");
        filters.add("=:menu.id_cliente_sistema:" + filter.getIdClienteSistema());
        filters.add("=:menu.is_persistente:true");
        if (filter.getIsOnlyPerfilMenu()) {
            filters.add("=:base.id_perfil:" + Long.parseLong(filter.getIdPerfil()));
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"menu.orden:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private String valuesByComas(int init, String values) {
        String valuesIn = "";
        String[] mapFilterFieldsValues = values.split(":");
        int valueControl = mapFilterFieldsValues.length - 1;
        for (int i = init; i < mapFilterFieldsValues.length; i++) {
            valuesIn = valuesIn + mapFilterFieldsValues[i] + (i < valueControl ? "," : "");
        }
        return valuesIn;
    }

    private ArrayNode menuParentsPerfiles(FilterPerfilMenu filter) throws Exception {
        String valuesIn = valuesByComas(0, filter.getIdPerfil());
        String table = "(select * from jofrantoba.seguridad.tg_perfil_menu where id_perfil in (" + valuesIn + ") and is_persistente=true) as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("distinct"));
        strFileds.append(share.append("menu.id as id,"));
        strFileds.append(share.append("menu.icono as icono,"));
        strFileds.append(share.append("menu.descripcion as descripcion,"));
        strFileds.append(share.append("menu.nivel as nivel,"));
        strFileds.append(share.append("menu.orden as orden,"));
        strFileds.append(share.append("menu.ruta as path,"));
        strFileds.append(share.append("menu.tipo as tipo,"));
        strFileds.append(share.append("menu.numero_submenu as numerosubmenu,"));
        strFileds.append(share.append("menu.id_cliente_sistema as idclientesistema"));
        String fields = strFileds.toString();
        String[] joinTables = new String[1];
        joinTables[0] = "right:jofrantoba.seguridad.tg_menu as menu:on:base.id_menu:menu.id";
        List<String> filters = new ArrayList();
        filters.add("isnull:menu.id_menu_padre");
        filters.add("=:menu.id_cliente_sistema:" + filter.getIdClienteSistema());
        filters.add("=:menu.is_persistente:true");
        filters.add("in:base.id_perfil:" + filter.getIdPerfil());
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"menu.orden:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private ArrayNode menuChildrens(FilterPerfilMenu filter) throws Exception {
        String table = "(select * from jofrantoba.seguridad.tg_perfil_menu where id_perfil=" + filter.getIdPerfil() + " and is_persistente=true) as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id_perfil_menu,"));
        strFileds.append(share.append("menu.id as id,"));
        strFileds.append(share.append("menu.descripcion as descripcion,"));
        strFileds.append(share.append("menu.nivel as nivel,"));
        strFileds.append(share.append("menu.orden as orden,"));
        strFileds.append(share.append("menu.ruta as path,"));
        strFileds.append(share.append("menu.tipo as tipo,"));
        strFileds.append(share.append("menu.numero_submenu as numerosubmenu,"));
        strFileds.append(share.append("menuPadre.id as idmenupadre,"));
        strFileds.append(share.append("case when not base.id is null then true else false end selected,"));
        strFileds.append(share.append(filter.getIdPerfil().toString())).append(" as idperfil,");
        strFileds.append(share.append("menu.id_cliente_sistema as idclientesistema"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "right:jofrantoba.seguridad.tg_menu as menu:on:base.id_menu:menu.id";
        joinTables[1] = "inner:jofrantoba.seguridad.tg_menu as menuPadre:on:menu.id_menu_padre:menuPadre.id";
        List<String> filters = new ArrayList();
        filters.add("=:menu.id_cliente_sistema:" + filter.getIdClienteSistema());
        filters.add("=:menu.is_persistente:true");
        if (filter.getIsOnlyPerfilMenu()) {
            filters.add("=:base.id_perfil:" + Long.parseLong(filter.getIdPerfil()));
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"menu.orden:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private ArrayNode menuChildrensPerfiles(FilterPerfilMenu filter) throws Exception {
        String valuesIn = valuesByComas(0, filter.getIdPerfil());
        String table = "(select * from jofrantoba.seguridad.tg_perfil_menu where id_perfil in (" + valuesIn + ") and is_persistente=true) as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("distinct"));
        strFileds.append(share.append("menu.id as id,"));
        strFileds.append(share.append("menu.icono as icono,"));
        strFileds.append(share.append("menu.descripcion as descripcion,"));
        strFileds.append(share.append("menu.nivel as nivel,"));
        strFileds.append(share.append("menu.orden as orden,"));
        strFileds.append(share.append("menu.ruta as path,"));
        strFileds.append(share.append("menu.tipo as tipo,"));
        strFileds.append(share.append("menu.numero_submenu as numerosubmenu,"));
        strFileds.append(share.append("menuPadre.id as idmenupadre,"));
        strFileds.append(share.append("menu.id_cliente_sistema as idclientesistema"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "right:jofrantoba.seguridad.tg_menu as menu:on:base.id_menu:menu.id";
        joinTables[1] = "inner:jofrantoba.seguridad.tg_menu as menuPadre:on:menu.id_menu_padre:menuPadre.id";
        List<String> filters = new ArrayList();
        filters.add("=:menu.id_cliente_sistema:" + filter.getIdClienteSistema());
        filters.add("=:menu.is_persistente:true");
        filters.add("in:base.id_perfil:" + filter.getIdPerfil());
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"menu.orden:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoPkTable.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoPkTable;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterPkTable;
import com.jofrantoba.examples.jofrantoba.entity.PkTable;
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
public class DaoPkTable extends AbstractJpaDaoV2<PkTable> implements InterDaoPkTable {
    
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
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoProvincia.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoProvincia;
import com.jofrantoba.examples.jofrantoba.entity.Provincia;
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
public class DaoProvincia extends AbstractJpaDaoV2<Provincia> implements InterDaoProvincia {
    
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoProvincia() {
        super();
        this.setClazz(Provincia.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<Provincia> listar(Long idDepartamento)throws Exception {
        String joinTable="inner:departamento";
        String[] mapFilterField={"=:departamento.id:"+idDepartamento,"=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigoProvincia as codigoProvincia,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento,"));
        strFields.append(share.append("departamento.codigoDepartamento as codigoDepartamento"));
        String fields=strFields.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Provincia bean=new Provincia();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Provincia> lista=(Collection<Provincia>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }
  
    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tm_provincia as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFields.append(share.append("base.codigo_departamento as codigoDepartamento,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields=strFields.toString();
        StringBuilder join=new StringBuilder();
        String[] joinTables=new String[1];
        joinTables[0]="inner:jofrantoba.catastro.tm_departamento as departamento:on:base.id_departamento:departamento.id";        
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tm_provincia as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.codigo_provincia as codigoProvincia,"));
        strFields.append(share.append("base.codigo_departamento as codigoDepartamento,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("departamento.id as idDepartamento,"));
        strFields.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields=strFields.toString();
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};
        String[] joinTable=new String[1];
        joinTable[0]="inner:jofrantoba.catastro.tm_departamento as departamento:on:base.id_departamento:departamento.id";        
        return this.allFieldsJoinPostgres(joinTable,table,fields,mapFilterField,mapOrder, "and");
    }    
    
    @Override
    public Long count(Long idDepartamento) throws Exception {
        String joinTable="inner:departamento";
        String[] mapFilterField={"=:departamento.id:"+idDepartamento,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoProyecto.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoProyecto;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterProyecto;
import com.jofrantoba.examples.jofrantoba.entity.Proyecto;
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
public class DaoProyecto extends AbstractJpaDaoV2<Proyecto> implements InterDaoProyecto {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoProyecto() {
        super();
        this.setClazz(Proyecto.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<Proyecto> listar(FilterProyecto filter) throws Exception {
        String[] joinTables = {"inner:distrito:fetch","inner:provincia:fetch","inner:departamento:fetch"};
        List<String> filters = new ArrayList();
        filters.add("=:base.isPersistente:true");
        if (filter.getIdDistrito() != null) {
            filters.add("=:distrito.id:" + filter.getIdDistrito());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder={"base.descripcion:asc"};
        return this.allFieldsJoinFilterAnd(joinTables,mapFilterField, mapOrder);
    }

    @Override
    public Collection<Proyecto> listar(FilterProyecto filter, int pageNumber, int pageSize) throws Exception {
        String joinTable = "inner:distrito:fetch";
        List<String> filters = new ArrayList();
        filters.add("=:base.isPersistente:true");
        if (filter.getIdDistrito() != null) {
            filters.add("=:distrito.id:" + filter.getIdDistrito());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder={"base.descripcion:asc"};
        return this.allFieldsJoinFilterAnd(joinTable,mapFilterField, mapOrder,pageNumber,pageSize);
    }
    
}

```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoRangoCuc.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoRangoCuc;
import com.jofrantoba.examples.jofrantoba.entity.RangoCuc;
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
public class DaoRangoCuc extends AbstractJpaDaoV2<RangoCuc> implements InterDaoRangoCuc {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoRangoCuc() {
        super();
        this.setClazz(RangoCuc.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<RangoCuc> listar(Long idDistrito)throws Exception {        
        String joinTable="inner:distrito";
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};
        String[] mapOrder={"distrito.descripcion:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.inicio as inicio,"));
        strFileds.append(share.append("base.fin as fin,"));
        strFileds.append(share.append("base.actual as actual,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                RangoCuc bean=new RangoCuc();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<RangoCuc> lista=(Collection<RangoCuc>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tm_rango_cuc as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.inicio as inicio,"));
        strFields.append(share.append("base.final as fin,"));
        strFields.append(share.append("base.actual as actual,"));
        strFields.append(share.append("distrito.id as idDistrito,"));
        strFields.append(share.append("distrito.descripcion as descripcionDistrito"));                
        String fields=strFields.toString();
        String[] joinTables=new String[1];
        joinTables[0]="inner:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";                
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tm_rango_cuc as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.inicio as inicio,"));
        strFields.append(share.append("base.final as fin,"));
        strFields.append(share.append("base.actual as actual,"));
        strFields.append(share.append("distrito.id as idDistrito,"));
        strFields.append(share.append("distrito.descripcion as descripcionDistrito"));                
        String fields=strFields.toString();
        String[] joinTables=new String[1];
        joinTables[0]="inner:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";                
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long count(Long idDistrito) throws Exception {
        String joinTable="inner:distrito";
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoRecapitulacionBc.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoRecapitulacionBc;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterRecapitulacionBc;
import com.jofrantoba.examples.jofrantoba.entity.RecapitulacionBc;
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
public class DaoRecapitulacionBc extends AbstractJpaDaoV2<RecapitulacionBc> implements InterDaoRecapitulacionBc {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoRecapitulacionBc() {
        super();
        this.setClazz(RecapitulacionBc.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterRecapitulacionBc filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterRecapitulacionBc filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterRecapitulacionBc filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }


    private Map<String, Object> buildQueryList(FilterRecapitulacionBc filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_recapitulacion_bc as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.porcentaje as numeroDocumento,"));
        strFileds.append(share.append("base.area_terreno_comun as areaTerrenoComun,"));
        strFileds.append(share.append("base.area_construida_comun as areaConstruidaComun,"));
        strFileds.append(share.append("base.area_otras_instalacion_comun as areaOtrasInstalacionComun,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("unidadCatastral.codigo_departamento as codigoDepartamento,"));
        strFileds.append(share.append("unidadCatastral.codigo_provincia as codigoProvincia,"));
        strFileds.append(share.append("unidadCatastral.codigo_distrito as codigoDistrito,"));
        strFileds.append(share.append("unidadCatastral.codigo_sector as codigoSector,"));
        strFileds.append(share.append("unidadCatastral.codigo_manzana as codigoManzana,"));
        strFileds.append(share.append("unidadCatastral.codigo_lote as codigoLote,"));
        strFileds.append(share.append("unidadCatastral.codigo_edificacion as codigoEdificacion,"));
        strFileds.append(share.append("unidadCatastral.codigo_entrada as codigoEntrada,"));
        strFileds.append(share.append("unidadCatastral.codigo_piso as codigoPiso,"));
        strFileds.append(share.append("unidadCatastral.codigo_unidad as codigoUnidad,"));
        strFileds.append(share.append("unidadCatastral.area_terreno_adquirida as areaTerrenoAdquirida,"));
        strFileds.append(share.append("unidadCatastral.area_terreno_verificada as areaTerrenoVerificada,"));
        strFileds.append(share.append("unidadCatastral.area_libre as areaLibre,"));
        strFileds.append(share.append("unidadCatastral.area_suma_const_ver as areaSumaConstVer,"));
        strFileds.append(share.append("unidadCatastral.area_ocupada as areaOcupada,"));
        strFileds.append(share.append("unidadCatastral.porc_bc_terreno_legal as porcBcTerrenoLegal,"));
        strFileds.append(share.append("unidadCatastral.porc_bc_terreno_fisico as porcBcTerrenoFisico,"));
        strFileds.append(share.append("unidadCatastral.porc_bc_construccion_legal as porcBcConstruccionLegal,"));
        strFileds.append(share.append("unidadCatastral.porc_bc_construccion_fisico as porcBcConstruccionFisico,"));        
        strFileds.append(share.append("unidadCatastralref.id as idUnidadCatastralBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_departamento as codigoDepartamentoBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_provincia as codigoProvinciaBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_distrito as codigoDistritoBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_sector as codigoSectorBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_manzana as codigoManzanaBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_lote as codigoLoteBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_edificacion as codigoEdificacionBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_entrada as codigoEntradaBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_piso as codigoPisoBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_unidad as codigoUnidadBc,"));
        strFileds.append(share.append("unidadCatastralref.area_terreno_adquirida as areaTerrenoAdquiridaBc,"));
        strFileds.append(share.append("unidadCatastralref.area_terreno_verificada as areaTerrenoVerificadaBc,"));
        strFileds.append(share.append("unidadCatastralref.area_libre as areaLibreBc,"));
        strFileds.append(share.append("unidadCatastralref.area_suma_const_ver as areaSumaConstVerBc,"));
        strFileds.append(share.append("unidadCatastralref.area_ocupada as areaOcupadaBc,"));
        strFileds.append(share.append("unidadCatastralref.porc_bc_terreno_legal as porcBcTerrenoLegalBc,"));
        strFileds.append(share.append("unidadCatastralref.porc_bc_terreno_fisico as porcBcTerrenoFisicoBc,"));
        strFileds.append(share.append("unidadCatastralref.porc_bc_construccion_legal as porcBcConstruccionLegalBc,"));
        strFileds.append(share.append("unidadCatastralref.porc_bc_construccion_fisico as porcBcConstruccionFisicoBc"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastralref:on:base.id_unidad_catastral_ref:unidadCatastralref.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdUnidadCatastralRef()!= null) {
            filters.add("=:unidadCatastralref.id:" + filter.getIdUnidadCatastralRef());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countUnidadCatastralRef(Long idUnidadCatastralRef) throws Exception {
        String joinTable = "inner:unidadCatastralRef";
        String[] mapFilterField = {"=:unidadCatastralRef.id:" + idUnidadCatastralRef, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoRecapitulacionEdificios.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoRecapitulacionEdificios;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterRecapitulacionEdificios;
import com.jofrantoba.examples.jofrantoba.entity.RecapitulacionEdificios;
import com.jofrantoba.examples.jofrantoba.entity.UnidadCatastral;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoRecapitulacionEdificios extends AbstractJpaDaoV2<RecapitulacionEdificios> implements InterDaoRecapitulacionEdificios {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoRecapitulacionEdificios() {
        super();
        this.setClazz(RecapitulacionEdificios.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterRecapitulacionEdificios filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterRecapitulacionEdificios filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterRecapitulacionEdificios filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }
    
    @Override
    public Collection<UnidadCatastral> udsCastSinAsociar() throws Exception{
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        
        sql.append(sharedUtil.append("select * from"));
        sql.append("catastro.tgv_unidad_catastral as uc");
        sql.append(sharedUtil.append("left join"));
        sql.append("catastro.tgv_recapitulacion_edificios as r_ed");
        sql.append(sharedUtil.append("on"));
        sql.append("uc.id = r_ed.id_unidad_catastral");
        sql.append(sharedUtil.append("where"));
        sql.append("r_ed.id is null");
        
        Query query = this.getCurrentSession().createQuery(sql.toString());
        return query.list();
    }

    private Map<String, Object> buildQueryList(FilterRecapitulacionEdificios filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_recapitulacion_edificios as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.porcentaje as numeroDocumento,"));
        strFileds.append(share.append("base.area_terreno_comun as areaTerrenoComun,"));
        strFileds.append(share.append("base.area_construida_comun as areaConstruidaComun,"));
        strFileds.append(share.append("base.area_otras_instalacion_comun as areaOtrasInstalacionComun,"));
        strFileds.append(share.append("unidadCatastral.id as idUnidadCatastral,"));
        strFileds.append(share.append("unidadCatastral.codigo_departamento as codigoDepartamento,"));
        strFileds.append(share.append("unidadCatastral.codigo_provincia as codigoProvincia,"));
        strFileds.append(share.append("unidadCatastral.codigo_distrito as codigoDistrito,"));
        strFileds.append(share.append("unidadCatastral.codigo_sector as codigoSector,"));
        strFileds.append(share.append("unidadCatastral.codigo_manzana as codigoManzana,"));
        strFileds.append(share.append("unidadCatastral.codigo_lote as codigoLote,"));
        strFileds.append(share.append("unidadCatastral.codigo_edificacion as codigoEdificacion,"));
        strFileds.append(share.append("unidadCatastral.codigo_entrada as codigoEntrada,"));
        strFileds.append(share.append("unidadCatastral.codigo_piso as codigoPiso,"));
        strFileds.append(share.append("unidadCatastral.codigo_unidad as codigoUnidad,"));
        strFileds.append(share.append("unidadCatastral.area_terreno_adquirida as areaTerrenoAdquirida,"));
        strFileds.append(share.append("unidadCatastral.area_terreno_verificada as areaTerrenoVerificada,"));
        strFileds.append(share.append("unidadCatastral.area_libre as areaLibre,"));
        strFileds.append(share.append("unidadCatastral.area_suma_const_ver as areaSumaConstVer,"));
        strFileds.append(share.append("unidadCatastral.area_ocupada as areaOcupada,"));
        strFileds.append(share.append("unidadCatastral.porc_bc_terreno_legal as porcBcTerrenoLegal,"));
        strFileds.append(share.append("unidadCatastral.porc_bc_terreno_fisico as porcBcTerrenoFisico,"));
        strFileds.append(share.append("unidadCatastral.porc_bc_construccion_legal as porcBcConstruccionLegal,"));
        strFileds.append(share.append("unidadCatastral.porc_bc_construccion_fisico as porcBcConstruccionFisico,"));        
        strFileds.append(share.append("unidadCatastralref.id as idBienComun,"));
        strFileds.append(share.append("unidadCatastralref.codigo_departamento as codigoDepartamentoBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_provincia as codigoProvinciaBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_distrito as codigoDistritoBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_sector as codigoSectorBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_manzana as codigoManzanaBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_lote as codigoLoteBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_edificacion as codigoEdificacionBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_entrada as codigoEntradaBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_piso as codigoPisoBc,"));
        strFileds.append(share.append("unidadCatastralref.codigo_unidad as codigoUnidadBc,"));
        strFileds.append(share.append("unidadCatastralref.area_terreno_adquirida as areaTerrenoAdquiridaBc,"));
        strFileds.append(share.append("unidadCatastralref.area_terreno_verificada as areaTerrenoVerificadaBc,"));
        strFileds.append(share.append("unidadCatastralref.area_libre as areaLibreBc,"));
        strFileds.append(share.append("unidadCatastralref.area_suma_const_ver as areaSumaConstVerBc,"));
        strFileds.append(share.append("unidadCatastralref.area_ocupada as areaOcupadaBc,"));
        strFileds.append(share.append("unidadCatastralref.porc_bc_terreno_legal as porcBcTerrenoLegalBc,"));
        strFileds.append(share.append("unidadCatastralref.porc_bc_terreno_fisico as porcBcTerrenoFisicoBc,"));
        strFileds.append(share.append("unidadCatastralref.porc_bc_construccion_legal as porcBcConstruccionLegalBc,"));
        strFileds.append(share.append("unidadCatastralref.porc_bc_construccion_fisico as porcBcConstruccionFisicoBc"));
        String fields = strFileds.toString();
        String[] joinTables = new String[2];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";
        joinTables[1] = "inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastralref:on:base.id_unidad_catastral_ref:unidadCatastralref.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdUnidadCatastral() != null) {
            filters.add("=:unidadCatastral.id:" + filter.getIdUnidadCatastral());
        }
        if (filter.getIdUnidadCatastralRef()!= null) {
            filters.add("=:unidadCatastralref.id:" + filter.getIdUnidadCatastralRef());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countUnidadCatastral(Long idUnidadCatastral) throws Exception {
        String joinTable = "inner:unidadCatastral";
        String[] mapFilterField = {"=:unidadCatastral.id:" + idUnidadCatastral, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countUnidadCatastralRef(Long idUnidadCatastralRef) throws Exception {
        String joinTable = "inner:unidadCatastralRef";
        String[] mapFilterField = {"=:unidadCatastralRef.id:" + idUnidadCatastralRef, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoReportFichaCatastraInd.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoCaracteristicasTitularidad;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoConstrucciones;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoDescripcionPredio;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoDocumentos;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoDomicilioFisTitularCatastral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoEvaluacionPredioCatastral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoIdentificacionTitularCatastral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoIncripcionPredioCatastral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoInformacionComplementaria;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoObrasComplementarias;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoObservaciones;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoServiciosPredio;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoUbicacionPredioCatastral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoUnidadCatastral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoZonificacion;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoLinderos;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoUbicacionPredCatastralHabUrb;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoPorcentajeBienComun;
import com.jofrantoba.examples.jofrantoba.entity.UnidadCatastral;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoReportFichaCatastraInd;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoReportFichaCatastraInd extends AbstractJpaDaoV2<UnidadCatastral> implements InterDaoReportFichaCatastraInd {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoReportFichaCatastraInd() {
        super();
        this.setClazz(UnidadCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public DtoUnidadCatastral dsMain(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_departamento codigoDepartamento,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_provincia codigoProvincia,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_distrito codigoDistrito,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_sector codigoSector,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_manzana codigoManzana,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_lote codigoLote,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_edificacion codigoEdificacion,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_entrada codigoEntrada,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_piso codigoPiso,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_unidad codigoUnidad,"));
        sql.append(sharedUtil.append("unidadCatastral.dc dc,"));
        sql.append(sharedUtil.append("(case when lotecatastral.cuc is not null and unidadCatastral.cuc_uc is not null then lotecatastral.cuc || '-' ||   lpad(CAST(unidadCatastral.cuc_uc AS VARCHAR), 4, '0') else "));
        sql.append(sharedUtil.append("(case when lotecatastral.cuc  is not null  and unidadCatastral.cuc_uc is null then lotecatastral.cuc  end) end) cuc,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_contribuyente_rentas codigoContribuyenteRentas,"));
        sql.append(sharedUtil.append("unidadCatastral.codigo_predial_rentas codigoPredialRentas"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_lote_catastral loteCatastral on loteCatastral.id = unidadCatastral.id_lote_catastral and loteCatastral.is_persistente = true"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id = ?" ));
        DtoUnidadCatastral dto = new DtoUnidadCatastral();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        dto.setCuc(rs.getString("cuc"));
                        dto.setCodigoDepartamento(rs.getString("codigoDepartamento"));
                        dto.setCodigoProvincia(rs.getString("codigoProvincia"));
                        dto.setCodigoDistrito(rs.getString("codigoDistrito"));
                        dto.setCodigoSector(rs.getString("codigoSector"));
                        dto.setCodigoManzana(rs.getString("codigoManzana"));
                        dto.setCodigoLote(rs.getString("codigoLote"));
                        dto.setCodigoEdificacion(rs.getString("codigoEdificacion"));
                        dto.setCodigoEntrada(rs.getString("codigoEntrada"));
                        dto.setCodigoPiso(rs.getString("codigoPiso"));
                        dto.setCodigoUnidad(rs.getString("codigoUnidad"));
                        dto.setDc(rs.getLong("dc"));
                        dto.setCodigoContribuyenteRentas(rs.getString("codigoContribuyenteRentas"));
                        dto.setCodigoPredialRentas(rs.getString("codigoPredialRentas"));
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return dto;
    }

    @Override
    public List<DtoUbicacionPredioCatastral> dsUbicacionPredioCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct via.codigo_via codigoVia,"));
        sql.append(sharedUtil.append("tipoVia.descripcion tipoDeVia,"));
        sql.append(sharedUtil.append("via.descripcion nombreDeVia,"));
        sql.append(sharedUtil.append("tipoPuerta.descripcion tipoDePuerta ,"));
        sql.append(sharedUtil.append("(case when TRIM(numeracion.numero)!= '' and TRIM(numeracion.letra)!= '' then  TRIM(numeracion.numero)||'-'|| TRIM(numeracion.letra) else (case when TRIM(numeracion.numero)!= '' and TRIM(numeracion.letra) = '' then  TRIM(numeracion.numero) else (case when TRIM(numeracion.numero) = '' and TRIM(numeracion.letra) != '' then  TRIM(numeracion.letra) else'' end )end)end ) nroMunicipal,"));
        sql.append(sharedUtil.append("tipoCondicionNumeracion.descripcion condNumer,"));
        sql.append(sharedUtil.append("tipoEdificacion.descripcion tipoDeEdificacion,"));
        sql.append(sharedUtil.append("tipoInterior.descripcion tipoDeInterior,"));
        sql.append(sharedUtil.append("(case when TRIM(interior.interior)!= '' and TRIM(interior.letra_interior)!= '' then  TRIM(interior.interior)||'-'|| TRIM(interior.letra_interior) else (case when TRIM(interior.interior)!= '' and TRIM(interior.letra_interior) = '' then  TRIM(interior.interior) else (case when TRIM(interior.interior) = '' and TRIM(interior.letra_interior) != '' then  TRIM(interior.letra_interior) else'' end )end)end ) nroInterior"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_interior interior on interior.id_unidad_catastral = unidadCatastral.id and interior.is_persistente=true"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_lote_catastral loteCatastral on loteCatastral.id = unidadCatastral.id_lote_catastral and loteCatastral.is_persistente=true"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_numeracion numeracion on numeracion.id = interior.id_numeracion and numeracion.is_persistente=true"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tg_manzana_via manzanaVia on manzanaVia.id= numeracion.id_manzana_via and manzanaVia.is_persistente=true"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tg_via_cuadra viacuadra on viacuadra.id = manzanaVia.id_via_cuadra and viacuadra.is_persistente=true"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tm_via via on via.id = viacuadra.id_via and via.is_persistente=true"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 1 and is_persistente = true ) tipoVia on  tipoVia.id = via.id_tipo_via"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 2 and is_persistente = true) tipoPuerta on tipoPuerta.id = numeracion.id_tipo_puerta"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 17 and is_persistente = true) tipoCondicionNumeracion on tipoCondicionNumeracion.id = numeracion.id_condicion_numeracion"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 18 and is_persistente = true) tipoEdificacion on tipoEdificacion.id = interior.id_tipo_edificacion"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 19 and is_persistente = true) tipoInterior on tipoInterior.id = interior.id_tipo_interior"));
        sql.append(sharedUtil.append("where  unidadCatastral.is_persistente = true and unidadCatastral.id = ?"));
        /*sql.append(sharedUtil.append("group by via.codigo_via, tipoVia.descripcion , via.descripcion ,tipoPuerta.descripcion ,"));
        sql.append(sharedUtil.append("(case when TRIM(numeracion.numero)!= '' and TRIM(numeracion.letra)!= '' then  TRIM(numeracion.numero)||'-'|| TRIM(numeracion.letra) else (case when TRIM(numeracion.numero)!= '' and TRIM(numeracion.letra) = '' then  TRIM(numeracion.numero) else (case when TRIM(numeracion.numero) = '' and TRIM(numeracion.letra) != '' then  TRIM(numeracion.letra) else'' end )end)end ) , tipoCondicionNumeracion.descripcion,"));
        sql.append(sharedUtil.append("(case when TRIM(interior.interior)!= '' and TRIM(interior.letra_interior)!= '' then  TRIM(interior.interior)||'-'|| TRIM(interior.letra_interior) else (case when TRIM(interior.interior)!= '' and TRIM(interior.letra_interior) = '' then  TRIM(interior.interior) else (case when TRIM(interior.interior) = '' and TRIM(interior.letra_interior) != '' then  TRIM(interior.letra_interior) else'' end )end)end ),"));
        sql.append(sharedUtil.append("tipoEdificacion.descripcion , tipoInterior.descripcion ;"));*/
        //System.out.println("dsUbicacionPredioCatastral: " + sql.toString());
        List<DtoUbicacionPredioCatastral> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoUbicacionPredioCatastral dto = new DtoUbicacionPredioCatastral();
                        dto.setCodigoVia(rs.getString("codigoVia"));
                        dto.setTipoDeVia(rs.getString("tipoDeVia"));
                        dto.setNombreDeVia(rs.getString("nombreDeVia"));
                        dto.setTipoDePuerta(rs.getString("tipoDePuerta"));
                        dto.setNroMunicipal(rs.getString("nroMunicipal"));
                        dto.setCondNumer(rs.getString("condNumer"));
                        dto.setTipoDeEdificacion(rs.getString("tipoDeEdificacion"));
                        dto.setTipoDeInterior(rs.getString("tipoDeInterior"));
                        dto.setNroInterior(rs.getString("nroInterior"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoUbicacionPredCatastralHabUrb> dsUbicacionPredCatastralHabUrb(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct habilitacionUrbana.codigo codigoHu,"));
        sql.append(sharedUtil.append("habilitacionUrbana.descripcion nombreHabilitacionUrbana,"));
        sql.append(sharedUtil.append("loteHabilitacionUrbana.zona_sector_etapa zonaSectorEtapa,"));
        sql.append(sharedUtil.append("loteHabilitacionUrbana.lote_urbano lote,"));
        sql.append(sharedUtil.append("loteHabilitacionUrbana.manzana_urbana manzana,"));
        sql.append(sharedUtil.append("loteHabilitacionUrbana.sub_lote_urbano subLote"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_lote_catastral loteCatastral on loteCatastral.id = unidadCatastral.id_lote_catastral and loteCatastral.is_persistente=true"));
        sql.append(sharedUtil.append("left  join jofrantoba.catastro.tgv_lote_habilitacion_urbana loteHabilitacionUrbana  on loteHabilitacionUrbana.id_lote_catastral = loteCatastral.id and loteHabilitacionUrbana.is_persistente=true"));
        sql.append(sharedUtil.append("left  join jofrantoba.catastro.tm_habilitacion_urbana habilitacionUrbana on habilitacionUrbana.id  =loteHabilitacionUrbana.id_habilitacion_urbana and habilitacionUrbana.is_persistente=true"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id = ?"));
        //sql.append(sharedUtil.append("group by habilitacionUrbana.codigo , habilitacionUrbana.descripcion ,loteHabilitacionUrbana.zona_sector_etapa,"));
        //sql.append(sharedUtil.append("loteHabilitacionUrbana.lote_urbano , loteHabilitacionUrbana.manzana_urbana , loteHabilitacionUrbana.sub_lote_urbano"));
        List<DtoUbicacionPredCatastralHabUrb> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoUbicacionPredCatastralHabUrb dto = new DtoUbicacionPredCatastralHabUrb();
                        dto.setCodigoHu(rs.getString("codigoHu"));
                        dto.setNombreHabilitacionUrbana(rs.getString("nombreHabilitacionUrbana"));
                        dto.setZonaSectorEtapa(rs.getString("zonaSectorEtapa"));
                        dto.setManzana(rs.getString("manzana"));
                        dto.setLote(rs.getString("lote"));
                        dto.setSubLote(rs.getString("subLote"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoIdentificacionTitularCatastral> dsIdentificacionTitularCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select unidadCatastral.id,"));
        sql.append(sharedUtil.append("tipoTitular.descripcion tipo_titular,"));
        sql.append(sharedUtil.append("estadoCivil.descripcion estado_civil,"));
        sql.append(sharedUtil.append("tipoDocumento.descripcion tipo_doc_identidad,"));
        sql.append(sharedUtil.append("titular.id_tipo_titular  id_tipo_titular,"));
        sql.append(sharedUtil.append("titular.numero_documento  nro_doc,"));
        sql.append(sharedUtil.append("titular.nombre_ruc nombres,"));
        sql.append(sharedUtil.append("titular.apellido_paterno apellido_paterno,"));
        sql.append(sharedUtil.append("titular.apellido_materno apellido_materno,"));
        sql.append(sharedUtil.append("titular.id_tipo_persona_juridica persona_juridica,"));
        sql.append(sharedUtil.append("tipoPersonaJuridica.descripcion tipo_persona_juridica "));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_titularidad titularidad on unidadCatastral.id=titularidad.id_unidad_catastral and titularidad.is_persistente = true"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_titular titular on titularidad.id=titular.id_titularidad  and titular.is_persistente = true"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 76 and is_persistente = true ) tipoTitular on  titular.id_tipo_titular=tipoTitular.id "));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 77 and is_persistente = true ) estadoCivil on  titular.id_estado_civil=estadoCivil.id "));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 78 and is_persistente = true ) tipoDocumento on titular.id_tipo_documento=tipoDocumento.id "));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 94 and is_persistente = true ) tipoPersonaJuridica on titular.id_tipo_persona_juridica=tipoPersonaJuridica.id "));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true "));
        sql.append(sharedUtil.append("and  unidadCatastral.id = ? "));
        sql.append(sharedUtil.append("order by titular.id asc limit 1 offset 0"));
        List<DtoIdentificacionTitularCatastral> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoIdentificacionTitularCatastral dto = new DtoIdentificacionTitularCatastral();
                        dto.setTipoDeTitular(rs.getString("tipo_titular"));
                        dto.setPersonaJuridica(rs.getString("tipo_persona_juridica"));
                        Long idTipoTitular = rs.getLong("id_tipo_titular");
                        if (idTipoTitular == 80l) {
                            dto.setNroRuc(rs.getString("nro_doc"));
                            dto.setRazonSocial(rs.getString("nombres"));
                        } else {
                            dto.setTipoDocIdentidad(rs.getString("tipo_doc_identidad"));
                            dto.setNombres(rs.getString("nombres"));
                            dto.setNroDoc(rs.getString("nro_doc"));
                            dto.setApellidoPaterno(rs.getString("apellido_paterno"));
                            dto.setApellidoMaterno(rs.getString("apellido_materno"));
                            dto.setEstadoCivil(rs.getString("estado_civil"));
                        }
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoDomicilioFisTitularCatastral> dsDomicilioFisTitularCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append("tipoUbicacion.descripcion ubicacion,"));
        sql.append(sharedUtil.append("departamento.descripcion departamento,"));
        sql.append(sharedUtil.append("provincia.descripcion provincia,"));
        sql.append(sharedUtil.append("distrito.descripcion distrito,"));
        sql.append(sharedUtil.append("titular.codigo_via codigoVia,"));
        sql.append(sharedUtil.append("tipoVia.descripcion tipoVia,"));
        sql.append(sharedUtil.append("titular.nombre_via nombreDeVia,"));
        sql.append(sharedUtil.append("titular.numero_municipal nroMunicipal,"));
        sql.append(sharedUtil.append("titular.numero_interior nroInterior,"));
        sql.append(sharedUtil.append("titular.codigo_hu codigoHu,"));
        sql.append(sharedUtil.append("titular.nombre_hu nombreHabitilitacionUrbana,"));
        sql.append(sharedUtil.append("titular.zona_sector_etapa zonaSectorEtapa,"));
        sql.append(sharedUtil.append("titular.manzana manzana,"));
        sql.append(sharedUtil.append("titular.lote lote,"));
        sql.append(sharedUtil.append("titular.sub_lote subLote,"));
        sql.append(sharedUtil.append("titular.telefono telefono,"));
        sql.append(sharedUtil.append("titular.anexo anexo,"));
        sql.append(sharedUtil.append("titular.correo_electronico correoElectronico"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_titularidad titularidad  on unidadCatastral.id  = titularidad.id_unidad_catastral and titularidad.is_persistente = true"));
        sql.append(sharedUtil.append("left  join jofrantoba.catastro.tgv_titular titular  on titularidad.id  =  titular.id_titularidad  and titular.is_persistente = true"));
        sql.append(sharedUtil.append("left  join jofrantoba.catastro.tm_departamento departamento  on  titular.id_departamento = departamento.id and departamento.is_persistente = true"));
        sql.append(sharedUtil.append("left  join jofrantoba.catastro.tm_provincia provincia on  titular.id_provincia = provincia.id and provincia.is_persistente = true"));
        sql.append(sharedUtil.append("left  join jofrantoba.catastro.tm_distrito distrito on titular.id_distrito = distrito.id and distrito.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 95 and is_persistente = true ) tipoUbicacion on  titular.id_tipo_ubicacion = tipoUbicacion.id"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 1 and is_persistente = true ) tipoVia on titular.id_tipo_via = tipoVia.id"));
        sql.append(sharedUtil.append("where unidadcatastral.is_persistente = true  and unidadCatastral.id = ? and"));
        sql.append(sharedUtil.append("titular.id="));
        sql.append(sharedUtil.append("(select  min(titular.id) from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_titularidad titularidad on titularidad.id_unidad_catastral = unidadCatastral.id and titularidad.is_persistente = true"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_titular titular on titular.id_titularidad = titularidad.id and titular.is_persistente = true"));
        sql.append(sharedUtil.append("where unidadCatastral.id=? and unidadCatastral.is_persistente = true)"));
        List<DtoDomicilioFisTitularCatastral> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    ps.setLong(2, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoDomicilioFisTitularCatastral dto = new DtoDomicilioFisTitularCatastral();
                        dto.setUbicacion(rs.getString("ubicacion"));
                        dto.setDepartamento(rs.getString("departamento"));
                        dto.setProvincia(rs.getString("provincia"));
                        dto.setDistrito(rs.getString("distrito"));
                        dto.setCodigoVia(rs.getString("codigoVia"));
                        dto.setTipoDeVia(rs.getString("tipoVia"));
                        dto.setNombreDeVia(rs.getString("nombreDeVia"));
                        dto.setNroMunicipal(rs.getString("nroMunicipal"));
                        dto.setNroInterior(rs.getString("nroInterior"));
                        dto.setCodigoHu(rs.getString("codigoHu"));
                        dto.setNombreHabitilitacionUrbana(rs.getString("nombreHabitilitacionUrbana"));
                        dto.setZonaSectorEtapa(rs.getString("zonaSectorEtapa"));
                        dto.setManzana(rs.getString("manzana"));
                        dto.setLote(rs.getString("lote"));
                        dto.setSubLote(rs.getString("subLote"));
                        dto.setTelefono(rs.getString("telefono"));
                        dto.setAnexo(rs.getString("anexo"));
                        dto.setCorreoElectronico(rs.getString("correoElectronico"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoCaracteristicasTitularidad> dsCaractTitularidadCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select condicionTitularidad.descripcion condicion_titularidad,"));
        sql.append(sharedUtil.append("formaAdquisicion.descripcion forma_adquisicion ,"));
        sql.append(sharedUtil.append("titularidad.fecha_adquisicion fechaAdquision "));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral "));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_titularidad titularidad  on titularidad.id_unidad_catastral = unidadCatastral.id"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_titular titular on titular.id_titularidad  = titularidad.id"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 96 and is_persistente = true ) condicionTitularidad on  titularidad.id_condicion_titularidad = condicionTitularidad.id "));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 111 and is_persistente = true ) formaAdquisicion on titularidad.id_forma_adquisicion =  formaAdquisicion.id "));
        sql.append(sharedUtil.append("where unidadcatastral.is_persistente = true and unidadCatastral.id = ?"));
        sql.append(sharedUtil.append("group by unidadCatastral.id ,titularidad.id,titularidad.id_condicion_titularidad ,condicionTitularidad.descripcion ,	titularidad.id_forma_adquisicion,formaAdquisicion.descripcion ,titularidad.fecha_adquisicion"));
        List<DtoCaracteristicasTitularidad> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoCaracteristicasTitularidad dto = new DtoCaracteristicasTitularidad();
                        dto.setCondicionTitular(rs.getString("condicion_titularidad"));
                        dto.setFormaAdquision(rs.getString("forma_adquisicion"));
                        dto.setFechaAdquision(rs.getDate("fechaAdquision"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoDescripcionPredio> dsDescripcionPredioCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct"));
        sql.append(sharedUtil.append("clasificacionPredio.descripcion clasificacionPredio,"));
        sql.append(sharedUtil.append("clasificacionPredioOtro.descripcion clasificacionPredioOtro,"));
        sql.append(sharedUtil.append("predioEn.descripcion predioCatastral,"));
        sql.append(sharedUtil.append("unidadcatastral.predio_en_otro predioCatastralOtro,"));
        sql.append(sharedUtil.append("usoEspecifico.codigo codigoUso,"));
        sql.append(sharedUtil.append("usoEspecifico.descripcion usoPredioCastral,"));
        sql.append(sharedUtil.append("unidadCatastral.area_terreno_adquirida areaTerrenoAdquirida,"));
        sql.append(sharedUtil.append("unidadCatastral.area_terreno_verificada areaTerrenoVerficada"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_lote_catastral loteCastral  on loteCastral.id = unidadCatastral.id_lote_catastral"));
        sql.append(sharedUtil.append("left  join jofrantoba.catastro.tm_uso_especifico usoEspecifico  on unidadCatastral.id_uso_especifico = usoEspecifico.id"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 112 and is_persistente = true) clasificacionPredio on unidadCatastral.id_clasificacion_predio = clasificacionPredio.id"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 135 and is_persistente = true) predioEn on unidadCatastral.id_predio_en =  predioEn.id"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 134 and is_persistente = true) clasificacionPredioOtro on unidadCatastral.id_clasificacion_predio = clasificacionPredioOtro.id"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id= ?"));
        /*sql.append(sharedUtil.append("group by clasificacionPredio.descripcion,"));
        sql.append(sharedUtil.append("clasificacionPredioOtro.descripcion,"));
        sql.append(sharedUtil.append("predioEn.descripcion,"));
        sql.append(sharedUtil.append("unidadcatastral.predio_en_otro,"));
        sql.append(sharedUtil.append("usoEspecifico.codigo,"));
        sql.append(sharedUtil.append("usoEspecifico.descripcion,"));
        sql.append(sharedUtil.append("unidadCatastral.area_terreno_adquirida,"));
        sql.append(sharedUtil.append("unidadCatastral.area_terreno_verificada"));*/
        List<DtoDescripcionPredio> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoDescripcionPredio dto = new DtoDescripcionPredio();
                        dto.setClasificacionPredio(rs.getString("clasificacionPredio"));
                        dto.setClasificacionPredioOtro(rs.getString("clasificacionPredioOtro"));
                        dto.setPredioCatastral(rs.getString("predioCatastral"));
                        dto.setPredioCatastralOtro(rs.getString("predioCatastralOtro"));
                        dto.setCodigoUso(rs.getString("codigoUso"));
                        dto.setUsoPredioCastral(rs.getString("usoPredioCastral"));
                        dto.setAreaTerrenoAdquirida(rs.getDouble("areaTerrenoAdquirida"));
                        dto.setAreaTerrenoVerficada(rs.getDouble("areaTerrenoVerficada"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoZonificacion> dsZonificacion(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct"));
        sql.append(sharedUtil.append("zonificacion.descripcion zonificacion"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_lote_catastral loteCastral  on loteCastral.id = unidadCatastral.id_lote_catastral and loteCastral.is_persistente = true"));
        sql.append(sharedUtil.append("left  join jofrantoba.catastro.tgv_lote_zonificacion loteZonificacion  on loteCastral.id = loteZonificacion.id_lote_catastral and loteZonificacion.is_persistente = true"));
        sql.append(sharedUtil.append("left  join jofrantoba.catastro.tm_zonificacion zonificacion  on loteZonificacion.id_zonificacion = zonificacion.id and zonificacion.is_persistente = true"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id= ?"));
        List<DtoZonificacion> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoZonificacion dto = new DtoZonificacion();
                        dto.setZonificacion(rs.getString("zonificacion"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoLinderos> dsLinderos(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct"));
        sql.append(sharedUtil.append("tiposLinderos.descripcion tipoLinderos,"));
        sql.append(sharedUtil.append("linderos.medida_campo medidaCampo,"));
        sql.append(sharedUtil.append("linderos.colindancia colindanciaCampo"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_lote_catastral loteCastral  on loteCastral.id = unidadCatastral.id_lote_catastral and loteCastral.is_persistente = true"));
        sql.append(sharedUtil.append("left  join jofrantoba.catastro.tgv_linderos linderos on loteCastral.id = linderos.id_lote_catastral and linderos.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 224 and is_persistente = true) tiposLinderos on linderos.id_tipo_lindero = tiposLinderos.id"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id= ? order by tiposLinderos.descripcion"));
        List<DtoLinderos> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoLinderos dto = new DtoLinderos();
                        dto.setMedidaCampo(rs.getDouble("medidaCampo"));
                        dto.setColindanciaCampo(rs.getString("colindanciaCampo"));
                        dto.setTipoLinderos(rs.getString("tipoLinderos"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoServiciosPredio> dsServiciosPredioCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_luz = '1' then 'SI' else 'NO' end) luz,"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_agua ='1'then 'SI' else 'NO' end) agua,"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_telefono ='1' then 'SI' else 'NO' end) telefono,"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_desague = '1'then 'SI' else 'NO' end) desague,"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_gas = '1' then 'SI' else 'NO' end) gas,"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_internet ='1' then 'SI' else 'NO' end) internet,"));
        sql.append(sharedUtil.append("(case when unidadCatastral.is_cable = '1' then 'SI' else 'NO' end) conexionTv"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and  unidadCatastral.id= ?"));
        List<DtoServiciosPredio> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoServiciosPredio dto = new DtoServiciosPredio();
                        dto.setLuz(rs.getString("luz"));
                        dto.setAgua(rs.getString("agua"));
                        dto.setTelefono(rs.getString("telefono"));
                        dto.setDesague(rs.getString("desague"));
                        dto.setGas(rs.getString("gas"));
                        dto.setInternet(rs.getString("internet"));
                        dto.setConexionTv(rs.getString("conexionTv"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoConstrucciones> dsConstruccionesCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select nivel.descripcion nroPisoSotano,"));
        sql.append(sharedUtil.append("construcciones.id_mes ,"));
        sql.append(sharedUtil.append("mes.descripcion  mes,"));
        sql.append(sharedUtil.append("construcciones.anio anio,"));
        sql.append(sharedUtil.append("materialEstructural.descripcion mep,"));
        sql.append(sharedUtil.append("estadoConservacion.descripcion ecs,"));
        sql.append(sharedUtil.append("estadoConstruccion.descripcion ecc,"));
        sql.append(sharedUtil.append("construcciones.muros murosColumnas,"));
        sql.append(sharedUtil.append("construcciones.techos techos,"));
        sql.append(sharedUtil.append("construcciones.pisos pisos,"));
        sql.append(sharedUtil.append("construcciones.puertas_ventanas puertasVentanas,"));
        sql.append(sharedUtil.append("construcciones.revestimiento revest,"));
        sql.append(sharedUtil.append("construcciones.banios banios,"));
        sql.append(sharedUtil.append("construcciones.instalaciones_electricas instElectricas,"));
        sql.append(sharedUtil.append("construcciones.area_verificada areaVerificada,"));
        sql.append(sharedUtil.append("uca.descripcion uca"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_construcciones construcciones on construcciones.id_unidad_catastral = unidadCatastral.id and construcciones.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 248 and is_persistente = true) nivel on  construcciones.id_nivel = nivel.id"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 249 and is_persistente = true) mes on  construcciones.id_mes = mes.id"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 167 and is_persistente = true) materialEstructural on construcciones.id_material_estructural =materialEstructural.id"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 168 and is_persistente = true) estadoConservacion on construcciones.id_estado_conservacion =  estadoConservacion.id"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 169 and is_persistente = true) estadoConstruccion on construcciones.id_estado_construccion =  estadoConstruccion.id"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 170 and is_persistente = true) uca on construcciones.id_uca = uca.id"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id= ? order by construcciones.id_nivel asc"));
        List<DtoConstrucciones> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoConstrucciones dto = new DtoConstrucciones();
                        dto.setNroPisoSotano(rs.getString("nroPisoSotano"));
                        dto.setMes(rs.getString("mes"));
                        dto.setAnio(rs.getLong("anio"));
                        dto.setMep(rs.getString("mep"));
                        dto.setEcs(rs.getString("ecs"));
                        dto.setEcc(rs.getString("ecc"));
                        dto.setMurosColumnas(rs.getString("murosColumnas"));
                        dto.setTechos(rs.getString("techos"));
                        dto.setPisos(rs.getString("pisos"));
                        dto.setPuertasVentanas(rs.getString("puertasVentanas"));
                        dto.setRevest(rs.getString("revest"));
                        dto.setBanios(rs.getString("banios"));
                        dto.setInstElectricas(rs.getString("instElectricas"));
                        dto.setAreaVerificada(rs.getDouble("areaVerificada"));
                        dto.setUca(rs.getString("uca"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }
    
    @Override
    public List<DtoPorcentajeBienComun> dsPorcentajeBienComun(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select unidadCatastral.porc_bc_terreno_legal terrenoLegal,"));
        sql.append(sharedUtil.append("unidadCatastral.porc_bc_terreno_fisico terrenoFisico,"));
        sql.append(sharedUtil.append("unidadCatastral.porc_bc_construccion_legal construccionesLegal,"));
        sql.append(sharedUtil.append("unidadCatastral.porc_bc_construccion_fisico construccionesFisico"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id = ?"));
        List<DtoPorcentajeBienComun> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoPorcentajeBienComun dto = new DtoPorcentajeBienComun();
                        dto.setTerrenoLegal(rs.getString("terrenoLegal"));
                        dto.setTerrenoFisico(rs.getString("terrenoFisico"));
                        dto.setConstruccionesLegal(rs.getString("construccionesLegal"));
                        dto.setConstruccionesFisico(rs.getString("construccionesFisico"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoObrasComplementarias> dsObrasComplementariasCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct tipoOtrasInstalaciones.codigo codigo,"));
        sql.append(sharedUtil.append("tipoOtrasInstalaciones.descripcion descripcion,"));
        sql.append(sharedUtil.append("mes.descripcion mes,"));
        sql.append(sharedUtil.append("otrasInstalaciones.anio anio,"));
        sql.append(sharedUtil.append("materialEstructural.descripcion mep,"));
        sql.append(sharedUtil.append("estadoConservacion.descripcion esc,"));
        sql.append(sharedUtil.append("estadoConstruccion.descripcion ecc,"));
        sql.append(sharedUtil.append("otrasInstalaciones.producto_total productoTotal ,"));
        sql.append(sharedUtil.append("tipoOtrasInstalaciones.unidad_medida unidadMedida,"));
        sql.append(sharedUtil.append("uca.descripcion uca"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_otras_instalaciones otrasInstalaciones on otrasInstalaciones.id_unidad_catastral = unidadCatastral.id and otrasInstalaciones.is_persistente = true"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tm_tipo_otras_instalaciones tipoOtrasInstalaciones on tipoOtrasInstalaciones.id  = otrasInstalaciones.id_tipo_otras_instalaciones and tipoOtrasInstalaciones.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 249 and is_persistente = true) mes on mes.id = otrasInstalaciones.id_mes"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 167 and is_persistente = true) materialEstructural on materialEstructural.id = otrasInstalaciones.id_material_estructural"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 168 and is_persistente = true) estadoConservacion on estadoConservacion.id = otrasInstalaciones.id_estado_conservacion"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 169 and is_persistente = true) estadoConstruccion on estadoConstruccion.id = otrasInstalaciones.id_estado_construccion"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 170 and is_persistente = true) uca on uca.id = otrasInstalaciones.id_uca"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id= ?"));
        List<DtoObrasComplementarias> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoObrasComplementarias dto = new DtoObrasComplementarias();
                        dto.setCodigo(rs.getString("codigo"));
                        dto.setDescripcion(rs.getString("descripcion"));
                        dto.setMes(rs.getString("mes"));
                        dto.setAnio(rs.getLong("anio"));
                        dto.setMep(rs.getString("mep"));
                        dto.setEcs(rs.getString("esc"));
                        dto.setEcc(rs.getString("ecc"));
                        dto.setProductoTotal(rs.getDouble("productoTotal"));
                        dto.setUnidadMedida(rs.getString("unidadMedida"));
                        dto.setUca(rs.getString("uca"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoDocumentos> dsDocumentosCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select tipoDocumento.descripcion tipoDocumento,"));
        sql.append(sharedUtil.append("documentos.numero_documento nroDocumento,"));
        sql.append(sharedUtil.append("documentos.fecha_documento fecha,"));
        sql.append(sharedUtil.append("documentos.area_autorizada areaAutorizada"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_documentos documentos on documentos.id_unidad_catastral = unidadCatastral.id  and documentos.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 171 and is_persistente = true) tipoDocumento on documentos.id_tipo_documento = tipoDocumento.id"));
        sql.append(sharedUtil.append("where unidadCatastral.is_persistente = true and unidadCatastral.id= ?"));
        List<DtoDocumentos> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoDocumentos dto = new DtoDocumentos();
                        dto.setTipoDocumento(rs.getString("tipoDocumento"));
                        dto.setNroDocumento(rs.getString("nroDocumento"));
                        dto.setFecha(rs.getDate("fecha"));
                        dto.setAreaAutorizada(rs.getDouble("areaAutorizada"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoIncripcionPredioCatastral> dsInscripcionPredioCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select tipoPartidaRegistral.descripcion tipoPartidadRegistral,"));
        sql.append(sharedUtil.append("inscripcioncatastral.numero numero ,"));
        sql.append(sharedUtil.append("inscripcioncatastral.fojas fojas,"));
        sql.append(sharedUtil.append("inscripcioncatastral.asiento asiento,"));
        sql.append(sharedUtil.append("inscripcioncatastral.fecha_inscripcion fechaInscripcionPredio,"));
        sql.append(sharedUtil.append("declaratoriafabrica.descripcion declaratoriaFabrica,"));
        sql.append(sharedUtil.append("inscripcioncatastral.inscripcion_fabrica asInsFabrica,"));
        sql.append(sharedUtil.append("inscripcioncatastral.fecha_inscripcion_fabrica  fechaInscripcionFabrica"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_inscripcion_catastral inscripcionCatastral on inscripcionCatastral.id_unidad_catastral = unidadcatastral.id and inscripcionCatastral.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 172 and is_persistente = true) tipoPartidaRegistral on  inscripcioncatastral.id_tipo_partida_registral = tipoPartidaRegistral.id"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 173 and is_persistente = true) declaratoriafabrica on inscripcioncatastral.id_declaratoria_fabrica = declaratoriafabrica.id"));
        sql.append(sharedUtil.append("where unidadcatastral.is_persistente = true and unidadcatastral.id  = ?"));
        List<DtoIncripcionPredioCatastral> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoIncripcionPredioCatastral dto = new DtoIncripcionPredioCatastral();
                        dto.setTipoPartidadRegistral(rs.getString("tipoPartidadRegistral"));
                        dto.setNumero(rs.getString("numero"));
                        dto.setFojas(rs.getString("fojas"));
                        dto.setAsiento(rs.getString("asiento"));
                        dto.setFechaInscripcionPredio(rs.getDate("fechaInscripcionPredio"));
                        dto.setDeclaratoriaFabrica(rs.getString("declaratoriaFabrica"));
                        dto.setAsInsFabrica(rs.getString("asInsFabrica"));
                        dto.setFechaInscripcionFabrica(rs.getDate("fechaInscripcionFabrica"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoInformacionComplementaria> dsInfoComplementariaCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select condicionDeclarante.descripcion condicionDeclarante,"));
        sql.append(sharedUtil.append("tipoDocumento.descripcion td,"));
        sql.append(sharedUtil.append("litigantes.numero_documento nroDocumento,"));
        sql.append(sharedUtil.append("litigantes.nombres apellidosNombresLitigantes,"));
        sql.append(sharedUtil.append("litigantes.codigo_contribuyente codigoContribuyente,"));
        sql.append(sharedUtil.append("estadoLlenado.descripcion estadoLlenadoFicha,"));
        sql.append(sharedUtil.append("informacionComplementaria.nro_habitantes nroHabitantes,"));
        sql.append(sharedUtil.append("informacionComplementaria.nro_familias nroFamilias,"));
        sql.append(sharedUtil.append("mantenimiento.descripcion mantenimiento"));
        //sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        //sql.append(sharedUtil.append("left join jofrantoba.catastro.tgv_informacion_compl informacionComplementaria  on unidadcatastral.id =  informacionComplementaria.id_unidad_catastral and informacionComplementaria.is_persistente = true"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_informacion_compl informacionComplementaria  "));
        sql.append(sharedUtil.append("left join jofrantoba.catastro.tgv_litigantes litigantes on  informacionComplementaria.id_unidad_catastral = litigantes.id_unidad_catastral and litigantes.is_persistente = true"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 174 and is_persistente = true) condicionDeclarante on  informacionComplementaria.id_condicion_declarante = condicionDeclarante.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 78 and is_persistente = true ) tipoDocumento on litigantes.id_tipo_documento = tipoDocumento.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 312 and is_persistente = true ) estadoLlenado on  informacionComplementaria.id_estado_llenado = estadoLlenado.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 175 and is_persistente = true ) mantenimiento on informacionComplementaria.id_mantenimiento =  mantenimiento.id"));
        sql.append(sharedUtil.append("where informacionComplementaria.is_persistente = true and informacionComplementaria.id_unidad_catastral  = ?"));
        List<DtoInformacionComplementaria> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoInformacionComplementaria dto = new DtoInformacionComplementaria();
                        dto.setCondicionDeclarante(rs.getString("condicionDeclarante"));
                        dto.setTd(rs.getString("td"));
                        dto.setNroDocumento(rs.getString("nroDocumento"));
                        dto.setApellidosNombresLitigantes(rs.getString("apellidosNombresLitigantes"));
                        dto.setCodigoContribuyente(rs.getString("codigoContribuyente"));
                        dto.setEstadoLlenadoFicha(rs.getString("estadoLlenadoFicha"));
                        dto.setNroHabitantes(rs.getInt("nroHabitantes"));
                        dto.setNroFamilias(rs.getInt("nroFamilias"));
                        dto.setMantenimiento(rs.getString("mantenimiento"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoObservaciones> dsObservacionesCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        sql.append(sharedUtil.append(""));
        List<DtoObservaciones> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoObservaciones dto = new DtoObservaciones();
                        /*dto.setCondicionDeclarante(rs.getString("condicionDeclarante"));
                        dto.setTd(rs.getString("td"));
                        dto.setNroDocumento(rs.getString("nroDocumento"));
                        dto.setApellidosNombresLitigantes(rs.getString("apellidosNombresLitigantes"));
                        dto.setCodigoContribuyente(rs.getString("codigoContribuyente"));
                        dto.setEstadoLlenadoFicha(rs.getString("estadoLlenadoFicha"));
                        dto.setNroHabitantes(rs.getInt("nroHabitantes"));
                        dto.setNroFamilias(rs.getInt("nroFamilias"));
                        dto.setMantenimiento(rs.getString("mantenimiento"));*/
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoEvaluacionPredioCatastral> dsEvaluacionPredioCatastral(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct informacionComplementaria.eva_lote_colindante  areaLoteColidante,"));
        sql.append(sharedUtil.append("informacionComplementaria.eva_area_publica areaPublica,"));
        sql.append(sharedUtil.append("informacionComplementaria.eva_jardin_aislamiento jardinAislamiento,"));
        sql.append(sharedUtil.append("informacionComplementaria.eva_intangible  areaIntangible"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_informacion_compl informacionComplementaria  on informacionComplementaria.id_unidad_catastral = unidadcatastral.id and informacionComplementaria.is_persistente = true"));
        sql.append(sharedUtil.append("where unidadcatastral.is_persistente = true and unidadcatastral.id  = ?"));
        //sql.append(sharedUtil.append("group  by unidadcatastral.id , informacionComplementaria.eva_lote_colindante , informacionComplementaria.eva_area_publica , informacionComplementaria.eva_jardin_aislamiento , informacionComplementaria.eva_intangible ;"));
        List<DtoEvaluacionPredioCatastral> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoEvaluacionPredioCatastral dto = new DtoEvaluacionPredioCatastral();
                        dto.setAreaLoteColidante(rs.getDouble("areaLoteColidante"));
                        dto.setAreaPublica(rs.getDouble("areaPublica"));
                        dto.setJardinAislamiento(rs.getDouble("jardinAislamiento"));
                        dto.setAreaIntangible(rs.getDouble("areaIntangible"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoReportFichaCatastralActEco.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoReportFichaCatastralActEco;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoActividadEconomica;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoAutorizacionAnuncio;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoConductorActEco;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoDomicilioFisConductorActEco;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoInformacionComplActEco;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoObservaciones;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoTipoActividadEconomica;
import com.jofrantoba.examples.jofrantoba.entity.UnidadCatastral;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoReportFichaCatastralActEco extends AbstractJpaDaoV2<UnidadCatastral> implements InterDaoReportFichaCatastralActEco {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoReportFichaCatastralActEco() {
        super();
        this.setClazz(UnidadCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public List<DtoConductorActEco> dsConductorActEco(Long idUnidadCatastral, Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select tipoConductor.descripcion tipoConductor,"));
        sql.append(sharedUtil.append("actividadEconomica.nombre_comercial nombreComercial,"));
        sql.append(sharedUtil.append("tipoDocumento.descripcion tipoDocIdentidad,"));
        sql.append(sharedUtil.append("actividadEconomica.numero_documento nroDocumento,"));
        sql.append(sharedUtil.append("actividadEconomica.numero_ruc nroRuc,"));
        sql.append(sharedUtil.append("actividadEconomica.nombre nombre,"));
        sql.append(sharedUtil.append("condicionConductor.descripcion condicionConductor"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_actividad_economica actividadEconomica"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 78 and is_persistente = true ) tipoDocumento on actividadEconomica.id_tipo_doc_ident = tipoDocumento.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 317 and is_persistente = true ) tipoConductor on actividadEconomica.id_tipo_conductor = tipoConductor.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 320 and is_persistente = true ) condicionConductor on actividadEconomica.id_condicion_conductor = condicionConductor.id"));
        sql.append(sharedUtil.append("where actividadEconomica.is_persistente = true and actividadEconomica.id_unidad_catastral= ?"));
        sql.append(sharedUtil.append("and actividadEconomica.id = ?"));
        //System.out.println(sql.toString());
        List<DtoConductorActEco> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    ps.setLong(2, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoConductorActEco dto = new DtoConductorActEco();
                        dto.setTipoDeConductor(rs.getString("tipoConductor"));
                        dto.setNombreComercial(rs.getString("nombreComercial"));
                        dto.setTipoDocumentoIdentidad(rs.getString("tipoDocIdentidad"));
                        dto.setNroDoc(rs.getString("nroDocumento"));
                        dto.setNroRuc(rs.getString("nroRuc"));
                        dto.setApellidosYNombresRazonSocialConductor(rs.getString("nombre"));
                        dto.setCondicionDelConductor(rs.getString("condicionConductor"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoDomicilioFisConductorActEco> dsDomicilioFisConductorActEco(Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select departamento.descripcion departamento,"));
        sql.append(sharedUtil.append("provincia.descripcion provincia ,"));
        sql.append(sharedUtil.append("distrito.descripcion distrito,"));
        sql.append(sharedUtil.append("conductorDomicilio.telefono telefono,"));
        sql.append(sharedUtil.append("conductorDomicilio.anexo anexo,"));
        sql.append(sharedUtil.append("conductorDomicilio.fax fax,"));
        sql.append(sharedUtil.append("conductorDomicilio.correo_electronico correoElectronico,"));
        sql.append(sharedUtil.append("conductorDomicilio.codigo_via codigoVia,"));
        sql.append(sharedUtil.append("tipoVia.descripcion tipoVia,"));
        sql.append(sharedUtil.append("conductorDomicilio.nombre_via nombreVia,"));
        sql.append(sharedUtil.append("conductorDomicilio.numero_municipal nroMunicipal,"));
        sql.append(sharedUtil.append("conductorDomicilio.numero_edificcion nroEdificacion,"));
        sql.append(sharedUtil.append("conductordomicilio.numero_interior nroInterior,"));
        sql.append(sharedUtil.append("conductorDomicilio.codigo_hu codigoHu,"));
        sql.append(sharedUtil.append("conductorDomicilio.nombre_hu nombreHabitilitacionUrbana,"));
        sql.append(sharedUtil.append("conductorDomicilio.zona_sector_etapa zonaSectorEtapa,"));
        sql.append(sharedUtil.append("conductorDomicilio.manzana manzana,"));
        sql.append(sharedUtil.append("conductorDomicilio.lote lote,"));
        sql.append(sharedUtil.append("conductorDomicilio.sub_lote subLote"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_conductor_domicilio conductorDomicilio"));
        sql.append(sharedUtil.append("left join jofrantoba.catastro.tm_departamento departamento  on  conductorDomicilio.id_departamento = departamento.id and departamento.is_persistente = true"));
        sql.append(sharedUtil.append("left join jofrantoba.catastro.tm_provincia provincia on  conductorDomicilio.id_provincia = provincia.id and provincia.is_persistente = true"));
        sql.append(sharedUtil.append("left join jofrantoba.catastro.tm_distrito distrito on conductorDomicilio.id_distrito = distrito.id and distrito.is_persistente = true"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 1 and is_persistente = true ) tipoVia on conductorDomicilio.id_tipo_via = tipoVia.id "));
        sql.append(sharedUtil.append("where conductorDomicilio.is_persistente = true"));
        sql.append(sharedUtil.append("and conductorDomicilio.id_actividad_economica = ?"));
        //System.out.println(sql.toString());
        List<DtoDomicilioFisConductorActEco> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoDomicilioFisConductorActEco dto = new DtoDomicilioFisConductorActEco();
                        dto.setDepartamento(rs.getString("departamento"));
                        dto.setProvincia(rs.getString("provincia"));
                        dto.setDistrito(rs.getString("distrito"));
                        dto.setTelefono(rs.getString("telefono"));
                        dto.setCodigoDeVia(rs.getString("codigoVia"));
                        dto.setTipoDeVia(rs.getString("tipoVia"));
                        dto.setNombreDeVia(rs.getString("nombreVia"));
                        dto.setNroMunicipal(rs.getString("nroMunicipal"));
                        dto.setNombreDeEdificacion(rs.getString("nroEdificacion"));
                        dto.setNroInterior(rs.getString("nroInterior"));
                        dto.setCodigoHu(rs.getString("codigoHu"));
                        dto.setNombreHu(rs.getString("nombreHabitilitacionUrbana"));
                        dto.setZonaSectorEtapa(rs.getString("zonaSectorEtapa"));
                        dto.setManzana(rs.getString("manzana"));
                        dto.setLote(rs.getString("lote"));
                        dto.setSubLote(rs.getString("subLote"));
                        dto.setAnexo(rs.getString("anexo"));
                        dto.setFax(rs.getString("fax"));
                        dto.setCorreoElectronico(rs.getString("correoElectronico"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoTipoActividadEconomica> dsTipoActividadEconomica(Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct tipoActEconoEspecifico.codigo codigoActividad,"));
        sql.append(sharedUtil.append("tipoActEconoEspecifico.descripcion descripcionActividad"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_autorizacion_municipal autorizacionMunicipal"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tm_tipo_act_econo_especifico tipoActEconoEspecifico on tipoActEconoEspecifico.id = autorizacionMunicipal.id_tipo_act_econo_especifico  and tipoActEconoEspecifico.is_persistente = true"));
        sql.append(sharedUtil.append("where autorizacionMunicipal.is_persistente = true"));
        sql.append(sharedUtil.append("and autorizacionMunicipal.id_actividad_economica = ?"));
        //System.out.println(sql.toString());
        List<DtoTipoActividadEconomica> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoTipoActividadEconomica dto = new DtoTipoActividadEconomica();
                        dto.setCodigoActividad(rs.getString("codigoActividad"));
                        dto.setDescripcionDeLaActividad(rs.getString("descripcionActividad"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoActividadEconomica> dsActividadEconomica(Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct actividadEconomica.numero_expediente nroExpediente,"));
        sql.append(sharedUtil.append("actividadEconomica.numero_licencia nroLicencia,"));
        sql.append(sharedUtil.append("actividadEconomica.fecha_expedicion fechaExpedicion,"));
        sql.append(sharedUtil.append("actividadEconomica.fecha_vencimiento fechaVencimiento,"));
        sql.append(sharedUtil.append("actividadEconomica.inicio_actividad inicioActividad,"));
        sql.append(sharedUtil.append("actividadEconomica.predio_catastral_aa predioCatastralAreaAutorizada,"));
        sql.append(sharedUtil.append("actividadEconomica.predio_catastral_av predioCatastralAreaVerificada,"));
        sql.append(sharedUtil.append("actividadEconomica.via_publica_aa viaPublicaAreaAutorizada,"));
        sql.append(sharedUtil.append("actividadEconomica.via_publica_av viaPublicaAreaVerificada,"));
        sql.append(sharedUtil.append("actividadEconomica.bien_comun_aa bienComunAreaAutorizada,"));
        sql.append(sharedUtil.append("actividadEconomica.bien_comun_av bienComunAreaVerificada"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_actividad_economica actividadEconomica"));
        sql.append(sharedUtil.append("where actividadEconomica.is_persistente = true"));
        sql.append(sharedUtil.append("and actividadEconomica.id = ?"));
        //System.out.println(sql.toString());
        List<DtoActividadEconomica> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoActividadEconomica dto = new DtoActividadEconomica();
                        dto.setNroDeExpediente(rs.getString("nroExpediente"));
                        dto.setNroDeLicencia(rs.getString("nroLicencia"));
                        dto.setFechaDeExpedicion(rs.getDate("fechaExpedicion"));
                        dto.setFechaDeVencimiento(rs.getDate("fechaVencimiento"));
                        dto.setInicioDeActividad(rs.getDate("inicioActividad"));
                        dto.setPredioCatastralAreaAutorizada(rs.getDouble("predioCatastralAreaAutorizada"));
                        dto.setPredioCatastralAreaVerificada(rs.getDouble("predioCatastralAreaVerificada"));
                        dto.setViaPublicaAreaAutorizada(rs.getDouble("viaPublicaAreaAutorizada"));
                        dto.setViaPublicaAreaVerificada(rs.getDouble("viaPublicaAreaVerificada"));
                        dto.setBienComunAreaAutorizada(rs.getDouble("bienComunAreaAutorizada"));
                        dto.setBienComunAreaVerificada(rs.getDouble("bienComunAreaVerificada"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoAutorizacionAnuncio> dsAutorizacionAnuncio(Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct anuncio.descripcion anuncio,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.nro_lados nroLados,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.area_autorizada_anuncio areaAutorizadaAnuncio,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.area_verificada_anuncio areaVerificadaAnuncio,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.nro_expediente nroExpediente,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.nro_licencia nroLicencia ,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.fecha_expedicion fechaExpedicion,"));
        sql.append(sharedUtil.append("autorizacionAnuncio.fecha_vencimiento fechaVencimiento"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_autorizacion_anuncio autorizacionAnuncio"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 231 and is_persistente = true ) anuncio on autorizacionAnuncio.id_anuncio = anuncio.id"));
        sql.append(sharedUtil.append("where autorizacionAnuncio.is_persistente = true "));
        sql.append(sharedUtil.append("and autorizacionAnuncio.id_actividad_economica = ?"));
        //System.out.println(sql.toString());
        List<DtoAutorizacionAnuncio> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoAutorizacionAnuncio dto = new DtoAutorizacionAnuncio();
                        dto.setDescripcionDelTipoDeAnuncio(rs.getString("anuncio"));
                        dto.setNroDeLados(rs.getString("nroLados"));
                        dto.setAreaAutorizadaDelAnuncio(rs.getDouble("areaAutorizadaAnuncio"));
                        dto.setAreaVerificadaDelAnuncio(rs.getDouble("areaVerificadaAnuncio"));
                        dto.setNroExpediente(rs.getString("nroExpediente"));
                        dto.setNroLicencia(rs.getString("nroLicencia"));
                        dto.setFechaDeExpedicion(rs.getDate("fechaExpedicion"));
                        dto.setFechaDeVencimiento(rs.getDate("fechaVencimiento"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoInformacionComplActEco> dsInformacionComplActEco(Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select condicionDeclarante.descripcion condicionDeclarante,"));
        sql.append(sharedUtil.append("informacionComplementariaActEcon.condicion_declarante_otro condicionDeclaranteOtro,"));
        sql.append(sharedUtil.append("documentos.descripcion documentoPresentados,"));
        sql.append(sharedUtil.append("informacionComplementariaActEcon.documento_presentado_otro documentoPresentadosOtro,"));
        sql.append(sharedUtil.append("estadoLlenado.descripcion estadoLlenadoFicha,"));
        sql.append(sharedUtil.append("mantenimiento.descripcion mantenimiento"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_informacion_compl_act_econ informacionComplementariaActEcon"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 174 and is_persistente = true) condicionDeclarante on  informacionComplementariaActEcon.id_condicion_declarante = condicionDeclarante.id "));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 312 and is_persistente = true ) estadoLlenado on  informacionComplementariaActEcon.id_estado_llenado = estadoLlenado.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 330 and is_persistente = true ) mantenimiento on  informacionComplementariaActEcon.id_mantenimiento  = mantenimiento.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 325 and is_persistente = true ) documentos on  informacionComplementariaActEcon.id_documento_presentado  = documentos.id"));
        sql.append(sharedUtil.append("where informacionComplementariaActEcon.is_persistente = true"));
        sql.append(sharedUtil.append("and informacionComplementariaActEcon.id_actividad_economica = ?"));
        //System.out.println(sql.toString());
        List<DtoInformacionComplActEco> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoInformacionComplActEco dto = new DtoInformacionComplActEco();
                        dto.setCondicionDeclarante(rs.getString("condicionDeclarante"));
                        dto.setCondicionDeclaranteOtros(rs.getString("condicionDeclaranteOtro"));
                        dto.setDocumentosPresentados(rs.getString("documentoPresentados"));
                        dto.setDocumentosPresentadosOtros(rs.getString("documentoPresentadosOtro"));
                        dto.setEstadoLlenadoFicha(rs.getString("estadoLlenadoFicha"));
                        dto.setMantenimiento(rs.getString("mantenimiento"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoObservaciones> dsObservacionesActEco(Long idActividadEconomica) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select informacionComplementariaActEcon.observacion observaciones,"));
        sql.append(sharedUtil.append("informacionComplementariaActEcon.numero_documento_decla dniDeclarante,"));
        sql.append(sharedUtil.append("informacionComplementariaActEcon.nombre_declarante nombresDeclarante,"));
        sql.append(sharedUtil.append("informacionComplementariaActEcon.apellido_declarante apellidosDeclarante, "));
        sql.append(sharedUtil.append("informacionComplementariaActEcon.fecha_declarante fecha"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_informacion_compl_act_econ informacionComplementariaActEcon"));
        sql.append(sharedUtil.append("where informacionComplementariaActEcon.is_persistente = true"));
        sql.append(sharedUtil.append("and informacionComplementariaActEcon.id_actividad_economica = ?"));
        //System.out.println(sql.toString());
        List<DtoObservaciones> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idActividadEconomica);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoObservaciones dto = new DtoObservaciones();
                        dto.setObservaciones(rs.getString("observaciones"));
                        dto.setDniDeclarante(rs.getString("dniDeclarante"));
                        dto.setNombresDeclarante(rs.getString("nombresDeclarante"));
                        dto.setApellidosDeclarante(rs.getString("apellidosDeclarante"));
                        dto.setFecha(rs.getDate("fecha"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoReportFichaCatastralBBCC.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoReportFichaCatastralBBCC;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoDocumentoDatoRegistral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoEdificacionBienComun;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoRecapitulacionBienComun;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoRecapitulacionEdificio;
import com.jofrantoba.examples.jofrantoba.entity.UnidadCatastral;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoReportFichaCatastralBBCC extends AbstractJpaDaoV2<UnidadCatastral> implements InterDaoReportFichaCatastralBBCC {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoReportFichaCatastralBBCC() {
        super();
        this.setClazz(UnidadCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public List<DtoEdificacionBienComun> dsEdificacionBienComun(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select distinct interior.nombre_edificacion,"));
        sql.append(sharedUtil.append("tipoEdificacion.descripcion tipoDeEdificacion"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_interior interior "));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 18 and is_persistente = true) tipoEdificacion on tipoEdificacion.id = interior.id_tipo_edificacion"));
        sql.append(sharedUtil.append("where  interior.is_persistente = true and interior.id_unidad_catastral = ?"));
        List<DtoEdificacionBienComun> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoEdificacionBienComun dto = new DtoEdificacionBienComun();
                        dto.setNombreEdificacion(rs.getString("nombre_edificacion"));
                        dto.setTipoDeEdificacion(rs.getString("tipoDeEdificacion"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoRecapitulacionEdificio> dsRecapitulacionEdificio(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select unidadcatastral.codigo_edificacion codigoEdificacion,"));
        sql.append(sharedUtil.append("recapitulacionEdificios.porcentaje porcentaje,"));
        sql.append(sharedUtil.append("recapitulacionEdificios.area_terreno_comun areaTerrenoComun,"));
        sql.append(sharedUtil.append("recapitulacionEdificios.area_construida_comun areaConstruidaComun,"));
        sql.append(sharedUtil.append("recapitulacionEdificios.area_otras_instalacion_comun areaOtrasInstalacionComun"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_unidad_catastral unidadCatastral "));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_recapitulacion_edificios recapitulacionEdificios on recapitulacionEdificios.id_unidad_catastral = unidadCatastral.id and recapitulacionEdificios.is_persistente  = true"));
        sql.append(sharedUtil.append("where unidadcatastral.is_persistente  = true and unidadcatastral.id = ?"));
        List<DtoRecapitulacionEdificio> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoRecapitulacionEdificio dto = new DtoRecapitulacionEdificio();
                        dto.setEdificio(rs.getString("codigoEdificacion"));
                        dto.setPorcentaje(rs.getDouble("porcentaje"));
                        dto.setAtc(rs.getDouble("areaTerrenoComun"));
                        dto.setAcc(rs.getDouble("areaConstruidaComun"));
                        dto.setAoic(rs.getDouble("areaOtrasInstalacionComun"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoRecapitulacionBienComun> dsRecapitulacionBienComun(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select unidadCatastralRef.codigo_edificacion codigoEdificacion,"));
        sql.append(sharedUtil.append("unidadCatastralRef.codigo_entrada codigoEntrada,"));
        sql.append(sharedUtil.append("unidadCatastralRef.codigo_piso codigoPiso,"));
        sql.append(sharedUtil.append("unidadCatastralRef.codigo_unidad codigoUnidad,"));
        sql.append(sharedUtil.append("recapitulacionBienesComunes.porcentaje porcentaje,"));
        sql.append(sharedUtil.append("recapitulacionBienesComunes.area_terreno_comun areaTerrenoComun,"));
        sql.append(sharedUtil.append("recapitulacionBienesComunes.area_construida_comun areaConstruidaComun,"));
        sql.append(sharedUtil.append("recapitulacionBienesComunes.area_otras_instalacion_comun areaOtrasInstalacionComun"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_recapitulacion_bc recapitulacionBienesComunes"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_unidad_catastral unidadCatastralRef on unidadCatastralRef.id=recapitulacionBienesComunes.id_unidad_catastral_ref"));
        sql.append(sharedUtil.append("where recapitulacionBienesComunes.is_persistente  = true and recapitulacionBienesComunes.id_unidad_catastral = ?"));
        sql.append(sharedUtil.append("order by recapitulacionBienesComunes.id_unidad_catastral_ref asc"));
        List<DtoRecapitulacionBienComun> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoRecapitulacionBienComun dto = new DtoRecapitulacionBienComun();
                        dto.setEdificacion(rs.getString("codigoEdificacion"));
                        dto.setEntrada(rs.getString("codigoEntrada"));
                        dto.setPiso(rs.getString("codigoPiso"));
                        dto.setUnidad(rs.getString("codigoUnidad"));
                        dto.setPorcentaje(rs.getDouble("porcentaje"));
                        dto.setAtc(rs.getDouble("areaTerrenoComun"));
                        dto.setAcc(rs.getDouble("areaConstruidaComun"));
                        dto.setAoic(rs.getDouble("areaOtrasInstalacionComun"));
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

    @Override
    public List<DtoDocumentoDatoRegistral> dsDocumentoDatoRegistral(Long idUnidadCatastral) {
        return new ArrayList();
    }

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoReportFichaCatastralCoti.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoReportFichaCatastralCoti;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoCotitularCatastral;
import com.jofrantoba.examples.jofrantoba.entity.UnidadCatastral;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Repository
public class DaoReportFichaCatastralCoti extends AbstractJpaDaoV2<UnidadCatastral> implements InterDaoReportFichaCatastralCoti {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoReportFichaCatastralCoti() {
        super();
        this.setClazz(UnidadCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public List<DtoCotitularCatastral> dsDatosCotitular(Long idUnidadCatastral) {
        Shared sharedUtil = new Shared();
        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append("row_number() over( order by titular.id  ) nroCotitular,"));
        sql.append(sharedUtil.append("totalCotitularidad.totalCotitulares totalCotitulares,"));
        sql.append(sharedUtil.append("tipoTitular.descripcion  tipoTitular,"));
        sql.append(sharedUtil.append("titular.id_tipo_titular  idTipoTitular,"));
        sql.append(sharedUtil.append("titular.porcentaje porcentajeCotitular,"));
        sql.append(sharedUtil.append("titular.codigo_contribuyente codigoContribuyente,"));
        sql.append(sharedUtil.append("tipoDocumento.descripcion  tipoDocIdentidad,"));
        sql.append(sharedUtil.append("titular.numero_documento nroDoc,"));
        sql.append(sharedUtil.append("titular.nombre_ruc nombres,"));
        sql.append(sharedUtil.append("titular.apellido_paterno apellidoPaterno,"));
        sql.append(sharedUtil.append("titular.apellido_materno apellidoMaterno,"));
        sql.append(sharedUtil.append("formaAdquisicion.descripcion formaAdquisicion,"));
        sql.append(sharedUtil.append("titularidad.fecha_adquisicion fechaAdquisicion,"));
        sql.append(sharedUtil.append("tipoPersonaJuridica.descripcion condicionEspecialTitular,"));
        sql.append(sharedUtil.append("departamento.descripcion departamento,"));
        sql.append(sharedUtil.append("provincia.descripcion provincia,"));
        sql.append(sharedUtil.append("distrito.descripcion distrito,"));
        sql.append(sharedUtil.append("titular.telefono telefono,"));
        sql.append(sharedUtil.append("titular.anexo anexo,"));
        sql.append(sharedUtil.append("titular.correo_electronico correoElectronico,"));
        sql.append(sharedUtil.append("titular.codigo_via codigoVia,"));
        sql.append(sharedUtil.append("tipoVia.descripcion tipoVia,"));
        sql.append(sharedUtil.append("titular.nombre_via nombreVia,"));
        sql.append(sharedUtil.append("titular.numero_municipal nroMunicipal,"));
        sql.append(sharedUtil.append("titular.numero_interior nroInterior,"));
        sql.append(sharedUtil.append("titular.codigo_hu codigoHu,"));
        sql.append(sharedUtil.append("titular.nombre_hu nombreHabilitacionUrbana,"));
        sql.append(sharedUtil.append("titular.zona_sector_etapa zonaSectorEtapa,"));
        sql.append(sharedUtil.append("titular.manzana manzana,"));
        sql.append(sharedUtil.append("titular.lote lote,"));
        sql.append(sharedUtil.append("titular.sub_lote subLote"));
        sql.append(sharedUtil.append("from jofrantoba.catastro.tgv_titularidad titularidad"));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_titular titular on  titular.id_titularidad = titularidad.id and titular.is_persistente = true"));
        sql.append(sharedUtil.append("left join jofrantoba.catastro.tm_departamento departamento  on  titular.id_departamento = departamento.id and departamento.is_persistente = true"));
        sql.append(sharedUtil.append("left join jofrantoba.catastro.tm_provincia provincia on  titular.id_provincia = provincia.id and provincia.is_persistente = true"));
        sql.append(sharedUtil.append("left join jofrantoba.catastro.tm_distrito distrito on titular.id_distrito = distrito.id and distrito.is_persistente = true"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 76 and is_persistente = true ) tipoTitular on titular.id_tipo_titular = tipoTitular.id "));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 78 and is_persistente = true ) tipoDocumento on titular.id_tipo_documento  = tipoDocumento.id "));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 94 and is_persistente = true ) tipoPersonaJuridica on titular.id_tipo_persona_juridica = tipoPersonaJuridica.id"));
        sql.append(sharedUtil.append("left join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 111 and is_persistente = true ) formaAdquisicion on titularidad.id_forma_adquisicion =  formaAdquisicion.id"));
        sql.append(sharedUtil.append("left join (select count(titular.id) totalcotitulares, id_unidad_catastral from jofrantoba.catastro.tgv_titularidad titularidad "));
        sql.append(sharedUtil.append("inner join jofrantoba.catastro.tgv_titular titular on titular.id_titularidad = titularidad.id and titular.is_persistente = true"));
        sql.append(sharedUtil.append("group by  id_unidad_catastral) totalCotitularidad on totalCotitularidad.id_unidad_catastral =  titularidad.id_unidad_catastral"));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 95 and is_persistente = true ) tipoUbicacion on  titular.id_tipo_ubicacion = tipoUbicacion.id "));
        sql.append(sharedUtil.append("left  join (select id,descripcion from jofrantoba.catastro.tm_parametrias where id_parametria_padre = 1 and is_persistente = true ) tipoVia on titular.id_tipo_via = tipoVia.id "));
        sql.append(sharedUtil.append("where  titularidad.id_unidad_catastral = ?"));
        System.out.println(sql.toString());
        List<DtoCotitularCatastral> datasource = new ArrayList();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = cnctn.prepareStatement(sql.toString());
                    ps.setLong(1, idUnidadCatastral);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        DtoCotitularCatastral dto = new DtoCotitularCatastral();
                        dto.setNroCotitular(rs.getLong("nroCotitular"));
                        dto.setTotalCotitulares(rs.getInt("totalCotitulares"));
                        dto.setTipoTitular(rs.getString("tipoTitular"));
                        dto.setPorcentajeCotitular(rs.getDouble("porcentajeCotitular"));
                        dto.setCodigoContribuyente(rs.getString("codigoContribuyente"));
                        dto.setFormaAdquisicion(rs.getString("formaAdquisicion"));
                        dto.setFechaAdquisicion(rs.getDate("fechaAdquisicion"));
                        dto.setCondicionEspecialTitular(rs.getString("condicionEspecialTitular"));
                        dto.setDepartamento(rs.getString("departamento"));
                        dto.setProvincia(rs.getString("provincia"));
                        dto.setDistrito(rs.getString("distrito"));
                        dto.setTelefono(rs.getString("telefono"));
                        dto.setAnexo(rs.getString("anexo"));
                        dto.setCorreoElectronico(rs.getString("correoElectronico"));
                        dto.setCodigoVia(rs.getString("codigoVia"));
                        dto.setTipoVia(rs.getString("tipoVia"));
                        dto.setNombreVia(rs.getString("nombreVia"));
                        dto.setNroMunicipal(rs.getString("nroMunicipal"));
                        dto.setNroInterior(rs.getString("nroInterior"));
                        dto.setCodigoHu(rs.getString("codigoHu"));
                        dto.setNombreHabilitacionUrbana(rs.getString("nombreHabilitacionUrbana"));
                        dto.setZonaSectorEtapa(rs.getString("zonaSectorEtapa"));
                        dto.setManzana(rs.getString("manzana"));
                        dto.setLote(rs.getString("lote"));
                        dto.setSubLote(rs.getString("subLote"));
                        Long idTipoTitular = rs.getLong("idTipoTitular");
                        if (idTipoTitular == 80l) {
                            dto.setNroRuc(rs.getString("nroDoc"));
                            dto.setRazonSocial(rs.getString("nombres"));
                        } else {
                            dto.setTipoDocIdentidad(rs.getString("tipoDocIdentidad"));
                            dto.setNombres(rs.getString("nombres"));
                            dto.setNroDoc(rs.getString("nroDoc"));
                            dto.setApellidoPaterno(rs.getString("apellidoPaterno"));
                            dto.setApellidoMaterno(rs.getString("apellidoMaterno"));                           
                        }
                        datasource.add(dto);
                    }
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        return datasource;
    }

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoReportFichaCommon.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;

/**
 *
 * @author jtorresb
 */
public class DaoReportFichaCommon {
    
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoSector.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoSector;
import com.jofrantoba.examples.jofrantoba.entity.Sector;
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
public class DaoSector extends AbstractJpaDaoV2<Sector> implements InterDaoSector {
    
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
        String table="jofrantoba.catastro.tg_sector as base";
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
        joinTables[0]="inner:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[2]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.codigo_departamento:asc","base.codigo_provincia:asc","base.codigo_distrito:asc","base.codigo_sector:asc"};                
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tg_sector as base";
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
        joinTables[0]="inner:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[2]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String fields=strFileds.toString();
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.codigo_departamento:asc","base.codigo_provincia:asc","base.codigo_distrito:asc","base.codigo_sector:asc"};                
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long count(Long idDistrito) throws Exception {
        String joinTable="inner:distrito";
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoSistema.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoSistema;
import com.jofrantoba.examples.jofrantoba.entity.Sistema;
import java.util.Collection;
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
public class DaoSistema extends AbstractJpaDaoV2<Sistema> implements InterDaoSistema {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoSistema() {
        super();
        this.setClazz(Sistema.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<Sistema> listar() throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder);
    }

    @Override
    public Collection<Sistema> listar(int pageNumber, int pageSize) throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder,pageNumber,pageSize);
    }
    
}

```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoTipoActEconoEspecifico.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoTipoActEconoEspecifico;
import com.jofrantoba.examples.jofrantoba.entity.TipoActEconoEspecifico;
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
public class DaoTipoActEconoEspecifico extends AbstractJpaDaoV2<TipoActEconoEspecifico> implements InterDaoTipoActEconoEspecifico {

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
        String table="jofrantoba.catastro.tm_tipo_act_econo_especifico as base";
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
        joinTables[0]="inner:jofrantoba.catastro.tm_tipo_act_econo_intermedio as tipoActEconoIntermedio:on:base.id_tipo_act_econo_intermedio:tipoActEconoIntermedio.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_tipo_act_econo_general as tipoActEconoGeneral:on:tipoActEconoIntermedio.id_tipo_act_econo_general:tipoActEconoGeneral.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tm_tipo_act_econo_especifico as base";
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
        joinTables[0]="inner:jofrantoba.catastro.tm_tipo_act_econo_intermedio as tipoActEconoIntermedio:on:base.id_tipo_act_econo_intermedio:tipoActEconoIntermedio.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_tipo_act_econo_general as tipoActEconoGeneral:on:tipoActEconoIntermedio.id_tipo_act_econo_general:tipoActEconoGeneral.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long count(Long idTipoActEconoIntermedio) throws Exception {
        String joinTable="inner:tipoActEconoIntermedio";
        String[] mapFilterField={"=:tipoActEconoIntermedio.id:"+idTipoActEconoIntermedio,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoTipoActEconoGeneral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoTipoActEconoGeneral;
import com.jofrantoba.examples.jofrantoba.entity.TipoActEconoGeneral;
import java.util.Collection;
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
public class DaoTipoActEconoGeneral extends AbstractJpaDaoV2<TipoActEconoGeneral> implements InterDaoTipoActEconoGeneral {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoTipoActEconoGeneral() {
        super();
        this.setClazz(TipoActEconoGeneral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<TipoActEconoGeneral> listar() throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder);
    }

    @Override
    public Collection<TipoActEconoGeneral> listar(int pageNumber, int pageSize) throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder,pageNumber,pageSize);
    }
    
}

```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoTipoActEconoIntermedio.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoTipoActEconoIntermedio;
import com.jofrantoba.examples.jofrantoba.entity.TipoActEconoIntermedio;
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
public class DaoTipoActEconoIntermedio extends AbstractJpaDaoV2<TipoActEconoIntermedio> implements InterDaoTipoActEconoIntermedio {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoTipoActEconoIntermedio() {
        super();
        this.setClazz(TipoActEconoIntermedio.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<TipoActEconoIntermedio> listar(Long idTipoActEconoGeneral)throws Exception {
        String joinTable="inner:tipoActEconoGeneral";
        String[] mapFilterField={"=:tipoActEconoGeneral.id:"+idTipoActEconoGeneral,"=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("tipoActEconoGeneral.id as idTipoActEconoGeneral,"));
        strFields.append(share.append("tipoActEconoGeneral.descripcion as descripcionTipoActEconoGeneral"));
        String fields=strFields.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                TipoActEconoIntermedio bean=new TipoActEconoIntermedio();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<TipoActEconoIntermedio> lista=(Collection<TipoActEconoIntermedio>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }
  
    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tm_tipo_act_econo_intermedio as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("tipoActEconoGeneral.id as idTipoActEconoGeneral,"));
        strFields.append(share.append("tipoActEconoGeneral.descripcion as descripcionTipoActEconoGeneral"));
        String fields=strFields.toString();
        String[] joinTables=new String[1];
        joinTables[0]="inner:jofrantoba.catastro.tm_tipo_act_econo_general as tipoActEconoGeneral:on:base.id_tipo_act_econo_general:tipoActEconoGeneral.id";        
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tm_tipo_act_econo_intermedio as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("tipoActEconoGeneral.id as idTipoActEconoGeneral,"));
        strFields.append(share.append("tipoActEconoGeneral.descripcion as descripcionTipoActEconoGeneral"));
        String fields=strFields.toString();
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};
        String[] joinTable=new String[1];
        joinTable[0]="inner:jofrantoba.catastro.tm_tipo_act_econo_general as tipoActEconoGeneral:on:base.id_tipo_act_econo_general:tipoActEconoGeneral.id";        
        return this.allFieldsJoinPostgres(joinTable,table,fields,mapFilterField,mapOrder, "and");
    }    
    
    @Override
    public Long count(Long idTipoActEconoGeneral) throws Exception {
        String joinTable="inner:tipoActEconoGeneral";
        String[] mapFilterField={"=:tipoActEconoGeneral.id:"+idTipoActEconoGeneral,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoTipoOtrasInstalaciones.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoTipoOtrasInstalaciones;
import com.jofrantoba.examples.jofrantoba.entity.TipoOtrasInstalaciones;
import java.util.Collection;
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
public class DaoTipoOtrasInstalaciones extends AbstractJpaDaoV2<TipoOtrasInstalaciones> implements InterDaoTipoOtrasInstalaciones {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoTipoOtrasInstalaciones(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(TipoOtrasInstalaciones.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<TipoOtrasInstalaciones> listar() throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder);
    }

    @Override
    public Collection<TipoOtrasInstalaciones> listar(int pageNumber, int pageSize) throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder,pageNumber,pageSize);
    }
    
}

```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoTitular.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoTitular;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterTitular;
import com.jofrantoba.examples.jofrantoba.entity.Titular;
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
public class DaoTitular extends AbstractJpaDaoV2<Titular> implements InterDaoTitular {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoTitular(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(Titular.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<Titular> listarFilter(FilterTitular filter) throws Exception {
        String[] joinTables = {"inner:titularidad", "left:tipoTitular", "left:estadoCivil", "left:tipoDocumento", "left:tipoPersonaJuridica",
            "left:tipoUbicacion", "left:departamento", "left:provincia", "left:distrito", "left:tipoVia"};

        List<String> filters = new ArrayList();

        filters.add("=:base.isPersistente:true");
        if (filter.getIdTitularidad() != null) {
            filters.add("=:titularidad.id:" + filter.getIdTitularidad());
        }
        if (filter.getIdTipoTitular() != null) {
            filters.add("=:tipoTitular.id:" + filter.getIdTipoTitular());
        }
        if (filter.getIdEstadoCivil() != null) {
            filters.add("=:estadoCivil.id:" + filter.getIdEstadoCivil());
        }
        if (filter.getIdTipoDocumento() != null) {
            filters.add("=:tipoDocumento.id:" + filter.getIdTipoDocumento());
        }
        if (filter.getIdTipoPersonaJuridica() != null) {
            filters.add("=:tipoPersonaJuridica.id:" + filter.getIdTipoPersonaJuridica());
        }
        if (filter.getIdTipoUbicacion() != null) {
            filters.add("=:tipoUbicacion.id:" + filter.getIdTipoUbicacion());
        }
        if (filter.getIdDepartamento() != null) {
            filters.add("=:departamento.id:" + filter.getIdDepartamento());
        }
        if (filter.getIdProvincia() != null) {
            filters.add("=:provincia.id:" + filter.getIdProvincia());
        }
        if (filter.getIdDistrito() != null) {
            filters.add("=:distrito.id:" + filter.getIdDistrito());
        }
        if (filter.getIdTipoVia() != null) {
            filters.add("=:tipoVia.id:" + filter.getIdTipoVia());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.nombreRuc as nombreRuc,"));
        strFileds.append(share.append("base.apellidoPaterno as apellidoPaterno,"));
        strFileds.append(share.append("base.apellidoMaterno as apellidoMaterno,"));
        strFileds.append(share.append("base.porcentaje as porcentaje,"));
        strFileds.append(share.append("titularidad.id as idTitularidad,"));
        strFileds.append(share.append("tipoTitular.id as idTipoTitular,"));
        strFileds.append(share.append("tipoTitular.descripcion as tipoTitular,"));
        strFileds.append(share.append("estadoCivil.id as idEstadoCivil,"));
        strFileds.append(share.append("estadoCivil.descripcion as estadoCivil,"));
        strFileds.append(share.append("tipoDocumento.id as idTipoDocumento,"));
        strFileds.append(share.append("tipoDocumento.descripcion as tipoDocumento,"));
        strFileds.append(share.append("base.codigoContribuyente as codigoContribuyente,"));
        strFileds.append(share.append("base.numeroDocumento as numeroDocumento,"));
        strFileds.append(share.append("tipoPersonaJuridica.id as idTipoPersonaJuridica,"));
        strFileds.append(share.append("tipoPersonaJuridica.descripcion as tipoPersonaJuridica,"));
        strFileds.append(share.append("tipoUbicacion.id as idTipoUbicacion,"));
        strFileds.append(share.append("tipoUbicacion.descripcion as tipoUbicacion,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as departamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as provincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as distrito,"));
        strFileds.append(share.append("tipoVia.id as idTipoVia,"));
        strFileds.append(share.append("tipoVia.descripcion as tipoVia"));
        String fields = strFileds.toString();
        ResultTransformer rt = new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Titular bean = new Titular();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Titular> lista = (Collection<Titular>) this.customFieldsJoinFilterAnd(rt, fields, joinTables, mapFilterField, mapOrder);
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table = "jofrantoba.catastro.tgv_titular as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.nombre_ruc as nombreRuc,"));
        strFileds.append(share.append("base.porcentaje as porcentaje,"));
        strFileds.append(share.append("base.codigo_contribuyente as codigoContribuyente,"));
        strFileds.append(share.append("base.numero_documento as numeroDocumento,"));
        strFileds.append(share.append("titularidad.id as idTitularidad,"));
        strFileds.append(share.append("tipoTitular.id as idTipoTitular,"));
        strFileds.append(share.append("tipoTitular.descripcion as tipoTitular,"));
        strFileds.append(share.append("estadoCivil.id as idEstadoCivil,"));
        strFileds.append(share.append("estadoCivil.descripcion as estadoCivil,"));
        strFileds.append(share.append("tipoDocumento.id as idTipoDocumento,"));
        strFileds.append(share.append("tipoDocumento.descripcion as tipoDocumento,"));
        strFileds.append(share.append("tipoPersonaJuridica.id as idTipoPersonaJuridica,"));
        strFileds.append(share.append("tipoPersonaJuridica.descripcion as tipoPersonaJuridica,"));
        strFileds.append(share.append("tipoUbicacion.id as idTipoUbicacion,"));
        strFileds.append(share.append("tipoUbicacion.descripcion as tipoUbicacion,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as departamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as provincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as distrito,"));
        strFileds.append(share.append("tipoVia.id as idTipoVia,"));
        strFileds.append(share.append("tipoVia.descripcion as tipoVia"));
        String fields = strFileds.toString();
        String[] joinTables = new String[10];

        joinTables[0] = "inner:jofrantoba.catastro.tgv_titularidad as titularidad:on:base.id_titularidad:titularidad.id";
        joinTables[1] = "left:jofrantoba.catastro.tm_parametrias as tipoTitular:on:base.id_tipo_titular:tipoTitular.id";
        joinTables[2] = "left:jofrantoba.catastro.tm_parametrias as estadoCivil:on:base.id_estado_civil:estadoCivil.id";
        joinTables[3] = "left:jofrantoba.catastro.tm_parametrias as tipoDocumento:on:base.id_tipo_documento:tipoDocumento.id";
        joinTables[4] = "left:jofrantoba.catastro.tm_parametrias as tipoPersonaJuridica:on:base.id_tipo_persona_juridica:tipoPersonaJuridica.id";
        joinTables[5] = "left:jofrantoba.catastro.tm_parametrias as tipoUbicacion:on:base.id_tipo_ubicacion:tipoUbicacion.id";
        joinTables[6] = "left:jofrantoba.catastro.tm_departamento as departamento:on:base.id_departamento:departamento.id";
        joinTables[7] = "left:jofrantoba.catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";
        joinTables[8] = "left:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";
        joinTables[9] = "left:(select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:base.id_tipo_via:tipoVia.id";

        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar() throws Exception {
        String table = "jofrantoba.catastro.tgv_titular as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.nombre_ruc as nombreRuc,"));
        strFileds.append(share.append("base.porcentaje as porcentaje,"));
        strFileds.append(share.append("base.codigo_contribuyente as codigoContribuyente,"));
        strFileds.append(share.append("base.numero_documento as numeroDocumento,"));
        strFileds.append(share.append("titularidad.id as idTitularidad,"));
        strFileds.append(share.append("tipoTitular.id as idTipoTitular,"));
        strFileds.append(share.append("tipoTitular.descripcion as tipoTitular,"));
        strFileds.append(share.append("estadoCivil.id as idEstadoCivil,"));
        strFileds.append(share.append("estadoCivil.descripcion as estadoCivil,"));
        strFileds.append(share.append("tipoDocumento.id as idTipoDocumento,"));
        strFileds.append(share.append("tipoDocumento.descripcion as tipoDocumento,"));
        strFileds.append(share.append("tipoPersonaJuridica.id as idTipoPersonaJuridica,"));
        strFileds.append(share.append("tipoPersonaJuridica.descripcion as tipoPersonaJuridica,"));
        strFileds.append(share.append("tipoUbicacion.id as idTipoUbicacion,"));
        strFileds.append(share.append("tipoUbicacion.descripcion as tipoUbicacion,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as departamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as provincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as distrito,"));
        strFileds.append(share.append("tipoVia.id as idTipoVia,"));
        strFileds.append(share.append("tipoVia.descripcion as tipoVia"));
        String fields = strFileds.toString();
        String[] joinTables = new String[10];
          joinTables[0] = "inner:jofrantoba.catastro.tgv_titularidad as titularidad:on:base.id_titularidad:titularidad.id";
        joinTables[1] = "left:jofrantoba.catastro.tm_parametrias as tipoTitular:on:base.id_tipo_titular:tipoTitular.id";
        joinTables[2] = "left:jofrantoba.catastro.tm_parametrias as estadoCivil:on:base.id_estado_civil:estadoCivil.id";
        joinTables[3] = "left:jofrantoba.catastro.tm_parametrias as tipoDocumento:on:base.id_tipo_documento:tipoDocumento.id";
        joinTables[4] = "left:jofrantoba.catastro.tm_parametrias as tipoPersonaJuridica:on:base.id_tipo_persona_juridica:tipoPersonaJuridica.id";
        joinTables[5] = "left:jofrantoba.catastro.tm_parametrias as tipoUbicacion:on:base.id_tipo_ubicacion:tipoUbicacion.id";
        joinTables[6] = "left:jofrantoba.catastro.tm_departamento as departamento:on:base.id_departamento:departamento.id";
        joinTables[7] = "left:jofrantoba.catastro.tm_provincia as provincia:on:base.id_provincia:provincia.id";
        joinTables[8] = "left:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";
        joinTables[9] = "left:(select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:base.id_tipo_via:tipoVia.id";
        String[] mapFilterField = {"=:base.is_persistente:true"};
        String[] mapOrder = {"base.id:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public Long countTipoTitular(Long idTipoTitular) throws Exception {
        String joinTable = "inner:tipoTitular";
        String[] mapFilterField = {"=:tipoTitular.id:" + idTipoTitular, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTitularidad(Long idTitularidad) throws Exception {
        String joinTable = "inner:titularidad";
        String[] mapFilterField = {"=:titularidad.id:" + idTitularidad, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countEstadoCivil(Long idEstadoCivil) throws Exception {
        String joinTable = "inner:estadoCivil";
        String[] mapFilterField = {"=:estadoCivil.id:" + idEstadoCivil, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoDocumento(Long idCountTipoDocumento) throws Exception {
        String joinTable = "inner:tipoDocumento";
        String[] mapFilterField = {"=:tipoDocumento.id:" + idCountTipoDocumento, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoPersonaJuridica(Long idTipoPersonaJuridica) throws Exception {
        String joinTable = "inner:tipoPersonaJuridica";
        String[] mapFilterField = {"=:tipoPersonaJuridica.id:" + idTipoPersonaJuridica, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoUbicacion(Long idTipoUbicacion) throws Exception {
        String joinTable = "inner:tipoTitular";
        String[] mapFilterField = {"=:tipoTitular.id:" + idTipoUbicacion, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countDepartamento(Long idDepartamento) throws Exception {
        String joinTable = "inner:departamento";
        String[] mapFilterField = {"=:departamento.id:" + idDepartamento, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countProvincia(Long idProvincia) throws Exception {
        String joinTable = "inner:provincia";
        String[] mapFilterField = {"=:provincia.id:" + idProvincia, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countDistrito(Long idDistrito) throws Exception {
        String joinTable = "inner:distrito";
        String[] mapFilterField = {"=:distrito.id:" + idDistrito, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countTipoVia(Long idTipoVia) throws Exception {
        String joinTable = "inner:tipoVia";
        String[] mapFilterField = {"=:tipoVia.id:" + idTipoVia, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoTitularidad.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoTitularidad;
import com.jofrantoba.examples.jofrantoba.entity.Titularidad;
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
public class DaoTitularidad extends AbstractJpaDaoV2<Titularidad> implements InterDaoTitularidad {

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
        String table="jofrantoba.catastro.tgv_titularidad as base";
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
        joinTables[0]="inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";        
        joinTables[1]="left:jofrantoba.catastro.tm_parametrias as condicionTitularidad:on:base.id_condicion_titularidad:condicionTitularidad.id";
        joinTables[2]="left:jofrantoba.catastro.tm_parametrias as formaAdquisicion:on:base.id_forma_adquisicion:formaAdquisicion.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tgv_titularidad as base";
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
        joinTables[0]="inner:jofrantoba.catastro.tgv_unidad_catastral as unidadCatastral:on:base.id_unidad_catastral:unidadCatastral.id";        
        joinTables[1]="left:jofrantoba.catastro.tm_parametrias as condicionTitularidad:on:base.id_condicion_titularidad:condicionTitularidad.id";
        joinTables[2]="left:jofrantoba.catastro.tm_parametrias as formaAdquisicion:on:base.id_forma_adquisicion:formaAdquisicion.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
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
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoUnidadCatastral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoUnidadCatastral;
import com.jofrantoba.examples.jofrantoba.dto.beans.CodigoUnidadCatastral;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterUnidadCatastral;
import com.jofrantoba.examples.jofrantoba.entity.UnidadCatastral;
import java.util.ArrayList;
import java.util.Collection;
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
public class DaoUnidadCatastral extends AbstractJpaDaoV2<UnidadCatastral> implements InterDaoUnidadCatastral {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoUnidadCatastral(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(UnidadCatastral.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public ArrayNode listarFilter(FilterUnidadCatastral filter)throws Exception {        
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String groupBy = (String) map.get("groupBy");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String fieldsSq = (String) map.get("fieldsSq");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBySq = (String) map.get("groupBySq");
        return this.allFieldsJoinPostgresGroupBySubQuery(fields, groupBy, mapOrder, joinTables, table, fieldsSq, mapFilterField, groupBySq);
    }

    @Override
    public ArrayNode listar(FilterUnidadCatastral filter,Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String groupBy = (String) map.get("groupBy");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String fieldsSq = (String) map.get("fieldsSq");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBySq = (String) map.get("groupBySq");
        return this.allFieldsLimitJoinPostgresGroupBySubQuery(fields, groupBy, mapOrder, joinTables, table, fieldsSq, mapFilterField, groupBySq, limit, offSet);
    }
    
    @Override
    public ArrayNode listar(FilterUnidadCatastral filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String groupBy = (String) map.get("groupBy");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String fieldsSq = (String) map.get("fieldsSq");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBySq = (String) map.get("groupBySq");
        return this.allFieldsJoinPostgresGroupBySubQuery(fields, groupBy, mapOrder, joinTables, table, fieldsSq, mapFilterField, groupBySq);
    }
    
    @Override
    public Long countLoteCatastral(Long idLoteCatastral) throws Exception {
        String joinTable="inner:loteCatastral";
        String[] mapFilterField={"=:loteCatastral.id:"+idLoteCatastral,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
    
    @Override
    public Long countUsoEspecifico(Long idUsoEspecifico) throws Exception {
        String joinTable="inner:usoEspecifico";
        String[] mapFilterField={"=:usoEspecifico.id:"+idUsoEspecifico,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public Long countClasificacionPredio(Long idClasificacionPredio) throws Exception {
        String joinTable="inner:clasificacionPredio";
        String[] mapFilterField={"=:clasificacionPredio.id:"+idClasificacionPredio,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public Long countPredioEn(Long idPredioEn) throws Exception {
        String joinTable="inner:predioEn";
        String[] mapFilterField={"=:predioEn.id:"+idPredioEn,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }

    @Override
    public String newValueCodigoUnidad(CodigoUnidadCatastral codigoUnidadCatastral) throws Exception {
        String fields = "cast(coalesce(max(base.codigoUnidad),'000') as long)+1";
        String[] mapFilterField = new String[10];
        //mapFilterField[0] = "=:base.isPersistente:true";
        mapFilterField[0] = "equal:base.codigoDepartamento:"+codigoUnidadCatastral.getCodigoDepartamento();
        mapFilterField[1] = "equal:base.codigoProvincia:"+codigoUnidadCatastral.getCodigoProvincia();
        mapFilterField[2] = "equal:base.codigoDistrito:"+codigoUnidadCatastral.getCodigoDistrito();
        mapFilterField[3] = "equal:base.codigoSector:"+codigoUnidadCatastral.getCodigoSector();
        mapFilterField[4] = "equal:base.codigoManzana:"+codigoUnidadCatastral.getCodigoManzana();
        mapFilterField[5] = "equal:base.codigoLote:"+codigoUnidadCatastral.getCodigoLote();
        mapFilterField[6] = "equal:base.codigoEdificacion:"+codigoUnidadCatastral.getCodigoEdificacion();
        mapFilterField[7] = "equal:base.codigoEntrada:"+codigoUnidadCatastral.getCodigoEntrada();
        mapFilterField[8] = "equal:base.codigoPiso:"+codigoUnidadCatastral.getCodigoPiso();
        mapFilterField[9] = "equal:base.tipo:"+"UNIDADCATASTRAL";
        
        Long valueNew = this.aggregateJoinFilterAndGroupBy(fields, null, mapFilterField, null);
        String codigo="000"+valueNew;
        codigo=codigo.substring(codigo.length()-3, codigo.length());
        return codigo;
    }
    
    private Map<String, Object> buildQueryList(FilterUnidadCatastral filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.catastro.tgv_unidad_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("id,"));
        strFileds.append(share.append("idLoteCatastral,"));
        strFileds.append(share.append("codigoUnidadCatastral,"));
        strFileds.append(share.append("codigoLoteCatastral,"));
        strFileds.append(share.append("ubigeo,"));
        strFileds.append(share.append("codigoSector,"));
        strFileds.append(share.append("codigoManzana,"));
        strFileds.append(share.append("codigoLote,"));
        strFileds.append(share.append("codigoEdificacion,"));
        strFileds.append(share.append("codigoEntrada,"));
        strFileds.append(share.append("codigoPiso,"));
        strFileds.append(share.append("codigoUnidad,"));
        strFileds.append(share.append("idManzana,"));
        strFileds.append(share.append("descripcionManzana,"));
        strFileds.append(share.append("idCondicionTitularidad,"));
        strFileds.append(share.append("descripcionCondicionTitularidad,"));
        strFileds.append(share.append("case when"));
        strFileds.append(share.append("(select count(*) from jofrantoba.catastro.tgv_interior ti where ti.id_unidad_catastral =subquery.id and ti.is_persistente =true)>0 then"));
        strFileds.append(share.append("string_agg(direccion, '; ')"));
                strFileds.append(share.append("else '' end as direccion"));
        String fields = strFileds.toString();
        
        StringBuilder strFiledsSq = new StringBuilder();
        strFiledsSq.append(share.append("base.id as id,"));
        strFiledsSq.append(share.append("base.codigo as codigoUnidadCatastral,"));
        strFiledsSq.append(share.append("loteCatastral.codigo as codigoLoteCatastral,"));
        strFiledsSq.append(share.append("loteCatastral.id as idLoteCatastral,"));
        strFiledsSq.append(share.append("concat(base.codigo_departamento,base.codigo_provincia,base.codigo_distrito) as ubigeo,"));
        strFiledsSq.append(share.append("base.codigo_sector as codigoSector,"));
        strFiledsSq.append(share.append("base.codigo_manzana as codigoManzana,"));
        strFiledsSq.append(share.append("base.codigo_lote as codigoLote,"));
        strFiledsSq.append(share.append("base.codigo_edificacion as codigoEdificacion,"));
        strFiledsSq.append(share.append("base.codigo_entrada as codigoEntrada,"));
        strFiledsSq.append(share.append("base.codigo_piso as codigoPiso,"));
        strFiledsSq.append(share.append("base.codigo_unidad as codigoUnidad,"));
        strFiledsSq.append(share.append("manzana.id as idManzana,"));
        strFiledsSq.append(share.append("manzana.descripcion as descripcionManzana,"));
        strFiledsSq.append(share.append("condicionTitularidad.id as idCondicionTitularidad,"));
        strFiledsSq.append(share.append("condicionTitularidad.descripcion as descripcionCondicionTitularidad,"));
        strFiledsSq.append(share.append("concat(tipoVia.descripcion,' ',via.descripcion,' NÂ° ',string_agg(distinct numeracion.numero,','),' ',tipoEdificacion.descripcion,' ',interior.interior,' ',interior.letra_interior) as direccion"));
        String fieldsSq = strFiledsSq.toString();
        
        String[] joinTables = new String[12];
        joinTables[0] = "inner:jofrantoba.catastro.tgv_lote_catastral as loteCatastral:on:base.id_lote_catastral:loteCatastral.id";
        joinTables[1] = "inner:jofrantoba.catastro.tg_manzana as manzana:on:loteCatastral.id_manzana:manzana.id";
        joinTables[2] = "inner:jofrantoba.catastro.tg_sector as sector:on:manzana.id_sector:sector.id";
        joinTables[3] = "left:jofrantoba.catastro.tgv_titularidad as titularidad:on:base.id:titularidad.id_unidad_catastral";
        joinTables[4] = "left:(select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre=96) as condicionTitularidad:on:titularidad.id_condicion_titularidad:condicionTitularidad.id";
        joinTables[5] = "left:jofrantoba.catastro.tgv_numeracion as numeracion:on:loteCatastral.id:numeracion.id_lote_catastral";
        joinTables[6] = "left:jofrantoba.catastro.tgv_interior as interior:on:interior.id_numeracion:numeracion.id:and:interior.id_unidad_catastral:base.id";
        joinTables[7] = "left:jofrantoba.catastro.tm_parametrias as tipoEdificacion:on:interior.id_tipo_edificacion:tipoEdificacion.id";
        joinTables[8] = "left:jofrantoba.catastro.tg_manzana_via as manzanaVia:on:numeracion.id_manzana_via:manzanaVia.id";
        joinTables[9] = "left:jofrantoba.catastro.tg_via_cuadra as viaCuadra:on:manzanaVia.id_via_cuadra:viaCuadra.id";
        joinTables[10] = "left:jofrantoba.catastro.tm_via as via:on:viaCuadra.id_via:via.id";
        joinTables[11] = "left:(select * from jofrantoba.catastro.tm_parametrias where id_parametria_padre=1) as tipoVia:on:via.id_tipo_via:tipoVia.id";
        
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (strValid(filter.getCodigoDepartamento())) {
            filters.add("equal:base.codigo_departamento:" + filter.getCodigoDepartamento());
        }
        if (strValid(filter.getCodigoProvincia())) {
            filters.add("equal:base.codigo_provincia:" + filter.getCodigoProvincia());
        }
        if (strValid(filter.getCodigoDistrito())) {
            filters.add("equal:base.codigo_distrito:" + filter.getCodigoDistrito());
        }
        if (strValid(filter.getCodigoSector())) {
            filters.add("equal:base.codigo_sector:" + filter.getCodigoSector());
        }
        if (strValid(filter.getCodigoManzana())) {
            filters.add("equal:base.codigo_manzana:" + filter.getCodigoManzana());
        }
        if (strValid(filter.getCodigoLote())) {
            filters.add("equal:base.codigo_lote:" + filter.getCodigoLote());
        }
        if (strValid(filter.getCodigoEdificacion())) {
            filters.add("equal:base.codigo_edificacion:" + filter.getCodigoEdificacion());
        }
        if (strValid(filter.getCodigoEntrada())) {
            filters.add("equal:base.codigo_entrada:" + filter.getCodigoEntrada());
        }
        if (strValid(filter.getCodigoPiso())) {
            filters.add("equal:base.codigo_piso:" + filter.getCodigoPiso());
        }
        if (strValid(filter.getCodigoUnidad())) {
            filters.add("equal:base.codigo_unidad:" + filter.getCodigoUnidad());
        }
        if (filter.getIdLoteCatastral() != null) {
            filters.add("=:base.id_lote_catastral:" + filter.getIdLoteCatastral());
        }
        if (filter.getIdUsoEspecifico() != null) {
            filters.add("=:base.id_uso_especifico:" + filter.getIdUsoEspecifico());
        }
        if (filter.getIdClasificacionPredio() != null) {
            filters.add("=:base.id_clasificacion_predio:" + filter.getIdClasificacionPredio());
        }
        if (filter.getIdPredioEn() != null) {
            filters.add("=:base.id_predio_en:" + filter.getIdPredioEn());
        }
        if (filter.getIdManzana() != null) {
            filters.add("=:manzana.id:" + filter.getIdManzana());
        }
       // if (filter.getIdHabilitacionUrbana() != null) {
        //    filters.add("=:habilitacionUrbana.id:" + filter.getIdHabilitacionUrbana());
       // }
        if (filter.getIdDistrito() != null) {
            filters.add("=:sector.id_distrito:" + filter.getIdDistrito());
        }
        if (filter.getIdVia() != null) {
            filters.add("=:via.id:" + filter.getIdVia());
        }
        if (strValid(filter.getNumero())) {
            filters.add("equal:numeracion.numero:" + filter.getNumero());
        }
        if (strValid(filter.getLetra())) {
            filters.add("equal:numeracion.letra:" + filter.getLetra());
        }
        if (strValid(filter.getTipo())) {
            filters.add("equal:base.tipo:" + filter.getTipo());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        StringBuilder groupBySq = new StringBuilder();
        groupBySq.append(share.append("base.id,"));
        groupBySq.append(share.append("base.codigo,"));
        groupBySq.append(share.append("loteCatastral.codigo,"));
        groupBySq.append(share.append("loteCatastral.id,"));
        groupBySq.append(share.append("base.codigo_departamento,"));
        groupBySq.append(share.append("base.codigo_provincia,"));
        groupBySq.append(share.append("base.codigo_distrito,"));
        groupBySq.append(share.append("base.codigo_sector,"));
        groupBySq.append(share.append("base.codigo_manzana,"));
        groupBySq.append(share.append("base.codigo_lote,"));
        groupBySq.append(share.append("base.codigo_edificacion,"));
        groupBySq.append(share.append("base.codigo_entrada,"));
        groupBySq.append(share.append("base.codigo_piso,"));
        groupBySq.append(share.append("base.codigo_unidad,"));
        groupBySq.append(share.append("manzana.id,"));
        groupBySq.append(share.append("manzana.descripcion,"));
        groupBySq.append(share.append("condicionTitularidad.id,"));
        groupBySq.append(share.append("condicionTitularidad.descripcion,"));
        groupBySq.append(share.append("tipoVia.descripcion,"));
        groupBySq.append(share.append("via.descripcion,"));
        groupBySq.append(share.append("tipoEdificacion.descripcion,"));
        groupBySq.append(share.append("interior.interior,"));
        groupBySq.append(share.append("interior.letra_interior"));
        
        StringBuilder groupBy = new StringBuilder();
        groupBy.append(share.append("id,"));
        groupBy.append(share.append("codigoUnidadCatastral,"));        
        groupBy.append(share.append("codigoLoteCatastral,"));        
        groupBy.append(share.append("idLoteCatastral,"));        
        groupBy.append(share.append("ubigeo,"));
        groupBy.append(share.append("codigoSector,"));
        groupBy.append(share.append("codigoManzana,"));
        groupBy.append(share.append("codigoLote,"));
        groupBy.append(share.append("codigoEdificacion,"));
        groupBy.append(share.append("codigoEntrada,"));
        groupBy.append(share.append("codigoPiso,"));
        groupBy.append(share.append("codigoUnidad,"));
        groupBy.append(share.append("idManzana,"));
        groupBy.append(share.append("descripcionManzana,"));
        groupBy.append(share.append("idCondicionTitularidad,"));
        groupBy.append(share.append("descripcionCondicionTitularidad"));
        String[] mapOrder = {"id:asc"};
        map.put("fields", fields);
        map.put("groupBy", groupBy.toString());
        map.put("mapOrder", mapOrder);
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fieldsSq", fieldsSq);
        map.put("mapFilterField", mapFilterField);
        map.put("groupBySq", groupBySq.toString());
        return map;
    }
    
    private boolean strValid(String value) {
        return (value != null && !value.trim().isEmpty());
    }

    @Override
    public Collection<UnidadCatastral> listarIdsBc(String codigoLoteCatastral) throws Exception {
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.codigo as codigo"));
        String fields=strFields.toString();
        String[] mapFilterField={"equal:base.tipo:UNIDADCATASTRAL","equal:base.codigoLoteCatastral:"+codigoLoteCatastral.trim(),"=:base.isPersistente:true"};
        String[] mapOrder={"base.id:asc"};
        Collection<UnidadCatastral> lista=(Collection<UnidadCatastral>)this.customFieldsFilterAnd(UnidadCatastral.class,fields,mapFilterField,mapOrder);        
        return lista;
    }

    @Override
    public Collection<UnidadCatastral> listarIdsEdi(String codigoLoteCatastral) throws Exception {
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.codigo as codigo"));
        String fields=strFields.toString();
        String[] mapFilterField={"equal:base.tipo:BIENCOMUN","equal:base.codigoLoteCatastral:"+codigoLoteCatastral,"notequal:base.codigoEdificacion:99","equal:base.codigoEntrada:99","=:base.isPersistente:true"};
        String[] mapOrder={"base.id:asc"};
        Collection<UnidadCatastral> lista=(Collection<UnidadCatastral>)this.customFieldsFilterAnd(UnidadCatastral.class,fields,mapFilterField,mapOrder);        
        return lista;
    }
    
    @Override
    public ArrayNode listarBienesComunesSinAsociar(String codigoLoteCatastral) throws Exception{
        String table = "jofrantoba.catastro.tgv_unidad_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.codigo_lote_catastral as codigoLoteCatastral"));
        String fields=strFileds.toString();
        String[] joinTables=new String[1];
        joinTables[0]="left:jofrantoba.catastro.tgv_recapitulacion_bc as recapitulacion_bc:on:base.id:recapitulacion_bc.id_unidad_catastral_ref";
        String[] mapFilterField={"isnull:recapitulacion_bc.id","=:base.is_persistente:true","equal:base.tipo:UNIDADCATASTRAL","equal:base.codigo_lote_catastral:"+codigoLoteCatastral};
        String[] mapOrder={"base.id:asc"};                
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public ArrayNode listarEdificionesSinAsociar(String codigoLoteCatastral) throws Exception{
        String table = "jofrantoba.catastro.tgv_unidad_catastral as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.codigo_lote_catastral as codigoLoteCatastral"));
        String fields=strFileds.toString();
        String[] joinTables=new String[1];
        joinTables[0]="left:jofrantoba.catastro.tgv_recapitulacion_edificios as recapitulacion_ed:on:base.id:recapitulacion_ed.id_unidad_catastral_ref";
        String[] mapFilterField={"notequal:base.codigo_edificacion:99","isnull:recapitulacion_ed.id","equal:base.tipo:BIENCOMUN","equal:base.codigo_lote_catastral:"+codigoLoteCatastral,"equal:base.codigo_entrada:99","=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};                
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoUsoEspecifico.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoUsoEspecifico;
import com.jofrantoba.examples.jofrantoba.entity.UsoEspecifico;
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
public class DaoUsoEspecifico extends AbstractJpaDaoV2<UsoEspecifico> implements InterDaoUsoEspecifico {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoUsoEspecifico(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(UsoEspecifico.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<UsoEspecifico> listar(Long idUsoIntermedio)throws Exception {        
        String joinTable="inner:usoIntermedio";
        String[] mapFilterField={"=:usoIntermedio.id:"+idUsoIntermedio,"=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("usoIntermedio.id as idUsoIntermedio,"));
        strFileds.append(share.append("usoIntermedio.descripcion as descripcionUsoIntermedio"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                UsoEspecifico bean=new UsoEspecifico();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<UsoEspecifico> lista=(Collection<UsoEspecifico>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tm_uso_especifico as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("usoIntermedio.id as idUsoIntermedio,"));
        strFields.append(share.append("usoIntermedio.descripcion as descripcionUsoIntermedio,"));
        strFields.append(share.append("usoGeneral.id as idUsoGeneral,"));
        strFields.append(share.append("usoGeneral.descripcion as descripcionUsoGeneral"));
        String fields=strFields.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:jofrantoba.catastro.tm_uso_intermedio as usoIntermedio:on:base.id_uso_intermedio:usoIntermedio.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_uso_general as usoGeneral:on:usoIntermedio.id_uso_general:usoGeneral.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tm_uso_especifico as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("usoIntermedio.id as idUsoIntermedio,"));
        strFields.append(share.append("usoIntermedio.descripcion as descripcionUsoIntermedio,"));
        strFields.append(share.append("usoGeneral.id as idUsoGeneral,"));
        strFields.append(share.append("usoGeneral.descripcion as descripcionUsoGeneral"));
        String fields=strFields.toString();
        String[] joinTables=new String[2];
        joinTables[0]="inner:jofrantoba.catastro.tm_uso_intermedio as usoIntermedio:on:base.id_uso_intermedio:usoIntermedio.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_uso_general as usoGeneral:on:usoIntermedio.id_uso_general:usoGeneral.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long count(Long idUsoIntermedio) throws Exception {
        String joinTable="inner:usoIntermedio";
        String[] mapFilterField={"=:usoIntermedio.id:"+idUsoIntermedio,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoUsoGeneral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoUsoGeneral;
import com.jofrantoba.examples.jofrantoba.entity.UsoGeneral;
import java.util.Collection;
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
public class DaoUsoGeneral extends AbstractJpaDaoV2<UsoGeneral> implements InterDaoUsoGeneral {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoUsoGeneral(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(UsoGeneral.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<UsoGeneral> listar() throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder);
    }

    @Override
    public Collection<UsoGeneral> listar(int pageNumber, int pageSize) throws Exception {
        String mapFilterField="istrue:isPersistente";
        String[] mapOrder={"descripcion:asc"};
        return this.allFields(mapFilterField, mapOrder,pageNumber,pageSize);
    }
    
}

```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoUsoIntermedio.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoUsoIntermedio;
import com.jofrantoba.examples.jofrantoba.entity.UsoIntermedio;
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
public class DaoUsoIntermedio extends AbstractJpaDaoV2<UsoIntermedio> implements InterDaoUsoIntermedio {

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoUsoIntermedio(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(UsoIntermedio.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<UsoIntermedio> listar(Long idUsoGeneral)throws Exception {
        String joinTable="inner:usoGeneral";
        String[] mapFilterField={"=:usoGeneral.id:"+idUsoGeneral,"=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("usoGeneral.id as idUsoGeneral,"));
        strFields.append(share.append("usoGeneral.descripcion as descripcionUsoGeneral"));
        String fields=strFields.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                UsoIntermedio bean=new UsoIntermedio();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<UsoIntermedio> lista=(Collection<UsoIntermedio>)this.customFieldsJoinFilterAnd(rt,fields,joinTable,mapFilterField, mapOrder);        
        return lista;
    }
  
    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tm_uso_intermedio as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("usoGeneral.id as idUsoGeneral,"));
        strFields.append(share.append("usoGeneral.descripcion as descripcionUsoGeneral"));
        String fields=strFields.toString();
        String[] joinTables=new String[1];
        joinTables[0]="inner:jofrantoba.catastro.tm_uso_general as usoGeneral:on:base.id_uso_general:usoGeneral.id";        
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};         
        return this.allFieldsJoinLimitOffsetPostgres(joinTables,table,fields,mapFilterField,mapOrder, limit, offSet, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tm_uso_intermedio as base";
        StringBuilder strFields=new StringBuilder();
        Shared share=new Shared();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("usoGeneral.id as idUsoGeneral,"));
        strFields.append(share.append("usoGeneral.descripcion as descripcionUsoGeneral"));
        String fields=strFields.toString();
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};
        String[] joinTable=new String[1];
        joinTable[0]="inner:jofrantoba.catastro.tm_uso_general as usoGeneral:on:base.id_uso_general:usoGeneral.id";        
        return this.allFieldsJoinPostgres(joinTable,table,fields,mapFilterField,mapOrder, "and");
    }    
    
    @Override
    public Long count(Long idUsoGeneral) throws Exception {
        String joinTable="inner:usoGeneral";
        String[] mapFilterField={"=:usoGeneral.id:"+idUsoGeneral,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoUsuario.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoUsuario;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterUsuario;
import com.jofrantoba.examples.jofrantoba.entity.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jtorresb
 */

@Slf4j
@Repository
public class DaoUsuario extends AbstractJpaDaoV2<Usuario> implements InterDaoUsuario {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoUsuario(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        super();
        this.setClazz(Usuario.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listar(FilterUsuario filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBy = (String) map.get("groupBy");
        StringBuilder sql=this.strAllFieldsJoinPostgresGroupBy(joinTables, table, fields, mapFilterField, mapOrder,groupBy);
        Shared sharedUtil = new Shared();
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                customExecuteStatement(cnctn, sql.toString(), sharedUtil, array,null,null);
            }
        });
        return array;
    }

    @Override
    public ArrayNode listar(FilterUsuario filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        String groupBy = (String) map.get("groupBy");
        StringBuilder sql=this.strAllFieldsJoinGroupByLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder,groupBy,limit,offSet);
        Shared sharedUtil = new Shared();
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                customExecuteStatement(cnctn, sql.toString(), sharedUtil, array,limit,offSet);
            }
        });
        return array;
    }

    private Map<String, Object> buildQueryList(FilterUsuario filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.seguridad.tg_usuario as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("distinct"));
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("userEntity.first_name as firstname,"));
        strFileds.append(share.append("userEntity.last_name as lastname,"));
        strFileds.append(share.append("userEntity.username as username,"));
        strFileds.append(share.append("userEntity.email as email,"));
        strFileds.append(share.append("base.fecha_alta as fechaalta,"));
        strFileds.append(share.append("base.fecha_baja as fechabaja,"));
        strFileds.append(share.append("base.estado as estado,"));
        strFileds.append(share.append("base.is_usuario_jofrantoba as isusuarioJofrantoba,"));
        strFileds.append(share.append("sistema.id as idsistema,"));
        strFileds.append(share.append("sistema.realm_id as idrealm,"));
        strFileds.append(share.append("base.user_entity_id as iduserentity,"));
        strFileds.append(share.append("string_agg(distinct concat('id:',usuarioRol.id_rol,',descripcion:',rol.descripcion,',isrolJofrantoba:',rol.is_rol_jofrantoba::int),';') as arrayroles,"));        
        strFileds.append(share.append("string_agg(distinct concat('id:',usuarioperfil.id_perfil,',descripcion:',perfil.descripcion,',idperfilJofrantoba:',perfil.is_perfil_jofrantoba::int),';') as arrayperfiles"));        
        String fields = strFileds.toString();
        String[] joinTables = new String[6];
        joinTables[0] = "inner:jofrantoba.seguridad.tg_sistema as sistema:on:base.id_sistema:sistema.id";
        joinTables[1] = "inner:jofrantoba.auth.user_entity as userEntity:on:userEntity.realm_id:sistema.realm_id:and:userEntity.realm_id:base.realm_id:and:userEntity.id:base.user_entity_id";
        joinTables[2] = "left:jofrantoba.seguridad.tg_usuario_rol as usuarioRol:on:base.id:usuarioRol.id_usuario:and:usuarioRol.is_persistente:true";
        joinTables[3] = "left:(select * from jofrantoba.seguridad.tg_rol where is_rol_jofrantoba=true and is_persistente=true) as rol:on:usuarioRol.id_rol:rol.id";
        joinTables[4] = "left:jofrantoba.seguridad.tg_usuario_perfil as usuarioPerfil:on:base.id:usuarioPerfil.id_usuario:and:usuarioPerfil.is_persistente:true";
        joinTables[5] = "left:(select * from jofrantoba.seguridad.tg_perfil where is_perfil_jofrantoba=true and is_persistente=true) as perfil:on:usuarioPerfil.id_perfil:perfil.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        filters.add("=:base.is_usuario_jofrantoba:true");
        filters.add("=:base.estado:true");
        if(filter.getIdUserEntity()!=null){
            filters.add("equal:base.user_entity_id:" + filter.getIdUserEntity());
        }
        if (filter.getIdUsuario()!= null) {
            filters.add("=:base.id:" + filter.getIdUsuario());
        }
        if (filter.getIdSistema() != null) {
            filters.add("=:sistema.id:" + filter.getIdSistema());
        }
        if (filter.getEmail() != null) {
            filters.add("equal:base.email:" + filter.getEmail());
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        StringBuilder groupBy = new StringBuilder();
        groupBy.append(share.append("base.id,"));
        groupBy.append(share.append("userEntity.first_name,"));
        groupBy.append(share.append("userEntity.last_name,"));
        groupBy.append(share.append("userEntity.username,"));
        groupBy.append(share.append("userEntity.email,"));
        groupBy.append(share.append("base.fecha_alta,"));
        groupBy.append(share.append("base.fecha_baja,"));
        groupBy.append(share.append("base.estado,"));
        groupBy.append(share.append("base.is_usuario_jofrantoba,"));
        groupBy.append(share.append("sistema.id,"));
        groupBy.append(share.append("sistema.realm_id,"));
        groupBy.append(share.append("base.user_entity_id"));
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("groupBy", groupBy.toString());
        map.put("mapOrder", mapOrder);
        return map;
    }
    
    private void customExecuteStatement(Connection cnctn, String sql, Shared sharedUtil, ArrayNode array, Long limit, Long offset) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = cnctn.prepareStatement(sql);
            if (limit != null && offset != null) {
                ps.setLong(1, limit);
                ps.setLong(2, offset);
            }
            rs = ps.executeQuery();
            ResultSetMetaData metadata = rs.getMetaData();
            int size = metadata.getColumnCount();
            while (rs.next()) {
                ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
                for (int i = 1; i <= size; i++) {
                    typesSet(node, rs, metadata, i);                    
                }
                array.add(node);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            sharedUtil.closePreparedStatement(ps);
            sharedUtil.closeResultSet(rs);
        }
    }
    
    private void typesSet(ObjectNode node, ResultSet rs, ResultSetMetaData metadata, int i) throws SQLException {
        log.error(metadata.getColumnLabel(i));
        log.error(metadata.getColumnClassName(i));
        log.error(metadata.getColumnName(i));
        log.error(metadata.getColumnTypeName(i));        
        if (metadata.getColumnTypeName(i).equals("numeric")) {
            node.put(metadata.getColumnName(i), rs.getBigDecimal(metadata.getColumnName(i)));
        }
        if (metadata.getColumnTypeName(i).equals("text")) {
            String value=rs.getString(metadata.getColumnName(i));
            String columName=metadata.getColumnName(i);
            if(columName.equals("arrayroles")){
                arrayField(value,node,"roles");
            }else if(columName.equals("arrayperfiles")){
                arrayField(value,node,"perfiles");
            }else{                
             node.put(columName, value);               
            }            
        }
        if (metadata.getColumnTypeName(i).equals("varchar")) {
            node.put(metadata.getColumnName(i), rs.getString(metadata.getColumnName(i)));
        }
        if (metadata.getColumnTypeName(i).equals("serial")) {
            node.put(metadata.getColumnName(i), rs.getLong(metadata.getColumnName(i)));
        }
        if (metadata.getColumnTypeName(i).equals("int8") || metadata.getColumnTypeName(i).equals("int4")) {
            node.put(metadata.getColumnName(i), rs.getLong(metadata.getColumnName(i)));
        }
        if (metadata.getColumnTypeName(i).equals("bool")) {
            node.put(metadata.getColumnName(i), rs.getBoolean(metadata.getColumnName(i)));
        }
        if (metadata.getColumnTypeName(i).equals("date")) {
            java.sql.Date fecha = rs.getDate(metadata.getColumnName(i));
            if (fecha != null) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String fechaStr = df.format(new java.util.Date(fecha.getTime()));
                node.put(metadata.getColumnName(i) + "longtime", fecha.getTime());
                node.put(metadata.getColumnName(i), fechaStr);
            } else {
                node.put(metadata.getColumnName(i) + "longtime", 0);
                node.put(metadata.getColumnName(i), "");
            }
        }
    }
    
    private void arrayField(String value,ObjectNode node,String nameField){
                String[] listado=value.split(";");
                ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
                for(int j=0;j<listado.length;j++){
                    String[] campos=listado[j].split(",");
                    if(campos.length==3 && campos[2].split(":").length==2 && campos[2].split(":")[1].equals("1")){
                        ObjectNode nodeArray = new ObjectNode(JsonNodeFactory.instance);                                        
                        String[] id=campos[0].split(":");
                        String[] descripcion=campos[1].split(":");
                        nodeArray.put(id[0],id[1]);
                        nodeArray.put(descripcion[0],descripcion[1]);
                        array.add(nodeArray);
                    }                    
                }
                node.putArray(nameField).addAll(array);
    }

    @Override
    public ArrayNode idPerfiles(FilterUsuario filter) throws Exception {
        String table = "jofrantoba.seguridad.tg_usuario as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("string_agg(distinct cast(perfil.id as varchar),':') as idperfiles"));
        String fields = strFileds.toString();
        String[] joinTables = new String[3];
        joinTables[0] = "inner:jofrantoba.seguridad.tg_sistema as sistema:on:base.id_sistema:sistema.id";
        joinTables[1] = "inner:jofrantoba.seguridad.tg_usuario_perfil as userPerfil:on:base.id:userPerfil.id_usuario";
        joinTables[2] = "inner:jofrantoba.seguridad.tg_perfil as perfil:on:userPerfil.id_perfil:perfil.id";        
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        filters.add("=:base.is_usuario_jofrantoba:true");
        filters.add("=:base.estado:true");
        if(filter.getIdUserEntity()!=null){
            filters.add("equal:base.user_entity_id:" + filter.getIdUserEntity());
        }
        if (filter.getIdUsuario()!= null) {
            filters.add("=:base.id:" + filter.getIdUsuario());
        }
        if (filter.getIdSistema() != null) {
            filters.add("=:sistema.id:" + filter.getIdSistema());
        }
        if (filter.getEmail() != null) {
            filters.add("equal:base.email:" + filter.getEmail());
        }
        String[] mapFilterField = filters.toArray(new String[0]);        
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, null, "and");
    }
        
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoUsuarioProyecto.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoUsuarioProyecto;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterUsuarioProyecto;
import com.jofrantoba.examples.jofrantoba.entity.UsuarioProyecto;
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
public class DaoUsuarioProyecto extends AbstractJpaDaoV2<UsuarioProyecto> implements InterDaoUsuarioProyecto {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoUsuarioProyecto() {
        super();
        this.setClazz(UsuarioProyecto.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public ArrayNode listarFilter(FilterUsuarioProyecto filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode listar(FilterUsuarioProyecto filter, Long limit, Long offSet) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterUsuarioProyecto filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    private Map<String, Object> buildQueryList(FilterUsuarioProyecto filter) {
        Map<String, Object> map = new HashMap();
        String table = "jofrantoba.seguridad.tg_usuario_proyecto as base";
        StringBuilder strFileds = new StringBuilder();
        Shared share = new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento,"));
        strFileds.append(share.append("departamento.codigo_departamento as codigoDepartamento,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFileds.append(share.append("provincia.codigo_provincia as codigoProvincia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("distrito.codigo_distrito as codigoDistrito,"));
        strFileds.append(share.append("proyecto.id as idProyecto,"));
        strFileds.append(share.append("proyecto.descripcion as descripcionProyecto,"));
        strFileds.append(share.append("usuario.id as idUsuario,"));        
        strFileds.append(share.append("usuario.username as userName"));                
        String fields = strFileds.toString();
        String[] joinTables = new String[5];
        joinTables[0] = "inner:jofrantoba.catastro.tg_proyecto as proyecto:on:base.id_proyecto:proyecto.id";
        joinTables[1] = "inner:jofrantoba.seguridad.tg_usuario as usuario:on:base.id_usuario:usuario.id";
        joinTables[2] = "inner:jofrantoba.catastro.tm_distrito as distrito:on:proyecto.id_distrito:distrito.id";
        joinTables[3] = "inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";
        joinTables[4] = "inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        List<String> filters = new ArrayList();
        filters.add("=:base.is_persistente:true");
        if (filter.getIdDistrito()!= null) {
            filters.add("=:distrito.id:" + filter.getIdDistrito());
        }
        if (filter.getIdProyecto()!= null) {
            filters.add("=:proyecto.id:" + filter.getIdProyecto());
        }
        if (filter.getIdUsuario()!= null) {
            filters.add("=:usuario.id:" + filter.getIdUsuario());
        }

        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"base.id:asc"};
        map.put("joinTables", joinTables);
        map.put("table", table);
        map.put("fields", fields);
        map.put("mapFilterField", mapFilterField);
        map.put("mapOrder", mapOrder);
        return map;
    }

    @Override
    public Long countProyecto(Long idProyecto) throws Exception {
        String joinTable = "inner:proyecto";
        String[] mapFilterField = {"=:proyecto.id:" + idProyecto, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countUsuario(Long idUsuario) throws Exception {
        String joinTable = "inner:user";
        String[] mapFilterField = {"=:user.id:" + idUsuario, "=:base.isPersistente:true"};
        return this.rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long countUsuarioProyecto(Long idProyecto, Long idUsuario) throws Exception {
        String[] joinTables = new String[2];
        joinTables[0] = "inner:proyecto";
        joinTables[1] = "inner:user";
        String[] mapFilterField={"=:proyecto.id:"+idProyecto,"=:user.id:"+idUsuario,"=:base.isPersistente:true"};        
        return this.rowCountJoinsFilterAnd(joinTables,mapFilterField); 
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoVia.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoVia;
import com.jofrantoba.examples.jofrantoba.entity.Via;
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
public class DaoVia extends AbstractJpaDaoV2<Via> implements InterDaoVia {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoVia() {
        super();
        this.setClazz(Via.class);
        //this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<Via> listar(Long idDistrito)throws Exception {        
        String[] joinTables={"inner:distrito","inner:tipoVia"};
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.codigoVia as codigoVia,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("tipoVia.id as idTipoVia,"));
        strFileds.append(share.append("tipoVia.descripcion as descripcionTipoVia"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Via bean=new Via();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Via> lista=(Collection<Via>)this.customFieldsJoinFilterAnd(rt,fields,joinTables,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tm_via as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.codigo_via as codigoVia,"));    
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields=strFileds.toString();
        String[] joinTables=new String[3];
        joinTables[0]="inner:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[2]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tm_via as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("base.codigo_via as codigoVia,"));        
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields=strFileds.toString();
        String[] joinTables=new String[3];
        joinTables[0]="inner:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[2]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long count(Long idDistrito) throws Exception {
        String joinTable="inner:distrito";
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoViaCuadra.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoViaCuadra;
import com.jofrantoba.examples.jofrantoba.entity.ViaCuadra;
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
public class DaoViaCuadra extends AbstractJpaDaoV2<ViaCuadra> implements InterDaoViaCuadra {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoViaCuadra() {
        super();
        this.setClazz(ViaCuadra.class);
        this.setSessionFactory(sessionFactory);
    }       

    @Override
    public Collection<ViaCuadra> listar(Long idVia)throws Exception {        
        String[] joinTables={"inner:via","inner:lado"};
        String[] mapFilterField={"=:via.id:"+idVia,"=:base.isPersistente:true"};
        String[] mapOrder={"base.id:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));      
        strFileds.append(share.append("base.cuadra as cuadra,"));
        strFileds.append(share.append("via.id as idVia,"));
        strFileds.append(share.append("via.descripcion as descripcionVia,"));
        strFileds.append(share.append("lado.id as idLado,"));
        strFileds.append(share.append("lado.descripcion as descripcionLado"));
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                ViaCuadra bean=new ViaCuadra();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<ViaCuadra> lista=(Collection<ViaCuadra>)this.customFieldsJoinFilterAnd(rt,fields,joinTables,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tg_via_cuadra as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.cuadra as cuadra,"));        
        strFileds.append(share.append("via.id as idVia,"));
        strFileds.append(share.append("via.descripcion as descripcionVia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields=strFileds.toString();
        String[] joinTables=new String[4];
        joinTables[0]="inner:jofrantoba.catastro.tm_via as via:on:base.id_via:via.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_distrito as distrito:on:via.id_distrito:distrito.id";        
        joinTables[2]="inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[3]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";        
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tg_via_cuadra as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.cuadra as cuadra,"));        
        strFileds.append(share.append("via.id as idVia,"));
        strFileds.append(share.append("via.descripcion as descripcionVia,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields=strFileds.toString();
        String[] joinTables=new String[4];
        joinTables[0]="inner:jofrantoba.catastro.tm_via as via:on:base.id_via:via.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_distrito as distrito:on:via.id_distrito:distrito.id";        
        joinTables[2]="inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[3]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";        
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long count(Long idVia) throws Exception {
        String joinTable="inner:via";
        String[] mapFilterField={"=:via.id:"+idVia,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\impl\DaoZonificacion.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoZonificacion;
import com.jofrantoba.examples.jofrantoba.entity.Zonificacion;
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
public class DaoZonificacion extends AbstractJpaDaoV2<Zonificacion> implements InterDaoZonificacion {
    
    @Autowired(required = false)
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
    
    @PostConstruct
    public void init() {
        if (this.getSessionFactory()==null && sessionFactory != null) {
            this.setSessionFactory(sessionFactory);
        }
    }
    
    public DaoZonificacion() {
        super();
        this.setClazz(Zonificacion.class);
        //this.setSessionFactory(sessionFactory);
    }

    @Override
    public Collection<Zonificacion> listar(Long idDistrito)throws Exception {        
        String[] joinTables={"inner:distrito"};
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};
        String[] mapOrder={"base.descripcion:asc"};
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.abreviatura as abreviatura,"));
        strFileds.append(share.append("base.orden as orden,"));
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito"));        
        String fields=strFileds.toString();
        ResultTransformer rt=new ResultTransformer(){
            @Override
            public Object transformTuple(Object[] os, String[] strings) {
                Zonificacion bean=new Zonificacion();
                bean.setTransformer(os, strings);
                return bean;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        };
        Collection<Zonificacion> lista=(Collection<Zonificacion>)this.customFieldsJoinFilterAnd(rt,fields,joinTables,mapFilterField, mapOrder);        
        return lista;
    }

    @Override
    public ArrayNode listar(Long limit, Long offSet) throws Exception {
        String table="jofrantoba.catastro.tm_zonificacion as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.abreviatura as abreviatura,"));
        strFileds.append(share.append("base.orden as orden,"));    
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields=strFileds.toString();
        String[] joinTables=new String[3];
        joinTables[0]="inner:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[2]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public ArrayNode listar() throws Exception {
        String table="jofrantoba.catastro.tm_zonificacion as base";
        StringBuilder strFileds=new StringBuilder();
        Shared share=new Shared();
        strFileds.append(share.append("base.id as id,"));
        strFileds.append(share.append("base.descripcion as descripcion,"));
        strFileds.append(share.append("base.abreviatura as abreviatura,"));
        strFileds.append(share.append("base.orden as orden,"));    
        strFileds.append(share.append("distrito.id as idDistrito,"));
        strFileds.append(share.append("distrito.descripcion as descripcionDistrito,"));
        strFileds.append(share.append("provincia.id as idProvincia,"));
        strFileds.append(share.append("provincia.descripcion as descripcionProvincia,"));
        strFileds.append(share.append("departamento.id as idDepartamento,"));
        strFileds.append(share.append("departamento.descripcion as descripcionDepartamento"));
        String fields=strFileds.toString();
        String[] joinTables=new String[3];
        joinTables[0]="inner:jofrantoba.catastro.tm_distrito as distrito:on:base.id_distrito:distrito.id";        
        joinTables[1]="inner:jofrantoba.catastro.tm_provincia as provincia:on:distrito.id_provincia:provincia.id";        
        joinTables[2]="inner:jofrantoba.catastro.tm_departamento as departamento:on:provincia.id_departamento:departamento.id";
        String[] mapFilterField={"=:base.is_persistente:true"};
        String[] mapOrder={"base.id:asc"};        
        return this.allFieldsJoinPostgres(joinTables,table,fields,mapFilterField,mapOrder, "and");
    }
    
    @Override
    public Long count(Long idDistrito) throws Exception {
        String joinTable="inner:distrito";
        String[] mapFilterField={"=:distrito.id:"+idDistrito,"=:base.isPersistente:true"};        
        return this.rowCountJoinFilterAnd(joinTable,mapFilterField);                
    }
    
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoActividadEconomica.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterActividadEconomica;
import com.jofrantoba.examples.jofrantoba.entity.ActividadEconomica;

/**
 *
 * @author jtorresb
 */
public interface InterDaoActividadEconomica extends InterCrud<ActividadEconomica> {

    ArrayNode listarFilter(FilterActividadEconomica filter) throws Exception;

    ArrayNode listar(FilterActividadEconomica filter, Long limit, Long offSet) throws Exception;

    ArrayNode listar(FilterActividadEconomica filter) throws Exception;

    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;

    Long couhtTipoDocIdent(Long idTipoDocIdent) throws Exception;

    Long countTipoConductor(Long idTipoConductor) throws Exception;

    Long countCondicionConductor(Long idCondicionConductor) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoAutorizacionAnuncio.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterAutorizacionAnuncio;
import com.jofrantoba.examples.jofrantoba.entity.AutorizacionAnuncio;

/**
 *
 * @author jtorresb
 */
public interface InterDaoAutorizacionAnuncio extends InterCrud<AutorizacionAnuncio> {

    ArrayNode listarFilter(FilterAutorizacionAnuncio filter) throws Exception;

    ArrayNode listar(FilterAutorizacionAnuncio filter, Long limit, Long offSet) throws Exception;

    ArrayNode listar(FilterAutorizacionAnuncio filter) throws Exception;

    Long countActividadEconomica(Long idActividadEconomica) throws Exception;

    Long countAnuncio(Long idAnuncio) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoAutorizacionMunicipal.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterAutorizacionMunicipal;
import com.jofrantoba.examples.jofrantoba.entity.AutorizacionMunicipal;

/**
 *
 * @author jtorresb
 */
public interface InterDaoAutorizacionMunicipal extends InterCrud<AutorizacionMunicipal> {

    ArrayNode listarFilter(FilterAutorizacionMunicipal filter) throws Exception;

    ArrayNode listar(FilterAutorizacionMunicipal filter, Long limit, Long offSet) throws Exception;

    ArrayNode listar(FilterAutorizacionMunicipal filter) throws Exception;

    Long countActividadEconomica(Long idActividadEconomica) throws Exception;

    Long countTipoActEconoEspecifico(Long idTipoActEconoEspecifico) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoCategoria.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterCategoria;
import com.jofrantoba.examples.jofrantoba.entity.Categoria;

/**
 *
 * @author jtorresb
 */


public interface InterDaoCategoria extends InterCrud<Categoria>{
    ArrayNode listar(FilterCategoria filter) throws Exception;
    ArrayNode listar(FilterCategoria filter, Long limit, Long offSet) throws Exception;
    Long count(Long idDistrito) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoClasificacion.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterClasificacion;
import com.jofrantoba.examples.jofrantoba.entity.Clasificacion;

/**
 *
 * @author jtorresb
 */


public interface InterDaoClasificacion extends InterCrud<Clasificacion>{
    ArrayNode listar(FilterClasificacion filter) throws Exception;
    ArrayNode listar(FilterClasificacion filter, Long limit, Long offSet) throws Exception;
    Long count(Long idCategoria) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoClienteSistema.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.ClienteSistema;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoClienteSistema extends InterCrud<ClienteSistema>{    
    Collection<ClienteSistema> listar(Long idDepartamento)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idDepartamento) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoConductorDomicilio.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterConductorDomicilio;
import com.jofrantoba.examples.jofrantoba.entity.ConductorDomicilio;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoConductorDomicilio extends InterCrud<ConductorDomicilio> {

    Collection<ConductorDomicilio> listarFilter(FilterConductorDomicilio filter) throws Exception;

    ArrayNode listar(Long limit, Long offSet) throws Exception;

    ArrayNode listar() throws Exception;

    Long countActividadEconomica(Long idActividadEconomica) throws Exception;

    Long countDepartamento(Long idDepartamento) throws Exception;

    Long countProvincia(Long idProvincia) throws Exception;

    Long countDistrito(Long idDistrito) throws Exception;

    Long countTipoVia(Long idTipoVia) throws Exception;

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoConstrucciones.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterConstrucciones;
import com.jofrantoba.examples.jofrantoba.entity.Construcciones;

/**
 *
 * @author jtorresb
 */
public interface InterDaoConstrucciones extends InterCrud<Construcciones>{    
    ArrayNode listarFilter(FilterConstrucciones filter)throws Exception;
    ArrayNode listar(FilterConstrucciones filter,Long limit, Long offSet)throws Exception;
    ArrayNode listar(FilterConstrucciones filter) throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countNivel(Long idNivel) throws Exception;
    Long countMes(Long idMes) throws Exception;
    Long countMaterialEstructural(Long idMaterialEstructural) throws Exception;
    Long countEstadoConservacion(Long idEstadoConservacion) throws Exception;
    Long countEstadoConstruccion(Long idEstadoConstruccion) throws Exception;
    Long countUca(Long idUca) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoCucUnidad.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.CucUnidad;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoCucUnidad extends InterCrud<CucUnidad>{
    Collection<CucUnidad> listar(Long idLoteCatastral) throws Exception;
    ArrayNode listar(Long limit, Long offset) throws Exception;
    ArrayNode listar() throws Exception;
    Long count(Long idLoteCatastral) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoDepartamento.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.Departamento;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoDepartamento extends InterCrud<Departamento>{
    Collection<Departamento> listar() throws Exception;
    Collection<Departamento> listar(int pageNumber, int pageSize) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoDistrito.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.Distrito;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoDistrito extends InterCrud<Distrito>{    
    Collection<Distrito> listar(Long idProvincia)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idProvincia) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoDocumentos.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterDocumentos;
import com.jofrantoba.examples.jofrantoba.entity.Documentos;

/**
 *
 * @author jtorresb
 */
public interface InterDaoDocumentos extends InterCrud<Documentos>{    
    ArrayNode listarFilter(FilterDocumentos filter)throws Exception;
    ArrayNode listar(FilterDocumentos filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterDocumentos filter)throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countTipoDocumento(Long idTipoDocumento) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoFileLoteCatastral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterFile;
import com.jofrantoba.examples.jofrantoba.entity.FileLoteCatastral;

/**
 *
 * @author jtorresb
 */


public interface InterDaoFileLoteCatastral extends InterCrud<FileLoteCatastral>{
    ArrayNode listar(FilterFile filter) throws Exception;
    ArrayNode listar(FilterFile filter, Long limit, Long offSet) throws Exception;
    void asignarPrincipal(Long id) throws Exception;
    void limpiarPrincipal(Long idLoteCatastral) throws Exception ;
    //Long count(Long idCategoria) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoFileUnidadCatastral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterFile;
import com.jofrantoba.examples.jofrantoba.entity.FileUnidadCatastral;

/**
 *
 * @author jtorresb
 */


public interface InterDaoFileUnidadCatastral extends InterCrud<FileUnidadCatastral>{
    ArrayNode listar(FilterFile filter) throws Exception;
    ArrayNode listar(FilterFile filter, Long limit, Long offSet) throws Exception;
    void asignarPrincipal(Long id) throws Exception;
    void limpiarPrincipal(Long idUnidadCatastral) throws Exception ;
    //Long count(Long idCategoria) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoHabilitacionUrbana.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.HabilitacionUrbana;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoHabilitacionUrbana extends InterCrud<HabilitacionUrbana>{
    Collection<HabilitacionUrbana> listar(Long idDistrito,Long idTipoHabilitacionUrbana,String descripcion)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idDistrito) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoInformacionCompl.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterInformacionCompl;
import com.jofrantoba.examples.jofrantoba.entity.InformacionCompl;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoInformacionCompl extends InterCrud<InformacionCompl> {

    Collection<InformacionCompl> listarFilter(FilterInformacionCompl filter) throws Exception;

    ArrayNode listar(Long limit, Long offSet) throws Exception;

    ArrayNode listar() throws Exception;

    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;

    Long countCondicionDeclarante(Long idCondicionDeclarante) throws Exception;

    Long countestadoLlenado(Long idEstadoLlenado) throws Exception;

    Long countDocumentoDecla(Long idDocumentoDecla) throws Exception;

    Long countMantenimiento(Long idMantenimiento) throws Exception;

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoInformacionComplActEcon.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterInformacionComplActEcon;
import com.jofrantoba.examples.jofrantoba.entity.InformacionComplActEcon;

/**
 *
 * @author jtorresb
 */
public interface InterDaoInformacionComplActEcon extends InterCrud<InformacionComplActEcon> {

    ArrayNode listarFilter(FilterInformacionComplActEcon filter) throws Exception;

    ArrayNode listar(FilterInformacionComplActEcon filter, Long limit, Long offSet) throws Exception;

    ArrayNode listar(FilterInformacionComplActEcon filter) throws Exception;

    Long countActividadEconomica(Long idActividadEconomica) throws Exception;

    Long countCondicionDeclarante(Long idCondicionDeclarante) throws Exception;

    Long countestadoLlenado(Long idEstadoLlenado) throws Exception;

    Long countTipoDocumentoDecla(Long idTipoDocumentoDecla) throws Exception;

    Long countMantenimiento(Long idMantenimiento) throws Exception;

    Long countDocumentoPresentado(Long idDocumentoPresentado) throws Exception;

;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoInscripcionCatastral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterInscripcionCatastral;
import com.jofrantoba.examples.jofrantoba.entity.InscripcionCatastral;

/**
 *
 * @author jtorresb
 */
public interface InterDaoInscripcionCatastral extends InterCrud<InscripcionCatastral>{    
    ArrayNode listarFilter(FilterInscripcionCatastral filter)throws Exception;
    ArrayNode listar(FilterInscripcionCatastral filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterInscripcionCatastral filter)throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countTipoPartidaRegistral(Long idTipoPartidaCatastral) throws Exception;
    Long countDeclaratoriaFabrica(Long idDeclaratoriaFabrica) throws Exception;

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoInterior.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterInterior;
import com.jofrantoba.examples.jofrantoba.entity.Interior;

/**
 *
 * @author jtorresb
 */
public interface InterDaoInterior extends InterCrud<Interior>{    
    ArrayNode listarFilter(FilterInterior filter)throws Exception;
    ArrayNode listar(FilterInterior filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterInterior filter)throws Exception;;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countNumeracion(Long idNumeracion) throws Exception;
    Long countTipoEdificacion(Long idTipoEdificacion) throws Exception;
    Long countTipoInterior(Long idTipoInterior) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoLinderos.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.Linderos;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoLinderos extends InterCrud<Linderos>{    
    Collection<Linderos> listarFilter(Long idLoteCatastral,Long idTipoLindero)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long countLoteCatastral(Long idLoteCatastral) throws Exception;
    Long countTipoLindero(Long idTipoLindero) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoLitigantes.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterLitigantes;
import com.jofrantoba.examples.jofrantoba.entity.Litigantes;

/**
 *
 * @author jtorresb
 */
public interface InterDaoLitigantes extends InterCrud<Litigantes>{    
    ArrayNode listarFilter(FilterLitigantes filter)throws Exception;
    ArrayNode listar(FilterLitigantes filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterLitigantes filter)throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countTipoDocumento(Long idTipoDocumento) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoLogsEntityOperation.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.LogsEntityOperation;

/**
 *
 * @author jtorresb
 */
public interface InterDaoLogsEntityOperation extends InterCrud<LogsEntityOperation>{
    
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoLoteCatastral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.CodigoLoteCatastral;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterLoteCatastral;
import com.jofrantoba.examples.jofrantoba.entity.LoteCatastral;

/**
 *
 * @author jtorresb
 */
public interface InterDaoLoteCatastral extends InterCrud<LoteCatastral>{    
    ArrayNode listarFilter(FilterLoteCatastral filter)throws Exception;
    ArrayNode listar(FilterLoteCatastral filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterLoteCatastral filter)throws Exception;
    ArrayNode listarLotes(Long idManzana)throws Exception;
    Long countManzana(Long idManzana) throws Exception;
    //Long countHabilitacionUrbana(Long idHabilitacionUrbana) throws Exception;
    String newValueCodigoLote(CodigoLoteCatastral codigoLoteCatastral)throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoLoteHabilitacionUrbana.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterLoteHabilitacionUrbana;
import com.jofrantoba.examples.jofrantoba.entity.LoteHabilitacionUrbana;

/**
 *
 * @author jtorresb
 */
public interface InterDaoLoteHabilitacionUrbana extends InterCrud<LoteHabilitacionUrbana> {

    ArrayNode listarFilter(FilterLoteHabilitacionUrbana filter) throws Exception;

    ArrayNode listar(FilterLoteHabilitacionUrbana filter, Long limit, Long offSet) throws Exception;

    ArrayNode listar(FilterLoteHabilitacionUrbana filter) throws Exception;

    Long countLoteCatastral(Long idCountLoteCatastral) throws Exception;

    Long countHabilitacionUrbana(Long idHabilitacionUrbana) throws Exception;
    
    Long countLoteHabilitacionUrbana(Long idLoteCatastral,Long idHabilitacionUrbana) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoLoteZonificacion.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.LoteZonificacion;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoLoteZonificacion extends InterCrud<LoteZonificacion>{    
    Collection<LoteZonificacion> listarFilter(Long idLoteCatastral,Long idZonificacion)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long countLoteCatastral(Long idLoteCatastral) throws Exception;
    Long countZonificacion(Long idZonificacion) throws Exception;
    Long countLoteZonificacion(Long idLoteCatastral,Long idZonificacion)throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoManzana.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.Manzana;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoManzana extends InterCrud<Manzana>{    
    Collection<Manzana> listar(Long idDistrito)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idSector) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoManzanaVia.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterManzanaVia;
import com.jofrantoba.examples.jofrantoba.entity.ManzanaVia;

/**
 *
 * @author jtorresb
 */
public interface InterDaoManzanaVia extends InterCrud<ManzanaVia>{    
    ArrayNode listarFilter(FilterManzanaVia filter)throws Exception;
    ArrayNode listar(FilterManzanaVia filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterManzanaVia filter)throws Exception;
    Long countManzana(Long idManzana) throws Exception;
    Long countViaCuadra(Long idViaCuadra) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoMenu.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterMenu;
import com.jofrantoba.examples.jofrantoba.entity.Menu;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoMenu extends InterCrud<Menu>{
    Collection<Menu> parents(FilterMenu filter)throws Exception;
    Collection<Menu> parents(FilterMenu filter,int pageNumber, int pageSize)throws Exception;
    Collection<Menu> childrens(FilterMenu filter)throws Exception;
    Collection<Menu> childrensByParents(Long idParent)throws Exception;
    ArrayNode listar(FilterMenu filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterMenu filter)throws Exception;
    Long countChildrens(Long idParent)throws Exception;
    Long maxOrdenChildrens(Long idParent) throws Exception;
    Long maxOrdenNivel(Long idClienteSistema,Long nivel) throws Exception;
    ArrayNode createTreeMenu(FilterMenu filter)throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoNumeracion.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.Numeracion;

/**
 *
 * @author jtorresb
 */
public interface InterDaoNumeracion extends InterCrud<Numeracion>{    
    ArrayNode listarFilter(Long idLoteCatastral,Long idManzanaVia,Long idTipoPuerta, Long idCondicionNumeracion)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long countLoteCatastral(Long idLoteCatastral) throws Exception;
    Long countManzanaVia(Long idManzanaVia) throws Exception;
    Long countTipoPuerta(Long idTipoPuerta) throws Exception;
    Long countCondicionNumeracion(Long idCondicionNumeracion) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoOtrasInstalaciones.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterOtrasInstalaciones;
import com.jofrantoba.examples.jofrantoba.entity.OtrasInstalaciones;

/**
 *
 * @author jtorresb
 */
public interface InterDaoOtrasInstalaciones extends InterCrud<OtrasInstalaciones> {

    ArrayNode listarFilter(FilterOtrasInstalaciones filter) throws Exception;  
    
    ArrayNode listar(FilterOtrasInstalaciones filter,Long limit, Long offSet) throws Exception;
    
    ArrayNode listar(FilterOtrasInstalaciones filter) throws Exception;

    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;

    Long countTipoOtrasInstalaciones(Long idTipoOtrasInstalaciones) throws Exception;

    Long counntMes(Long counntMes) throws Exception;

    Long countMaterialEstructural(Long idMaterialEstructural) throws Exception;

    Long countEstadoConservacion(Long idEstadoConservacion) throws Exception;

    Long countEstadoConstruccion(Long idEstadoConstruccion) throws Exception;

    Long countUca(Long idUca) throws Exception;

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoParametrias.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.Parametrias;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoParametrias extends InterCrud<Parametrias>{
    Collection<Parametrias> parents()throws Exception;
    Collection<Parametrias> parents(int pageNumber, int pageSize)throws Exception;
    Collection<Parametrias> childrens()throws Exception;
    Collection<Parametrias> childrensByParents(Long idParent)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long countChildrens(Long idParent)throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoPerfil.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterPerfil;
import com.jofrantoba.examples.jofrantoba.entity.Perfil;

/**
 *
 * @author jtorresb
 */


public interface InterDaoPerfil extends InterCrud<Perfil>{
    ArrayNode listar(FilterPerfil filter) throws Exception;
    ArrayNode listar(FilterPerfil filter, Long limit, Long offSet) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoPerfilMenu.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterPerfilMenu;
import com.jofrantoba.examples.jofrantoba.entity.PerfilMenu;

/**
 *
 * @author jtorresb
 */
public interface InterDaoPerfilMenu extends InterCrud<PerfilMenu>{   
    ArrayNode listar(FilterPerfilMenu filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterPerfilMenu filter)throws Exception;
    Long countPerfil(Long idPerfil) throws Exception;
    Long countMenu(Long idMenu) throws Exception;
    int deletePerfilMenu(Long idPerfil)throws Exception;
    ArrayNode createTreeMenu(FilterPerfilMenu filter) throws Exception;
    ArrayNode createTreeMenuPerfiles(FilterPerfilMenu filter) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoPkTable.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterPkTable;
import com.jofrantoba.examples.jofrantoba.entity.PkTable;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoPkTable extends InterCrud<PkTable>{
    Collection<PkTable> listar() throws Exception;
    Collection<PkTable> listar(int pageNumber, int pageSize) throws Exception;
    Collection<PkTable> listar(FilterPkTable filter)throws Exception;
    Long count(String tabla, String codigo) throws Exception;
    int deletePkbyLikeCodigo(String codigo) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoProvincia.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.Provincia;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoProvincia extends InterCrud<Provincia>{    
    Collection<Provincia> listar(Long idDepartamento)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idDepartamento) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoProyecto.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterProyecto;
import com.jofrantoba.examples.jofrantoba.entity.Proyecto;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoProyecto extends InterCrud<Proyecto>{
    Collection<Proyecto> listar(FilterProyecto filter) throws Exception;
    Collection<Proyecto> listar(FilterProyecto filter, int pageNumber, int pageSize) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoRangoCuc.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.RangoCuc;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoRangoCuc extends InterCrud<RangoCuc>{    
    Collection<RangoCuc> listar(Long idDistrito)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idDistrito) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoRecapitulacionBc.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterRecapitulacionBc;
import com.jofrantoba.examples.jofrantoba.entity.RecapitulacionBc;

/**
 *
 * @author jtorresb
 */
public interface InterDaoRecapitulacionBc extends InterCrud<RecapitulacionBc>{    
    ArrayNode listarFilter(FilterRecapitulacionBc filter)throws Exception;
    ArrayNode listar(FilterRecapitulacionBc filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterRecapitulacionBc filter)throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countUnidadCatastralRef(Long idUnidadCatastralBc) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoRecapitulacionEdificios.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterRecapitulacionEdificios;
import com.jofrantoba.examples.jofrantoba.entity.RecapitulacionEdificios;
import com.jofrantoba.examples.jofrantoba.entity.UnidadCatastral;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoRecapitulacionEdificios extends InterCrud<RecapitulacionEdificios>{    
    ArrayNode listarFilter(FilterRecapitulacionEdificios filter)throws Exception;
    ArrayNode listar(FilterRecapitulacionEdificios filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterRecapitulacionEdificios filter)throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countUnidadCatastralRef(Long idUnidadCatastralBc) throws Exception;
    Collection<UnidadCatastral> udsCastSinAsociar() throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoReportFichaCatastraInd.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoCaracteristicasTitularidad;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoConstrucciones;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoDescripcionPredio;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoDocumentos;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoDomicilioFisTitularCatastral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoEvaluacionPredioCatastral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoIdentificacionTitularCatastral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoIncripcionPredioCatastral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoInformacionComplementaria;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoObrasComplementarias;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoObservaciones;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoServiciosPredio;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoUbicacionPredioCatastral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoUnidadCatastral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoZonificacion;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoLinderos;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoPorcentajeBienComun;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoUbicacionPredCatastralHabUrb;
import com.jofrantoba.examples.jofrantoba.entity.UnidadCatastral;

import java.util.List;

/**
 *
 * @author jtorresb
 */
 public interface InterDaoReportFichaCatastraInd extends InterCrud<UnidadCatastral> {

    DtoUnidadCatastral dsMain(Long idUnidadCatastral);

    List<DtoUbicacionPredioCatastral> dsUbicacionPredioCatastral(Long idUnidadCatastral);
    
    List<DtoUbicacionPredCatastralHabUrb> dsUbicacionPredCatastralHabUrb(Long idUnidadCatastral);
    
    List<DtoIdentificacionTitularCatastral> dsIdentificacionTitularCatastral(Long idUnidadCatastral);

    List<DtoDomicilioFisTitularCatastral> dsDomicilioFisTitularCatastral(Long idUnidadCatastral);

    List<DtoCaracteristicasTitularidad> dsCaractTitularidadCatastral(Long idUnidadCatastral);

    List<DtoDescripcionPredio> dsDescripcionPredioCatastral(Long idUnidadCatastral);
    
    List<DtoZonificacion> dsZonificacion(Long idUnidadCatastral);
    
    List<DtoLinderos> dsLinderos (Long idUnidadCatastral);

    List<DtoServiciosPredio> dsServiciosPredioCatastral(Long idUnidadCatastral);

    List<DtoConstrucciones> dsConstruccionesCatastral(Long idUnidadCatastral);
    
    List<DtoPorcentajeBienComun> dsPorcentajeBienComun(Long idUnidadCatastral);

    List<DtoObrasComplementarias> dsObrasComplementariasCatastral(Long idUnidadCatastral);

    List<DtoDocumentos> dsDocumentosCatastral(Long idUnidadCatastral);

    List<DtoIncripcionPredioCatastral> dsInscripcionPredioCatastral(Long idUnidadCatastral);

    List<DtoInformacionComplementaria> dsInfoComplementariaCatastral(Long idUnidadCatastral);

    List<DtoObservaciones> dsObservacionesCatastral(Long idUnidadCatastral);

    List<DtoEvaluacionPredioCatastral> dsEvaluacionPredioCatastral(Long idUnidadCatastral);

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoReportFichaCatastralActEco.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoActividadEconomica;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoAutorizacionAnuncio;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoConductorActEco;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoDomicilioFisConductorActEco;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoInformacionComplActEco;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoObservaciones;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoTipoActividadEconomica;
import com.jofrantoba.examples.jofrantoba.entity.UnidadCatastral;
import java.util.List;

/**
 *
 * @author jtorresb
 */
public interface InterDaoReportFichaCatastralActEco extends InterCrud<UnidadCatastral> {

    List<DtoConductorActEco> dsConductorActEco(Long idUnidadCatastral, Long idActividadEconomica);

    List<DtoDomicilioFisConductorActEco> dsDomicilioFisConductorActEco(Long idActividadEconomica);

    List<DtoTipoActividadEconomica> dsTipoActividadEconomica(Long idActividadEconomica);

    List<DtoActividadEconomica> dsActividadEconomica(Long idActividadEconomica);

    List<DtoAutorizacionAnuncio> dsAutorizacionAnuncio(Long idActividadEconomica);

    List<DtoInformacionComplActEco> dsInformacionComplActEco(Long idActividadEconomica);

    List<DtoObservaciones> dsObservacionesActEco(Long idActividadEconomica);
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoReportFichaCatastralBBCC.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoDocumentoDatoRegistral;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoEdificacionBienComun;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoRecapitulacionBienComun;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoRecapitulacionEdificio;
import com.jofrantoba.examples.jofrantoba.entity.UnidadCatastral;
import java.util.List;

/**
 *
 * @author jtorresb
 */
public interface InterDaoReportFichaCatastralBBCC extends InterCrud<UnidadCatastral> {

    List<DtoEdificacionBienComun> dsEdificacionBienComun(Long idUnidadCatastral);

    List<DtoRecapitulacionEdificio> dsRecapitulacionEdificio(Long idUnidadCatastral);

    List<DtoRecapitulacionBienComun> dsRecapitulacionBienComun(Long idUnidadCatastral);

    List<DtoDocumentoDatoRegistral> dsDocumentoDatoRegistral(Long idUnidadCatastral);

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoReportFichaCatastralCoti.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.reports.DtoCotitularCatastral;
import com.jofrantoba.examples.jofrantoba.entity.UnidadCatastral;
import java.util.List;

/**
 *
 * @author jtorresb
 */
public interface InterDaoReportFichaCatastralCoti extends InterCrud<UnidadCatastral> {

    List<DtoCotitularCatastral> dsDatosCotitular(Long idUnidadCatastral);
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoReportFichaCommon.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

/**
 *
 * @author jtorresb
 */
public interface InterDaoReportFichaCommon {
    
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoSector.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.Sector;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoSector extends InterCrud<Sector>{    
    Collection<Sector> listar(Long idDistrito)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idDistrito) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoSistema.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.Sistema;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoSistema extends InterCrud<Sistema>{
    Collection<Sistema> listar() throws Exception;
    Collection<Sistema> listar(int pageNumber, int pageSize) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoTipoActEconoEspecifico.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.TipoActEconoEspecifico;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoTipoActEconoEspecifico extends InterCrud<TipoActEconoEspecifico>{    
    Collection<TipoActEconoEspecifico> listar(Long idTipoActEconoIntermedio)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idTipoActEconoIntermedio) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoTipoActEconoGeneral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.TipoActEconoGeneral;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoTipoActEconoGeneral extends InterCrud<TipoActEconoGeneral>{
    Collection<TipoActEconoGeneral> listar() throws Exception;
    Collection<TipoActEconoGeneral> listar(int pageNumber, int pageSize) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoTipoActEconoIntermedio.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.TipoActEconoIntermedio;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoTipoActEconoIntermedio extends InterCrud<TipoActEconoIntermedio>{    
    Collection<TipoActEconoIntermedio> listar(Long idTipoActEconoGeneral)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idTipoActEconoGeneral) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoTipoOtrasInstalaciones.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.TipoOtrasInstalaciones;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoTipoOtrasInstalaciones extends InterCrud<TipoOtrasInstalaciones>{
    Collection<TipoOtrasInstalaciones> listar() throws Exception;
    Collection<TipoOtrasInstalaciones> listar(int pageNumber, int pageSize) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoTitular.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterTitular;
import com.jofrantoba.examples.jofrantoba.entity.Titular;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoTitular extends InterCrud<Titular> {

    Collection<Titular> listarFilter(FilterTitular filter) throws Exception;

    ArrayNode listar(Long limit, Long offSet) throws Exception;

    ArrayNode listar() throws Exception;

    Long countTitularidad(Long idTitularidad) throws Exception;

    Long countTipoTitular(Long idTipoTitular) throws Exception;

    Long countEstadoCivil(Long idEstadoCivil) throws Exception;

    Long countTipoDocumento(Long idCountTipoDocumento) throws Exception;

    Long countTipoPersonaJuridica(Long idTipoPersonaJuridica) throws Exception;

    Long countTipoUbicacion(Long idTipoUbicacion) throws Exception;

    Long countDepartamento(Long idDepartamento) throws Exception;

    Long countProvincia(Long idProvincia) throws Exception;

    Long countDistrito(Long idDistrito) throws Exception;

    Long countTipoVia(Long idTipoVia) throws Exception;

}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoTitularidad.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.Titularidad;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoTitularidad extends InterCrud<Titularidad>{    
    Collection<Titularidad> listarFilter(Long idUnidadCatastral,Long idCondicionTitular,Long idFormaAdquisicion)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long countUnidadCatastral(Long idUnidadCatastral) throws Exception;
    Long countCondicionTitular(Long idCondicionTitular) throws Exception;
    Long countFormaAdquisicion(Long idFormaAdquisicion) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoUnidadCatastral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.CodigoUnidadCatastral;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterUnidadCatastral;
import com.jofrantoba.examples.jofrantoba.entity.UnidadCatastral;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoUnidadCatastral extends InterCrud<UnidadCatastral>{    
    ArrayNode listarFilter(FilterUnidadCatastral filter)throws Exception;
    ArrayNode listar(FilterUnidadCatastral filter,Long limit,Long offSet)throws Exception;
    ArrayNode listar(FilterUnidadCatastral filter)throws Exception;
    Long countLoteCatastral(Long idLoteCatastral) throws Exception;
    Long countUsoEspecifico(Long idUsoEspecifico) throws Exception;
    Long countClasificacionPredio(Long idClasificionPredio) throws Exception;
    Long countPredioEn(Long idPredioEn) throws Exception;
    String newValueCodigoUnidad(CodigoUnidadCatastral codigoUnidadCatastral)throws Exception;
    Collection<UnidadCatastral> listarIdsBc(String codigoLoteCatastral)throws Exception;
    Collection<UnidadCatastral> listarIdsEdi(String codigoLoteCatastral)throws Exception;
    ArrayNode listarBienesComunesSinAsociar(String codigoLoteCatastral) throws Exception;
    ArrayNode listarEdificionesSinAsociar(String codigoLoteCatastral) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoUsoEspecifico.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.UsoEspecifico;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoUsoEspecifico extends InterCrud<UsoEspecifico>{    
    Collection<UsoEspecifico> listar(Long idUsoIntermedio)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idUsoIntermedio) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoUsoGeneral.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.UsoGeneral;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoUsoGeneral extends InterCrud<UsoGeneral>{
    Collection<UsoGeneral> listar() throws Exception;
    Collection<UsoGeneral> listar(int pageNumber, int pageSize) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoUsoIntermedio.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.UsoIntermedio;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoUsoIntermedio extends InterCrud<UsoIntermedio>{    
    Collection<UsoIntermedio> listar(Long idUsoGeneral)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idUsoGeneral) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoUsuario.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterUsuario;
import com.jofrantoba.examples.jofrantoba.entity.Usuario;

/**
 *
 * @author jtorresb
 */


public interface InterDaoUsuario extends InterCrud<Usuario>{
    ArrayNode listar(FilterUsuario filter) throws Exception;
    ArrayNode listar(FilterUsuario filter, Long limit, Long offSet) throws Exception;
    ArrayNode idPerfiles(FilterUsuario filter) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoUsuarioProyecto.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.dto.beans.FilterUsuarioProyecto;
import com.jofrantoba.examples.jofrantoba.entity.UsuarioProyecto;

/**
 *
 * @author jtorresb
 */
public interface InterDaoUsuarioProyecto extends InterCrud<UsuarioProyecto> {

    ArrayNode listarFilter(FilterUsuarioProyecto filter) throws Exception;

    ArrayNode listar(FilterUsuarioProyecto filter, Long limit, Long offSet) throws Exception;

    ArrayNode listar(FilterUsuarioProyecto filter) throws Exception;

    Long countProyecto(Long idCountLoteCatastral) throws Exception;

    Long countUsuario(Long idHabilitacionUrbana) throws Exception;
    
    Long countUsuarioProyecto(Long idLoteCatastral,Long idHabilitacionUrbana) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoVia.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.Via;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoVia extends InterCrud<Via>{    
    Collection<Via> listar(Long idDistrito)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idDistrito) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoViaCuadra.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.ViaCuadra;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoViaCuadra extends InterCrud<ViaCuadra>{    
    Collection<ViaCuadra> listar(Long idVia)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idDistrito) throws Exception;
}
```

### pg-jofrantoba-repository\src\main\java\com/jofrantoba/examples\jofrantoba\dao\inter\InterDaoZonificacion.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jofrantoba.examples.jofrantoba.dao.inter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.daoentity.InterCrud;
import com.jofrantoba.examples.jofrantoba.entity.Zonificacion;
import java.util.Collection;

/**
 *
 * @author jtorresb
 */
public interface InterDaoZonificacion extends InterCrud<Zonificacion>{
    Collection<Zonificacion> listar(Long idDistrito)throws Exception;
    ArrayNode listar(Long limit,Long offSet)throws Exception;
    ArrayNode listar()throws Exception;
    Long count(Long idDistrito) throws Exception;
}
```

### pg-jofrantoba-repository\src\test\java\com/jofrantoba/examples\dao\impl\TestBaseDao.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.dao.impl;

import com.jofrantoba.examples.jofrantoba.config.ConfigDao;
import com.jofrantoba.examples.jofrantoba.config.ConfigEntity;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author jtorresb
 */
public class TestBaseDao {

    protected AnnotationConfigApplicationContext contextEntity = new AnnotationConfigApplicationContext(ConfigEntity.class);
    protected AnnotationConfigApplicationContext contextDao = new AnnotationConfigApplicationContext(ConfigDao.class);
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

}
```

### pg-jofrantoba-repository\src\test\java\com/jofrantoba/examples\dao\impl\TestInsertDaoHabilitacionUrbana.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.dao.impl;

import com.jofrantoba.examples.jofrantoba.dao.impl.DaoHabilitacionUrbana;
import com.jofrantoba.examples.jofrantoba.dao.impl.DaoLogsEntityOperation;
import com.jofrantoba.examples.jofrantoba.dao.impl.DaoParametrias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoLogsEntityOperation;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoHabilitacionUrbana;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoParametrias;
import com.jofrantoba.examples.jofrantoba.entity.LogsEntityOperation;
import com.jofrantoba.examples.jofrantoba.entity.HabilitacionUrbana;
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
```

### pg-jofrantoba-repository\src\test\java\com/jofrantoba/examples\dao\impl\TestInsertDaoParametria.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.dao.impl;

import com.jofrantoba.examples.jofrantoba.dao.impl.DaoLogsEntityOperation;
import com.jofrantoba.examples.jofrantoba.dao.impl.DaoParametrias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoLogsEntityOperation;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoParametrias;
import com.jofrantoba.examples.jofrantoba.entity.Distrito;
import com.jofrantoba.examples.jofrantoba.entity.LogsEntityOperation;
import com.jofrantoba.examples.jofrantoba.entity.Parametrias;
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
```

### pg-jofrantoba-repository\src\test\java\com/jofrantoba/examples\dao\impl\TestSelectDaoParametria.java

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.examples.dao.impl;

import com.jofrantoba.examples.jofrantoba.dao.impl.DaoParametrias;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.examples.jofrantoba.dao.inter.InterDaoParametrias;
import com.jofrantoba.examples.jofrantoba.entity.Parametrias;
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
```

