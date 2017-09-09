/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.entity.Usuario;
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
     * @param dataSelecionada
     * @return 
     */
    public List<Evento> findEventosDia(Date dataSelecionada)  {
        try {
            return getPureList(Evento.class, "select event from Evento event where event.dataEvento = ?1", dataSelecionada);
        } catch (Exception ex) {
            Logger.getLogger(EventoRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Evento>();
    }

    /**
     * Recuperar um evento a partir de um orçamento
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

}
