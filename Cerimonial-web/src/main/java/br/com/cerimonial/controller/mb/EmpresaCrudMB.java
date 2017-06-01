/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Empresa;
import br.com.cerimonial.service.EmpresaService;
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
@ManagedBean(name = "EmpresaCrudMB")
@ViewScoped
public class EmpresaCrudMB extends BasicControl {

    private Empresa entity;
    @EJB
    private EmpresaService service;

    public EmpresaCrudMB() {
        init();
    }

    /**
     * Evento invocado ao abrir o xhtml na edição de uma empresa objetivo de
     * carregar os dados da empresa
     */
    public void init() {
        try {
//            entity = service.getEntity(null);
            entity = new Empresa();
        } catch (Exception ex) {
            Logger.getLogger(UsuarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /**
     *Evento invocado pela tela de form para salvar um novo ou edicao de uma empresa
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

    public Empresa getEntity() {
        return entity;
    }

    public void setEntity(Empresa entity) {
        this.entity = entity;
    }
    
    public List<SelectItem> getComboTipoPessoa(){
        return SelectItemUtils.getComboTipoPessoa();
    }

}
