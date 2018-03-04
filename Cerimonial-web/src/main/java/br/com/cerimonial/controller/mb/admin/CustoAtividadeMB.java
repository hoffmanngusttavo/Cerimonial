/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.LancamentoService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "CustoAtividadeMB")
@ViewScoped
public class CustoAtividadeMB extends BasicControl {

    private Long idAtividade;

    private Lancamento entity;
    
    private Evento evento;

    @EJB
    private LancamentoService lancamentoService;
    @EJB
    private EventoService eventoService;

    public CustoAtividadeMB() {
    }

    /**
     * Evento invocado ao abrir o xhtml
     */
    public void init() {
        try {
            
            if(idAtividade != null){
                
                entity = lancamentoService.findLancamentoByAtividadeId(idAtividade);
                
                if(entity == null){
                    entity = lancamentoService.criarLancamentoDespesaEventoByAtividadeId(idAtividade);
                }
                
                evento = eventoService.findEventoByAtividadeId(idAtividade);
                
            }
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar o custo: " + ex.getCause().getMessage());
        }
    }

    /**
     * Evento invocado pela tela de form para salvar um evento
     *
     * @return
     */
    public synchronized String save() {
        try {

            entity = lancamentoService.saveLancamentoAtividade(entity, evento);
            
            createFacesInfoMessage("Dados gravados com sucesso!");

            return "/intranet/admin/operacional/pre-evento/partials/custo-atividade.xhtml?idAtividade=" + idAtividade + "&faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível salvar o cadastro dos noivos " + ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }

    public Long getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(Long idAtividade) {
        this.idAtividade = idAtividade;
    }

    public Lancamento getEntity() {
        return entity;
    }

    public void setEntity(Lancamento entity) {
        this.entity = entity;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    
    
    
    
    
    

}
