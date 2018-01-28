/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ContatoEnvolvido;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.ContatoEnvolvidoRepository;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.List;
import javax.annotation.PostConstruct;
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
public class ContatoEnvolvidoService extends BasicService<ContatoEnvolvido> {

    private ContatoEnvolvidoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ContatoEnvolvidoRepository(em);
    }

    @Override
    public ContatoEnvolvido findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public ContatoEnvolvido save(ContatoEnvolvido entity) throws Exception {

        validateObject(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }

    }

   

    /**
     * Método responsável por remover os contatos.
     * Só pode remover contato que existe no banco de dados.
     * @param contatosRemover
     */
    public void removerContatos(List<ContatoEnvolvido> contatosRemover) {
        
        if(CollectionUtils.isNotBlank(contatosRemover)){
            
            for (ContatoEnvolvido contato : contatosRemover) {
                
                if (contato.getId() == null) {
                    throw new GenericException("Não pode remover um contato sem id", ErrorCode.BAD_REQUEST.getCode());
                }
                
                repository.delete(contato.getId());
                
            }
            
        }
        
    }

}
