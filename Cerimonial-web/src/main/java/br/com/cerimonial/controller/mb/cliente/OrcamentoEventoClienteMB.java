/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

import br.com.cerimonial.controller.mb.ContatoInicialCrudMB;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.service.OrcamentoEventoService;
import br.com.cerimonial.utils.ArquivoUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "OrcamentoEventoClienteMB")
@ViewScoped
public class OrcamentoEventoClienteMB extends ClienteControl{
    
    
    /**
     * Id do Evento
     */
    protected Long idEvento;
    protected OrcamentoEvento orcamento;
    
    @EJB
    protected OrcamentoEventoService service;
    
    
    /**
     * Evento invocado ao abrir o xhtml de impressão de contrato do cliente
     */
    public void init() {

        try {
            
            orcamento = service.findOrcamentoByIdEventoAndContratante(idEvento, cliente);

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível carregar o orçamento: "+ex.getCause().getMessage());
            scrollTopMessage();
        }
    }
    
    
    /**
     * Evento invocado pela tela de form para fazer download do arquivo
     *
     */
    public void baixarArquivoOrcamento() {
       try {
            ArquivoUtils.carregarArquivo(orcamento.getArquivo());
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getCause().getMessage());
        }
    }


    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public OrcamentoEvento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(OrcamentoEvento orcamento) {
        this.orcamento = orcamento;
    }
    
    
    
}
