/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity;

import jakarta.persistence.ParameterMode;

public class ParameterProcedure {

    private String name;
    private Class type;
    private ParameterMode pm;
    private Object value;

    public ParameterProcedure() {
    }

    public ParameterProcedure(String name, Class type, ParameterMode pm) {
        this.name = name;
        this.type = type;
        this.pm = pm;
    }

    public ParameterProcedure(String name, Class type, ParameterMode pm, Object value) {
        this.name = name;
        this.type = type;
        this.pm = pm;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public ParameterMode getPm() {
        return pm;
    }

    public void setPm(ParameterMode pm) {
        this.pm = pm;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}