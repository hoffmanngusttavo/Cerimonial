/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.utils.ModelFilter;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class PessoaRepository extends BasicRepository {

    public PessoaRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public Pessoa create(Pessoa entity) throws Exception {
        addEntity(Pessoa.class, entity);
        return entity;
    }

    public void delete(Pessoa entity) throws Exception {
        removeEntity(entity);
    }

    public Pessoa edit(Pessoa entity) throws Exception {
        return setEntity(Pessoa.class, entity);
    }

    public Pessoa getPessoa(Long id) throws Exception {
        return getEntity(Pessoa.class, id);
    }

    public int countAll() throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Pessoa.class);
        return getCount(modelFilter.getSqlCountBase());
    }

    //-----------Clientes----------------------
    public List<Pessoa> findRangeListagemClientes(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) throws Exception {
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

        return getPureList(Pessoa.class, modelFilter.getSqlBase());
    }

    public int countListagemClientes(HashMap<String, Object> filter) throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Pessoa.class);
        modelFilter.addFilter(filter);
        modelFilter.addOperador(ModelFilter.Operadores.ILIKE, "nome");
        return getCount(modelFilter.getSqlCountBase());
    }

    //-----------Fornecedores----------------------
    public List<Pessoa> findRangeListagemFornecedor(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) throws Exception {
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

        return getPureList(Pessoa.class, modelFilter.getSqlBase());
    }

    public int countListagemFornecedor(HashMap<String, Object> filter) throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Pessoa.class);
        modelFilter.addFilter(filter);
        modelFilter.addOperador(ModelFilter.Operadores.ILIKE, "nome");
        return getCount(modelFilter.getSqlCountBase());
    }

    //-----------Colaboradores----------------------
    public int countListagemColaborador(HashMap<String, Object> filter) throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(Pessoa.class);
        modelFilter.addFilter(filter);
        modelFilter.addOperador(ModelFilter.Operadores.ILIKE, "nome");
        return getCount(modelFilter.getSqlCountBase());
    }

    public List<Pessoa> findRangeListagemColaborador(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) throws Exception {
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

        return getPureList(Pessoa.class, modelFilter.getSqlBase());
    }

}
