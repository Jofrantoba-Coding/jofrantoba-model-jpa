/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daomenu;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jofrantoba.model.jpa.daoentity.AbstractJpaDaoV2;
import com.jofrantoba.model.jpa.shared.Shared;
import com.jofrantoba.model.jpa.shared.UnknownException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.hibernate.transform.ResultTransformer;

/**
 *
 * @author jofrantoba
 */
public class DaoMenu extends AbstractJpaDaoV2<Menu>
        implements InterDaoMenu {

    public DaoMenu() {
        super();
        setClazz(Menu.class);
    }

    // --- select ---

    @Override
    public Collection<Menu> parents(FilterMenu filter) throws Exception {
        String[] joinTable = {"left:parent", "inner:clienteSistema", "inner:clienteSistema.sistema"};
        List<String> filterList = buildFilter(filter);
        filterList.add(0, "isnull:parent.id");
        String[] mapFilterField = filterList.toArray(new String[0]);
        String[] mapOrder = {"base.orden:asc"};
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.icono as icono,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("base.nivel as nivel,"));
        strFields.append(share.append("base.tipo as tipo,"));
        strFields.append(share.append("base.ruta as ruta,"));
        strFields.append(share.append("base.numeroSubmenu as numeroSubmenu"));
        return customFieldsJoinFilterAnd(strFields.toString(), joinTable, mapFilterField, mapOrder);
    }

    @Override
    public Collection<Menu> parents(FilterMenu filter, int pageNumber, int pageSize) throws Exception {
        String[] joinTable = {"left:parent", "inner:clienteSistema", "inner:clienteSistema.sistema"};
        List<String> filterList = buildFilter(filter);
        filterList.add(0, "isnull:parent.id");
        String[] mapFilterField = filterList.toArray(new String[0]);
        String[] mapOrder = {"base.orden:asc"};
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("base.nivel as nivel,"));
        strFields.append(share.append("base.tipo as tipo,"));
        strFields.append(share.append("base.ruta as ruta,"));
        strFields.append(share.append("base.numeroSubmenu as numeroSubmenu"));
        return customFieldsJoinFilterAnd(strFields.toString(), joinTable, mapFilterField, mapOrder, pageNumber, pageSize);
    }

    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    @Override
    public Collection<Menu> childrens(FilterMenu filter) throws Exception {
        String[] joinTable = {"left:parent", "inner:clienteSistema", "inner:clienteSistema.sistema"};
        List<String> filterList = buildFilter(filter);
        filterList.add(0, "isnotnull:parent.id");
        String[] mapFilterField = filterList.toArray(new String[0]);
        String[] mapOrder = {"base.orden:asc"};
        String fields = fieldsChildrens();
        ResultTransformer rt = resultTransformer();
        return (Collection<Menu>) customFieldsJoinFilterAnd(rt, fields, joinTable, mapFilterField, mapOrder);
    }

    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    @Override
    public Collection<Menu> childrensByParents(Long idParent) throws Exception {
        String joinTable = "left:parent";
        String[] mapFilterField = {"=:parent.id:" + idParent, "=:base.isPersistente:true"};
        String[] mapOrder = {"base.orden:asc"};
        String fields = fieldsChildrens();
        ResultTransformer rt = resultTransformer();
        return (Collection<Menu>) customFieldsJoinFilterAnd(rt, fields, joinTable, mapFilterField, mapOrder);
    }

    @Override
    public Long countChildrens(Long idParent) throws Exception {
        String joinTable = "left:parent";
        String[] mapFilterField = {"=:parent.id:" + idParent, "=:base.isPersistente:true"};
        return rowCountJoinFilterAnd(joinTable, mapFilterField);
    }

    @Override
    public Long maxOrdenChildrens(Long idParent) throws Exception {
        String joinTable = "left:parent";
        String[] mapFilterField = {"=:parent.id:" + idParent, "=:base.isPersistente:true"};
        Long maxOrden = (Long) maxValueJoinFilterAnd("base.orden", joinTable, mapFilterField);
        return maxOrden != null ? maxOrden : 0L;
    }

    @Override
    public Long maxOrdenNivel(Long idClienteSistema, Long nivel) throws Exception {
        String joinTable = "inner:clienteSistema";
        String[] mapFilterField = {
            "=:clienteSistema.id:" + idClienteSistema,
            "=:base.isPersistente:true",
            "=:base.nivel:" + nivel
        };
        Long maxOrden = (Long) maxValueJoinFilterAnd("base.orden", joinTable, mapFilterField);
        return maxOrden != null ? maxOrden : 0L;
    }

    @Override
    public ArrayNode listar(FilterMenu filter, Long limit, Long offSet) throws Exception {
        String table = "seguridad.tg_menu as base";
        String fields = fieldsListar();
        String[] joinTables = joinTablesListar();
        String[] mapFilterField = buildFilterSql(filter).toArray(new String[0]);
        String[] mapOrder = {"orden:asc"};
        return allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet, "and");
    }

    @Override
    public ArrayNode listar(FilterMenu filter) throws Exception {
        String table = "seguridad.tg_menu as base";
        String fields = fieldsListar();
        String[] joinTables = joinTablesListar();
        String[] mapFilterField = buildFilterSql(filter).toArray(new String[0]);
        String[] mapOrder = {"orden:asc"};
        return allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder, "and");
    }

    public ArrayNode listarChildrens(FilterMenu filter) throws Exception {
        String table = "seguridad.tg_menu as base";
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.icono as icono,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.nivel as nivel,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("base.ruta as path,"));
        strFields.append(share.append("base.tipo as tipo,"));
        strFields.append(share.append("base.numero_submenu as numerosubmenu,"));
        strFields.append(share.append("menuPadre.id as idmenupadre"));
        String[] joinTables = new String[3];
        joinTables[0] = "inner:seguridad.tg_menu as menuPadre:on:base.id_menu_padre:menuPadre.id";
        joinTables[1] = "inner:seguridad.tg_cliente_sistema as clienteSistema:on:base.id_cliente_sistema:clienteSistema.id";
        joinTables[2] = "inner:seguridad.tg_sistema as sistema:on:clienteSistema.id_sistema:sistema.id";
        String[] mapFilterField = buildFilterSql(filter).toArray(new String[0]);
        String[] mapOrder = {"orden:asc"};
        return allFieldsJoinPostgres(joinTables, table, strFields.toString(), mapFilterField, mapOrder, "and");
    }

    @Override
    public ArrayNode createTreeMenu(FilterMenu filter) throws Exception {
        Collection<Menu> listParents = parents(filter);
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

    // --- insert ---

    @Override
    public void saveAll(List<Menu> lista) throws UnknownException {
        for (Menu menu : lista) {
            save(menu);
        }
    }

    // --- update ---

    @Override
    public void updateDescripcion(Long id, String descripcion) throws Exception {
        Menu menu = findById(id);
        if (menu != null) {
            menu.setDescripcion(descripcion);
            menu.setVersion(System.currentTimeMillis());
            update(menu);
        }
    }

    @Override
    public void updateOrden(Long id, Long orden) throws Exception {
        Menu menu = findById(id);
        if (menu != null) {
            menu.setOrden(orden);
            menu.setVersion(System.currentTimeMillis());
            update(menu);
        }
    }

    @Override
    public void marcarInactivo(Long id) throws Exception {
        Menu menu = findById(id);
        if (menu != null) {
            menu.setIsPersistente(Boolean.FALSE);
            menu.setVersion(System.currentTimeMillis());
            update(menu);
        }
    }

    // --- delete ---

    @Override
    public void deleteByIds(List<Long> ids) throws Exception {
        for (Long id : ids) {
            delete(id);
        }
    }

    @Override
    public int deleteInactivos() throws Exception {
        return deleteFilterAnd(new String[]{"=:base.isPersistente:false"});
    }

    @Override
    public int deleteAll() throws Exception {
        return deleteFilterAnd(new String[]{"isnotnull:base.id"});
    }

    private List<String> buildFilter(FilterMenu filter) {
        List<String> filterList = new ArrayList<>();
        filterList.add("=:base.isPersistente:true");
        if (filter.getIdClienteSistema() != null) {
            filterList.add("=:clienteSistema.id:" + filter.getIdClienteSistema());
        }
        if (filter.getIdSistema() != null) {
            filterList.add("=:sistema.id:" + filter.getIdSistema());
        }
        return filterList;
    }

    private List<String> buildFilterSql(FilterMenu filter) {
        List<String> filterList = new ArrayList<>();
        filterList.add("=:base.is_persistente:true");
        if (filter.getIdClienteSistema() != null) {
            filterList.add("=:clienteSistema.id:" + filter.getIdClienteSistema());
        }
        if (filter.getIdSistema() != null) {
            filterList.add("=:sistema.id:" + filter.getIdSistema());
        }
        return filterList;
    }

    private String fieldsChildrens() {
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("base.nivel as nivel,"));
        strFields.append(share.append("base.tipo as tipo,"));
        strFields.append(share.append("base.ruta as ruta,"));
        strFields.append(share.append("base.numeroSubmenu as numeroSubmenu,"));
        strFields.append(share.append("parent.id as idParent,"));
        strFields.append(share.append("parent.descripcion as descripcionParent"));
        return strFields.toString();
    }

    private String fieldsListar() {
        Shared share = new Shared();
        StringBuilder strFields = new StringBuilder();
        strFields.append(share.append("base.id as id,"));
        strFields.append(share.append("base.orden as orden,"));
        strFields.append(share.append("base.icono as icono,"));
        strFields.append(share.append("base.descripcion as descripcion,"));
        strFields.append(share.append("menuPadre.id as idMenuPadre,"));
        strFields.append(share.append("menuPadre.descripcion as descripcionMenuPadre,"));
        strFields.append(share.append("clienteSistema.descripcion as descripcionApp,"));
        strFields.append(share.append("sistema.descripcion as descripcionSistema"));
        return strFields.toString();
    }

    private String[] joinTablesListar() {
        String[] joinTables = new String[3];
        joinTables[0] = "left:seguridad.tg_menu as menuPadre:on:base.id_menu_padre:menuPadre.id";
        joinTables[1] = "inner:seguridad.tg_cliente_sistema as clienteSistema:on:base.id_cliente_sistema:clienteSistema.id";
        joinTables[2] = "inner:seguridad.tg_sistema as sistema:on:clienteSistema.id_sistema:sistema.id";
        return joinTables;
    }

    @SuppressWarnings({"deprecation", "rawtypes"})
    private ResultTransformer resultTransformer() {
        return new ResultTransformer() {
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
    }

    private void createTree(ObjectNode nodePadre, ArrayNode arrayChildrens) throws Exception {
        int cont = 0;
        for (int j = 0; j < arrayChildrens.size(); j++) {
            JsonNode beanHijo = arrayChildrens.get(j);
            if (beanHijo.isObject()) {
                ObjectNode nodeHijo = (ObjectNode) beanHijo;
                if (nodePadre.get("id").asLong() == nodeHijo.get("idmenupadre").asLong()) {
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
                if (cont == nodePadre.get("numerosubmenu").asInt()) {
                    break;
                }
            }
        }
    }
}
