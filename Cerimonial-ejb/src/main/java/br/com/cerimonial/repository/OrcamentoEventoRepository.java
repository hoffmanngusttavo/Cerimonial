/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.entity.Pessoa;
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
        //carregar lazy
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

     /**
     * Método vai buscar o orçamento de um evento;
     *
     * @param idEvento do Evento
     * @return
     */
    public OrcamentoEvento getOrcamentoByEvento(Long idEvento) {

        StringBuilder sql = new StringBuilder("SELECT orc FROM OrcamentoEvento orc  ");
        sql.append(" INNER JOIN orc.eventos evento");
        sql.append(" LEFT JOIN orc.modeloProposta mp");
        sql.append(" LEFT JOIN orc.anexos arq");
        sql.append(" WHERE 1=1");
        sql.append(" AND evento.id = ?1");

        return getPurePojo(OrcamentoEvento.class, sql.toString(), idEvento);
    }
    
    /**
     * Método vai buscar o orçamento de um evento de um contratante;
     *
     * @param idEvento do Evento
     * @param contratante
     * @return
     */
    public OrcamentoEvento getOrcamentoContratante(Long idEvento, Pessoa contratante) {

        StringBuilder sql = new StringBuilder("SELECT orc FROM OrcamentoEvento orc  ");
        sql.append(" INNER JOIN orc.eventos evento");
        sql.append(" INNER JOIN evento.contratantes cli");
        sql.append(" INNER JOIN cli.pessoa con");
        sql.append(" LEFT JOIN orc.modeloProposta mp");
        sql.append(" LEFT JOIN orc.anexos arq");
        sql.append(" WHERE 1=1");
        sql.append(" AND evento.id = ?1");
        sql.append(" AND con.id = ?2");

        return getPurePojo(OrcamentoEvento.class, sql.toString(), idEvento, contratante.getId());

    }

}
