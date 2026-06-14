package com.jofrantoba.model.jpa.shared;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * Excepción comprobada (<em>checked</em>) propia de la librería, usada por toda
 * la capa DAO para señalar errores de acceso a datos.
 * <p>
 * Además del mensaje y la causa habituales, conserva la {@link Class} de origen
 * ({@link #getClase()}) para facilitar el diagnóstico de en qué entidad o
 * componente se produjo el fallo, y ofrece {@link #traceLog(boolean)} para
 * volcar la traza completa al log de Log4j2.
 *
 * @author jona
 */
@Log4j2
@Data
public class UnknownException extends Exception implements Serializable {
    /** Clase de origen donde se originó el error, para fines de diagnóstico. */
    private Class<?> clase;

    /** Crea una excepción vacía, sin mensaje ni clase de origen. */
    public UnknownException() {
        super();
    }

    /**
     * Crea la excepción con la clase de origen y un mensaje descriptivo.
     *
     * @param clase   clase donde se originó el error
     * @param message descripción del error
     */
    public UnknownException(Class<?> clase,String message) {
        super(message);
        this.clase=clase;
    }

    /**
     * Crea la excepción encadenando la causa original.
     *
     * @param clase   clase donde se originó el error
     * @param message descripción del error
     * @param cause   excepción original que provocó este error
     */
    public UnknownException(Class<?> clase,String message, Throwable cause) {
        super(message, cause);
        this.clase=clase;
    }

    /**
     * Constructor completo que permite controlar la supresión de excepciones y
     * si la traza de pila es escribible.
     *
     * @param clase              clase donde se originó el error
     * @param message            descripción del error
     * @param cause              excepción original que provocó este error
     * @param enableSuppression  si se habilita la supresión de excepciones
     * @param writableStackTrace si la traza de pila debe ser escribible
     */
    public UnknownException(Class<?> clase,String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
        this.clase=clase;
    }

    /**
     * Registra la traza completa de la excepción en el log de error de Log4j2.
     *
     * @param allTrace si es {@code true} se escribe la traza de pila completa
     *                 en el log; si es {@code false} no se registra nada
     */
    public void traceLog(boolean allTrace){
        StringWriter stack = new StringWriter();
        super.printStackTrace(new PrintWriter(stack));
        if (allTrace) {
            log.error(stack.toString());
        }
    }

}
