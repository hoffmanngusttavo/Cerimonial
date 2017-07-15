/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.utils.ModelFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 * @param <T>
 */
public class AbstractRepository<T> extends BasicRepository {

    private final Class<T> clazz;

    public AbstractRepository(EntityManager entityManager, Class<T> clazz) {
        super(entityManager);
        this.clazz = clazz;
    }

    public T create(T entity) throws Exception {
        addEntity(clazz, entity);
        return entity;
    }

    public T edit(T entity) throws Exception {
        return (T) setEntity(clazz, entity);
    }

    public T getEntity(Long id) throws Exception {
        return (T) getEntity(clazz, id);
    }

    public List<T> findAll() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(clazz.getSimpleName()).append(" emp ");
        return getPureListNative(clazz, sb.toString());
    }

    public int countAll() throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(clazz);
        return getCount(modelFilter.getSqlCountBase());
    }
    
    public int countListagem(HashMap<String, Object> filter) throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(clazz);
        modelFilter.addFilter(filter);
        return getCount(modelFilter.getSqlCountBase());
    }
    
    public int countListagem(ModelFilter modelFilter) throws Exception {
        modelFilter.setEntidade(clazz);
        return getCount(modelFilter.getSqlCountBase());
    }

    public void delete(T entity) throws Exception {
        removeEntity(entity);
    }

    public List<T> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) throws Exception {
        return findRangeListagem(null, max, offset, sortField, sortAscDesc);
    }

    public List<T> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(clazz);
        if (params != null) {
            modelFilter.addFilter(params);
        }
        modelFilter.setLimit(max);
        modelFilter.setOffSet(offset);
        if (sortField != null) {
            modelFilter.addOrderBy(sortField, sortAscDesc);
        }

        return findRangeListagem(modelFilter);
    }

    public List<T> findRangeListagem(ModelFilter modelFilter) throws Exception {
        try {
            modelFilter.setEntidade(clazz);
            return getPureListRange(clazz, modelFilter.getSqlBase(), modelFilter.getLimit(), modelFilter.getOffSet());
        } catch (Exception ex) {
            Logger.getLogger(LoginRepository.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

}
