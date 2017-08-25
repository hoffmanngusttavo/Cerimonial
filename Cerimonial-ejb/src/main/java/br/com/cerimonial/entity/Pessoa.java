/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.enums.TipoEnvolvido;
import br.com.cerimonial.enums.TipoPessoa;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @NotNull
    @Size(min = 2, max = 255)
    private String nome;

    @Column
    private String rg;

    @Column
    private String cpf;

    @Column
    private String cnpj;

    @Column
    private String email;

    @Column
    private String facebook;

    @Column
    private String instagram;

    @Column(nullable = false)
    @NotNull
    private String telefone1;

    @Column
    private String telefone2;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @OneToOne
    private Usuario usuarioCliente;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, optional = false, fetch = FetchType.LAZY)
    private Endereco endereco = new Endereco();

    @Column
    @Enumerated(EnumType.STRING)
    private TipoPessoa tipoPessoa = TipoPessoa.JURIDICA;

    @Column
    @Enumerated(EnumType.STRING)
    private TipoEnvolvido tipoEnvolvido = TipoEnvolvido.CLIENTE;

    @Column(columnDefinition = "boolean default true")
    private boolean ativo = true;

    //Particularidades Colaborador
    @Column(columnDefinition = "boolean default true")
    private boolean carroProprio = true;

    //Particularidades Fornecedor
    @ManyToMany(fetch = FetchType.LAZY)
    private List<CategoriaFornecedor> categoriasFornecedor;

    //Particularidades Cliente
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataNascimento;

    
    
    @OneToMany(mappedBy = "contratante", fetch = FetchType.LAZY)
    private List<Evento> eventos;

    public Pessoa(TipoEnvolvido tipoEnvolvido, TipoPessoa tipoPessoa) {
        this.tipoEnvolvido = tipoEnvolvido;
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
        this.nome = StringUtils.isNotBlank(nome) ? nome.toUpperCase().trim(): nome;
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

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
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

    public TipoEnvolvido getTipoEnvolvido() {
        return tipoEnvolvido;
    }

    public void setTipoEnvolvido(TipoEnvolvido tipoEnvolvido) {
        this.tipoEnvolvido = tipoEnvolvido;
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
        return usuarioCliente;
    }

    public void setUsuarioCliente(Usuario usuarioCliente) {
        this.usuarioCliente = usuarioCliente;
    }



    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
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

    public void adicionarCategoria(CategoriaFornecedor categoriaFornecedor) throws Exception{
        if(categoriaFornecedor == null){
            throw new Exception("Categoria Inválida");
        }
        if(this.getCategoriasFornecedor() == null){
            this.setCategoriasFornecedor(new LinkedList<>());
        }
        
        if(!this.getCategoriasFornecedor().contains(categoriaFornecedor)){
            this.getCategoriasFornecedor().add(categoriaFornecedor);
        }
    }
    
    public void removerCategoria(CategoriaFornecedor categoriaFornecedor) throws Exception{
        if(categoriaFornecedor == null){
            throw new Exception("Categoria Inválida");
        }
        if(this.getCategoriasFornecedor().isEmpty()){
            throw new Exception("Lista de Categorias está vazia");
        }
        this.getCategoriasFornecedor().remove(categoriaFornecedor);
    }

}
