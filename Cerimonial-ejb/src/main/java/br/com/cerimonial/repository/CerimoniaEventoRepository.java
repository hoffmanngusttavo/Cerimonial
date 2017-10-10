/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.CerimoniaEvento;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class CerimoniaEventoRepository extends AbstractRepository<CerimoniaEvento>{

    public CerimoniaEventoRepository(EntityManager entityManager) {
        super(entityManager, CerimoniaEvento.class);
    }
 
    
    
}
