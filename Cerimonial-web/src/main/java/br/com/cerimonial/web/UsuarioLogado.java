/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.web;

import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.service.UsuarioService;
import br.com.cerimonial.service.utils.ServiceLookupUtil;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.shiro.SecurityUtils;

/**
 *
 * @author hoffmann
 */
public class UsuarioLogado implements Serializable {

    private static UsuarioLogado instance;
    private static Usuario usuario;

    private UsuarioLogado() {

    }

    public static UsuarioLogado getInstance() {

        if (instance == null) {
            try {
                instance = new UsuarioLogado();
                
                ServiceLookupUtil lookupUtil = new ServiceLookupUtil();
                UsuarioService service = lookupUtil.lookupService(UsuarioService.class);
                
                Usuario user = (Usuario) SecurityUtils.getSubject().getPrincipal();
                
                usuario = service.getEntity(user.getId());
            } catch (Exception ex) {
                Logger.getLogger(UsuarioLogado.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return instance;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setInstance(UsuarioLogado instance) {
        UsuarioLogado.instance = instance;
    }

   


    
}
