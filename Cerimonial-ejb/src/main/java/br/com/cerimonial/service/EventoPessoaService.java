/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.EventoPessoaRepository;
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
public class EventoPessoaService extends BasicService<EventoPessoa> {

    @EJB
    protected PessoaService pessoaService;

    @EJB
    protected EvolucaoPreenchimentoService preenchimentoService;

    @EJB
    protected AlertaService alertaService;
    
    @EJB
    protected UsuarioService usuarioService;

    private EventoPessoaRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EventoPessoaRepository(em);

    }

    @Override
    public EventoPessoa getEntity(Long id) throws Exception {
        EventoPessoa entity = repository.getEntity(id);

        if (entity != null && entity.getPessoa() != null) {

            if (entity.getPessoa().getContatosFamiliares() != null) {
                entity.getPessoa().getContatosFamiliares().size();
            }

            if (entity.getPessoa().getEndereco() != null) {
                entity.getPessoa().getEndereco().getId();
            }
        }

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

        isValid(entity);

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
     * @param entity
     * @return 
     * @throws java.lang.Exception
     */
    public EventoPessoa saveNoivo(EventoPessoa entity) throws Exception {

        isValid(entity);

        int porcentagemConcluidaAntesSalvar = entity.getPorcentagemPreenchimentoConcluida();

        preenchimentoService.validarPorcentagemPreenchimentoNoivo(entity);

        pessoaService.saveCliente(entity.getPessoa());

        if (entity.getId() == null) {
            repository.create(entity);
        } else {
            repository.edit(entity);
        }

        if (porcentagemConcluidaAntesSalvar < 100 && entity.getPorcentagemPreenchimentoConcluida() == 100) {
            // TODO
            // enviar alerta ao administrador que foi cloncluido preenchimento

        }

        return entity;
    }

    /**
     * Salvar os dados do contratante e validar a porcentagem de preenchimento
     * enviar alerta se alcançar os 100%.
     * @param entity
     * @return 
     * @throws java.lang.Exception
     */
    public EventoPessoa saveContratante(EventoPessoa entity) throws Exception {

        isValid(entity);

        int porcentagemConcluidaAntesSalvar = entity.getPorcentagemPreenchimentoConcluida();

        preenchimentoService.validarPorcentagemPreenchimentoContratante(entity);

        pessoaService.saveCliente(entity.getPessoa());

        if (entity.getId() == null) {
            repository.create(entity);
        } else {
            repository.edit(entity);
        }

        if (porcentagemConcluidaAntesSalvar < 100 && entity.getPorcentagemPreenchimentoConcluida() == 100) {

            enviarAlertaUsuarioAdminContratante();

        }

        return entity;
    }

     
    /**
     * enviar alerta ao administrador que foi concluido preenchimento.
     */ 
    private void enviarAlertaUsuarioAdminContratante() {

        try {
            
            List<Usuario> usuarios = usuarioService.findUsuariosAdminAtivos();
            
            String path = "";
            String titulo = "Dados do contratante";
            String mensagem = "Todos as informações obrigatórias do contratante foram preenchidas";
            
            alertaService.criarAlerta(path, titulo, mensagem, usuarios);
            
        } catch (Exception ex) {
            Logger.getLogger(EventoPessoaService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    /**
     * enviar alerta ao administrador que foi concluido preenchimento.
     */ 
    private void enviarAlertaUsuarioAdminDadosNoivo() {
        try {
            
            List<Usuario> usuarios = usuarioService.findUsuariosAdminAtivos();
            
            String path = "";
            String titulo = "Dados do noivo(a)";
            String mensagem = "Todos as informações obrigatórias do(a) noivo(a) foram preenchido(a)s";
            
            alertaService.criarAlerta(path, titulo, mensagem, usuarios);
            
        } catch (Exception ex) {
            Logger.getLogger(EventoPessoaService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public boolean isValid(EventoPessoa entity) {
        if (entity == null) {
            throw new GenericException("Evento Pessoa nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (entity.getPessoa() == null) {
            throw new GenericException("Contratate nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (entity.getEvento() == null) {
            throw new GenericException("Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
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

        if (evento == null) {
            throw new GenericException("Evento Nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        if (cliente == null) {
            throw new GenericException("Cliente Nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        EventoPessoa eventoPessoa = this.getEntityByEvento(evento);

        if (eventoPessoa == null) {
            eventoPessoa = new EventoPessoa(evento, cliente, evento.getOrcamentoEvento().getContatoEvento().getTipoEnvolvidoEvento(), true);
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

        if (evento == null) {
            throw new GenericException("Evento Nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        if (evento.getId() == null) {
            throw new GenericException("Id do Evento Nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        return repository.getEntityByEvento(evento);

    }

}
