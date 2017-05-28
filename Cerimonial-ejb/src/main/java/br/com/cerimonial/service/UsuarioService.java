/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.repository.UsuarioRepository;
import br.com.cerimonial.utils.CerimonialUtils;
import br.com.cerimonial.utils.Criptografia;
import br.com.cerimonial.utils.ModelFilter;
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

    private UsuarioRepository usuarioRepository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        usuarioRepository = new UsuarioRepository(em);

    }

    @Override
    public Usuario getEntity(Long id) throws Exception {
        return usuarioRepository.getUsuario(id);
    }

    public synchronized Usuario save(Usuario entity) throws Exception {
        if (entity != null) {
            if (entity.getId() == null) {
                alterarSaltSenha(entity);
                return usuarioRepository.create(entity);
            } else {
                return usuarioRepository.edit(entity);
            }
        }
        return null;
    }

    public Usuario getUsuarioByLoginSenha(String login, String senha) throws Exception {
        Usuario usuarioSalt = this.getUsuarioByLogin(login);
        String senhaMd5 = Criptografia.md5(senha + usuarioSalt.getSalt());
        return usuarioRepository.getUsuarioByLoginSenha(login, senhaMd5);
    }

    public Usuario getUsuarioByLogin(String login) throws Exception {
        return usuarioRepository.getUsuarioByLogin(login);
    }

    public synchronized Usuario alterarSenha(Usuario entity) throws Exception {
        Usuario usuario = this.getEntity(entity.getId());
        if (usuario != null && !usuario.getSenha().equals(entity.getSenha())) {
            alterarSaltSenha(entity);
        }
        return usuarioRepository.edit(entity);
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
        usuario.setToken(CerimonialUtils.SHA1(usuario.getLogin() + usuario.getSenha()));
        return save(usuario);
    }

}
