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
@Table(catalog = "seguridad", schema = "seguridad", name = "tg_cliente_sistema")
public class ClienteSistema extends GlobalEntityPkNumeric {

    @Column(name = "descripcion")
    private String descripcion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sistema")
    private Sistema sistema;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "realm_id")
    private String realmId;
    @Column(name = "is_app_icl")
    private Boolean isAppIcl;

    public void setTransformer(Object[] os, String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]) {
                case "id":
                    this.setId((Long) os[i]);
                    break;
                case "descripcion":
                    this.setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "idSistema":
                    if (sistema == null) {
                        this.setSistema(new Sistema());
                    }
                    this.getSistema().setId((Long) os[i]);
                    break;
                case "descripcionSistema":
                    if (sistema == null) {
                        this.setSistema(new Sistema());
                    }
                    this.getSistema().setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
            }
        }
    }
}
