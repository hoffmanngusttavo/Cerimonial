/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.PreEvento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.PreEventoRepository;
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
public class PreEventoService extends BasicService<PreEvento> {
    
    private PreEventoRepository repository;
    
    
    
    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new PreEventoRepository(em);
    }

    @Override
    public PreEvento findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }
    
    public PreEvento findEntityById(Long id, List<String> pathsLazy) throws Exception {
        
        PreEvento entity = repository.getEntity(id);
        
        smartLazy(entity, pathsLazy);
        
        return entity;
    }
    
    @Override
    public PreEvento save(PreEvento entity) throws Exception {
        
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
    public void validateObjectNull(PreEvento entity) {

        if (entity == null) {
            throw new GenericException(" Pre Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    @Override
    public void validateObjectAndIdNull(PreEvento entity) {

        validateObjectNull(entity);

        validateId(entity.getId());

    }

    
    
     /**
     * Vai criar um evento a partir de uma proposta aceita. Vai Criar um
     * cliente. Vai criar o evento do cliente
     *
     * @param entity
     * @throws java.lang.Exception
     */
    public void criarEvento(PreEvento preEvento) throws Exception {

//        validateObjectNull(entity);
//
//        contatoEventoService.validateObjectNull(entity.getContatoEvento());
//
//        if (!entity.isPropostaAceita()) {
//            throw new GenericException("Proposta não aceita", ErrorCode.BAD_REQUEST.getCode());
//        }
//
//        Pessoa cliente = pessoaService.criarClienteFromContato(entity);
//        Evento evento = eventoService.criarEventoFromOrcamento(entity);
//        CustoEvento custoEvento = custoEventoService.criarCustoEvento(entity, evento);
//        EventoPessoa eventoPessoa = eventoPessoaService.criarContratanteEvento(evento, cliente);
//
//        //salvar em cascata
//        pessoaService.saveCliente(cliente);
//        eventoService.save(evento);
//        eventoPessoaService.save(eventoPessoa);
//        custoEventoService.save(custoEvento);

    }

   
    
}
