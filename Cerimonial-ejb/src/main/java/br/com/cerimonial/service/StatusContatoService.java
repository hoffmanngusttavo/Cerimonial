/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.StatusContato;
import br.com.cerimonial.repository.StatusContatoRepository;
import br.com.cerimonial.exceptions.DAOException;
import br.com.cerimonial.exceptions.ErrorCode;
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
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StatusContatoService extends BasicService<StatusContato> {

    private StatusContatoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new StatusContatoRepository(em);
    }

    @Override
    public StatusContato getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public StatusContato save(StatusContato entity) throws Exception {

        isValid(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public List<StatusContato> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(StatusContatoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(StatusContato categoria) throws Exception {

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

    public List<StatusContato> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean isValid(StatusContato entity) {
        if (entity == null) {
            throw new DAOException("Status nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

}
