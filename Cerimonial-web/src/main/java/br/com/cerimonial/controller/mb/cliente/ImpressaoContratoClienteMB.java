/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

import br.com.cerimonial.entity.ContratoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.service.ContratoEventoService;
import br.com.cerimonial.utils.SelectItemUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "ImpressaoContratoClienteMB")
@ViewScoped
public class ImpressaoContratoClienteMB extends ClienteControl {

    /**
     * Id do Evento
     */
    protected Long idEvento;
    protected ContratoEvento contrato;

    @EJB
    protected ContratoEventoService service;

    /**
     * Evento invocado ao abrir o xhtml de impressão de contrato do cliente
     */
    public void init() {

        try {
            
            contrato = service.getContratoByEventoContratante(idEvento, cliente);

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

    public ContratoEvento getContrato() {
        return contrato;
    }

    public void setContrato(ContratoEvento contrato) {
        this.contrato = contrato;
    }
    
    
    

}
