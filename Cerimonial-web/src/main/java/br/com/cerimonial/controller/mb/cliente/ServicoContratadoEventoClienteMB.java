/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.cliente;

import br.com.cerimonial.controller.mb.ContatoInicialCrudMB;
import br.com.cerimonial.entity.ServicoPrestadoEvento;
import br.com.cerimonial.service.ServicoPrestadoEventoService;
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
@ManagedBean(name = "ServicoContratadoEventoClienteMB")
@ViewScoped
public class ServicoContratadoEventoClienteMB extends ClienteControl{
    
    
    private Long idPreEvento;
    
    private ServicoPrestadoEvento entity;
    
    @EJB
    protected ServicoPrestadoEventoService service;
    
    
    /**
     * Evento invocado ao abrir o xhtml de impressão de contrato do cliente
     */
    public void init() {

        try {
            
            entity = service.findEntityByIdEventoAndContratante(idPreEvento, cliente);

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
            ArquivoUtils.carregarArquivo(entity.getArquivo());
        } catch (Exception e) {
            Logger.getLogger(ContatoInicialCrudMB.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getCause().getMessage());
        }
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
