/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EvolucaoPreenchimento;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.EvolucaoPreenchimentoService;
import java.util.LinkedList;
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
@ManagedBean(name = "EventoClienteMB")
@ViewScoped
public class EventoClienteMB extends ClienteControl {

    protected Long idEvento;
    protected Evento evento;
    
    private EvolucaoPreenchimento evolucaoEvento;
    private EvolucaoPreenchimento evolucaoContratante;
    private EvolucaoPreenchimento evolucaoNoivo;
    private EvolucaoPreenchimento evolucaoNoiva;

    @EJB
    protected EventoService eventoService;
    @EJB
    protected EvolucaoPreenchimentoService evolucaoPreenchimentoService;

    /**
     * Evento invocado ao abrir o xhtml de um contato inicial de pre evento
     */
    public void initEvento() {
        try {

            List<String> camposLazy = new LinkedList<String>();
            camposLazy.add("contratos");
            camposLazy.add("evolucoesPreenchimento");
            camposLazy.add("contratantes");
            camposLazy.add("contratantes.evolucaoPreenchimento");
            
            evento = eventoService.findEventoByIdAndContratante(idEvento, cliente, camposLazy);
            
            
            preencherPorcentagemConcluida(evento);

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar o evento: " + ex.getCause().getMessage());
        }
    }
    
    public void preencherPorcentagemConcluida(Evento evento) {

        evolucaoContratante = evolucaoPreenchimentoService.getEvolucaoDadosContratante(evento);
        evolucaoNoivo = evolucaoPreenchimentoService.getEvolucaoDadosNoivo(evento);
        evolucaoNoiva = evolucaoPreenchimentoService.getEvolucaoDadosNoiva(evento);
        evolucaoEvento = evolucaoPreenchimentoService.getEvolucaoDadosEvento(evento);

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

    public EvolucaoPreenchimento getEvolucaoEvento() {
        return evolucaoEvento;
    }

    public void setEvolucaoEvento(EvolucaoPreenchimento evolucaoEvento) {
        this.evolucaoEvento = evolucaoEvento;
    }

    public EvolucaoPreenchimento getEvolucaoContratante() {
        return evolucaoContratante;
    }

    public void setEvolucaoContratante(EvolucaoPreenchimento evolucaoContratante) {
        this.evolucaoContratante = evolucaoContratante;
    }

    public EvolucaoPreenchimento getEvolucaoNoivo() {
        return evolucaoNoivo;
    }

    public void setEvolucaoNoivo(EvolucaoPreenchimento evolucaoNoivo) {
        this.evolucaoNoivo = evolucaoNoivo;
    }

    public EvolucaoPreenchimento getEvolucaoNoiva() {
        return evolucaoNoiva;
    }

    public void setEvolucaoNoiva(EvolucaoPreenchimento evolucaoNoiva) {
        this.evolucaoNoiva = evolucaoNoiva;
    }

    
    
}
