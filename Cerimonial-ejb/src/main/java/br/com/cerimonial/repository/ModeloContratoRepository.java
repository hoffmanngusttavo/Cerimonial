/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.ModeloContrato;
import br.com.cerimonial.entity.TipoEvento;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class ModeloContratoRepository extends AbstractRepository<ModeloContrato> {

    public ModeloContratoRepository(EntityManager entityManager) {
        super(entityManager, ModeloContrato.class);
    }

    public List<ModeloContrato> findModelosContratoByTipoEvento(TipoEvento tipoEvento) {

        try {
            
            StringBuilder sql = new StringBuilder("SELECT mc FROM ModeloContrato mc  ");
            sql.append("INNER JOIN mc.tipoEvento tipo");
            sql.append(" WHERE 1=1");
            sql.append(" AND tipo.id = ?1");

            return getPureList(ModeloContrato.class, sql.toString(), tipoEvento.getId());
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<>();

    }

}
