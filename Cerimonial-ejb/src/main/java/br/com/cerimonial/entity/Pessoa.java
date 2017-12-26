/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.entity.validators.CPF;
import br.com.cerimonial.enums.TipoEnvolvido;
import br.com.cerimonial.enums.TipoPessoa;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.utils.CerimonialUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.br.CNPJ;

/**
 *
 * @author Gustavo Hoffmann
 */
@Entity
@Audited
public class Pessoa implements Serializable, ModelInterface {

    @Id
    @GeneratedValue(generator = "GENERATE_Pessoa", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_Pessoa", sequenceName = "Pessoa_pk_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Preencha um nome válido")
    @Size(min = 2, max = 255)
    private String nome;

    @Column
    private String rg;

    @Column
    @CPF
    private String cpf;

    @Column
    @CNPJ
    private String cnpj;

    @Column(unique = true)
    @NotNull
    private String email;

    @Column
    private String facebook;

    @Column
    private String instagram;

    @Column
    private String telefoneResidencial;

    @Column
    private String telefoneComercial;

    @Column
    private String telefoneCelular;

    @Column
    private String profissao;

    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataNascimento;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Usuario> usuariosClientes;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Endereco endereco = new Endereco();

    @Column
    @Enumerated(EnumType.STRING)
    private TipoPessoa tipoPessoa = TipoPessoa.JURIDICA;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = TipoEnvolvido.class)
    @JoinTable(name = "tipoEnvolvido", joinColumns = @JoinColumn(name = "pessoa_id", nullable = false))
    @Column(name = "tiposEnvolvidos")
    @Enumerated(EnumType.STRING)
    private List<TipoEnvolvido> tiposEnvolvidos;

    @Column(columnDefinition = "boolean default true")
    private boolean ativo = true;
    
    
    @OneToMany(mappedBy = "pessoa", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<ContatoEnvolvido> contatosFamiliares;
    
    @OneToMany(mappedBy = "contratante", fetch = FetchType.LAZY)
    private List<EventoPessoa> eventos;
    
    
    //Particularidades Colaborador
    @Column(columnDefinition = "boolean default true")
    private boolean carroProprio = true;

    //Particularidades Fornecedor
    @ManyToMany(fetch = FetchType.LAZY)
    private List<CategoriaFornecedor> categoriasFornecedor;
    

    
    public Pessoa(TipoEnvolvido tipoEnvolvido, TipoPessoa tipoPessoa) {
        setTipoEnvolvido(tipoEnvolvido);
        this.tipoPessoa = tipoPessoa;
        endereco = new Endereco();
    }

    public Pessoa() {
        endereco = new Endereco();
    }

    public boolean isPessoaFisica() {
        return this.getTipoPessoa().equals(TipoPessoa.FISICA);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Date getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    @Override
    public void setDataUltimaAlteracao(Date data) {
        this.dataUltimaAlteracao = data;
    }

    @Override
    public Usuario getModificadoPor() {
        return modificadoPor;
    }

    @Override
    public void setModificadoPor(Usuario modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = StringUtils.isNotBlank(nome) ? nome.toUpperCase().trim() : nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTelefoneResidencial() {
        return telefoneResidencial;
    }

    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }

    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public List<Usuario> getUsuariosClientes() {
        return usuariosClientes;
    }

    public void setUsuariosClientes(List<Usuario> usuariosClientes) {
        this.usuariosClientes = usuariosClientes;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public boolean isCarroProprio() {
        return carroProprio;
    }

    public void setCarroProprio(boolean carroProprio) {
        this.carroProprio = carroProprio;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public List<CategoriaFornecedor> getCategoriasFornecedor() {
        return categoriasFornecedor;
    }

    public void setCategoriasFornecedor(List<CategoriaFornecedor> categoriasFornecedor) {
        this.categoriasFornecedor = categoriasFornecedor;
    }

    public Usuario getUsuarioCliente() {

        if (CerimonialUtils.isListNotBlank(usuariosClientes)) {
            return usuariosClientes.get(0);
        }

        return null;
    }

    public void setUsuarioCliente(Usuario usuarioCliente) {

        if (usuarioCliente == null) {
            usuariosClientes = null;
        } else {

            if (CerimonialUtils.isListNotBlank(usuariosClientes)) {
                usuariosClientes.set(0, usuarioCliente);
            } else {

                if (usuariosClientes == null) {
                    usuariosClientes = new ArrayList<>();
                }

                usuariosClientes.add(usuarioCliente);
            }
        }

    }

    public List<TipoEnvolvido> getTiposEnvolvidos() {
        return tiposEnvolvidos;
    }

    public void setTiposEnvolvidos(List<TipoEnvolvido> tiposEnvolvidos) {
        this.tiposEnvolvidos = tiposEnvolvidos;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public List<ContatoEnvolvido> getContatosFamiliares() {
        return contatosFamiliares;
    }

    public void setContatosFamiliares(List<ContatoEnvolvido> contatosFamiliares) {
        this.contatosFamiliares = contatosFamiliares;
    }

    public List<EventoPessoa> getEventos() {
        return eventos;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pessoa other = (Pessoa) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.Pessoa[ id=" + id + " ]";
    }

    @PrePersist
    @Override
    public void prePersistEntity() {
        try {
            this.setDataUltimaAlteracao(new Date());
            this.setModificadoPor((Usuario) SecurityUtils.getSubject().getPrincipal());
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    @PreUpdate
    @Override
    public void preUpdateEntity() {
        try {
            this.setDataUltimaAlteracao(new Date());
            this.setModificadoPor((Usuario) SecurityUtils.getSubject().getPrincipal());
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    @PreRemove
    @Override
    public void preRemoveEntity() {
        try {
            this.setDataUltimaAlteracao(new Date());
            this.setModificadoPor((Usuario) SecurityUtils.getSubject().getPrincipal());
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public void adicionarCategoria(CategoriaFornecedor categoriaFornecedor) throws Exception {
        if (categoriaFornecedor == null) {
            throw new Exception("Categoria Inválida");
        }
        if (this.getCategoriasFornecedor() == null) {
            this.setCategoriasFornecedor(new LinkedList<>());
        }

        if (!this.getCategoriasFornecedor().contains(categoriaFornecedor)) {
            this.getCategoriasFornecedor().add(categoriaFornecedor);
        }
    }

    public void removerCategoria(CategoriaFornecedor categoriaFornecedor) throws Exception {
        if (categoriaFornecedor == null) {
            throw new Exception("Categoria Inválida");
        }
        if (this.getCategoriasFornecedor().isEmpty()) {
            throw new Exception("Lista de Categorias está vazia");
        }
        this.getCategoriasFornecedor().remove(categoriaFornecedor);
    }

    public void setTipoEnvolvido(TipoEnvolvido tipoEnvolvido) {

        if (tipoEnvolvido == null) {
            throw new GenericException("Tipo Envolvido nulo", ErrorCode.BAD_REQUEST.getCode());
        }

        if (tiposEnvolvidos == null) {
            tiposEnvolvidos = new ArrayList<>();
        }

        if (!tiposEnvolvidos.contains(tipoEnvolvido)) {
            tiposEnvolvidos.add(tipoEnvolvido);
        }
    }
    
    public void adicionarNovoContato(ContatoEnvolvido contatoEnvolvido) {

        if(contatoEnvolvido == null){
             throw new GenericException("Contato Envolvido nulo", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if(StringUtils.isBlank(contatoEnvolvido.getNome())){
             throw new GenericException("Contato deve possuir pelo menos um nome", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (contatosFamiliares == null) {
            contatosFamiliares = new ArrayList<>();
        }

        contatosFamiliares.add(contatoEnvolvido);
    }
    
    
    public void editarContato(ContatoEnvolvido contatoEnvolvido, int posicao) {

        if(contatoEnvolvido == null){
             throw new GenericException("Contato Envolvido nulo", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if(StringUtils.isBlank(contatoEnvolvido.getNome())){
             throw new GenericException("Contato deve possuir pelo menos um nome", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (contatosFamiliares == null) {
            contatosFamiliares = new ArrayList<>();
        }

        contatosFamiliares.set(posicao, contatoEnvolvido);
    }

    public void removerContato(Integer posicao) {

        if(posicao == null){
            throw new GenericException("Posição do contato não pode ser nula", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (CerimonialUtils.isListNotBlank(contatosFamiliares)) {

            contatosFamiliares.remove(posicao.intValue());

        }

    }

}
