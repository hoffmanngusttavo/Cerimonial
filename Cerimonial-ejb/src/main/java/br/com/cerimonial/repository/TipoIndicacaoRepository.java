/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.TipoIndicacao;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class TipoIndicacaoRepository  extends AbstractRepository<TipoIndicacao>{

    public TipoIndicacaoRepository(EntityManager entityManager) {
        super(entityManager, TipoIndicacao.class);
    }

    public List<TipoIndicacao> findTiposIndicacaoByNome(String value) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT (ti) FROM TipoIndicacao as ti");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND TRANSLATE(UPPER(ti.nome), 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')");
        sql.append(" LIKE TRANSLATE('%").append(value).append("%', 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')");

        return getPureList(TipoIndicacao.class, sql.toString());
    }
    
}
