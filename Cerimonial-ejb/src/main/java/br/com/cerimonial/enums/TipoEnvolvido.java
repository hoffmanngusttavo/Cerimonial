/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gustavo Hoffmann
 */
public enum TipoEnvolvido {

    CLIENTE("Cliente"),
    FORNECEDOR("Fornecedor"),
    COLABORADOR("Colaborador");

    private String label;

    private TipoEnvolvido(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static List<TipoEnvolvido> getList() {
        List<TipoEnvolvido> l = new ArrayList<>();
        for (TipoEnvolvido e : values()) {
            l.add(e);
        }
        return l;
    }

}
