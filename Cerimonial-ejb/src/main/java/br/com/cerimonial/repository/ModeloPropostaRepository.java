/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class ModeloPropostaRepository extends BasicRepository{

    public ModeloPropostaRepository(EntityManager entityManager) {
        super(entityManager);
    }
    
    
}
