/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Cidade;
import br.com.cerimonial.repository.CidadeRepository;
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
public class CidadeService  extends BasicService<Cidade>{

    
    private CidadeRepository repository;
    
    
    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new CidadeRepository(em);
    }
    
    @Override
    public Cidade getEntity(Long id) throws Exception {
       return repository.getEntity(id);
    }

    public List<Cidade> findAll() throws Exception {
       return repository.findAll();
    }
    
    @Override
    public Cidade save(Cidade entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
