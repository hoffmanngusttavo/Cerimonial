/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.enums.SituacaoEvento;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class EventoRepository extends AbstractRepository<Evento> {

    public EventoRepository(EntityManager entityManager) {
        super(entityManager, Evento.class);
    }

    /**
     * Vai retornar todos os eventos do dia
     *
     * @param dataSelecionada
     * @return
     */
    public List<Evento> findEventosDia(Date dataSelecionada) {
        try {
            return getPureList(Evento.class, "select event from Evento event where event.dataInicio = ?1", dataSelecionada);
        } catch (Exception ex) {
            Logger.getLogger(EventoRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Evento>();
    }

    /**
     * Vai retornar todos os eventos ativos
     *
     * @param limit
     * @return
     */
    public List<Evento> findEventosAtivos(int limit) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT event FROM Evento event ");
            sb.append("WHERE event.situacaoEvento = ");
            sb.append(SituacaoEvento.ATIVO);

            return getPureListRange(Evento.class, sb.toString(), limit, 0);
        } catch (Exception ex) {
            Logger.getLogger(EventoRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Evento>();
    }

    /**
     * Vai retornar todos os eventos ativos de um cliente
     *
     * @param cliente
     * @return
     */
    public List<Evento> findEventosAtivosCliente(Pessoa cliente) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT eve FROM Evento eve ");
        sb.append("INNER JOIN eve.contratante cli ");
        sb.append("WHERE eve.situacaoEvento = ?1 ");
        sb.append("AND cli.id = ?2 ");

        return getPureList(Evento.class, sb.toString(), SituacaoEvento.ATIVO, cliente.getId());
    }

    /**
     * Recuperar um evento a partir de um orçamento
     *
     * @param orcamento
     * @return
     */
    public List<Evento> getEventosByOrcamento(OrcamentoEvento orcamento) {

        try {
            return getPureList(Evento.class, "select eve from Evento eve where eve.orcamentoEvento.id = ?1", orcamento.getId());
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    /**
     * Recuperar um evento a partir de um contato
     *
     * @param contato
     * @return
     */
    public List<Evento> getEventosByContatoEvento(ContatoEvento contato) {
        try {
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT eve FROM Evento eve ");
            sb.append("INNER JOIN eve.orcamentoEvento orc ");
            sb.append("INNER JOIN orc.contatoEvento con ");
            sb.append("WHERE 1=1 ");
            sb.append("AND con.id =?1");

            return getPureList(Evento.class, sb.toString(), contato.getId());

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    /**
     * Vai retornar o evento que pertence a somente esse cliente
     *
     * @param idEvento
     * @param contratante
     * @return
     */
    public Evento getEventoByIdEventoContratante(Long idEvento, Pessoa contratante) {

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT eve FROM Evento eve ");
        sb.append("INNER JOIN eve.contratante con ");
        sb.append("WHERE 1=1 ");
        sb.append("AND eve.id =?1 ");
        sb.append("AND con.id =?2");

        return getPurePojo(Evento.class, sb.toString(), idEvento, contratante.getId());
    }

}
