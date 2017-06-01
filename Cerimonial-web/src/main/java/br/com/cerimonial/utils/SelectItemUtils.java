/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.utils;

import br.com.cerimonial.enums.TipoPessoa;
import java.util.LinkedList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author Gustavo Hoffmann
 */
public class SelectItemUtils {

    public static List<SelectItem> getComboTipoPessoa() {
        List<SelectItem> items = new LinkedList<>();
        for (TipoPessoa item : TipoPessoa.getList()) {
            items.add(new SelectItem(item, item.getLabel()));
        }
        return items;
    }

}
