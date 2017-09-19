/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ContratoEvento;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class ContratoEventoRepository extends AbstractRepository<ContratoEvento> {

    public ContratoEventoRepository(EntityManager entityManager) {
        super(entityManager, ContratoEvento.class);
    }

    public List<ContratoEvento> getContratosByEvento(Long idEvento) {

        StringBuilder sql = new StringBuilder("select contrato from ContratoEvento contrato  ");
        sql.append("INNER JOIN contrato.evento evento");
        sql.append(" where 1=1");
        sql.append(" and evento.id = ?1");
        
        return getPureList(ContratoEvento.class, sql.toString(), idEvento);
    }

}
