/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Login;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.LoginRepository;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LoginService extends BasicService<Login> {

    private LoginRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new LoginRepository(em);

    }

    @Override
    public Login findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Login save(Login entity) throws Exception {

        validateObjectNull(entity);

        return repository.create(entity);
    }

    public List<Login> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {
        try {
            if (params == null) {
                params = new HashMap<>();
            }
            return repository.findRangeListagem(params, max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void validateId(Long idEntity) {

        if (idEntity == null) {
            throw new GenericException("Id nulo ", ErrorCode.BAD_REQUEST.getCode());
        }

        if (idEntity <= 0) {
            throw new GenericException("Id não pode ser menor ou igual a zero ", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    @Override
    public void validateObjectNull(Login entity) {

        if (entity == null) {
            throw new GenericException(" Login nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    @Override
    public void validateObjectAndIdNull(Login entity) {

        validateObjectNull(entity);

        validateId(entity.getId());

    }

    public void validateLogin(String login) {
        
        if (StringUtils.isBlank(login)) {
            throw new GenericException(" Login Inválido.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

}
