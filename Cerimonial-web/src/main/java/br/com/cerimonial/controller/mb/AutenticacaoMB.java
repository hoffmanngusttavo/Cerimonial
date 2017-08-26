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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "AutenticacaoMB")
@ViewScoped
public class AutenticacaoMB extends BasicControl {

    protected String login;
    protected String senha;
    protected String email;
    protected boolean exibirLogin = true;
    protected boolean remember = false;
    protected Usuario usuarioLogado = null;

    @EJB
    private UsuarioService usuarioService;

    public AutenticacaoMB() {
        if (usuarioLogado == null) {
            usuarioLogado = getUsuario();
        }
    }

    /**
     * Evento vindo da tela de login para autenticação
     *
     * @return vai redirecionar para dentro do sistema, se estiver autenticado,
     * caso contrário, volta para o login.
     */
    public String logar() {
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(login, senha.toCharArray(), remember);
            SecurityUtils.getSubject().login(token);
            return "/intranet/admin/index.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(AutenticacaoMB.class.getSimpleName()).log(Level.WARNING, null, ex);
            createFacesErrorMessage("Login ou senha inválidos");
        }
        return null;
    }

    /**
     * evento vindo da tela de login para criar um usuário master exibido
     * somente uma vez para ter acesso ao sistema. login: master, senha:master
     */
    public void criarUsuarioMaster() {
        try {
            Usuario user = usuarioService.criarSalvarUsuarioMaster();
            createFacesInfoMessage("Usuário " + user.getNome() + " criado com sucesso");
        } catch (Exception ex) {
            Logger.getLogger(AutenticacaoMB.class.getSimpleName()).log(Level.WARNING, null, ex);
            createFacesErrorMessage("Não foi possível criar usuario master");
        } finally {
            scrollTopMessage();
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

    /**
     * metodo invocado pela tela de login para enviar senha por email
     */
    public void enviarLembrarSenha() {
        try {
            usuarioService.enviarLembreteSenha(email);
            exibirLogin = true;
        } catch (Exception ex) {
            Logger.getLogger(AutenticacaoMB.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getLocalizedMessage());
        }
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

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isExibirLogin() {
        return exibirLogin;
    }

    public void setExibirLogin(boolean exibirLogin) {
        this.exibirLogin = exibirLogin;
    }

}
