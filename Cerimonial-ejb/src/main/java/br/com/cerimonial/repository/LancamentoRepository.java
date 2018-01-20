/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Lancamento;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class LancamentoRepository extends AbstractRepository<Lancamento> {

    public LancamentoRepository(EntityManager entityManager) {
        super(entityManager, Lancamento.class);
    }

}
