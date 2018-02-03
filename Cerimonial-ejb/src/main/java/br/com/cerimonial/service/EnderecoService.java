/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Cidade;
import br.com.cerimonial.entity.Endereco;
import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.EnderecoRepository;
import br.com.cerimonial.utils.CerimonialUtils;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnderecoService extends BasicService<Endereco> {

    private EnderecoRepository repository;
    @EJB
    private CidadeService cidadeService;
    @EJB
    private EstadoService estadoService;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EnderecoRepository(em);
    }

     @Override
    public Endereco findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public Endereco save(Endereco entity) throws Exception {

        validateObjectNull(entity);

        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }
    
    
    public Endereco buscaCep(Endereco endereco) throws Exception {
        
        validateObjectNull(endereco);

        String cep = endereco.getCep();
        String strCep = CerimonialUtils.removerNaoDigitos(cep);
        String jsonString = CerimonialUtils.callURL("http://viacep.com.br/ws/" + strCep + "/json/");
        String jsonStringFormat = new String(jsonString.getBytes(), "UTF-8");

        JSONArray jl = new JSONArray("[" + jsonStringFormat + "]");
        JSONObject json = jl.getJSONObject(0);

        if (json != null && !json.isNull("erro")) {
            throw new Exception("Cep Inválido");
        }

        String uf = (String) json.get("uf");

        Estado estado = estadoService.findBySigla(uf);
        if (estado == null) {
            estado = new Estado();
            estado.setNome(uf);
            estado.setSigla(uf);
            estado = estadoService.save(estado);
        }

        String city = (String) json.get("localidade");
        Cidade cidade = cidadeService.findByNomeEstado(city, estado.getSigla());

        if (cidade == null) {
            cidade = new Cidade();
            cidade.setNome(city);
            cidade.setEstado(estado);
            cidade = cidadeService.save(cidade);
        }

        endereco.setLogradouro((String) json.get("logradouro"));
        endereco.setBairro((String) json.get("bairro"));
        endereco.setCidade(cidade);
        endereco.setEstado(estado);

        return endereco;
    }

    public Endereco buscaCep(String cep) throws Exception {
        Endereco endereco = new Endereco();
        endereco.setCep(cep);
        return buscaCep(endereco);
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
    public void validateObjectNull(Endereco entity) {
        
         if (entity == null) {
            throw new GenericException(" Endereco nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(Endereco entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }

   
}
