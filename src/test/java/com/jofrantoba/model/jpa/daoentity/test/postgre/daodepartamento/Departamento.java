/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daodepartamento;

import com.jofrantoba.model.jpa.daoentity.test.GlobalEntityPkNumeric;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(catalog = "catastro", schema = "catastro", name = "tm_departamento")
public class Departamento extends GlobalEntityPkNumeric {

    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "codigo_departamento", length = 2)
    private String codigoDepartamento;
    @Column(name = "orden")
    private Long orden;
}
