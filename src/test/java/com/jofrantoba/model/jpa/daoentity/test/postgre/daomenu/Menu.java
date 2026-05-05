/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daomenu;

import com.jofrantoba.model.jpa.daoentity.test.GlobalEntityPkNumeric;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author jofrantoba
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(catalog = "seguridad", schema = "seguridad", name = "tg_menu")
public class Menu extends GlobalEntityPkNumeric {

    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "ruta")
    private String ruta;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "orden")
    private Long orden;
    @Column(name = "nivel")
    private Long nivel;
    @Column(name = "numero_submenu")
    private Long numeroSubmenu;
    @Column(name = "icono")
    private String icono;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente_sistema")
    private ClienteSistema clienteSistema;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_menu_padre", referencedColumnName = "id")
    private Menu parent;

    public void setTransformer(Object[] os, String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]) {
                case "id":
                    this.setId((Long) os[i]);
                    break;
                case "descripcion":
                    this.setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "icono":
                    this.setIcono(os[i] != null ? os[i].toString() : null);
                    break;
                case "ruta":
                    this.setRuta(os[i] != null ? os[i].toString() : null);
                    break;
                case "tipo":
                    this.setTipo(os[i] != null ? os[i].toString() : null);
                    break;
                case "orden":
                    this.setOrden(os[i] != null ? (Long) os[i] : null);
                    break;
                case "nivel":
                    this.setNivel(os[i] != null ? (Long) os[i] : null);
                    break;
                case "numeroSubmenu":
                    this.setNumeroSubmenu(os[i] != null ? (Long) os[i] : null);
                    break;
                case "idParent":
                    if (parent == null) {
                        this.setParent(new Menu());
                    }
                    this.getParent().setId((Long) os[i]);
                    break;
                case "descripcionParent":
                    if (parent == null) {
                        this.setParent(new Menu());
                    }
                    this.getParent().setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "idClienteSistema":
                    if (clienteSistema == null) {
                        this.setClienteSistema(new ClienteSistema());
                    }
                    this.getClienteSistema().setId((Long) os[i]);
                    break;
                case "descripcionClienteSistema":
                    if (clienteSistema == null) {
                        this.setClienteSistema(new ClienteSistema());
                    }
                    this.getClienteSistema().setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
            }
        }
    }
}
