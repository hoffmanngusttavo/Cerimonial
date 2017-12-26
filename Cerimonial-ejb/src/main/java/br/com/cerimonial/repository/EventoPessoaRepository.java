/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Cidade;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class EventoPessoaRepository extends AbstractRepository<EventoPessoa> {

    public EventoPessoaRepository(EntityManager entityManager) {
        super(entityManager, EventoPessoa.class);
    }

    public EventoPessoa getEntityByEvento(Evento evento) {

        StringBuilder sql = new StringBuilder("SELECT entity from EventoPessoa entity  ");
        sql.append(" INNER JOIN entity.evento evento ");
        sql.append(" WHERE 1=1");
        sql.append(" AND evento.id = ?1");

        return getPurePojo(EventoPessoa.class, sql.toString(), evento.getId());

    }

}
