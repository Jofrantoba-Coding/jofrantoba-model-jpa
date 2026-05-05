/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daovia;

import com.jofrantoba.model.jpa.daoentity.test.GlobalEntityPkNumeric;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito.Distrito;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daoparametrias.Parametrias;
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
 * @author jona
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(catalog = "catastro", schema = "catastro", name = "tm_via")
public class Via extends GlobalEntityPkNumeric {

    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "orden")
    private Long orden;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_distrito")
    private Distrito distrito;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_via")
    private Parametrias tipoVia;
    @Column(name = "codigo_via")
    private String codigoVia;

    public void setTransformer(Object[] os, String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]) {
                case "id":
                    this.setId((Long) os[i]);
                    break;
                case "descripcion":
                    this.setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "codigoVia":
                    this.setCodigoVia(os[i] != null ? os[i].toString() : null);
                    break;
                case "orden":
                    this.setOrden(os[i] != null ? (Long) os[i] : null);
                    break;
                case "idDistrito":
                    if (distrito == null) {
                        this.setDistrito(new Distrito());
                    }
                    this.getDistrito().setId((Long) os[i]);
                    break;
                case "descripcionDistrito":
                    if (distrito == null) {
                        this.setDistrito(new Distrito());
                    }
                    this.getDistrito().setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "idTipoVia":
                    if (tipoVia == null) {
                        this.setTipoVia(new Parametrias());
                    }
                    this.getTipoVia().setId((Long) os[i]);
                    break;
                case "descripcionTipoVia":
                    if (tipoVia == null) {
                        this.setTipoVia(new Parametrias());
                    }
                    this.getTipoVia().setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "isPersistente":
                    this.setIsPersistente(os[i] != null ? Boolean.valueOf(os[i].toString()) : null);
                    break;
            }
        }
    }
}
