/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daosector;

import com.jofrantoba.model.jpa.daoentity.test.GlobalEntityPkNumeric;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito.Distrito;
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
@Table(catalog = "catastro", schema = "catastro", name = "tg_sector")
public class Sector extends GlobalEntityPkNumeric {

    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "orden")
    private Long orden;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_distrito")
    private Distrito distrito;
    @Column(name = "codigo_sector")
    private String codigoSector;
    @Column(name = "codigo_distrito")
    private String codigoDistrito;
    @Column(name = "codigo_provincia")
    private String codigoProvincia;
    @Column(name = "codigo_departamento")
    private String codigoDepartamento;

    public void setTransformer(Object[] os, String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]) {
                case "id":
                    this.setId((Long) os[i]);
                    break;
                case "isPersistente":
                    this.setIsPersistente(os[i] != null ? Boolean.valueOf(os[i].toString()) : null);
                    break;
                case "descripcion":
                    this.setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "codigoSector":
                    this.setCodigoSector(os[i] != null ? os[i].toString() : null);
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
                case "codigoDistrito":
                    if (distrito == null) {
                        this.setDistrito(new Distrito());
                    }
                    String codeDist = os[i] != null ? os[i].toString() : null;
                    this.getDistrito().setCodigoDistrito(codeDist);
                    this.setCodigoDistrito(codeDist);
                    break;
                case "codigoProvincia":
                    if (distrito == null) {
                        this.setDistrito(new Distrito());
                    }
                    String codeProv = os[i] != null ? os[i].toString() : null;
                    this.setCodigoProvincia(codeProv);
                    this.getDistrito().setCodigoProvincia(codeProv);
                    break;
                case "codigoDepartamento":
                    if (distrito == null) {
                        this.setDistrito(new Distrito());
                    }
                    String codeDpto = os[i] != null ? os[i].toString() : null;
                    this.setCodigoDepartamento(codeDpto);
                    this.getDistrito().setCodigoDepartamento(codeDpto);
                    break;
            }
        }
    }
}
