/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.service.ModeloPropostaService;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "ModeloPropostaCrudMB")
@ViewScoped
public class ModeloPropostaCrudMB extends BasicControl{
    
    private LazyDataModel<ModeloProposta> lazyLista;
    private ModeloProposta entity;
    
    @EJB
    private ModeloPropostaService service;

    public ModeloPropostaCrudMB() {
    }

    public ModeloProposta getEntity() {
        return entity;
    }

    public void setEntity(ModeloProposta entity) {
        this.entity = entity;
    }
    
    
    
}
