/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.OrcamentoEvento;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class OrcamentoEventoRepository extends AbstractRepository<OrcamentoEvento>{

    public OrcamentoEventoRepository(EntityManager entityManager) {
        super(entityManager, OrcamentoEvento.class);
    }
    
    
}
