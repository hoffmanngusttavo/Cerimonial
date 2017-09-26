/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.AbstractFilter;
import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.TipoIndicacao;
import br.com.cerimonial.service.ContatoEventoService;
import br.com.cerimonial.service.OrcamentoEventoService;
import br.com.cerimonial.service.TipoIndicacaoService;
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
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

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

    protected AbstractFilter filtros;
    protected SelectItemUtils selectItemUtils;

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
     * Evento invocado pela tela de listagem para remover os itens selecionados
     */
    public void delete() {
        try {
            service.delete(entity);
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
            service.save(entity);
            createFacesInfoMessage("Dados gravados com sucesso!");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "/intranet/admin/operacional/pre-evento/form.xhtml?id=" + entity.getId() + "&faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
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

    public AbstractFilter getFiltros() {
        return filtros;
    }

    public void setFiltros(AbstractFilter filtros) {
        this.filtros = filtros;
    }

}
