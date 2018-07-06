package com.q1.chart;

import java.awt.Paint;
import java.text.ParseException;
import java.util.TreeMap;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;



public class JXYPlot
{
  ValueAxis xAxis;
  NumberAxis yAxis;
  public XYPlot plot;
  Integer plotIndex = Integer.valueOf(0);
  
  String dateFormat;
  
  String dateInterval;
  
  public JXYPlot(String xTitle, String yTitle, String dateFormat, String dateInterval)
  {
    this.xAxis = new DateAxis(xTitle);
    this.yAxis = new NumberAxis(yTitle);
    this.yAxis.setAutoRangeIncludesZero(false);
    

    this.plot = new XYPlot(null, this.xAxis, this.yAxis, null);
    this.plot.setDomainPannable(true);
    this.plot.setRangePannable(true);
    

    this.dateFormat = dateFormat;
    this.dateInterval = dateInterval;
  }
  


  public void addCandlestickPlot(String title, TreeMap<Long, Double[]> candlestickMap)
    throws ParseException
  {
    CandlestickRenderer renderer = new CandlestickRenderer();
    this.plot.setRenderer(this.plotIndex.intValue(), renderer);
    

    DefaultHighLowDataset dataset = JDataset.createTimeSeriesOHLCDataset(
      title, candlestickMap, this.dateFormat);
    this.plot.setDataset(this.plotIndex.intValue(), dataset);
    
    this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
  }
  


  public void addAreaPlot(String title, TreeMap<Long, Double> scatterMap)
    throws ParseException
  {
    XYAreaRenderer renderer = new XYAreaRenderer();
    this.plot.setRenderer(this.plotIndex.intValue(), renderer);
    

    TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset(
      title, scatterMap, this.dateInterval);
    this.plot.setDataset(this.plotIndex.intValue(), dataset);
    
    this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
  }
  


  public void addAreaPlot(String title, TreeMap<Long, Double> scatterMap, Paint color)
    throws ParseException
  {
    XYAreaRenderer renderer = new XYAreaRenderer();
    renderer.setSeriesPaint(0, color);
    this.plot.setRenderer(this.plotIndex.intValue(), renderer);
    

    TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset(
      title, scatterMap, this.dateInterval);
    this.plot.setDataset(this.plotIndex.intValue(), dataset);
    
    this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
  }
  


  public void addBarPlot(String title, TreeMap<Long, Double> scatterMap)
    throws ParseException
  {
    XYBarRenderer renderer = new XYBarRenderer();
    this.plot.setRenderer(this.plotIndex.intValue(), renderer);
    

    TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset(
      title, scatterMap, this.dateInterval);
    this.plot.setDataset(this.plotIndex.intValue(), dataset);
    
    this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
  }
  


  public void addScatterPlot(String title, TreeMap<Long, Double> scatterMap, Paint color)
    throws ParseException
  {
    XYItemRenderer renderer = new XYLineAndShapeRenderer(false, true);
    renderer.setSeriesPaint(0, color);
    this.plot.setRenderer(this.plotIndex.intValue(), renderer);
    

    TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset(
      title, scatterMap, this.dateInterval);
    this.plot.setDataset(this.plotIndex.intValue(), dataset);
    
    this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
  }
  


  public void addScatterPlot(String title, TreeMap<Long, Double> scatterMap)
    throws ParseException
  {
    XYItemRenderer renderer = new XYLineAndShapeRenderer(false, true);
    this.plot.setRenderer(this.plotIndex.intValue(), renderer);
    

    TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset(
      title, scatterMap, this.dateInterval);
    this.plot.setDataset(this.plotIndex.intValue(), dataset);
    
    this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
  }
  


  public void addLinePlot(String title, TreeMap<Long, Double> lineMap)
    throws ParseException
  {
    XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
    this.plot.setRenderer(this.plotIndex.intValue(), renderer);
    

    TimeSeriesCollection dataset = JDataset.createTimeSeriesXYDataset(
      title, lineMap, this.dateInterval);
    this.plot.setDataset(this.plotIndex.intValue(), dataset);
    
    this.plotIndex = Integer.valueOf(this.plotIndex.intValue() + 1);
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/chart/JXYPlot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */