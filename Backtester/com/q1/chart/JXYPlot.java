/*     */ package com.q1.chart;
/*     */ 
/*     */ import java.awt.Paint;
/*     */ import java.text.ParseException;
/*     */ import java.util.TreeMap;
/*     */ import org.jfree.chart.axis.DateAxis;
/*     */ import org.jfree.chart.axis.NumberAxis;
/*     */ import org.jfree.chart.axis.ValueAxis;
/*     */ import org.jfree.chart.plot.XYPlot;
/*     */ import org.jfree.chart.renderer.xy.CandlestickRenderer;
/*     */ import org.jfree.chart.renderer.xy.XYAreaRenderer;
/*     */ import org.jfree.chart.renderer.xy.XYBarRenderer;
/*     */ import org.jfree.chart.renderer.xy.XYItemRenderer;
/*     */ import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
/*     */ import org.jfree.data.time.TimeSeriesCollection;
/*     */ import org.jfree.data.xy.DefaultHighLowDataset;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXYPlot
/*     */ {
/*     */   ValueAxis xAxis;
/*     */   NumberAxis yAxis;
/*     */   public XYPlot plot;
/*  25 */   Integer plotIndex = Integer.valueOf(0);
/*     */   
/*     */   String dateFormat;
/*     */   
/*     */   String dateInterval;
/*     */   
/*     */   public JXYPlot(String xTitle, String yTitle, String dateFormat, String dateInterval)
/*     */   {
/*  33 */     this.xAxis = new DateAxis(xTitle);
/*  34 */     this.yAxis = new NumberAxis(yTitle);
/*  35 */     this.yAxis.setAutoRangeIncludesZero(false);
/*     */     
/*     */ 
/*  38 */     this.plot = new XYPlot(null, this.xAxis, this.yAxis, null);
/*  39 */     this.plot.setDomainPannable(true);
/*  40 */     this.plot.setRangePannable(true);
/*     */     
/*     */ 
/*  43 */     this.dateFormat = dateFormat;
/*  44 */     this.dateInterval = dateInterval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addCandlestickPlot(String title, TreeMap<Long, Double[]> candlestickMap)
/*     */     throws ParseException
/*     */   {
/*  52 */     CandlestickRenderer renderer = new CandlestickRenderer();
/*  53 */     this.plot.setRenderer(this.plotIndex.intValue(), renderer);
/*     */     
/*     */ 
/*  56 */     DefaultHighLowDataset dataset = JDataset.createTimeSeriesOHLCDataset(
/*  57 */       title, candlestickMap, this.dateFormat);
/*  58 */     this.plot.setDataset(this.plotIndex.intValue(), dataset);
/*     */     
/*  60 */     this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addAreaPlot(String title, TreeMap<Long, Double> scatterMap)
/*     */     throws ParseException
/*     */   {
/*  68 */     XYAreaRenderer renderer = new XYAreaRenderer();
/*  69 */     this.plot.setRenderer(this.plotIndex.intValue(), renderer);
/*     */     
/*     */ 
/*  72 */     TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset(
/*  73 */       title, scatterMap, this.dateInterval);
/*  74 */     this.plot.setDataset(this.plotIndex.intValue(), dataset);
/*     */     
/*  76 */     this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addAreaPlot(String title, TreeMap<Long, Double> scatterMap, Paint color)
/*     */     throws ParseException
/*     */   {
/*  84 */     XYAreaRenderer renderer = new XYAreaRenderer();
/*  85 */     renderer.setSeriesPaint(0, color);
/*  86 */     this.plot.setRenderer(this.plotIndex.intValue(), renderer);
/*     */     
/*     */ 
/*  89 */     TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset(
/*  90 */       title, scatterMap, this.dateInterval);
/*  91 */     this.plot.setDataset(this.plotIndex.intValue(), dataset);
/*     */     
/*  93 */     this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addBarPlot(String title, TreeMap<Long, Double> scatterMap)
/*     */     throws ParseException
/*     */   {
/* 101 */     XYBarRenderer renderer = new XYBarRenderer();
/* 102 */     this.plot.setRenderer(this.plotIndex.intValue(), renderer);
/*     */     
/*     */ 
/* 105 */     TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset(
/* 106 */       title, scatterMap, this.dateInterval);
/* 107 */     this.plot.setDataset(this.plotIndex.intValue(), dataset);
/*     */     
/* 109 */     this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addScatterPlot(String title, TreeMap<Long, Double> scatterMap, Paint color)
/*     */     throws ParseException
/*     */   {
/* 117 */     XYItemRenderer renderer = new XYLineAndShapeRenderer(false, true);
/* 118 */     renderer.setSeriesPaint(0, color);
/* 119 */     this.plot.setRenderer(this.plotIndex.intValue(), renderer);
/*     */     
/*     */ 
/* 122 */     TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset(
/* 123 */       title, scatterMap, this.dateInterval);
/* 124 */     this.plot.setDataset(this.plotIndex.intValue(), dataset);
/*     */     
/* 126 */     this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addScatterPlot(String title, TreeMap<Long, Double> scatterMap)
/*     */     throws ParseException
/*     */   {
/* 134 */     XYItemRenderer renderer = new XYLineAndShapeRenderer(false, true);
/* 135 */     this.plot.setRenderer(this.plotIndex.intValue(), renderer);
/*     */     
/*     */ 
/* 138 */     TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset(
/* 139 */       title, scatterMap, this.dateInterval);
/* 140 */     this.plot.setDataset(this.plotIndex.intValue(), dataset);
/*     */     
/* 142 */     this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addLinePlot(String title, TreeMap<Long, Double> lineMap)
/*     */     throws ParseException
/*     */   {
/* 150 */     XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
/* 151 */     this.plot.setRenderer(this.plotIndex.intValue(), renderer);
/*     */     
/*     */ 
/* 154 */     TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset(
/* 155 */       title, lineMap, this.dateInterval);
/* 156 */     this.plot.setDataset(this.plotIndex.intValue(), dataset);
/*     */     
/* 158 */     this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/chart/JXYPlot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */