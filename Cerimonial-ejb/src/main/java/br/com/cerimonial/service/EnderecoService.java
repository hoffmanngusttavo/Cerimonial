/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Endereco;
import br.com.cerimonial.repository.EnderecoRepository;
import br.com.cerimonial.repository.UsuarioRepository;
import br.com.cerimonial.utils.CerimonialUtils;
import javax.annotation.PostConstruct;
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

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EnderecoRepository(em);
    }

    

    public Endereco buscaCep(Endereco endereco) throws Exception {

        String cep = endereco.getCep();
        String strCep = cep.replaceAll("\\D", "");
        String jsonString = CerimonialUtils.callURL("http://viacep.com.br/ws/" + strCep + "/json/");
        String jsonStringFormat = new String(jsonString.getBytes(), "UTF-8");

        JSONArray jl = new JSONArray("[" + jsonStringFormat + "]");
        JSONObject json = jl.getJSONObject(0);

        if (json != null && !json.isNull("erro")) {
            throw new Exception("Cep Inválido");
        }

        String city = (String) json.get("localidade");
        String uf = (String) json.get("uf");

        endereco.setLogradouro((String) json.get("logradouro"));
        endereco.setBairro((String) json.get("bairro"));
        endereco.setCidade(null);

        return endereco;
    }

    public Endereco buscaCep(String cep) throws Exception {
        Endereco endereco = new Endereco();
        endereco.setCep(cep);
        return buscaCep(endereco);
    }
    
    @Override
    public Endereco getEntity(Long id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Endereco save(Endereco entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}