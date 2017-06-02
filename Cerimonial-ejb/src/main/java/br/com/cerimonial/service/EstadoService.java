/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.repository.EstadoRepository;
import br.com.cerimonial.repository.UsuarioRepository;
import java.util.LinkedList;
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
public class EstadoService extends BasicService<Estado> {

    private EstadoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EstadoRepository(em);
    }

    @Override
    public Estado getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    public List<Estado> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(EstadoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new LinkedList<>();
    }

    @Override
    public Estado save(Estado entity) {
        try {
            if (entity != null) {
                if (entity.getId() == null) {
                    return repository.create(entity);

                } else {
                    return repository.edit(entity);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(EstadoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Estado findBySigla(String uf) {
        try {
            return repository.findBySigla(uf);
        } catch (Exception ex) {
            Logger.getLogger(EstadoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
