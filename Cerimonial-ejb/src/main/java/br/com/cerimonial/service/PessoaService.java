/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.enums.TipoEnvolvido;
import br.com.cerimonial.enums.TipoPessoa;
import br.com.cerimonial.repository.PessoaRepository;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.utils.CerimonialUtils;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.Arrays;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaService extends BasicService<Pessoa> {

    private PessoaRepository repository;

    @EJB
    private UsuarioService usuarioService;
    
    @EJB
    private ContatoEventoService contatoEventoService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new PessoaRepository(em);
    }

    @Override
    public Pessoa findEntityById(Long id) throws Exception {

        return repository.getEntity(id);

    }

    public Pessoa findEntityById(Long id, List<String> pathsLazy) throws Exception {

        Pessoa entity = repository.getEntity(id);

        smartLazy(entity, pathsLazy);

        return entity;
    }

    @Override
    public synchronized Pessoa save(Pessoa entity) throws Exception {

        validateObjectNull(entity);

        if (entity.getEndereco() != null && !entity.getEndereco().isValid()) {
            entity.setEndereco(null);
        }

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public void delete(Pessoa entity) throws Exception {

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

    public int countListagem(HashMap<String, Object> filter) {
        try {
            if (filter == null) {
                filter = new HashMap<>();
            }

            return repository.countListagem(filter);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Pessoa> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {
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

    //-----------Clientes----------------------
    public Pessoa saveCliente(Pessoa entity) throws Exception {

        validateObjectNull(entity);

        if (entity.getEndereco() != null && !entity.getEndereco().isValid()) {
            entity.setEndereco(null);
        }

        entity.setTipoEnvolvido(TipoEnvolvido.CLIENTE);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    /**
     * Somente pode salvar um cliente jÃ¡ cadastrado
     *
     * @param entity
     * @return
     * @throws java.lang.Exception
     */
    public Pessoa editCliente(Pessoa entity) throws Exception {

        validateObjectNull(entity);

        isValidCliente(entity);

        if (entity.getId() == null) {
            throw new GenericException("Não pode gravar um novo cliente", ErrorCode.BAD_REQUEST.getCode());
        }

        return saveCliente(entity);
    }

    /**
     * Somente pode salvar um cliente novo
     *
     * @param entity
     * @return
     * @throws java.lang.Exception
     */
    public Pessoa createCliente(Pessoa entity) throws Exception {

        validateObjectNull(entity);

        entity.setId(null);

        return saveCliente(entity);
    }

    /**
     * Retorna o cliente de acordo com o usuario logado
     *
     * @param usuarioLogado
     * @return
     */
    public Pessoa getClienteByUsuario(Usuario usuarioLogado) {

        usuarioService.validateObjectAndIdNull(usuarioLogado);

        return repository.getClienteByUsuario(usuarioLogado);
    }

    //-----------Fornecedores----------------------
    public Pessoa saveFornecedor(Pessoa entity) throws Exception {

        validateObjectNull(entity);

        if (entity.getEndereco() != null && !entity.getEndereco().isValid()) {
            entity.setEndereco(null);
        }

        entity.setTipoEnvolvido(TipoEnvolvido.FORNECEDOR);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    //--------------------COLABORADOR----------------------------
    public Pessoa saveColaborador(Pessoa entity) throws Exception {

        validateObjectNull(entity);

        if (entity.getEndereco() != null && !entity.getEndereco().isValid()) {
            entity.setEndereco(null);
        }

        entity.setTipoEnvolvido(TipoEnvolvido.COLABORADOR);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    /**
     * Verifica se já existe um cliente cadastrado com esse email Se não houver
     * cadastrado, instancia um novo
     *
     * @param entity
     * @return
     * @throws java.lang.Exception
     */
    public Pessoa criarClienteFromContato(ContatoEvento entity) throws Exception {

        contatoEventoService.validateObjectNull(entity);

        Pessoa cliente = this.getPessoaByEmail(entity.getEmailContato());

        try {

            if (cliente == null) {
                cliente = new Pessoa(TipoEnvolvido.CLIENTE, TipoPessoa.FISICA);
            }

            cliente.setTipoEnvolvido(TipoEnvolvido.CLIENTE);
            cliente.setEmail(entity.getEmailContato());
            cliente.setNome(entity.getNomeContato());
            cliente.setTelefoneResidencial(entity.getTelefonePrincipal());
            cliente.setTelefoneCelular(entity.getTelefoneSecundario());
            cliente.setAtivo(true);

        } catch (Exception e) {

            throw new GenericException("Não Foi possivel criar um cliente a partir de um contato", ErrorCode.BAD_REQUEST.getCode());
        }
        return cliente;
    }

    /**
     * Vai validar se o email é válido Vai buscar uma pessoa pelo email
     *
     * @param email
     * @return
     */
    private Pessoa getPessoaByEmail(String emailContato) throws Exception {

        CerimonialUtils.validarEmail(emailContato);

        return repository.getPessoaByEmail(emailContato);
    }

    /**
     * Vai buscar uma pessoa pelo cpf carregando em lazy os dados de contatos e
     * endereço
     *
     * @param cpf
     * @return
     */
    public Pessoa findPessoaByCpf(String cpf) {

        if (StringUtils.isBlank(cpf)) {
            throw new GenericException("Preencha um cpf válido", ErrorCode.BAD_REQUEST.getCode());
        }

        Pessoa pessoa = repository.findPessoaByCpf(cpf);
        
        smartLazy(pessoa, Arrays.asList("contatosFamiliares", "endereco"));

        return pessoa;
    }

    public boolean isValidCliente(Pessoa entity) throws Exception {

        if (entity == null) {
            throw new GenericException("Pessoa nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (StringUtils.isBlank(entity.getNome())) {
            throw new GenericException("Preencha o nome corretamente.", ErrorCode.BAD_REQUEST.getCode());
        }
        if (StringUtils.isBlank(entity.getCpf())) {
            throw new GenericException("Preencha o cpf corretamente.", ErrorCode.BAD_REQUEST.getCode());
        }
        if (StringUtils.isBlank(entity.getRg())) {
            throw new GenericException("Preencha o rg corretamente.", ErrorCode.BAD_REQUEST.getCode());
        }
        if (StringUtils.isBlank(entity.getTelefoneResidencial()) && StringUtils.isBlank(entity.getTelefoneCelular())) {
            throw new GenericException("Preencha pelo menos um telefone.", ErrorCode.BAD_REQUEST.getCode());
        }

        CerimonialUtils.validarEmail(entity.getEmail());

        return true;

    }

    /**
     * Vai retornar o Contratante do evento carregar em lazy o endereço
     *
     * @param idEvento
     * @return
     */
    public List<Pessoa> findContratantesByEventoId(Long idEvento) {

        validateId(idEvento);

        List<Pessoa> contratantes = repository.findContratantesByEventoId(idEvento);

        if (CollectionUtils.isNotBlank(contratantes)) {

            for (Pessoa contratante : contratantes) {
                
                smartLazy(contratante, Arrays.asList("contatosFamiliares", "endereco"));
            }

        }
        return contratantes;

    }

    /**
     * Método responsavel por inativar Pessoa
     *
     * @param contratante
     */
    public void inativarPessoa(Pessoa contratante) {

        validateObjectNull(contratante);

        contratante.setAtivo(false);

        repository.edit(contratante);

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
    public void validateObjectNull(Pessoa entity) {

        if (entity == null) {
            throw new GenericException(" Pessoa nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    @Override
    public void validateObjectAndIdNull(Pessoa entity) {

        validateObjectNull(entity);

        validateId(entity.getId());

    }

}
