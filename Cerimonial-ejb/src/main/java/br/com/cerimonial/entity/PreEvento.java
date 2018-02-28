/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.utils.CollectionUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
public class PreEvento implements Serializable, ModelInterface {
    
    @Id
    @GeneratedValue(generator = "GENERATE_PreEvento", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_PreEvento", sequenceName = "PreEvento_pk_seq", allocationSize = 1)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;

    @OneToMany(mappedBy = "preEvento", fetch = FetchType.LAZY)
    private List<ContatoEvento> contatosEvento;
    
    @OneToMany(mappedBy = "preEvento", fetch = FetchType.LAZY)
    private List<EmailContatoEvento> emailsContatoEvento;
    
    @OneToOne(fetch = FetchType.LAZY)
    private ServicoPrestadoEvento servicoPrestadoEvento;
    
    @OneToOne(fetch = FetchType.LAZY)
    private Evento evento;
    
    
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

    public List<ContatoEvento> getContatosEvento() {
        return contatosEvento;
    }

    public void setContatosEvento(List<ContatoEvento> contatosEvento) {
        this.contatosEvento = contatosEvento;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<EmailContatoEvento> getEmailsContatoEvento() {
        return emailsContatoEvento;
    }

    public void setEmailsContatoEvento(List<EmailContatoEvento> emailsContatoEvento) {
        this.emailsContatoEvento = emailsContatoEvento;
    }

    public ContatoEvento getContatoEvento() {
        return CollectionUtils.isNotBlank(contatosEvento) ? contatosEvento.get(0) : null;
    }

    public ServicoPrestadoEvento getServicoPrestadoEvento() {
        return servicoPrestadoEvento;
    }

    public void setServicoPrestadoEvento(ServicoPrestadoEvento servicoPrestadoEvento) {
        this.servicoPrestadoEvento = servicoPrestadoEvento;
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
        if (!(object instanceof PreEvento)) {
            return false;
        }
        PreEvento other = (PreEvento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.PreEvento[ id=" + id + " ]";
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
