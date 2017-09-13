/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.repository.ArquivoRepository;
import br.com.cerimonial.repository.exceptions.DAOException;
import br.com.cerimonial.repository.exceptions.ErrorCode;
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
public class ArquivoService extends BasicService<Arquivo> {

    private ArquivoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ArquivoRepository(em);
    }

    @Override
    public Arquivo getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Arquivo save(Arquivo entity) throws Exception {

        isValid(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }

    }

    public void delete(Arquivo categoria) throws Exception {

        isValid(categoria);

        repository.delete(categoria.getId());
    }

    public List<Arquivo> getArquivosByModeloProposta(ModeloProposta entity) {
        try {
            if (entity != null && entity.getId() != null) {
                return repository.getArquivosByModeloProposta(entity);
            }
        } catch (Exception ex) {
            Logger.getLogger(ArquivoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean isValid(Arquivo entity) {

        if (entity == null) {
            throw new DAOException("Arquivo nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        return true;
    }

}
