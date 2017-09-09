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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "AcessoSistemaMB")
@ViewScoped
public class AcessoSistemaMB extends BasicControl {

    private String login;
    private String senha;
    private String email;
    private boolean exibirLogin = true;

    @EJB
    private UsuarioService usuarioService;

    public AcessoSistemaMB() {
        System.out.println("Construtor");
    }

    /**
     * Evento vindo da tela de login para autenticação
     *
     * @return vai redirecionar para dentro do sistema, se estiver autenticado,
     * caso contrário, volta para o login.
     */
    public String logar() {
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(login, senha.toCharArray(), false);
            SecurityUtils.getSubject().login(token);
            return "/intranet/index.xhtml?faces-redirect=true";
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
