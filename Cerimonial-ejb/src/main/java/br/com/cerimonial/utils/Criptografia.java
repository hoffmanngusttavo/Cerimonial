/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Gustavo Hoffmann
 */
public class Criptografia {
    
     public static String crypt(String valor, String keyC) {
        try {
            // only the first 8 Bytes of the constructor argument are used 
// as material for generating the keySpec
            DESKeySpec keySpec = new DESKeySpec("YourSecr".getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            sun.misc.BASE64Encoder base64encoder = new BASE64Encoder();

// ENCODE plainTextPassword String
            byte[] cleartext = valor.getBytes("UTF8");

            Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String encrypedPwd = base64encoder.encode(cipher.doFinal(cleartext));
// now you can store it 

// DECODE encryptedPwd String
            return encrypedPwd;

        } catch (Exception ex) {
            return valor;
        }
    }

    public static String decrypt(String valor) {
        try {
            DESKeySpec keySpec = new DESKeySpec("YourSecr".getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            sun.misc.BASE64Decoder base64decoder = new BASE64Decoder();

            byte[] encrypedPwdBytes = base64decoder.decodeBuffer(valor);

            Cipher cipher = Cipher.getInstance("DES");// cipher is not thread safe
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));

            return new String(plainTextPwdBytes);
        } catch (Exception ex) {
            Logger.getLogger(Criptografia.class.getName()).log(Level.SEVERE, null, ex);
            return valor;
        }
    }

    public static String md5(String value) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(value.getBytes(), 0, value.length());
            BigInteger i = new BigInteger(1, m.digest());
            return String.format("%1$032x", i);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
