/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.controller.mb.ContatoInicialCrudMB;
import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.EmailContatoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EvolucaoPreenchimento;
import br.com.cerimonial.entity.PreEvento;
import br.com.cerimonial.service.EmailContatoEventoService;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.EvolucaoPreenchimentoService;
import br.com.cerimonial.service.PreEventoService;
import br.com.cerimonial.utils.ArquivoUtils;
import br.com.cerimonial.utils.SelectItemUtils;
import java.util.LinkedList;
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

    protected PreEvento preEvento;
    protected Evento evento;
    protected EmailContatoEvento emailContato;
    
    protected UploadedFile file;

    protected Long idPreEvento;

    private EvolucaoPreenchimento evolucaoEvento;
    private EvolucaoPreenchimento evolucaoContratante;
    private EvolucaoPreenchimento evolucaoNoivo;
    private EvolucaoPreenchimento evolucaoNoiva;

    @EJB
    protected PreEventoService preEventoService;
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

    public void initPreEvento() {
        try {
            if (idPreEvento != null) {

                List<String> camposLazy = new LinkedList<String>();
                camposLazy.add("emailsContatoEvento");
                camposLazy.add("contatosEvento");
                camposLazy.add("servicoPrestadoEvento");
                
                preEvento = preEventoService.findEntityById(idPreEvento, camposLazy);

                evento = preEvento.getEvento();
                
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
     * Evento invocado pela tela para liberar acesso um evento
     * @return 
     */
    public String liberarAcessoContratante() {
        try {

            eventoService.liberarAcessoSistemaContratanteEvento(evento);

            createFacesInfoMessage("Liberado acesso com sucesso");
            
            return "/intranet/admin/operacional/pre-evento/index.xhtml?id=" + preEvento.getId() + "&faces-redirect=true";

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível liberar o acesso: " + ex.getCause().getMessage());
        }
        
        return null;
    }
    
    
    /**
     * Evento invocado pela tela para liberar acesso um evento
     * @return 
     */
    public String criarEvento() {
        try {

            eventoService.criarEventoFromPreEvento(preEvento);

            createFacesInfoMessage("Evento criado com sucesso");
            
            return "/intranet/admin/operacional/pre-evento/index.xhtml?id=" + preEvento.getId() + "&faces-redirect=true";

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível liberar o acesso: " + ex.getCause().getMessage());
        }
        
        return null;
    }

    /**
     * Evento invocado pela tela para liberar acesso um evento
     * @return 
     */
    public String cancelarAcessoContratante() {
        try {

            eventoService.cancelarAcessoSistemaContratanteEvento(evento);

            createFacesInfoMessage("Cancelado acesso com sucesso");
            
            return "/intranet/admin/operacional/pre-evento/index.xhtml?id=" + preEvento.getId() + "&faces-redirect=true";

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível cancelar o evento: " + ex.getCause().getMessage());
        }
        
        return null;
    }

    public void adicionarNovoEmailContato() {

        emailContato = new EmailContatoEvento(preEvento);

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
                emailContato = new EmailContatoEvento(preEvento);
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

            initPreEvento();

            createFacesInfoMessage("Email enviado com sucesso");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            String msg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
            createFacesErrorMessage("Não foi possível salvar o registro do email: " + msg);
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

    public PreEvento getPreEvento() {
        return preEvento;
    }

    public void setPreEvento(PreEvento preEvento) {
        this.preEvento = preEvento;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
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

    public Long getIdPreEvento() {
        return idPreEvento;
    }

    public void setIdPreEvento(Long idPreEvento) {
        this.idPreEvento = idPreEvento;
    }
 
    
    
    

    

}
