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
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
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
/*     */   HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMap;
/*     */   HashMap<String, Boolean> segmentFirstCallMap;
/*     */   public boolean bias;
/*     */   
/*     */   public BinaryGenerator(HashMap<Long, String> tsTradedSelectedScripsMap, HashMap<Long, String> tsTradedNotSelectedScripsMap, TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals, HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps, HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMap, boolean bias)
/*     */   {
/*  37 */     this.tsTradedSelectedScripsMap = tsTradedSelectedScripsMap;
/*  38 */     this.tsTradedNotSelectedScripsMap = tsTradedNotSelectedScripsMap;
/*  39 */     this.correlVals = correlVals;
/*  40 */     this.tradeStartEndMaps = tradeStartEndMaps;
/*  41 */     this.tradeStartDateTradeSideMap = tradeStartDateTradeSideMap;
/*  42 */     this.bias = bias;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HashMap<String, Boolean> generateBinary(HashMap<String, Double> modelOutput, int segmentMLCount, int overallMLCount, double segmentCorrelThreshold, double overallCorrelThresh, long timeStamp, ArrayList<Long> dateList, HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap, boolean firstCall)
/*     */   {
/*  51 */     Long stDate = Long.valueOf(timeStamp);
/*  52 */     if (firstCall) {
/*  53 */       this.segmentFirstCallMap = initializeSegmentFirstCallMap(postModelSelectionSegmentWiseAssetUniverseMap.keySet());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */     for (Map.Entry<String, ArrayList<Asset>> segAssetEntry : postModelSelectionSegmentWiseAssetUniverseMap.entrySet())
/*     */     {
/*  62 */       String segmentName = (String)segAssetEntry.getKey();
/*  63 */       if (((Boolean)this.segmentFirstCallMap.get(segmentName)).booleanValue())
/*     */       {
/*  65 */         for (Asset asset : (ArrayList)segAssetEntry.getValue())
/*     */         {
/*     */ 
/*  68 */           String assetName = asset.getAssetName();
/*  69 */           TreeMap<Long, Long> startEndMap = (TreeMap)this.tradeStartEndMaps.get(assetName);
/*     */           
/*  71 */           if (this.bias)
/*     */           {
/*  73 */             if ((modelOutput.containsKey("SHO#" + assetName)) || (modelOutput.containsKey("LON#" + assetName))) {
/*  74 */               this.segmentFirstCallMap.put(segmentName, Boolean.valueOf(false));
/*     */             }
/*     */             
/*     */           }
/*  78 */           else if (modelOutput.containsKey(assetName)) {
/*  79 */             this.segmentFirstCallMap.put(segmentName, Boolean.valueOf(false));
/*     */           }
/*     */           
/*     */ 
/*  83 */           if (startEndMap != null)
/*     */           {
/*     */ 
/*  86 */             for (Map.Entry<Long, Long> startEndDates : startEndMap.entrySet()) {
/*  87 */               if ((stDate.compareTo((Long)startEndDates.getKey()) > 0) && (stDate.compareTo((Long)startEndDates.getValue()) <= 0))
/*     */               {
/*  89 */                 if (this.bias)
/*     */                 {
/*  91 */                   updateTradedNotTradedScripsMap((Long)startEndDates.getKey(), (Long)startEndDates.getValue(), this.tsTradedNotSelectedScripsMap, "1|" + assetName);
/*  92 */                   updateTradedNotTradedScripsMap((Long)startEndDates.getKey(), (Long)startEndDates.getValue(), this.tsTradedNotSelectedScripsMap, "-1|" + assetName);
/*     */                 }
/*     */                 else
/*     */                 {
/*  96 */                   updateTradedNotTradedScripsMap((Long)startEndDates.getKey(), (Long)startEndDates.getValue(), this.tsTradedNotSelectedScripsMap, assetName);
/*     */                 }
/*     */                 
/*     */               }
/*     */               else {
/* 101 */                 if ((stDate.compareTo((Long)startEndDates.getKey()) < 0) && (stDate.compareTo((Long)startEndDates.getValue()) < 0)) {
/*     */                   break;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 110 */     String alreadyRunningSelectedScripsString = (String)this.tsTradedSelectedScripsMap.get(stDate);
/* 111 */     Long date = Long.valueOf(timeStamp);
/*     */     
/*     */ 
/*     */ 
/* 115 */     if (alreadyRunningSelectedScripsString != null) {
/* 116 */       String[] alreadyRunningScrips = alreadyRunningSelectedScripsString.split(",");
/*     */       
/* 118 */       for (int i = 0; i < alreadyRunningScrips.length; i++)
/*     */       {
/* 120 */         if (this.bias)
/*     */         {
/* 122 */           String[] splittedTradeSideAssetName = alreadyRunningScrips[i].split("\\|");
/* 123 */           Integer tradeSide = Integer.valueOf(Integer.parseInt(splittedTradeSideAssetName[0]));
/* 124 */           String assetName = splittedTradeSideAssetName[1];
/*     */           
/* 126 */           if (tradeSide.intValue() == 1) {
/* 127 */             modelOutput.put("LON#" + assetName, Double.valueOf(Double.POSITIVE_INFINITY));
/*     */           } else {
/* 129 */             modelOutput.put("SHO#" + assetName, Double.valueOf(Double.POSITIVE_INFINITY));
/*     */           }
/*     */         }
/*     */         else {
/* 133 */           modelOutput.put(alreadyRunningScrips[i], Double.valueOf(Double.POSITIVE_INFINITY));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 138 */     String alreadyRunningNotSelectedScripsString = (String)this.tsTradedNotSelectedScripsMap.get(stDate);
/*     */     String assetName;
/* 140 */     if (alreadyRunningNotSelectedScripsString != null) {
/* 141 */       String[] alreadyRunningNotSelectedScrips = alreadyRunningNotSelectedScripsString.split(",");
/*     */       
/* 143 */       for (int i = 0; i < alreadyRunningNotSelectedScrips.length; i++)
/*     */       {
/* 145 */         if (this.bias)
/*     */         {
/* 147 */           String[] splittedTradeSideAssetName = alreadyRunningNotSelectedScrips[i].split("\\|");
/* 148 */           Integer tradeSide = Integer.valueOf(Integer.parseInt(splittedTradeSideAssetName[0]));
/* 149 */           assetName = splittedTradeSideAssetName[1];
/* 150 */           String strTradeSide = tradeSide.intValue() == 1 ? "LON#" : "SHO#";
/* 151 */           modelOutput.put(strTradeSide + assetName, Double.valueOf(Double.NEGATIVE_INFINITY));
/*     */         }
/*     */         else
/*     */         {
/* 155 */           modelOutput.put(alreadyRunningNotSelectedScrips[i], Double.valueOf(Double.NEGATIVE_INFINITY));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 161 */     HashMap<String, Boolean> result = new HashMap();
/* 162 */     Object segmentSelectedAssetList = new ArrayList();
/* 163 */     HashMap<String, ArrayList<AssetProp>> segmentWiseSelectedAssetUniverseMap = new HashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 169 */     for (Map.Entry<String, ArrayList<Asset>> segmentAssetEntry : postModelSelectionSegmentWiseAssetUniverseMap.entrySet())
/*     */     {
/* 171 */       Object assetProps = new ArrayList();
/* 172 */       String segmentName = (String)segmentAssetEntry.getKey();
/*     */       
/* 174 */       for (Asset asset : (ArrayList)segmentAssetEntry.getValue())
/*     */       {
/* 176 */         if (this.bias)
/*     */         {
/* 178 */           String strategy = asset.getStrategyName();
/* 179 */           String scripList = asset.getScripListName();
/* 180 */           Scrip scrip = asset.getScrip();
/*     */           
/* 182 */           Asset longAsset = new Asset("LON#" + strategy, scripList, scrip.scripID);
/* 183 */           Asset shortAsset = new Asset("SHO#" + strategy, scripList, scrip.scripID);
/*     */           
/* 185 */           ((ArrayList)assetProps).add(new AssetProp(longAsset, modelOutput.get(longAsset.getAssetName()) == null ? -1.0D : ((Double)modelOutput.get(longAsset.getAssetName())).doubleValue()));
/* 186 */           ((ArrayList)assetProps).add(new AssetProp(shortAsset, modelOutput.get(shortAsset.getAssetName()) == null ? -1.0D : ((Double)modelOutput.get(shortAsset.getAssetName())).doubleValue()));
/*     */         }
/*     */         else
/*     */         {
/* 190 */           ((ArrayList)assetProps).add(new AssetProp(asset, modelOutput.get(asset.getAssetName()) == null ? -1.0D : ((Double)modelOutput.get(asset.getAssetName())).doubleValue()));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 195 */       ArrayList<AssetProp> selectedAssets = sortAndSelectAssets((ArrayList)assetProps, segmentMLCount, segmentCorrelThreshold, modelOutput, result, date, alreadyRunningSelectedScripsString);
/* 196 */       segmentWiseSelectedAssetUniverseMap.put(segmentName, selectedAssets);
/* 197 */       ((ArrayList)segmentSelectedAssetList).addAll(selectedAssets);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 203 */     sortAndSelectAssets((ArrayList)segmentSelectedAssetList, overallMLCount, overallCorrelThresh, modelOutput, result, date, alreadyRunningSelectedScripsString);
/*     */     
/*     */ 
/*     */ 
/* 207 */     if (!date.equals(Long.valueOf(99999999L)))
/*     */     {
/* 209 */       for (String assetName : result.keySet()) {
/* 210 */         if (result.get(assetName) != null)
/*     */         {
/*     */           String transformedAssetName;
/*     */           Integer tradeSide;
/*     */           String assetNameWithoutTradeSide;
/*     */           String transformedAssetName;
/* 216 */           if (this.bias)
/*     */           {
/* 218 */             String[] splittedAssetName = assetName.split("\\#");
/* 219 */             Integer tradeSide = Integer.valueOf(splittedAssetName[0].equals("LON") ? 1 : -1);
/* 220 */             String assetNameWithoutTradeSide = assetName.split("\\#")[1];
/* 221 */             transformedAssetName = tradeSide + "|" + assetNameWithoutTradeSide;
/*     */           }
/*     */           else
/*     */           {
/* 225 */             tradeSide = null;
/* 226 */             assetNameWithoutTradeSide = assetName;
/* 227 */             transformedAssetName = assetName;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 232 */           Integer btTradeSide = (Integer)((TreeMap)this.tradeStartDateTradeSideMap.get(assetNameWithoutTradeSide)).get(Long.valueOf(timeStamp));
/* 233 */           if (btTradeSide != null) {
/*     */             Object tempTradeMap;
/*     */             Object tempTradeMap;
/* 236 */             if (this.bias) {
/*     */               Object tempTradeMap;
/* 238 */               if ((((Boolean)result.get(assetName)).booleanValue()) && ((btTradeSide.equals(tradeSide)) || (btTradeSide.equals(Integer.valueOf(0))))) {
/* 239 */                 tempTradeMap = this.tsTradedSelectedScripsMap;
/*     */               } else {
/* 241 */                 tempTradeMap = this.tsTradedNotSelectedScripsMap;
/*     */               }
/*     */             }
/*     */             else {
/*     */               Object tempTradeMap;
/* 246 */               if (((Boolean)result.get(assetName)).booleanValue()) {
/* 247 */                 tempTradeMap = this.tsTradedSelectedScripsMap;
/*     */               } else {
/* 249 */                 tempTradeMap = this.tsTradedNotSelectedScripsMap;
/*     */               }
/*     */             }
/*     */             
/* 253 */             Long endTSLong = (Long)((TreeMap)this.tradeStartEndMaps.get(assetNameWithoutTradeSide)).get(Long.valueOf(timeStamp));
/*     */             
/* 255 */             if ((endTSLong != null) && (endTSLong.equals(Long.valueOf(99999999L)))) {
/* 256 */               updateTradedNotTradedScripsMap(stDate, endTSLong, (HashMap)tempTradeMap, transformedAssetName);
/*     */               
/*     */ 
/* 259 */               endTSLong = (Long)dateList.get(dateList.size() - 1);
/*     */             }
/*     */             
/* 262 */             updateTradedNotTradedScripsMap(stDate, endTSLong, (HashMap)tempTradeMap, transformedAssetName);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 268 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private HashMap<String, Boolean> initializeSegmentFirstCallMap(Set<String> segmentNameSet)
/*     */   {
/* 274 */     HashMap<String, Boolean> segmentFirstCallMap = new HashMap();
/* 275 */     for (String segmentName : segmentNameSet)
/*     */     {
/* 277 */       segmentFirstCallMap.put(segmentName, Boolean.valueOf(true));
/*     */     }
/*     */     
/* 280 */     return segmentFirstCallMap;
/*     */   }
/*     */   
/* 283 */   public HashMap<Long, String> getTsTradedSelectedScripsMap() { return this.tsTradedSelectedScripsMap; }
/*     */   
/*     */   public HashMap<Long, String> getTsTradedNotSelectedScripsMap()
/*     */   {
/* 287 */     return this.tsTradedNotSelectedScripsMap;
/*     */   }
/*     */   
/*     */   private void updateTradedNotTradedScripsMap(Long startTS, Long endTS, HashMap<Long, String> tempTradeMap, String assetName) {
/* 291 */     if (endTS == null) {
/* 292 */       return;
/*     */     }
/* 294 */     if (endTS.equals(Long.valueOf(99999999L))) {
/* 295 */       processTradedNotTradedScripsMap(endTS, tempTradeMap, assetName);
/*     */     }
/*     */     else
/*     */     {
/* 299 */       SimpleDateFormat numericDate = new SimpleDateFormat("yyyyMMdd");
/* 300 */       Calendar startDate = new GregorianCalendar();
/* 301 */       Calendar endDate = new GregorianCalendar();
/*     */       try
/*     */       {
/* 304 */         if (endTS != null) {
/* 305 */           Long eDate = endTS;
/* 306 */           endDate.setTime(numericDate.parse(eDate));
/*     */         }
/* 308 */         startDate.setTime(numericDate.parse(startTS));
/*     */       } catch (ParseException e) {
/* 310 */         e.printStackTrace();
/*     */       }
/*     */       
/* 313 */       for (Calendar dt = (Calendar)startDate.clone(); dt.compareTo(endDate) <= 0; dt.add(5, 1))
/*     */       {
/* 315 */         Long processingDate = Long.valueOf(Long.parseLong(numericDate.format(dt.getTime())));
/* 316 */         processTradedNotTradedScripsMap(processingDate, tempTradeMap, assetName);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void processTradedNotTradedScripsMap(Long processingDate, HashMap<Long, String> tempTradeMap, String assetName)
/*     */   {
/* 323 */     String tradedScripsString = (String)tempTradeMap.get(processingDate);
/*     */     
/* 325 */     if (tradedScripsString == null) {
/* 326 */       tempTradeMap.put(processingDate, assetName);
/*     */     }
/*     */     else
/*     */     {
/* 330 */       String[] tradedScrips = tradedScripsString.split(",");
/*     */       
/* 332 */       boolean found = false;
/*     */       
/* 334 */       for (int i = 0; i < tradedScrips.length; i++) {
/* 335 */         if (assetName.equals(tradedScrips[i])) {
/* 336 */           found = true;
/* 337 */           break;
/*     */         }
/*     */       }
/*     */       
/* 341 */       if (!found) {
/* 342 */         tempTradeMap.put(processingDate, tradedScripsString + "," + assetName);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private ArrayList<AssetProp> sortAndSelectAssets(ArrayList<AssetProp> assetProps, int assetCountThreshold, double correlThreshold, HashMap<String, Double> modelOutput, HashMap<String, Boolean> result, Long date, String alreadyRunningSelectedScripsString)
/*     */   {
/* 349 */     if (assetCountThreshold == 0) {
/* 350 */       assetCountThreshold = Integer.MAX_VALUE;
/*     */     }
/* 352 */     ArrayList<AssetProp> selectedAssets = new ArrayList();
/*     */     
/* 354 */     Collections.sort(assetProps, AssetProp.AssetComparator);
/*     */     
/* 356 */     for (int i = 0; i < assetProps.size(); i++)
/*     */     {
/* 358 */       int j = 0;
/* 359 */       Asset asset1 = ((AssetProp)assetProps.get(i)).getAsset();
/* 360 */       String assetName = asset1.getAssetName();
/*     */       
/* 362 */       if (modelOutput.get(assetName) == null) {
/* 363 */         result.put(assetName, null);
/*     */ 
/*     */       }
/* 366 */       else if ((selectedAssets.size() < assetCountThreshold) && (((AssetProp)assetProps.get(i)).getWeight().doubleValue() > 0.0D))
/*     */       {
/* 368 */         for (; j < i; j++) {
/* 369 */           Asset asset2 = ((AssetProp)assetProps.get(j)).getAsset();
/* 370 */           Integer correlMultiplier = Integer.valueOf(1);
/*     */           
/* 372 */           if (this.bias)
/*     */           {
/* 374 */             Integer asset1TradeSide = Integer.valueOf(asset1.getAssetName().split("\\#")[0].equals("LON") ? 1 : -1);
/* 375 */             Integer asset2TradeSide = Integer.valueOf(asset2.getAssetName().split("\\#")[0].equals("LON") ? 1 : -1);
/* 376 */             correlMultiplier = Integer.valueOf(asset1TradeSide.intValue() * asset2TradeSide.intValue());
/*     */           }
/*     */           
/* 379 */           String scrip1 = asset1.getScrip().scripID;
/* 380 */           String scrip2 = asset2.getScrip().scripID;
/* 381 */           double correlValue = correlMultiplier.intValue() * ((Double)((HashMap)((HashMap)this.correlVals.get(date)).get(scrip1)).get(scrip2)).doubleValue();
/*     */           
/* 383 */           if ((correlValue > correlThreshold) && (result.get(asset2.getAssetName()) != null) && 
/* 384 */             (((Boolean)result.get(asset2.getAssetName())).booleanValue())) {
/* 385 */             result.put(assetName, Boolean.valueOf(false));
/* 386 */             break;
/*     */           }
/*     */         }
/*     */         
/* 390 */         String transformedAssetName = assetName;
/*     */         
/* 392 */         if (this.bias)
/*     */         {
/* 394 */           String[] splittedAssetName = assetName.split("\\#");
/* 395 */           Integer tradeSide = Integer.valueOf(splittedAssetName[0].equals("LON") ? 1 : -1);
/* 396 */           String assetNameWithoutTradeSide = assetName.split("\\#")[1];
/* 397 */           transformedAssetName = tradeSide + "|" + assetNameWithoutTradeSide;
/*     */         }
/*     */         
/* 400 */         if ((j == i) || ((alreadyRunningSelectedScripsString != null) && 
/* 401 */           (alreadyRunningSelectedScripsString.contains(transformedAssetName)))) {
/* 402 */           result.put(assetName, Boolean.valueOf(true));
/* 403 */           selectedAssets.add((AssetProp)assetProps.get(i));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 408 */         result.put(assetName, Boolean.valueOf(false));
/*     */       }
/*     */     }
/*     */     
/* 412 */     return selectedAssets;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/driverHelperClasses/BinaryGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */