package com.q1.bt.postprocess;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class ResultsStatistics
{
  private static Boolean flagRemoveZeros = Boolean.valueOf(true);
  
  public static enum StatisticsType { TOTAL_RETURN, 
    ANNUAL_RETURN, 
    AVERAGE_RETURN, 
    STANDARD_DEVIATION, 
    ANNUAL_VOLATILITY, 
    MAX_DRAWDOWN, 
    AVERAGE_DRAWDOWN, 
    SHARPE_RATIO, 
    SORTINO_RATIO, 
    CALMER_RATIO, 
    SMOOTH_CALMER, 
    ULCER_INDEX, 
    MARTIN_RATIO, 
    COMPOSITE1, 
    EXPECTED_MDD, 
    USER_MADE;
  }
  
  private static double getResultsStatistics(ArrayList<Double> mtmDataPoints, StatisticsType resultsStatistic)
  {
    switch (resultsStatistic)
    {
    case ANNUAL_RETURN: 
      return totalReturn(mtmDataPoints).doubleValue();
    
    case AVERAGE_DRAWDOWN: 
      if (!flagRemoveZeros.booleanValue()) return avergaeReturn(mtmDataPoints).doubleValue();
      return avergaeReturn(removeZerosFromArrayListOfDouble(mtmDataPoints)).doubleValue();
    

    case AVERAGE_RETURN: 
      if (!flagRemoveZeros.booleanValue()) return standardDeviation(mtmDataPoints).doubleValue();
      return standardDeviation(removeZerosFromArrayListOfDouble(mtmDataPoints)).doubleValue();
    case CALMER_RATIO: 
      if (!flagRemoveZeros.booleanValue()) return standardDeviation(mtmDataPoints).doubleValue() * 16.0D;
      return standardDeviation(removeZerosFromArrayListOfDouble(mtmDataPoints)).doubleValue() * 16.0D;
    }
    
    

    System.out.println("Eror in statistics");
    System.exit(0);
    return 0.0D;
  }
  



  private static double getResultsStatistics(ArrayList<Double> mtmDataPoints, StatisticsType resultsStatistic, ArrayList<Double> extraInfoPoints)
  {
    switch (resultsStatistic)
    {
    case SORTINO_RATIO: 
      return ulcerIndex(mtmDataPoints, extraInfoPoints).doubleValue();
    
    case COMPOSITE1: 
      return maxDrawdown(mtmDataPoints, extraInfoPoints).doubleValue();
    
    case EXPECTED_MDD: 
      return avgDrawdown(mtmDataPoints, extraInfoPoints).doubleValue();
    }
    
    
    return getResultsStatistics(mtmDataPoints, resultsStatistic);
  }
  

  public static double getResultsStatistics(ArrayList<Double> mtmDataPoints, StatisticsType resultsStatistic, ArrayList<Double> extraInfoPoints, Integer startDate, Integer endDate)
  {
    switch (resultsStatistic)
    {
    case ANNUAL_VOLATILITY: 
      return totalReturn(mtmDataPoints).doubleValue() * 365.0D / getCalendarDays(startDate, endDate).intValue();
    
    case SHARPE_RATIO: 
      return totalReturn(mtmDataPoints).doubleValue() * 365.0D / getCalendarDays(startDate, endDate).intValue() / maxDrawdown(mtmDataPoints, extraInfoPoints).doubleValue();
    
    case SMOOTH_CALMER: 
      return totalReturn(mtmDataPoints).doubleValue() * 365.0D / getCalendarDays(startDate, endDate).intValue() / smoothMaxDrawdown(mtmDataPoints, extraInfoPoints).doubleValue();
    
    case STANDARD_DEVIATION: 
      return totalReturn(mtmDataPoints).doubleValue() * 365.0D / getCalendarDays(startDate, endDate).intValue() / ulcerIndex(mtmDataPoints, extraInfoPoints).doubleValue();
    

    case MARTIN_RATIO: 
      return avergaeReturn(mtmDataPoints).doubleValue() / standardDeviation(mtmDataPoints).doubleValue() * Math.sqrt(mtmDataPoints.size() * 365 / getCalendarDays(startDate, endDate).intValue());
    

    case MAX_DRAWDOWN: 
      return avergaeReturn(mtmDataPoints).doubleValue() / downsideStandardDeviation(mtmDataPoints).doubleValue() * Math.sqrt(mtmDataPoints.size() * 365 / getCalendarDays(startDate, endDate).intValue());
    

    case ULCER_INDEX: 
      return expectedMDD(mtmDataPoints, startDate, endDate).doubleValue();
    }
    
    

    return getResultsStatistics(mtmDataPoints, resultsStatistic, extraInfoPoints);
  }
  






  public static Integer getCalendarDays(Integer startDate, Integer endDate)
  {
    Integer startYear = Integer.valueOf(startDate.intValue() / 10000);
    Integer startMonth = Integer.valueOf(startDate.intValue() % 10000 / 100);
    Integer startDay = Integer.valueOf(startDate.intValue() % 100);
    
    Integer endYear = Integer.valueOf(endDate.intValue() / 10000);
    Integer endMonth = Integer.valueOf(endDate.intValue() % 10000 / 100);
    Integer endDay = Integer.valueOf(endDate.intValue() % 100);
    
    Date startCalDate = new GregorianCalendar(startYear.intValue(), startMonth.intValue() - 1, startDay.intValue()).getTime();
    Date endCalDate = new GregorianCalendar(endYear.intValue(), endMonth.intValue() - 1, endDay.intValue()).getTime();
    
    Long diff = Long.valueOf(-(startCalDate.getTime() - endCalDate.getTime()));
    
    return Integer.valueOf((int)(diff.longValue() / 86400000L) + 1);
  }
  
  private static Double downsideStandardDeviation(ArrayList<Double> mtmDataPoints) {
    Double average = Double.valueOf(0.0D);Double var = Double.valueOf(0.0D);Double sd = Double.valueOf(0.0D);
    Double sum = Double.valueOf(0.0D);
    average = avergaeReturn(mtmDataPoints);
    
    Integer count = Integer.valueOf(0);
    for (int i = 0; i < mtmDataPoints.size(); i++) {
      if (((Double)mtmDataPoints.get(i)).doubleValue() < average.doubleValue()) {
        sum = Double.valueOf(sum.doubleValue() + (((Double)mtmDataPoints.get(i)).doubleValue() - average.doubleValue()) * (((Double)mtmDataPoints.get(i)).doubleValue() - average.doubleValue()));
        count = Integer.valueOf(count.intValue() + 1);
      }
    }
    var = Double.valueOf(sum.doubleValue() / count.intValue());
    sd = Double.valueOf(Math.sqrt(var.doubleValue()));
    return sd;
  }
  
  public static Double standardDeviation(ArrayList<Double> mtmDataPoints) {
    Double average = Double.valueOf(0.0D);Double var = Double.valueOf(0.0D);Double sd = Double.valueOf(0.0D);
    Double sum = Double.valueOf(0.0D);
    average = avergaeReturn(mtmDataPoints);
    
    for (int i = 0; i < mtmDataPoints.size(); i++) {
      sum = Double.valueOf(sum.doubleValue() + (((Double)mtmDataPoints.get(i)).doubleValue() - average.doubleValue()) * (((Double)mtmDataPoints.get(i)).doubleValue() - average.doubleValue()));
    }
    var = Double.valueOf(sum.doubleValue() / mtmDataPoints.size());
    sd = Double.valueOf(Math.sqrt(var.doubleValue()));
    return sd;
  }
  
  public static Double covariance(ArrayList<Double> mtmDataPoints1, ArrayList<Double> mtmDataPoints2)
  {
    Double average1 = Double.valueOf(0.0D);Double average2 = Double.valueOf(0.0D);Double covar = Double.valueOf(0.0D);
    Double sum = Double.valueOf(0.0D);
    average1 = avergaeReturn(mtmDataPoints1);
    average2 = avergaeReturn(mtmDataPoints2);
    
    for (int i = 0; i < mtmDataPoints1.size(); i++) {
      sum = Double.valueOf(sum.doubleValue() + (((Double)mtmDataPoints1.get(i)).doubleValue() - average1.doubleValue()) * (((Double)mtmDataPoints2.get(i)).doubleValue() - average2.doubleValue()));
    }
    covar = Double.valueOf(sum.doubleValue() / mtmDataPoints1.size());
    
    return covar;
  }
  
  public static Double correlation(ArrayList<Double> mtmDataPoints1, ArrayList<Double> mtmDataPoints2)
  {
    Double cov = covariance(mtmDataPoints1, mtmDataPoints2);
    Double sd1 = standardDeviation(mtmDataPoints1);
    Double sd2 = standardDeviation(mtmDataPoints2);
    return Double.valueOf(cov.doubleValue() / (sd1.doubleValue() * sd2.doubleValue()));
  }
  
  private static Double smoothMaxDrawdown(ArrayList<Double> mtmDataPoints, ArrayList<Double> drawdowns)
  {
    ArrayList<Double> drawdownsInLookbackPeriod = new ArrayList(drawdowns.subList(drawdowns.size() - mtmDataPoints.size(), drawdowns.size()));
    java.util.Collections.sort(drawdownsInLookbackPeriod);
    ArrayList<Double> sortedDrawdowns = drawdownsInLookbackPeriod;
    Double smoothValue = Double.valueOf(0.0D);Double reducingFactor = Double.valueOf(0.5D);
    for (int i = sortedDrawdowns.size() - 1; i >= sortedDrawdowns.size() - 5; i--) {
      if (i == -1) return smoothValue;
      smoothValue = Double.valueOf(smoothValue.doubleValue() + ((Double)sortedDrawdowns.get(i)).doubleValue() * reducingFactor.doubleValue());
      reducingFactor = Double.valueOf(reducingFactor.doubleValue() / 2.0D);
    }
    return smoothValue;
  }
  
  private static Double avergaeReturn(ArrayList<Double> mtmDataPoints) { Double sum = Double.valueOf(0.0D);
    for (int i = 0; i < mtmDataPoints.size(); i++) {
      sum = Double.valueOf(sum.doubleValue() + ((Double)mtmDataPoints.get(i)).doubleValue());
    }
    return Double.valueOf(sum.doubleValue() / mtmDataPoints.size());
  }
  
  private static Double totalReturn(ArrayList<Double> mtmDataPoints)
  {
    Double sum = Double.valueOf(0.0D);
    for (int i = 0; i < mtmDataPoints.size(); i++) {
      sum = Double.valueOf(sum.doubleValue() + ((Double)mtmDataPoints.get(i)).doubleValue());
    }
    return sum;
  }
  
  private static Double maxDrawdown(ArrayList<Double> mtmDataPoints, ArrayList<Double> drawdowns) {
    Double max = Double.valueOf(0.0D);
    
    for (int i = drawdowns.size() - mtmDataPoints.size(); i < drawdowns.size(); i++) {
      max = maxDouble(max, (Double)drawdowns.get(i));
    }
    
    return max;
  }
  
  private static Double expectedMDD(ArrayList<Double> mtmDataPoints, Integer startDate, Integer endDate) {
    Double mu = avergaeReturn(mtmDataPoints);
    
    if (mu.doubleValue() < 0.0D) {
      return Double.valueOf(1000000.0D);
    }
    
    Double stdev = standardDeviation(mtmDataPoints);
    Double years = Double.valueOf(getCalendarDays(startDate, endDate).intValue() / 365.0D);
    
    Double expectedMDD = Double.valueOf(stdev.doubleValue() * stdev.doubleValue() / mu.doubleValue() * (0.63519D + 0.5D * Math.log10(years.doubleValue()) + Math.log10(mu.doubleValue() / stdev.doubleValue())));
    
    expectedMDD.doubleValue();
    

    return expectedMDD;
  }
  
  private static Double avgDrawdown(ArrayList<Double> mtmDataPoints, ArrayList<Double> drawdowns) {
    Double sum = Double.valueOf(0.0D);
    
    for (int i = drawdowns.size() - mtmDataPoints.size(); i < drawdowns.size(); i++)
    {

      sum = Double.valueOf(sum.doubleValue() + ((Double)drawdowns.get(i)).doubleValue());
    }
    

    return Double.valueOf(sum.doubleValue() / mtmDataPoints.size());
  }
  







































































  private static Double ulcerIndex(ArrayList<Double> mtmDataPoints, ArrayList<Double> drawdowns)
  {
    Double sum = Double.valueOf(0.0D);
    






































    for (int i = drawdowns.size() - mtmDataPoints.size(); i < drawdowns.size(); i++) {
      if (i == -1) { sum = Double.valueOf(0.0D);
      } else {
        sum = Double.valueOf(sum.doubleValue() + ((Double)drawdowns.get(i)).doubleValue() * ((Double)drawdowns.get(i)).doubleValue());
      }
    }
    
    return Double.valueOf(Math.sqrt(sum.doubleValue() / mtmDataPoints.size()));
  }
  
  public static ArrayList<Double> removeZerosFromArrayListOfDouble(ArrayList<Double> data) {
    ArrayList<Double> temp = new ArrayList();
    
    for (int i = 0; i < data.size(); i++) {
      if (Math.abs(((Double)data.get(i)).doubleValue() - 0.0D) > 1.0E-6D)
      {
        temp.add((Double)data.get(i));
      }
    }
    return temp;
  }
  
  public static ArrayList<Double> removeLeadingZerosFromArrayListOfDouble(ArrayList<Double> data) {
    ArrayList<Double> temp = new ArrayList();
    Boolean flagLeadingZeroesOver = Boolean.valueOf(false);
    
    for (int i = 0; i < data.size(); i++) {
      if ((Math.abs(((Double)data.get(i)).doubleValue() - 0.0D) > 1.0E-6D) && (!flagLeadingZeroesOver.booleanValue())) {
        flagLeadingZeroesOver = Boolean.valueOf(true);
      }
      

      if (flagLeadingZeroesOver.booleanValue()) {
        temp.add((Double)data.get(i));
      }
    }
    return temp;
  }
  
  public static Integer indexOfFirstNonZeroValue(ArrayList<Double> data)
  {
    for (int i = 0; i < data.size(); i++) {
      if (Math.abs(((Double)data.get(i)).doubleValue() - 0.0D) > 1.0E-6D) {
        return Integer.valueOf(i);
      }
    }
    
    return Integer.valueOf(-1);
  }
  

  public static Double maxValueFromDoubleArrayList(ArrayList<Double> data)
  {
    Double max = Double.valueOf(-1000000.0D);
    
    for (int i = 0; i < data.size(); i++) {
      if (((Double)data.get(i)).doubleValue() > max.doubleValue()) {
        max = (Double)data.get(i);
      }
    }
    return max;
  }
  
  public static Double minValueFromDoubleArrayList(ArrayList<Double> data)
  {
    Double min = Double.valueOf(1000000.0D);
    
    for (int i = 0; i < data.size(); i++) {
      if (((Double)data.get(i)).doubleValue() < min.doubleValue()) {
        min = (Double)data.get(i);
      }
    }
    return min;
  }
  
  public static Double maxDouble(Double d1, Double d2)
  {
    if (d1.doubleValue() > d2.doubleValue()) {
      return d1;
    }
    return d2;
  }
  


  public static boolean isBetterOrEqual(Double statDataPoint1, Double statDataPoint2, StatisticsType resultsStatistic)
  {
    boolean result = false;
    
    switch (resultsStatistic)
    {
    case ANNUAL_RETURN: 
    case ANNUAL_VOLATILITY: 
    case AVERAGE_DRAWDOWN: 
    case MARTIN_RATIO: 
    case MAX_DRAWDOWN: 
    case SHARPE_RATIO: 
    case SMOOTH_CALMER: 
    case STANDARD_DEVIATION: 
    case TOTAL_RETURN: 
      if ((statDataPoint1.doubleValue() - statDataPoint2.doubleValue() > 1.0E-6D) || (Math.abs(statDataPoint1.doubleValue() - statDataPoint2.doubleValue()) < 1.0E-6D)) {
        result = true;
      } else {
        result = false;
      }
      break;
    case AVERAGE_RETURN: 
    case CALMER_RATIO: 
    case COMPOSITE1: 
    case EXPECTED_MDD: 
    case SORTINO_RATIO: 
    case ULCER_INDEX: 
      if ((statDataPoint2.doubleValue() - statDataPoint1.doubleValue() > 1.0E-6D) || (Math.abs(statDataPoint1.doubleValue() - statDataPoint2.doubleValue()) < 1.0E-6D)) {
        result = true;
      } else {
        result = false;
      }
      break;
    default: 
      System.out.println("Something went wrong in comparing result statistic");
    }
    return result;
  }
  
  public static boolean isBetterStrictly(Double statDataPoint1, Double statDataPoint2, StatisticsType resultsStatistic) {
    boolean result = false;
    
    switch (resultsStatistic)
    {
    case ANNUAL_RETURN: 
    case ANNUAL_VOLATILITY: 
    case AVERAGE_DRAWDOWN: 
    case MARTIN_RATIO: 
    case MAX_DRAWDOWN: 
    case SHARPE_RATIO: 
    case SMOOTH_CALMER: 
    case STANDARD_DEVIATION: 
    case TOTAL_RETURN: 
      if (statDataPoint1.doubleValue() - statDataPoint2.doubleValue() > 1.0E-6D) {
        result = true;
      } else {
        result = false;
      }
      break;
    case AVERAGE_RETURN: 
    case CALMER_RATIO: 
    case COMPOSITE1: 
    case EXPECTED_MDD: 
    case SORTINO_RATIO: 
    case ULCER_INDEX: 
      if (statDataPoint2.doubleValue() - statDataPoint1.doubleValue() > 1.0E-6D) {
        result = true;
      } else {
        result = false;
      }
      break;
    default: 
      System.out.println("Something went wrong in comparing result statistic");
    }
    return result;
  }
  
  public static Double getInitalComaprisonValue(StatisticsType resultsStatistic) {
    switch (resultsStatistic)
    {
    case ANNUAL_RETURN: 
    case ANNUAL_VOLATILITY: 
    case AVERAGE_DRAWDOWN: 
    case MARTIN_RATIO: 
    case MAX_DRAWDOWN: 
    case SHARPE_RATIO: 
    case SMOOTH_CALMER: 
    case STANDARD_DEVIATION: 
    case TOTAL_RETURN: 
      return Double.valueOf(-1000000.0D);
    case AVERAGE_RETURN: 
    case CALMER_RATIO: 
    case COMPOSITE1: 
    case EXPECTED_MDD: 
    case SORTINO_RATIO: 
    case ULCER_INDEX: 
      return Double.valueOf(1000000.0D);
    }
    System.out.println("Something went wrong in comparing result statistic");
    

    return null;
  }
  

  public static Double getZeroValue(StatisticsType resultsStatistic)
  {
    switch (resultsStatistic)
    {
    case ANNUAL_RETURN: 
    case ANNUAL_VOLATILITY: 
    case AVERAGE_DRAWDOWN: 
    case MARTIN_RATIO: 
    case MAX_DRAWDOWN: 
    case SHARPE_RATIO: 
    case SMOOTH_CALMER: 
    case STANDARD_DEVIATION: 
    case TOTAL_RETURN: 
      return Double.valueOf(0.0D);
    case AVERAGE_RETURN: 
    case CALMER_RATIO: 
    case COMPOSITE1: 
    case EXPECTED_MDD: 
    case SORTINO_RATIO: 
    case ULCER_INDEX: 
      return Double.valueOf(1000000.0D);
    }
    System.out.println("Something went wrong in comparing result statistic");
    

    return null;
  }
  
  public static Double getSumOfDoubleArrayList(ArrayList<Double> data)
  {
    Double sum = Double.valueOf(0.0D);
    for (int i = 0; i < data.size(); i++) {
      sum = Double.valueOf(sum.doubleValue() + ((Double)data.get(i)).doubleValue());
    }
    
    return sum;
  }
  
  public static ArrayList<Double> removeNanAndInfFromDoubleArray(ArrayList<Double> data) {
    ArrayList<Double> cleanData = new ArrayList();
    
    for (int i = 0; i < data.size(); i++) {
      Double temp = (Double)data.get(i);
      if ((!Double.isNaN(temp.doubleValue())) && (!Double.isInfinite(temp.doubleValue()))) {
        cleanData.add(temp);
      }
    }
    

    return cleanData;
  }
  
  public static Double getMutliplicationValue(Double statDataPoint, Double multiplier, StatisticsType resultsStatistic)
  {
    switch (resultsStatistic)
    {
    case ANNUAL_RETURN: 
    case ANNUAL_VOLATILITY: 
    case AVERAGE_DRAWDOWN: 
    case MARTIN_RATIO: 
    case MAX_DRAWDOWN: 
    case SHARPE_RATIO: 
    case SMOOTH_CALMER: 
    case STANDARD_DEVIATION: 
    case TOTAL_RETURN: 
      if (statDataPoint.doubleValue() > 0.0D) {
        return Double.valueOf(statDataPoint.doubleValue() * multiplier.doubleValue());
      }
      return Double.valueOf(statDataPoint.doubleValue() / multiplier.doubleValue());
    

    case AVERAGE_RETURN: 
    case CALMER_RATIO: 
    case COMPOSITE1: 
    case EXPECTED_MDD: 
    case SORTINO_RATIO: 
    case ULCER_INDEX: 
      if (statDataPoint.doubleValue() > 0.0D) {
        return Double.valueOf(statDataPoint.doubleValue() / multiplier.doubleValue());
      }
      return Double.valueOf(statDataPoint.doubleValue() * multiplier.doubleValue());
    }
    
    System.out.println("Something went wrong in multiplying result statistic");
    

    return null;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/ResultsStatistics.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */