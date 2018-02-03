/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.repository.EstadoRepository;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.exceptions.ErrorCode;
import java.util.LinkedList;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EstadoService extends BasicService<Estado> {

    private EstadoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EstadoRepository(em);
    }

    @Override
    public Estado findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    public List<Estado> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(EstadoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new LinkedList<>();
    }

    @Override
    public Estado save(Estado entity) {

        validateObjectNull(entity);

        if (entity.getId() == null) {
            return repository.create(entity);

        } else {
            return repository.edit(entity);
        }
    }

    public Estado findBySigla(String uf) {
        
        if(StringUtils.isBlank(uf)){
            throw new GenericException("A sigla de estado está nula", ErrorCode.BAD_REQUEST.getCode());
        }
        
        try {
            return repository.findBySigla(uf);
        } catch (Exception ex) {
            Logger.getLogger(EstadoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
    public void validateObjectNull(Estado entity) {
        
         if (entity == null) {
            throw new GenericException(" Estado nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(Estado entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }

}
