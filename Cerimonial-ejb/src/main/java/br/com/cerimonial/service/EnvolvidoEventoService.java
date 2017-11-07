/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.EnvolvidoEvento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.EnvolvidoEventoRepository;
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
public class EnvolvidoEventoService extends BasicService<EnvolvidoEvento> {

    private EnvolvidoEventoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EnvolvidoEventoRepository(em);
    }

    @Override
    public EnvolvidoEvento getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public EnvolvidoEvento save(EnvolvidoEvento entity) throws Exception {

        isValid(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }

    }

    @Override
    public boolean isValid(EnvolvidoEvento entity) {

        if (entity == null) {
            throw new GenericException("Envolvido nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (entity.getTipoEnvolvidoEvento() == null) {
            throw new GenericException("Tipo do envolvido nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (entity.getEvento() == null) {
            throw new GenericException("Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        return true;

    }

    

}
