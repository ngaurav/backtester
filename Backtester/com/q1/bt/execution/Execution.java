/*     */ package com.q1.bt.execution;
/*     */ 
/*     */ import com.q1.bt.data.DataTypeViewer;
/*     */ import com.q1.bt.execution.order.Order;
/*     */ import com.q1.bt.process.backtest.SlippageModel;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import com.q1.math.MathLib;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Execution
/*     */ {
/*  19 */   public Double candleMTM = Double.valueOf(0.0D); public Double cumCandleMTM = Double.valueOf(0.0D); public Double prevCumCandleMTM = Double.valueOf(0.0D);
/*  20 */   public Double buy = Double.valueOf(NaN.0D); public Double sell = Double.valueOf(NaN.0D);
/*     */   
/*     */ 
/*  23 */   public HashMap<String, Long> positionMap = new HashMap();
/*  24 */   public HashMap<String, Double> mtmMap = new HashMap();
/*  25 */   public HashMap<String, Double> dayMTMMap = new HashMap();
/*  26 */   Double prevCl = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  32 */   HashMap<String, CSVWriter> scripTradebookWriterMap = new HashMap();
/*     */   
/*  34 */   public HashMap<String, ArrayList<String[]>> scripTradeBookMap = new HashMap();
/*     */   
/*  36 */   public HashMap<String, ScripExecution> executionMap = new HashMap();
/*     */   public DataTypeViewer mainData;
/*     */   public DataTypeViewer metaData;
/*     */   Double capital;
/*     */   
/*  41 */   public Execution(String scripListID, Double capital, HashMap<String, CSVWriter> scripTradebookWriterMap, SlippageModel slippageModel, RolloverMethod rolloverMethod, HashMap<String, ScripExecution> executionMap) { this.capital = capital;
/*  42 */     this.scripTradebookWriterMap = scripTradebookWriterMap;
/*  43 */     this.slippageModel = slippageModel;
/*  44 */     this.rolloverMethod = rolloverMethod;
/*  45 */     this.scripListID = scripListID;
/*  46 */     this.executionMap = executionMap;
/*     */   }
/*     */   
/*     */   public void updateWriter(HashMap<String, CSVWriter> scripTradebookWriterMap)
/*     */   {
/*  51 */     this.scripTradebookWriterMap = scripTradebookWriterMap;
/*     */   }
/*     */   
/*     */   SlippageModel slippageModel;
/*     */   RolloverMethod rolloverMethod;
/*     */   String scripListID;
/*     */   public void processOrders(DataTypeViewer mainData, HashMap<String, DataTypeViewer> dataMap, ArrayList<Order> orderBook, Long prevOrderTimestamp) throws Exception
/*     */   {
/*  59 */     this.mainData = mainData;
/*     */     
/*     */ 
/*     */ 
/*  63 */     String scripID = null;
/*  64 */     CSVWriter tbWriter; for (String curScripID : mainData.scripDataViewerMap.keySet())
/*     */     {
/*     */ 
/*  67 */       scripID = curScripID;
/*     */       
/*     */ 
/*  70 */       tbWriter = (CSVWriter)this.scripTradebookWriterMap.get(scripID);
/*     */       
/*     */ 
/*  73 */       if (!this.executionMap.containsKey(scripID)) {
/*  74 */         ScripExecution scripExec = new ScripExecution(tbWriter, this.slippageModel, this.rolloverMethod);
/*  75 */         this.executionMap.put(scripID, scripExec);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  81 */     HashMap<String, ArrayList<Order>> orderBookMap = new HashMap();
/*  82 */     for (Order order : orderBook)
/*     */     {
/*     */ 
/*  85 */       if (mainData.scripCount == 1) {
/*  86 */         order.scripID = scripID;
/*     */       }
/*     */       
/*  89 */       ArrayList<Order> curOrderList = new ArrayList();
/*  90 */       if (orderBookMap.containsKey(order.scripID)) {
/*  91 */         curOrderList = (ArrayList)orderBookMap.get(order.scripID);
/*     */       }
/*     */       
/*  94 */       curOrderList.add(order);
/*  95 */       orderBookMap.put(order.scripID, curOrderList);
/*     */     }
/*     */     
/*     */ 
/*  99 */     Double candle$MTM = Double.valueOf(0.0D);
/* 100 */     this.buy = (this.sell = Double.valueOf(NaN.0D));
/* 101 */     Double usedCapital = Double.valueOf(0.0D);
/* 102 */     for (Map.Entry<String, ScripExecution> entry : this.executionMap.entrySet())
/*     */     {
/*     */ 
/* 105 */       scripID = (String)entry.getKey();
/* 106 */       ScripExecution scripExec = (ScripExecution)entry.getValue();
/*     */       
/*     */ 
/* 109 */       ArrayList<Order> scripIDOrderBook = (ArrayList)orderBookMap.get(scripID);
/*     */       
/*     */ 
/* 112 */       if (scripIDOrderBook == null) {
/* 113 */         scripIDOrderBook = new ArrayList();
/*     */       }
/*     */       
/* 116 */       ExecutionData execData = new ExecutionData(this.scripListID, scripID, mainData, dataMap, prevOrderTimestamp);
/*     */       
/*     */ 
/* 119 */       scripExec.processOrders(execData, scripIDOrderBook, this.capital);
/*     */       
/*     */ 
/* 122 */       candle$MTM = Double.valueOf(candle$MTM.doubleValue() + scripExec.$TradeMTM.doubleValue());
/*     */       
/*     */ 
/* 125 */       this.positionMap.put(scripID, scripExec.position);
/*     */       
/*     */ 
/* 128 */       this.mtmMap.put(scripID, Double.valueOf(scripExec.$TradeMTM.doubleValue() / this.capital.doubleValue()));
/*     */       
/*     */ 
/* 131 */       this.dayMTMMap.put(scripID, Double.valueOf((scripExec.$CumMTM.doubleValue() - scripExec.$PrevDayCumMTM.doubleValue()) / this.capital.doubleValue()));
/*     */       
/*     */ 
/* 134 */       this.scripTradeBookMap.put(scripID, scripExec.tradeBook);
/*     */       
/*     */ 
/* 137 */       usedCapital = Double.valueOf(usedCapital.doubleValue() + scripExec.usedCapital.doubleValue());
/*     */       
/*     */ 
/* 140 */       if (MathLib.doubleCompare(usedCapital, this.capital).intValue() > 0.0D) {
/* 141 */         throw new Exception(this.scripListID + ": Capital has been exceeded, Please check your orders!");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 146 */     this.candleMTM = Double.valueOf(candle$MTM.doubleValue() / this.capital.doubleValue());
/* 147 */     this.cumCandleMTM = Double.valueOf(this.cumCandleMTM.doubleValue() + this.candleMTM.doubleValue());
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/Execution.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */