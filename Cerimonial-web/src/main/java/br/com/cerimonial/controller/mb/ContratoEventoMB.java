/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.ContratoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.service.ContratoEventoService;
import br.com.cerimonial.service.EventoService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "ContratoEventoMB")
@ViewScoped
public class ContratoEventoMB extends BasicControl {

    /**
     * Id do Evento
     */
    protected Long id;
    protected ContratoEvento entity;
    
    @EJB
    protected ContratoEventoService service;
    
    @EJB
    protected EventoService eventoService;
    
    
    

    /**
     * Evento invocado ao abrir o xhtml carregar os dados do contrato do evento
     */
    public void init() {

        if (id != null) {
            try {
                entity = service.getContratoByEvento(id);
                
                if(entity == null){
                    entity = new ContratoEvento();
                    Evento evento = eventoService.getEntity(id);
                    entity.setEvento(evento);
                }
                
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                createFacesErrorMessage(ex.getCause().getMessage());
                scrollTopMessage();
            }
        }

    }
    
    
    /**
     * Evento invocado pela tela de form para salvar um novo ou edicao de um
     * contato
     *
     * @return
     */
    public synchronized String save() {
        try {
            if (entity != null) {
                service.save(entity);
                createFacesInfoMessage("Dados gravados com sucesso!");
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                return "index.xhtml?faces-redirect=true";
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContratoEvento getEntity() {
        return entity;
    }

    public void setEntity(ContratoEvento entity) {
        this.entity = entity;
    }
    
    
    
    
    

}
