/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.service.PessoaService;
import br.com.cerimonial.service.UsuarioService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.apache.shiro.SecurityUtils;

/**
 *
 * @author hoffmann
 */
public class ClienteControl extends BasicControl implements Serializable {

    protected Usuario usuarioLogado = null;
    protected Pessoa cliente = null;

    @EJB
    PessoaService pessoaService;
    
    @EJB
    UsuarioService usuarioService;

    @PostConstruct
    public void postConstruct() {
        try {
            
            Usuario user = (Usuario) SecurityUtils.getSubject().getPrincipal();
            
            usuarioLogado = usuarioService.getEntity(user.getId());

            cliente = pessoaService.getClienteByUsuario(usuarioLogado);

        } catch (Exception e) {
            if (e.getCause() != null) {
                createFacesErrorMessage(e.getCause().getMessage());
            } else {
                createFacesErrorMessage(e.getMessage());
            }
        }

    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public Pessoa getCliente() {
        return cliente;
    }

    public void setCliente(Pessoa cliente) {
        this.cliente = cliente;
    }

}
