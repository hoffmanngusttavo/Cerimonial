/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.utils.CerimonialUtils;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.validation.constraints.DecimalMin;
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
public class ModeloProposta implements Serializable, ModelInterface {

    @Id
    @GeneratedValue(generator = "GENERATE_ModeloProposta", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_ModeloProposta", sequenceName = "ModeloProposta_pk_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "O título não pode ser nulo")
    @Size(min = 2, max = 255)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    @Column(columnDefinition = "boolean default true")
    private boolean ativo = true;

    @NotNull(message = "O tipo de evento não pode ser nulo")
    @ManyToOne
    private TipoEvento tipoEvento;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;

    @DecimalMin(value = "0.0", message = "O valor não pode ser negativo")
    @Column(precision = 16, scale = 2)
    private BigDecimal valorProposta = BigDecimal.ZERO;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Arquivo> anexos;

    @OneToMany(mappedBy = "modeloProposta", fetch = FetchType.LAZY)
    private List<OrcamentoEvento> orcamentos;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = StringUtils.isNotBlank(titulo) ? titulo.toUpperCase().trim() : titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
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

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public BigDecimal getValorProposta() {
        return valorProposta;
    }

    public void setValorProposta(BigDecimal valorProposta) {
        this.valorProposta = valorProposta;
    }

    public List<OrcamentoEvento> getOrcamentos() {
        return orcamentos;
    }

    public void setOrcamentos(List<OrcamentoEvento> orcamentos) {
        this.orcamentos = orcamentos;
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

    public List<Arquivo> getAnexos() {
        return anexos;
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
        if (!(object instanceof ModeloProposta)) {
            return false;
        }
        ModeloProposta other = (ModeloProposta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.ModeloProposta[ id=" + id + " ]";
    }

}
