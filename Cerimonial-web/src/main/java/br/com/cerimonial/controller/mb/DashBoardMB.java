/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Login;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.LoginService;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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
    
    private List<Login> ultimosLogins;
    private List<Evento> eventosDia;
    
    private Date dataSelecionada;
    
    

    public DashBoardMB() {
        
    }

    public void carregarLogins(){
        ultimosLogins = loginService.findRangeListagem(null, 4, 0, null, null);
    }
    
    public void carregarEventosDoDia(){
        eventosDia = eventoService.findEventosDia(dataSelecionada);
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
    
    
    
    
}
