/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

import br.com.cerimonial.entity.ContatoEnvolvido;
import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.enums.TipoEnvolvido;
import br.com.cerimonial.enums.TipoEnvolvidoEvento;
import br.com.cerimonial.enums.TipoPessoa;
import br.com.cerimonial.service.ContatoEnvolvidoService;
import br.com.cerimonial.service.EnderecoService;
import br.com.cerimonial.service.EventoPessoaService;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.utils.CollectionUtils;
import br.com.cerimonial.utils.SelectItemUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "FichaAniversarianteMB")
@ViewScoped
public class FichaAniversarianteMB extends ClienteControl {

    protected Long idEvento;

    protected Integer posicaoContato;

    protected Evento evento;

    protected EventoPessoa envolvido;

    protected ContatoEnvolvido contato;

    protected List<ContatoEnvolvido> contatosRemover;

    @EJB
    protected EventoService eventoService;

    @EJB
    protected EventoPessoaService eventoPessoaService;

    @EJB
    protected ContatoEnvolvidoService contatoEnvolvidoService;

    @EJB
    protected EnderecoService enderecoService;

    protected final SelectItemUtils selectItemUtils;

    public FichaAniversarianteMB() {
        this.selectItemUtils = new SelectItemUtils();
        contatosRemover = new ArrayList<>();
    }

    /**
     * Evento invocado ao abrir o xhtml de ficha cadastral
     */
    public void initEvento() {
        try {

            evento = eventoService.findEventoByIdAndContratante(idEvento, cliente, Arrays.asList("contratantes"));

            if (evento != null) {

                if (CollectionUtils.isNotBlank(evento.getContratantes())) {
                    envolvido = evento.getTipoEnvolvidoEvento(TipoEnvolvidoEvento.ANIVERSARIANTE);
                }

            }

            if (envolvido == null) {
                envolvido = new EventoPessoa(evento, new Pessoa(TipoEnvolvido.CLIENTE, TipoPessoa.FISICA), TipoEnvolvidoEvento.ANIVERSARIANTE, true);
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

            eventoPessoaService.save(envolvido);

            contatoEnvolvidoService.removerContatos(contatosRemover);

            createFacesInfoMessage("Dados gravados com sucesso!");

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

            return "/intranet/cliente/evento/partials/cadastro-aniversariante.xhtml?idEvento=" + idEvento + "&faces-redirect=true";
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível salvar o cadastro dos noivos " + ex.getMessage());
        } finally {
            scrollTopMessage();
        }
        return null;
    }

    /**
     * Método pra buscar cep e carregar um endereço a partir do cep digitado.
     */
    public void buscaCep() {
        try {

            envolvido.getPessoa().setEndereco(enderecoService.buscaCep(envolvido.getPessoa().getEndereco()));

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método por instanciar um novo contato, quando clicado no botão de +
     */
    public void criarNovoContato() {
        try {

            contato = new ContatoEnvolvido(envolvido.getPessoa());

            posicaoContato = null;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para salvar a adição ou edição em memória o contato.
     */
    public void salvarContato() {
        try {

            if (posicaoContato != null) {

                envolvido.getPessoa().editarContato(contato, posicaoContato);

            } else {

                envolvido.getPessoa().adicionarNovoContato(contato);

            }
            posicaoContato = null;

            createFacesInfoMessage("Adicionado contato com sucesso");

            createFacesWarnMessage("Não esqueça de salvar as alterações feitas");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {

            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("$('#editDialogVar').modal('hide');");

        }
    }

    /**
     * Método para remover da memória o contato selecionado Se o contato já
     * tiver no banco de dados, será armazenado em uma lista para depois remover
     * quando salvar.
     */
    public void removerContatoNoivo() {
        try {

            envolvido.getPessoa().removerContato(posicaoContato);
            
            if (contato != null && contato.getId() != null) {
                contatosRemover.add(contato);
            }

            posicaoContato = null;
            setContato(null);

            createFacesInfoMessage("Removido contato com sucesso");

            createFacesWarnMessage("Não esqueça de salvar as alterações feitas");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível remover contato");
        }
    }

    /**
     * Método para carregar o objeto contato para editar ou remover
     *
     * @param item
     */
    public void carregarContato(ContatoEnvolvido item) {

        setContato(item);

        posicaoContato = Integer.parseInt(getRequestParam("posicaoContatoNoivo"));

    }

    public List<SelectItem> getComboCidade(Estado estado) {
        return selectItemUtils.getComboCidade(estado);
    }

    public List<SelectItem> getComboEstado() {
        return selectItemUtils.getComboEstado();
    }

    public List<SelectItem> getComboGrauParentesco() {
        return SelectItemUtils.getComboGrauParentesco();
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

    public ContatoEnvolvido getContato() {
        return contato;
    }

    public void setContato(ContatoEnvolvido contato) {
        this.contato = contato;
    }

    public Integer getPosicaoContato() {
        return posicaoContato;
    }

    public void setPosicaoContato(Integer posicaoContato) {
        this.posicaoContato = posicaoContato;
    }

    public List<ContatoEnvolvido> getContatosRemover() {
        return contatosRemover;
    }

    public void setContatosRemover(List<ContatoEnvolvido> contatosRemover) {
        this.contatosRemover = contatosRemover;
    }

    public EventoPessoa getEnvolvido() {
        return envolvido;
    }

    public void setEnvolvido(EventoPessoa envolvido) {
        this.envolvido = envolvido;
    }
    
    

}
