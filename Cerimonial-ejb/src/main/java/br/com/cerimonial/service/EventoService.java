/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.CerimoniaEvento;
import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.Endereco;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.FestaCerimonia;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.enums.AcessoSistema;
import br.com.cerimonial.enums.SituacaoEvento;
import br.com.cerimonial.repository.EventoRepository;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.ArrayList;
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
    protected UsuarioService usuarioService;

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

        List<Evento> retorno = repository.findEventosAtivosCliente(cliente);

        if (CollectionUtils.isNotBlank(retorno)) {
            // fazer isso para nao ter dados duplicados
            Set<Evento> eventos = new HashSet<Evento>(retorno);

            retorno = new ArrayList<Evento>(eventos);
        }

        return retorno;

    }

    public Evento criarEventoFromOrcamento(OrcamentoEvento orcamento) throws Exception {

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

//        evento.setContratante(cliente);
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

        if (CollectionUtils.isNotBlank(eventos)) {
            return eventos.get(0);
        }

        return null;

    }

    public Evento getEventoByContatoInicial(ContatoEvento contatoEvento) throws Exception {

        if (contatoEvento == null || contatoEvento.getId() == null) {
            throw new GenericException("Contato nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        List<Evento> eventos = repository.getEventosByContatoEvento(contatoEvento);

        if (CollectionUtils.isNotBlank(eventos)) {
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

        }

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

        if (idEvento == null) {
            throw new GenericException("Id Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        Evento evento = repository.getEntity(idEvento);

        if (evento != null) {

            if (evento.getCerimoniaEvento() != null) {
                evento.getCerimoniaEvento().getId();
            }

            if (evento.getFestaCerimonia() != null) {
                evento.getFestaCerimonia().getId();
            }

        }

        return evento;

    }

    /**
     * Vai retornar o evento que pertence a somente esse cliente Carregar em
     * lazy os noivos
     *
     * @param idEvento
     * @param contratante
     * @return
     */
    public Evento getEventoCasamento(Long idEvento, Pessoa contratante) {

        if (idEvento == null) {
            throw new GenericException("Id Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (contratante == null || contratante.getId() == null) {
            throw new GenericException("Cliente contratante nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        Evento evento = repository.getEventoByIdEventoContratante(idEvento, contratante);

        if (evento != null) {
            if (evento.getContratantes() != null) {
                evento.getContratantes().size();
            }
        }

        return evento;

    }
    
    /**
     * Vai retornar o evento Carregar em
     * lazy os noivos
     *
     * @param idEvento
     * @return
     */
    public Evento findEventoLazyContratante(Long idEvento) {

        if (idEvento == null) {
            throw new GenericException("Id Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        Evento evento = repository.getEntity(idEvento);

        if (evento != null) {
            if (evento.getContratantes() != null) {
                evento.getContratantes().size();
            }
        }

        return evento;

    }

    /**
     * Vai retornar o evento que pertence a somente esse cliente Carregar em
     * lazy os noivos
     *
     * @param idEvento
     * @param contratante
     * @return
     */
    public Evento getEventoAniversario(Long idEvento, Pessoa contratante) {

        if (idEvento == null) {
            throw new GenericException("Id Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (contratante == null || contratante.getId() == null) {
            throw new GenericException("Cliente contratante nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        Evento evento = repository.getEventoByIdEventoContratante(idEvento, contratante);

        if (evento != null) {
            if (evento.getContratantes() != null) {
                evento.getContratantes().size();
            }
        }

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

        Evento evento = getEntity(idEvento);

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

        isValid(evento);
        
        evento = getEntity(evento.getId());
        
        Pessoa cliente = pessoaService.criarClienteFromContato(evento.getOrcamentoEvento());
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

        isValid(evento);

        evento = getEntity(evento.getId());

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

}
