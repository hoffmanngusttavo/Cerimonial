/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.filter.FilterColaborador;
import br.com.cerimonial.entity.Pessoa;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "ColaboradorCrudMB")
@ViewScoped
public class ColaboradorCrudMB extends PessoaMB{

    public ColaboradorCrudMB() {
        try {
            filtros = new FilterColaborador();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
    
    
     /**
     * Evento invocado pela tela de form para salvar um novo ou edicao de um
     * colaborador
     *
     * @return
     */
    public synchronized String save() {
        try {
            if (entity != null) {
                service.saveColaborador(entity);
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
     *Evento invocado pela tela de index para listar os colaboradores
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

                    int count = service.countListagemColaborador(filtros.getFilter());
                    this.setRowCount(count);

                    String sortAscDesc = "ASC";
                    if (sortField != null) {
                        sortAscDesc = SortOrder.ASCENDING == sortOrder ? "ASC" : "DESC";
                    }

                    List<Pessoa> clientes = service.findRangeListagemColaborador(filtros.getFilter(), max, offset, sortField, sortAscDesc);
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
    
    
}
