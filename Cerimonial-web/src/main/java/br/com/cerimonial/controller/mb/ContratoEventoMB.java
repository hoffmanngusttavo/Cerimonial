/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.ContratoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.service.ContratoEventoService;
import br.com.cerimonial.service.EventoService;
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
@ManagedBean(name = "ContratoEventoMB")
@ViewScoped
public class ContratoEventoMB extends BasicControl {

    /**
     * Id do Evento
     */
    protected Long id;
    protected ContratoEvento entity;

    @EJB
    protected ContratoEventoService service;

    @EJB
    protected EventoService eventoService;

    protected SelectItemUtils selectItemUtils;

    protected List<SelectItem> comboModeloContrato;

    /**
     * Evento invocado ao abrir o xhtml carregar os dados do contrato do evento
     */
    public void init() {

        this.selectItemUtils = new SelectItemUtils();

        if (id != null) {
            try {
                entity = service.getContratoByEvento(id);

                if (entity == null) {
                    Evento evento = eventoService.findEntityById(id);
                    entity = new ContratoEvento(evento);
                }

                postConstruct();

            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                createFacesErrorMessage(ex.getCause().getMessage());
                scrollTopMessage();
            }
        }

    }

    /**
     * Evento invocado ao abrir o xhtml de impressao do contrato
     */
    public void initImpressao() {

        if (id != null) {
            try {

                entity = service.findEntityById(id);

            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                createFacesErrorMessage(ex.getCause().getMessage());
                scrollTopMessage();
            }
        }

    }

    private void postConstruct() {
        try {

            comboModeloContrato = selectItemUtils.getComboModeloContratoByTipoEvento(entity.getEvento().getTipoEvento());

        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getCause().getMessage());
            scrollTopMessage();
        }

    }

    /**
     * Evento do combo após selecionar um modelo de contrato
     */
    public void carregarModeloContrato() {
        try {

            service.carregarContratoDeModelo(entity);

        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage("Não foi possível carregar o contrato: " + e.getCause().getMessage());
            scrollTopMessage();
        }
    }

    /**
     * Evento invocado pela tela de form para salvar um novo ou edicao de um
     * contato
     *
     * @return 
     */
    public synchronized String save() {
        try {
            
            service.save(entity);
            
            createFacesInfoMessage("Dados gravados com sucesso!");
            
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            return "/intranet/admin/operacional/pre-evento/partials/contrato.xhtml?idEvento=" + id + "&faces-redirect=true";

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
            return null;
        } finally {
            scrollTopMessage();
        }
    }

    /**
     * Evento invocado pela tela de contrato para liberar para o cliente
     *
     * @return 
     */
    public synchronized String liberarContrato() {
        try {

            service.liberarContrato(entity);

            createFacesInfoMessage("Dados gravados com sucesso!");

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            return "/intranet/admin/operacional/pre-evento/partials/contrato.xhtml?idEvento=" +id + "&faces-redirect=true";

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
            
            return null;
        } finally {
            scrollTopMessage();
        }
    }

    public List<SelectItem> getComboModeloContrato() {
        return comboModeloContrato;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContratoEvento getEntity() {
        return entity;
    }

    public void setEntity(ContratoEvento entity) {
        this.entity = entity;
    }

}
