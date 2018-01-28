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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class UsuarioRepository extends AbstractRepository<Usuario> {

    public UsuarioRepository(EntityManager entityManager) {
        super(entityManager, Usuario.class);
    }

    public Usuario getUsuarioByLoginSenha(String login, String senhaMd5, boolean ativo) throws Exception {
        return getPurePojo(Usuario.class, "select usr from Usuario usr where usr.login = ?1 and usr.senha = ?2 and usr.ativo = ?3", login, senhaMd5, ativo);
    }

    public Usuario getUsuarioByLoginAtivo(String login, boolean ativo) throws Exception {
        return getPurePojo(Usuario.class, "select usr from Usuario usr where usr.login = ?1 and usr.ativo = ?2", login, ativo);
    }
    
    public Usuario getUsuarioByLogin(String login) {
        try {
            return getPurePojo(Usuario.class, "select usr from Usuario usr where usr.login = ?1", login);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
   public Usuario getUsuarioByEmail(String email) {
        try {
            return getPurePojo(Usuario.class, "select usr from Usuario usr where usr.email = ?1", email);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
   

    @Override
    public int countListagem(HashMap<String, Object> filter) {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Usuario.class);
        modelFilter.addFilter(filter);
        modelFilter.addOperador(ModelFilter.Operadores.ILIKE, "nome");
        return super.countListagem(modelFilter);
    }

//   
    @Override
    public List<Usuario> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {

        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Usuario.class);
        if (params != null) {
            modelFilter.addFilter(params);
            modelFilter.addOperador(ModelFilter.Operadores.ILIKE, "nome");
        }
        modelFilter.setLimit(max);
        modelFilter.setOffSet(offset);
        if (sortField != null) {
            modelFilter.addOrderBy(sortField, sortAscDesc);
        }

        return super.findRangeListagem(modelFilter); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Usuario> findUsuariosAdminAtivos() {
        
        return getPureList(Usuario.class, "select usr from Usuario usr where usr.admin = true and usr.ativo = true");
        
    }

    
}
