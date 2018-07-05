/*     */ package com.q1.math;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ import org.jfree.data.time.Day;
/*     */ import org.jfree.data.time.Minute;
/*     */ import org.jfree.data.time.Month;
/*     */ import org.jfree.data.time.TimeSeries;
/*     */ import org.jfree.data.time.Year;
/*     */ 
/*     */ 
/*     */ public class MathLib
/*     */ {
/*     */   public static final double epsilon = 1.0E-13D;
/*  22 */   private static final int[] POW10 = { 1, 10, 100, 1000, 10000, 100000, 1000000 };
/*     */   
/*     */   public static String DoubleToString(double val, int precision) {
/*  25 */     StringBuilder sb = new StringBuilder();
/*  26 */     if (val < 0.0D) {
/*  27 */       sb.append('-');
/*  28 */       val = -val;
/*     */     }
/*  30 */     int exp = POW10[precision];
/*  31 */     long lval = (val * exp + 0.5D);
/*  32 */     sb.append(lval / exp).append('.');
/*  33 */     long fval = lval % exp;
/*  34 */     for (int p = precision - 1; (p > 0) && (fval < POW10[p]); p--) {
/*  35 */       sb.append('0');
/*     */     }
/*  37 */     sb.append(fval);
/*  38 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static double simpleAvg(double[] vals)
/*     */   {
/*  43 */     double cumSum = 0.0D;
/*  44 */     for (int i = 0; i < vals.length; i++) {
/*  45 */       cumSum += vals[i];
/*     */     }
/*  47 */     return cumSum / vals.length;
/*     */   }
/*     */   
/*     */   public static double simpleAvg(ArrayList<Long> vals)
/*     */   {
/*  52 */     double cumSum = 0.0D;
/*  53 */     for (Long val : vals) {
/*  54 */       cumSum += val.doubleValue();
/*     */     }
/*  56 */     return cumSum / vals.size();
/*     */   }
/*     */   
/*     */   public static double simpleAvg(Collection<Double> vals)
/*     */   {
/*  61 */     double cumSum = 0.0D;
/*  62 */     for (Double val : vals) {
/*  63 */       cumSum += val.doubleValue();
/*     */     }
/*  65 */     return cumSum / vals.size();
/*     */   }
/*     */   
/*     */   public static double simpleAvgInt(Collection<Integer> vals)
/*     */   {
/*  70 */     double cumSum = 0.0D;
/*  71 */     for (Integer val : vals) {
/*  72 */       cumSum += val.doubleValue();
/*     */     }
/*  74 */     return cumSum / vals.size();
/*     */   }
/*     */   
/*     */   public static double simpleAvgDouble(ArrayList<Double> vals)
/*     */   {
/*  79 */     double cumSum = 0.0D;
/*  80 */     for (Double val : vals) {
/*  81 */       cumSum += val.doubleValue();
/*     */     }
/*  83 */     return cumSum / vals.size();
/*     */   }
/*     */   
/*     */   public static double sumDouble(ArrayList<Double> vals)
/*     */   {
/*  88 */     double cumSum = 0.0D;
/*  89 */     for (Double val : vals) {
/*  90 */       cumSum += val.doubleValue();
/*     */     }
/*  92 */     return cumSum;
/*     */   }
/*     */   
/*     */   public static double max(double val1, double val2)
/*     */   {
/*  97 */     if (val1 > val2) {
/*  98 */       return val1;
/*     */     }
/* 100 */     return val2;
/*     */   }
/*     */   
/*     */   public static int max(int val1, int val2)
/*     */   {
/* 105 */     if (val1 > val2) {
/* 106 */       return val1;
/*     */     }
/* 108 */     return val2;
/*     */   }
/*     */   
/*     */   public static long max(long val1, long val2)
/*     */   {
/* 113 */     if (val1 > val2) {
/* 114 */       return val1;
/*     */     }
/* 116 */     return val2;
/*     */   }
/*     */   
/*     */   public static int min(int val1, int val2)
/*     */   {
/* 121 */     if (val1 < val2) {
/* 122 */       return val1;
/*     */     }
/* 124 */     return val2;
/*     */   }
/*     */   
/*     */   public static double min(double val1, double val2)
/*     */   {
/* 129 */     if (val1 < val2) {
/* 130 */       return val1;
/*     */     }
/* 132 */     return val2;
/*     */   }
/*     */   
/*     */   public static long min(long val1, long val2)
/*     */   {
/* 137 */     if (val1 < val2) {
/* 138 */       return val1;
/*     */     }
/* 140 */     return val2;
/*     */   }
/*     */   
/*     */   public static double max(double[] vals)
/*     */   {
/* 145 */     double max_val = vals[0];
/* 146 */     for (int i = 1; i < vals.length; i++) {
/* 147 */       if (vals[i] > max_val) {
/* 148 */         max_val = vals[i];
/*     */       }
/*     */     }
/* 151 */     return max_val;
/*     */   }
/*     */   
/*     */   public static double max(ArrayList<Double> vals)
/*     */   {
/* 156 */     double max_val = 0.0D;
/*     */     try {
/* 158 */       max_val = ((Double)vals.get(0)).doubleValue();
/*     */     } catch (Exception e) {
/* 160 */       return 0.0D;
/*     */     }
/* 162 */     for (int i = 1; i < vals.size(); i++) {
/* 163 */       if (((Double)vals.get(i)).doubleValue() > max_val) {
/* 164 */         max_val = ((Double)vals.get(i)).doubleValue();
/*     */       }
/*     */     }
/* 167 */     return max_val;
/*     */   }
/*     */   
/*     */   public static double max(Collection<Double> valsC)
/*     */   {
/* 172 */     ArrayList<Double> vals = new ArrayList(valsC);
/* 173 */     double max_val = ((Double)vals.get(0)).doubleValue();
/* 174 */     for (int i = 1; i < vals.size(); i++) {
/* 175 */       if (((Double)vals.get(i)).doubleValue() > max_val) {
/* 176 */         max_val = ((Double)vals.get(i)).doubleValue();
/*     */       }
/*     */     }
/* 179 */     return max_val;
/*     */   }
/*     */   
/*     */   public static Long maxLong(ArrayList<Long> vals)
/*     */   {
/* 184 */     Long max_val = Long.valueOf(0L);
/*     */     try {
/* 186 */       max_val = (Long)vals.get(0);
/*     */     } catch (Exception e) {
/* 188 */       return Long.valueOf(0L);
/*     */     }
/* 190 */     for (int i = 1; i < vals.size(); i++) {
/* 191 */       if (((Long)vals.get(i)).longValue() > max_val.longValue()) {
/* 192 */         max_val = (Long)vals.get(i);
/*     */       }
/*     */     }
/* 195 */     return max_val;
/*     */   }
/*     */   
/*     */   public static double max(TreeMap<Long, Double> vals)
/*     */   {
/* 200 */     double max_val = Double.NEGATIVE_INFINITY;
/* 201 */     for (Double curVal : vals.values()) {
/* 202 */       if (curVal.doubleValue() > max_val) {
/* 203 */         max_val = curVal.doubleValue();
/*     */       }
/*     */     }
/* 206 */     return max_val;
/*     */   }
/*     */   
/*     */   public static TreeMap<Long, Double> multiply(TreeMap<Long, Double> vals, Double multiple)
/*     */   {
/* 211 */     for (Map.Entry<Long, Double> entry : new TreeMap(vals).entrySet()) {
/* 212 */       vals.put((Long)entry.getKey(), Double.valueOf(((Double)entry.getValue()).doubleValue() * multiple.doubleValue()));
/*     */     }
/* 214 */     return vals;
/*     */   }
/*     */   
/*     */   public static int maxIndex(double[] vals)
/*     */   {
/* 219 */     double max_val = vals[0];
/* 220 */     int max_ind = 0;
/* 221 */     for (int i = 1; i < vals.length; i++) {
/* 222 */       if (vals[i] > max_val) {
/* 223 */         max_val = vals[i];
/* 224 */         max_ind = i;
/*     */       }
/*     */     }
/* 227 */     return max_ind;
/*     */   }
/*     */   
/*     */   public static int maxIndex(ArrayList<Double> vals)
/*     */   {
/* 232 */     double max_val = ((Double)vals.get(0)).doubleValue();
/* 233 */     int max_ind = 0;
/* 234 */     for (int i = 1; i < vals.size(); i++) {
/* 235 */       if (((Double)vals.get(i)).doubleValue() > max_val) {
/* 236 */         max_val = ((Double)vals.get(i)).doubleValue();
/* 237 */         max_ind = i;
/*     */       }
/*     */     }
/* 240 */     return max_ind;
/*     */   }
/*     */   
/*     */   public static double min(double[] vals)
/*     */   {
/* 245 */     double min_val = vals[0];
/* 246 */     for (int i = 1; i < vals.length; i++) {
/* 247 */       if (vals[i] < min_val) {
/* 248 */         min_val = vals[i];
/*     */       }
/*     */     }
/* 251 */     return min_val;
/*     */   }
/*     */   
/*     */   public static double min(ArrayList<Double> vals)
/*     */   {
/* 256 */     double min_val = ((Double)vals.get(0)).doubleValue();
/* 257 */     for (int i = 1; i < vals.size(); i++) {
/* 258 */       if (((Double)vals.get(i)).doubleValue() < min_val) {
/* 259 */         min_val = ((Double)vals.get(i)).doubleValue();
/*     */       }
/*     */     }
/* 262 */     return min_val;
/*     */   }
/*     */   
/*     */   public static double min(TreeMap<Long, Double> vals)
/*     */   {
/* 267 */     double min_val = Double.POSITIVE_INFINITY;
/* 268 */     for (Double curVal : vals.values()) {
/* 269 */       if (curVal.doubleValue() < min_val) {
/* 270 */         min_val = curVal.doubleValue();
/*     */       }
/*     */     }
/* 273 */     return min_val;
/*     */   }
/*     */   
/*     */   public static int minIndex(double[] vals)
/*     */   {
/* 278 */     double min_val = vals[0];
/* 279 */     int min_ind = 0;
/* 280 */     for (int i = 1; i < vals.length; i++) {
/* 281 */       if (vals[i] < min_val) {
/* 282 */         min_val = vals[i];
/* 283 */         min_ind = i;
/*     */       }
/*     */     }
/* 286 */     return min_ind;
/*     */   }
/*     */   
/*     */   public static int minIndex(ArrayList<Double> vals)
/*     */   {
/* 291 */     double min_val = ((Double)vals.get(0)).doubleValue();
/* 292 */     int min_ind = 0;
/* 293 */     for (int i = 1; i < vals.size(); i++) {
/* 294 */       if (((Double)vals.get(i)).doubleValue() < min_val) {
/* 295 */         min_val = ((Double)vals.get(i)).doubleValue();
/* 296 */         min_ind = i;
/*     */       }
/*     */     }
/* 299 */     return min_ind;
/*     */   }
/*     */   
/*     */   public static double[] ArrayList2ArrayDouble(ArrayList<Double> array)
/*     */   {
/* 304 */     double[] out_array = new double[array.size()];
/* 305 */     for (int i = 0; i < array.size(); i++) {
/* 306 */       out_array[i] = ((Double)array.get(i)).doubleValue();
/*     */     }
/* 308 */     return out_array;
/*     */   }
/*     */   
/*     */   public static double[][] ArrayListList2MatrixDouble(ArrayList<ArrayList<Double>> array) {
/* 312 */     double[][] out_array = new double[array.size()][((ArrayList)array.get(0)).size()];
/* 313 */     for (int i = 0; i < array.size(); i++) {
/* 314 */       for (int j = 0; j < ((ArrayList)array.get(i)).size(); j++) {
/* 315 */         out_array[i][j] = ((Double)((ArrayList)array.get(i)).get(j)).doubleValue();
/*     */       }
/*     */     }
/* 318 */     return out_array;
/*     */   }
/*     */   
/*     */   public static int[] ArrayList2ArrayInt(ArrayList<Integer> array)
/*     */   {
/* 323 */     int[] out_array = new int[array.size()];
/* 324 */     for (int i = 0; i < array.size(); i++) {
/* 325 */       out_array[i] = ((Integer)array.get(i)).intValue();
/*     */     }
/* 327 */     return out_array;
/*     */   }
/*     */   
/*     */   public static Date[] ArrayList2ArrayDate(ArrayList<Date> array)
/*     */   {
/* 332 */     Date[] out_array = new Date[array.size()];
/* 333 */     for (int i = 0; i < array.size(); i++) {
/* 334 */       out_array[i] = ((Date)array.get(i));
/*     */     }
/* 336 */     return out_array;
/*     */   }
/*     */   
/*     */   public static double[][] ArrayList2MatrixDouble(ArrayList<Double> array)
/*     */   {
/* 341 */     double[][] out_array = new double[1][array.size()];
/* 342 */     for (int i = 0; i < array.size(); i++) {
/* 343 */       out_array[0][i] = ((Double)array.get(i)).doubleValue();
/*     */     }
/* 345 */     return out_array;
/*     */   }
/*     */   
/*     */   public static ArrayList<Double> cumSum(ArrayList<Double> array)
/*     */   {
/* 350 */     ArrayList<Double> out_array = new ArrayList();
/* 351 */     double currentSum = 0.0D;
/* 352 */     for (int i = 0; i < array.size(); i++) {
/* 353 */       currentSum += ((Double)array.get(i)).doubleValue();
/* 354 */       out_array.add(Double.valueOf(currentSum));
/*     */     }
/* 356 */     return out_array;
/*     */   }
/*     */   
/*     */   public static TreeMap<Long, Double> cumSum(TreeMap<Long, Double> map)
/*     */   {
/* 361 */     Double currentSum = Double.valueOf(0.0D);
/* 362 */     for (Map.Entry<Long, Double> entry : map.entrySet()) {
/* 363 */       currentSum = Double.valueOf(currentSum.doubleValue() + ((Double)entry.getValue()).doubleValue());
/* 364 */       map.put((Long)entry.getKey(), currentSum);
/*     */     }
/* 366 */     return map;
/*     */   }
/*     */   
/*     */ 
/*     */   public static TimeSeries ArrayLists2Series(String seriesName, ArrayList<Date> dateArray, ArrayList<Double> array, String type)
/*     */   {
/* 372 */     TimeSeries series = new TimeSeries(seriesName);
/* 373 */     for (int i = 0; i < array.size(); i++) {
/* 374 */       if (type.equals("Minute")) {
/* 375 */         series.add(new Minute((Date)dateArray.get(i)), (Number)array.get(i));
/* 376 */       } else if (type.equals("Day")) {
/* 377 */         series.add(new Day((Date)dateArray.get(i)), (Number)array.get(i));
/*     */       }
/*     */     }
/* 380 */     return series;
/*     */   }
/*     */   
/*     */   public static TimeSeries Treemap2Series(String seriesName, TreeMap<Long, Double> tsMap, String type)
/*     */     throws ParseException
/*     */   {
/* 386 */     TimeSeries series = new TimeSeries(seriesName);
/* 387 */     for (Map.Entry<Long, Double> entry : tsMap.entrySet()) {
/* 388 */       Double val = (Double)entry.getValue();
/* 389 */       if (type.equals("Minute")) {
/* 390 */         Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(((Long)entry.getKey()).toString());
/* 391 */         series.add(new Minute(date), val);
/* 392 */       } else if (type.equals("Day")) {
/* 393 */         Date date = new SimpleDateFormat("yyyyMMdd").parse(((Long)entry.getKey()).toString());
/* 394 */         series.add(new Day(date), val);
/* 395 */       } else if (type.equals("Month")) {
/* 396 */         Date date = new SimpleDateFormat("yyyyMM").parse(((Long)entry.getKey()).toString());
/* 397 */         series.add(new Month(date), val);
/* 398 */       } else if (type.equals("Year")) {
/* 399 */         Date date = new SimpleDateFormat("yyyy").parse(((Long)entry.getKey()).toString());
/* 400 */         series.add(new Year(date), val);
/*     */       }
/*     */     }
/* 403 */     return series;
/*     */   }
/*     */   
/*     */   public static double round(double inp, int places)
/*     */   {
/* 408 */     int x = (int)Math.pow(10.0D, places);
/* 409 */     return Math.round(inp * x) / x;
/*     */   }
/*     */   
/*     */   public static double floorTick(double inp, double tick)
/*     */   {
/* 414 */     return Math.floor((inp + 1.0E-13D) / tick) * tick;
/*     */   }
/*     */   
/*     */   public static double ceilTick(double inp, double tick)
/*     */   {
/* 419 */     return Math.ceil((inp - 1.0E-13D) / tick) * tick;
/*     */   }
/*     */   
/*     */   public static double roundTick(double inp, double tick)
/*     */   {
/* 424 */     return Math.round((inp - 1.0E-13D) / tick) * tick;
/*     */   }
/*     */   
/*     */   public static double roundTickWithPrecision(double inp, double tick)
/*     */   {
/* 429 */     NumberFormat formatter = new DecimalFormat("#0.00000000");
/* 430 */     String textTick = formatter.format(inp);
/* 431 */     int integerPlaces = textTick.indexOf('.');
/* 432 */     int places = textTick.length() - integerPlaces - 1;
/* 433 */     return round(Math.round(inp / tick) * tick, places);
/*     */   }
/*     */   
/*     */   public static ArrayList<Double> createZeroDoubleArrayList(int size)
/*     */   {
/* 438 */     ArrayList<Double> outArray = new ArrayList();
/* 439 */     for (int i = 0; i < size; i++)
/* 440 */       outArray.add(Double.valueOf(0.0D));
/* 441 */     return outArray;
/*     */   }
/*     */   
/*     */   public static Integer longCompare(Long l1, Long l2)
/*     */   {
/* 446 */     if (l1.longValue() > l2.longValue())
/* 447 */       return Integer.valueOf(1);
/* 448 */     if (l1.longValue() < l2.longValue()) {
/* 449 */       return Integer.valueOf(-1);
/*     */     }
/* 451 */     return Integer.valueOf(0);
/*     */   }
/*     */   
/*     */   public static Integer intCompare(Integer l1, Integer l2)
/*     */   {
/* 456 */     if (l1.intValue() > l2.intValue())
/* 457 */       return Integer.valueOf(1);
/* 458 */     if (l1.intValue() < l2.intValue()) {
/* 459 */       return Integer.valueOf(-1);
/*     */     }
/* 461 */     return Integer.valueOf(0);
/*     */   }
/*     */   
/*     */   public static Integer doubleCompare(Double d1, Double d2)
/*     */   {
/* 466 */     if ((d1.doubleValue() >= d2.doubleValue() - 1.0E-13D) && (d1.doubleValue() <= d2.doubleValue() + 1.0E-13D))
/* 467 */       return Integer.valueOf(0);
/* 468 */     if (d1.doubleValue() > d2.doubleValue() - 1.0E-13D)
/* 469 */       return Integer.valueOf(1);
/* 470 */     if (d1.doubleValue() < d2.doubleValue() + 1.0E-13D)
/* 471 */       return Integer.valueOf(-1);
/* 472 */     return null;
/*     */   }
/*     */   
/*     */   public static ArrayList<String> split(String line, char splitChar)
/*     */   {
/* 477 */     ArrayList<String> str = new ArrayList();
/* 478 */     int index = line.indexOf(splitChar);
/* 479 */     int prevIndex = index;
/* 480 */     if (index >= 0)
/* 481 */       str.add(line.substring(0, index));
/*     */     for (;;) {
/* 483 */       index = line.indexOf(splitChar, index + 1);
/* 484 */       if (index < 0)
/*     */         break;
/* 486 */       str.add(line.substring(prevIndex + 1, index));
/* 487 */       prevIndex = index;
/*     */     }
/* 489 */     str.add(line.substring(prevIndex + 1));
/* 490 */     return str;
/*     */   }
/*     */   
/*     */   public static String[] splitAsArray(String line, char splitChar)
/*     */   {
/* 495 */     ArrayList<String> str = new ArrayList();
/* 496 */     int index = line.indexOf(splitChar);
/* 497 */     int prevIndex = index;
/* 498 */     if (index >= 0)
/* 499 */       str.add(line.substring(0, index));
/*     */     for (;;) {
/* 501 */       index = line.indexOf(splitChar, index + 1);
/* 502 */       if (index < 0)
/*     */         break;
/* 504 */       str.add(line.substring(prevIndex + 1, index));
/* 505 */       prevIndex = index;
/*     */     }
/* 507 */     str.add(line.substring(prevIndex + 1));
/*     */     
/* 509 */     String[] outArray = new String[str.size()];
/* 510 */     for (int i = 0; i < str.size(); i++) {
/* 511 */       outArray[i] = ((String)str.get(i));
/*     */     }
/* 513 */     return outArray;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/math/MathLib.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */