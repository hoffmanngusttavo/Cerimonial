/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.entity.Parcela;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.enums.TipoLancamento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.LancamentoRepository;
import br.com.cerimonial.service.utils.EmpresaCache;
import br.com.cerimonial.utils.CollectionUtils;
import br.com.cerimonial.utils.DateUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LancamentoService extends BasicService<Lancamento> {

    private LancamentoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new LancamentoRepository(em);
    }

    @Override
    public Lancamento getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Lancamento save(Lancamento entity) throws Exception {

        isValid(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public List<Lancamento> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(LancamentoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(Lancamento categoria) throws Exception {

        isValid(categoria);

        repository.delete(categoria.getId());
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Lancamento> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean isValid(Lancamento entity) {
        if (entity == null) {
            throw new GenericException("Lançamento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

    /**
     * Vai buscar um lançamento que foi gerado a partir de um orçamento
     * carregando em lazy as parcelas
     *
     * @param idOrcamento
     * @return
     */
    public Lancamento findLancamentoOrcamento(Long idOrcamento) {

        if (idOrcamento == null) {
            throw new GenericException("Id Orçamento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        Lancamento lancamento = repository.findLancamentoOrcamento(idOrcamento);

        if (lancamento != null) {

            if (lancamento.getParcelas() != null) {
                lancamento.getParcelas().size();
            }

        }

        return lancamento;
    }

    /**
     * Vai incrementar no lançamento a quantidade de parcelas que é passado por
     * parametro
     *
     * @param numeroParcelas
     * @param entity
     * @return
     */
    public Lancamento atualizarNumeroParcelas(int numeroParcelas, Lancamento entity) {

        isValid(entity);

        if (numeroParcelas <= 0) {
            throw new GenericException("O número de parcelas deve ser maior que zero", ErrorCode.BAD_REQUEST.getCode());
        }

        double valorBase = entity.getValorBase() != null ? entity.getValorBase() : 0;
        double valorPago = entity.getValorTotalPago() != null ? entity.getValorTotalPago() : 0;

        //
        double valorRestanteBruto = valorBase - valorPago;

        //INICIO calibragem
        int criarRemoverParcelas = numeroParcelas;

        if (criarRemoverParcelas > 0) {//adicionar

            for (int i = 0; i < criarRemoverParcelas; i++) {

                entity.adicionarParcela(new Parcela());

            }
        } else if (criarRemoverParcelas < 0) {

            int qntdeRemover = criarRemoverParcelas * -1;

            for (int i = 0; i < qntdeRemover; i++) {

                entity.getParcelas().remove(entity.getParcelas().size() - 1);
            }
        }
        //FIM calibragem
        //
        double valorCadaParcelaBruto = ((int) ((valorRestanteBruto / numeroParcelas) * 100d)) / 100d;
        double compensarArredondamentoBruto = valorRestanteBruto - (valorCadaParcelaBruto * (double) numeroParcelas);

        int countParcelaRecalculada = 1;//pegar mes sequinte

        Date dataUltimaParcelaPaga = entity.getDataVencimentoUltimaParcelaPaga();

        if (dataUltimaParcelaPaga == null) {
            dataUltimaParcelaPaga = entity.getDataVencimentoPrimeiraParcela();

        }

        for (int i = 0; i < entity.getParcelas().size(); i++) {

            if (entity.getParcelas().get(i).isPago()) {
                continue;
            }

            entity.getParcelas().get(i).setNumeroParcela(i + 1);
            entity.getParcelas().get(i).setValorCobrado(valorCadaParcelaBruto);

            if (i == (entity.getParcelas().size() - 1)) {
                entity.getParcelas().get(i).setValorCobrado(entity.getParcelas().get(i).getValorCobrado() + compensarArredondamentoBruto);
            }

            entity.getParcelas().get(i).setDataVencimento(DateUtils.somaSubtraiDatasDeDataBase(dataUltimaParcelaPaga, countParcelaRecalculada, GregorianCalendar.MONTH));
            //
            countParcelaRecalculada++;
        }

        return entity;
    }

    /**
     * Vai instanciar um lançamento a partir dos dados do orçamento aprovado do evento
     * @param orcamentoEvento relacionamento 1 to 1 lançamento
     * @param contratantes
     * @return 
     * @throws java.lang.Exception 
     */
    public Lancamento criarNovoLancamentoSaidaOrcamento(OrcamentoEvento orcamentoEvento, List<Pessoa> contratantes) throws Exception {

        if (orcamentoEvento == null) {
            throw new GenericException("Orçamento é nulo para o lançamento", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if(CollectionUtils.isBlank(contratantes)){
            throw new GenericException("Para o lançamento deve ter pelo menos 1 contratante responsável pelo pagamento", ErrorCode.BAD_REQUEST.getCode());
        }

        Lancamento entity = new Lancamento(TipoLancamento.DESPESA);
        
        entity.setOrcamentoEvento(orcamentoEvento);

        entity.setValorBase(orcamentoEvento.getValorFinal());
        
        //rita
        entity.setEnvolvidoOrigem(EmpresaCache.getEmpresa().getPessoa());
        
        // responsavel pelo pagamento
        entity.setEnvolvidoDestino(contratantes.get(0));
        
        entity.adicionarParcela(new Parcela(entity, orcamentoEvento.getValorFinal(), new Date()));
        
        return entity;
    }

    public Lancamento saveLancamentoOrcamento(Lancamento entity) throws Exception {
        
        isValid(entity);
        
        if(entity.getId() == null){
            
            // criar vinculo com lancamento de saida do evento com lancamento de entrada da empresa
            
            return save(entity);
            
        }else{
        
            return save(entity);
        }
        
    }

}
