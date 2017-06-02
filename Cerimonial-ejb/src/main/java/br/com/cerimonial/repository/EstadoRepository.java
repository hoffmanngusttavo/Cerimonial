/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Estado;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class EstadoRepository extends BasicRepository{

    public EstadoRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public Estado getEntity(Long id) throws Exception {
       return getEntity(Estado.class, id);
    }

    public List<Estado> findAll() throws Exception {
        return getPureList(Estado.class, "select estado from Estado estado");
    }
    
}
