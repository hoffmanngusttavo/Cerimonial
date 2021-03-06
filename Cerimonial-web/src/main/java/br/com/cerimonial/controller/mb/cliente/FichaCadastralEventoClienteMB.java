/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

import br.com.cerimonial.entity.CerimoniaEvento;
import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.FestaCerimonia;
import br.com.cerimonial.service.EnderecoService;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.utils.SelectItemUtils;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "FichaCadastralEventoClienteMB")
@ViewScoped
public class FichaCadastralEventoClienteMB extends ClienteControl{
    
    protected Long idEvento;
    
    protected Evento evento;
    
    @EJB
    protected EventoService eventoService;
    
    @EJB
    protected EnderecoService enderecoService;

    protected final SelectItemUtils selectItemUtils;

    public FichaCadastralEventoClienteMB() {
        this.selectItemUtils = new SelectItemUtils();
    }

    /**
     * Evento invocado ao abrir o xhtml de ficha cadastral
     */
    public void initEvento() {
        try {
            
            evento = eventoService.findEventoByIdAndContratante(idEvento, cliente, Arrays.asList("cerimoniaEvento", "festaCerimonia", "evolucoesPreenchimento"));
           
            if(evento.isCategoriaCasamento() && evento.getCerimoniaEvento() == null){
                evento.setCerimoniaEvento(new CerimoniaEvento());
            }
            
            if(evento.getFestaCerimonia() == null){
                evento.setFestaCerimonia(new FestaCerimonia());
            }

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar o evento: " + ex.getCause().getMessage());
        }
    }

    /**
     * Evento invocado pela tela de form para salvar um evento
     *
     * @return
     */
    public synchronized String save() {
        try {

            eventoService.saveEventoCliente(evento);

            createFacesInfoMessage("Dados gravados com sucesso!");

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            return "/intranet/cliente/evento/partials/ficha-cadastral.xhtml?idEvento=" + idEvento + "&faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível salvar o evento "+ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }

    public void copiarEnderecoCerimonia() {
        try {

            eventoService.copiarLocalizacaoCerimonia(evento.getCerimoniaEvento(), evento.getFestaCerimonia());
            
            createFacesInfoMessage("Copiado com sucesso");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi copiar o local: " + ex.getCause().getMessage());
        }
    }
    
    public void buscaCepCerimoniaEvento() {
        try {

            evento.getCerimoniaEvento().setEndereco(enderecoService.buscaCep(evento.getCerimoniaEvento().getEndereco()));

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void buscaCepFestaEvento() {
        try {

            evento.getFestaCerimonia().setEndereco(enderecoService.buscaCep(evento.getFestaCerimonia().getEndereco()));

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<SelectItem> getComboCidade(Estado estado) {
        return selectItemUtils.getComboCidade(estado);
    }

    public List<SelectItem> getComboEstado() {
        return selectItemUtils.getComboEstado();
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
