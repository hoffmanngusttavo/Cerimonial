/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import br.com.cerimonial.controller.BasicControl;
import br.com.cerimonial.entity.Login;
import br.com.cerimonial.service.LoginService;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "DashBoardMB")
@ViewScoped
public class DashBoardMB extends BasicControl{
    
    @EJB
    protected LoginService loginService;
    
    private List<Login> ultimosLogins;

    public DashBoardMB() {
        
    }

    public void carregarLogins(){
        ultimosLogins = loginService.findRangeListagem(null, 4, 0, null, null);
    }
    
    
    
    
    //----------GETTERS AND SETTERS--------------------------------
    
    
    public List<Login> getUltimosLogins() {
        return ultimosLogins;
    }

    public void setUltimosLogins(List<Login> ultimosLogins) {
        this.ultimosLogins = ultimosLogins;
    }
    
    
    
    
}
