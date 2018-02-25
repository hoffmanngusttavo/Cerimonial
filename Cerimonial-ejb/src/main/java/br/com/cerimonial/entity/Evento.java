/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import br.com.cerimonial.enums.AcessoSistema;
import br.com.cerimonial.enums.TipoEvento;
import br.com.cerimonial.enums.SituacaoEvento;
import br.com.cerimonial.enums.TipoEnvolvidoEvento;
import br.com.cerimonial.utils.CollectionUtils;
import br.com.cerimonial.utils.DateUtils;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

    @Column
    private String emailEvento;

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
    private OrcamentoEvento orcamentoEvento;

    @NotNull(message = "A categoria não pode ser nula")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvento tipoEvento = TipoEvento.CASAMENTO;

    @NotNull(message = "A situação não pode ser nula")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoEvento situacaoEvento = SituacaoEvento.ATIVO;

    @NotNull(message = "A situação não pode ser nula")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcessoSistema acessoSistema = AcessoSistema.NEGADO;

    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ContratoEvento> contratos;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private CerimoniaEvento cerimoniaEvento;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private FestaCerimonia festaCerimonia;

    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<EventoPessoa> contratantes;

    @Column(columnDefinition = "TEXT")
    private String observacaoEnvolvidos;

    @Column(columnDefinition = "TEXT")
    private String motivoCancelamento;

    /**
     * Se o evento é para a pessoa que entrou em contato
     */
    @Column(columnDefinition = "boolean default false")
    private boolean eventoProprioContratante = false;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<EvolucaoPreenchimento> evolucoesPreenchimento;
    
    
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private CustoEvento custoEvento;
    
    
    @OneToMany(mappedBy = "evento", cascade = {CascadeType.REMOVE})
    private List<AtividadeEvento> atividades;
    

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
        this.nome = nome != null ? nome.toUpperCase().trim() : nome;
    }

    public OrcamentoEvento getOrcamentoEvento() {
        return orcamentoEvento;
    }

    public void setOrcamentoEvento(OrcamentoEvento orcamentoEvento) {
        this.orcamentoEvento = orcamentoEvento;
    }

    public boolean isAtivo() {
        if (situacaoEvento != null) {
            return situacaoEvento.equals(SituacaoEvento.ATIVO);
        }
        return false;
    }

    public boolean isCancelado() {
        if (situacaoEvento != null) {
            return situacaoEvento.equals(SituacaoEvento.CANCELADO);
        }
        return false;
    }

    public boolean isFinalizado() {
        if (situacaoEvento != null) {
            return situacaoEvento.equals(SituacaoEvento.FINALIZADO);
        }
        return false;
    }

    public boolean isAcessoSistemaLiberado() {
        if (acessoSistema != null) {
            return acessoSistema.equals(AcessoSistema.LIBERADO);
        }
        return false;
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

    public boolean isEventoProprioContratante() {
        return eventoProprioContratante;
    }

    public void setEventoProprioContratante(boolean eventoProprioContratante) {
        this.eventoProprioContratante = eventoProprioContratante;
    }

    public ContratoEvento getContrato() {

        if (CollectionUtils.isNotBlank(contratos)) {
            return contratos.get(0);
        }

        return null;
    }

    public List<ContratoEvento> getContratos() {
        return contratos;
    }

    public List<EvolucaoPreenchimento> getEvolucoesPreenchimento() {
        return evolucoesPreenchimento;
    }

    
    
    public void setContratos(ContratoEvento contrato) {

        if (contrato == null) {
            contratos = null;
        } else {

            if (CollectionUtils.isNotBlank(contratos)) {
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

    public String getObservacaoEnvolvidos() {
        return observacaoEnvolvidos;
    }

    public void setObservacaoEnvolvidos(String observacaoEnvolvidos) {
        this.observacaoEnvolvidos = observacaoEnvolvidos;
    }

    public SituacaoEvento getSituacaoEvento() {
        return situacaoEvento;
    }

    public void setSituacaoEvento(SituacaoEvento situacaoEvento) {
        this.situacaoEvento = situacaoEvento;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    public List<EventoPessoa> getContratantes() {
        return contratantes;
    }

    public void setContratantes(List<EventoPessoa> contratantes) {
        this.contratantes = contratantes;
    }

    public String getEmailEvento() {
        return emailEvento;
    }

    public void setEmailEvento(String emailEvento) {
        this.emailEvento = emailEvento;
    }

    public AcessoSistema getAcessoSistema() {
        return acessoSistema;
    }

    public void setAcessoSistema(AcessoSistema acessoSistema) {
        this.acessoSistema = acessoSistema;
    }
    
    public EvolucaoPreenchimento getEvolucaoPreenchimento(){
        
        if(CollectionUtils.isNotBlank(evolucoesPreenchimento)){
            return evolucoesPreenchimento.get(0);
        }
        
        return null;
    }
    
    public void setEvolucaoPreenchimento(EvolucaoPreenchimento evolucaoPreenchimento) {
         
        if(evolucaoPreenchimento == null){
            evolucoesPreenchimento = null;
        }else{
            
            if(evolucoesPreenchimento == null){
                evolucoesPreenchimento = new ArrayList<>();
            }
            
            if(CollectionUtils.isNotBlank(evolucoesPreenchimento)){
              evolucoesPreenchimento.set(0, evolucaoPreenchimento);
            }else{
                evolucoesPreenchimento.add(evolucaoPreenchimento);
            }
            
        }
        
    }

    public CustoEvento getCustoEvento() {
        return custoEvento;
    }

    public void setCustoEvento(CustoEvento custoEvento) {
        this.custoEvento = custoEvento;
    }

    public List<AtividadeEvento> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<AtividadeEvento> atividades) {
        this.atividades = atividades;
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

    public int getPorcentagemPreenchimentoConcluida(){
        
        if(getEvolucaoPreenchimento() != null){
            return getEvolucaoPreenchimento().getPorcentagemConcluida();
        }
    
        return 0;
    }
    
    
    
    public EventoPessoa getTipoEnvolvidoEvento(TipoEnvolvidoEvento tipo) {

        if (tipo != null) {

            switch (tipo) {
                case NOIVO:
                    return getNoivo();

                case NOIVA:
                    return getNoiva();

                case ANIVERSARIANTE:
                    return getAniversariante();

                default:
                    return this.contratantes.get(0);
            }
        }

        return null;

    }

    public EventoPessoa getNoivo() {

        if (CollectionUtils.isNotBlank(contratantes)) {

            for (EventoPessoa env : contratantes) {
                if (env.getTipoEnvolvidoEvento() != null && env.getTipoEnvolvidoEvento().equals(TipoEnvolvidoEvento.NOIVO)) {
                    return env;
                }
            }

        }

        return null;

    }

    public EventoPessoa getAniversariante() {

        if (CollectionUtils.isNotBlank(contratantes)) {

            for (EventoPessoa env : contratantes) {
                if (env.getTipoEnvolvidoEvento() != null && env.getTipoEnvolvidoEvento().equals(TipoEnvolvidoEvento.ANIVERSARIANTE)) {
                    return env;
                }
            }

        }

        return null;

    }

    public EventoPessoa getNoiva() {

        for (EventoPessoa env : contratantes) {
            if (env.getTipoEnvolvidoEvento() != null && env.getTipoEnvolvidoEvento().equals(TipoEnvolvidoEvento.NOIVA)) {
                return env;
            }
        }

        return null;

    }
    
    public EventoPessoa getContratante() {

        for (EventoPessoa env : contratantes) {
            if (env.isContratante()) {
                return env;
            }
        }

        return null;

    }

    public Pessoa getContratanteUsuario() {

        for (EventoPessoa env : contratantes) {
            if (env.isContratante() && env.getPessoa() != null && env.getPessoa().getUsuarioCliente() != null) {
                return env.getPessoa();
            }
        }

        return null;

    }

    public boolean isCategoriaCasamento() {
        return isEventoCasamento()
                || isEventoBodas();
    }

    public boolean isCategoriaAniversario() {
        return isEventoAniversario()
                || isEventoAniversario15Anos()
                || isEventoAniversarioInfantil();
    }

    public boolean isEventoCasamento() {

        if (tipoEvento != null) {
            return tipoEvento.equals(TipoEvento.CASAMENTO);
        }

        return false;
    }

    public boolean isEventoAniversario() {

        if (tipoEvento != null) {
            return tipoEvento.equals(TipoEvento.ANIVERSARIO);
        }

        return false;
    }

    public boolean isEventoAniversario15Anos() {

        if (tipoEvento != null) {
            return tipoEvento.equals(TipoEvento.ANIVERSARIO_15_ANOS);
        }

        return false;
    }

    public boolean isEventoAniversarioInfantil() {

        if (tipoEvento != null) {
            return tipoEvento.equals(TipoEvento.ANIVERSARIO_INFANTIL);
        }

        return false;
    }

    public boolean isEventoBodas() {

        if (tipoEvento != null) {
            return tipoEvento.equals(TipoEvento.BODAS);
        }

        return false;
    }

    public String toStringDadosCompleto() {

        StringBuilder sb = new StringBuilder();

        if (this.dataInicio != null) {
            sb.append("Data: ").append(DateUtils.formatDate(this.dataInicio, DateUtils.ddMMyyyy));
        } else {
            sb.append("Data: Á definir ");
        }

        sb.append("\n ");

        if (this.horaInicio != null) {
            sb.append("Horário: ").append(DateUtils.formatDate(this.horaInicio, DateUtils.HHmm));
        } else {
            sb.append("Horário: Á definir ");
        }

        sb.append("\n ");

        if (this.cerimoniaEvento != null) {

            sb.append(this.cerimoniaEvento.toStringDadosCompleto());

        } else {

            sb.append("Local da cerimônia: Á definir ");
        }

        sb.append("\n ");

        if (this.festaCerimonia != null) {

            sb.append(this.festaCerimonia.toStringDadosCompleto());
        } else {

            sb.append("Local da recepção: Á definir ");
        }

        sb.append("\n ");

        return sb.toString();
    }

   

}
