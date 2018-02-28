/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.PreEvento;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class PreEventoRepository extends AbstractRepository<PreEvento>{

    public PreEventoRepository(EntityManager entityManager) {
        super(entityManager, PreEvento.class);
    }
    
    
    
}
