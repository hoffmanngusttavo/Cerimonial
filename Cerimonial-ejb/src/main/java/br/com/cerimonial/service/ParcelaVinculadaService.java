/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.entity.Parcela;
import br.com.cerimonial.entity.ParcelaVinculada;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.ParcelaVinculadaRepository;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.List;
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
public class ParcelaVinculadaService extends BasicService<ParcelaVinculada> {

    private ParcelaVinculadaRepository repository;

    @EJB
    protected LancamentoService lancamentoService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ParcelaVinculadaRepository(em);
    }

    @Override
    public ParcelaVinculada findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public ParcelaVinculada save(ParcelaVinculada entity) throws Exception {
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
    public void validateObjectNull(ParcelaVinculada entity) {

        if (entity == null) {
            throw new GenericException(" Parcela nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    @Override
    public void validateObjectAndIdNull(ParcelaVinculada entity) {

        validateObjectNull(entity);

        validateId(entity.getId());

    }

    public void vincularParcelas(List<Parcela> parcelasDespesa, List<Parcela> parcelasReceita) throws Exception {

        if(CollectionUtils.isNotBlank(parcelasDespesa) 
                && CollectionUtils.isNotBlank(parcelasReceita)){
        
            if(parcelasDespesa.size() != parcelasReceita.size()){
                throw new Exception("Numero de parcelas deve ser igual para serem vinculadas");
            }
            
            for (Parcela parcelaDespesa : parcelasDespesa) {
                
                for (Parcela parcelaReceita : parcelasReceita) {
                    
                    if(parcelaDespesa.getNumeroParcela() == parcelaReceita.getNumeroParcela()){
                        
                        ParcelaVinculada vinculada = new ParcelaVinculada(parcelaDespesa, parcelaReceita);
                        
                        save(vinculada);
                        break;
                    }
                    
                }
            }
        }
        
    }

}
