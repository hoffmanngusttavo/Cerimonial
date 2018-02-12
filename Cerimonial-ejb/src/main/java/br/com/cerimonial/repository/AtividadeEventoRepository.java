/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.AtividadeEvento;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class AtividadeEventoRepository extends AbstractRepository<AtividadeEvento>{

    public AtividadeEventoRepository(EntityManager entityManager) {
        super(entityManager, AtividadeEvento.class);
    }

    public List<AtividadeEvento> findAtividadesByIdEvento(Long idEvento) {
        
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT task FROM AtividadeEvento task ");
        sb.append("INNER JOIN task.evento eve ");
        sb.append("WHERE 1=1 ");
        sb.append("AND eve.id =?1 ");
        sb.append("ORDER BY task.prazoInicial ASC");
        
        return getPureList(AtividadeEvento.class, sb.toString(), idEvento);
    }
    
    
    
}
