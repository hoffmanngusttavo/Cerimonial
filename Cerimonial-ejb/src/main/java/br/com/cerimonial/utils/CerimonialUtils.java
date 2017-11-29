/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.utils;

import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gustavo Hoffmann
 */
public class CerimonialUtils {

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();

        return convertToHex(sha1hash);
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static boolean isListNotBlank(List list) {
        return list != null && !list.isEmpty();
    }

    public static boolean isListBlank(List list) {
        return list == null || list.isEmpty();
    }

    public static String callURL(String paramUrl) {
        StringBuilder sb = new StringBuilder();
        try {
            URLConnection urlConn;
            InputStreamReader in = null;
            URL url = new URL(paramUrl);
            urlConn = url.openConnection();
            if (urlConn != null) {
                urlConn.setReadTimeout(1000);
            }
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
                try (BufferedReader bufferedReader = new BufferedReader(in)) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                }
            }

            in.close();
        } catch (Exception ex) {

            throw new RuntimeException("Exception while calling URL:" + paramUrl, ex);
        }

        return sb.toString();
    }

    public static String removerNaoDigitos(String valor) {
        if (StringUtils.isNotBlank(valor)) {
            return valor.replaceAll("\\D", "");
        }
        return "";
    }

    public static String gerarAlfaNumericoAleatoria() {
        UUID uuid = UUID.randomUUID();
        String random = uuid.toString();
        return random.substring(0, 6);
    }

    public static void validarEmail(String email) throws Exception {
        if (StringUtils.isBlank(email)) {
            throw new GenericException("Preencha um email válido", ErrorCode.BAD_REQUEST.getCode());
        }
    }

    public static String urlSistema() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getRequestContextPath();
    }

    public static String getApplicationUri() {
        try {
            FacesContext ctxt = FacesContext.getCurrentInstance();
            ExternalContext ext = ctxt.getExternalContext();
            URI uri = new URI(ext.getRequestScheme(),
                    null, ext.getRequestServerName(), ext.getRequestServerPort(),
                    ext.getRequestContextPath(), null, null);
            return uri.toASCIIString();
        } catch (URISyntaxException e) {
            throw new FacesException(e);
        }
    }

}
