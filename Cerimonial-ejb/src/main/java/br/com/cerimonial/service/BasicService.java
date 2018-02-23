/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.service;

import br.com.cerimonial.entity.ModelInterface;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

/**
 *
 * @author Gustavo Hoffmann
 * @param <T>
 */
public abstract class BasicService<T> implements Serializable {

    @PersistenceContext(unitName = "CerimonialPU")
    protected EntityManager em;

    public abstract T findEntityById(Long id) throws Exception;

    public abstract T save(T entity) throws Exception;

    public abstract void validateObjectNull(T entity);

    public abstract void validateObjectAndIdNull(T entity);

    public abstract void validateId(Long idEntity);

    public T smartLazy(T obj, List<String> parametros) {
        
        if (obj != null && parametros != null) {

            for (String parametro : parametros) {
                try {
                    if (!parametro.contains(".")) {
                        Field f = obj.getClass().getDeclaredField(parametro);
                        if (f != null) {
                            String campo = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                            Object atributo = obj.getClass().getDeclaredMethod("get" + campo).invoke(obj);

                            if (atributo != null) {

                                // isso é necessário para evitar erros ao acessar campos de objects em Proxy do Hibernate...
                                atributo = unproxy(obj, parametro, atributo);

//                                atributo = this.unproxy(atributo);
                                if (atributo instanceof List) {
                                    ((List) atributo).size();
                                    unproxyList(obj, parametro, (List) atributo);
                                } else if (atributo instanceof Set) {
                                    ((Set) atributo).size();
                                    unproxySet(obj, parametro, (Set) atributo);
                                    set(obj, parametro, atributo);
                                } else {
                                    ((ModelInterface) atributo).getId();
                                }
                            }

                        }
                    } else {
                        String[] campos = parametro.split("\\.");

                        Field f = obj.getClass().getDeclaredField(campos[0]);

                        String campo = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                        Object atributo = obj.getClass().getDeclaredMethod("get" + campo).invoke(obj);

                        List<String> p = new ArrayList();
                        String c = "";
                        if (campos.length > 1) {
                            for (int i = 1; i < campos.length; i++) {
                                if (i > 1) {
                                    c += ".";
                                }
                                c += campos[i];
                            }
                        }

                        if (c != null) {
                            p.add(c);
                        }

                        if (atributo != null) {

                            // isso é necessário para evitar erros ao acessar campos de objects em Proxy do Hibernate...
                            atributo = unproxy(obj, parametro, atributo);

                            if (atributo instanceof List) {
                                for (Object o : ((List) atributo)) {
                                    smartLazy((T) o, p);
                                }
                            } else if (atributo instanceof Set) {
                                for (Object o : ((Set) atributo)) {
                                    smartLazy((T) o, p);
                                }
                            } else {
                                smartLazy(((T) atributo), p);
                            }
                        }

                    }
                } catch (NoSuchMethodException | SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | java.lang.ClassCastException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return obj;
    }

    private boolean set(Object object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, fieldValue);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    protected <V extends Object> V unproxy(V proxied) {

        V entity = proxied;
        if (entity != null && entity instanceof HibernateProxy) {

            Hibernate.initialize(entity);
            entity = (V) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
        }
        return entity;
    }

    protected <V extends Object> V unproxy(Object obj, String parametro, V proxied) {

        V entity = proxied;
        if (entity != null && entity instanceof HibernateProxy) {
            Hibernate.initialize(entity);
            entity = (V) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
            set(obj, parametro, entity);
            //return entity;
        }

        return entity;
    }

    public <V> void unproxyList(Object obj, String parametro, Collection<V> objects) {
        List<V> result = new ArrayList<V>();
        boolean proxy = false;
        for (V entity : objects) {
            if (entity instanceof HibernateProxy) {
                proxy = true;
            }
            entity = unproxy(entity);
            result.add((V) entity);

        }

        if (proxy) {
            set(obj, parametro, result);
        }
    }

    public <V> void unproxySet(Object obj, String parametro, Set<V> objects) {
        Set<V> result = new HashSet<V>();
        boolean proxy = false;
        for (V entity : objects) {
            if (entity instanceof HibernateProxy) {
                proxy = true;
            }
            entity = unproxy(entity);
            result.add((V) entity);
        }

        if (proxy) {
            set(obj, parametro, result);
        }
    }

}
