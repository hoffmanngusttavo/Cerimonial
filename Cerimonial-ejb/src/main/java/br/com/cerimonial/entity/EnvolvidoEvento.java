/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.enums.TipoEnvolvidoEvento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import br.com.cerimonial.utils.CerimonialUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
import org.hibernate.validator.constraints.br.CPF;

/**
 *
 * @author hoffmann
 */
@Entity
@Audited
public class EnvolvidoEvento implements Serializable, ModelInterface {

    @Id
    @GeneratedValue(generator = "GENERATE_EnvolvidoEvento", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_EnvolvidoEvento", sequenceName = "EnvolvidoEvento_pk_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    @NotNull
    @Size(min = 2, max = 255)
    private String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoEnvolvidoEvento tipoEnvolvidoEvento = TipoEnvolvidoEvento.NOIVO;

    @Column
    private String rg;

    @Column
    @CPF
    private String cpf;

    @Column
    private String email;

    @Column
    private String facebook;

    @Column
    private String instagram;

    @Column
    private String telefoneResidencial;

    @Column
    private String telefoneCelular;
    
    @Column
    private String profissao;
    
    @Column(columnDefinition = "TEXT")
    private String observacao;
    
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataNascimento;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Endereco endereco = new Endereco();

    @ManyToOne
    private Evento evento;

    @OneToMany(mappedBy = "envolvidoEvento", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<ContatoEnvolvido> contatosFamiliar;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Usuario getModificadoPor() {
        return modificadoPor;
    }

    @Override
    public void setModificadoPor(Usuario modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    @Override
    public Date getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    @Override
    public void setDataUltimaAlteracao(Date data) {
        this.dataUltimaAlteracao = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoEnvolvidoEvento getTipoEnvolvidoEvento() {
        return tipoEnvolvidoEvento;
    }

    public void setTipoEnvolvidoEvento(TipoEnvolvidoEvento tipoEnvolvidoEvento) {
        this.tipoEnvolvidoEvento = tipoEnvolvidoEvento;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<ContatoEnvolvido> getContatosFamiliar() {
        return contatosFamiliar;
    }

    public void setContatosFamiliar(List<ContatoEnvolvido> contatosFamiliar) {
        this.contatosFamiliar = contatosFamiliar;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    
    

    public EnvolvidoEvento(Evento evento, TipoEnvolvidoEvento tipoEnvolvidoEvento) {
        this.evento = evento;
        this.tipoEnvolvidoEvento = tipoEnvolvidoEvento;
    }

    public EnvolvidoEvento() {
    
    }
    
    

    public void adicionarNovoContato(ContatoEnvolvido contatoEnvolvido) {

        if(contatoEnvolvido == null){
             throw new GenericException("Contato Envolvido nulo", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if(StringUtils.isBlank(contatoEnvolvido.getNome())){
             throw new GenericException("Contato deve possuir pelo menos um nome", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (contatosFamiliar == null) {
            contatosFamiliar = new ArrayList<>();
        }

        contatosFamiliar.add(contatoEnvolvido);
    }
    
    
    public void editarContato(ContatoEnvolvido contatoEnvolvido, int posicao) {

        if(contatoEnvolvido == null){
             throw new GenericException("Contato Envolvido nulo", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if(StringUtils.isBlank(contatoEnvolvido.getNome())){
             throw new GenericException("Contato deve possuir pelo menos um nome", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (contatosFamiliar == null) {
            contatosFamiliar = new ArrayList<>();
        }

        contatosFamiliar.set(posicao, contatoEnvolvido);
    }

    public void removerContato(Integer posicao) {

        if(posicao == null){
            throw new GenericException("Posição do contato não pode ser nula", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if (CerimonialUtils.isListNotBlank(contatosFamiliar)) {

            contatosFamiliar.remove(posicao.intValue());

        }

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnvolvidoEvento)) {
            return false;
        }
        EnvolvidoEvento other = (EnvolvidoEvento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.EnvolvidoEvento[ id=" + id + " ]";
    }

}
