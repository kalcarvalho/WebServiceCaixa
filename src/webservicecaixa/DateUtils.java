/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservicecaixa;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author kalcarvalho
 */
public class DateUtils {

    private static DateUtils instance = null;
    private static DateFormat format = null;
    private static SimpleDateFormat simple;
    private static final String FORMAT_DEFAULT = "yyyyMMddHHmmss";

    protected DateUtils() {
        Calendar c = Calendar.getInstance();
        format = DateFormat.getDateInstance();
        format.setCalendar(c);
        simple = new SimpleDateFormat(FORMAT_DEFAULT, new Locale("pt", "BR"));
    }

    protected static DateUtils getInstance() {
        if (instance == null) {
            instance = new DateUtils();
        }
        return instance;
    }

    public static Date parse(String date) throws ParseException {
        DateUtils.getInstance();
        return format.parse(date);
    }

    public static String format(Date date) {
        DateUtils.getInstance();
        return simple.format(date);
    }

    public static String format(String date, String f) throws ParseException {
        DateUtils.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(f, new Locale("pt", "BR"));
        Date d = parse(date);
        return s.format(d);
    }
    
    public static String getCurrentTime(String f) {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat(f);
        return s.format(d);
    }
    
    public static String parseDate(String d, String f0, String f1) throws ParseException {
        DateFormat df = new SimpleDateFormat(f0);
        Date date = df.parse(d);
        
        SimpleDateFormat s = new SimpleDateFormat(f1);
        return s.format(date);
        
    }
    
    public static Date parseDate(String d, String f) throws ParseException {
        DateFormat df = new SimpleDateFormat(f);
        return df.parse(d);
    }

    public static String format(String date) throws ParseException {
        DateUtils.getInstance();
        Date d = parse(date);
        return simple.format(d);
    }

    public static String getTime() {
        DateUtils.getInstance();
        return simple.format(Calendar.getInstance().getTime());
    }

    public static String parseDateToMySQL(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DEFAULT);
            Date parsedDate = dateFormat.parse(date);
            String timestamp = new java.sql.Timestamp(parsedDate.getTime()).toString();
            return timestamp;
        } catch (Exception e) {//this generic but you can control another types of exception
            e.printStackTrace();
            //look the origin of excption 
        }
        return null;
    }

    public static XMLGregorianCalendar parseDateToXMLGregorianCalendar(Date date) throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }

}
