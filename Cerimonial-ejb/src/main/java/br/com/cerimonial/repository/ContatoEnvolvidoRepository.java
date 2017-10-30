/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ContatoEnvolvido;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class ContatoEnvolvidoRepository extends AbstractRepository<ContatoEnvolvido>{

    public ContatoEnvolvidoRepository(EntityManager entityManager) {
        super(entityManager, ContatoEnvolvido.class);
    }
    
    
    
}
