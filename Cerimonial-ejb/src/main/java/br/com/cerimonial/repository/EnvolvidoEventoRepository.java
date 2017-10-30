/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.EnvolvidoEvento;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class EnvolvidoEventoRepository extends AbstractRepository<EnvolvidoEvento>{

    public EnvolvidoEventoRepository(EntityManager entityManager) {
        super(entityManager, EnvolvidoEvento.class);
    }
    
    
    
}
