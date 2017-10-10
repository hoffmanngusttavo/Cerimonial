/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.FestaCerimonia;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class FestaCerimoniaRepository extends AbstractRepository<FestaCerimonia>{

    public FestaCerimoniaRepository(EntityManager entityManager) {
        super(entityManager, FestaCerimonia.class);
    }
    
    
    
}
