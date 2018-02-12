/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.repository;

import br.com.cerimonial.entity.Parcela;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author hoffmann
 */
public class ParcelaRepository extends AbstractRepository<Parcela> {

    public ParcelaRepository(EntityManager entityManager) {
        super(entityManager, Parcela.class);
    }

    public List<Parcela> findParcelasAPagarEvento(int max, String data) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT par FROM Parcela par ");
        sb.append(" INNER JOIN par.lancamento lan ");
        sb.append(" INNER JOIN lan.custoEvento custo ");
        sb.append(" WHERE custo.id IS NOT NULL ");
        sb.append(" AND lan.tipoLancamento = 'DESPESA' ");
        sb.append(" AND par.pago = false ");
        sb.append(" AND par.dataVencimento > '").append(data).append("'");
        sb.append(" ORDER BY par.dataVencimento ASC ");

        return getPureListRange(Parcela.class, sb.toString(), max, 0);
    }

    public List<Parcela> findParcelasVencidasEvento(int max, String dataFormatada) {
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT par FROM Parcela par ");
        sb.append(" INNER JOIN par.lancamento lan ");
        sb.append(" INNER JOIN lan.custoEvento custo ");
        sb.append(" WHERE custo.id IS NOT NULL ");
        sb.append(" AND par.pago = false ");
        sb.append(" AND lan.tipoLancamento = 'DESPESA' ");
        sb.append(" AND par.dataVencimento <= '").append(dataFormatada).append("'");
        sb.append(" ORDER BY par.dataVencimento ASC ");

        return getPureListRange(Parcela.class, sb.toString(), max, 0);
    }

    public List<Parcela> findParcelasVencidasEmpresa(int max, String dataFormatada) {
       
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT par FROM Parcela par ");
        sb.append(" INNER JOIN par.lancamento lan ");
        sb.append(" INNER JOIN lan.custoEvento custo ");
        sb.append(" WHERE custo.id IS NULL ");
        sb.append(" AND par.pago = false ");
        sb.append(" AND par.dataVencimento <= '").append(dataFormatada).append("'");
        sb.append(" ORDER BY par.dataVencimento ASC ");

        return getPureListRange(Parcela.class, sb.toString(), max, 0);
        
    }

    public List<Parcela> findParcelasAReceberEmpresa(int max, String dataFormatada) {
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT par FROM Parcela par ");
        sb.append(" INNER JOIN par.lancamento lan ");
        sb.append(" INNER JOIN lan.custoEvento custo ");
        sb.append(" WHERE custo.id IS NULL ");
        sb.append(" AND par.pago = false ");
        sb.append(" AND lan.tipoLancamento = 'ENTRADA' ");
        sb.append(" AND par.dataVencimento <= '").append(dataFormatada).append("'");
        sb.append(" ORDER BY par.dataVencimento ASC ");

        return getPureListRange(Parcela.class, sb.toString(), max, 0);
        
    }

    public List<Parcela> findParcelasAPagarEmpresa(int max, String dataFormatada) {
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT par FROM Parcela par ");
        sb.append(" INNER JOIN par.lancamento lan ");
        sb.append(" INNER JOIN lan.custoEvento custo ");
        sb.append(" WHERE custo.id IS NULL ");
        sb.append(" AND par.pago = false ");
        sb.append(" AND lan.tipoLancamento = 'DESPESA' ");
        sb.append(" AND par.dataVencimento <= '").append(dataFormatada).append("'");
        sb.append(" ORDER BY par.dataVencimento ASC ");

        return getPureListRange(Parcela.class, sb.toString(), max, 0);
        
    }

}
