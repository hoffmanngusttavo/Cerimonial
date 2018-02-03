/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ContratoEvento;
import br.com.cerimonial.entity.Evento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.ModeloContrato;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.repository.ContratoEventoRepository;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.ArrayList;
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

        super.validateId(idEvento);

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
        
        pessoaService.validateObjectAndIdNull(Pessoa.class, contratante);

        List<ContratoEvento> contratos = repository.getContratoByEventoContratante(idEvento, contratante);

        if (CollectionUtils.isNotBlank(contratos)) {
            return contratos.get(0);
        }

        return null;
    }

    @Override
    public ContratoEvento save(ContratoEvento entity) throws Exception {

        validateObjectNull(ContratoEvento.class, entity);
        
        eventoService.validateObjectNull(Evento.class, entity.getEvento());
        
        modeloContratoService.validateObjectNull(ModeloContrato.class, entity.getModeloContrato());

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    /**
     * Vai liberar contrato para o contratante e vai criar um alerta para ele visualizar ao logar
     * @param entity
     * @throws java.lang.Exception
     */
    public void liberarContrato(ContratoEvento entity) throws Exception {

        validateObjectAndIdNull(ContratoEvento.class, entity);

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

        validateObjectAndIdNull(ContratoEvento.class, contato);

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

        validateObjectNull(ContratoEvento.class, entity);
        
        eventoService.validateObjectAndIdNull(Evento.class, entity.getEvento());
        
        modeloContratoService.validateObjectNull(ModeloContrato.class, entity.getModeloContrato());

        String conteudo = entity.getModeloContrato().getConteudo();
        
        if (StringUtils.isNotBlank(conteudo)) {
            
            Evento evento = eventoService.findEntityById(entity.getEvento().getId());

            conteudo = conteudo.replaceAll("#dadosContratante#", substituirDadosContratante(evento));
            conteudo = conteudo.replaceAll("#dadosEvento#", substituirDadosEvento(evento));
//            conteudo = conteudo.replaceAll("#formaPagamento#", substituirDadosPagamentoEvento(entity.getEvento()));
        }

        entity.setConteudo(conteudo);

    }

    public String substituirDadosContratante(Evento evento) {

        StringBuilder sb = new StringBuilder("");

        if (evento != null  && CollectionUtils.isNotBlank(evento.getContratantes())) {

            for (EventoPessoa eventoPessoa : evento.getContratantes()) {

                if (eventoPessoa.isContratante()) {

                    sb.append(eventoPessoa.toStringPessoa());

                }
            }
        }

        return sb.toString();
    }

    public String substituirDadosEvento(Evento evento) {
        StringBuilder sb = new StringBuilder("");

        if (evento != null) {

            sb.append(evento.toStringDadosCompleto());

        }

        return sb.toString();
    }

    public String substituirDadosPagamentoEvento(ContratoEvento contratoEvento) {
        StringBuilder sb = new StringBuilder("");

        if (contratoEvento != null) {

        }

        return sb.toString();
    }

}
