package com.q1.bt.machineLearning.driver;

import com.q1.bt.data.classes.Scrip;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.machineLearning.absclasses.MLAlgo;
import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
import com.q1.bt.machineLearning.utility.TradeAndMTMDataProcessor;
import com.q1.bt.process.machinelearning.MergeType;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.objects.MachineLearning;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.bt.process.parameter.MachineLearningParameter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MLPreProcessor
{
  HashMap<String, MLAlgo> algorithmMap = new HashMap();
  
  HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps;
  
  private TreeMap<Long, HashMap<String, Double>> tradeMTMMat;
  
  private HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMaps;
  
  MachineLearningParameter mlParameter;
  
  HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap;
  
  HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap;
  
  ArrayList<String> scripListUniverse;
  
  ArrayList<String> scripUniverse;
  
  String destPath;
  
  String sourcePath;
  
  private TradeAndMTMDataProcessor stratTradePnL;
  
  private String algoLastModifiedTimeStamp;
  
  Backtest backtest;
  MachineLearning machineLearning;
  BacktesterGlobal btGlobal;
  
  public MLPreProcessor(BacktesterGlobal btGlobal, Backtest backtest, MachineLearning machineLearning)
  {
    this.btGlobal = btGlobal;
    this.backtest = backtest;
    this.machineLearning = machineLearning;
    this.mlParameter = machineLearning.getMlParameter();
    
    this.modelSegmentWiseAssetUniverseMap = new HashMap();
    this.postModelSelectionSegmentWiseAssetUniverseMap = new HashMap();
  }
  

  public void mlPreProcess()
    throws Exception
  {
    HashMap<String, LinkedHashMap<String, ArrayList<Scrip>>> backtestMap = this.backtest.backtestMap;
    
    this.sourcePath = (this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp);
    this.destPath = (this.btGlobal.loginParameter.getOutputPath() + "/" + this.machineLearning.getTimeStamp());
    





    HashSet<String> scripSet = new HashSet();
    HashSet<String> scripListSet = new HashSet();
    Iterator localIterator2; for (Iterator localIterator1 = backtestMap.entrySet().iterator(); localIterator1.hasNext(); 
        


        localIterator2.hasNext())
    {
      Map.Entry<String, LinkedHashMap<String, ArrayList<Scrip>>> strategyScripListScripEntry = (Map.Entry)localIterator1.next();
      
      String strategyName = (String)strategyScripListScripEntry.getKey();
      
      localIterator2 = ((LinkedHashMap)strategyScripListScripEntry.getValue()).entrySet().iterator(); continue;Map.Entry<String, ArrayList<Scrip>> scripListScripEntry = (Map.Entry)localIterator2.next();
      
      String scripList = (String)scripListScripEntry.getKey();
      scripListSet.add(scripList);
      for (Scrip scrip : (ArrayList)scripListScripEntry.getValue())
      {
        scripSet.add(scrip.scripID);
        
        insertIntoSegmentAssetMap(this.mlParameter.getModelMergeType(), this.mlParameter.getSelectionMergeType(), strategyName, scripList, scrip.scripID);
      }
    }
    
    this.scripUniverse = new ArrayList(scripSet);
    this.scripListUniverse = new ArrayList(scripListSet);
    

    for (String segmentKey : this.modelSegmentWiseAssetUniverseMap.keySet()) {
      if (this.algorithmMap.get(segmentKey) == null) {
        MLAlgo mltemp = this.mlParameter.getMlAlgorithm().copy();
        this.algorithmMap.put(segmentKey, mltemp);
      }
    }
    

    TradeAndMTMDataProcessor stratTradeMTM = new TradeAndMTMDataProcessor(this.sourcePath);
    this.stratTradePnL = stratTradeMTM;
    

    stratTradeMTM.processTradeBooksAndMTMs();
    
    this.tradeStartEndMaps = stratTradeMTM.getTradeStartEndMaps();
    this.tradeMTMMat = stratTradeMTM.getTradeMTMMat();
    this.tradeStartDateTradeSideMaps = stratTradeMTM.getAssetStartDateTradeSideMaps();
    


    MLAlgo mlAlgo = this.mlParameter.getMlAlgorithm();
    String packageLocR = mlAlgo.getModelPackage().replaceAll("\\.", "/");
    String packagePathR = this.btGlobal.loginParameter.getMainPath() + "/src/" + packageLocR;
    String algoPathTS = packagePathR + "/" + mlAlgo.getModelName();
    File algoFile = new File(algoPathTS);
    SimpleDateFormat stratFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    this.algoLastModifiedTimeStamp = stratFormat.format(Long.valueOf(algoFile.lastModified()));
    
    createFolderStructure();
  }
  
  private void createFolderStructure() {
    new File(this.destPath + "/ML").mkdirs();
    new File(this.destPath + "/Parameters").mkdirs();
    new File(this.destPath + "/Trade Data").mkdirs();
    new File(this.destPath + "/MTM Data").mkdirs();
  }
  

  public void insertIntoSegmentAssetMap(MergeType modelMergeType, MergeType postModelSelectionMergeType, String strategy, String scripList, String scrip)
  {
    String modelKey = "";String postModelSelectionKey = "";
    
    switch (this.mlParameter.getModelMergeType()) {
    case All: 
      modelKey = "All";
      break;
    case AssetClass: 
      modelKey = strategy;
      break;
    case ScripList: 
      modelKey = scripList;
      break;
    case Scrip: 
      modelKey = scrip.split(" ")[1];
      break;
    case Strategy: 
      modelKey = scrip;
      break;
    }
    
    

    switch (this.mlParameter.getSelectionMergeType()) {
    case All: 
      postModelSelectionKey = "All";
      break;
    case AssetClass: 
      postModelSelectionKey = strategy;
      break;
    case ScripList: 
      postModelSelectionKey = scripList;
      break;
    case Scrip: 
      postModelSelectionKey = scrip.split(" ")[1];
      break;
    case Strategy: 
      postModelSelectionKey = scrip;
      break;
    }
    
    

    ArrayList<Asset> assetUniverse = (ArrayList)this.modelSegmentWiseAssetUniverseMap.get(modelKey);
    if (assetUniverse == null) {
      assetUniverse = new ArrayList();
    }
    assetUniverse.add(new Asset(strategy, scripList, scrip));
    this.modelSegmentWiseAssetUniverseMap.put(modelKey, assetUniverse);
    
    assetUniverse = (ArrayList)this.postModelSelectionSegmentWiseAssetUniverseMap.get(postModelSelectionKey);
    if (assetUniverse == null) {
      assetUniverse = new ArrayList();
    }
    assetUniverse.add(new Asset(strategy, scripList, scrip));
    this.postModelSelectionSegmentWiseAssetUniverseMap.put(postModelSelectionKey, assetUniverse);
  }
  



  public HashMap<String, MLAlgo> getAlgorithmMap()
  {
    return this.algorithmMap;
  }
  
  public TreeMap<Long, HashMap<String, Double>> getTradeMTMMat() {
    return this.tradeMTMMat;
  }
  
  public TradeAndMTMDataProcessor getStratTradePnL() {
    return this.stratTradePnL;
  }
  
  public ArrayList<String> getScripUniverse() {
    return this.scripUniverse;
  }
  
  public String getDestPath() {
    return this.destPath;
  }
  
  public String getSourcePath() {
    return this.sourcePath;
  }
  
  public HashMap<String, TreeMap<Long, Long>> getTradeStartEndMaps() {
    return this.tradeStartEndMaps;
  }
  
  public HashMap<String, TreeMap<Long, Integer>> getTradeStartDateTradeSideMaps()
  {
    return this.tradeStartDateTradeSideMaps;
  }
  
  public String getAlgoLastModifiedTimeStamp() {
    return this.algoLastModifiedTimeStamp;
  }
  
  public HashMap<String, ArrayList<Asset>> getModelSegmentWiseAssetUniverseMap() {
    return this.modelSegmentWiseAssetUniverseMap;
  }
  
  public HashMap<String, ArrayList<Asset>> getPostModelSelectionSegmentWiseAssetUniverseMap() {
    return this.postModelSelectionSegmentWiseAssetUniverseMap;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/MLPreProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */