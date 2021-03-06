/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.EventoPessoaRepository;
import java.util.Arrays;
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
public class EventoPessoaService extends BasicService<EventoPessoa> {

    @EJB
    protected PessoaService pessoaService;

    @EJB
    protected EvolucaoPreenchimentoService preenchimentoService;

    @EJB
    protected AlertaService alertaService;

    @EJB
    protected UsuarioService usuarioService;
    
    @EJB
    protected EventoService eventoService;

    private EventoPessoaRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EventoPessoaRepository(em);

    }

    @Override
    public EventoPessoa findEntityById(Long id) throws Exception {

        return repository.getEntity(id);
        
    }
    
    public EventoPessoa findEntityById(Long id, List<String> pathsLazy) throws Exception {

        EventoPessoa entity = repository.getEntity(id);
        
        smartLazy(entity, pathsLazy);

        return entity;
    }

    /**
     * Vai apenas salvar o objeto, sem fazer validações
     *
     * @param entity
     * @return
     * @throws java.lang.Exception
     */
    @Override
    public EventoPessoa save(EventoPessoa entity) throws Exception {

        validateObjectNull(entity);
        
        eventoService.validateObjectNull(entity.getEvento());

        pessoaService.validateObjectNull(entity.getPessoa());

        preenchimentoService.validarPorcentagemPreenchimento(entity);

        pessoaService.saveCliente(entity.getPessoa());

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    /**
     * Salvar os dados do noivo(a) e validar a porcentagem de preenchimento
     * enviar alerta se alcançar os 100%.
     *
     * @param entity
     * @return
     * @throws java.lang.Exception
     */
    public EventoPessoa saveNoivo(EventoPessoa entity) throws Exception {

        validateObjectNull(entity);
        
        eventoService.validateObjectNull(entity.getEvento());

        pessoaService.validateObjectNull(entity.getPessoa());

        int porcentagemConcluidaAntesSalvar = entity.getPorcentagemPreenchimentoConcluida();

        preenchimentoService.validarPorcentagemPreenchimentoNoivo(entity);

        pessoaService.saveCliente(entity.getPessoa());

        if (entity.getId() == null) {
            repository.create(entity);
        } else {
            repository.edit(entity);
        }

        if (porcentagemConcluidaAntesSalvar < 100 && entity.getPorcentagemPreenchimentoConcluida() == 100) {

            alertaService.enviarAlertaUsuarioAdminDadosNoivo(entity);
        }

        return entity;
    }

    /**
     * Salvar os dados do contratante e validar a porcentagem de preenchimento
     * enviar alerta se alcançar os 100%.
     *
     * @param entity
     * @return
     * @throws java.lang.Exception
     */
    public EventoPessoa saveContratanteCliente(EventoPessoa entity) throws Exception {

        validateObjectNull(entity);
        
        eventoService.validateObjectNull(entity.getEvento());

        pessoaService.validateObjectNull(entity.getPessoa());

        int porcentagemConcluidaAntesSalvar = entity.getPorcentagemPreenchimentoConcluida();

        preenchimentoService.validarPorcentagemPreenchimentoContratante(entity);

        pessoaService.saveCliente(entity.getPessoa());

        if (entity.getId() == null) {
            repository.create(entity);
        } else {
            repository.edit(entity);
        }

        if (porcentagemConcluidaAntesSalvar < 100 && entity.getPorcentagemPreenchimentoConcluida() == 100) {

            alertaService.enviarAlertaUsuarioAdminContratante(entity);

        }

        return entity;
    }

    /**
     * Salvar os dados do contratante e validar a porcentagem de preenchimento
     * enviar alerta se alcançar os 100%.
     *
     * @param entity
     * @return
     * @throws java.lang.Exception
     */
    public EventoPessoa saveContratante(EventoPessoa entity) throws Exception {

        validateObjectNull(entity);
        
        eventoService.validateObjectNull(entity.getEvento());

        pessoaService.validateObjectNull(entity.getPessoa());

        preenchimentoService.validarPorcentagemPreenchimentoContratante(entity);

        pessoaService.saveCliente(entity.getPessoa());

        if (entity.getId() == null) {
            repository.create(entity);
        } else {
            repository.edit(entity);
        }

        return entity;
    }

    /**
     * Garante que o objeto terá somente um evento
     *
     * @param evento
     * @param cliente
     * @return
     * @throws java.lang.Exception
     */
    public EventoPessoa criarContratanteEvento(Evento evento, Pessoa cliente) throws Exception {

        eventoService.validateObjectNull(evento);
        
        pessoaService.validateObjectNull(cliente);
        
        EventoPessoa eventoPessoa = null;
        
        if (evento.getId() != null) {
            eventoPessoa = this.getEntityByEvento(evento);
        }

        if (eventoPessoa == null) {
            eventoPessoa = new EventoPessoa(evento, cliente, evento.getPreEvento().getContatoEvento().getTipoEnvolvidoEvento(), true);
        }

        preenchimentoService.validarPorcentagemPreenchimento(eventoPessoa);

        return eventoPessoa;
    }

    /**
     * Recupera o objeto EventoPessoa a partir de um evento
     *
     * @param evento
     * @return
     */
    public EventoPessoa getEntityByEvento(Evento evento) {

        eventoService.validateObjectAndIdNull(evento);
        
        return repository.getEntityByEvento(evento);

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
    public void validateObjectNull(EventoPessoa entity) {
        
         if (entity == null) {
            throw new GenericException(" Evento Pessoa nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(EventoPessoa entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }
    
}
