/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.testentityoracle.daoaduana;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @Column(name = "DESCRPCION")
    private String descripcion;
    /*@Column(name = "\"_USER_CREA\"")
    private String userCrea;
    @Column(name = "\"_USER_UPDATE\"")
    private String userUpdate;
    @Column(name = "\"_DATE_CREA\"")
    @Temporal(TemporalType.DATE)
    private Date dateCrea;
    @Column(name = "\"_DATE_UPDATE\"")
    @Temporal(TemporalType.DATE)
    private Date dateUpdate;
    @Column(name = "\"_ESTADO\"")
    private String estado;*/
}
