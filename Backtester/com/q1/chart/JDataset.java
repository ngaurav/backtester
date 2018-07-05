/*    */ package com.q1.chart;
/*    */ 
/*    */ import com.q1.math.MathLib;
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Date;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.TreeMap;
/*    */ import org.jfree.data.time.TimeSeries;
/*    */ import org.jfree.data.time.TimeSeriesCollection;
/*    */ import org.jfree.data.xy.DefaultHighLowDataset;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JDataset
/*    */ {
/*    */   public static TimeSeriesCollection createTimeSeriesXYDataset(String title, ArrayList<Date> dateTime, ArrayList<Double> array, String type)
/*    */   {
/* 21 */     TimeSeries series = MathLib.ArrayLists2Series(title, dateTime, array, 
/* 22 */       type);
/* 23 */     TimeSeriesCollection dataset = new TimeSeriesCollection(series);
/* 24 */     return dataset;
/*    */   }
/*    */   
/*    */   public static TimeSeriesCollection createTimeSeriesXYDataset(String title, TreeMap<Long, Double> tsMap, String type)
/*    */     throws ParseException
/*    */   {
/* 30 */     TimeSeries series = MathLib.Treemap2Series(title, tsMap, type);
/* 31 */     TimeSeriesCollection dataset = new TimeSeriesCollection(series);
/* 32 */     return dataset;
/*    */   }
/*    */   
/*    */   public static DefaultHighLowDataset createTimeSeriesOHLCDataset(String title, TreeMap<Long, Double[]> tsMap, String format)
/*    */     throws ParseException
/*    */   {
/* 38 */     Integer size = Integer.valueOf(tsMap.size());
/* 39 */     Date[] ts = new Date[size.intValue()];
/* 40 */     double[] op = new double[size.intValue()];
/* 41 */     double[] hi = new double[size.intValue()];
/* 42 */     double[] lo = new double[size.intValue()];
/* 43 */     double[] cl = new double[size.intValue()];
/* 44 */     double[] vol = new double[size.intValue()];
/* 45 */     int i = 0;
/* 46 */     for (Map.Entry<Long, Double[]> entry : tsMap.entrySet()) {
/* 47 */       Long date = (Long)entry.getKey();
/* 48 */       Double[] values = (Double[])entry.getValue();
/* 49 */       ts[i] = new SimpleDateFormat(format).parse(date
/* 50 */         .toString());
/* 51 */       op[i] = values[0].doubleValue();
/* 52 */       hi[i] = values[1].doubleValue();
/* 53 */       lo[i] = values[2].doubleValue();
/* 54 */       cl[i] = values[3].doubleValue();
/* 55 */       vol[i] = 0.0D;
/* 56 */       i++;
/*    */     }
/* 58 */     DefaultHighLowDataset dataset = new DefaultHighLowDataset(title, ts, hi, 
/* 59 */       lo, op, cl, vol);
/* 60 */     return dataset;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/chart/JDataset.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */