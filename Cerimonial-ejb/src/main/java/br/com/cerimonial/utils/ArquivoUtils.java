/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.utils;

import br.com.cerimonial.entity.Arquivo;
import java.io.IOException;
import java.io.OutputStream;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gustavo Hoffmann
 */
public class ArquivoUtils {
    
    
    
    public static void carregarArquivo(Arquivo arquivo) throws Exception {

        if(arquivo == null){
            throw new Exception("Arquivo nulo");
        }
        
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();
//        ec.setResponseContentType("application/pdf");
//        ec.setResponseContentType("application/octet-stream");
//        ec.setResponseContentType("image/png");
//        ec.setResponseContentType("text/html;charset=ISO-8859-1");
//        ec.setResponseContentType("application/xml");
//        ec.setResponseContentType("application/zip");
        ec.setResponseContentType(arquivo.getExtensao());
        ec.setResponseContentLength(arquivo.getConteudo().length);
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + arquivo.getNome() + "\"");
        OutputStream output = ec.getResponseOutputStream();
        output.write(arquivo.getConteudo());
        output.flush();
        output.close();
        fc.responseComplete();

    }
    
}
