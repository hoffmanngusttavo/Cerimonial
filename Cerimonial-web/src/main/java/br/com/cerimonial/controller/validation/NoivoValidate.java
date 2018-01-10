/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.validation;

import br.com.cerimonial.controller.AbstractValidatorJSF;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.ArrayList;

/**
 *
 * @author hoffmann
 */
public class NoivoValidate extends AbstractValidatorJSF {

    private Pessoa entity;

    public NoivoValidate(Pessoa entity) {
        this.entity = entity;
    }

    @Override
    public boolean isValid() {

        setMessagesError(new ArrayList<String>());
        setMessagesWarn(new ArrayList<String>());

        if (CollectionUtils.isBlank(entity.getContatosFamiliares())) {
            getMessagesWarn().add("Cadastre pelo menos um contato familiar");
        }

        return (getMessagesError().isEmpty() && getMessagesWarn().isEmpty());
    }

}
