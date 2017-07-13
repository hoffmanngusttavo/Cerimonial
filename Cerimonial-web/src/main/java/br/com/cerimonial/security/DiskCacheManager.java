/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.security;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.MapCache;
import org.apache.shiro.util.SoftHashMap;

/**
 *
 * @author Gustavo Hoffmann
 */
public class DiskCacheManager extends AbstractCacheManager{

    @Override
    protected Cache createCache(String name) throws CacheException {
        return new MapCache<Object, Object>(name, new SoftHashMap<Object, Object>());
    }
    
}
