/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.service.ContatoEventoService;
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
@ManagedBean(name = "PreEventoMB")
@ViewScoped
public class PreEventoMB extends BasicControl{
    
    protected ContatoEvento entity;
    protected Long id;
    
    @EJB
    protected ContatoEventoService service;

    /**
     * Evento invocado ao abrir o xhtml na ediÃ§Ã£o de um cliente objetivo de
     * carregar os dados do cliente
     */
    public void init() {

        if (id != null) {
            try {
                entity = service.getEntity(id);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            entity = new ContatoEvento();
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
    
    
    
    

    
    
    
    
}
