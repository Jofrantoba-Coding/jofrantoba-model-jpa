/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jofrantoba.model.jpa.shared;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;

/**
 * Conjunto de utilidades de uso general empleadas por la capa DAO.
 * <p>
 * Agrupa pequeñas funciones de apoyo para la construcción de cadenas SQL/HQL
 * (concatenación con espacios, entrecomillado), conversión de tipos desde
 * texto, limpieza de cadenas y cierre seguro de recursos JDBC
 * ({@link ResultSet}, {@link PreparedStatement}, {@link CallableStatement}).
 *
 * @author jona
 */
public class Shared {

    /**
     * Devuelve el texto dado rodeado de un espacio a cada lado.
     * <p>
     * Es la pieza básica con la que la capa DAO concatena <em>tokens</em> al
     * armar consultas, garantizando que las palabras clave no queden pegadas.
     *
     * @param str fragmento de texto a envolver
     * @return un {@link StringBuilder} con el formato {@code " str "}
     */
    public StringBuilder append(String str) {
        StringBuilder concatena = new StringBuilder();
        concatena.append(" ");
        concatena.append(str);
        concatena.append(" ");
        return concatena;
    }

    /**
     * Encierra el texto dado entre comillas simples.
     *
     * @param str texto a entrecomillar
     * @return el texto con el formato {@code 'str'}
     */
    public String strApostrofe(String str) {
        StringBuilder builder = new StringBuilder();
        builder.append("\'");
        builder.append(str);
        builder.append("\'");
        return builder.toString();
    }

    /**
     * Devuelve la marca de tiempo actual en milisegundos desde la época Unix.
     *
     * @return {@link System#currentTimeMillis()}
     */
    public Long getUnixTime() {
        return System.currentTimeMillis();
    }

    /**
     * Normaliza una cadena eliminando los acentos/diacríticos y recortando los
     * espacios de los extremos.
     *
     * @param string cadena a limpiar
     * @return la cadena sin acentos y sin espacios sobrantes
     */
    public String getClean(String string) {
        return StringUtils.stripAccents(string).trim();
    }

    /**
     * Convierte una cadena a {@link Long}, devolviendo {@code 0L} si la entrada
     * es {@code null} o vacía.
     *
     * @param value texto a convertir
     * @return el valor numérico, o {@code 0L} si no hay valor
     */
    public Long convertValueLong(String value) {
        return (value != null && !value.isEmpty() ? Long.parseLong(value) : 0L);
    }

    /**
     * Cierra un {@link ResultSet} si no es {@code null}.
     *
     * @param rs el {@code ResultSet} a cerrar (puede ser {@code null})
     * @throws SQLException si ocurre un error al cerrarlo
     */
    public void closeResultSet(ResultSet rs) throws SQLException{
        if(rs!=null){
            rs.close();
        }
    }

    /**
     * Cierra un {@link PreparedStatement} si no es {@code null}.
     *
     * @param st el {@code PreparedStatement} a cerrar (puede ser {@code null})
     * @throws SQLException si ocurre un error al cerrarlo
     */
    public void closePreparedStatement(PreparedStatement st) throws SQLException{
        if(st!=null){
            st.close();
        }
    }

    /**
     * Cierra un {@link CallableStatement} si no es {@code null}.
     *
     * @param cst el {@code CallableStatement} a cerrar (puede ser {@code null})
     * @throws SQLException si ocurre un error al cerrarlo
     */
    public void closeCallableStatement(CallableStatement cst) throws SQLException{
        if(cst!=null){
            cst.close();
        }
    }

    /**
     * Convierte una cadena al tipo Java indicado por su nombre.
     * <p>
     * Soporta {@code "String"}, {@code "Date"} (interpreta el valor como
     * milisegundos epoch), {@code "Integer"}, {@code "Double"}, {@code "Long"}
     * y {@code "BigDecimal"}. Para cualquier tipo, una cadena vacía se traduce a
     * {@code null}; un nombre de tipo desconocido también devuelve {@code null}.
     * Se usa al enlazar valores del DSL a parámetros de consultas nativas.
     *
     * @param tipo  nombre del tipo destino (p.&nbsp;ej. {@code "Long"}, {@code "Date"})
     * @param value representación textual del valor
     * @return el objeto convertido, o {@code null} si el valor está vacío o el tipo es desconocido
     */
    public Object StringToObject(String tipo,String value){
        switch(tipo){
            case "String":
                return value.isEmpty()?null:value;
            case "Date":
                return value.isEmpty()?null:new java.util.Date(Long.parseLong(value));
            case "Integer":
                return value.isEmpty()?null:Integer.parseInt(value);
            case "Double":
                return value.isEmpty()?null:Double.parseDouble(value);
            case "Long":
                return value.isEmpty()?null:Long.parseLong(value);
            case "BigDecimal":
                return value.isEmpty()?null:BigDecimal.valueOf(Double.parseDouble(value));
            default:
                return null;
        }
    }
    
    /**
     * Comprueba que <em>todos</em> los objetos recibidos tengan valor, es
     * decir, que ninguno sea {@code null} ni una cadena en blanco (su
     * {@code toString()} recortado no puede tener longitud cero).
     *
     * @param objetos objetos a validar
     * @return {@code true} si todos tienen valor; {@code false} si alguno es
     *         {@code null} o está en blanco
     */
    public boolean notIsNullVacioObjects(Object... objetos){
        for(int i=0;i<objetos.length;i++){
            if(objetos[i]==null || objetos[i].toString().trim().length()==0){
                return false;
            }
        }
        return true;
    }
}
