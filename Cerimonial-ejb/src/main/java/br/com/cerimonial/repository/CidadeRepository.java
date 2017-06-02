/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Cidade;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class CidadeRepository extends BasicRepository{

    public CidadeRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Cidade> findAll() throws Exception {
        return getPureList(Cidade.class, "select city from Cidade city");
    }

    public Cidade getEntity(Long id) throws Exception {
       return getEntity(Cidade.class, id);
    }
    
}
