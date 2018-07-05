/*    */ package com.q1.chart;
/*    */ 
/*    */ import org.jfree.chart.ChartPanel;
/*    */ import org.jfree.chart.JFreeChart;
/*    */ import org.jfree.chart.plot.CombinedDomainXYPlot;
/*    */ import org.jfree.chart.plot.DatasetRenderingOrder;
/*    */ import org.jfree.chart.plot.PlotOrientation;
/*    */ import org.jfree.chart.plot.XYPlot;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JChart
/*    */ {
/*    */   public static ChartPanel createChart(String plotTitle, JXSyncPlot xsPlot)
/*    */   {
/* 16 */     CombinedDomainXYPlot plot = xsPlot.plot;
/*    */     
/*    */ 
/* 19 */     plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
/*    */     
/*    */ 
/* 22 */     plot.setOrientation(PlotOrientation.VERTICAL);
/*    */     
/*    */ 
/* 25 */     JFreeChart chart = new JFreeChart(plotTitle, 
/* 26 */       JFreeChart.DEFAULT_TITLE_FONT, plot, true);
/*    */     
/*    */ 
/* 29 */     ChartPanel panel = new ChartPanel(chart);
/* 30 */     panel.setMouseWheelEnabled(true);
/*    */     
/* 32 */     return panel;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static ChartPanel createChart(String plotTitle, JXYPlot xyPlot)
/*    */   {
/* 39 */     XYPlot plot = xyPlot.plot;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 45 */     JFreeChart chart = new JFreeChart(plotTitle, 
/* 46 */       JFreeChart.DEFAULT_TITLE_FONT, plot, true);
/*    */     
/*    */ 
/* 49 */     ChartPanel panel = new ChartPanel(chart);
/* 50 */     panel.setMouseWheelEnabled(true);
/*    */     
/* 52 */     return panel;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/chart/JChart.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */