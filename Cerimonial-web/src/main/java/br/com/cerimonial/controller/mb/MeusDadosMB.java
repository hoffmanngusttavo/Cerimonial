/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.service.UsuarioService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "MeusDadosMB")
@ViewScoped
public class MeusDadosMB extends BasicControl {

    private Usuario entity;
    @EJB
    private UsuarioService service;
    
    private String confirmarSenha;

    /**
     * Evento invocado ao abrir o xhtml na edição de uma usuario objetivo de
     */
    public void init() {
        try {
            if (!entityLoaded) {

                if (SecurityUtils.getSubject().getPrincipal() != null) {
                    Usuario usuario = (Usuario) SecurityUtils.getSubject().getPrincipal();

                    entity = service.findEntityById(usuario.getId());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Evento invocado ao abrir o xhtml na edição senha do usuário
     */
    public void initSenha() {
        init();
        entity.setSenha(null);
    }

    /**
     * Evento invocado pela tela de form para salvar um novo ou edicao de uma
     * empresa
     *
     * @return
     */
    public synchronized String save() {
        try {
            if (entity != null) {
                service.save(entity);
                createFacesInfoMessage("Dados gravados com sucesso!");
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                return "index.xhtml?faces-redirect=true";
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }
    
    /**
     * Evento invocado pela tela de form para trocar a senha do usuário
     *
     * @return
     */
    public synchronized String trocarSenha() {
        try {
            if (entity != null) {
                
                if(StringUtils.isNotBlank(entity.getSenha()) && StringUtils.isNotBlank(confirmarSenha)){
                    
                    if(!entity.getSenha().equals(confirmarSenha)){
                        createFacesWarnMessage("As senhas estão diferentes, preencha corretamente");
                        return null;
                    }
                    
                }else{
                    createFacesWarnMessage("As senhas estão diferentes, preencha corretamente");
                    return null;
                }
                
                service.alterarSenha(entity);
                createFacesInfoMessage("Dados gravados com sucesso!");
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                return "index.xhtml?faces-redirect=true";
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
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
