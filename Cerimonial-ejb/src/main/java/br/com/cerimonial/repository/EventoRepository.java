/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.enums.SituacaoEvento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
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
            sb.append("WHERE event.situacaoEvento = '");
            sb.append(SituacaoEvento.ATIVO);
            sb.append("'");

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
        sb.append("INNER JOIN eve.contratantes cli ");
        sb.append("INNER JOIN cli.pessoa con ");
        sb.append("WHERE eve.situacaoEvento = ?1 ");
        sb.append("AND con.id = ?2 ");

        return getPureList(Evento.class, sb.toString(), SituacaoEvento.ATIVO, cliente.getId());
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
        sb.append("INNER JOIN eve.contratantes ep ");
        sb.append("INNER JOIN ep.pessoa con ");
        sb.append("WHERE 1=1 ");
        sb.append("AND eve.id =?1 ");
        sb.append("AND con.id =?2 ");
        sb.append("AND eve.situacaoEvento =?3");

        return getPurePojo(Evento.class, sb.toString(), idEvento, contratante.getId(), SituacaoEvento.ATIVO);
    }

    public Evento findEventoByServicoPrestadoId(Long idServicoPrestado) {
        
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT eve FROM Evento eve ");
        sb.append("INNER JOIN eve.preEvento pre ");
        sb.append("INNER JOIN pre.servicoPrestadoEvento ser ");
        sb.append("WHERE 1=1 ");
        sb.append("AND ser.id =?1 ");

        return getPurePojo(Evento.class, sb.toString(), idServicoPrestado);
        
    }
    
    public Evento findEventoByPreEventoId(Long idPreEvento) throws Exception{

        try {

            StringBuilder sb = new StringBuilder();

            sb.append("SELECT eve FROM Evento eve ");
            sb.append("INNER JOIN eve.preEvento preevento ");
            sb.append("WHERE 1=1 ");
            sb.append("AND preevento.id =?1 ");

            return getPurePojo(Evento.class, sb.toString(), idPreEvento);

        } catch (GenericException e) {

            if (e.getCode() != ErrorCode.NOT_FOUND.getCode()) {
                throw new Exception(e);
            }
        }

        return null;

    }

}
