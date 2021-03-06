/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.enums.FormaPagamento;
import br.com.cerimonial.utils.DateUtils;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import org.apache.shiro.SecurityUtils;
import org.hibernate.envers.Audited;

/**
 *
 * @author hoffmann
 */
@Entity
@Audited
public class Parcela implements Serializable, ModelInterface {
    

    @Id
    @GeneratedValue(generator = "GENERATE_Parcela", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_Parcela", sequenceName = "Parcela_pk_seq", allocationSize = 1)
    private Long id;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataVencimento;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataPagamento;
    
    @NotNull(message = "A forma de pagamento não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormaPagamento formaPagamento = FormaPagamento.DINHEIRO;

    @DecimalMin("0.0")
    @Column(precision = 16, scale = 2)
    private Double valorCobrado;

    @DecimalMin("0.0")
    @Column(precision = 16, scale = 2)
    private Double valorPago;
    
    @Column(nullable = false, columnDefinition = "int default 1")
    private int numeroParcela = 1;
    
    @Column(columnDefinition = "boolean default false")
    private boolean pago = false;
    
    @ManyToOne
    private Lancamento lancamento;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;
    
    @OneToOne(mappedBy = "parcelaPai")
    private ParcelaVinculada parcelaVinculadaPai;
    
    @OneToOne(mappedBy = "parcelaFilha")
    private ParcelaVinculada parcelaVinculadaFilha;

    public Parcela() {
    }

    public Parcela(Lancamento lancamento) {
        this.lancamento = lancamento;
    }
    
    public Parcela(Lancamento lancamento, double valorParcela, Date dataVencimento) {
        this.lancamento = lancamento;
        this.valorCobrado = valorParcela;
        this.dataVencimento = dataVencimento;
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

    public Double getValorCobrado() {
        return valorCobrado;
    }

    public void setValorCobrado(Double valorCobrado) {
        this.valorCobrado = valorCobrado;
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public Lancamento getLancamento() {
        return lancamento;
    }

    public void setLancamento(Lancamento lancamento) {
        this.lancamento = lancamento;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public int getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(int numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public ParcelaVinculada getParcelaVinculadaPai() {
        return parcelaVinculadaPai;
    }

    public void setParcelaVinculadaPai(ParcelaVinculada parcelaVinculadaPai) {
        this.parcelaVinculadaPai = parcelaVinculadaPai;
    }

    public ParcelaVinculada getParcelaVinculadaFilha() {
        return parcelaVinculadaFilha;
    }

    public void setParcelaVinculadaFilha(ParcelaVinculada parcelaVinculadaFilha) {
        this.parcelaVinculadaFilha = parcelaVinculadaFilha;
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
        if (!(object instanceof Parcela)) {
            return false;
        }
        Parcela other = (Parcela) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.Parcela[ id=" + id + " ]";
    }

    public String toStringDadosCobranca() {

        StringBuilder sb = new StringBuilder();

        sb.append("<td>");
        if (this.dataVencimento != null) {
            sb.append(" ").append(DateUtils.formatDate(this.dataVencimento, DateUtils.ddMMyyyy));
        } else {
            sb.append(" Não definido ");
        }
        sb.append("</td>");
        
        sb.append("<td>");
        if (this.valorCobrado != null) {
            sb.append(" ").append(valorCobrado);
        } else {
            sb.append(" Não definido ");
        }
        sb.append("</td>");
        
        sb.append("<td>");
        if (this.formaPagamento != null) {
            sb.append(" ").append(formaPagamento.getLabel());
        } else {
            sb.append(" Não definido ");
        }
        sb.append("</td>");

       sb.append("<td>");
       sb.append("</td>");

        return sb.toString();
    }

    public Parcela clonar(Lancamento lancamento) {
        
        Parcela parcela = new Parcela(lancamento);
        
        parcela.setDataPagamento(dataPagamento);
        parcela.setDataVencimento(dataVencimento);
        parcela.setFormaPagamento(formaPagamento);
        parcela.setNumeroParcela(numeroParcela);
        parcela.setPago(pago);
        parcela.setValorCobrado(valorCobrado);
        parcela.setValorPago(valorPago);
        
        return parcela;
    }

}
