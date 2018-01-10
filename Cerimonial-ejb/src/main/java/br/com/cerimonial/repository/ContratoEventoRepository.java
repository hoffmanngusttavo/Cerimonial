/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ContratoEvento;
import br.com.cerimonial.entity.Pessoa;
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

    
    /**
     * Método monta sql para buscar ao banco o contrato a partir de um id do Evento
     * @param idEvento do Evento
     * @return
     */
    public List<ContratoEvento> getContratosByEvento(Long idEvento) {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT contrato FROM ContratoEvento contrato");
        sql.append(" INNER JOIN contrato.evento evento");
        sql.append(" WHERE 1=1");
        sql.append(" AND evento.id = ?1");
        
        return getPureList(ContratoEvento.class, sql.toString(), idEvento);
    }
    
    /**
     * Método monta sql para buscar ao banco o contrato de um contratante
     * a partir de um id do Evento
     * @param idEvento do Evento
     * @param contratante
     * @return
     */
    public List<ContratoEvento> getContratoByEventoContratante(Long idEvento, Pessoa contratante) {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT contrato FROM ContratoEvento contrato");
        sql.append(" INNER JOIN contrato.evento evento");
        sql.append(" INNER JOIN evento.contratantes con");
        sql.append(" INNER JOIN con.pessoa cliente");
        sql.append(" WHERE 1=1");
        sql.append(" AND evento.id = ?1");
        sql.append(" AND cliente.id = ?2");
        
        return getPureList(ContratoEvento.class, sql.toString(), idEvento, contratante.getId());
    }

}
