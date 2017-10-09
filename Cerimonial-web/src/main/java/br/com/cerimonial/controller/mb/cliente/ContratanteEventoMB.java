/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.service.PessoaService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "ContratanteEventoMB")
@ViewScoped
public class ContratanteEventoMB extends ClienteControl{
    
    protected Long idEvento;
    protected Pessoa contratante;
    
    @EJB
    protected PessoaService service;
   
     /**
     * Evento invocado ao abrir o xhtml de impressão de contrato do cliente
     */
    public void init() {

        try {
            
            contratante = service.getContratanteEvento(idEvento);

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getCause().getMessage());
            scrollTopMessage();
        }
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public Pessoa getContratante() {
        return contratante;
    }

    public void setContratante(Pessoa contratante) {
        this.contratante = contratante;
    }
    
    
    
    
}
