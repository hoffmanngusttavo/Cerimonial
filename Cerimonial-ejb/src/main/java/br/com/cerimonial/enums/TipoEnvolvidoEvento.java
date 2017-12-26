/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.enums;

import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author hoffmann
 */
public enum TipoEnvolvidoEvento {

    NOIVO(0, "NOIVO"),
    NOIVA(1, "NOIVA"),
    FORMANDO(2, "FORMANDO(A)"),
    ANIVERSARIANTE(3, "ANIVERSARIANTE"),
    FESTEIRO(4, "FESTEIRO(A)");

    private String label;
    private int codigo;

    private TipoEnvolvidoEvento(int codigo, String label) {
        this.codigo = codigo;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public static List<TipoEnvolvidoEvento> getList() {
        return Arrays.asList(values());
    }

    public static TipoEnvolvidoEvento getTipoByCode(Integer tipoEnvolvido) {

        if (tipoEnvolvido == null) {
            throw new GenericException("Não foi possível carregar tipo de envolvido do evento, código nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        for (TipoEnvolvidoEvento tipo : getList()) {

            if (tipo.getCodigo() == tipoEnvolvido) {
                return tipo;
            }

        }

        return null;

    }

    public static List<TipoEnvolvidoEvento> getTiposEnvolvidosByTipoEvento(TipoEvento tipoEvento) {

        if (tipoEvento == null) {
            throw new GenericException("Não foi possível carregar tipo de envolvido do evento, Tipo Evento nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        List<TipoEnvolvidoEvento> itens = new ArrayList<>();

        switch (tipoEvento) {

            case ANIVERSARIO:
                itens.add(ANIVERSARIANTE);
                break;
            case ANIVERSARIO_15_ANOS:
                break;
            case ANIVERSARIO_INFANTIL:
                break;
            case BODAS:
            case CASAMENTO:
                itens.add(NOIVA);
                itens.add(NOIVO);
                break;
            case RELIGIOSO:
                itens.add(FESTEIRO);
                break;
            case OUTROS:
                itens.add(FORMANDO);
                break;

        }

        return itens;

    }

}
