/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.AbstractFilter;
import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.controller.filter.FilterPessoa;
import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.service.EnderecoService;
import br.com.cerimonial.service.PessoaService;
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
@ManagedBean(name = "PessoaMB")
@ViewScoped
public class PessoaMB extends BasicControl {

    @EJB
    protected PessoaService service;
    @EJB
    protected EnderecoService enderecoService;
    
    
    protected LazyDataModel<Pessoa> lazyLista;
    protected AbstractFilter filtros;
    protected Long id;
    protected Pessoa entity;
    protected List<Pessoa> itensSelecionados;
    
    
    protected final SelectItemUtils selectItemUtils;
    

    public PessoaMB() {
        this.selectItemUtils = new SelectItemUtils();
        filtros = new FilterPessoa();
    }

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
            entity = new Pessoa();
        }
    }
    
    
    /**
     * Evento invocado pela tela de form para salvar um novo ou edicao de um
     * cliente
     *
     * @return
     */
    public synchronized String save() {
        try {
            service.save(entity);
            createFacesInfoMessage("Dados gravados com sucesso!");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "index.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }

    /**
     * Evento invocado pela tela de listagem para remover os itens selecionados
     */
    public void delete() {
        try {
            
            service.delete(entity);
            createFacesInfoMessage("Dados removidos com sucesso!");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public void buscaCep() {
        if (entity != null && entity.getEndereco() != null) {
            try {
                entity.setEndereco(enderecoService.buscaCep(entity.getEndereco()));
            } catch (Exception ex) {
                Logger.getLogger(EmpresaCrudMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    /**
     * Evento invocado pela tela de index para listar os clientes
     *
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

                    int count = service.countListagem(filtros.getFilter());
                    this.setRowCount(count);

                    String sortAscDesc = "ASC";
                    if (sortField != null) {
                        sortAscDesc = SortOrder.ASCENDING == sortOrder ? "ASC" : "DESC";
                    }

                    List<Pessoa> clientes = service.findRangeListagem(filtros.getFilter(), max, offset, sortField, sortAscDesc);
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
    

    public List<SelectItem> getComboCidade(Estado estado) {
        return selectItemUtils.getComboCidade(estado);
    }

    public List<SelectItem> getComboEstado() {
        return selectItemUtils.getComboEstado();
    }

    public List<SelectItem> getComboTipoPessoa() {
        return SelectItemUtils.getComboTipoPessoa();
    }

    public Pessoa getEntity() {
        return entity;
    }

    public void setEntity(Pessoa entity) {
        this.entity = entity;
    }

    public List<Pessoa> getItensSelecionados() {
        return itensSelecionados;
    }

    public void setItensSelecionados(List<Pessoa> itensSelecionados) {
        this.itensSelecionados = itensSelecionados;
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
