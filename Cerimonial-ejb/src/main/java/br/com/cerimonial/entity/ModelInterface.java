/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.entity;

/**
 *
 * @author Gustavo Hoffmann
 */
public interface ModelInterface  {
    
    public Long getId();
    public void setId(Long id);
    
    public Usuario getModificadoPor();
    public void setModificadoPor(Usuario user);
    
}
