/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ContatoEvento;
import java.util.List;
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
    public ContatoEvento getEntity(Long id) throws Exception {
        ContatoEvento entity = super.getEntity(id);

        if (entity != null) {
            if (entity.getTipoIndicacao() != null) {
                entity.getTipoIndicacao().getId();
            }
            if (entity.getStatus() != null) {
                entity.getStatus().getId();
            }
            
            if (entity.getPropostas() != null) {
                entity.getPropostas().size();
                entity.getPropostas().stream().forEach(item -> {
                    if(item.getModeloProposta() != null){
                        item.getModeloProposta().getId();
                    }
                    if(item.getAnexos() != null){
                        item.getAnexos().size();
                    }
                });
            }
        }

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

}
