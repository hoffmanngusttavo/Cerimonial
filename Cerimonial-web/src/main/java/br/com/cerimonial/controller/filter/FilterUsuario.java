/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.filter;

import br.com.cerimonial.controller.AbstractFilter;

/**
 *
 * @author Gustavo Hoffmann
 */
public class FilterUsuario extends AbstractFilter{

    private String nome;

    public FilterUsuario() {
        paths.put("nome", "nome");
    }
    
    @Override
    public void limparFiltros() {
        nome = null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
    
}
