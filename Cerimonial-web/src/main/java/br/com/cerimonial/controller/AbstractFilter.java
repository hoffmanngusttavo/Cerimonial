/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;

/**
 *
 * @author Gustavo Hoffmann
 */
public abstract class AbstractFilter implements Serializable {

    protected HashMap<String, String> paths;
    protected boolean exibirFiltroAvancado;
    protected boolean exibirFiltro = false;

    public AbstractFilter() {
        paths = new HashMap<String, String>();
        exibirFiltro = false;

    }

    public void exibirEsconderFiltroAvancado(){
        if(exibirFiltroAvancado){
            exibirFiltroAvancado = false;
        }else{
            exibirFiltroAvancado = true;
        }
    }
    
    public abstract void limparFiltros();
    
    public abstract void filtrar();

    public static AbstractFilter getFilter(Class classe) {
        String sessionID = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).getId();
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + sessionID + "_" + classe.getSimpleName());
        try {
            if (file.exists()) {
                return (AbstractFilter) SerializationUtils.deserialize(FileUtils.readFileToByteArray(file));
            }
        } catch (IOException ex) {
            Logger.getLogger(AbstractFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            return (AbstractFilter) classe.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractFilter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public void saveFilter() {
        String sessionID = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).getId();
        try {
            FileUtils.writeByteArrayToFile(new File(System.getProperty("java.io.tmpdir") + "/" + sessionID + "_" + this.getClass().getSimpleName()), SerializationUtils.serialize(this));
        } catch (IOException ex) {
            Logger.getLogger(AbstractFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public HashMap<String, Object> getFilter()  {
        saveFilter();
        
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        for (Field f : this.getClass().getDeclaredFields()) {
            if (!f.getName().equalsIgnoreCase("paths")) {
                if (paths.containsKey(f.getName())) {
                    String campo = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);

                    String methodName = (f.getType().equals(boolean.class) ? "is" : "get") + campo;

                    if (methodName.equalsIgnoreCase("get_methods_") || methodName.equalsIgnoreCase("get_filter_signature")) {
                        continue;
                    }

                    try {
                        Method declaredMethod = null;
                        declaredMethod = this.getClass().getDeclaredMethod(methodName, null);
                        Object ret = declaredMethod.invoke(this, null);
                        //
                        if (ret != null && (((ret instanceof String && !((String) ret).isEmpty()) || !(ret instanceof String)))
                                && ((ret instanceof List && !((List) ret).isEmpty()) || !(ret instanceof List))) {
                            retorno.put(paths.get(f.getName()), ret);
                        }
                    } catch (Exception e) {
                        Logger.getLogger(AbstractFilter.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
        }

        return retorno;
    }

    public HashMap<String, String> getPaths() {
        return paths;
    }

    public void setPaths(HashMap<String, String> paths) {
        this.paths = paths;
    }

    public boolean isExibirFiltroAvancado() {
        return exibirFiltroAvancado;
    }

    public void setExibirFiltroAvancado(boolean exibirFiltroAvancado) {
        this.exibirFiltroAvancado = exibirFiltroAvancado;
    }

    public boolean isExibirFiltro() {
        return exibirFiltro;
    }

    public void setExibirFiltro(boolean exibirFiltro) {
        this.exibirFiltro = exibirFiltro;
    }
    
    
    
    
}
