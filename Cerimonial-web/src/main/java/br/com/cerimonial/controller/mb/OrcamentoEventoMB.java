/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.service.ContatoEventoService;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.OrcamentoEventoService;
import br.com.cerimonial.utils.ArquivoUtils;
import br.com.cerimonial.utils.CerimonialUtils;
import br.com.cerimonial.utils.SelectItemUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "OrcamentoEventoMB")
@ViewScoped
public class OrcamentoEventoMB extends BasicControl {

    protected Long idContato;
    protected ContatoEvento contatoEvento;
    protected Evento evento;

    protected OrcamentoEvento entity;
    protected List<OrcamentoEvento> orcamentos;

    protected boolean abrirFormOrcamento = false;
    protected UploadedFile file;
    protected SelectItemUtils selectItemUtils;

    @EJB
    protected OrcamentoEventoService service;

    @EJB
    protected ContatoEventoService contatoEventoService;
    
    @EJB
    protected EventoService eventoService;

    /**
     * Evento invocado ao abrir o xhtml carregar os dados do contrato do evento
     */
    public void init() {

        if (idContato != null) {
            try {

                contatoEvento = contatoEventoService.getEntity(idContato);
                
                evento = eventoService.getEventoByContatoInicial(contatoEvento);

                if(contatoEvento == null){
                    throw new GenericException("Não foi possível encontrar o Contato Inicial com esse Id", ErrorCode.NOT_FOUND.getCode());
                }
                
                orcamentos = service.findAllByContatoId(contatoEvento.getId());

                if (CerimonialUtils.isListBlank(orcamentos)) {
                   instanciarOrcamento();
                }

            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                createFacesErrorMessage(ex.getCause().getMessage());
                scrollTopMessage();
            }
        }
        
        this.selectItemUtils = new SelectItemUtils();

    }

    /**
     * Evento invocado pela tela de form para fazer download do arquivo
     *
     */
    public void baixarArquivoOrcamento() {
        baixarArquivoOrcamento(entity);
    }

    /**
     * Evento invocado pela tela de form para fazer download do arquivo
     *
     * @param orcamentoEvento
     */
    public void baixarArquivoOrcamento(OrcamentoEvento orcamentoEvento) {
        try {
            ArquivoUtils.carregarArquivo(orcamentoEvento.getArquivo());
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getCause().getMessage());
        }
    }

    /**
     * Evento invocado pela tela de form para remover download do arquivo
     */
    public void removerArquivoOrcamento() {
        try {
            entity.setArquivo(null);
            createFacesInfoMessage("Anexo removido, clique em salvar para concluir a remoção");
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getMessage());
        }
    }

    /**
     * Evento ao selecionar um pdf na tela de form
     *
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        if (file != null && file.getSize() > 0) {
            entity.setArquivo(new Arquivo(file.getFileName(), file.getContentType(), file.getContents()));
            createFacesInfoMessage("Arquivo anexado com sucesso");
        }
    }

    /**
     * Evento vindo da tela de form, quando seleciona um modelo de proposta
     */
    public void carregarDadosProposta() {
        try {
            entity = service.carregarPropostaModelo(entity, entity.getModeloProposta());
        } catch (Exception ex) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        }
    }

    /**
     * Método invocado pelo botão de criar nova proposta na tela de form
     */
    public void instanciarOrcamento() {
        entity = new OrcamentoEvento(contatoEvento);
        abrirFormOrcamento = true;
    }

    /**
     * Evento invocado pela tela de form para salvar no banco um novo orÃ§amento
     * e depois recarregar todos as propostas
     *
     */
    public synchronized void saveOrcamento() {
        try {
            service.save(entity);
            orcamentos = service.findAllByContatoId(contatoEvento.getId());
            entity = null;
            abrirFormOrcamento = false;
            createFacesInfoMessage("Orçamento gravado com sucesso!");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
    }

    /**
     * MÃ©todo invocado da tela de form para visualizar o orÃ§amento
     *
     * @param orcamento
     */
    public void visualizarOrcamento(OrcamentoEvento orcamento) {
        setEntity(orcamento);
        abrirFormOrcamento = true;
    }

    /**
     * Método invocado pelo botÃ£o de criar evento na tela de form
     *
     * @param orcamentoEvento
     */
    public void criarEvento(OrcamentoEvento orcamentoEvento) {
        try {
            service.criarEvento(orcamentoEvento);
            createFacesInfoMessage("Evento criado com sucesso!");
        } catch (Exception ex) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível criar o evento, entre em contato com o suporte do sistema: "+ex.getCause().getMessage());
        }
    }

    /**
     * MÃ©todo invocado da tela de form para aprovar um orÃ§amento e recarregar
     * a listagem de propostas
     *
     * @param orcamento
     */
    public void aceitarProposta(OrcamentoEvento orcamento) {
        try {
            service.aceitarProposta(orcamento);
            orcamentos = service.findAllByContatoId(contatoEvento.getId());
            createFacesInfoMessage("Orçamento aprovado com sucesso!");
        } catch (Exception ex) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getCause().getMessage());
        }
    }

    /**
     * MÃ©todo invocado da tela de form para aprovar um orÃ§amento e recarregar
     * a listagem de propostas
     *
     * @param orcamento
     */
    public void cancelarProposta(OrcamentoEvento orcamento) {
        try {
            service.cancelarProposta(orcamento);

            orcamentos = service.findAllByContatoId(contatoEvento.getId());

            createFacesInfoMessage("Orçamento cancelado com sucesso!");
        } catch (Exception ex) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Evento invocado pela tela de form para enviar email
     *
     * @param proposta
     */
    public synchronized void enviarPropostaEmail(OrcamentoEvento proposta) {
        try {
            service.enviarOrcamentoEmail(proposta);
            createFacesInfoMessage("Proposta enviada com sucesso!");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível enviar o email com a proposta: "+ex.getCause().getMessage());
        } finally {
            scrollTopMessage();
        }
    }

    /**
     * Evento invocado pela tela de form para remover um orÃ§amento
     *
     */
    public void deleteOrcamento() {
        try {
            service.delete(entity);

            orcamentos = service.findAllByContatoId(contatoEvento.getId());

            entity = null;

            createFacesInfoMessage("Orçamento removido com sucesso!");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível remover o orçamento: "+ex.getCause().getMessage());
        } finally {
            scrollTopMessage();
        }
    }

    /**
     * MÃ©todo invocado da tela de form para editar um valor
     */
    public void alterarValorProposta() {
        entity.setValorAlterado(entity.getValorProposta());
    }

    /**
     * MÃ©todo invocado da tela de form cancelar ediÃ§Ã£o de um valor
     */
    public void cancelarAlterarValorProposta() {
        entity.setValorAlterado(-1);
    }
    
    
    public List<SelectItem> getComboModelosProposta() {
        return selectItemUtils.getComboModelosProposta();
    }
    
    

    public Long getIdContato() {
        return idContato;
    }

    public void setIdContato(Long idContato) {
        this.idContato = idContato;
    }

    public OrcamentoEvento getEntity() {
        return entity;
    }

    public void setEntity(OrcamentoEvento entity) {
        this.entity = entity;
    }

    public ContatoEvento getContatoEvento() {
        return contatoEvento;
    }

    public void setContatoEvento(ContatoEvento contatoEvento) {
        this.contatoEvento = contatoEvento;
    }

    public List<OrcamentoEvento> getOrcamentos() {
        return orcamentos;
    }

    public void setOrcamentos(List<OrcamentoEvento> orcamentos) {
        this.orcamentos = orcamentos;
    }

    public boolean isAbrirFormOrcamento() {
        return abrirFormOrcamento;
    }

    public void setAbrirFormOrcamento(boolean abrirFormOrcamento) {
        this.abrirFormOrcamento = abrirFormOrcamento;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    
}
