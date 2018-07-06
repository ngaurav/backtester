package com.q1.bt.driver.backtest;

import com.q1.bt.data.classes.Scrip;
import com.q1.bt.driver.backtest.enums.OutputMode;
import com.q1.bt.execution.OrderbookStrategy;
import com.q1.bt.execution.Strategy;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.gui.main.ParamUI;
import com.q1.bt.process.BacktesterProcess;
import com.q1.bt.process.ProcessFlow;
import com.q1.bt.process.backtest.SlippageModel;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.parameter.BacktestParameter;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.csv.CSVWriter;
import java.io.File;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class StrategyDriver
{
  String strategyID;
  String strategyName;
  String strategyDataType;
  BacktesterGlobal btGlobal;
  Backtest backtest;
  ArrayList<String> strategyParameterList = new ArrayList();
  HashMap<String, HashMap<String, HashMap<String, String>>> outputMap = new HashMap();
  
  String parameterKey = "";
  
  int finalMode = 0;
  
  Long maxEndDate;
  
  String strategyPackage;
  
  public HashMap<String, ScripListDriver> scripListDriverMap = new HashMap();
  
  public StrategyDriver(StrategyDriver sD, HashMap<String, HashMap<String, HashMap<String, String>>> outputMap)
  {
    this.strategyID = sD.strategyID;
    this.btGlobal = sD.btGlobal;
    this.backtest = sD.backtest;
    this.strategyParameterList = new ArrayList(sD.strategyParameterList);
    this.outputMap = outputMap;
    this.parameterKey = sD.parameterKey;
  }
  



  public StrategyDriver(BacktesterGlobal btGlobal, String strategyID, Backtest backtest, LinkedHashMap<String, ArrayList<Scrip>> scripListMap, HashMap<String, HashMap<String, HashMap<String, String>>> outputMap)
    throws Exception
  {
    this.strategyID = strategyID;
    String[] strategyVal = strategyID.split("_");
    this.strategyName = strategyVal[0];
    this.strategyDataType = strategyVal[1];
    this.outputMap = outputMap;
    

    this.btGlobal = btGlobal;
    this.backtest = backtest;
    String scripListID;
    if (backtest.fileBacktest)
    {
      for (Map.Entry<String, ArrayList<Scrip>> entry : scripListMap.entrySet())
      {
        scripListID = (String)entry.getKey();
        ArrayList<Scrip> scripSet = (ArrayList)entry.getValue();
        

        if (btGlobal.processFlow.getCurrentProcess().equals(BacktesterProcess.Backtest)) {
          btGlobal.displayMessage(strategyID + " " + scripListID + ": Backtest started");
        }
        ScripListDriver scripListDriver = new ScripListDriver(scripListID, scripSet, strategyID, 
          this.strategyDataType, btGlobal, backtest, this.parameterKey, outputMap);
        

        try
        {
          Strategy strategy = new OrderbookStrategy(this.strategyDataType, 
            backtest.orderBookPath + "/" + strategyID + " " + scripListID);
          strategy.strategyID = strategyID;
        } catch (Exception e) {
          btGlobal.displayMessage("Error creating strategy object for: " + strategyID);
          e.printStackTrace();
          continue;
        }
        
        Strategy strategy;
        scripListDriver.createBacktestDriver(strategy);
        

        scripListDriver.start();
        

        this.scripListDriverMap.put(strategyID + " " + scripListID, scripListDriver);
      }
      
    }
    else
    {
      ArrayList<String[]> inputParameterList = getParameterList();
      

      backtest.strategyParameterMap.put(strategyID, inputParameterList);
      

      writeParameterListToFile(inputParameterList);
      

      for (Object entry : scripListMap.entrySet())
      {
        String scripListID = (String)((Map.Entry)entry).getKey();
        ArrayList<Scrip> scripSet = (ArrayList)((Map.Entry)entry).getValue();
        

        if (btGlobal.processFlow.getCurrentProcess().equals(BacktesterProcess.Backtest)) {
          btGlobal.displayMessage(strategyID + " " + scripListID + ": Backtest started");
        }
        ScripListDriver scripListDriver = new ScripListDriver(scripListID, scripSet, strategyID, 
          this.strategyDataType, btGlobal, backtest, this.parameterKey, outputMap);
        

        if (scripListDriver.outputMode.equals(OutputMode.Normal))
        {

          try
          {

            Strategy strategy = getStrategyInstance(scripSet);
            strategy.strategyID = strategyID;
          } catch (Exception e) {
            btGlobal.displayMessage("Error creating strategy object for: " + strategyID);
            e.printStackTrace();
            continue;
          }
          
          Strategy strategy;
          scripListDriver.createBacktestDriver(strategy);
          

          scripListDriver.start();
          

          this.scripListDriverMap.put(strategyID + " " + scripListID, scripListDriver);
        }
      }
    }
  }
  
  public static String createDataPath(String folder)
  {
    if (!new File(folder).exists())
      new File(folder).mkdirs();
    return folder;
  }
  
  public Strategy getStrategyInstance(ArrayList<Scrip> scripSet)
    throws Exception
  {
    Class<?> stratClass = Class.forName(this.strategyPackage + "." + this.strategyID);
    Constructor<?> stratConstructor = stratClass.getConstructor(new Class[] { ArrayList.class, ArrayList.class });
    Strategy stratObject = 
      (Strategy)stratConstructor.newInstance(new Object[] { this.strategyParameterList, scripSet });
    return stratObject;
  }
  

  public ArrayList<String[]> getParameterList()
    throws Exception
  {
    ArrayList<String[]> inputParameterList = (ArrayList)this.backtest.strategyParameterMap.get(this.strategyID);
    

    if (inputParameterList == null) {
      inputParameterList = new ArrayList();
    }
    
    this.strategyPackage = this.btGlobal.packageParameter.getStrategyPackage();
    if (inputParameterList.size() > 0) {
      return inputParameterList;
    }
    
    if (this.backtest.backtestParameter.isDefaultParametersCheck()) {
      Class<?> stratClass = Class.forName(this.strategyPackage + "." + this.strategyID);
      Constructor<?> stratTempConstructor = stratClass.getConstructor(new Class[0]);
      Strategy stratTempObject = (Strategy)stratTempConstructor.newInstance(new Object[0]);
      return stratTempObject.getParameterList();
    }
    


    ParamUI pUI = new ParamUI(this.strategyID, this.strategyPackage);
    return pUI.getParameters();
  }
  


  public void writeParameterListToFile(ArrayList<String[]> inputParameterList)
    throws java.io.IOException
  {
    String[] stratFolder = this.strategyPackage.split("\\.");
    String stratPath = "";
    String[] arrayOfString1; int j = (arrayOfString1 = stratFolder).length; for (int i = 0; i < j; i++) { String sF = arrayOfString1[i];
      stratPath = stratPath + "/" + sF; }
    File stratFile = new File(
      this.btGlobal.loginParameter.getMainPath() + "/src" + stratPath + "/" + this.strategyID + ".java");
    SimpleDateFormat stratFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    String timeStamp = stratFormat.format(Long.valueOf(stratFile.lastModified()));
    
    String outPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Parameters";
    if (!new File(outPath).exists())
      new File(outPath).mkdirs();
    String paramPath = outPath + "/" + this.strategyID + " Parameters.csv";
    

    CSVWriter writer = new CSVWriter(paramPath, false, ",");
    

    ArrayList<String> parameterList = new ArrayList();
    

    String[] tsLine = { "Strategy Timestamp", timeStamp };
    parameterList.add(timeStamp);
    writer.writeLine(tsLine);
    

    Double capital = this.btGlobal.loginParameter.getCapital();
    String capOut;
    String capOut; if (Double.compare(Math.floor(capital.doubleValue()), capital.doubleValue()) == 0) {
      Long cInt = Long.valueOf(capital.longValue());
      capOut = cInt.toString();
    } else {
      capOut = capital.toString();
    }
    String[] capLine = { "Capital", capOut };
    parameterList.add(capOut);
    writer.writeLine(capLine);
    

    Double rpt = this.btGlobal.loginParameter.getRiskPerTrade();
    String rptOut;
    String rptOut; if (Double.compare(Math.floor(rpt.doubleValue()), rpt.doubleValue()) == 0) {
      Integer cInt = Integer.valueOf(rpt.intValue());
      rptOut = cInt.toString();
    } else {
      rptOut = rpt.toString();
    }
    String[] rptLine = { "Risk Per Trade", rptOut };
    parameterList.add(rptOut);
    writer.writeLine(rptLine);
    

    if (this.backtest.backtestParameter.isExportResultsCheck()) {
      String[] outputLine = { "Export Results", "1" };
      parameterList.add("1");
      writer.writeLine(outputLine);
    } else {
      String[] outputLine = { "Export Results", "0" };
      parameterList.add("0");
      writer.writeLine(outputLine);
    }
    

    if (this.backtest.backtestParameter.isGenerateOutputCheck()) {
      String[] outputLine = { "Output", "1" };
      parameterList.add("1");
      writer.writeLine(outputLine);
    } else {
      String[] outputLine = { "Output", "0" };
      parameterList.add("0");
      writer.writeLine(outputLine);
    }
    

    String slippageModelStr = this.backtest.backtestParameter.getSlippageModel().toString();
    String[] slippageLine = { "Slippage Model", slippageModelStr };
    parameterList.add(slippageModelStr);
    writer.writeLine(slippageLine);
    

    String rolloverMethodStr = this.backtest.backtestParameter.getRolloverMethod().toString();
    String[] rollLine = { "Rollover Method", rolloverMethodStr };
    parameterList.add(rolloverMethodStr);
    writer.writeLine(rollLine);
    

    String postProcessModeStr = this.backtest.backtestParameter.getPostProcessMode().toString();
    String[] postProcessLine = { "Post Process Mode", postProcessModeStr };
    parameterList.add(postProcessModeStr);
    writer.writeLine(postProcessLine);
    

    for (String[] param : inputParameterList) {
      try {
        Double p = Double.valueOf(Double.parseDouble(param[1]));
        if (Double.compare(Math.floor(p.doubleValue()), p.doubleValue()) == 0) {
          Integer pInt = Integer.valueOf(p.intValue());
          String pStr = pInt.toString();
          parameterList.add(pStr);
          this.strategyParameterList.add(pStr);
          param[1] = pInt.toString();
        } else {
          parameterList.add(param[1]);
          this.strategyParameterList.add(param[1]);
        }
      } catch (NumberFormatException ne) {
        parameterList.add(param[1]);
        this.strategyParameterList.add(param[1]);
      }
      
      String[] paramLine = { param[0], param[1] };
      writer.writeLine(paramLine);
    }
    writer.close();
    
    this.parameterKey = "";
    for (String param : parameterList) {
      if (this.parameterKey.equals("")) {
        this.parameterKey = param;
      } else {
        this.parameterKey = (this.parameterKey + "$" + param);
      }
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/backtest/StrategyDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */