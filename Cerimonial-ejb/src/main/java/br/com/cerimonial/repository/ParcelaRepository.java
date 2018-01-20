/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Parcela;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class ParcelaRepository extends AbstractRepository<Parcela>{
    
    public ParcelaRepository(EntityManager entityManager) {
        super(entityManager, Parcela.class);
    }
    
}
