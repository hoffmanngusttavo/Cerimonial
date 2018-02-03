/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.CustoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.CustoEventoRepository;
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
    
    @EJB
    protected OrcamentoEventoService orcamentoEventoService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new CustoEventoRepository(em);

    }

    @Override
    public CustoEvento findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
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

            if (custo.getLancamentos() != null) {
                
                custo.getLancamentos().size();
                
                for (Lancamento lancamento : custo.getLancamentos()) {
                    
                    if (lancamento.getParcelas() != null) {
                        
                        lancamento.getParcelas().size();
                    }
                }
            }
        }
        
        return custo;
    }

    /**
     * Só vai instanciar um novo custo de evento
     * @param orcamento
     * @param evento
     * @return 
     */
    public CustoEvento criarCustoEvento(OrcamentoEvento orcamento, Evento evento) {
        
        orcamentoEventoService.validateObjectNull(orcamento);
        
        eventoService.validateObjectNull(evento);
        
        CustoEvento custoEvento = evento.getCustoEvento();
        
        if(custoEvento == null){
            custoEvento = new CustoEvento(evento);
        }
        
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
    
}
