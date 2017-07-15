/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Login;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class LoginRepository extends AbstractRepository<Login> {

    public LoginRepository(EntityManager entityManager) {
        super(entityManager, Login.class);
    }

}
