/*      */ package com.q1.bt.driver;
/*      */ 
/*      */ import com.q1.bt.driver.backtest.enums.AggregationMode;
/*      */ import com.q1.bt.global.BacktesterGlobal;
/*      */ import com.q1.bt.postprocess.PostProcess;
/*      */ import com.q1.bt.process.backtest.PostProcessMode;
/*      */ import com.q1.bt.process.parameter.LoginParameter;
/*      */ import com.q1.csv.CSVReader;
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
/*      */ public class OOSAnalysisDriver
/*      */ {
/*      */   BacktesterGlobal btGlobal;
/*      */   String[] timeStamps;
/*      */   PostProcessMode postProcessMode;
/*      */   AggregationMode aggregationMode;
/*   28 */   TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, String>>>>> resultsFileMaps = new TreeMap();
/*   29 */   TreeSet<String> strategySet = new TreeSet();
/*   30 */   TreeSet<String> scripListSet = new TreeSet();
/*   31 */   TreeSet<String> assetClassSet = new TreeSet();
/*   32 */   TreeSet<String> scripSet = new TreeSet();
/*   33 */   TreeSet<Long> dateSet = new TreeSet();
/*   34 */   long startDate = 0L;
/*   35 */   long endDate = 20910101L;
/*      */   String outputKey;
/*      */   
/*      */   public OOSAnalysisDriver(BacktesterGlobal btGlobal, PostProcessMode postProcessMode, AggregationMode aggregationMode)
/*      */     throws Exception
/*      */   {
/*   41 */     this.btGlobal = btGlobal;
/*      */     
/*   43 */     File numberedFolders = new File(btGlobal.loginParameter.getOutputPath());
/*   44 */     this.timeStamps = numberedFolders.list();
/*      */     
/*   46 */     this.postProcessMode = postProcessMode;
/*   47 */     this.aggregationMode = aggregationMode;
/*   48 */     generateResultsFileMap();
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeSet<String> getSelectableStrategySet()
/*      */   {
/*   54 */     TreeSet<String> selectableStrategySet = new TreeSet();
/*      */     
/*      */ 
/*   57 */     for (String strategyID : ((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).keySet()) {
/*   58 */       selectableStrategySet.add(strategyID);
/*      */     }
/*   60 */     return selectableStrategySet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public TreeSet<String> getSelectableScripListSet(String strategyID)
/*      */   {
/*   67 */     TreeSet<String> selectableScripListSet = new TreeSet();
/*   68 */     this.strategySet = new TreeSet();
/*      */     
/*      */     TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap;
/*   71 */     if (strategyID.equals("All"))
/*      */     {
/*   73 */       Iterator localIterator1 = ((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).entrySet().iterator();
/*   72 */       while (localIterator1.hasNext()) {
/*   73 */         Map.Entry<String, TreeMap<String, TreeMap<String, TreeMap<String, String>>>> strategyEntry = (Map.Entry)localIterator1.next();
/*      */         
/*      */ 
/*   76 */         scripListMap = (TreeMap)strategyEntry.getValue();
/*   77 */         for (String scripListID : scripListMap.keySet()) {
/*   78 */           selectableScripListSet.add(scripListID);
/*      */         }
/*      */         
/*   81 */         String curStrategyID = (String)strategyEntry.getKey();
/*   82 */         this.strategySet.add(curStrategyID);
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*   90 */       TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(strategyID);
/*   91 */       for (String scripListID : scripListMap.keySet()) {
/*   92 */         selectableScripListSet.add(scripListID);
/*      */       }
/*      */       
/*   95 */       this.strategySet.add(strategyID);
/*      */     }
/*      */     
/*      */ 
/*   99 */     this.outputKey = strategyID;
/*      */     
/*  101 */     return selectableScripListSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public TreeSet<String> getSelectableAssetClassSet(String scripListID)
/*      */   {
/*  108 */     TreeSet<String> selectableAssetClassSet = new TreeSet();
/*  109 */     this.scripListSet = new TreeSet();
/*      */     Iterator localIterator1;
/*      */     TreeMap<String, TreeMap<String, String>> assetClassMap;
/*  112 */     if (scripListID.equals("All")) { Iterator localIterator2;
/*  113 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  114 */           localIterator2.hasNext())
/*      */       {
/*  113 */         String curStrategyID = (String)localIterator1.next();
/*      */         
/*  115 */         localIterator2 = ((TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(curStrategyID)).entrySet().iterator(); continue;Map.Entry<String, TreeMap<String, TreeMap<String, String>>> scripListEntry = (Map.Entry)localIterator2.next();
/*      */         
/*      */ 
/*  118 */         assetClassMap = (TreeMap)scripListEntry.getValue();
/*  119 */         for (String assetClassID : assetClassMap.keySet()) {
/*  120 */           selectableAssetClassSet.add(assetClassID);
/*      */         }
/*      */         
/*  123 */         String curScripListID = (String)scripListEntry.getKey();
/*  124 */         this.scripListSet.add(curScripListID);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  129 */       for (String curStrategyID : this.strategySet)
/*      */       {
/*      */ 
/*  132 */         TreeMap<String, TreeMap<String, String>> assetClassMap = 
/*  133 */           (TreeMap)((TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(curStrategyID)).get(scripListID);
/*  134 */         for (String assetClassID : assetClassMap.keySet()) {
/*  135 */           selectableAssetClassSet.add(assetClassID);
/*      */         }
/*      */         
/*  138 */         this.scripListSet.add(scripListID);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  143 */     this.outputKey = (this.outputKey + "|" + scripListID);
/*      */     
/*  145 */     return selectableAssetClassSet;
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeSet<String> getSelectableScripSet(String assetClassID)
/*      */   {
/*  151 */     TreeSet<String> selectableScripSet = new TreeSet();
/*  152 */     this.assetClassSet = new TreeSet();
/*      */     Iterator localIterator1;
/*      */     Iterator localIterator2;
/*  155 */     TreeMap<String, String> scripMap; if (assetClassID.equals("All")) {
/*  156 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  157 */           localIterator2.hasNext())
/*      */       {
/*  156 */         String curStrategyID = (String)localIterator1.next();
/*  157 */         localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
/*      */         
/*  159 */         Iterator localIterator3 = ((TreeMap)((TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(curStrategyID)).get(curScripListID)).entrySet().iterator();
/*  158 */         while (localIterator3.hasNext()) {
/*  159 */           Map.Entry<String, TreeMap<String, String>> assetClassEntry = (Map.Entry)localIterator3.next();
/*      */           
/*      */ 
/*  162 */           scripMap = (TreeMap)assetClassEntry.getValue();
/*  163 */           for (String scripID : scripMap.keySet()) {
/*  164 */             selectableScripSet.add(scripID);
/*      */           }
/*      */           
/*  167 */           String curAssetClassID = (String)assetClassEntry.getKey();
/*  168 */           this.assetClassSet.add(curAssetClassID);
/*      */         }
/*      */         
/*      */       }
/*      */     } else {
/*  173 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  174 */           localIterator2.hasNext())
/*      */       {
/*  173 */         String curStrategyID = (String)localIterator1.next();
/*  174 */         localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
/*      */         
/*      */ 
/*  177 */         TreeMap<String, String> scripMap = 
/*  178 */           (TreeMap)((TreeMap)((TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(curStrategyID)).get(curScripListID)).get(assetClassID);
/*  179 */         if (scripMap != null)
/*      */         {
/*  181 */           for (String scripID : scripMap.keySet()) {
/*  182 */             selectableScripSet.add(scripID);
/*      */           }
/*      */           
/*  185 */           this.assetClassSet.add(assetClassID);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  192 */     return selectableScripSet;
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeSet<Long> getSelectableDateSet(String scripID)
/*      */   {
/*  198 */     TreeSet<Long> selectableDateSet = new TreeSet();
/*  199 */     this.scripSet = new TreeSet();
/*      */     Iterator localIterator1;
/*      */     Iterator localIterator2;
/*  202 */     if (scripID.equals("All")) {
/*  203 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  204 */           localIterator2.hasNext())
/*      */       {
/*  203 */         String curStrategyID = (String)localIterator1.next();
/*  204 */         localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
/*  205 */         for (String curAssetClassID : this.assetClassSet) {
/*  206 */           TreeMap<String, String> scripMap = 
/*  207 */             (TreeMap)((TreeMap)((TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(curStrategyID)).get(curScripListID)).get(curAssetClassID);
/*  208 */           if (scripMap != null)
/*      */           {
/*  210 */             for (Map.Entry<String, String> scripEntry : scripMap.entrySet())
/*      */             {
/*      */ 
/*  213 */               String mtmFilePath = (String)scripEntry.getValue();
/*  214 */               CSVReader reader = null;
/*      */               try {
/*  216 */                 reader = new CSVReader(mtmFilePath, ',', 0);
/*      */                 String[] dataLine;
/*  218 */                 while ((dataLine = reader.getLine()) != null) { String[] dataLine;
/*  219 */                   selectableDateSet.add(Long.valueOf(Long.parseLong(dataLine[0])));
/*      */                 }
/*      */               }
/*      */               catch (Exception localException) {}
/*      */               
/*      */ 
/*  225 */               String curScripID = (String)scripEntry.getKey();
/*  226 */               this.scripSet.add(curScripID);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     } else {
/*  232 */       for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
/*  233 */           localIterator2.hasNext())
/*      */       {
/*  232 */         String curStrategyID = (String)localIterator1.next();
/*  233 */         localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
/*      */         
/*  235 */         String curAssetClassID = scripID.split(" ")[1];
/*      */         
/*      */ 
/*  238 */         Object scripMap = 
/*  239 */           (TreeMap)((TreeMap)((TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(curStrategyID)).get(curScripListID)).get(curAssetClassID);
/*  240 */         if (scripMap != null)
/*      */         {
/*  242 */           String mtmFilePath = (String)((TreeMap)scripMap).get(scripID);
/*      */           try {
/*  244 */             CSVReader reader = new CSVReader(mtmFilePath, ',', 0);
/*      */             String[] dataLine;
/*  246 */             while ((dataLine = reader.getLine()) != null) { String[] dataLine;
/*  247 */               selectableDateSet.add(Long.valueOf(Long.parseLong(dataLine[0])));
/*      */             }
/*      */           }
/*      */           catch (Exception localException1) {}
/*      */           
/*  252 */           this.scripSet.add(scripID);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  257 */     this.outputKey = (this.outputKey + "|" + scripID);
/*      */     
/*  259 */     return selectableDateSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateSetsForKeys(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
/*      */   {
/*  266 */     getSelectableScripListSet(strategyKey);
/*  267 */     getSelectableAssetClassSet(assetClassKey);
/*  268 */     getSelectableScripSet(scripListKey);
/*  269 */     getSelectableDateSet(scripKey);
/*      */     
/*      */ 
/*  272 */     this.startDate = 0L;
/*  273 */     this.endDate = 20910101L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public PostProcess createPostProcessObject(String timeStamp)
/*      */     throws Exception
/*      */   {
/*  281 */     TreeMap<Long, Double> mtmMap = generateMTMMap(timeStamp);
/*      */     
/*      */ 
/*  284 */     ArrayList<String[]> tradeBook = generateTradebook(timeStamp);
/*      */     
/*      */ 
/*  287 */     return new PostProcess(mtmMap, this.outputKey, tradeBook, this.btGlobal, this.postProcessMode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void generateResults(long startDate, long endDate, int split)
/*      */     throws Exception
/*      */   {
/*  325 */     ArrayList<HashMap<String, Double>> resultMapsIS = new ArrayList();
/*  326 */     ArrayList<HashMap<String, Double>> resultMapsOS = new ArrayList();
/*      */     
/*  328 */     if (split < 10) { split = 10;
/*  329 */     } else if (split > 90) { split = 90;
/*      */     }
/*  331 */     long years = (0.01D * (endDate - startDate) * split);
/*  332 */     years /= 10000L;
/*      */     
/*      */ 
/*  335 */     this.startDate = startDate;
/*  336 */     this.endDate = (endDate - years * 10000L);
/*      */     String[] arrayOfString;
/*  338 */     int j = (arrayOfString = this.timeStamps).length; for (int i = 0; i < j; i++) { String timeStamp = arrayOfString[i];
/*      */       
/*  340 */       PostProcess ppObj = createPostProcessObject(timeStamp);
/*      */       
/*      */       try
/*      */       {
/*  344 */         ppObj.runPostprocess();
/*      */       }
/*      */       catch (ParseException e) {
/*  347 */         this.btGlobal.displayMessage("Error running post process");
/*  348 */         e.printStackTrace();
/*      */       }
/*      */       
/*  351 */       resultMapsIS.add(ppObj.getResultsMap());
/*      */     }
/*      */     
/*      */ 
/*  355 */     this.startDate = this.endDate;
/*  356 */     this.endDate = endDate;
/*      */     
/*  358 */     j = (arrayOfString = this.timeStamps).length; for (i = 0; i < j; i++) { String timeStamp = arrayOfString[i];
/*      */       
/*  360 */       PostProcess ppObj = createPostProcessObject(timeStamp);
/*      */       
/*      */       try
/*      */       {
/*  364 */         ppObj.runPostprocess();
/*      */       }
/*      */       catch (ParseException e) {
/*  367 */         this.btGlobal.displayMessage("Error running post process");
/*  368 */         e.printStackTrace();
/*      */       }
/*      */       
/*  371 */       resultMapsOS.add(ppObj.getResultsMap());
/*      */     }
/*      */     
/*      */ 
/*  375 */     this.btGlobal.displayResults(resultMapsIS, resultMapsOS);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ArrayList<String[]> generateTradebook(String timeStamp)
/*      */     throws Exception
/*      */   {
/*  493 */     ArrayList<String[]> consolTradebook = new ArrayList();
/*      */     
/*      */ 
/*  496 */     for (String strategyID : this.strategySet)
/*      */     {
/*      */ 
/*  499 */       ArrayList<String[]> strategyTradebook = new ArrayList();
/*      */       
/*      */ 
/*  502 */       for (String scripListID : this.scripListSet)
/*      */       {
/*      */ 
/*  505 */         ArrayList<String[]> scripListTradebook = new ArrayList();
/*      */         
/*      */ 
/*  508 */         for (String scripID : this.scripSet)
/*      */         {
/*      */ 
/*  511 */           if (checkIfResultExpected(strategyID, scripListID, scripID))
/*      */           {
/*      */ 
/*  514 */             ArrayList<String[]> scripTradebook = getStrategyScripListScripTradebook(timeStamp, strategyID, scripListID, 
/*  515 */               scripID);
/*      */             
/*  517 */             if (!this.postProcessMode.equals(PostProcessMode.Portfolio)) {
/*  518 */               scripTradebook = cleanTradebookForRunningTrades(scripTradebook);
/*      */             }
/*      */             
/*      */ 
/*  522 */             scripListTradebook.addAll(scripTradebook);
/*      */           }
/*      */         }
/*      */         
/*  526 */         if (this.postProcessMode.equals(PostProcessMode.Spread)) {
/*  527 */           scripListTradebook = timeSortTradebook(scripListTradebook);
/*      */         }
/*      */         
/*      */ 
/*  531 */         strategyTradebook.addAll(scripListTradebook);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  536 */       consolTradebook.addAll(strategyTradebook);
/*      */     }
/*      */     
/*  539 */     return consolTradebook;
/*      */   }
/*      */   
/*      */   private ArrayList<String[]> timeSortTradebook(ArrayList<String[]> scripListTradebook) {
/*  543 */     TreeMap<String, ArrayList<String[]>> tradeBookMap = new TreeMap();
/*      */     
/*      */ 
/*  546 */     for (String[] line : scripListTradebook) {
/*  547 */       if (tradeBookMap.get(line[0]) == null) {
/*  548 */         ArrayList<String[]> trades = new ArrayList();
/*  549 */         trades.add(line);
/*  550 */         tradeBookMap.put(line[0], trades);
/*      */       } else {
/*  552 */         trades = (ArrayList)tradeBookMap.get(line[0]);
/*  553 */         trades.add(line);
/*  554 */         tradeBookMap.put(line[0], trades);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  559 */     ArrayList<String[]> sortedTradeBook = new ArrayList();
/*      */     
/*      */     Iterator localIterator2;
/*  562 */     for (ArrayList<String[]> trades = tradeBookMap.keySet().iterator(); trades.hasNext(); 
/*      */         
/*  564 */         localIterator2.hasNext())
/*      */     {
/*  562 */       String key = (String)trades.next();
/*  563 */       ArrayList<String[]> trades = (ArrayList)tradeBookMap.get(key);
/*  564 */       localIterator2 = trades.iterator(); continue;String[] line = (String[])localIterator2.next();
/*  565 */       sortedTradeBook.add(line);
/*      */     }
/*      */     
/*  568 */     return sortedTradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ArrayList<String[]> getStrategyScripListScripTradebook(String timeStamp, String strategyID, String scripListID, String scripID)
/*      */   {
/*  640 */     ArrayList<String[]> tradeBook = new ArrayList();
/*  641 */     String tbPath = this.btGlobal.loginParameter.getOutputPath() + "/" + timeStamp + "/Trade Data";
/*  642 */     String tbFilePath = tbPath + "/" + strategyID + " " + scripListID + "/" + scripID + " Tradebook.csv";
/*  643 */     CSVReader reader = null;
/*      */     try {
/*  645 */       reader = new CSVReader(tbFilePath, ',', 0);
/*      */       String[] dataLine;
/*  647 */       while ((dataLine = reader.getLine()) != null) {
/*      */         String[] dataLine;
/*  649 */         long date = Long.parseLong(dataLine[0]) / 1000000L;
/*      */         
/*      */ 
/*  652 */         if (date >= this.startDate)
/*      */         {
/*      */ 
/*      */ 
/*  656 */           if (date > this.endDate) {
/*      */             break;
/*      */           }
/*      */           
/*  660 */           tradeBook.add(dataLine);
/*      */         }
/*      */       }
/*  663 */     } catch (IOException e1) { this.btGlobal.displayMessage("Could not find Tradebook: " + strategyID + " " + scripListID + " " + scripID);
/*  664 */       e1.printStackTrace();
/*  665 */       return null;
/*      */     }
/*      */     
/*  668 */     return tradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreeMap<Long, Double> generateMTMMap(String timeStamp)
/*      */     throws Exception
/*      */   {
/*  677 */     TreeMap<Long, Double> consolMTMMap = new TreeMap();
/*      */     
/*      */ 
/*  680 */     for (String strategyID : this.strategySet)
/*      */     {
/*      */ 
/*  683 */       TreeMap<Long, Double> strategyMTMMap = new TreeMap();
/*      */       
/*      */ 
/*  686 */       for (String scripListID : this.scripListSet)
/*      */       {
/*      */ 
/*  689 */         TreeMap<Long, Double> scripListMTMMap = new TreeMap();
/*      */         
/*      */ 
/*  692 */         for (String scripID : this.scripSet)
/*      */         {
/*      */ 
/*  695 */           if (checkIfResultExpected(strategyID, scripListID, scripID))
/*      */           {
/*      */ 
/*  698 */             TreeMap<Long, Double> scripMTMMap = getStrategyScripListScripMTM(timeStamp, strategyID, scripListID, scripID);
/*      */             
/*      */ 
/*  701 */             appendMTMmap(scripListMTMMap, scripMTMMap);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  706 */         appendMTMmap(strategyMTMMap, scripListMTMMap);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  711 */       TreeMap<Long, Integer> activeScripCountMap = new TreeMap();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  717 */       adjustMTMmap(strategyMTMMap, Integer.valueOf(this.scripListSet.size()), "Fixed", activeScripCountMap);
/*      */       
/*      */ 
/*  720 */       appendMTMmap(consolMTMMap, strategyMTMMap);
/*      */     }
/*      */     
/*      */ 
/*  724 */     adjustMTMmap(consolMTMMap, Integer.valueOf(this.strategySet.size()), "Fixed", new TreeMap());
/*      */     
/*  726 */     return consolMTMMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreeMap<Long, Double> getStrategyScripListScripMTM(String timeStamp, String strategyID, String scripListID, String scripID)
/*      */   {
/*  817 */     TreeMap<Long, Double> mtmMap = new TreeMap();
/*      */     
/*  819 */     String mtmPath = this.btGlobal.loginParameter.getOutputPath() + "/" + timeStamp + "/MTM Data/" + strategyID + " " + 
/*  820 */       scripListID;
/*      */     
/*  822 */     String mtmFilePath = mtmPath + "/" + scripID + " MTM.csv";
/*      */     
/*  824 */     CSVReader reader = null;
/*      */     try {
/*  826 */       reader = new CSVReader(mtmFilePath, ',', 0);
/*      */       String[] inData;
/*  828 */       while ((inData = reader.getLine()) != null) {
/*      */         String[] inData;
/*  830 */         Long date = Long.valueOf(Long.parseLong(inData[0]));
/*      */         
/*      */ 
/*  833 */         if (date.longValue() >= this.startDate)
/*      */         {
/*      */ 
/*      */ 
/*  837 */           if (date.longValue() > this.endDate) {
/*      */             break;
/*      */           }
/*      */           
/*  841 */           Double mtm = Double.valueOf(Double.parseDouble(inData[1]));
/*  842 */           mtmMap.put(date, mtm);
/*      */         }
/*      */       }
/*  845 */     } catch (IOException e1) { this.btGlobal.displayMessage("Could not find MTM File: " + strategyID + " " + scripListID + " " + scripID);
/*  846 */       e1.printStackTrace();
/*  847 */       return null;
/*      */     }
/*      */     
/*  850 */     return mtmMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean checkIfResultExpected(String strategyID, String scripListID, String scripID)
/*      */   {
/*  860 */     TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(strategyID);
/*  861 */     if (scripListMap == null) {
/*  862 */       return false;
/*      */     }
/*      */     
/*  865 */     TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListMap.get(scripListID);
/*  866 */     if (assetClassMap == null) {
/*  867 */       return false;
/*      */     }
/*  869 */     for (Map.Entry<String, TreeMap<String, String>> assetClassEntry : assetClassMap.entrySet()) {
/*  870 */       TreeMap<String, String> scripMap = (TreeMap)assetClassEntry.getValue();
/*      */       
/*  872 */       if (scripMap.keySet().contains(scripID)) {
/*  873 */         return true;
/*      */       }
/*      */     }
/*  876 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ArrayList<String[]> cleanTradebookForRunningTrades(ArrayList<String[]> tradeBook)
/*      */   {
/*  883 */     Double curPosition = Double.valueOf(0.0D);
/*  884 */     Double prevPosition = Double.valueOf(0.0D);
/*  885 */     int lastExitIndex = 0;
/*  886 */     for (int i = 0; i < tradeBook.size(); i++) {
/*  887 */       String[] trade = (String[])tradeBook.get(i);
/*  888 */       String side = trade[3];
/*  889 */       String type = trade[4];
/*      */       
/*  891 */       if (!type.equals("ROLLOVER"))
/*      */       {
/*      */ 
/*  894 */         int signal = 0;
/*  895 */         if (side.equals("BUY")) {
/*  896 */           signal = 1;
/*      */         } else {
/*  898 */           signal = -1;
/*      */         }
/*  900 */         prevPosition = curPosition;
/*  901 */         curPosition = Double.valueOf(curPosition.doubleValue() + signal * Double.parseDouble(trade[7]));
/*      */         
/*  903 */         if (((MathLib.doubleCompare(curPosition, Double.valueOf(0.0D)).intValue() == 0 ? 1 : 0) & (MathLib.doubleCompare(prevPosition, Double.valueOf(0.0D)).intValue() != 0 ? 1 : 0)) != 0) {
/*  904 */           lastExitIndex = i;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  910 */     while (tradeBook.size() > lastExitIndex + 1) {
/*  911 */       tradeBook.remove(lastExitIndex + 1);
/*      */     }
/*      */     
/*  914 */     return tradeBook;
/*      */   }
/*      */   
/*      */ 
/*      */   public void appendMTMmap(TreeMap<Long, Double> currentMap, TreeMap<Long, Double> newMap)
/*      */   {
/*  920 */     if (newMap.size() == 0) {
/*  921 */       return;
/*      */     }
/*  923 */     for (Map.Entry<Long, Double> mtmEntry : newMap.entrySet()) {
/*  924 */       Long dateTime = (Long)mtmEntry.getKey();
/*  925 */       Double mtm = (Double)mtmEntry.getValue();
/*  926 */       Double curMTM = Double.valueOf(0.0D);
/*      */       try {
/*  928 */         curMTM = (Double)currentMap.get(dateTime);
/*  929 */         if (curMTM == null)
/*  930 */           curMTM = Double.valueOf(0.0D);
/*  931 */         currentMap.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */       } catch (Exception e) {
/*  933 */         curMTM = Double.valueOf(0.0D);
/*  934 */         currentMap.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void adjustMTMmap(TreeMap<Long, Double> currentMap, Integer count, String aggregationMode, TreeMap<Long, Integer> activeScripCountMap)
/*      */     throws Exception
/*      */   {
/*  944 */     if (aggregationMode.equalsIgnoreCase("Fixed")) {
/*  945 */       for (Map.Entry<Long, Double> mtmEntry : new TreeMap(currentMap).entrySet()) {
/*  946 */         Long dateTime = (Long)mtmEntry.getKey();
/*  947 */         Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / count.doubleValue());
/*  948 */         currentMap.put(dateTime, mtm);
/*      */ 
/*      */       }
/*      */       
/*      */     }
/*  953 */     else if (aggregationMode.equalsIgnoreCase("Active")) {
/*  954 */       for (Map.Entry<Long, Double> mtmEntry : new TreeMap(currentMap).entrySet()) {
/*  955 */         Long dateTime = (Long)mtmEntry.getKey();
/*  956 */         Integer activeScripCount = (Integer)activeScripCountMap.get(dateTime);
/*  957 */         if (activeScripCount == null) {
/*  958 */           this.btGlobal.displayMessage("Scrip count for date: " + dateTime + " not available");
/*  959 */           throw new Exception("Scrip count for date: " + dateTime + " not available");
/*      */         }
/*  961 */         Double mtm = Double.valueOf(activeScripCount.intValue() < 1 ? 0.0D : ((Double)mtmEntry.getValue()).doubleValue() / activeScripCount.intValue());
/*  962 */         currentMap.put(dateTime, mtm);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeMap<Long, Double> combineMTMmaps(HashMap<String, TreeMap<Long, Double>> mtmMaps)
/*      */   {
/*  970 */     Integer count = Integer.valueOf(mtmMaps.size());
/*  971 */     TreeMap<Long, Double> consolMTM = new TreeMap();
/*  972 */     for (Map.Entry<String, TreeMap<Long, Double>> entry : mtmMaps.entrySet()) {
/*  973 */       TreeMap<Long, Double> mtmMap = (TreeMap)entry.getValue();
/*  974 */       if (mtmMap.size() != 0)
/*      */       {
/*  976 */         for (Map.Entry<Long, Double> mtmEntry : mtmMap.entrySet()) {
/*  977 */           Long dateTime = (Long)mtmEntry.getKey();
/*  978 */           Double mtm = (Double)mtmEntry.getValue();
/*  979 */           Double curMTM = Double.valueOf(0.0D);
/*      */           try {
/*  981 */             curMTM = (Double)consolMTM.get(dateTime);
/*  982 */             if (curMTM == null)
/*  983 */               curMTM = Double.valueOf(0.0D);
/*  984 */             consolMTM.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */           } catch (Exception e) {
/*  986 */             curMTM = Double.valueOf(0.0D);
/*  987 */             consolMTM.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
/*      */           }
/*      */         } }
/*      */     }
/*  991 */     for (Map.Entry<Long, Double> mtmEntry : new TreeMap(consolMTM).entrySet()) {
/*  992 */       Long dateTime = (Long)mtmEntry.getKey();
/*  993 */       Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / count.doubleValue());
/*  994 */       consolMTM.put(dateTime, mtm);
/*      */     }
/*  996 */     return consolMTM;
/*      */   }
/*      */   
/*      */   public void updateNumActiveScrips(TreeMap<Long, Integer> activeScripMap, String scripName, String auxFilesPath) {
/* 1000 */     CSVReader reader = null;
/*      */     try {
/* 1002 */       reader = new CSVReader(auxFilesPath + "\\" + scripName + ".csv", ',', 0);
/* 1003 */       String[] line = reader.getLine();
/* 1004 */       Long firstDate = Long.valueOf(Long.parseLong(line[0]));
/* 1005 */       int isThisScripActive = Integer.parseInt(line[2]);
/* 1006 */       activeScripMap.put(firstDate, Integer.valueOf(0));
/* 1007 */       while ((line = reader.getLine()) != null) {
/* 1008 */         Long date = Long.valueOf(Long.parseLong(line[0]));
/* 1009 */         if (activeScripMap.containsKey(date)) {
/* 1010 */           int currentNumActiveScrips = ((Integer)activeScripMap.get(date)).intValue();
/* 1011 */           activeScripMap.put(date, Integer.valueOf(currentNumActiveScrips + isThisScripActive));
/*      */         } else {
/* 1013 */           activeScripMap.put(date, Integer.valueOf(isThisScripActive));
/*      */         }
/* 1015 */         isThisScripActive = Integer.parseInt(line[2]);
/*      */       }
/*      */     }
/*      */     catch (IOException e) {
/* 1019 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   String getMTMFilePath(String timeStamp, String strategyID, String scripListID, String assetClassID, String scripID)
/*      */   {
/* 1027 */     return (String)((TreeMap)((TreeMap)((TreeMap)((TreeMap)this.resultsFileMaps.get(timeStamp)).get(strategyID)).get(scripListID)).get(assetClassID)).get(scripID);
/*      */   }
/*      */   
/*      */   public void generateResultsFileMap() throws Exception
/*      */   {
/*      */     String[] arrayOfString1;
/* 1033 */     int j = (arrayOfString1 = this.timeStamps).length; for (int i = 0; i < j; i++) { String timeStamp = arrayOfString1[i];
/*      */       
/* 1035 */       TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, String>>>> resultsFileMap = new TreeMap();
/*      */       
/* 1037 */       String mtmFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + timeStamp + "/MTM Data";
/*      */       
/* 1039 */       File mtmFolderPathFile = new File(mtmFolderPath);
/* 1040 */       File[] mtmFolders = mtmFolderPathFile.listFiles();
/*      */       
/*      */       File[] arrayOfFile1;
/* 1043 */       int m = (arrayOfFile1 = mtmFolders).length; for (int k = 0; k < m; k++) { File mtmFolder = arrayOfFile1[k];
/*      */         
/*      */ 
/* 1046 */         String[] mtmFolderVal = mtmFolder.getName().split(" ");
/*      */         
/* 1048 */         String strategyID = mtmFolderVal[0];
/*      */         
/*      */ 
/* 1051 */         TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListFileMap = (TreeMap)resultsFileMap.get(strategyID);
/* 1052 */         if (scripListFileMap == null) {
/* 1053 */           scripListFileMap = new TreeMap();
/*      */         }
/* 1055 */         String scripListID = mtmFolderVal[1];
/* 1056 */         if (mtmFolderVal.length > 2) {
/* 1057 */           scripListID = 
/* 1058 */             mtmFolderVal[1] + " " + mtmFolderVal[2] + " " + mtmFolderVal[3] + " " + mtmFolderVal[4] + " " + mtmFolderVal[5];
/*      */         }
/*      */         
/* 1061 */         String[] splits = mtmFolderVal[1].split("\\$");
/* 1062 */         String assetClassID = splits[1];
/* 1063 */         TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListFileMap.get(assetClassID);
/* 1064 */         if (assetClassMap == null) {
/* 1065 */           assetClassMap = new TreeMap();
/*      */         }
/*      */         
/* 1068 */         TreeMap<String, String> scripFileMap = (TreeMap)assetClassMap.get(scripListID);
/* 1069 */         if (scripFileMap == null) {
/* 1070 */           scripFileMap = new TreeMap();
/*      */         }
/*      */         
/* 1073 */         String mtmPath = mtmFolderPath + "/" + mtmFolder.getName();
/* 1074 */         File mtmPathFile = new File(mtmPath);
/* 1075 */         File[] mtmFiles = mtmPathFile.listFiles();
/*      */         
/*      */         File[] arrayOfFile2;
/* 1078 */         int i1 = (arrayOfFile2 = mtmFiles).length; for (int n = 0; n < i1; n++) { File mtmFile = arrayOfFile2[n];
/* 1079 */           String mtmFileName = mtmFile.getName();
/* 1080 */           String scripID = mtmFileName.substring(0, mtmFileName.length() - 8);
/*      */           
/*      */ 
/* 1083 */           String mtmFilePath = mtmFile.getAbsolutePath();
/* 1084 */           scripFileMap.put(scripID, mtmFilePath);
/*      */         }
/*      */         
/*      */ 
/* 1088 */         assetClassMap.put(assetClassID, scripFileMap);
/* 1089 */         scripListFileMap.put(scripListID, assetClassMap);
/* 1090 */         resultsFileMap.put(strategyID, scripListFileMap);
/*      */       }
/*      */       
/* 1093 */       this.resultsFileMaps.put(timeStamp, resultsFileMap);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/OOSAnalysisDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */