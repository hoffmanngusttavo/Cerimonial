/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller;

import br.com.cerimonial.entity.Usuario;

/**
 *
 * @author Gustavo Hoffmann
 */
public interface AutenticacaoInterface {
    
    public String retornoLogin();

    public String retornoLogout();

    public Usuario consultaLembrarSenha(String login);

    public void enviarSenha(Usuario usuario);
    
}
