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
import javax.annotation.PostConstruct;
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
    private Integer quantidadeAlertas = null;

    @EJB
    private AlertaDestinatarioService service;

    @PostConstruct
    public void init() {

        try {
            Usuario user = (Usuario) SecurityUtils.getSubject().getPrincipal();

            quantidadeAlertas = service.countAlertasUsuarioNaoVisualizados(user);

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<AlertaDestinatario> getLazyLista() {

        try {
            Usuario user = (Usuario) SecurityUtils.getSubject().getPrincipal();
            lazyLista = service.findAlertasUsuarioNaoVisualizados(Integer.MAX_VALUE, user);
        } catch (Exception ex) {
            Logger.getLogger(AlertaDestinatarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
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
        try {

            this.entity = service.alterarAlertaVisualizado(entity);

        } catch (Exception ex) {
            Logger.getLogger(AlertaDestinatarioCrudMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Integer getQuantidadeAlertas() {
        return quantidadeAlertas;
    }

    public void setQuantidadeAlertas(Integer quantidadeAlertas) {
        this.quantidadeAlertas = quantidadeAlertas;
    }
    
    
    

}
