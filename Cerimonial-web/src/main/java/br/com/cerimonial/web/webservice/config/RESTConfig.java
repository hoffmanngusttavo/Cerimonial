/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.web.webservice.config;

import br.com.cerimonial.web.webservice.resources.ClienteResource;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

/**
 *
 * @author hoffmann
 */
@Path("webapi")
public class RESTConfig extends Application{
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        
        classes.add(ClienteResource.class);

        return classes;
    }
    
}
