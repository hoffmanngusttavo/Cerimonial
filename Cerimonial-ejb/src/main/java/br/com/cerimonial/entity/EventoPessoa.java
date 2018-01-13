/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.enums.TipoEnvolvidoEvento;
import java.io.Serializable;
import java.util.Date;
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
public class EventoPessoa implements Serializable, ModelInterface {

    @Id
    @GeneratedValue(generator = "GENERATE_EventoPessoa", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_EventoPessoa", sequenceName = "EventoPessoa_pk_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;

    @ManyToOne
    private Evento evento;
    
    @ManyToOne
    private Pessoa pessoa;

    @Enumerated(EnumType.STRING)
    private TipoEnvolvidoEvento tipoEnvolvidoEvento;
    
    @Column(columnDefinition = "boolean default false")
    private boolean contratante = false;
    
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private EvolucaoPreenchimento evolucaoPreenchimento;

    public EventoPessoa() {
    }

    public EventoPessoa(Evento evento, Pessoa pessoa, TipoEnvolvidoEvento tipoEnvolvidoEvento, boolean contratante) {
        this.evento = evento;
        this.pessoa = pessoa;
        this.tipoEnvolvidoEvento = tipoEnvolvidoEvento;
        this.contratante = contratante;
        evolucaoPreenchimento = new EvolucaoPreenchimento();
    }
    
    public EventoPessoa(Evento evento, Pessoa pessoa, TipoEnvolvidoEvento tipoEnvolvidoEvento) {
        this.evento = evento;
        this.pessoa = pessoa;
        this.tipoEnvolvidoEvento = tipoEnvolvidoEvento;
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

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public TipoEnvolvidoEvento getTipoEnvolvidoEvento() {
        return tipoEnvolvidoEvento;
    }

    public void setTipoEnvolvidoEvento(TipoEnvolvidoEvento tipoEnvolvidoEvento) {
        this.tipoEnvolvidoEvento = tipoEnvolvidoEvento;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public boolean isContratante() {
        return contratante;
    }

    public void setContratante(boolean contratante) {
        this.contratante = contratante;
    }

    public EvolucaoPreenchimento getEvolucaoPreenchimento() {
        return evolucaoPreenchimento;
    }

    public void setEvolucaoPreenchimento(EvolucaoPreenchimento evolucaoPreenchimento) {
        this.evolucaoPreenchimento = evolucaoPreenchimento;
    }

   
    public int getPorcentagemPreenchimentoConcluida(){
        
        if(this.evolucaoPreenchimento != null){
            return this.evolucaoPreenchimento.getPorcentagemConcluida();
        }
    
        return 0;
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
        if (!(object instanceof EventoPessoa)) {
            return false;
        }
        EventoPessoa other = (EventoPessoa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.EventoPessoa[ id=" + id + " ]";
    }

    public String toStringPessoa() {
        
        StringBuilder sb = new StringBuilder("");
        
        if(this.pessoa != null){
            sb.append(this.pessoa.toStringDadosCompleto());
        }
        
        return sb.toString();
    }

}
