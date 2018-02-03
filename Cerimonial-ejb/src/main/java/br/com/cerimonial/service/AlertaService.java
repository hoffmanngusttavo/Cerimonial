/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Alerta;
import br.com.cerimonial.entity.ContratoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.AlertaRepository;
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

/**
 *
 * @author hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AlertaService extends BasicService<Alerta> {

    @EJB
    protected UsuarioService usuarioService;
    
    @EJB
    protected ContratoEventoService contratoEventoService;
    
    @EJB
    protected PessoaService pessoaService;
    
    @EJB
    protected EventoPessoaService eventoPessoaService;
    
    @EJB
    protected EventoService eventoService;

    private AlertaRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new AlertaRepository(em);
    }

    @Override
    public Alerta findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Alerta save(Alerta entity) throws Exception {

        validateObjectNull(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
        
    }

    public List<Alerta> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(AlertaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(Alerta alerta) throws Exception {
        
        validateObjectAndIdNull(alerta);
        
        repository.delete(alerta.getId());
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Alerta> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    public void enviarAlertaContratoLiberado(ContratoEvento entity) throws Exception {

        contratoEventoService.validateObjectNull(entity);
        
        eventoService.validateObjectAndIdNull(entity.getEvento());
        
        pessoaService.validateObjectNull(entity.getEvento().getContratanteUsuario());

        String caminho = "/intranet/cliente/evento/partials/impressao-contrato.xhtml?idEvento=" + entity.getEvento().getId();
        String titulo = "Contrato";
        String msg = "Contrato foi gerado e liberado para visualização";

        Alerta alerta = criarAlerta(caminho, titulo, msg, entity.getEvento().getContratanteUsuario().getUsuarioCliente());

        this.save(alerta);

    }

    /**
     * enviar alerta ao administrador que foi concluido preenchimento.
     *
     * @param entity
     */
    public void enviarAlertaUsuarioAdminContratante(EventoPessoa entity) {
        
        eventoPessoaService.validateObjectNull(entity);

         eventoService.validateObjectAndIdNull(entity.getEvento());
         
        try {

            List<Usuario> usuarios = usuarioService.findUsuariosAdminAtivos();

            String path = "/intranet/admin/operacional/pre-evento/partials/dados-contratante.xhtml?idEvento" + entity.getEvento().getId();
            String titulo = "Dados do contratante";
            String mensagem = "Todos as informações obrigatórias do contratante foram preenchidas";

            Alerta alerta = this.criarAlerta(path, titulo, mensagem, usuarios);

            this.save(alerta);

        } catch (Exception ex) {
            Logger.getLogger(EventoPessoaService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * enviar alerta ao administrador que foi concluido preenchimento.
     *
     * @param entity
     */
    public void enviarAlertaUsuarioAdminDadosNoivo(EventoPessoa entity) {
        
        eventoPessoaService.validateObjectNull(entity);
        
        eventoService.validateObjectAndIdNull(entity.getEvento());

        try {

            List<Usuario> usuarios = usuarioService.findUsuariosAdminAtivos();

            String path = "/intranet/admin/operacional/pre-evento/form.xhtml?idEvento=" + entity.getEvento().getId();
            String titulo = "Dados do noivo(a)";
            String mensagem = "Todos as informações obrigatórias do(a) noivo(a) foram preenchido(a)s";

            Alerta alerta = this.criarAlerta(path, titulo, mensagem, usuarios);

            this.save(alerta);

        } catch (Exception ex) {
            Logger.getLogger(EventoPessoaService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void enviarAlertaUsuarioAdminDadosEvento(Evento entity) {

        eventoService.validateObjectAndIdNull(entity);
        
        try {

            List<Usuario> usuarios = usuarioService.findUsuariosAdminAtivos();

            String path = "/intranet/admin/operacional/pre-evento/partials/ficha-cadastral.xhtml?idEvento=" + entity.getId();
            String titulo = "Dados do Evento " + entity.getNome();
            String mensagem = "Todos as informações obrigatórias do evento foram preenchidas";

            Alerta alerta = this.criarAlerta(path, titulo, mensagem, usuarios);

            this.save(alerta);

        } catch (Exception ex) {
            Logger.getLogger(EventoPessoaService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Instancia um alerta com mensagem, caminho e destinatarios
     *
     * @param caminho
     * @param titulo
     * @param mensagem
     * @param destinatario
     * @return
     * @throws java.lang.Exception
     */
    public Alerta criarAlerta(String caminho, String titulo, String mensagem, Usuario destinatario) throws Exception {

        usuarioService.validateObjectNull(destinatario);

        return criarAlerta(caminho, titulo, mensagem, Arrays.asList(destinatario));
    }

    /**
     * Instancia um alerta com mensagem, caminho e destinatarios
     *
     * @param caminho
     * @param titulo
     * @param mensagem
     * @param destinatarios
     * @return
     * @throws java.lang.Exception
     */
    public Alerta criarAlerta(String caminho, String titulo, String mensagem, List<Usuario> destinatarios) throws Exception {

        if (CollectionUtils.isBlank(destinatarios)) {
            throw new GenericException("Destinatarios do alerta não pode ser nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        return new Alerta(titulo, mensagem, caminho, destinatarios);

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
    public void validateObjectNull(Alerta entity) {
        
         if (entity == null) {
            throw new GenericException(" Alerta Destinatario nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(Alerta entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }

}
