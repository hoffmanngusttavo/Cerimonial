/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.ContratoEvento;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.utils.CerimonialUtils;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gustavo Hoffmann
 */
public class OrcamentoEventoRepository extends AbstractRepository<OrcamentoEvento> {

    public OrcamentoEventoRepository(EntityManager entityManager) {
        super(entityManager, OrcamentoEvento.class);
    }

    /**
     *
     * @param id do ContatoEvento
     * @return Uma lista de orçamentos/propostas de um contato ordenados
     * @throws java.lang.Exception
     */
    public List<OrcamentoEvento> findAllByContatoId(Long id) throws Exception {
        StringBuilder sql = new StringBuilder("select o from OrcamentoEvento o  ");
        sql.append("INNER JOIN o.contatoEvento contato");
        sql.append(" where 1=1");
        sql.append(" and contato.id = ?1");
        sql.append(" order by o.id desc");
        List<OrcamentoEvento> propostas = getPureList(OrcamentoEvento.class, sql.toString(), id);
        if (CerimonialUtils.isListNotBlank(propostas)) {
            propostas.stream().forEach(item -> {
                if (item.getModeloProposta() != null) {
                    item.getModeloProposta().getId();
                }
                if (item.getAnexos() != null) {
                    item.getAnexos().size();
                }
            });
        }

        return propostas;

    }

    public OrcamentoEvento getOrcamentoByEvento(Long idEvento) {

        StringBuilder sql = new StringBuilder("SELECT orc FROM OrcamentoEvento orc  ");
        sql.append(" INNER JOIN orc.eventos evento");
        sql.append(" LEFT JOIN orc.modeloProposta mp");
        sql.append(" LEFT JOIN orc.anexos arq");
        sql.append(" WHERE 1=1");
        sql.append(" AND evento.id = ?1");

        OrcamentoEvento orcamento = getPurePojo(OrcamentoEvento.class, sql.toString(), idEvento);

        if (orcamento != null) {

            if (orcamento.getEvento() != null) {
                orcamento.getEvento().getId();
            }
            
            if (orcamento.getAnexos() != null) {
                orcamento.getAnexos().size();
            }

            if (orcamento.getModeloProposta() != null) {
                orcamento.getModeloProposta().getId();
            }

        }
        return orcamento;
    }

}
