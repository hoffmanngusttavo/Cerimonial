/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service.invoice;

import java.io.IOException;
import java.io.InputStream;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gustavo Hoffmann
 */
public class InvoiceUtils {

    public static String readFileToString(String fileName) throws IOException {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/jasper/"+fileName);
        byte[] invoiceBytes = new byte[stream.available()];
        stream.read(invoiceBytes);
        stream.close();
        return new String(invoiceBytes);
    }
    
}
