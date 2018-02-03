/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author hoffmann
 */
public class DateUtils {

    public static final String ddMMyyyy = "dd/MM/yyyy";
    public static final String ddMMyyyy_HHmm = "dd/MM/yyyy HH:mm";
    public static final String HHmm = "HH:mm";

    public static String formatDate(Date data, String pattern) {

        if (data != null) {

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            return sdf.format(data);
        }

        return "";
    }

    public static Date parseDate(String data, String pattern) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.parse(data);
    }

    public static boolean isFimDeSemana(Date data) {
        GregorianCalendar gcDiaAgenda = new GregorianCalendar();
        gcDiaAgenda.setTime(data);
        return (gcDiaAgenda.get(Calendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY
                || gcDiaAgenda.get(Calendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY);
    }

    public static Date getDateAt(Date date, int day) {
        if (day <= 0) {
            return date;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.DATE, day);
            return c.getTime();
        }
    }

    public static Date getDateAt(int day, int month, int year) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DATE, day);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.YEAR, year);
        return date.getTime();
    }

    public static Date getDateAndTime(Date date, int h, int m, int s) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, h);
        c.set(Calendar.MINUTE, m);
        c.set(Calendar.SECOND, s);
        return c.getTime();
    }

    public static int getCurrentYear() {
        return getYear(new Date());
    }

    public static Integer getYear(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static Date addDays(Date currentDate, int qtdDias){
    
        // convert date to localdatetime
        LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // plus one
        localDateTime = localDateTime.plusDays(qtdDias);
        
        // convert LocalDateTime to date
        return  Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    public static Date addMonths(Date currentDate, int qtdMeses){
    
        // convert date to localdatetime
        LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // plus one
        localDateTime = localDateTime.plusMonths(qtdMeses);
        
        // convert LocalDateTime to date
        return  Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    

}
