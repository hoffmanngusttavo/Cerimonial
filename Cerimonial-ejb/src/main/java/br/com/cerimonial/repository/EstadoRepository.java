/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Estado;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class EstadoRepository extends AbstractRepository<Estado> {

    public EstadoRepository(EntityManager entityManager) {
        super(entityManager, Estado.class);
    }

    public Estado findBySigla(String uf) throws Exception {
        return getPurePojo(Estado.class, "select estado from Estado estado where estado.sigla = ?1", uf);
    }

}
