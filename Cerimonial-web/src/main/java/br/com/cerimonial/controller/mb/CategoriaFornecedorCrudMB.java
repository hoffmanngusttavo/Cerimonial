/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.CategoriaFornecedor;
import br.com.cerimonial.service.CategoriaFornecedorService;
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
@ManagedBean(name = "CategoriaFornecedorCrudMB")
@ViewScoped
public class CategoriaFornecedorCrudMB extends BasicControl {

    protected LazyDataModel<CategoriaFornecedor> lazyLista;
    protected Long id;
    protected CategoriaFornecedor entity;
    protected List<CategoriaFornecedor> itensSelecionados;
    @EJB
    protected CategoriaFornecedorService service;

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
            entity = new CategoriaFornecedor();
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
    public LazyDataModel<CategoriaFornecedor> getLazyDataModel() {

        if (lazyLista == null) {
            lazyLista = new LazyDataModel<CategoriaFornecedor>() {

                @Override
                public CategoriaFornecedor getRowData(String rowKey) {
                    List<CategoriaFornecedor> list = (List<CategoriaFornecedor>) getWrappedData();
                    for (CategoriaFornecedor cli : list) {
                        if (cli.getId().toString().equals(rowKey)) {
                            return cli;
                        }
                    }
                    return null;
                }

                @Override
                public Object getRowKey(CategoriaFornecedor object) {
                    return object.getId(); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public List<CategoriaFornecedor> load(int offset, int max, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                    int count = service.countAll();
                    this.setRowCount(count);

                    String sortAscDesc = "ASC";
                    if (sortField != null) {
                        sortAscDesc = SortOrder.ASCENDING == sortOrder ? "ASC" : "DESC";
                    }

                    List<CategoriaFornecedor> clientes = service.findRangeListagem(max, offset, sortField, sortAscDesc);
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

    public CategoriaFornecedor getEntity() {
        return entity;
    }

    public void setEntity(CategoriaFornecedor entity) {
        this.entity = entity;
    }

    public List<CategoriaFornecedor> getItensSelecionados() {
        return itensSelecionados;
    }

    public void setItensSelecionados(List<CategoriaFornecedor> itensSelecionados) {
        this.itensSelecionados = itensSelecionados;
    }

}
