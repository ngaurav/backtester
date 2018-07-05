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
/*      */         
/*  237 */         String mtmFilePath = (String)((TreeMap)scripMap).get(scripID);
/*      */         try {
/*  239 */           CSVReader reader = new CSVReader(mtmFilePath, ',', 0);
/*      */           String[] dataLine;
/*  241 */           while ((dataLine = reader.getLine()) != null) { String[] dataLine;
/*  242 */             selectableDateSet.add(Long.valueOf(Long.parseLong(dataLine[0])));
/*      */           }
/*      */         }
/*      */         catch (Exception localException1) {}
/*      */         
/*  247 */         this.scripSet.add(scripID);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  252 */     this.outputKey = (this.outputKey + "|" + scripID);
/*      */     
/*  254 */     return selectableDateSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateSetsForKeys(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */   {
/*  261 */     getSelectableScripListSet(strategyKey);
/*  262 */     getSelectableAssetClassSet(scripListKey);
/*  263 */     getSelectableScripSet(assetClassKey);
/*  264 */     getSelectableDateSet(scripKey);
/*      */     
/*      */ 
/*  267 */     this.startDate = 0L;
/*  268 */     this.endDate = 20910101L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public PostProcess createPostProcessObject()
/*      */     throws Exception
/*      */   {
/*  276 */     TreeMap<Long, Double> mtmMap = generateMTMMap();
/*      */     
/*      */ 
/*  279 */     ArrayList<String[]> tradeBook = generateTradebook();
/*      */     
/*      */ 
/*  282 */     return new PostProcess(mtmMap, this.outputKey, tradeBook, this.btGlobal, this.postProcessMode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void generateResults(String strategyID, String scripListID, String scripID, boolean exportResultsCheck)
/*      */     throws Exception
/*      */   {
/*  290 */     String filePath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results";
/*  291 */     if (!new File(filePath).exists()) {
/*  292 */       new File(filePath).mkdirs();
/*      */     }
/*      */     
/*  295 */     generateResults(this.startDate, this.endDate);
/*      */     
/*      */     try
/*      */     {
/*  299 */       exportAllResults(exportResultsCheck, true);
/*      */     }
/*      */     catch (Exception e) {
/*  302 */       e.printStackTrace();
/*  303 */       return;
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  308 */       this.btGlobal.displayRunParameters(this.timeStamp);
/*      */     }
/*      */     catch (IOException e) {
/*  311 */       this.btGlobal.displayMessage("Error displaying last run parameters");
/*  312 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void generateResults(long startDate, long endDate)
/*      */     throws Exception
/*      */   {
/*  321 */     this.startDate = startDate;
/*  322 */     this.endDate = endDate;
/*      */     
/*      */ 
/*  325 */     PostProcess ppObj = createPostProcessObject();
/*      */     
/*      */     try
/*      */     {
/*  329 */       ppObj.runPostprocess();
/*      */     }
/*      */     catch (ParseException e) {
/*  332 */       this.btGlobal.displayMessage("Error running post process");
/*  333 */       e.printStackTrace();
/*      */     }
/*      */     
/*      */ 
/*  337 */     this.btGlobal.displayResults(ppObj);
/*      */   }
/*      */   
/*      */ 
/*      */   public void exportAllResults(boolean exportResultsCheck, boolean isGui)
/*      */     throws Exception
/*      */   {
/*  344 */     if (exportResultsCheck)
/*      */     {
/*  346 */       Iterator localIterator1 = this.resultsFileMap.entrySet().iterator();
/*      */       Iterator localIterator2;
/*  345 */       for (; localIterator1.hasNext(); 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  353 */           localIterator2.hasNext())
/*      */       {
/*  346 */         Map.Entry<String, TreeMap<String, TreeMap<String, TreeMap<String, String>>>> strategyEntry = (Map.Entry)localIterator1.next();
/*      */         
/*      */ 
/*  349 */         String strategyID = (String)strategyEntry.getKey();
/*  350 */         exportResultsForKey(strategyID, "All", "All", "All");
/*      */         
/*  352 */         TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)strategyEntry.getValue();
/*  353 */         localIterator2 = scripListMap.entrySet().iterator(); continue;Map.Entry<String, TreeMap<String, TreeMap<String, String>>> scripListEntry = (Map.Entry)localIterator2.next();
/*      */         
/*      */ 
/*  356 */         String scripListID = (String)scripListEntry.getKey();
/*  357 */         exportResultsForKey(strategyID, scripListID, "All", "All");
/*      */         
/*  359 */         TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListEntry.getValue();
/*  360 */         Iterator localIterator4; for (Iterator localIterator3 = assetClassMap.entrySet().iterator(); localIterator3.hasNext(); 
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  367 */             localIterator4.hasNext())
/*      */         {
/*  360 */           Map.Entry<String, TreeMap<String, String>> assetClassEntry = (Map.Entry)localIterator3.next();
/*      */           
/*      */ 
/*  363 */           String assetClassID = (String)assetClassEntry.getKey();
/*  364 */           exportResultsForKey(strategyID, scripListID, assetClassID, "All");
/*      */           
/*  366 */           TreeMap<String, String> scripMap = (TreeMap)assetClassEntry.getValue();
/*  367 */           localIterator4 = scripMap.keySet().iterator(); continue;String scripID = (String)localIterator4.next();
/*      */           
/*      */ 
/*  370 */           exportResultsForKey(strategyID, scripListID, assetClassID, scripID);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  376 */     exportResultsForKey("All", "All", "All", "All");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void generateAndExportResultsForKey(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */     throws Exception
/*      */   {
/*  385 */     updateSetsForKeys(strategyKey, scripListKey, assetClassKey, scripKey);
/*      */     
/*      */ 
/*  388 */     PostProcess ppObj = createPostProcessObject();
/*      */     
/*      */     try
/*      */     {
/*  392 */       ppObj.runPostprocess();
/*  393 */       ppObj.printResults();
/*      */     }
/*      */     catch (ParseException e) {
/*  396 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void exportResultsForKey(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */     throws Exception
/*      */   {
/*  406 */     updateSetsForKeys(strategyKey, scripListKey, assetClassKey, scripKey);
/*      */     
/*      */ 
/*  409 */     PostProcess ppObj = createPostProcessObject();
/*      */     
/*      */ 
/*  412 */     if ((scripListKey.equals("All")) && (assetClassKey.equals("All")) && (scripKey.equals("All")))
/*      */     {
/*      */       try
/*      */       {
/*  416 */         String resultsPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results";
/*  417 */         new File(resultsPath).mkdirs();
/*      */         
/*      */ 
/*  420 */         CSVWriter writer = null;
/*  421 */         writer = new CSVWriter(resultsPath + "/" + strategyKey + " MTM.csv", false, ",");
/*  422 */         for (Map.Entry<Long, Double> entry : ppObj.consolMTM.entrySet()) {
/*  423 */           String[] outLine = { ((Long)entry.getKey()).toString(), ((Double)entry.getValue()).toString() };
/*  424 */           writer.writeLine(outLine);
/*      */         }
/*  426 */         writer.close();
/*      */         
/*      */ 
/*  429 */         writer = new CSVWriter(resultsPath + "/" + strategyKey + " Tradebook.csv", false, ",");
/*  430 */         for (String[] trade : ppObj.tradeBook)
/*  431 */           writer.writeLine(trade);
/*  432 */         writer.close();
/*      */       }
/*      */       catch (IOException e) {
/*  435 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  441 */       ppObj.runPostprocess();
/*  442 */       ppObj.writeToFile(this.timeStamp);
/*      */     } catch (ParseException e) {
/*  444 */       e.printStackTrace();
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
/*  455 */     ArrayList<String[]> consolTradebook = new ArrayList();
/*      */     
/*      */ 
/*  458 */     for (String strategyID : this.strategySet)
/*      */     {
/*      */ 
/*  461 */       ArrayList<String[]> strategyTradebook = new ArrayList();
/*      */       
/*      */ 
/*  464 */       for (String scripListID : this.scripListSet)
/*      */       {
/*      */ 
/*  467 */         ArrayList<String[]> scripListTradebook = new ArrayList();
/*      */         
/*      */ 
/*  470 */         for (String scripID : this.scripSet)
/*      */         {
/*      */ 
/*  473 */           if (checkIfResultExpected(strategyID, scripListID, scripID))
/*      */           {
/*      */ 
/*  476 */             ArrayList<String[]> scripTradebook = getStrategyScripListScripTradebook(strategyID, scripListID, 
/*  477 */               scripID);
/*      */             
/*  479 */             if (!this.postProcessMode.equals(PostProcessMode.Portfolio)) {
/*  480 */               scripTradebook = cleanTradebookForRunningTrades(scripTradebook);
/*      */             }
/*      */             
/*      */ 
/*  484 */             scripListTradebook.addAll(scripTradebook);
/*      */           }
/*      */         }
/*      */         
/*  488 */         if (this.postProcessMode.equals(PostProcessMode.Spread)) {
/*  489 */           scripListTradebook = timeSortTradebook(scripListTradebook);
/*      */         }
/*      */         
/*      */ 
/*  493 */         strategyTradebook.addAll(scripListTradebook);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  498 */       consolTradebook.addAll(strategyTradebook);
/*      */     }
/*      */     
/*  501 */     return consolTradebook;
/*      */   }
/*      */   
/*      */   private ArrayList<String[]> timeSortTradebook(ArrayList<String[]> scripListTradebook) {
/*  505 */     TreeMap<String, ArrayList<String[]>> tradeBookMap = new TreeMap();
/*      */     
/*      */ 
/*  508 */     for (String[] line : scripListTradebook) {
/*  509 */       if (tradeBookMap.get(line[0]) == null) {
/*  510 */         ArrayList<String[]> trades = new ArrayList();
/*  511 */         trades.add(line);
/*  512 */         tradeBookMap.put(line[0], trades);
/*      */       } else {
/*  514 */         trades = (ArrayList)tradeBookMap.get(line[0]);
/*  515 */         trades.add(line);
/*  516 */         tradeBookMap.put(line[0], trades);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  521 */     ArrayList<String[]> sortedTradeBook = new ArrayList();
/*      */     
/*      */     Iterator localIterator2;
/*  524 */     for (ArrayList<String[]> trades = tradeBookMap.keySet().iterator(); trades.hasNext(); 
/*      */         
/*  526 */         localIterator2.hasNext())
/*      */     {
/*  524 */       String key = (String)trades.next();
/*  525 */       ArrayList<String[]> trades = (ArrayList)tradeBookMap.get(key);
/*  526 */       localIterator2 = trades.iterator(); continue;String[] line = (String[])localIterator2.next();
/*  527 */       sortedTradeBook.add(line);
/*      */     }
/*      */     
/*  530 */     return sortedTradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */   public ArrayList<String[]> getConsolTradebook()
/*      */   {
/*  536 */     String tbFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Trade Data";
/*      */     
/*  538 */     File tbFolderPathFile = new File(tbFolderPath);
/*  539 */     File[] tbFolders = tbFolderPathFile.listFiles();
/*      */     
/*  541 */     ArrayList<String[]> consolTradeBook = new ArrayList();
/*      */     
/*      */     try
/*      */     {
/*  545 */       CSVWriter writer = null;
/*  546 */       writer = new CSVWriter(
/*  547 */         this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results/Consol Tradebook.csv", false, 
/*  548 */         ",");
/*      */       
/*      */       File[] arrayOfFile1;
/*  551 */       int j = (arrayOfFile1 = tbFolders).length; for (int i = 0; i < j; i++) { File tbFolder = arrayOfFile1[i];
/*      */         
/*      */ 
/*  554 */         String[] tbFolderVal = tbFolder.getName().split(" ");
/*      */         
/*  556 */         String strategyID = tbFolderVal[0];
/*  557 */         String scripListID = tbFolderVal[1];
/*      */         
/*      */ 
/*  560 */         if (tbFolderVal.length > 2) {
/*  561 */           scripListID = 
/*  562 */             tbFolderVal[1] + " " + tbFolderVal[2] + " " + tbFolderVal[3] + " " + tbFolderVal[4] + " " + tbFolderVal[5];
/*      */         }
/*      */         
/*  565 */         String tbPath = tbFolderPath + "/" + tbFolder.getName();
/*  566 */         File tbPathFile = new File(tbPath);
/*  567 */         File[] tbFiles = tbPathFile.listFiles();
/*      */         
/*      */         File[] arrayOfFile2;
/*  570 */         int m = (arrayOfFile2 = tbFiles).length; for (int k = 0; k < m; k++) { File tbFile = arrayOfFile2[k];
/*      */           
/*  572 */           String tbFileName = tbFile.getName();
/*  573 */           String scripID = tbFileName.substring(0, tbFileName.length() - 14);
/*      */           
/*  575 */           ArrayList<String[]> tradeBook = getStrategyScripListScripTradebook(strategyID, scripListID, 
/*  576 */             scripID);
/*      */           
/*  578 */           for (String[] trade : tradeBook) {
/*  579 */             String[] tradeOut = { trade[9], trade[10], trade[0], trade[1], trade[2], trade[3], trade[4], 
/*  580 */               trade[5], trade[6], trade[7], trade[8] };
/*  581 */             writer.writeLine(tradeOut);
/*      */           }
/*      */           
/*  584 */           if (tradeBook.size() > 0) {
/*  585 */             consolTradeBook.addAll(tradeBook);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  590 */       writer.close();
/*      */     }
/*      */     catch (IOException e) {
/*  593 */       e.printStackTrace();
/*      */     }
/*  595 */     return consolTradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ArrayList<String[]> getStrategyScripListScripTradebook(String strategyID, String scripListID, String scripID)
/*      */   {
/*  602 */     ArrayList<String[]> tradeBook = new ArrayList();
/*  603 */     String tbPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Trade Data";
/*  604 */     String tbFilePath = tbPath + "/" + strategyID + " " + scripListID + "/" + scripID + " Tradebook.csv";
/*  605 */     CSVReader reader = null;
/*      */     try {
/*  607 */       reader = new CSVReader(tbFilePath, ',', 0);
/*      */       String[] dataLine;
/*  609 */       while ((dataLine = reader.getLine()) != null) {
/*      */         String[] dataLine;
/*  611 */         long date = Long.parseLong(dataLine[0]) / 1000000L;
/*      */         
/*      */ 
/*  614 */         if (date >= this.startDate)
/*      */         {
/*      */ 
/*      */ 
/*  618 */           if (date > this.endDate) {
/*      */             break;
/*      */           }
/*      */           
/*  622 */           tradeBook.add(dataLine);
/*      */         }
/*      */       }
/*  625 */     } catch (IOException e1) { this.btGlobal.displayMessage("Could not find Tradebook: " + strategyID + " " + scripListID + " " + scripID);
/*  626 */       e1.printStackTrace();
/*  627 */       return null;
/*      */     }
/*      */     
/*  630 */     return tradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreeMap<Long, Double> generateMTMMap()
/*      */     throws Exception
/*      */   {
/*  639 */     TreeMap<Long, Double> consolMTMMap = new TreeMap();
/*      */     
/*      */ 
/*  642 */     for (String strategyID : this.strategySet)
/*      */     {
/*      */ 
/*  645 */       TreeMap<Long, Double> strategyMTMMap = new TreeMap();
/*      */       
/*      */ 
/*  648 */       for (String scripListID : this.scripListSet)
/*      */       {
/*      */ 
/*  651 */         TreeMap<Long, Double> scripListMTMMap = new TreeMap();
/*      */         
/*      */ 
/*  654 */         for (String scripID : this.scripSet)
/*      */         {
/*      */ 
/*  657 */           if (checkIfResultExpected(strategyID, scripListID, scripID))
/*      */           {
/*      */ 
/*  660 */             TreeMap<Long, Double> scripMTMMap = getStrategyScripListScripMTM(strategyID, scripListID, scripID);
/*      */             
/*      */ 
/*  663 */             appendMTMmap(scripListMTMMap, scripMTMMap);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  668 */         appendMTMmap(strategyMTMMap, scripListMTMMap);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  673 */       TreeMap<Long, Integer> dateScripCountMap = new TreeMap();
/*      */       
/*  675 */       if (this.aggregationMode.equals(AggregationMode.Active)) {
/*  676 */         dateScripCountMap = createDateScripCountMap(strategyID);
/*      */       }
/*      */       
/*  679 */       adjustMTMmap(strategyMTMMap, Integer.valueOf(this.scripListSet.size()), this.aggregationMode, dateScripCountMap);
/*      */       
/*      */ 
/*  682 */       appendMTMmap(consolMTMMap, strategyMTMMap);
/*      */     }
/*      */     
/*      */ 
/*  686 */     adjustMTMmap(consolMTMMap, Integer.valueOf(this.strategySet.size()), AggregationMode.Fixed, new TreeMap());
/*      */     
/*  688 */     return consolMTMMap;
/*      */   }
/*      */   
/*      */   public TreeMap<Long, Double> getConsolMTM()
/*      */     throws Exception
/*      */   {
/*  694 */     String mtmFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/MTM Data";
/*      */     
/*  696 */     File mtmFolderPathFile = new File(mtmFolderPath);
/*  697 */     File[] mtmFolders = mtmFolderPathFile.listFiles();
/*      */     
/*      */ 
/*  700 */     TreeMap<Long, Double> consolMTMMap = new TreeMap();
/*      */     
/*      */ 
/*  703 */     TreeMap<Long, Integer> dateScripCountMap = new TreeMap();
/*      */     
/*      */     File[] arrayOfFile1;
/*  706 */     int j = (arrayOfFile1 = mtmFolders).length; for (int i = 0; i < j; i++) { File mtmFolder = arrayOfFile1[i];
/*      */       
/*      */ 
/*  709 */       String[] mtmFolderVal = mtmFolder.getName().split(" ");
/*      */       
/*  711 */       String strategyID = mtmFolderVal[0];
/*  712 */       String scripListID = mtmFolderVal[1];
/*      */       
/*      */ 
/*  715 */       if (mtmFolderVal.length > 2) {
/*  716 */         scripListID = 
/*  717 */           mtmFolderVal[1] + " " + mtmFolderVal[2] + " " + mtmFolderVal[3] + " " + mtmFolderVal[4] + " " + mtmFolderVal[5];
/*      */       }
/*      */       
/*  720 */       TreeMap<Long, Double> scripListMTMMap = new TreeMap();
/*      */       
/*      */ 
/*  723 */       String mtmPath = mtmFolderPath + "/" + mtmFolder.getName();
/*  724 */       File mtmPathFile = new File(mtmPath);
/*  725 */       File[] mtmFiles = mtmPathFile.listFiles();
/*      */       
/*      */       File[] arrayOfFile2;
/*  728 */       int m = (arrayOfFile2 = mtmFiles).length; for (int k = 0; k < m; k++) { File mtmFile = arrayOfFile2[k];
/*      */         
/*  730 */         String mtmFileName = mtmFile.getName();
/*  731 */         String scripID = mtmFileName.substring(0, mtmFileName.length() - 8);
/*      */         
/*  733 */         TreeMap<Long, Double> scripMTMMap = getStrategyScripListScripMTM(strategyID, scripListID, scripID);
/*      */         
/*      */ 
/*  736 */         appendMTMmap(scripListMTMMap, scripMTMMap);
/*      */       }
/*      */       
/*      */ 
/*  740 */       appendMTMmap(consolMTMMap, scripListMTMMap);
/*      */       
/*  742 */       if (this.aggregationMode.equals(AggregationMode.Active)) {
/*  743 */         dateScripCountMap = createDateScripCountMap(strategyID);
/*      */       }
/*      */     }
/*      */     
/*  747 */     adjustMTMmap(consolMTMMap, Integer.valueOf(mtmFolders.length), this.aggregationMode, dateScripCountMap);
/*      */     
/*      */ 
/*  750 */     CSVWriter writer = null;
/*      */     try {
/*  752 */       writer = new CSVWriter(
/*  753 */         this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/Results/Consol MTM.csv", false, ",");
/*      */     }
/*      */     catch (IOException e) {
/*  756 */       e.printStackTrace();
/*      */     }
/*  758 */     for (Object entry : consolMTMMap.entrySet()) {
/*  759 */       String[] outLine = { ((Long)((Map.Entry)entry).getKey()).toString(), ((Double)((Map.Entry)entry).getValue()).toString() };
/*      */       try {
/*  761 */         writer.writeLine(outLine);
/*      */       }
/*      */       catch (IOException e) {
/*  764 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     try {
/*  768 */       writer.close();
/*      */     }
/*      */     catch (IOException e) {
/*  771 */       e.printStackTrace();
/*      */     }
/*  773 */     return consolMTMMap;
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeMap<Long, Double> getStrategyScripListScripMTM(String strategyID, String scripListID, String scripID)
/*      */   {
/*  779 */     TreeMap<Long, Double> mtmMap = new TreeMap();
/*      */     
/*  781 */     String mtmPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/MTM Data/" + strategyID + " " + 
/*  782 */       scripListID;
/*      */     
/*  784 */     String mtmFilePath = mtmPath + "/" + scripID + " MTM.csv";
/*      */     
/*  786 */     CSVReader reader = null;
/*      */     try {
/*  788 */       reader = new CSVReader(mtmFilePath, ',', 0);
/*      */       String[] inData;
/*  790 */       while ((inData = reader.getLine()) != null) {
/*      */         String[] inData;
/*  792 */         Long date = Long.valueOf(Long.parseLong(inData[0]));
/*      */         
/*      */ 
/*  795 */         if (date.longValue() >= this.startDate)
/*      */         {
/*      */ 
/*      */ 
/*  799 */           if (date.longValue() > this.endDate) {
/*      */             break;
/*      */           }
/*      */           
/*  803 */           Double mtm = Double.valueOf(Double.parseDouble(inData[1]));
/*  804 */           mtmMap.put(date, mtm);
/*      */         }
/*      */       }
/*  807 */     } catch (IOException e1) { this.btGlobal.displayMessage("Could not find MTM File: " + strategyID + " " + scripListID + " " + scripID);
/*  808 */       e1.printStackTrace();
/*  809 */       return null;
/*      */     }
/*      */     
/*  812 */     return mtmMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean checkIfResultExpected(String strategyID, String scripListID, String scripID)
/*      */   {
/*  822 */     TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)this.resultsFileMap.get(strategyID);
/*  823 */     if (scripListMap == null) {
/*  824 */       return false;
/*      */     }
/*      */     
/*  827 */     TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListMap.get(scripListID);
/*  828 */     if (assetClassMap == null) {
/*  829 */       return false;
/*      */     }
/*  831 */     for (Map.Entry<String, TreeMap<String, String>> assetClassEntry : assetClassMap.entrySet()) {
/*  832 */       TreeMap<String, String> scripMap = (TreeMap)assetClassEntry.getValue();
/*      */       
/*  834 */       if (scripMap != null)
/*      */       {
/*  836 */         if (scripMap.keySet().contains(scripID))
/*  837 */           return true;
/*      */       }
/*      */     }
/*  840 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ArrayList<String[]> cleanTradebookForRunningTrades(ArrayList<String[]> tradeBook)
/*      */   {
/*  847 */     Double curPosition = Double.valueOf(0.0D);
/*  848 */     Double prevPosition = Double.valueOf(0.0D);
/*  849 */     int lastExitIndex = 0;
/*  850 */     for (int i = 0; i < tradeBook.size(); i++) {
/*  851 */       String[] trade = (String[])tradeBook.get(i);
/*  852 */       String side = trade[3];
/*  853 */       String type = trade[4];
/*      */       
/*  855 */       if (!type.equals("ROLLOVER"))
/*      */       {
/*      */ 
/*  858 */         int signal = 0;
/*  859 */         if (side.equals("BUY")) {
/*  860 */           signal = 1;
/*      */         } else {
/*  862 */           signal = -1;
/*      */         }
/*  864 */         prevPosition = curPosition;
/*  865 */         curPosition = Double.valueOf(curPosition.doubleValue() + signal * Double.parseDouble(trade[7]));
/*      */         
/*  867 */         if (((MathLib.doubleCompare(curPosition, Double.valueOf(0.0D)).intValue() == 0 ? 1 : 0) & (MathLib.doubleCompare(prevPosition, Double.valueOf(0.0D)).intValue() != 0 ? 1 : 0)) != 0) {
/*  868 */           lastExitIndex = i;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  874 */     while (tradeBook.size() > lastExitIndex + 1) {
/*  875 */       tradeBook.remove(lastExitIndex + 1);
/*      */     }
/*      */     
/*  878 */     return tradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */   public void appendMTMmap(TreeMap<Long, Double> currentMap, TreeMap<Long, Double> newMap)
/*      */   {
/*  884 */     if (newMap.size() == 0) {
/*  885 */       return;
/*      */     }
/*  887 */     for (Map.Entry<Long, Double> mtmEntry : newMap.entrySet()) {
/*  888 */       Long dateTime = (Long)mtmEntry.getKey();
/*  889 */       Double mtm = (Double)mtmEntry.getValue();
/*  890 */       Double curMTM = Double.valueOf(0.0D);
/*      */       try {
/*  892 */         curMTM = (Double)currentMap.get(dateTime);
/*  893 */         if (curMTM == null)
/*  894 */           curMTM = Double.valueOf(0.0D);
/*  895 */         currentMap.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */       } catch (Exception e) {
/*  897 */         curMTM = Double.valueOf(0.0D);
/*  898 */         currentMap.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void adjustMTMmap(TreeMap<Long, Double> currentMap, Integer fixedCount, AggregationMode aggregationMode, TreeMap<Long, Integer> dateScripCountMap)
/*      */     throws Exception
/*      */   {
/*  908 */     if (aggregationMode.equals(AggregationMode.Fixed)) {
/*  909 */       for (Map.Entry<Long, Double> mtmEntry : new TreeMap(currentMap).entrySet()) {
/*  910 */         Long dateTime = (Long)mtmEntry.getKey();
/*  911 */         Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / fixedCount.doubleValue());
/*  912 */         currentMap.put(dateTime, mtm);
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*  917 */     else if (aggregationMode.equals(AggregationMode.Active))
/*      */     {
/*  919 */       ??? = new TreeMap(currentMap).entrySet().iterator();
/*  918 */       while (???.hasNext()) {
/*  919 */         Map.Entry<Long, Double> mtmEntry = (Map.Entry)???.next();
/*  920 */         Long dateTime = (Long)mtmEntry.getKey();
/*  921 */         Double activeCount = getActiveCount(dateTime, dateScripCountMap);
/*  922 */         Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / activeCount.doubleValue());
/*  923 */         currentMap.put(dateTime, mtm);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Double getActiveCount(Long dateTime, TreeMap<Long, Integer> dateScripCountMap)
/*      */   {
/*  932 */     double activeCount = 0.0D;
/*  933 */     for (Map.Entry<Long, Integer> entry : dateScripCountMap.entrySet()) {
/*  934 */       Long curDate = (Long)entry.getKey();
/*  935 */       if (curDate.compareTo(dateTime) > 0) break;
/*  936 */       activeCount += ((Integer)entry.getValue()).intValue();
/*      */     }
/*      */     
/*      */ 
/*  940 */     return Double.valueOf(activeCount);
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeMap<Long, Double> combineMTMmaps(HashMap<String, TreeMap<Long, Double>> mtmMaps)
/*      */   {
/*  946 */     Integer count = Integer.valueOf(mtmMaps.size());
/*  947 */     TreeMap<Long, Double> consolMTM = new TreeMap();
/*  948 */     for (Map.Entry<String, TreeMap<Long, Double>> entry : mtmMaps.entrySet()) {
/*  949 */       TreeMap<Long, Double> mtmMap = (TreeMap)entry.getValue();
/*  950 */       if (mtmMap.size() != 0)
/*      */       {
/*  952 */         for (Map.Entry<Long, Double> mtmEntry : mtmMap.entrySet()) {
/*  953 */           Long dateTime = (Long)mtmEntry.getKey();
/*  954 */           Double mtm = (Double)mtmEntry.getValue();
/*  955 */           Double curMTM = Double.valueOf(0.0D);
/*      */           try {
/*  957 */             curMTM = (Double)consolMTM.get(dateTime);
/*  958 */             if (curMTM == null)
/*  959 */               curMTM = Double.valueOf(0.0D);
/*  960 */             consolMTM.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */           } catch (Exception e) {
/*  962 */             curMTM = Double.valueOf(0.0D);
/*  963 */             consolMTM.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */           }
/*      */         } }
/*      */     }
/*  967 */     for (Map.Entry<Long, Double> mtmEntry : new TreeMap(consolMTM).entrySet()) {
/*  968 */       Long dateTime = (Long)mtmEntry.getKey();
/*  969 */       Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / count.doubleValue());
/*  970 */       consolMTM.put(dateTime, mtm);
/*      */     }
/*  972 */     return consolMTM;
/*      */   }
/*      */   
/*      */   public TreeMap<Long, Integer> createDateScripCountMap(String strategyID) {
/*  976 */     TreeMap<Long, Integer> dateScripCountMap = new TreeMap();
/*  977 */     CSVReader reader = null;
/*      */     try {
/*  979 */       reader = new CSVReader(this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + 
/*  980 */         "/Parameters/" + strategyID + " ScripListDateMap.csv", ',', 0);
/*  981 */       String[] line = reader.getLine();
/*  982 */       while ((line = reader.getLine()) != null) {
/*  983 */         Long date = Long.valueOf(Long.parseLong(line[1]));
/*  984 */         if (dateScripCountMap.containsKey(date)) {
/*  985 */           Integer currentCount = (Integer)dateScripCountMap.get(date);
/*  986 */           dateScripCountMap.put(date, Integer.valueOf(currentCount.intValue() + 1));
/*      */         } else {
/*  988 */           dateScripCountMap.put(date, Integer.valueOf(1));
/*      */         }
/*      */       }
/*      */     } catch (IOException e) {
/*  992 */       e.printStackTrace();
/*      */     }
/*      */     
/*  995 */     return dateScripCountMap;
/*      */   }
/*      */   
/*      */ 
/*      */   String getMTMFilePath(String strategyID, String scripListID, String assetClassID, String scripID)
/*      */   {
/* 1001 */     return (String)((TreeMap)((TreeMap)((TreeMap)this.resultsFileMap.get(strategyID)).get(scripListID)).get(assetClassID)).get(scripID);
/*      */   }
/*      */   
/*      */   public void generateResultsFileMap()
/*      */     throws Exception
/*      */   {
/* 1007 */     this.resultsFileMap = new TreeMap();
/*      */     
/* 1009 */     String mtmFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.timeStamp + "/MTM Data";
/*      */     
/* 1011 */     File mtmFolderPathFile = new File(mtmFolderPath);
/* 1012 */     File[] mtmFolders = mtmFolderPathFile.listFiles();
/*      */     
/*      */     File[] arrayOfFile1;
/* 1015 */     int j = (arrayOfFile1 = mtmFolders).length; for (int i = 0; i < j; i++) { File mtmFolder = arrayOfFile1[i];
/*      */       
/*      */ 
/* 1018 */       String[] mtmFolderVal = mtmFolder.getName().split(" ");
/*      */       
/* 1020 */       String strategyID = mtmFolderVal[0];
/*      */       
/*      */ 
/* 1023 */       TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListFileMap = (TreeMap)this.resultsFileMap.get(strategyID);
/* 1024 */       if (scripListFileMap == null) {
/* 1025 */         scripListFileMap = new TreeMap();
/*      */       }
/* 1027 */       String scripListID = mtmFolderVal[1];
/* 1028 */       if (mtmFolderVal.length > 2) {
/* 1029 */         scripListID = 
/* 1030 */           mtmFolderVal[1] + " " + mtmFolderVal[2] + " " + mtmFolderVal[3] + " " + mtmFolderVal[4] + " " + mtmFolderVal[5];
/*      */       }
/*      */       
/* 1033 */       String[] splits = mtmFolderVal[1].split("\\$");
/* 1034 */       String assetClassID = splits[1];
/* 1035 */       TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListFileMap.get(scripListID);
/* 1036 */       if (assetClassMap == null) {
/* 1037 */         assetClassMap = new TreeMap();
/*      */       }
/*      */       
/* 1040 */       TreeMap<String, String> scripFileMap = (TreeMap)assetClassMap.get(assetClassID);
/* 1041 */       if (scripFileMap == null) {
/* 1042 */         scripFileMap = new TreeMap();
/*      */       }
/*      */       
/* 1045 */       String mtmPath = mtmFolderPath + "/" + mtmFolder.getName();
/* 1046 */       File mtmPathFile = new File(mtmPath);
/* 1047 */       File[] mtmFiles = mtmPathFile.listFiles();
/*      */       
/*      */       File[] arrayOfFile2;
/* 1050 */       int m = (arrayOfFile2 = mtmFiles).length; for (int k = 0; k < m; k++) { File mtmFile = arrayOfFile2[k];
/* 1051 */         String mtmFileName = mtmFile.getName();
/* 1052 */         String scripID = mtmFileName.substring(0, mtmFileName.length() - 8);
/*      */         
/*      */ 
/* 1055 */         String mtmFilePath = mtmFile.getAbsolutePath();
/* 1056 */         scripFileMap.put(scripID, mtmFilePath);
/*      */       }
/*      */       
/*      */ 
/* 1060 */       assetClassMap.put(assetClassID, scripFileMap);
/* 1061 */       scripListFileMap.put(scripListID, assetClassMap);
/* 1062 */       this.resultsFileMap.put(strategyID, scripListFileMap);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/ResultDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */