/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.Empresa;
import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.enums.TipoEnvolvido;
import br.com.cerimonial.enums.TipoPessoa;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.repository.EmpresaRepository;
import br.com.cerimonial.utils.CollectionUtils;
import java.util.Arrays;
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
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmpresaService extends BasicService<Empresa> {

    @EJB
    protected PessoaService pessoaService;
    
    private EmpresaRepository repository;

    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new EmpresaRepository(em);
    }

    @Override
    public Empresa findEntityById(Long id) throws Exception {
        return repository.getEntity(id);
    }

    public Empresa getEmpresa() throws Exception {
        
        List<Empresa> empresas = repository.findRangeListagem(1, 0, null, null);
        
        if (CollectionUtils.isNotBlank(empresas)) {
            
            for (Empresa empresa : empresas) {
                
                smartLazy(empresa, Arrays.asList("pessoa", "pessoa.endereco"));
                
//                if(empresa.getPessoa() != null){
//                    empresa.getPessoa().getId();
//                    
//                    if(empresa.getPessoa().getEndereco() != null){
//                        empresa.getPessoa().getEndereco().getId();
//                    }
//                }
                
                return empresa;
            }
        }
        return new Empresa();
    }

    @Override
    public Empresa save(Empresa entity) throws Exception {

        validateObjectNull(entity);
        
        Pessoa pessoa = atualizarDadosPessoa(entity);
        
        pessoaService.save(pessoa);
        
        entity.setPessoa(pessoa);
        
        if (entity.getId() == null) {
            return repository.create(entity);
        } else {
            return repository.edit(entity);
        }
    }

   

    private Pessoa atualizarDadosPessoa(Empresa entity) {
        
        Pessoa pessoa = entity.getPessoa();
        
        if(pessoa == null){
            pessoa = new Pessoa();
        }
        
        pessoa.setAtivo(true);
        pessoa.setCnpj(entity.getCnpj());
        pessoa.setCpf(entity.getCpf());
        pessoa.setEmail(entity.getEmail());
        pessoa.setNome(entity.getNome());
        pessoa.setTelefoneCelular(entity.getTelefone1());
        pessoa.setTelefoneResidencial(entity.getTelefone2());
        pessoa.setTipoEnvolvido(TipoEnvolvido.FORNECEDOR);
        pessoa.setTipoPessoa(TipoPessoa.JURIDICA);
        
        pessoa.setEndereco(pessoa.getEndereco().copiarEndereco(entity.getEndereco()));
        
        return pessoa;
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
    public void validateObjectNull(Empresa entity) {
        
         if (entity == null) {
            throw new GenericException(" Empresa nulo.", ErrorCode.BAD_REQUEST.getCode());
        }
        
    }

    @Override
    public void validateObjectAndIdNull(Empresa entity) {
        
        validateObjectNull(entity);
        
        validateId(entity.getId());
        
    }
    

}
