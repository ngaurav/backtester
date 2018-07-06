package com.q1.bt.driver;

import com.q1.bt.data.classes.Scrip;
import com.q1.bt.driver.backtest.ScripListDriver;
import com.q1.bt.driver.backtest.StrategyDriver;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.process.BacktesterProcess;
import com.q1.bt.process.ProcessFlow;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.parameter.BacktestParameter;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.math.MathLib;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class BacktestMainDriver implements Runnable
{
  BacktesterGlobal btGlobal;
  LinkedHashMap<String, StrategyDriver> strategyDriverMap = new LinkedHashMap();
  
  Backtest backtest;
  
  HashMap<String, HashMap<String, HashMap<String, String>>> outputMap = new HashMap();
  


  public BacktestMainDriver(BacktesterGlobal btGlobal, Backtest backtest)
  {
    this.btGlobal = btGlobal;
    this.backtest = backtest;
    
    if (backtest.timeStamp == null) {
      backtest.timeStamp = getTimeStamp();
    }
    new File(btGlobal.loginParameter.getOutputPath() + "/" + backtest.timeStamp).mkdirs();
    
    if ((backtest.backtestParameter.isSkipExistingBacktest()) || (backtest.fileBacktest)) {
      this.outputMap = new HashMap();
    } else {
      try
      {
        this.outputMap = btGlobal.createOutputMap();
      } catch (IOException e) {
        e.printStackTrace();
        btGlobal.displayMessage("Could not create Output Map.");
        return;
      }
    }
  }
  
  public void run()
  {
    double backtestCount = 0.0D;
    
    BacktesterProcess currentProcess = this.btGlobal.processFlow.getCurrentProcess();
    

    for (Map.Entry<String, LinkedHashMap<String, ArrayList<Scrip>>> entry : this.backtest.backtestMap.entrySet())
    {

      String strategyID = (String)entry.getKey();
      LinkedHashMap<String, ArrayList<Scrip>> scripListMap = (LinkedHashMap)entry.getValue();
      

      if (!this.strategyDriverMap.containsKey(strategyID)) {
        try {
          StrategyDriver strategyDriver = new StrategyDriver(this.btGlobal, strategyID, this.backtest, scripListMap, 
            this.outputMap);
          this.strategyDriverMap.put(strategyID, strategyDriver);
          backtestCount += strategyDriver.scripListDriverMap.size();
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }
      }
    }
    




    if (currentProcess.equals(BacktesterProcess.Backtest)) {
      this.btGlobal.setProgressBar(0);
    }
    boolean check = false;
    while (!check) {
      check = true;
      double count = 0.0D;
      
      Iterator localIterator2;
      for (e = this.strategyDriverMap.values().iterator(); e.hasNext(); 
          

          localIterator2.hasNext())
      {
        StrategyDriver strategyDriver = (StrategyDriver)e.next();
        HashMap<String, ScripListDriver> scripDriverMap = strategyDriver.scripListDriverMap;
        
        localIterator2 = scripDriverMap.values().iterator(); continue;ScripListDriver scripDriver = (ScripListDriver)localIterator2.next();
        if (scripDriver.getState() != Thread.State.TERMINATED) {
          check = false;
        } else {
          count += 1.0D;
        }
      }
      if (currentProcess.equals(BacktesterProcess.Backtest)) {
        int curVal = (int)(100.0D * (count / backtestCount));
        if (this.btGlobal.getProgressBarValue() != curVal) {
          this.btGlobal.setProgressBar(curVal);
        }
      }
    }
    
    if (currentProcess.equals(BacktesterProcess.Backtest))
    {

      this.btGlobal.displayMessage("Done backtesting for all scrips and strategies.");
      

      this.btGlobal.processFlow.update();
      

      if (this.btGlobal.isGui) {
        this.btGlobal.initializeProcess(this.backtest);
      }
    } else if (currentProcess.equals(BacktesterProcess.BatchProcess)) {
      try {
        ResultDriver resultDriver = new ResultDriver(this.btGlobal, this.backtest.timeStamp, 
          this.backtest.backtestParameter.getPostProcessMode(), this.backtest.backtestParameter.getAggregationMode());
        resultDriver.exportAllResults(this.backtest.backtestParameter.isExportResultsCheck(), this.btGlobal.isGui);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  


  public String getTimeStamp()
  {
    File folder = new File(this.btGlobal.loginParameter.getOutputPath());
    File[] folders = folder.listFiles();
    Integer maxVal = Integer.valueOf(0);
    File[] arrayOfFile1; int j = (arrayOfFile1 = folders).length; for (int i = 0; i < j; i++) { File f = arrayOfFile1[i];
      Integer val = Integer.valueOf(-1);
      try {
        val = Integer.valueOf(Integer.parseInt(f.getName()));
      }
      catch (Exception localException) {}
      
      maxVal = Integer.valueOf(MathLib.max(val.intValue(), maxVal.intValue()));
    }
    maxVal = Integer.valueOf(maxVal.intValue() + 1);
    return maxVal.toString();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/BacktestMainDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */