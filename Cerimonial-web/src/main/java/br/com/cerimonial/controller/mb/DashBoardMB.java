/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.AlertaDestinatario;
import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Login;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.service.AlertaDestinatarioService;
import br.com.cerimonial.service.ContatoEventoService;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.LoginService;
import java.util.Date;
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
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "DashBoardMB")
@ViewScoped
public class DashBoardMB extends BasicControl{
    
    @EJB
    protected LoginService loginService;
    @EJB
    protected EventoService eventoService;
    @EJB
    protected ContatoEventoService contatoEventoService;
    @EJB
    protected AlertaDestinatarioService alertaDestinatarioService;
    
    private List<Login> ultimosLogins;
    private List<Evento> eventosDia;
    private List<Evento> eventosAtivos;
    private List<ContatoEvento> contatosAtivos;
    private List<AlertaDestinatario> alertasDestinatario;
    
    private Date dataSelecionada;
    
    

    public DashBoardMB() {
        dataSelecionada = new Date();
    }
    
    @PostConstruct
    public void postConstruct(){
        carregarEventosDoDia();
        carregarEventosAtivos();
        carregarContatosAtivos();
        carregarAlertasUsuario();
        carregarLogins();
    }
    

    public void carregarLogins(){
        ultimosLogins = loginService.findRangeListagem(null, 4, 0, null, null);
    }
    
    public void carregarEventosDoDia(){
        eventosDia = eventoService.findEventosDia(dataSelecionada);
    }
    
    public void carregarEventosAtivos(){
        eventosAtivos = eventoService.findEventosAtivos(10);
    }
    
    public void carregarContatosAtivos(){
        contatosAtivos = contatoEventoService.findContatosAtivos(10);
    }
    
    /**
     * Carregar os 10 ultimos alertas que não foram visualizados do usuário logado
     */
    public void carregarAlertasUsuario(){
        try {
            Usuario user = (Usuario) SecurityUtils.getSubject().getPrincipal();
            alertasDestinatario = alertaDestinatarioService.findAlertasUsuarioNaoVisualizados(10, user);
        } catch (Exception ex) {
            Logger.getLogger(DashBoardMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
    
    //----------GETTERS AND SETTERS--------------------------------
    
    
    public List<Login> getUltimosLogins() {
        return ultimosLogins;
    }

    public void setUltimosLogins(List<Login> ultimosLogins) {
        this.ultimosLogins = ultimosLogins;
    }

    public Date getDataSelecionada() {
        return dataSelecionada;
    }

    public void setDataSelecionada(Date dataSelecionada) {
        this.dataSelecionada = dataSelecionada;
    }

    public List<Evento> getEventosDia() {
        return eventosDia;
    }

    public void setEventosDia(List<Evento> eventosDia) {
        this.eventosDia = eventosDia;
    }

    public List<Evento> getEventosAtivos() {
        return eventosAtivos;
    }

    public void setEventosAtivos(List<Evento> eventosAtivos) {
        this.eventosAtivos = eventosAtivos;
    }

    public List<ContatoEvento> getContatosAtivos() {
        return contatosAtivos;
    }

    public void setContatosAtivos(List<ContatoEvento> contatosAtivos) {
        this.contatosAtivos = contatosAtivos;
    }

    public List<AlertaDestinatario> getAlertasDestinatario() {
        return alertasDestinatario;
    }

    public void setAlertasDestinatario(List<AlertaDestinatario> alertasDestinatario) {
        this.alertasDestinatario = alertasDestinatario;
    }
    
    
    
    
}
