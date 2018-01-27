/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Servico;
import br.com.cerimonial.enums.FormaPagamento;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.LancamentoService;
import br.com.cerimonial.service.OrcamentoEventoService;
import br.com.cerimonial.service.PessoaService;
import br.com.cerimonial.service.ServicoService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

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
    protected Lancamento entity;

    protected List<Pessoa> contratantes;
    protected List<Servico> servicos;
    protected List<FormaPagamento> formasPagamentos;

    @EJB
    protected OrcamentoEventoService orcamentoService;

    @EJB
    protected EventoService eventoService;

    @EJB
    protected LancamentoService service;

    @EJB
    protected PessoaService pessoaService;

    @EJB
    protected ServicoService servicoService;

    public LancamentoOrcamentoMB() {

    }

    /**
     * Evento invocado ao abrir o xhtml carregar os dados do contrato do evento
     */
    public void init() {

        if (idOrcamento != null) {
            try {

                entity = service.findLancamentoOrcamento(idOrcamento);

                evento = eventoService.findEventoByIdOrcamento(idOrcamento);

                contratantes = pessoaService.getContratantesEvento(evento.getId());

                if (entity == null) {

                    orcamentoEvento = orcamentoService.getEntity(idOrcamento);

                    entity = service.criarNovoLancamentoSaidaOrcamento(orcamentoEvento, contratantes);

                }

                servicos = servicoService.findAll();

                formasPagamentos = FormaPagamento.getList();

            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                createFacesErrorMessage(ex.getCause().getMessage());
                scrollTopMessage();
            }
        }

    }

    public void atualizarNumeroParcelas() {

        entity = service.atualizarNumeroParcelas(entity.getNumeroParcelas(), entity);

    }

    public String save() {
        try {
            
            service.saveLancamentoOrcamento(entity);

            createFacesInfoMessage("Dados gravados com sucesso!");

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            return "/intranet/admin/operacional/pre-evento/partials/lancamento-orcamento.xhtml?idOrcamento=" + orcamentoEvento.getId() + "&faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(LancamentoOrcamentoMB.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }

    public Long getIdOrcamento() {
        return idOrcamento;
    }

    public void setIdOrcamento(Long idOrcamento) {
        this.idOrcamento = idOrcamento;
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

    public List<Pessoa> getContratantes() {
        return contratantes;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public List<FormaPagamento> getFormasPagamentos() {
        return formasPagamentos;
    }

}
