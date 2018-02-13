/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb.admin;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.AtividadeEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Servico;
import br.com.cerimonial.service.AtividadeEventoService;
import br.com.cerimonial.service.EventoService;
import br.com.cerimonial.service.ServicoService;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hoffmann
 */
@ManagedBean(name = "CheckListEventoMB")
@ViewScoped
public class CheckListEventoMB extends BasicControl {

    private Long idEvento;

    private Evento evento;

    private AtividadeEvento entity;

    private List<AtividadeEvento> atividades;

    @EJB
    protected AtividadeEventoService service;

    @EJB
    protected EventoService eventoService;
    
    @EJB
    protected ServicoService servicoService;

    /**
     * Evento invocado ao abrir o xhtml carregar os dados do contrato do evento
     */
    public void init() {

        try {

            if (idEvento != null) {

                evento = eventoService.findEntityById(idEvento);

                atividades = service.findAtividadesByIdEvento(idEvento);

            }

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getCause().getMessage());
            scrollTopMessage();
        }

    }

    public void setEntity(AtividadeEvento entity) {
        this.entity = entity;
    }

    public String removerAtividade() {
        
        try {
            
            service.delete(entity);
            
            createFacesInfoMessage("Atividade removida com sucesso!");
            
            return "/intranet/admin/operacional/pre-evento/partials/atividades.xhtml?idEvento=" + evento.getId() + "&faces-redirect=true";

        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getCause().getMessage());
            scrollTopMessage();
        }
        
        return null;

    }
    
    public String salvarAtividade(){
        
        try {
            
            service.save(entity);
            
            createFacesInfoMessage("Atividade gravada com sucesso!");
            
            return "/intranet/admin/operacional/pre-evento/partials/atividades.xhtml?idEvento=" + evento.getId() + "&faces-redirect=true";

        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getCause().getMessage());
            scrollTopMessage();
        }
        
        return null;
    
    }
    
    
    public void instanciarNovaAtividade(){
    
        entity = new AtividadeEvento(evento);
    
    }
    
    public List<Servico> completeServico(String query){
        
        List<Servico> servicos = null;
        
        try {
            
          servicos = servicoService.findAllByNome(query);
            
          if(CollectionUtils.isBlank(servicos)){
              
              Servico servico = new Servico();
              servico.setNome(query);
              
              servicos = new ArrayList<>();
              servicos.add(servico);
              
          }
          
        } catch (Exception e) {
            
            Logger.getLogger(e.getMessage());
        }

        return servicos;
        
    }
    

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public List<AtividadeEvento> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<AtividadeEvento> atividades) {
        this.atividades = atividades;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public AtividadeEvento getEntity() {
        return entity;
    }

}
