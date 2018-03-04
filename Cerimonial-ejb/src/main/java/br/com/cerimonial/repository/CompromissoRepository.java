/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.AlertaDestinatario;
import br.com.cerimonial.entity.Compromisso;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class CompromissoRepository extends AbstractRepository<Compromisso>{

    public CompromissoRepository(EntityManager entityManager) {
        super(entityManager, Compromisso.class);
    }

    public List<Compromisso> findCompromissosDia(String data) {
       
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT comp FROM Compromisso comp ");
            sb.append(" WHERE 1=1 ");
            sb.append(" AND comp.data = '").append(data).append("'");
            sb.append(" ORDER BY comp.data, comp.data ASC ");

            return getPureList(Compromisso.class, sb.toString());
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<Compromisso>();
        
    }
 
    
    
}
