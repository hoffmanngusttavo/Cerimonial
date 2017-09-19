/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.utils;

import br.com.cerimonial.entity.CategoriaFornecedor;
import br.com.cerimonial.entity.Cidade;
import br.com.cerimonial.entity.Estado;
import br.com.cerimonial.entity.ModeloContrato;
import br.com.cerimonial.entity.ModeloProposta;
import br.com.cerimonial.entity.StatusContato;
import br.com.cerimonial.entity.TipoEvento;
import br.com.cerimonial.enums.CategoriaEvento;
import br.com.cerimonial.enums.TipoEmail;
import br.com.cerimonial.enums.TipoPessoa;
import br.com.cerimonial.service.CategoriaFornecedorService;
import br.com.cerimonial.service.CidadeService;
import br.com.cerimonial.service.EstadoService;
import br.com.cerimonial.service.ModeloContratoService;
import br.com.cerimonial.service.ModeloPropostaService;
import br.com.cerimonial.service.StatusContatoService;
import br.com.cerimonial.service.TipoEventoService;
import br.com.cerimonial.service.utils.ServiceLookupUtil;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;

/**
 *
 * @author Gustavo Hoffmann
 */
public class SelectItemUtils {

    public static List<SelectItem> getComboTipoPessoa() {
        List<SelectItem> items = new LinkedList<>();
        TipoPessoa.getList().stream().forEach((item) -> {
            items.add(new SelectItem(item, item.getLabel()));
        });
        return items;
    }

    public static List<SelectItem> getComboCategoriaEvento() {
        List<SelectItem> items = new LinkedList<>();
        CategoriaEvento.getList().stream().forEach((item) -> {
            items.add(new SelectItem(item, item.getLabel()));
        });
        return items;
    }

    public static List<SelectItem> getComboTipoEmail() {
        List<SelectItem> items = new LinkedList<>();
        TipoEmail.getList().stream().forEach((item) -> {
            items.add(new SelectItem(item, item.getLabel()));
        });
        return items;
    }

    public List<SelectItem> getComboCidade(Estado estado) {
        List<SelectItem> items = new LinkedList<>();

        ServiceLookupUtil lookupUtil = new ServiceLookupUtil();
        CidadeService service = lookupUtil.lookupService(CidadeService.class);

        try {
            for (Cidade item : service.findByEstado(estado)) {
                items.add(new SelectItem(item, item.getNome()));
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    public List<SelectItem> getComboEstado() {
        List<SelectItem> items = new LinkedList<>();

        ServiceLookupUtil lookupUtil = new ServiceLookupUtil();
        EstadoService service = lookupUtil.lookupService(EstadoService.class);

        try {
            for (Estado item : service.findAll()) {
                items.add(new SelectItem(item, item.getSigla()));
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    public List<SelectItem> getComboCategoriasFornecedor() {
        List<SelectItem> items = new LinkedList<>();

        ServiceLookupUtil lookupUtil = new ServiceLookupUtil();
        CategoriaFornecedorService service = lookupUtil.lookupService(CategoriaFornecedorService.class);

        try {
            for (CategoriaFornecedor item : service.findAll()) {
                items.add(new SelectItem(item, item.getNome()));
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    public List<SelectItem> getComboTipoEvento() {
        List<SelectItem> items = new LinkedList<>();
        ServiceLookupUtil lookupUtil = new ServiceLookupUtil();
        TipoEventoService service = lookupUtil.lookupService(TipoEventoService.class);

        try {
            for (TipoEvento item : service.findAll()) {
                items.add(new SelectItem(item, item.getNome()));
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    public List<SelectItem> getComboModeloContratoByTipoEvento(TipoEvento tipoEvento) {
        List<SelectItem> items = new LinkedList<>();
        ServiceLookupUtil lookupUtil = new ServiceLookupUtil();
        ModeloContratoService service = lookupUtil.lookupService(ModeloContratoService.class);

        try {
            service.findModelosContratoByTipoEvento(tipoEvento).stream().forEach((item) -> {
                items.add(new SelectItem(item, item.getTitulo()));
            });
            
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    public List<SelectItem> getComboModelosProposta() {
        List<SelectItem> items = new LinkedList<>();

        ServiceLookupUtil lookupUtil = new ServiceLookupUtil();
        ModeloPropostaService service = lookupUtil.lookupService(ModeloPropostaService.class);

        try {
            for (ModeloProposta item : service.findAll()) {
                items.add(new SelectItem(item, item.getTitulo()));
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    public List<SelectItem> getComboStatusContato() {
        List<SelectItem> items = new LinkedList<>();

        ServiceLookupUtil lookupUtil = new ServiceLookupUtil();
        StatusContatoService service = lookupUtil.lookupService(StatusContatoService.class);

        try {
            for (StatusContato item : service.findAll()) {
                items.add(new SelectItem(item, item.getNome()));
            }
        } catch (Exception ex) {
            Logger.getLogger(SelectItemUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

}
