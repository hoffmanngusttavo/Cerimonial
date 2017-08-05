/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.ConfiguracaoEmail;
import br.com.cerimonial.service.ConfiguracaoEmailService;
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
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "ConfiguracaoEmailCrudMB")
@ViewScoped
public class ConfiguracaoEmailCrudMB extends BasicControl {

    private ConfiguracaoEmail entity;
    @EJB
    private ConfiguracaoEmailService service;

    /**
     * Evento invocado ao abrir o xhtml na edição de uma config. objetivo de
     * carregar os dados da config de email
     */
    public void init() {
        try {
            if (!entityLoaded) {
                entity = service.getConfiguracaoEmail();
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            entity = new ConfiguracaoEmail();
        }
    }

    /**
     * Evento invocado pela tela de form para salvar um novo ou edicao de uma
     * config. de email
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
     * Evento invocado selecionar um tipo de email
     */
    public void alterarConfiguracao(){
        entity = service.alterarConfiguracaoEmail(entity, entity.getTipoEmail());
    }
    
    
     public List<SelectItem> getComboTipoEmail() {
        return SelectItemUtils.getComboTipoEmail();
    }
    
    
    public ConfiguracaoEmail getEntity() {
        return entity;
    }

    public void setEntity(ConfiguracaoEmail entity) {
        this.entity = entity;
    }
    
    

}
