/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.utils;

import java.util.List;

/**
 *
 * @author hoffmann
 */
public class CollectionUtils {
    
    
     public static boolean isNotBlank(List list) {
        return list != null && !list.isEmpty();
    }

    public static boolean isBlank(List list) {
        return list == null || list.isEmpty();
    }
    
    
}
