/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.enums.TipoEnvolvido;
import br.com.cerimonial.enums.TipoPessoa;
import br.com.cerimonial.repository.PessoaRepository;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.utils.CerimonialUtils;
import java.util.HashMap;
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

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new PessoaRepository(em);
    }

    @Override
    public Pessoa getEntity(Long id) throws Exception {
        Pessoa entity = repository.getEntity(id);

        if (entity != null) {
            
            if (entity.getCategoriasFornecedor() != null) {
                entity.getCategoriasFornecedor().size();
            }
            
            if (entity.getEndereco() != null) {
                entity.getEndereco().getId();
            }
        }

        return entity;
    }


    @Override
    public synchronized Pessoa save(Pessoa entity) throws Exception {

        isValid(entity);

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

        isValid(entity);

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

        isValid(entity);

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

        isValid(entity);
        
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

        isValid(entity);

        entity.setId(null);
        return saveCliente(entity);
    }

   
    
     /**
      * Retorna o cliente de acordo com o usuario logado
     * @param usuarioLogado
     * @return 
      */
     public Pessoa getClienteByUsuario(Usuario usuarioLogado) {
        
         if(usuarioLogado == null || usuarioLogado.getId() == null){
             throw new GenericException("Não foi possível carregar cliente, usuário nulo", ErrorCode.BAD_REQUEST.getCode());
         }
        
        return repository.getClienteByUsuario(usuarioLogado);
    }
    

    //-----------Fornecedores----------------------
    public Pessoa saveFornecedor(Pessoa entity) throws Exception {

        isValid(entity);

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

        isValid(entity);

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
    public Pessoa criarClienteFromContato(OrcamentoEvento entity) throws Exception {

        Pessoa cliente = this.getPessoaByEmail(entity.getContatoEvento().getEmailContato());

        try {
            
            if (cliente == null) {
                cliente = new Pessoa(TipoEnvolvido.CLIENTE, TipoPessoa.FISICA);
            }
            
            cliente.setTipoEnvolvido(TipoEnvolvido.CLIENTE);
            cliente.setEmail(entity.getContatoEvento().getEmailContato());
            cliente.setNome(entity.getContatoEvento().getNomeContato());
            cliente.setTelefoneResidencial(entity.getContatoEvento().getTelefonePrincipal());
            cliente.setTelefoneCelular(entity.getContatoEvento().getTelefoneSecundario());
            cliente.setAtivo(true);

        } catch (Exception e) {
            
            throw new GenericException("Não Foi possivel criar um cliente a partir de um contato", ErrorCode.BAD_REQUEST.getCode());
        }
        return cliente;
    }

    
    /** 
     * Vai validar se o email é válido
     * Vai buscar uma pessoa pelo email
     * @param email
     * @return 
     */
    private Pessoa getPessoaByEmail(String emailContato) throws Exception {
        
        CerimonialUtils.validarEmail(emailContato);
        
        return repository.getPessoaByEmail(emailContato);
    }
    
   

    @Override
    public boolean isValid(Pessoa entity) {

        if (entity == null) {
            throw new GenericException("Pessoa nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;

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
     * Vai retornar o Contratante do evento
     * carregar em lazy o endereço
     * @param idEvento
     * @return 
     */
    public Pessoa getContratanteEvento(Long idEvento) {
        
        if (idEvento == null) {
            throw new GenericException("Id evento nulo", ErrorCode.BAD_REQUEST.getCode());
        }
        
        Pessoa contratante = repository.getContratanteEvento(idEvento);
        
        if(contratante != null){
            
            if(contratante.getEndereco() != null){
                contratante.getEndereco().getId();
            }
            
        }
        return contratante;
        
    }

    /**
     * Método responsavel por inativar Pessoa
     * @param contratante
     */
    public void inativarPessoa(Pessoa contratante) {
        
        isValid(contratante);
        
        contratante.setAtivo(false);
        
        repository.edit(contratante);
        
    }

    

}
