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
public enum CategoriaEvento {
    
    CASAMENTO("Casamento"),
    ANIVERSARIO("Aniversário"),
    ANIVERSARIO_15_ANOS("Aniversário 15 anos"),
    ANIVERSARIO_INFANTIL("Aniversário infantil"),
    BODAS("Bodas"),
    RELIGIOSO("Religioso");
    
     private String label;

    private CategoriaEvento(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
     public static List<CategoriaEvento> getList() {
        return Arrays.asList(values());
    }
    
}
