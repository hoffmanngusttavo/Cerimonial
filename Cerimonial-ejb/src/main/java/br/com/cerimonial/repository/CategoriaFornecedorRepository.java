/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.CategoriaFornecedor;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class CategoriaFornecedorRepository extends AbstractRepository<CategoriaFornecedor> {

    public CategoriaFornecedorRepository(EntityManager entityManager) {
        super(entityManager, CategoriaFornecedor.class);
    }

    public List<CategoriaFornecedor> findAllByNome(String nome) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM categoriafornecedor as cf");
        sb.append(" WHERE 1=1 ");
        sb.append(" AND TRANSLATE(UPPER(cf.nome), 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')");
        sb.append(" LIKE TRANSLATE('%").append(nome).append("%', 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')");
        return getPureListNative(CategoriaFornecedor.class, sb.toString());
    }
}
