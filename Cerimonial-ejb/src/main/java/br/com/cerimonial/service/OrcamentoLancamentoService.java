/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.OrcamentoLancamento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.OrcamentoLancamentoRepository;
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
public class OrcamentoLancamentoService extends BasicService<OrcamentoLancamento> {
    
    private OrcamentoLancamentoRepository repository;
    
    
    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new OrcamentoLancamentoRepository(em);
    }

    @Override
    public OrcamentoLancamento findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }
    
     @Override
    public OrcamentoLancamento save(OrcamentoLancamento entity) throws Exception {

        validateObjectNull(entity);
        
        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }

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
    public void validateObjectNull(OrcamentoLancamento entity) {
        
         if (entity == null) {
            throw new GenericException(" Orcamento Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(OrcamentoLancamento entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }
    
    
}
