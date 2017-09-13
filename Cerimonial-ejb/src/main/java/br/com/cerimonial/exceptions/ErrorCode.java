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
public enum ErrorCode {
    
    BAD_REQUEST(400),
    NOT_FOUND(404),
    SERVER_ERROR(500);
    
    
    private int code;

    private ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
    
}
