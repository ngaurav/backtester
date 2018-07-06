package com.q1.bt.global;

import com.q1.bt.driver.BacktestMainDriver;
import com.q1.bt.driver.MachineLearningMainDriver;
import com.q1.bt.driver.ResultDriver;
import com.q1.bt.machineLearning.driver.MLPreProcessor;
import com.q1.bt.process.BacktesterProcess;
import com.q1.bt.process.ProcessFlow;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.objects.MachineLearning;
import com.q1.bt.process.parameter.BacktestParameter;
import com.q1.bt.process.parameter.MachineLearningParameter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class BacktesterNonGUI extends Thread
{
  private String version = "8.17.2";
  

  BacktesterGlobal btGlobal;
  

  LinkedHashMap<BacktesterProcess, Object> processFlowMap = new LinkedHashMap();
  

  public BacktesterNonGUI(LinkedHashMap<BacktesterProcess, Object> processFlowMap, com.q1.bt.process.parameter.PackageParameter packageParameter)
    throws Exception
  {
    System.out.println("----- Backtester v" + this.version + " -----");
    

    this.processFlowMap = processFlowMap;
    com.q1.bt.process.parameter.LoginParameter loginParameter = (com.q1.bt.process.parameter.LoginParameter)processFlowMap.get(BacktesterProcess.Login);
    this.btGlobal = new BacktesterGlobal(this, loginParameter, packageParameter);
  }
  


  public void run()
  {
    this.btGlobal.displayMessage("Succesfully Logged In.");
    

    Backtest backtest = null;
    MachineLearningMainDriver mlDriver = null;
    MachineLearning machineLearning = null;
    BacktesterProcess prevProcess = null;
    for (Map.Entry<BacktesterProcess, Object> processEntry : this.processFlowMap.entrySet())
    {

      BacktesterProcess process = (BacktesterProcess)processEntry.getKey();
      

      this.btGlobal.processFlow.add(process);
      

      if (process.equals(BacktesterProcess.Login)) {
        this.btGlobal.processFlow.update();


      }
      else if (process.equals(BacktesterProcess.Backtest))
      {
        if (prevProcess.equals(BacktesterProcess.MachineLearning))
        {

          this.btGlobal.displayMessage("Running Backtest on Updated Orderbook");
          this.btGlobal.processFlow.update();
          try
          {
            backtest = new Backtest(mlDriver.backtest.backtestParameter, 
              mlDriver.mlPreProcessor.getDestPath() + "/ML Order Data");
            backtest.fileBacktest = true;
            backtest.timeStamp = machineLearning.getTimeStamp();
            BacktestMainDriver backtestDriver = new BacktestMainDriver(this.btGlobal, backtest);
            Thread t = new Thread(backtestDriver);
            t.start();
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
        else {
          backtest = (Backtest)processEntry.getValue();
          

          this.btGlobal.btDriver = new BacktestMainDriver(this.btGlobal, backtest);
          Thread t = new Thread(this.btGlobal.btDriver);
          t.start();
          try {
            t.join();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          
        }
        
      }
      else if (process.equals(BacktesterProcess.MachineLearning))
      {

        MachineLearningParameter mlParameter = (MachineLearningParameter)processEntry.getValue();
        machineLearning = new MachineLearning(mlParameter, backtest);
        
        try
        {
          mlDriver = new MachineLearningMainDriver(this.btGlobal, machineLearning);
        } catch (IOException e1) {
          e1.printStackTrace();
          this.btGlobal.displayMessage("Error creating Machine Learning Driver!");
          return;
        }
        Thread t1 = new Thread(mlDriver);
        t1.start();
        try {
          t1.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        
        try
        {
          backtest = new Backtest(backtest.backtestParameter, 
            mlDriver.mlPreProcessor.getDestPath() + "/ML Order Data");
          backtest.fileBacktest = true;
          backtest.timeStamp = machineLearning.getTimeStamp();
          BacktestMainDriver backtestDriver = new BacktestMainDriver(this.btGlobal, backtest);
          Thread t2 = new Thread(backtestDriver);
          t2.start();
          t2.join();
        }
        catch (Exception e) {
          e.printStackTrace();

        }
        

      }
      else if (process.equals(BacktesterProcess.Results))
      {

        this.btGlobal.displayMessage("Generating Results..");
        
        try
        {
          this.btGlobal.updateResultDriver(backtest.timeStamp, backtest.backtestParameter.getPostProcessMode(), 
            backtest.backtestParameter.getAggregationMode());
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }
        

        this.btGlobal.displayMessage("Done Generating Results");
        
        try
        {
          this.btGlobal.resultDriver.generateAndExportResultsForKey("All", "All", "All", "All");
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }
        
        try
        {
          this.btGlobal.resultDriver.exportAllResults(backtest.backtestParameter.isExportResultsCheck(), false);
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }
      }
      

      prevProcess = process;
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/global/BacktesterNonGUI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */