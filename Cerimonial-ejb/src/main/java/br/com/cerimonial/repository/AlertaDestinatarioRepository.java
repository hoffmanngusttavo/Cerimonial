/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.AlertaDestinatario;
import br.com.cerimonial.entity.Usuario;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class AlertaDestinatarioRepository extends AbstractRepository<AlertaDestinatario>{

    public AlertaDestinatarioRepository(EntityManager entityManager) {
        super(entityManager, AlertaDestinatario.class);
    }
    
    
    /**
     * Vai retornar todos os alertas não vizualizados de um usuário a partir de tal data
     *
     * @param limit
     * @param usuario
     * @param data
     * @return
     */
    public List<AlertaDestinatario> findAlertasUsuarioNaoVisualizados(int limit, Usuario usuario, Date data) {
        try {
            SimpleDateFormat sdfPi = new SimpleDateFormat("dd/MM/yyyy");
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT dest FROM AlertaDestinatario dest ");
            sb.append(" INNER JOIN dest.alerta alerta ");
            sb.append(" INNER JOIN dest.destinatario usu ");
            sb.append(" WHERE dest.visualizado = false ");
            sb.append(" AND alerta.vigenciaInicial >= '").append(sdfPi.format(data)).append("'");
            sb.append(" AND usu.id = ").append(usuario.getId());
            sb.append(" ORDER BY dest.id DESC ");

            return getPureListRange(AlertaDestinatario.class, sb.toString(), limit, 0);
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<AlertaDestinatario>();
    }
    
    
}
