/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Cidade;
import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.repository.CidadeRepository;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.exceptions.ErrorCode;
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
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CidadeService extends BasicService<Cidade> {

    private CidadeRepository repository;

    @EJB
    private EstadoService estadoService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new CidadeRepository(em);
    }

    @Override
    public Cidade findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    public List<Cidade> findAll() throws Exception {
        return repository.findAll();
    }

    public Cidade findByNomeEstado(String city, String sigla) {

        if (StringUtils.isBlank(city)) {
            throw new GenericException("Nome da cidade não pode ser nula", ErrorCode.BAD_REQUEST.getCode());
        }

        if (StringUtils.isBlank(sigla)) {
            throw new GenericException("Sigla do estado não pode ser nula", ErrorCode.BAD_REQUEST.getCode());
        }

        try {
            return repository.findByNomeEstado(city, sigla);
        } catch (Exception ex) {
            Logger.getLogger(CidadeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Cidade> findByEstado(Estado estado) {

       estadoService.validateObjectAndId(estado);

        try {
            return repository.findByIdEstado(estado.getId());
        } catch (Exception ex) {
            Logger.getLogger(CidadeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    /**
     *
     * @param entity
     * @return
     */
    @Override
    public Cidade save(Cidade entity) {

        validateObject(entity);

        if (entity.getId() == null) {

            return repository.create(entity);

        } else {

            return repository.edit(entity);
        }
    }

   

}
