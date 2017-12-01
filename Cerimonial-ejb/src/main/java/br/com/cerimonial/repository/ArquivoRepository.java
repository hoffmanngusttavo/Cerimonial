/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ModeloEmail;
import br.com.cerimonial.entity.ModeloProposta;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class ArquivoRepository extends AbstractRepository<Arquivo> {

    public ArquivoRepository(EntityManager entityManager) {
        super(entityManager, Arquivo.class);
    }

    public List<Arquivo> getArquivosByModeloProposta(ModeloProposta entity) throws Exception {

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT (o) FROM Arquivo as o  ");
        hql.append("INNER JOIN o.modelosPropostas mp ");
        hql.append("WHERE mp.id = ?1");

        return getPureList(Arquivo.class, hql.toString(), entity.getId());
    }

    public List<Arquivo> getArquivosByModeloProposta(ModeloEmail entity) {
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT (o) FROM Arquivo as o  ");
        hql.append("INNER JOIN o.modeloEmails me ");
        hql.append("WHERE me.id = ?1");

        return getPureList(Arquivo.class, hql.toString(), entity.getId());
    }

}
