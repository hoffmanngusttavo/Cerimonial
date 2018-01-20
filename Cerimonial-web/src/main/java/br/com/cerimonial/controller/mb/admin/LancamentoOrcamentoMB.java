/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.enums.TipoLancamento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.LancamentoService;
import br.com.cerimonial.service.OrcamentoEventoService;
import br.com.cerimonial.utils.CollectionUtils;
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
@ManagedBean(name = "LancamentoOrcamentoMB")
@ViewScoped
public class LancamentoOrcamentoMB extends BasicControl {

    protected Long idOrcamento;
    protected OrcamentoEvento orcamentoEvento;
    protected Evento evento;
    protected Lancamento lancamento;

    @EJB
    protected OrcamentoEventoService orcamentoService;

    @EJB
    protected EventoService eventoService;

    @EJB
    protected LancamentoService lancamentoService;

    
    
    /**
     * Evento invocado ao abrir o xhtml carregar os dados do contrato do evento
     */
    public void init() {

        if (idOrcamento != null) {
            try {

              lancamento = lancamentoService.findLancamentoOrcamento(idOrcamento);
              
              if(lancamento == null){
                  
                  orcamentoEvento = orcamentoService.getEntity(idOrcamento);
                  
                  lancamento = new Lancamento(TipoLancamento.DESPESA);
                  lancamento.setValorBase(orcamentoEvento.getValorFinal());
              }

            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                createFacesErrorMessage(ex.getCause().getMessage());
                scrollTopMessage();
            }
        }


    }

    public Long getIdOrcamento() {
        return idOrcamento;
    }

    public void setIdOrcamento(Long idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public Lancamento getLancamento() {
        return lancamento;
    }

    public void setLancamento(Lancamento lancamento) {
        this.lancamento = lancamento;
    }
    
    
    
    
    
    
}
