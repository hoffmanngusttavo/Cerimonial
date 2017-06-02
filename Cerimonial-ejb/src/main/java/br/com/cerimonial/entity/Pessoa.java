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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @GeneratedValue(generator = "GENERATE_Pessoa", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "GENERATE_Pessoa", sequenceName = "Pessoa_pk_seq", allocationSize = 1)
    private Long id;
    @Column(nullable = false)
    @NotNull
    @Size(min = 2, max = 255)
    private String nome;
    @Column(nullable = false)
    @NotNull
    @Size(min = 2, max = 255)
    private String rg;
    @Column
    @Size(min = 11, max = 25)
    private String cpf;
    @Column
    @Size(min = 14, max = 25)
    private String cnpj;
    @Column(nullable = false)
    @Size(min = 2, max = 255)
    private String email;
    @Column
    @Size(min = 2, max = 255)
    private String facebook;
    @Column
    @Size(min = 2, max = 255)
    private String instagram;
    @Column
    @NotNull
    private String telefone1;
    @Column
    private String telefone2;
    @Column(columnDefinition = "TEXT")
    private String observacao;
    @ManyToOne
    private Usuario modificadoPor;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;
    @OneToOne
    private Endereco endereco;
    
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
    @ManyToOne
    private CategoriaFornecedor categoria;
    
    //Particularidades Cliente
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataNascimento;
    
    
    

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
        this.nome = nome;
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

    public CategoriaFornecedor getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaFornecedor categoria) {
        this.categoria = categoria;
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
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pessoa)) {
            return false;
        }
        Pessoa other = (Pessoa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
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

}