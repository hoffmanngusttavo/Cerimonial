/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.web.converters;

import br.com.cerimonial.utils.DateUtils;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author hoffmann
 */
@FacesConverter(value = "DateConverter")
public class DateConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        
        try {
            
            if (StringUtils.isNotEmpty(string)) {
                return DateUtils.parseDate(string, DateUtils.format_dd_MM_yyyy_ptBR);
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(DateConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object obj) {
        if (obj != null) {
            if (obj instanceof Timestamp) {
                long millis = ((Timestamp) obj).getTime();
                Date date = new Date(millis);
                return DateUtils.formatDate(date, "dd/MM/yyyy");
            }
            if (obj instanceof Date) {
                long millis = ((Date) obj).getTime();
                Date date = new Date(millis);
                return DateUtils.formatDate(date, "dd/MM/yyyy");
            }
        }
        return null;
    }

}
