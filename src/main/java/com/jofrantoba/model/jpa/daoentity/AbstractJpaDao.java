/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity;

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
        sql.append(Shared.append("delete")).append(Shared.append("from")).append(clazz.getName());
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
    public Collection<T> allFieldsLimitOffsetPostgres(String table,String[] mapOrder,Long limit, Long offset)throws UnknownException{
        StringBuilder sql = new StringBuilder();
        sql.append(Shared.append("select * from").append(Shared.append(table)));
        if (mapOrder != null) {
            sql.append(orderString(mapOrder));
        }
        sql.append(Shared.append("limit :paramLimit offset :paramOffset"));
        Query query=getCurrentSession().createNativeQuery(sql.toString(),clazz);
        query.setParameter("paramLimit", limit);
        query.setParameter("paramOffset", offset);
        return query.getResultList();
    }

    @Override
    public Collection<T> customFields(String fields) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        sql.append(Shared.append("select")).append(fields).append(Shared.append("from")).append(clazz.getName()).append(Shared.append("as clase"));
        Query query = getCurrentSession().createQuery(sql.toString());
        return query.list();
    }

    @Override
    public Collection<T> customFieldsFilterAnd(String fields, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        sql.append(Shared.append("select")).append(fields).append(Shared.append("from")).append(clazz.getName()).append(Shared.append("as clase"));
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
        sql.append(Shared.append("select")).append(fields).append(Shared.append("from")).append(clazz.getName()).append(Shared.append("as clase"));
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
    public Collection<T> allFieldsJoinFilter(String joinTable,String mapFilter, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        sql.append(Shared.append("select base from"));
        sql.append(clazz.getName());
        sql.append(Shared.append("as base"));
        sql.append(Shared.append(joinTable.split(":")[0]+" join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        sql.append(Shared.append("where 1=1"));
        sql.append(Shared.append("and " + filterStringSelect(mapFilter).toString()));     
        if (mapOrder != null) {
            sql.append(Shared.append(orderString(mapOrder).toString()));
        }        
        Query query = getCurrentSession().createQuery(sql.toString());        
        Collection<T> valores = query.list();
        return valores;
    }        
    
    @Override
    public Collection<T> allFieldsJoinFilterAnd(String joinTable,String[] mapFilter, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        sql.append(Shared.append("select base from"));
        sql.append(clazz.getName());
        sql.append(Shared.append("as base"));
        sql.append(Shared.append(joinTable.split(":")[0]+" join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        sql.append(Shared.append(buildFilterStringSelect("and", mapFilter).toString()));
        sql.append(Shared.append(orderString(mapOrder).toString()));
        Query query = getCurrentSession().createQuery(sql.toString());        
        Collection<T> valores = query.list();
        return valores;
    }
    
    @Override
    public Long rowCountJoinFilterAnd(String joinTable, String[] mapFilterField) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        sql.append(Shared.append("select").append("count(*)").append(Shared.append("from")));
        sql.append(clazz.getName());
        sql.append(Shared.append("as base"));
        sql.append(Shared.append(joinTable.split(":")[0]+" join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        sql.append(Shared.append(buildFilterStringSelect("and", mapFilterField).toString()));        
        Query query = getCurrentSession().createQuery(sql.toString());                                                   
        Long count = (Long)query.uniqueResult();
        return count;
    }
    
    @Override
    public Collection<T> customFieldsJoinFilterAnd(String fields,String joinTable, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        sql.append(Shared.append("select").append(fields).append(Shared.append("from")));
        sql.append(clazz.getName());
        sql.append(Shared.append("as base"));
        sql.append(Shared.append(joinTable.split(":")[0]+" join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        sql.append(Shared.append(buildFilterStringSelect("and", mapFilterField).toString()));
        sql.append(Shared.append(orderString(mapOrder).toString()));
        Query query = getCurrentSession().createQuery(sql.toString());                                           
        query.setResultTransformer(Transformers.aliasToBean(clazz));
        Collection valores = query.getResultList();
        return valores;
    }
    
    @Override
    public Collection<?> customFieldsJoinFilterAnd(Class<?> dto,String fields,String joinTable, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        sql.append(Shared.append("select").append(fields).append(Shared.append("from")));
        sql.append(clazz.getName());
        sql.append(Shared.append("as base"));
        sql.append(Shared.append(joinTable.split(":")[0]+" join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        sql.append(Shared.append(buildFilterStringSelect("and", mapFilterField).toString()));
        sql.append(Shared.append(orderString(mapOrder).toString()));
        Query query = getCurrentSession().createQuery(sql.toString());                                           
        query.setResultTransformer(Transformers.aliasToBean(dto));
        Collection valores = query.getResultList();
        return valores;
    }
    
    
    @Override
    public Collection<?> customFieldsJoinFilterAnd(ResultTransformer rt,String fields,String joinTable, String[] mapFilterField, String[] mapOrder) throws UnknownException {
        StringBuilder sql = new StringBuilder();
        sql.append(Shared.append("select").append(fields).append(Shared.append("from")));
        sql.append(clazz.getName());
        sql.append(Shared.append("as base"));
        sql.append(Shared.append(joinTable.split(":")[0]+" join base." + joinTable.split(":")[1] + " " + joinTable.split(":")[1]));
        sql.append(Shared.append(buildFilterStringSelect("and", mapFilterField).toString()));
        sql.append(Shared.append(orderString(mapOrder).toString()));
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
        if (mapFilterField.length > 0) {
            filter.append(Shared.append("where 1=1"));
            for (int i = 0; i < mapFilterField.length; i++) {
                filter.append(Shared.append(conectorLogico)).append(Shared.append(filterStringSelect(mapFilterField[i]).toString()));
                //return filter;
            }
        } else {
            filter.append(Shared.append("where 1=2"));
        }
        return filter;
    }

    private StringBuilder buildFilterString(String conectorLogico, String[] mapFilterField) {
        StringBuilder filter = new StringBuilder();
        if (mapFilterField.length > 0) {
            filter.append(Shared.append("where 1=1"));
            for (int i = 0; i < mapFilterField.length; i++) {
                filter.append(Shared.append(conectorLogico)).append(Shared.append(filterString(mapFilterField[i]).toString()));
                //return filter;
            }
        } else {
            filter.append(Shared.append("where 1=2"));
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
        order.append(Shared.append("order by"));
        if (mapOrder != null && mapOrder.length > 0) {
            for (int i = 0; i < mapOrder.length; i++) {
                order.append(Shared.append(mapOrder[i].split(":")[0]));
                order.append(Shared.append(mapOrder[i].split(":")[1]));
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
        switch (mapFilterField.split(":")[0]) {
            case ">":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append(">"));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case "<":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("<"));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case ">=":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append(">="));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case "<=":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("<="));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case "=":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("="));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case "!=":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("!="));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case "like":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("like"));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case "between":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("between"));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                pre.append(Shared.append("and"));
                pre.append(Shared.append(mapFilterField.split(":")[3]));
                break;
            case "in":
                String valuesIn = "";
                for (int i = 2; i < mapFilterField.split(":").length; i++) {
                    valuesIn = valuesIn + mapFilterField.split(":")[i];
                }
                pre.append(Shared.append("in"));
                pre.append(Shared.append("("));
                pre.append(Shared.append(valuesIn.replaceAll(":", ",")));
                pre.append(Shared.append(")"));
                break;
            case "isnull":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("is null"));                
                break;
            case "isnotnull":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("is not null"));                
                break;
        }
        return pre;
    }

    private StringBuilder filterString(String mapFilterField) {
        StringBuilder pre = new StringBuilder();
        switch (mapFilterField.split(":")[0]) {
            case ">":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append(">"));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case "<":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("<"));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case ">=":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append(">="));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case "<=":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("<="));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case "=":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("="));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case "!=":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("!="));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case "like":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("like"));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                break;
            case "between":
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                pre.append(Shared.append("between"));
                pre.append(Shared.append(mapFilterField.split(":")[2]));
                pre.append(Shared.append("and"));
                pre.append(Shared.append(mapFilterField.split(":")[3]));
                break;
            case "in":
                String valuesIn = "";
                for (int i = 2; i < mapFilterField.split(":").length; i++) {
                    valuesIn = valuesIn + mapFilterField.split(":")[i];
                }
                pre.append(Shared.append("in"));
                pre.append(Shared.append("("));
                pre.append(Shared.append(valuesIn.replaceAll(":", ",")));
                pre.append(Shared.append(")"));
                break;
            case "isnull":
                pre.append(Shared.append("is null"));
                pre.append(Shared.append(mapFilterField.split(":")[1]));
                break;
            case "isnotnull":
                pre.append(Shared.append("is not null"));
                pre.append(Shared.append(mapFilterField.split(":")[1]));
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
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData metadata = rs.getMetaData();
                int size = metadata.getColumnCount();
                Object[] cabecera = new Object[size];
                for (int i = 1; i <= size; i++) {
                    cabecera[i - 1] = metadata.getColumnLabel(i);
                }
                data.put(1, cabecera);
                Shared.closePreparedStatement(ps);
                Shared.closeResultSet(rs);
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
        builder.append(Shared.append("INSERT INTO "));
        builder.append(Shared.append(table));
        builder.append(Shared.append("("));
        StringBuilder fields = new StringBuilder();
        StringBuilder parameter = new StringBuilder();
        HashMap<String, Object> queryParam = new HashMap();
        for (int i = 0; i < fieldValues.length; i++) {
            fields.append(Shared.append(fieldValues[i].split(":")[0]));
            parameter.append(":").append(fieldValues[i].split(":")[0]);
            if (i < fieldValues.length - 1) {
                fields.append(Shared.append(","));
                parameter.append(Shared.append(","));
            }
            queryParam.put(fieldValues[i].split(":")[0], Shared.StringToObject(fieldValues[i].split(":")[1], fieldValues[i].split(":")[2]));
        }
        builder.append(Shared.append(fields.toString()));
        builder.append(Shared.append(")"));
        builder.append(Shared.append("values"));
        builder.append(Shared.append("("));
        builder.append(Shared.append(parameter.toString()));
        builder.append(Shared.append(")"));
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
