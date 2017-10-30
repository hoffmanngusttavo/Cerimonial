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
public enum GrauParentesco {

    PAI_MAE("PAI/MÃE"),
    FILHA("FILHA(O)"),
    IRMAO("IRMÃ(O)");

    private String label;

    private GrauParentesco(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static List<GrauParentesco> getList() {
        return Arrays.asList(values());
    }

}
