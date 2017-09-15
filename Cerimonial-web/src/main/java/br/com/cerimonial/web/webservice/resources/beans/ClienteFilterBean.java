/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.web.webservice.resources.beans;

import javax.ws.rs.QueryParam;

/**
 *
 * @author hoffmann
 */
public class ClienteFilterBean {

    private @QueryParam("offset") int offset;
    
    private @QueryParam("limit") int limit;
    
    private @QueryParam("name") String name;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    
}
