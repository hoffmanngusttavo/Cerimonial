/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.controller.filter.FilterUsuario;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.service.UsuarioService;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "UsuarioCrudMB")
@ViewScoped
public class UsuarioCrudMB extends BasicControl{
    
    private Usuario entity;
    private List<Usuario> usuariosSelecionados;
    private String confirmarSenha;
    @EJB
    private UsuarioService usuarioService;
    private LazyDataModel<Usuario> lazyLista;
    private FilterUsuario filtros;
    private Long id;

    public UsuarioCrudMB() {
        
        try {
            filtros = (FilterUsuario) FilterUsuario.getFilter(FilterUsuario.class);
        } catch (Exception e) {
            Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, e);
            filtros = new FilterUsuario();
        }
        
    }
    
    public void init() {
        
        if (id != null) {
            try {
                entity = usuarioService.getEntity(id);
            } catch (Exception ex) {
                Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            entity = new Usuario();
        }
    }
    
    public void delete() {
        int numCars = 0;
        if (usuariosSelecionados != null) {
            for (Usuario selectedCar : usuariosSelecionados) {
                try {
                    usuarioService.delete(selectedCar);
                    numCars++;
                } catch (Exception ex) {
                    Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            usuariosSelecionados.clear();
            createFacesInfoMessage(numCars + " usuários removidos com sucesso!");
        }
    }
    
    
    public synchronized String save()  {
        try {
            usuarioService.save(entity);
            createFacesInfoMessage("Dados gravados com sucesso!");
            return "index.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        }finally{
            scrollTopMessage();
        }
        return null;
    }
    
    public synchronized String alterarSenha()  {
        try {
            usuarioService.alterarSenha(entity);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public LazyDataModel<Usuario> getLazyDataModel() {

        if (lazyLista == null) {
            lazyLista = new LazyDataModel<Usuario>() {

                @Override
                public Usuario getRowData(String rowKey) {
                    List<Usuario> list = (List<Usuario>) getWrappedData();
                    for (Usuario cli : list) {
                        if (cli.getId().toString().equals(rowKey)) {
                            return cli;
                        }
                    }
                    return null;
                }

                @Override
                public Object getRowKey(Usuario object) {
                    return object.getId(); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public List<Usuario> load(int offset, int max, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                    int count = usuarioService.countListagem(filtros.getFilter());
                    this.setRowCount(count);
                    
                    String sortAscDesc= "ASC";
                    if (sortField != null) {
                        sortAscDesc= SortOrder.ASCENDING == sortOrder ? "ASC" : "DESC";
                    }
                    
                    List<Usuario> clientes = usuarioService.findRangeListagem(filtros.getFilter(), max, offset, sortField, sortAscDesc);
                    return clientes;
                }
            };
            //
            int count = usuarioService.countAll();
            lazyLista.setRowCount(count);
            //
        }
        return lazyLista;
    }
    
    
    public Usuario getEntity() {
        return entity;
    }

    public void setEntity(Usuario entity) {
        this.entity = entity;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public List<Usuario> getUsuariosSelecionados() {
        return usuariosSelecionados;
    }

    public void setUsuariosSelecionados(List<Usuario> usuariosSelecionados) {
        this.usuariosSelecionados = usuariosSelecionados;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    
    
}
