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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "AutenticacaoMB")
@ViewScoped
public class AutenticacaoMB extends BasicControl {

    protected String login;
    protected String senha;
    protected boolean remember = false;
    protected AutenticacaoInterface autenticacaoInterface;

    @EJB
    private UsuarioService usuarioService;

    public AutenticacaoMB() {

        autenticacaoInterface = new AutenticacaoInterface() {

            @Override
            public String retornoLogin() {
                try {
                    FacesContext fc = FacesContext.getCurrentInstance();
                    return fc.getExternalContext().getInitParameter("logar");
                } catch (Exception ex) {
                    String msg = "Falha ao tentar entrar no sistema";
                    createFacesErrorMessage(msg);
                    return null;
                }
            }

            @Override
            public String retornoLogout() {
                try {
                    FacesContext fc = FacesContext.getCurrentInstance();
                    String retorno = fc.getExternalContext().getInitParameter("sair");
                    return retorno;
                } catch (Exception ex) {
                    return null;
                }
            }

            @Override
            public Usuario consultaLembrarSenha(String login) {
                try {
                    return usuarioService.getUsuarioByLogin(login);
                } catch (Exception ex) {
                    Logger.getLogger(AutenticacaoMB.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }

            @Override
            public void enviarSenha(Usuario usuario) {

            }
        };

    }

    public String logar() {
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(login, senha.toCharArray(), remember);
            SecurityUtils.getSubject().login(token);
            return getRequestParam("retorno") != null ? getRequestParam("retorno") : "login";
        } catch (Exception ex) {
            Logger.getLogger(AutenticacaoMB.class.getSimpleName()).log(Level.WARNING, null, ex);
            createFacesErrorMessage("Login ou senha inválidos");
        }
        return null;
    }
    
    public void criarUsuarioMaster() {
        try {
            Usuario user = usuarioService.criarUsuarioMaster();
            createFacesInfoMessage("Usuário "+user.getNome()+" criado com sucesso");
        } catch (Exception ex) {
            Logger.getLogger(AutenticacaoMB.class.getSimpleName()).log(Level.WARNING, null, ex);
            createFacesErrorMessage("Não foi possível criar usuario master");
        }finally{
            scrollTopMessage();
        }
    }

    public boolean hasPermission(String permissao) {
        if (SecurityUtils.getSubject() != null && SecurityUtils.getSubject().getPrincipal() != null) {
            if (((Usuario) SecurityUtils.getSubject().getPrincipal()).isMaster()) {
                return true;
            }
        }
        return SecurityUtils.getSubject().isPermitted(permissao);
    }

    public String sair() {
        try {
            SecurityUtils.getSubject().logout();
            return autenticacaoInterface.retornoLogout();
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getLocalizedMessage());
        } finally {
            try {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("AtributoSessao");
                HttpSession ref = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                ref.invalidate();
                System.gc();
            } catch (Exception ex) {
                createFacesErrorMessage(ex.getLocalizedMessage());
            }
        }
        return null;
    }

    public Usuario getUsuario() {
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    
    
}
