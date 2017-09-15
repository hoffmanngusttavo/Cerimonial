/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.exceptions;

/**
 *
 * @author hoffmann
 */
public class GenericException extends RuntimeException{
    
     private int code;

    public GenericException( String string, int code) {
        super(string);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
}
