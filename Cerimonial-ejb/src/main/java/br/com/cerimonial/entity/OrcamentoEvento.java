/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import org.apache.shiro.SecurityUtils;
import org.hibernate.envers.Audited;

/**
 *
 * @author Gustavo Hoffmann
 */
@Entity
@Audited
public class OrcamentoEvento implements Serializable, ModelInterface {
    

    @Id
    @GeneratedValue(generator = "GENERATE_OrcamentoEvento", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_OrcamentoEvento", sequenceName = "OrcamentoEvento_pk_seq", allocationSize = 1)
    private Long id;
    
    @Column(precision = 16, scale = 2)
    private double valorProposta = 0;
    
    @Column(precision = 16, scale = 2)
    private double valorAlterado = -1;
    
    //Se a proposta foi enviada por email
    @Column(columnDefinition = "boolean default false")
    private boolean propostaEnviada = false;
    
    //Se a proposta foi aceita
    @Column(columnDefinition = "boolean default false")
    private boolean propostaAceita = false;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataEnvio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private ModeloProposta modeloProposta;
    
    @Column(columnDefinition = "TEXT")
    private String proposta;
    
    @ManyToOne
    private ContatoEvento contatoEvento;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    public OrcamentoEvento(ContatoEvento contatoEvento) {
        this.contatoEvento = contatoEvento;
    }

    public OrcamentoEvento() {
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

    public double getValorProposta() {
        return valorProposta;
    }

    public void setValorProposta(double valorProposta) {
        this.valorProposta = valorProposta;
    }

    public double getValorAlterado() {
        return valorAlterado;
    }

    public void setValorAlterado(double valorAlterado) {
        this.valorAlterado = valorAlterado;
    }

    public boolean isPropostaEnviada() {
        return propostaEnviada;
    }

    public void setPropostaEnviada(boolean propostaEnviada) {
        this.propostaEnviada = propostaEnviada;
    }

    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public ModeloProposta getModeloProposta() {
        return modeloProposta;
    }

    public void setModeloProposta(ModeloProposta modeloProposta) {
        this.modeloProposta = modeloProposta;
    }

    public String getProposta() {
        return proposta;
    }

    public void setProposta(String proposta) {
        this.proposta = proposta;
    }

    public ContatoEvento getContatoEvento() {
        return contatoEvento;
    }

    public void setContatoEvento(ContatoEvento contatoEvento) {
        this.contatoEvento = contatoEvento;
    }

    public boolean isPropostaAceita() {
        return propostaAceita;
    }

    public void setPropostaAceita(boolean propostaAceita) {
        this.propostaAceita = propostaAceita;
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
        if (!(object instanceof OrcamentoEvento)) {
            return false;
        }
        OrcamentoEvento other = (OrcamentoEvento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.OrcamentoEvento[ id=" + id + " ]";
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
