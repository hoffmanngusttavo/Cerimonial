/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
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
public class Usuario implements Serializable, ModelInterface {
    

    @Id
    @GeneratedValue(generator = "GENERATE_Usuario", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "GENERATE_Usuario", sequenceName = "Usuario_pk_seq", allocationSize = 1)
    private Long id;
    @Column(nullable = false)
    @NotNull
    @Size(min = 2, max = 255)
    private String nome;
    @Column(nullable = false, unique = true)
    @NotNull
    @Size(min = 4)
    private String login;
    @Column(nullable = false, unique = true)
    @NotNull
    @Size(min = 4)
    private String email;
    @Column(nullable = false)
    @NotNull
    @Size(min = 4)
    private String senha;
    @Column(nullable = false)
    @NotNull
    @Size(min = 4)
    private String salt;
    @Column(columnDefinition = "boolean default true")
    private boolean ativo = true;
    @Column(columnDefinition = "boolean default false")
    private boolean master = false;
    @ManyToOne
    private Usuario modificadoPor;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Login> logins;
    
   

    @PrePersist
    @PreUpdate
    @PreRemove
    @Override
    public void preCrudEntity() {
        try {
            this.setDataUltimaAlteracao(new Date());
            this.setModificadoPor((Usuario) SecurityUtils.getSubject().getPrincipal());
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
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
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
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
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Login> getLogins() {
        return logins;
    }

    public void setLogins(List<Login> logins) {
        this.logins = logins;
    }

    
    
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.project.rural.entity.Usuario[ id=" + id + " ]";
    }

}
