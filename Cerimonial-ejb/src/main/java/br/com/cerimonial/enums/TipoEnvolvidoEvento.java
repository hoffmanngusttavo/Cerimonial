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
 * @author hoffmann
 */
public enum TipoEnvolvidoEvento {
    
    NOIVO("NOIVO"),
    NOIVA("NOIVA"),
    FORMANDO("FORMANDO(A)"),
    ANIVERSARIANTE("ANIVERSARIANTE"),
    FESTEIRO("FESTEIRO(A)");
    

    private String label;

    private TipoEnvolvidoEvento(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static List<TipoEnvolvidoEvento> getList() {
        return Arrays.asList(values());
    }
    
}
