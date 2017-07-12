/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.utils.ModelFilter;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class ModeloPropostaRepository extends BasicRepository{

    public ModeloPropostaRepository(EntityManager entityManager) {
        super(entityManager);
    }
    
    public ModeloProposta getEntity(Long id) throws Exception {
        return getEntity(ModeloProposta.class, id);
    }

    public ModeloProposta create(ModeloProposta entity) throws Exception {
        addEntity(ModeloProposta.class, entity);
        return entity;
    }

    public ModeloProposta edit(ModeloProposta entity) throws Exception {
        return setEntity(ModeloProposta.class, entity);
    }

    public void delete(ModeloProposta entity) throws Exception {
        removeEntity(entity);
    }
   
    public List<ModeloProposta> findRangeListagemCategorias(int max, int offset, String sortField, String sortAscDesc) throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(ModeloProposta.class);
        modelFilter.setLimit(max);
        modelFilter.setOffSet(offset);
        if (sortField != null) {
            modelFilter.addOrderBy(sortField, sortAscDesc);
        }

        return getPureListRange(ModeloProposta.class, modelFilter.getSqlBase(), max, offset);
    }
    
    public List<ModeloProposta> findAll() throws Exception {
        return getPureList(ModeloProposta.class, "select cat from ModeloProposta cat ");
    }

    public int countAll() throws Exception {
        ModelFilter modelFilter = ModelFilter.getInstance();
        modelFilter.setEntidade(ModeloProposta.class);
        return getCount(modelFilter.getSqlCountBase());
    }
    
    
    
}
