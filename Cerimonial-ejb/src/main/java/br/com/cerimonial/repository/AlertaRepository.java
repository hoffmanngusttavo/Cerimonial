/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Alerta;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class AlertaRepository extends AbstractRepository<Alerta>{

    public AlertaRepository(EntityManager entityManager) {
        super(entityManager, Alerta.class);
    }
    
    
    
}
