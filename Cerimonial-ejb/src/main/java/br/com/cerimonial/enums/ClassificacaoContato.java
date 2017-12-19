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
public enum ClassificacaoContato {

    NEGOCIANDO("Negociando"),
    REUNIAO_REALIZADA("Reunião Realizada"),
    AGUARDANDO_RETORNO("Aguardando Retorno"),
    CONTRATO_FECHADO("Contrato Fechado"),
    DATA_CONTRATADA("Data Contratada"),
    OUTROS("Outros");

    private String label;

    private ClassificacaoContato(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

     public static List<ClassificacaoContato> getList() {
        return Arrays.asList(values());
    }

}
