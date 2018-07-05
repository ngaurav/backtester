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
/*      */ public class RollingAnalysisDriver
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
/*      */   public RollingAnalysisDriver(BacktesterGlobal btGlobal, String timeStamp, PostProcessMode postProcessMode, AggregationMode aggregationMode)
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
/*      */ 
/*      */ 
/*  190 */     return selectableScripSet;
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeSet<Long> getSelectableDateSet(String scripID)
/*      */   {
/*  196 */     TreeSet<Long> selectableDateSet = new TreeSet();
/*  197 */     this.scripSet = new TreeSet();
/*      */     Iterator localIterator1;
/*      */     Iterator localIterator2;
/*  200 */     if (scripID.equals("All")) {
/*  201 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  202 */           localIterator2.hasNext())
/*      */       {
/*  201 */         String curStrategyID = (String)localIterator1.next();
/*  202 */         localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
/*  203 */         for (String curAssetClassID : this.assetClassSet) {
/*  204 */           TreeMap<String, String> scripMap = 
/*  205 */             (TreeMap)((TreeMap)((TreeMap)this.resultsFileMap.get(curStrategyID)).get(curScripListID)).get(curAssetClassID);
/*  206 */           if (scripMap != null)
/*      */           {
/*  208 */             for (Map.Entry<String, String> scripEntry : scripMap.entrySet())
/*      */             {
/*      */ 
/*  211 */               String mtmFilePath = (String)scripEntry.getValue();
/*  212 */               CSVReader reader = null;
/*      */               try {
/*  214 */                 reader = new CSVReader(mtmFilePath, ',', 0);
/*      */                 String[] dataLine;
/*  216 */                 while ((dataLine = reader.getLine()) != null) { String[] dataLine;
/*  217 */                   selectableDateSet.add(Long.valueOf(Long.parseLong(dataLine[0])));
/*      */                 }
/*      */               }
/*      */               catch (Exception localException) {}
/*      */               
/*      */ 
/*  223 */               String curScripID = (String)scripEntry.getKey();
/*  224 */               this.scripSet.add(curScripID);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     } else {
/*  230 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  231 */           localIterator2.hasNext())
/*      */       {
/*  230 */         String curStrategyID = (String)localIterator1.next();
/*  231 */         localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
/*      */         
/*  233 */         String curAssetClassID = scripID.split(" ")[1];
/*      */         
/*      */ 
/*  236 */         Object scripMap = 
/*  237 */           (TreeMap)((TreeMap)((TreeMap)this.resultsFileMap.get(curStrategyID)).get(curScripListID)).get(curAssetClassID);
/*  238 */         if (scripMap != null)
/*      */         {
/*  240 */           String mtmFilePath = (String)((TreeMap)scripMap).get(scripID);
/*      */           try {
/*  242 */             CSVReader reader = new CSVReader(mtmFilePath, ',', 0);
/*      */             String[] dataLine;
/*  244 */             while ((dataLine = reader.getLine()) != null) { String[] dataLine;
/*  245 */               selectableDateSet.add(Long.valueOf(Long.parseLong(dataLine[0])));
/*      */             }
/*      */           }
/*      */           catch (Exception localException1) {}
/*      */           
/*  250 */           this.scripSet.add(scripID);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  255 */     this.outputKey = (this.outputKey + "|" + scripID);
/*      */     
/*  257 */     return selectableDateSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateSetsForKeys(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */   {
/*  264 */     getSelectableScripListSet(strategyKey);
/*  265 */     getSelectableAssetClassSet(assetClassKey);
/*  266 */     getSelectableScripSet(scripListKey);
/*  267 */     getSelectableDateSet(scripKey);
/*      */     
/*      */ 
/*  270 */     this.startDate = 0L;
/*  271 */     this.endDate = 20910101L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public PostProcess createPostProcessObject()
/*      */     throws Exception
/*      */   {
/*  279 */     TreeMap<Long, Double> mtmMap = generateMTMMap();
/*      */     
/*      */ 
/*  282 */     ArrayList<String[]> tradeBook = generateTradebook();
/*      */     
/*      */ 
/*  285 */     return new PostProcess(mtmMap, this.outputKey, tradeBook, this.btGlobal, this.postProcessMode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void generateResults(String strategyID, String scripListID, String scripID, boolean exportResultsCheck)
/*      */     throws Exception
/*      */   {
/*  293 */     String filePath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results";
/*  294 */     if (!new File(filePath).exists()) {
/*  295 */       new File(filePath).mkdirs();
/*      */     }
/*      */     
/*  298 */     generateResults(this.startDate, this.endDate);
/*      */     
/*      */     try
/*      */     {
/*  302 */       exportAllResults(exportResultsCheck, true);
/*      */     }
/*      */     catch (Exception e) {
/*  305 */       e.printStackTrace();
/*  306 */       return;
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  311 */       this.btGlobal.displayRunParameters(this.timeStamp);
/*      */     }
/*      */     catch (IOException e) {
/*  314 */       this.btGlobal.displayMessage("Error displaying last run parameters");
/*  315 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void generateResults(long startDate, long endDate)
/*      */     throws Exception
/*      */   {
/*  323 */     HashMap<Long, HashMap<String, Double>> resultMaps = new HashMap();
/*      */     
/*      */ 
/*  326 */     long yearEnd = startDate / 10000L;
/*  327 */     yearEnd = yearEnd * 10000L + 1231L;
/*      */     
/*      */ 
/*  330 */     this.startDate = startDate;
/*      */     
/*  332 */     while (yearEnd < endDate) {
/*  333 */       this.endDate = yearEnd;
/*  334 */       PostProcess ppObj = createPostProcessObject();
/*      */       
/*      */       try
/*      */       {
/*  338 */         ppObj.runPostprocess();
/*      */       }
/*      */       catch (ParseException e) {
/*  341 */         this.btGlobal.displayMessage("Error running post process");
/*  342 */         e.printStackTrace();
/*      */       }
/*      */       
/*  345 */       resultMaps.put(Long.valueOf(this.endDate), ppObj.getResultsMap());
/*      */       
/*  347 */       this.startDate = yearEnd;
/*  348 */       yearEnd += 10000L;
/*      */     }
/*      */     
/*      */ 
/*  352 */     this.endDate = endDate;
/*  353 */     PostProcess ppObj = createPostProcessObject();
/*      */     try {
/*  355 */       ppObj.runPostprocess();
/*      */     }
/*      */     catch (ParseException e) {
/*  358 */       this.btGlobal.displayMessage("Error running post process");
/*  359 */       e.printStackTrace();
/*      */     }
/*  361 */     resultMaps.put(Long.valueOf(this.endDate), ppObj.getResultsMap());
/*      */     
/*      */ 
/*      */ 
/*  365 */     this.btGlobal.displayResults(resultMaps);
/*      */   }
/*      */   
/*      */ 
/*      */   public void exportAllResults(boolean exportResultsCheck, boolean isGui)
/*      */     throws Exception
/*      */   {
/*  372 */     if (isGui) {
/*  373 */       exportResultsForKey("All", "All", "All", "All");
/*  374 */     } else if (exportResultsCheck)
/*      */     {
/*  376 */       Iterator localIterator1 = this.resultsFileMap.entrySet().iterator();
/*      */       Iterator localIterator2;
/*  375 */       for (; localIterator1.hasNext(); 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  383 */           localIterator2.hasNext())
/*      */       {
/*  376 */         Map.Entry<String, TreeMap<String, TreeMap<String, TreeMap<String, String>>>> strategyEntry = (Map.Entry)localIterator1.next();
/*      */         
/*      */ 
/*  379 */         String strategyID = (String)strategyEntry.getKey();
/*  380 */         exportResultsForKey(strategyID, "All", "All", "All");
/*      */         
/*  382 */         TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)strategyEntry.getValue();
/*  383 */         localIterator2 = scripListMap.entrySet().iterator(); continue;Map.Entry<String, TreeMap<String, TreeMap<String, String>>> scripListEntry = (Map.Entry)localIterator2.next();
/*      */         
/*      */ 
/*  386 */         String scripListID = (String)scripListEntry.getKey();
/*  387 */         exportResultsForKey(strategyID, scripListID, "All", "All");
/*      */         
/*  389 */         TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListEntry.getValue();
/*  390 */         Iterator localIterator4; for (Iterator localIterator3 = assetClassMap.entrySet().iterator(); localIterator3.hasNext(); 
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  397 */             localIterator4.hasNext())
/*      */         {
/*  390 */           Map.Entry<String, TreeMap<String, String>> assetClassEntry = (Map.Entry)localIterator3.next();
/*      */           
/*      */ 
/*  393 */           String assetClassID = (String)assetClassEntry.getKey();
/*  394 */           exportResultsForKey(strategyID, scripListID, assetClassID, "All");
/*      */           
/*  396 */           TreeMap<String, String> scripMap = (TreeMap)assetClassEntry.getValue();
/*  397 */           localIterator4 = scripMap.keySet().iterator(); continue;String scripID = (String)localIterator4.next();
/*      */           
/*      */ 
/*  400 */           exportResultsForKey(strategyID, scripListID, assetClassID, scripID);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void generateAndExportResultsForKey(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */     throws Exception
/*      */   {
/*  413 */     updateSetsForKeys(strategyKey, scripListKey, assetClassKey, scripKey);
/*      */     
/*      */ 
/*  416 */     PostProcess ppObj = createPostProcessObject();
/*      */     
/*      */     try
/*      */     {
/*  420 */       ppObj.runPostprocess();
/*  421 */       ppObj.printResults();
/*      */     }
/*      */     catch (ParseException e) {
/*  424 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void exportResultsForKey(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */     throws Exception
/*      */   {
/*  434 */     updateSetsForKeys(strategyKey, scripListKey, assetClassKey, scripKey);
/*      */     
/*      */ 
/*  437 */     PostProcess ppObj = createPostProcessObject();
/*      */     
/*      */ 
/*  440 */     if ((scripListKey.equals("All")) && (assetClassKey.equals("All")) && (scripKey.equals("All")))
/*      */     {
/*      */       try
/*      */       {
/*  444 */         String resultsPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results";
/*  445 */         new File(resultsPath).mkdirs();
/*      */         
/*      */ 
/*  448 */         CSVWriter writer = null;
/*  449 */         writer = new CSVWriter(resultsPath + "/" + strategyKey + " MTM.csv", false, ",");
/*  450 */         for (Map.Entry<Long, Double> entry : ppObj.consolMTM.entrySet()) {
/*  451 */           String[] outLine = { ((Long)entry.getKey()).toString(), ((Double)entry.getValue()).toString() };
/*  452 */           writer.writeLine(outLine);
/*      */         }
/*  454 */         writer.close();
/*      */         
/*      */ 
/*  457 */         writer = new CSVWriter(resultsPath + "/" + strategyKey + " Tradebook.csv", false, ",");
/*  458 */         for (String[] trade : ppObj.tradeBook)
/*  459 */           writer.writeLine(trade);
/*  460 */         writer.close();
/*      */       }
/*      */       catch (IOException e) {
/*  463 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  469 */       ppObj.runPostprocess();
/*  470 */       ppObj.writeToFile(this.timeStamp);
/*      */     } catch (ParseException e) {
/*  472 */       e.printStackTrace();
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
/*  483 */     ArrayList<String[]> consolTradebook = new ArrayList();
/*      */     
/*      */ 
/*  486 */     for (String strategyID : this.strategySet)
/*      */     {
/*      */ 
/*  489 */       ArrayList<String[]> strategyTradebook = new ArrayList();
/*      */       
/*      */ 
/*  492 */       for (String scripListID : this.scripListSet)
/*      */       {
/*      */ 
/*  495 */         ArrayList<String[]> scripListTradebook = new ArrayList();
/*      */         
/*      */ 
/*  498 */         for (String scripID : this.scripSet)
/*      */         {
/*      */ 
/*  501 */           if (checkIfResultExpected(strategyID, scripListID, scripID))
/*      */           {
/*      */ 
/*  504 */             ArrayList<String[]> scripTradebook = getStrategyScripListScripTradebook(strategyID, scripListID, 
/*  505 */               scripID);
/*      */             
/*  507 */             if (!this.postProcessMode.equals(PostProcessMode.Portfolio)) {
/*  508 */               scripTradebook = cleanTradebookForRunningTrades(scripTradebook);
/*      */             }
/*      */             
/*      */ 
/*  512 */             scripListTradebook.addAll(scripTradebook);
/*      */           }
/*      */         }
/*      */         
/*  516 */         if (this.postProcessMode.equals(PostProcessMode.Spread)) {
/*  517 */           scripListTradebook = timeSortTradebook(scripListTradebook);
/*      */         }
/*      */         
/*      */ 
/*  521 */         strategyTradebook.addAll(scripListTradebook);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  526 */       consolTradebook.addAll(strategyTradebook);
/*      */     }
/*      */     
/*  529 */     return consolTradebook;
/*      */   }
/*      */   
/*      */   private ArrayList<String[]> timeSortTradebook(ArrayList<String[]> scripListTradebook) {
/*  533 */     TreeMap<String, ArrayList<String[]>> tradeBookMap = new TreeMap();
/*      */     
/*      */ 
/*  536 */     for (String[] line : scripListTradebook) {
/*  537 */       if (tradeBookMap.get(line[0]) == null) {
/*  538 */         ArrayList<String[]> trades = new ArrayList();
/*  539 */         trades.add(line);
/*  540 */         tradeBookMap.put(line[0], trades);
/*      */       } else {
/*  542 */         trades = (ArrayList)tradeBookMap.get(line[0]);
/*  543 */         trades.add(line);
/*  544 */         tradeBookMap.put(line[0], trades);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  549 */     ArrayList<String[]> sortedTradeBook = new ArrayList();
/*      */     
/*      */     Iterator localIterator2;
/*  552 */     for (ArrayList<String[]> trades = tradeBookMap.keySet().iterator(); trades.hasNext(); 
/*      */         
/*  554 */         localIterator2.hasNext())
/*      */     {
/*  552 */       String key = (String)trades.next();
/*  553 */       ArrayList<String[]> trades = (ArrayList)tradeBookMap.get(key);
/*  554 */       localIterator2 = trades.iterator(); continue;String[] line = (String[])localIterator2.next();
/*  555 */       sortedTradeBook.add(line);
/*      */     }
/*      */     
/*  558 */     return sortedTradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */   public ArrayList<String[]> getConsolTradebook()
/*      */   {
/*  564 */     String tbFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Trade Data";
/*      */     
/*  566 */     File tbFolderPathFile = new File(tbFolderPath);
/*  567 */     File[] tbFolders = tbFolderPathFile.listFiles();
/*      */     
/*  569 */     ArrayList<String[]> consolTradeBook = new ArrayList();
/*      */     
/*      */     try
/*      */     {
/*  573 */       CSVWriter writer = null;
/*  574 */       writer = new CSVWriter(
/*  575 */         this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results/Consol Tradebook.csv", false, 
/*  576 */         ",");
/*      */       
/*      */       File[] arrayOfFile1;
/*  579 */       int j = (arrayOfFile1 = tbFolders).length; for (int i = 0; i < j; i++) { File tbFolder = arrayOfFile1[i];
/*      */         
/*      */ 
/*  582 */         String[] tbFolderVal = tbFolder.getName().split(" ");
/*      */         
/*  584 */         String strategyID = tbFolderVal[0];
/*  585 */         String scripListID = tbFolderVal[1];
/*      */         
/*      */ 
/*  588 */         if (tbFolderVal.length > 2) {
/*  589 */           scripListID = 
/*  590 */             tbFolderVal[1] + " " + tbFolderVal[2] + " " + tbFolderVal[3] + " " + tbFolderVal[4] + " " + tbFolderVal[5];
/*      */         }
/*      */         
/*  593 */         String tbPath = tbFolderPath + "/" + tbFolder.getName();
/*  594 */         File tbPathFile = new File(tbPath);
/*  595 */         File[] tbFiles = tbPathFile.listFiles();
/*      */         
/*      */         File[] arrayOfFile2;
/*  598 */         int m = (arrayOfFile2 = tbFiles).length; for (int k = 0; k < m; k++) { File tbFile = arrayOfFile2[k];
/*      */           
/*  600 */           String tbFileName = tbFile.getName();
/*  601 */           String scripID = tbFileName.substring(0, tbFileName.length() - 14);
/*      */           
/*  603 */           ArrayList<String[]> tradeBook = getStrategyScripListScripTradebook(strategyID, scripListID, 
/*  604 */             scripID);
/*      */           
/*  606 */           for (String[] trade : tradeBook) {
/*  607 */             String[] tradeOut = { trade[9], trade[10], trade[0], trade[1], trade[2], trade[3], trade[4], 
/*  608 */               trade[5], trade[6], trade[7], trade[8] };
/*  609 */             writer.writeLine(tradeOut);
/*      */           }
/*      */           
/*  612 */           if (tradeBook.size() > 0) {
/*  613 */             consolTradeBook.addAll(tradeBook);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  618 */       writer.close();
/*      */     }
/*      */     catch (IOException e) {
/*  621 */       e.printStackTrace();
/*      */     }
/*  623 */     return consolTradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ArrayList<String[]> getStrategyScripListScripTradebook(String strategyID, String scripListID, String scripID)
/*      */   {
/*  630 */     ArrayList<String[]> tradeBook = new ArrayList();
/*  631 */     String tbPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Trade Data";
/*  632 */     String tbFilePath = tbPath + "/" + strategyID + " " + scripListID + "/" + scripID + " Tradebook.csv";
/*  633 */     CSVReader reader = null;
/*      */     try {
/*  635 */       reader = new CSVReader(tbFilePath, ',', 0);
/*      */       String[] dataLine;
/*  637 */       while ((dataLine = reader.getLine()) != null) {
/*      */         String[] dataLine;
/*  639 */         long date = Long.parseLong(dataLine[0]) / 1000000L;
/*      */         
/*      */ 
/*  642 */         if (date >= this.startDate)
/*      */         {
/*      */ 
/*      */ 
/*  646 */           if (date > this.endDate) {
/*      */             break;
/*      */           }
/*      */           
/*  650 */           tradeBook.add(dataLine);
/*      */         }
/*      */       }
/*  653 */     } catch (IOException e1) { this.btGlobal.displayMessage("Could not find Tradebook: " + strategyID + " " + scripListID + " " + scripID);
/*  654 */       e1.printStackTrace();
/*  655 */       return null;
/*      */     }
/*      */     
/*  658 */     return tradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreeMap<Long, Double> generateMTMMap()
/*      */     throws Exception
/*      */   {
/*  667 */     TreeMap<Long, Double> consolMTMMap = new TreeMap();
/*      */     
/*      */ 
/*  670 */     for (String strategyID : this.strategySet)
/*      */     {
/*      */ 
/*  673 */       TreeMap<Long, Double> strategyMTMMap = new TreeMap();
/*      */       
/*      */ 
/*  676 */       for (String scripListID : this.scripListSet)
/*      */       {
/*      */ 
/*  679 */         TreeMap<Long, Double> scripListMTMMap = new TreeMap();
/*      */         
/*      */ 
/*  682 */         for (String scripID : this.scripSet)
/*      */         {
/*      */ 
/*  685 */           if (checkIfResultExpected(strategyID, scripListID, scripID))
/*      */           {
/*      */ 
/*  688 */             TreeMap<Long, Double> scripMTMMap = getStrategyScripListScripMTM(strategyID, scripListID, scripID);
/*      */             
/*      */ 
/*  691 */             appendMTMmap(scripListMTMMap, scripMTMMap);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  696 */         appendMTMmap(strategyMTMMap, scripListMTMMap);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  701 */       TreeMap<Long, Integer> activeScripCountMap = new TreeMap();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  707 */       adjustMTMmap(strategyMTMMap, Integer.valueOf(this.scripListSet.size()), "Fixed", activeScripCountMap);
/*      */       
/*      */ 
/*  710 */       appendMTMmap(consolMTMMap, strategyMTMMap);
/*      */     }
/*      */     
/*      */ 
/*  714 */     adjustMTMmap(consolMTMMap, Integer.valueOf(this.strategySet.size()), "Fixed", new TreeMap());
/*      */     
/*  716 */     return consolMTMMap;
/*      */   }
/*      */   
/*      */   public TreeMap<Long, Double> getConsolMTM()
/*      */     throws Exception
/*      */   {
/*  722 */     String mtmFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/MTM Data";
/*      */     
/*  724 */     File mtmFolderPathFile = new File(mtmFolderPath);
/*  725 */     File[] mtmFolders = mtmFolderPathFile.listFiles();
/*      */     
/*      */ 
/*  728 */     TreeMap<Long, Double> consolMTMMap = new TreeMap();
/*      */     
/*      */ 
/*  731 */     TreeMap<Long, Integer> activeScripCountMap = new TreeMap();
/*      */     
/*      */     File[] arrayOfFile1;
/*  734 */     int j = (arrayOfFile1 = mtmFolders).length; for (int i = 0; i < j; i++) { File mtmFolder = arrayOfFile1[i];
/*      */       
/*      */ 
/*  737 */       String[] mtmFolderVal = mtmFolder.getName().split(" ");
/*      */       
/*  739 */       String strategyID = mtmFolderVal[0];
/*  740 */       String scripListID = mtmFolderVal[1];
/*      */       
/*      */ 
/*  743 */       if (mtmFolderVal.length > 2) {
/*  744 */         scripListID = 
/*  745 */           mtmFolderVal[1] + " " + mtmFolderVal[2] + " " + mtmFolderVal[3] + " " + mtmFolderVal[4] + " " + mtmFolderVal[5];
/*      */       }
/*      */       
/*  748 */       TreeMap<Long, Double> scripListMTMMap = new TreeMap();
/*      */       
/*      */ 
/*  751 */       String mtmPath = mtmFolderPath + "/" + mtmFolder.getName();
/*  752 */       File mtmPathFile = new File(mtmPath);
/*  753 */       File[] mtmFiles = mtmPathFile.listFiles();
/*      */       
/*      */       File[] arrayOfFile2;
/*  756 */       int m = (arrayOfFile2 = mtmFiles).length; for (int k = 0; k < m; k++) { File mtmFile = arrayOfFile2[k];
/*      */         
/*  758 */         String mtmFileName = mtmFile.getName();
/*  759 */         String scripID = mtmFileName.substring(0, mtmFileName.length() - 8);
/*      */         
/*  761 */         TreeMap<Long, Double> scripMTMMap = getStrategyScripListScripMTM(strategyID, scripListID, scripID);
/*      */         
/*      */ 
/*  764 */         appendMTMmap(scripListMTMMap, scripMTMMap);
/*      */       }
/*      */       
/*      */ 
/*  768 */       appendMTMmap(consolMTMMap, scripListMTMMap);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  775 */     adjustMTMmap(consolMTMMap, Integer.valueOf(mtmFolders.length), "Fixed", activeScripCountMap);
/*      */     
/*      */ 
/*  778 */     CSVWriter writer = null;
/*      */     try {
/*  780 */       writer = new CSVWriter(
/*  781 */         this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results/Consol MTM.csv", false, ",");
/*      */     }
/*      */     catch (IOException e) {
/*  784 */       e.printStackTrace();
/*      */     }
/*  786 */     for (Object entry : consolMTMMap.entrySet()) {
/*  787 */       String[] outLine = { ((Long)((Map.Entry)entry).getKey()).toString(), ((Double)((Map.Entry)entry).getValue()).toString() };
/*      */       try {
/*  789 */         writer.writeLine(outLine);
/*      */       }
/*      */       catch (IOException e) {
/*  792 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     try {
/*  796 */       writer.close();
/*      */     }
/*      */     catch (IOException e) {
/*  799 */       e.printStackTrace();
/*      */     }
/*  801 */     return consolMTMMap;
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeMap<Long, Double> getStrategyScripListScripMTM(String strategyID, String scripListID, String scripID)
/*      */   {
/*  807 */     TreeMap<Long, Double> mtmMap = new TreeMap();
/*      */     
/*  809 */     String mtmPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/MTM Data/" + strategyID + " " + 
/*  810 */       scripListID;
/*      */     
/*  812 */     String mtmFilePath = mtmPath + "/" + scripID + " MTM.csv";
/*      */     
/*  814 */     CSVReader reader = null;
/*      */     try {
/*  816 */       reader = new CSVReader(mtmFilePath, ',', 0);
/*      */       String[] inData;
/*  818 */       while ((inData = reader.getLine()) != null) {
/*      */         String[] inData;
/*  820 */         Long date = Long.valueOf(Long.parseLong(inData[0]));
/*      */         
/*      */ 
/*  823 */         if (date.longValue() >= this.startDate)
/*      */         {
/*      */ 
/*      */ 
/*  827 */           if (date.longValue() > this.endDate) {
/*      */             break;
/*      */           }
/*      */           
/*  831 */           Double mtm = Double.valueOf(Double.parseDouble(inData[1]));
/*  832 */           mtmMap.put(date, mtm);
/*      */         }
/*      */       }
/*  835 */     } catch (IOException e1) { this.btGlobal.displayMessage("Could not find MTM File: " + strategyID + " " + scripListID + " " + scripID);
/*  836 */       e1.printStackTrace();
/*  837 */       return null;
/*      */     }
/*      */     
/*  840 */     return mtmMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean checkIfResultExpected(String strategyID, String scripListID, String scripID)
/*      */   {
/*  850 */     TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)this.resultsFileMap.get(strategyID);
/*  851 */     if (scripListMap == null) {
/*  852 */       return false;
/*      */     }
/*      */     
/*  855 */     TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListMap.get(scripListID);
/*  856 */     if (assetClassMap == null) {
/*  857 */       return false;
/*      */     }
/*  859 */     for (Map.Entry<String, TreeMap<String, String>> assetClassEntry : assetClassMap.entrySet()) {
/*  860 */       TreeMap<String, String> scripMap = (TreeMap)assetClassEntry.getValue();
/*      */       
/*  862 */       if (scripMap.keySet().contains(scripID)) {
/*  863 */         return true;
/*      */       }
/*      */     }
/*  866 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ArrayList<String[]> cleanTradebookForRunningTrades(ArrayList<String[]> tradeBook)
/*      */   {
/*  873 */     Double curPosition = Double.valueOf(0.0D);
/*  874 */     Double prevPosition = Double.valueOf(0.0D);
/*  875 */     int lastExitIndex = 0;
/*  876 */     for (int i = 0; i < tradeBook.size(); i++) {
/*  877 */       String[] trade = (String[])tradeBook.get(i);
/*  878 */       String side = trade[3];
/*  879 */       String type = trade[4];
/*      */       
/*  881 */       if (!type.equals("ROLLOVER"))
/*      */       {
/*      */ 
/*  884 */         int signal = 0;
/*  885 */         if (side.equals("BUY")) {
/*  886 */           signal = 1;
/*      */         } else {
/*  888 */           signal = -1;
/*      */         }
/*  890 */         prevPosition = curPosition;
/*  891 */         curPosition = Double.valueOf(curPosition.doubleValue() + signal * Double.parseDouble(trade[7]));
/*      */         
/*  893 */         if (((MathLib.doubleCompare(curPosition, Double.valueOf(0.0D)).intValue() == 0 ? 1 : 0) & (MathLib.doubleCompare(prevPosition, Double.valueOf(0.0D)).intValue() != 0 ? 1 : 0)) != 0) {
/*  894 */           lastExitIndex = i;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  900 */     while (tradeBook.size() > lastExitIndex + 1) {
/*  901 */       tradeBook.remove(lastExitIndex + 1);
/*      */     }
/*      */     
/*  904 */     return tradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */   public void appendMTMmap(TreeMap<Long, Double> currentMap, TreeMap<Long, Double> newMap)
/*      */   {
/*  910 */     if (newMap.size() == 0) {
/*  911 */       return;
/*      */     }
/*  913 */     for (Map.Entry<Long, Double> mtmEntry : newMap.entrySet()) {
/*  914 */       Long dateTime = (Long)mtmEntry.getKey();
/*  915 */       Double mtm = (Double)mtmEntry.getValue();
/*  916 */       Double curMTM = Double.valueOf(0.0D);
/*      */       try {
/*  918 */         curMTM = (Double)currentMap.get(dateTime);
/*  919 */         if (curMTM == null)
/*  920 */           curMTM = Double.valueOf(0.0D);
/*  921 */         currentMap.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */       } catch (Exception e) {
/*  923 */         curMTM = Double.valueOf(0.0D);
/*  924 */         currentMap.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void adjustMTMmap(TreeMap<Long, Double> currentMap, Integer count, String aggregationMode, TreeMap<Long, Integer> activeScripCountMap)
/*      */     throws Exception
/*      */   {
/*  934 */     if (aggregationMode.equalsIgnoreCase("Fixed")) {
/*  935 */       for (Map.Entry<Long, Double> mtmEntry : new TreeMap(currentMap).entrySet()) {
/*  936 */         Long dateTime = (Long)mtmEntry.getKey();
/*  937 */         Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / count.doubleValue());
/*  938 */         currentMap.put(dateTime, mtm);
/*      */ 
/*      */       }
/*      */       
/*      */     }
/*  943 */     else if (aggregationMode.equalsIgnoreCase("Active")) {
/*  944 */       for (Map.Entry<Long, Double> mtmEntry : new TreeMap(currentMap).entrySet()) {
/*  945 */         Long dateTime = (Long)mtmEntry.getKey();
/*  946 */         Integer activeScripCount = (Integer)activeScripCountMap.get(dateTime);
/*  947 */         if (activeScripCount == null) {
/*  948 */           this.btGlobal.displayMessage("Scrip count for date: " + dateTime + " not available");
/*  949 */           throw new Exception("Scrip count for date: " + dateTime + " not available");
/*      */         }
/*  951 */         Double mtm = Double.valueOf(activeScripCount.intValue() < 1 ? 0.0D : ((Double)mtmEntry.getValue()).doubleValue() / activeScripCount.intValue());
/*  952 */         currentMap.put(dateTime, mtm);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeMap<Long, Double> combineMTMmaps(HashMap<String, TreeMap<Long, Double>> mtmMaps)
/*      */   {
/*  960 */     Integer count = Integer.valueOf(mtmMaps.size());
/*  961 */     TreeMap<Long, Double> consolMTM = new TreeMap();
/*  962 */     for (Map.Entry<String, TreeMap<Long, Double>> entry : mtmMaps.entrySet()) {
/*  963 */       TreeMap<Long, Double> mtmMap = (TreeMap)entry.getValue();
/*  964 */       if (mtmMap.size() != 0)
/*      */       {
/*  966 */         for (Map.Entry<Long, Double> mtmEntry : mtmMap.entrySet()) {
/*  967 */           Long dateTime = (Long)mtmEntry.getKey();
/*  968 */           Double mtm = (Double)mtmEntry.getValue();
/*  969 */           Double curMTM = Double.valueOf(0.0D);
/*      */           try {
/*  971 */             curMTM = (Double)consolMTM.get(dateTime);
/*  972 */             if (curMTM == null)
/*  973 */               curMTM = Double.valueOf(0.0D);
/*  974 */             consolMTM.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */           } catch (Exception e) {
/*  976 */             curMTM = Double.valueOf(0.0D);
/*  977 */             consolMTM.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */           }
/*      */         } }
/*      */     }
/*  981 */     for (Map.Entry<Long, Double> mtmEntry : new TreeMap(consolMTM).entrySet()) {
/*  982 */       Long dateTime = (Long)mtmEntry.getKey();
/*  983 */       Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / count.doubleValue());
/*  984 */       consolMTM.put(dateTime, mtm);
/*      */     }
/*  986 */     return consolMTM;
/*      */   }
/*      */   
/*      */   public void updateNumActiveScrips(TreeMap<Long, Integer> activeScripMap, String scripName, String auxFilesPath) {
/*  990 */     CSVReader reader = null;
/*      */     try {
/*  992 */       reader = new CSVReader(auxFilesPath + "\\" + scripName + ".csv", ',', 0);
/*  993 */       String[] line = reader.getLine();
/*  994 */       Long firstDate = Long.valueOf(Long.parseLong(line[0]));
/*  995 */       int isThisScripActive = Integer.parseInt(line[2]);
/*  996 */       activeScripMap.put(firstDate, Integer.valueOf(0));
/*  997 */       while ((line = reader.getLine()) != null) {
/*  998 */         Long date = Long.valueOf(Long.parseLong(line[0]));
/*  999 */         if (activeScripMap.containsKey(date)) {
/* 1000 */           int currentNumActiveScrips = ((Integer)activeScripMap.get(date)).intValue();
/* 1001 */           activeScripMap.put(date, Integer.valueOf(currentNumActiveScrips + isThisScripActive));
/*      */         } else {
/* 1003 */           activeScripMap.put(date, Integer.valueOf(isThisScripActive));
/*      */         }
/* 1005 */         isThisScripActive = Integer.parseInt(line[2]);
/*      */       }
/*      */     }
/*      */     catch (IOException e) {
/* 1009 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   String getMTMFilePath(String strategyID, String scripListID, String assetClassID, String scripID)
/*      */   {
/* 1017 */     return (String)((TreeMap)((TreeMap)((TreeMap)this.resultsFileMap.get(strategyID)).get(scripListID)).get(assetClassID)).get(scripID);
/*      */   }
/*      */   
/*      */   public void generateResultsFileMap()
/*      */     throws Exception
/*      */   {
/* 1023 */     this.resultsFileMap = new TreeMap();
/*      */     
/* 1025 */     String mtmFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/MTM Data";
/*      */     
/* 1027 */     File mtmFolderPathFile = new File(mtmFolderPath);
/* 1028 */     File[] mtmFolders = mtmFolderPathFile.listFiles();
/*      */     
/*      */     File[] arrayOfFile1;
/* 1031 */     int j = (arrayOfFile1 = mtmFolders).length; for (int i = 0; i < j; i++) { File mtmFolder = arrayOfFile1[i];
/*      */       
/*      */ 
/* 1034 */       String[] mtmFolderVal = mtmFolder.getName().split(" ");
/*      */       
/* 1036 */       String strategyID = mtmFolderVal[0];
/*      */       
/*      */ 
/* 1039 */       TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListFileMap = (TreeMap)this.resultsFileMap.get(strategyID);
/* 1040 */       if (scripListFileMap == null) {
/* 1041 */         scripListFileMap = new TreeMap();
/*      */       }
/* 1043 */       String scripListID = mtmFolderVal[1];
/* 1044 */       if (mtmFolderVal.length > 2) {
/* 1045 */         scripListID = 
/* 1046 */           mtmFolderVal[1] + " " + mtmFolderVal[2] + " " + mtmFolderVal[3] + " " + mtmFolderVal[4] + " " + mtmFolderVal[5];
/*      */       }
/*      */       
/* 1049 */       String[] splits = mtmFolderVal[1].split("\\$");
/* 1050 */       String assetClassID = splits[1];
/* 1051 */       TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListFileMap.get(assetClassID);
/* 1052 */       if (assetClassMap == null) {
/* 1053 */         assetClassMap = new TreeMap();
/*      */       }
/*      */       
/* 1056 */       TreeMap<String, String> scripFileMap = (TreeMap)assetClassMap.get(scripListID);
/* 1057 */       if (scripFileMap == null) {
/* 1058 */         scripFileMap = new TreeMap();
/*      */       }
/*      */       
/* 1061 */       String mtmPath = mtmFolderPath + "/" + mtmFolder.getName();
/* 1062 */       File mtmPathFile = new File(mtmPath);
/* 1063 */       File[] mtmFiles = mtmPathFile.listFiles();
/*      */       
/*      */       File[] arrayOfFile2;
/* 1066 */       int m = (arrayOfFile2 = mtmFiles).length; for (int k = 0; k < m; k++) { File mtmFile = arrayOfFile2[k];
/* 1067 */         String mtmFileName = mtmFile.getName();
/* 1068 */         String scripID = mtmFileName.substring(0, mtmFileName.length() - 8);
/*      */         
/*      */ 
/* 1071 */         String mtmFilePath = mtmFile.getAbsolutePath();
/* 1072 */         scripFileMap.put(scripID, mtmFilePath);
/*      */       }
/*      */       
/*      */ 
/* 1076 */       assetClassMap.put(assetClassID, scripFileMap);
/* 1077 */       scripListFileMap.put(scripListID, assetClassMap);
/* 1078 */       this.resultsFileMap.put(strategyID, scripListFileMap);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/RollingAnalysisDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */