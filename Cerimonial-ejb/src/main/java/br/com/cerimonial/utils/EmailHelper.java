/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.utils;

import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ConfiguracaoEmail;
import br.com.cerimonial.service.ConfiguracaoEmailService;
import br.com.cerimonial.service.utils.ServiceLookupUtil;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Gustavo Hoffmann
 */
public class EmailHelper {

    private final ConfiguracaoEmail configuracaoEmail;

    private final MimeMultipart mpRoot = new MimeMultipart("mixed");
    private final MimeMultipart mpContent = new MimeMultipart("alternative");
    private final MimeBodyPart contentPartRoot = new MimeBodyPart();

    public EmailHelper() throws Exception {
        ServiceLookupUtil lookupUtil = new ServiceLookupUtil();
        ConfiguracaoEmailService service = lookupUtil.lookupService(ConfiguracaoEmailService.class);
        configuracaoEmail = service.getConfiguracaoEmail();

    }

    public static void validarConfiguracaoEmail() throws Exception {
        ServiceLookupUtil lookupUtil = new ServiceLookupUtil();
        ConfiguracaoEmailService service = lookupUtil.lookupService(ConfiguracaoEmailService.class);
        service.getConfiguracaoEmail();
    }

    /**
     *
     * @param destinatario
     * @param assunto
     * @param mensagem
     * @throws java.lang.Exception
     */
    public void enviarEmail(String destinatario, String assunto, String mensagem) throws Exception {
        this.enviarEmail(destinatario, assunto, mensagem, null);
    }

    public void send(String destinatario, String assunto, String mensagem, HashMap<String, Object> anexos) throws Exception{

        String remetente = configuracaoEmail.getLogin();
        
        //1) get the session object   
        Properties properties = getProperties();

        SimpleAuth auth = new SimpleAuth(remetente, configuracaoEmail.getSenha());

        Address[] toUser = InternetAddress.parse(destinatario.trim().toLowerCase());
        
        Session session = Session.getDefaultInstance(properties, auth);
        session.setDebug(true);
        
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(remetente));
        message.setRecipients(Message.RecipientType.TO, toUser);
        message.setSubject(assunto);
        
        //3) create MimeBodyPart object and set your message content    
        BodyPart messageBodyPart1 = new MimeBodyPart();
        messageBodyPart1.setText(mensagem);
        
         //5) create Multipart object and add MimeBodyPart objects to this object    
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart1);
       
        //4) create new MimeBodyPart object and set DataHandler object to this object    
        if (anexos != null && anexos.size() > 0) {
            for (Map.Entry e : anexos.entrySet()) {
                if (e.getValue() != null) {
                    MimeBodyPart mbp = new MimeBodyPart();

                    String arquivo = System.getProperty("java.io.tmpdir") + "/arquivo_" + e.getKey();
                    try {
                        
                        Arquivo arq = (Arquivo) e.getValue();

                        FileUtils.writeByteArrayToFile(new File(arquivo), arq.getConteudo());

                        DataSource fds = new FileDataSource(arquivo);
                        mbp.setDisposition(Part.ATTACHMENT);
                        mbp.setDataHandler(new DataHandler(fds));
                        mbp.setFileName(arq.getNome());

                        multipart.addBodyPart(mbp);
                        
                    } catch (Exception ex) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }
        
        //6) set the multiplart object to the message object
        message.setContent(multipart);

        //7) send message
        Transport.send(message);

        System.out.println("message sent....");

    }

    /**
     * @param destinatario
     * @param assunto
     * @param mensagem
     * @param anexos
     * @throws java.lang.Exception
     */
    public void enviarEmail(String destinatario, String assunto, String mensagem, HashMap<String, Object> anexos) throws Exception {
        String remetente = configuracaoEmail.getLogin();

        Properties properties = getProperties();

        SimpleAuth auth = new SimpleAuth(remetente, configuracaoEmail.getSenha());

        Address[] toUser = InternetAddress.parse(destinatario.trim().toLowerCase());
        Transport tr = null;
        try {
            Session session = Session.getDefaultInstance(properties, auth);
            session.setDebug(true);
            tr = session.getTransport("smtps");

            Message msg = new MimeMessage(session);
            msg.setRecipients(Message.RecipientType.TO, toUser);
            msg.setFrom(new InternetAddress(remetente));
            msg.setSubject(assunto);
            msg.setContent(mensagem, "text/html; charset=UTF-8"); //charset=ISO-8859-1

            mpRoot.addBodyPart(contentPartRoot);

            MimeBodyPart mbpHtml = new MimeBodyPart();
            mbpHtml.setContent(mensagem, "text/html");

            mpContent.addBodyPart(mbpHtml);

            contentPartRoot.setContent(mpContent);

            tr.connect(configuracaoEmail.getSmtp(), configuracaoEmail.getLogin(), configuracaoEmail.getSenha());

//            if (anexos != null && anexos.size() > 0) {
//                for (Map.Entry e : anexos.entrySet()) {
//                    if (e.getValue() != null) {
//                        MimeBodyPart mbp = new MimeBodyPart();
//
//                        String arquivo = System.getProperty("java.io.tmpdir") + "/arquivo_" + e.getValue();
//                        try {
//                            FileUtils.writeByteArrayToFile(new File(arquivo), (byte[]) e.getValue());
//                        } catch (Exception ex) {
//                            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
//                        }
//
//                        DataSource fds = new FileDataSource(arquivo);
//                        mbp.setDisposition(Part.ATTACHMENT);
//                        mbp.setDataHandler(new DataHandler(fds));
//                        mbp.setFileName(e.getKey().toString());
//
//                        mpRoot.addBodyPart(mbp);
//                    }
//                }
//            }
            msg.setContent(mpRoot);

            msg.saveChanges();
            tr.sendMessage(msg, msg.getAllRecipients());
            tr.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (tr != null && tr.isConnected()) {
                tr.close();
            }
        }
    }

    /**
     * @param destinatario
     * @param assunto
     * @param htmlMessage
     * @param anexos
     * @throws java.lang.Exception
     */
    public void enviarEmailOld(String destinatario, String assunto, String htmlMessage, HashMap<String, Object> anexos) throws Exception {
        //objeto para definicao das propriedades de configuracao do provider
        Properties props = getProperties();

        SimpleAuth auth = new SimpleAuth(configuracaoEmail.getLogin(), configuracaoEmail.getSenha());

        //obtendo um Session com a configuração 
        //do servidor SMTP definida em props
        Session session = Session.getDefaultInstance(props, auth);

        //criando a mensagem
        MimeMessage message = new MimeMessage(session);

        //substituir pelos e-mails desejados
        Address from = new InternetAddress(configuracaoEmail.getLogin());
        Address[] toUser = InternetAddress.parse(destinatario.trim().toLowerCase());

        //configurando o remetente e o destinatario
        message.setFrom(from);
        message.setRecipients(Message.RecipientType.TO, toUser);

        //configurando a data de envio,  o assunto e o texto da mensagem
        message.setSentDate(new Date());
        message.setSubject(assunto);

        //criando a Multipart
        Multipart multipart = new MimeMultipart();

        //criando a primeira parte da mensagem
        MimeBodyPart attachment0 = new MimeBodyPart();
        //configurando o htmlMessage com o mime type
        attachment0.setContent(htmlMessage, "text/html; charset=UTF-8");
        //adicionando na multipart
        multipart.addBodyPart(attachment0);

        //adicionando anexos
//        if (anexos != null && anexos.size() > 0) {
//            for (Map.Entry e : anexos.entrySet()) {
//                if (e.getValue() != null) {
//                    String arquivo = System.getProperty("java.io.tmpdir") + "/arquivo_" + e.getValue();
//                    try {
//                        FileUtils.writeByteArrayToFile(new File(arquivo), (byte[]) e.getValue());
//                    } catch (Exception ex) {
//                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                    DataSource fds = new FileDataSource(arquivo);
//                    MimeBodyPart mbp = new MimeBodyPart();
//                    mbp.setDisposition(Part.ATTACHMENT);
//                    mbp.setDataHandler(new DataHandler(fds));
//                    mbp.setFileName(e.getKey().toString());
//
//                    multipart.addBodyPart(mbp);
//                }
//            }
//        }
        //adicionando a multipart como conteudo da mensagem 
        message.setContent(multipart);

        //enviando
        Transport.send(message);

        System.out.println("E-mail enviado com sucesso!");
    }

    private Properties getProperties() {
        Properties props = new Properties();

        props.put("mail.smtp.user", configuracaoEmail.getLogin());
        props.put("mail.smtp.host", configuracaoEmail.getSmtp());
        props.put("mail.smtp.port", configuracaoEmail.getPortaSmtp());//465, 587
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", configuracaoEmail.getPortaSmtp());//465, 587
        props.put("mail.smtp.socketFactory.fallback", "false");

        if (!"587".equals(configuracaoEmail.getPortaSmtp())) {
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

//        props.put("mail.transport.protocol", "smtp");
        return props;
    }

}

class SimpleAuth extends Authenticator {

    public String username = null;
    public String password = null;

    public SimpleAuth(String user, String pwd) {
        username = user;
        password = pwd;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}
