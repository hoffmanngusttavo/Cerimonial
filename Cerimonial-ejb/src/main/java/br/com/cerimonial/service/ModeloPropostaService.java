/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.repository.ModeloPropostaRepository;
import br.com.cerimonial.repository.exceptions.DAOException;
import br.com.cerimonial.repository.exceptions.ErrorCode;
import br.com.cerimonial.utils.CerimonialUtils;
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

/**
 *
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ModeloPropostaService extends BasicService<ModeloProposta> {

    private ModeloPropostaRepository repository;

    @EJB
    private ArquivoService arquivoService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ModeloPropostaRepository(em);
    }

    @Override
    public ModeloProposta getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public ModeloProposta save(ModeloProposta entity) throws Exception {

        isValid(entity);

        //salvar arquivo
        if (entity.getArquivo() != null) {
            //remover os arquivos antigos
            removerArquivosAntigosProposta(entity);
            arquivoService.save(entity.getArquivo());
        }

        //salvar ModeloProposta
        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

    private void removerArquivosAntigosProposta(ModeloProposta entity) {

        if (entity.getArquivo().getId() == null && entity.getId() != null) {
            List<Arquivo> arquivosAntigos = arquivoService.getArquivosByModeloProposta(entity);
            if (CerimonialUtils.isListNotBlank(arquivosAntigos)) {
                arquivosAntigos.stream().forEach((arquivosAntigo) -> {
                    try {
                        arquivoService.delete(arquivosAntigo);
                    } catch (Exception ex) {
                        Logger.getLogger(ModeloPropostaService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        }
    }

    public List<ModeloProposta> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ModeloPropostaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(ModeloProposta categoria) throws Exception {

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

    public List<ModeloProposta> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean isValid(ModeloProposta entity) {
        if (entity == null) {
            throw new DAOException("Modelo nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

}
