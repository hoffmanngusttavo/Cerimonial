/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ModelInterface;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Gustavo Hoffmann
 * @param <T>
 */
abstract class BasicService<T> implements Serializable {

    @PersistenceContext(unitName = "CerimonialPU")
    protected EntityManager em;

    public abstract T findEntityById(Long id) throws Exception;

    public abstract T save(T entity) throws Exception;

    public void validateObject(T entity) {

        if (entity == null) {
            throw new GenericException(entity +"Objeto nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

    }
    
    public void validateObjectAndId(T entity) {

        validateObject(entity);
        
        if (((ModelInterface)entity).getId()  == null) {
            throw new GenericException(entity +"Id nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

    }

   
}
