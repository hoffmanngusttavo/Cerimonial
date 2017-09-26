/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.repository.ContatoEventoRepository;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.utils.CerimonialUtils;
import java.util.ArrayList;
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
public class ContatoEventoService extends BasicService<ContatoEvento> {

    private ContatoEventoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ContatoEventoRepository(em);
    }

    /**
     * TODO[Ordenar tipo desc] Retorna contato pesquisado por um id e ordenado
     * as propostas de forma desc
     *
     * @param id
     * @return
     * @throws java.lang.Exception
     */
    @Override
    public ContatoEvento getEntity(Long id) throws Exception {
        ContatoEvento entity = repository.getEntity(id);
        return entity;
    }

    @Override
    public ContatoEvento save(ContatoEvento entity) throws Exception {

        isValid(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public List<ContatoEvento> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ContatoEventoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(ContatoEvento contato) throws Exception {

        isValid(contato);

        repository.delete(contato.getId());
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<ContatoEvento> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<String> getLocaisEvento(String value) {
        if (StringUtils.isNotBlank(value)) {
            try {
                return repository.getLocaisEvento(value.toUpperCase());
            } catch (Exception ex) {
                Logger.getLogger(ContatoEventoService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public boolean isValid(ContatoEvento entity) {
        if (entity == null) {
            throw new GenericException("Arquivo nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        if (StringUtils.isBlank(entity.getNomeContato())) {
            throw new GenericException("Contato sem nome.", ErrorCode.BAD_REQUEST.getCode());
        }
        if (StringUtils.isBlank(entity.getNomeEvento())) {
            throw new GenericException("Contato sem nome de evento.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

}
