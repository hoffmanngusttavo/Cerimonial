/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.AtividadeEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.entity.Servico;
import br.com.cerimonial.enums.FormaPagamento;
import br.com.cerimonial.service.AtividadeEventoService;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.LancamentoService;
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
@ManagedBean(name = "CustoEventoCrudMB")
@ViewScoped
public class CustoEventoCrudMB extends BasicControl {

    protected Long idAtividade;
    protected Long idLancamento;

    protected Evento evento;
    protected AtividadeEvento atividadeEvento;
    protected Lancamento entity;
    
    protected List<Servico> servicos;

    @EJB
    protected EventoService eventoService;

    @EJB
    protected LancamentoService service;

    @EJB
    protected AtividadeEventoService atividadeEventoService;

    public void initAtividade() {

        if (idAtividade != null) {
            try {

                atividadeEvento = atividadeEventoService.findEntityById(idAtividade);

                entity = new Lancamento(atividadeEvento);

                evento = eventoService.findEntityById(atividadeEvento.getEvento().getId());

            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                createFacesErrorMessage(ex.getCause().getMessage());
                scrollTopMessage();
            }
        }

    }

    public String save() {

        try {
            service.save(entity);

        } catch (Exception ex) {
            Logger.getLogger(CustoEventoCrudMB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    public Long getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(Long idAtividade) {
        this.idAtividade = idAtividade;
    }

    public Long getIdLancamento() {
        return idLancamento;
    }

    public void setIdLancamento(Long idLancamento) {
        this.idLancamento = idLancamento;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public AtividadeEvento getAtividadeEvento() {
        return atividadeEvento;
    }

    public void setAtividadeEvento(AtividadeEvento atividadeEvento) {
        this.atividadeEvento = atividadeEvento;
    }

    public Lancamento getEntity() {
        return entity;
    }

    public void setEntity(Lancamento entity) {
        this.entity = entity;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }
    
    

}
