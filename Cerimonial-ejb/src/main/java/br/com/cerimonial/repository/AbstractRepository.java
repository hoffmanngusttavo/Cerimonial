/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.exceptions.DAOException;
import br.com.cerimonial.exceptions.ErrorCode;
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

    public T create(T entity) {
        addEntity(clazz, entity);
        return entity;
    }

    public T edit(T entity) {
        return (T) setEntity(clazz, entity);
    }

    public T getEntity(Long id) {

        if (id == null) {
            throw new DAOException("O id é nulo", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (id <= 0) {
            throw new DAOException("O id precisa ser maior do que 0.", ErrorCode.BAD_REQUEST.getCode());
        }

        return (T) getEntity(clazz, id);
    }

    public List<T> findAll() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(clazz.getSimpleName()).append(" emp ");
        return getPureListNative(clazz, sb.toString());
    }

    public int countAll() {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(clazz);
        return getCount(modelFilter.getSqlCountBase());
    }

    public int countListagem(HashMap<String, Object> filter) {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(clazz);
        modelFilter.addFilter(filter);
        return getCount(modelFilter.getSqlCountBase());
    }

    public int countListagem(ModelFilter modelFilter) {
        modelFilter.setEntidade(clazz);
        return getCount(modelFilter.getSqlCountBase());
    }

    public void delete(Long id) {
        T entity = getEntity(id);
        removeEntity(entity);
    }

    public List<T> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        return findRangeListagem(null, max, offset, sortField, sortAscDesc);
    }

    public List<T> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {
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

    public List<T> findRangeListagem(ModelFilter modelFilter) {
        modelFilter.setEntidade(clazz);
        return getPureListRange(clazz, modelFilter.getSqlBase(), modelFilter.getLimit(), modelFilter.getOffSet());
    }

}
