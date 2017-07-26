/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Gustavo Hoffmann
 */
public class Relatorio {

    
    
    public static void exportarPdf(byte[] propostaPdf, String fileName) throws IOException {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();
        ec.setResponseContentType("application/pdf");
        ec.setResponseContentLength(propostaPdf.length);
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".pdf\"");
        OutputStream output = ec.getResponseOutputStream();
        output.write(propostaPdf);
        output.flush();
        output.close();
        fc.responseComplete();

    }

     /**
     * ex: arquivo.jasper
     * @param parameters
     * @param inputStream
     * @return 
     * @throws net.sf.jasperreports.engine.JRException
     */
    public static byte[] compileReport(Map<String, Object> parameters, InputStream inputStream) throws JRException {
        JasperReport jr = JasperCompileManager.compileReport(inputStream);
        JasperPrint jp = JasperFillManager.fillReport(jr, parameters);
        return JasperExportManager.exportReportToPdf(jp);
    }
    
    /**
     * ex: arquivo.jasper
     * @param parameters
     * @param inputStream
     * @return 
     * @throws net.sf.jasperreports.engine.JRException
     */
    public static byte[] loadReport(Map<String, Object> parameters, InputStream inputStream) throws JRException {
        JasperReport jr = (JasperReport) JRLoader.loadObject(inputStream);
        JasperPrint jp = JasperFillManager.fillReport(jr, parameters);
        return JasperExportManager.exportReportToPdf(jp);
    }

}
