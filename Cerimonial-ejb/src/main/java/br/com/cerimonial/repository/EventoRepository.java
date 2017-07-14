/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.utils.ModelFilter;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class EventoRepository extends BasicRepository{

    public EventoRepository(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Evento getEntity(Long id) throws Exception {
        return getEntity(Evento.class, id);
    }

    public Evento create(Evento entity) throws Exception {
        addEntity(Evento.class, entity);
        return entity;
    }

    public Evento edit(Evento entity) throws Exception {
        return setEntity(Evento.class, entity);
    }

    public void delete(Evento entity) throws Exception {
        removeEntity(entity);
    }
   
    public List<Evento> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Evento.class);
        modelFilter.setLimit(max);
        modelFilter.setOffSet(offset);
        if (sortField != null) {
            modelFilter.addOrderBy(sortField, sortAscDesc);
        }

        return getPureListRange(Evento.class, modelFilter.getSqlBase(), max, offset);
    }
    
    public List<Evento> findAll() throws Exception {
        return getPureList(Evento.class, "select cat from Evento cat ");
    }

    public int countAll() throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Evento.class);
        return getCount(modelFilter.getSqlCountBase());
    }

    public List<Evento> findEventosDia(Date dataSelecionada) throws Exception {
        return getPureList(Evento.class, "select event from Evento event where event.dataEvento?1", dataSelecionada);
    }
    
}
