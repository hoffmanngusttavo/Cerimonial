/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Evento;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class EventoRepository extends AbstractRepository<Evento> {

    public EventoRepository(EntityManager entityManager) {
        super(entityManager, Evento.class);
    }

    public List<Evento> findEventosDia(Date dataSelecionada) throws Exception {
        return getPureList(Evento.class, "select event from Evento event where event.dataEvento = ?1", dataSelecionada);
    }

}
