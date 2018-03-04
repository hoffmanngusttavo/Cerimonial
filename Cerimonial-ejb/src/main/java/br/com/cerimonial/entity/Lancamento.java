/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.enums.TipoLancamento;
import br.com.cerimonial.exceptions.ErrorCode;
import br.com.cerimonial.exceptions.GenericException;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.validation.constraints.NotNull;
import org.apache.shiro.SecurityUtils;
import org.hibernate.envers.Audited;

/**
 *
 * @author hoffmann
 */
@Entity
@Audited
public class Lancamento implements Serializable, ModelInterface {

    @Id
    @GeneratedValue(generator = "GENERATE_Lancamento", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_Lancamento", sequenceName = "Lancamento_pk_seq", allocationSize = 1)
    private Long id;

    @NotNull(message = "O tipo não pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoLancamento tipoLancamento = TipoLancamento.DESPESA;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataCadastro;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataVencimento;

    @DecimalMin("0.0")
    @Column(precision = 16, scale = 2)
    private Double valorBase;

    @DecimalMin("0.0")
    @Column(precision = 16, scale = 2)
    private Double valorEstimado;

    @DecimalMin("0.0")
    @Column(precision = 16, scale = 2)
    private Double valorTotalPago;

    @Column
    private int numeroParcelas = 1;

    @ManyToOne
    private Pessoa envolvidoOrigem;

    @ManyToOne
    private Pessoa envolvidoDestino;

    @ManyToOne
    private Servico servico;

    @OneToMany(mappedBy = "lancamento", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Parcela> parcelas;

    @OneToMany(mappedBy = "lancamento", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<OrcamentoLancamento> orcamentos;

    @ManyToOne
    private CustoEvento custoEvento;

    @OneToOne
    private ServicoPrestadoEvento servicoPrestadoEvento;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;

    
    @OneToOne
    private AtividadeEvento atividadeEvento;

    public Lancamento() {
        this.dataCadastro = new Date();
        this.dataVencimento = new Date();
    }

    public Lancamento(TipoLancamento tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
        this.dataCadastro = new Date();
        this.dataVencimento = new Date();
    }

    public Lancamento(AtividadeEvento atividadeEvento) {
        this.tipoLancamento = TipoLancamento.DESPESA;
        this.dataCadastro = new Date();
        this.atividadeEvento = atividadeEvento;
        this.servico = atividadeEvento.getServico();

    }

    
    
    
    
    public ServicoPrestadoEvento getServicoPrestadoEvento() {
        return servicoPrestadoEvento;
    }

    public void setServicoPrestadoEvento(ServicoPrestadoEvento servicoPrestadoEvento) {
        this.servicoPrestadoEvento = servicoPrestadoEvento;
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

    public TipoLancamento getTipoLancamento() {
        return tipoLancamento;
    }

    public void setTipoLancamento(TipoLancamento tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }

    public Double getValorBase() {
        return valorBase;
    }

    public void setValorBase(Double valorBase) {
        this.valorBase = valorBase;
    }

    public Double getValorTotalPago() {
        return valorTotalPago;
    }

    public void setValorTotalPago(Double valorTotalPago) {
        this.valorTotalPago = valorTotalPago;
    }

    public Pessoa getEnvolvidoOrigem() {
        return envolvidoOrigem;
    }

    public void setEnvolvidoOrigem(Pessoa envolvidoOrigem) {
        this.envolvidoOrigem = envolvidoOrigem;
    }

    public Pessoa getEnvolvidoDestino() {
        return envolvidoDestino;
    }

    public void setEnvolvidoDestino(Pessoa envolvidoDestino) {
        this.envolvidoDestino = envolvidoDestino;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }

    public CustoEvento getCustoEvento() {
        return custoEvento;
    }

    public void setCustoEvento(CustoEvento custoEvento) {
        this.custoEvento = custoEvento;
    }

    public int getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(int numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public AtividadeEvento getAtividadeEvento() {
        return atividadeEvento;
    }

    public void setAtividadeEvento(AtividadeEvento atividadeEvento) {
        this.atividadeEvento = atividadeEvento;
    }

    public Double getValorEstimado() {
        return valorEstimado;
    }

    public void setValorEstimado(Double valorEstimado) {
        this.valorEstimado = valorEstimado;
    }

    public List<OrcamentoLancamento> getOrcamentos() {
        return orcamentos;
    }

    public void calcularParcelas() {

        this.valorTotalPago = 0D;

        if (CollectionUtils.isNotBlank(parcelas)) {

            for (Parcela parcela : parcelas) {

                this.valorTotalPago += parcela.getValorPago() != null ? parcela.getValorPago() : 0D;

            }
        }

    }
    
   

    public void adicionarParcela(Parcela parcela) {

        if (parcela == null) {
            throw new GenericException("Parcela nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (parcelas == null) {
            parcelas = new ArrayList<>();
        }

        parcela.setLancamento(this);

        parcelas.add(parcela);

    }

    public void adicionarOrcamento(OrcamentoLancamento orcamento) {

        if (orcamento == null) {
            throw new GenericException("Orçamento nulo.", ErrorCode.BAD_REQUEST.getCode());
        }

        if (orcamentos == null) {
            orcamentos = new ArrayList<>();
        }

        if (this.possuiFornecedorContratado()) {
            throw new GenericException("Não pode adicionar Orçamento, pois já existe um fornecedor contratado", ErrorCode.BAD_REQUEST.getCode());
        }

        orcamento.setLancamento(this);

        orcamentos.add(orcamento);

    }

    public Date getDataVencimentoUltimaParcelaPaga() {
        Date dataUltimaParcelaPaga = null;

        if (CollectionUtils.isNotBlank(parcelas)) {
            for (Parcela parcela : this.parcelas) {
                if (parcela.isPago()) {
                    if (dataUltimaParcelaPaga == null || parcela.getDataVencimento().after(dataUltimaParcelaPaga)) {
                        dataUltimaParcelaPaga = parcela.getDataVencimento();
                    }
                }
            }
        }
        return dataUltimaParcelaPaga;
    }

    public Date getDataVencimentoPrimeiraParcela() {
        Date dataPrimeiraParcela = null;

        if (CollectionUtils.isNotBlank(parcelas)) {
            for (Parcela parcela : this.parcelas) {
                if (parcela.getNumeroParcela() == 1) {
                    dataPrimeiraParcela = parcela.getDataVencimento();
                    break;
                }
            }
        }
        return dataPrimeiraParcela;
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
        if (!(object instanceof Lancamento)) {
            return false;
        }
        Lancamento other = (Lancamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.cerimonial.entity.Lancamento[ id=" + id + " ]";
    }

    public boolean possuiFornecedorContratado() {

        if (CollectionUtils.isNotBlank(orcamentos)) {

            if (orcamentos.stream().anyMatch((orcamento) -> (orcamento.isContratado()))) {
                return true;
            }

        }

        return false;

    }
    
    public boolean possuiParcelaPaga() {

        if (CollectionUtils.isNotBlank(parcelas)) {

            if (parcelas.stream().anyMatch((parcela) -> (parcela.isPago()))) {
                return true;
            }

        }

        return false;

    }
    
    public boolean possuiLancamentoGeradoServicoPrestado() {

       return servicoPrestadoEvento != null;

    }
    
    public boolean permiteRemoverLancamento() {

       return !(possuiFornecedorContratado() 
               || possuiParcelaPaga() 
               || possuiLancamentoGeradoServicoPrestado());

    }

}
