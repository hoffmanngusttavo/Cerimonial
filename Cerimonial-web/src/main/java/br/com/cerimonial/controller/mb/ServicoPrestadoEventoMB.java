/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ServicoPrestadoEvento;
import br.com.cerimonial.service.ServicoPrestadoEventoService;
import br.com.cerimonial.utils.ArquivoUtils;
import br.com.cerimonial.utils.SelectItemUtils;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "ServicoPrestadoEventoMB")
@ViewScoped
public class ServicoPrestadoEventoMB extends BasicControl {

    private Long idPreEvento;
    private ServicoPrestadoEvento entity;

    private UploadedFile file;
    private SelectItemUtils selectItemUtils;

    @EJB
    protected ServicoPrestadoEventoService service;

    /**
     * Evento invocado ao abrir o xhtml carregar os dados do contrato do evento
     */
    public void init() {

        if (idPreEvento != null) {
            try {

                entity = service.findEntityByPreEventoId(idPreEvento, Arrays.asList("anexos", "preEvento", "preEvento.contatosEvento"));

            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                createFacesErrorMessage(ex.getCause().getMessage());
                scrollTopMessage();
            }
        }

        this.selectItemUtils = new SelectItemUtils();

    }

    /**
     * Evento invocado pela tela de form para fazer download do arquivo
     *
     * @param servicoPrestadoEvento
     */
    public void baixarArquivo(ServicoPrestadoEvento servicoPrestadoEvento) {
        try {
            ArquivoUtils.carregarArquivo(servicoPrestadoEvento.getArquivo());
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getCause().getMessage());
        }
    }

    /**
     * Evento invocado pela tela de form para remover download do arquivo
     */
    public void removerArquivoOrcamento() {
        try {
            entity.setArquivo(null);
            createFacesInfoMessage("Anexo removido, clique em salvar para concluir a remoção");
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getMessage());
        }
    }

    /**
     * Evento ao selecionar um pdf na tela de form
     *
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        if (file != null && file.getSize() > 0) {
            entity.setArquivo(new Arquivo(file.getFileName(), file.getContentType(), file.getContents()));
            createFacesInfoMessage("Arquivo anexado com sucesso");
        }
    }

    /**
     * Evento vindo da tela de form, quando seleciona um modelo de proposta
     */
    public void carregarDadosProposta() {
        try {
            entity = service.carregarPropostaModelo(entity, entity.getModeloProposta());
        } catch (Exception ex) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        }
    }

    /**
     * Evento invocado pela tela de form para salvar no banco um novo orÃ§amento
     * e depois recarregar todos as propostas
     *
     */
    public synchronized void salvar() {
        try {
            
            service.save(entity);

            createFacesInfoMessage("Dados gravados com sucesso!");

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
        } finally {
            scrollTopMessage();
        }
    }

    /**
     * MÃ©todo invocado da tela de form para editar um valor
     */
    public void alterarValorProposta() {
        entity.setValorAlterado(entity.getValorProposta());
    }

    /**
     * MÃ©todo invocado da tela de form cancelar ediÃ§Ã£o de um valor
     */
    public void cancelarAlterarValorProposta() {
        entity.setValorAlterado(-1);
    }

    public List<SelectItem> getComboModelosProposta() {
        return selectItemUtils.getComboModelosPropostaTipoEvento(entity.getPreEvento().getContatoEvento().getTipoEvento());
    }

    public Long getIdPreEvento() {
        return idPreEvento;
    }

    public void setIdPreEvento(Long idPreEvento) {
        this.idPreEvento = idPreEvento;
    }

    public ServicoPrestadoEvento getEntity() {
        return entity;
    }

    public void setEntity(ServicoPrestadoEvento entity) {
        this.entity = entity;
    }

    
    
}
