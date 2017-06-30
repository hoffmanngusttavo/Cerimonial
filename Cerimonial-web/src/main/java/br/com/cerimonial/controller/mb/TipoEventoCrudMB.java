/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.entity.TipoEvento;
import br.com.cerimonial.service.TipoEventoService;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "TipoEventoCrudMB")
@ViewScoped
public class TipoEventoCrudMB extends BasicControl{
    
    @EJB
    private TipoEventoService service;
    
    private TipoEvento entity;
    
    private LazyDataModel<TipoEvento> lazyLista;

    public TipoEventoCrudMB() {
    }

   

    public TipoEvento getEntity() {
        return entity;
    }

    public void setEntity(TipoEvento entity) {
        this.entity = entity;
    }
    
    
    
}
