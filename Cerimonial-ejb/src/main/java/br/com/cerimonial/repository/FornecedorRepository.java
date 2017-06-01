/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Fornecedor;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class FornecedorRepository extends BasicRepository{

    public FornecedorRepository(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Fornecedor create(Fornecedor fornecedor) throws Exception {
        addEntity(Fornecedor.class, fornecedor);
        return fornecedor;
    }

    public void delete(Fornecedor fornecedor) throws Exception{
        removeEntity(fornecedor);
    }

    public Fornecedor edit(Fornecedor fornecedor) throws Exception{
        return setEntity(Fornecedor.class, fornecedor);
    }

    public Fornecedor getFornecedor(Long id) throws Exception{
        return getEntity(Fornecedor.class, id);
    }
    
}
