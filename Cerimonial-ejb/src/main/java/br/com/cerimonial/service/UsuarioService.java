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
import java.util.UUID;
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

    public void delete(Usuario user) throws Exception {

        if (user != null) {
            if (user.isMaster()) {
                throw new Exception("Usuário master não pode ser removido");
            }
            repository.delete(user);
        }
    }

    public Usuario getUsuarioByLoginSenha(String login, String senha) throws Exception {
        Usuario usuarioSalt = this.getUsuarioByLoginAtivo(login);
        String senhaMd5 = Criptografia.md5(senha + usuarioSalt.getSalt());
        return repository.getUsuarioByLoginSenha(login, senhaMd5, Boolean.TRUE);
    }

    public Usuario getUsuarioByLoginAtivo(String login) throws Exception {
        if (StringUtils.isBlank(login)) {
            throw new Exception("O login está vazio");
        }
        return repository.getUsuarioByLoginAtivo(login, Boolean.TRUE);
    }
    
    public Usuario getUsuarioByEmail(String email) throws Exception{
        CerimonialUtils.validarEmail(email);
        return repository.getUsuarioByEmail(email.trim());
    }

    public Usuario getUsuarioByLogin(String login) throws Exception {
        if (StringUtils.isBlank(login)) {
            throw new Exception("O login está vazio");
        }
        return repository.getUsuarioByLogin(login);
    }

    public synchronized Usuario alterarSenha(Usuario entity) throws Exception {
        Usuario usuario = this.getEntity(entity.getId());
        if (usuario != null && !usuario.getSenha().equals(entity.getSenha())) {
            alterarSaltSenha(entity);
        }
        return repository.edit(entity);
    }

    public void alterarSaltSenha(Usuario entity) {
        ByteSource salt = new SecureRandomNumberGenerator().nextBytes();
        entity.setSenha(Criptografia.md5(entity.getSenha() + salt.toString()));
        entity.setSalt(salt.toString());
    }

    public Usuario criarSalvarUsuarioMaster() throws Exception {
        Usuario usuario = criarUsuarioMaster();
        return save(usuario);
    }

    public Usuario criarUsuarioMaster() throws Exception {

        Usuario usuario = this.getUsuarioByLogin("master");

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setNome("Master");
            usuario.setLogin("master");
            usuario.setSenha("master");
            usuario.setEmail("hoffmann.gusttavo@gmail.com");
            
        }

        usuario.setMaster(true);
        usuario.setAtivo(true);

        return usuario;
    }

    /**
     * Verifica se já existe um usuario cadastrado com esse login Se não
     * existir, instancia um novo e gera uma senha aleatoria.
     *
     * @param cliente
     * @return
     * @throws java.lang.Exception
     */
    public Usuario criarUsuarioCliente(Pessoa cliente) throws Exception {
        if (cliente == null) {
            throw new Exception("Cliente Null");
        }

        Usuario usuario = this.getUsuarioByLogin(cliente.getEmail());

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setLogin(cliente.getEmail());
            usuario.setSenha(CerimonialUtils.gerarAlfaNumericoAleatoria());
            usuario.setEmail(cliente.getEmail());
        }
        
        usuario.setNome(cliente.getNome());
        usuario.setAtivo(true);
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

    /**
     * Enviar email de boas vindas quando cadastrar um cliente novo
     * @param user
     * @param senha
     */
    public void enviarEmailBoasVindas(Usuario user, String senha) {

    }
    
    /**
     * Enviar email quando usuario esquecer a senha
     * @param user
     * @param senha
     */
    public void enviarEmailEsqueciMinhaSenha(Usuario user, String senha) {

    }

    /**
     * método para enviar email com a senha para o usuário
     * quando ele esquecer
     * @param email
     * @throws java.lang.Exception
     */
    public void enviarLembreteSenha(String email) throws Exception{
        
      CerimonialUtils.validarEmail(email);
        
       Usuario usuario = getUsuarioByEmail(email);
       if(usuario == null){
           throw new Exception("Usuário inválido");
       }
       if(!usuario.isAtivo()){
           throw new Exception("Seu usuário está inativo, entre em contato com seu cerimonial");
       }
       
       String novaSenha = CerimonialUtils.gerarAlfaNumericoAleatoria();
       System.out.println(novaSenha);
       usuario.setSenha("master");
       
       this.alterarSaltSenha(usuario);
        
       this.enviarEmailEsqueciMinhaSenha(usuario, novaSenha);
    }

    
}
