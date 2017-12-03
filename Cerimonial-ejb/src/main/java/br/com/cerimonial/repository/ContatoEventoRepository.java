/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.Evento;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class ContatoEventoRepository extends AbstractRepository<ContatoEvento> {

    public ContatoEventoRepository(EntityManager entityManager) {
        super(entityManager, ContatoEvento.class);
    }

    @Override
    public ContatoEvento getEntity(Long id)  {
        ContatoEvento entity = super.getEntity(id);
        return entity;
    }

    public List<String> getLocaisEvento(String value) throws Exception {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT localevento ");
        sql.append(" FROM contatoevento ce ");
        sql.append(" WHERE localevento IS NOT NULL ");
        sql.append(" AND TRANSLATE(UPPER(ce.localevento), 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')");
        sql.append(" LIKE TRANSLATE('%").append(value).append("%', 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')");
        sql.append(" GROUP BY localevento");

        return getPureListNative(sql.toString());
    }

    public ContatoEvento getContatoInicialByEvento(Evento evento) {

        try {
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT con FROM ContatoEvento con ");
            sb.append("INNER JOIN con.propostas pro ");
            sb.append("INNER JOIN pro.eventos eve ");
            sb.append("WHERE 1=1 ");
            sb.append("AND eve.id =?1");

            return getPurePojo(ContatoEvento.class, sb.toString(), evento.getId());

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

}
