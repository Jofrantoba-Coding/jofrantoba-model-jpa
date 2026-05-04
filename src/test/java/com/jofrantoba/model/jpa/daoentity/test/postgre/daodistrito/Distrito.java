/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daodistrito;

import com.jofrantoba.model.jpa.daoentity.test.GlobalEntityPkNumeric;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia.Provincia;
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
@Table(catalog = "catastro", schema = "catastro", name = "tm_distrito")
public class Distrito extends GlobalEntityPkNumeric {

    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "orden")
    private Long orden;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provincia")
    private Provincia provincia;
    @Column(name = "codigo_distrito", length = 6)
    private String codigoDistrito;
    @Column(name = "codigo_provincia", length = 4)
    private String codigoProvincia;
    @Column(name = "codigo_departamento", length = 2)
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
                case "codigoDistrito":
                    this.setCodigoDistrito(os[i] != null ? os[i].toString() : null);
                    break;
                case "orden":
                    this.setOrden(os[i] != null ? (Long) os[i] : null);
                    break;
                case "idProvincia":
                    if (provincia == null) {
                        this.setProvincia(new Provincia());
                    }
                    this.getProvincia().setId((Long) os[i]);
                    break;
                case "descripcionProvincia":
                    if (provincia == null) {
                        this.setProvincia(new Provincia());
                    }
                    this.getProvincia().setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "codigoProvincia":
                    if (provincia == null) {
                        this.setProvincia(new Provincia());
                    }
                    String codeProv = os[i] != null ? os[i].toString() : null;
                    this.setCodigoProvincia(codeProv);
                    this.getProvincia().setCodigoProvincia(codeProv);
                    break;
                case "codigoDepartamento":
                    if (provincia == null) {
                        this.setProvincia(new Provincia());
                    }
                    String codeDpto = os[i] != null ? os[i].toString() : null;
                    this.setCodigoDepartamento(codeDpto);
                    this.getProvincia().setCodigoDepartamento(codeDpto);
                    break;
            }
        }
    }
}
