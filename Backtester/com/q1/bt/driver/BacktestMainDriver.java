/*     */ package com.q1.bt.driver;
/*     */ 
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.driver.backtest.ScripListDriver;
/*     */ import com.q1.bt.driver.backtest.StrategyDriver;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.process.BacktesterProcess;
/*     */ import com.q1.bt.process.ProcessFlow;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.parameter.BacktestParameter;
/*     */ import com.q1.bt.process.parameter.LoginParameter;
/*     */ import com.q1.math.MathLib;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public class BacktestMainDriver implements Runnable
/*     */ {
/*     */   BacktesterGlobal btGlobal;
/*  25 */   LinkedHashMap<String, StrategyDriver> strategyDriverMap = new LinkedHashMap();
/*     */   
/*     */   Backtest backtest;
/*     */   
/*  29 */   HashMap<String, HashMap<String, HashMap<String, String>>> outputMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   public BacktestMainDriver(BacktesterGlobal btGlobal, Backtest backtest)
/*     */   {
/*  35 */     this.btGlobal = btGlobal;
/*  36 */     this.backtest = backtest;
/*     */     
/*  38 */     if (backtest.timeStamp == null) {
/*  39 */       backtest.timeStamp = getTimeStamp();
/*     */     }
/*  41 */     new File(btGlobal.loginParameter.getOutputPath() + "/" + backtest.timeStamp).mkdirs();
/*     */     
/*  43 */     if ((backtest.backtestParameter.isSkipExistingBacktest()) || (backtest.fileBacktest)) {
/*  44 */       this.outputMap = new HashMap();
/*     */     } else {
/*     */       try
/*     */       {
/*  48 */         this.outputMap = btGlobal.createOutputMap();
/*     */       } catch (IOException e) {
/*  50 */         e.printStackTrace();
/*  51 */         btGlobal.displayMessage("Could not create Output Map.");
/*  52 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  59 */     double backtestCount = 0.0D;
/*     */     
/*  61 */     BacktesterProcess currentProcess = this.btGlobal.processFlow.getCurrentProcess();
/*     */     
/*     */ 
/*  64 */     for (Map.Entry<String, LinkedHashMap<String, ArrayList<Scrip>>> entry : this.backtest.backtestMap.entrySet())
/*     */     {
/*     */ 
/*  67 */       String strategyID = (String)entry.getKey();
/*  68 */       LinkedHashMap<String, ArrayList<Scrip>> scripListMap = (LinkedHashMap)entry.getValue();
/*     */       
/*     */ 
/*  71 */       if (!this.strategyDriverMap.containsKey(strategyID)) {
/*     */         try {
/*  73 */           StrategyDriver strategyDriver = new StrategyDriver(this.btGlobal, strategyID, this.backtest, scripListMap, 
/*  74 */             this.outputMap);
/*  75 */           this.strategyDriverMap.put(strategyID, strategyDriver);
/*  76 */           backtestCount += strategyDriver.scripListDriverMap.size();
/*     */         } catch (Exception e) {
/*  78 */           e.printStackTrace();
/*  79 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */     if (currentProcess.equals(BacktesterProcess.Backtest)) {
/*  89 */       this.btGlobal.setProgressBar(0);
/*     */     }
/*  91 */     boolean check = false;
/*  92 */     while (!check) {
/*  93 */       check = true;
/*  94 */       double count = 0.0D;
/*     */       
/*     */       Iterator localIterator2;
/*  97 */       for (e = this.strategyDriverMap.values().iterator(); e.hasNext(); 
/*     */           
/*     */ 
/* 100 */           localIterator2.hasNext())
/*     */       {
/*  97 */         StrategyDriver strategyDriver = (StrategyDriver)e.next();
/*  98 */         HashMap<String, ScripListDriver> scripDriverMap = strategyDriver.scripListDriverMap;
/*     */         
/* 100 */         localIterator2 = scripDriverMap.values().iterator(); continue;ScripListDriver scripDriver = (ScripListDriver)localIterator2.next();
/* 101 */         if (scripDriver.getState() != Thread.State.TERMINATED) {
/* 102 */           check = false;
/*     */         } else {
/* 104 */           count += 1.0D;
/*     */         }
/*     */       }
/* 107 */       if (currentProcess.equals(BacktesterProcess.Backtest)) {
/* 108 */         int curVal = (int)(100.0D * (count / backtestCount));
/* 109 */         if (this.btGlobal.getProgressBarValue() != curVal) {
/* 110 */           this.btGlobal.setProgressBar(curVal);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 115 */     if (currentProcess.equals(BacktesterProcess.Backtest))
/*     */     {
/*     */ 
/* 118 */       this.btGlobal.displayMessage("Done backtesting for all scrips and strategies.");
/*     */       
/*     */ 
/* 121 */       this.btGlobal.processFlow.update();
/*     */       
/*     */ 
/* 124 */       if (this.btGlobal.isGui) {
/* 125 */         this.btGlobal.initializeProcess(this.backtest);
/*     */       }
/* 127 */     } else if (currentProcess.equals(BacktesterProcess.BatchProcess)) {
/*     */       try {
/* 129 */         ResultDriver resultDriver = new ResultDriver(this.btGlobal, this.backtest.timeStamp, 
/* 130 */           this.backtest.backtestParameter.getPostProcessMode(), this.backtest.backtestParameter.getAggregationMode());
/* 131 */         resultDriver.exportAllResults(this.backtest.backtestParameter.isExportResultsCheck(), this.btGlobal.isGui);
/*     */       }
/*     */       catch (Exception e) {
/* 134 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getTimeStamp()
/*     */   {
/* 143 */     File folder = new File(this.btGlobal.loginParameter.getOutputPath());
/* 144 */     File[] folders = folder.listFiles();
/* 145 */     Integer maxVal = Integer.valueOf(0);
/* 146 */     File[] arrayOfFile1; int j = (arrayOfFile1 = folders).length; for (int i = 0; i < j; i++) { File f = arrayOfFile1[i];
/* 147 */       Integer val = Integer.valueOf(-1);
/*     */       try {
/* 149 */         val = Integer.valueOf(Integer.parseInt(f.getName()));
/*     */       }
/*     */       catch (Exception localException) {}
/*     */       
/* 153 */       maxVal = Integer.valueOf(MathLib.max(val.intValue(), maxVal.intValue()));
/*     */     }
/* 155 */     maxVal = Integer.valueOf(maxVal.intValue() + 1);
/* 156 */     return maxVal.toString();
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/BacktestMainDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */