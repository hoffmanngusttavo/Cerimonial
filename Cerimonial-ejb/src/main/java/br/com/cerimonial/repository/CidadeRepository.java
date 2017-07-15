/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Cidade;
import br.com.cerimonial.entity.Estado;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class CidadeRepository extends AbstractRepository<Cidade> {

    public CidadeRepository(EntityManager entityManager) {
        super(entityManager, Cidade.class);
    }

    public Cidade findByNomeEstado(String city, String sigla) throws Exception {
        StringBuilder sql = new StringBuilder("select city from Cidade city  ");
        sql.append("INNER JOIN city.estado estado");
        sql.append(" where 1=1");
        sql.append(" and city.nome = ?1");
        sql.append(" and estado.sigla = ?2");
        return getPurePojo(Cidade.class, sql.toString(), city, sigla);
    }

    public List<Cidade> findByEstado(Estado estado) throws Exception {
        StringBuilder sql = new StringBuilder("select city from Cidade city  ");
        sql.append("INNER JOIN city.estado estado");
        sql.append(" where 1=1");
        sql.append(" and estado.id = ?1");
        return getPureList(Cidade.class, sql.toString(), estado.getId());
    }

}
