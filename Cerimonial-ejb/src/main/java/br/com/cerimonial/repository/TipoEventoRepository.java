/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.TipoEvento;
import br.com.cerimonial.utils.ModelFilter;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class TipoEventoRepository extends BasicRepository{

    public TipoEventoRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public TipoEvento getEntity(Long id) throws Exception {
        return getEntity(TipoEvento.class, id);
    }

    public TipoEvento create(TipoEvento entity) throws Exception {
        addEntity(TipoEvento.class, entity);
        return entity;
    }

    public TipoEvento edit(TipoEvento entity) throws Exception {
        return setEntity(TipoEvento.class, entity);
    }

    public void delete(TipoEvento entity) throws Exception {
        removeEntity(entity);
    }
   
    public List<TipoEvento> findRangeListagemCategorias(int max, int offset, String sortField, String sortAscDesc) throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(TipoEvento.class);
        modelFilter.setLimit(max);
        modelFilter.setOffSet(offset);
        if (sortField != null) {
            modelFilter.addOrderBy(sortField, sortAscDesc);
        }

        return getPureListRange(TipoEvento.class, modelFilter.getSqlBase(), max, offset);
    }
    
    public List<TipoEvento> findAll() throws Exception {
        return getPureList(TipoEvento.class, "select cat from TipoEvento cat ");
    }

    public int countAll() throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(TipoEvento.class);
        return getCount(modelFilter.getSqlCountBase());
    }
    
}
