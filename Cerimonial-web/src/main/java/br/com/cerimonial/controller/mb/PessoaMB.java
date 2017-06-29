/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.AbstractFilter;
import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.service.EnderecoService;
import br.com.cerimonial.service.PessoaService;
import br.com.cerimonial.utils.SelectItemUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Gustavo Hoffmann
 */
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
     * Evento invocado pela tela de listagem para remover os itens selecionados
     */
    public void delete() {
        if (entity != null && entity.getId() != null) {
            try {
                String tipoPessoa = entity.getTipoEnvolvido().toString();
                service.delete(entity);
                createFacesInfoMessage(tipoPessoa+" removido com sucesso!");
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                createFacesErrorMessage(ex.getMessage());
            }
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
