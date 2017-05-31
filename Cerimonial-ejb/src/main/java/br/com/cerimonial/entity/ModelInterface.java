/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

import java.util.Date;

/**
 *
 * @author Gustavo Hoffmann
 */
public interface ModelInterface  {
    
    public Long getId();
    public void setId(Long id);
    
    public Usuario getModificadoPor();
    public void setModificadoPor(Usuario user);
    
    public Date getDataUltimaAlteracao();
    public void setDataUltimaAlteracao(Date data);
    
    public void prePersistEntity();
    public void preUpdateEntity();
    public void preRemoveEntity();
    
}
