/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Alerta;
import br.com.cerimonial.entity.ContratoEvento;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.AlertaRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AlertaService extends BasicService<Alerta> {

    private AlertaRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new AlertaRepository(em);
    }

    @Override
    public Alerta getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Alerta save(Alerta entity) throws Exception {
        if (entity != null) {
            if (entity.getId() == null) {
                return repository.create(entity);
            } else {
                return repository.edit(entity);
            }
        }
        return null;
    }

    public List<Alerta> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(AlertaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(Alerta categoria) throws Exception {
        isValid(categoria);
        repository.delete(categoria.getId());
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Alerta> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean isValid(Alerta entity) {
        if (entity == null) {
            throw new GenericException("Alerta nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

    public void enviarAlertaContratoLiberado(ContratoEvento entity) throws Exception {

        if (entity == null) {
            throw new GenericException("Contrato nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if(entity.getEvento().getContratanteUsuario() == null){
             throw new GenericException("Usuario do evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
        String caminho = "/intranet/cliente/evento/partials/impressao-contrato.xhtml?idEvento="+entity.getEvento().getId();
        String titulo = "Contrato";
        String msg = "Contrato foi gerado e liberado para visualização";
        
        
        Alerta alerta = criarAlerta(caminho, titulo, msg, entity.getEvento().getContratanteUsuario().getUsuarioCliente());
        
        this.save(alerta);
        
    }

    /**
     * Instancia um alerta com mensagem, caminho e destinatarios
     *
     * @param caminho
     * @param titulo
     * @param mensagem
     * @param destinatario
     * @return
     * @throws java.lang.Exception
     */
    public Alerta criarAlerta(String caminho, String titulo, String mensagem, Usuario destinatario) throws Exception {

        if (destinatario == null) {
            throw new GenericException("Destinatario do alerta nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        Alerta alerta = new Alerta();

        alerta.setDataCadastro(new Date());
        alerta.setVigenciaInicial(new Date());
        alerta.setTitulo(titulo);
        alerta.setMensagem(mensagem);
        alerta.setLink(caminho);

        alerta.adicionarDestinatarios(destinatario);

        return alerta;
    }

}
