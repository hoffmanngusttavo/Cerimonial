/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ParcelaVinculada;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class ParcelaVinculadaRepository extends AbstractRepository<ParcelaVinculada>{

    public ParcelaVinculadaRepository(EntityManager entityManager) {
        super(entityManager, ParcelaVinculada.class);
    }
    
    
    
}
