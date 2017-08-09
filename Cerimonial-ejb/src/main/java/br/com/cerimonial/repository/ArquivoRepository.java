/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Arquivo;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class ArquivoRepository extends AbstractRepository<Arquivo>{

    public ArquivoRepository(EntityManager entityManager) {
        super(entityManager, Arquivo.class);
    }
    
    
    
}
