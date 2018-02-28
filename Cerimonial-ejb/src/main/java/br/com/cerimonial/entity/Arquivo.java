/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.apache.shiro.SecurityUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 *
 * @author Gustavo Hoffmann
 */
@Entity
@Audited
public class Arquivo implements Serializable, ModelInterface {

    @Id
    @GeneratedValue(generator = "GENERATE_Arquivo", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_Arquivo", sequenceName = "Arquivo_pk_seq", allocationSize = 1)
    private Long id;

    @NotNull(message = "O nome desse arquivo não pode ser nulo")
    @Column
    private String nome;

    @NotNull(message = "A extensão não pode ser nula")
    @Column
    private String extensao;

    @NotNull(message = "O conteúdo desse arquivo não pode ser nulo")
    @NotAudited
    @Column
    private byte[] conteudo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;

    @ManyToMany(mappedBy = "anexos")
    private List<ModeloProposta> modelosPropostas;
    
    @ManyToMany(mappedBy = "anexos")
    private List<ModeloEmail> modeloEmails;
    
    @ManyToMany(mappedBy = "anexos")
    private List<EmailContatoEvento> emailContatoEventos;
    
    @ManyToMany(mappedBy = "anexos")
    private List<OrcamentoLancamento> orcamentoLancamentos;

    public Arquivo() {

    }

    public Arquivo(String nome, String extensao, byte[] conteudo) {
        this.nome = nome;
        this.extensao = extensao;
        this.conteudo = conteudo;
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
        this.nome = nome;
    }

    public byte[] getConteudo() {
        return conteudo;
    }

    public void setConteudo(byte[] conteudo) {
        this.conteudo = conteudo;
    }

    public List<ModeloProposta> getModelosPropostas() {
        return modelosPropostas;
    }

    public void setModelosPropostas(List<ModeloProposta> modelosPropostas) {
        this.modelosPropostas = modelosPropostas;
    }

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public List<ModeloEmail> getModeloEmails() {
        return modeloEmails;
    }

    public void setModeloEmails(List<ModeloEmail> modeloEmails) {
        this.modeloEmails = modeloEmails;
    }

    public List<EmailContatoEvento> getEmailContatoEventos() {
        return emailContatoEventos;
    }

    public void setEmailContatoEventos(List<EmailContatoEvento> emailContatoEventos) {
        this.emailContatoEventos = emailContatoEventos;
    }

    public List<OrcamentoLancamento> getOrcamentoLancamentos() {
        return orcamentoLancamentos;
    }

    public void setOrcamentoLancamentos(List<OrcamentoLancamento> orcamentoLancamentos) {
        this.orcamentoLancamentos = orcamentoLancamentos;
    }
    
    

    public Arquivo clonar(Arquivo file) {
        if (file != null) {
            return new Arquivo(file.getNome(), file.getExtensao(), file.getConteudo());
        }
        return null;
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
        if (!(object instanceof Arquivo)) {
            return false;
        }
        Arquivo other = (Arquivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.Arquivo[ id=" + id + " ]";
    }

}
