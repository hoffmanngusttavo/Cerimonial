/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.EventoPessoaRepository;
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
public class EventoPessoaService extends BasicService<EventoPessoa> {

    private EventoPessoaRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EventoPessoaRepository(em);

    }

    @Override
    public EventoPessoa getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public EventoPessoa save(EventoPessoa entity) throws Exception {

        isValid(entity);

        return repository.create(entity);
    }

    @Override
    public boolean isValid(EventoPessoa entity) {
        if (entity == null) {
            throw new GenericException("Evento Pessoa nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (entity.getContratante() == null) {
            throw new GenericException("Contratate nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (entity.getEvento() == null) {
            throw new GenericException("Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

    /**
     * Garante que o objeto terá somente um evento
     *
     * @param evento
     * @param cliente
     * @return
     * @throws java.lang.Exception
     */
    public EventoPessoa criarEventoPessoa(Evento evento, Pessoa cliente) throws Exception {

        if (evento == null) {
            throw new GenericException("Evento Nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        if (cliente == null) {
            throw new GenericException("Cliente Nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        EventoPessoa eventoPessoa = this.getEntityByEvento(evento);

        if (eventoPessoa == null) {
            eventoPessoa = new EventoPessoa(evento, cliente, evento.getOrcamentoEvento().getContatoEvento().getTipoEnvolvidoEvento());
        }

        return eventoPessoa;
    }

    /**
     * Recupera o objeto EventoPessoa a partir de um evento
     * @param evento
     * @return 
     */
    public EventoPessoa getEntityByEvento(Evento evento)  {

        if (evento == null) {
            throw new GenericException("Evento Nulo", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (evento.getId() == null) {
            throw new GenericException("Id do Evento Nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        return repository.getEntityByEvento(evento);
        
    }

}
