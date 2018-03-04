/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.CustoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.service.CustoEventoService;
import br.com.cerimonial.service.EventoService;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "PlanilhaCustosMB")
@ViewScoped
public class PlanilhaCustosMB extends BasicControl {

    /**
     * Id do Evento
     */
    protected Long idEvento;
    protected CustoEvento entity;

    @EJB
    protected CustoEventoService service;
    @EJB
    protected EventoService eventoService;

    /**
     * Evento invocado ao abrir o xhtml carregar os dados do contrato do evento
     */
    public void init() {

        try {

            entity = service.findCustoEventoByIdEvento(idEvento);
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getCause().getMessage());
            scrollTopMessage();
        }

    }
    
    public void salvarValor(){
        
        try {

            entity = service.save(entity);
            
            createFacesInfoMessage("Dados gravados com sucesso");
            
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

    public CustoEvento getEntity() {
        return entity;
    }

    public void setEntity(CustoEvento entity) {
        this.entity = entity;
    }
    
    
    
    

}
