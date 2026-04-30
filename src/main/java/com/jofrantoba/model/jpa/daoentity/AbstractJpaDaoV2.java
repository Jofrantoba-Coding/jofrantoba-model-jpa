/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

/**
 *
 * @author jona
 * @param <T>
 */
@Log4j2
@Data
public abstract class AbstractJpaDaoV2<T extends Serializable> implements InterCrud<T> {

    private Class<T> clazz;

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected void setClazz(final Class<T> clazzToSet) {
        clazz = Preconditions.checkNotNull(clazzToSet);
    }

    @Override
    public T findById(final long id) {
        return (T) getCurrentSession().get(clazz, id);
    }

    @Override
    public T findById(final String id) {
        return (T) getCurrentSession().get(clazz, id);
    }

    @Override
    public void save(final T entity) {
        Preconditions.checkNotNull(entity);
        getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public Long iudProcedureJson(String nameProcedure, String json) {
        StoredProcedureQuery query = getCurrentSession()
                .createStoredProcedureQuery(nameProcedure)
                .registerStoredProcedureParameter("json", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("count", Long.class, ParameterMode.OUT)
                .setParameter("json", json);
        query.execute();
        Long count = (Long) query
                .getOutputParameterValue("count");
        return count;
    }

    @Override
    public List<T> listProcedureMsql(String nameProcedure, Map<String, Object> mapParameter) {
        Query<T> query = getCurrentSession().createNativeQuery(nameProcedure).addEntity(clazz);
        for (Map.Entry<String, Object> entry : mapParameter.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.list();
    }

    @Override
    public void update(final T entity) {
        Preconditions.checkNotNull(entity);
        getCurrentSession().merge(entity);
    }

    @Override
    public void delete(final T entity) {
        Preconditions.checkNotNull(entity);
        getCurrentSession().delete(entity);
    }

    @Override
    public void delete(final long entityId) {
        final T entity = findById(entityId);
        Preconditions.checkState(entity != null);
        delete(entity);
    }

    @Override
    public int deleteFilterAnd(String[] mapFilterField) {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("delete")).append(sharedUtil.append("from"))
           .append(clazz.getName()).append(sharedUtil.append("as base"));
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        return query.executeUpdate();
    }

    @Override
    public Collection<T> allFields() throws UnknownException {
        CriteriaBuilder build = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> criteria = build.createQuery(clazz);
        criteria.from(clazz);
        Collection<T> collection = getCurrentSession().createQuery(criteria).getResultList();
        return collection;
    }

    @Override
    public ArrayNode allFieldsPostgres(String table, String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append(fields));
        sql.append(sharedUtil.append("from"));
        sql.append(sharedUtil.append(table));
        DslFragment frag = buildNativeSqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(cnctn ->
            executeStatement(cnctn, sql.toString(), sharedUtil, array, frag.params, null, null));
        return array;
    }

    @Override
    public ArrayNode allFieldsJoinPostgres(String[] joinTables, String table, String fields, String[] mapFilterField, String[] mapOrder, String filterConectorLogicoDefault) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append(fields));
        sql.append(sharedUtil.append("from"));
        sql.append(sharedUtil.append(table));
        joins(joinTables, sql);
        DslFragment frag = buildNativeSqlFragment(filterConectorLogicoDefault, mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(cnctn ->
            executeStatement(cnctn, sql.toString(), sharedUtil, array, frag.params, null, null));
        return array;
    }

    @Override
    public StringBuilder strAllFieldsJoinPostgres(String[] joinTables, String table, String fields, String[] mapFilterField, String[] mapOrder) {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append(fields));
        sql.append(sharedUtil.append("from"));
        sql.append(sharedUtil.append(table));
        joins(joinTables, sql);
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilterField).toString()));
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        return sql;
    }

    private void joins(String[] joinTables, StringBuilder sql) {
        Shared sharedUtil = new Shared();
        for (String joinTable : joinTables) {
            String[] join = joinTable.split(":");
            if (!VALID_JOIN_TYPES.contains(join[0].toLowerCase())) {
                throw new IllegalArgumentException("Invalid join type: " + join[0]);
            }
            validateTableRef(join[1]);
            sql.append(sharedUtil.append(join[0] + " join " + join[1] + " on "));
            if (join.length == 5) {
                validateIdentifier(join[3]);
                validateIdentifier(join[4]);
                sql.append(sharedUtil.append(join[3] + "=" + join[4]));
                continue;
            }
            int nextValue = 3;
            while (nextValue + 1 < join.length) {
                String leftCol  = join[nextValue];
                String rightCol = join[nextValue + 1];
                validateIdentifier(leftCol);
                validateIdentifier(rightCol);
                sql.append(sharedUtil.append(leftCol + "=" + rightCol));
                nextValue += 2;
                if (nextValue == join.length) {
                    break;
                }
                validateConnector(join[nextValue]);
                sql.append(sharedUtil.append(join[nextValue]));
                ++nextValue;
            }
            if (nextValue != join.length) {
                throw new IllegalArgumentException("Invalid join condition: " + joinTable);
            }
        }
    }

    @Override
    public ArrayNode allFieldsJoinPostgresGroupBy(String[] joinTables, String table, String fields, String[] mapFilterField, String[] mapOrder, String groupBy) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append(fields));
        sql.append(sharedUtil.append("from"));
        sql.append(sharedUtil.append(table));
        joins(joinTables, sql);
        DslFragment frag = buildNativeSqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (groupBy != null) {
            sql.append(sharedUtil.append("group by"));
            sql.append(sharedUtil.append(groupBy));
        }
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(cnctn ->
            executeStatement(cnctn, sql.toString(), sharedUtil, array, frag.params, null, null));
        return array;
    }

    @Override
    public StringBuilder strAllFieldsJoinPostgresGroupBy(String[] joinTables, String table, String fields, String[] mapFilterField, String[] mapOrder, String groupBy) {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append(fields));
        sql.append(sharedUtil.append("from"));
        sql.append(sharedUtil.append(table));
        joins(joinTables, sql);
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilterField).toString()));
        if (groupBy != null) {
            sql.append(sharedUtil.append("group by"));
            sql.append(sharedUtil.append(groupBy));
        }
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        return sql;
    }

    @Override
    public StringBuilder strAllFieldsJoinGroupByLimitOffsetPostgres(String[] joinTables, String table, String fields, String[] mapFilterField, String[] mapOrder, String groupBy, Long limit, Long offset) {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append(fields));
        sql.append(sharedUtil.append("from"));
        sql.append(sharedUtil.append(table));
        joins(joinTables, sql);
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilterField).toString()));
        if (groupBy != null) {
            sql.append(sharedUtil.append("group by"));
            sql.append(sharedUtil.append(groupBy));
        }
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        sql.append(sharedUtil.append("limit ? offset ?"));
        return sql;
    }

    @Override
    public ArrayNode allFieldsJoinPostgresGroupBySubQuery(String fields, String groupBy, String[] mapOrder, String[] joinTablesSq, String tableSq, String fieldsSq, String[] mapFilterFieldSq, String groupBySq) throws UnknownException {
        StringBuilder subquery = new StringBuilder();
        Shared sharedUtil = new Shared();
        subquery.append(sharedUtil.append("select"));
        subquery.append(sharedUtil.append(fieldsSq));
        subquery.append(sharedUtil.append("from"));
        subquery.append(sharedUtil.append(tableSq));
        joins(joinTablesSq, subquery);
        DslFragment fragSq = buildNativeSqlFragment("and", mapFilterFieldSq);
        subquery.append(fragSq.sql);
        if (groupBySq != null) {
            subquery.append(sharedUtil.append("group by"));
            subquery.append(sharedUtil.append(groupBySq));
        }

        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append(fields));
        sql.append(sharedUtil.append("from"));
        sql.append("(");
        sql.append(subquery.toString());
        sql.append(") as subquery");

        if (groupBy != null) {
            sql.append(sharedUtil.append("group by"));
            sql.append(sharedUtil.append(groupBy));
        }
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }

        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(cnctn ->
            executeStatement(cnctn, sql.toString(), sharedUtil, array, fragSq.params, null, null));
        return array;
    }

    @Override
    public ArrayNode allFieldsLimitJoinPostgresGroupBySubQuery(String fields, String groupBy, String[] mapOrder, String[] joinTablesSq, String tableSq, String fieldsSq, String[] mapFilterFieldSq, String groupBySq, Long limit, Long offset) throws UnknownException {
        StringBuilder subquery = new StringBuilder();
        Shared sharedUtil = new Shared();
        subquery.append(sharedUtil.append("select"));
        subquery.append(sharedUtil.append(fieldsSq));
        subquery.append(sharedUtil.append("from"));
        subquery.append(sharedUtil.append(tableSq));
        joins(joinTablesSq, subquery);
        DslFragment fragSq = buildNativeSqlFragment("and", mapFilterFieldSq);
        subquery.append(fragSq.sql);
        if (groupBySq != null) {
            subquery.append(sharedUtil.append("group by"));
            subquery.append(sharedUtil.append(groupBySq));
        }

        StringBuilder sql = new StringBuilder();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append(fields));
        sql.append(sharedUtil.append("from"));
        sql.append("(");
        sql.append(subquery.toString());
        sql.append(") as subquery");

        if (groupBy != null) {
            sql.append(sharedUtil.append("group by"));
            sql.append(sharedUtil.append(groupBy));
        }
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        sql.append(sharedUtil.append("limit ? offset ?"));

        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(cnctn ->
            executeStatement(cnctn, sql.toString(), sharedUtil, array, fragSq.params, limit, offset));
        return array;
    }

    @Override
    public ArrayNode allFieldsLimitOffsetPostgres(String table, String fields, String[] mapFilterField, String[] mapOrder, Long limit, Long offset) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append(fields));
        sql.append(sharedUtil.append("from"));
        sql.append(sharedUtil.append(table));
        DslFragment frag = buildNativeSqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        sql.append(sharedUtil.append("limit ? offset ?"));
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(cnctn ->
            executeStatement(cnctn, sql.toString(), sharedUtil, array, frag.params, limit, offset));
        return array;
    }

    private void typesSet(ObjectNode node, ResultSet rs, ResultSetMetaData metadata, int i) throws SQLException {
        if (metadata.getColumnTypeName(i).equals("numeric")) {
            node.put(metadata.getColumnName(i), rs.getBigDecimal(metadata.getColumnName(i)));
        }
        if (metadata.getColumnTypeName(i).equals("text")) {
            node.put(metadata.getColumnName(i), rs.getString(metadata.getColumnName(i)));
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

    @Override
    public ArrayNode allFieldsJoinLimitOffsetPostgres(String[] joinTables, String table, String fields, String[] mapFilterField, String[] mapOrder, Long limit, Long offset, String filterConectorLogicoDefault) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append(fields));
        sql.append(sharedUtil.append("from"));
        sql.append(sharedUtil.append(table));
        joins(joinTables, sql);
        DslFragment frag = buildNativeSqlFragment(filterConectorLogicoDefault, mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        sql.append(sharedUtil.append("limit ? offset ?"));
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(cnctn ->
            executeStatement(cnctn, sql.toString(), sharedUtil, array, frag.params, limit, offset));
        return array;
    }

    @Override
    public Collection<T> customFields(String fields) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select")).append(fields).append(sharedUtil.append("from")).append(clazz.getName()).append(sharedUtil.append("as clase"));
        Query query = getCurrentSession().createQuery(sql.toString());
        return query.list();
    }

    @Override
    public Collection<T> customFields(ResultTransformer rt, String fields) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select")).append(fields).append(sharedUtil.append("from")).append(clazz.getName()).append(sharedUtil.append("as clase"));
        Query query = getCurrentSession().createQuery(sql.toString());
        query.setResultTransformer(rt);
        Collection valores = query.getResultList();
        return valores;
    }

    @Override
    public Collection<?> customFieldsFilterAnd(Class<?> dto, String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select")).append(fields).append(sharedUtil.append("from")).append(clazz.getName()).append(sharedUtil.append("as base"));
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        query.setResultTransformer(Transformers.aliasToBean(dto));
        Collection valores = query.getResultList();
        return valores;
    }

    @Override
    public Collection<T> customFieldsFilterAnd(String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select")).append(fields).append(sharedUtil.append("from")).append(clazz.getName()).append(sharedUtil.append("as base"));
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        return query.list();
    }

    @Override
    public Collection<T> customFieldsFilterOr(String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select")).append(fields).append(sharedUtil.append("from")).append(clazz.getName()).append(sharedUtil.append("as clase"));
        DslFragment frag = buildHqlFragment("or", mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        return query.list();
    }

    @Override
    public Collection<T> allFields(HashMap<String, String> mapOrder) throws UnknownException {
        CriteriaBuilder build = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> criteria = build.createQuery(clazz);
        orderMap(build, criteria, mapOrder);
        Collection<T> collection = getCurrentSession().createQuery(criteria).getResultList();
        return collection;
    }

    @Override
    public Collection<T> allFields(String[] mapOrder) throws UnknownException {
        CriteriaBuilder build = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> criteria = build.createQuery(clazz);
        Root<T> root = criteria.from(clazz);
        orderString(build, criteria, root, mapOrder);
        Collection<T> collection = getCurrentSession().createQuery(criteria).getResultList();
        return collection;
    }

    @Override
    public Collection<T> allFields(String mapFilterField, String[] mapOrder) throws UnknownException {
        CriteriaBuilder build = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> criteria = build.createQuery(clazz);
        Root<T> root = criteria.from(clazz);
        orderString(build, criteria, root, mapOrder);
        criteria.select(root).where(filterString(build, criteria, root, mapFilterField));
        Collection<T> collection = getCurrentSession().createQuery(criteria).getResultList();
        return collection;
    }

    @Override
    public Collection<T> allFieldsJoinFilter(String joinTable, String mapFilter, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select base from"));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        String[] join = joinTable.split(":");
        String fetch = join.length > 2 ? join[2] : "";
        sql.append(sharedUtil.append(join[0] + " join " + fetch + " base." + join[1] + " " + join[1]));
        DslFragment frag = buildHqlFragment("and", new String[]{mapFilter});
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        Collection<T> valores = query.list();
        return valores;
    }

    @Override
    public Collection<T> allFieldsJoinFilter(String joinTable, String mapFilter, String[] mapOrder, int pageNumber, int pageSize) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select base from"));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        String[] join = joinTable.split(":");
        String fetch = join.length > 2 ? join[2] : "";
        sql.append(sharedUtil.append(join[0] + " join " + fetch + " base." + join[1] + " " + join[1]));
        DslFragment frag = buildHqlFragment("and", new String[]{mapFilter});
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        Collection<T> valores = query.list();
        return valores;
    }

    @Override
    public Collection<T> allFieldsJoinFilterAnd(String joinTable, String[] mapFilter, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select base from"));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        String[] join = joinTable.split(":");
        String fetch = join.length > 2 ? join[2] : "";
        sql.append(sharedUtil.append(join[0] + " join " + fetch + " base." + join[1] + " " + join[1]));
        DslFragment frag = buildHqlFragment("and", mapFilter);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        Collection<T> valores = query.list();
        return valores;
    }

    @Override
    public Collection<T> allFieldsJoinFilterAnd(String[] joinTables, String[] mapFilter, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select base from"));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        String nameObjectParent = "base";
        for (String joinTable : joinTables) {
            String[] join = joinTable.split(":");
            String fetch = join.length > 2 ? join[2] : "";
            sql.append(sharedUtil.append(join[0] + " join " + fetch + " " + nameObjectParent + "." + join[1] + " " + join[1]));
            nameObjectParent = join[1];
        }
        DslFragment frag = buildHqlFragment("and", mapFilter);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        Collection<T> valores = query.list();
        return valores;
    }

    @Override
    public Collection<T> allFieldsJoinFilterAnd(String joinTable, String[] mapFilter, String[] mapOrder, int pageNumber, int pageSize) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select base from"));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        String[] join = joinTable.split(":");
        String fetch = join.length > 2 ? join[2] : "";
        sql.append(sharedUtil.append(join[0] + " join " + fetch + " base." + join[1] + " " + join[1]));
        DslFragment frag = buildHqlFragment("and", mapFilter);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        Collection<T> valores = query.list();
        return valores;
    }

    @Override
    public Long rowCountJoinFilterAnd(String joinTable, String[] mapFilterField) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select").append("count(*)").append(sharedUtil.append("from")));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        if (joinTable != null) {
            sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        }
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        Long count = (Long) query.uniqueResult();
        return count;
    }

    @Override
    public Object maxValueJoinFilterAnd(String field, String joinTable, String[] mapFilterField) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select").append("max(").append(field).append(")").append(sharedUtil.append("from")));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        if (joinTable != null) {
            sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        }
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        return query.uniqueResult();
    }

    @Override
    public Long rowCountJoinsFilterAnd(String[] joinTables, String[] mapFilterField) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select").append("count(*)").append(sharedUtil.append("from")));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        for (String joinTable : joinTables) {
            sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        }
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        Long count = (Long) query.uniqueResult();
        return count;
    }

    @Override
    public Long aggregateJoinFilterAndGroupBy(String fields, String joinTable, String[] mapFilterField, String groupBy) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select").append(fields).append(sharedUtil.append("from")));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        if (joinTable != null) {
            sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        }
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (groupBy != null) {
            sql.append(sharedUtil.append(groupBy));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        Long count = (Long) query.uniqueResult();
        return count;
    }

    @Override
    public Collection<T> customFieldsJoinFilterAnd(String fields, String joinTable, String[] mapFilterField, String[] mapOrder, int pageNumber, int pageSize) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select").append(fields).append(sharedUtil.append("from")));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        query.setResultTransformer(Transformers.aliasToBean(clazz));
        Collection valores = query.getResultList();
        return valores;
    }

    @Override
    public Collection<T> customFieldsJoinFilterAnd(String fields, String[] joinTables, String[] mapFilterField, String[] mapOrder, int pageNumber, int pageSize) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select").append(fields).append(sharedUtil.append("from")));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        for (String joinTable : joinTables) {
            String entityFk = joinTable.split(":")[1];
            if (entityFk.contains(".")) {
                sql.append(sharedUtil.append(joinTable.split(":")[0] + " join " + entityFk + " " + entityFk.split("\\.")[1]));
            } else {
                sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + entityFk + " " + entityFk));
            }
        }
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        query.setResultTransformer(Transformers.aliasToBean(clazz));
        Collection valores = query.getResultList();
        return valores;
    }

    @Override
    public Collection<T> allFields(String mapFilterField, String[] mapOrder, int pageNumber, int pageSize) throws UnknownException {
        CriteriaBuilder build = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> criteria = build.createQuery(clazz);
        Root<T> root = criteria.from(clazz);
        orderString(build, criteria, root, mapOrder);
        criteria.select(root).where(filterString(build, criteria, root, mapFilterField));
        Query query = getCurrentSession().createQuery(criteria);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        Collection<T> collection = query.getResultList();
        return collection;
    }

    @Override
    public Collection<T> customFieldsJoinFilterAnd(String fields, String joinTable, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select").append(fields).append(sharedUtil.append("from")));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        query.setResultTransformer(Transformers.aliasToBean(clazz));
        Collection valores = query.getResultList();
        return valores;
    }

    @Override
    public Collection<?> customFieldsJoinFilterAnd(Class<?> dto, String fields, String joinTable, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select").append(fields).append(sharedUtil.append("from")));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        query.setResultTransformer(Transformers.aliasToBean(dto));
        Collection valores = query.getResultList();
        return valores;
    }

    @Override
    public Collection<?> customFieldsJoinFilterAnd(ResultTransformer rt, String fields, String joinTable, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select").append(fields).append(sharedUtil.append("from")));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        query.setResultTransformer(rt);
        Collection valores = query.getResultList();
        return valores;
    }

    @Override
    public Collection<?> customFieldsJoinFilterAnd(ResultTransformer rt, String fields, String[] joinTables, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select").append(fields).append(sharedUtil.append("from")));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        for (String joinTable : joinTables) {
            String entityFk = joinTable.split(":")[1];
            if (entityFk.contains(".")) {
                sql.append(sharedUtil.append(joinTable.split(":")[0] + " join " + entityFk + " " + entityFk.split("\\.")[1]));
            } else {
                sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + entityFk + " " + entityFk));
            }
        }
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        query.setResultTransformer(rt);
        Collection valores = query.getResultList();
        return valores;
    }

    @Override
    public Collection<T> customFieldsJoinFilterAnd(String fields, String[] joinTables, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select").append(fields).append(sharedUtil.append("from")));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        for (String joinTable : joinTables) {
            String entityFk = joinTable.split(":")[1];
            if (entityFk.contains(".")) {
                sql.append(sharedUtil.append(joinTable.split(":")[0] + " join " + entityFk + " " + entityFk.split("\\.")[1]));
            } else {
                sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + entityFk + " " + entityFk));
            }
        }
        DslFragment frag = buildHqlFragment("and", mapFilterField);
        sql.append(frag.sql);
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        applyHqlParams(query, frag.params);
        query.setResultTransformer(Transformers.aliasToBean(clazz));
        Collection valores = query.getResultList();
        return valores;
    }

    @Override
    public Collection<T> allFieldsFilterAnd(String[] mapFilterField, String[] mapOrder) throws UnknownException {
        CriteriaBuilder build = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> criteria = build.createQuery(clazz);
        Root<T> root = criteria.from(clazz);
        orderString(build, criteria, root, mapOrder);
        criteria.select(root).where(build.and(stringToPredicate(build, criteria, root, mapFilterField)));
        Collection<T> collection = getCurrentSession().createQuery(criteria).getResultList();
        return collection;
    }

    @Override
    public Collection<T> allFieldsFilterOr(String[] mapFilterField, String[] mapOrder) throws UnknownException {
        CriteriaBuilder build = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> criteria = build.createQuery(clazz);
        Root<T> root = criteria.from(clazz);
        orderString(build, criteria, root, mapOrder);
        criteria.select(root).where(build.or(stringToPredicate(build, criteria, root, mapFilterField)));
        Collection<T> collection = getCurrentSession().createQuery(criteria).getResultList();
        return collection;
    }

    private Predicate[] stringToPredicate(CriteriaBuilder build, CriteriaQuery<T> criteria, Root<T> root,
            String[] mapFilterField) {
        Predicate[] filters = new Predicate[mapFilterField.length];
        for (int i = 0; i < mapFilterField.length; i++) {
            filters[i] = filterString(build, criteria, root, mapFilterField[i]);
        }
        return filters;
    }

    // -------------------------------------------------------------------------
    // Legacy string-building helpers (kept for str* debug methods only).
    // These are NOT used in any query-executing path.
    // -------------------------------------------------------------------------

    private StringBuilder buildFilterStringSelect(String conectorLogicoDefault, String[] mapFilterField) {
        StringBuilder filter = new StringBuilder();
        Shared sharedUtil = new Shared();
        if (mapFilterField.length > 0) {
            filter.append(sharedUtil.append("where 1=1"));
            for (int i = 0; i < mapFilterField.length; i++) {
                filter.append(sharedUtil.append(conectorLogicoDefault))
                      .append(sharedUtil.append(filterStringSelect(mapFilterField[i]).toString()));
            }
        } else {
            filter.append(sharedUtil.append("where 1=2"));
        }
        return filter;
    }

    private void orderMap(CriteriaBuilder build, CriteriaQuery<T> criteria, HashMap<String, String> mapOrder) {
        if (mapOrder != null && mapOrder.size() > 0) {
            Root<T> c = criteria.from(clazz);
            List<Order> orders = new ArrayList();
            Iterator<String> iterador = mapOrder.keySet().iterator();
            while (iterador.hasNext()) {
                String field = iterador.next();
                String orden = mapOrder.get(field);
                if (orden.equalsIgnoreCase("asc")) {
                    orders.add(build.asc(c.get(field)));
                } else {
                    orders.add(build.desc(c.get(field)));
                }
            }
            criteria.orderBy(orders);
        }
    }

    private void orderString(CriteriaBuilder build, CriteriaQuery<T> criteria, Root<T> root, String... mapOrder) {
        if (mapOrder != null && mapOrder.length > 0) {
            List<Order> orders = new ArrayList();
            for (int i = 0; i < mapOrder.length; i++) {
                String field = mapOrder[i].split(":")[0];
                String orden = mapOrder[i].split(":")[1];
                if (orden.equalsIgnoreCase("asc")) {
                    orders.add(build.asc(root.get(field)));
                } else {
                    orders.add(build.desc(root.get(field)));
                }
            }
            criteria.orderBy(orders);
        }
    }

    private StringBuilder orderString(String... mapOrder) {
        StringBuilder order = new StringBuilder();
        Shared sharedUtil = new Shared();
        order.append(sharedUtil.append("order by"));
        if (mapOrder != null && mapOrder.length > 0) {
            for (int i = 0; i < mapOrder.length; i++) {
                String field = mapOrder[i].split(":")[0];
                String dir   = mapOrder[i].split(":")[1];
                validateIdentifier(field);
                if (!dir.equalsIgnoreCase("asc") && !dir.equalsIgnoreCase("desc")) {
                    throw new IllegalArgumentException("Invalid order direction: " + dir);
                }
                order.append(sharedUtil.append(field));
                order.append(sharedUtil.append(dir));
                if (i == mapOrder.length - 1) {
                    continue;
                }
                order.append(",");
            }
        }
        return order;
    }

    private StringBuilder filterStringSelect(String mapFilterField) {
        StringBuilder pre = new StringBuilder();
        Shared sharedUtil = new Shared();
        String[] v = mapFilterField.split(":");
        switch (v[0]) {
            case ">":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append(">")).append(sharedUtil.append(v[2]));
                break;
            case "<":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append("<")).append(sharedUtil.append(v[2]));
                break;
            case ">=":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append(">=")).append(sharedUtil.append(v[2]));
                break;
            case "<=":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append("<=")).append(sharedUtil.append(v[2]));
                break;
            case "=":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append("=")).append(sharedUtil.append(v[2]));
                break;
            case "equal":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append("=")).append(sharedUtil.append("'" + v[2] + "'"));
                break;
            case "notequal":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append("!=")).append(sharedUtil.append("'" + v[2] + "'"));
                break;
            case "!=":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append("!=")).append(sharedUtil.append(v[2]));
                break;
            case "like":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append("like")).append(sharedUtil.append(v[2]));
                break;
            case "between":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append("between"))
                   .append(sharedUtil.append(v[2])).append(sharedUtil.append("and")).append(sharedUtil.append(v[3]));
                break;
            case "in":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append("in"))
                   .append(sharedUtil.append("(")).append(sharedUtil.append(valuesByComas(2, v))).append(sharedUtil.append(")"));
                break;
            case "isnull":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append("is null"));
                break;
            case "isnotnull":
                pre.append(sharedUtil.append(v[1])).append(sharedUtil.append("is not null"));
                break;
        }
        return pre;
    }

    private String valuesByComas(int init, String[] mapFilterFieldsValues) {
        String valuesIn = "";
        int valueControl = mapFilterFieldsValues.length - 1;
        for (int i = init; i < mapFilterFieldsValues.length; i++) {
            valuesIn = valuesIn + mapFilterFieldsValues[i] + (i < valueControl ? "," : "");
        }
        return valuesIn;
    }

    private Predicate filterString(CriteriaBuilder build, CriteriaQuery<T> criteria, Root<T> root,
            String mapFilterField) {
        Predicate pre = null;
        String[] mapFilterFieldsValues = mapFilterField.split(":");
        switch (mapFilterFieldsValues[0]) {
            case ">":
                pre = build.gt(root.get(mapFilterFieldsValues[1]), Double.parseDouble(mapFilterFieldsValues[2]));
                break;
            case "<":
                pre = build.lt(root.get(mapFilterFieldsValues[1]), Double.parseDouble(mapFilterFieldsValues[2]));
                break;
            case ">=":
                pre = build.ge(root.get(mapFilterFieldsValues[1]), Double.parseDouble(mapFilterFieldsValues[2]));
                break;
            case "<=":
                pre = build.le(root.get(mapFilterFieldsValues[1]), Double.parseDouble(mapFilterFieldsValues[2]));
                break;
            case "=":
                pre = build.equal(root.get(mapFilterFieldsValues[1]), Double.parseDouble(mapFilterFieldsValues[2]));
                break;
            case "like":
                pre = build.like(root.get(mapFilterFieldsValues[1]), mapFilterFieldsValues[2]);
                break;
            case "between":
                pre = build.between(root.get(mapFilterFieldsValues[1]),
                        Double.parseDouble(mapFilterFieldsValues[2]), Double.parseDouble(mapFilterFieldsValues[3]));
                break;
            case "in":
                Collection inValue = new ArrayList();
                for (int i = 2; i < mapFilterFieldsValues.length; i++) {
                    inValue.add(mapFilterFieldsValues[i]);
                }
                pre = root.get(mapFilterFieldsValues[1]).in(inValue);
                break;
            case "isnull":
                pre = build.isNull(root.get(mapFilterFieldsValues[1]));
                break;
            case "isnotnull":
                pre = build.isNotNull(root.get(mapFilterFieldsValues[1]));
                break;
            case "istrue":
                pre = build.isTrue(root.get(mapFilterFieldsValues[1]));
                break;
            case "isfalse":
                pre = build.isFalse(root.get(mapFilterFieldsValues[1]));
                break;
            case "equal":
                pre = build.equal(root.get(mapFilterFieldsValues[1]), mapFilterFieldsValues[2]);
                break;
        }
        return pre;
    }

    @Override
    public Map<Integer, Object[]> sqlExportTOExcel(String sql) throws UnknownException {
        Map<Integer, Object[]> data = new HashMap<Integer, Object[]>();
        Query queryReport = this.getCurrentSession().createNativeQuery(sql);
        List<Object[]> rows = queryReport.list();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Shared sharedUtil = new Shared();
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    ps = connection.prepareStatement(sql);
                    rs = ps.executeQuery();
                    ResultSetMetaData metadata = rs.getMetaData();
                    int size = metadata.getColumnCount();
                    Object[] cabecera = new Object[size];
                    for (int i = 1; i <= size; i++) {
                        cabecera[i - 1] = metadata.getColumnLabel(i);
                    }
                    data.put(1, cabecera);
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    sharedUtil.closePreparedStatement(ps);
                    sharedUtil.closeResultSet(rs);
                }
            }
        });
        int i = 2;
        for (Object[] row : rows) {
            data.put(i, row);
            i++;
        }
        return data;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public int saveNativeQuery(String table, String[] fieldValues) throws UnknownException {
        validateIdentifier(table);
        StringBuilder builder = new StringBuilder();
        Shared sharedUtil = new Shared();
        builder.append(sharedUtil.append("INSERT INTO "));
        builder.append(sharedUtil.append(table));
        builder.append(sharedUtil.append("("));
        StringBuilder fields = new StringBuilder();
        StringBuilder parameter = new StringBuilder();
        HashMap<String, Object> queryParam = new HashMap();
        for (int i = 0; i < fieldValues.length; i++) {
            String fieldName = fieldValues[i].split(":")[0];
            validateIdentifier(fieldName);
            fields.append(sharedUtil.append(fieldName));
            parameter.append(":").append(fieldName);
            if (i < fieldValues.length - 1) {
                fields.append(sharedUtil.append(","));
                parameter.append(sharedUtil.append(","));
            }
            queryParam.put(fieldName, sharedUtil.StringToObject(fieldValues[i].split(":")[1], fieldValues[i].split(":")[2]));
        }
        builder.append(sharedUtil.append(fields.toString()));
        builder.append(sharedUtil.append(")"));
        builder.append(sharedUtil.append("values"));
        builder.append(sharedUtil.append("("));
        builder.append(sharedUtil.append(parameter.toString()));
        builder.append(sharedUtil.append(")"));
        NativeQuery query = this.getCurrentSession().createNativeQuery(builder.toString());
        Iterator<String> iteradorKey = queryParam.keySet().iterator();
        while (iteradorKey.hasNext()) {
            String paramField = iteradorKey.next();
            query.setParameter(paramField, queryParam.get(paramField));
        }
        int value = query.executeUpdate();
        return value;
    }

    public int iudNativeQuery(String sql) throws UnknownException {
        NativeQuery query = this.getCurrentSession().createNativeQuery(sql);
        int value = query.executeUpdate();
        return value;
    }

    public int iudNativeQuery(String sql, String[] parametersValues) throws UnknownException {
        Shared sharedUtil = new Shared();
        HashMap<String, Object> queryParam = new HashMap();
        for (int i = 0; i < parametersValues.length; i++) {
            queryParam.put(parametersValues[i].split(":")[0], sharedUtil.StringToObject(parametersValues[i].split(":")[1], parametersValues[i].split(":")[2]));
        }
        NativeQuery query = this.getCurrentSession().createNativeQuery(sql);
        Iterator<String> iteradorKey = queryParam.keySet().iterator();
        while (iteradorKey.hasNext()) {
            String paramField = iteradorKey.next();
            query.setParameter(paramField, queryParam.get(paramField));
        }
        int value = query.executeUpdate();
        return value;
    }

    @Override
    public void flush() {
        sessionFactory.getCurrentSession().flush();
    }

    @Override
    public void detach(T entity) {
        sessionFactory.getCurrentSession().detach(entity);
    }

    @Override
    public void clear() {
        sessionFactory.getCurrentSession().clear();
    }

    @Override
    public void evict(T entity) {
        sessionFactory.getCurrentSession().evict(entity);
    }

    @Override
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Session getNewSession() {
        return sessionFactory.openSession();
    }

    // =========================================================================
    // Parameterized DSL infrastructure — prevents SQL injection
    // =========================================================================

    /** Carries a SQL/HQL fragment and its bound parameter values together. */
    private static final class DslFragment {
        final StringBuilder sql    = new StringBuilder();
        final List<Object>  params = new ArrayList<>();
    }

    /**
     * Coerces a DSL string token to the most appropriate Java type so that
     * the JDBC driver / Hibernate can bind it to the correct column type.
     * Falls back to String when no numeric conversion succeeds.
     */
    private static Object smartParse(String value) {
        if ("true".equalsIgnoreCase(value))  return Boolean.TRUE;
        if ("false".equalsIgnoreCase(value)) return Boolean.FALSE;
        try { return Long.parseLong(value);     } catch (NumberFormatException ignored) {}
        try { return Double.parseDouble(value); } catch (NumberFormatException ignored) {}
        return value;
    }

    // Allows: campo, base.campo, tabla.campo_nombre — rejects anything with spaces, quotes, or SQL punctuation.
    private static final java.util.regex.Pattern SAFE_IDENTIFIER =
        java.util.regex.Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$");

    private static void validateIdentifier(String id) {
        if (id == null || !SAFE_IDENTIFIER.matcher(id.trim()).matches()) {
            throw new IllegalArgumentException("Unsafe SQL identifier: " + id);
        }
    }

    private static void validateConnector(String connector) {
        if (connector == null || (!connector.equalsIgnoreCase("and") && !connector.equalsIgnoreCase("or"))) {
            throw new IllegalArgumentException("Invalid SQL connector: " + connector);
        }
    }

    private static final java.util.Set<String> VALID_JOIN_TYPES =
        java.util.Collections.unmodifiableSet(new java.util.HashSet<>(
            java.util.Arrays.asList("left", "inner", "right", "cross", "full")));

    // schema.table, schema.table alias, schema.table as alias
    private static final java.util.regex.Pattern SAFE_TABLE_REF =
        java.util.regex.Pattern.compile(
            "^[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*" +
            "(\\s+(?:[aA][sS]\\s+)?[a-zA-Z_][a-zA-Z0-9_]*)?$"
        );

    private static void validateTableRef(String ref) {
        if (ref == null) throw new IllegalArgumentException("Null table reference");
        if (ref.trim().startsWith("(")) return; // subquery from trusted internal code
        if (!SAFE_TABLE_REF.matcher(ref.trim()).matches()) {
            throw new IllegalArgumentException("Unsafe table reference: " + ref);
        }
    }

    /**
     * Builds a parameterized WHERE fragment for <strong>native SQL</strong>.
     * Each value becomes a positional {@code ?} parameter; bind via
     * {@link PreparedStatement#setObject}.
     */
    private DslFragment buildNativeSqlFragment(String connector, String[] filters) {
        DslFragment result = new DslFragment();
        Shared sh = new Shared();
        if (filters==null) {
            return result;
        }        
        if (filters.length == 0) {
            result.sql.append(sh.append("where 1=2"));
            return result;
        }
        validateConnector(connector);
        result.sql.append(sh.append("where 1=1"));
        for (String f : filters) {
            result.sql.append(sh.append(connector));
            appendNativeFilter(f, result, sh);
        }
        return result;
    }

    private void appendNativeFilter(String expr, DslFragment f, Shared sh) {
        String[] v = expr.split(":");
        validateIdentifier(v[1]);
        switch (v[0]) {
            case "=":
            case ">":
            case "<":
            case ">=":
            case "<=":
            case "!=":
                f.sql.append(sh.append(v[1])).append(sh.append(v[0])).append(sh.append("?"));
                f.params.add(smartParse(v[2]));
                break;
            case "equal":
                f.sql.append(sh.append(v[1])).append(sh.append("=")).append(sh.append("?"));
                f.params.add(v[2]);
                break;
            case "notequal":
                f.sql.append(sh.append(v[1])).append(sh.append("!=")).append(sh.append("?"));
                f.params.add(v[2]);
                break;
            case "like":
                f.sql.append(sh.append(v[1])).append(sh.append("like")).append(sh.append("?"));
                f.params.add(v[2]);
                break;
            case "between":
                f.sql.append(sh.append(v[1])).append(sh.append("between"))
                     .append(sh.append("?")).append(sh.append("and")).append(sh.append("?"));
                f.params.add(smartParse(v[2]));
                f.params.add(smartParse(v[3]));
                break;
            case "in":
                f.sql.append(sh.append(v[1])).append(sh.append("in ("));
                for (int i = 2; i < v.length; i++) {
                    if (i > 2) f.sql.append(",");
                    f.sql.append("?");
                    f.params.add(smartParse(v[i]));
                }
                f.sql.append(")");
                break;
            case "isnull":
                f.sql.append(sh.append(v[1])).append(sh.append("is null"));
                break;
            case "isnotnull":
                f.sql.append(sh.append(v[1])).append(sh.append("is not null"));
                break;
            default:
                throw new IllegalArgumentException("Unknown DSL operator: " + v[0]);
        }
    }

    /**
     * Builds a parameterized WHERE fragment for <strong>HQL</strong>.
     * Each value becomes a named {@code :dslPN} parameter; bind via
     * {@link #applyHqlParams}.
     */
    private DslFragment buildHqlFragment(String connector, String[] filters) {
        DslFragment result = new DslFragment();
        Shared sh = new Shared();
        if (filters == null || filters.length == 0) {
            result.sql.append(sh.append("where 1=2"));
            return result;
        }
        validateConnector(connector);
        result.sql.append(sh.append("where 1=1"));
        for (String f : filters) {
            result.sql.append(sh.append(connector));
            appendHqlFilter(f, result, sh);
        }
        return result;
    }

    private void appendHqlFilter(String expr, DslFragment f, Shared sh) {
        String[] v = expr.split(":");
        validateIdentifier(v[1]);
        int n = f.params.size();
        switch (v[0]) {
            case "=":
            case ">":
            case "<":
            case ">=":
            case "<=":
            case "!=":
                f.sql.append(sh.append(v[1])).append(sh.append(v[0])).append(sh.append(":dslP" + n));
                f.params.add(smartParse(v[2]));
                break;
            case "equal":
                f.sql.append(sh.append(v[1])).append(sh.append("=")).append(sh.append(":dslP" + n));
                f.params.add(v[2]);
                break;
            case "notequal":
                f.sql.append(sh.append(v[1])).append(sh.append("!=")).append(sh.append(":dslP" + n));
                f.params.add(v[2]);
                break;
            case "like":
                f.sql.append(sh.append(v[1])).append(sh.append("like")).append(sh.append(":dslP" + n));
                f.params.add(v[2]);
                break;
            case "between":
                f.sql.append(sh.append(v[1])).append(sh.append("between"))
                     .append(sh.append(":dslP" + n)).append(sh.append("and")).append(sh.append(":dslP" + (n + 1)));
                f.params.add(smartParse(v[2]));
                f.params.add(smartParse(v[3]));
                break;
            case "in": {
                List<Object> inList = new ArrayList<>();
                for (int i = 2; i < v.length; i++) inList.add(smartParse(v[i]));
                f.sql.append(sh.append(v[1])).append(sh.append("in")).append(sh.append(":dslP" + n));
                f.params.add(inList);
                break;
            }
            case "isnull":
                f.sql.append(sh.append(v[1])).append(sh.append("is null"));
                break;
            case "isnotnull":
                f.sql.append(sh.append(v[1])).append(sh.append("is not null"));
                break;
            default:
                throw new IllegalArgumentException("Unknown DSL operator: " + v[0]);
        }
    }

    /** Binds collected HQL named parameters to a Hibernate Query. */
    @SuppressWarnings("unchecked")
    private void applyHqlParams(Query query, List<Object> params) {
        for (int i = 0; i < params.size(); i++) {
            Object val = params.get(i);
            if (val instanceof List) {
                query.setParameterList("dslP" + i, (List<Object>) val);
            } else {
                query.setParameter("dslP" + i, val);
            }
        }
    }

    /**
     * Executes a native SQL query via PreparedStatement, binding DSL filter
     * parameters first and then optional LIMIT/OFFSET at the end.
     */
    private void executeStatement(Connection cnctn, String sql, Shared sharedUtil, ArrayNode array,
                                  List<Object> filterParams, Long limit, Long offset) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = cnctn.prepareStatement(sql);
            int idx = 1;
            if (filterParams != null) {
                for (Object param : filterParams) {
                    ps.setObject(idx++, param);
                }
            }
            if (limit != null && offset != null) {
                ps.setLong(idx++, limit);
                ps.setLong(idx,   offset);
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
}
