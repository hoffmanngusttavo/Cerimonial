/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Arquivo;
import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.entity.ServicoPrestadoEvento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.ServicoPrestadoEventoRepository;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.List;
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
public class ServicoPrestadoEventoService extends BasicService<ServicoPrestadoEvento> {

    private ServicoPrestadoEventoRepository repository;
    
    @EJB
    protected ArquivoService arquivoService;
    
    @EJB
    protected PessoaService pessoaService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ServicoPrestadoEventoRepository(em);
    }

    @Override
    public ServicoPrestadoEvento findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public ServicoPrestadoEvento save(ServicoPrestadoEvento entity) throws Exception {

        validateObjectNull(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }

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
    public void validateObjectNull(ServicoPrestadoEvento entity) {

        if (entity == null) {
            throw new GenericException(" Orcamento Evento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

    }

    @Override
    public void validateObjectAndIdNull(ServicoPrestadoEvento entity) {

        validateObjectNull(entity);

        validateId(entity.getId());

    }

    public ServicoPrestadoEvento findEntityByPreEventoId(Long idPreEvento) {
        
        validateId(idPreEvento);
        
        return repository.findServicoPrestadoByPreEventoId(idPreEvento);
        
    }
    
    public ServicoPrestadoEvento findEntityByEventoId(Long idEvento) {
        
        validateId(idEvento);
        
        return repository.findEntityByEventoId(idEvento);
        
    }

    
    public ServicoPrestadoEvento findEntityByIdEventoAndContratante(Long idEvento, Pessoa contratante) throws Exception {

        validateId(idEvento);

        pessoaService.validateObjectAndIdNull(contratante);

        ServicoPrestadoEvento orcamento = repository.findServicoPrestadoByEventoIdAndContratante(idEvento, contratante);

//        smartLazy(orcamento, Arrays.asList("evento", "anexos", "modeloProposta"));

        return orcamento;

    }
    
    
    
     public ServicoPrestadoEvento carregarPropostaModelo(ServicoPrestadoEvento servicoPrestado, ModeloProposta modelo) throws Exception {

        if (servicoPrestado != null && modelo != null) {
            
            servicoPrestado.setModeloProposta(modelo);
            servicoPrestado.setProposta(modelo.getConteudo());
            servicoPrestado.setValorProposta(modelo.getValorProposta() != null ? modelo.getValorProposta().doubleValue() : 0);
            List<Arquivo> arquivosByModeloProposta = arquivoService.getArquivosByModeloProposta(modelo);
            servicoPrestado.setArquivo(null);
            if (CollectionUtils.isNotBlank(arquivosByModeloProposta)) {
                arquivosByModeloProposta.stream().forEach((Arquivo file) -> {
                    servicoPrestado.adicionarAnexo(file.clonar(file));
                });
            }
        }
        return servicoPrestado;
    }
    
}
