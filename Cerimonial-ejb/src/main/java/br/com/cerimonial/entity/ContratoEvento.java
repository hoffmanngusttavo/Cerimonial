/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * @author hoffmann
 */
@Entity
@Audited
public class ContratoEvento implements Serializable, ModelInterface {
    
    
    @Id
    @GeneratedValue(generator = "GENERATE_ContratoEvento", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_ContratoEvento", sequenceName = "ContratoEvento_pk_seq", allocationSize = 1)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String conteudo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataImpressao;
    
    @ManyToOne(optional = false)
    private Evento evento;
    
    
    @ManyToOne
    private ModeloContrato modeloContrato;

    public ContratoEvento() {
    }

    public ContratoEvento(Evento evento) {
        this.evento = evento;
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

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Date getDataImpressao() {
        return dataImpressao;
    }

    public void setDataImpressao(Date dataImpressao) {
        this.dataImpressao = dataImpressao;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public ModeloContrato getModeloContrato() {
        return modeloContrato;
    }

    public void setModeloContrato(ModeloContrato modeloContrato) {
        this.modeloContrato = modeloContrato;
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
        if (!(object instanceof ContratoEvento)) {
            return false;
        }
        ContratoEvento other = (ContratoEvento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.ContratoEvento[ id=" + id + " ]";
    }
    
}
