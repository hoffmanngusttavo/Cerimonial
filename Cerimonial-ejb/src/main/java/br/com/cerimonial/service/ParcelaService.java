/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Parcela;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.ParcelaRepository;
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

/**
 *
 * @author hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ParcelaService extends BasicService<Parcela>{
    
    private ParcelaRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ParcelaRepository(em);
    }

    @Override
    public Parcela getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Parcela save(Parcela entity) throws Exception {

        isValid(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public List<Parcela> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ParcelaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(Parcela categoria) throws Exception {

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

    public List<Parcela> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean isValid(Parcela entity) {
        if (entity == null) {
            throw new GenericException("Parcela nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (entity.getLancamento() == null) {
            throw new GenericException("Lançamento da Parcela nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }
    
    
}
