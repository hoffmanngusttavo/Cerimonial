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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
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

        String[] destinatarios = new String[1];
        destinatarios[0] = destinatario;

        this.enviarEmail(destinatarios, assunto, mensagem, null);
    }

    /**
     *
     * @param destinatarios
     * @param assunto
     * @param mensagem
     * @throws java.lang.Exception
     */
    public void enviarEmail(String[] destinatarios, String assunto, String mensagem) throws Exception {
        this.enviarEmail(destinatarios, assunto, mensagem, null);
    }

    /**
     * @param destinatario
     * @param assunto
     * @param mensagem
     * @param anexos
     * @throws java.lang.Exception
     */
    public void enviarEmail(String destinatario, String assunto, String mensagem, HashMap<String, Object> anexos) throws Exception {
        String[] destinatarios = new String[1];
        destinatarios[0] = destinatario;
        this.enviarEmail(destinatarios, assunto, mensagem, anexos);
    }

    /**
     *
     * @param destinatarios
     * @param assunto
     * @param mensagem
     * @param anexos
     * @throws java.lang.Exception
     */
    public void enviarEmail(String[] destinatarios, String assunto, String mensagem, HashMap<String, Object> anexos) throws Exception {
        String remetente = configuracaoEmail.getLogin();

        Properties properties = getProperties();

        SimpleAuth auth = new SimpleAuth(remetente, configuracaoEmail.getSenha());

        Address[] toUser = new Address[destinatarios.length];

        for (int i = 0; i < destinatarios.length; i++) {
            toUser[i] = new InternetAddress(destinatarios[i]);
        }

        Transport tr = null;

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

        if (anexos != null && anexos.size() > 0) {
            for (Map.Entry e : anexos.entrySet()) {
                if (e.getValue() != null) {
                    MimeBodyPart mbp = new MimeBodyPart();

                    String arquivo = System.getProperty("java.io.tmpdir") + "/arquivo_" + e.getKey();

                    Arquivo arq = (Arquivo) e.getValue();

                    FileUtils.writeByteArrayToFile(new File(arquivo), arq.getConteudo());

                    DataSource fds = new FileDataSource(arquivo);
                    mbp.setDisposition(Part.ATTACHMENT);
                    mbp.setDataHandler(new DataHandler(fds));
                    mbp.setFileName(arq.getNome());

                    mpRoot.addBodyPart(mbp);
                }
            }
        }
        msg.setContent(mpRoot);

        msg.saveChanges();
        tr.sendMessage(msg, msg.getAllRecipients());
        tr.close();

        if (tr != null && tr.isConnected()) {
            tr.close();
        }

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
