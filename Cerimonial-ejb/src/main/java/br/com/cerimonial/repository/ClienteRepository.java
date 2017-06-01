/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Cliente;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class ClienteRepository extends BasicRepository{

    public ClienteRepository(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Cliente create(Cliente cliente) throws Exception {
        addEntity(Cliente.class, cliente);
        return cliente;
    }

    public void delete(Cliente cliente) throws Exception{
        removeEntity(cliente);
    }

    public Cliente edit(Cliente cliente) throws Exception{
        return setEntity(Cliente.class, cliente);
    }

    public Cliente getCliente(Long id) throws Exception{
        return getEntity(Cliente.class, id);
    }
    
    
}
