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

    public Pessoa getPessoa(Long id) throws Exception {
        Pessoa entity = super.getEntity(Pessoa.class, id);
        if (entity != null && entity.getEndereco() != null) {
            entity.getEndereco().getId();
        }
        return entity;
    }

    @Override
    public int countListagem(HashMap<String, Object> filter)  {
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

    //-----------Fornecedores----------------------
    public List<Pessoa> findRangeListagemFornecedor(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) throws Exception {
        List<Pessoa> fornecedores = this.findRangeListagem(params, max, offset, sortField, sortAscDesc);
        if (CerimonialUtils.isListNotBlank(fornecedores)) {
            for (Pessoa fornecedor : fornecedores) {
                if (fornecedor.getCategoriasFornecedor() != null) {
                    fornecedor.getCategoriasFornecedor().size();
                }
            }
        }
        return fornecedores;
    }

    public Pessoa getEntityFornecedorCategoria(Long id) throws Exception {
        Pessoa entity = getEntity(Pessoa.class, id);
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

    public Pessoa getClienteByEmail(String email) {

        try {
            return getPurePojo(Pessoa.class, "select usr from Pessoa usr where usr.email = ?1 and usr.tipoEnvolvido = ?2", email, TipoEnvolvido.CLIENTE);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

        return null;
    }

}
