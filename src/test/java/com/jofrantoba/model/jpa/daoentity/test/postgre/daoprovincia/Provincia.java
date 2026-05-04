/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoprovincia;

import com.jofrantoba.model.jpa.daoentity.test.GlobalEntityPkNumeric;
import com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento.Departamento;
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
@Table(catalog = "catastro", schema = "catastro", name = "tm_provincia")
public class Provincia extends GlobalEntityPkNumeric {

    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "orden")
    private Long orden;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_departamento")
    private Departamento departamento;
    @Column(name = "codigo_departamento", length = 2)
    private String codigoDepartamento;
    @Column(name = "codigo_provincia", length = 4)
    private String codigoProvincia;

    public void setTransformer(Object[] os, String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]) {
                case "id":
                    this.setId((Long) os[i]);
                    break;
                case "descripcion":
                    this.setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "codigoProvincia":
                    this.setCodigoProvincia(os[i] != null ? os[i].toString() : null);
                    break;
                case "orden":
                    this.setOrden(os[i] != null ? (Long) os[i] : null);
                    break;
                case "idDepartamento":
                    if (departamento == null) {
                        this.setDepartamento(new Departamento());
                    }
                    this.getDepartamento().setId((Long) os[i]);
                    break;
                case "descripcionDepartamento":
                    if (departamento == null) {
                        this.setDepartamento(new Departamento());
                    }
                    this.getDepartamento().setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "codigoDepartamento":
                    if (departamento == null) {
                        this.setDepartamento(new Departamento());
                    }
                    String codeDpto = os[i] != null ? os[i].toString() : null;
                    this.setCodigoDepartamento(codeDpto);
                    this.getDepartamento().setCodigoDepartamento(codeDpto);
                    break;
            }
        }
    }
}
