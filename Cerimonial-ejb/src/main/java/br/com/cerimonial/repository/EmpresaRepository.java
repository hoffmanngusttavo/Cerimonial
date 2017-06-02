/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Empresa;
import br.com.cerimonial.entity.Usuario;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class EmpresaRepository extends BasicRepository{

    public EmpresaRepository(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Empresa create(Empresa empresa) throws Exception {
        addEntity(Empresa.class, empresa);
        return empresa;
    }

    public Empresa edit(Empresa empresa) throws Exception{
        return setEntity(Empresa.class, empresa);
    }

    public Empresa getEntity(Long id) throws Exception{
        return getEntity(Empresa.class, id);
    }

    public List<Empresa> findAll() throws Exception {
        return getPureList(Empresa.class, "select emp from Empresa emp ");
        
        
    }
    
    
}
