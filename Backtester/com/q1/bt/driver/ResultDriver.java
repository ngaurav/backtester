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
/*  187 */     return selectableScripSet;
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeSet<Long> getSelectableDateSet(String scripID)
/*      */   {
/*  193 */     TreeSet<Long> selectableDateSet = new TreeSet();
/*  194 */     this.scripSet = new TreeSet();
/*      */     Iterator localIterator1;
/*      */     Iterator localIterator2;
/*  197 */     if (scripID.equals("All")) {
/*  198 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  199 */           localIterator2.hasNext())
/*      */       {
/*  198 */         String curStrategyID = (String)localIterator1.next();
/*  199 */         localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
/*  200 */         for (String curAssetClassID : this.assetClassSet) {
/*  201 */           TreeMap<String, String> scripMap = 
/*  202 */             (TreeMap)((TreeMap)((TreeMap)this.resultsFileMap.get(curStrategyID)).get(curScripListID)).get(curAssetClassID);
/*  203 */           if (scripMap != null)
/*      */           {
/*  205 */             for (Map.Entry<String, String> scripEntry : scripMap.entrySet())
/*      */             {
/*      */ 
/*  208 */               String mtmFilePath = (String)scripEntry.getValue();
/*  209 */               CSVReader reader = null;
/*      */               try {
/*  211 */                 reader = new CSVReader(mtmFilePath, ',', 0);
/*      */                 String[] dataLine;
/*  213 */                 while ((dataLine = reader.getLine()) != null) { String[] dataLine;
/*  214 */                   selectableDateSet.add(Long.valueOf(Long.parseLong(dataLine[0])));
/*      */                 }
/*      */               }
/*      */               catch (Exception localException) {}
/*      */               
/*      */ 
/*  220 */               String curScripID = (String)scripEntry.getKey();
/*  221 */               this.scripSet.add(curScripID);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     } else {
/*  227 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  228 */           localIterator2.hasNext())
/*      */       {
/*  227 */         String curStrategyID = (String)localIterator1.next();
/*  228 */         localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
/*      */         
/*  230 */         String curAssetClassID = scripID.split(" ")[1];
/*      */         
/*      */ 
/*  233 */         Object scripMap = 
/*  234 */           (TreeMap)((TreeMap)((TreeMap)this.resultsFileMap.get(curStrategyID)).get(curScripListID)).get(curAssetClassID);
/*  235 */         if (scripMap != null)
/*      */         {
/*      */ 
/*  238 */           String mtmFilePath = (String)((TreeMap)scripMap).get(scripID);
/*      */           try {
/*  240 */             CSVReader reader = new CSVReader(mtmFilePath, ',', 0);
/*      */             String[] dataLine;
/*  242 */             while ((dataLine = reader.getLine()) != null) { String[] dataLine;
/*  243 */               selectableDateSet.add(Long.valueOf(Long.parseLong(dataLine[0])));
/*      */             }
/*      */           }
/*      */           catch (Exception localException1) {}
/*      */           
/*  248 */           this.scripSet.add(scripID);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  253 */     this.outputKey = (this.outputKey + "|" + scripID);
/*      */     
/*  255 */     return selectableDateSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateSetsForKeys(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */   {
/*  262 */     getSelectableScripListSet(strategyKey);
/*  263 */     getSelectableAssetClassSet(scripListKey);
/*  264 */     getSelectableScripSet(assetClassKey);
/*  265 */     getSelectableDateSet(scripKey);
/*      */     
/*      */ 
/*  268 */     this.startDate = 0L;
/*  269 */     this.endDate = 20910101L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public PostProcess createPostProcessObject()
/*      */     throws Exception
/*      */   {
/*  277 */     TreeMap<Long, Double> mtmMap = generateMTMMap();
/*      */     
/*      */ 
/*  280 */     ArrayList<String[]> tradeBook = generateTradebook();
/*      */     
/*      */ 
/*  283 */     return new PostProcess(mtmMap, this.outputKey, tradeBook, this.btGlobal, this.postProcessMode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void generateResults(String strategyID, String scripListID, String scripID, boolean exportResultsCheck)
/*      */     throws Exception
/*      */   {
/*  291 */     String filePath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results";
/*  292 */     if (!new File(filePath).exists()) {
/*  293 */       new File(filePath).mkdirs();
/*      */     }
/*      */     
/*  296 */     generateResults(this.startDate, this.endDate);
/*      */     
/*      */     try
/*      */     {
/*  300 */       exportAllResults(exportResultsCheck, true);
/*      */     }
/*      */     catch (Exception e) {
/*  303 */       e.printStackTrace();
/*  304 */       return;
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  309 */       this.btGlobal.displayRunParameters(this.timeStamp);
/*      */     }
/*      */     catch (IOException e) {
/*  312 */       this.btGlobal.displayMessage("Error displaying last run parameters");
/*  313 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void generateResults(long startDate, long endDate)
/*      */     throws Exception
/*      */   {
/*  322 */     this.startDate = startDate;
/*  323 */     this.endDate = endDate;
/*      */     
/*      */ 
/*  326 */     PostProcess ppObj = createPostProcessObject();
/*      */     
/*      */     try
/*      */     {
/*  330 */       ppObj.runPostprocess();
/*      */     }
/*      */     catch (ParseException e) {
/*  333 */       this.btGlobal.displayMessage("Error running post process");
/*  334 */       e.printStackTrace();
/*      */     }
/*      */     
/*      */ 
/*  338 */     this.btGlobal.displayResults(ppObj);
/*      */   }
/*      */   
/*      */ 
/*      */   public void exportAllResults(boolean exportResultsCheck, boolean isGui)
/*      */     throws Exception
/*      */   {
/*  345 */     if (exportResultsCheck)
/*      */     {
/*  347 */       Iterator localIterator1 = this.resultsFileMap.entrySet().iterator();
/*      */       Iterator localIterator2;
/*  346 */       for (; localIterator1.hasNext(); 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  354 */           localIterator2.hasNext())
/*      */       {
/*  347 */         Map.Entry<String, TreeMap<String, TreeMap<String, TreeMap<String, String>>>> strategyEntry = (Map.Entry)localIterator1.next();
/*      */         
/*      */ 
/*  350 */         String strategyID = (String)strategyEntry.getKey();
/*  351 */         exportResultsForKey(strategyID, "All", "All", "All");
/*      */         
/*  353 */         TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)strategyEntry.getValue();
/*  354 */         localIterator2 = scripListMap.entrySet().iterator(); continue;Map.Entry<String, TreeMap<String, TreeMap<String, String>>> scripListEntry = (Map.Entry)localIterator2.next();
/*      */         
/*      */ 
/*  357 */         String scripListID = (String)scripListEntry.getKey();
/*  358 */         exportResultsForKey(strategyID, scripListID, "All", "All");
/*      */         
/*  360 */         TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListEntry.getValue();
/*  361 */         Iterator localIterator4; for (Iterator localIterator3 = assetClassMap.entrySet().iterator(); localIterator3.hasNext(); 
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  368 */             localIterator4.hasNext())
/*      */         {
/*  361 */           Map.Entry<String, TreeMap<String, String>> assetClassEntry = (Map.Entry)localIterator3.next();
/*      */           
/*      */ 
/*  364 */           String assetClassID = (String)assetClassEntry.getKey();
/*  365 */           exportResultsForKey(strategyID, scripListID, assetClassID, "All");
/*      */           
/*  367 */           TreeMap<String, String> scripMap = (TreeMap)assetClassEntry.getValue();
/*  368 */           localIterator4 = scripMap.keySet().iterator(); continue;String scripID = (String)localIterator4.next();
/*      */           
/*      */ 
/*  371 */           exportResultsForKey(strategyID, scripListID, assetClassID, scripID);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  377 */     exportResultsForKey("All", "All", "All", "All");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void generateAndExportResultsForKey(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */     throws Exception
/*      */   {
/*  386 */     updateSetsForKeys(strategyKey, scripListKey, assetClassKey, scripKey);
/*      */     
/*      */ 
/*  389 */     PostProcess ppObj = createPostProcessObject();
/*      */     
/*      */     try
/*      */     {
/*  393 */       ppObj.runPostprocess();
/*  394 */       ppObj.printResults();
/*      */     }
/*      */     catch (ParseException e) {
/*  397 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void exportResultsForKey(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */     throws Exception
/*      */   {
/*  407 */     updateSetsForKeys(strategyKey, scripListKey, assetClassKey, scripKey);
/*      */     
/*      */ 
/*  410 */     PostProcess ppObj = createPostProcessObject();
/*      */     
/*      */ 
/*  413 */     if ((scripListKey.equals("All")) && (assetClassKey.equals("All")) && (scripKey.equals("All")))
/*      */     {
/*      */       try
/*      */       {
/*  417 */         String resultsPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results";
/*  418 */         new File(resultsPath).mkdirs();
/*      */         
/*      */ 
/*  421 */         CSVWriter writer = null;
/*  422 */         writer = new CSVWriter(resultsPath + "/" + strategyKey + " MTM.csv", false, ",");
/*  423 */         for (Map.Entry<Long, Double> entry : ppObj.consolMTM.entrySet()) {
/*  424 */           String[] outLine = { ((Long)entry.getKey()).toString(), ((Double)entry.getValue()).toString() };
/*  425 */           writer.writeLine(outLine);
/*      */         }
/*  427 */         writer.close();
/*      */         
/*      */ 
/*  430 */         writer = new CSVWriter(resultsPath + "/" + strategyKey + " Tradebook.csv", false, ",");
/*  431 */         for (String[] trade : ppObj.tradeBook)
/*  432 */           writer.writeLine(trade);
/*  433 */         writer.close();
/*      */       }
/*      */       catch (IOException e) {
/*  436 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  442 */       ppObj.runPostprocess();
/*  443 */       ppObj.writeToFile(this.timeStamp);
/*      */     } catch (ParseException e) {
/*  445 */       e.printStackTrace();
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
/*  456 */     ArrayList<String[]> consolTradebook = new ArrayList();
/*      */     
/*      */ 
/*  459 */     for (String strategyID : this.strategySet)
/*      */     {
/*      */ 
/*  462 */       ArrayList<String[]> strategyTradebook = new ArrayList();
/*      */       
/*      */ 
/*  465 */       for (String scripListID : this.scripListSet)
/*      */       {
/*      */ 
/*  468 */         ArrayList<String[]> scripListTradebook = new ArrayList();
/*      */         
/*      */ 
/*  471 */         for (String scripID : this.scripSet)
/*      */         {
/*      */ 
/*  474 */           if (checkIfResultExpected(strategyID, scripListID, scripID))
/*      */           {
/*      */ 
/*  477 */             ArrayList<String[]> scripTradebook = getStrategyScripListScripTradebook(strategyID, scripListID, 
/*  478 */               scripID);
/*      */             
/*  480 */             if (!this.postProcessMode.equals(PostProcessMode.Portfolio)) {
/*  481 */               scripTradebook = cleanTradebookForRunningTrades(scripTradebook);
/*      */             }
/*      */             
/*      */ 
/*  485 */             scripListTradebook.addAll(scripTradebook);
/*      */           }
/*      */         }
/*      */         
/*  489 */         if (this.postProcessMode.equals(PostProcessMode.Spread)) {
/*  490 */           scripListTradebook = timeSortTradebook(scripListTradebook);
/*      */         }
/*      */         
/*      */ 
/*  494 */         strategyTradebook.addAll(scripListTradebook);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  499 */       consolTradebook.addAll(strategyTradebook);
/*      */     }
/*      */     
/*  502 */     return consolTradebook;
/*      */   }
/*      */   
/*      */   private ArrayList<String[]> timeSortTradebook(ArrayList<String[]> scripListTradebook) {
/*  506 */     TreeMap<String, ArrayList<String[]>> tradeBookMap = new TreeMap();
/*      */     
/*      */ 
/*  509 */     for (String[] line : scripListTradebook) {
/*  510 */       if (tradeBookMap.get(line[0]) == null) {
/*  511 */         ArrayList<String[]> trades = new ArrayList();
/*  512 */         trades.add(line);
/*  513 */         tradeBookMap.put(line[0], trades);
/*      */       } else {
/*  515 */         trades = (ArrayList)tradeBookMap.get(line[0]);
/*  516 */         trades.add(line);
/*  517 */         tradeBookMap.put(line[0], trades);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  522 */     ArrayList<String[]> sortedTradeBook = new ArrayList();
/*      */     
/*      */     Iterator localIterator2;
/*  525 */     for (ArrayList<String[]> trades = tradeBookMap.keySet().iterator(); trades.hasNext(); 
/*      */         
/*  527 */         localIterator2.hasNext())
/*      */     {
/*  525 */       String key = (String)trades.next();
/*  526 */       ArrayList<String[]> trades = (ArrayList)tradeBookMap.get(key);
/*  527 */       localIterator2 = trades.iterator(); continue;String[] line = (String[])localIterator2.next();
/*  528 */       sortedTradeBook.add(line);
/*      */     }
/*      */     
/*  531 */     return sortedTradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */   public ArrayList<String[]> getConsolTradebook()
/*      */   {
/*  537 */     String tbFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Trade Data";
/*      */     
/*  539 */     File tbFolderPathFile = new File(tbFolderPath);
/*  540 */     File[] tbFolders = tbFolderPathFile.listFiles();
/*      */     
/*  542 */     ArrayList<String[]> consolTradeBook = new ArrayList();
/*      */     
/*      */     try
/*      */     {
/*  546 */       CSVWriter writer = null;
/*  547 */       writer = new CSVWriter(
/*  548 */         this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results/Consol Tradebook.csv", false, 
/*  549 */         ",");
/*      */       
/*      */       File[] arrayOfFile1;
/*  552 */       int j = (arrayOfFile1 = tbFolders).length; for (int i = 0; i < j; i++) { File tbFolder = arrayOfFile1[i];
/*      */         
/*      */ 
/*  555 */         String[] tbFolderVal = tbFolder.getName().split(" ");
/*      */         
/*  557 */         String strategyID = tbFolderVal[0];
/*  558 */         String scripListID = tbFolderVal[1];
/*      */         
/*      */ 
/*  561 */         if (tbFolderVal.length > 2) {
/*  562 */           scripListID = 
/*  563 */             tbFolderVal[1] + " " + tbFolderVal[2] + " " + tbFolderVal[3] + " " + tbFolderVal[4] + " " + tbFolderVal[5];
/*      */         }
/*      */         
/*  566 */         String tbPath = tbFolderPath + "/" + tbFolder.getName();
/*  567 */         File tbPathFile = new File(tbPath);
/*  568 */         File[] tbFiles = tbPathFile.listFiles();
/*      */         
/*      */         File[] arrayOfFile2;
/*  571 */         int m = (arrayOfFile2 = tbFiles).length; for (int k = 0; k < m; k++) { File tbFile = arrayOfFile2[k];
/*      */           
/*  573 */           String tbFileName = tbFile.getName();
/*  574 */           String scripID = tbFileName.substring(0, tbFileName.length() - 14);
/*      */           
/*  576 */           ArrayList<String[]> tradeBook = getStrategyScripListScripTradebook(strategyID, scripListID, 
/*  577 */             scripID);
/*      */           
/*  579 */           for (String[] trade : tradeBook) {
/*  580 */             String[] tradeOut = { trade[9], trade[10], trade[0], trade[1], trade[2], trade[3], trade[4], 
/*  581 */               trade[5], trade[6], trade[7], trade[8] };
/*  582 */             writer.writeLine(tradeOut);
/*      */           }
/*      */           
/*  585 */           if (tradeBook.size() > 0) {
/*  586 */             consolTradeBook.addAll(tradeBook);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  591 */       writer.close();
/*      */     }
/*      */     catch (IOException e) {
/*  594 */       e.printStackTrace();
/*      */     }
/*  596 */     return consolTradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ArrayList<String[]> getStrategyScripListScripTradebook(String strategyID, String scripListID, String scripID)
/*      */   {
/*  603 */     ArrayList<String[]> tradeBook = new ArrayList();
/*  604 */     String tbPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Trade Data";
/*  605 */     String tbFilePath = tbPath + "/" + strategyID + " " + scripListID + "/" + scripID + " Tradebook.csv";
/*  606 */     CSVReader reader = null;
/*      */     try {
/*  608 */       reader = new CSVReader(tbFilePath, ',', 0);
/*      */       String[] dataLine;
/*  610 */       while ((dataLine = reader.getLine()) != null) {
/*      */         String[] dataLine;
/*  612 */         long date = Long.parseLong(dataLine[0]) / 1000000L;
/*      */         
/*      */ 
/*  615 */         if (date >= this.startDate)
/*      */         {
/*      */ 
/*      */ 
/*  619 */           if (date > this.endDate) {
/*      */             break;
/*      */           }
/*      */           
/*  623 */           tradeBook.add(dataLine);
/*      */         }
/*      */       }
/*  626 */     } catch (IOException e1) { this.btGlobal.displayMessage("Could not find Tradebook: " + strategyID + " " + scripListID + " " + scripID);
/*  627 */       e1.printStackTrace();
/*  628 */       return null;
/*      */     }
/*      */     
/*  631 */     return tradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreeMap<Long, Double> generateMTMMap()
/*      */     throws Exception
/*      */   {
/*  640 */     TreeMap<Long, Double> consolMTMMap = new TreeMap();
/*      */     
/*      */ 
/*  643 */     for (String strategyID : this.strategySet)
/*      */     {
/*      */ 
/*  646 */       TreeMap<Long, Double> strategyMTMMap = new TreeMap();
/*      */       
/*      */ 
/*  649 */       for (String scripListID : this.scripListSet)
/*      */       {
/*      */ 
/*  652 */         TreeMap<Long, Double> scripListMTMMap = new TreeMap();
/*      */         
/*      */ 
/*  655 */         for (String scripID : this.scripSet)
/*      */         {
/*      */ 
/*  658 */           if (checkIfResultExpected(strategyID, scripListID, scripID))
/*      */           {
/*      */ 
/*  661 */             TreeMap<Long, Double> scripMTMMap = getStrategyScripListScripMTM(strategyID, scripListID, scripID);
/*      */             
/*      */ 
/*  664 */             appendMTMmap(scripListMTMMap, scripMTMMap);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  669 */         appendMTMmap(strategyMTMMap, scripListMTMMap);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  674 */       TreeMap<Long, Integer> dateScripCountMap = new TreeMap();
/*      */       
/*  676 */       if (this.aggregationMode.equals(AggregationMode.Active)) {
/*  677 */         dateScripCountMap = createDateScripCountMap(strategyID);
/*      */       }
/*      */       
/*  680 */       adjustMTMmap(strategyMTMMap, Integer.valueOf(this.scripListSet.size()), this.aggregationMode, dateScripCountMap);
/*      */       
/*      */ 
/*  683 */       appendMTMmap(consolMTMMap, strategyMTMMap);
/*      */     }
/*      */     
/*      */ 
/*  687 */     adjustMTMmap(consolMTMMap, Integer.valueOf(this.strategySet.size()), AggregationMode.Fixed, new TreeMap());
/*      */     
/*  689 */     return consolMTMMap;
/*      */   }
/*      */   
/*      */   public TreeMap<Long, Double> getConsolMTM()
/*      */     throws Exception
/*      */   {
/*  695 */     String mtmFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/MTM Data";
/*      */     
/*  697 */     File mtmFolderPathFile = new File(mtmFolderPath);
/*  698 */     File[] mtmFolders = mtmFolderPathFile.listFiles();
/*      */     
/*      */ 
/*  701 */     TreeMap<Long, Double> consolMTMMap = new TreeMap();
/*      */     
/*      */ 
/*  704 */     TreeMap<Long, Integer> dateScripCountMap = new TreeMap();
/*      */     
/*      */     File[] arrayOfFile1;
/*  707 */     int j = (arrayOfFile1 = mtmFolders).length; for (int i = 0; i < j; i++) { File mtmFolder = arrayOfFile1[i];
/*      */       
/*      */ 
/*  710 */       String[] mtmFolderVal = mtmFolder.getName().split(" ");
/*      */       
/*  712 */       String strategyID = mtmFolderVal[0];
/*  713 */       String scripListID = mtmFolderVal[1];
/*      */       
/*      */ 
/*  716 */       if (mtmFolderVal.length > 2) {
/*  717 */         scripListID = 
/*  718 */           mtmFolderVal[1] + " " + mtmFolderVal[2] + " " + mtmFolderVal[3] + " " + mtmFolderVal[4] + " " + mtmFolderVal[5];
/*      */       }
/*      */       
/*  721 */       TreeMap<Long, Double> scripListMTMMap = new TreeMap();
/*      */       
/*      */ 
/*  724 */       String mtmPath = mtmFolderPath + "/" + mtmFolder.getName();
/*  725 */       File mtmPathFile = new File(mtmPath);
/*  726 */       File[] mtmFiles = mtmPathFile.listFiles();
/*      */       
/*      */       File[] arrayOfFile2;
/*  729 */       int m = (arrayOfFile2 = mtmFiles).length; for (int k = 0; k < m; k++) { File mtmFile = arrayOfFile2[k];
/*      */         
/*  731 */         String mtmFileName = mtmFile.getName();
/*  732 */         String scripID = mtmFileName.substring(0, mtmFileName.length() - 8);
/*      */         
/*  734 */         TreeMap<Long, Double> scripMTMMap = getStrategyScripListScripMTM(strategyID, scripListID, scripID);
/*      */         
/*      */ 
/*  737 */         appendMTMmap(scripListMTMMap, scripMTMMap);
/*      */       }
/*      */       
/*      */ 
/*  741 */       appendMTMmap(consolMTMMap, scripListMTMMap);
/*      */       
/*  743 */       if (this.aggregationMode.equals(AggregationMode.Active)) {
/*  744 */         dateScripCountMap = createDateScripCountMap(strategyID);
/*      */       }
/*      */     }
/*      */     
/*  748 */     adjustMTMmap(consolMTMMap, Integer.valueOf(mtmFolders.length), this.aggregationMode, dateScripCountMap);
/*      */     
/*      */ 
/*  751 */     CSVWriter writer = null;
/*      */     try {
/*  753 */       writer = new CSVWriter(
/*  754 */         this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results/Consol MTM.csv", false, ",");
/*      */     }
/*      */     catch (IOException e) {
/*  757 */       e.printStackTrace();
/*      */     }
/*  759 */     for (Object entry : consolMTMMap.entrySet()) {
/*  760 */       String[] outLine = { ((Long)((Map.Entry)entry).getKey()).toString(), ((Double)((Map.Entry)entry).getValue()).toString() };
/*      */       try {
/*  762 */         writer.writeLine(outLine);
/*      */       }
/*      */       catch (IOException e) {
/*  765 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     try {
/*  769 */       writer.close();
/*      */     }
/*      */     catch (IOException e) {
/*  772 */       e.printStackTrace();
/*      */     }
/*  774 */     return consolMTMMap;
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeMap<Long, Double> getStrategyScripListScripMTM(String strategyID, String scripListID, String scripID)
/*      */   {
/*  780 */     TreeMap<Long, Double> mtmMap = new TreeMap();
/*      */     
/*  782 */     String mtmPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/MTM Data/" + strategyID + " " + 
/*  783 */       scripListID;
/*      */     
/*  785 */     String mtmFilePath = mtmPath + "/" + scripID + " MTM.csv";
/*      */     
/*  787 */     CSVReader reader = null;
/*      */     try {
/*  789 */       reader = new CSVReader(mtmFilePath, ',', 0);
/*      */       String[] inData;
/*  791 */       while ((inData = reader.getLine()) != null) {
/*      */         String[] inData;
/*  793 */         Long date = Long.valueOf(Long.parseLong(inData[0]));
/*      */         
/*      */ 
/*  796 */         if (date.longValue() >= this.startDate)
/*      */         {
/*      */ 
/*      */ 
/*  800 */           if (date.longValue() > this.endDate) {
/*      */             break;
/*      */           }
/*      */           
/*  804 */           Double mtm = Double.valueOf(Double.parseDouble(inData[1]));
/*  805 */           mtmMap.put(date, mtm);
/*      */         }
/*      */       }
/*  808 */     } catch (IOException e1) { this.btGlobal.displayMessage("Could not find MTM File: " + strategyID + " " + scripListID + " " + scripID);
/*  809 */       e1.printStackTrace();
/*  810 */       return null;
/*      */     }
/*      */     
/*  813 */     return mtmMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean checkIfResultExpected(String strategyID, String scripListID, String scripID)
/*      */   {
/*  823 */     TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)this.resultsFileMap.get(strategyID);
/*  824 */     if (scripListMap == null) {
/*  825 */       return false;
/*      */     }
/*      */     
/*  828 */     TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListMap.get(scripListID);
/*  829 */     if (assetClassMap == null) {
/*  830 */       return false;
/*      */     }
/*  832 */     for (Map.Entry<String, TreeMap<String, String>> assetClassEntry : assetClassMap.entrySet()) {
/*  833 */       TreeMap<String, String> scripMap = (TreeMap)assetClassEntry.getValue();
/*      */       
/*  835 */       if (scripMap != null)
/*      */       {
/*  837 */         if (scripMap.keySet().contains(scripID))
/*  838 */           return true;
/*      */       }
/*      */     }
/*  841 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ArrayList<String[]> cleanTradebookForRunningTrades(ArrayList<String[]> tradeBook)
/*      */   {
/*  848 */     Double curPosition = Double.valueOf(0.0D);
/*  849 */     Double prevPosition = Double.valueOf(0.0D);
/*  850 */     int lastExitIndex = 0;
/*  851 */     for (int i = 0; i < tradeBook.size(); i++) {
/*  852 */       String[] trade = (String[])tradeBook.get(i);
/*  853 */       String side = trade[3];
/*  854 */       String type = trade[4];
/*      */       
/*  856 */       if (!type.equals("ROLLOVER"))
/*      */       {
/*      */ 
/*  859 */         int signal = 0;
/*  860 */         if (side.equals("BUY")) {
/*  861 */           signal = 1;
/*      */         } else {
/*  863 */           signal = -1;
/*      */         }
/*  865 */         prevPosition = curPosition;
/*  866 */         curPosition = Double.valueOf(curPosition.doubleValue() + signal * Double.parseDouble(trade[7]));
/*      */         
/*  868 */         if (((MathLib.doubleCompare(curPosition, Double.valueOf(0.0D)).intValue() == 0 ? 1 : 0) & (MathLib.doubleCompare(prevPosition, Double.valueOf(0.0D)).intValue() != 0 ? 1 : 0)) != 0) {
/*  869 */           lastExitIndex = i;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  875 */     while (tradeBook.size() > lastExitIndex + 1) {
/*  876 */       tradeBook.remove(lastExitIndex + 1);
/*      */     }
/*      */     
/*  879 */     return tradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */   public void appendMTMmap(TreeMap<Long, Double> currentMap, TreeMap<Long, Double> newMap)
/*      */   {
/*  885 */     if (newMap.size() == 0) {
/*  886 */       return;
/*      */     }
/*  888 */     for (Map.Entry<Long, Double> mtmEntry : newMap.entrySet()) {
/*  889 */       Long dateTime = (Long)mtmEntry.getKey();
/*  890 */       Double mtm = (Double)mtmEntry.getValue();
/*  891 */       Double curMTM = Double.valueOf(0.0D);
/*      */       try {
/*  893 */         curMTM = (Double)currentMap.get(dateTime);
/*  894 */         if (curMTM == null)
/*  895 */           curMTM = Double.valueOf(0.0D);
/*  896 */         currentMap.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */       } catch (Exception e) {
/*  898 */         curMTM = Double.valueOf(0.0D);
/*  899 */         currentMap.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void adjustMTMmap(TreeMap<Long, Double> currentMap, Integer fixedCount, AggregationMode aggregationMode, TreeMap<Long, Integer> dateScripCountMap)
/*      */     throws Exception
/*      */   {
/*  909 */     if (aggregationMode.equals(AggregationMode.Fixed)) {
/*  910 */       for (Map.Entry<Long, Double> mtmEntry : new TreeMap(currentMap).entrySet()) {
/*  911 */         Long dateTime = (Long)mtmEntry.getKey();
/*  912 */         Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / fixedCount.doubleValue());
/*  913 */         currentMap.put(dateTime, mtm);
/*      */ 
/*      */       }
/*      */       
/*      */     }
/*  918 */     else if (aggregationMode.equals(AggregationMode.Active)) {
/*  919 */       for (Map.Entry<Long, Double> mtmEntry : new TreeMap(currentMap).entrySet()) {
/*  920 */         Long dateTime = (Long)mtmEntry.getKey();
/*  921 */         Double activeCount = getActiveCount(dateTime, dateScripCountMap);
/*  922 */         Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / activeCount.doubleValue());
/*  923 */         currentMap.put(dateTime, mtm);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public Double getActiveCount(Long dateTime, TreeMap<Long, Integer> dateScripCountMap)
/*      */   {
/*  931 */     double activeCount = 0.0D;
/*  932 */     for (Map.Entry<Long, Integer> entry : dateScripCountMap.entrySet()) {
/*  933 */       Long curDate = (Long)entry.getKey();
/*  934 */       if (curDate.compareTo(dateTime) > 0) break;
/*  935 */       activeCount += ((Integer)entry.getValue()).intValue();
/*      */     }
/*      */     
/*      */ 
/*  939 */     return Double.valueOf(activeCount);
/*      */   }
/*      */   
/*      */   public TreeMap<Long, Double> combineMTMmaps(HashMap<String, TreeMap<Long, Double>> mtmMaps)
/*      */   {
/*  944 */     Integer count = Integer.valueOf(mtmMaps.size());
/*  945 */     TreeMap<Long, Double> consolMTM = new TreeMap();
/*  946 */     for (Map.Entry<String, TreeMap<Long, Double>> entry : mtmMaps.entrySet()) {
/*  947 */       TreeMap<Long, Double> mtmMap = (TreeMap)entry.getValue();
/*  948 */       if (mtmMap.size() != 0)
/*      */       {
/*  950 */         for (Map.Entry<Long, Double> mtmEntry : mtmMap.entrySet()) {
/*  951 */           Long dateTime = (Long)mtmEntry.getKey();
/*  952 */           Double mtm = (Double)mtmEntry.getValue();
/*  953 */           Double curMTM = Double.valueOf(0.0D);
/*      */           try {
/*  955 */             curMTM = (Double)consolMTM.get(dateTime);
/*  956 */             if (curMTM == null)
/*  957 */               curMTM = Double.valueOf(0.0D);
/*  958 */             consolMTM.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */           } catch (Exception e) {
/*  960 */             curMTM = Double.valueOf(0.0D);
/*  961 */             consolMTM.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */           }
/*      */         } }
/*      */     }
/*  965 */     for (Map.Entry<Long, Double> mtmEntry : new TreeMap(consolMTM).entrySet()) {
/*  966 */       Long dateTime = (Long)mtmEntry.getKey();
/*  967 */       Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / count.doubleValue());
/*  968 */       consolMTM.put(dateTime, mtm);
/*      */     }
/*  970 */     return consolMTM;
/*      */   }
/*      */   
/*      */   public TreeMap<Long, Integer> createDateScripCountMap(String strategyID) {
/*  974 */     TreeMap<Long, Integer> dateScripCountMap = new TreeMap();
/*  975 */     CSVReader reader = null;
/*      */     try {
/*  977 */       reader = new CSVReader(this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Parameters/" + 
/*  978 */         strategyID + " ScripListDateMap.csv", ',', 0);
/*  979 */       String[] line = reader.getLine();
/*  980 */       while ((line = reader.getLine()) != null) {
/*  981 */         Long date = Long.valueOf(Long.parseLong(line[1]));
/*  982 */         if (dateScripCountMap.containsKey(date)) {
/*  983 */           Integer currentCount = (Integer)dateScripCountMap.get(date);
/*  984 */           dateScripCountMap.put(date, Integer.valueOf(currentCount.intValue() + 1));
/*      */         } else {
/*  986 */           dateScripCountMap.put(date, Integer.valueOf(1));
/*      */         }
/*      */       }
/*      */     } catch (IOException e) {
/*  990 */       e.printStackTrace();
/*      */     }
/*      */     
/*  993 */     return dateScripCountMap;
/*      */   }
/*      */   
/*      */ 
/*      */   String getMTMFilePath(String strategyID, String scripListID, String assetClassID, String scripID)
/*      */   {
/*  999 */     return (String)((TreeMap)((TreeMap)((TreeMap)this.resultsFileMap.get(strategyID)).get(scripListID)).get(assetClassID)).get(scripID);
/*      */   }
/*      */   
/*      */   public void generateResultsFileMap()
/*      */     throws Exception
/*      */   {
/* 1005 */     this.resultsFileMap = new TreeMap();
/*      */     
/* 1007 */     String mtmFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/MTM Data";
/*      */     
/* 1009 */     File mtmFolderPathFile = new File(mtmFolderPath);
/* 1010 */     File[] mtmFolders = mtmFolderPathFile.listFiles();
/*      */     
/*      */     File[] arrayOfFile1;
/* 1013 */     int j = (arrayOfFile1 = mtmFolders).length; for (int i = 0; i < j; i++) { File mtmFolder = arrayOfFile1[i];
/*      */       
/*      */ 
/* 1016 */       String[] mtmFolderVal = mtmFolder.getName().split(" ");
/*      */       
/* 1018 */       String strategyID = mtmFolderVal[0];
/*      */       
/*      */ 
/* 1021 */       TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListFileMap = (TreeMap)this.resultsFileMap.get(strategyID);
/* 1022 */       if (scripListFileMap == null) {
/* 1023 */         scripListFileMap = new TreeMap();
/*      */       }
/* 1025 */       String scripListID = mtmFolderVal[1];
/* 1026 */       if (mtmFolderVal.length > 2) {
/* 1027 */         scripListID = 
/* 1028 */           mtmFolderVal[1] + " " + mtmFolderVal[2] + " " + mtmFolderVal[3] + " " + mtmFolderVal[4] + " " + mtmFolderVal[5];
/*      */       }
/* 1030 */       TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListFileMap.get(scripListID);
/* 1031 */       if (assetClassMap == null) {
/* 1032 */         assetClassMap = new TreeMap();
/*      */       }
/*      */       
/* 1035 */       String mtmPath = mtmFolderPath + "/" + mtmFolder.getName();
/* 1036 */       File mtmPathFile = new File(mtmPath);
/* 1037 */       File[] mtmFiles = mtmPathFile.listFiles();
/*      */       
/*      */       File[] arrayOfFile2;
/* 1040 */       int m = (arrayOfFile2 = mtmFiles).length; for (int k = 0; k < m; k++) { File mtmFile = arrayOfFile2[k];
/* 1041 */         String mtmFileName = mtmFile.getName();
/* 1042 */         String scripID = mtmFileName.substring(0, mtmFileName.length() - 8);
/* 1043 */         String assetClassID = scripID.split(" ")[1];
/*      */         
/*      */ 
/* 1046 */         TreeMap<String, String> scripFileMap = (TreeMap)assetClassMap.get(assetClassID);
/* 1047 */         if (scripFileMap == null)
/* 1048 */           scripFileMap = new TreeMap();
/* 1049 */         String mtmFilePath = mtmFile.getAbsolutePath();
/* 1050 */         scripFileMap.put(scripID, mtmFilePath);
/* 1051 */         assetClassMap.put(assetClassID, scripFileMap);
/*      */       }
/*      */       
/*      */ 
/* 1055 */       scripListFileMap.put(scripListID, assetClassMap);
/* 1056 */       this.resultsFileMap.put(strategyID, scripListFileMap);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/ResultDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */