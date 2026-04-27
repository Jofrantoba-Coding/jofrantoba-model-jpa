/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gob.pe.icl.icl.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDao;
import com.jofrantoba.model.jpa.shared.Shared;
import gob.pe.icl.icl.dao.inter.InterDaoMenu;
import gob.pe.icl.icl.dto.beans.FilterMenu;
import gob.pe.icl.icl.entity.Menu;
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
public class DaoMenu extends AbstractJpaDao<Menu> implements InterDaoMenu {

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
        String table = "icl.seguridad.tg_menu as base";
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
        joinTables[0] = "left:icl.seguridad.tg_menu as menuPadre:on:base.id_menu_padre:menuPadre.id";
        joinTables[1] = "inner:icl.seguridad.tg_cliente_sistema as clienteSistema:on:base.id_cliente_sistema:clienteSistema.id";
        joinTables[2] = "inner:icl.seguridad.tg_sistema as sistema:on:clienteSistema.id_sistema:sistema.id";
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
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }

    @Override
    public ArrayNode listar(FilterMenu filter) throws Exception {
        String table = "icl.seguridad.tg_menu as base";
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
        joinTables[0] = "left:icl.seguridad.tg_menu as menuPadre:on:base.id_menu_padre:menuPadre.id";
        joinTables[1] = "inner:icl.seguridad.tg_cliente_sistema as clienteSistema:on:base.id_cliente_sistema:clienteSistema.id";
        joinTables[2] = "inner:icl.seguridad.tg_sistema as sistema:on:clienteSistema.id_sistema:sistema.id";
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
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    public ArrayNode listarChildrens(FilterMenu filter) throws Exception {
        String table = "icl.seguridad.tg_menu as base";
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
        joinTables[0] = "inner:icl.seguridad.tg_menu as menuPadre:on:base.id_menu_padre:menuPadre.id";
        joinTables[1] = "inner:icl.seguridad.tg_cliente_sistema as clienteSistema:on:base.id_cliente_sistema:clienteSistema.id";
        joinTables[2] = "inner:icl.seguridad.tg_sistema as sistema:on:clienteSistema.id_sistema:sistema.id";
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
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
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
