/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.daoentity.test;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author jona
 */
@Log4j2
@Data
@MappedSuperclass
public abstract class GlobalEntityPkString implements Serializable{
    @Id    
    @Column(length=3,columnDefinition = "char",name = "ID_ADUANA")  
    private String id;
    /*@Column(name = "version")
    private Long version;    
    @Column(name = "is_persistente")
    private Boolean isPersistente;*/
    /*@Transient          
    private String operacion;*/
}
