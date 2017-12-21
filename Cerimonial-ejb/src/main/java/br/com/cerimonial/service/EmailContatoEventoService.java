/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ContatoEvento;
import br.com.cerimonial.entity.EmailContatoEvento;
import br.com.cerimonial.entity.ModeloEmail;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.EmailContatoEventoRepository;
import br.com.cerimonial.utils.EmailHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
public class EmailContatoEventoService extends BasicService<EmailContatoEvento> {

    private EmailContatoEventoRepository repository;

    @EJB
    protected ModeloEmailService modeloEmailService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EmailContatoEventoRepository(em);
    }

    /**
     *
     * @param id
     * @return
     * @throws java.lang.Exception
     */
    @Override
    public EmailContatoEvento getEntity(Long id) throws Exception {
        EmailContatoEvento entity = repository.getEntity(id);

        return entity;
    }

    @Override
    public EmailContatoEvento save(EmailContatoEvento entity) throws Exception {

        isValid(entity);

        if (entity.getId() == null) {

            enviarEmail(entity);

            entity.setDataCadastro(new Date());

            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    public void delete(EmailContatoEvento contato) throws Exception {

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

    public List<EmailContatoEvento> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean isValid(EmailContatoEvento entity) {
        if (entity == null) {
            throw new GenericException("Objeto nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        if (StringUtils.isBlank(entity.getDestinatarios())) {
            throw new GenericException("Destinatários do email vazio.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (StringUtils.isBlank(entity.getCorpoEmail())) {
            throw new GenericException("Corpo do email vazio.", ErrorCode.BAD_REQUEST.getCode());
        }

        return true;
    }

    public EmailContatoEvento carregarDadosModeloEmail(EmailContatoEvento entity, ModeloEmail modeloEmail) throws Exception {

        if (entity != null) {
            entity.setArquivo(null);
            entity.setCorpoEmail(null);
            entity.setTituloEmail(null);
        }

        if (modeloEmail == null) {
            throw new GenericException("Modelo de Email nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        // carregar em lazy os anexoss
        modeloEmail = modeloEmailService.getEntity(modeloEmail.getId());

        if (entity != null) {

            if (StringUtils.isBlank(entity.getDestinatarios())) {
                entity.setDestinatarios(entity.getContatoEvento().getEmailContato());
            }

            entity.setModeloEmail(modeloEmail);
            entity.setCorpoEmail(modeloEmail.getConteudo());
            entity.setTituloEmail(modeloEmail.getTitulo());
            entity.setArquivo(new Arquivo(modeloEmail.getArquivo().getNome(), modeloEmail.getArquivo().getExtensao(), modeloEmail.getArquivo().getConteudo()));
        }

        return entity;

    }

    private void enviarEmail(EmailContatoEvento entity) throws Exception {

        isValid(entity);

        EmailHelper emailUtil = new EmailHelper();

        String[] destinatarios = entity.getDestinatarios().split(";");

        //anexar dados emmail
        HashMap<String, Object> anexos = new HashMap<>();
        if (entity.getAnexos() != null) {
            entity.getAnexos().stream().forEach((file) -> {
                anexos.put(file.getNome(), file);
            });
        }

        emailUtil.enviarEmail(destinatarios, entity.getTituloEmail(), entity.getCorpoEmail(), anexos);

    }

}
