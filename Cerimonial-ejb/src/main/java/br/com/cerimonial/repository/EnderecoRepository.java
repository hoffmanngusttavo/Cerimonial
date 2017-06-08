/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Endereco;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class EnderecoRepository extends BasicRepository{

    public EnderecoRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public Endereco create(Endereco entity) throws Exception {
        return addEntity(Endereco.class, entity);
    }

    public Endereco edit(Endereco entity) throws Exception {
        return setEntity(Endereco.class, entity);
    }
    
}
