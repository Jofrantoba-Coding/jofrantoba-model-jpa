/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daoparametrias;

import com.jofrantoba.model.jpa.daoentity.test.GlobalEntityPkNumeric;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(catalog = "catastro", schema = "catastro", name = "tm_parametrias")
public class Parametrias extends GlobalEntityPkNumeric {

    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "orden")
    private Long orden;
    @Column(name = "marca_tiempo")
    private LocalDateTime marcaTiempo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parametria_padre", referencedColumnName = "id")
    private Parametrias parent;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Parametrias> children;

    public void setTransformer(Object[] os, String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]) {
                case "id":
                    this.setId((Long) os[i]);
                    break;
                case "descripcion":
                    this.setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
                case "codigo":
                    this.setCodigo(os[i] != null ? os[i].toString() : null);
                    break;
                case "abreviatura":
                    this.setAbreviatura(os[i] != null ? os[i].toString() : null);
                    break;
                case "orden":
                    this.setOrden(os[i] != null ? (Long) os[i] : null);
                    break;
                case "idParent":
                    if (parent == null) {
                        this.setParent(new Parametrias());
                    }
                    this.getParent().setId((Long) os[i]);
                    break;
                case "descripcionParent":
                    if (parent == null) {
                        this.setParent(new Parametrias());
                    }
                    this.getParent().setDescripcion(os[i] != null ? os[i].toString() : null);
                    break;
            }
        }
    }
}
