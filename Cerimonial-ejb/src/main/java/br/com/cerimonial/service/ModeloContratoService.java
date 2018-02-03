/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ModeloContrato;
import br.com.cerimonial.enums.TipoEvento;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.repository.ModeloContratoRepository;
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

/**
 *
 * @author hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ModeloContratoService extends BasicService<ModeloContrato> {

    private ModeloContratoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ModeloContratoRepository(em);
    }

    @Override
    public ModeloContrato findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public ModeloContrato save(ModeloContrato entity) throws Exception {

        validateObjectNull(entity);

        //salvar ModeloContrato
        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public List<ModeloContrato> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ModeloContratoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(ModeloContrato entity) throws Exception {

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

    public List<ModeloContrato> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }



    public List<ModeloContrato> findModelosContratoByTipoEvento(TipoEvento tipoEvento) {

        if (tipoEvento == null) {
            throw new GenericException("Tipo Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        return repository.findModelosContratoByTipoEvento(tipoEvento);

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
    public void validateObjectNull(ModeloContrato entity) {
        
         if (entity == null) {
            throw new GenericException(" Modelo Contrato nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(ModeloContrato entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }

}
