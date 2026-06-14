/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jofrantoba.model.jpa.daoentity;

import jakarta.persistence.ParameterMode;

/**
 * Descriptor de un parámetro de un procedimiento almacenado.
 * <p>
 * Agrupa los datos necesarios para registrar un parámetro frente a una
 * {@link jakarta.persistence.StoredProcedureQuery}: su nombre, el tipo Java, el
 * modo ({@link ParameterMode#IN}, {@link ParameterMode#OUT},
 * {@link ParameterMode#INOUT}, {@link ParameterMode#REF_CURSOR}) y,
 * opcionalmente, el valor a enlazar para los parámetros de entrada.
 *
 * @author jona
 */
public class ParameterProcedure {

    /** Nombre del parámetro tal como lo declara el procedimiento. */
    private String name;
    /** Tipo Java del parámetro (p.&nbsp;ej. {@code String.class}, {@code Long.class}). */
    private Class type;
    /** Modo del parámetro: entrada, salida, entrada/salida o cursor. */
    private ParameterMode pm;
    /** Valor a enlazar para parámetros de entrada (puede ser {@code null}). */
    private Object value;

    /** Crea un descriptor vacío para poblar mediante los <em>setters</em>. */
    public ParameterProcedure() {
    }

    /**
     * Crea un descriptor de parámetro sin valor (útil para parámetros de
     * salida o para fijar el valor más tarde).
     *
     * @param name nombre del parámetro
     * @param type tipo Java del parámetro
     * @param pm   modo del parámetro
     */
    public ParameterProcedure(String name, Class type, ParameterMode pm) {
        this.name = name;
        this.type = type;
        this.pm = pm;
    }

    /**
     * Crea un descriptor de parámetro de entrada con su valor enlazado.
     *
     * @param name  nombre del parámetro
     * @param type  tipo Java del parámetro
     * @param pm    modo del parámetro
     * @param value valor a enlazar
     */
    public ParameterProcedure(String name, Class type, ParameterMode pm, Object value) {
        this.name = name;
        this.type = type;
        this.pm = pm;
        this.value = value;
    }

    /**
     * @return el nombre del parámetro
     */
    public String getName() {
        return name;
    }

    /**
     * @param name el nombre del parámetro
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return el tipo Java del parámetro
     */
    public Class getType() {
        return type;
    }

    /**
     * @param type el tipo Java del parámetro
     */
    public void setType(Class type) {
        this.type = type;
    }

    /**
     * @return el modo ({@link ParameterMode}) del parámetro
     */
    public ParameterMode getPm() {
        return pm;
    }

    /**
     * @param pm el modo ({@link ParameterMode}) del parámetro
     */
    public void setPm(ParameterMode pm) {
        this.pm = pm;
    }

    /**
     * @return el valor enlazado al parámetro, o {@code null} si no tiene
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value el valor a enlazar al parámetro
     */
    public void setValue(Object value) {
        this.value = value;
    }

}