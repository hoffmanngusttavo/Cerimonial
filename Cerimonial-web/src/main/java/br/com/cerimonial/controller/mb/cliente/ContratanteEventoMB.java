/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

import br.com.cerimonial.controller.mb.EmpresaCrudMB;
import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.service.EnderecoService;
import br.com.cerimonial.service.PessoaService;
import br.com.cerimonial.utils.SelectItemUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "ContratanteEventoMB")
@ViewScoped
public class ContratanteEventoMB extends ClienteControl {

    protected Long idEvento;
    protected Pessoa contratante;

    @EJB
    protected PessoaService service;
    @EJB
    protected EnderecoService enderecoService;

    protected final SelectItemUtils selectItemUtils;

    public ContratanteEventoMB() {
        this.selectItemUtils = new SelectItemUtils();
    }

    /**
     * Evento invocado ao abrir o xhtml do contratante
     */
    public void init() {

        try {

            contratante = service.getContratanteEvento(idEvento);

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar o contrante:" + ex.getCause().getMessage());
            scrollTopMessage();
        }
    }

    /**
     * Evento invocado pela tela de form para salvar contratante
     *
     * @return
     */
    public synchronized String save() {
        try {
            
            service.editCliente(contratante);
            
            createFacesInfoMessage("Dados gravados com sucesso!");
            
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            
            return "/intranet/cliente/evento/partials/dados-contratante.xhtml?idEvento=" + idEvento + "&faces-redirect=true";
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível salvar os dados: "+ex.getCause().getLocalizedMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }

    public void buscaCep() {
        if (contratante != null && contratante.getEndereco() != null) {
            try {
                contratante.setEndereco(enderecoService.buscaCep(contratante.getEndereco()));
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<SelectItem> getComboCidade(Estado estado) {
        return selectItemUtils.getComboCidade(estado);
    }

    public List<SelectItem> getComboEstado() {
        return selectItemUtils.getComboEstado();
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public Pessoa getContratante() {
        return contratante;
    }

    public void setContratante(Pessoa contratante) {
        this.contratante = contratante;
    }

}
