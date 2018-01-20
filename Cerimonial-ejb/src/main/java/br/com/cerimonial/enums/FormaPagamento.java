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
public enum FormaPagamento {

    BOLETO("Boleto"),
    CARTAO_CREDITO("Cartão de Crédito"),
    CARTAO_DEBITO("Cartão de Débito"),
    CHEQUE("Cheque"),
    DEPOSITO("Depósito"),
    DINHEIRO("Dinheiro"),
    TRANSFERENCIA("Transferência"),
    OUTROS("Outros");

    private String label;


private FormaPagamento(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static List<FormaPagamento> getList() {
        return Arrays.asList(values());
    }
}
