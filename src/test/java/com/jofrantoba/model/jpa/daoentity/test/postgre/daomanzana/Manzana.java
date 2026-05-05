/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daomanzana;

import com.jofrantoba.model.jpa.daoentity.test.GlobalEntityPkNumeric;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daosector.Sector;
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
@Table(catalog = "catastro", schema = "catastro", name = "tg_manzana")
public class Manzana extends GlobalEntityPkNumeric {

    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "orden")
    private Long orden;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sector")
    private Sector sector;
    @Column(name = "codigo_manzana")
    private String codigoManzana;
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
                case "descripcion":
                    this.setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "codigoManzana":
                    this.setCodigoManzana(os[i] != null ? os[i].toString() : null);
                    break;
                case "codigoSector":
                    this.setCodigoSector(os[i] != null ? os[i].toString() : null);
                    break;
                case "orden":
                    this.setOrden(os[i] != null ? (Long) os[i] : null);
                    break;
                case "isPersistente":
                    this.setIsPersistente(os[i] != null ? Boolean.valueOf(os[i].toString()) : null);
                    break;
                case "idSector":
                    if (sector == null) {
                        this.setSector(new Sector());
                    }
                    this.getSector().setId((Long) os[i]);
                    break;
                case "descripcionSector":
                    if (sector == null) {
                        this.setSector(new Sector());
                    }
                    this.getSector().setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "codigoDistrito":
                    if (sector == null) {
                        this.setSector(new Sector());
                    }
                    String codeDist = os[i] != null ? os[i].toString() : null;
                    this.getSector().setCodigoDistrito(codeDist);
                    this.setCodigoDistrito(codeDist);
                    break;
                case "codigoProvincia":
                    if (sector == null) {
                        this.setSector(new Sector());
                    }
                    String codeProv = os[i] != null ? os[i].toString() : null;
                    this.setCodigoProvincia(codeProv);
                    this.getSector().setCodigoProvincia(codeProv);
                    break;
                case "codigoDepartamento":
                    if (sector == null) {
                        this.setSector(new Sector());
                    }
                    String codeDpto = os[i] != null ? os[i].toString() : null;
                    this.setCodigoDepartamento(codeDpto);
                    this.getSector().setCodigoDepartamento(codeDpto);
                    break;
            }
        }
    }
}
