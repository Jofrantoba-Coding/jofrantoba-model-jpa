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
import gob.pe.icl.icl.dao.inter.InterDaoPerfilMenu;
import gob.pe.icl.icl.dto.beans.FilterPerfilMenu;
import gob.pe.icl.icl.entity.PerfilMenu;
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
public class DaoPerfilMenu extends AbstractJpaDao<PerfilMenu> implements InterDaoPerfilMenu {
    
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
        return this.allFieldsJoinLimitOffsetPostgres(joinTables, table, fields, mapFilterField, mapOrder, limit, offSet);
    }

    @Override
    public ArrayNode listar(FilterPerfilMenu filter) throws Exception {
        Map<String, Object> map = buildQueryList(filter);
        String fields = (String) map.get("fields");
        String[] mapOrder = (String[]) map.get("mapOrder");
        String[] joinTables = (String[]) map.get("joinTables");
        String table = (String) map.get("table");
        String[] mapFilterField = (String[]) map.get("mapFilterField");
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    private Map<String, Object> buildQueryList(FilterPerfilMenu filter) {
        Map<String, Object> map = new HashMap();
        String table = "icl.seguridad.tg_perfil_menu as base";
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
        joinTables[0] = "inner:icl.seguridad.tg_perfil as perfil:on:base.id_perfil:perfil.id";
        joinTables[1] = "inner:icl.seguridad.tg_menu as menu:on:base.id_menu:menu.id";
        joinTables[2] = "inner:icl.seguridad.tg_sistema as sistema:on:perfil.id_sistema:sistema.id";
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
        String table = "(select * from icl.seguridad.tg_perfil_menu where id_perfil=" + filter.getIdPerfil() + " and is_persistente=true) as base";
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
        joinTables[0] = "right:icl.seguridad.tg_menu as menu:on:base.id_menu:menu.id";
        List<String> filters = new ArrayList();
        filters.add("isnull:menu.id_menu_padre");
        filters.add("=:menu.id_cliente_sistema:" + filter.getIdClienteSistema());
        filters.add("=:menu.is_persistente:true");
        if (filter.getIsOnlyPerfilMenu()) {
            filters.add("=:base.id_perfil:" + Long.parseLong(filter.getIdPerfil()));
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"menu.orden:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
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
        String table = "(select * from icl.seguridad.tg_perfil_menu where id_perfil in (" + valuesIn + ") and is_persistente=true) as base";
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
        joinTables[0] = "right:icl.seguridad.tg_menu as menu:on:base.id_menu:menu.id";
        List<String> filters = new ArrayList();
        filters.add("isnull:menu.id_menu_padre");
        filters.add("=:menu.id_cliente_sistema:" + filter.getIdClienteSistema());
        filters.add("=:menu.is_persistente:true");
        filters.add("in:base.id_perfil:" + filter.getIdPerfil());
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"menu.orden:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    private ArrayNode menuChildrens(FilterPerfilMenu filter) throws Exception {
        String table = "(select * from icl.seguridad.tg_perfil_menu where id_perfil=" + filter.getIdPerfil() + " and is_persistente=true) as base";
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
        joinTables[0] = "right:icl.seguridad.tg_menu as menu:on:base.id_menu:menu.id";
        joinTables[1] = "inner:icl.seguridad.tg_menu as menuPadre:on:menu.id_menu_padre:menuPadre.id";
        List<String> filters = new ArrayList();
        filters.add("=:menu.id_cliente_sistema:" + filter.getIdClienteSistema());
        filters.add("=:menu.is_persistente:true");
        if (filter.getIsOnlyPerfilMenu()) {
            filters.add("=:base.id_perfil:" + Long.parseLong(filter.getIdPerfil()));
        }
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"menu.orden:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

    private ArrayNode menuChildrensPerfiles(FilterPerfilMenu filter) throws Exception {
        String valuesIn = valuesByComas(0, filter.getIdPerfil());
        String table = "(select * from icl.seguridad.tg_perfil_menu where id_perfil in (" + valuesIn + ") and is_persistente=true) as base";
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
        joinTables[0] = "right:icl.seguridad.tg_menu as menu:on:base.id_menu:menu.id";
        joinTables[1] = "inner:icl.seguridad.tg_menu as menuPadre:on:menu.id_menu_padre:menuPadre.id";
        List<String> filters = new ArrayList();
        filters.add("=:menu.id_cliente_sistema:" + filter.getIdClienteSistema());
        filters.add("=:menu.is_persistente:true");
        filters.add("in:base.id_perfil:" + filter.getIdPerfil());
        String[] mapFilterField = filters.toArray(new String[0]);
        String[] mapOrder = {"menu.orden:asc"};
        return this.allFieldsJoinPostgres(joinTables, table, fields, mapFilterField, mapOrder);
    }

}
