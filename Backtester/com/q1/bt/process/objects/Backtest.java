/*     */ package com.q1.bt.process.objects;
/*     */ 
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.execution.ParamObject;
/*     */ import com.q1.bt.process.parameter.BacktestParameter;
/*     */ import com.q1.bt.process.parameter.PackageParameter;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Backtest
/*     */ {
/*     */   public HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap;
/*     */   public HashMap<String, ArrayList<String[]>> strategyParameterMap;
/*     */   public BacktestParameter backtestParameter;
/*     */   public String timeStamp;
/*  23 */   public boolean fileBacktest = false;
/*     */   
/*     */   public String orderBookPath;
/*     */   
/*     */ 
/*     */   public Backtest(BacktestParameter backtestParameter, HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap, HashMap<String, ArrayList<String[]>> strategyParameterMap)
/*     */   {
/*  30 */     this.backtestParameter = backtestParameter;
/*  31 */     this.backtestMap = new HashMap(backtestMap);
/*  32 */     this.strategyParameterMap = new HashMap(strategyParameterMap);
/*     */   }
/*     */   
/*     */   public Backtest(BacktestParameter backtestParameter, String orderBookPath) throws Exception {
/*  36 */     this.backtestParameter = backtestParameter;
/*  37 */     this.backtestMap = createBacktestMapFromFolder(orderBookPath);
/*  38 */     this.strategyParameterMap = new HashMap();
/*  39 */     this.orderBookPath = orderBookPath;
/*  40 */     this.fileBacktest = true;
/*     */   }
/*     */   
/*     */   public void setStrategyParametersAsDefault(PackageParameter packageParameter)
/*     */   {
/*  45 */     this.strategyParameterMap = new HashMap();
/*     */     
/*  47 */     for (String strategyID : this.backtestMap.keySet())
/*     */     {
/*     */ 
/*  50 */       ArrayList<String[]> defaultParameters = new ArrayList();
/*     */       try {
/*  52 */         defaultParameters = ParamObject.getParameterList(packageParameter.getStrategyPackage(), strategyID);
/*     */       } catch (Exception e) {
/*  54 */         e.printStackTrace();
/*  55 */         return;
/*     */       }
/*     */       
/*     */ 
/*  59 */       this.strategyParameterMap.put(strategyID, defaultParameters);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> createBacktestMapFromFolder(String folderPath)
/*     */     throws Exception
/*     */   {
/*  67 */     HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap = new HashMap();
/*     */     
/*  69 */     File folderPathFile = new File(folderPath);
/*     */     
/*  71 */     File[] backtestTable = folderPathFile.listFiles();
/*  72 */     int backtestCount = backtestTable.length;
/*     */     
/*     */ 
/*  75 */     for (int i = 0; i < backtestCount; i++)
/*     */     {
/*     */ 
/*  78 */       String[] backtestVal = backtestTable[i].getName().split(" ");
/*  79 */       String strategyID = backtestVal[0];
/*  80 */       String scripListID = backtestVal[1];
/*     */       
/*  82 */       File[] scripListTable = backtestTable[i].listFiles();
/*     */       
/*     */ 
/*  85 */       ArrayList<Scrip> scripSet = new ArrayList();
/*     */       
/*     */       File[] arrayOfFile1;
/*  88 */       int j = (arrayOfFile1 = scripListTable).length; for (int i = 0; i < j; i++) { File scripFile = arrayOfFile1[i];
/*  89 */         String scripID = scripFile.getName().substring(0, scripFile.getName().length() - 14);
/*  90 */         scripSet.add(new Scrip(scripID));
/*     */       }
/*     */       
/*     */ 
/*  94 */       LinkedHashMap<String, ArrayList<Scrip>> scripListMap = (LinkedHashMap)backtestMap.get(strategyID);
/*  95 */       if (scripListMap == null) {
/*  96 */         scripListMap = new LinkedHashMap();
/*  97 */         scripListMap.put(scripListID, scripSet);
/*  98 */         backtestMap.put(strategyID, scripListMap);
/*     */       } else {
/* 100 */         scripListMap.put(scripListID, scripSet);
/* 101 */         backtestMap.put(strategyID, scripListMap);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 106 */     return backtestMap;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/objects/Backtest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */