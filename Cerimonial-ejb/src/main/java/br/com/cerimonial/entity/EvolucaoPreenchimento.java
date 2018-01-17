/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import java.io.Serializable;
import java.util.Arrays;
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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.envers.Audited;

/**
 *
 * @author hoffmann
 */
@Entity
@Audited
public class EvolucaoPreenchimento implements Serializable, ModelInterface {
    

    @Id
    @GeneratedValue(generator = "GENERATE_EvolucaoPreenchimento", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_EvolucaoPreenchimento", sequenceName = "EvolucaoPreenchimento_pk_seq", allocationSize = 1)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    @Max(value = 100, message = "Quantidade máxima deve ser 100")
    @Min(value = 0, message = "Quantidade mínima deve ser 0")
    private int porcentagemConcluida = 0;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;
    
    @OneToOne(mappedBy = "evolucaoPreenchimento")
    private EventoPessoa eventoPessoa;
    
    @ManyToMany(mappedBy = "evolucoesPreenchimento")
    private List<Evento> eventos;

    public EvolucaoPreenchimento() {
    }

    public EvolucaoPreenchimento(EventoPessoa eventoPessoa) {
        this.eventoPessoa = eventoPessoa;
    }
    
    public EvolucaoPreenchimento(Evento evento) {
        this.eventos = Arrays.asList(evento);
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        
        if(StringUtils.isNotBlank(mensagem)){
           StringBuilder sb = new StringBuilder("Você deve preencher os seguintes campos: ");
           sb.append(mensagem);
           mensagem = sb.toString();
        }
        
        this.mensagem = mensagem;
    }

    public int getPorcentagemConcluida() {
        return porcentagemConcluida;
    }

    public void setPorcentagemConcluida(int porcentagemConcluida) {
        this.porcentagemConcluida = porcentagemConcluida;
    }

    public EventoPessoa getEventoPessoa() {
        return eventoPessoa;
    }

    public void setEventoPessoa(EventoPessoa eventoPessoa) {
        this.eventoPessoa = eventoPessoa;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
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
        if (!(object instanceof EvolucaoPreenchimento)) {
            return false;
        }
        EvolucaoPreenchimento other = (EvolucaoPreenchimento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.EvolucaoPreenchimento[ id=" + id + " ]";
    }

}
