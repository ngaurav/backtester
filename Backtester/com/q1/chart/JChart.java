package com.q1.chart;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;



public class JChart
{
  public static ChartPanel createChart(String plotTitle, JXSyncPlot xsPlot)
  {
    CombinedDomainXYPlot plot = xsPlot.plot;
    

    plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
    

    plot.setOrientation(PlotOrientation.VERTICAL);
    

    JFreeChart chart = new JFreeChart(plotTitle, 
      JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    

    ChartPanel panel = new ChartPanel(chart);
    panel.setMouseWheelEnabled(true);
    
    return panel;
  }
  


  public static ChartPanel createChart(String plotTitle, JXYPlot xyPlot)
  {
    XYPlot plot = xyPlot.plot;
    




    JFreeChart chart = new JFreeChart(plotTitle, 
      JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    

    ChartPanel panel = new ChartPanel(chart);
    panel.setMouseWheelEnabled(true);
    
    return panel;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/chart/JChart.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */