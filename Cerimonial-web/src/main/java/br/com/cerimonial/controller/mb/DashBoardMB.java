/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Login;
import br.com.cerimonial.entity.Parcela;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.service.AlertaDestinatarioService;
import br.com.cerimonial.service.ContatoEventoService;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.LoginService;
import br.com.cerimonial.service.ParcelaService;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "DashBoardMB")
@ViewScoped
public class DashBoardMB extends BasicControl {

    @EJB
    protected LoginService loginService;
    @EJB
    protected EventoService eventoService;
    @EJB
    protected ContatoEventoService contatoEventoService;
    @EJB
    protected AlertaDestinatarioService alertaDestinatarioService;
    @EJB
    protected ParcelaService parcelaService;

    private List<Login> ultimosLogins;
    private List<Evento> eventosDia;
    private List<Evento> eventosAtivos;
    private List<ContatoEvento> contatosAtivos;
    //Financeiro Evento
    private List<Parcela> contasPagarEvento;
    private List<Parcela> contasVencidasEvento;
    //Financeiro Empresa
    private List<Parcela> contasPagarEmpresa;
    private List<Parcela> contasReceberEmpresa;
    private List<Parcela> contasVencidasEmpresa;

    private Date dataSelecionada;

    public DashBoardMB() {
        dataSelecionada = new Date();
    }

    @PostConstruct
    public void postConstruct() {
        carregarEventosDoDia();
        carregarEventosAtivos();
        carregarContatosAtivos();
        carregarFinanceiroEventos();
        carregarFinanceiroEmpresa();
//        carregarLogins();
    }

    public void carregarFinanceiroEventos() {

        contasPagarEvento = parcelaService.findParcelasAPagarEvento(10);

        contasVencidasEvento = parcelaService.findParcelasVencidasEvento(10);

    }

    public void carregarFinanceiroEmpresa() {

        contasPagarEmpresa = parcelaService.findParcelasAPagarEmpresa(10);

        contasReceberEmpresa = parcelaService.findParcelasAReceberEmpresa(10);

        contasVencidasEmpresa = parcelaService.findDespesasVencidasEmpresa(10);

    }

    public void carregarEventosDoDia() {
        try {

            eventosDia = eventoService.findEventosDia(dataSelecionada);
            
        } catch (GenericException e) {
            createFacesErrorMessage(e.getCause().getMessage());
        }
    }

    public void carregarEventosAtivos() {
        eventosAtivos = eventoService.findEventosAtivos(10);
    }

    public void carregarContatosAtivos() {
        contatosAtivos = contatoEventoService.findContatosAtivos(10);
    }

    public void carregarLogins() {
        ultimosLogins = loginService.findRangeListagem(null, 4, 0, null, null);
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

    public List<Parcela> getContasPagarEvento() {
        return contasPagarEvento;
    }

    public void setContasPagarEvento(List<Parcela> contasPagarEvento) {
        this.contasPagarEvento = contasPagarEvento;
    }

    public List<Parcela> getContasVencidasEvento() {
        return contasVencidasEvento;
    }

    public void setContasVencidasEvento(List<Parcela> contasVencidasEvento) {
        this.contasVencidasEvento = contasVencidasEvento;
    }

    public List<Parcela> getContasPagarEmpresa() {
        return contasPagarEmpresa;
    }

    public void setContasPagarEmpresa(List<Parcela> contasPagarEmpresa) {
        this.contasPagarEmpresa = contasPagarEmpresa;
    }

    public List<Parcela> getContasReceberEmpresa() {
        return contasReceberEmpresa;
    }

    public void setContasReceberEmpresa(List<Parcela> contasReceberEmpresa) {
        this.contasReceberEmpresa = contasReceberEmpresa;
    }

    public List<Parcela> getContasVencidasEmpresa() {
        return contasVencidasEmpresa;
    }

    public void setContasVencidasEmpresa(List<Parcela> contasVencidasEmpresa) {
        this.contasVencidasEmpresa = contasVencidasEmpresa;
    }

}
