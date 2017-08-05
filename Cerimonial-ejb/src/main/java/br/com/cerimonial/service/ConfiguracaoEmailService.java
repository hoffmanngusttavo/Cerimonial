/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ConfiguracaoEmail;
import br.com.cerimonial.enums.TipoEmail;
import br.com.cerimonial.repository.ConfiguracaoEmailRepository;
import br.com.cerimonial.utils.CerimonialUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author Gustavo Hoffmann
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfiguracaoEmailService extends BasicService<ConfiguracaoEmail>{
    
    private ConfiguracaoEmailRepository repository;
    
    @PostConstruct
    @PostActivate
    private void postConstruct() {
        repository = new ConfiguracaoEmailRepository(em);
    }
    
    @Override
    public ConfiguracaoEmail getEntity(Long id) throws Exception {
        return repository.getEntity(id);
    }

    public ConfiguracaoEmail getConfiguracaoEmail() throws Exception {
        List<ConfiguracaoEmail> emails = repository.findRangeListagem(1, 0, null, null);
        if(CerimonialUtils.isListNotBlank(emails)){
            return emails.get(0);
        }
        return new ConfiguracaoEmail();
    }
    
    @Override
    public ConfiguracaoEmail save(ConfiguracaoEmail entity) throws Exception {
        if (entity != null) {
            
            if (entity.getId() == null) {
                return repository.create(entity);
            } else {
                return repository.edit(entity);
            }
        }
        return null;
    }

    public List<ConfiguracaoEmail> findAll() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            Logger.getLogger(ConfiguracaoEmailService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public void delete(ConfiguracaoEmail contato) throws Exception {
        if(contato != null){
            repository.delete(contato);
        }
    }

    public int countAll() {
        try {
            return repository.countAll();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<ConfiguracaoEmail> findRangeListagem(int max, int offset, String sortField, String sortAscDesc) {
        try {
            return repository.findRangeListagem(max, offset, sortField, sortAscDesc);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ConfiguracaoEmail alterarConfiguracaoEmail(ConfiguracaoEmail entity, TipoEmail tipoEmail) {
        
        if(entity == null){
            entity = new ConfiguracaoEmail();
        }
        
        if(tipoEmail == null){
            return getConfiguracaoServidorEmail(entity, null, null, null, null);
        }
        
        switch (tipoEmail) {
            case GMAIL:
                return getConfiguracaoServidorEmail(entity, "smtp.gmail.com", "imap.gmail.com", "465 ", "993");
            case HOTMAIL:
                return getConfiguracaoServidorEmail(entity, "smtp-mail.outlook.com", "imap-mail.outlook.com", "587", "993");
            case YAHOO:
                return getConfiguracaoServidorEmail(entity, "smtp.mail.yahoo.com", "imap.mail.yahoo.com", "465", "993");
            case OUTROS:
                return getConfiguracaoServidorEmail(entity, null, null, "465", "993");
        }
        
        return entity;
    }

    private ConfiguracaoEmail getConfiguracaoServidorEmail(ConfiguracaoEmail email, String smtp, String imap, String portaSmtp, String portaImap) {
        email.setSmtp(smtp);
        email.setPortaSmtp(portaSmtp);
        email.setImap(imap);
        email.setPortaImap(portaImap);
        return email;
    }
    
}