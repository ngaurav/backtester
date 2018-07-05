/*     */ package com.q1.bt.machineLearning.driver;
/*     */ 
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.machineLearning.absclasses.MLAlgo;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
/*     */ import com.q1.bt.machineLearning.utility.TradeAndMTMDataProcessor;
/*     */ import com.q1.bt.process.machinelearning.MergeType;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.objects.MachineLearning;
/*     */ import com.q1.bt.process.parameter.LoginParameter;
/*     */ import com.q1.bt.process.parameter.MachineLearningParameter;
/*     */ import java.io.File;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class MLPreProcessor
/*     */ {
/*  25 */   HashMap<String, MLAlgo> algorithmMap = new HashMap();
/*     */   
/*     */   HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps;
/*     */   
/*     */   private TreeMap<Long, HashMap<String, Double>> tradeMTMMat;
/*     */   
/*     */   private HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMaps;
/*     */   
/*     */   MachineLearningParameter mlParameter;
/*     */   
/*     */   HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap;
/*     */   
/*     */   HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap;
/*     */   
/*     */   ArrayList<String> scripListUniverse;
/*     */   
/*     */   ArrayList<String> scripUniverse;
/*     */   
/*     */   String destPath;
/*     */   
/*     */   String sourcePath;
/*     */   
/*     */   private TradeAndMTMDataProcessor stratTradePnL;
/*     */   
/*     */   private String algoLastModifiedTimeStamp;
/*     */   
/*     */   Backtest backtest;
/*     */   MachineLearning machineLearning;
/*     */   BacktesterGlobal btGlobal;
/*     */   
/*     */   public MLPreProcessor(BacktesterGlobal btGlobal, Backtest backtest, MachineLearning machineLearning)
/*     */   {
/*  57 */     this.btGlobal = btGlobal;
/*  58 */     this.backtest = backtest;
/*  59 */     this.machineLearning = machineLearning;
/*  60 */     this.mlParameter = machineLearning.getMlParameter();
/*     */     
/*  62 */     this.modelSegmentWiseAssetUniverseMap = new HashMap();
/*  63 */     this.postModelSelectionSegmentWiseAssetUniverseMap = new HashMap();
/*     */   }
/*     */   
/*     */ 
/*     */   public void mlPreProcess()
/*     */     throws Exception
/*     */   {
/*  70 */     HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap = this.backtest.backtestMap;
/*     */     
/*  72 */     this.sourcePath = (this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp);
/*  73 */     this.destPath = (this.btGlobal.loginParameter.getOutputPath() + "/" + this.machineLearning.getTimeStamp());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */     HashSet<String> scripSet = new HashSet();
/*  81 */     HashSet<String> scripListSet = new HashSet();
/*  82 */     Iterator localIterator2; for (Iterator localIterator1 = backtestMap.entrySet().iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*  86 */         localIterator2.hasNext())
/*     */     {
/*  82 */       Map.Entry<String, LinkedHashMap<String, ArrayList<Scrip>>> strategyScripListScripEntry = (Map.Entry)localIterator1.next();
/*     */       
/*  84 */       String strategyName = (String)strategyScripListScripEntry.getKey();
/*     */       
/*  86 */       localIterator2 = ((LinkedHashMap)strategyScripListScripEntry.getValue()).entrySet().iterator(); continue;Map.Entry<String, ArrayList<Scrip>> scripListScripEntry = (Map.Entry)localIterator2.next();
/*     */       
/*  88 */       String scripList = (String)scripListScripEntry.getKey();
/*  89 */       scripListSet.add(scripList);
/*  90 */       for (Scrip scrip : (ArrayList)scripListScripEntry.getValue())
/*     */       {
/*  92 */         scripSet.add(scrip.scripID);
/*     */         
/*  94 */         insertIntoSegmentAssetMap(this.mlParameter.getModelMergeType(), this.mlParameter.getSelectionMergeType(), strategyName, scripList, scrip.scripID);
/*     */       }
/*     */     }
/*     */     
/*  98 */     this.scripUniverse = new ArrayList(scripSet);
/*  99 */     this.scripListUniverse = new ArrayList(scripListSet);
/*     */     
/*     */ 
/* 102 */     for (String segmentKey : this.modelSegmentWiseAssetUniverseMap.keySet()) {
/* 103 */       if (this.algorithmMap.get(segmentKey) == null) {
/* 104 */         MLAlgo mltemp = this.mlParameter.getMlAlgorithm().copy();
/* 105 */         this.algorithmMap.put(segmentKey, mltemp);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 110 */     TradeAndMTMDataProcessor stratTradeMTM = new TradeAndMTMDataProcessor(this.sourcePath);
/* 111 */     this.stratTradePnL = stratTradeMTM;
/*     */     
/*     */ 
/* 114 */     stratTradeMTM.processTradeBooksAndMTMs();
/*     */     
/* 116 */     this.tradeStartEndMaps = stratTradeMTM.getTradeStartEndMaps();
/* 117 */     this.tradeMTMMat = stratTradeMTM.getTradeMTMMat();
/* 118 */     this.tradeStartDateTradeSideMaps = stratTradeMTM.getAssetStartDateTradeSideMaps();
/*     */     
/*     */ 
/*     */ 
/* 122 */     MLAlgo mlAlgo = this.mlParameter.getMlAlgorithm();
/* 123 */     String packageLocR = mlAlgo.getModelPackage().replaceAll("\\.", "/");
/* 124 */     String packagePathR = this.btGlobal.loginParameter.getMainPath() + "/src/" + packageLocR;
/* 125 */     String algoPathTS = packagePathR + "/" + mlAlgo.getModelName();
/* 126 */     File algoFile = new File(algoPathTS);
/* 127 */     SimpleDateFormat stratFormat = new SimpleDateFormat("yyyyMMddHHmmss");
/* 128 */     this.algoLastModifiedTimeStamp = stratFormat.format(Long.valueOf(algoFile.lastModified()));
/*     */     
/* 130 */     createFolderStructure();
/*     */   }
/*     */   
/*     */   private void createFolderStructure() {
/* 134 */     new File(this.destPath + "/ML").mkdirs();
/* 135 */     new File(this.destPath + "/Parameters").mkdirs();
/* 136 */     new File(this.destPath + "/Trade Data").mkdirs();
/* 137 */     new File(this.destPath + "/MTM Data").mkdirs();
/*     */   }
/*     */   
/*     */ 
/*     */   public void insertIntoSegmentAssetMap(MergeType modelMergeType, MergeType postModelSelectionMergeType, String strategy, String scripList, String scrip)
/*     */   {
/* 143 */     String modelKey = "";String postModelSelectionKey = "";
/*     */     
/* 145 */     switch (this.mlParameter.getModelMergeType()) {
/*     */     case All: 
/* 147 */       modelKey = "All";
/* 148 */       break;
/*     */     case AssetClass: 
/* 150 */       modelKey = strategy;
/* 151 */       break;
/*     */     case ScripList: 
/* 153 */       modelKey = scripList;
/* 154 */       break;
/*     */     case Scrip: 
/* 156 */       modelKey = scrip.split(" ")[1];
/* 157 */       break;
/*     */     case Strategy: 
/* 159 */       modelKey = scrip;
/* 160 */       break;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 165 */     switch (this.mlParameter.getSelectionMergeType()) {
/*     */     case All: 
/* 167 */       postModelSelectionKey = "All";
/* 168 */       break;
/*     */     case AssetClass: 
/* 170 */       postModelSelectionKey = strategy;
/* 171 */       break;
/*     */     case ScripList: 
/* 173 */       postModelSelectionKey = scripList;
/* 174 */       break;
/*     */     case Scrip: 
/* 176 */       postModelSelectionKey = scrip.split(" ")[1];
/* 177 */       break;
/*     */     case Strategy: 
/* 179 */       postModelSelectionKey = scrip;
/* 180 */       break;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 185 */     ArrayList<Asset> assetUniverse = (ArrayList)this.modelSegmentWiseAssetUniverseMap.get(modelKey);
/* 186 */     if (assetUniverse == null) {
/* 187 */       assetUniverse = new ArrayList();
/*     */     }
/* 189 */     assetUniverse.add(new Asset(strategy, scripList, scrip));
/* 190 */     this.modelSegmentWiseAssetUniverseMap.put(modelKey, assetUniverse);
/*     */     
/* 192 */     assetUniverse = (ArrayList)this.postModelSelectionSegmentWiseAssetUniverseMap.get(postModelSelectionKey);
/* 193 */     if (assetUniverse == null) {
/* 194 */       assetUniverse = new ArrayList();
/*     */     }
/* 196 */     assetUniverse.add(new Asset(strategy, scripList, scrip));
/* 197 */     this.postModelSelectionSegmentWiseAssetUniverseMap.put(postModelSelectionKey, assetUniverse);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HashMap<String, MLAlgo> getAlgorithmMap()
/*     */   {
/* 205 */     return this.algorithmMap;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, HashMap<String, Double>> getTradeMTMMat() {
/* 209 */     return this.tradeMTMMat;
/*     */   }
/*     */   
/*     */   public TradeAndMTMDataProcessor getStratTradePnL() {
/* 213 */     return this.stratTradePnL;
/*     */   }
/*     */   
/*     */   public ArrayList<String> getScripUniverse() {
/* 217 */     return this.scripUniverse;
/*     */   }
/*     */   
/*     */   public String getDestPath() {
/* 221 */     return this.destPath;
/*     */   }
/*     */   
/*     */   public String getSourcePath() {
/* 225 */     return this.sourcePath;
/*     */   }
/*     */   
/*     */   public HashMap<String, TreeMap<Long, Long>> getTradeStartEndMaps() {
/* 229 */     return this.tradeStartEndMaps;
/*     */   }
/*     */   
/*     */   public HashMap<String, TreeMap<Long, Integer>> getTradeStartDateTradeSideMaps()
/*     */   {
/* 234 */     return this.tradeStartDateTradeSideMaps;
/*     */   }
/*     */   
/*     */   public String getAlgoLastModifiedTimeStamp() {
/* 238 */     return this.algoLastModifiedTimeStamp;
/*     */   }
/*     */   
/*     */   public HashMap<String, ArrayList<Asset>> getModelSegmentWiseAssetUniverseMap() {
/* 242 */     return this.modelSegmentWiseAssetUniverseMap;
/*     */   }
/*     */   
/*     */   public HashMap<String, ArrayList<Asset>> getPostModelSelectionSegmentWiseAssetUniverseMap() {
/* 246 */     return this.postModelSelectionSegmentWiseAssetUniverseMap;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/MLPreProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */