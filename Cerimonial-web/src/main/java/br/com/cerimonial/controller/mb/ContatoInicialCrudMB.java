/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.AbstractFilter;
import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.entity.TipoIndicacao;
import br.com.cerimonial.service.ContatoEventoService;
import br.com.cerimonial.service.OrcamentoEventoService;
import br.com.cerimonial.service.TipoIndicacaoService;
import br.com.cerimonial.utils.ArquivoUtils;
import br.com.cerimonial.utils.SelectItemUtils;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "ContatoInicialCrudMB")
@ViewScoped
public class ContatoInicialCrudMB extends BasicControl {

    protected LazyDataModel<ContatoEvento> lazyLista;
    protected Long id;
    protected ContatoEvento entity;
    protected OrcamentoEvento orcamento;
    protected boolean abrirFormOrcamento = false;
    protected List<ContatoEvento> itensSelecionados;
    protected AbstractFilter filtros;
    protected SelectItemUtils selectItemUtils;
    protected UploadedFile file;

    @EJB
    protected ContatoEventoService service;
    @EJB
    protected OrcamentoEventoService orcamentoService;
    @EJB
    protected TipoIndicacaoService tipoIndicacaoService;

    /**
     * Evento invocado ao abrir o xhtml na ediÃ§Ã£o de um cliente objetivo de
     * carregar os dados do cliente
     */
    public void init() {

        if (id != null) {
            try {
                entity = service.getEntity(id);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            entity = new ContatoEvento();
        }

        this.selectItemUtils = new SelectItemUtils();
    }

    /**
     * MÃ©todo invocado pelo botÃ£o de criar nova proposta
     * na tela de form
     */
    public void instanciarOrcamento() {
        orcamento = new OrcamentoEvento(entity);
        abrirFormOrcamento = true;
    }
    
    /**
     * Método invocado pelo botÃ£o de criar evento
     * na tela de form
     * @param orcamentoEvento
     */
    public void criarEvento(OrcamentoEvento orcamentoEvento) {
        try {
            orcamentoService.criarEvento(orcamentoEvento);
        } catch (Exception ex) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Evento invocado pela tela de listagem para remover os itens selecionados
     */
    public void delete() {
        try {
            if (entity != null && entity.getId() != null) {
                service.delete(entity);
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        }
    }

    /**
     * Evento invocado pela tela de form para salvar um novo ou edicao de um
     * contato
     *
     * @return
     */
    public synchronized String save() {
        try {
            if (entity != null) {
                service.save(entity);
                createFacesInfoMessage("Dados gravados com sucesso!");
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                return "index.xhtml?faces-redirect=true";
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }

    /**
     * Evento invocado pela tela de form para salvar no banco um novo orÃ§amento
     * e depois recarregar todos as propostas
     *
     */
    public synchronized void saveOrcamento() {
        try {
            
            orcamentoService.save(orcamento);
            entity.setPropostas(orcamentoService.findAllByContatoId(entity.getId()));
            orcamento = null;
            abrirFormOrcamento = false;
            createFacesInfoMessage("OrÃ§amento gravado com sucesso!");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
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
            orcamentoService.delete(orcamento);
            entity.setPropostas(orcamentoService.findAllByContatoId(entity.getId()));
            orcamento = null;
            createFacesInfoMessage("OrÃ§amento removido com sucesso!");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
    }
    
    /**
     * MÃ©todo invocado da tela de form para editar um valor 
     */
    public void alterarValorProposta(){
        orcamento.setValorAlterado(orcamento.getValorProposta());
    }
    
     /**
     * MÃ©todo invocado da tela de form cancelar ediÃ§Ã£o de um valor 
     */
    public void cancelarAlterarValorProposta(){
        orcamento.setValorAlterado(-1);
    }
    
    /**
     * MÃ©todo invocado da tela de form para visualizar o orÃ§amento
     * @param orcamento
     */
    public void visualizarOrcamento(OrcamentoEvento orcamento) {
        setOrcamento(orcamento);
        abrirFormOrcamento = true;
    }
    
    /**
     * MÃ©todo invocado da tela de form para aprovar um orÃ§amento
     * e recarregar a listagem de propostas
     * @param orcamento
     */
    public void aceitarProposta(OrcamentoEvento orcamento) {
        try {
            orcamentoService.aceitarProposta(orcamento);
            entity.setPropostas(orcamentoService.findAllByContatoId(entity.getId()));
            createFacesInfoMessage("OrÃ§amento aprovado com sucesso!");
        } catch (Exception ex) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        }
    }
    
    /**
     * MÃ©todo invocado da tela de form para aprovar um orÃ§amento
     * e recarregar a listagem de propostas
     * @param orcamento
     */
    public void cancelarProposta(OrcamentoEvento orcamento) {
        try {
            orcamentoService.cancelarProposta(orcamento);
            entity.setPropostas(orcamentoService.findAllByContatoId(entity.getId()));
            createFacesInfoMessage("OrÃ§amento cancelado com sucesso!");
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
            orcamentoService.enviarOrcamentoEmail(proposta);
            createFacesInfoMessage("Proposta enviada com sucesso!");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
    }
    
    /**
     * Evento invocado pela tela de form para fazer download do arquivo
     *
     */
    public void baixarArquivoOrcamento() {
        try {
            ArquivoUtils.carregarArquivo(orcamento.getArquivo());
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getMessage());
        }
    }
    
    /**
     * Evento invocado pela tela de form para fazer download do arquivo
     *
     * @param orcamentoEvento
     */
    public void baixarArquivoOrcamento(OrcamentoEvento orcamentoEvento) {
        try {
            if(orcamentoEvento != null){
                ArquivoUtils.carregarArquivo(orcamentoEvento.getArquivo());
            }
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getMessage());
        }
    }

    /**
     * Evento invocado pela tela de form para remover download do arquivo
     */
    public void removerArquivoOrcamento() {
        try {
            orcamento.setArquivo(null);
            createFacesInfoMessage("Anexo removido, clique em salvar para concluir a remoÃ§Ã£o");
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getMessage());
        }
    }
    
    /**
     * Evento ao selecionar um pdf na tela de form
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        if (file != null && file.getSize() > 0) {
            orcamento.setArquivo(new Arquivo(file.getFileName(), file.getContentType(), file.getContents()));
        }
        createFacesInfoMessage("Arquivo anexado com sucesso");
    }
    

    /**
     * Evento invocado pela tela de index para listar os clientes
     *
     * @return
     */
    public LazyDataModel<ContatoEvento> getLazyDataModel() {

        if (lazyLista == null) {
            lazyLista = new LazyDataModel<ContatoEvento>() {

                @Override
                public ContatoEvento getRowData(String rowKey) {
                    List<ContatoEvento> list = (List<ContatoEvento>) getWrappedData();
                    for (ContatoEvento cli : list) {
                        if (cli.getId().toString().equals(rowKey)) {
                            return cli;
                        }
                    }
                    return null;
                }

                @Override
                public Object getRowKey(ContatoEvento object) {
                    return object.getId(); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public List<ContatoEvento> load(int offset, int max, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                    int count = service.countAll();
                    this.setRowCount(count);

                    String sortAscDesc = "ASC";
                    if (sortField != null) {
                        sortAscDesc = SortOrder.ASCENDING == sortOrder ? "ASC" : "DESC";
                    }

                    List<ContatoEvento> clientes = service.findRangeListagem(max, offset, sortField, sortAscDesc);
                    return clientes;
                }
            };
            //
            int count = service.countAll();
            lazyLista.setRowCount(count);
            //
        }
        return lazyLista;
    }

    /**
     *Evento vindo da tela de form, quando seleciona um modelo de proposta
     */
    public void carregarDadosProposta() {
        try {
            orcamento = orcamentoService.carregarPropostaModelo(orcamento, orcamento.getModeloProposta());
        } catch (Exception ex) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public List<SelectItem> getComboTipoEvento() {
        return selectItemUtils.getComboTipoEvento();
    }

    public List<SelectItem> getComboModelosProposta() {
        return selectItemUtils.getComboModelosProposta();
    }

    public List<SelectItem> getComboStatusContato() {
        return selectItemUtils.getComboStatusContato();
    }

    public List<String> completeLocalEvento(String value) {
        return service.getLocaisEvento(value);
    }

    public List<TipoIndicacao> completeTipoIndicacao(String value) {
        return tipoIndicacaoService.findTiposIndicacaoByNome(value);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContatoEvento getEntity() {
        return entity;
    }

    public void setEntity(ContatoEvento entity) {
        this.entity = entity;
    }

    public List<ContatoEvento> getItensSelecionados() {
        return itensSelecionados;
    }

    public void setItensSelecionados(List<ContatoEvento> itensSelecionados) {
        this.itensSelecionados = itensSelecionados;
    }

    public AbstractFilter getFiltros() {
        return filtros;
    }

    public void setFiltros(AbstractFilter filtros) {
        this.filtros = filtros;
    }

    public OrcamentoEvento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(OrcamentoEvento orcamento) {
        this.orcamento = orcamento;
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

    
    
}
