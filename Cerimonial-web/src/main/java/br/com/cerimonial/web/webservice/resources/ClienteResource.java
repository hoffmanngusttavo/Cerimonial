/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.web.webservice.resources;

import br.com.cerimonial.entity.Pessoa;
import br.com.cerimonial.service.PessoaService;
import br.com.cerimonial.web.webservice.resources.beans.ClienteFilterBean;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author hoffmann
 */
@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ClienteResource {

    @EJB
    protected PessoaService service;

    @GET
    public List<Pessoa> getClientes(@BeanParam ClienteFilterBean filter) {

        if ((filter.getOffset() >= 0) && (filter.getLimit() > 0)) {
            return service.findRangeListagem(null, filter.getLimit(), filter.getOffset(), null, null);
        }
        
        if (filter.getName() != null) {
            return service.findRangeListagem(null, 0, 0, null, null);
        }

        return service.findRangeListagem(null, 0, 0, null, null);
    }

    @GET
    @Path("{clienteId}")
    public Pessoa getProduto(@PathParam("clienteId") long id) throws Exception {
        return service.getEntity(id);
    }

    @POST
    public Response save(Pessoa cliente) throws Exception {
        cliente = service.createCliente(cliente);
        return Response.status(Response.Status.CREATED)
                .entity(cliente)
                .build();
    }

    @PUT
    @Path("{clienteId}")
    public void update(@PathParam("clienteId") long id, Pessoa cliente) throws Exception {
        cliente.setId(id);
        service.saveCliente(cliente);
    }

    @DELETE
    @Path("{clienteId}")
    public void delete(@PathParam("clienteId") long id) throws Exception {
        Pessoa entity = service.getEntity(id);
        service.delete(entity);
    }

}
