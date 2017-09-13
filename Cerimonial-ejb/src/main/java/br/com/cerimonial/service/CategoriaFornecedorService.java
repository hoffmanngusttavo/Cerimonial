/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.CategoriaFornecedor;
import br.com.cerimonial.repository.CategoriaFornecedorRepository;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CategoriaFornecedorService extends BasicService<CategoriaFornecedor> {

    private CategoriaFornecedorRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new CategoriaFornecedorRepository(em);
    }

    @Override
    public CategoriaFornecedor getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public CategoriaFornecedor save(CategoriaFornecedor entity) throws Exception {
        if (entity != null) {
            if (entity.getId() == null) {
                return repository.create(entity);
            } else {
                return repository.edit(entity);
            }
        }
        return null;
    }

    public List<CategoriaFornecedor> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(CategoriaFornecedorService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(CategoriaFornecedor categoria) throws Exception {
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

    public List<CategoriaFornecedor> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<CategoriaFornecedor> findAllByNome(String nome) {
        try {
            if (StringUtils.isNotEmpty(nome)) {
                return repository.findAllByNome(nome.toUpperCase());
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean isValid(CategoriaFornecedor entity) {
        if (entity == null) {
            throw new DAOException("Arquivo nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

}
