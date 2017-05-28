/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Gustavo Hoffmann
 */
public abstract class BasicControl implements Serializable {

    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    protected void createFacesErrorMessage(String message) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    protected void createFacesInfoMessage(String message) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, message, message);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    protected void createFacesWarnMessage(String message) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, message, message);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    protected Set<ConstraintViolation<Serializable>> getViolantions(Serializable entity) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Serializable>> toReturn = validator.validate(entity);
        return toReturn;
    }

    protected boolean existsViolationForJSF(Serializable entity) {
        Set<ConstraintViolation<Serializable>> toReturn = getViolantions(entity);
        if (toReturn.isEmpty()) {
            return false;
        }
        for (ConstraintViolation<Serializable> violation : toReturn) {
            createFacesErrorMessage(violation.getMessage());
        }
        return true;
    }
    
     public String getRequestParam(String param) {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return req.getParameter(param);
    }
     
    public void scrollTopMessage(){
        scrollTopMessage(null);
    }
    
    public void scrollTopMessage(String id){
        if(id == null || id.isEmpty()){
            id = "form-menu";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("$('html, body').animate({ scrollTop: $('#"+id+"').offset().top }, 800);");
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    
    
}
