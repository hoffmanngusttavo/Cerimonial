/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.service.OrcamentoEventoService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "OrcamentoEventoMB")
@ViewScoped
public class OrcamentoEventoMB extends BasicControl {

    protected Long id;

    protected OrcamentoEvento entity;

    @EJB
    protected OrcamentoEventoService service;

    /**
     * Evento invocado ao abrir o xhtml carregar os dados do contrato do evento
     */
    public void init() {

        if (id != null) {
            try {
                entity = service.getOrcamentoByEvento(id);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                createFacesErrorMessage(ex.getCause().getMessage());
                scrollTopMessage();
            }
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrcamentoEvento getEntity() {
        return entity;
    }

    public void setEntity(OrcamentoEvento entity) {
        this.entity = entity;
    }

}
