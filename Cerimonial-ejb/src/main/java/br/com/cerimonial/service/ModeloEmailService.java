/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ModeloEmail;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.ModeloEmailRepository;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class ModeloEmailService extends BasicService<ModeloEmail>{
    
    private ModeloEmailRepository repository;
    
    @EJB
    private ArquivoService arquivoService;
    
    
    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ModeloEmailRepository(em);
    }

    @Override
    public ModeloEmail findEntityById(Long id) throws Exception {
        
        return repository.getEntity(id);
    }
    
    public ModeloEmail findEntityById(Long id, List<String> paths) throws Exception {
        
        ModeloEmail entity = repository.getEntity(id);
        
        smartLazy(entity, paths);
        
        return entity;
    }

    @Override
    public ModeloEmail save(ModeloEmail entity) throws Exception {

        validateObjectNull(entity);

        //salvar arquivo
        if (entity.getArquivo() != null) {
            //remover os arquivos antigos
            removerArquivosAntigosProposta(entity);
            arquivoService.save(entity.getArquivo());
        }

        //salvar ModeloEmail
        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    private void removerArquivosAntigosProposta(ModeloEmail entity) {

        if (entity.getArquivo().getId() == null && entity.getId() != null) {
            List<Arquivo> arquivosAntigos = arquivoService.getArquivosByModeloEmail(entity);
            if (CollectionUtils.isNotBlank(arquivosAntigos)) {
                arquivosAntigos.stream().forEach((arquivosAntigo) -> {
                    try {
                        arquivoService.delete(arquivosAntigo);
                    } catch (Exception ex) {
                        Logger.getLogger(ModeloEmailService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        }
    }

    public List<ModeloEmail> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ModeloEmailService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
   
    public void delete(ModeloEmail entity) throws Exception {

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

    public List<ModeloEmail> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
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
    public void validateObjectNull(ModeloEmail entity) {
        
         if (entity == null) {
            throw new GenericException(" Modelo Email nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(ModeloEmail entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }
    
}
