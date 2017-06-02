/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Empresa;
import br.com.cerimonial.repository.EmpresaRepository;
import br.com.cerimonial.utils.CerimonialUtils;
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
public class EmpresaService extends BasicService<Empresa>{

    private EmpresaRepository repository;
    
    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EmpresaRepository(em);
    }
    
    
    @Override
    public Empresa getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }
    
    public Empresa getEmpresa() throws Exception {
        List<Empresa> empresas = repository.findAll();
        if(CerimonialUtils.isListNotBlank(empresas)){
            return empresas.get(0);
        }
        return null;
    }
    

    @Override
    public Empresa save(Empresa entity) throws Exception {
        if (entity != null) {
            if (entity.getId() == null) {
                return repository.create(entity);
            } else {
                return repository.edit(entity);
            }
        }
        return null;
    }

    
}