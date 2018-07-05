/*      */ package com.q1.bt.driver;
/*      */ 
/*      */ import com.q1.bt.driver.backtest.enums.AggregationMode;
/*      */ import com.q1.bt.global.BacktesterGlobal;
/*      */ import com.q1.bt.postprocess.PostProcess;
/*      */ import com.q1.bt.process.backtest.PostProcessMode;
/*      */ import com.q1.bt.process.parameter.LoginParameter;
/*      */ import com.q1.csv.CSVReader;
/*      */ import com.q1.csv.CSVWriter;
/*      */ import com.q1.math.MathLib;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ 
/*      */ public class ResultDriver
/*      */ {
/*      */   BacktesterGlobal btGlobal;
/*      */   String timeStamp;
/*      */   PostProcessMode postProcessMode;
/*      */   AggregationMode aggregationMode;
/*   29 */   TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, String>>>> resultsFileMap = new TreeMap();
/*   30 */   TreeSet<String> strategySet = new TreeSet();
/*   31 */   TreeSet<String> scripListSet = new TreeSet();
/*   32 */   TreeSet<String> assetClassSet = new TreeSet();
/*   33 */   TreeSet<String> scripSet = new TreeSet();
/*   34 */   TreeSet<Long> dateSet = new TreeSet();
/*   35 */   long startDate = 0L;
/*   36 */   long endDate = 20910101L;
/*      */   String outputKey;
/*      */   
/*      */   public ResultDriver(BacktesterGlobal btGlobal, String timeStamp, PostProcessMode postProcessMode, AggregationMode aggregationMode)
/*      */     throws Exception
/*      */   {
/*   42 */     this.btGlobal = btGlobal;
/*   43 */     this.timeStamp = timeStamp;
/*   44 */     this.postProcessMode = postProcessMode;
/*   45 */     this.aggregationMode = aggregationMode;
/*   46 */     generateResultsFileMap();
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeSet<String> getSelectableStrategySet()
/*      */   {
/*   52 */     TreeSet<String> selectableStrategySet = new TreeSet();
/*      */     
/*      */ 
/*   55 */     for (String strategyID : this.resultsFileMap.keySet()) {
/*   56 */       selectableStrategySet.add(strategyID);
/*      */     }
/*   58 */     return selectableStrategySet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public TreeSet<String> getSelectableScripListSet(String strategyID)
/*      */   {
/*   65 */     TreeSet<String> selectableScripListSet = new TreeSet();
/*   66 */     this.strategySet = new TreeSet();
/*      */     
/*      */     TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap;
/*   69 */     if (strategyID.equals("All"))
/*      */     {
/*   71 */       Iterator localIterator1 = this.resultsFileMap.entrySet().iterator();
/*   70 */       while (localIterator1.hasNext()) {
/*   71 */         Map.Entry<String, TreeMap<String, TreeMap<String, TreeMap<String, String>>>> strategyEntry = (Map.Entry)localIterator1.next();
/*      */         
/*      */ 
/*   74 */         scripListMap = (TreeMap)strategyEntry.getValue();
/*   75 */         for (String scripListID : scripListMap.keySet()) {
/*   76 */           selectableScripListSet.add(scripListID);
/*      */         }
/*      */         
/*   79 */         String curStrategyID = (String)strategyEntry.getKey();
/*   80 */         this.strategySet.add(curStrategyID);
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*   88 */       TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)this.resultsFileMap.get(strategyID);
/*   89 */       for (String scripListID : scripListMap.keySet()) {
/*   90 */         selectableScripListSet.add(scripListID);
/*      */       }
/*      */       
/*   93 */       this.strategySet.add(strategyID);
/*      */     }
/*      */     
/*      */ 
/*   97 */     this.outputKey = strategyID;
/*      */     
/*   99 */     return selectableScripListSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public TreeSet<String> getSelectableAssetClassSet(String scripListID)
/*      */   {
/*  106 */     TreeSet<String> selectableAssetClassSet = new TreeSet();
/*  107 */     this.scripListSet = new TreeSet();
/*      */     Iterator localIterator1;
/*      */     TreeMap<String, TreeMap<String, String>> assetClassMap;
/*  110 */     if (scripListID.equals("All")) { Iterator localIterator2;
/*  111 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  112 */           localIterator2.hasNext())
/*      */       {
/*  111 */         String curStrategyID = (String)localIterator1.next();
/*      */         
/*  113 */         localIterator2 = ((TreeMap)this.resultsFileMap.get(curStrategyID)).entrySet().iterator(); continue;Map.Entry<String, TreeMap<String, TreeMap<String, String>>> scripListEntry = (Map.Entry)localIterator2.next();
/*      */         
/*      */ 
/*  116 */         assetClassMap = (TreeMap)scripListEntry.getValue();
/*  117 */         for (String assetClassID : assetClassMap.keySet()) {
/*  118 */           selectableAssetClassSet.add(assetClassID);
/*      */         }
/*      */         
/*  121 */         String curScripListID = (String)scripListEntry.getKey();
/*  122 */         this.scripListSet.add(curScripListID);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  127 */       for (String curStrategyID : this.strategySet)
/*      */       {
/*      */ 
/*  130 */         TreeMap<String, TreeMap<String, String>> assetClassMap = 
/*  131 */           (TreeMap)((TreeMap)this.resultsFileMap.get(curStrategyID)).get(scripListID);
/*  132 */         for (String assetClassID : assetClassMap.keySet()) {
/*  133 */           selectableAssetClassSet.add(assetClassID);
/*      */         }
/*      */         
/*  136 */         this.scripListSet.add(scripListID);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  141 */     this.outputKey = (this.outputKey + "|" + scripListID);
/*      */     
/*  143 */     return selectableAssetClassSet;
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeSet<String> getSelectableScripSet(String assetClassID)
/*      */   {
/*  149 */     TreeSet<String> selectableScripSet = new TreeSet();
/*  150 */     this.assetClassSet = new TreeSet();
/*      */     Iterator localIterator1;
/*      */     Iterator localIterator2;
/*  153 */     TreeMap<String, String> scripMap; if (assetClassID.equals("All")) {
/*  154 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  155 */           localIterator2.hasNext())
/*      */       {
/*  154 */         String curStrategyID = (String)localIterator1.next();
/*  155 */         localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
/*      */         
/*  157 */         Iterator localIterator3 = ((TreeMap)((TreeMap)this.resultsFileMap.get(curStrategyID)).get(curScripListID)).entrySet().iterator();
/*  156 */         while (localIterator3.hasNext()) {
/*  157 */           Map.Entry<String, TreeMap<String, String>> assetClassEntry = (Map.Entry)localIterator3.next();
/*      */           
/*      */ 
/*  160 */           scripMap = (TreeMap)assetClassEntry.getValue();
/*  161 */           for (String scripID : scripMap.keySet()) {
/*  162 */             selectableScripSet.add(scripID);
/*      */           }
/*      */           
/*  165 */           String curAssetClassID = (String)assetClassEntry.getKey();
/*  166 */           this.assetClassSet.add(curAssetClassID);
/*      */         }
/*      */         
/*      */       }
/*      */     } else {
/*  171 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  172 */           localIterator2.hasNext())
/*      */       {
/*  171 */         String curStrategyID = (String)localIterator1.next();
/*  172 */         localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
/*      */         
/*      */ 
/*  175 */         TreeMap<String, String> scripMap = 
/*  176 */           (TreeMap)((TreeMap)((TreeMap)this.resultsFileMap.get(curStrategyID)).get(curScripListID)).get(assetClassID);
/*  177 */         if (scripMap != null)
/*      */         {
/*  179 */           for (String scripID : scripMap.keySet()) {
/*  180 */             selectableScripSet.add(scripID);
/*      */           }
/*      */           
/*  183 */           this.assetClassSet.add(assetClassID);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  188 */     return selectableScripSet;
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeSet<Long> getSelectableDateSet(String scripID)
/*      */   {
/*  194 */     TreeSet<Long> selectableDateSet = new TreeSet();
/*  195 */     this.scripSet = new TreeSet();
/*      */     Iterator localIterator1;
/*      */     Iterator localIterator2;
/*  198 */     if (scripID.equals("All")) {
/*  199 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  200 */           localIterator2.hasNext())
/*      */       {
/*  199 */         String curStrategyID = (String)localIterator1.next();
/*  200 */         localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
/*  201 */         for (String curAssetClassID : this.assetClassSet) {
/*  202 */           TreeMap<String, String> scripMap = 
/*  203 */             (TreeMap)((TreeMap)((TreeMap)this.resultsFileMap.get(curStrategyID)).get(curScripListID)).get(curAssetClassID);
/*  204 */           if (scripMap != null)
/*      */           {
/*  206 */             for (Map.Entry<String, String> scripEntry : scripMap.entrySet())
/*      */             {
/*      */ 
/*  209 */               String mtmFilePath = (String)scripEntry.getValue();
/*  210 */               CSVReader reader = null;
/*      */               try {
/*  212 */                 reader = new CSVReader(mtmFilePath, ',', 0);
/*      */                 String[] dataLine;
/*  214 */                 while ((dataLine = reader.getLine()) != null) { String[] dataLine;
/*  215 */                   selectableDateSet.add(Long.valueOf(Long.parseLong(dataLine[0])));
/*      */                 }
/*      */               }
/*      */               catch (Exception localException) {}
/*      */               
/*      */ 
/*  221 */               String curScripID = (String)scripEntry.getKey();
/*  222 */               this.scripSet.add(curScripID);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     } else {
/*  228 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  229 */           localIterator2.hasNext())
/*      */       {
/*  228 */         String curStrategyID = (String)localIterator1.next();
/*  229 */         localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
/*      */         
/*  231 */         String curAssetClassID = scripID.split(" ")[1];
/*      */         
/*      */ 
/*  234 */         Object scripMap = 
/*  235 */           (TreeMap)((TreeMap)((TreeMap)this.resultsFileMap.get(curStrategyID)).get(curScripListID)).get(curAssetClassID);
/*  236 */         if (scripMap != null)
/*      */         {
/*      */ 
/*  239 */           String mtmFilePath = (String)((TreeMap)scripMap).get(scripID);
/*      */           try {
/*  241 */             CSVReader reader = new CSVReader(mtmFilePath, ',', 0);
/*      */             String[] dataLine;
/*  243 */             while ((dataLine = reader.getLine()) != null) { String[] dataLine;
/*  244 */               selectableDateSet.add(Long.valueOf(Long.parseLong(dataLine[0])));
/*      */             }
/*      */           }
/*      */           catch (Exception localException1) {}
/*      */           
/*  249 */           this.scripSet.add(scripID);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  254 */     this.outputKey = (this.outputKey + "|" + scripID);
/*      */     
/*  256 */     return selectableDateSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateSetsForKeys(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */   {
/*  263 */     getSelectableScripListSet(strategyKey);
/*  264 */     getSelectableAssetClassSet(scripListKey);
/*  265 */     getSelectableScripSet(assetClassKey);
/*  266 */     getSelectableDateSet(scripKey);
/*      */     
/*      */ 
/*  269 */     this.startDate = 0L;
/*  270 */     this.endDate = 20910101L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public PostProcess createPostProcessObject()
/*      */     throws Exception
/*      */   {
/*  278 */     TreeMap<Long, Double> mtmMap = generateMTMMap();
/*      */     
/*      */ 
/*  281 */     ArrayList<String[]> tradeBook = generateTradebook();
/*      */     
/*      */ 
/*  284 */     return new PostProcess(mtmMap, this.outputKey, tradeBook, this.btGlobal, this.postProcessMode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void generateResults(String strategyID, String scripListID, String scripID, boolean exportResultsCheck)
/*      */     throws Exception
/*      */   {
/*  292 */     String filePath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results";
/*  293 */     if (!new File(filePath).exists()) {
/*  294 */       new File(filePath).mkdirs();
/*      */     }
/*      */     
/*  297 */     generateResults(this.startDate, this.endDate);
/*      */     
/*      */     try
/*      */     {
/*  301 */       exportAllResults(exportResultsCheck, true);
/*      */     }
/*      */     catch (Exception e) {
/*  304 */       e.printStackTrace();
/*  305 */       return;
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  310 */       this.btGlobal.displayRunParameters(this.timeStamp);
/*      */     }
/*      */     catch (IOException e) {
/*  313 */       this.btGlobal.displayMessage("Error displaying last run parameters");
/*  314 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void generateResults(long startDate, long endDate)
/*      */     throws Exception
/*      */   {
/*  323 */     this.startDate = startDate;
/*  324 */     this.endDate = endDate;
/*      */     
/*      */ 
/*  327 */     PostProcess ppObj = createPostProcessObject();
/*      */     
/*      */     try
/*      */     {
/*  331 */       ppObj.runPostprocess();
/*      */     }
/*      */     catch (ParseException e) {
/*  334 */       this.btGlobal.displayMessage("Error running post process");
/*  335 */       e.printStackTrace();
/*      */     }
/*      */     
/*      */ 
/*  339 */     this.btGlobal.displayResults(ppObj);
/*      */   }
/*      */   
/*      */ 
/*      */   public void exportAllResults(boolean exportResultsCheck, boolean isGui)
/*      */     throws Exception
/*      */   {
/*  346 */     if (exportResultsCheck)
/*      */     {
/*  348 */       Iterator localIterator1 = this.resultsFileMap.entrySet().iterator();
/*      */       Iterator localIterator2;
/*  347 */       for (; localIterator1.hasNext(); 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  355 */           localIterator2.hasNext())
/*      */       {
/*  348 */         Map.Entry<String, TreeMap<String, TreeMap<String, TreeMap<String, String>>>> strategyEntry = (Map.Entry)localIterator1.next();
/*      */         
/*      */ 
/*  351 */         String strategyID = (String)strategyEntry.getKey();
/*  352 */         exportResultsForKey(strategyID, "All", "All", "All");
/*      */         
/*  354 */         TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)strategyEntry.getValue();
/*  355 */         localIterator2 = scripListMap.entrySet().iterator(); continue;Map.Entry<String, TreeMap<String, TreeMap<String, String>>> scripListEntry = (Map.Entry)localIterator2.next();
/*      */         
/*      */ 
/*  358 */         String scripListID = (String)scripListEntry.getKey();
/*  359 */         exportResultsForKey(strategyID, scripListID, "All", "All");
/*      */         
/*  361 */         TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListEntry.getValue();
/*  362 */         Iterator localIterator4; for (Iterator localIterator3 = assetClassMap.entrySet().iterator(); localIterator3.hasNext(); 
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  369 */             localIterator4.hasNext())
/*      */         {
/*  362 */           Map.Entry<String, TreeMap<String, String>> assetClassEntry = (Map.Entry)localIterator3.next();
/*      */           
/*      */ 
/*  365 */           String assetClassID = (String)assetClassEntry.getKey();
/*  366 */           exportResultsForKey(strategyID, scripListID, assetClassID, "All");
/*      */           
/*  368 */           TreeMap<String, String> scripMap = (TreeMap)assetClassEntry.getValue();
/*  369 */           localIterator4 = scripMap.keySet().iterator(); continue;String scripID = (String)localIterator4.next();
/*      */           
/*      */ 
/*  372 */           exportResultsForKey(strategyID, scripListID, assetClassID, scripID);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  378 */     exportResultsForKey("All", "All", "All", "All");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void generateAndExportResultsForKey(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */     throws Exception
/*      */   {
/*  387 */     updateSetsForKeys(strategyKey, scripListKey, assetClassKey, scripKey);
/*      */     
/*      */ 
/*  390 */     PostProcess ppObj = createPostProcessObject();
/*      */     
/*      */     try
/*      */     {
/*  394 */       ppObj.runPostprocess();
/*  395 */       ppObj.printResults();
/*      */     }
/*      */     catch (ParseException e) {
/*  398 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void exportResultsForKey(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */     throws Exception
/*      */   {
/*  408 */     updateSetsForKeys(strategyKey, scripListKey, assetClassKey, scripKey);
/*      */     
/*      */ 
/*  411 */     PostProcess ppObj = createPostProcessObject();
/*      */     
/*      */ 
/*  414 */     if ((scripListKey.equals("All")) && (assetClassKey.equals("All")) && (scripKey.equals("All")))
/*      */     {
/*      */       try
/*      */       {
/*  418 */         String resultsPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results";
/*  419 */         new File(resultsPath).mkdirs();
/*      */         
/*      */ 
/*  422 */         CSVWriter writer = null;
/*  423 */         writer = new CSVWriter(resultsPath + "/" + strategyKey + " MTM.csv", false, ",");
/*  424 */         for (Map.Entry<Long, Double> entry : ppObj.consolMTM.entrySet()) {
/*  425 */           String[] outLine = { ((Long)entry.getKey()).toString(), ((Double)entry.getValue()).toString() };
/*  426 */           writer.writeLine(outLine);
/*      */         }
/*  428 */         writer.close();
/*      */         
/*      */ 
/*  431 */         writer = new CSVWriter(resultsPath + "/" + strategyKey + " Tradebook.csv", false, ",");
/*  432 */         for (String[] trade : ppObj.tradeBook)
/*  433 */           writer.writeLine(trade);
/*  434 */         writer.close();
/*      */       }
/*      */       catch (IOException e) {
/*  437 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  443 */       ppObj.runPostprocess();
/*  444 */       ppObj.writeToFile(this.timeStamp);
/*      */     } catch (ParseException e) {
/*  446 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ArrayList<String[]> generateTradebook()
/*      */     throws Exception
/*      */   {
/*  457 */     ArrayList<String[]> consolTradebook = new ArrayList();
/*      */     
/*      */ 
/*  460 */     for (String strategyID : this.strategySet)
/*      */     {
/*      */ 
/*  463 */       ArrayList<String[]> strategyTradebook = new ArrayList();
/*      */       
/*      */ 
/*  466 */       for (String scripListID : this.scripListSet)
/*      */       {
/*      */ 
/*  469 */         ArrayList<String[]> scripListTradebook = new ArrayList();
/*      */         
/*      */ 
/*  472 */         for (String scripID : this.scripSet)
/*      */         {
/*      */ 
/*  475 */           if (checkIfResultExpected(strategyID, scripListID, scripID))
/*      */           {
/*      */ 
/*  478 */             ArrayList<String[]> scripTradebook = getStrategyScripListScripTradebook(strategyID, scripListID, 
/*  479 */               scripID);
/*      */             
/*  481 */             if (!this.postProcessMode.equals(PostProcessMode.Portfolio)) {
/*  482 */               scripTradebook = cleanTradebookForRunningTrades(scripTradebook);
/*      */             }
/*      */             
/*      */ 
/*  486 */             scripListTradebook.addAll(scripTradebook);
/*      */           }
/*      */         }
/*      */         
/*  490 */         if (this.postProcessMode.equals(PostProcessMode.Spread)) {
/*  491 */           scripListTradebook = timeSortTradebook(scripListTradebook);
/*      */         }
/*      */         
/*      */ 
/*  495 */         strategyTradebook.addAll(scripListTradebook);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  500 */       consolTradebook.addAll(strategyTradebook);
/*      */     }
/*      */     
/*  503 */     return consolTradebook;
/*      */   }
/*      */   
/*      */   private ArrayList<String[]> timeSortTradebook(ArrayList<String[]> scripListTradebook) {
/*  507 */     TreeMap<String, ArrayList<String[]>> tradeBookMap = new TreeMap();
/*      */     
/*      */ 
/*  510 */     for (String[] line : scripListTradebook) {
/*  511 */       if (tradeBookMap.get(line[0]) == null) {
/*  512 */         ArrayList<String[]> trades = new ArrayList();
/*  513 */         trades.add(line);
/*  514 */         tradeBookMap.put(line[0], trades);
/*      */       } else {
/*  516 */         trades = (ArrayList)tradeBookMap.get(line[0]);
/*  517 */         trades.add(line);
/*  518 */         tradeBookMap.put(line[0], trades);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  523 */     ArrayList<String[]> sortedTradeBook = new ArrayList();
/*      */     
/*      */     Iterator localIterator2;
/*  526 */     for (ArrayList<String[]> trades = tradeBookMap.keySet().iterator(); trades.hasNext(); 
/*      */         
/*  528 */         localIterator2.hasNext())
/*      */     {
/*  526 */       String key = (String)trades.next();
/*  527 */       ArrayList<String[]> trades = (ArrayList)tradeBookMap.get(key);
/*  528 */       localIterator2 = trades.iterator(); continue;String[] line = (String[])localIterator2.next();
/*  529 */       sortedTradeBook.add(line);
/*      */     }
/*      */     
/*  532 */     return sortedTradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */   public ArrayList<String[]> getConsolTradebook()
/*      */   {
/*  538 */     String tbFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Trade Data";
/*      */     
/*  540 */     File tbFolderPathFile = new File(tbFolderPath);
/*  541 */     File[] tbFolders = tbFolderPathFile.listFiles();
/*      */     
/*  543 */     ArrayList<String[]> consolTradeBook = new ArrayList();
/*      */     
/*      */     try
/*      */     {
/*  547 */       CSVWriter writer = null;
/*  548 */       writer = new CSVWriter(
/*  549 */         this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results/Consol Tradebook.csv", false, 
/*  550 */         ",");
/*      */       
/*      */       File[] arrayOfFile1;
/*  553 */       int j = (arrayOfFile1 = tbFolders).length; for (int i = 0; i < j; i++) { File tbFolder = arrayOfFile1[i];
/*      */         
/*      */ 
/*  556 */         String[] tbFolderVal = tbFolder.getName().split(" ");
/*      */         
/*  558 */         String strategyID = tbFolderVal[0];
/*  559 */         String scripListID = tbFolderVal[1];
/*      */         
/*      */ 
/*  562 */         if (tbFolderVal.length > 2) {
/*  563 */           scripListID = 
/*  564 */             tbFolderVal[1] + " " + tbFolderVal[2] + " " + tbFolderVal[3] + " " + tbFolderVal[4] + " " + tbFolderVal[5];
/*      */         }
/*      */         
/*  567 */         String tbPath = tbFolderPath + "/" + tbFolder.getName();
/*  568 */         File tbPathFile = new File(tbPath);
/*  569 */         File[] tbFiles = tbPathFile.listFiles();
/*      */         
/*      */         File[] arrayOfFile2;
/*  572 */         int m = (arrayOfFile2 = tbFiles).length; for (int k = 0; k < m; k++) { File tbFile = arrayOfFile2[k];
/*      */           
/*  574 */           String tbFileName = tbFile.getName();
/*  575 */           String scripID = tbFileName.substring(0, tbFileName.length() - 14);
/*      */           
/*  577 */           ArrayList<String[]> tradeBook = getStrategyScripListScripTradebook(strategyID, scripListID, 
/*  578 */             scripID);
/*      */           
/*  580 */           for (String[] trade : tradeBook) {
/*  581 */             String[] tradeOut = { trade[9], trade[10], trade[0], trade[1], trade[2], trade[3], trade[4], 
/*  582 */               trade[5], trade[6], trade[7], trade[8] };
/*  583 */             writer.writeLine(tradeOut);
/*      */           }
/*      */           
/*  586 */           if (tradeBook.size() > 0) {
/*  587 */             consolTradeBook.addAll(tradeBook);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  592 */       writer.close();
/*      */     }
/*      */     catch (IOException e) {
/*  595 */       e.printStackTrace();
/*      */     }
/*  597 */     return consolTradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ArrayList<String[]> getStrategyScripListScripTradebook(String strategyID, String scripListID, String scripID)
/*      */   {
/*  604 */     ArrayList<String[]> tradeBook = new ArrayList();
/*  605 */     String tbPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Trade Data";
/*  606 */     String tbFilePath = tbPath + "/" + strategyID + " " + scripListID + "/" + scripID + " Tradebook.csv";
/*  607 */     CSVReader reader = null;
/*      */     try {
/*  609 */       reader = new CSVReader(tbFilePath, ',', 0);
/*      */       String[] dataLine;
/*  611 */       while ((dataLine = reader.getLine()) != null) {
/*      */         String[] dataLine;
/*  613 */         long date = Long.parseLong(dataLine[0]) / 1000000L;
/*      */         
/*      */ 
/*  616 */         if (date >= this.startDate)
/*      */         {
/*      */ 
/*      */ 
/*  620 */           if (date > this.endDate) {
/*      */             break;
/*      */           }
/*      */           
/*  624 */           tradeBook.add(dataLine);
/*      */         }
/*      */       }
/*  627 */     } catch (IOException e1) { this.btGlobal.displayMessage("Could not find Tradebook: " + strategyID + " " + scripListID + " " + scripID);
/*  628 */       e1.printStackTrace();
/*  629 */       return null;
/*      */     }
/*      */     
/*  632 */     return tradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreeMap<Long, Double> generateMTMMap()
/*      */     throws Exception
/*      */   {
/*  641 */     TreeMap<Long, Double> consolMTMMap = new TreeMap();
/*      */     
/*      */ 
/*  644 */     for (String strategyID : this.strategySet)
/*      */     {
/*      */ 
/*  647 */       TreeMap<Long, Double> strategyMTMMap = new TreeMap();
/*      */       
/*      */ 
/*  650 */       for (String scripListID : this.scripListSet)
/*      */       {
/*      */ 
/*  653 */         TreeMap<Long, Double> scripListMTMMap = new TreeMap();
/*      */         
/*      */ 
/*  656 */         for (String scripID : this.scripSet)
/*      */         {
/*      */ 
/*  659 */           if (checkIfResultExpected(strategyID, scripListID, scripID))
/*      */           {
/*      */ 
/*  662 */             TreeMap<Long, Double> scripMTMMap = getStrategyScripListScripMTM(strategyID, scripListID, scripID);
/*      */             
/*      */ 
/*  665 */             appendMTMmap(scripListMTMMap, scripMTMMap);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  670 */         appendMTMmap(strategyMTMMap, scripListMTMMap);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  675 */       TreeMap<Long, Integer> dateScripCountMap = new TreeMap();
/*      */       
/*  677 */       if (this.aggregationMode.equals(AggregationMode.Active)) {
/*  678 */         dateScripCountMap = createDateScripCountMap(strategyID);
/*      */       }
/*      */       
/*  681 */       adjustMTMmap(strategyMTMMap, Integer.valueOf(this.scripListSet.size()), this.aggregationMode, dateScripCountMap);
/*      */       
/*      */ 
/*  684 */       appendMTMmap(consolMTMMap, strategyMTMMap);
/*      */     }
/*      */     
/*      */ 
/*  688 */     adjustMTMmap(consolMTMMap, Integer.valueOf(this.strategySet.size()), AggregationMode.Fixed, new TreeMap());
/*      */     
/*  690 */     return consolMTMMap;
/*      */   }
/*      */   
/*      */   public TreeMap<Long, Double> getConsolMTM()
/*      */     throws Exception
/*      */   {
/*  696 */     String mtmFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/MTM Data";
/*      */     
/*  698 */     File mtmFolderPathFile = new File(mtmFolderPath);
/*  699 */     File[] mtmFolders = mtmFolderPathFile.listFiles();
/*      */     
/*      */ 
/*  702 */     TreeMap<Long, Double> consolMTMMap = new TreeMap();
/*      */     
/*      */ 
/*  705 */     TreeMap<Long, Integer> dateScripCountMap = new TreeMap();
/*      */     
/*      */     File[] arrayOfFile1;
/*  708 */     int j = (arrayOfFile1 = mtmFolders).length; for (int i = 0; i < j; i++) { File mtmFolder = arrayOfFile1[i];
/*      */       
/*      */ 
/*  711 */       String[] mtmFolderVal = mtmFolder.getName().split(" ");
/*      */       
/*  713 */       String strategyID = mtmFolderVal[0];
/*  714 */       String scripListID = mtmFolderVal[1];
/*      */       
/*      */ 
/*  717 */       if (mtmFolderVal.length > 2) {
/*  718 */         scripListID = 
/*  719 */           mtmFolderVal[1] + " " + mtmFolderVal[2] + " " + mtmFolderVal[3] + " " + mtmFolderVal[4] + " " + mtmFolderVal[5];
/*      */       }
/*      */       
/*  722 */       TreeMap<Long, Double> scripListMTMMap = new TreeMap();
/*      */       
/*      */ 
/*  725 */       String mtmPath = mtmFolderPath + "/" + mtmFolder.getName();
/*  726 */       File mtmPathFile = new File(mtmPath);
/*  727 */       File[] mtmFiles = mtmPathFile.listFiles();
/*      */       
/*      */       File[] arrayOfFile2;
/*  730 */       int m = (arrayOfFile2 = mtmFiles).length; for (int k = 0; k < m; k++) { File mtmFile = arrayOfFile2[k];
/*      */         
/*  732 */         String mtmFileName = mtmFile.getName();
/*  733 */         String scripID = mtmFileName.substring(0, mtmFileName.length() - 8);
/*      */         
/*  735 */         TreeMap<Long, Double> scripMTMMap = getStrategyScripListScripMTM(strategyID, scripListID, scripID);
/*      */         
/*      */ 
/*  738 */         appendMTMmap(scripListMTMMap, scripMTMMap);
/*      */       }
/*      */       
/*      */ 
/*  742 */       appendMTMmap(consolMTMMap, scripListMTMMap);
/*      */       
/*  744 */       if (this.aggregationMode.equals(AggregationMode.Active)) {
/*  745 */         dateScripCountMap = createDateScripCountMap(strategyID);
/*      */       }
/*      */     }
/*      */     
/*  749 */     adjustMTMmap(consolMTMMap, Integer.valueOf(mtmFolders.length), this.aggregationMode, dateScripCountMap);
/*      */     
/*      */ 
/*  752 */     CSVWriter writer = null;
/*      */     try {
/*  754 */       writer = new CSVWriter(
/*  755 */         this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results/Consol MTM.csv", false, ",");
/*      */     }
/*      */     catch (IOException e) {
/*  758 */       e.printStackTrace();
/*      */     }
/*  760 */     for (Object entry : consolMTMMap.entrySet()) {
/*  761 */       String[] outLine = { ((Long)((Map.Entry)entry).getKey()).toString(), ((Double)((Map.Entry)entry).getValue()).toString() };
/*      */       try {
/*  763 */         writer.writeLine(outLine);
/*      */       }
/*      */       catch (IOException e) {
/*  766 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     try {
/*  770 */       writer.close();
/*      */     }
/*      */     catch (IOException e) {
/*  773 */       e.printStackTrace();
/*      */     }
/*  775 */     return consolMTMMap;
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeMap<Long, Double> getStrategyScripListScripMTM(String strategyID, String scripListID, String scripID)
/*      */   {
/*  781 */     TreeMap<Long, Double> mtmMap = new TreeMap();
/*      */     
/*  783 */     String mtmPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/MTM Data/" + strategyID + " " + 
/*  784 */       scripListID;
/*      */     
/*  786 */     String mtmFilePath = mtmPath + "/" + scripID + " MTM.csv";
/*      */     
/*  788 */     CSVReader reader = null;
/*      */     try {
/*  790 */       reader = new CSVReader(mtmFilePath, ',', 0);
/*      */       String[] inData;
/*  792 */       while ((inData = reader.getLine()) != null) {
/*      */         String[] inData;
/*  794 */         Long date = Long.valueOf(Long.parseLong(inData[0]));
/*      */         
/*      */ 
/*  797 */         if (date.longValue() >= this.startDate)
/*      */         {
/*      */ 
/*      */ 
/*  801 */           if (date.longValue() > this.endDate) {
/*      */             break;
/*      */           }
/*      */           
/*  805 */           Double mtm = Double.valueOf(Double.parseDouble(inData[1]));
/*  806 */           mtmMap.put(date, mtm);
/*      */         }
/*      */       }
/*  809 */     } catch (IOException e1) { this.btGlobal.displayMessage("Could not find MTM File: " + strategyID + " " + scripListID + " " + scripID);
/*  810 */       e1.printStackTrace();
/*  811 */       return null;
/*      */     }
/*      */     
/*  814 */     return mtmMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean checkIfResultExpected(String strategyID, String scripListID, String scripID)
/*      */   {
/*  824 */     TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)this.resultsFileMap.get(strategyID);
/*  825 */     if (scripListMap == null) {
/*  826 */       return false;
/*      */     }
/*      */     
/*  829 */     TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListMap.get(scripListID);
/*  830 */     if (assetClassMap == null) {
/*  831 */       return false;
/*      */     }
/*  833 */     for (Map.Entry<String, TreeMap<String, String>> assetClassEntry : assetClassMap.entrySet()) {
/*  834 */       TreeMap<String, String> scripMap = (TreeMap)assetClassEntry.getValue();
/*      */       
/*  836 */       if (scripMap != null)
/*      */       {
/*  838 */         if (scripMap.keySet().contains(scripID))
/*  839 */           return true;
/*      */       }
/*      */     }
/*  842 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ArrayList<String[]> cleanTradebookForRunningTrades(ArrayList<String[]> tradeBook)
/*      */   {
/*  849 */     Double curPosition = Double.valueOf(0.0D);
/*  850 */     Double prevPosition = Double.valueOf(0.0D);
/*  851 */     int lastExitIndex = 0;
/*  852 */     for (int i = 0; i < tradeBook.size(); i++) {
/*  853 */       String[] trade = (String[])tradeBook.get(i);
/*  854 */       String side = trade[3];
/*  855 */       String type = trade[4];
/*      */       
/*  857 */       if (!type.equals("ROLLOVER"))
/*      */       {
/*      */ 
/*  860 */         int signal = 0;
/*  861 */         if (side.equals("BUY")) {
/*  862 */           signal = 1;
/*      */         } else {
/*  864 */           signal = -1;
/*      */         }
/*  866 */         prevPosition = curPosition;
/*  867 */         curPosition = Double.valueOf(curPosition.doubleValue() + signal * Double.parseDouble(trade[7]));
/*      */         
/*  869 */         if (((MathLib.doubleCompare(curPosition, Double.valueOf(0.0D)).intValue() == 0 ? 1 : 0) & (MathLib.doubleCompare(prevPosition, Double.valueOf(0.0D)).intValue() != 0 ? 1 : 0)) != 0) {
/*  870 */           lastExitIndex = i;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  876 */     while (tradeBook.size() > lastExitIndex + 1) {
/*  877 */       tradeBook.remove(lastExitIndex + 1);
/*      */     }
/*      */     
/*  880 */     return tradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */   public void appendMTMmap(TreeMap<Long, Double> currentMap, TreeMap<Long, Double> newMap)
/*      */   {
/*  886 */     if (newMap.size() == 0) {
/*  887 */       return;
/*      */     }
/*  889 */     for (Map.Entry<Long, Double> mtmEntry : newMap.entrySet()) {
/*  890 */       Long dateTime = (Long)mtmEntry.getKey();
/*  891 */       Double mtm = (Double)mtmEntry.getValue();
/*  892 */       Double curMTM = Double.valueOf(0.0D);
/*      */       try {
/*  894 */         curMTM = (Double)currentMap.get(dateTime);
/*  895 */         if (curMTM == null)
/*  896 */           curMTM = Double.valueOf(0.0D);
/*  897 */         currentMap.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */       } catch (Exception e) {
/*  899 */         curMTM = Double.valueOf(0.0D);
/*  900 */         currentMap.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void adjustMTMmap(TreeMap<Long, Double> currentMap, Integer fixedCount, AggregationMode aggregationMode, TreeMap<Long, Integer> dateScripCountMap)
/*      */     throws Exception
/*      */   {
/*  910 */     if (aggregationMode.equals(AggregationMode.Fixed)) {
/*  911 */       for (Map.Entry<Long, Double> mtmEntry : new TreeMap(currentMap).entrySet()) {
/*  912 */         Long dateTime = (Long)mtmEntry.getKey();
/*  913 */         Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / fixedCount.doubleValue());
/*  914 */         currentMap.put(dateTime, mtm);
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*  919 */     else if (aggregationMode.equals(AggregationMode.Active))
/*      */     {
/*  921 */       ??? = new TreeMap(currentMap).entrySet().iterator();
/*  920 */       while (???.hasNext()) {
/*  921 */         Map.Entry<Long, Double> mtmEntry = (Map.Entry)???.next();
/*  922 */         Long dateTime = (Long)mtmEntry.getKey();
/*  923 */         Double activeCount = getActiveCount(dateTime, dateScripCountMap);
/*  924 */         Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / activeCount.doubleValue());
/*  925 */         currentMap.put(dateTime, mtm);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Double getActiveCount(Long dateTime, TreeMap<Long, Integer> dateScripCountMap)
/*      */   {
/*  934 */     double activeCount = 0.0D;
/*  935 */     for (Map.Entry<Long, Integer> entry : dateScripCountMap.entrySet()) {
/*  936 */       Long curDate = (Long)entry.getKey();
/*  937 */       if (curDate.compareTo(dateTime) > 0) break;
/*  938 */       activeCount += ((Integer)entry.getValue()).intValue();
/*      */     }
/*      */     
/*      */ 
/*  942 */     return Double.valueOf(activeCount);
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeMap<Long, Double> combineMTMmaps(HashMap<String, TreeMap<Long, Double>> mtmMaps)
/*      */   {
/*  948 */     Integer count = Integer.valueOf(mtmMaps.size());
/*  949 */     TreeMap<Long, Double> consolMTM = new TreeMap();
/*  950 */     for (Map.Entry<String, TreeMap<Long, Double>> entry : mtmMaps.entrySet()) {
/*  951 */       TreeMap<Long, Double> mtmMap = (TreeMap)entry.getValue();
/*  952 */       if (mtmMap.size() != 0)
/*      */       {
/*  954 */         for (Map.Entry<Long, Double> mtmEntry : mtmMap.entrySet()) {
/*  955 */           Long dateTime = (Long)mtmEntry.getKey();
/*  956 */           Double mtm = (Double)mtmEntry.getValue();
/*  957 */           Double curMTM = Double.valueOf(0.0D);
/*      */           try {
/*  959 */             curMTM = (Double)consolMTM.get(dateTime);
/*  960 */             if (curMTM == null)
/*  961 */               curMTM = Double.valueOf(0.0D);
/*  962 */             consolMTM.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */           } catch (Exception e) {
/*  964 */             curMTM = Double.valueOf(0.0D);
/*  965 */             consolMTM.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */           }
/*      */         } }
/*      */     }
/*  969 */     for (Map.Entry<Long, Double> mtmEntry : new TreeMap(consolMTM).entrySet()) {
/*  970 */       Long dateTime = (Long)mtmEntry.getKey();
/*  971 */       Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / count.doubleValue());
/*  972 */       consolMTM.put(dateTime, mtm);
/*      */     }
/*  974 */     return consolMTM;
/*      */   }
/*      */   
/*      */   public TreeMap<Long, Integer> createDateScripCountMap(String strategyID) {
/*  978 */     TreeMap<Long, Integer> dateScripCountMap = new TreeMap();
/*  979 */     CSVReader reader = null;
/*      */     try {
/*  981 */       reader = new CSVReader(this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + 
/*  982 */         "/Parameters/" + strategyID + " ScripListDateMap.csv", ',', 0);
/*  983 */       String[] line = reader.getLine();
/*  984 */       while ((line = reader.getLine()) != null) {
/*  985 */         Long date = Long.valueOf(Long.parseLong(line[1]));
/*  986 */         if (dateScripCountMap.containsKey(date)) {
/*  987 */           Integer currentCount = (Integer)dateScripCountMap.get(date);
/*  988 */           dateScripCountMap.put(date, Integer.valueOf(currentCount.intValue() + 1));
/*      */         } else {
/*  990 */           dateScripCountMap.put(date, Integer.valueOf(1));
/*      */         }
/*      */       }
/*      */     } catch (IOException e) {
/*  994 */       e.printStackTrace();
/*      */     }
/*      */     
/*  997 */     return dateScripCountMap;
/*      */   }
/*      */   
/*      */ 
/*      */   String getMTMFilePath(String strategyID, String scripListID, String assetClassID, String scripID)
/*      */   {
/* 1003 */     return (String)((TreeMap)((TreeMap)((TreeMap)this.resultsFileMap.get(strategyID)).get(scripListID)).get(assetClassID)).get(scripID);
/*      */   }
/*      */   
/*      */   public void generateResultsFileMap()
/*      */     throws Exception
/*      */   {
/* 1009 */     this.resultsFileMap = new TreeMap();
/*      */     
/* 1011 */     String mtmFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/MTM Data";
/*      */     
/* 1013 */     File mtmFolderPathFile = new File(mtmFolderPath);
/* 1014 */     File[] mtmFolders = mtmFolderPathFile.listFiles();
/*      */     
/*      */     File[] arrayOfFile1;
/* 1017 */     int j = (arrayOfFile1 = mtmFolders).length; for (int i = 0; i < j; i++) { File mtmFolder = arrayOfFile1[i];
/*      */       
/*      */ 
/* 1020 */       String[] mtmFolderVal = mtmFolder.getName().split(" ");
/*      */       
/* 1022 */       String strategyID = mtmFolderVal[0];
/*      */       
/*      */ 
/* 1025 */       TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListFileMap = (TreeMap)this.resultsFileMap.get(strategyID);
/* 1026 */       if (scripListFileMap == null) {
/* 1027 */         scripListFileMap = new TreeMap();
/*      */       }
/* 1029 */       String scripListID = mtmFolderVal[1];
/* 1030 */       if (mtmFolderVal.length > 2) {
/* 1031 */         scripListID = 
/* 1032 */           mtmFolderVal[1] + " " + mtmFolderVal[2] + " " + mtmFolderVal[3] + " " + mtmFolderVal[4] + " " + mtmFolderVal[5];
/*      */       }
/*      */       
/* 1035 */       String[] splits = mtmFolderVal[1].split("\\$");
/* 1036 */       String assetClassID = splits[1];
/* 1037 */       TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListFileMap.get(scripListID);
/* 1038 */       if (assetClassMap == null) {
/* 1039 */         assetClassMap = new TreeMap();
/*      */       }
/*      */       
/* 1042 */       TreeMap<String, String> scripFileMap = (TreeMap)assetClassMap.get(assetClassID);
/* 1043 */       if (scripFileMap == null) {
/* 1044 */         scripFileMap = new TreeMap();
/*      */       }
/*      */       
/* 1047 */       String mtmPath = mtmFolderPath + "/" + mtmFolder.getName();
/* 1048 */       File mtmPathFile = new File(mtmPath);
/* 1049 */       File[] mtmFiles = mtmPathFile.listFiles();
/*      */       
/*      */       File[] arrayOfFile2;
/* 1052 */       int m = (arrayOfFile2 = mtmFiles).length; for (int k = 0; k < m; k++) { File mtmFile = arrayOfFile2[k];
/* 1053 */         String mtmFileName = mtmFile.getName();
/* 1054 */         String scripID = mtmFileName.substring(0, mtmFileName.length() - 8);
/*      */         
/*      */ 
/* 1057 */         String mtmFilePath = mtmFile.getAbsolutePath();
/* 1058 */         scripFileMap.put(scripID, mtmFilePath);
/*      */       }
/*      */       
/*      */ 
/* 1062 */       assetClassMap.put(assetClassID, scripFileMap);
/* 1063 */       scripListFileMap.put(scripListID, assetClassMap);
/* 1064 */       this.resultsFileMap.put(strategyID, scripListFileMap);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/ResultDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */