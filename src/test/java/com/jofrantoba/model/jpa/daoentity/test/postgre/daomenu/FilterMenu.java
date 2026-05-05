/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity.test.postgre.daomenu;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author jofrantoba
 */
@Data
public class FilterMenu implements Serializable {

    private Long idClienteSistema;
    private Long idSistema;
}
