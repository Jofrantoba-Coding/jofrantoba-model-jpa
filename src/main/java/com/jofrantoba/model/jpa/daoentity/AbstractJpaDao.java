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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.hibernate.LockMode;
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
public abstract class AbstractJpaDao<T extends Serializable> implements InterCrud<T> {

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
        sql.append(sharedUtil.append("delete")).append(sharedUtil.append("from")).append(clazz.getName());
        sql.append(buildFilterString("and", mapFilterField));
        Query query = getCurrentSession().createQuery(sql.toString());
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
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilterField).toString()));
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = cnctn.prepareStatement(sql.toString());
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData metadata = rs.getMetaData();
                int size = metadata.getColumnCount();
                while (rs.next()) {
                    ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
                    for (int i = 1; i <= size; i++) {
                        log.debug(metadata.getColumnLabel(i));
                        log.debug(metadata.getColumnClassName(i));
                        log.debug(metadata.getColumnName(i));
                        log.debug(metadata.getColumnTypeName(i));
                        if (metadata.getColumnTypeName(i).equals("varchar")) {
                            node.put(metadata.getColumnName(i), rs.getString(metadata.getColumnName(i)));
                        }
                        if (metadata.getColumnTypeName(i).equals("serial")) {
                            node.put(metadata.getColumnName(i), rs.getLong(metadata.getColumnName(i)));
                        }
                    }
                    array.add(node);
                }

                sharedUtil.closePreparedStatement(ps);
                sharedUtil.closeResultSet(rs);
            }
        });
        return array;
    }
    
    @Override
    public ArrayNode allFieldsJoinPostgres(String joinTable,String table, String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append(fields));
        sql.append(sharedUtil.append("from"));
        sql.append(sharedUtil.append(table));
        String[] join=joinTable.split(":");
        sql.append(sharedUtil.append(join[0] + " join " + join[1] + " on " + join[3]+"="+join[4]));
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilterField).toString()));
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = cnctn.prepareStatement(sql.toString());
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData metadata = rs.getMetaData();
                int size = metadata.getColumnCount();
                while (rs.next()) {
                    ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
                    for (int i = 1; i <= size; i++) {
                        log.debug(metadata.getColumnLabel(i));
                        log.debug(metadata.getColumnClassName(i));
                        log.debug(metadata.getColumnName(i));
                        log.debug(metadata.getColumnTypeName(i));
                        if (metadata.getColumnTypeName(i).equals("varchar")) {
                            node.put(metadata.getColumnName(i), rs.getString(metadata.getColumnName(i)));
                        }
                        if (metadata.getColumnTypeName(i).equals("serial")) {
                            node.put(metadata.getColumnName(i), rs.getLong(metadata.getColumnName(i)));
                        }
                    }
                    array.add(node);
                }

                sharedUtil.closePreparedStatement(ps);
                sharedUtil.closeResultSet(rs);
            }
        });
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
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilterField).toString()));
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        sql.append(sharedUtil.append("limit ? offset ?"));
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = cnctn.prepareStatement(sql.toString());
                ps.setLong(1, limit);
                ps.setLong(2, offset);
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData metadata = rs.getMetaData();
                int size = metadata.getColumnCount();
                while (rs.next()) {
                    ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
                    for (int i = 1; i <= size; i++) {
                        log.debug(metadata.getColumnLabel(i));
                        log.debug(metadata.getColumnClassName(i));
                        log.debug(metadata.getColumnName(i));
                        log.debug(metadata.getColumnTypeName(i));
                        if (metadata.getColumnTypeName(i).equals("varchar")) {
                            node.put(metadata.getColumnName(i), rs.getString(metadata.getColumnName(i)));
                        }
                        if (metadata.getColumnTypeName(i).equals("serial")) {
                            node.put(metadata.getColumnName(i), rs.getLong(metadata.getColumnName(i)));
                        }
                    }
                    array.add(node);
                }

                sharedUtil.closePreparedStatement(ps);
                sharedUtil.closeResultSet(rs);
            }
        });
        return array;
    }
    
    @Override
    public ArrayNode allFieldsJoinLimitOffsetPostgres(String joinTable,String table, String fields, String[] mapFilterField, String[] mapOrder, Long limit, Long offset) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select"));
        sql.append(sharedUtil.append(fields));
        sql.append(sharedUtil.append("from"));
        sql.append(sharedUtil.append(table));
        String[] join=joinTable.split(":");
        sql.append(sharedUtil.append(join[0] + " join " + join[1] + " on " + join[3]+"="+join[4]));
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilterField).toString()));
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        sql.append(sharedUtil.append("limit ? offset ?"));
        ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection cnctn) throws SQLException {
                PreparedStatement ps = cnctn.prepareStatement(sql.toString());
                ps.setLong(1, limit);
                ps.setLong(2, offset);
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData metadata = rs.getMetaData();
                int size = metadata.getColumnCount();
                while (rs.next()) {
                    ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
                    for (int i = 1; i <= size; i++) {
                        log.debug(metadata.getColumnLabel(i));
                        log.debug(metadata.getColumnClassName(i));
                        log.debug(metadata.getColumnName(i));
                        log.debug(metadata.getColumnTypeName(i));
                        if (metadata.getColumnTypeName(i).equals("varchar")) {
                            node.put(metadata.getColumnName(i), rs.getString(metadata.getColumnName(i)));
                        }
                        if (metadata.getColumnTypeName(i).equals("serial")) {
                            node.put(metadata.getColumnName(i), rs.getLong(metadata.getColumnName(i)));
                        }
                    }
                    array.add(node);
                }

                sharedUtil.closePreparedStatement(ps);
                sharedUtil.closeResultSet(rs);
            }
        });
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
    public Collection<T> customFieldsFilterAnd(String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select")).append(fields).append(sharedUtil.append("from")).append(clazz.getName()).append(sharedUtil.append("as clase"));
        sql.append(buildFilterString("and", mapFilterField));
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
        return query.list();
    }

    @Override
    public Collection<T> customFieldsFilterOr(String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select")).append(fields).append(sharedUtil.append("from")).append(clazz.getName()).append(sharedUtil.append("as clase"));
        sql.append(buildFilterString("or", mapFilterField));
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
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
        sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        sql.append(sharedUtil.append("where 1=1"));
        sql.append(sharedUtil.append("and " + filterStringSelect(mapFilter).toString()));
        if (mapOrder != null) {
            sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        }
        Query query = getCurrentSession().createQuery(sql.toString());
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
        sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilter).toString()));
        sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        Query query = getCurrentSession().createQuery(sql.toString());
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
        sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilterField).toString()));
        Query query = getCurrentSession().createQuery(sql.toString());
        Long count = (Long) query.uniqueResult();
        return count;
    }
    
    @Override
    public Collection<T> customFieldsJoinFilterAnd(String fields, String joinTable, String[] mapFilterField, String[] mapOrder,int pageNumber, int pageSize) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        Shared sharedUtil = new Shared();
        sql.append(sharedUtil.append("select").append(fields).append(sharedUtil.append("from")));
        sql.append(clazz.getName());
        sql.append(sharedUtil.append("as base"));
        sql.append(sharedUtil.append(joinTable.split(":")[0] + " join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilterField).toString()));
        sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        Query query = getCurrentSession().createQuery(sql.toString()); 
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        query.setResultTransformer(Transformers.aliasToBean(clazz));
        Collection valores = query.getResultList();
        return valores;
    }
    
    @Override
    public Collection<T> allFields(String mapFilterField, String[] mapOrder,int pageNumber, int pageSize) throws UnknownException {
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
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilterField).toString()));
        sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        Query query = getCurrentSession().createQuery(sql.toString());        
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
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilterField).toString()));
        sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        Query query = getCurrentSession().createQuery(sql.toString());
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
        sql.append(sharedUtil.append(buildFilterStringSelect("and", mapFilterField).toString()));
        sql.append(sharedUtil.append(orderString(mapOrder).toString()));
        Query query = getCurrentSession().createQuery(sql.toString());
        query.setResultTransformer(rt);
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

    private StringBuilder buildFilterStringSelect(String conectorLogico, String[] mapFilterField) {
        StringBuilder filter = new StringBuilder();
        Shared sharedUtil = new Shared();
        if (mapFilterField.length > 0) {
            filter.append(sharedUtil.append("where 1=1"));
            for (int i = 0; i < mapFilterField.length; i++) {
                filter.append(sharedUtil.append(conectorLogico)).append(sharedUtil.append(filterStringSelect(mapFilterField[i]).toString()));
                //return filter;
            }
        } else {
            filter.append(sharedUtil.append("where 1=2"));
        }
        return filter;
    }

    private StringBuilder buildFilterString(String conectorLogico, String[] mapFilterField) {
        StringBuilder filter = new StringBuilder();
        Shared sharedUtil = new Shared();
        if (mapFilterField.length > 0) {
            filter.append(sharedUtil.append("where 1=1"));
            for (int i = 0; i < mapFilterField.length; i++) {
                filter.append(sharedUtil.append(conectorLogico)).append(sharedUtil.append(filterString(mapFilterField[i]).toString()));
                //return filter;
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
                order.append(sharedUtil.append(mapOrder[i].split(":")[0]));
                order.append(sharedUtil.append(mapOrder[i].split(":")[1]));
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
        switch (mapFilterField.split(":")[0]) {
            case ">":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append(">"));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case "<":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("<"));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case ">=":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append(">="));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case "<=":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("<="));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case "=":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("="));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case "!=":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("!="));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case "like":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("like"));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case "between":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("between"));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                pre.append(sharedUtil.append("and"));
                pre.append(sharedUtil.append(mapFilterField.split(":")[3]));
                break;
            case "in":
                String valuesIn = "";
                for (int i = 2; i < mapFilterField.split(":").length; i++) {
                    valuesIn = valuesIn + mapFilterField.split(":")[i];
                }
                pre.append(sharedUtil.append("in"));
                pre.append(sharedUtil.append("("));
                pre.append(sharedUtil.append(valuesIn.replaceAll(":", ",")));
                pre.append(sharedUtil.append(")"));
                break;
            case "isnull":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("is null"));
                break;
            case "isnotnull":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("is not null"));
                break;
        }
        return pre;
    }

    private StringBuilder filterString(String mapFilterField) {
        StringBuilder pre = new StringBuilder();
        Shared sharedUtil = new Shared();
        switch (mapFilterField.split(":")[0]) {
            case ">":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append(">"));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case "<":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("<"));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case ">=":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append(">="));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case "<=":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("<="));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case "=":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("="));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case "!=":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("!="));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case "like":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("like"));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                break;
            case "between":
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                pre.append(sharedUtil.append("between"));
                pre.append(sharedUtil.append(mapFilterField.split(":")[2]));
                pre.append(sharedUtil.append("and"));
                pre.append(sharedUtil.append(mapFilterField.split(":")[3]));
                break;
            case "in":
                String valuesIn = "";
                for (int i = 2; i < mapFilterField.split(":").length; i++) {
                    valuesIn = valuesIn + mapFilterField.split(":")[i];
                }
                pre.append(sharedUtil.append("in"));
                pre.append(sharedUtil.append("("));
                pre.append(sharedUtil.append(valuesIn.replaceAll(":", ",")));
                pre.append(sharedUtil.append(")"));
                break;
            case "isnull":
                pre.append(sharedUtil.append("is null"));
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                break;
            case "isnotnull":
                pre.append(sharedUtil.append("is not null"));
                pre.append(sharedUtil.append(mapFilterField.split(":")[1]));
                break;
        }
        return pre;
    }

    private Predicate filterString(CriteriaBuilder build, CriteriaQuery<T> criteria, Root<T> root,
            String mapFilterField) {
        Predicate pre = null;
        switch (mapFilterField.split(":")[0]) {
            case ">":
                pre = build.gt(root.get(mapFilterField.split(":")[1]), Double.parseDouble(mapFilterField.split(":")[2]));
                break;
            case "<":
                pre = build.lt(root.get(mapFilterField.split(":")[1]), Double.parseDouble(mapFilterField.split(":")[2]));
                break;
            case ">=":
                pre = build.ge(root.get(mapFilterField.split(":")[1]), Double.parseDouble(mapFilterField.split(":")[2]));
                break;
            case "<=":
                pre = build.le(root.get(mapFilterField.split(":")[1]), Double.parseDouble(mapFilterField.split(":")[2]));
                break;
            case "=":
                pre = build.equal(root.get(mapFilterField.split(":")[1]), Double.parseDouble(mapFilterField.split(":")[2]));
                break;
            case "like":
                pre = build.like(root.get(mapFilterField.split(":")[1]), mapFilterField.split(":")[2]);
                break;
            case "between":
                pre = build.between(root.get(mapFilterField.split(":")[1]),
                        Double.parseDouble(mapFilterField.split(":")[2]), Double.parseDouble(mapFilterField.split(":")[3]));
                break;
            case "in":
                Collection inValue = new ArrayList();
                for (int i = 2; i < mapFilterField.split(":").length; i++) {
                    inValue.add(mapFilterField.split(":")[i]);
                }
                pre = root.get(mapFilterField.split(":")[1]).in(inValue);
                break;
            case "isnull":
                pre = build.isNull(root.get(mapFilterField.split(":")[1]));
                break;
            case "isnotnull":
                pre = build.isNotNull(root.get(mapFilterField.split(":")[1]));
                break;
            case "istrue":
                pre = build.isTrue(root.get(mapFilterField.split(":")[1]));
                break;
            case "isfalse":
                pre = build.isFalse(root.get(mapFilterField.split(":")[1]));
                break;
            case "equal":
                pre = build.equal(root.get(mapFilterField.split(":")[1]), mapFilterField.split(":")[2]);
                break;
        }
        return pre;
    }

    @Override
    public Map<Integer, Object[]> sqlExportTOExcel(String sql) throws UnknownException {
        Map<Integer, Object[]> data = new HashMap<Integer, Object[]>();
        Query queryReport = this.getCurrentSession().createSQLQuery(sql);
        List<Object[]> rows = queryReport.list();
        this.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Shared sharedUtil = new Shared();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData metadata = rs.getMetaData();
                int size = metadata.getColumnCount();
                Object[] cabecera = new Object[size];
                for (int i = 1; i <= size; i++) {
                    cabecera[i - 1] = metadata.getColumnLabel(i);
                }
                data.put(1, cabecera);
                sharedUtil.closePreparedStatement(ps);
                sharedUtil.closeResultSet(rs);
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
    public int saveNativeQuery(String table, String[] fieldValues) {
        StringBuilder builder = new StringBuilder();
        Shared sharedUtil = new Shared();
        builder.append(sharedUtil.append("INSERT INTO "));
        builder.append(sharedUtil.append(table));
        builder.append(sharedUtil.append("("));
        StringBuilder fields = new StringBuilder();
        StringBuilder parameter = new StringBuilder();
        HashMap<String, Object> queryParam = new HashMap();
        for (int i = 0; i < fieldValues.length; i++) {
            fields.append(sharedUtil.append(fieldValues[i].split(":")[0]));
            parameter.append(":").append(fieldValues[i].split(":")[0]);
            if (i < fieldValues.length - 1) {
                fields.append(sharedUtil.append(","));
                parameter.append(sharedUtil.append(","));
            }
            queryParam.put(fieldValues[i].split(":")[0], sharedUtil.StringToObject(fieldValues[i].split(":")[1], fieldValues[i].split(":")[2]));
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

}
