/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.LancamentoRepository;
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
public class LancamentoService extends BasicService<Lancamento> {

    private LancamentoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new LancamentoRepository(em);
    }

    @Override
    public Lancamento getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Lancamento save(Lancamento entity) throws Exception {

        isValid(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public List<Lancamento> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(LancamentoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(Lancamento categoria) throws Exception {

        isValid(categoria);

        repository.delete(categoria.getId());
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Lancamento> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean isValid(Lancamento entity) {
        if (entity == null) {
            throw new GenericException("Lançamento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

    
    /**
     * Vai buscar um lançamento que foi gerado a partir de um orçamento
     * carregando em lazy as parcelas
     * @param idOrcamento
     * @return 
     */
    public Lancamento findLancamentoOrcamento(Long idOrcamento) {
        
        if (idOrcamento == null) {
            throw new GenericException("Id Orçamento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
        Lancamento lancamento = repository.findLancamentoOrcamento(idOrcamento);
        
        if(lancamento != null){
                
            if(lancamento.getParcelas() != null){
                lancamento.getParcelas().size();
            }
            
        }
        
        return lancamento;
    }

}
