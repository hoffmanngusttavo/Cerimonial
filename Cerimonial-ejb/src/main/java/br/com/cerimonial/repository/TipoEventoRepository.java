/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.TipoEvento;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class TipoEventoRepository extends AbstractRepository<TipoEvento> {

    public TipoEventoRepository(EntityManager entityManager) {
        super(entityManager, TipoEvento.class);
    }

    public TipoEvento findTipoEventoByEvento(Evento evento) {

        StringBuilder sql = new StringBuilder("SELECT tipo FROM TipoEvento tipo  ");
        sql.append(" INNER JOIN tipo.contatosEventos contatos");
        sql.append(" INNER JOIN contatos.propostas propostas");
        sql.append(" INNER JOIN propostas.eventos eventos");
        sql.append(" WHERE 1=1");
        sql.append(" AND eventos.id = ?1");

        return getPurePojo(TipoEvento.class, sql.toString(), evento.getId());

    }

}
