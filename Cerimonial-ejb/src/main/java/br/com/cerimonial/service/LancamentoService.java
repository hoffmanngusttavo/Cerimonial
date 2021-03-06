/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.AtividadeEvento;
import br.com.cerimonial.entity.CustoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.entity.Parcela;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Servico;
import br.com.cerimonial.enums.TipoLancamento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.LancamentoRepository;
import br.com.cerimonial.service.utils.EmpresaCache;
import br.com.cerimonial.utils.CollectionUtils;
import br.com.cerimonial.utils.DateUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

    @EJB
    private ServicoService servicoService;
    @EJB
    private EventoService eventoService;
    @EJB
    private ServicoPrestadoEventoService servicoPrestadoEventoService;
    @EJB
    private CustoEventoService custoEventoService;
    @EJB
    private ParcelaService parcelaService;
    @EJB
    private ParcelaVinculadaService parcelaVinculadaService;
    @EJB
    private AtividadeEventoService atividadeEventoService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new LancamentoRepository(em);
    }

    @Override
    public Lancamento findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Lancamento save(Lancamento entity) throws Exception {

        validateObjectNull(entity);

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

    public void delete(Lancamento entity) throws Exception {

        validateObjectAndIdNull(entity);

        repository.delete(entity.getId());
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

    /**
     * Vai buscar um lançamento que foi gerado a partir de um orçamento
     * carregando em lazy as parcelas
     *
     * @param idServicoPrestado
     * @return
     */
    public Lancamento findLancamentoByServicoPrestadoId(Long idServicoPrestado) {

        validateId(idServicoPrestado);

        Lancamento lancamento = repository.findLancamentoByServicoPrestadoId(idServicoPrestado);

        smartLazy(lancamento, Arrays.asList("parcelas"));

        return lancamento;
    }

    /**
     * Vai buscar um lançamento que foi gerado a partir de uma atividade
     * carregando em lazy as parcelas e orcamentos
     *
     * @param idAtividade
     * @return
     */
    public Lancamento findLancamentoByAtividadeId(Long idAtividade) {

        validateId(idAtividade);

        Lancamento lancamento = repository.findLancamentoByAtividadeId(idAtividade);

        return smartLazy(lancamento, Arrays.asList("parcelas", "orcamentos"));
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

        validateObjectNull(entity);

        if (numeroParcelas <= 0) {
            throw new GenericException("O número de parcelas deve ser maior que zero", ErrorCode.BAD_REQUEST.getCode());
        }

        if (entity.getParcelas() == null) {
            entity.setParcelas(new ArrayList<Parcela>());
        }

        //INICIO calibragem
        int criarRemoverParcelas = numeroParcelas - entity.getParcelas().size();

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

        double valorBase = entity.getValorBase() != null ? entity.getValorBase() : 0;
        double valorPago = entity.getValorTotalPago() != null ? entity.getValorTotalPago() : 0;
        //
        double valorRestanteBruto = valorBase - valorPago;
        //
        double valorCadaParcelaBruto = ((int) ((valorRestanteBruto / numeroParcelas) * 100d)) / 100d;
        double compensarArredondamentoBruto = valorRestanteBruto - (valorCadaParcelaBruto * (double) numeroParcelas);

        Date dataVencimento = new Date(entity.getDataVencimento().getTime());

        for (int i = 0; i < entity.getParcelas().size(); i++) {

            if (entity.getParcelas().get(i).isPago()) {
                continue;
            }

            entity.getParcelas().get(i).setNumeroParcela(i + 1);
            entity.getParcelas().get(i).setValorCobrado(valorCadaParcelaBruto);

            if (i == (entity.getParcelas().size() - 1)) {
                entity.getParcelas().get(i).setValorCobrado(entity.getParcelas().get(i).getValorCobrado() + compensarArredondamentoBruto);
            }

            if (i == 0) {

                entity.getParcelas().get(i).setDataVencimento(dataVencimento);

            } else {

                entity.getParcelas().get(i).setDataVencimento(DateUtils.addMonths(dataVencimento, 1));

            }

            dataVencimento = entity.getParcelas().get(i).getDataVencimento();
        }

        return entity;
    }

    /**
     * Vai instanciar um lançamento a partir dos dados do orçamento aprovado do
     * evento
     *
     * @param servicoPrestadoEvento
     * @param contratantes
     * @param evento
     * @return
     * @throws java.lang.Exception
     */
    public Lancamento criarNovoLancamentoSaidaOrcamento(List<Pessoa> contratantes, Evento evento) throws Exception {

        eventoService.validateObjectAndIdNull(evento);

        custoEventoService.validateObjectAndIdNull(evento.getCustoEvento());

        servicoPrestadoEventoService.validateObjectNull(evento.getPreEvento().getServicoPrestadoEvento());

        if (CollectionUtils.isBlank(contratantes)) {
            throw new GenericException("Para o lançamento deve ter pelo menos 1 contratante responsável pelo pagamento", ErrorCode.BAD_REQUEST.getCode());
        }

        Lancamento entity = new Lancamento(TipoLancamento.DESPESA);

        entity.setServicoPrestadoEvento(evento.getPreEvento().getServicoPrestadoEvento());

        entity.setCustoEvento(evento.getCustoEvento());

        entity.setValorEstimado(evento.getPreEvento().getServicoPrestadoEvento().getValorFinal());

        entity.setValorBase(evento.getPreEvento().getServicoPrestadoEvento().getValorFinal());

        //rita
        entity.setEnvolvidoOrigem(EmpresaCache.getEmpresa().getPessoa());

        if (entity.getEnvolvidoOrigem() != null) {

            List<Servico> servicos = servicoService.findAllByFornecedorId(entity.getEnvolvidoOrigem().getId());

            if (CollectionUtils.isNotBlank(servicos)) {
                entity.setServico(servicos.get(0));
            }

        }

        // responsavel pelo pagamento
        entity.setEnvolvidoDestino(contratantes.get(0));

        entity.adicionarParcela(new Parcela(entity, evento.getPreEvento().getServicoPrestadoEvento().getValorFinal(), new Date()));

        return entity;
    }

    /**
     * Vai salvar o lancamento de despesa do evento
     *
     * @param lancamentoDespesa
     * @return
     * @throws java.lang.Exception
     */
    public Lancamento saveLancamentoServicoPrestado(Lancamento lancamentoDespesa) throws Exception {

        validateObjectNull(lancamentoDespesa);

        custoEventoService.validateObjectAndIdNull(lancamentoDespesa.getCustoEvento());

        lancamentoDespesa.calcularParcelas();

        lancamentoDespesa = save(lancamentoDespesa);

        CustoEvento custoEvento = custoEventoService.findEntityById(lancamentoDespesa.getCustoEvento().getId());
        custoEvento.adicionarLancamento(lancamentoDespesa);
        custoEventoService.atualizarValoresCusto(custoEvento);
        custoEventoService.save(custoEvento);

        return lancamentoDespesa;
    }

    public Lancamento saveLancamentoAtividade(Lancamento entity, Evento evento) throws Exception {

        validateObjectNull(entity);

        eventoService.validateObjectAndIdNull(evento);
        
        entity.calcularParcelas();

        CustoEvento custoEvento = custoEventoService.findCustoEventoByIdEvento(evento.getId());
        entity.setCustoEvento(custoEvento);

        custoEvento.adicionarLancamento(entity);

        save(entity);
        
        custoEventoService.atualizarValoresCusto(custoEvento);
        custoEventoService.save(custoEvento);
        
        return entity;
    }

    public Lancamento criarSalvarLancamentoReceita(Lancamento lancamentoDespesa) throws Exception {

        validateObjectNull(lancamentoDespesa);

        Lancamento lancamentoEntradaEmpresa = criarLancamentoEntrada(lancamentoDespesa);

        parcelaVinculadaService.vincularParcelas(lancamentoDespesa.getParcelas(), lancamentoEntradaEmpresa.getParcelas());

        return save(lancamentoEntradaEmpresa);
    }

    public Lancamento criarLancamentoEntrada(Lancamento entity) throws Exception {

        validateObjectNull(entity);

        Lancamento lancamento = new Lancamento(TipoLancamento.RECEITA);

        lancamento.setDataVencimento(entity.getDataVencimento());
        lancamento.setEnvolvidoDestino(entity.getEnvolvidoOrigem());
        lancamento.setEnvolvidoOrigem(entity.getEnvolvidoDestino());
        lancamento.setNumeroParcelas(entity.getNumeroParcelas());
        lancamento.setParcelas(parcelaService.criarParcelasDeParcelas(entity.getParcelas(), lancamento));
        lancamento.setServico(entity.getServico());
        lancamento.setValorBase(entity.getValorBase());
        lancamento.setValorEstimado(entity.getValorEstimado());
        lancamento.setValorTotalPago(entity.getValorTotalPago());
        lancamento.calcularParcelas();

        return lancamento;

    }

    @Override
    public void validateId(Long idEntity) {

        if (idEntity == null) {
            throw new GenericException("Id nulo ", ErrorCode.BAD_REQUEST.getCode());
        }

        if (idEntity <= 0) {
            throw new GenericException("Id não pode ser menor ou igual a zero ", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    @Override
    public void validateObjectNull(Lancamento entity) {

        if (entity == null) {
            throw new GenericException(" Lancamento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    @Override
    public void validateObjectAndIdNull(Lancamento entity) {

        validateObjectNull(entity);

        validateId(entity.getId());

    }

    public void removerLancamentoPlanilhaCustoEvento(Lancamento lancamento) throws GenericException, Exception {

        validateObjectAndIdNull(lancamento);

        lancamento = this.findEntityById(lancamento.getId());

        if (!lancamento.permiteRemoverLancamento()) {

            motivoNaoRemoverLancamento(lancamento);
        }

        this.delete(lancamento);
    }

    public void motivoNaoRemoverLancamento(Lancamento lancamento) throws Exception {

        validateObjectNull(lancamento);

        if (lancamento.possuiFornecedorContratado()) {
            throw new GenericException("Lançamento já possui um Fornecedor Contratado", ErrorCode.BAD_REQUEST.getCode());
        }

        if (lancamento.possuiParcelaPaga()) {
            throw new GenericException("Lançamento já possui parcela(s) paga(s)", ErrorCode.BAD_REQUEST.getCode());
        }

        if (lancamento.possuiLancamentoGeradoServicoPrestado()) {
            throw new GenericException("Lançamento gerado a partir de um servico contratado da empresa", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    public Lancamento criarLancamentoDespesaEventoByAtividadeId(Long idAtividade) throws Exception {

        atividadeEventoService.validateId(idAtividade);

        AtividadeEvento atividade = atividadeEventoService.findEntityById(idAtividade);

        return new Lancamento(atividade);
    }

}
