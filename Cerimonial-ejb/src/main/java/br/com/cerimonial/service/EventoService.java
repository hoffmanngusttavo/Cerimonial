/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.repository.EventoRepository;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.utils.CerimonialUtils;
import java.util.ArrayList;
import java.util.Date;
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
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EventoService extends BasicService<Evento> {

    private EventoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EventoRepository(em);
    }

    @Override
    public Evento getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Evento save(Evento entity) throws Exception {

        isValid(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }

    }

    public List<Evento> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(EventoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(Evento categoria) throws Exception {

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

    public List<Evento> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Evento>();
    }

    public List<Evento> findEventosDia(Date dataSelecionada) {
        try {
            if (dataSelecionada == null) {
                return new ArrayList<Evento>();
            }
            return repository.findEventosDia(dataSelecionada);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Evento>();
    }

    /**
     * Vai retornar todos os eventos ativos de acordo com o limit
     *
     * @param limit
     * @return
     */
    public List<Evento> findEventosAtivos(int limit) {
        try {
            return repository.findEventosAtivos(limit);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Evento>();
    }

    /**
     * Vai retornar todos os eventos ativos do cliente
     *
     * @param cliente
     * @return
     */
    public List<Evento> findEventosAtivosCliente(Pessoa cliente) {

        if (cliente == null || cliente.getId() == null) {
            throw new GenericException("Não foi possível carregar os eventos, cliente nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        return repository.findEventosAtivosCliente(cliente);

    }

    public Evento criarEventoFromOrcamento(OrcamentoEvento orcamento, Pessoa cliente) throws Exception {

        if (orcamento == null) {
            throw new GenericException("Orçamento Nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        Evento evento = this.getEventoByOrcamento(orcamento);

        if (evento == null) {
            evento = new Evento();
            evento.setDataInicio(orcamento.getContatoEvento().getDataEvento());
            evento.setDataTermino(orcamento.getContatoEvento().getDataEvento());
            evento.setHoraInicio(orcamento.getContatoEvento().getHoraEvento());
            evento.setQuantidadeConvidados(orcamento.getContatoEvento().getQuantidadeConvidados());
        }

        evento.setContratante(cliente);
        evento.setNome(orcamento.getContatoEvento().getNomeEvento());
        evento.setOrcamentoEvento(orcamento);
        evento.setTipoEvento(orcamento.getContatoEvento().getTipoEvento());

        return evento;
    }

    public Evento getEventoByOrcamento(OrcamentoEvento orcamento) throws Exception {

        if (orcamento == null || orcamento.getId() == null) {
            throw new GenericException("Orçamento nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        List<Evento> eventos = repository.getEventosByOrcamento(orcamento);

        if (CerimonialUtils.isListNotBlank(eventos)) {
            return eventos.get(0);
        }

        return null;

    }

    public Evento getEventoByContatoInicial(ContatoEvento contatoEvento) throws Exception {

        if (contatoEvento == null || contatoEvento.getId() == null) {
            throw new GenericException("Contato nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        List<Evento> eventos = repository.getEventosByContatoEvento(contatoEvento);

        if (CerimonialUtils.isListNotBlank(eventos)) {
            return eventos.get(0);
        }

        return null;

    }

    @Override
    public boolean isValid(Evento entity) {
        if (entity == null) {
            throw new GenericException("Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

    /**
     * Vai retornar o evento que pertence a somente esse cliente Carregar em
     * lazy o contrato
     *
     * @param idEvento
     * @param contratante
     * @return
     */
    public Evento getEventoByIdEventoContratante(Long idEvento, Pessoa contratante) {

        if (idEvento == null) {
            throw new GenericException("Id Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (contratante == null || contratante.getId() == null) {
            throw new GenericException("Cliente contratante nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        Evento evento = repository.getEventoByIdEventoContratante(idEvento, contratante);

        if (evento != null) {
            if (evento.getContrato() != null) {
                evento.getContrato().getId();
            }
        }

        return evento;

    }
    
    /**
     * Vai retornar o evento que pertence a somente esse cliente Carregar em
     * lazy o cerimonia, festa, tipo evento
     *
     * @param idEvento
     * @param contratante
     * @return
     */
    public Evento getEventoLocalizacao(Long idEvento, Pessoa contratante) {

        if (idEvento == null) {
            throw new GenericException("Id Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (contratante == null || contratante.getId() == null) {
            throw new GenericException("Cliente contratante nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        Evento evento = repository.getEventoByIdEventoContratante(idEvento, contratante);

        if (evento != null) {
            if (evento.getCerimoniaEvento() != null) {
                evento.getCerimoniaEvento().getId();
            }
            
            if (evento.getFestaCerimonia() != null) {
                evento.getFestaCerimonia().getId();
            }
            
            if (evento.getTipoEvento() != null) {
                evento.getTipoEvento().getId();
            }
        }

        return evento;

    }

}
