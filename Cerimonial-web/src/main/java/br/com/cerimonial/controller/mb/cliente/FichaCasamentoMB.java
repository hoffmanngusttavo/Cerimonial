/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

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

    protected Evento evento;

    protected EnvolvidoEvento noivo;

    protected EnvolvidoEvento noiva;

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

                    noivo = evento.getEnvolvidos().get(0);

                    if (evento.getEnvolvidos().size() > 1) {
                        noiva = evento.getEnvolvidos().get(1);
                    }

                }

            }

            if (noivo == null) {
                noivo = new EnvolvidoEvento(evento, TipoEnvolvidoEvento.NOIVO);
            }

            if (noiva == null) {
                noiva = new EnvolvidoEvento(evento, TipoEnvolvidoEvento.NOIVO);
            }
            
            if(noivo.getEndereco() == null){
                noivo.setEndereco(new Endereco());
            }
            
            if(noiva.getEndereco() == null){
                noiva.setEndereco(new Endereco());
            }

        } catch (Exception ex) {
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

            envolvidoEventoService.salvarNoivos(noivo, noiva);

            createFacesInfoMessage("Dados gravados com sucesso!");

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            return "/intranet/cliente/evento/partials/cadastro-casamento.xhtml?idEvento=" + idEvento + "&faces-redirect=true";
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

            noivo.setEndereco(enderecoService.buscaCep(noivo.getEndereco()));

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscaCepNoiva() {
        try {

            noiva.setEndereco(enderecoService.buscaCep(noiva.getEndereco()));

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void adicionarContatoNoivo() {
        try {

            noivo.adicionarNovoContato();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removerContatoNoivo() {
        try {

            noivo.removerContato(Integer.parseInt("posicaoContatoNoivo"));

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void adicionarContatoNoiva() {
        try {

            noiva.adicionarNovoContato();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removerContatoNoiva() {
        try {

            noiva.removerContato(Integer.parseInt("posicaoContatoNoiva"));

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

    public EnvolvidoEvento getNoivo() {
        return noivo;
    }

    public void setNoivo(EnvolvidoEvento noivo) {
        this.noivo = noivo;
    }

    public EnvolvidoEvento getNoiva() {
        return noiva;
    }

    public void setNoiva(EnvolvidoEvento noiva) {
        this.noiva = noiva;
    }
    
    

}
