/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller;

import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.service.UsuarioService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "UsuarioCrudMB")
@ViewScoped
public class UsuarioCrudMB extends BasicControl{
    
    private Usuario entity;
    private String confirmarSenha;
    @EJB
    private UsuarioService usuarioService;

    public UsuarioCrudMB() {
    }
    
    public synchronized String save()  {
        try {
            usuarioService.save(entity);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public synchronized String alterarSenha()  {
        try {
            usuarioService.alterarSenha(entity);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public Usuario getEntity() {
        return entity;
    }

    public void setEntity(Usuario entity) {
        this.entity = entity;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }
    
    
    
    
}
