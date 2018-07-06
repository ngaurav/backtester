package com.q1.math;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.jfree.data.time.Day;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Year;


public class MathLib
{
  public static final double epsilon = 1.0E-13D;
  private static final int[] POW10 = { 1, 10, 100, 1000, 10000, 100000, 1000000 };
  
  public static String DoubleToString(double val, int precision) {
    StringBuilder sb = new StringBuilder();
    if (val < 0.0D) {
      sb.append('-');
      val = -val;
    }
    int exp = POW10[precision];
    long lval = (val * exp + 0.5D);
    sb.append(lval / exp).append('.');
    long fval = lval % exp;
    for (int p = precision - 1; (p > 0) && (fval < POW10[p]); p--) {
      sb.append('0');
    }
    sb.append(fval);
    return sb.toString();
  }
  
  public static double simpleAvg(double[] vals)
  {
    double cumSum = 0.0D;
    for (int i = 0; i < vals.length; i++) {
      cumSum += vals[i];
    }
    return cumSum / vals.length;
  }
  
  public static double simpleAvg(ArrayList<Long> vals)
  {
    double cumSum = 0.0D;
    for (Long val : vals) {
      cumSum += val.doubleValue();
    }
    return cumSum / vals.size();
  }
  
  public static double simpleAvg(Collection<Double> vals)
  {
    double cumSum = 0.0D;
    for (Double val : vals) {
      cumSum += val.doubleValue();
    }
    return cumSum / vals.size();
  }
  
  public static double simpleAvgInt(Collection<Integer> vals)
  {
    double cumSum = 0.0D;
    for (Integer val : vals) {
      cumSum += val.doubleValue();
    }
    return cumSum / vals.size();
  }
  
  public static double simpleAvgDouble(ArrayList<Double> vals)
  {
    double cumSum = 0.0D;
    for (Double val : vals) {
      cumSum += val.doubleValue();
    }
    return cumSum / vals.size();
  }
  
  public static double sumDouble(ArrayList<Double> vals)
  {
    double cumSum = 0.0D;
    for (Double val : vals) {
      cumSum += val.doubleValue();
    }
    return cumSum;
  }
  
  public static double max(double val1, double val2)
  {
    if (val1 > val2) {
      return val1;
    }
    return val2;
  }
  
  public static int max(int val1, int val2)
  {
    if (val1 > val2) {
      return val1;
    }
    return val2;
  }
  
  public static long max(long val1, long val2)
  {
    if (val1 > val2) {
      return val1;
    }
    return val2;
  }
  
  public static int min(int val1, int val2)
  {
    if (val1 < val2) {
      return val1;
    }
    return val2;
  }
  
  public static double min(double val1, double val2)
  {
    if (val1 < val2) {
      return val1;
    }
    return val2;
  }
  
  public static long min(long val1, long val2)
  {
    if (val1 < val2) {
      return val1;
    }
    return val2;
  }
  
  public static double max(double[] vals)
  {
    double max_val = vals[0];
    for (int i = 1; i < vals.length; i++) {
      if (vals[i] > max_val) {
        max_val = vals[i];
      }
    }
    return max_val;
  }
  
  public static double max(ArrayList<Double> vals)
  {
    double max_val = 0.0D;
    try {
      max_val = ((Double)vals.get(0)).doubleValue();
    } catch (Exception e) {
      return 0.0D;
    }
    for (int i = 1; i < vals.size(); i++) {
      if (((Double)vals.get(i)).doubleValue() > max_val) {
        max_val = ((Double)vals.get(i)).doubleValue();
      }
    }
    return max_val;
  }
  
  public static double max(Collection<Double> valsC)
  {
    ArrayList<Double> vals = new ArrayList(valsC);
    double max_val = ((Double)vals.get(0)).doubleValue();
    for (int i = 1; i < vals.size(); i++) {
      if (((Double)vals.get(i)).doubleValue() > max_val) {
        max_val = ((Double)vals.get(i)).doubleValue();
      }
    }
    return max_val;
  }
  
  public static Long maxLong(ArrayList<Long> vals)
  {
    Long max_val = Long.valueOf(0L);
    try {
      max_val = (Long)vals.get(0);
    } catch (Exception e) {
      return Long.valueOf(0L);
    }
    for (int i = 1; i < vals.size(); i++) {
      if (((Long)vals.get(i)).longValue() > max_val.longValue()) {
        max_val = (Long)vals.get(i);
      }
    }
    return max_val;
  }
  
  public static double max(TreeMap<Long, Double> vals)
  {
    double max_val = Double.NEGATIVE_INFINITY;
    for (Double curVal : vals.values()) {
      if (curVal.doubleValue() > max_val) {
        max_val = curVal.doubleValue();
      }
    }
    return max_val;
  }
  
  public static TreeMap<Long, Double> multiply(TreeMap<Long, Double> vals, Double multiple)
  {
    for (Map.Entry<Long, Double> entry : new TreeMap(vals).entrySet()) {
      vals.put((Long)entry.getKey(), Double.valueOf(((Double)entry.getValue()).doubleValue() * multiple.doubleValue()));
    }
    return vals;
  }
  
  public static int maxIndex(double[] vals)
  {
    double max_val = vals[0];
    int max_ind = 0;
    for (int i = 1; i < vals.length; i++) {
      if (vals[i] > max_val) {
        max_val = vals[i];
        max_ind = i;
      }
    }
    return max_ind;
  }
  
  public static int maxIndex(ArrayList<Double> vals)
  {
    double max_val = ((Double)vals.get(0)).doubleValue();
    int max_ind = 0;
    for (int i = 1; i < vals.size(); i++) {
      if (((Double)vals.get(i)).doubleValue() > max_val) {
        max_val = ((Double)vals.get(i)).doubleValue();
        max_ind = i;
      }
    }
    return max_ind;
  }
  
  public static double min(double[] vals)
  {
    double min_val = vals[0];
    for (int i = 1; i < vals.length; i++) {
      if (vals[i] < min_val) {
        min_val = vals[i];
      }
    }
    return min_val;
  }
  
  public static double min(ArrayList<Double> vals)
  {
    double min_val = ((Double)vals.get(0)).doubleValue();
    for (int i = 1; i < vals.size(); i++) {
      if (((Double)vals.get(i)).doubleValue() < min_val) {
        min_val = ((Double)vals.get(i)).doubleValue();
      }
    }
    return min_val;
  }
  
  public static double min(TreeMap<Long, Double> vals)
  {
    double min_val = Double.POSITIVE_INFINITY;
    for (Double curVal : vals.values()) {
      if (curVal.doubleValue() < min_val) {
        min_val = curVal.doubleValue();
      }
    }
    return min_val;
  }
  
  public static int minIndex(double[] vals)
  {
    double min_val = vals[0];
    int min_ind = 0;
    for (int i = 1; i < vals.length; i++) {
      if (vals[i] < min_val) {
        min_val = vals[i];
        min_ind = i;
      }
    }
    return min_ind;
  }
  
  public static int minIndex(ArrayList<Double> vals)
  {
    double min_val = ((Double)vals.get(0)).doubleValue();
    int min_ind = 0;
    for (int i = 1; i < vals.size(); i++) {
      if (((Double)vals.get(i)).doubleValue() < min_val) {
        min_val = ((Double)vals.get(i)).doubleValue();
        min_ind = i;
      }
    }
    return min_ind;
  }
  
  public static double[] ArrayList2ArrayDouble(ArrayList<Double> array)
  {
    double[] out_array = new double[array.size()];
    for (int i = 0; i < array.size(); i++) {
      out_array[i] = ((Double)array.get(i)).doubleValue();
    }
    return out_array;
  }
  
  public static double[][] ArrayListList2MatrixDouble(ArrayList<ArrayList<Double>> array) {
    double[][] out_array = new double[array.size()][((ArrayList)array.get(0)).size()];
    for (int i = 0; i < array.size(); i++) {
      for (int j = 0; j < ((ArrayList)array.get(i)).size(); j++) {
        out_array[i][j] = ((Double)((ArrayList)array.get(i)).get(j)).doubleValue();
      }
    }
    return out_array;
  }
  
  public static int[] ArrayList2ArrayInt(ArrayList<Integer> array)
  {
    int[] out_array = new int[array.size()];
    for (int i = 0; i < array.size(); i++) {
      out_array[i] = ((Integer)array.get(i)).intValue();
    }
    return out_array;
  }
  
  public static Date[] ArrayList2ArrayDate(ArrayList<Date> array)
  {
    Date[] out_array = new Date[array.size()];
    for (int i = 0; i < array.size(); i++) {
      out_array[i] = ((Date)array.get(i));
    }
    return out_array;
  }
  
  public static double[][] ArrayList2MatrixDouble(ArrayList<Double> array)
  {
    double[][] out_array = new double[1][array.size()];
    for (int i = 0; i < array.size(); i++) {
      out_array[0][i] = ((Double)array.get(i)).doubleValue();
    }
    return out_array;
  }
  
  public static ArrayList<Double> cumSum(ArrayList<Double> array)
  {
    ArrayList<Double> out_array = new ArrayList();
    double currentSum = 0.0D;
    for (int i = 0; i < array.size(); i++) {
      currentSum += ((Double)array.get(i)).doubleValue();
      out_array.add(Double.valueOf(currentSum));
    }
    return out_array;
  }
  
  public static TreeMap<Long, Double> cumSum(TreeMap<Long, Double> map)
  {
    Double currentSum = Double.valueOf(0.0D);
    for (Map.Entry<Long, Double> entry : map.entrySet()) {
      currentSum = Double.valueOf(currentSum.doubleValue() + ((Double)entry.getValue()).doubleValue());
      map.put((Long)entry.getKey(), currentSum);
    }
    return map;
  }
  

  public static TimeSeries ArrayLists2Series(String seriesName, ArrayList<Date> dateArray, ArrayList<Double> array, String type)
  {
    TimeSeries series = new TimeSeries(seriesName);
    for (int i = 0; i < array.size(); i++) {
      if (type.equals("Minute")) {
        series.add(new Minute((Date)dateArray.get(i)), (Number)array.get(i));
      } else if (type.equals("Day")) {
        series.add(new Day((Date)dateArray.get(i)), (Number)array.get(i));
      }
    }
    return series;
  }
  
  public static TimeSeries Treemap2Series(String seriesName, TreeMap<Long, Double> tsMap, String type)
    throws ParseException
  {
    TimeSeries series = new TimeSeries(seriesName);
    for (Map.Entry<Long, Double> entry : tsMap.entrySet()) {
      Double val = (Double)entry.getValue();
      if (type.equals("Minute")) {
        Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(((Long)entry.getKey()).toString());
        series.add(new Minute(date), val);
      } else if (type.equals("Day")) {
        Date date = new SimpleDateFormat("yyyyMMdd").parse(((Long)entry.getKey()).toString());
        series.add(new Day(date), val);
      } else if (type.equals("Month")) {
        Date date = new SimpleDateFormat("yyyyMM").parse(((Long)entry.getKey()).toString());
        series.add(new Month(date), val);
      } else if (type.equals("Year")) {
        Date date = new SimpleDateFormat("yyyy").parse(((Long)entry.getKey()).toString());
        series.add(new Year(date), val);
      }
    }
    return series;
  }
  
  public static double round(double inp, int places)
  {
    int x = (int)Math.pow(10.0D, places);
    return Math.round(inp * x) / x;
  }
  
  public static double floorTick(double inp, double tick)
  {
    return Math.floor((inp + 1.0E-13D) / tick) * tick;
  }
  
  public static double ceilTick(double inp, double tick)
  {
    return Math.ceil((inp - 1.0E-13D) / tick) * tick;
  }
  
  public static double roundTick(double inp, double tick)
  {
    return Math.round((inp - 1.0E-13D) / tick) * tick;
  }
  
  public static double roundTickWithPrecision(double inp, double tick)
  {
    NumberFormat formatter = new DecimalFormat("#0.00000000");
    String textTick = formatter.format(inp);
    int integerPlaces = textTick.indexOf('.');
    int places = textTick.length() - integerPlaces - 1;
    return round(Math.round(inp / tick) * tick, places);
  }
  
  public static ArrayList<Double> createZeroDoubleArrayList(int size)
  {
    ArrayList<Double> outArray = new ArrayList();
    for (int i = 0; i < size; i++)
      outArray.add(Double.valueOf(0.0D));
    return outArray;
  }
  
  public static Integer longCompare(Long l1, Long l2)
  {
    if (l1.longValue() > l2.longValue())
      return Integer.valueOf(1);
    if (l1.longValue() < l2.longValue()) {
      return Integer.valueOf(-1);
    }
    return Integer.valueOf(0);
  }
  
  public static Integer intCompare(Integer l1, Integer l2)
  {
    if (l1.intValue() > l2.intValue())
      return Integer.valueOf(1);
    if (l1.intValue() < l2.intValue()) {
      return Integer.valueOf(-1);
    }
    return Integer.valueOf(0);
  }
  
  public static Integer doubleCompare(Double d1, Double d2)
  {
    if ((d1.doubleValue() >= d2.doubleValue() - 1.0E-13D) && (d1.doubleValue() <= d2.doubleValue() + 1.0E-13D))
      return Integer.valueOf(0);
    if (d1.doubleValue() > d2.doubleValue() - 1.0E-13D)
      return Integer.valueOf(1);
    if (d1.doubleValue() < d2.doubleValue() + 1.0E-13D)
      return Integer.valueOf(-1);
    return null;
  }
  
  public static ArrayList<String> split(String line, char splitChar)
  {
    ArrayList<String> str = new ArrayList();
    int index = line.indexOf(splitChar);
    int prevIndex = index;
    if (index >= 0)
      str.add(line.substring(0, index));
    for (;;) {
      index = line.indexOf(splitChar, index + 1);
      if (index < 0)
        break;
      str.add(line.substring(prevIndex + 1, index));
      prevIndex = index;
    }
    str.add(line.substring(prevIndex + 1));
    return str;
  }
  
  public static String[] splitAsArray(String line, char splitChar)
  {
    ArrayList<String> str = new ArrayList();
    int index = line.indexOf(splitChar);
    int prevIndex = index;
    if (index >= 0)
      str.add(line.substring(0, index));
    for (;;) {
      index = line.indexOf(splitChar, index + 1);
      if (index < 0)
        break;
      str.add(line.substring(prevIndex + 1, index));
      prevIndex = index;
    }
    str.add(line.substring(prevIndex + 1));
    
    String[] outArray = new String[str.size()];
    for (int i = 0; i < str.size(); i++) {
      outArray[i] = ((String)str.get(i));
    }
    return outArray;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/math/MathLib.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */