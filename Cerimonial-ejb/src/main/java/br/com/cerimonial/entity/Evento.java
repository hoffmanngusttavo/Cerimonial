/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.enums.CategoriaEvento;
import br.com.cerimonial.enums.TipoEnvolvido;
import br.com.cerimonial.enums.TipoEnvolvidoEvento;
import br.com.cerimonial.utils.CerimonialUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.validation.constraints.Min;
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
public class Evento implements Serializable, ModelInterface {

    @Id
    @GeneratedValue(generator = "GENERATE_Evento", strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "GENERATE_Evento", sequenceName = "Evento_pk_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    @NotNull
    @Size(min = 2, max = 255)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario modificadoPor;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInicio;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataTermino;

    @Temporal(javax.persistence.TemporalType.TIME)
    private Date horaInicio;

    @Temporal(javax.persistence.TemporalType.TIME)
    private Date horaTermino;

    @Column
    @Min(1)
    private Integer quantidadeConvidados;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pessoa contratante;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrcamentoEvento orcamentoEvento;

    @NotNull(message = "O tipo de evento não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoEvento tipoEvento;

    @Column(columnDefinition = "boolean default true")
    private boolean ativo = true;

    @Column(columnDefinition = "boolean default false")
    private boolean cancelado = false;

    @Column(columnDefinition = "boolean default false")
    private boolean finalizado = false;

    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY)
    private List<ContratoEvento> contratos;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private CerimoniaEvento cerimoniaEvento;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private FestaCerimonia festaCerimonia;

    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY)
    private List<EnvolvidoEvento> envolvidos;

    @Column(columnDefinition = "TEXT")
    private String observacaoEnvolvidos;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Pessoa getContratante() {
        return contratante;
    }

    public void setContratante(Pessoa contratante) {
        this.contratante = contratante;
    }

    public OrcamentoEvento getOrcamentoEvento() {
        return orcamentoEvento;
    }

    public void setOrcamentoEvento(OrcamentoEvento orcamentoEvento) {
        this.orcamentoEvento = orcamentoEvento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
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

    public Date getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(Date horaTermino) {
        this.horaTermino = horaTermino;
    }

    public FestaCerimonia getFestaCerimonia() {
        return festaCerimonia;
    }

    public void setFestaCerimonia(FestaCerimonia festaCerimonia) {
        this.festaCerimonia = festaCerimonia;
    }

    public ContratoEvento getContrato() {

        if (CerimonialUtils.isListNotBlank(contratos)) {
            return contratos.get(0);
        }

        return null;
    }

    public void setContratos(ContratoEvento contrato) {

        if (contrato == null) {
            contratos = null;
        } else {

            if (CerimonialUtils.isListNotBlank(contratos)) {
                contratos.set(0, contrato);
            }

            if (contratos == null) {
                contratos = new ArrayList<>();
            }

            contratos.add(contrato);
        }
    }

    public CerimoniaEvento getCerimoniaEvento() {
        return cerimoniaEvento;
    }

    public void setCerimoniaEvento(CerimoniaEvento cerimoniaEvento) {
        this.cerimoniaEvento = cerimoniaEvento;
    }

    public List<EnvolvidoEvento> getEnvolvidos() {
        return envolvidos;
    }

    public void setEnvolvidos(List<EnvolvidoEvento> envolvidos) {
        this.envolvidos = envolvidos;
    }

    public String getObservacaoEnvolvidos() {
        return observacaoEnvolvidos;
    }

    public void setObservacaoEnvolvidos(String observacaoEnvolvidos) {
        this.observacaoEnvolvidos = observacaoEnvolvidos;
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
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.id);
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
        final Evento other = (Evento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Evento{" + "id=" + id + '}';
    }

    public EnvolvidoEvento getTipoEnvolvidoEvento(TipoEnvolvidoEvento tipo) {

        if (tipo != null) {

            switch (tipo) {
                case NOIVO:
                    return getNoivo();

                case NOIVA:
                    return getNoiva();

                default:
                    return getNoivo();
            }
        }

        return null;

    }

    public EnvolvidoEvento getNoivo() {

        if (CerimonialUtils.isListNotBlank(envolvidos)) {

            for (EnvolvidoEvento env : envolvidos) {
                if (env != null && env.getTipoEnvolvidoEvento().equals(TipoEnvolvidoEvento.NOIVO)) {
                    return env;
                }
            }

        }

        return null;

    }

    public EnvolvidoEvento getNoiva() {

        for (EnvolvidoEvento env : envolvidos) {
            if (env != null && env.getTipoEnvolvidoEvento().equals(TipoEnvolvidoEvento.NOIVA)) {
                return env;
            }
        }

        return null;

    }
    
    
    public boolean isEventoCasamento(){
        
        if(tipoEvento != null && tipoEvento.getCategoria() != null){
            return tipoEvento.getCategoria().equals(CategoriaEvento.CASAMENTO);
        }
        
        return false;
    }
    
    public boolean isEventoBodas(){
        
        if(tipoEvento != null && tipoEvento.getCategoria() != null){
            return tipoEvento.getCategoria().equals(CategoriaEvento.BODAS);
        }
        
        return false;
    }

}
