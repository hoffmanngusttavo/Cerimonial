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
public abstract class BasicService<T> implements Serializable {

    @PersistenceContext(unitName = "CerimonialPU")
    protected EntityManager em;

    public abstract T findEntityById(Long id) throws Exception;

    public abstract T save(T entity) throws Exception;

    public abstract void validateObjectNull(T entity); 
    
    public abstract void validateObjectAndIdNull(T entity); 
    
    public abstract void validateId(Long idEntity); 

   
}
