package com.q1.chart;

import com.q1.math.MathLib;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;




public class JDataset
{
  public static TimeSeriesCollection createTimeSeriesXYDataset(String title, ArrayList<Date> dateTime, ArrayList<Double> array, String type)
  {
    TimeSeries series = MathLib.ArrayLists2Series(title, dateTime, array, 
      type);
    TimeSeriesCollection dataset = new TimeSeriesCollection(series);
    return dataset;
  }
  
  public static TimeSeriesCollection createTimeSeriesXYDataset(String title, TreeMap<Long, Double> tsMap, String type)
    throws ParseException
  {
    TimeSeries series = MathLib.Treemap2Series(title, tsMap, type);
    TimeSeriesCollection dataset = new TimeSeriesCollection(series);
    return dataset;
  }
  
  public static DefaultHighLowDataset createTimeSeriesOHLCDataset(String title, TreeMap<Long, Double[]> tsMap, String format)
    throws ParseException
  {
    Integer size = Integer.valueOf(tsMap.size());
    Date[] ts = new Date[size.intValue()];
    double[] op = new double[size.intValue()];
    double[] hi = new double[size.intValue()];
    double[] lo = new double[size.intValue()];
    double[] cl = new double[size.intValue()];
    double[] vol = new double[size.intValue()];
    int i = 0;
    for (Map.Entry<Long, Double[]> entry : tsMap.entrySet()) {
      Long date = (Long)entry.getKey();
      Double[] values = (Double[])entry.getValue();
      ts[i] = new SimpleDateFormat(format).parse(date
        .toString());
      op[i] = values[0].doubleValue();
      hi[i] = values[1].doubleValue();
      lo[i] = values[2].doubleValue();
      cl[i] = values[3].doubleValue();
      vol[i] = 0.0D;
      i++;
    }
    DefaultHighLowDataset dataset = new DefaultHighLowDataset(title, ts, hi, 
      lo, op, cl, vol);
    return dataset;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/chart/JDataset.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */