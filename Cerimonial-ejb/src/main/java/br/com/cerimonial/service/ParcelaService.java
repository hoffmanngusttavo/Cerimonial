/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.entity.Parcela;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.ParcelaRepository;
import br.com.cerimonial.utils.CollectionUtils;
import br.com.cerimonial.utils.DateUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class ParcelaService extends BasicService<Parcela> {

    private ParcelaRepository repository;

    @EJB
    private LancamentoService lancamentoService;
    

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ParcelaRepository(em);
    }

    @Override
    public Parcela findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Parcela save(Parcela entity) throws Exception {

        validateObjectNull(entity);

        lancamentoService.validateObjectNull(entity.getLancamento());

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public List<Parcela> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ParcelaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(Parcela entity) throws Exception {

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

    public List<Parcela> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Parcela> findParcelasAPagarEvento(int max) {
        try {
            
            return repository.findParcelasAPagarEvento(max, DateUtils.formatDate(new Date(), DateUtils.format_dd_MM_yyyy_ptBR));
        
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public List<Parcela> findParcelasVencidasEvento(int max) {
        try {

            return repository.findParcelasVencidasEvento(max, DateUtils.formatDate(new Date(), DateUtils.format_dd_MM_yyyy_ptBR));

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

     public List<Parcela> findParcelasAPagarEmpresa(int max) {
        
         try {

            return repository.findParcelasAPagarEmpresa(max, DateUtils.formatDate(new Date(), DateUtils.format_dd_MM_yyyy_ptBR));

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return null;
         
    }

    public List<Parcela> findParcelasAReceberEmpresa(int max) {
        
        try {

            return repository.findParcelasAReceberEmpresa(max, DateUtils.formatDate(new Date(), DateUtils.format_dd_MM_yyyy_ptBR));

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return null;
        
    }

    public List<Parcela> findParcelasVencidasEmpresa(int max) {
        
        try {

            return repository.findParcelasVencidasEmpresa(max, DateUtils.formatDate(new Date(), DateUtils.format_dd_MM_yyyy_ptBR));

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
    public void validateObjectNull(Parcela entity) {

        if (entity == null) {
            throw new GenericException(" Parcela nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    @Override
    public void validateObjectAndIdNull(Parcela entity) {

        validateObjectNull(entity);

        validateId(entity.getId());

    }

    public List<Parcela> criarParcelasDeParcelas(List<Parcela> parcelas, Lancamento lancamento) throws Exception {
       
        List<Parcela> lista = null;
        
        if(CollectionUtils.isNotBlank(parcelas)){
            
            lista = new ArrayList<>();
            
            for (Parcela parcela : parcelas) {
                
                lista.add(parcela.clonar(lancamento));
                
            }
            
        }
        
        return lista;
    }

   

}
