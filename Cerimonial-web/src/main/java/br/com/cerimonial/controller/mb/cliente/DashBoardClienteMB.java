/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.service.EventoService;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "DashBoardClienteMB")
@ViewScoped
public class DashBoardClienteMB extends ClienteControl{

    
    private List<Evento> eventosAtivos;
    
    @EJB
    protected EventoService eventoService;
    
    
    public DashBoardClienteMB() {
        try {
            
            eventosAtivos = eventoService.findEventosAtivosCliente(cliente);
            
        } catch (Exception e) {
            
            createFacesErrorMessage(e.getCause().getMessage());
        }
        
    
    }

    public List<Evento> getEventosAtivos() {
        return eventosAtivos;
    }

    public void setEventosAtivos(List<Evento> eventosAtivos) {
        this.eventosAtivos = eventosAtivos;
    }
    
    
    
    
}
