/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.CustoEvento;
import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.service.CustoEventoService;
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
@ManagedBean(name = "PlanilhaCustosMB")
@ViewScoped
public class PlanilhaCustosMB extends BasicControl {

    /**
     * Id do Evento
     */
    protected Long idEvento;
    protected CustoEvento entity;
    protected Lancamento lancamento;

    @EJB
    protected CustoEventoService service;
    @EJB
    protected EventoService eventoService;
    @EJB
    protected LancamentoService lancamentoService;

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
    
    public String removerLancamento(){
        try {

            lancamentoService.removerLancamentoPlanilhaCustoEvento(lancamento);
            
            service.atualizarSalvarValoresCusto(entity);
            
            createFacesInfoMessage("Dados removidos com sucesso");
            
            return "/intranet/admin/operacional/pre-evento/partials/planilha-custos.xhtml?idEvento=" + idEvento + "&faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            
            String msg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
            
            createFacesErrorMessage("Não foi possível remover: "+msg);
        }finally{
            scrollTopMessage();
        }
    
        return null;
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

    public Lancamento getLancamento() {
        return lancamento;
    }

    public void setLancamento(Lancamento lancamento) {
        this.lancamento = lancamento;
    }
    
    
    
    

}
