/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.AlertaDestinatario;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.AlertaDestinatarioRepository;
import br.com.cerimonial.utils.DateUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class AlertaDestinatarioService extends BasicService<AlertaDestinatario> {

    private AlertaDestinatarioRepository repository;
    
    @EJB
    private UsuarioService usuarioService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new AlertaDestinatarioRepository(em);
    }

    @Override
    public AlertaDestinatario findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public AlertaDestinatario save(AlertaDestinatario entity) throws Exception {

        validateObjectNull(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }

    }

    public List<AlertaDestinatario> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(AlertaDestinatarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(AlertaDestinatario alertaDestinatario) throws Exception {
        
        validateObjectAndIdNull(alertaDestinatario);
        
        repository.delete(alertaDestinatario.getId());
        
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<AlertaDestinatario> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

   

    /**
     * Carregar os ultimos alertas que não foram visualizados do usuário logado
     *
     * @param limit
     * @param usuario
     * @return
     * @throws java.lang.Exception
     */
    public List<AlertaDestinatario> findAlertasUsuarioNaoVisualizados(int limit, Usuario usuario) throws Exception {

        usuarioService.validateObjectNull(usuario);
        
        return repository.findAlertasUsuarioNaoVisualizados(limit, usuario, DateUtils.formatDate(new Date(), DateUtils.format_dd_MM_yyyy_ptBR));

    }

    /**
     * Contabilizar os ultimos alertas que não foram visualizados do usuário
     * logado
     *
     * @param usuario
     * @return
     * @throws java.lang.Exception
     */
    public Integer countAlertasUsuarioNaoVisualizados(Usuario usuario) throws Exception {

        usuarioService.validateObjectNull(usuario);

        return repository.countAlertasUsuarioNaoVisualizados(usuario, new Date());

    }

    /**
     * Alterar para visualizado o alerta
     *
     * @param entity
     * @return
     * @throws java.lang.Exception
     */
    public AlertaDestinatario alterarAlertaVisualizado(AlertaDestinatario entity) throws Exception {

        validateObjectNull(entity);

        if (!entity.isVisualizado()) {

            entity.setVisualizado(true);

            return this.save(entity);
        }

        return entity;
    }
    
    @Override
    public void validateId(Long idEntity) {
        
        if (idEntity == null) {
            throw new GenericException("Id nulo ", ErrorCode.BAD_REQUEST.getCode());
        }

        if (idEntity <= 0) {
            throw new GenericException("Id não pode ser menor ou igual a zero ", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectNull(AlertaDestinatario entity) {
        
         if (entity == null) {
            throw new GenericException(" Alerta Destinatario nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(AlertaDestinatario entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }

}
