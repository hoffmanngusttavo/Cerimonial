/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.CustoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.CustoEventoRepository;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.Arrays;
import java.util.HashMap;
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
public class CustoEventoService extends BasicService<CustoEvento> {

    private CustoEventoRepository repository;
    
    @EJB
    protected EventoService eventoService;
    
    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new CustoEventoRepository(em);

    }

    @Override
    public CustoEvento findEntityById(Long id) throws Exception {
        
        CustoEvento entity = repository.getEntity(id);
        
        return smartLazy(entity, Arrays.asList("lancamentos"));
    }

    @Override
    public CustoEvento save(CustoEvento entity) throws Exception {

        validateObjectNull(entity);
        
        eventoService.validateObjectNull(entity.getEvento());

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
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

    /**
     * Vai retornar o custo do evento carregando em lazy os lançamentos e
     * parcelas
     *
     * @param idEvento
     * @return
     */
    public CustoEvento findCustoEventoByIdEvento(Long idEvento) {

        eventoService.validateId(idEvento);

        CustoEvento custo = repository.findCustoEventoByIdEvento(idEvento);

        if (custo != null) {
            
            smartLazy(custo, Arrays.asList("evento","evento.preEvento", "lancamentos", "lancamentos.parcelas"));

        }
        
        return custo;
    }

    /**
     * Só vai instanciar um novo custo de evento
     * @param evento
     * @return 
     */
    public CustoEvento criarCustoEvento(Evento evento) {
        
        eventoService.validateObjectNull(evento);
        
        CustoEvento custoEvento = evento.getCustoEvento();
        
        if(custoEvento == null){
            custoEvento = new CustoEvento(evento);
        }

        evento.setCustoEvento(custoEvento);
        
        return custoEvento;
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
    public void validateObjectNull(CustoEvento entity) {
        
         if (entity == null) {
            throw new GenericException(" Custo Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(CustoEvento entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }
    
    public CustoEvento atualizarSalvarValoresCusto(CustoEvento custoEvento) throws Exception {
    
        validateObjectNull(custoEvento);
       
        if(custoEvento.getId() != null){
            custoEvento = this.findEntityById(custoEvento.getId());
        }
        
        atualizarValoresCusto(custoEvento);
        
        return save(custoEvento);
    }

    public void atualizarValoresCusto(CustoEvento custoEvento) throws Exception {
        
        validateObjectNull(custoEvento);
        
        Double valorEstimado = 0D;
        Double valorCustoReal = 0D;
        Double valorTotalPago = 0D;
        
        if(CollectionUtils.isNotBlank(custoEvento.getLancamentos())){
            
            for (Lancamento lancamento : custoEvento.getLancamentos()) {
                
                valorEstimado += lancamento.getValorEstimado() != null ? lancamento.getValorEstimado() : 0D;
                
                valorCustoReal += lancamento.getValorBase() != null ? lancamento.getValorBase() : 0D;
                
                valorTotalPago += lancamento.getValorTotalPago() != null ? lancamento.getValorTotalPago() : 0D;
                
            }
            
        }
        
        custoEvento.setValorCustoEstimado(valorEstimado);
        custoEvento.setValorCustoReal(valorCustoReal);
        custoEvento.setValorTotalPago(valorTotalPago);
        
    }
    
}
