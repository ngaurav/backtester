/*     */ package com.q1.bt.driver.backtest;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.q1.bt.data.DataTypeViewer;
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.execution.Execution;
/*     */ import com.q1.bt.execution.ScripExecution;
/*     */ import com.q1.bt.execution.Strategy;
/*     */ import com.q1.bt.execution.order.Order;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.postprocess.OutputShape;
/*     */ import com.q1.bt.postprocess.PostProcessData;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.parameter.BacktestParameter;
/*     */ import com.q1.bt.process.parameter.LoginParameter;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ public class BacktestDriver
/*     */ {
/*     */   BacktesterGlobal btGlobal;
/*     */   Backtest btObj;
/*     */   String scripListID;
/*  32 */   public TreeMap<Long, Double> dailyMTM = new TreeMap();
/*     */   
/*     */   public Long startDate;
/*     */   
/*     */   public Long endDate;
/*     */   
/*     */   public String orderBookPath;
/*     */   public String postProcessPath;
/*     */   HashMap<String, DataTypeViewer> dataMap;
/*     */   DataTypeViewer mainData;
/*  42 */   DataTypeViewer rolloverData = null;
/*  43 */   Long prevRolloverDate = Long.valueOf(0L);
/*  44 */   boolean rollover = false;
/*     */   
/*     */   Strategy strategy;
/*     */   Execution execution;
/*  48 */   int funCall = 0;
/*     */   
/*     */   Long prevDateTime;
/*     */   
/*     */   String backtestKey;
/*     */   
/*  54 */   TreeMap<String, TreeMap<String, PostProcessData>> postProcessMap = new TreeMap();
/*  55 */   HashMap<String, CSVWriter> scripTradebookWriterMap = new HashMap();
/*  56 */   HashMap<String, CSVWriter> scripPostProcessWriterMap = new HashMap();
/*  57 */   boolean ppHeaderCheck = false;
/*     */   
/*     */ 
/*  60 */   ArrayList<Long> dailyDates = new ArrayList();
/*  61 */   ArrayList<Order> orderBook = new ArrayList();
/*  62 */   Long prevOrderTimestamp = Long.valueOf(0L);
/*     */   
/*     */ 
/*     */ 
/*     */   public BacktestDriver(String scripListID, ArrayList<Scrip> scripSet, Strategy strategy, HashMap<String, DataTypeViewer> dataMap, BacktesterGlobal btGlobal, Backtest backtest, Long startDate, Long endDate)
/*     */     throws IOException
/*     */   {
/*  69 */     this.btGlobal = btGlobal;
/*  70 */     this.btObj = backtest;
/*     */     
/*     */ 
/*  73 */     this.dataMap = dataMap;
/*     */     
/*     */ 
/*  76 */     String[] strategyVal = strategy.strategyID.split("_");
/*  77 */     String mainDataType = strategyVal[1];
/*  78 */     this.mainData = ((DataTypeViewer)dataMap.get(mainDataType));
/*     */     
/*  80 */     if (mainDataType.contains("M")) {
/*  81 */       this.rolloverData = ((DataTypeViewer)dataMap.get("1D"));
/*     */     }
/*     */     
/*  84 */     this.scripListID = scripListID;
/*  85 */     this.strategy = strategy;
/*  86 */     this.backtestKey = (strategy.strategyID + " " + scripListID);
/*  87 */     this.startDate = startDate;
/*  88 */     this.endDate = endDate;
/*     */     
/*     */ 
/*  91 */     this.scripTradebookWriterMap = new HashMap();
/*     */     
/*  93 */     String tbPath = btGlobal.loginParameter.getOutputPath() + "/" + backtest.timeStamp + "/Trade Data" + "/" + 
/*  94 */       this.backtestKey;
/*  95 */     tbPath = createDataPath(tbPath);
/*     */     
/*  97 */     HashMap<String, ScripExecution> executionMap = new HashMap();
/*     */     
/*  99 */     for (Scrip scrip : scripSet) {
/*     */       try {
/* 101 */         CSVWriter writer = new CSVWriter(tbPath + "/" + scrip.scripID + " Tradebook.csv", false, ",");
/*     */         
/*     */ 
/* 104 */         ScripExecution scripExec = new ScripExecution(writer, backtest.backtestParameter.getSlippageModel(), 
/* 105 */           backtest.backtestParameter.getRolloverMethod());
/* 106 */         executionMap.put(scrip.scripID, scripExec);
/*     */         
/* 108 */         this.scripTradebookWriterMap.put(scrip.scripID, writer);
/*     */       } catch (IOException e1) {
/* 110 */         btGlobal.displayMessage("Error creating tradebook writer for: " + this.backtestKey + ", " + scrip.scripID);
/* 111 */         e1.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 116 */     this.execution = new Execution(scripListID, btGlobal.loginParameter.getCapital(), this.scripTradebookWriterMap, 
/* 117 */       backtest.backtestParameter.getSlippageModel(), backtest.backtestParameter.getRolloverMethod(), 
/* 118 */       executionMap);
/*     */     
/*     */ 
/* 121 */     this.prevDateTime = this.mainData.dateTime;
/*     */     
/*     */ 
/* 124 */     this.postProcessPath = createDataPath(btGlobal.loginParameter.getOutputPath() + "/" + backtest.timeStamp + 
/* 125 */       "/Post Process Data" + "/" + this.backtestKey);
/*     */     
/*     */ 
/* 128 */     if (backtest.backtestParameter.isGenerateOutputCheck()) {
/* 129 */       for (PostProcessData ppData : strategy.getPostProcessData()) {
/* 130 */         updatePostProcessMap(ppData, ((Scrip)scripSet.get(0)).scripID);
/*     */       }
/*     */     }
/*     */     
/* 134 */     for (Scrip scrip : scripSet) {
/*     */       try {
/* 136 */         CSVWriter writer = new CSVWriter(this.postProcessPath + "/" + scrip.scripID + " Post Process.csv", false, 
/* 137 */           ",");
/*     */         
/* 139 */         this.scripPostProcessWriterMap.put(scrip.scripID, writer);
/*     */       } catch (IOException e1) {
/* 141 */         btGlobal.displayMessage("Error creating output writer for: " + this.backtestKey + ", " + scrip.scripID);
/* 142 */         e1.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public PostProcessData getPostProcessData(String scripID, String outputName)
/*     */   {
/* 149 */     TreeMap<String, PostProcessData> scripPostProcessMap = (TreeMap)this.postProcessMap.get(scripID);
/*     */     
/*     */ 
/* 152 */     if (scripPostProcessMap == null) {
/* 153 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 157 */     return (PostProcessData)scripPostProcessMap.get(outputName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updatePostProcessMap(PostProcessData ppData, String scripID)
/*     */   {
/* 164 */     if (ppData.scripID == null) {
/* 165 */       ppData.scripID = scripID;
/*     */     }
/*     */     
/* 168 */     TreeMap<String, PostProcessData> scripPostProcessMap = (TreeMap)this.postProcessMap.get(ppData.scripID);
/*     */     
/*     */ 
/* 171 */     if (scripPostProcessMap == null) {
/* 172 */       scripPostProcessMap = new TreeMap();
/* 173 */       scripPostProcessMap.put(ppData.getFileHeader(), ppData);
/* 174 */       this.postProcessMap.put(ppData.scripID, scripPostProcessMap);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 179 */       scripPostProcessMap.put(ppData.getFileHeader(), ppData);
/*     */     }
/*     */   }
/*     */   
/*     */   public void runBacktest()
/*     */     throws Exception
/*     */   {
/* 186 */     if (((this.mainData.date.longValue() > this.startDate.longValue()) || (this.mainData.date.equals(this.startDate))) && (
/* 187 */       (this.mainData.date.longValue() < this.endDate.longValue()) || (this.mainData.date.equals(this.endDate))))
/*     */     {
/*     */ 
/* 190 */       if (!this.mainData.skipBacktest)
/*     */       {
/* 192 */         this.execution.processOrders(this.mainData, this.dataMap, this.orderBook, this.prevOrderTimestamp);
/*     */       }
/*     */       
/*     */ 
/* 196 */       if (!this.mainData.skipBacktest)
/*     */       {
/*     */ 
/* 199 */         if (this.btObj.backtestParameter.isGenerateOutputCheck())
/*     */         {
/*     */ 
/* 202 */           for (Map.Entry<String, ScripExecution> scripEntry : this.execution.executionMap.entrySet())
/*     */           {
/* 204 */             String scripID = (String)scripEntry.getKey();
/* 205 */             ScripExecution scripExec = (ScripExecution)scripEntry.getValue();
/*     */             
/*     */ 
/* 208 */             PostProcessData ppData = getPostProcessData(scripID, "MTM");
/* 209 */             if (ppData == null) {
/* 210 */               ppData = new PostProcessData(scripID, "MTM", OutputShape.Line, 2);
/* 211 */               updatePostProcessMap(ppData, scripID);
/*     */             }
/*     */             
/*     */ 
/* 215 */             Double mtm = Double.valueOf(scripExec.$TradeMTM.doubleValue() / this.btGlobal.loginParameter.getCapital().doubleValue());
/* 216 */             ppData.updateValue(mtm);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 221 */           if (!this.ppHeaderCheck)
/* 222 */             this.ppHeaderCheck = writePostProcessHeader();
/* 223 */           writePostProcessData(this.mainData.dateTime);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 228 */         this.orderBook = this.strategy.processStrategy(this.dataMap, this.execution.positionMap, this.execution.mtmMap, 
/* 229 */           this.execution.dayMTMMap, this.execution.scripTradeBookMap, this.btGlobal.loginParameter.getCapital(), 
/* 230 */           this.btGlobal.loginParameter.getRiskPerTrade());
/* 231 */         this.prevOrderTimestamp = this.mainData.dateTime;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 238 */     this.prevDateTime = this.mainData.dateTime;
/*     */   }
/*     */   
/*     */   public void closeFileStreams()
/*     */   {
/*     */     try
/*     */     {
/* 245 */       for (CSVWriter writer : this.scripPostProcessWriterMap.values())
/* 246 */         writer.close();
/* 247 */       for (CSVWriter writer : this.scripTradebookWriterMap.values())
/* 248 */         writer.close();
/*     */     } catch (IOException e) {
/* 250 */       this.btGlobal.displayMessage("Could not close filewriters: " + this.scripListID);
/* 251 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean writePostProcessHeader()
/*     */   {
/* 258 */     for (Map.Entry<String, CSVWriter> entry : this.scripPostProcessWriterMap.entrySet())
/*     */     {
/* 260 */       String scripID = (String)entry.getKey();
/* 261 */       CSVWriter writer = (CSVWriter)entry.getValue();
/* 262 */       TreeMap<String, PostProcessData> scripPostProcessMap = (TreeMap)this.postProcessMap.get(scripID);
/*     */       
/* 264 */       ArrayList<String> header = new ArrayList();
/*     */       
/*     */ 
/* 267 */       header.add("Date");
/* 268 */       header.add("Time");
/*     */       
/* 270 */       for (String outputName : scripPostProcessMap.keySet()) {
/* 271 */         header.add(outputName);
/*     */       }
/*     */       try
/*     */       {
/* 275 */         writer.writeLine(header);
/*     */       } catch (IOException e1) {
/* 277 */         this.btGlobal.displayMessage("Error writing Post Process Data: " + this.scripListID + ", " + scripID);
/* 278 */         e1.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 283 */     return true;
/*     */   }
/*     */   
/*     */   public void writePostProcessData(Long dateTime)
/*     */   {
/* 288 */     String dateTimeStr = dateTime.toString();
/* 289 */     String date = dateTimeStr.substring(0, 8);
/* 290 */     String time = dateTimeStr.substring(8);
/*     */     
/*     */ 
/* 293 */     for (Map.Entry<String, CSVWriter> entry : this.scripPostProcessWriterMap.entrySet())
/*     */     {
/* 295 */       String scripID = (String)entry.getKey();
/* 296 */       CSVWriter writer = (CSVWriter)entry.getValue();
/* 297 */       TreeMap<String, PostProcessData> scripPostProcessMap = (TreeMap)this.postProcessMap.get(scripID);
/*     */       
/* 299 */       ArrayList<String> dataLine = new ArrayList();
/*     */       
/*     */ 
/*     */ 
/* 303 */       dataLine.add(date);
/* 304 */       dataLine.add(time);
/*     */       
/*     */ 
/* 307 */       for (PostProcessData ppData : scripPostProcessMap.values()) {
/* 308 */         dataLine.add(ppData.outputValue);
/*     */       }
/*     */       try
/*     */       {
/* 312 */         writer.writeLine(dataLine);
/*     */       } catch (IOException e1) {
/* 314 */         this.btGlobal.displayMessage("Error writing Post Process Data: " + this.scripListID + ", " + scripID);
/* 315 */         e1.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static String createDataPath(String folder)
/*     */   {
/* 322 */     if (!new File(folder).exists())
/* 323 */       new File(folder).mkdirs();
/* 324 */     return folder;
/*     */   }
/*     */   
/*     */   public static void createPath(String folder)
/*     */   {
/* 329 */     if (!new File(folder).exists()) {
/* 330 */       new File(folder).mkdirs();
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeObjects() throws IOException
/*     */   {
/* 336 */     String obPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.btObj.timeStamp + "/Object Data";
/* 337 */     createPath(obPath);
/*     */     
/*     */ 
/* 340 */     Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues()
/* 341 */       .excludeFieldsWithModifiers(new int[] {128 }).create();
/* 342 */     FileWriter startWriter = new FileWriter(obPath + "/" + this.backtestKey + " Strategy.json");
/* 343 */     gson.toJson(this.strategy, startWriter);
/* 344 */     startWriter.close();
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/backtest/BacktestDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */