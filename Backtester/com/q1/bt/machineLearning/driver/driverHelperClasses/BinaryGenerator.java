/*     */ package com.q1.bt.machineLearning.driver.driverHelperClasses;
/*     */ 
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.machineLearning.driver.AssetProp;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BinaryGenerator
/*     */ {
/*     */   HashMap<Long, String> tsTradedSelectedScripsMap;
/*     */   HashMap<Long, String> tsTradedNotSelectedScripsMap;
/*     */   TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals;
/*     */   HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps;
/*     */   
/*     */   public BinaryGenerator(HashMap<Long, String> tsTradedSelectedScripsMap, HashMap<Long, String> tsTradedNotSelectedScripsMap, TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals, HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps)
/*     */   {
/*  30 */     this.tsTradedSelectedScripsMap = tsTradedSelectedScripsMap;
/*  31 */     this.tsTradedNotSelectedScripsMap = tsTradedNotSelectedScripsMap;
/*  32 */     this.correlVals = correlVals;
/*  33 */     this.tradeStartEndMaps = tradeStartEndMaps;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HashMap<String, Boolean> generateBinary(HashMap<String, Double> modelOutput, int segmentMLCount, int overallMLCount, double segmentCorrelThreshold, double overallCorrelThresh, long timeStamp, ArrayList<Long> dateList, HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap, boolean firstCall)
/*     */   {
/*  42 */     Long stDate = Long.valueOf(timeStamp);
/*     */     
/*  44 */     if (firstCall)
/*     */     {
/*  46 */       for (Map.Entry<String, TreeMap<Long, Long>> assetStartEndMap : this.tradeStartEndMaps.entrySet()) {
/*  47 */         String assetName = (String)assetStartEndMap.getKey();
/*  48 */         TreeMap<Long, Long> startEndMap = (TreeMap)assetStartEndMap.getValue();
/*     */         
/*  50 */         for (Map.Entry<Long, Long> startEndDates : startEndMap.entrySet()) {
/*  51 */           if ((stDate.compareTo((Long)startEndDates.getKey()) >= 0) && (stDate.compareTo((Long)startEndDates.getValue()) <= 0))
/*     */           {
/*  53 */             updateTradedNotTradedScripsMap(stDate, (Long)startEndDates.getValue(), this.tsTradedNotSelectedScripsMap, assetName);
/*     */           }
/*     */           else {
/*  56 */             if ((stDate.compareTo((Long)startEndDates.getKey()) < 0) && (stDate.compareTo((Long)startEndDates.getValue()) < 0)) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  64 */     String alreadyRunningSelectedScripsString = (String)this.tsTradedSelectedScripsMap.get(stDate);
/*  65 */     Long date = Long.valueOf(timeStamp);
/*     */     
/*     */ 
/*     */ 
/*  69 */     if (alreadyRunningSelectedScripsString != null) {
/*  70 */       String[] alreadyRunningScrips = alreadyRunningSelectedScripsString.split(",");
/*     */       
/*  72 */       for (int i = 0; i < alreadyRunningScrips.length; i++)
/*     */       {
/*  74 */         if (!date.equals(Long.valueOf(99999999L)))
/*     */         {
/*  76 */           modelOutput.put(alreadyRunningScrips[i], Double.valueOf(100.0D));
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*  83 */           modelOutput.put("LON#" + alreadyRunningScrips[i], Double.valueOf(100.0D));
/*  84 */           modelOutput.put("SHO#" + alreadyRunningScrips[i], Double.valueOf(-100.0D));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  89 */     String alreadyRunningNotSelectedScripsString = (String)this.tsTradedNotSelectedScripsMap.get(stDate);
/*     */     
/*     */ 
/*     */ 
/*  93 */     if (alreadyRunningNotSelectedScripsString != null) {
/*  94 */       String[] alreadyRunningNotSelectedScrips = alreadyRunningNotSelectedScripsString.split(",");
/*     */       
/*  96 */       for (int i = 0; i < alreadyRunningNotSelectedScrips.length; i++)
/*     */       {
/*  98 */         if (!date.equals(Long.valueOf(99999999L)))
/*     */         {
/* 100 */           modelOutput.put(alreadyRunningNotSelectedScrips[i], Double.valueOf(-100.0D));
/*     */         }
/*     */         else
/*     */         {
/* 104 */           modelOutput.put("LON#" + alreadyRunningNotSelectedScrips[i], Double.valueOf(-100.0D));
/* 105 */           modelOutput.put("SHO#" + alreadyRunningNotSelectedScrips[i], Double.valueOf(-100.0D));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 110 */     HashMap<String, Boolean> result = new HashMap();
/* 111 */     ArrayList<AssetProp> segmentSelectedAssetList = new ArrayList();
/* 112 */     Object segmentWiseSelectedAssetUniverseMap = new HashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */     for (Map.Entry<String, ArrayList<Asset>> segmentAssetEntry : postModelSelectionSegmentWiseAssetUniverseMap.entrySet())
/*     */     {
/* 120 */       ArrayList<AssetProp> assetProps = new ArrayList();
/* 121 */       String segmentName = (String)segmentAssetEntry.getKey();
/*     */       
/* 123 */       for (Asset asset : (ArrayList)segmentAssetEntry.getValue())
/*     */       {
/*     */ 
/* 126 */         if (!date.equals(Long.valueOf(99999999L)))
/*     */         {
/* 128 */           assetProps.add(new AssetProp(asset, modelOutput.get(asset.getAssetName()) == null ? -1.0D : ((Double)modelOutput.get(asset.getAssetName())).doubleValue()));
/*     */         }
/*     */         else
/*     */         {
/* 132 */           String strategy = asset.getStrategyName();
/* 133 */           String scripList = asset.getScripListName();
/* 134 */           Scrip scrip = asset.getScrip();
/*     */           
/* 136 */           Asset longAsset = new Asset("LON#" + strategy, scripList, scrip.scripID);
/* 137 */           Asset shortAsset = new Asset("SHO#" + strategy, scripList, scrip.scripID);
/*     */           
/* 139 */           assetProps.add(new AssetProp(longAsset, modelOutput.get(longAsset.getAssetName()) == null ? -1.0D : ((Double)modelOutput.get(longAsset.getAssetName())).doubleValue()));
/* 140 */           assetProps.add(new AssetProp(shortAsset, modelOutput.get(shortAsset.getAssetName()) == null ? -1.0D : ((Double)modelOutput.get(shortAsset.getAssetName())).doubleValue()));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 147 */       ArrayList<AssetProp> selectedAssets = sortAndSelectAssets(assetProps, segmentMLCount, segmentCorrelThreshold, modelOutput, result, date, alreadyRunningSelectedScripsString);
/* 148 */       ((HashMap)segmentWiseSelectedAssetUniverseMap).put(segmentName, selectedAssets);
/* 149 */       segmentSelectedAssetList.addAll(selectedAssets);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 155 */     sortAndSelectAssets(segmentSelectedAssetList, overallMLCount, overallCorrelThresh, modelOutput, result, date, alreadyRunningSelectedScripsString);
/*     */     
/*     */ 
/*     */ 
/* 159 */     if (!date.equals(Long.valueOf(99999999L)))
/*     */     {
/* 161 */       for (String assetName : result.keySet()) {
/* 162 */         if (result.get(assetName) != null)
/*     */         {
/*     */           HashMap<Long, String> tempTradeMap;
/*     */           
/*     */           HashMap<Long, String> tempTradeMap;
/*     */           
/* 168 */           if (((Boolean)result.get(assetName)).booleanValue()) {
/* 169 */             tempTradeMap = this.tsTradedSelectedScripsMap;
/*     */           } else {
/* 171 */             tempTradeMap = this.tsTradedNotSelectedScripsMap;
/*     */           }
/* 173 */           Long endTSLong = (Long)((TreeMap)this.tradeStartEndMaps.get(assetName)).get(Long.valueOf(timeStamp));
/*     */           
/* 175 */           if ((endTSLong != null) && (endTSLong.equals(Long.valueOf(99999999L)))) {
/* 176 */             updateTradedNotTradedScripsMap(stDate, endTSLong, tempTradeMap, assetName);
/*     */             
/*     */ 
/* 179 */             endTSLong = (Long)dateList.get(dateList.size() - 1);
/*     */           }
/*     */           
/* 182 */           updateTradedNotTradedScripsMap(stDate, endTSLong, tempTradeMap, assetName);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 191 */     return result;
/*     */   }
/*     */   
/*     */   public HashMap<Long, String> getTsTradedSelectedScripsMap()
/*     */   {
/* 196 */     return this.tsTradedSelectedScripsMap;
/*     */   }
/*     */   
/*     */   public HashMap<Long, String> getTsTradedNotSelectedScripsMap() {
/* 200 */     return this.tsTradedNotSelectedScripsMap;
/*     */   }
/*     */   
/*     */ 
/*     */   private void updateTradedNotTradedScripsMap(Long startTS, Long endTS, HashMap<Long, String> tempTradeMap, String assetName)
/*     */   {
/* 206 */     if (endTS == null) {
/* 207 */       return;
/*     */     }
/* 209 */     if (endTS.equals(Long.valueOf(99999999L))) {
/* 210 */       processTradedNotTradedScripsMap(endTS, tempTradeMap, assetName);
/*     */     }
/*     */     else
/*     */     {
/* 214 */       SimpleDateFormat numericDate = new SimpleDateFormat("yyyyMMdd");
/* 215 */       Calendar startDate = new GregorianCalendar();
/* 216 */       Calendar endDate = new GregorianCalendar();
/*     */       try
/*     */       {
/* 219 */         if (endTS != null) {
/* 220 */           Long eDate = endTS;
/* 221 */           endDate.setTime(numericDate.parse(eDate));
/*     */         }
/* 223 */         startDate.setTime(numericDate.parse(startTS));
/*     */       } catch (ParseException e) {
/* 225 */         e.printStackTrace();
/*     */       }
/*     */       
/* 228 */       for (Calendar dt = (Calendar)startDate.clone(); dt.compareTo(endDate) <= 0; dt.add(5, 1))
/*     */       {
/* 230 */         Long processingDate = Long.valueOf(Long.parseLong(numericDate.format(dt.getTime())));
/* 231 */         processTradedNotTradedScripsMap(processingDate, tempTradeMap, assetName);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void processTradedNotTradedScripsMap(Long processingDate, HashMap<Long, String> tempTradeMap, String assetName)
/*     */   {
/* 238 */     String tradedScripsString = (String)tempTradeMap.get(processingDate);
/*     */     
/* 240 */     if (tradedScripsString == null) {
/* 241 */       tempTradeMap.put(processingDate, assetName);
/*     */     }
/*     */     else
/*     */     {
/* 245 */       String[] tradedScrips = tradedScripsString.split(",");
/*     */       
/* 247 */       boolean found = false;
/*     */       
/* 249 */       for (int i = 0; i < tradedScrips.length; i++) {
/* 250 */         if (assetName.equals(tradedScrips[i])) {
/* 251 */           found = true;
/* 252 */           break;
/*     */         }
/*     */       }
/*     */       
/* 256 */       if (!found) {
/* 257 */         tempTradeMap.put(processingDate, tradedScripsString + "," + assetName);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private ArrayList<AssetProp> sortAndSelectAssets(ArrayList<AssetProp> assetProps, int assetCountThreshold, double correlThreshold, HashMap<String, Double> modelOutput, HashMap<String, Boolean> result, Long date, String alreadyRunningSelectedScripsString)
/*     */   {
/* 264 */     ArrayList<AssetProp> selectedAssets = new ArrayList();
/*     */     
/* 266 */     Collections.sort(assetProps, AssetProp.AssetComparator);
/*     */     
/* 268 */     for (int i = 0; i < assetProps.size(); i++)
/*     */     {
/* 270 */       int j = 0;
/* 271 */       Asset asset1 = ((AssetProp)assetProps.get(i)).getAsset();
/* 272 */       String assetName = asset1.getAssetName();
/*     */       
/* 274 */       if (modelOutput.get(assetName) == null) {
/* 275 */         result.put(assetName, null);
/*     */ 
/*     */       }
/* 278 */       else if ((selectedAssets.size() < assetCountThreshold) && (((AssetProp)assetProps.get(i)).getWeight().doubleValue() > 0.0D))
/*     */       {
/* 280 */         for (; j < i; j++) {
/* 281 */           Asset asset2 = ((AssetProp)assetProps.get(j)).getAsset();
/* 282 */           String scrip1 = asset1.getScrip().scripID;
/* 283 */           String scrip2 = asset2.getScrip().scripID;
/*     */           
/* 285 */           double correlValue = ((Double)((HashMap)((HashMap)this.correlVals.get(date)).get(scrip1)).get(scrip2)).doubleValue();
/*     */           
/* 287 */           if ((correlValue > correlThreshold) && (result.get(asset2.getAssetName()) != null) && 
/* 288 */             (((Boolean)result.get(asset2.getAssetName())).booleanValue())) {
/* 289 */             result.put(assetName, Boolean.valueOf(false));
/* 290 */             break;
/*     */           }
/*     */         }
/*     */         
/* 294 */         if ((j == i) || ((alreadyRunningSelectedScripsString != null) && 
/* 295 */           (alreadyRunningSelectedScripsString.contains(assetName)))) {
/* 296 */           result.put(assetName, Boolean.valueOf(true));
/* 297 */           selectedAssets.add((AssetProp)assetProps.get(i));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 302 */         result.put(assetName, Boolean.valueOf(false));
/*     */       }
/*     */     }
/*     */     
/* 306 */     return selectedAssets;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/driverHelperClasses/BinaryGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */