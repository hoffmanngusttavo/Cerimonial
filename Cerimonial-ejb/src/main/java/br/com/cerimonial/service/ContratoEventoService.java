/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ContratoEvento;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.ContratoEventoRepository;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
    public ContratoEvento getEntity(Long id) throws Exception {
        ContratoEvento entity = repository.getEntity(id);

        return entity;
    }

    /**
     * Método vai buscar todos os contratos de um evento, que por sinal só deve
     * trazer 1 Traz em lista devido ao mapeamento lazy
     *
     * @param idEvento do Evento
     * @return
     * @throws java.lang.Exception
     */
    public ContratoEvento getContratoByEvento(Long idEvento) throws Exception {

        if (idEvento == null) {
            throw new GenericException("Id nulo do evento ", ErrorCode.BAD_REQUEST.getCode());
        }

        if (idEvento < 0) {
            throw new GenericException("Id menor que zero ", ErrorCode.BAD_REQUEST.getCode());
        }

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

        if (idEvento == null) {
            throw new GenericException("Id nulo do evento ", ErrorCode.BAD_REQUEST.getCode());
        }

        if (idEvento < 0) {
            throw new GenericException("Id menor que zero ", ErrorCode.BAD_REQUEST.getCode());
        }

        if (contratante == null || contratante.getId() == null) {
            throw new GenericException("Contratante nulo ", ErrorCode.BAD_REQUEST.getCode());
        }

        List<ContratoEvento> contratos = repository.getContratoByEventoContratante(idEvento, contratante);

        if (CollectionUtils.isNotBlank(contratos)) {
            return contratos.get(0);
        }

        return null;
    }

    @Override
    public ContratoEvento save(ContratoEvento entity) throws Exception {

        isValid(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public void liberarContrato(ContratoEvento entity) throws Exception {

        isValid(entity);

        entity.setLiberadoCliente(true);

        save(entity);

        //criar notificacao de alerta para os contratantes do evento
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

        isValid(contato);

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

    @Override
    public boolean isValid(ContratoEvento entity) {
        if (entity == null) {
            throw new GenericException("Arquivo nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (entity.getEvento() == null) {
            throw new GenericException("Evento do Contrato nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (entity.getModeloContrato() == null) {
            throw new GenericException("Modelo de Contrato nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

    /**
     * Vai preencher o conteudo do contrato do evento de acordo com o modelo
     * específico TODO[substituir variaves de acordo com hashTags]
     *
     * @param entity
     */
    public void carregarContratoDeModelo(ContratoEvento entity) {

        isValid(entity);

        String conteudo = entity.getModeloContrato().getConteudo();
        
        if (StringUtils.isNotBlank(conteudo)) {

            conteudo = conteudo.replaceAll("#dadosContratante#", substituirDadosContratante(entity));
            conteudo = conteudo.replaceAll("#dadosEvento#", substituirDadosEvento(entity));
            conteudo = conteudo.replaceAll("#formaPagamento#", substituirDadosPagamentoEvento(entity));
        }

        entity.setConteudo(conteudo);

    }

    public String substituirDadosContratante(ContratoEvento contratoEvento) {

        StringBuilder sb = new StringBuilder("");

        if (contratoEvento != null && contratoEvento.getEvento() != null
                && CollectionUtils.isNotBlank(contratoEvento.getEvento().getContratantes())) {

            for (EventoPessoa eventoPessoa : contratoEvento.getEvento().getContratantes()) {

                if (eventoPessoa.isContratante()) {

                    sb.append(eventoPessoa.toStringPessoa());

                }
            }
        }

        return sb.toString();
    }

    public String substituirDadosEvento(ContratoEvento contratoEvento) {
        StringBuilder sb = new StringBuilder("");

        if (contratoEvento != null && contratoEvento.getEvento() != null) {

            sb.append(contratoEvento.getEvento().toStringDadosCompleto());

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
