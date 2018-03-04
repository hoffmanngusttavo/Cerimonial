/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.web.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author hoffmann
 */
@FacesConverter(value = "ObjectConverter")
public class ObjectConverter implements Converter {
    
     @Override
    public Object getAsObject(FacesContext ctx, UIComponent component,  String value) {  
        if (value != null) { 
            return component.getAttributes().get(value);  
        }  
        return null;  
    }  

    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object obj) {
        if (obj != null) {
            String chave = String.valueOf(obj);
            component.getAttributes().put(chave, obj);                                                                              
            return chave;
        }
        return null;  
    } 
    
}
