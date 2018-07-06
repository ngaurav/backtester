package com.q1.chart;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.TreeMap;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.DateRange;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.IntervalXYDataset;





public class ChartLib
{
  public static ChartPanel timeSeriesAreaChart(TreeMap<Long, Double> mtmMap, String dateFormat, String dateInterval, String plotTitle, String xTitle, String yTitle)
    throws Exception
  {
    ValueAxis dateAxis = new DateAxis(xTitle);
    NumberAxis valueAxis = new NumberAxis(yTitle);
    valueAxis.setAutoRangeIncludesZero(false);
    dateAxis.setRange(new DateRange(new SimpleDateFormat(dateFormat)
      .parse(((Long)mtmMap.firstKey()).toString()), new SimpleDateFormat(
      dateFormat).parse(((Long)mtmMap.lastKey()).toString())));
    

    TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset("", 
      mtmMap, dateInterval);
    

    XYItemRenderer renderer = new XYAreaRenderer();
    renderer.setSeriesPaint(0, Color.BLUE);
    

    XYPlot plot = new XYPlot(dataset, dateAxis, valueAxis, renderer);
    plot.setDomainPannable(true);
    plot.setRangePannable(true);
    

    JFreeChart chart = new JFreeChart(plotTitle, 
      JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    chart.removeLegend();
    

    ChartPanel panel = new ChartPanel(chart);
    panel.setMouseWheelEnabled(true);
    
    return panel;
  }
  



  public static ChartPanel timeSeriesBarChart(TreeMap<Long, Double> mtmMap, String dateFormat, String dateInterval, String plotTitle, String xTitle, String yTitle)
    throws Exception
  {
    ValueAxis dateAxis = new DateAxis(xTitle);
    NumberAxis valueAxis = new NumberAxis(yTitle);
    dateAxis.setRange(new DateRange(new SimpleDateFormat(dateFormat)
      .parse(((Long)mtmMap.firstKey()).toString()), new SimpleDateFormat(
      dateFormat).parse(((Long)mtmMap.lastKey()).toString())));
    

    IntervalXYDataset dataset = JDataset.createTimeSeriesXYDataset(
      "Time Series", mtmMap, dateInterval);
    

    XYBarRenderer renderer = new XYBarRenderer();
    renderer.setShadowVisible(false);
    renderer.setBarPainter(new StandardXYBarPainter());
    renderer.setSeriesPaint(0, Color.BLUE);
    

    XYPlot plot = new XYPlot(dataset, dateAxis, valueAxis, renderer);
    plot.setDomainPannable(true);
    plot.setRangePannable(true);
    

    JFreeChart chart = new JFreeChart(plotTitle, 
      JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    chart.removeLegend();
    

    ChartPanel panel = new ChartPanel(chart);
    panel.setMouseWheelEnabled(true);
    
    return panel;
  }
  



  public static ChartPanel timeSeriesCandlestickChart(TreeMap<Long, Double[]> candlestickMap, String plotTitle, String xTitle, String yTitle, String dateFormat)
    throws Exception
  {
    ValueAxis dateAxis = new DateAxis(xTitle);
    NumberAxis valueAxis = new NumberAxis(yTitle);
    valueAxis.setAutoRangeIncludesZero(false);
    

    DefaultHighLowDataset dataset = JDataset.createTimeSeriesOHLCDataset(
      "", candlestickMap, dateFormat);
    

    CandlestickRenderer renderer = new CandlestickRenderer();
    

    XYPlot plot = new XYPlot(dataset, dateAxis, valueAxis, renderer);
    plot.setDomainPannable(true);
    plot.setRangePannable(true);
    

    JFreeChart chart = new JFreeChart(plotTitle, 
      JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    chart.removeLegend();
    

    ChartPanel panel = new ChartPanel(chart);
    panel.setMouseWheelEnabled(true);
    
    return panel;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/chart/ChartLib.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */