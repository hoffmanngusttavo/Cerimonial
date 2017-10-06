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
import br.com.cerimonial.web.UsuarioLogado;
import java.io.Serializable;
import javax.ejb.EJB;

/**
 *
 * @author hoffmann
 */
public class ClienteControl extends BasicControl implements Serializable {

    protected Usuario usuarioLogado = null;
    protected Pessoa cliente = null;
    
    @EJB
    PessoaService pessoaService;

    public ClienteControl() {

        try {
            usuarioLogado = UsuarioLogado.getInstance().getUsuario();

            cliente = pessoaService.getClienteByUsuario(usuarioLogado);
            
        } catch (Exception e) {
            
            createFacesErrorMessage(e.getCause().getMessage());
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
