/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.service.report.Relatorio;
import br.com.cerimonial.repository.OrcamentoEventoRepository;
import br.com.cerimonial.service.invoice.InvoiceUtils;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrcamentoEventoService extends BasicService<OrcamentoEvento>{
    
     private OrcamentoEventoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new OrcamentoEventoRepository(em);
    }
    
      @Override
    public OrcamentoEvento getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public OrcamentoEvento save(OrcamentoEvento entity) throws Exception {
        if (entity != null) {
            
            if(entity.getContatoEvento() == null){
                throw new Exception("Você precisa associar um contato inicial primeiro");
            }
            
            if (entity.getId() == null) {
                return repository.create(entity);
            } else {
                return repository.edit(entity);
            }
        }
        return null;
    }

    public List<OrcamentoEvento> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(OrcamentoEventoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(OrcamentoEvento categoria) throws Exception {
        repository.delete(categoria);
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<OrcamentoEvento> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem( max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

  
    public OrcamentoEvento carregarPropostaModelo(OrcamentoEvento orcamento, ModeloProposta modelo) {
        
        if(orcamento != null && modelo != null){
             orcamento.setModeloProposta(modelo);
             orcamento.setProposta(modelo.getConteudo());
             orcamento.setValorProposta(modelo.getValorProposta() != null ? modelo.getValorProposta().doubleValue() : 0 );
        }
        
        return orcamento;
    }

    public List<OrcamentoEvento> findAllByContatoId(Long id) {
        if(id != null){
            try {
                return repository.findAllByContatoId(id);
            } catch (Exception ex) {
                Logger.getLogger(OrcamentoEventoService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new ArrayList<>();
    }

    public void enviarOrcamentoEmail(OrcamentoEvento proposta) throws Exception{
        
        if(proposta == null){
            throw new Exception("Objeto nulo");
        }
        
        byte[] pdfProposta = getPDF(proposta);
        
        String body = InvoiceUtils.readFileToString("invoice.html");
        body = body.replaceAll("@@@NOME_CLIENTE@@@", proposta.getContatoEvento().getNomeContato());
        body = body.replaceAll("@@@PARCELA_DATA@@@", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        body = body.replaceAll("@@@PARCELA_VALOR@@@", new DecimalFormat("#0.00").format(proposta.getValorProposta()));
        
        callGlassfishJavaMail(body, pdfProposta, proposta.getContatoEvento().getEmailContato());
        
    }

    public byte[] getPDF(OrcamentoEvento proposta) throws JRException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("parcela", proposta);
        parameters.put("codigobarras", "51515");
        InputStream inputStream = OrcamentoEventoService.class.getResourceAsStream("boletoParcela.jrxml");
        return Relatorio.compileReport(parameters, inputStream);
    }

    private void callGlassfishJavaMail(String body, byte[] pdfProposta, String emailContato) {
        try {
//            Multipart multipart = new MimeMultipart();;
//            Message msg = new MimeMessage(mailSTMP);
//            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailContato));
//            msg.setFrom(new InternetAddress("consultorioEEDevmedia@gmail.com"));
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//            msg.setSubject("[ConsultorioEE] Invoice para pagamento referente a consulta Odontologica enviado em " + sdf.format(new Date()) + " [/ConsultorioEE]");
//            // The Message
//            BodyPart messageBodyPart = new MimeBodyPart();
//            messageBodyPart.setContent(body, "text/html; charset=ISO-8859-1");
//            multipart.addBodyPart(messageBodyPart);
//            // The PDF File
//            BodyPart boletoBodyPart = new MimeBodyPart();
//            boletoBodyPart.setFileName("boleto.pdf");
//            boletoBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(pdfProposta, "application/pdf")));
//            multipart.addBodyPart(boletoBodyPart);
//            // Attach the Multipart Data
//            msg.setContent(multipart);
//            Transport.send(msg);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
