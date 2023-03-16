package com.jofrantoba.model.jpa.shared;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
public class UnknownException extends Exception implements Serializable {    
    private Class<?> clase;

    public UnknownException() {
        super();        
    }
    
    public UnknownException(Class<?> clase,String message) {
        super(message);
        this.clase=clase;
    } 
    
    public UnknownException(Class<?> clase,String message, Throwable cause) {
        super(message, cause);
        this.clase=clase;
    }
    
    public UnknownException(Class<?> clase,String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
        this.clase=clase;
    }    
    public void traceLog(boolean allTrace){        
        StringWriter stack = new StringWriter();
        super.printStackTrace(new PrintWriter(stack));
        if (allTrace) {
            log.error(stack.toString());
        }        
    }
    
}
