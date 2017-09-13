/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

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

    public abstract T getEntity(Long id) throws Exception;
    
    public abstract T save(T entity) throws Exception;
    
    public abstract boolean isValid(T entity);
    
}
