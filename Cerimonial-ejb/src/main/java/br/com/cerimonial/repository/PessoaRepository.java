/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.enums.TipoEnvolvido;
import br.com.cerimonial.utils.CerimonialUtils;
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
public class PessoaRepository extends AbstractRepository<Pessoa> {

    public PessoaRepository(EntityManager entityManager) {
        super(entityManager, Pessoa.class);
    }

    @Override
    public int countListagem(HashMap<String, Object> filter) {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Pessoa.class);
        modelFilter.addFilter(filter);
        modelFilter.addOperador(ModelFilter.Operadores.ILIKE, "nome");
        return super.countListagem(modelFilter); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Pessoa> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {

        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Pessoa.class);
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

   


    
    /**
     * Vai buscar uma pessoa pelo email
     * @param email
     * @return 
     */
    public Pessoa getPessoaByEmail(String email) {

        try {
            return getPurePojo(Pessoa.class, "select usr from Pessoa usr where usr.email = ?1 ", email);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

        return null;
    }

    
    /**
      * Retorna o cliente de acordo com o usuario logado
     * @param usuarioLogado
     * @return 
      */
    public Pessoa getClienteByUsuario(Usuario usuarioLogado) {

        try {

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT cli FROM Pessoa cli ");
            sb.append("INNER JOIN cli.usuariosClientes user ");
            sb.append("INNER JOIN cli.tiposEnvolvidos tipo ");
            sb.append("WHERE user.id =  ").append(usuarioLogado.getId());
            sb.append(" AND tipo IN ('").append(TipoEnvolvido.CLIENTE).append("')");

            return getPurePojo(Pessoa.class, sb.toString());

        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

        return null;

    }

    /**
     * Vai retornar o contratante de um evento
     *
     * @param idEvento
     * @return
     */
    public Pessoa getContratanteEvento(Long idEvento) {

        try {
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT cli FROM Pessoa cli ");
            sb.append("INNER JOIN cli.eventos eve ");
            sb.append("WHERE eve.id = ?1 ");

            return getPurePojo(Pessoa.class, sb.toString(), idEvento);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
        return null;

    }

}
