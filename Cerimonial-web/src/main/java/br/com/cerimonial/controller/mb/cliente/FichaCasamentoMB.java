/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

import br.com.cerimonial.entity.ContatoEnvolvido;
import br.com.cerimonial.entity.Endereco;
import br.com.cerimonial.entity.EnvolvidoEvento;
import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.enums.TipoEnvolvidoEvento;
import br.com.cerimonial.service.EnderecoService;
import br.com.cerimonial.service.EnvolvidoEventoService;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.utils.CerimonialUtils;
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
@ManagedBean(name = "FichaCasamentoMB")
@ViewScoped
public class FichaCasamentoMB extends ClienteControl {

    protected Long idEvento;
    
    protected Integer tipoEnvolvido;

    protected Evento evento;

    protected EnvolvidoEvento envolvido;
    
    protected ContatoEnvolvido contato;

    @EJB
    protected EventoService eventoService;
    
    @EJB
    protected EnvolvidoEventoService envolvidoEventoService;

    @EJB
    protected EnderecoService enderecoService;

    protected final SelectItemUtils selectItemUtils;

    public FichaCasamentoMB() {
        this.selectItemUtils = new SelectItemUtils();
    }

    /**
     * Evento invocado ao abrir o xhtml de ficha cadastral
     */
    public void initEvento() {
        try {

            evento = eventoService.getEventoCasamento(idEvento, cliente);

            if (evento != null) {

                if (CerimonialUtils.isListNotBlank(evento.getEnvolvidos())) {

                    envolvido = evento.getTipoEnvolvidoEvento(TipoEnvolvidoEvento.getTipoByCode(tipoEnvolvido));

                }

            }

            if (envolvido == null) {
                envolvido = new EnvolvidoEvento(evento, TipoEnvolvidoEvento.getTipoByCode(tipoEnvolvido));
            }

            if(envolvido.getEndereco() == null){
                envolvido.setEndereco(new Endereco());
            }
            
            

        } catch (Exception ex) {
            envolvido = new EnvolvidoEvento();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar o evento: " + ex.getCause().getMessage());
        }
    }

    /**
     * Evento invocado pela tela de form para salvar um evento
     *
     * @return
     */
    public synchronized String save() {
        try {

            envolvidoEventoService.save(envolvido);

            createFacesInfoMessage("Dados gravados com sucesso!");

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            return "/intranet/cliente/evento/partials/cadastro-casamento.xhtml?idEvento=" + idEvento + "&tipoEnvolvido="+tipoEnvolvido+"&faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível salvar o cadastro dos noivos " + ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }

    public void buscaCepNoivo() {
        try {

            envolvido.setEndereco(enderecoService.buscaCep(envolvido.getEndereco()));

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void salvarContato() {
        try {

            envolvido.adicionarNovoContato(contato);

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void criarNovoContato() {
        try {

            contato = new ContatoEnvolvido();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removerContatoNoivo() {
        try {

            envolvido.removerContato(Integer.parseInt("posicaoContatoNoivo"));

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<SelectItem> getComboCidade(Estado estado) {
        return selectItemUtils.getComboCidade(estado);
    }

    public List<SelectItem> getComboEstado() {
        return selectItemUtils.getComboEstado();
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public EnvolvidoEvento getEnvolvido() {
        return envolvido;
    }

    public void setEnvolvido(EnvolvidoEvento envolvido) {
        this.envolvido = envolvido;
    }

    public Integer getTipoEnvolvido() {
        return tipoEnvolvido;
    }

    public void setTipoEnvolvido(Integer tipoEnvolvido) {
        this.tipoEnvolvido = tipoEnvolvido;
    }

    public ContatoEnvolvido getContato() {
        return contato;
    }

    public void setContato(ContatoEnvolvido contato) {
        this.contato = contato;
    }

    

}
