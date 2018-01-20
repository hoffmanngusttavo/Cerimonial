/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.CustoEvento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.CustoEventoRepository;
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

/**
 *
 * @author hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CustoEventoService extends BasicService<CustoEvento>{
    
    private CustoEventoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new CustoEventoRepository(em);

    }

    @Override
    public CustoEvento getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public CustoEvento save(CustoEvento entity) throws Exception {

        isValid(entity);

        return repository.create(entity);
    }

    public List<CustoEvento> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {
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
    public boolean isValid(CustoEvento entity) {
        if (entity == null) {
            throw new GenericException("Custo Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (entity.getEvento() == null) {
            throw new GenericException("Evento de Custo Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
        return true;
    }

    
    
}
