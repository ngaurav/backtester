package com.q1.bt.postprocess;

import java.util.LinkedHashMap;

public class PostProcess { public Double mtmSharpe;
  public Double mtmSortino;
  public Double mtmStdDev;
  public Double mtmCalmar;
  public Double mtmSmoothCalmar;
  public Double mtmHitRate;
  public Double mtmWinLoss;
  public Double trades;
  public Double maxDrawdown;
  public Double avgDrawdown;
  public Double avgDrawdownDuration;
  public Double profitFactor;
  public Long maxDrawdownDuration;
  public Double avgDailyReturn;
  public Double avgTradeReturn;
  public Double slippageFactor;
  public Double normSlippageFactor;
  public Double expectancy;
  public Double monthlyHitRate;
  public Double monthlyWinLoss;
  public Double avgTradeDuration;
  public Double avgWinDuration;
  public Double avgLossDuration;
  public Double maxTradesPerDay;
  public Double avgTradePerDay;
  public Double avgSlippage;
  public Double avgOpenSlippage;
  public Double opTradePerc;
  public Double avgNormSlippage;
  public Integer tradeCount;
  public Double avgWinTradePL;
  public Double avgLossTradePL;
  public Double tradeHitRate;
  public Double tradeWinLoss;
  public Double annualReturn;
  public Double annualVolatility;
  public Double annualHitRate; public Double annualWinLoss; public Double tradingDays; public Double avgLeverage; public Double maxLeverage; public java.util.ArrayList<String[]> tradeBook = new java.util.ArrayList();
  public java.util.TreeMap<Long, Double> consolMTM = new java.util.TreeMap();
  public com.q1.bt.global.BacktesterGlobal btGlobal;
  public String ppName;
  java.util.HashMap<String, Integer> tbIdxMap = getTradeBookIndexMap();
  
  com.q1.bt.process.backtest.PostProcessMode postProcessMode;
  

  public PostProcess(java.util.TreeMap<Long, Double> consolMTM, String ppName, java.util.ArrayList<String[]> tradeBook, com.q1.bt.global.BacktesterGlobal btGlobal, com.q1.bt.process.backtest.PostProcessMode postProcessMode)
  {
    this.consolMTM = trimMTMMap(consolMTM);
    this.ppName = ppName;
    this.tradeBook = tradeBook;
    this.btGlobal = btGlobal;
    this.postProcessMode = postProcessMode;
  }
  

  public java.util.TreeMap<Long, Double> trimMTMMap(java.util.TreeMap<Long, Double> consolMTM)
  {
    java.util.TreeMap<Long, Double> outputMTM = new java.util.TreeMap();
    
    boolean checkMTM = false;
    
    for (java.util.Map.Entry<Long, Double> entry : consolMTM.entrySet())
    {
      Double mtm = (Double)entry.getValue();
      
      if ((!checkMTM) && (com.q1.math.MathLib.doubleCompare(mtm, Double.valueOf(0.0D)).intValue() != 0)) {
        checkMTM = true;
      }
      if (checkMTM) {
        outputMTM.put((Long)entry.getKey(), mtm);
      }
    }
    
    return outputMTM;
  }
  
  public void writeToFileForFolder(String outputPath) throws java.io.IOException
  {
    String filePath = outputPath + "/Results";
    
    LinkedHashMap<String, String> resultsMap = new LinkedHashMap();
    
    if ((this.consolMTM.isEmpty()) || (this.tradeBook.size() == 0)) {
      resultsMap.put("Annual Return", "0.0%");
      resultsMap.put("Annual Vol", "0.0%");
      resultsMap.put("Return Per Trade", "0.0%");
      resultsMap.put("Sharpe Ratio", "0.0");
      resultsMap.put("Sortino Ratio", "0.0");
      resultsMap.put("Calmar Ratio", "0.0");
      resultsMap.put("Smooth Calmar Ratio", "0.0");
      resultsMap.put("Max Drawdown", "0.0%");
      resultsMap.put("Avg Drawdown", "0.0%");
      resultsMap.put("Max Drawdown Duration", "0 days");
      resultsMap.put("Avg Drawdown Duration", "0 days");
      resultsMap.put("Daily Win Loss", "0.0");
      resultsMap.put("Daily Hit Rate", "0.0%");
      resultsMap.put("Daily Expectancy", "0.0");
      resultsMap.put("Profit Factor", "0.0");
      resultsMap.put("Avg Trade Duration", "0 days");
      resultsMap.put("Win-Trade Duration", "0 days");
      resultsMap.put("Loss-Trade Duration", "0 days");
      resultsMap.put("Average Slippage", "0.0%");
      resultsMap.put("Trade Win Loss", "0.0");
      resultsMap.put("Trade Hit Rate", "0.0%");
      resultsMap.put("Trade Count", "0");
      resultsMap.put("Trading Days", "0%");
      resultsMap.put("Max Trades Per Day", "0.0");
      resultsMap.put("Avg Trades Per Day", "0.0");
      resultsMap.put("Open Slippage", "0.0%");
      resultsMap.put("Normal Slippage", "0.0%");
      resultsMap.put("Slippage Factor", "0.0");
      resultsMap.put("Normal Slippage Factor", "0.0");
      resultsMap.put("Open Trades", "0.0%");
      resultsMap.put("Avg Leverage", "0.0");
      resultsMap.put("Max Leverage", "0.0");
    } else {
      resultsMap.put("Annual Return", this.annualReturn + "%");
      resultsMap.put("Annual Vol", this.annualVolatility + "%");
      resultsMap.put("Return Per Trade", this.avgTradeReturn + "%");
      resultsMap.put("Sharpe Ratio", this.mtmSharpe.toString());
      resultsMap.put("Sortino Ratio", this.mtmSortino.toString());
      resultsMap.put("Calmar Ratio", this.mtmCalmar.toString());
      resultsMap.put("Smooth Calmar Ratio", this.mtmSmoothCalmar.toString());
      resultsMap.put("Max Drawdown", this.maxDrawdown + "%");
      resultsMap.put("Avg Drawdown", this.avgDrawdown + "%");
      resultsMap.put("Max Drawdown Duration", this.maxDrawdownDuration + " days");
      resultsMap.put("Avg Drawdown Duration", this.avgDrawdownDuration + " days");
      resultsMap.put("Daily Win Loss", this.mtmWinLoss.toString());
      resultsMap.put("Daily Hit Rate", this.mtmHitRate + "%");
      resultsMap.put("Daily Expectancy", this.expectancy.toString());
      resultsMap.put("Profit Factor", this.profitFactor.toString());
      resultsMap.put("Avg Trade Duration", getDurationVal(this.avgTradeDuration));
      resultsMap.put("Win-Trade Duration", getDurationVal(this.avgWinDuration));
      resultsMap.put("Loss-Trade Duration", getDurationVal(this.avgLossDuration));
      resultsMap.put("Average Slippage", this.avgSlippage + "%");
      resultsMap.put("Trade Win Loss", this.tradeWinLoss.toString());
      resultsMap.put("Trade Hit Rate", this.tradeHitRate + "%");
      resultsMap.put("Trade Count", this.tradeCount.toString());
      resultsMap.put("Trading Days", this.tradingDays + "%");
      resultsMap.put("Max Trades Per Day", this.maxTradesPerDay.toString());
      resultsMap.put("Avg Trades Per Day", this.avgTradePerDay.toString());
      resultsMap.put("Open Slippage", this.avgOpenSlippage + "%");
      resultsMap.put("Normal Slippage", this.avgNormSlippage + "%");
      resultsMap.put("Slippage Factor", this.slippageFactor);
      resultsMap.put("Normal Slippage Factor", this.normSlippageFactor);
      resultsMap.put("Open Trades", this.opTradePerc + "%");
      resultsMap.put("Avg Leverage", this.avgLeverage.toString());
      resultsMap.put("Max Leverage", this.maxLeverage.toString());
    }
    
    java.util.ArrayList<String[]> currentData = new java.util.ArrayList();
    if (new java.io.File(filePath + "/Performance.csv").exists()) {
      com.q1.csv.CSVReader reader = new com.q1.csv.CSVReader(filePath + "/Performance.csv", ',', 0);
      currentData = reader.readAll();
      reader.close();
    }
    if (currentData.size() == 0) {
      com.q1.csv.CSVWriter writer = new com.q1.csv.CSVWriter(filePath + "/Performance.csv", false, ",");
      String[] header = { "Parameters", this.ppName };
      writer.writeLine(header);
      for (java.util.Map.Entry<String, String> entry : resultsMap.entrySet()) {
        String[] outData = { (String)entry.getKey(), (String)entry.getValue() };
        writer.writeLine(outData);
      }
      writer.close();
    } else {
      com.q1.csv.CSVWriter writer = new com.q1.csv.CSVWriter(filePath + "/Performance.csv", false, ",");
      String[] curHeader = (String[])currentData.get(0);
      int len = curHeader.length;
      String[] header = new String[len + 1];
      for (int k = 0; k < len; k++) {
        header[k] = curHeader[k];
      }
      header[len] = this.ppName;
      writer.writeLine(header);
      for (int i = 1; i < currentData.size(); i++) {
        String[] curData = (String[])currentData.get(i);
        String[] outData = new String[len + 1];
        String key = curData[0];
        for (int k = 0; k < len; k++)
          outData[k] = curData[k];
        outData[len] = ((String)resultsMap.get(key));
        writer.writeLine(outData);
      }
      writer.close();
    }
  }
  
  public void writeToFile(String btTimeStamp)
    throws java.io.IOException
  {
    String filePath = this.btGlobal.loginParameter.getOutputPath() + "/" + btTimeStamp + "/Results";
    if (!new java.io.File(filePath).exists()) {
      new java.io.File(filePath).mkdirs();
    }
    LinkedHashMap<String, String> resultsMap = new LinkedHashMap();
    
    if ((this.consolMTM.isEmpty()) || (this.tradeBook.size() == 0)) {
      resultsMap.put("Annual Return", "0.0%");
      resultsMap.put("Annual Vol", "0.0%");
      resultsMap.put("Return Per Trade", "0.0%");
      resultsMap.put("Sharpe Ratio", "0.0");
      resultsMap.put("Sortino Ratio", "0.0");
      resultsMap.put("Calmar Ratio", "0.0");
      resultsMap.put("Smooth Calmar Ratio", "0.0");
      resultsMap.put("Max Drawdown", "0.0%");
      resultsMap.put("Avg Drawdown", "0.0%");
      resultsMap.put("Max Drawdown Duration", "0 days");
      resultsMap.put("Avg Drawdown Duration", "0 days");
      resultsMap.put("Daily Win Loss", "0.0");
      resultsMap.put("Daily Hit Rate", "0.0%");
      resultsMap.put("Daily Expectancy", "0.0");
      resultsMap.put("Profit Factor", "0.0");
      resultsMap.put("Avg Trade Duration", "0 days");
      resultsMap.put("Win-Trade Duration", "0 days");
      resultsMap.put("Loss-Trade Duration", "0 days");
      resultsMap.put("Average Slippage", "0.0%");
      resultsMap.put("Trade Win Loss", "0.0");
      resultsMap.put("Trade Hit Rate", "0.0%");
      resultsMap.put("Trade Count", "0");
      resultsMap.put("Trading Days", "0%");
      resultsMap.put("Max Trades Per Day", "0.0");
      resultsMap.put("Avg Trades Per Day", "0.0");
      resultsMap.put("Open Slippage", "0.0%");
      resultsMap.put("Normal Slippage", "0.0%");
      resultsMap.put("Slippage Factor", "0.0");
      resultsMap.put("Normal Slippage Factor", "0.0");
      resultsMap.put("Open Trades", "0.0%");
      resultsMap.put("Avg Leverage", "0.0");
      resultsMap.put("Max Leverage", "0.0");
    } else {
      resultsMap.put("Annual Return", this.annualReturn + "%");
      resultsMap.put("Annual Vol", this.annualVolatility + "%");
      resultsMap.put("Return Per Trade", this.avgTradeReturn + "%");
      resultsMap.put("Sharpe Ratio", this.mtmSharpe.toString());
      resultsMap.put("Sortino Ratio", this.mtmSortino.toString());
      resultsMap.put("Calmar Ratio", this.mtmCalmar.toString());
      resultsMap.put("Smooth Calmar Ratio", this.mtmSmoothCalmar.toString());
      resultsMap.put("Max Drawdown", this.maxDrawdown + "%");
      resultsMap.put("Avg Drawdown", this.avgDrawdown + "%");
      resultsMap.put("Max Drawdown Duration", this.maxDrawdownDuration + " days");
      resultsMap.put("Avg Drawdown Duration", this.avgDrawdownDuration + " days");
      resultsMap.put("Daily Win Loss", this.mtmWinLoss.toString());
      resultsMap.put("Daily Hit Rate", this.mtmHitRate + "%");
      resultsMap.put("Daily Expectancy", this.expectancy.toString());
      resultsMap.put("Profit Factor", this.profitFactor.toString());
      resultsMap.put("Avg Trade Duration", getDurationVal(this.avgTradeDuration));
      resultsMap.put("Win-Trade Duration", getDurationVal(this.avgWinDuration));
      resultsMap.put("Loss-Trade Duration", getDurationVal(this.avgLossDuration));
      resultsMap.put("Average Slippage", this.avgSlippage + "%");
      resultsMap.put("Trade Win Loss", this.tradeWinLoss.toString());
      resultsMap.put("Trade Hit Rate", this.tradeHitRate + "%");
      resultsMap.put("Trade Count", this.tradeCount.toString());
      resultsMap.put("Trading Days", this.tradingDays + "%");
      resultsMap.put("Max Trades Per Day", this.maxTradesPerDay.toString());
      resultsMap.put("Avg Trades Per Day", this.avgTradePerDay.toString());
      resultsMap.put("Open Slippage", this.avgOpenSlippage + "%");
      resultsMap.put("Normal Slippage", this.avgNormSlippage + "%");
      resultsMap.put("Slippage Factor", this.slippageFactor);
      resultsMap.put("Normal Slippage Factor", this.normSlippageFactor);
      resultsMap.put("Open Trades", this.opTradePerc + "%");
      resultsMap.put("Avg Leverage", this.avgLeverage.toString());
      resultsMap.put("Max Leverage", this.maxLeverage.toString());
    }
    
    java.util.ArrayList<String[]> currentData = new java.util.ArrayList();
    if (new java.io.File(filePath + "/Performance.csv").exists()) {
      com.q1.csv.CSVReader reader = new com.q1.csv.CSVReader(filePath + "/Performance.csv", ',', 0);
      currentData = reader.readAll();
      reader.close();
    }
    if (currentData.size() == 0) {
      com.q1.csv.CSVWriter writer = new com.q1.csv.CSVWriter(filePath + "/Performance.csv", false, ",");
      String[] header = { "Parameters", this.ppName };
      writer.writeLine(header);
      for (java.util.Map.Entry<String, String> entry : resultsMap.entrySet()) {
        String[] outData = { (String)entry.getKey(), (String)entry.getValue() };
        writer.writeLine(outData);
      }
      writer.close();
    } else {
      com.q1.csv.CSVWriter writer = new com.q1.csv.CSVWriter(filePath + "/Performance.csv", false, ",");
      String[] curHeader = (String[])currentData.get(0);
      int len = curHeader.length;
      String[] header = new String[len + 1];
      for (int k = 0; k < len; k++) {
        header[k] = curHeader[k];
      }
      header[len] = this.ppName;
      writer.writeLine(header);
      for (int i = 1; i < currentData.size(); i++) {
        String[] curData = (String[])currentData.get(i);
        String[] outData = new String[len + 1];
        String key = curData[0];
        for (int k = 0; k < len; k++)
          outData[k] = curData[k];
        outData[len] = ((String)resultsMap.get(key));
        writer.writeLine(outData);
      }
      writer.close();
    }
  }
  

  public void runPostprocess()
    throws java.text.ParseException
  {
    Long dataCount = Long.valueOf(0L);
    Integer tradingCount = Integer.valueOf(0);
    int mtmCount = 0;
    int winCount = 0;
    int lossCount = 0;
    Double cumReturn = Double.valueOf(0.0D);
    Double cumWinReturn = Double.valueOf(0.0D);
    Double cumLossReturn = Double.valueOf(0.0D);
    Double currentMax = Double.valueOf(0.0D);
    Double drawdown = Double.valueOf(0.0D);Double prevDrawdown = Double.valueOf(0.0D);
    java.util.ArrayList<Long> durations = new java.util.ArrayList();
    java.util.ArrayList<Double> drawdowns = new java.util.ArrayList();
    Double curDrawdown = Double.valueOf(0.0D);
    java.util.Date drawStart = null;
    java.util.Date date = null;
    

    for (java.util.Map.Entry<Long, Double> entry : this.consolMTM.entrySet())
    {
      date = new java.text.SimpleDateFormat("yyyyMMdd").parse(((Long)entry.getKey()).toString());
      Double mtm = (Double)entry.getValue();
      
      cumReturn = Double.valueOf(cumReturn.doubleValue() + mtm.doubleValue());
      
      if (mtm.doubleValue() > 0.0D)
      {
        cumWinReturn = Double.valueOf(cumWinReturn.doubleValue() + mtm.doubleValue());
        winCount++;

      }
      else if (mtm.doubleValue() < 0.0D) {
        cumLossReturn = Double.valueOf(cumLossReturn.doubleValue() + mtm.doubleValue());
        lossCount++;
      }
      
      if (cumReturn.doubleValue() > currentMax.doubleValue()) {
        currentMax = cumReturn;
      }
      drawdown = Double.valueOf(currentMax.doubleValue() - cumReturn.doubleValue());
      
      if (drawdown.doubleValue() > curDrawdown.doubleValue()) {
        curDrawdown = drawdown;
      }
      if ((com.q1.math.MathLib.doubleCompare(drawdown, Double.valueOf(0.0D)).intValue() == 0) && (com.q1.math.MathLib.doubleCompare(prevDrawdown, Double.valueOf(0.0D)).intValue() > 0)) {
        java.util.Date drawEnd = date;
        drawdowns.add(curDrawdown);
        durations.add(Long.valueOf(com.q1.math.DateTime.getDateDiff(drawStart, drawEnd)));
        curDrawdown = Double.valueOf(0.0D);
        drawStart = null;
      }
      
      if ((drawStart == null) && 
        (com.q1.math.MathLib.doubleCompare(curDrawdown, Double.valueOf(0.0D)).intValue() > 0)) {
        drawStart = date;
      }
      prevDrawdown = drawdown;
      if (com.q1.math.MathLib.doubleCompare(mtm, Double.valueOf(0.0D)).intValue() != 0) {
        mtmCount++;
      }
    }
    if (com.q1.math.MathLib.doubleCompare(curDrawdown, Double.valueOf(0.0D)).intValue() != 0) {
      java.util.Date drawEnd = date;
      drawdowns.add(curDrawdown);
      durations.add(Long.valueOf(com.q1.math.DateTime.getDateDiff(drawStart, drawEnd)));
      curDrawdown = Double.valueOf(0.0D);
      drawStart = null;
    }
    java.util.Collections.sort(drawdowns);
    

    Double smoothDraw = Double.valueOf(0.0D);
    if (drawdowns.size() != 0) {
      smoothDraw = (Double)drawdowns.get(0);
      for (int i = 1; i < drawdowns.size(); i++) {
        smoothDraw = Double.valueOf(((Double)drawdowns.get(i)).doubleValue() * 0.5D + 0.5D * smoothDraw.doubleValue());
      }
    }
    
    if (com.q1.math.MathLib.doubleCompare(curDrawdown, Double.valueOf(0.0D)).intValue() > 0) {
      java.util.Date drawEnd = date;
      drawdowns.add(curDrawdown);
      durations.add(Long.valueOf(com.q1.math.DateTime.getDateDiff(drawStart, drawEnd)));
    }
    

    if ((this.consolMTM.isEmpty()) || (this.tradeBook.size() == 0)) {
      return;
    }
    
    java.util.Date startDate = new java.text.SimpleDateFormat("yyyyMMdd").parse(((Long)this.consolMTM.firstKey()).toString());
    java.util.Date endDate = new java.text.SimpleDateFormat("yyyyMMdd").parse(((Long)this.consolMTM.lastKey()).toString());
    
    dataCount = Long.valueOf(com.q1.math.DateTime.getDateDiff(startDate, endDate) + 1L);
    tradingCount = Integer.valueOf(this.consolMTM.size());
    
    Double tradingDayCount = Double.valueOf(365.0D * (tradingCount.doubleValue() / dataCount.doubleValue()));
    

    this.avgDailyReturn = Double.valueOf(cumReturn.doubleValue() / tradingCount.intValue());
    
    Double avgWinReturn = Double.valueOf(cumWinReturn.doubleValue() / winCount);
    
    Double avgLossReturn = Double.valueOf(0.0D);
    if (lossCount > 0) {
      avgLossReturn = Double.valueOf(-cumLossReturn.doubleValue() / lossCount);
    }
    
    Double cumSqrDeviation = Double.valueOf(0.0D);
    Double cumNegDeviation = Double.valueOf(0.0D);
    Integer negCount = Integer.valueOf(0);
    
    for (Double mtm : this.consolMTM.values()) {
      cumSqrDeviation = Double.valueOf(cumSqrDeviation.doubleValue() + Math.pow(mtm.doubleValue() - this.avgDailyReturn.doubleValue(), 2.0D));
      if (com.q1.math.MathLib.doubleCompare(Double.valueOf(mtm.doubleValue() - this.avgDailyReturn.doubleValue()), Double.valueOf(0.0D)).intValue() < 0) {
        cumNegDeviation = Double.valueOf(cumNegDeviation.doubleValue() + Math.pow(mtm.doubleValue() - this.avgDailyReturn.doubleValue(), 2.0D));
        negCount = Integer.valueOf(negCount.intValue() + 1);
      }
    }
    this.annualReturn = Double.valueOf(tradingDayCount.doubleValue() * (this.avgDailyReturn.doubleValue() * 100.0D));
    this.maxDrawdown = Double.valueOf(com.q1.math.MathLib.max(drawdowns) * 100.0D);
    this.avgDrawdown = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(com.q1.math.MathLib.simpleAvgDouble(drawdowns) * 100.0D, 0.01D));
    this.maxDrawdownDuration = com.q1.math.MathLib.maxLong(durations);
    this.avgDrawdownDuration = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(com.q1.math.MathLib.simpleAvg(durations), 0.01D));
    
    if (com.q1.math.MathLib.doubleCompare(this.maxDrawdown, Double.valueOf(0.0D)).intValue() == 0) {
      this.mtmCalmar = Double.valueOf(Double.POSITIVE_INFINITY);
    } else {
      this.mtmCalmar = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(this.annualReturn.doubleValue() / this.maxDrawdown.doubleValue(), 0.01D));
    }
    if (com.q1.math.MathLib.doubleCompare(smoothDraw, Double.valueOf(0.0D)).intValue() == 0) {
      this.mtmSmoothCalmar = Double.valueOf(Double.POSITIVE_INFINITY);
    } else {
      this.mtmSmoothCalmar = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(this.annualReturn.doubleValue() / (smoothDraw.doubleValue() * 100.0D), 0.01D));
    }
    this.maxDrawdown = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(this.maxDrawdown.doubleValue(), 0.01D));
    
    this.mtmStdDev = Double.valueOf(Math.sqrt(cumSqrDeviation.doubleValue() / tradingCount.intValue()));
    
    Double mtmNegDev = Double.valueOf(Math.sqrt(cumNegDeviation.doubleValue() / negCount.intValue()));
    
    this.mtmHitRate = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(winCount / (winCount + lossCount) * 100.0D, 0.01D));
    
    if (com.q1.math.MathLib.doubleCompare(avgLossReturn, Double.valueOf(0.0D)).intValue() == 0) {
      this.mtmWinLoss = Double.valueOf(Double.POSITIVE_INFINITY);
    } else {
      this.mtmWinLoss = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(avgWinReturn.doubleValue() / avgLossReturn.doubleValue(), 0.01D));
    }
    
    this.expectancy = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(this.mtmHitRate.doubleValue() / 100.0D * this.mtmWinLoss.doubleValue() - (1.0D - this.mtmHitRate.doubleValue() / 100.0D), 0.01D));
    

    if (com.q1.math.MathLib.doubleCompare(this.mtmStdDev, Double.valueOf(0.0D)).intValue() == 0) {
      this.mtmSharpe = Double.valueOf(Double.POSITIVE_INFINITY);
    } else {
      this.mtmSharpe = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(Math.sqrt(tradingDayCount.doubleValue()) * (this.avgDailyReturn.doubleValue() / this.mtmStdDev.doubleValue()), 0.01D));
    }
    if (com.q1.math.MathLib.doubleCompare(mtmNegDev, Double.valueOf(0.0D)).intValue() == 0) {
      this.mtmSortino = Double.valueOf(Double.POSITIVE_INFINITY);
    } else {
      this.mtmSortino = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(Math.sqrt(tradingDayCount.doubleValue()) * (this.avgDailyReturn.doubleValue() / mtmNegDev.doubleValue()), 
        0.01D));
    }
    this.annualReturn = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(this.annualReturn.doubleValue(), 0.01D));
    
    this.annualVolatility = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(this.annualReturn.doubleValue() / this.mtmSharpe.doubleValue(), 0.01D));
    
    this.tradingDays = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(100.0D * mtmCount / dataCount.longValue(), 0.01D));
    
    runTradePostProcess();
  }
  
  public void runTradePostProcess() throws java.text.ParseException
  {
    java.util.HashMap<String, Integer> tbIdxMap = getTradeBookIndexMap();
    java.util.HashMap<String, java.util.HashSet<String>> scripSet = getScripSet();
    
    TradePostProcess tradepp = new TradePostProcess(scripSet, tbIdxMap);
    
    if (this.postProcessMode.equals(com.q1.bt.process.backtest.PostProcessMode.SingleScrip)) {
      tradepp.runSingleTradePP(this.tradeBook);
    } else if (this.postProcessMode.equals(com.q1.bt.process.backtest.PostProcessMode.Spread)) {
      tradepp.runSpreadTradePP(this.tradeBook);
    } else if (this.postProcessMode.equals(com.q1.bt.process.backtest.PostProcessMode.Portfolio)) {
      tradepp.runPortfolioPP(this.tradeBook);
    } else {
      throw new Error("Unknown op mode");
    }
    
    this.avgTradeReturn = tradepp.avgTradeReturn;
    this.profitFactor = tradepp.profitFactor;
    this.avgTradeDuration = tradepp.avgTradeDuration;
    this.avgWinDuration = tradepp.avgWinDuration;
    this.avgLossDuration = tradepp.avgLossDuration;
    this.avgSlippage = tradepp.avgSlippage;
    this.avgOpenSlippage = tradepp.avgOpenSlippage;
    this.avgNormSlippage = tradepp.avgNormSlippage;
    this.tradeWinLoss = tradepp.tradeWinLoss;
    this.tradeHitRate = tradepp.tradeHitRate;
    this.tradeCount = Integer.valueOf(tradepp.tradeCount);
    this.slippageFactor = tradepp.slippageFactor;
    this.normSlippageFactor = tradepp.normSlippageFactor;
    this.opTradePerc = tradepp.opTradePerc;
    this.maxTradesPerDay = tradepp.maxTradesPerDay;
    this.avgTradePerDay = tradepp.avgTradePerDay;
    this.avgLeverage = tradepp.avgLeverage;
    this.maxLeverage = tradepp.maxLeverage;
  }
  
  public void printResults()
  {
    System.out.println("----- Backtest Results ------");
    System.out.println("Annual Return: " + this.annualReturn + "%");
    System.out.println("Annual Vol: " + this.annualVolatility + "%");
    System.out.println("Return Per Trade: " + this.avgTradeReturn + "%");
    System.out.println("Sharpe Ratio: " + this.mtmSharpe);
    System.out.println("Sortino Ratio: " + this.mtmSortino);
    System.out.println("Calmar Ratio: " + this.mtmCalmar);
    System.out.println("Smooth Calmar Ratio: " + this.mtmSmoothCalmar);
    System.out.println("Max Drawdown: " + this.maxDrawdown + "%");
    System.out.println("Avg Drawdown: " + this.avgDrawdown + "%");
    System.out.println("Max Drawdown Duration: " + this.maxDrawdownDuration + " days");
    System.out.println("Avg Drawdown Duration: " + this.avgDrawdownDuration + " days");
    System.out.println("Daily Win Loss: " + this.mtmWinLoss);
    System.out.println("Daily Hit Rate: " + this.mtmHitRate + "%");
    System.out.println("Daily Expectancy: " + this.expectancy);
    System.out.println("Profit Factor: " + this.profitFactor);
    System.out.println("Avg Trade Duration: " + getDurationVal(this.avgTradeDuration));
    System.out.println("Win-Trade Duration: " + getDurationVal(this.avgWinDuration));
    System.out.println("Loss-Trade Duration: " + getDurationVal(this.avgLossDuration));
    System.out.println("Average Slippage: " + this.avgSlippage + "%");
    System.out.println("Trade Win Loss: " + this.tradeWinLoss);
    System.out.println("Trade Hit Rate: " + this.tradeHitRate + "%");
    System.out.println("Trade Count: " + this.tradeCount);
    System.out.println("Open Trades: " + this.opTradePerc);
    System.out.println("Avg Leverage: " + this.avgLeverage);
    System.out.println("Max Leverage: " + this.maxLeverage);
  }
  
  public void displayResults(com.q1.bt.global.BacktesterNonGUI bt) {
    printResults();
  }
  
  public String getDurationVal(Double avgTradeDuration)
  {
    Double curDuration = Double.valueOf(0.0D);
    String durationType; if (avgTradeDuration.doubleValue() > 1440.0D) {
      String durationType = "Days";
      curDuration = Double.valueOf(avgTradeDuration.doubleValue() / 1440.0D);
    } else if (avgTradeDuration.doubleValue() > 60.0D) {
      String durationType = "Hours";
      curDuration = Double.valueOf(avgTradeDuration.doubleValue() / 60.0D);
    } else {
      curDuration = avgTradeDuration;
      durationType = "Minutes";
    }
    curDuration = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(curDuration.doubleValue(), 0.01D));
    return curDuration + " " + durationType;
  }
  
  public java.util.HashMap<String, Integer> getTradeBookIndexMap() {
    java.util.HashMap<String, Integer> tbIdxMap = new java.util.HashMap();
    
    tbIdxMap.put("dateTime", Integer.valueOf(0));
    tbIdxMap.put("capital", Integer.valueOf(1));
    tbIdxMap.put("scripID", Integer.valueOf(2));
    tbIdxMap.put("orderSide", Integer.valueOf(3));
    tbIdxMap.put("orderType", Integer.valueOf(4));
    tbIdxMap.put("triggerPrice", Integer.valueOf(5));
    tbIdxMap.put("execPrice", Integer.valueOf(6));
    tbIdxMap.put("qty", Integer.valueOf(7));
    tbIdxMap.put("execType", Integer.valueOf(8));
    tbIdxMap.put("scripListID", Integer.valueOf(9));
    return tbIdxMap;
  }
  

  public java.util.HashMap<String, java.util.HashSet<String>> getScripSet()
  {
    java.util.HashMap<String, java.util.HashSet<String>> scripset = new java.util.HashMap();
    
    for (int i = 0; i < this.tradeBook.size(); i++)
    {
      String[] trade = (String[])this.tradeBook.get(i);
      
      if (scripset.get(trade[((Integer)this.tbIdxMap.get("scripListID")).intValue()]) == null) {
        java.util.HashSet<String> scripList = new java.util.HashSet();
        scripList.add(trade[((Integer)this.tbIdxMap.get("scripID")).intValue()]);
        scripset.put(trade[((Integer)this.tbIdxMap.get("scripListID")).intValue()], scripList);
      } else {
        java.util.HashSet<String> scripList = (java.util.HashSet)scripset.get(trade[((Integer)this.tbIdxMap.get("scripListID")).intValue()]);
        scripList.add(trade[((Integer)this.tbIdxMap.get("scripID")).intValue()]);
        scripset.put(trade[((Integer)this.tbIdxMap.get("scripListID")).intValue()], scripList);
      }
    }
    

    return scripset;
  }
  
  public java.util.HashMap<String, Double> getResultsMap()
  {
    java.util.HashMap<String, Double> resultsMap = new java.util.HashMap();
    if ((this.consolMTM.isEmpty()) || (this.tradeBook.size() == 0)) {
      resultsMap.put("Annual Return", Double.valueOf(0.0D));
      resultsMap.put("Annual Vol", Double.valueOf(0.0D));
      resultsMap.put("Return Per Trade", Double.valueOf(0.0D));
      resultsMap.put("Sharpe Ratio", Double.valueOf(0.0D));
      resultsMap.put("Sortino Ratio", Double.valueOf(0.0D));
      resultsMap.put("Calmar Ratio", Double.valueOf(0.0D));
      resultsMap.put("Smooth Calmar Ratio", Double.valueOf(0.0D));
      resultsMap.put("Max Drawdown", Double.valueOf(0.0D));
      resultsMap.put("Avg Drawdown", Double.valueOf(0.0D));
      resultsMap.put("Max Drawdown Duration", Double.valueOf(0.0D));
      resultsMap.put("Avg Drawdown Duration", Double.valueOf(0.0D));
      resultsMap.put("Daily Win Loss", Double.valueOf(0.0D));
      resultsMap.put("Daily Hit Rate", Double.valueOf(0.0D));
      resultsMap.put("Daily Expectancy", Double.valueOf(0.0D));
      resultsMap.put("Profit Factor", Double.valueOf(0.0D));
      resultsMap.put("Avg Trade Duration", Double.valueOf(0.0D));
      resultsMap.put("Win-Trade Duration", Double.valueOf(0.0D));
      resultsMap.put("Loss-Trade Duration", Double.valueOf(0.0D));
      resultsMap.put("Average Slippage", Double.valueOf(0.0D));
      resultsMap.put("Trade Win Loss", Double.valueOf(0.0D));
      resultsMap.put("Trade Hit Rate", Double.valueOf(0.0D));
      resultsMap.put("Trade Count", Double.valueOf(0.0D));
      resultsMap.put("Trading Days", Double.valueOf(0.0D));
      resultsMap.put("Max Trades Per Day", Double.valueOf(0.0D));
      resultsMap.put("Avg Trades Per Day", Double.valueOf(0.0D));
      resultsMap.put("Open Slippage", Double.valueOf(0.0D));
      resultsMap.put("Normal Slippage", Double.valueOf(0.0D));
      resultsMap.put("Slippage Factor", Double.valueOf(0.0D));
      resultsMap.put("Normal Slippage Factor", Double.valueOf(0.0D));
      resultsMap.put("Open Trades", Double.valueOf(0.0D));
      resultsMap.put("Avg Leverage", Double.valueOf(0.0D));
      resultsMap.put("Max Leverage", Double.valueOf(0.0D));
    } else {
      resultsMap.put("Annual Return", this.annualReturn);
      resultsMap.put("Annual Vol", this.annualVolatility);
      resultsMap.put("Return Per Trade", this.avgTradeReturn);
      resultsMap.put("Sharpe Ratio", this.mtmSharpe);
      resultsMap.put("Sortino Ratio", this.mtmSortino);
      resultsMap.put("Calmar Ratio", this.mtmCalmar);
      resultsMap.put("Smooth Calmar Ratio", this.mtmSmoothCalmar);
      resultsMap.put("Max Drawdown", this.maxDrawdown);
      resultsMap.put("Avg Drawdown", this.avgDrawdown);
      resultsMap.put("Max Drawdown Duration", Double.valueOf(this.maxDrawdownDuration.longValue()));
      resultsMap.put("Avg Drawdown Duration", this.avgDrawdownDuration);
      resultsMap.put("Daily Win Loss", this.mtmWinLoss);
      resultsMap.put("Daily Hit Rate", this.mtmHitRate);
      resultsMap.put("Daily Expectancy", this.expectancy);
      resultsMap.put("Profit Factor", this.profitFactor);
      resultsMap.put("Avg Trade Duration", this.avgTradeDuration);
      resultsMap.put("Win-Trade Duration", this.avgWinDuration);
      resultsMap.put("Loss-Trade Duration", this.avgLossDuration);
      resultsMap.put("Average Slippage", this.avgSlippage);
      resultsMap.put("Trade Win Loss", this.tradeWinLoss);
      resultsMap.put("Trade Hit Rate", this.tradeHitRate);
      resultsMap.put("Trade Count", Double.valueOf(this.tradeCount.intValue()));
      resultsMap.put("Trading Days", this.tradingDays);
      resultsMap.put("Max Trades Per Day", this.maxTradesPerDay);
      resultsMap.put("Avg Trades Per Day", this.avgTradePerDay);
      resultsMap.put("Open Slippage", this.avgOpenSlippage);
      resultsMap.put("Normal Slippage", this.avgNormSlippage);
      resultsMap.put("Slippage Factor", this.slippageFactor);
      resultsMap.put("Normal Slippage Factor", this.normSlippageFactor);
      resultsMap.put("Open Trades", this.opTradePerc);
      resultsMap.put("Avg Leverage", this.avgLeverage);
      resultsMap.put("Max Leverage", this.maxLeverage);
    }
    
    return resultsMap;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/PostProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */