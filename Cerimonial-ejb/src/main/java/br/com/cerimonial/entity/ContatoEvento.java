/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.enums.ClassificacaoContato;
import br.com.cerimonial.enums.TipoEnvolvidoEvento;
import br.com.cerimonial.enums.TipoEvento;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;
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
    @Size(min = 2, max = 255, message = "Nome deve estar entre 2 a 255 caracteres")
    private String nomeContato;
    
    @Column(nullable = false)
    @NotNull
    @Size(min = 2, max = 255, message = "Nome deve estar entre 2 a 255 caracteres")
    private String nomeEvento;
    
    @Column(nullable = false)
    @NotNull
    private String telefonePrincipal;
    
    @Column
    private String telefoneSecundario;
    
    @Column(nullable = false)
    @NotNull
    private String emailContato;
    
    @Column
    private String localEvento;
    
    @Column
    private String localFesta;
    
    @Column(columnDefinition = "TEXT")
    private String observacao;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;
    
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEvento;
    
    @Column
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date horaEvento;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataContato = new Date();
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;
    
    @Column
    @Min(value = 1, message = "Quantidade mínima deve ser 1")
    private Integer quantidadeConvidados;
    
    @NotNull(message = "A categoria não pode ser nula")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvento tipoEvento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoIndicacao tipoIndicacao;

    @NotNull(message = "O status não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassificacaoContato status = ClassificacaoContato.AGUARDANDO_RETORNO;
    
    /**
     * Se o evento é para a pessoa que entrou em contato
     */
    @Column(columnDefinition = "boolean default false")
    private boolean eventoProprioContratante = false;
    
    
    @Enumerated(EnumType.STRING)
    private TipoEnvolvidoEvento tipoEnvolvidoEvento;
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private PreEvento preEvento;

    public ContatoEvento() {
        preEvento = new PreEvento();
    }


    public PreEvento getPreEvento() {
        return preEvento;
    }

    public void setPreEvento(PreEvento preEvento) {
        this.preEvento = preEvento;
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

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getTelefonePrincipal() {
        return telefonePrincipal;
    }

    public void setTelefonePrincipal(String telefonePrincipal) {
        this.telefonePrincipal = telefonePrincipal;
    }

    public String getTelefoneSecundario() {
        return telefoneSecundario;
    }

    public void setTelefoneSecundario(String telefoneSecundario) {
        this.telefoneSecundario = telefoneSecundario;
    }

   

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = StringUtils.isNotBlank(emailContato) ? emailContato.trim() : emailContato;
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

    public TipoIndicacao getTipoIndicacao() {
        return tipoIndicacao;
    }

    public void setTipoIndicacao(TipoIndicacao tipoIndicacao) {
        this.tipoIndicacao = tipoIndicacao;
    }

    public ClassificacaoContato getStatus() {
        return status;
    }

    public void setStatus(ClassificacaoContato status) {
        this.status = status;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento != null ? nomeEvento.toUpperCase().trim() : nomeEvento;
    }

    public boolean isEventoProprioContratante() {
        return eventoProprioContratante;
    }

    public void setEventoProprioContratante(boolean eventoProprioContratante) {
        this.eventoProprioContratante = eventoProprioContratante;
    }

    public TipoEnvolvidoEvento getTipoEnvolvidoEvento() {
        return tipoEnvolvidoEvento;
    }

    public void setTipoEnvolvidoEvento(TipoEnvolvidoEvento tipoEnvolvidoEvento) {
        this.tipoEnvolvidoEvento = tipoEnvolvidoEvento;
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
