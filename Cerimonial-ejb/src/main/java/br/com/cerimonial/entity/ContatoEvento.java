/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.apache.shiro.SecurityUtils;
import org.hibernate.envers.Audited;

/**
 *
 * @author Gustavo Hoffmann
 */
@Entity
@Audited
public class ContatoEvento implements Serializable, ModelInterface{
    
    @Id
    @GeneratedValue(generator = "GENERATE_ContatoEvento", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_ContatoEvento", sequenceName = "ContatoEvento_pk_seq", allocationSize = 1)
    private Long id;
    
    @Column(nullable = false)
    @NotNull
    @Size(min = 2, max = 255)
    private String nomeContato;
    
    @Column(nullable = false)
    @NotNull
    private String telefoneContato;
    
    @Column
    private String emailContato;
    
    @Column(nullable = false)
    @NotNull
    @Size(min = 2, max = 255)
    private String localEvento;
    
    @Column(nullable = false)
    @NotNull
    @Size(min = 2, max = 255)
    private String localFesta;
    
    @Column(columnDefinition = "TEXT")
    private String observacao;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEvento;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date horaEvento;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataContato;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;
    
    @Column
    private Integer quantidadeConvidados;
    
    @ManyToOne
    private TipoEvento tipoEvento;
    
    @OneToMany(mappedBy = "contatoEvento", fetch = FetchType.LAZY)
    private List<OrcamentoEvento> propostas;
 
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoIndicacao tipoIndicacao;
    
    
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

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    public String getLocalEvento() {
        return localEvento;
    }

    public void setLocalEvento(String localEvento) {
        this.localEvento = localEvento;
    }

    public String getLocalFesta() {
        return localFesta;
    }

    public void setLocalFesta(String localFesta) {
        this.localFesta = localFesta;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(Date dataEvento) {
        this.dataEvento = dataEvento;
    }

    public Date getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(Date horaEvento) {
        this.horaEvento = horaEvento;
    }

    public Date getDataContato() {
        return dataContato;
    }

    public void setDataContato(Date dataContato) {
        this.dataContato = dataContato;
    }

    public Integer getQuantidadeConvidados() {
        return quantidadeConvidados;
    }

    public void setQuantidadeConvidados(Integer quantidadeConvidados) {
        this.quantidadeConvidados = quantidadeConvidados;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public List<OrcamentoEvento> getPropostas() {
        return propostas;
    }

    public void setPropostas(List<OrcamentoEvento> propostas) {
        this.propostas = propostas;
    }

    public TipoIndicacao getTipoIndicacao() {
        return tipoIndicacao;
    }

    public void setTipoIndicacao(TipoIndicacao tipoIndicacao) {
        this.tipoIndicacao = tipoIndicacao;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final ContatoEvento other = (ContatoEvento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ContatoEvento{" + "id=" + id + '}';
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
