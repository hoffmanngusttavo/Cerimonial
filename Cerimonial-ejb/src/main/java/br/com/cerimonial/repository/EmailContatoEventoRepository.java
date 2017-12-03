/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.EmailContatoEvento;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class EmailContatoEventoRepository extends AbstractRepository<EmailContatoEvento>{

    public EmailContatoEventoRepository(EntityManager entityManager) {
        super(entityManager, EmailContatoEvento.class);
    }

    public List<EmailContatoEvento> findAllByContato(ContatoEvento contatoEvento) {
        
        StringBuilder sql = new StringBuilder("select email from EmailContatoEvento email  ");
        sql.append("INNER JOIN email.contatoEvento contato");
        sql.append(" where 1=1");
        sql.append(" and contato.id = ?1");
        
        return getPureList(EmailContatoEvento.class, sql.toString(), contatoEvento.getId());
        
    }
    
}
