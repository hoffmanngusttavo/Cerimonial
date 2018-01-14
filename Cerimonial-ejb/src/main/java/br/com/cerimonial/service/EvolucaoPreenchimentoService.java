/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Endereco;
import br.com.cerimonial.entity.EventoPessoa;
import br.com.cerimonial.entity.EvolucaoPreenchimento;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.EvolucaoPreenchimentoRepository;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.HashMap;
import java.util.List;
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
public class EvolucaoPreenchimentoService extends BasicService<EvolucaoPreenchimento> {

    private EvolucaoPreenchimentoRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EvolucaoPreenchimentoRepository(em);

    }

    @Override
    public EvolucaoPreenchimento getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    @Override
    public EvolucaoPreenchimento save(EvolucaoPreenchimento entity) throws Exception {

        isValid(entity);

        return repository.create(entity);
    }

    public List<EvolucaoPreenchimento> findRangeListagem(HashMap<String, Object> params, int max, int offset, String sortField, String sortAscDesc) {
        try {
            if (params == null) {
                params = new HashMap<>();
            }
            return repository.findRangeListagem(params, max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean isValid(EvolucaoPreenchimento entity) {
        if (entity == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        return true;
    }

    public void validarPorcentagemPreenchimento(EventoPessoa eventoPessoa) {

        if (eventoPessoa == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (eventoPessoa.getPessoa() == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        EvolucaoPreenchimento evolucaoPreenchimento = eventoPessoa.getEvolucaoPreenchimento();

        if (evolucaoPreenchimento == null) {
            evolucaoPreenchimento = new EvolucaoPreenchimento(eventoPessoa);
        }
        
        if(eventoPessoa.isContratante()){
            
            validarPorcentagemPreenchimentoContratante(eventoPessoa);
            
        }else{
        
            //TODO: VALIDAR DE ACORDO COM O TIPO DO ENVOLVIDO
        }
            
    }

    /**
     * Vai contabilizar a porcentagem de preenchimento do contratante.
     *
     * @param eventoPessoa
     */
    public void validarPorcentagemPreenchimentoContratante(EventoPessoa eventoPessoa) {

        if (eventoPessoa == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (eventoPessoa.getPessoa() == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        EvolucaoPreenchimento evolucaoPreenchimento = eventoPessoa.getEvolucaoPreenchimento();

        if (evolucaoPreenchimento == null) {
            evolucaoPreenchimento = new EvolucaoPreenchimento(eventoPessoa);
        }

        double totalCamposValidar = 11;
        double totalCamposValidos = 0;
        StringBuilder sb = new StringBuilder();
        Pessoa pessoa = eventoPessoa.getPessoa();

        if (StringUtils.isNotBlank(pessoa.getNome())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Nome, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getCpf())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Cpf, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getRg())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Rg, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getEmail())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Email, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getTelefoneCelular()) || StringUtils.isNotBlank(pessoa.getTelefoneResidencial())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Telefone, \n");
        }

        if (pessoa.getDataNascimento() != null) {
            totalCamposValidos += 1;
        } else {
            sb.append("Data de nascimento, \n");
        }

        if (pessoa.getEndereco() == null) {
            sb.append("Endereço, \n");
        } else {

            Endereco endereco = pessoa.getEndereco();

            if (StringUtils.isNotBlank(endereco.getCep())) {
                totalCamposValidos += 1;
            } else {
                sb.append("Cep, \n");
            }

            if (StringUtils.isNotBlank(endereco.getBairro())) {
                totalCamposValidos += 1;
            } else {
                sb.append("Bairro, \n");
            }

            if (StringUtils.isNotBlank(endereco.getLogradouro())) {
                totalCamposValidos += 1;
            } else {
                sb.append("Logradouro, \n");
            }

            if (endereco.getCidade() != null) {
                totalCamposValidos += 1;
            } else {
                sb.append("Cidade, \n");
            }

            if (endereco.getEstado() != null) {
                totalCamposValidos += 1;
            } else {
                sb.append("Estado, \n");
            }

        }

        double porcentagem = (totalCamposValidos / totalCamposValidar) * 100;

        evolucaoPreenchimento.setPorcentagemConcluida((int) porcentagem);

        evolucaoPreenchimento.setMensagem(sb.toString());
        
        eventoPessoa.setEvolucaoPreenchimento(evolucaoPreenchimento);

    }

    /**
     * Vai contabilizar a porcentagem de preenchimento do contratante.
     *
     * @param eventoPessoa
     */
    public void validarPorcentagemPreenchimentoNoivo(EventoPessoa eventoPessoa) {

        if (eventoPessoa == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (eventoPessoa.getPessoa() == null) {
            throw new GenericException("Evolucao Preenchimento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        EvolucaoPreenchimento evolucaoPreenchimento = eventoPessoa.getEvolucaoPreenchimento();

        if (evolucaoPreenchimento == null) {
            evolucaoPreenchimento = new EvolucaoPreenchimento(eventoPessoa);
        }

        double totalCamposValidar = 12;
        double totalCamposValidos = 0;
        StringBuilder sb = new StringBuilder();
        Pessoa pessoa = eventoPessoa.getPessoa();

        if (StringUtils.isNotBlank(pessoa.getNome())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Nome, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getCpf())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Cpf, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getRg())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Rg, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getEmail())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Email, \n");
        }

        if (StringUtils.isNotBlank(pessoa.getTelefoneCelular()) || StringUtils.isNotBlank(pessoa.getTelefoneResidencial())) {
            totalCamposValidos += 1;
        } else {
            sb.append("Telefone, \n");
        }

        if (pessoa.getDataNascimento() != null) {
            totalCamposValidos += 1;
        } else {
            sb.append("Data de nascimento, \n");
        }

        if (pessoa.getEndereco() == null) {
            sb.append("Endereço, \n");
        } else {

            Endereco endereco = pessoa.getEndereco();

            if (StringUtils.isNotBlank(endereco.getCep())) {
                totalCamposValidos += 1;
            } else {
                sb.append("Cep, \n");
            }

            if (StringUtils.isNotBlank(endereco.getBairro())) {
                totalCamposValidos += 1;
            } else {
                sb.append("Bairro, \n");
            }

            if (StringUtils.isNotBlank(endereco.getLogradouro())) {
                totalCamposValidos += 1;
            } else {
                sb.append("Logradouro, \n");
            }

            if (endereco.getCidade() != null) {
                totalCamposValidos += 1;
            } else {
                sb.append("Cidade, \n");
            }

            if (endereco.getEstado() != null) {
                totalCamposValidos += 1;
            } else {
                sb.append("Estado, \n");
            }

        }
        
        if(CollectionUtils.isNotBlank(pessoa.getContatosFamiliares())){
             totalCamposValidos += 1;
        }else{
            sb.append("Contato familiar \n");
        }

        double porcentagem = (totalCamposValidos / totalCamposValidar) * 100;
        
        evolucaoPreenchimento.setPorcentagemConcluida((int)porcentagem);

        evolucaoPreenchimento.setMensagem(sb.toString());
        
        eventoPessoa.setEvolucaoPreenchimento(evolucaoPreenchimento);

    }

}
