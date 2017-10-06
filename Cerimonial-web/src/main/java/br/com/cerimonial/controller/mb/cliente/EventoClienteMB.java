/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.service.EventoService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "EventoClienteMB")
@ViewScoped
public class EventoClienteMB extends ClienteControl{
    
    protected Long idEvento;
    protected Evento evento;
    
    @EJB
    protected EventoService eventoService;

    public EventoClienteMB() {
        
    }
    
     /**
     * Evento invocado ao abrir o xhtml de um contato inicial de pre evento
     */
    public void initEvento() {
        try {
            if (idEvento != null) {

                evento = eventoService.getEntity(idEvento);

//                entity = service.getContatoInicialByEvento(evento);

            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar o evento: " + ex.getCause().getMessage());
        }
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    
    
    
    
}
