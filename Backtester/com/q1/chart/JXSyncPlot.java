package com.q1.chart;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;


public class JXSyncPlot
{
  ValueAxis xAxis;
  NumberAxis yAxis;
  CombinedDomainXYPlot plot;
  Integer plotIndex = Integer.valueOf(0);
  
  String dateFormat;
  
  String dateInterval;
  
  public JXSyncPlot(String xTitle, String yTitle, String dateFormat, String dateInterval)
  {
    this.xAxis = new DateAxis(xTitle);
    

    this.plot = new CombinedDomainXYPlot(this.xAxis);
    this.plot.setDomainPannable(true);
    this.plot.setRangePannable(true);
    

    this.dateFormat = dateFormat;
    this.dateInterval = dateInterval;
  }
  
  public void addPlot(JXYPlot xyPlot)
  {
    this.plot.add(xyPlot.plot, 1);
  }
  

  public ChartPanel createChart(String plotTitle)
  {
    this.plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
    

    this.plot.setOrientation(PlotOrientation.VERTICAL);
    

    JFreeChart chart = new JFreeChart(plotTitle, 
      JFreeChart.DEFAULT_TITLE_FONT, this.plot, true);
    

    ChartPanel panel = new ChartPanel(chart);
    panel.setMouseWheelEnabled(true);
    
    return panel;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/chart/JXSyncPlot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */