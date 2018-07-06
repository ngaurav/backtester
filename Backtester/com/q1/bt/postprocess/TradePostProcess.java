package com.q1.bt.postprocess;

import com.q1.math.DateTime;
import com.q1.math.MathLib;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;




public class TradePostProcess
{
  Double normSlipVal;
  Long normSlipQty;
  Long openTradeQty;
  Double opSlipVal;
  Long opSlipQty;
  Long totalTradeQty;
  Double dollarPNL;
  Double slippagePNL;
  Double normSlippagePNL;
  ArrayList<Long> tradeDurations = new ArrayList();
  ArrayList<Long> winDurations = new ArrayList();
  ArrayList<Long> lossDurations = new ArrayList();
  ArrayList<Double> tradePLs = new ArrayList();
  ArrayList<Double> winPLs = new ArrayList();
  ArrayList<Double> lossPLs = new ArrayList();
  ArrayList<Double> leverageVals = new ArrayList();
  HashMap<String, Double> tradeCounts = new HashMap();
  HashMap<String, HashSet<String>> scripSet = new HashMap();
  HashMap<String, Integer> tbIdxMap = new HashMap();
  
  int winCount;
  
  int lossCount;
  
  int tradeCount;
  Double avgTradeDuration;
  Double avgWinDuration;
  Double avgLossDuration;
  Double avgLeverage;
  Double maxLeverage;
  Double avgSlippage;
  Double avgOpenSlippage;
  Double avgNormSlippage;
  Double avgTradeReturn;
  Double slippageFactor;
  Double normSlippageFactor;
  Double tradeWinLoss;
  Double profitFactor;
  Double tradeHitRate;
  Double opTradePerc;
  Double maxTradesPerDay;
  Double avgTradePerDay;
  int scripCount;
  Double tradePnL;
  Double slipVal;
  Double slipQty;
  HashMap<String, HashMap<String, Long>> posMap = new HashMap();
  
  Integer last_exit_trade;
  
  public TradePostProcess(HashMap<String, HashSet<String>> scripSet, HashMap<String, Integer> tbIdxmap)
  {
    this.winCount = (this.lossCount = this.tradeCount = 0);
    this.normSlipVal = Double.valueOf(0.0D);
    this.normSlipQty = Long.valueOf(0L);
    this.openTradeQty = Long.valueOf(0L);
    this.opSlipVal = Double.valueOf(0.0D);
    this.totalTradeQty = Long.valueOf(0L);
    this.opSlipQty = Long.valueOf(0L);
    this.dollarPNL = Double.valueOf(0.0D);
    this.slippagePNL = Double.valueOf(0.0D);
    this.normSlippagePNL = Double.valueOf(0.0D);
    this.tradeDurations = new ArrayList();
    this.winDurations = new ArrayList();
    this.lossDurations = new ArrayList();
    this.tradePLs = new ArrayList();
    this.winPLs = new ArrayList();
    this.lossPLs = new ArrayList();
    this.leverageVals = new ArrayList();
    this.tradeCounts = new HashMap();
    this.scripSet = scripSet;
    this.tbIdxMap = tbIdxmap;
    
    for (String scripListID : scripSet.keySet()) {
      HashMap<String, Long> scripListPosMap = new HashMap();
      this.scripCount = 0;
      for (String scripID : (HashSet)scripSet.get(scripListID)) {
        scripListPosMap.put(scripID, Long.valueOf(0L));
        this.scripCount += 1;
      }
      
      this.posMap.put(scripListID, scripListPosMap);
    }
  }
  
  public void runSpreadTradePP(ArrayList<String[]> tradeBook)
    throws ParseException
  {
    boolean spreadinPos = false;
    this.tradePnL = Double.valueOf(0.0D);
    this.slipVal = Double.valueOf(0.0D);
    this.slipQty = Double.valueOf(0.0D);
    this.last_exit_trade = Integer.valueOf(0);
    
    Date tradeStartDT = null;Date tradeEndDT = null;
    int i = 0;
    while (i < tradeBook.size())
    {
      String[] trade = (String[])tradeBook.get(i);
      
      if (!spreadinPos)
      {

        Date dT = new SimpleDateFormat("yyyyMMddHHmmss").parse(trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()]);
        tradeStartDT = dT;
        

        for (int j = 0; j < this.scripCount; j++) {
          trade = (String[])tradeBook.get(i + j);
          processTrade(trade, true);
        }
        
        i = i + this.scripCount - 1;
        spreadinPos = true;
      }
      else {
        boolean exit = processTrade(trade, false);
        


        if (exit)
        {
          for (int j = 1; j < this.scripCount; j++) {
            trade = (String[])tradeBook.get(i + j);
            boolean exit_sync = processTrade(trade, false);
            if (!exit_sync) {
              throw new Error("Unsync exit. Please make sure all scrips in a scriplist have trade exits at the same dateTime");
            }
          }
          

          Date dT = new SimpleDateFormat("yyyyMMddHHmmss").parse(trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()]);
          tradeEndDT = dT;
          
          long tradeDuration = DateTime.getDateTimeDiff(tradeStartDT, tradeEndDT);
          this.tradeDurations.add(Long.valueOf(tradeDuration));
          this.tradePLs.add(this.tradePnL);
          if (MathLib.doubleCompare(this.tradePnL, Double.valueOf(0.0D)).intValue() > 0) {
            this.winPLs.add(this.tradePnL);
            this.winDurations.add(Long.valueOf(tradeDuration));
            this.winCount += 1;
          } else if (MathLib.doubleCompare(this.tradePnL, Double.valueOf(0.0D)).intValue() < 0) {
            this.lossPLs.add(Double.valueOf(Math.abs(this.tradePnL.doubleValue())));
            this.lossDurations.add(Long.valueOf(tradeDuration));
            this.lossCount += 1;
          }
          this.tradeCount += 1;
          

          this.tradePnL = Double.valueOf(0.0D);
          spreadinPos = false;
          
          i = i + this.scripCount - 1;
        }
      }
      i++;
    }
    

    this.avgTradeDuration = Double.valueOf(MathLib.simpleAvg(this.tradeDurations));
    this.avgWinDuration = Double.valueOf(MathLib.simpleAvg(this.winDurations));
    this.avgLossDuration = Double.valueOf(MathLib.simpleAvg(this.lossDurations));
    this.avgLeverage = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.simpleAvg(this.leverageVals), 0.01D));
    this.maxLeverage = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.max(this.leverageVals), 0.01D));
    this.avgSlippage = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.slipVal.doubleValue() / this.slipQty.doubleValue()), 0.001D));
    this.avgOpenSlippage = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.opSlipVal.doubleValue() / this.opSlipQty.longValue()), 0.001D));
    this.avgNormSlippage = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.normSlipVal.doubleValue() / this.normSlipQty.longValue()), 0.001D));
    this.avgTradeReturn = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * MathLib.simpleAvgDouble(this.tradePLs), 0.001D));
    this.slippageFactor = Double.valueOf(MathLib.roundTickWithPrecision(1.0D + this.dollarPNL.doubleValue() / this.slippagePNL.doubleValue(), 0.01D));
    this.normSlippageFactor = Double.valueOf(MathLib.roundTickWithPrecision(1.0D + this.dollarPNL.doubleValue() / this.normSlippagePNL.doubleValue(), 0.01D));
    if (this.lossPLs.size() == 0) {
      this.tradeWinLoss = Double.valueOf(Double.POSITIVE_INFINITY);
    } else
      this.tradeWinLoss = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.simpleAvgDouble(this.winPLs) / MathLib.simpleAvgDouble(this.lossPLs), 0.01D));
    if (this.lossPLs.size() == 0) {
      this.profitFactor = Double.valueOf(Double.POSITIVE_INFINITY);
    } else
      this.profitFactor = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.sumDouble(this.winPLs) / MathLib.sumDouble(this.lossPLs), 0.01D));
    this.tradeHitRate = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * this.winCount / (this.winCount + this.lossCount), 0.01D));
    this.opTradePerc = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.openTradeQty.longValue() / this.totalTradeQty.longValue()), 0.01D));
    
    this.maxTradesPerDay = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.max(this.tradeCounts.values()) / 2.0D, 0.01D));
    this.avgTradePerDay = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.simpleAvg(this.tradeCounts.values()) / 2.0D, 0.01D));
  }
  
  public void runSingleTradePP(ArrayList<String[]> tradeBook)
    throws ParseException
  {
    boolean inPos = false;
    this.tradePnL = Double.valueOf(0.0D);
    this.slipVal = Double.valueOf(0.0D);
    this.slipQty = Double.valueOf(0.0D);
    this.last_exit_trade = Integer.valueOf(0);
    
    Date tradeStartDT = null;Date tradeEndDT = null;
    int i = 0;
    while (i < tradeBook.size())
    {
      String[] trade = (String[])tradeBook.get(i);
      
      if (!inPos)
      {
        Date dT = new SimpleDateFormat("yyyyMMddHHmmss").parse(trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()]);
        tradeStartDT = dT;
        

        trade = (String[])tradeBook.get(i);
        processTrade(trade, true);
        inPos = true;
      }
      else {
        boolean exit = processTrade(trade, false);
        


        if (exit)
        {

          Date dT = new SimpleDateFormat("yyyyMMddHHmmss").parse(trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()]);
          tradeEndDT = dT;
          
          long tradeDuration = DateTime.getDateTimeDiff(tradeStartDT, tradeEndDT);
          this.tradeDurations.add(Long.valueOf(tradeDuration));
          this.tradePLs.add(this.tradePnL);
          if (MathLib.doubleCompare(this.tradePnL, Double.valueOf(0.0D)).intValue() > 0) {
            this.winPLs.add(this.tradePnL);
            this.winDurations.add(Long.valueOf(tradeDuration));
            this.winCount += 1;
          } else if (MathLib.doubleCompare(this.tradePnL, Double.valueOf(0.0D)).intValue() < 0) {
            this.lossPLs.add(Double.valueOf(Math.abs(this.tradePnL.doubleValue())));
            this.lossDurations.add(Long.valueOf(tradeDuration));
            this.lossCount += 1;
          }
          this.tradeCount += 1;
          

          this.tradePnL = Double.valueOf(0.0D);
          inPos = false;
        }
      }
      i++;
    }
    

    this.avgTradeDuration = Double.valueOf(MathLib.simpleAvg(this.tradeDurations));
    this.avgWinDuration = Double.valueOf(MathLib.simpleAvg(this.winDurations));
    this.avgLossDuration = Double.valueOf(MathLib.simpleAvg(this.lossDurations));
    this.avgLeverage = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.simpleAvg(this.leverageVals), 0.01D));
    this.maxLeverage = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.max(this.leverageVals), 0.01D));
    this.avgSlippage = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.slipVal.doubleValue() / this.slipQty.doubleValue()), 0.001D));
    this.avgOpenSlippage = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.opSlipVal.doubleValue() / this.opSlipQty.longValue()), 0.001D));
    this.avgNormSlippage = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.normSlipVal.doubleValue() / this.normSlipQty.longValue()), 0.001D));
    this.avgTradeReturn = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * MathLib.simpleAvgDouble(this.tradePLs), 0.001D));
    this.slippageFactor = Double.valueOf(MathLib.roundTickWithPrecision(1.0D + this.dollarPNL.doubleValue() / this.slippagePNL.doubleValue(), 0.01D));
    this.normSlippageFactor = Double.valueOf(MathLib.roundTickWithPrecision(1.0D + this.dollarPNL.doubleValue() / this.normSlippagePNL.doubleValue(), 0.01D));
    if (this.lossPLs.size() == 0) {
      this.tradeWinLoss = Double.valueOf(Double.POSITIVE_INFINITY);
    } else
      this.tradeWinLoss = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.simpleAvgDouble(this.winPLs) / MathLib.simpleAvgDouble(this.lossPLs), 0.01D));
    if (this.lossPLs.size() == 0) {
      this.profitFactor = Double.valueOf(Double.POSITIVE_INFINITY);
    } else
      this.profitFactor = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.sumDouble(this.winPLs) / MathLib.sumDouble(this.lossPLs), 0.01D));
    this.tradeHitRate = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * this.winCount / (this.winCount + this.lossCount), 0.01D));
    this.opTradePerc = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.openTradeQty.longValue() / this.totalTradeQty.longValue()), 0.01D));
    this.maxTradesPerDay = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.max(this.tradeCounts.values()) / 2.0D, 0.01D));
    this.avgTradePerDay = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.simpleAvg(this.tradeCounts.values()) / 2.0D, 0.01D));
  }
  

  public void runPortfolioPP(ArrayList<String[]> tradebook)
  {
    this.avgTradeDuration = Double.valueOf(0.0D);
    this.avgWinDuration = Double.valueOf(0.0D);
    this.avgLossDuration = Double.valueOf(0.0D);
    this.avgLeverage = Double.valueOf(0.0D);
    this.maxLeverage = Double.valueOf(0.0D);
    this.avgSlippage = Double.valueOf(0.0D);
    this.avgOpenSlippage = Double.valueOf(0.0D);
    this.avgNormSlippage = Double.valueOf(0.0D);
    this.avgTradeReturn = Double.valueOf(0.0D);
    this.slippageFactor = Double.valueOf(0.0D);
    this.normSlippageFactor = Double.valueOf(0.0D);
    this.tradeWinLoss = Double.valueOf(0.0D);
    this.profitFactor = Double.valueOf(0.0D);
    this.tradeHitRate = Double.valueOf(0.0D);
    this.opTradePerc = Double.valueOf(0.0D);
    this.maxTradesPerDay = Double.valueOf(0.0D);
    this.avgTradePerDay = Double.valueOf(0.0D);
  }
  
  public boolean processTrade(String[] trade, boolean entry) throws ParseException
  {
    Date dT = new SimpleDateFormat("yyyyMMddHHmmss").parse(trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()]);
    String dateStr = new SimpleDateFormat("yyyyMMdd").format(dT);
    Long orderQty = Long.valueOf(Long.parseLong(trade[((Integer)this.tbIdxMap.get("qty")).intValue()]));
    Double execPrice = Double.valueOf(Double.parseDouble(trade[((Integer)this.tbIdxMap.get("execPrice")).intValue()]));
    Double capital = Double.valueOf(Double.parseDouble(trade[((Integer)this.tbIdxMap.get("capital")).intValue()]));
    String orderSide = trade[((Integer)this.tbIdxMap.get("orderSide")).intValue()];
    String orderType = trade[((Integer)this.tbIdxMap.get("orderType")).intValue()];
    Double triggerPrice = Double.valueOf(Double.parseDouble(trade[((Integer)this.tbIdxMap.get("triggerPrice")).intValue()]));
    String triggerType = trade[((Integer)this.tbIdxMap.get("execType")).intValue()];
    String scripID = trade[((Integer)this.tbIdxMap.get("scripID")).intValue()];
    String scripListID = trade[((Integer)this.tbIdxMap.get("scripListID")).intValue()];
    

    if (!orderType.equals("ROLLOVER")) {
      Double curCount = (Double)this.tradeCounts.get(dateStr);
      if (curCount == null) {
        this.tradeCounts.put(dateStr, Double.valueOf(1.0D));
      } else {
        this.tradeCounts.put(dateStr, Double.valueOf(curCount.doubleValue() + 1.0D));
      }
      this.totalTradeQty = Long.valueOf(this.totalTradeQty.longValue() + orderQty.longValue());
      if (triggerType.equalsIgnoreCase("OP")) {
        this.openTradeQty = Long.valueOf(this.openTradeQty.longValue() + orderQty.longValue());
      }
    }
    
    int tradeSignal = 0;
    if (orderSide.equalsIgnoreCase("Buy")) {
      tradeSignal = 1;
    } else if (orderSide.equalsIgnoreCase("Sell")) {
      tradeSignal = -1;
    }
    this.tradePnL = Double.valueOf(this.tradePnL.doubleValue() + execPrice.doubleValue() * orderQty.longValue() * -tradeSignal / capital.doubleValue());
    this.dollarPNL = Double.valueOf(this.dollarPNL.doubleValue() + execPrice.doubleValue() * orderQty.longValue() * -tradeSignal);
    

    if ((orderType.equals("STOP")) || (orderType.equals("EOD")) || (orderType.equals("OPEN")))
    {
      Double slip = Double.valueOf(tradeSignal * ((execPrice.doubleValue() - triggerPrice.doubleValue()) * orderQty.longValue()) / triggerPrice.doubleValue());
      this.slippagePNL = Double.valueOf(this.slippagePNL.doubleValue() + tradeSignal * ((execPrice.doubleValue() - triggerPrice.doubleValue()) * orderQty.longValue()));
      this.slipVal = Double.valueOf(this.slipVal.doubleValue() + slip.doubleValue());
      this.slipQty = Double.valueOf(this.slipQty.doubleValue() + orderQty.longValue());
      
      if (triggerType.equalsIgnoreCase("OP")) {
        this.opSlipVal = Double.valueOf(this.opSlipVal.doubleValue() + slip.doubleValue());
        this.opSlipQty = Long.valueOf(this.opSlipQty.longValue() + orderQty.longValue());
      }
      else
      {
        this.normSlippagePNL = Double.valueOf(this.normSlippagePNL.doubleValue() + tradeSignal * ((execPrice.doubleValue() - triggerPrice.doubleValue()) * orderQty.longValue()));
        this.normSlipVal = Double.valueOf(this.normSlipVal.doubleValue() + slip.doubleValue());
        this.normSlipQty = Long.valueOf(this.normSlipQty.longValue() + orderQty.longValue());
      }
    }
    

    Long newpos = Long.valueOf(((Long)((HashMap)this.posMap.get(scripListID)).get(scripID)).longValue() + tradeSignal * orderQty.longValue());
    
    HashMap<String, Long> scripListPosMap = (HashMap)this.posMap.get(scripListID);
    scripListPosMap.put(scripID, newpos);
    this.posMap.put(scripListID, scripListPosMap);
    

    if (entry) {
      this.leverageVals.add(Double.valueOf(Math.abs(newpos.longValue()) * triggerPrice.doubleValue() / capital.doubleValue()));
    }
    
    if ((newpos.longValue() == 0L) && (!orderType.equals("ROLLOVER"))) {
      return true;
    }
    return false;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/TradePostProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */