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

        ContatoEvento entity = repository.getEntity(id);

        validateObjectNull(ContatoEvento.class, entity);

        if (entity.getTipoIndicacao() != null) {
            entity.getTipoIndicacao().getId();
        }

        if (entity.getEmailsContato() != null) {
            entity.getEmailsContato().size();
        }

        return entity;
    }

    @Override
    public ContatoEvento save(ContatoEvento entity) throws Exception {

        validateObjectNull(ContatoEvento.class, entity);

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

        validateObjectAndIdNull(ContatoEvento.class, contato);

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

        eventoService.validateObjectAndIdNull(Evento.class, evento);
        
        if (evento.getId() == null) {
            throw new GenericException("Evento nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        ContatoEvento entity = repository.getContatoInicialByEvento(evento);

        validateObjectNull(ContatoEvento.class, entity);

        if (entity.getEmailsContato() != null) {
            entity.getEmailsContato().size();
        }

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
            return repository.findContatosAtivos(limit);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<ContatoEvento>();
    }

}
