package com.q1.math;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTime
{
  public static String getCurrentDate()
  {
    Date dt = Calendar.getInstance().getTime();
    SimpleDateFormat tf = new SimpleDateFormat("dd-MMM-yyyy");
    return tf.format(dt).toString();
  }
  

  public static Integer getCurrentDateInt()
  {
    Date dt = Calendar.getInstance().getTime();
    SimpleDateFormat tf = new SimpleDateFormat("yyyyMMdd");
    return Integer.valueOf(Integer.parseInt(tf.format(dt)));
  }
  

  public static Long getCurrentDateLong()
  {
    Date dt = Calendar.getInstance().getTime();
    SimpleDateFormat tf = new SimpleDateFormat("yyyyMMdd");
    return Long.valueOf(Long.parseLong(tf.format(dt).toString()));
  }
  
  public static long getDateTimeDiff(Date dT1, Date dT2)
  {
    long diffInMillies = dT2.getTime() - dT1.getTime();
    return TimeUnit.MILLISECONDS.toMinutes(diffInMillies);
  }
  
  public static long getDateDiff(Date dT1, Date dT2) {
    long diffInMillies = dT2.getTime() - dT1.getTime();
    return TimeUnit.MILLISECONDS.toDays(diffInMillies);
  }
  
  public static String getCurrentTime()
  {
    Date time = Calendar.getInstance().getTime();
    SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
    return tf.format(time).toString();
  }
  

  public static String getCurrentDateTime()
  {
    Date time = Calendar.getInstance().getTime();
    SimpleDateFormat tf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    return tf.format(time).toString();
  }
  

  public static String getCurrentTimestamp()
  {
    Date time = Calendar.getInstance().getTime();
    SimpleDateFormat tf = new SimpleDateFormat("yyyyMMddHHmmss");
    return tf.format(time).toString();
  }
  

  public static String getCurrentDateTimeNoColons()
  {
    Date time = Calendar.getInstance().getTime();
    SimpleDateFormat tf = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss");
    return tf.format(time).toString();
  }
  
  public static boolean isGreater(String time1, String time2)
  {
    int hh1 = Integer.parseInt(time1.substring(0, 2));
    int mm1 = Integer.parseInt(time1.substring(3, 5));
    int ss1 = Integer.parseInt(time1.substring(6, 8));
    int hh2 = Integer.parseInt(time2.substring(0, 2));
    int mm2 = Integer.parseInt(time2.substring(3, 5));
    int ss2 = Integer.parseInt(time2.substring(6, 8));
    
    if (hh1 > hh2)
      return true;
    if (hh2 > hh1) {
      return false;
    }
    
    if (mm1 > mm2)
      return true;
    if (mm2 > mm1) {
      return false;
    }
    
    if (ss1 > ss2)
      return true;
    if (ss2 > ss1) {
      return false;
    }
    return false;
  }
  


  public static int getTimeDifference(String time1, String time2)
  {
    int hh1 = Integer.parseInt(time1.substring(0, 2));
    int mm1 = Integer.parseInt(time1.substring(3, 5));
    int hh2 = Integer.parseInt(time2.substring(0, 2));
    int mm2 = Integer.parseInt(time2.substring(3, 5));
    
    return (hh2 - hh1) * 60 + (mm2 - mm1);
  }
  
  public static Integer getTimeDifference(Integer time1, Integer time2)
  {
    Integer hh1 = Integer.valueOf(time1.intValue() / 10000);
    Integer mm1 = Integer.valueOf(time1.intValue() / 100 - hh1.intValue() * 100);
    Integer hh2 = Integer.valueOf(time2.intValue() / 10000);
    Integer mm2 = Integer.valueOf(time2.intValue() / 100 - hh2.intValue() * 100);
    return Integer.valueOf((hh2.intValue() - hh1.intValue()) * 60 + (mm2.intValue() - mm1.intValue()));
  }
  
  public static Integer addMins(Integer time, Integer mins)
  {
    Integer hh = Integer.valueOf(time.intValue() / 10000);
    Integer mm = Integer.valueOf(time.intValue() / 100 - hh.intValue() * 100);
    Integer outMM = Integer.valueOf(mm.intValue() + mins.intValue());
    mm = Integer.valueOf(outMM.intValue() % 60);
    if (mm.intValue() < 0) {
      mm = Integer.valueOf(mm.intValue() + 60);
      hh = Integer.valueOf(hh.intValue() - 1);
    }
    Integer outHH = Integer.valueOf(outMM.intValue() / 60);
    hh = Integer.valueOf((hh.intValue() + outHH.intValue()) % 24);
    if (hh.intValue() < 0) {
      hh = Integer.valueOf(hh.intValue() + 24);
    }
    return Integer.valueOf(hh.intValue() * 10000 + mm.intValue() * 100);
  }
  
  public static Integer addMins(Long time, Integer mins)
  {
    Integer hh = Integer.valueOf(time.intValue() / 10000);
    Integer mm = Integer.valueOf(time.intValue() / 100 - hh.intValue() * 100);
    Integer outMM = Integer.valueOf(mm.intValue() + mins.intValue());
    mm = Integer.valueOf(outMM.intValue() % 60);
    if (mm.intValue() < 0) {
      mm = Integer.valueOf(mm.intValue() + 60);
      hh = Integer.valueOf(hh.intValue() - 1);
    }
    Integer outHH = Integer.valueOf(outMM.intValue() / 60);
    hh = Integer.valueOf((hh.intValue() + outHH.intValue()) % 24);
    if (hh.intValue() < 0) {
      hh = Integer.valueOf(hh.intValue() + 24);
    }
    return Integer.valueOf(hh.intValue() * 10000 + mm.intValue() * 100);
  }
  
  public static String addMins(String time, int mins)
  {
    Calendar now = Calendar.getInstance();
    int hh_in = Integer.parseInt(time.substring(0, 2));
    int mm_in = Integer.parseInt(time.substring(3, 5));
    int hh_out = hh_in + mins / 60;
    int mm_out = mm_in + mins % 60;
    now.add(12, 64906);
    String time_out;
    String time_out; if (hh_out < 10) { String time_out;
      if (mm_out < 10) {
        time_out = "0" + hh_out + ":" + "0" + mm_out + ":00";
      } else
        time_out = "0" + hh_out + ":" + mm_out + ":00";
    } else { String time_out;
      if (mm_out < 10) {
        time_out = hh_out + ":" + "0" + mm_out + ":00";
      } else
        time_out = hh_out + ":" + mm_out + ":00";
    }
    return time_out;
  }
  
  public static int numberOfDays(String fromDate, String toDate) throws Exception {
    Calendar cal1 = new java.util.GregorianCalendar();
    Calendar cal2 = new java.util.GregorianCalendar();
    fromDate = new SimpleDateFormat("dd-MM-yy").format(new SimpleDateFormat("dd-MMM-yy").parse(fromDate));
    toDate = new SimpleDateFormat("dd-MM-yy").format(new SimpleDateFormat("dd-MMM-yy").parse(toDate));
    
    StringBuffer sBuffer = new StringBuffer(fromDate);
    String yearFrom = sBuffer.substring(6, 8);
    String monFrom = sBuffer.substring(3, 5);
    String ddFrom = sBuffer.substring(0, 2);
    int intYearFrom = Integer.parseInt(yearFrom);
    int intMonFrom = Integer.parseInt(monFrom);
    int intDdFrom = Integer.parseInt(ddFrom);
    

    cal1.set(intYearFrom, intMonFrom, intDdFrom);
    

    StringBuffer sBuffer1 = new StringBuffer(toDate);
    String yearTo = sBuffer1.substring(6, 8);
    String monTo = sBuffer1.substring(3, 5);
    String ddTo = sBuffer1.substring(0, 2);
    int intYearTo = Integer.parseInt(yearTo);
    int intMonTo = Integer.parseInt(monTo);
    int intDdTo = Integer.parseInt(ddTo);
    

    cal2.set(intYearTo, intMonTo, intDdTo);
    

    int days = daysBetween(cal1.getTime(), cal2.getTime());
    return days;
  }
  
  public static int daysBetween(Date d1, Date d2)
  {
    return (int)((d2.getTime() - d1.getTime()) / 86400000L);
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/math/DateTime.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */