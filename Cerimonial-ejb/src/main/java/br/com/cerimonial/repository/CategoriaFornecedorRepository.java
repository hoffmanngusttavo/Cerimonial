/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.CategoriaFornecedor;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.utils.ModelFilter;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class CategoriaFornecedorRepository extends BasicRepository {

    public CategoriaFornecedorRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public CategoriaFornecedor create(CategoriaFornecedor entity) throws Exception {
        addEntity(CategoriaFornecedor.class, entity);
        return entity;
    }

    public void delete(CategoriaFornecedor entity) throws Exception {
        removeEntity(entity);
    }

    public CategoriaFornecedor edit(CategoriaFornecedor entity) throws Exception {
        return setEntity(CategoriaFornecedor.class, entity);
    }

    public CategoriaFornecedor getEntity(Long id) throws Exception {
        return getEntity(CategoriaFornecedor.class, id);
    }

    public List<CategoriaFornecedor> findAll() throws Exception {
        return getPureList(CategoriaFornecedor.class, "select cat from CategoriaFornecedor cat ");
    }

    public int countAll() throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(CategoriaFornecedor.class);
        return getCount(modelFilter.getSqlCountBase());
    }

    public List<CategoriaFornecedor> findRangeListagemCategorias(int max, int offset, String sortField, String sortAscDesc) throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(CategoriaFornecedor.class);
        modelFilter.setLimit(max);
        modelFilter.setOffSet(offset);
        if (sortField != null) {
            modelFilter.addOrderBy(sortField, sortAscDesc);
        }

        return getPureList(CategoriaFornecedor.class, modelFilter.getSqlBase());
    }
}
