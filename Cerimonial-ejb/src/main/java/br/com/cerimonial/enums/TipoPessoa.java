/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.enums;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Gustavo Hoffmann
 */
public enum TipoPessoa {
    
    FISICA("Física"),
    JURIDICA("Jurídica");
    
    private String label;

    private TipoPessoa(String label) {
        this.label = label;
    }
    

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    public static List<TipoPessoa> getList() {
        return Arrays.asList(values());
    }
    
}
