/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.validation.constraints.DecimalMin;
import org.apache.shiro.SecurityUtils;
import org.hibernate.envers.Audited;

/**
 *
 * @author hoffmann
 */
@Entity
@Audited
public class CustoEvento implements Serializable, ModelInterface{
    
    
    @Id
    @GeneratedValue(generator = "GENERATE_CustoEvento", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_CustoEvento", sequenceName = "CustoEvento_pk_seq", allocationSize = 1)
    private Long id;
    
    @DecimalMin("0.0")
    @Column(precision = 16, scale = 2)
    private Double valorOrcamento;
    
    @DecimalMin("0.0")
    @Column(precision = 16, scale = 2)
    private Double valorCustoReal;
    
    @DecimalMin("0.0")
    @Column(precision = 16, scale = 2)
    private Double valorCustoEstimado;
    
    @DecimalMin("0.0")
    @Column(precision = 16, scale = 2)
    private Double valorTotalPago;
    
    @OneToOne(mappedBy = "custoEvento")
    private Evento evento;
    
    @OneToMany(mappedBy = "custoEvento", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Lancamento> lancamentos;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;
    
    
    

    public CustoEvento(Evento evento) {
        this.evento = evento;
    }

    public CustoEvento() {
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

    public Double getValorOrcamento() {
        return valorOrcamento;
    }

    public void setValorOrcamento(Double valorOrcamento) {
        this.valorOrcamento = valorOrcamento;
    }

    public Double getValorCustoReal() {
        return valorCustoReal;
    }

    public void setValorCustoReal(Double valorCustoReal) {
        this.valorCustoReal = valorCustoReal;
    }

    public Double getValorTotalPago() {
        return valorTotalPago;
    }

    public void setValorTotalPago(Double valorTotalPago) {
        this.valorTotalPago = valorTotalPago;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<Lancamento> getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(List<Lancamento> lancamentos) {
        this.lancamentos = lancamentos;
    }

    public Double getValorCustoEstimado() {
        return valorCustoEstimado;
    }

    public void setValorCustoEstimado(Double valorCustoEstimado) {
        this.valorCustoEstimado = valorCustoEstimado;
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
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final CustoEvento other = (CustoEvento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CustoEvento{" + "id=" + id + '}';
    }

    public void adicionarLancamento(Lancamento lancamento) {
        
        if(lancamento == null){
            throw new GenericException("Lancamento nulo", ErrorCode.BAD_REQUEST.getCode());
        }
        
        if(lancamentos == null){
            lancamentos = new LinkedList<Lancamento>();
        }
        
        if(!lancamentos.contains(lancamento)){
            lancamentos.add(lancamento);
        }
        
    }
    
    
    
    
}
