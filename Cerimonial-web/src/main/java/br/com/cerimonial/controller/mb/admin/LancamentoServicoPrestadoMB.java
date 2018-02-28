/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Servico;
import br.com.cerimonial.enums.FormaPagamento;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.LancamentoService;
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
@ManagedBean(name = "LancamentoServicoPrestadoMB")
@ViewScoped
public class LancamentoServicoPrestadoMB extends BasicControl{
    
    
    private Long idServicoPrestado;
    private Evento evento;
    private Lancamento entity;
    
    private List<Pessoa> contratantes;
    private List<Servico> servicos;
    private List<FormaPagamento> formasPagamentos;
    
    @EJB
    protected EventoService eventoService;

    @EJB
    protected LancamentoService service;

    @EJB
    protected PessoaService pessoaService;

    @EJB
    protected ServicoService servicoService;
    
    
    public LancamentoServicoPrestadoMB() {

    }

    /**
     * Evento invocado ao abrir o xhtml carregar os dados do contrato do evento
     */
    public void init() {

        if (idServicoPrestado != null) {
            try {

                entity = service.findLancamentoByServicoPrestadoId(idServicoPrestado);

                evento = eventoService.findEventoByServicoPrestadoId(idServicoPrestado);

                contratantes = pessoaService.findContratantesByEventoId(evento.getId());

                if (entity == null) {

                    entity = service.criarNovoLancamentoSaidaOrcamento(contratantes, evento);

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
            
            service.saveLancamentoServicoPrestado(entity);

            createFacesInfoMessage("Dados gravados com sucesso!");

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            return "/intranet/admin/operacional/pre-evento/index.xhtml?idPreEvento=" + evento.getPreEvento().getId() + "&faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }

    public Long getIdServicoPrestado() {
        return idServicoPrestado;
    }

    public void setIdServicoPrestado(Long idServicoPrestado) {
        this.idServicoPrestado = idServicoPrestado;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Lancamento getEntity() {
        return entity;
    }

    public void setEntity(Lancamento entity) {
        this.entity = entity;
    }

    public List<Pessoa> getContratantes() {
        return contratantes;
    }

    public void setContratantes(List<Pessoa> contratantes) {
        this.contratantes = contratantes;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }

    public List<FormaPagamento> getFormasPagamentos() {
        return formasPagamentos;
    }

    public void setFormasPagamentos(List<FormaPagamento> formasPagamentos) {
        this.formasPagamentos = formasPagamentos;
    }
    
    
    
    
}
