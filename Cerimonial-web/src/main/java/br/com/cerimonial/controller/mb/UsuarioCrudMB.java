/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.AbstractFilter;
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
import javax.faces.context.FacesContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "UsuarioCrudMB")
@ViewScoped
public class UsuarioCrudMB extends BasicControl {

    private Usuario entity;
    private List<Usuario> usuariosSelecionados;
    private String confirmarSenha;
    @EJB
    private UsuarioService usuarioService;
    private LazyDataModel<Usuario> lazyLista;
    private AbstractFilter filtros;
    private Long id;

    public UsuarioCrudMB() {

        try {
            filtros = new FilterUsuario();
        } catch (Exception e) {
            Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Evento invocado ao abrir o xhtml na edição de um usuário objetivo de
     * carregar os dados do usuario
     */
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

    /**
     * Evento invocado pela tela de listagem para remover os itens selecionados
     */
    public void delete() {
        if (entity != null && entity.getId() != null) {
            try {
                usuarioService.delete(entity);
                createFacesInfoMessage("Usuário removido com sucesso!");
            } catch (Exception ex) {
                Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
                createFacesErrorMessage(ex.getMessage());
            }finally{
                scrollTopMessage();
            }
        }
    }

    /**
     * Evento invocado pela tela de form para salvar um novo ou edicao de um
     * usuario
     *
     * @return
     */
    public synchronized String save() {
        try {

            if (entity != null) {
                if (entity.getId() == null) {
                    if (!entity.getSenha().equals(confirmarSenha)) {
                        createFacesErrorMessage("As senhas não estão iguais, favor verificar");
                        return null;
                    }
                }
                usuarioService.save(entity);
                createFacesInfoMessage("Dados gravados com sucesso!");
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                return "index.xhtml?faces-redirect=true";
            }
        } catch (Exception ex) {
            Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage("form");
        }
        return null;
    }

    public synchronized String alterarSenha() {
        try {
            usuarioService.alterarSenha(entity);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * Evento invocado pela tela de index para listar os usuarios
     *
     * @return
     */
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

                    String sortAscDesc = "ASC";
                    if (sortField != null) {
                        sortAscDesc = SortOrder.ASCENDING == sortOrder ? "ASC" : "DESC";
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

    public AbstractFilter getFiltros() {
        return filtros;
    }

}
