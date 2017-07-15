/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.repository.UsuarioRepository;
import br.com.cerimonial.utils.CerimonialUtils;
import br.com.cerimonial.utils.Criptografia;
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
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.util.ByteSource;

/**
 *
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UsuarioService extends BasicService<Usuario> {

    private UsuarioRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new UsuarioRepository(em);
    }

    @Override
    public Usuario getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public synchronized Usuario save(Usuario entity) throws Exception {
        if (entity != null) {
            if (entity.getId() == null) {
                alterarSaltSenha(entity);
                return repository.create(entity);
            } else {
                return repository.edit(entity);
            }
        }
        return null;
    }
    
    public void delete(Usuario user) throws Exception{
        
        if(user != null){
            if(user.isMaster()){
                throw new Exception("Usuário master não pode ser removido");
            }
            repository.delete(user);
        }
    }

    public Usuario getUsuarioByLoginSenha(String login, String senha) throws Exception {
        Usuario usuarioSalt = this.getUsuarioByLogin(login);
        String senhaMd5 = Criptografia.md5(senha + usuarioSalt.getSalt());
        return repository.getUsuarioByLoginSenha(login, senhaMd5, Boolean.TRUE);
    }

    public Usuario getUsuarioByLogin(String login) throws Exception {
        return repository.getUsuarioByLogin(login, Boolean.TRUE);
    }

    public synchronized Usuario alterarSenha(Usuario entity) throws Exception {
        Usuario usuario = this.getEntity(entity.getId());
        if (usuario != null && !usuario.getSenha().equals(entity.getSenha())) {
            alterarSaltSenha(entity);
        }
        return repository.edit(entity);
    }

    private void alterarSaltSenha(Usuario entity) {
        ByteSource salt = new SecureRandomNumberGenerator().nextBytes();
        entity.setSenha(Criptografia.md5(entity.getSenha() + salt.toString()));
        entity.setSalt(salt.toString());
    }

    public Usuario criarUsuarioMaster() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setNome("Master");
        usuario.setLogin("master");
        usuario.setSenha("master");
        usuario.setEmail("hoffmann.gusttavo@gmail.com");
        usuario.setMaster(true);
        return save(usuario);
    }
    
    public Usuario criarUsuarioCliente(Pessoa cliente) throws Exception {
        if(cliente == null){
            throw new Exception("Cliente Null");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(cliente.getNome());
        usuario.setLogin(cliente.getEmail());
        usuario.setSenha(CerimonialUtils.removerNaoDigitos(cliente.getCpf()));
        usuario.setEmail(cliente.getEmail());
        usuario.setMaster(false);
        return usuario;
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
            if (filter != null) {
                return repository.countListagem(filter);
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Usuario> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(params, max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

   public void enviarEmailBoasVindas(Usuario user, String senha) {
        
    }
}
