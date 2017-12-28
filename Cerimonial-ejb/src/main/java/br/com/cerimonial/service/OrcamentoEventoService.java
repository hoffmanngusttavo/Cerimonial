/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.entity.OrcamentoEvento;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.Usuario;
import br.com.cerimonial.service.report.Relatorio;
import br.com.cerimonial.repository.OrcamentoEventoRepository;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.service.utils.EmpresaCache;
import br.com.cerimonial.service.utils.InvoiceUtils;
import br.com.cerimonial.utils.CerimonialUtils;
import br.com.cerimonial.utils.EmailHelper;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrcamentoEventoService extends BasicService<OrcamentoEvento> {

    private OrcamentoEventoRepository repository;

    @EJB
    protected ConfiguracaoEmailService configuracaoEmailService;
    @EJB
    protected ArquivoService arquivoService;
    @EJB
    protected PessoaService pessoaService;
    @EJB
    protected UsuarioService usuarioService;
    @EJB
    protected EventoService eventoService;
    @EJB
    protected EventoPessoaService eventoPessoaService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new OrcamentoEventoRepository(em);
    }

    @Override
    public OrcamentoEvento getEntity(Long id) throws Exception {
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
    public OrcamentoEvento getOrcamentoByEvento(Long idEvento) throws Exception {

        if (idEvento == null) {
            throw new GenericException("Id nulo do evento ", ErrorCode.BAD_REQUEST.getCode());
        }

        if (idEvento < 0) {
            throw new GenericException("Id menor que zero ", ErrorCode.BAD_REQUEST.getCode());
        }

        OrcamentoEvento orcamento = repository.getOrcamentoByEvento(idEvento);
        
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
    
    
    /**
     * Método vai buscar o orçamento de um evento de um contratante;
     *
     * @param idEvento do Evento
     * @param contratante
     * @return
     * @throws java.lang.Exception
     */
    public OrcamentoEvento getOrcamentoContratante(Long idEvento, Pessoa contratante) throws Exception {

        if (idEvento == null) {
            throw new GenericException("Id nulo do evento ", ErrorCode.BAD_REQUEST.getCode());
        }

        if (idEvento < 0) {
            throw new GenericException("Id menor que zero ", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (contratante == null) {
            throw new GenericException("Contratante nulo ", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (contratante.getId() == null) {
            throw new GenericException("Id Contratante nulo ", ErrorCode.BAD_REQUEST.getCode());
        }

        OrcamentoEvento orcamento = repository.getOrcamentoContratante(idEvento, contratante);
        
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

    @Override
    public OrcamentoEvento save(OrcamentoEvento entity) throws Exception {

        isValid(entity);

        //salvar arquivo
        if (entity.getArquivo() != null) {
            arquivoService.save(entity.getArquivo());
        }

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }

    }

    public List<OrcamentoEvento> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(OrcamentoEventoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(OrcamentoEvento proposta) throws Exception {

        isValid(proposta);

        if (proposta.isPropostaAceita()) {
            throw new GenericException("Não pode remover um orçamento aprovado", ErrorCode.BAD_REQUEST.getCode());
        } else {
            repository.delete(proposta.getId());
        }
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<OrcamentoEvento> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Vai carregar os dados para um orÃ§amento a partir de um modelo
     * selecionado
     *
     * @param orcamento
     * @param modelo
     * @return
     * @throws java.lang.Exception
     */
    public OrcamentoEvento carregarPropostaModelo(OrcamentoEvento orcamento, ModeloProposta modelo) throws Exception {

        if (orcamento != null && modelo != null) {
            orcamento.setModeloProposta(modelo);
            orcamento.setProposta(modelo.getConteudo());
            orcamento.setValorProposta(modelo.getValorProposta() != null ? modelo.getValorProposta().doubleValue() : 0);
            List<Arquivo> arquivosByModeloProposta = arquivoService.getArquivosByModeloProposta(modelo);
            orcamento.setArquivo(null);
            if (CerimonialUtils.isListNotBlank(arquivosByModeloProposta)) {
                arquivosByModeloProposta.stream().forEach((Arquivo file) -> {
                    orcamento.adicionarAnexo(file.clonar(file));
                });
            }
        }
        return orcamento;
    }

    /**
     *
     * @param id do ContatoEvento
     * @return Uma lista de orçamentos/propostas de um contato
     */
    public List<OrcamentoEvento> findAllByContatoId(Long id) {
        if (id != null) {
            try {
                return repository.findAllByContatoId(id);
            } catch (Exception ex) {
                Logger.getLogger(OrcamentoEventoService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new ArrayList<>();
    }

    /**
     * Enviar por email a proposta com anexo se possuir
     *
     * @param proposta
     * @throws java.lang.Exception
     */
    public void enviarOrcamentoEmail(OrcamentoEvento proposta) throws Exception {

        isValid(proposta);

        //carregar invoice padrao
        String body = InvoiceUtils.readFileToString("proposta-orcamento.html");
        body = body.replaceAll("@@@NOME_CONTATO@@@", proposta.getContatoEvento().getNomeContato());
        body = body.replaceAll("@@@NOME_PLANO@@@", proposta.getModeloProposta().getTitulo());
        body = body.replaceAll("@@@NOME_EMPRESA@@@", EmpresaCache.getEmpresa().getNome());
        body = body.replaceAll("@@@PROPOSTA_VALOR@@@", new DecimalFormat("#0.00").format(proposta.getValorProposta()));

        //anexar dados emmail
        HashMap<String, Object> anexos = new HashMap<>();
        if (proposta.getAnexos() != null) {
            proposta.getAnexos().stream().forEach((file) -> {
                anexos.put(file.getId().toString(), file);
            });
        }

        //enviar email
        EmailHelper emailHelper = new EmailHelper();
        emailHelper.enviarEmail(proposta.getContatoEvento().getEmailContato(), "Proposta/Orçamento", body, anexos);

        //atualizar no banco que foi enviado o email com sucesso
        proposta.setPropostaEnviada(true);
        repository.edit(proposta);
    }

    @Deprecated
    public byte[] getPDFOrcamento(OrcamentoEvento proposta) throws JRException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("idOrcamento", proposta.getId());
        InputStream inputStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/jasper/orcamento.jrxml");
        return Relatorio.compileReport(parameters, inputStream);
    }

    /**
     * Aceitar/Aprovar um orçamento que não está aprovado ainda. É verificado se
     * já existe um orçamento aprovado
     *
     * @param orcamento
     * @throws java.lang.Exception
     */
    public void aceitarProposta(OrcamentoEvento orcamento) throws Exception {
        if (orcamento != null && orcamento.getId() != null && !orcamento.isPropostaAceita()) {
            List<OrcamentoEvento> orcamentos = findAllByContatoId(orcamento.getContatoEvento().getId());
            if (CerimonialUtils.isListNotBlank(orcamentos)) {
                for (OrcamentoEvento proposta : orcamentos) {
                    if (proposta.isPropostaAceita()) {
                        throw new GenericException("Já existe um orçamento aprovado", ErrorCode.BAD_REQUEST.getCode());
                    }
                }
                orcamento.setPropostaAceita(true);
                repository.edit(orcamento);
            }
        }
    }

    /**
     * Cancelar um orçamento que está aprovado.
     *
     * @param orcamento
     * @throws java.lang.Exception
     */
    public void cancelarProposta(OrcamentoEvento orcamento) throws Exception {
        if (orcamento != null && orcamento.getId() != null && orcamento.isPropostaAceita()) {
            orcamento.setPropostaAceita(false);
            repository.edit(orcamento);
        }
    }

    /**
     * Vai criar um evento a partir de uma proposta aceita.
     * Vai Criar um cliente.
     * Vai criar o evento do cliente 
     *
     * @param entity
     * @throws java.lang.Exception
     */
    public void criarEvento(OrcamentoEvento entity) throws Exception {

        isValid(entity);

        if (!entity.isPropostaAceita()) {
            throw new GenericException("Proposta não aceita", ErrorCode.BAD_REQUEST.getCode());
        }

        Pessoa cliente = pessoaService.criarClienteFromContato(entity);
        pessoaService.saveCliente(cliente);

        Evento evento = eventoService.criarEventoFromOrcamento(entity);
        eventoService.save(evento);
        
        // atrelar os dados do contratante de acordo com o tipo do evento
        EventoPessoa eventoPessoa = eventoPessoaService.criarEventoPessoa(evento, cliente);
        eventoPessoaService.save(eventoPessoa);

    }

    @Override
    public boolean isValid(OrcamentoEvento entity) {
        if (entity == null) {
            throw new GenericException("Orçamento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (entity.getContatoEvento() == null) {
            throw new GenericException("Orçamento possui um contato nulo", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

}
