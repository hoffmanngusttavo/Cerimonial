/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller.mb;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Gustavo Hoffmann
 */
@ManagedBean(name = "LayoutMB")
@SessionScoped
public class LayoutMB implements Serializable{
    
    private String layout;

    @PostConstruct
    public void init() {
        setDefaultLayout();
    }

    public String getLayout() {
        return layout;
    }

    public void setHorizontalLayout() {
        layout = "/WEB-INF/admin/layout/default.xhtml";
    }

    public void setDefaultLayout() {
        layout = "/WEB-INF/admin/layout/default.xhtml";
    }
    
}
