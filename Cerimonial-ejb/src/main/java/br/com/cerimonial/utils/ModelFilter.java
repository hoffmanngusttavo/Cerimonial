/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gustavo Hoffmann
 */
public class ModelFilter implements Serializable {

    private static ModelFilter instance;

    private String sqlBase;
    private String sqlCountBase;
    private int limit;
    private int offSet;
    private Map<String, Object> filtros;
    private Map<String, String> joins;
    private Map<String, ArrayList<String>> operadores;
    private Map<String, String> orderBy;
    private Class entidade;
    public static String ISNULL = "ISNULL";
    public static String ISNOTNULL = "ISNOTNULL";
    public static String IN = "IN";
    public static String NOTIN = "NOTIN";
    public static String OR = "OR";
    public static String AND = "AND";
    public static String ASC = "ASC";
    public static String DESC = "DESC";
    public static String LESSTHAN = "LESSTHAN";
    public static String GREATERTHAN = "GREATERTHAN";
    public static String LESSTHANOREQUAL = "LESSTHANOREQUAL";
    public static String GREATERTHANOREQUAL = "GREATERTHANOREQUAL";
    public static String DIFF = "DIFF";
    public static String BETWEEN = "BETWEEN";
    public static String NOT = "NOT";
    public static String DDMMYYYY = "dd/mm/yyyy";
    public static String LIKELEFT = "LIKELEFT";
    public static String ILIKE = "ILIKE";
    public static String LIKERIGHT = "LIKERIGHT";
    public static String EQUAL = "EQUAL";
    public static String LEFTJOIN = "LEFT JOIN";
    public static String INNERJOIN = "INNER JOIN";

    public enum Operadores {

        ISNULL,
        ISNOTNULL,
        IN,
        NOTIN,
        OR,
        AND,
        ASC,
        DESC,
        LESSTHAN,
        GREATERTHAN,
        LESSTHANOREQUAL,
        GREATERTHANOREQUAL,
        DIFF,
        BETWEEN,
        NOT,
        LIKELEFT,
        LIKERIGHT,
        EQUAL,
        ILIKE,
        NAOTEM,
        DISTANCE
    };

    public ModelFilter() {

        filtros = new HashMap<String, Object>();
        operadores = new HashMap<>();
        orderBy = new HashMap<String, String>();
        joins = new HashMap<String, String>();

    }

    public void addOrderBy(String campo, String posicao) {
        orderBy.put(campo, posicao);
    }

    public void addFilter(String campo, Object value) {
        filtros.put(campo, value);
    }

    public void addJoin(String campo, String value) {
        joins.put(campo, value);
    }

    public void addFilter(Map value) {
        filtros.putAll(value);
    }

    public void addOperador(Operadores operador, String campo) {
        ArrayList<String> campos = new ArrayList<>();
        campos.add(campo);
        operadores.put(operador.toString(), campos);
    }

    public String getSqlCountBase() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(o) FROM ").append(entidade.getSimpleName()).append(" as o ");
        
        if (joins != null && !joins.isEmpty()) {
            sb.append(getJoins());
        }

        sb.append("WHERE 1 = 1  ");
        
        if (filtros != null) {
            for (String key : filtros.keySet()) {
                sb.append(getOperadorCampo(key));
            }
        }
        return sb.toString();
    }

    public String getSqlBase() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT (o) FROM ").append(entidade.getSimpleName()).append(" as o  ");

        if (joins != null && !joins.isEmpty()) {
            sb.append(getJoins());
        }

        sb.append("WHERE 1 = 1  ");

        if (filtros != null) {
            for (String key : filtros.keySet()) {
                sb.append(getOperadorCampo(key));
            }
        }

        sb.append(" ORDER BY ");
        if (orderBy == null || orderBy.isEmpty()) {
            sb.append(" o.id DESC ");
        } else {
            for (String campo : orderBy.keySet()) {
                if (orderBy.get(campo).equalsIgnoreCase("ASC")) {
                    if (!campo.contains(".")) {
                        campo = "o." + campo;
                    }
                    sb.append(campo).append(" ASC ,  ");
                } else {
                    if (!campo.contains(".")) {
                        campo = "o." + campo;
                    }
                    sb.append(campo).append(" DESC ,  ");
                }
            }
            sb.append(" o.id ");
        }
        return sb.toString();
    }

    public String getOperadorCampo(String campo) {
        return getOperadorCampo(campo, "AND");
    }

    public String getOperadorCampo(String campo, String agregador) {
        String aliasCampo = campo;

        if (!aliasCampo.contains(".")) {
            aliasCampo = "o." + aliasCampo;
        }

        if (operadores != null) {
            if (operadores.containsKey(BETWEEN) && operadores.get(BETWEEN).contains(campo)) {
                return agregador + "  " + aliasCampo + " BETWEEN  '" + ((List) filtros.get(campo)).get(0) + "' AND '" + ((List) filtros.get(campo)).get(1) + "'";
            }
            if (operadores.containsKey(DIFF) && operadores.get(DIFF).contains(campo)) {
                return agregador + "  " + aliasCampo + " <>  '" + filtros.get(campo) + "' ";

            }
            if (operadores.containsKey(EQUAL) && operadores.get(EQUAL).contains(campo)) {
                return agregador + "  " + aliasCampo + " =  '" + filtros.get(campo) + "' ";
            }
            if (operadores.containsKey(GREATERTHAN) && operadores.get(GREATERTHAN).contains(campo)) {
                return agregador + "  " + aliasCampo + " >  '" + filtros.get(campo) + "' ";
            }
            if (operadores.containsKey(GREATERTHANOREQUAL) && operadores.get(GREATERTHANOREQUAL).contains(campo)) {
                return agregador + "  " + aliasCampo + " >=  '" + filtros.get(campo) + "' ";
            }
            if (operadores.containsKey(IN) && operadores.get(IN).contains(campo)) {
                String in = "-9999";

                for (Object o : (List) filtros.get(campo)) {
                    in += ",'" + String.valueOf(o) + "'";
                }

                return agregador + "  " + aliasCampo + " IN  (" + in + ") ";
            }

            if (operadores.containsKey(NOTIN) && operadores.get(NOTIN).contains(campo)) {
                String in = "-9999";

                for (Object o : (List) filtros.get(campo)) {
                    in += ",'" + String.valueOf(o) + "'";
                }

                return agregador + "  " + aliasCampo + " NOT IN  (" + in + ") ";
            }

            if ((operadores.containsKey(ISNOTNULL) && operadores.get(ISNOTNULL).contains(campo)) || (filtros.get(campo).toString().equalsIgnoreCase(ISNOTNULL))) {
                return agregador + "  " + aliasCampo + " IS NOT NULL";
            }
            if ((operadores.containsKey(ISNULL) && operadores.get(ISNULL).contains(campo)) || (filtros.get(campo).toString().equalsIgnoreCase(ISNULL))) {
                return agregador + "  " + aliasCampo + " IS NULL";
            }

            if (operadores.containsKey(LESSTHAN) && operadores.get(LESSTHAN).contains(campo)) {
                return agregador + "  " + aliasCampo + " <  '" + filtros.get(campo) + "' ";
            }
            if (operadores.containsKey(LESSTHANOREQUAL) && operadores.get(LESSTHANOREQUAL).contains(campo)) {
                return agregador + "  " + aliasCampo + " <=  '" + filtros.get(campo) + "' ";
            }
            if (operadores.containsKey(LIKELEFT) && operadores.get(LIKELEFT).contains(campo)) {
                return agregador + " UPPER(" + aliasCampo + ") like UPPER('%" + filtros.get(campo) + "') ";
            }
            if (operadores.containsKey(LIKERIGHT) && operadores.get(LIKERIGHT).contains(campo)) {
                return agregador + " UPPER(" + aliasCampo + ") like UPPER('" + filtros.get(campo) + "%') ";
            }
            if (operadores.containsKey(ILIKE) && operadores.get(ILIKE).contains(campo)) {
                return agregador + "  UPPER(" + aliasCampo + ") like UPPER('%" + filtros.get(campo) + "%') ";
            }
        }

        Object f = filtros.get(campo);

        if (f instanceof List) {
            List lista = (List) f;

            if (lista.size() == 2) {
                if (lista.get(0) instanceof Date || lista.get(0) instanceof Double || lista.get(0) instanceof Integer || lista.get(0) instanceof Long || lista.get(0) instanceof Float) {
                    return agregador + "  " + aliasCampo + " BETWEEN  '" + ((List) filtros.get(campo)).get(0) + "' AND '" + ((List) filtros.get(campo)).get(1) + "'";
                }
            }

            String in = "-9999";

            if (operadores != null) {
                for (Object o : (List) filtros.get(campo)) {
                    in += ",'" + String.valueOf(o) + "'";
                }
            }

            return agregador + "  " + aliasCampo + "  IN  (" + in + ") ";
        }

        return agregador + "  " + aliasCampo + " =  '" + filtros.get(campo) + "' ";
    }

    public String getJoins() {
        StringBuilder sb = new StringBuilder();
        if (joins != null) {
            for (Map.Entry<String, String> entry : joins.entrySet()) {
               sb.append(" ").append(entry.getValue()).append(" o.").append(entry.getKey()).append(" ");
            }
        }
        return sb.toString();
    }

    public static ModelFilter getInstance() {
        instance = new ModelFilter();
        return instance;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }

    public void setEntidade(Class entidade) {
        this.entidade = entidade;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffSet() {
        return offSet;
    }

}
