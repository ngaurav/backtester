/*     */ package com.q1.chart;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.TreeMap;
/*     */ import org.jfree.chart.ChartPanel;
/*     */ import org.jfree.chart.JFreeChart;
/*     */ import org.jfree.chart.axis.DateAxis;
/*     */ import org.jfree.chart.axis.NumberAxis;
/*     */ import org.jfree.chart.axis.ValueAxis;
/*     */ import org.jfree.chart.plot.XYPlot;
/*     */ import org.jfree.chart.renderer.xy.CandlestickRenderer;
/*     */ import org.jfree.chart.renderer.xy.StandardXYBarPainter;
/*     */ import org.jfree.chart.renderer.xy.XYAreaRenderer;
/*     */ import org.jfree.chart.renderer.xy.XYBarRenderer;
/*     */ import org.jfree.chart.renderer.xy.XYItemRenderer;
/*     */ import org.jfree.data.time.DateRange;
/*     */ import org.jfree.data.time.TimeSeriesCollection;
/*     */ import org.jfree.data.xy.DefaultHighLowDataset;
/*     */ import org.jfree.data.xy.IntervalXYDataset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChartLib
/*     */ {
/*     */   public static ChartPanel timeSeriesAreaChart(TreeMap<Long, Double> mtmMap, String dateFormat, String dateInterval, String plotTitle, String xTitle, String yTitle)
/*     */     throws Exception
/*     */   {
/*  31 */     ValueAxis dateAxis = new DateAxis(xTitle);
/*  32 */     NumberAxis valueAxis = new NumberAxis(yTitle);
/*  33 */     valueAxis.setAutoRangeIncludesZero(false);
/*  34 */     dateAxis.setRange(new DateRange(new SimpleDateFormat(dateFormat)
/*  35 */       .parse(((Long)mtmMap.firstKey()).toString()), new SimpleDateFormat(
/*  36 */       dateFormat).parse(((Long)mtmMap.lastKey()).toString())));
/*     */     
/*     */ 
/*  39 */     TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset("", 
/*  40 */       mtmMap, dateInterval);
/*     */     
/*     */ 
/*  43 */     XYItemRenderer renderer = new XYAreaRenderer();
/*  44 */     renderer.setSeriesPaint(0, Color.BLUE);
/*     */     
/*     */ 
/*  47 */     XYPlot plot = new XYPlot(dataset, dateAxis, valueAxis, renderer);
/*  48 */     plot.setDomainPannable(true);
/*  49 */     plot.setRangePannable(true);
/*     */     
/*     */ 
/*  52 */     JFreeChart chart = new JFreeChart(plotTitle, 
/*  53 */       JFreeChart.DEFAULT_TITLE_FONT, plot, true);
/*  54 */     chart.removeLegend();
/*     */     
/*     */ 
/*  57 */     ChartPanel panel = new ChartPanel(chart);
/*  58 */     panel.setMouseWheelEnabled(true);
/*     */     
/*  60 */     return panel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ChartPanel timeSeriesBarChart(TreeMap<Long, Double> mtmMap, String dateFormat, String dateInterval, String plotTitle, String xTitle, String yTitle)
/*     */     throws Exception
/*     */   {
/*  69 */     ValueAxis dateAxis = new DateAxis(xTitle);
/*  70 */     NumberAxis valueAxis = new NumberAxis(yTitle);
/*  71 */     dateAxis.setRange(new DateRange(new SimpleDateFormat(dateFormat)
/*  72 */       .parse(((Long)mtmMap.firstKey()).toString()), new SimpleDateFormat(
/*  73 */       dateFormat).parse(((Long)mtmMap.lastKey()).toString())));
/*     */     
/*     */ 
/*  76 */     IntervalXYDataset dataset = JDataset.createTimeSeriesXYDataset(
/*  77 */       "Time Series", mtmMap, dateInterval);
/*     */     
/*     */ 
/*  80 */     XYBarRenderer renderer = new XYBarRenderer();
/*  81 */     renderer.setShadowVisible(false);
/*  82 */     renderer.setBarPainter(new StandardXYBarPainter());
/*  83 */     renderer.setSeriesPaint(0, Color.BLUE);
/*     */     
/*     */ 
/*  86 */     XYPlot plot = new XYPlot(dataset, dateAxis, valueAxis, renderer);
/*  87 */     plot.setDomainPannable(true);
/*  88 */     plot.setRangePannable(true);
/*     */     
/*     */ 
/*  91 */     JFreeChart chart = new JFreeChart(plotTitle, 
/*  92 */       JFreeChart.DEFAULT_TITLE_FONT, plot, true);
/*  93 */     chart.removeLegend();
/*     */     
/*     */ 
/*  96 */     ChartPanel panel = new ChartPanel(chart);
/*  97 */     panel.setMouseWheelEnabled(true);
/*     */     
/*  99 */     return panel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ChartPanel timeSeriesCandlestickChart(TreeMap<Long, Double[]> candlestickMap, String plotTitle, String xTitle, String yTitle, String dateFormat)
/*     */     throws Exception
/*     */   {
/* 108 */     ValueAxis dateAxis = new DateAxis(xTitle);
/* 109 */     NumberAxis valueAxis = new NumberAxis(yTitle);
/* 110 */     valueAxis.setAutoRangeIncludesZero(false);
/*     */     
/*     */ 
/* 113 */     DefaultHighLowDataset dataset = JDataset.createTimeSeriesOHLCDataset(
/* 114 */       "", candlestickMap, dateFormat);
/*     */     
/*     */ 
/* 117 */     CandlestickRenderer renderer = new CandlestickRenderer();
/*     */     
/*     */ 
/* 120 */     XYPlot plot = new XYPlot(dataset, dateAxis, valueAxis, renderer);
/* 121 */     plot.setDomainPannable(true);
/* 122 */     plot.setRangePannable(true);
/*     */     
/*     */ 
/* 125 */     JFreeChart chart = new JFreeChart(plotTitle, 
/* 126 */       JFreeChart.DEFAULT_TITLE_FONT, plot, true);
/* 127 */     chart.removeLegend();
/*     */     
/*     */ 
/* 130 */     ChartPanel panel = new ChartPanel(chart);
/* 131 */     panel.setMouseWheelEnabled(true);
/*     */     
/* 133 */     return panel;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/chart/ChartLib.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */