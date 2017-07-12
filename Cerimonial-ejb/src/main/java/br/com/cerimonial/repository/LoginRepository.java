/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Login;
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
 */
public class LoginRepository extends BasicRepository {

    public LoginRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public Login create(Login entity) throws Exception {
        addEntity(Login.class, entity);
        return entity;
    }

    public List<Login> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {
        try {
            ModelFilter modelFilter = ModelFilter.getInstance();
            modelFilter.setEntidade(Login.class);
            if (params != null) {
                modelFilter.addFilter(params);
            }
            modelFilter.setLimit(max);
            modelFilter.setOffSet(offset);
            if (sortField != null) {
                modelFilter.addOrderBy(sortField, sortAscDesc);
            }

            return getPureListRange(Login.class, modelFilter.getSqlBase(), max, offset);
        } catch (Exception ex) {
            Logger.getLogger(LoginRepository.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

}
