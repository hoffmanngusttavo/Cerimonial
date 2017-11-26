/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.service.ContatoEventoService;
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
@ManagedBean(name = "PreEventoMB")
@ViewScoped
public class PreEventoMB extends BasicControl {

    protected ContatoEvento entity;
    protected Evento evento;

    protected Long id;
    protected Long idEvento;

    @EJB
    protected ContatoEventoService service;
    @EJB
    protected EventoService eventoService;

    /**
     * Evento invocado ao abrir o xhtml de um contato inicial de pre evento
     */
    public void init() {
        try {
            if (id != null) {

                entity = service.getEntity(id);

                evento = eventoService.getEventoByContatoInicial(entity);

            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar o evento: " + ex.getCause().getMessage());
        }
    }

    /**
     * Evento invocado ao abrir o xhtml de um contato inicial de pre evento
     */
    public void initEvento() {
        try {
            if (idEvento != null) {

                evento = eventoService.getEntity(idEvento);

                entity = service.getContatoInicialByEvento(evento);

            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar o evento: " + ex.getCause().getMessage());
        }
    }

    /**
     * Evento invocado pela tela para cancelar um evento
     */
    public void cancelarEvento() {
        try {

            eventoService.cancelarEvento(evento.getId(), evento.getMotivoCancelamento());
            
            evento = eventoService.getEntity(evento.getId());
            
            createFacesInfoMessage("Evento cancelado com sucesso");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível cancelar o evento: " + ex.getCause().getMessage());
        }
    }

    public ContatoEvento getEntity() {
        return entity;
    }

    public void setEntity(ContatoEvento entity) {
        this.entity = entity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

}
