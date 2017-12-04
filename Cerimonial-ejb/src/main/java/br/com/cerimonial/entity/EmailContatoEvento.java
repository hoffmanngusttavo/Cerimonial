/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

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
import javax.validation.constraints.Size;
import org.apache.shiro.SecurityUtils;
import org.hibernate.envers.Audited;

/**
 *
 * @author hoffmann
 */
@Entity
@Audited
public class EmailContatoEvento implements Serializable, ModelInterface {
    
    
     @Id
    @GeneratedValue(generator = "GENERATE_EmailContatoEvento", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_EmailContatoEvento", sequenceName = "EmailContatoEvento_pk_seq", allocationSize = 1)
    private Long id;
    
    @Column(nullable = false)
    @NotNull
    @Size(min = 2, max = 255, message = "Nome deve estar entre 2 a 255 caracteres")
    private String tituloEmail;
    
    @Column(nullable = false)
    @NotNull
    @Size(min = 10, message = "Destinatários deve ter no mínimo 10 caracteres")
    private String destinatarios;
    
    @Column(columnDefinition = "TEXT")
    private String corpoEmail;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Arquivo> anexos;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataCadastro;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;
    
    @ManyToOne
    private ContatoEvento contatoEvento;
    
    @ManyToOne
    private ModeloEmail modeloEmail;

    public EmailContatoEvento(ContatoEvento contatoEvento) {
        this.contatoEvento = contatoEvento;
    }

    public EmailContatoEvento() {
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

    public String getTituloEmail() {
        return tituloEmail;
    }

    public void setTituloEmail(String tituloEmail) {
        this.tituloEmail = tituloEmail;
    }

    public String getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(String destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getCorpoEmail() {
        return corpoEmail;
    }

    public void setCorpoEmail(String corpoEmail) {
        this.corpoEmail = corpoEmail;
    }

    public ContatoEvento getContatoEvento() {
        return contatoEvento;
    }

    public void setContatoEvento(ContatoEvento contatoEvento) {
        this.contatoEvento = contatoEvento;
    }

    public ModeloEmail getModeloEmail() {
        return modeloEmail;
    }

    public void setModeloEmail(ModeloEmail modeloEmail) {
        this.modeloEmail = modeloEmail;
    }

    public List<Arquivo> getAnexos() {
        return anexos;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    public void setArquivo(Arquivo file) {
       anexos =  anexos = new ArrayList<>();
       adicionarAnexo(file);
    }

    public Arquivo getArquivo() {
        if (CerimonialUtils.isListNotBlank(anexos)) {
            return anexos.get(0);
        }
        return null;
    }
    
     public void adicionarAnexo(Arquivo arquivo) {
        if (arquivo != null) {

            if (anexos == null) {
                anexos = new ArrayList<>();
            }
            anexos.add(arquivo);
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
        if (!(object instanceof EmailContatoEvento)) {
            return false;
        }
        EmailContatoEvento other = (EmailContatoEvento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.EmailContatoEvento[ id=" + id + " ]";
    }
    
}
