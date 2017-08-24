/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.enums.TipoEnvolvido;
import br.com.cerimonial.enums.TipoPessoa;
import br.com.cerimonial.repository.PessoaRepository;
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

/**
 *
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaService extends BasicService<Pessoa> {

    @EJB
    private UsuarioService usuarioService;
    
    @EJB
    private EnderecoService enderecoService;
    
    private PessoaRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new PessoaRepository(em);
    }

    @Override
    public Pessoa getEntity(Long id) throws Exception {
        return repository.getPessoa(id);
    }
    
    
    public Pessoa getEntityFornecedorCategoria(Long id) throws Exception {
        return repository.getEntityFornecedorCategoria(id);
    }

    @Override
    public synchronized Pessoa save(Pessoa entity) throws Exception {
        if (entity != null) {
            if (entity.getId() == null) {
                return repository.create(entity);
            } else {
                return repository.edit(entity);
            }
        }
        return null;
    }

    public void delete(Pessoa entity) throws Exception {
        if (entity != null) {
            repository.delete(entity);
        }
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    
    //-----------Clientes----------------------
    
    private Pessoa saveCliente(Pessoa entity) throws Exception {
        if (entity != null) {
            entity.setTipoEnvolvido(TipoEnvolvido.CLIENTE);
            
            if (entity.getId() == null) {
                //criar novo usuario
                Usuario user = usuarioService.criarUsuarioCliente(entity);
                String senha = user.getSenha();
                user = usuarioService.save(user);
                entity.setUsuarioCliente(user);
                repository.create(entity);
                usuarioService.enviarEmailBoasVindas(user,senha);
                return entity;
            } else {
                return repository.edit(entity);
            }
        }
        return null;
    }
    
    /**
     * Somente pode salvar um cliente já cadastrado
     * @param entity
     * @return 
     * @throws java.lang.Exception
     */
    public Pessoa editCliente(Pessoa entity) throws Exception {
        if (entity != null) {
            if(entity.getId() == null){
                throw new Exception("Não pode gravar um novo cliente");
            }
            return saveCliente(entity);
        }
        return null;
    }
    
    /**
     * Somente pode salvar um cliente novo
     * @param entity
     * @return 
     * @throws java.lang.Exception
     */
    public Pessoa createCliente(Pessoa entity) throws Exception {
        if (entity != null) {
            entity.setId(null);
            return saveCliente(entity);
        }
        return null;
    }
    
    public int countListagemClientes(HashMap<String, Object> filter) {
        try {
            if (filter == null) {
                filter = new HashMap<>();
            }

            filter.put("tipoEnvolvido", TipoEnvolvido.CLIENTE);
            return repository.countListagem(filter);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Pessoa> findRangeListagemClientes(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {
        try {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put("tipoEnvolvido", TipoEnvolvido.CLIENTE);
            return repository.findRangeListagem(params, max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    //-----------Fornecedores----------------------
    
    
    public Pessoa saveFornecedor(Pessoa entity) throws Exception {
        if (entity != null) {
            entity.setTipoEnvolvido(TipoEnvolvido.FORNECEDOR);
            
            if (entity.getId() == null) {
                return repository.create(entity);
            } else {
                return repository.edit(entity);
            }
        }
        return null;
    }
    
    public int countListagemFornecedor(HashMap<String, Object> filter) {
        try {
            if (filter == null) {
                filter = new HashMap<>();
            }

            filter.put("tipoEnvolvido", TipoEnvolvido.FORNECEDOR);
            return repository.countListagem(filter);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Pessoa> findRangeListagemFornecedor(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {
        try {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put("tipoEnvolvido", TipoEnvolvido.FORNECEDOR);
            return repository.findRangeListagemFornecedor(params, max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //--------------------COLABORADOR----------------------------

    public Pessoa saveColaborador(Pessoa entity) throws Exception {
        if (entity != null) {
            entity.setTipoEnvolvido(TipoEnvolvido.COLABORADOR);
            
            if (entity.getId() == null) {
                return repository.create(entity);
            } else {
                return repository.edit(entity);
            }
        }
        return null;
    }
    
    public int countListagemColaborador(HashMap<String, Object> filter) {
        try {
            if (filter == null) {
                filter = new HashMap<>();
            }

            filter.put("tipoEnvolvido", TipoEnvolvido.COLABORADOR);
            return repository.countListagem(filter);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Pessoa> findRangeListagemColaborador(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {
        try {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put("tipoEnvolvido", TipoEnvolvido.COLABORADOR);
            return repository.findRangeListagem(params, max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 
     * @param entity
     * @return 
     * @throws java.lang.Exception 
     */
    public Pessoa criarClientePropostaAceita(OrcamentoEvento entity) throws Exception{
        Pessoa cliente = new Pessoa();
        try {
            cliente.setTipoEnvolvido(TipoEnvolvido.CLIENTE);
            cliente.setAtivo(true);
            cliente.setEmail(entity.getContatoEvento().getEmailContato());
            cliente.setNome(entity.getContatoEvento().getNomeContato());
            cliente.setTelefone1(entity.getContatoEvento().getTelefonePrincipal());
            cliente.setTelefone2(entity.getContatoEvento().getTelefoneSecundario());
            cliente.setTipoPessoa(TipoPessoa.FISICA);
        } catch (Exception e) {
            throw new Exception("Não Foi possivel criar um cliente a partir de um contato");
        }
        return cliente;
    }
    
    

}
