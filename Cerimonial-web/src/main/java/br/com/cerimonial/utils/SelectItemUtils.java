/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.utils;

import br.com.cerimonial.entity.CategoriaFornecedor;
import br.com.cerimonial.entity.Cidade;
import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.entity.StatusContato;
import br.com.cerimonial.entity.TipoEvento;
import br.com.cerimonial.enums.TipoPessoa;
import br.com.cerimonial.service.CategoriaFornecedorService;
import br.com.cerimonial.service.CidadeService;
import br.com.cerimonial.service.EstadoService;
import br.com.cerimonial.service.ModeloPropostaService;
import br.com.cerimonial.service.StatusContatoService;
import br.com.cerimonial.service.TipoEventoService;
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
    

    private final StatusContatoService statusContatoService = lookupStatusContatoServiceBean();
    
    private final TipoEventoService tipoEventoService = lookupTipoEventoServiceBean();

    private final EstadoService estadoService = lookupEstadoServiceBean();

    private final CidadeService cidadeService = lookupCidadeServiceBean();

    private final CategoriaFornecedorService categoriaFornecedorService = lookupCategoriaFornecedorServiceBean();
    
    private final ModeloPropostaService modeloPropostaService = lookupModeloPropostaServiceBean();
    
    
    

    public static List<SelectItem> getComboTipoPessoa() {
        List<SelectItem> items = new LinkedList<>();
        for (TipoPessoa item : TipoPessoa.getList()) {
            items.add(new SelectItem(item, item.getLabel()));
        }
        return items;
    }

    public List<SelectItem> getComboCidade(Estado estado) {
        List<SelectItem> items = new LinkedList<>();
        try {
            for (Cidade item : cidadeService.findByEstado(estado)) {
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

    public List<SelectItem> getComboCategoriasFornecedor() {
        List<SelectItem> items = new LinkedList<>();
        try {
            for (CategoriaFornecedor item : categoriaFornecedorService.findAll()) {
                items.add(new SelectItem(item, item.getNome()));
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }
    
    public List<SelectItem> getComboTipoEvento() {
        List<SelectItem> items = new LinkedList<>();
        try {
            for (TipoEvento item : tipoEventoService.findAll()) {
                items.add(new SelectItem(item, item.getNome()));
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }
    
    public List<SelectItem> getComboModelosProposta() {
        List<SelectItem> items = new LinkedList<>();
        try {
            for (ModeloProposta item : modeloPropostaService.findAll()) {
                items.add(new SelectItem(item, item.getTitulo()));
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }
    
    public List<SelectItem> getComboStatusContato() {
        List<SelectItem> items = new LinkedList<>();
        try {
            for (StatusContato item : statusContatoService.findAll()) {
                items.add(new SelectItem(item, item.getNome()));
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    private CidadeService lookupCidadeServiceBean() {
        try {
            Context c = new InitialContext();
            return (CidadeService) c.lookup("java:global/" + ConstantsProject.appName + "/" + ConstantsProject.moduleEjbName + "/CidadeService!br.com.cerimonial.service.CidadeService");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private EstadoService lookupEstadoServiceBean() {
        try {
            Context c = new InitialContext();
            return (EstadoService) c.lookup("java:global/" + ConstantsProject.appName + "/" + ConstantsProject.moduleEjbName + "/EstadoService!br.com.cerimonial.service.EstadoService");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CategoriaFornecedorService lookupCategoriaFornecedorServiceBean() {
        try {
            Context c = new InitialContext();
            return (CategoriaFornecedorService) c.lookup("java:global/" + ConstantsProject.appName + "/" + ConstantsProject.moduleEjbName + "/CategoriaFornecedorService!br.com.cerimonial.service.CategoriaFornecedorService");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private TipoEventoService lookupTipoEventoServiceBean() {
        try {
            Context c = new InitialContext();
            return (TipoEventoService) c.lookup("java:global/" + ConstantsProject.appName + "/" + ConstantsProject.moduleEjbName + "/TipoEventoService!br.com.cerimonial.service.TipoEventoService");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private StatusContatoService lookupStatusContatoServiceBean() {
        try {
            Context c = new InitialContext();
            return (StatusContatoService) c.lookup("java:global/" + ConstantsProject.appName + "/" + ConstantsProject.moduleEjbName + "/StatusContatoService!br.com.cerimonial.service.StatusContatoService");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ModeloPropostaService lookupModeloPropostaServiceBean() {
        try {
            Context c = new InitialContext();
            return (ModeloPropostaService) c.lookup("java:global/" + ConstantsProject.appName + "/" + ConstantsProject.moduleEjbName + "/ModeloPropostaService!br.com.cerimonial.service.ModeloPropostaService");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    

}
