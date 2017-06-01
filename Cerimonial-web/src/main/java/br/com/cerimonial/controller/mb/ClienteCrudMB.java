/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.AbstractFilter;
import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Cliente;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.service.ClienteService;
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
@ManagedBean(name = "ClienteCrudMB")
@ViewScoped
public class ClienteCrudMB extends BasicControl{
    
    private Cliente entity;
    private List<Cliente> clientesSelecionados;
    @EJB
    private ClienteService service;
    private LazyDataModel<Cliente> lazyLista;
    private AbstractFilter filtros;
    private Long id;

    public ClienteCrudMB() {
        
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
                Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            entity = new Cliente();
        }
    }

    
    /**
     *Evento invocado pela tela de listagem para remover os itens selecionados
     */
    public void delete() {

        if (CerimonialUtils.isListBlank(clientesSelecionados)) {
            createFacesWarnMessage("Selecione ao menos um item");
            return;
        }

        int numCars = 0;
        if (clientesSelecionados != null) {
            for (Cliente cliente : clientesSelecionados) {
                try {
                    service.delete(cliente);
                    numCars++;
                } catch (Exception ex) {
                    Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
                    createFacesInfoMessage(ex.getMessage());
                }
            }
            
            clientesSelecionados.clear();
            
            if(numCars > 0){
                createFacesInfoMessage(numCars + " clientes removidos com sucesso!");
            }
        }
    }

    
    /**
     *Evento invocado pela tela de form para salvar um novo ou edicao de um cliente
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
            Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
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
    public LazyDataModel<Cliente> getLazyDataModel() {

        if (lazyLista == null) {
            lazyLista = new LazyDataModel<Cliente>() {

                @Override
                public Cliente getRowData(String rowKey) {
                    List<Cliente> list = (List<Cliente>) getWrappedData();
                    for (Cliente cli : list) {
                        if (cli.getId().toString().equals(rowKey)) {
                            return cli;
                        }
                    }
                    return null;
                }

                @Override
                public Object getRowKey(Cliente object) {
                    return object.getId(); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public List<Cliente> load(int offset, int max, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                    int count = service.countListagem(filtros.getFilter());
                    this.setRowCount(count);

                    String sortAscDesc = "ASC";
                    if (sortField != null) {
                        sortAscDesc = SortOrder.ASCENDING == sortOrder ? "ASC" : "DESC";
                    }

                    List<Cliente> clientes = service.findRangeListagem(filtros.getFilter(), max, offset, sortField, sortAscDesc);
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

    public Cliente getEntity() {
        return entity;
    }

    public void setEntity(Cliente entity) {
        this.entity = entity;
    }

    public List<Cliente> getClientesSelecionados() {
        return clientesSelecionados;
    }

    public void setClientesSelecionados(List<Cliente> clientesSelecionados) {
        this.clientesSelecionados = clientesSelecionados;
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
