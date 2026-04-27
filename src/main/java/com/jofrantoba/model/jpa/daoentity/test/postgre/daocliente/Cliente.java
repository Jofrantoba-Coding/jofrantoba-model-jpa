/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daocliente;

import com.jofrantoba.model.jpa.daoentity.test.GlobalEntityPkNumeric;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author jona
 */
@Log4j2
@EqualsAndHashCode(callSuper=false)
@Data
@Entity
@Table(catalog="catastro",schema="catastro",name = "cliente")
public class Cliente extends GlobalEntityPkNumeric implements Serializable{
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "tipo_documento")
    private String tipoDocumento;
    @Column(name = "numero_documento")
    private String numeroDocumento;
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;
    @Column(name = "fecha_vinculacion")
    private Date fechaVinculacion;
    @Column(name = "cargo")
    private String cargo;
    @Column(name = "salario")
    private BigDecimal salario;
}
