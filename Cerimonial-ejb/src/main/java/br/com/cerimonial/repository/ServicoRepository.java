/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Servico;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class ServicoRepository extends AbstractRepository<Servico>{
    
    
    public ServicoRepository(EntityManager entityManager) {
        super(entityManager, Servico.class);
    }
    
    
    public List<Servico> findAllByNome(String nome) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM servico as cf");
        sb.append(" WHERE 1=1 ");
        sb.append(" AND TRANSLATE(UPPER(cf.nome), 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')");
        sb.append(" LIKE TRANSLATE('%").append(nome).append("%', 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')");
        return getPureListNative(Servico.class, sb.toString());
    }

    public List<Servico> findAllByFornecedorId(Long idFornecedor) {
        
        StringBuilder sb = new StringBuilder();
        sb.append(" select serv from Servico serv");
        sb.append(" INNER JOIN serv.fornecedores forne");
        sb.append(" WHERE 1=1 ");
        sb.append(" AND forne.id = ?1 ");
        
        return getPureList(Servico.class, sb.toString(), idFornecedor);
        
    }
}
