/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.AbstractFilter;
import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.controller.filter.FilterFornecedor;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.service.PessoaService;
import br.com.cerimonial.utils.CerimonialUtils;
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
@ManagedBean(name = "FornecedorCrudMB")
@ViewScoped
public class FornecedorCrudMB extends BasicControl{
    
    private Pessoa entity;
    private List<Pessoa> fornecedoresSelecionados;
    @EJB
    private PessoaService service;
    private LazyDataModel<Pessoa> lazyLista;
    private AbstractFilter filtros;
    private Long id;

    public FornecedorCrudMB() {
        try {
            filtros = new FilterFornecedor();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
    /**
     *Evento invocado ao abrir o xhtml na edição de um cliente
     * objetivo de carregar os dados do cliente
     */
    public void init() {

        if (id != null) {
            try {
                entity = service.getEntity(id);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            entity = new Pessoa();
        }
    }
    
     /**
     *Evento invocado pela tela de listagem para remover os itens selecionados
     */
    public void delete() {

        if (CerimonialUtils.isListBlank(fornecedoresSelecionados)) {
            createFacesWarnMessage("Selecione ao menos um item");
            return;
        }

        int numCars = 0;
        if (fornecedoresSelecionados != null) {
            for (Pessoa fornecedor : fornecedoresSelecionados) {
                try {
                    service.delete(fornecedor);
                    numCars++;
                } catch (Exception ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                    createFacesInfoMessage(ex.getMessage());
                }
            }
            
            fornecedoresSelecionados.clear();
            
            if(numCars > 0){
                createFacesInfoMessage(numCars + " fornecedores removidos com sucesso!");
            }
        }
    }
    
     /**
     *Evento invocado pela tela de form para salvar um novo ou edicao de um fornecedor
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
     *Evento invocado pela tela de index para listar os clientes
     * @return 
     */
    public LazyDataModel<Pessoa> getLazyDataModel() {

        if (lazyLista == null) {
            lazyLista = new LazyDataModel<Pessoa>() {

                @Override
                public Pessoa getRowData(String rowKey) {
                    List<Pessoa> list = (List<Pessoa>) getWrappedData();
                    for (Pessoa cli : list) {
                        if (cli.getId().toString().equals(rowKey)) {
                            return cli;
                        }
                    }
                    return null;
                }

                @Override
                public Object getRowKey(Pessoa object) {
                    return object.getId(); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public List<Pessoa> load(int offset, int max, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                    int count = service.countListagemFornecedor(filtros.getFilter());
                    this.setRowCount(count);

                    String sortAscDesc = "ASC";
                    if (sortField != null) {
                        sortAscDesc = SortOrder.ASCENDING == sortOrder ? "ASC" : "DESC";
                    }

                    List<Pessoa> clientes = service.findRangeListagemFornecedor(filtros.getFilter(), max, offset, sortField, sortAscDesc);
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

    public Pessoa getEntity() {
        return entity;
    }

    public void setEntity(Pessoa entity) {
        this.entity = entity;
    }

    public List<Pessoa> getFornecedoresSelecionados() {
        return fornecedoresSelecionados;
    }

    public void setFornecedoresSelecionados(List<Pessoa> fornecedoresSelecionados) {
        this.fornecedoresSelecionados = fornecedoresSelecionados;
    }
    
    public AbstractFilter getFiltros() {
        return filtros;
    }

    public void setFiltros(AbstractFilter filtros) {
        this.filtros = filtros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    
    
}