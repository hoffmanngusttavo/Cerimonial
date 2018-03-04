/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.AlertaDestinatario;
import br.com.cerimonial.entity.Compromisso;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.CompromissoRepository;
import br.com.cerimonial.utils.DateUtils;
import java.util.Date;
import java.util.List;
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
public class CompromissoService extends BasicService<Compromisso> {

    private CompromissoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new CompromissoRepository(em);
    }

    @Override
    public Compromisso findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Compromisso save(Compromisso entity) throws Exception {

        validateObjectNull(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }

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
    public void validateObjectNull(Compromisso entity) {

        if (entity == null) {
            throw new GenericException(" Compromisso nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    @Override
    public void validateObjectAndIdNull(Compromisso entity) {

        validateObjectNull(entity);

        validateId(entity.getId());

    }
    
    /**
     * @param limit
     * @param data
     * @return 
     * @throws java.lang.Exception
     */
    public List<Compromisso> findCompromissosDia(Date data) throws Exception {

        if(data == null){
            data  = new Date();
        }
        
        return repository.findCompromissosDia(DateUtils.formatDate(data, DateUtils.format_dd_MM_yyyy_ptBR));

    }
    
    

}
