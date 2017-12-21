/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.enums.TipoEvento;
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

   

    public List<ModeloProposta> findModelosPropostaByTipoEvento(TipoEvento tipoEvento) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT mod FROM ModeloProposta mod ");
        sb.append("WHERE mod.tipoEvento =?1 ");

        return getPureList(ModeloProposta.class, sb.toString(), tipoEvento);

    }

}
