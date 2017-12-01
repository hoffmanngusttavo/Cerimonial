/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.entity.TipoEvento;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class ModeloPropostaRepository extends AbstractRepository<ModeloProposta> {

    public ModeloPropostaRepository(EntityManager entityManager) {
        super(entityManager, ModeloProposta.class);
    }

    @Override
    public ModeloProposta getEntity(Long id) {
        ModeloProposta modeloProposta = super.getEntity(id);
        if (modeloProposta != null && modeloProposta.getAnexos() != null) {
            modeloProposta.getAnexos().size();
        }
        return modeloProposta;
    }

    public List<ModeloProposta> findModelosPropostaByTipoEvento(TipoEvento tipoEvento) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT mod FROM ModeloProposta mod ");
        sb.append("INNER JOIN mod.tipoEvento tipo ");
        sb.append("WHERE tipo.id = ?1 ");

        return getPureList(ModeloProposta.class, sb.toString(), tipoEvento.getId());

    }

}
