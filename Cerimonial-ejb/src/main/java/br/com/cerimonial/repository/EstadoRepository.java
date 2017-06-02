/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Estado;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class EstadoRepository extends BasicRepository{

    public EstadoRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public Estado getEntity(Long id) throws Exception {
       return getEntity(Estado.class, id);
    }

    public List<Estado> findAll() throws Exception {
        return getPureList(Estado.class, "select estado from Estado estado");
    }

    public Estado findBySigla(String uf) throws Exception {
        return getPurePojo(Estado.class, "select estado from Estado estado where estado.sigla = ?1", uf);
    }

    public Estado create(Estado entity) throws Exception{
        addEntity(Estado.class, entity);
        return entity;
    }

    public Estado edit(Estado entity) throws Exception{
        return setEntity(Estado.class, entity);
    }
    
}
