/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service.utils;

import br.com.cerimonial.entity.Empresa;
import br.com.cerimonial.service.EmpresaService;
import java.io.Serializable;

/**
 *
 * @author hoffmann
 */
public class EmpresaCache implements Serializable {

    public static Empresa getEmpresa() throws Exception {

        ServiceLookupUtil lookupUtil = new ServiceLookupUtil();
        EmpresaService service = lookupUtil.lookupService(EmpresaService.class);

        return service.getEmpresa();
    }

}
