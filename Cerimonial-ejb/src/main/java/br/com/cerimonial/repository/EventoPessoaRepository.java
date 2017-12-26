/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Cidade;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
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

        try {

            StringBuilder sql = new StringBuilder("SELECT entity from EventoPessoa entity  ");
            sql.append(" INNER JOIN entity.evento evento ");
            sql.append(" WHERE 1=1");
            sql.append(" AND evento.id = ?1");

            return getPurePojo(EventoPessoa.class, sql.toString(), evento.getId());

        } catch (Exception e) {
            GenericException exception = (GenericException) e;
            if(exception != null && exception.getMessage() != null && !exception.getMessage().contains("No entity found for query")){
                throw new GenericException("Erro ao recuperar objeto do banco: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
            }
        }
        
        return null;
    }

}
