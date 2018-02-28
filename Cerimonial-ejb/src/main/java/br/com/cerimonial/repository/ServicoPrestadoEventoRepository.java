/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.ServicoPrestadoEvento;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class ServicoPrestadoEventoRepository extends AbstractRepository<ServicoPrestadoEvento>{

    public ServicoPrestadoEventoRepository(EntityManager entityManager) {
        super(entityManager, ServicoPrestadoEvento.class);
    }
    
    
      /**
     * Método vai buscar o orçamento de um evento;
     *
     * @param idEvento do Evento
     * @return
     */
    public ServicoPrestadoEvento findServicoPrestadoByEventoId(Long idEvento) {

        StringBuilder sql = new StringBuilder("SELECT orc FROM OrcamentoEvento orc  ");
        sql.append(" INNER JOIN orc.eventos evento");
        sql.append(" LEFT JOIN orc.modeloProposta mp");
        sql.append(" LEFT JOIN orc.anexos arq");
        sql.append(" WHERE 1=1");
        sql.append(" AND evento.id = ?1");

        return getPurePojo(ServicoPrestadoEvento.class, sql.toString(), idEvento);
    }
    
    
    public ServicoPrestadoEvento findServicoPrestadoByPreEventoId(Long idPreEvento) {

        StringBuilder sql = new StringBuilder("SELECT orc FROM OrcamentoEvento orc  ");
        sql.append(" INNER JOIN orc.eventos evento");
        sql.append(" LEFT JOIN orc.modeloProposta mp");
        sql.append(" LEFT JOIN orc.anexos arq");
        sql.append(" WHERE 1=1");
        sql.append(" AND evento.id = ?1");

        return getPurePojo(ServicoPrestadoEvento.class, sql.toString(), idPreEvento);
    }
    
    
    public ServicoPrestadoEvento findEntityByEventoId(Long idPreEvento) {

        StringBuilder sql = new StringBuilder("SELECT orc FROM OrcamentoEvento orc  ");
        sql.append(" INNER JOIN orc.eventos evento");
        sql.append(" LEFT JOIN orc.modeloProposta mp");
        sql.append(" LEFT JOIN orc.anexos arq");
        sql.append(" WHERE 1=1");
        sql.append(" AND evento.id = ?1");

        return getPurePojo(ServicoPrestadoEvento.class, sql.toString(), idPreEvento);
    }
    
    
    
    
    /**
     * Método vai buscar o orçamento de um evento de um contratante;
     *
     * @param idEvento do Evento
     * @param contratante
     * @return
     */
    public ServicoPrestadoEvento findServicoPrestadoByEventoIdAndContratante(Long idEvento, Pessoa contratante) {

        StringBuilder sql = new StringBuilder("SELECT orc FROM OrcamentoEvento orc  ");
        sql.append(" INNER JOIN orc.eventos evento");
        sql.append(" INNER JOIN evento.contratantes cli");
        sql.append(" INNER JOIN cli.pessoa con");
        sql.append(" LEFT JOIN orc.modeloProposta mp");
        sql.append(" LEFT JOIN orc.anexos arq");
        sql.append(" WHERE 1=1");
        sql.append(" AND evento.id = ?1");
        sql.append(" AND con.id = ?2");

        return getPurePojo(ServicoPrestadoEvento.class, sql.toString(), idEvento, contratante.getId());

    }
    
}
