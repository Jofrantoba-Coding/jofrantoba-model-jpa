/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;

/**
 *
 * @author jona
 * @param <T>
 */
public interface InterCrud<T extends Serializable> {    
    T findById(final long id)throws UnknownException;
    
    T findById(final String id)throws UnknownException;   

    void save(final T entity)throws UnknownException;
    
    int saveNativeQuery(String table,String[] fieldValue)throws UnknownException;
    
    int iudNativeQuery(String sql)throws UnknownException;

    void update(final T entity)throws UnknownException;

    void delete(final T entity)throws UnknownException;

    void delete(final long id)throws UnknownException;
    
    int deleteFilterAnd(String[] filters)throws UnknownException;
    
    void flush()throws UnknownException;
    
    void detach(T entity)throws UnknownException;
    
    void clear()throws UnknownException;
    
    void evict(T entity)throws UnknownException;
    
    Session getSession()throws UnknownException;
    
    Session getNewSession()throws UnknownException;
    
    Collection<T> allFields()throws UnknownException;            
    
    Collection<T> allFields(HashMap<String,String> mapOrder)throws UnknownException;
    
    Collection<T> allFields(String[] mapOrder)throws UnknownException;
    
    Collection<T> allFields(String mapFilter,String[] mapOrder) throws UnknownException;
    
    Collection<T> allFieldsFilterAnd(String[] mapFilterField, String[] mapOrder) throws UnknownException;
    
    Collection<T> allFieldsFilterOr(String[] mapFilterField, String[] mapOrder) throws UnknownException;
    
    Collection<T> customFields(String fields) throws UnknownException;
    
    Collection<T> customFields(ResultTransformer rt,String fields) throws UnknownException;
    
    Collection<T> customFieldsFilterAnd(String fields,String[] mapFilterField, String[] mapOrder) throws UnknownException;
    
    Collection<T> customFieldsFilterOr(String fields,String[] mapFilterField, String[] mapOrder) throws UnknownException;
    
    Map<Integer, Object[]> sqlExportTOExcel(String sql)throws UnknownException;
    
    Collection<T> allFieldsJoinFilter(String joinTable,String mapJoinFilter,String[] mapOrder) throws UnknownException;
    
    Collection<T> allFieldsJoinFilterAnd(String joinTable,String[] mapFilter, String[] mapOrder) throws UnknownException;
    
    Collection<?> customFieldsJoinFilterAnd(Class<?> dto,String fields,String joinTable, String[] mapFilterField, String[] mapOrder) throws UnknownException;
    
    Collection<T> customFieldsJoinFilterAnd(String fields,String joinTable, String[] mapFilterField, String[] mapOrder) throws UnknownException;
    
    Collection<?> customFieldsJoinFilterAnd(ResultTransformer rt,String fields,String joinTable, String[] mapFilterField, String[] mapOrder) throws UnknownException;
    
    Long rowCountJoinFilterAnd(String joinTable, String[] mapFilterField) throws UnknownException;
    
    Long rowCountJoinsFilterAnd(String[] joinTables, String[] mapFilterField) throws UnknownException;
    
    ArrayNode allFieldsLimitOffsetPostgres(String table,String fields,String[] mapFilterField,String[] mapOrder,Long limit, Long offset)throws UnknownException;
    
    ArrayNode allFieldsPostgres(String table, String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException;
    
    Collection<T> customFieldsJoinFilterAnd(String fields, String joinTable, String[] mapFilterField, String[] mapOrder,int pageNumber, int pageSize) throws UnknownException;
    
    Collection<T> allFields(String mapFilterField, String[] mapOrder,int pageNumber, int pageSize) throws UnknownException;
    
    ArrayNode allFieldsJoinPostgres(String[] joinTables,String table, String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException;
    
    ArrayNode allFieldsJoinPostgresGroupBy(String[] joinTables,String table, String fields, String[] mapFilterField, String[] mapOrder,String groupBy) throws UnknownException;
    
    ArrayNode allFieldsLimitJoinPostgresGroupBySubQuery(String fields,String groupBy,String[] mapOrder,String[] joinTablesSq, String tableSq, String fieldsSq, String[] mapFilterFieldSq,String groupBySq,Long limit, Long offset) throws UnknownException;
    
    ArrayNode allFieldsJoinPostgresGroupBySubQuery(String fields,String groupBy,String[] mapOrder,String[] joinTablesSq, String tableSq, String fieldsSq, String[] mapFilterFieldSq,String groupBySq) throws UnknownException;
    
    ArrayNode allFieldsJoinLimitOffsetPostgres(String[] joinTables,String table, String fields, String[] mapFilterField, String[] mapOrder, Long limit, Long offset) throws UnknownException;
    
    Collection<?> customFieldsJoinFilterAnd(ResultTransformer rt, String fields, String[] joinTables, String[] mapFilterField, String[] mapOrder) throws UnknownException;
    
    Long aggregateJoinFilterAndGroupBy(String fields,String joinTable, String[] mapFilterField,String groupBy) throws UnknownException;
    
    Collection<?> customFieldsFilterAnd(Class<?> dto,String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException;
}
