/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service.utils;

import br.com.cerimonial.utils.ConstantsProject;
import br.com.cerimonial.utils.EmailHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 *
 * @author Gustavo Hoffmann
 */
public class ServiceLookupUtil {

    public ServiceLookupUtil() {
        super();
    }

    public <S> S lookupService(Class<S> clazz) {
        S service = null;
        Context context = null;
        try {
            context = new InitialContext();
            String jndi = getLookupName(clazz);
//                log.debug("Looking up: " + jndi);
            service = (S) context.lookup(jndi);
        } catch (Exception ex) {
                Logger.getLogger(EmailHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (context != null) {
                try {
                    context.close();
                } catch (Exception e) {
                    Logger.getLogger(EmailHelper.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        return service;
    }

    private <S> String getLookupName(Class<S> clazz) {
        return "java:global/" + ConstantsProject.appName + "/" + ConstantsProject.moduleEjbName + "/" + clazz.getSimpleName() + "!" + clazz.getName();
    }

}
