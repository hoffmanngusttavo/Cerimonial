/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerimonial.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;

/**
 *
 * @author hoffmann
 */
public class DateUtils {

    public static final String ddMMyyyy = "dd/MM/yyyy";
    public static final String ddMMyyyy_HHmm = "dd/MM/yyyy HH:mm";
    public static final String HHmm = "HH:mm";
    
    //en_US formats
    public static final String format_yyyy_MM_dd = "yyyy-MM-dd";
    public static final String format_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    public static final String format_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String format_yyyy_MM_dd_T_HH_mm_ss = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String format_HH_mm = "HH:mm";
    public static final String format_HH = "HH";
    public static final String format_mm = "mm";
    public static final String format_yyyy = "yyyy";
    public static final String format_MM = "MM";
    public static final String format_yyyyMM = "yyyyMM";
    public static final String format_yyyyMMdd = "yyyyMMdd";

    //pt_BR formats
    public static final String format_dd_ptBR = "dd";
    public static final String format_dd_MM_ptBR = "dd/MM";
    public static final String format_MM_yyyy_ptBR = "MM/yyyy";
    public static final String format_dd_MM_yyyy_HH_mm_ptBR = "dd/MM/yyyy HH:mm";
    public static final String format_dd_MM_yyyy_HH_mm_ss_ptBR = "dd/MM/yyyy HH:mm:ss";
    public static final String format_dd_MM_yyyy_ptBR = "dd/MM/yyyy";
    public static final String format_dd_MM_yyyy_underline_ptBR = "dd_MM_yyyy";

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

    public static Date addDays(Date currentDate, int qtdDias) {

        // convert date to localdatetime
        LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // plus one
        localDateTime = localDateTime.plusDays(qtdDias);

        // convert LocalDateTime to date
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date addMonths(Date currentDate, int qtdMeses) {

        if(currentDate == null){
            return null;
        }
        
        // convert date to localdatetime
        LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // plus one
        localDateTime = localDateTime.plusMonths(qtdMeses);

        // convert LocalDateTime to date
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static int getDaysOfInterval(Date currentDate, Date endDate) {

        if (currentDate != null && endDate != null) {

            return Days.daysBetween(new LocalDate(currentDate), new LocalDate(endDate)).getDays();
        }

        return 0;
    }
    
    
    //
    
    
    public static SimpleDateFormat getSimpleDateFormat(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat;
    }

    

    public static Date parseDateWithTimeZoneServer(String data, String format, String database) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
            return simpleDateFormat.parse(data);
            
        } catch (ParseException ex) {
          
            Logger.getLogger(DateUtils.class.getSimpleName()).log(Level.SEVERE, null, ex);
            
        }
        return null;
    }

    public static boolean isSabado(Date data) {
        if (data == null) {
            return false;
        }
        GregorianCalendar greg = new GregorianCalendar();
        greg.setTime(data);
        return greg.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY;
    }

    public static String getDiaDaSemana(Date data) {

        if (data == null) {
            return "";
        }

        GregorianCalendar gc = new GregorianCalendar();
        gc.setFirstDayOfWeek(GregorianCalendar.SUNDAY);
        gc.setTime(data);
        String texto = "";
        switch (gc.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                texto += "DOM";
                return texto;
            case 2:
                texto += "SEG";
                return texto;
            case 3:
                texto += "TER";
                return texto;
            case 4:
                texto += "QUA";
                return texto;
            case 5:
                texto += "QUI";
                return texto;
            case 6:
                texto += "SEX";
                return texto;
            case 7:
                texto += "SAB";
                return texto;
        }
        return texto;
    }


    /*
    * Com calendar os meses vão de 0 - 11
     */
    public static int getMonth(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }

    public static String getMonthName(Date date) {
        switch (getMonth(date) + 1) {
            case 1:
                return "JANEIRO";
            case 2:
                return "FEVEREIRO";
            case 3:
                return "MARÇO";
            case 4:
                return "ABRIL";
            case 5:
                return "MAIO";
            case 6:
                return "JUNHO";
            case 7:
                return "JULHO";
            case 8:
                return "AGOSTO";
            case 9:
                return "SETEMBRO";
            case 10:
                return "OUTUBRO";
            case 11:
                return "NOVEMBRO";
            case 12:
                return "DEZEMBRO";
            default:
                return "";
        }
    }

    public static Integer getDayFromDate(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DATE);
    }

    public static int getPreviousYear() {
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -1);
        return prevYear.get(Calendar.YEAR);
    }

    public static Date getDateInYear(Date date, int year) {

        if (date == null) {
            return null;
        }

        if (year < 0) {
            return date;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.YEAR, year);
        return c.getTime();
    }

    public static Date getDateInMonth(Date date, int month) {

        if (date == null) {
            return null;
        }

        if (month <= 0 || month > 12) {
            return date;
        }

        month -= 1;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, month);
        return c.getTime();
    }

    public static int getWeek(Date dia) {

        if (dia == null) {
            return 0;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dia);

        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public static int getDayOfWeek(Date dia) {

        if (dia == null) {
            return 0;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dia);

        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String getDay(Date dia) {

        if (dia == null) {
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dia);
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return "DOMINGO";
            case 2:
                return "SEGUNDA";
            case 3:
                return "TERÇA";
            case 4:
                return "QUARTA";
            case 5:
                return "QUINTA";
            case 6:
                return "SEXTA";
            case 7:
                return "SÁBADO";
            default:
                return "";
        }
    }

    /**
     * @param date the actual date
     * @param n the n-1 next days.
     * @return return a list of n next days including the actual date as the
     * first date in the list
     */
    public static List<Date> getNextDays(Date date, int n) {

        if (date == null) {
            return null;
        } else if (n <= 0) {
            return Arrays.asList(date);
        }

        List<Date> dates = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            dates.add(getDateAfterDay(date, i));
        }
        return dates;
    }

    public static Date getDateAfterMinute(Date date, int numMinutes) {
        if (date == null) {
            return date;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MINUTE, numMinutes);
            return c.getTime();
        }
    }

    public static Date getDateAfterHour(Date date, int numHours) {
        if (date == null) {
            return date;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.HOUR, numHours);
            return c.getTime();
        }
    }

    public static Date getDateAfterDay(Date date, int numDays) {
        if (date == null) {
            return date;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, numDays);
            return c.getTime();
        }
    }

    public static Date getDateAfterWeek(Date date, int numSemanas) {
        if (date == null) {
            return date;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.WEEK_OF_YEAR, numSemanas);
            return c.getTime();
        }
    }

    public static Date getDateAfterMonth(Date date, int numMeses) {
        if (date == null) {
            return date;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, numMeses);
            return c.getTime();
        }
    }

    public static Date getDateAfterYears(Date date, int numAnos) {
        if (date == null) {
            return date;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.YEAR, numAnos);
            return c.getTime();
        }
    }

    public static Date getDateAndTime(Date date, Date hoursMinutes) throws ParseException {
        if (date != null && hoursMinutes != null) {

            Date dateHourMinute;

            String dateFormatted = formatDate(date, format_dd_MM_yyyy_ptBR);
            String hoursMinutesFormatted = formatDate(hoursMinutes, format_HH_mm);

            String dateHourMinuteFormatted = dateFormatted + " " + hoursMinutesFormatted;

            dateHourMinute = parseDate(dateHourMinuteFormatted, format_dd_MM_yyyy_HH_mm_ptBR);

            return dateHourMinute;
        }
        return null;
    }

    public static Date getBeginningOfDay(Date date) {
        return getDateAndTime(date, 0, 0, 0);
    }

    public static Date getEndOfDay(Date date) {
        return getDateAndTime(date, 23, 59, 59);
    }

    public static int getCurrentMonth() {
        return getMonth(new Date());
    }

    public static Date getInitialDateInPeriod(Date date, int period) {
        if (date != null) {
            switch (period) {
                case Calendar.DAY_OF_MONTH:
                    return getDateAndTime(new Date(), 0, 0, 0);
                case Calendar.WEEK_OF_MONTH:
                    return getInitialDateInWeek(date);
                case Calendar.MONTH:
                    return getInitialDateInMonth(date);
                case Calendar.YEAR:
                    return getDateAt(1, Calendar.JANUARY, getCurrentYear());
                default:
                    return null;
            }
        }
        return null;
    }

    public static Date getFinalDateInPeriod(Date date, int period) {
        if (date != null) {
            switch (period) {
                case Calendar.DAY_OF_MONTH:
                    return getDateAndTime(new Date(), 23, 59, 59);
                case Calendar.WEEK_OF_MONTH:
                    return getFinalDateInWeek(date);
                case Calendar.MONTH:
                    return getFinalDateInMonth(date);
                case Calendar.YEAR:
                    return getDateAt(31, Calendar.DECEMBER, getYear(date));
                default:
                    return null;
            }
        }
        return null;
    }

    public static Date getInitialDateInMonth(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        if (date == null) {
            calendar.setTime(new Date());
        } else {
            calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getFinalDateInMonth(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        if (date == null) {
            calendar.setTime(new Date());
        } else {
            calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getInitialDateInWeek(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        if (date == null) {
            calendar.setTime(new Date());
        } else {
            calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
        return calendar.getTime();
    }

    public static Date getFinalDateInWeek(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        if (date == null) {
            calendar.setTime(new Date());
        } else {
            calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
        return calendar.getTime();
    }

    public static String formatDate(String date, String pattern) throws ParseException {
        DateFormat formatter = DateUtils.getSimpleDateFormat(pattern);
        Date dateFormated = formatter.parse(date);
        return formatDate(dateFormated, pattern);
    }

    public static Date parseDate(Date date, String pattern) throws ParseException {
        String dateFormatted = formatDate(date, pattern);
        return parseDate(dateFormatted, pattern);
    }

    public static int getHour(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    public static int getSecond(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }

    public static boolean isValidDate(String value, String pattern) {
        try {
            return parseDate(value, pattern) != null;
        } catch (ParseException ex) {
            return false;
        }
    }

    public static boolean isBetweenTwoDates(Date start, Date current, Date end) {
        if (start != null && end != null && current != null) {
            if (start.equals(end)) {
                return start.equals(current);
            } else if (start.before(end)) {
                return (start.before(current) || start.equals(current)) && (end.after(current) || end.equals(current));
            } else {
                return (end.before(current) || end.equals(current)) && (start.after(current) || start.equals(current));
            }
        }
        return false;
    }

    public static boolean isTimeBetweenTwoTime(Date start, Date end, Date current) {

        //Start Time
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(start);
        //Current Time
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(current);
        //Stop Time
        Calendar StopTime = Calendar.getInstance();
        StopTime.setTime(end);

        int i = startTime.get(Calendar.HOUR_OF_DAY) * 60 + startTime.get(Calendar.MINUTE);
        int e = StopTime.get(Calendar.HOUR_OF_DAY) * 60 + StopTime.get(Calendar.MINUTE);
        int x = currentTime.get(Calendar.HOUR_OF_DAY) * 60 + currentTime.get(Calendar.MINUTE);

        int mins = 24 * 60;

        if (i <= mins && e <= mins && i <= e) {
            return i <= x && x <= e;
        } else if (i <= mins && e <= mins && e < i) {
            return e <= x && x <= i;
        } else {
            return false;
        }
    }

    public static Date getWeekBefore(Date data) {
        if (data != null) {
            Calendar inicio = Calendar.getInstance();
            inicio.setTime(data);
            inicio.add(Calendar.DAY_OF_MONTH, -7);
            return inicio.getTime();
        }
        return null;
    }

    public static Date addMinutes(Date date, int minutes) {

        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);

        return calendar.getTime();
    }

    public static Date addHours(Date date, int hours) {

        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);

        return calendar.getTime();
    }

    public static Date addDays(Date dataBase, int quantidadeDeUnidadesCorridas, int calendarIndex) {
        Calendar inicio = Calendar.getInstance();

        if (dataBase != null) {

            inicio.setTime(dataBase);
        }

        inicio.add(calendarIndex, quantidadeDeUnidadesCorridas);

        return inicio.getTime();
    }

    public static boolean isConflictedPeriods(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
        if (startDate1 != null && endDate1 != null && startDate2 != null && endDate2 != null) {
            return isBetweenTwoDates(startDate1, startDate2, endDate1) || isBetweenTwoDates(startDate1, endDate2, endDate1);
        } else if ((startDate1 == null && endDate1 == null) || (startDate2 == null && endDate2 == null)) {
            return true;
        } else if (startDate1 != null && endDate1 == null) {
            return endDate2 == null || !endDate2.before(startDate1);
        } else if (startDate1 == null && endDate1 != null) {
            return startDate2 == null || !startDate2.after(endDate1);
        } else if (startDate2 != null && endDate2 == null) {
            return endDate1 == null || !endDate1.before(startDate2);
        }else if (startDate2 == null && endDate2 != null) {
            return startDate1 == null || !startDate1.after(endDate1);
        } else {
            return false;
        }
    }

    public static boolean isBeforeOrEquals(Date date1, Date date2) {
        return date1 != null && date2 != null && (date1.before(date2) || date1.equals(date2));
    }

    public static boolean isAfterOrEquals(Date date1, Date date2) {
        return date1 != null && date2 != null && (date1.before(date2) || date1.equals(date2));
    }

    public static boolean isHourBetweenTwoHours(Date startHour, Date endHour, Date givenHour) {
        if (startHour != null && endHour != null && givenHour != null) {
            int startSeconds = getHour(startHour) * 3600 + getMinute(startHour) * 60 + getSecond(startHour);
            int endSeconds = getHour(endHour) * 3600 + getMinute(endHour) * 60 + getSecond(endHour);
            int currentSeconds = getHour(givenHour) * 3600 + getMinute(givenHour) * 60 + getSecond(givenHour);

            if (startSeconds < endSeconds) {
                return startSeconds <= currentSeconds && currentSeconds <= endSeconds;
            } else if (endSeconds < startSeconds) {
                return !(endSeconds < currentSeconds && currentSeconds < startSeconds);
            }
        }
        return false;
    }

    /**
     * This method only considers hours from dates, independently of which date
     * is passed
     */
    public static int getTotalSecondsBetweenTwoHours(Date startHour, Date endHour) {
        if (startHour != null && endHour != null) {
            int startSeconds = getHour(startHour) * 3600 + getMinute(startHour) * 60 + getSecond(startHour);
            int endSeconds = getHour(endHour) * 3600 + getMinute(endHour) * 60 + getSecond(endHour);

            if (startSeconds < endSeconds) {
                return endSeconds - startSeconds;
            } else if (endSeconds < startSeconds) {
                return (24 * 3600 - startSeconds) + endSeconds;
            }
        }
        return 0;
    }

    public static int getHoursFromDates(Date dataInicio, Date dataFim) {
        if (dataInicio == null || dataFim == null) {
            return 0;
        }
        return (int) ((dataFim.getTime() - dataInicio.getTime()) / (1000 * 60 * 60));
    }

    public static int getDaysFromDates(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {

            return (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
        }

        return 0;
    }

    /**
     *
     * @param currentDay
     * @param startHour
     * @param endHour
     * @param breakTime
     * @param startInterval
     * @param endInterval
     * @param hourTimes
     * @return Lista de horarios entre o horario inicial e o horario final,
     * ignorando o intervalo inicial e intervalo final
     */
    public static List<Date> getHoursFromTwoHoursWithInterval(Date currentDay, Date startHour, Date endHour, int breakTime, Date startInterval, Date endInterval, int hourTimes) {
        if (startHour != null && endHour != null && breakTime > 0 && hourTimes > 0) {
            List<Date> result = new ArrayList<>();

            if (startInterval != null && endInterval != null) {
                List<Date> partialResult = getHoursFromTwoHoursWithInterval(currentDay, startHour, startInterval, breakTime, hourTimes, false);

                if (CollectionUtils.isNotBlank(partialResult)) {
                    result.addAll(partialResult);
                }
                partialResult = getHoursFromTwoHoursWithInterval(currentDay, endInterval, endHour, breakTime, hourTimes, true);

                if (CollectionUtils.isNotBlank(partialResult)) {
                    result.addAll(partialResult);
                }
                return result;
            } else {
                return getHoursFromTwoHoursWithInterval(currentDay, startHour, endHour, breakTime, hourTimes, true);
            }
        }
        return null;
    }

    public static List<Date> getHoursFromTwoHoursWithInterval(Date currentDate, Date startHour, Date endHour, int breakTime, int hourTimes, boolean addLastHour) {
        if (startHour != null && endHour != null && breakTime > 0 && hourTimes > 0) {
            currentDate = currentDate != null ? currentDate : new Date();
            int startMinutes = getHour(startHour) * 60 + getMinute(startHour);
            int endMinutes = getHour(endHour) * 60 + getMinute(endHour);
            
            if(startMinutes <= endMinutes) {
                
                List<Date> result = new ArrayList<>();
                
                for(int m = startMinutes; addLastHour ? m <= endMinutes : m < endMinutes; m += breakTime) {
                    Date date = getDateAndTime(currentDate, m/60, m%60, 0);
                    
                    if(date != null) {
                        result.add(date);
                    }
                }
                return result;
            } else {
                return null;
            }
        }
        return null;
    }

    public static String getHourMinFromMinutes(int totalMinutos) {
        int totalMinutes = totalMinutos;
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return String.format("%d:%02d", hours, minutes);
    }

   
    public static Period getPeriodFromDate(Date dataNascimento) {
        return getPeriodFromDate(dataNascimento, null);
    }

    public static Period getPeriodFromDate(Date dataNascimento, Date dataFim) {

        if (dataNascimento == null) {
            dataNascimento = new Date();
        }

        if (dataFim == null) {
            dataFim = new Date();
        }

        DateTime start = new DateTime(dataNascimento);
        DateTime end = new DateTime(dataFim);
        return new Period(start, end);
    }

    /**
     * This method is not applied to timestamp comparation
     *
     * @param startDate.
     * @param endDate
     * @param numberOfDates: the size of the returned list
     * @return
     */
    public static List<Date> getDatesBetween(Date startDate, Date endDate, int numberOfDates) {
        if (startDate != null && endDate != null && numberOfDates > 1 && !startDate.equals(endDate)) {
            boolean asc = startDate.before(endDate);

            Date start = asc ? startDate : endDate;
            Date end = asc ? endDate : startDate;

            int totalDays = getDaysFromDates(start, end);

            if (totalDays >= numberOfDates) {
                List<Date> dates = new ArrayList<>(numberOfDates);
                dates.add(start);
                int interval = totalDays / (numberOfDates - 1);
                int rest = totalDays % (numberOfDates - 1);

                for (int days = interval; days < totalDays - rest; days += interval) {
                    dates.add(getDateAfterDay(start, days));
                }
                dates.add(end);

                if (asc) {
                    return dates;
                } else {
                    Collections.reverse(dates);
                    return dates;
                }
            }
        }
        return null;
    }

}
