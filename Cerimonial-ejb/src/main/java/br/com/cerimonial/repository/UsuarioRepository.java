/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.utils.ModelFilter;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class UsuarioRepository extends BasicRepository{

    public UsuarioRepository(EntityManager entityManager) {
        super(entityManager);
    }
    
    public Usuario create(Usuario usuario) throws Exception {
        addEntity(Usuario.class, usuario);
        return usuario;
    }

    public void delete(Usuario usuario) throws Exception{
        removeEntity(usuario);
    }

    public Usuario edit(Usuario usuario) throws Exception{
        return setEntity(Usuario.class, usuario);
    }

    public Usuario getUsuario(Long id) throws Exception{
        return getEntity(Usuario.class, id);
    }

    public Usuario getUsuarioByLoginSenha(String login, String senhaMd5, boolean ativo) throws Exception {
        return getPurePojo(Usuario.class, "select usr from Usuario usr where usr.login = ?1 and usr.senha = ?2 and usr.ativo = ?3", login, senhaMd5, ativo);
    }

    public Usuario getUsuarioByLogin(String login,  boolean ativo)  throws Exception{
        return getPurePojo(Usuario.class, "select usr from Usuario usr where usr.login = ?1 and usr.ativo = ?2", login, ativo);
    }

    public int countAll() throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Usuario.class);
        return getCount(modelFilter.getSqlCountBase());
    }
    
    public int countListagem(HashMap<String, Object> filter) throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Usuario.class);
        modelFilter.addFilter(filter);
        modelFilter.addOperador(ModelFilter.Operadores.ILIKE, "nome");
        return getCount(modelFilter.getSqlCountBase());
    }
    
    public List<Usuario> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) throws Exception{
        
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Usuario.class);
        if(params != null){
            modelFilter.addFilter(params);
            modelFilter.addOperador(ModelFilter.Operadores.ILIKE, "nome");
        }
        modelFilter.setLimit(max);
        modelFilter.setOffSet(offset);
        if(sortField != null){
            modelFilter.addOrderBy(sortField, sortAscDesc);
        }
        
        return getPureListRange(Usuario.class, modelFilter.getSqlBase(), max, offset);
    }
   
}
