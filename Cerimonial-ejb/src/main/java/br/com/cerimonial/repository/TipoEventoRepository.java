/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.TipoEvento;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class TipoEventoRepository extends AbstractRepository<TipoEvento>{

    public TipoEventoRepository(EntityManager entityManager) {
        super(entityManager, TipoEvento.class);
    }

   
}