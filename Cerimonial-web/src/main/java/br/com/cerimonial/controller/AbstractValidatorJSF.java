/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller;

import br.com.cerimonial.utils.CerimonialUtils;
import java.util.List;
import org.primefaces.context.RequestContext;

/**
 *
 * @author hoffmann
 */
public abstract class AbstractValidatorJSF extends BasicControl {

    protected List<String> messagesError;
    protected List<String> messagesWarn;
    protected RequestContext context = RequestContext.getCurrentInstance();

    public abstract boolean isValid();

    public List<String> getMessagesError() {
        return messagesError;
    }

    public void setMessagesError(List<String> messagesError) {
        this.messagesError = messagesError;
    }

    public List<String> getMessagesWarn() {
        return messagesWarn;
    }

    public void setMessagesWarn(List<String> messagesWarn) {
        this.messagesWarn = messagesWarn;
    }

    public RequestContext getContext() {
        return context;
    }

    public void setContext(RequestContext context) {
        this.context = context;
    }

    public String getMessageError() {
        if (messagesError == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (String msg : messagesError) {
            sb.append(msg).append(" , ");
        }

        return sb.toString().substring(0, sb.toString().trim().length() - 1);
    }

    public String getMessageWarn() {
        if (messagesWarn == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (String msg : messagesWarn) {
            sb.append(msg).append(" , ");
        }

        return sb.toString().substring(0, sb.toString().trim().length() - 1);
    }

    public void showMessages() {

        if (CerimonialUtils.isListNotBlank(this.getMessagesError())) {
            createFacesErrorMessage(this.getMessageError());
        }

        if (CerimonialUtils.isListNotBlank(this.getMessagesWarn())) {
            createFacesWarnMessage(this.getMessageWarn());
        }

    }

}
