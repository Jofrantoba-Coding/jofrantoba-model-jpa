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
 *
 * @author jona
 */
public class Shared {
    
    public StringBuilder append(String str) {        
        StringBuilder concatena = new StringBuilder();
        concatena.append(" ");
        concatena.append(str);
        concatena.append(" ");
        return concatena;
    }

    public String strApostrofe(String str) {
        StringBuilder builder = new StringBuilder();
        builder.append("\'");
        builder.append(str);
        builder.append("\'");
        return builder.toString();
    }

    public Long getUnixTime() {
        return System.currentTimeMillis();
    }

    public String getClean(String string) {
        return StringUtils.stripAccents(string).trim();
    }

    public Long convertValueLong(String value) {
        return (value != null && !value.isEmpty() ? Long.parseLong(value) : 0L);
    }
    
    public void closeResultSet(ResultSet rs) throws SQLException{
        if(rs!=null){
            rs.close();
        }
    }
    
    public void closePreparedStatement(PreparedStatement st) throws SQLException{
        if(st!=null){
            st.close();
        }
    }
    
    public void closeCallableStatement(CallableStatement cst) throws SQLException{
        if(cst!=null){
            cst.close();
        }
    }
    
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
    
    public boolean notIsNullVacioObjects(Object... objetos){
        for(int i=0;i<objetos.length;i++){
            if(objetos[i]==null || objetos[i].toString().trim().length()==0){
                return false;
            }
        }
        return true;
    }
}
