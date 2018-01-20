/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.CustoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.enums.SituacaoEvento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class CustoEventoRepository extends AbstractRepository<CustoEvento> {

    public CustoEventoRepository(EntityManager entityManager) {
        super(entityManager, CustoEvento.class);
    }

    public CustoEvento findCustoEventoByIdEvento(Long idEvento) {

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ce FROM CustoEvento ce ");
        sb.append("INNER JOIN ce.evento eve ");
        sb.append("WHERE 1=1 ");
        sb.append("AND eve.id =?1 ");

        CustoEvento custoEvento = null;

        try {
            custoEvento = getPurePojo(CustoEvento.class, sb.toString(), idEvento);
            
        } catch (GenericException e) {
            
            if(e != null && ErrorCode.NOT_FOUND.getCode() != e.getCode()){
                throw new GenericException(e.getMessage(), e.getCode());
            }
            
        }

        return custoEvento;
    }
}
