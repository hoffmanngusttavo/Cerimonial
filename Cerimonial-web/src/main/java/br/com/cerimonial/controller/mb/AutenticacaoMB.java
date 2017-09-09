/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Usuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Gustavo Hoffmann
 */
@ViewScoped
@ManagedBean(name = "AutenticacaoMB")
public class AutenticacaoMB extends BasicControl {

    private Usuario usuarioLogado = null;


    public AutenticacaoMB() {

        if (usuarioLogado == null) {
            usuarioLogado = getUsuario();
        }
    }

    /**
     * Evento vindo de qualquer view para identificar se o usuário tem
     * determinada permissão
     *
     * @param permissao
     * @return
     */
    public boolean hasPermission(String permissao) {
        if (SecurityUtils.getSubject() != null && SecurityUtils.getSubject().getPrincipal() != null) {
            if (((Usuario) SecurityUtils.getSubject().getPrincipal()).isMaster()) {
                return true;
            }
        }
        return SecurityUtils.getSubject().isPermitted(permissao);
    }

    /**
     * Evento invocado do menu pelo usuário identificando que ele deseja fazer
     * logout. retornando para o login;
     */
    public void sair() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            String externalContextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            SecurityUtils.getSubject().logout();
            context.execute("window.location='" + externalContextPath + "/index.xhtml?faces-redirect=true'");
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getLocalizedMessage());
        } finally {
            try {
                HttpSession ref = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                ref.invalidate();
                System.gc();
            } catch (Exception ex) {
                createFacesErrorMessage(ex.getLocalizedMessage());
            }
        }

    }

    /**
     * metodo que recupera o usuário logado
     *
     * @return
     */
    private Usuario getUsuario() {
        try {
            if (SecurityUtils.getSubject().getPrincipal() != null) {
                Usuario usuario = (Usuario) SecurityUtils.getSubject().getPrincipal();
                return usuario;
            }
        } catch (Exception ex) {
            Logger.getLogger(AutenticacaoMB.class.getSimpleName()).log(Level.WARNING, null, ex);
        }

        return null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

}
