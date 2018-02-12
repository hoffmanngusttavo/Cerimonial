/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.AtividadeEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.service.AtividadeEventoService;
import br.com.cerimonial.service.EventoService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "CheckListEventoMB")
@ViewScoped
public class CheckListEventoMB extends BasicControl {

    private Long idEvento;
    
    private Evento evento;

    private List<AtividadeEvento> atividades;

    @EJB
    protected AtividadeEventoService service;
    @EJB
    protected EventoService eventoService;

    /**
     * Evento invocado ao abrir o xhtml carregar os dados do contrato do evento
     */
    public void init() {

        try {

            if (idEvento != null) {

                evento = eventoService.findEntityById(idEvento);
                
                atividades = service.findAtividadesByIdEvento(idEvento);

            }

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

    public List<AtividadeEvento> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<AtividadeEvento> atividades) {
        this.atividades = atividades;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    
    
    
    

}
