/*     */ package com.q1.math;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ public class DateTime
/*     */ {
/*     */   public static String getCurrentDate()
/*     */   {
/*  12 */     Date dt = Calendar.getInstance().getTime();
/*  13 */     SimpleDateFormat tf = new SimpleDateFormat("dd-MMM-yyyy");
/*  14 */     return tf.format(dt).toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static Integer getCurrentDateInt()
/*     */   {
/*  20 */     Date dt = Calendar.getInstance().getTime();
/*  21 */     SimpleDateFormat tf = new SimpleDateFormat("yyyyMMdd");
/*  22 */     return Integer.valueOf(Integer.parseInt(tf.format(dt)));
/*     */   }
/*     */   
/*     */ 
/*     */   public static Long getCurrentDateLong()
/*     */   {
/*  28 */     Date dt = Calendar.getInstance().getTime();
/*  29 */     SimpleDateFormat tf = new SimpleDateFormat("yyyyMMdd");
/*  30 */     return Long.valueOf(Long.parseLong(tf.format(dt).toString()));
/*     */   }
/*     */   
/*     */   public static long getDateTimeDiff(Date dT1, Date dT2)
/*     */   {
/*  35 */     long diffInMillies = dT2.getTime() - dT1.getTime();
/*  36 */     return TimeUnit.MILLISECONDS.toMinutes(diffInMillies);
/*     */   }
/*     */   
/*     */   public static long getDateDiff(Date dT1, Date dT2) {
/*  40 */     long diffInMillies = dT2.getTime() - dT1.getTime();
/*  41 */     return TimeUnit.MILLISECONDS.toDays(diffInMillies);
/*     */   }
/*     */   
/*     */   public static String getCurrentTime()
/*     */   {
/*  46 */     Date time = Calendar.getInstance().getTime();
/*  47 */     SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
/*  48 */     return tf.format(time).toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static String getCurrentDateTime()
/*     */   {
/*  54 */     Date time = Calendar.getInstance().getTime();
/*  55 */     SimpleDateFormat tf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
/*  56 */     return tf.format(time).toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static String getCurrentTimestamp()
/*     */   {
/*  62 */     Date time = Calendar.getInstance().getTime();
/*  63 */     SimpleDateFormat tf = new SimpleDateFormat("yyyyMMddHHmmss");
/*  64 */     return tf.format(time).toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static String getCurrentDateTimeNoColons()
/*     */   {
/*  70 */     Date time = Calendar.getInstance().getTime();
/*  71 */     SimpleDateFormat tf = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss");
/*  72 */     return tf.format(time).toString();
/*     */   }
/*     */   
/*     */   public static boolean isGreater(String time1, String time2)
/*     */   {
/*  77 */     int hh1 = Integer.parseInt(time1.substring(0, 2));
/*  78 */     int mm1 = Integer.parseInt(time1.substring(3, 5));
/*  79 */     int ss1 = Integer.parseInt(time1.substring(6, 8));
/*  80 */     int hh2 = Integer.parseInt(time2.substring(0, 2));
/*  81 */     int mm2 = Integer.parseInt(time2.substring(3, 5));
/*  82 */     int ss2 = Integer.parseInt(time2.substring(6, 8));
/*     */     
/*  84 */     if (hh1 > hh2)
/*  85 */       return true;
/*  86 */     if (hh2 > hh1) {
/*  87 */       return false;
/*     */     }
/*     */     
/*  90 */     if (mm1 > mm2)
/*  91 */       return true;
/*  92 */     if (mm2 > mm1) {
/*  93 */       return false;
/*     */     }
/*     */     
/*  96 */     if (ss1 > ss2)
/*  97 */       return true;
/*  98 */     if (ss2 > ss1) {
/*  99 */       return false;
/*     */     }
/* 101 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int getTimeDifference(String time1, String time2)
/*     */   {
/* 108 */     int hh1 = Integer.parseInt(time1.substring(0, 2));
/* 109 */     int mm1 = Integer.parseInt(time1.substring(3, 5));
/* 110 */     int hh2 = Integer.parseInt(time2.substring(0, 2));
/* 111 */     int mm2 = Integer.parseInt(time2.substring(3, 5));
/*     */     
/* 113 */     return (hh2 - hh1) * 60 + (mm2 - mm1);
/*     */   }
/*     */   
/*     */   public static Integer getTimeDifference(Integer time1, Integer time2)
/*     */   {
/* 118 */     Integer hh1 = Integer.valueOf(time1.intValue() / 10000);
/* 119 */     Integer mm1 = Integer.valueOf(time1.intValue() / 100 - hh1.intValue() * 100);
/* 120 */     Integer hh2 = Integer.valueOf(time2.intValue() / 10000);
/* 121 */     Integer mm2 = Integer.valueOf(time2.intValue() / 100 - hh2.intValue() * 100);
/* 122 */     return Integer.valueOf((hh2.intValue() - hh1.intValue()) * 60 + (mm2.intValue() - mm1.intValue()));
/*     */   }
/*     */   
/*     */   public static Integer addMins(Integer time, Integer mins)
/*     */   {
/* 127 */     Integer hh = Integer.valueOf(time.intValue() / 10000);
/* 128 */     Integer mm = Integer.valueOf(time.intValue() / 100 - hh.intValue() * 100);
/* 129 */     Integer outMM = Integer.valueOf(mm.intValue() + mins.intValue());
/* 130 */     mm = Integer.valueOf(outMM.intValue() % 60);
/* 131 */     if (mm.intValue() < 0) {
/* 132 */       mm = Integer.valueOf(mm.intValue() + 60);
/* 133 */       hh = Integer.valueOf(hh.intValue() - 1);
/*     */     }
/* 135 */     Integer outHH = Integer.valueOf(outMM.intValue() / 60);
/* 136 */     hh = Integer.valueOf((hh.intValue() + outHH.intValue()) % 24);
/* 137 */     if (hh.intValue() < 0) {
/* 138 */       hh = Integer.valueOf(hh.intValue() + 24);
/*     */     }
/* 140 */     return Integer.valueOf(hh.intValue() * 10000 + mm.intValue() * 100);
/*     */   }
/*     */   
/*     */   public static Integer addMins(Long time, Integer mins)
/*     */   {
/* 145 */     Integer hh = Integer.valueOf(time.intValue() / 10000);
/* 146 */     Integer mm = Integer.valueOf(time.intValue() / 100 - hh.intValue() * 100);
/* 147 */     Integer outMM = Integer.valueOf(mm.intValue() + mins.intValue());
/* 148 */     mm = Integer.valueOf(outMM.intValue() % 60);
/* 149 */     if (mm.intValue() < 0) {
/* 150 */       mm = Integer.valueOf(mm.intValue() + 60);
/* 151 */       hh = Integer.valueOf(hh.intValue() - 1);
/*     */     }
/* 153 */     Integer outHH = Integer.valueOf(outMM.intValue() / 60);
/* 154 */     hh = Integer.valueOf((hh.intValue() + outHH.intValue()) % 24);
/* 155 */     if (hh.intValue() < 0) {
/* 156 */       hh = Integer.valueOf(hh.intValue() + 24);
/*     */     }
/* 158 */     return Integer.valueOf(hh.intValue() * 10000 + mm.intValue() * 100);
/*     */   }
/*     */   
/*     */   public static String addMins(String time, int mins)
/*     */   {
/* 163 */     Calendar now = Calendar.getInstance();
/* 164 */     int hh_in = Integer.parseInt(time.substring(0, 2));
/* 165 */     int mm_in = Integer.parseInt(time.substring(3, 5));
/* 166 */     int hh_out = hh_in + mins / 60;
/* 167 */     int mm_out = mm_in + mins % 60;
/* 168 */     now.add(12, 64906);
/*     */     String time_out;
/* 170 */     String time_out; if (hh_out < 10) { String time_out;
/* 171 */       if (mm_out < 10) {
/* 172 */         time_out = "0" + hh_out + ":" + "0" + mm_out + ":00";
/*     */       } else
/* 174 */         time_out = "0" + hh_out + ":" + mm_out + ":00";
/*     */     } else { String time_out;
/* 176 */       if (mm_out < 10) {
/* 177 */         time_out = hh_out + ":" + "0" + mm_out + ":00";
/*     */       } else
/* 179 */         time_out = hh_out + ":" + mm_out + ":00";
/*     */     }
/* 181 */     return time_out;
/*     */   }
/*     */   
/*     */   public static int numberOfDays(String fromDate, String toDate) throws Exception {
/* 185 */     Calendar cal1 = new java.util.GregorianCalendar();
/* 186 */     Calendar cal2 = new java.util.GregorianCalendar();
/* 187 */     fromDate = new SimpleDateFormat("dd-MM-yy").format(new SimpleDateFormat("dd-MMM-yy").parse(fromDate));
/* 188 */     toDate = new SimpleDateFormat("dd-MM-yy").format(new SimpleDateFormat("dd-MMM-yy").parse(toDate));
/*     */     
/* 190 */     StringBuffer sBuffer = new StringBuffer(fromDate);
/* 191 */     String yearFrom = sBuffer.substring(6, 8);
/* 192 */     String monFrom = sBuffer.substring(3, 5);
/* 193 */     String ddFrom = sBuffer.substring(0, 2);
/* 194 */     int intYearFrom = Integer.parseInt(yearFrom);
/* 195 */     int intMonFrom = Integer.parseInt(monFrom);
/* 196 */     int intDdFrom = Integer.parseInt(ddFrom);
/*     */     
/*     */ 
/* 199 */     cal1.set(intYearFrom, intMonFrom, intDdFrom);
/*     */     
/*     */ 
/* 202 */     StringBuffer sBuffer1 = new StringBuffer(toDate);
/* 203 */     String yearTo = sBuffer1.substring(6, 8);
/* 204 */     String monTo = sBuffer1.substring(3, 5);
/* 205 */     String ddTo = sBuffer1.substring(0, 2);
/* 206 */     int intYearTo = Integer.parseInt(yearTo);
/* 207 */     int intMonTo = Integer.parseInt(monTo);
/* 208 */     int intDdTo = Integer.parseInt(ddTo);
/*     */     
/*     */ 
/* 211 */     cal2.set(intYearTo, intMonTo, intDdTo);
/*     */     
/*     */ 
/* 214 */     int days = daysBetween(cal1.getTime(), cal2.getTime());
/* 215 */     return days;
/*     */   }
/*     */   
/*     */   public static int daysBetween(Date d1, Date d2)
/*     */   {
/* 220 */     return (int)((d2.getTime() - d1.getTime()) / 86400000L);
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/math/DateTime.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */