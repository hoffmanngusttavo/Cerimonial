/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.AlertaDestinatario;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.service.AlertaDestinatarioService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.shiro.SecurityUtils;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "AlertaDestinatarioCrudMB")
@ViewScoped
public class AlertaDestinatarioCrudMB extends BasicControl {

    private List<AlertaDestinatario> lazyLista;
    private Long id;
    private AlertaDestinatario entity;

    @EJB
    private AlertaDestinatarioService service;

    /**
     * Evento invocado ao abrir o alerta para visualizar
     */
    public void init() {

        if (id != null) {
            try {
                entity = service.getEntity(id);
                
                entity = service.alterarAlertaVisualizado(entity);
                
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public List<AlertaDestinatario> getLazyLista() {
        
        if(lazyLista == null){
            try {
                Usuario user = (Usuario) SecurityUtils.getSubject().getPrincipal();
                lazyLista = service.findAlertasUsuarioNaoVisualizados(Integer.MAX_VALUE, user);
            } catch (Exception ex) {
                Logger.getLogger(AlertaDestinatarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return lazyLista;
    }

    public void setLazyLista(List<AlertaDestinatario> lazyLista) {
        this.lazyLista = lazyLista;
    }


    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlertaDestinatario getEntity() {
        return entity;
    }

    public void setEntity(AlertaDestinatario entity) {
        this.entity = entity;
    }

}
