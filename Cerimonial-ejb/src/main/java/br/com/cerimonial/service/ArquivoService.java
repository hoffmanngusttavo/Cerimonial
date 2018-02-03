/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ModeloEmail;
import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.repository.ArquivoRepository;
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
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ArquivoService extends BasicService<Arquivo> {

    private ArquivoRepository repository;

    @EJB
    private ModeloPropostaService modeloPropostaService;

    @EJB
    private ModeloEmailService modeloEmailService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ArquivoRepository(em);
    }

    @Override
    public Arquivo findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Arquivo save(Arquivo entity) throws Exception {

        validateObjectNull(Arquivo.class, entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }

    }

    public void delete(Arquivo arquivo) throws Exception {

        validateObjectAndIdNull(Arquivo.class, arquivo);

        repository.delete(arquivo.getId());
    }

    public List<Arquivo> getArquivosByModeloProposta(ModeloProposta modeloProposta) {

        modeloPropostaService.validateObjectAndIdNull(ModeloProposta.class, modeloProposta);

        try {
           
            return repository.getArquivosByModeloProposta(modeloProposta);
           
        } catch (Exception ex) {
            Logger.getLogger(ArquivoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Arquivo> getArquivosByModeloEmail(ModeloEmail modeloEmail) {

        modeloEmailService.validateObjectAndIdNull(ModeloEmail.class, modeloEmail);

        try {

            return repository.getArquivosByModeloProposta(modeloEmail);
            
        } catch (Exception ex) {
            Logger.getLogger(ArquivoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
