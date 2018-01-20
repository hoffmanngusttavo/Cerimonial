/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.controller.mb.ContatoInicialCrudMB;
import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.EmailContatoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EvolucaoPreenchimento;
import br.com.cerimonial.service.ContatoEventoService;
import br.com.cerimonial.service.EmailContatoEventoService;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.EvolucaoPreenchimentoService;
import br.com.cerimonial.utils.ArquivoUtils;
import br.com.cerimonial.utils.SelectItemUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "PreEventoMB")
@ViewScoped
public class PreEventoMB extends BasicControl {

    protected ContatoEvento entity;
    protected Evento evento;
    protected EmailContatoEvento emailContato;
    protected UploadedFile file;

    protected Long id;
    protected Long idEvento;

    private EvolucaoPreenchimento evolucaoEvento;
    private EvolucaoPreenchimento evolucaoContratante;
    private EvolucaoPreenchimento evolucaoNoivo;
    private EvolucaoPreenchimento evolucaoNoiva;

    @EJB
    protected ContatoEventoService service;
    @EJB
    protected EventoService eventoService;
    @EJB
    protected EmailContatoEventoService emailContatoEventoService;
    @EJB
    protected EvolucaoPreenchimentoService evolucaoPreenchimentoService;

    protected SelectItemUtils selectItemUtils;

    public PreEventoMB() {

        this.selectItemUtils = new SelectItemUtils();

    }

    /**
     * Evento invocado ao abrir o xhtml de um contato inicial de pre evento
     */
    public void init() {
        try {
            if (id != null) {

                entity = service.getEntity(id);

                evento = eventoService.getEventoByContatoInicial(entity);
                
                preencherPorcentagemConcluida(evento);
                
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar o evento: " + ex.getCause().getMessage());
        }
    }

    /**
     * Evento invocado ao abrir o xhtml de um contato inicial de pre evento
     */
    public void initEvento() {
        try {
            if (idEvento != null) {

                evento = eventoService.findEventoLazyContratanteEvolucao(idEvento);

                entity = service.getContatoInicialByEvento(evento);
                
                preencherPorcentagemConcluida(evento);

            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar o evento: " + ex.getCause().getMessage());
        }
    }

    public void preencherPorcentagemConcluida(Evento evento) {

        evolucaoContratante = evolucaoPreenchimentoService.getEvolucaoDadosContratante(evento);
        evolucaoNoivo = evolucaoPreenchimentoService.getEvolucaoDadosNoivo(evento);
        evolucaoNoiva = evolucaoPreenchimentoService.getEvolucaoDadosNoiva(evento);
        evolucaoEvento = evolucaoPreenchimentoService.getEvolucaoDadosEvento(evento);
    
    }

    /**
     * Evento invocado pela tela para cancelar um evento
     */
    public void cancelarEvento() {
        try {

            eventoService.cancelarEvento(evento.getId(), evento.getMotivoCancelamento());

            evento = eventoService.getEntity(evento.getId());

            createFacesInfoMessage("Evento cancelado com sucesso");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível cancelar o evento: " + ex.getCause().getMessage());
        }
    }

    /**
     * Evento invocado pela tela para liberar acesso um evento
     */
    public void liberarAcessoContratante() {
        try {

            eventoService.liberarAcessoSistemaContratanteEvento(evento);

            evento = eventoService.getEntity(evento.getId());

            createFacesInfoMessage("Liberado acesso com sucesso");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível liberar o acesso: " + ex.getCause().getMessage());
        }
    }

    /**
     * Evento invocado pela tela para liberar acesso um evento
     */
    public void cancelarAcessoContratante() {
        try {

            eventoService.cancelarAcessoSistemaContratanteEvento(evento);

            evento = eventoService.getEntity(evento.getId());

            createFacesInfoMessage("Cancelado acesso com sucesso");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível cancelar o evento: " + ex.getCause().getMessage());
        }
    }

    public void adicionarNovoEmailContato() {

        emailContato = new EmailContatoEvento(entity);

    }

    /**
     * Evento invocado pelo dialog de enviar email do contato inicial
     */
    public void selecionarModeloEmailContato() {
        try {

            emailContato = emailContatoEventoService.carregarDadosModeloEmail(emailContato, emailContato.getModeloEmail());

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar os dados do modelo: " + ex.getCause().getMessage());
        } finally {

            if (emailContato == null) {
                emailContato = new EmailContatoEvento(entity);
            }

        }
    }

    /**
     * Evento invocado pelo dialog de enviar email do contato inicial
     */
    public void salvarEmailContato() {
        try {

            if (file != null && file.getSize() > 0) {
                emailContato.setArquivo(new Arquivo(file.getFileName(), file.getContentType(), file.getContents()));
            }

            emailContatoEventoService.save(emailContato);

            init();

            createFacesInfoMessage("Email enviado com sucesso");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível salvar o registro do email: " + ex.getCause().getMessage());
        }
    }

    /**
     * Evento invocado pela tela de form para fazer download do arquivo
     *
     */
    public void baixarArquivo() {
        try {
            ArquivoUtils.carregarArquivo(emailContato.getArquivo());
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getMessage());
        }
    }

    /**
     * Evento invocado pela tela de form para remover download do arquivo
     * TODO[FIXME]
     */
    public void removerArquivo() {
        try {
            emailContato.setArquivo(null);
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getMessage());
        }
    }

    /**
     * Método vai retornar o nome do css que deve atribuir ao panel de acordo
     * com a porcentagem do preenchimento
     *
     * @param evolucaoPreenchimento
     * @return
     */
    public String getNomePanelEvolucaoPreenchimento(EvolucaoPreenchimento evolucaoPreenchimento) {

        return evolucaoPreenchimentoService.getNomePanelEvolucaoPreenchimento(evolucaoPreenchimento);
    }

    public List<SelectItem> getComboModeloEmail() {
        return selectItemUtils.getComboModeloEmail();
    }

    public ContatoEvento getEntity() {
        return entity;
    }

    public void setEntity(ContatoEvento entity) {
        this.entity = entity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public EmailContatoEvento getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(EmailContatoEvento emailContato) {
        this.emailContato = emailContato;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public EvolucaoPreenchimento getEvolucaoEvento() {
        return evolucaoEvento;
    }

    public void setEvolucaoEvento(EvolucaoPreenchimento evolucaoEvento) {
        this.evolucaoEvento = evolucaoEvento;
    }

    public EvolucaoPreenchimento getEvolucaoNoivo() {
        return evolucaoNoivo;
    }

    public void setEvolucaoNoivo(EvolucaoPreenchimento evolucaoNoivo) {
        this.evolucaoNoivo = evolucaoNoivo;
    }

    public EvolucaoPreenchimento getEvolucaoNoiva() {
        return evolucaoNoiva;
    }

    public void setEvolucaoNoiva(EvolucaoPreenchimento evolucaoNoiva) {
        this.evolucaoNoiva = evolucaoNoiva;
    }

    public EvolucaoPreenchimento getEvolucaoContratante() {
        return evolucaoContratante;
    }

    public void setEvolucaoContratante(EvolucaoPreenchimento evolucaoContratante) {
        this.evolucaoContratante = evolucaoContratante;
    }
 
    

    

    

}
