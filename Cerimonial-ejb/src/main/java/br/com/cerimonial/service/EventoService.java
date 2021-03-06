/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.CerimoniaEvento;
import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.CustoEvento;
import br.com.cerimonial.entity.Endereco;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.FestaCerimonia;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.PreEvento;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.enums.AcessoSistema;
import br.com.cerimonial.enums.SituacaoEvento;
import br.com.cerimonial.repository.EventoRepository;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class EventoService extends BasicService<Evento> {

    private EventoRepository repository;

    @EJB
    protected PessoaService pessoaService;
    @EJB
    protected EventoPessoaService eventoPessoaService;
    @EJB
    protected UsuarioService usuarioService;
    @EJB
    protected EvolucaoPreenchimentoService preenchimentoService;
    @EJB
    protected AlertaService alertaService;
    @EJB
    protected ContatoEventoService contatoEventoService;
    @EJB
    protected PreEventoService preEventoService;
    @EJB
    protected CustoEventoService custoEventoService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EventoRepository(em);
    }

    @Override
    public Evento findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    public Evento findEntityById(Long id, List<String> pathLazy) throws Exception {

        Evento entity = repository.getEntity(id);

        smartLazy(entity, pathLazy);

        return entity;
    }

    @Override
    public Evento save(Evento entity) throws Exception {

        validateObjectNull(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }

    }

    public Evento saveEventoCliente(Evento entity) throws Exception {

        validateObjectNull(entity);

        int porcentagemConcluidaAntesSalvar = entity.getPorcentagemPreenchimentoConcluida();

        preenchimentoService.validarPorcentagemPreenchimentoEvento(entity);

        entity = save(entity);

        if (porcentagemConcluidaAntesSalvar < 100 && entity.getPorcentagemPreenchimentoConcluida() == 100) {

            alertaService.enviarAlertaUsuarioAdminDadosEvento(entity);
        }

        return entity;

    }

    public Evento saveEventoAdmin(Evento entity) throws Exception {

        validateObjectNull(entity);

        preenchimentoService.validarPorcentagemPreenchimentoEvento(entity);

        return save(entity);

    }

    public List<Evento> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(EventoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(Evento entity) throws Exception {

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

    public List<Evento> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Evento>();
    }

    public List<Evento> findEventosDia(Date dataSelecionada) throws GenericException {

        if (dataSelecionada == null) {
            throw new GenericException("Data não pode ser nula", ErrorCode.BAD_REQUEST.getCode());
        }

        List<Evento> eventos = repository.findEventosDia(dataSelecionada);
        
        if(CollectionUtils.isNotBlank(eventos)){
        
            for (Evento evento : eventos) {
                smartLazy(evento, Arrays.asList("preEvento"));
            }
        }

        return eventos;
    }

    /**
     * Vai retornar todos os eventos ativos de acordo com o limit
     *
     * @param limit
     * @return
     */
    public List<Evento> findEventosAtivos(int limit) {
        try {
            List<Evento> eventos = repository.findEventosAtivos(limit);
            
            if(CollectionUtils.isNotBlank(eventos)){
                for (Evento evento : eventos) {
                    smartLazy(evento, Arrays.asList("preEvento"));
                }
            }
            
            return eventos;
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

        pessoaService.validateObjectAndIdNull(cliente);

        List<Evento> retorno = repository.findEventosAtivosCliente(cliente);

        if (CollectionUtils.isNotBlank(retorno)) {
            // fazer isso para nao ter dados duplicados
            Set<Evento> eventos = new HashSet<Evento>(retorno);

            retorno = new ArrayList<Evento>(eventos);
        }

        return retorno;

    }

    /**
     * Vai criar um evento a partir de uma proposta aceita. Vai Criar um
     * cliente. Vai criar o evento do cliente
     *
     * @param preEvento
     * @return
     * @throws java.lang.Exception
     */
    public Evento criarNovoEvento(PreEvento preEvento) throws Exception {

        preEventoService.validateObjectAndIdNull(preEvento);

        contatoEventoService.validateObjectNull(preEvento.getContatoEvento());

        Pessoa cliente = pessoaService.criarClienteFromContato(preEvento.getContatoEvento());

        Evento evento = this.criarEventoFromPreEvento(preEvento);

        CustoEvento custoEvento = custoEventoService.criarCustoEvento(evento);

        EventoPessoa eventoPessoa = eventoPessoaService.criarContratanteEvento(evento, cliente);

        //salvar em cascata
        pessoaService.saveCliente(cliente);
        this.save(evento);
        eventoPessoaService.save(eventoPessoa);
        custoEventoService.save(custoEvento);

        return evento;
    }

    public Evento criarEventoFromPreEvento(PreEvento preEvento) throws Exception {

        preEventoService.validateObjectAndIdNull(preEvento);

        Evento evento = this.findEventoByPreEventoId(preEvento.getId());

        if (evento == null) {
            evento = new Evento();
            evento.setDataInicio(preEvento.getContatoEvento().getDataEvento());
            evento.setDataTermino(preEvento.getContatoEvento().getDataEvento());
            evento.setHoraInicio(preEvento.getContatoEvento().getHoraEvento());
            evento.setQuantidadeConvidados(preEvento.getContatoEvento().getQuantidadeConvidados());
        }

        evento.setEventoProprioContratante(preEvento.getContatoEvento().isEventoProprioContratante());
        evento.setNome(preEvento.getContatoEvento().getNomeEvento());
        evento.setTipoEvento(preEvento.getContatoEvento().getTipoEvento());
        evento.setPreEvento(preEvento);

        return evento;
    }

    public Evento findEventoByServicoPrestadoId(Long idServicoPrestado) throws Exception {

        validateId(idServicoPrestado);

        Evento evento = repository.findEventoByServicoPrestadoId(idServicoPrestado);
        
        return smartLazy(evento, Arrays.asList("preEvento", "preEvento.servicoPrestado"));
    }

    public Evento findEventoByPreEventoId(Long idPreEvento) throws Exception {

        validateId(idPreEvento);

        return repository.findEventoByPreEventoId(idPreEvento);
    }
    
    public Evento findEventoByAtividadeId(Long idAtividade) throws Exception {

        validateId(idAtividade);

        return repository.findEventoByAtividadeId(idAtividade);
    }

    public Evento getEventoByContatoInicial(ContatoEvento contatoEvento) throws Exception {

        contatoEventoService.validateObjectAndIdNull(contatoEvento);

        List<Evento> eventos = repository.getEventosByContatoEvento(contatoEvento);

        if (CollectionUtils.isNotBlank(eventos)) {

            return eventos.get(0);
        }

        return null;

    }

    /**
     * Encontrar evento a partir de um contato inicial
     *
     * @param contatoEvento
     * @param pathsLazy
     * @return
     * @throws java.lang.Exception
     */
    public Evento getEventoByContatoInicial(ContatoEvento contatoEvento, List<String> pathsLazy) throws Exception {

        contatoEventoService.validateObjectAndIdNull(contatoEvento);

        List<Evento> eventos = repository.getEventosByContatoEvento(contatoEvento);

        if (CollectionUtils.isNotBlank(eventos)) {

            for (Evento evento : eventos) {

                smartLazy(evento, pathsLazy);

                return evento;
            }
        }

        return null;

    }

    public Evento findEventoByIdAndContratante(Long idEvento, Pessoa contratante) {

        validateId(idEvento);

        pessoaService.validateObjectAndIdNull(contratante);

        return repository.getEventoByIdEventoContratante(idEvento, contratante);

    }

    public Evento findEventoByIdAndContratante(Long idEvento, Pessoa contratante, List<String> pathsLazy) {

        validateId(idEvento);

        pessoaService.validateObjectAndIdNull(contratante);

        Evento evento = repository.getEventoByIdEventoContratante(idEvento, contratante);

        smartLazy(evento, pathsLazy);

        return evento;
    }

    /**
     * Vai retornar o evento que pertence a somente esse cliente Carregar em
     * lazy o cerimonia, festa, tipo evento
     *
     * @param idEvento
     * @return
     */
    public Evento findEventoLocalizacaoCerimonia(Long idEvento) {

        validateId(idEvento);

        Evento evento = repository.getEntity(idEvento);

        smartLazy(evento, Arrays.asList("preEvento", "cerimoniaEvento", "festaCerimonia", "evolucoesPreenchimento"));

        return evento;

    }

    /**
     * Vai copiar o endereco da cerimonia e setar no endereco da festa
     *
     * @param cerimoniaEvento
     * @param festaCerimonia
     */
    public void copiarLocalizacaoCerimonia(CerimoniaEvento cerimoniaEvento, FestaCerimonia festaCerimonia) {

        if (cerimoniaEvento == null || cerimoniaEvento.getEndereco() == null) {
            throw new GenericException("Endereço da cerimônia nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (festaCerimonia.getEndereco() == null) {
            festaCerimonia.setEndereco(new Endereco());
        }

        festaCerimonia.setNomeLocalFesta(cerimoniaEvento.getNomeLocalEvento());
        festaCerimonia.setEndereco(festaCerimonia.getEndereco().copiarEndereco(cerimoniaEvento.getEndereco()));

    }

    /**
     * Vai cancelar o evento, vai inativar o usuario do contratante para não ter
     * mais acesso ao sistema
     *
     * @param idEvento
     * @param motivoCancelamento
     * @throws java.lang.Exception
     */
    public void cancelarEvento(Long idEvento, String motivoCancelamento) throws Exception {

        if (StringUtils.isBlank(motivoCancelamento)) {
            throw new GenericException("Motivo de cancelamento não deve ser vazio", ErrorCode.BAD_REQUEST.getCode());
        }

        if (idEvento == null) {
            throw new GenericException("Motivo de cancelamento não deve ser vazio", ErrorCode.BAD_REQUEST.getCode());
        }

        Evento evento = findEntityById(idEvento, Arrays.asList("contratantes", "contratantes.usuariosClientes"));

        List<Evento> eventosAtivos = findEventosAtivosCliente(evento.getContratanteUsuario());
//        
        if (CollectionUtils.isNotBlank(eventosAtivos)) {

            if (eventosAtivos.size() == 1) {

                usuarioService.inativarUsuario(evento.getContratanteUsuario().getUsuarioCliente());
            }
        }

        evento.setSituacaoEvento(SituacaoEvento.CANCELADO);
        evento.setMotivoCancelamento(motivoCancelamento);

        repository.edit(evento);

    }

    /**
     * Vai criar usuario para cliente ter acesso ao sistema e enviar por email
     * os acessos
     *
     * @param evento
     * @throws java.lang.Exception
     */
    public void liberarAcessoSistemaContratanteEvento(Evento evento) throws Exception {

        validateObjectAndIdNull(evento);

        evento = findEntityById(evento.getId(), Arrays.asList("preEvento", "preEvento.contatoEvento"));

        Pessoa cliente = pessoaService.criarClienteFromContato(evento.getPreEvento().getContatoEvento());
        pessoaService.saveCliente(cliente);

        Usuario usuarioCliente = usuarioService.criarUsuarioCliente(cliente);

        String senha = usuarioCliente.getSenha();

        usuarioCliente.setCliente(cliente);

        if (usuarioCliente.getId() != null) {
            usuarioService.alterarSaltSenha(usuarioCliente);
        }

        evento.setAcessoSistema(AcessoSistema.LIBERADO);

        this.repository.edit(evento);

        usuarioCliente = usuarioService.save(usuarioCliente);

        usuarioService.enviarEmailBoasVindas(usuarioCliente, senha);

    }

    /**
     * Vai criar usuario para cliente ter acesso ao sistema e enviar por email
     * os acessos
     *
     * @param evento
     * @throws java.lang.Exception
     */
    public void cancelarAcessoSistemaContratanteEvento(Evento evento) throws Exception {

        validateObjectAndIdNull(evento);

        evento = findEntityById(evento.getId(), Arrays.asList("contratantes", "contratantes.usuariosClientes"));

        List<Evento> eventosAtivos = findEventosAtivosCliente(evento.getContratanteUsuario());
//        
        if (CollectionUtils.isNotBlank(eventosAtivos)) {

            if (eventosAtivos.size() == 1) {

                usuarioService.inativarUsuario(evento.getContratanteUsuario().getUsuarioCliente());
            }
        }

        evento.setAcessoSistema(AcessoSistema.NEGADO);

        this.repository.edit(evento);

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
    public void validateObjectNull(Evento entity) {

        if (entity == null) {
            throw new GenericException(" Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    @Override
    public void validateObjectAndIdNull(Evento entity) {

        validateObjectNull(entity);

        validateId(entity.getId());

    }

}
