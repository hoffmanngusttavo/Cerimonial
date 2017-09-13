/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.exceptions.DAOException;
import br.com.cerimonial.exceptions.ErrorCode;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Gustavo Hoffmann
 * @param <T>
 */
abstract class BasicRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    private final EntityManager entityManager;

    public BasicRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected <T> T addEntity(Class<T> classToCast, Object entity) {
        try {
            getEntityManager().persist(entity);
        } catch (RuntimeException e) {
            throw new DAOException("Erro ao salvar no banco de dados: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        }

        return (T) entity;
    }

    protected <T> T getEntity(Class<T> classToCast, Serializable pk) {

        T find = null;

        try {

            find = getEntityManager().find(classToCast, pk);

        } catch (RuntimeException e) {

            throw new DAOException("Erro ao buscar por id no banco de dados: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());

        }

        if (find == null) {
            throw new DAOException("Objeto de id " + pk + " não existe.", ErrorCode.NOT_FOUND.getCode());
        }

        return find;
    }

    protected <T> T setEntity(Class<T> classToCast, Object entity) {

        Object updatedObj = null;
        try {

            updatedObj = getEntityManager().merge(entity);

        } catch (RuntimeException e) {
            throw new DAOException("Erro ao atualizar no banco de dados: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        }

        return (T) updatedObj;
    }

    protected void removeEntity(Object entity) {
        try {

            getEntityManager().remove(entity);

        } catch (IllegalArgumentException ex) {
            throw new DAOException("Objeto informado para remoção não existe: " + ex.getMessage(), ErrorCode.NOT_FOUND.getCode());
        } catch (RuntimeException ex) {
            throw new DAOException("Erro ao remover objeto do banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        }

    }

    protected <T> List<T> getPureList(Class<T> classToCast, String query, Object... values) {
        List resultList = null;
        try {
            Query qr = createQuery(query, values);
            resultList = qr.getResultList();
        } catch (RuntimeException e) {
            throw new DAOException("Erro ao recuperar os itens do banco: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        }

        return resultList;
    }

    protected <T> List<T> getPureList(Class<T> classToCast, String query) {
        List resultList = null;
        try {
            Query qr = createQuery(query, null);
            resultList = qr.getResultList();
        } catch (RuntimeException e) {
            throw new DAOException("Erro ao recuperar os itens do banco: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        }
        return resultList;
    }

    protected <T> List<T> getPureListNative(Class<T> classToCast, String query) {
        List resultList = null;
        try {
            Query qr = getEntityManager().createNativeQuery(query, classToCast);
            resultList = qr.getResultList();
        } catch (RuntimeException e) {
            throw new DAOException("Erro ao recuperar todos os itens do banco: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        }

        return resultList;
    }

    protected <T> List<T> getPureListNative(String query) {
        List resultList = null;
        try {
            Query qr = getEntityManager().createNativeQuery(query);
            resultList = qr.getResultList();
        } catch (RuntimeException e) {
            throw new DAOException("Erro ao recuperar todos os itens do banco: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        }
        return resultList;
    }

    protected <T> List<T> getPureListRange(Class<T> classToCast, String query, int limit, int offSet) {
        List resultList = null;
        try {
            Query qr = createQuery(query, limit, offSet);
            resultList = qr.getResultList();
        } catch (RuntimeException e) {
            throw new DAOException("Erro ao recuperar todos os itens do banco: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        }

        return resultList;
    }

    protected <T> T getPurePojo(Class<T> classToCast, String query, Object... values) {
        Object singleResult = null;
        try {
            Query qr = createQuery(query, values);
            singleResult = qr.getSingleResult();
        } catch (RuntimeException e) {
            throw new DAOException("Erro ao recuperar objeto do banco: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        }

        return (T) singleResult;
    }

    protected int executeCommand(String query, Object... values) {
        int executeUpdate = 0;
        try {
            Query qr = createQuery(query, values);
            executeUpdate = qr.executeUpdate();
        } catch (RuntimeException e) {
            throw new DAOException("Erro ao executar comando no banco: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        }

        return executeUpdate;
    }

    protected int getCount(String query) {
        int intValue = 0;
        try {
            Query qr = getEntityManager().createQuery(query);
            intValue = ((Number) qr.getSingleResult()).intValue();
        } catch (RuntimeException e) {
            throw new DAOException("Erro ao contabilizar objeto no banco: " + e.getMessage(), ErrorCode.SERVER_ERROR.getCode());
        }
        
        return intValue;
    }

    private Query createQuery(String query, Object... values) {
        Query qr = getEntityManager().createQuery(query);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                qr.setParameter((i + 1), values[i]);
            }
        }
        return qr;
    }

    private Query createQuery(String query, int limit, int offSet) {
        Query qr = getEntityManager().createQuery(query);
        if (limit > 0) {
            qr.setMaxResults(limit);
        }
        if (offSet > 0) {
            qr.setFirstResult(offSet);
        }
        return qr;
    }

}
