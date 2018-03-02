/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.utils.CollectionUtils;
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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.validation.constraints.DecimalMin;
import org.apache.shiro.SecurityUtils;
import org.hibernate.envers.Audited;

/**
 *
 * @author hoffmann
 */
@Entity
@Audited
public class ServicoPrestadoEvento implements Serializable, ModelInterface {
    
    

    @Id
    @GeneratedValue(generator = "GENERATE_ServicoPrestadoEvento", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_ServicoPrestadoEvento", sequenceName = "ServicoPrestadoEvento_pk_seq", allocationSize = 1)
    private Long id;

    @DecimalMin("0.0")
    @Column(precision = 16, scale = 2)
    private Double valorProposta;

    @Column(precision = 16, scale = 2)
    private double valorAlterado = -1;

    @ManyToOne(fetch = FetchType.LAZY)
    private ModeloProposta modeloProposta;

    @Column(columnDefinition = "TEXT")
    private String proposta;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Arquivo> anexos;

    @OneToOne(mappedBy = "servicoPrestadoEvento")
    private Lancamento lancamento;
    
    @OneToOne
    private PreEvento preEvento;

    public ServicoPrestadoEvento() {
    }

    public ServicoPrestadoEvento(PreEvento preEvento) {
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

    public Double getValorProposta() {
        return valorProposta;
    }

    public void setValorProposta(Double valorProposta) {
        this.valorProposta = valorProposta;
    }

    public double getValorAlterado() {
        return valorAlterado;
    }

    public void setValorAlterado(double valorAlterado) {
        this.valorAlterado = valorAlterado;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Arquivo> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<Arquivo> anexos) {
        this.anexos = anexos;
    }
    
     public Arquivo getArquivo() {
        if (CollectionUtils.isNotBlank(anexos)) {
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
    
    public void setArquivo(Arquivo file) {
        anexos = anexos = new ArrayList<>();
        adicionarAnexo(file);
    }

    public double getValorFinal(){
        if(valorAlterado > -1){
            return valorAlterado;
        }else{
            return valorProposta;
        }
    }

    public Lancamento getLancamento() {
        return lancamento;
    }

    public void setLancamento(Lancamento lancamento) {
        this.lancamento = lancamento;
    }

    public PreEvento getPreEvento() {
        return preEvento;
    }

    public void setPreEvento(PreEvento preEvento) {
        this.preEvento = preEvento;
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
        if (!(object instanceof ServicoPrestadoEvento)) {
            return false;
        }
        ServicoPrestadoEvento other = (ServicoPrestadoEvento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.ServicoPrestadoEvento[ id=" + id + " ]";
    }

}
