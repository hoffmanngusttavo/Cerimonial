/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Servico;
import br.com.cerimonial.service.ServicoService;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "ServicoCrudMB")
@ViewScoped
public class ServicoCrudMB extends BasicControl {

    protected LazyDataModel<Servico> lazyLista;
    protected Long id;
    protected Servico entity;
    protected List<Servico> itensSelecionados;
    @EJB
    protected ServicoService service;

    /**
     * Evento invocado ao abrir o xhtml na edição de um cliente objetivo de
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
            entity = new Servico();
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
     * fornecedor
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
     * Evento invocado pela tela de index para listar os clientes
     *
     * @return
     */
    public LazyDataModel<Servico> getLazyDataModel() {

        if (lazyLista == null) {
            lazyLista = new LazyDataModel<Servico>() {

                @Override
                public Servico getRowData(String rowKey) {
                    List<Servico> list = (List<Servico>) getWrappedData();
                    for (Servico cli : list) {
                        if (cli.getId().toString().equals(rowKey)) {
                            return cli;
                        }
                    }
                    return null;
                }

                @Override
                public Object getRowKey(Servico object) {
                    return object.getId(); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public List<Servico> load(int offset, int max, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                    int count = service.countAll();
                    this.setRowCount(count);

                    String sortAscDesc = "ASC";
                    if (sortField != null) {
                        sortAscDesc = SortOrder.ASCENDING == sortOrder ? "ASC" : "DESC";
                    }

                    List<Servico> clientes = service.findRangeListagem(max, offset, sortField, sortAscDesc);
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Servico getEntity() {
        return entity;
    }

    public void setEntity(Servico entity) {
        this.entity = entity;
    }

    public List<Servico> getItensSelecionados() {
        return itensSelecionados;
    }

    public void setItensSelecionados(List<Servico> itensSelecionados) {
        this.itensSelecionados = itensSelecionados;
    }

}
