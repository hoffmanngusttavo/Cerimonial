/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Usuario;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class UsuarioRepository extends BasicRepository{

    public UsuarioRepository(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Usuario create(Usuario cliente) throws Exception {
        addEntity(Usuario.class, cliente);
        return cliente;
    }

    public void delete(Usuario cliente) throws Exception{
        removeEntity(cliente);
    }

    public Usuario edit(Usuario cliente) throws Exception{
        return setEntity(Usuario.class, cliente);
    }

    public Usuario getUsuario(Long id) throws Exception{
        return getEntity(Usuario.class, id);
    }

    public Usuario getUsuarioByLoginSenha(String login, String senhaMd5) throws Exception {
        return getPurePojo(Usuario.class, "select usr from Usuario usr where usr.login = ?1 and usr.senha = ?2 and usr.ativo = ?3", login, senhaMd5, Boolean.TRUE);
    }

    public Usuario getUsuarioByLogin(String login)  throws Exception{
        return getPurePojo(Usuario.class, "select usr from Usuario usr where usr.login = ?1 and usr.ativo = ?2", login, Boolean.TRUE);
    }
   
}
