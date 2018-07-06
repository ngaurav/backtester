package com.q1.bt.machineLearning.driver.driverHelperClasses;

import com.q1.bt.data.classes.Scrip;
import com.q1.bt.machineLearning.driver.AssetProp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;










public class BinaryGenerator
{
  HashMap<Long, String> tsTradedSelectedScripsMap;
  HashMap<Long, String> tsTradedNotSelectedScripsMap;
  TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals;
  HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps;
  HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMap;
  HashMap<String, Boolean> segmentFirstCallMap;
  public boolean bias;
  
  public BinaryGenerator(HashMap<Long, String> tsTradedSelectedScripsMap, HashMap<Long, String> tsTradedNotSelectedScripsMap, TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals, HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps, HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMap, boolean bias)
  {
    this.tsTradedSelectedScripsMap = tsTradedSelectedScripsMap;
    this.tsTradedNotSelectedScripsMap = tsTradedNotSelectedScripsMap;
    this.correlVals = correlVals;
    this.tradeStartEndMaps = tradeStartEndMaps;
    this.tradeStartDateTradeSideMap = tradeStartDateTradeSideMap;
    this.bias = bias;
  }
  




  public HashMap<String, Boolean> generateBinary(HashMap<String, Double> modelOutput, int segmentMLCount, int overallMLCount, double segmentCorrelThreshold, double overallCorrelThresh, long timeStamp, ArrayList<Long> dateList, HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap, boolean firstCall)
  {
    Long stDate = Long.valueOf(timeStamp);
    if (firstCall) {
      this.segmentFirstCallMap = initializeSegmentFirstCallMap(postModelSelectionSegmentWiseAssetUniverseMap.keySet());
    }
    




    for (Map.Entry<String, ArrayList<Asset>> segAssetEntry : postModelSelectionSegmentWiseAssetUniverseMap.entrySet())
    {
      String segmentName = (String)segAssetEntry.getKey();
      if (((Boolean)this.segmentFirstCallMap.get(segmentName)).booleanValue())
      {
        for (Asset asset : (ArrayList)segAssetEntry.getValue())
        {

          String assetName = asset.getAssetName();
          TreeMap<Long, Long> startEndMap = (TreeMap)this.tradeStartEndMaps.get(assetName);
          
          if (this.bias)
          {
            if ((modelOutput.containsKey("SHO#" + assetName)) || (modelOutput.containsKey("LON#" + assetName))) {
              this.segmentFirstCallMap.put(segmentName, Boolean.valueOf(false));
            }
            
          }
          else if (modelOutput.containsKey(assetName)) {
            this.segmentFirstCallMap.put(segmentName, Boolean.valueOf(false));
          }
          

          if (startEndMap != null)
          {

            for (Map.Entry<Long, Long> startEndDates : startEndMap.entrySet()) {
              if ((stDate.compareTo((Long)startEndDates.getKey()) > 0) && (stDate.compareTo((Long)startEndDates.getValue()) <= 0))
              {
                if (this.bias)
                {
                  updateTradedNotTradedScripsMap((Long)startEndDates.getKey(), (Long)startEndDates.getValue(), this.tsTradedNotSelectedScripsMap, "1|" + assetName);
                  updateTradedNotTradedScripsMap((Long)startEndDates.getKey(), (Long)startEndDates.getValue(), this.tsTradedNotSelectedScripsMap, "-1|" + assetName);
                }
                else
                {
                  updateTradedNotTradedScripsMap((Long)startEndDates.getKey(), (Long)startEndDates.getValue(), this.tsTradedNotSelectedScripsMap, assetName);
                }
                
              }
              else {
                if ((stDate.compareTo((Long)startEndDates.getKey()) < 0) && (stDate.compareTo((Long)startEndDates.getValue()) < 0)) {
                  break;
                }
              }
            }
          }
        }
      }
    }
    String alreadyRunningSelectedScripsString = (String)this.tsTradedSelectedScripsMap.get(stDate);
    Long date = Long.valueOf(timeStamp);
    


    if (alreadyRunningSelectedScripsString != null) {
      String[] alreadyRunningScrips = alreadyRunningSelectedScripsString.split(",");
      
      for (int i = 0; i < alreadyRunningScrips.length; i++)
      {
        if (this.bias)
        {
          String[] splittedTradeSideAssetName = alreadyRunningScrips[i].split("\\|");
          Integer tradeSide = Integer.valueOf(Integer.parseInt(splittedTradeSideAssetName[0]));
          String assetName = splittedTradeSideAssetName[1];
          
          if (tradeSide.intValue() == 1) {
            modelOutput.put("LON#" + assetName, Double.valueOf(Double.POSITIVE_INFINITY));
          } else {
            modelOutput.put("SHO#" + assetName, Double.valueOf(Double.POSITIVE_INFINITY));
          }
        }
        else {
          modelOutput.put(alreadyRunningScrips[i], Double.valueOf(Double.POSITIVE_INFINITY));
        }
      }
    }
    
    String alreadyRunningNotSelectedScripsString = (String)this.tsTradedNotSelectedScripsMap.get(stDate);
    String assetName;
    if (alreadyRunningNotSelectedScripsString != null) {
      String[] alreadyRunningNotSelectedScrips = alreadyRunningNotSelectedScripsString.split(",");
      
      for (int i = 0; i < alreadyRunningNotSelectedScrips.length; i++)
      {
        if (this.bias)
        {
          String[] splittedTradeSideAssetName = alreadyRunningNotSelectedScrips[i].split("\\|");
          Integer tradeSide = Integer.valueOf(Integer.parseInt(splittedTradeSideAssetName[0]));
          assetName = splittedTradeSideAssetName[1];
          String strTradeSide = tradeSide.intValue() == 1 ? "LON#" : "SHO#";
          modelOutput.put(strTradeSide + assetName, Double.valueOf(Double.NEGATIVE_INFINITY));
        }
        else
        {
          modelOutput.put(alreadyRunningNotSelectedScrips[i], Double.valueOf(Double.NEGATIVE_INFINITY));
        }
      }
    }
    

    HashMap<String, Boolean> result = new HashMap();
    Object segmentSelectedAssetList = new ArrayList();
    HashMap<String, ArrayList<AssetProp>> segmentWiseSelectedAssetUniverseMap = new HashMap();
    




    for (Map.Entry<String, ArrayList<Asset>> segmentAssetEntry : postModelSelectionSegmentWiseAssetUniverseMap.entrySet())
    {
      Object assetProps = new ArrayList();
      String segmentName = (String)segmentAssetEntry.getKey();
      
      for (Asset asset : (ArrayList)segmentAssetEntry.getValue())
      {
        if (this.bias)
        {
          String strategy = asset.getStrategyName();
          String scripList = asset.getScripListName();
          Scrip scrip = asset.getScrip();
          
          Asset longAsset = new Asset("LON#" + strategy, scripList, scrip.scripID);
          Asset shortAsset = new Asset("SHO#" + strategy, scripList, scrip.scripID);
          
          ((ArrayList)assetProps).add(new AssetProp(longAsset, modelOutput.get(longAsset.getAssetName()) == null ? -1.0D : ((Double)modelOutput.get(longAsset.getAssetName())).doubleValue()));
          ((ArrayList)assetProps).add(new AssetProp(shortAsset, modelOutput.get(shortAsset.getAssetName()) == null ? -1.0D : ((Double)modelOutput.get(shortAsset.getAssetName())).doubleValue()));
        }
        else
        {
          ((ArrayList)assetProps).add(new AssetProp(asset, modelOutput.get(asset.getAssetName()) == null ? -1.0D : ((Double)modelOutput.get(asset.getAssetName())).doubleValue()));
        }
      }
      

      ArrayList<AssetProp> selectedAssets = sortAndSelectAssets((ArrayList)assetProps, segmentMLCount, segmentCorrelThreshold, modelOutput, result, date, alreadyRunningSelectedScripsString);
      segmentWiseSelectedAssetUniverseMap.put(segmentName, selectedAssets);
      ((ArrayList)segmentSelectedAssetList).addAll(selectedAssets);
    }
    



    sortAndSelectAssets((ArrayList)segmentSelectedAssetList, overallMLCount, overallCorrelThresh, modelOutput, result, date, alreadyRunningSelectedScripsString);
    


    if (!date.equals(Long.valueOf(99999999L)))
    {
      for (String assetName : result.keySet()) {
        if (result.get(assetName) != null)
        {
          String transformedAssetName;
          Integer tradeSide;
          String assetNameWithoutTradeSide;
          String transformedAssetName;
          if (this.bias)
          {
            String[] splittedAssetName = assetName.split("\\#");
            Integer tradeSide = Integer.valueOf(splittedAssetName[0].equals("LON") ? 1 : -1);
            String assetNameWithoutTradeSide = assetName.split("\\#")[1];
            transformedAssetName = tradeSide + "|" + assetNameWithoutTradeSide;
          }
          else
          {
            tradeSide = null;
            assetNameWithoutTradeSide = assetName;
            transformedAssetName = assetName;
          }
          


          Integer btTradeSide = (Integer)((TreeMap)this.tradeStartDateTradeSideMap.get(assetNameWithoutTradeSide)).get(Long.valueOf(timeStamp));
          if (btTradeSide != null) {
            Object tempTradeMap;
            Object tempTradeMap;
            if (this.bias) {
              Object tempTradeMap;
              if ((((Boolean)result.get(assetName)).booleanValue()) && ((btTradeSide.equals(tradeSide)) || (btTradeSide.equals(Integer.valueOf(0))))) {
                tempTradeMap = this.tsTradedSelectedScripsMap;
              } else {
                tempTradeMap = this.tsTradedNotSelectedScripsMap;
              }
            }
            else {
              Object tempTradeMap;
              if (((Boolean)result.get(assetName)).booleanValue()) {
                tempTradeMap = this.tsTradedSelectedScripsMap;
              } else {
                tempTradeMap = this.tsTradedNotSelectedScripsMap;
              }
            }
            
            Long endTSLong = (Long)((TreeMap)this.tradeStartEndMaps.get(assetNameWithoutTradeSide)).get(Long.valueOf(timeStamp));
            
            if ((endTSLong != null) && (endTSLong.equals(Long.valueOf(99999999L)))) {
              updateTradedNotTradedScripsMap(stDate, endTSLong, (HashMap)tempTradeMap, transformedAssetName);
              

              endTSLong = (Long)dateList.get(dateList.size() - 1);
            }
            
            updateTradedNotTradedScripsMap(stDate, endTSLong, (HashMap)tempTradeMap, transformedAssetName);
          }
        }
      }
    }
    
    return result;
  }
  

  private HashMap<String, Boolean> initializeSegmentFirstCallMap(Set<String> segmentNameSet)
  {
    HashMap<String, Boolean> segmentFirstCallMap = new HashMap();
    for (String segmentName : segmentNameSet)
    {
      segmentFirstCallMap.put(segmentName, Boolean.valueOf(true));
    }
    
    return segmentFirstCallMap;
  }
  
  public HashMap<Long, String> getTsTradedSelectedScripsMap() { return this.tsTradedSelectedScripsMap; }
  
  public HashMap<Long, String> getTsTradedNotSelectedScripsMap()
  {
    return this.tsTradedNotSelectedScripsMap;
  }
  
  private void updateTradedNotTradedScripsMap(Long startTS, Long endTS, HashMap<Long, String> tempTradeMap, String assetName) {
    if (endTS == null) {
      return;
    }
    if (endTS.equals(Long.valueOf(99999999L))) {
      processTradedNotTradedScripsMap(endTS, tempTradeMap, assetName);
    }
    else
    {
      SimpleDateFormat numericDate = new SimpleDateFormat("yyyyMMdd");
      Calendar startDate = new GregorianCalendar();
      Calendar endDate = new GregorianCalendar();
      try
      {
        if (endTS != null) {
          Long eDate = endTS;
          endDate.setTime(numericDate.parse(eDate));
        }
        startDate.setTime(numericDate.parse(startTS));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      
      for (Calendar dt = (Calendar)startDate.clone(); dt.compareTo(endDate) <= 0; dt.add(5, 1))
      {
        Long processingDate = Long.valueOf(Long.parseLong(numericDate.format(dt.getTime())));
        processTradedNotTradedScripsMap(processingDate, tempTradeMap, assetName);
      }
    }
  }
  
  private void processTradedNotTradedScripsMap(Long processingDate, HashMap<Long, String> tempTradeMap, String assetName)
  {
    String tradedScripsString = (String)tempTradeMap.get(processingDate);
    
    if (tradedScripsString == null) {
      tempTradeMap.put(processingDate, assetName);
    }
    else
    {
      String[] tradedScrips = tradedScripsString.split(",");
      
      boolean found = false;
      
      for (int i = 0; i < tradedScrips.length; i++) {
        if (assetName.equals(tradedScrips[i])) {
          found = true;
          break;
        }
      }
      
      if (!found) {
        tempTradeMap.put(processingDate, tradedScripsString + "," + assetName);
      }
    }
  }
  
  private ArrayList<AssetProp> sortAndSelectAssets(ArrayList<AssetProp> assetProps, int assetCountThreshold, double correlThreshold, HashMap<String, Double> modelOutput, HashMap<String, Boolean> result, Long date, String alreadyRunningSelectedScripsString)
  {
    if (assetCountThreshold == 0) {
      assetCountThreshold = Integer.MAX_VALUE;
    }
    ArrayList<AssetProp> selectedAssets = new ArrayList();
    
    Collections.sort(assetProps, AssetProp.AssetComparator);
    
    for (int i = 0; i < assetProps.size(); i++)
    {
      int j = 0;
      Asset asset1 = ((AssetProp)assetProps.get(i)).getAsset();
      String assetName = asset1.getAssetName();
      
      if (modelOutput.get(assetName) == null) {
        result.put(assetName, null);

      }
      else if ((selectedAssets.size() < assetCountThreshold) && (((AssetProp)assetProps.get(i)).getWeight().doubleValue() > 0.0D))
      {
        for (; j < i; j++) {
          Asset asset2 = ((AssetProp)assetProps.get(j)).getAsset();
          Integer correlMultiplier = Integer.valueOf(1);
          
          if (this.bias)
          {
            Integer asset1TradeSide = Integer.valueOf(asset1.getAssetName().split("\\#")[0].equals("LON") ? 1 : -1);
            Integer asset2TradeSide = Integer.valueOf(asset2.getAssetName().split("\\#")[0].equals("LON") ? 1 : -1);
            correlMultiplier = Integer.valueOf(asset1TradeSide.intValue() * asset2TradeSide.intValue());
          }
          
          String scrip1 = asset1.getScrip().scripID;
          String scrip2 = asset2.getScrip().scripID;
          double correlValue = correlMultiplier.intValue() * ((Double)((HashMap)((HashMap)this.correlVals.get(date)).get(scrip1)).get(scrip2)).doubleValue();
          
          if ((correlValue > correlThreshold) && (result.get(asset2.getAssetName()) != null) && 
            (((Boolean)result.get(asset2.getAssetName())).booleanValue())) {
            result.put(assetName, Boolean.valueOf(false));
            break;
          }
        }
        
        String transformedAssetName = assetName;
        
        if (this.bias)
        {
          String[] splittedAssetName = assetName.split("\\#");
          Integer tradeSide = Integer.valueOf(splittedAssetName[0].equals("LON") ? 1 : -1);
          String assetNameWithoutTradeSide = assetName.split("\\#")[1];
          transformedAssetName = tradeSide + "|" + assetNameWithoutTradeSide;
        }
        
        if ((j == i) || ((alreadyRunningSelectedScripsString != null) && 
          (alreadyRunningSelectedScripsString.contains(transformedAssetName)))) {
          result.put(assetName, Boolean.valueOf(true));
          selectedAssets.add((AssetProp)assetProps.get(i));
        }
      }
      else
      {
        result.put(assetName, Boolean.valueOf(false));
      }
    }
    
    return selectedAssets;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/driverHelperClasses/BinaryGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */