/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ContratoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.Lancamento;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.entity.Parcela;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.ContratoEventoRepository;
import br.com.cerimonial.utils.CollectionUtils;
import br.com.cerimonial.utils.ValorPorExtensoUtils;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContratoEventoService extends BasicService<ContratoEvento> {

    private ContratoEventoRepository repository;

    @EJB
    protected EventoService eventoService;

    @EJB
    protected ModeloContratoService modeloContratoService;

    @EJB
    protected AlertaService alertaService;

    @EJB
    protected PessoaService pessoaService;

    @EJB
    protected LancamentoService lancamentoService;
    @EJB
    protected OrcamentoEventoService orcamentoEventoService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ContratoEventoRepository(em);
    }

    /**
     *
     * @param id
     * @return
     * @throws java.lang.Exception
     */
    @Override
    public ContratoEvento findEntityById(Long id) throws Exception {

        return repository.getEntity(id);

    }

    /**
     * Método vai buscar todos os contratos de um evento, que por sinal só deve
     * trazer 1 Traz em lista devido ao mapeamento lazy
     *
     * @param idEvento do Evento
     * @return
     * @throws java.lang.Exception
     */
    public ContratoEvento getContratoByEventoId(Long idEvento) throws Exception {

        eventoService.validateId(idEvento);

        List<ContratoEvento> contratos = repository.getContratosByEvento(idEvento);

        if (CollectionUtils.isNotBlank(contratos)) {
            return contratos.get(0);
        }

        return null;
    }

    /**
     * Método vai buscar todos os contratos de um evento e contratante
     * exclusivo, que por sinal só deve trazer 1 Traz em lista devido ao
     * mapeamento lazy
     *
     * @param idEvento do Evento
     * @param contratante
     * @return
     * @throws java.lang.Exception
     */
    public ContratoEvento getContratoByEventoContratante(Long idEvento, Pessoa contratante) throws Exception {

        validateId(idEvento);

        pessoaService.validateObjectAndIdNull(contratante);

        List<ContratoEvento> contratos = repository.getContratoByEventoContratante(idEvento, contratante);

        if (CollectionUtils.isNotBlank(contratos)) {
            return contratos.get(0);
        }

        return null;
    }

    @Override
    public ContratoEvento save(ContratoEvento entity) throws Exception {

        validateObjectNull(entity);

        eventoService.validateObjectNull(entity.getEvento());

        modeloContratoService.validateObjectNull(entity.getModeloContrato());

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    /**
     * Vai liberar contrato para o contratante e vai criar um alerta para ele
     * visualizar ao logar
     *
     * @param entity
     * @throws java.lang.Exception
     */
    public void liberarContrato(ContratoEvento entity) throws Exception {

        validateObjectAndIdNull(entity);

        entity = this.findEntityById(entity.getId());

        entity.setLiberadoCliente(true);

        save(entity);

        alertaService.enviarAlertaContratoLiberado(entity);

    }

    public List<ContratoEvento> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ContratoEventoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(ContratoEvento contato) throws Exception {

        validateObjectAndIdNull(contato);

        repository.delete(contato.getId());
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<ContratoEvento> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Vai preencher o conteudo do contrato do evento de acordo com o modelo
     * específico
     *
     * @param entity
     * @throws java.lang.Exception
     */
    public void carregarContratoDeModelo(ContratoEvento entity) throws Exception {

        validateObjectNull(entity);

        eventoService.validateObjectAndIdNull(entity.getEvento());

        modeloContratoService.validateObjectNull(entity.getModeloContrato());

        String conteudo = entity.getModeloContrato().getConteudo();

        if (StringUtils.isNotBlank(conteudo)) {

            Evento evento = eventoService.findEntityById(entity.getEvento().getId());

            conteudo = conteudo.replaceAll("#dadosContratante#", substituirDadosContratante(evento));

            conteudo = conteudo.replaceAll("#dadosEvento#", substituirDadosEvento(evento));

            conteudo = conteudo.replaceAll("#formaPagamento#", substituirDadosPagamentoEvento(entity.getEvento()));
        }

        entity.setConteudo(conteudo);

    }

    private String substituirDadosContratante(Evento evento) {

        StringBuilder sb = new StringBuilder("");

        try {

            if (evento != null && CollectionUtils.isNotBlank(evento.getContratantes())) {

                for (EventoPessoa eventoPessoa : evento.getContratantes()) {

                    if (eventoPessoa.isContratante()) {

                        sb.append(eventoPessoa.toStringPessoa());

                    }
                }
            }

        } catch (Exception e) {
            Logger.getLogger(e.getMessage());
        }

        return sb.toString();
    }

    private String substituirDadosEvento(Evento evento) {
        StringBuilder sb = new StringBuilder("");

        try {

            if (evento != null) {

                sb.append(evento.toStringDadosCompleto());

            }

        } catch (Exception e) {
            Logger.getLogger(e.getMessage());
        }

        return sb.toString();
    }

    private String substituirDadosPagamentoEvento(Evento evento) {

        StringBuilder sb = new StringBuilder("");

        try {

            if (evento != null && evento.getOrcamentoEvento() != null) {

                OrcamentoEvento orcamento = orcamentoEventoService.findOrcamentoByEventoId(evento.getId());

                Lancamento lancamento = lancamentoService.findLancamentoByOrcamentoId(orcamento.getId());

                if (lancamento != null && CollectionUtils.isNotBlank(lancamento.getParcelas())) {

                    sb.append("Valor: ").append(lancamento.getValorBase());

                    sb.append(" - ").append(ValorPorExtensoUtils.converteValorParaExtenso(lancamento.getValorBase())).append(" ");

                    sb.append("\n");

                    sb.append("<table>");

                    sb.append("<tr>");

                    sb.append("<td>");
                    sb.append("Data Pagamento");
                    sb.append("</td>");

                    sb.append("<td>");
                    sb.append("Valor Pagamento");
                    sb.append("</td>");

                    sb.append("<td>");
                    sb.append("Forma de Pagamento");
                    sb.append("</td>");
                    
                    sb.append("<td>");
                    sb.append("Dados Bancários");
                    sb.append("</td>");

                    sb.append("</tr>");

                    for (Parcela parcela : lancamento.getParcelas()) {

                        sb.append("<tr>");
                        sb.append(parcela.toStringDadosCobranca());
                        sb.append("</tr>");
                        
                    }

                    sb.append("</table>");

                }

            }

        } catch (Exception e) {
            Logger.getLogger(e.getMessage());
        }

        return sb.toString();
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
    public void validateObjectNull(ContratoEvento entity) {

        if (entity == null) {
            throw new GenericException(" Contrato Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    @Override
    public void validateObjectAndIdNull(ContratoEvento entity) {

        validateObjectNull(entity);

        validateId(entity.getId());

    }

}
