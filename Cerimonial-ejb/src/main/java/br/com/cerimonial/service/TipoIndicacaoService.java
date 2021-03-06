/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.TipoIndicacao;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.TipoIndicacaoRepository;
import java.util.ArrayList;
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
public class TipoIndicacaoService extends BasicService<TipoIndicacao> {

    private TipoIndicacaoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new TipoIndicacaoRepository(em);
    }

    @Override
    public TipoIndicacao findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public TipoIndicacao save(TipoIndicacao entity) throws Exception {

        validateObjectNull(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public List<TipoIndicacao> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(TipoIndicacaoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(TipoIndicacao entity) throws Exception {

        validateObjectAndIdNull(entity);

        repository.delete(entity.getId());
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<TipoIndicacao> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<TipoIndicacao> findTiposIndicacaoByNome(String value) {
        if (StringUtils.isNotBlank(value)) {
            try {
                return repository.findTiposIndicacaoByNome(value.toUpperCase());
            } catch (Exception ex) {
                Logger.getLogger(ContatoEventoService.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    public void validateObjectNull(TipoIndicacao entity) {
        
         if (entity == null) {
            throw new GenericException(" Tipo Indicacao nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(TipoIndicacao entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }

}
