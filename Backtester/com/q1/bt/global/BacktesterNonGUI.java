/*     */ package com.q1.bt.global;
/*     */ 
/*     */ import com.q1.bt.driver.BacktestMainDriver;
/*     */ import com.q1.bt.driver.MachineLearningMainDriver;
/*     */ import com.q1.bt.driver.ResultDriver;
/*     */ import com.q1.bt.machineLearning.driver.MLPreProcessor;
/*     */ import com.q1.bt.process.BacktesterProcess;
/*     */ import com.q1.bt.process.ProcessFlow;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.objects.MachineLearning;
/*     */ import com.q1.bt.process.parameter.BacktestParameter;
/*     */ import com.q1.bt.process.parameter.MachineLearningParameter;
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public class BacktesterNonGUI extends Thread
/*     */ {
/*  19 */   private String version = "8.16.1";
/*     */   
/*     */ 
/*     */   BacktesterGlobal btGlobal;
/*     */   
/*     */ 
/*  25 */   LinkedHashMap<BacktesterProcess, Object> processFlowMap = new LinkedHashMap();
/*     */   
/*     */ 
/*     */   public BacktesterNonGUI(LinkedHashMap<BacktesterProcess, Object> processFlowMap, com.q1.bt.process.parameter.PackageParameter packageParameter)
/*     */     throws Exception
/*     */   {
/*  31 */     System.out.println("----- Backtester v" + this.version + " -----");
/*     */     
/*     */ 
/*  34 */     this.processFlowMap = processFlowMap;
/*  35 */     com.q1.bt.process.parameter.LoginParameter loginParameter = (com.q1.bt.process.parameter.LoginParameter)processFlowMap.get(BacktesterProcess.Login);
/*  36 */     this.btGlobal = new BacktesterGlobal(this, loginParameter, packageParameter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/*  43 */     this.btGlobal.displayMessage("Succesfully Logged In.");
/*     */     
/*     */ 
/*  46 */     Backtest backtest = null;
/*  47 */     MachineLearningMainDriver mlDriver = null;
/*  48 */     MachineLearning machineLearning = null;
/*  49 */     BacktesterProcess prevProcess = null;
/*  50 */     for (Map.Entry<BacktesterProcess, Object> processEntry : this.processFlowMap.entrySet())
/*     */     {
/*     */ 
/*  53 */       BacktesterProcess process = (BacktesterProcess)processEntry.getKey();
/*     */       
/*     */ 
/*  56 */       this.btGlobal.processFlow.add(process);
/*     */       
/*     */ 
/*  59 */       if (process.equals(BacktesterProcess.Login)) {
/*  60 */         this.btGlobal.processFlow.update();
/*     */ 
/*     */ 
/*     */       }
/*  64 */       else if (process.equals(BacktesterProcess.Backtest))
/*     */       {
/*  66 */         if (prevProcess.equals(BacktesterProcess.MachineLearning))
/*     */         {
/*     */ 
/*  69 */           this.btGlobal.displayMessage("Running Backtest on Updated Orderbook");
/*  70 */           this.btGlobal.processFlow.update();
/*     */           try
/*     */           {
/*  73 */             backtest = new Backtest(mlDriver.backtest.backtestParameter, 
/*  74 */               mlDriver.mlPreProcessor.getDestPath() + "/ML Order Data");
/*  75 */             backtest.fileBacktest = true;
/*  76 */             backtest.timeStamp = machineLearning.getTimeStamp();
/*  77 */             BacktestMainDriver backtestDriver = new BacktestMainDriver(this.btGlobal, backtest);
/*  78 */             Thread t = new Thread(backtestDriver);
/*  79 */             t.start();
/*     */           }
/*     */           catch (Exception e) {
/*  82 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */         else {
/*  86 */           backtest = (Backtest)processEntry.getValue();
/*     */           
/*     */ 
/*  89 */           this.btGlobal.btDriver = new BacktestMainDriver(this.btGlobal, backtest);
/*  90 */           Thread t = new Thread(this.btGlobal.btDriver);
/*  91 */           t.start();
/*     */           try {
/*  93 */             t.join();
/*     */           } catch (InterruptedException e) {
/*  95 */             e.printStackTrace();
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/* 101 */       else if (process.equals(BacktesterProcess.MachineLearning))
/*     */       {
/*     */ 
/* 104 */         MachineLearningParameter mlParameter = (MachineLearningParameter)processEntry.getValue();
/* 105 */         machineLearning = new MachineLearning(mlParameter, backtest);
/*     */         
/*     */         try
/*     */         {
/* 109 */           mlDriver = new MachineLearningMainDriver(this.btGlobal, machineLearning);
/*     */         } catch (IOException e1) {
/* 111 */           e1.printStackTrace();
/* 112 */           this.btGlobal.displayMessage("Error creating Machine Learning Driver!");
/* 113 */           return;
/*     */         }
/* 115 */         Thread t1 = new Thread(mlDriver);
/* 116 */         t1.start();
/*     */         try {
/* 118 */           t1.join();
/*     */         } catch (InterruptedException e) {
/* 120 */           e.printStackTrace();
/*     */         }
/*     */         
/*     */         try
/*     */         {
/* 125 */           backtest = new Backtest(backtest.backtestParameter, 
/* 126 */             mlDriver.mlPreProcessor.getDestPath() + "/ML Order Data");
/* 127 */           backtest.fileBacktest = true;
/* 128 */           backtest.timeStamp = machineLearning.getTimeStamp();
/* 129 */           BacktestMainDriver backtestDriver = new BacktestMainDriver(this.btGlobal, backtest);
/* 130 */           Thread t2 = new Thread(backtestDriver);
/* 131 */           t2.start();
/* 132 */           t2.join();
/*     */         }
/*     */         catch (Exception e) {
/* 135 */           e.printStackTrace();
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 141 */       else if (process.equals(BacktesterProcess.Results))
/*     */       {
/*     */ 
/* 144 */         this.btGlobal.displayMessage("Generating Results..");
/*     */         
/*     */         try
/*     */         {
/* 148 */           this.btGlobal.updateResultDriver(backtest.timeStamp, backtest.backtestParameter.getPostProcessMode(), 
/* 149 */             backtest.backtestParameter.getAggregationMode());
/*     */         } catch (Exception e) {
/* 151 */           e.printStackTrace();
/* 152 */           return;
/*     */         }
/*     */         
/*     */ 
/* 156 */         this.btGlobal.displayMessage("Done Generating Results");
/*     */         
/*     */         try
/*     */         {
/* 160 */           this.btGlobal.resultDriver.generateAndExportResultsForKey("All", "All", "All", "All");
/*     */         } catch (Exception e) {
/* 162 */           e.printStackTrace();
/* 163 */           return;
/*     */         }
/*     */         
/*     */         try
/*     */         {
/* 168 */           this.btGlobal.resultDriver.exportAllResults(backtest.backtestParameter.isExportResultsCheck(), false);
/*     */         } catch (Exception e) {
/* 170 */           e.printStackTrace();
/* 171 */           return;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 176 */       prevProcess = process;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/global/BacktesterNonGUI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */