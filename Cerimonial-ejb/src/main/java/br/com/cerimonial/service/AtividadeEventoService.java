/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.AtividadeEvento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.AtividadeEventoRepository;
import java.util.HashMap;
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
public class AtividadeEventoService extends BasicService<AtividadeEvento>{
    
    private AtividadeEventoRepository repository;
    
    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new AtividadeEventoRepository(em);

    }

    @Override
    public AtividadeEvento findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public AtividadeEvento save(AtividadeEvento entity) throws Exception {

        validateObjectNull(entity);
        
        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public List<AtividadeEvento> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {
        try {
            if (params == null) {
                params = new HashMap<>();
            }
            return repository.findRangeListagem(params, max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
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
    public void validateObjectNull(AtividadeEvento entity) {
        
         if (entity == null) {
            throw new GenericException(" Custo Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(AtividadeEvento entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }

    public List<AtividadeEvento> findAtividadesByIdEvento(Long idEvento) {
        
        validateId(idEvento);
        
        return repository.findAtividadesByIdEvento(idEvento);
        
    }
    
    
    
}
