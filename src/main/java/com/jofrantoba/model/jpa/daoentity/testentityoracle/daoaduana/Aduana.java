/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.testentityoracle.daoaduana;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.extern.log4j.Log4j2;


/**
 *
 * @author jona
 */
@Log4j2
@Data
@Entity
@Table(catalog="sysdecorprueba",schema="sysdecorprueba",name = "ADUANA")
//@Table(name = "ADUANA")
public class Aduana implements Serializable{
    @Id    
    @Column(name = "ID_ADUANA")  
    private String id;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "USER_CREA")
    private String userCrea;
    @Column(name = "USER_UPDATE")
    private String userUpdate;
    @Column(name = "DATE_CREA")
    @Temporal(TemporalType.DATE)
    private Date dateCrea;
    @Column(name = "DATE_UPDATE")
    @Temporal(TemporalType.DATE)
    private Date dateUpdate;
    @Column(name = "ESTADO")
    private String estado;
}
