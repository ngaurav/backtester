/*     */ package com.q1.bt.postprocess;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ 
/*     */ public class ResultsStatistics
/*     */ {
/*  10 */   private static Boolean flagRemoveZeros = Boolean.valueOf(true);
/*     */   
/*  12 */   public static enum StatisticsType { TOTAL_RETURN, 
/*  13 */     ANNUAL_RETURN, 
/*  14 */     AVERAGE_RETURN, 
/*  15 */     STANDARD_DEVIATION, 
/*  16 */     ANNUAL_VOLATILITY, 
/*  17 */     MAX_DRAWDOWN, 
/*  18 */     AVERAGE_DRAWDOWN, 
/*  19 */     SHARPE_RATIO, 
/*  20 */     SORTINO_RATIO, 
/*  21 */     CALMER_RATIO, 
/*  22 */     SMOOTH_CALMER, 
/*  23 */     ULCER_INDEX, 
/*  24 */     MARTIN_RATIO, 
/*  25 */     COMPOSITE1, 
/*  26 */     EXPECTED_MDD, 
/*  27 */     USER_MADE;
/*     */   }
/*     */   
/*     */   private static double getResultsStatistics(ArrayList<Double> mtmDataPoints, StatisticsType resultsStatistic)
/*     */   {
/*  32 */     switch (resultsStatistic)
/*     */     {
/*     */     case ANNUAL_RETURN: 
/*  35 */       return totalReturn(mtmDataPoints).doubleValue();
/*     */     
/*     */     case AVERAGE_DRAWDOWN: 
/*  38 */       if (!flagRemoveZeros.booleanValue()) return avergaeReturn(mtmDataPoints).doubleValue();
/*  39 */       return avergaeReturn(removeZerosFromArrayListOfDouble(mtmDataPoints)).doubleValue();
/*     */     
/*     */ 
/*     */     case AVERAGE_RETURN: 
/*  43 */       if (!flagRemoveZeros.booleanValue()) return standardDeviation(mtmDataPoints).doubleValue();
/*  44 */       return standardDeviation(removeZerosFromArrayListOfDouble(mtmDataPoints)).doubleValue();
/*     */     case CALMER_RATIO: 
/*  46 */       if (!flagRemoveZeros.booleanValue()) return standardDeviation(mtmDataPoints).doubleValue() * 16.0D;
/*  47 */       return standardDeviation(removeZerosFromArrayListOfDouble(mtmDataPoints)).doubleValue() * 16.0D;
/*     */     }
/*     */     
/*     */     
/*     */ 
/*  52 */     System.out.println("Eror in statistics");
/*  53 */     System.exit(0);
/*  54 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static double getResultsStatistics(ArrayList<Double> mtmDataPoints, StatisticsType resultsStatistic, ArrayList<Double> extraInfoPoints)
/*     */   {
/*  62 */     switch (resultsStatistic)
/*     */     {
/*     */     case SORTINO_RATIO: 
/*  65 */       return ulcerIndex(mtmDataPoints, extraInfoPoints).doubleValue();
/*     */     
/*     */     case COMPOSITE1: 
/*  68 */       return maxDrawdown(mtmDataPoints, extraInfoPoints).doubleValue();
/*     */     
/*     */     case EXPECTED_MDD: 
/*  71 */       return avgDrawdown(mtmDataPoints, extraInfoPoints).doubleValue();
/*     */     }
/*     */     
/*     */     
/*  75 */     return getResultsStatistics(mtmDataPoints, resultsStatistic);
/*     */   }
/*     */   
/*     */ 
/*     */   public static double getResultsStatistics(ArrayList<Double> mtmDataPoints, StatisticsType resultsStatistic, ArrayList<Double> extraInfoPoints, Integer startDate, Integer endDate)
/*     */   {
/*  81 */     switch (resultsStatistic)
/*     */     {
/*     */     case ANNUAL_VOLATILITY: 
/*  84 */       return totalReturn(mtmDataPoints).doubleValue() * 365.0D / getCalendarDays(startDate, endDate).intValue();
/*     */     
/*     */     case SHARPE_RATIO: 
/*  87 */       return totalReturn(mtmDataPoints).doubleValue() * 365.0D / getCalendarDays(startDate, endDate).intValue() / maxDrawdown(mtmDataPoints, extraInfoPoints).doubleValue();
/*     */     
/*     */     case SMOOTH_CALMER: 
/*  90 */       return totalReturn(mtmDataPoints).doubleValue() * 365.0D / getCalendarDays(startDate, endDate).intValue() / smoothMaxDrawdown(mtmDataPoints, extraInfoPoints).doubleValue();
/*     */     
/*     */     case STANDARD_DEVIATION: 
/*  93 */       return totalReturn(mtmDataPoints).doubleValue() * 365.0D / getCalendarDays(startDate, endDate).intValue() / ulcerIndex(mtmDataPoints, extraInfoPoints).doubleValue();
/*     */     
/*     */ 
/*     */     case MARTIN_RATIO: 
/*  97 */       return avergaeReturn(mtmDataPoints).doubleValue() / standardDeviation(mtmDataPoints).doubleValue() * Math.sqrt(mtmDataPoints.size() * 365 / getCalendarDays(startDate, endDate).intValue());
/*     */     
/*     */ 
/*     */     case MAX_DRAWDOWN: 
/* 101 */       return avergaeReturn(mtmDataPoints).doubleValue() / downsideStandardDeviation(mtmDataPoints).doubleValue() * Math.sqrt(mtmDataPoints.size() * 365 / getCalendarDays(startDate, endDate).intValue());
/*     */     
/*     */ 
/*     */     case ULCER_INDEX: 
/* 105 */       return expectedMDD(mtmDataPoints, startDate, endDate).doubleValue();
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 110 */     return getResultsStatistics(mtmDataPoints, resultsStatistic, extraInfoPoints);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Integer getCalendarDays(Integer startDate, Integer endDate)
/*     */   {
/* 121 */     Integer startYear = Integer.valueOf(startDate.intValue() / 10000);
/* 122 */     Integer startMonth = Integer.valueOf(startDate.intValue() % 10000 / 100);
/* 123 */     Integer startDay = Integer.valueOf(startDate.intValue() % 100);
/*     */     
/* 125 */     Integer endYear = Integer.valueOf(endDate.intValue() / 10000);
/* 126 */     Integer endMonth = Integer.valueOf(endDate.intValue() % 10000 / 100);
/* 127 */     Integer endDay = Integer.valueOf(endDate.intValue() % 100);
/*     */     
/* 129 */     Date startCalDate = new GregorianCalendar(startYear.intValue(), startMonth.intValue() - 1, startDay.intValue()).getTime();
/* 130 */     Date endCalDate = new GregorianCalendar(endYear.intValue(), endMonth.intValue() - 1, endDay.intValue()).getTime();
/*     */     
/* 132 */     Long diff = Long.valueOf(-(startCalDate.getTime() - endCalDate.getTime()));
/*     */     
/* 134 */     return Integer.valueOf((int)(diff.longValue() / 86400000L) + 1);
/*     */   }
/*     */   
/*     */   private static Double downsideStandardDeviation(ArrayList<Double> mtmDataPoints) {
/* 138 */     Double average = Double.valueOf(0.0D);Double var = Double.valueOf(0.0D);Double sd = Double.valueOf(0.0D);
/* 139 */     Double sum = Double.valueOf(0.0D);
/* 140 */     average = avergaeReturn(mtmDataPoints);
/*     */     
/* 142 */     Integer count = Integer.valueOf(0);
/* 143 */     for (int i = 0; i < mtmDataPoints.size(); i++) {
/* 144 */       if (((Double)mtmDataPoints.get(i)).doubleValue() < average.doubleValue()) {
/* 145 */         sum = Double.valueOf(sum.doubleValue() + (((Double)mtmDataPoints.get(i)).doubleValue() - average.doubleValue()) * (((Double)mtmDataPoints.get(i)).doubleValue() - average.doubleValue()));
/* 146 */         count = Integer.valueOf(count.intValue() + 1);
/*     */       }
/*     */     }
/* 149 */     var = Double.valueOf(sum.doubleValue() / count.intValue());
/* 150 */     sd = Double.valueOf(Math.sqrt(var.doubleValue()));
/* 151 */     return sd;
/*     */   }
/*     */   
/*     */   public static Double standardDeviation(ArrayList<Double> mtmDataPoints) {
/* 155 */     Double average = Double.valueOf(0.0D);Double var = Double.valueOf(0.0D);Double sd = Double.valueOf(0.0D);
/* 156 */     Double sum = Double.valueOf(0.0D);
/* 157 */     average = avergaeReturn(mtmDataPoints);
/*     */     
/* 159 */     for (int i = 0; i < mtmDataPoints.size(); i++) {
/* 160 */       sum = Double.valueOf(sum.doubleValue() + (((Double)mtmDataPoints.get(i)).doubleValue() - average.doubleValue()) * (((Double)mtmDataPoints.get(i)).doubleValue() - average.doubleValue()));
/*     */     }
/* 162 */     var = Double.valueOf(sum.doubleValue() / mtmDataPoints.size());
/* 163 */     sd = Double.valueOf(Math.sqrt(var.doubleValue()));
/* 164 */     return sd;
/*     */   }
/*     */   
/*     */   public static Double covariance(ArrayList<Double> mtmDataPoints1, ArrayList<Double> mtmDataPoints2)
/*     */   {
/* 169 */     Double average1 = Double.valueOf(0.0D);Double average2 = Double.valueOf(0.0D);Double covar = Double.valueOf(0.0D);
/* 170 */     Double sum = Double.valueOf(0.0D);
/* 171 */     average1 = avergaeReturn(mtmDataPoints1);
/* 172 */     average2 = avergaeReturn(mtmDataPoints2);
/*     */     
/* 174 */     for (int i = 0; i < mtmDataPoints1.size(); i++) {
/* 175 */       sum = Double.valueOf(sum.doubleValue() + (((Double)mtmDataPoints1.get(i)).doubleValue() - average1.doubleValue()) * (((Double)mtmDataPoints2.get(i)).doubleValue() - average2.doubleValue()));
/*     */     }
/* 177 */     covar = Double.valueOf(sum.doubleValue() / mtmDataPoints1.size());
/*     */     
/* 179 */     return covar;
/*     */   }
/*     */   
/*     */   public static Double correlation(ArrayList<Double> mtmDataPoints1, ArrayList<Double> mtmDataPoints2)
/*     */   {
/* 184 */     Double cov = covariance(mtmDataPoints1, mtmDataPoints2);
/* 185 */     Double sd1 = standardDeviation(mtmDataPoints1);
/* 186 */     Double sd2 = standardDeviation(mtmDataPoints2);
/* 187 */     return Double.valueOf(cov.doubleValue() / (sd1.doubleValue() * sd2.doubleValue()));
/*     */   }
/*     */   
/*     */   private static Double smoothMaxDrawdown(ArrayList<Double> mtmDataPoints, ArrayList<Double> drawdowns)
/*     */   {
/* 192 */     ArrayList<Double> drawdownsInLookbackPeriod = new ArrayList(drawdowns.subList(drawdowns.size() - mtmDataPoints.size(), drawdowns.size()));
/* 193 */     java.util.Collections.sort(drawdownsInLookbackPeriod);
/* 194 */     ArrayList<Double> sortedDrawdowns = drawdownsInLookbackPeriod;
/* 195 */     Double smoothValue = Double.valueOf(0.0D);Double reducingFactor = Double.valueOf(0.5D);
/* 196 */     for (int i = sortedDrawdowns.size() - 1; i >= sortedDrawdowns.size() - 5; i--) {
/* 197 */       if (i == -1) return smoothValue;
/* 198 */       smoothValue = Double.valueOf(smoothValue.doubleValue() + ((Double)sortedDrawdowns.get(i)).doubleValue() * reducingFactor.doubleValue());
/* 199 */       reducingFactor = Double.valueOf(reducingFactor.doubleValue() / 2.0D);
/*     */     }
/* 201 */     return smoothValue;
/*     */   }
/*     */   
/* 204 */   private static Double avergaeReturn(ArrayList<Double> mtmDataPoints) { Double sum = Double.valueOf(0.0D);
/* 205 */     for (int i = 0; i < mtmDataPoints.size(); i++) {
/* 206 */       sum = Double.valueOf(sum.doubleValue() + ((Double)mtmDataPoints.get(i)).doubleValue());
/*     */     }
/* 208 */     return Double.valueOf(sum.doubleValue() / mtmDataPoints.size());
/*     */   }
/*     */   
/*     */   private static Double totalReturn(ArrayList<Double> mtmDataPoints)
/*     */   {
/* 213 */     Double sum = Double.valueOf(0.0D);
/* 214 */     for (int i = 0; i < mtmDataPoints.size(); i++) {
/* 215 */       sum = Double.valueOf(sum.doubleValue() + ((Double)mtmDataPoints.get(i)).doubleValue());
/*     */     }
/* 217 */     return sum;
/*     */   }
/*     */   
/*     */   private static Double maxDrawdown(ArrayList<Double> mtmDataPoints, ArrayList<Double> drawdowns) {
/* 221 */     Double max = Double.valueOf(0.0D);
/*     */     
/* 223 */     for (int i = drawdowns.size() - mtmDataPoints.size(); i < drawdowns.size(); i++) {
/* 224 */       max = maxDouble(max, (Double)drawdowns.get(i));
/*     */     }
/*     */     
/* 227 */     return max;
/*     */   }
/*     */   
/*     */   private static Double expectedMDD(ArrayList<Double> mtmDataPoints, Integer startDate, Integer endDate) {
/* 231 */     Double mu = avergaeReturn(mtmDataPoints);
/*     */     
/* 233 */     if (mu.doubleValue() < 0.0D) {
/* 234 */       return Double.valueOf(1000000.0D);
/*     */     }
/*     */     
/* 237 */     Double stdev = standardDeviation(mtmDataPoints);
/* 238 */     Double years = Double.valueOf(getCalendarDays(startDate, endDate).intValue() / 365.0D);
/*     */     
/* 240 */     Double expectedMDD = Double.valueOf(stdev.doubleValue() * stdev.doubleValue() / mu.doubleValue() * (0.63519D + 0.5D * Math.log10(years.doubleValue()) + Math.log10(mu.doubleValue() / stdev.doubleValue())));
/*     */     
/* 242 */     expectedMDD.doubleValue();
/*     */     
/*     */ 
/* 245 */     return expectedMDD;
/*     */   }
/*     */   
/*     */   private static Double avgDrawdown(ArrayList<Double> mtmDataPoints, ArrayList<Double> drawdowns) {
/* 249 */     Double sum = Double.valueOf(0.0D);
/*     */     
/* 251 */     for (int i = drawdowns.size() - mtmDataPoints.size(); i < drawdowns.size(); i++)
/*     */     {
/*     */ 
/* 254 */       sum = Double.valueOf(sum.doubleValue() + ((Double)drawdowns.get(i)).doubleValue());
/*     */     }
/*     */     
/*     */ 
/* 258 */     return Double.valueOf(sum.doubleValue() / mtmDataPoints.size());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Double ulcerIndex(ArrayList<Double> mtmDataPoints, ArrayList<Double> drawdowns)
/*     */   {
/* 334 */     Double sum = Double.valueOf(0.0D);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 374 */     for (int i = drawdowns.size() - mtmDataPoints.size(); i < drawdowns.size(); i++) {
/* 375 */       if (i == -1) { sum = Double.valueOf(0.0D);
/*     */       } else {
/* 377 */         sum = Double.valueOf(sum.doubleValue() + ((Double)drawdowns.get(i)).doubleValue() * ((Double)drawdowns.get(i)).doubleValue());
/*     */       }
/*     */     }
/*     */     
/* 381 */     return Double.valueOf(Math.sqrt(sum.doubleValue() / mtmDataPoints.size()));
/*     */   }
/*     */   
/*     */   public static ArrayList<Double> removeZerosFromArrayListOfDouble(ArrayList<Double> data) {
/* 385 */     ArrayList<Double> temp = new ArrayList();
/*     */     
/* 387 */     for (int i = 0; i < data.size(); i++) {
/* 388 */       if (Math.abs(((Double)data.get(i)).doubleValue() - 0.0D) > 1.0E-6D)
/*     */       {
/* 390 */         temp.add((Double)data.get(i));
/*     */       }
/*     */     }
/* 393 */     return temp;
/*     */   }
/*     */   
/*     */   public static ArrayList<Double> removeLeadingZerosFromArrayListOfDouble(ArrayList<Double> data) {
/* 397 */     ArrayList<Double> temp = new ArrayList();
/* 398 */     Boolean flagLeadingZeroesOver = Boolean.valueOf(false);
/*     */     
/* 400 */     for (int i = 0; i < data.size(); i++) {
/* 401 */       if ((Math.abs(((Double)data.get(i)).doubleValue() - 0.0D) > 1.0E-6D) && (!flagLeadingZeroesOver.booleanValue())) {
/* 402 */         flagLeadingZeroesOver = Boolean.valueOf(true);
/*     */       }
/*     */       
/*     */ 
/* 406 */       if (flagLeadingZeroesOver.booleanValue()) {
/* 407 */         temp.add((Double)data.get(i));
/*     */       }
/*     */     }
/* 410 */     return temp;
/*     */   }
/*     */   
/*     */   public static Integer indexOfFirstNonZeroValue(ArrayList<Double> data)
/*     */   {
/* 415 */     for (int i = 0; i < data.size(); i++) {
/* 416 */       if (Math.abs(((Double)data.get(i)).doubleValue() - 0.0D) > 1.0E-6D) {
/* 417 */         return Integer.valueOf(i);
/*     */       }
/*     */     }
/*     */     
/* 421 */     return Integer.valueOf(-1);
/*     */   }
/*     */   
/*     */ 
/*     */   public static Double maxValueFromDoubleArrayList(ArrayList<Double> data)
/*     */   {
/* 427 */     Double max = Double.valueOf(-1000000.0D);
/*     */     
/* 429 */     for (int i = 0; i < data.size(); i++) {
/* 430 */       if (((Double)data.get(i)).doubleValue() > max.doubleValue()) {
/* 431 */         max = (Double)data.get(i);
/*     */       }
/*     */     }
/* 434 */     return max;
/*     */   }
/*     */   
/*     */   public static Double minValueFromDoubleArrayList(ArrayList<Double> data)
/*     */   {
/* 439 */     Double min = Double.valueOf(1000000.0D);
/*     */     
/* 441 */     for (int i = 0; i < data.size(); i++) {
/* 442 */       if (((Double)data.get(i)).doubleValue() < min.doubleValue()) {
/* 443 */         min = (Double)data.get(i);
/*     */       }
/*     */     }
/* 446 */     return min;
/*     */   }
/*     */   
/*     */   public static Double maxDouble(Double d1, Double d2)
/*     */   {
/* 451 */     if (d1.doubleValue() > d2.doubleValue()) {
/* 452 */       return d1;
/*     */     }
/* 454 */     return d2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isBetterOrEqual(Double statDataPoint1, Double statDataPoint2, StatisticsType resultsStatistic)
/*     */   {
/* 461 */     boolean result = false;
/*     */     
/* 463 */     switch (resultsStatistic)
/*     */     {
/*     */     case ANNUAL_RETURN: 
/*     */     case ANNUAL_VOLATILITY: 
/*     */     case AVERAGE_DRAWDOWN: 
/*     */     case MARTIN_RATIO: 
/*     */     case MAX_DRAWDOWN: 
/*     */     case SHARPE_RATIO: 
/*     */     case SMOOTH_CALMER: 
/*     */     case STANDARD_DEVIATION: 
/*     */     case TOTAL_RETURN: 
/* 474 */       if ((statDataPoint1.doubleValue() - statDataPoint2.doubleValue() > 1.0E-6D) || (Math.abs(statDataPoint1.doubleValue() - statDataPoint2.doubleValue()) < 1.0E-6D)) {
/* 475 */         result = true;
/*     */       } else {
/* 477 */         result = false;
/*     */       }
/* 479 */       break;
/*     */     case AVERAGE_RETURN: 
/*     */     case CALMER_RATIO: 
/*     */     case COMPOSITE1: 
/*     */     case EXPECTED_MDD: 
/*     */     case SORTINO_RATIO: 
/*     */     case ULCER_INDEX: 
/* 486 */       if ((statDataPoint2.doubleValue() - statDataPoint1.doubleValue() > 1.0E-6D) || (Math.abs(statDataPoint1.doubleValue() - statDataPoint2.doubleValue()) < 1.0E-6D)) {
/* 487 */         result = true;
/*     */       } else {
/* 489 */         result = false;
/*     */       }
/* 491 */       break;
/*     */     default: 
/* 493 */       System.out.println("Something went wrong in comparing result statistic");
/*     */     }
/* 495 */     return result;
/*     */   }
/*     */   
/*     */   public static boolean isBetterStrictly(Double statDataPoint1, Double statDataPoint2, StatisticsType resultsStatistic) {
/* 499 */     boolean result = false;
/*     */     
/* 501 */     switch (resultsStatistic)
/*     */     {
/*     */     case ANNUAL_RETURN: 
/*     */     case ANNUAL_VOLATILITY: 
/*     */     case AVERAGE_DRAWDOWN: 
/*     */     case MARTIN_RATIO: 
/*     */     case MAX_DRAWDOWN: 
/*     */     case SHARPE_RATIO: 
/*     */     case SMOOTH_CALMER: 
/*     */     case STANDARD_DEVIATION: 
/*     */     case TOTAL_RETURN: 
/* 512 */       if (statDataPoint1.doubleValue() - statDataPoint2.doubleValue() > 1.0E-6D) {
/* 513 */         result = true;
/*     */       } else {
/* 515 */         result = false;
/*     */       }
/* 517 */       break;
/*     */     case AVERAGE_RETURN: 
/*     */     case CALMER_RATIO: 
/*     */     case COMPOSITE1: 
/*     */     case EXPECTED_MDD: 
/*     */     case SORTINO_RATIO: 
/*     */     case ULCER_INDEX: 
/* 524 */       if (statDataPoint2.doubleValue() - statDataPoint1.doubleValue() > 1.0E-6D) {
/* 525 */         result = true;
/*     */       } else {
/* 527 */         result = false;
/*     */       }
/* 529 */       break;
/*     */     default: 
/* 531 */       System.out.println("Something went wrong in comparing result statistic");
/*     */     }
/* 533 */     return result;
/*     */   }
/*     */   
/*     */   public static Double getInitalComaprisonValue(StatisticsType resultsStatistic) {
/* 537 */     switch (resultsStatistic)
/*     */     {
/*     */     case ANNUAL_RETURN: 
/*     */     case ANNUAL_VOLATILITY: 
/*     */     case AVERAGE_DRAWDOWN: 
/*     */     case MARTIN_RATIO: 
/*     */     case MAX_DRAWDOWN: 
/*     */     case SHARPE_RATIO: 
/*     */     case SMOOTH_CALMER: 
/*     */     case STANDARD_DEVIATION: 
/*     */     case TOTAL_RETURN: 
/* 548 */       return Double.valueOf(-1000000.0D);
/*     */     case AVERAGE_RETURN: 
/*     */     case CALMER_RATIO: 
/*     */     case COMPOSITE1: 
/*     */     case EXPECTED_MDD: 
/*     */     case SORTINO_RATIO: 
/*     */     case ULCER_INDEX: 
/* 555 */       return Double.valueOf(1000000.0D);
/*     */     }
/* 557 */     System.out.println("Something went wrong in comparing result statistic");
/*     */     
/*     */ 
/* 560 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static Double getZeroValue(StatisticsType resultsStatistic)
/*     */   {
/* 566 */     switch (resultsStatistic)
/*     */     {
/*     */     case ANNUAL_RETURN: 
/*     */     case ANNUAL_VOLATILITY: 
/*     */     case AVERAGE_DRAWDOWN: 
/*     */     case MARTIN_RATIO: 
/*     */     case MAX_DRAWDOWN: 
/*     */     case SHARPE_RATIO: 
/*     */     case SMOOTH_CALMER: 
/*     */     case STANDARD_DEVIATION: 
/*     */     case TOTAL_RETURN: 
/* 577 */       return Double.valueOf(0.0D);
/*     */     case AVERAGE_RETURN: 
/*     */     case CALMER_RATIO: 
/*     */     case COMPOSITE1: 
/*     */     case EXPECTED_MDD: 
/*     */     case SORTINO_RATIO: 
/*     */     case ULCER_INDEX: 
/* 584 */       return Double.valueOf(1000000.0D);
/*     */     }
/* 586 */     System.out.println("Something went wrong in comparing result statistic");
/*     */     
/*     */ 
/* 589 */     return null;
/*     */   }
/*     */   
/*     */   public static Double getSumOfDoubleArrayList(ArrayList<Double> data)
/*     */   {
/* 594 */     Double sum = Double.valueOf(0.0D);
/* 595 */     for (int i = 0; i < data.size(); i++) {
/* 596 */       sum = Double.valueOf(sum.doubleValue() + ((Double)data.get(i)).doubleValue());
/*     */     }
/*     */     
/* 599 */     return sum;
/*     */   }
/*     */   
/*     */   public static ArrayList<Double> removeNanAndInfFromDoubleArray(ArrayList<Double> data) {
/* 603 */     ArrayList<Double> cleanData = new ArrayList();
/*     */     
/* 605 */     for (int i = 0; i < data.size(); i++) {
/* 606 */       Double temp = (Double)data.get(i);
/* 607 */       if ((!Double.isNaN(temp.doubleValue())) && (!Double.isInfinite(temp.doubleValue()))) {
/* 608 */         cleanData.add(temp);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 613 */     return cleanData;
/*     */   }
/*     */   
/*     */   public static Double getMutliplicationValue(Double statDataPoint, Double multiplier, StatisticsType resultsStatistic)
/*     */   {
/* 618 */     switch (resultsStatistic)
/*     */     {
/*     */     case ANNUAL_RETURN: 
/*     */     case ANNUAL_VOLATILITY: 
/*     */     case AVERAGE_DRAWDOWN: 
/*     */     case MARTIN_RATIO: 
/*     */     case MAX_DRAWDOWN: 
/*     */     case SHARPE_RATIO: 
/*     */     case SMOOTH_CALMER: 
/*     */     case STANDARD_DEVIATION: 
/*     */     case TOTAL_RETURN: 
/* 629 */       if (statDataPoint.doubleValue() > 0.0D) {
/* 630 */         return Double.valueOf(statDataPoint.doubleValue() * multiplier.doubleValue());
/*     */       }
/* 632 */       return Double.valueOf(statDataPoint.doubleValue() / multiplier.doubleValue());
/*     */     
/*     */ 
/*     */     case AVERAGE_RETURN: 
/*     */     case CALMER_RATIO: 
/*     */     case COMPOSITE1: 
/*     */     case EXPECTED_MDD: 
/*     */     case SORTINO_RATIO: 
/*     */     case ULCER_INDEX: 
/* 641 */       if (statDataPoint.doubleValue() > 0.0D) {
/* 642 */         return Double.valueOf(statDataPoint.doubleValue() / multiplier.doubleValue());
/*     */       }
/* 644 */       return Double.valueOf(statDataPoint.doubleValue() * multiplier.doubleValue());
/*     */     }
/*     */     
/* 647 */     System.out.println("Something went wrong in multiplying result statistic");
/*     */     
/*     */ 
/* 650 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/ResultsStatistics.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */