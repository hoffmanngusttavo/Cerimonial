/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.utils;

import br.com.cerimonial.entity.Cidade;
import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.enums.TipoPessoa;
import br.com.cerimonial.service.CidadeService;
import br.com.cerimonial.service.EstadoService;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Gustavo Hoffmann
 */
public class SelectItemUtils {
    
    
    private final EstadoService estadoService = lookupEstadoServiceBean();

    private final CidadeService cidadeService = lookupCidadeServiceBean();

    
    public static List<SelectItem> getComboTipoPessoa() {
        List<SelectItem> items = new LinkedList<>();
        for (TipoPessoa item : TipoPessoa.getList()) {
            items.add(new SelectItem(item, item.getLabel()));
        }
        return items;
    }
    
    public  List<SelectItem> getComboCidade() {
        List<SelectItem> items = new LinkedList<>();
        try {
            for (Cidade item : cidadeService.findAll()) {
                items.add(new SelectItem(item, item.getNome()));
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }
    
    public List<SelectItem> getComboEstado() {
        List<SelectItem> items = new LinkedList<>();
        try {
            for (Estado item : estadoService.findAll()) {
                items.add(new SelectItem(item, item.getSigla()));
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    private CidadeService lookupCidadeServiceBean() {
        try {
            Context c = new InitialContext();
            return (CidadeService) c.lookup("java:global/"+ConstantsProject.appName+"/"+ConstantsProject.moduleEjbName+"/CidadeService!br.com.cerimonial.service.CidadeService");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private EstadoService lookupEstadoServiceBean() {
        try {
            Context c = new InitialContext();
            return (EstadoService) c.lookup("java:global/"+ConstantsProject.appName+"/"+ConstantsProject.moduleEjbName+"/EstadoService!br.com.cerimonial.service.EstadoService");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
