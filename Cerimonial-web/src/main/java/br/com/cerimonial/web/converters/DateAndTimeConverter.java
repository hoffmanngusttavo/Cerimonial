/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.web.converters;

import br.com.cerimonial.utils.DateUtils;
import java.sql.Timestamp;
import java.util.Date;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author hoffmann
 */
@FacesConverter(value = "DateAndTimeConverter")
public class DateAndTimeConverter implements Converter{
    
     @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        
        if (string != null) { 
            return uic.getAttributes().get(string);
        }  
        return null; 
        
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object obj) {
        if (obj != null) {
            if (obj instanceof Timestamp) {
                long millis = ((Timestamp) obj).getTime();
                Date date = new Date(millis);
                String label = DateUtils.formatDate(date, DateUtils.format_dd_MM_yyyy_HH_mm_ptBR);
                uic.getAttributes().put(label, obj);
                return label;
            }
            if (obj instanceof Date) {
                long millis = ((Date) obj).getTime();
                Date date = new Date(millis);
                String label = DateUtils.formatDate(date, DateUtils.format_dd_MM_yyyy_HH_mm_ptBR);
                uic.getAttributes().put(label, obj);
                return label;
            }
        }
        return null;
    }
    
}
