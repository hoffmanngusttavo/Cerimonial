/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * ex: arquivo.jrxml
     * @param parameters
     * @param inputStream
     * @return 
     */
    public static byte[] compileReport(Map<String, Object> parameters, InputStream inputStream)  {
        byte[] exportReportToPdf  = null;
        Connection connectionDB = getConnectionDB();
        try {
            JasperReport jr = JasperCompileManager.compileReport(inputStream);
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, getConnectionDB());
            exportReportToPdf = JasperExportManager.exportReportToPdf(jp);
        } catch (JRException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connectionDB.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
            try {
                connectionDB.close();
            } catch (SQLException ex) {
                Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return exportReportToPdf;
    }
    
    private static Connection getConnectionDB() {
        String driver = "org.postgresql.Driver";
        String user = "postgres";
        String senha = "postgres";
        String database = "cerimonial";
        String url = "jdbc:postgresql://localhost:5432/"+database;
        Connection con = null;
        try {
            Class.forName(driver);
            con = (Connection) DriverManager.getConnection(url, user, senha);
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.print(ex.getMessage());
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
    
    /**
     * ex: arquivo.jasper, pois o arquivo já está compilado
     * @param parameters
     * @param inputStream
     * @return 
     * @throws net.sf.jasperreports.engine.JRException
     */
    public static byte[] loadReport(Map<String, Object> parameters, InputStream inputStream) throws JRException {
        JasperReport jr = (JasperReport) JRLoader.loadObject(inputStream);
        JasperPrint jp = JasperFillManager.fillReport(jr, parameters, getConnectionDB());
        return JasperExportManager.exportReportToPdf(jp);
    }

}
