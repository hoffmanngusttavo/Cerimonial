/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.CustoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.enums.SituacaoEvento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class LancamentoRepository extends AbstractRepository<Lancamento> {

    public LancamentoRepository(EntityManager entityManager) {
        super(entityManager, Lancamento.class);
    }

    public Lancamento findLancamentoOrcamento(Long idOrcamento) {

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT lan FROM Lancamento lan ");
        sb.append("INNER JOIN lan.orcamentoEvento orc ");
        sb.append("WHERE 1=1 ");
        sb.append("AND orc.id =?1 ");

        Lancamento lancamento = null;

        try {
            
            lancamento = getPurePojo(Lancamento.class, sb.toString(), idOrcamento);

        } catch (GenericException e) {

            if (e != null && ErrorCode.NOT_FOUND.getCode() != e.getCode()) {
                throw new GenericException(e.getMessage(), e.getCode());
            }

        }

        return lancamento;
    }

}
