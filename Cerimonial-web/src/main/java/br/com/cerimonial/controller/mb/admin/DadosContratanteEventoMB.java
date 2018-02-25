/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Endereco;
import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.service.EnderecoService;
import br.com.cerimonial.service.EventoPessoaService;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.utils.CollectionUtils;
import br.com.cerimonial.utils.SelectItemUtils;
import java.util.Arrays;
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
@ManagedBean(name = "DadosContratanteEventoMB")
@ViewScoped
public class DadosContratanteEventoMB  extends BasicControl{
    
    protected Long idEvento;
    protected EventoPessoa contratante;
    protected Evento evento;

    @EJB
    protected EventoPessoaService service;
   
    @EJB
    protected EnderecoService enderecoService;
    @EJB
    protected EventoService eventoService;

    protected final SelectItemUtils selectItemUtils;

    public DadosContratanteEventoMB() {
        this.selectItemUtils = new SelectItemUtils();
    }

    /**
     * Evento invocado ao abrir o xhtml do contratante
     */
    public void init() {

        try {

           evento = eventoService.findEntityById(idEvento, Arrays.asList("contratantes"));

            if (evento != null) {

                if (CollectionUtils.isNotBlank(evento.getContratantes())) {

                    contratante = evento.getContratante();

                    if (contratante != null && contratante.getId() != null) {
                        
                        contratante = service.findEntityById(contratante.getId(), Arrays.asList("pessoa", "pessoa.contatosFamiliares","pessoa.endereco", "evolucaoPreenchimento"));

                        if (contratante.getPessoa().getEndereco() == null) {
                            contratante.getPessoa().setEndereco(new Endereco());
                        }
                    }

                }

            }

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar o contrante:" + ex.getCause().getMessage());
            scrollTopMessage();
        }
    }

    /**
     * Evento invocado pela tela de form para salvar contratante
     *
     * @return
     */
    public synchronized String save() {
        try {

            service.saveContratante(contratante);

            createFacesInfoMessage("Dados gravados com sucesso!");

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            return "/intranet/admin/operacional/pre-evento/partials/dados-contratante.xhtml?idEvento=" + idEvento + "&faces-redirect=true";

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível salvar os dados: " + ex.getCause().getLocalizedMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }

    public void buscaCep() {
        if (contratante != null && contratante.getPessoa().getEndereco() != null) {
            try {
                contratante.getPessoa().setEndereco(enderecoService.buscaCep(contratante.getPessoa().getEndereco()));
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
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

    public EventoPessoa getContratante() {
        return contratante;
    }

    public void setContratante(EventoPessoa contratante) {
        this.contratante = contratante;
    }

   
    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    
}
