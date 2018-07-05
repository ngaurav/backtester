/*    */ package com.q1.chart;
/*    */ 
/*    */ import org.jfree.chart.ChartPanel;
/*    */ import org.jfree.chart.JFreeChart;
/*    */ import org.jfree.chart.axis.DateAxis;
/*    */ import org.jfree.chart.axis.NumberAxis;
/*    */ import org.jfree.chart.axis.ValueAxis;
/*    */ import org.jfree.chart.plot.CombinedDomainXYPlot;
/*    */ import org.jfree.chart.plot.DatasetRenderingOrder;
/*    */ import org.jfree.chart.plot.PlotOrientation;
/*    */ 
/*    */ 
/*    */ public class JXSyncPlot
/*    */ {
/*    */   ValueAxis xAxis;
/*    */   NumberAxis yAxis;
/*    */   CombinedDomainXYPlot plot;
/* 18 */   Integer plotIndex = Integer.valueOf(0);
/*    */   
/*    */   String dateFormat;
/*    */   
/*    */   String dateInterval;
/*    */   
/*    */   public JXSyncPlot(String xTitle, String yTitle, String dateFormat, String dateInterval)
/*    */   {
/* 26 */     this.xAxis = new DateAxis(xTitle);
/*    */     
/*    */ 
/* 29 */     this.plot = new CombinedDomainXYPlot(this.xAxis);
/* 30 */     this.plot.setDomainPannable(true);
/* 31 */     this.plot.setRangePannable(true);
/*    */     
/*    */ 
/* 34 */     this.dateFormat = dateFormat;
/* 35 */     this.dateInterval = dateInterval;
/*    */   }
/*    */   
/*    */   public void addPlot(JXYPlot xyPlot)
/*    */   {
/* 40 */     this.plot.add(xyPlot.plot, 1);
/*    */   }
/*    */   
/*    */ 
/*    */   public ChartPanel createChart(String plotTitle)
/*    */   {
/* 46 */     this.plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
/*    */     
/*    */ 
/* 49 */     this.plot.setOrientation(PlotOrientation.VERTICAL);
/*    */     
/*    */ 
/* 52 */     JFreeChart chart = new JFreeChart(plotTitle, 
/* 53 */       JFreeChart.DEFAULT_TITLE_FONT, this.plot, true);
/*    */     
/*    */ 
/* 56 */     ChartPanel panel = new ChartPanel(chart);
/* 57 */     panel.setMouseWheelEnabled(true);
/*    */     
/* 59 */     return panel;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/chart/JXSyncPlot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */