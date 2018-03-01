/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.repository.ContatoEventoRepository;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContatoEventoService extends BasicService<ContatoEvento> {

    private ContatoEventoRepository repository;
    
    @EJB
    private EventoService eventoService;
    @EJB
    private PreEventoService preEventoService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ContatoEventoRepository(em);
    }

    /**
     * TODO[Ordenar tipo desc] Retorna contato pesquisado por um id e ordenado
     * as propostas de forma desc
     *
     * @param id
     * @return
     * @throws java.lang.Exception
     */
    @Override
    public ContatoEvento findEntityById(Long id) throws Exception {

        return repository.getEntity(id);
    }
    
    public ContatoEvento findEntityById(Long id, List<String> pathLazy) throws Exception {

        ContatoEvento entity = repository.getEntity(id);

        validateObjectNull(entity);

        smartLazy(entity, pathLazy);
        
        return entity;
    }
    
    @Override
    public ContatoEvento save(ContatoEvento entity) throws Exception {

        validateObjectNull(entity);
        
        preEventoService.validateObjectNull(entity.getPreEvento());

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public List<ContatoEvento> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ContatoEventoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(ContatoEvento contato) throws Exception {

        validateObjectAndIdNull(contato);

        repository.delete(contato.getId());
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * Método para retornar os contatos da listagem em lazy o status
     *
     * @param max
     * @param offset
     * @param sortField
     * @param sortAscDesc
     * @return
     */
    public List<ContatoEvento> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            List<ContatoEvento> findRangeListagem = repository.findRangeListagem(max, offset, sortField, sortAscDesc);
            return findRangeListagem;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<String> getLocaisEvento(String value) {
        
        if (StringUtils.isNotBlank(value)) {
            try {
                return repository.getLocaisEvento(value.toUpperCase());
            } catch (Exception ex) {
                Logger.getLogger(ContatoEventoService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

   

    public ContatoEvento getContatoInicialByEvento(Evento evento) {

        eventoService.validateObjectAndIdNull(evento);
        
        ContatoEvento entity = repository.getContatoInicialByEvento(evento);

        validateObjectNull(entity);
        
        smartLazy(entity, Arrays.asList("emailsContato"));

        return entity;

    }

    /**
     * Vai retornar todos os contatos ativos {NEGOCIANDO, REUNIAO_REALIZADA,
     * AGUARDANDO_RETORNO, CONTRATO_FECHADO} e que não tem evento criado
     *
     * @param limit
     * @return
     */
    public List<ContatoEvento> findContatosAtivos(int limit) {
        try {
            List<ContatoEvento> contatos = repository.findContatosAtivos(limit);
            if(CollectionUtils.isNotBlank(contatos)){
                for (ContatoEvento contato : contatos) {
                    smartLazy(contato, Arrays.asList("preEvento"));
                }
            }
            return contatos;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<ContatoEvento>();
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
    public void validateObjectNull(ContatoEvento entity) {
        
         if (entity == null) {
            throw new GenericException(" Contato Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(ContatoEvento entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }

    public ContatoEvento findEntityByPreEventoId(Long idPreEvento) {
        
        validateId(idPreEvento);
        
        return repository.findEntityByPreEventoId(idPreEvento);
        
    }
    
}
