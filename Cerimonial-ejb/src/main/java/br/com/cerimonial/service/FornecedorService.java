/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Fornecedor;
import br.com.cerimonial.repository.FornecedorRepository;
import java.util.HashMap;
import java.util.List;
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
public class FornecedorService extends BasicService<Fornecedor> {

    private FornecedorRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new FornecedorRepository(em);
    }

    @Override
    public Fornecedor getEntity(Long id) throws Exception {
        return repository.getFornecedor(id);
    }

    @Override
    public synchronized Fornecedor save(Fornecedor entity) throws Exception {
        if (entity != null) {
            if (entity.getId() == null) {
                return repository.create(entity);
            } else {
                return repository.edit(entity);
            }
        }
        return null;
    }

    public void delete(Fornecedor entity) throws Exception {
        if (entity != null) {
            repository.delete(entity);
        }
    }

    public int countListagem(HashMap<String, Object> filter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Fornecedor> findRangeListagem(HashMap<String, Object> filter, int max, int offset, String sortField, String sortAscDesc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int countAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
