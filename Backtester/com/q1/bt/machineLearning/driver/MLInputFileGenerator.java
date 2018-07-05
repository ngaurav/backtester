/*     */ package com.q1.bt.machineLearning.driver;
/*     */ 
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.machineLearning.absclasses.Correl;
/*     */ import com.q1.bt.machineLearning.absclasses.DailyIndColl;
/*     */ import com.q1.bt.machineLearning.absclasses.Factor;
/*     */ import com.q1.bt.machineLearning.absclasses.FactorType;
/*     */ import com.q1.bt.machineLearning.absclasses.ValueType;
/*     */ import com.q1.bt.machineLearning.absclasses.VarList;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
/*     */ import com.q1.bt.machineLearning.utility.CandleData;
/*     */ import com.q1.bt.machineLearning.utility.DailyData;
/*     */ import com.q1.bt.machineLearning.utility.DailyDataReader;
/*     */ import com.q1.bt.machineLearning.utility.MetadataReader;
/*     */ import com.q1.bt.machineLearning.utility.TradeAndMTMDataProcessor;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.parameter.MachineLearningParameter;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MLInputFileGenerator
/*     */ {
/*     */   public boolean bias;
/*     */   HashMap<String, DailyDataReader> dailyReaderCollection;
/*     */   HashMap<String, DailyIndColl> dailyPriceIndCollection;
/*     */   HashMap<String, DailyIndColl> dailyMTMIndCollection;
/*     */   HashMap<String, DailyData> dailyDataCollection;
/*     */   HashMap<String, CandleData> candleDataCollection;
/*     */   HashMap<String, HashMap<String, Correl>> correlMap;
/*     */   TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals;
/*     */   HashMap<String, CSVWriter> rInputWriterMap;
/*     */   HashMap<String, ArrayList<Double[]>> dailyPriceIndVars;
/*     */   HashMap<String, ArrayList<Double[]>> dailyMTMIndVars;
/*     */   private ArrayList<String> priceIndNames;
/*     */   private ArrayList<String> mtmIndNames;
/*     */   BacktesterGlobal btGlobal;
/*     */   Backtest backtest;
/*     */   MachineLearningParameter mlParameter;
/*     */   
/*     */   public MLInputFileGenerator(BacktesterGlobal btGlobal, Backtest backtest, MachineLearningParameter mlParameter, HashMap<String, DailyDataReader> dailyReaderCollection, boolean bias)
/*     */   {
/*  74 */     this.btGlobal = btGlobal;
/*  75 */     this.backtest = backtest;
/*  76 */     this.mlParameter = mlParameter;
/*     */     
/*  78 */     this.dailyReaderCollection = dailyReaderCollection;
/*     */     
/*  80 */     this.bias = bias;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void createInputFile(boolean nextLayer, String sourcePath, String destPath, String dataPath, HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap, ArrayList<String> scripUniverse, TradeAndMTMDataProcessor stratTradePnL, ArrayList<Long> dateList, HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps, HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMaps, Long dailyStartDate)
/*     */     throws Exception
/*     */   {
/*     */     HashMap<String, TreeMap<Long, ArrayList<Long>>> mdScripwiseMap;
/*     */     
/*     */     try
/*     */     {
/*  92 */       MetadataReader mdMap = new MetadataReader(sourcePath, dataPath, scripUniverse);
/*  93 */       mdScripwiseMap = mdMap.getMetadataMap();
/*     */     } catch (Exception e) { HashMap<String, TreeMap<Long, ArrayList<Long>>> mdScripwiseMap;
/*  95 */       mdScripwiseMap = new HashMap();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 100 */     File destDir = new File(destPath);
/* 101 */     if ((destDir.exists()) && (nextLayer))
/*     */     {
/* 103 */       for (int i = 1;; i++) {
/* 104 */         File tempDir = new File(destPath + "/Temp/Temp" + i);
/* 105 */         if (!tempDir.exists()) {
/* 106 */           tempDir.mkdirs();
/* 107 */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 112 */       sourcePath = destPath + "/Temp/Temp" + i;
/*     */       String[] arrayOfString1;
/* 114 */       int j = (arrayOfString1 = destDir.list()).length; for (int i = 0; i < j; i++) { String dirName = arrayOfString1[i];
/* 115 */         if (!dirName.contains("Temp")) {
/* 116 */           File dir = new File(destPath + "/" + dirName);
/* 117 */           if (dir.isFile()) {
/* 118 */             if (dir.renameTo(new File(destPath + "/Temp/Temp" + i + "/" + dirName))) {
/* 119 */               System.out.println(dirName + " moved successfully");
/*     */             } else
/* 121 */               System.out.println(dirName + " could not be moved");
/*     */           } else {
/* 123 */             File newDir = new File(destPath + "/Temp/Temp" + i + "/" + dirName);
/* 124 */             newDir.mkdirs();
/* 125 */             String[] arrayOfString2; int m = (arrayOfString2 = dir.list()).length; for (int k = 0; k < m; k++) { String fileName = arrayOfString2[k];
/* 126 */               File file = new File(destPath + "/" + dirName + "/" + fileName);
/* 127 */               if (file.renameTo(new File(destPath + "/Temp/Temp" + i + "/" + dirName + "/" + fileName))) {
/* 128 */                 System.out.println(fileName + " moved successfully");
/*     */               } else {
/* 130 */                 System.out.println(fileName + " could not be moved");
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 137 */     initializeML(segmentWiseAssetUniverseMap, scripUniverse, mdScripwiseMap, sourcePath, destPath, dataPath, 
/* 138 */       dailyStartDate);
/*     */     
/*     */ 
/* 141 */     Calendar startDate = new GregorianCalendar();
/* 142 */     Calendar endDate = new GregorianCalendar();
/*     */     
/* 144 */     SimpleDateFormat numericDate = new SimpleDateFormat("yyyyMMdd");
/* 145 */     SimpleDateFormat numericYear = new SimpleDateFormat("yyyy");
/*     */     try {
/* 147 */       startDate.setTime(numericDate.parse(dailyStartDate.toString()));
/* 148 */       endDate.setTime(numericDate.parse(((Long)dateList.get(dateList.size() - 1)).toString()));
/*     */     } catch (ParseException e1) {
/* 150 */       System.out.println("Error in parsing Start and End daily date");
/* 151 */       e1.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 158 */     this.mlParameter.getVarList().getVarNames();
/* 159 */     Long curYear = Long.valueOf(1770L);
/* 160 */     HashMap<String, ArrayList<Double[]>> priceIndVarsMap = new HashMap();
/* 161 */     HashMap<String, ArrayList<Double[]>> mtmIndVarsMap = new HashMap();
/*     */     
/* 163 */     endDate.add(5, 1);
/*     */     
/* 165 */     for (Calendar dt = startDate; dt.compareTo(endDate) <= 0; dt.add(5, 1))
/*     */     {
/*     */ 
/*     */ 
/* 169 */       priceIndVarsMap.clear();
/* 170 */       mtmIndVarsMap.clear();
/*     */       Long year;
/* 172 */       Long year; if (dt.compareTo(endDate) == 0) {
/* 173 */         Long date = Long.valueOf(99999999L);
/* 174 */         year = Long.valueOf(9999L);
/*     */       } else {
/* 176 */         date = Long.valueOf(Long.parseLong(numericDate.format(dt.getTime())));
/* 177 */         year = Long.valueOf(Long.parseLong(numericYear.format(dt.getTime())));
/*     */       }
/*     */       
/* 180 */       if (!curYear.equals(year)) {
/* 181 */         System.out.println("Processing Year: " + year);
/* 182 */         curYear = year;
/*     */       }
/*     */       
/*     */ 
/* 186 */       dailyUpdate(segmentWiseAssetUniverseMap, scripUniverse, (Long)date, stratTradePnL);
/*     */       
/* 188 */       Object tradePnLList = (HashMap)stratTradePnL.getTradeMTMMat().get(date);
/*     */       
/* 190 */       priceIndVarsMap.putAll(this.dailyPriceIndVars);
/* 191 */       mtmIndVarsMap.putAll(this.dailyMTMIndVars);
/* 192 */       Iterator localIterator2; for (Iterator localIterator1 = segmentWiseAssetUniverseMap.entrySet().iterator(); localIterator1.hasNext(); 
/*     */           
/*     */ 
/*     */ 
/* 196 */           localIterator2.hasNext())
/*     */       {
/* 192 */         Map.Entry<String, ArrayList<Asset>> entry = (Map.Entry)localIterator1.next();
/* 193 */         String segmentName = (String)entry.getKey();
/* 194 */         ArrayList<Asset> segmentAssetUniverse = (ArrayList)entry.getValue();
/*     */         
/* 196 */         localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
/* 197 */         String assetName = asset.getAssetName();
/* 198 */         if (tradeStartEndMaps.get(assetName) != null)
/*     */         {
/* 200 */           Double mtm = tradePnLList == null ? null : (Double)((HashMap)tradePnLList).get(assetName);
/*     */           
/* 202 */           if (dt.compareTo(endDate) == 0) {
/* 203 */             mtm = Double.valueOf(0.0D);
/*     */           }
/*     */           
/* 206 */           if (mtm != null) {
/* 207 */             String scripName = asset.getScrip().scripID;
/*     */             
/* 209 */             if ((this.priceIndNames.size() == 0) || (priceIndVarsMap.get(scripName) != null))
/*     */             {
/*     */ 
/* 212 */               if ((this.mtmIndNames.size() == 0) || (mtmIndVarsMap.get(assetName) != null))
/*     */               {
/*     */ 
/* 215 */                 CSVWriter rInputWriter = (CSVWriter)this.rInputWriterMap.get(segmentName);
/* 216 */                 String tradeEndDate = "";
/*     */                 
/* 218 */                 if (((TreeMap)tradeStartEndMaps.get(assetName)).get(date) == null) {
/* 219 */                   tradeEndDate = "99999999";
/*     */                 } else {
/* 221 */                   tradeEndDate = ((Long)((TreeMap)tradeStartEndMaps.get(assetName)).get(date)).toString();
/*     */                 }
/*     */                 
/*     */ 
/* 225 */                 Integer tradeSide = (Integer)((TreeMap)tradeStartDateTradeSideMaps.get(assetName)).get(date);
/*     */                 
/* 227 */                 if (this.bias)
/*     */                 {
/* 229 */                   if (tradeSide == null)
/*     */                   {
/* 231 */                     writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(1), priceIndVarsMap, mtmIndVarsMap, scripName, Double.valueOf(0.0D), false);
/* 232 */                     writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(-1), priceIndVarsMap, mtmIndVarsMap, scripName, Double.valueOf(0.0D), false);
/*     */                   }
/*     */                   else
/*     */                   {
/* 236 */                     writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, tradeSide, priceIndVarsMap, mtmIndVarsMap, scripName, mtm, false);
/* 237 */                     writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(-tradeSide.intValue()), priceIndVarsMap, mtmIndVarsMap, scripName, Double.valueOf(0.0D), false);
/*     */                   }
/*     */                   
/*     */                 }
/*     */                 else {
/* 242 */                   writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(0), priceIndVarsMap, mtmIndVarsMap, scripName, mtm, false);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 258 */     for (Object date = this.rInputWriterMap.values().iterator(); ((Iterator)date).hasNext();) { CSVWriter rInputWriter = (CSVWriter)((Iterator)date).next();
/* 259 */       rInputWriter.flush();
/* 260 */       rInputWriter.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initializeML(HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap, ArrayList<String> scripUniverse, HashMap<String, TreeMap<Long, ArrayList<Long>>> mdScripwiseMap, String sourcePath, String destPath, String dataPath, Long dailyStartDate)
/*     */     throws Exception
/*     */   {
/* 274 */     this.dailyMTMIndVars = new HashMap();
/* 275 */     this.dailyPriceIndVars = new HashMap();
/*     */     
/* 277 */     initDailyData(segmentWiseAssetUniverseMap);
/* 278 */     initCandleData(scripUniverse);
/*     */     
/* 280 */     initIndicators(segmentWiseAssetUniverseMap, scripUniverse, this.mlParameter.getFactorList(), 
/* 281 */       this.mlParameter.getVarList(), dataPath);
/* 282 */     initCorelMap(scripUniverse, this.mlParameter.getCorrelPeriod().intValue());
/*     */     
/*     */ 
/* 285 */     initwriters(dataPath, segmentWiseAssetUniverseMap.keySet(), sourcePath, destPath);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void initCorelMap(ArrayList<String> scripList, int correlPeriod)
/*     */   {
/* 292 */     this.correlMap = new HashMap();
/* 293 */     this.correlVals = new TreeMap();
/* 294 */     for (int i = 0; i < scripList.size(); i++) {
/* 295 */       String scrip1 = (String)scripList.get(i);
/* 296 */       this.correlMap.put(scrip1, new HashMap());
/* 297 */       for (int j = 0; j < i; j++) {
/* 298 */         String scrip2 = (String)scripList.get(j);
/* 299 */         Correl corEle = new Correl(correlPeriod);
/* 300 */         ((HashMap)this.correlMap.get(scrip1)).put(scrip2, corEle);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initIndicators(HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap, ArrayList<String> scripList, ArrayList<String> factorList, VarList varListType, String dataPath)
/*     */     throws Exception
/*     */   {
/* 311 */     this.dailyPriceIndCollection = new HashMap();
/* 312 */     this.dailyMTMIndCollection = new HashMap();
/*     */     
/* 314 */     this.priceIndNames = new ArrayList();
/* 315 */     this.mtmIndNames = new ArrayList();
/*     */     
/*     */     Iterator localIterator2;
/* 318 */     for (Iterator localIterator1 = segmentWiseAssetUniverseMap.values().iterator(); localIterator1.hasNext(); 
/* 319 */         localIterator2.hasNext())
/*     */     {
/* 318 */       ArrayList<Asset> segmentAssetUniverse = (ArrayList)localIterator1.next();
/* 319 */       localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
/*     */       
/*     */ 
/* 322 */       DailyIndColl dailyMTMIndicator = new DailyIndColl(factorList, varListType, FactorType.MTM, 
/* 323 */         ValueType.Numerical);
/*     */       
/*     */ 
/* 326 */       this.dailyMTMIndCollection.put(asset.getAssetName(), dailyMTMIndicator);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 331 */     for (String scripName : scripList) {
/* 332 */       DailyIndColl dailyPriceIndicator = new DailyIndColl(factorList, varListType, FactorType.Price, 
/* 333 */         ValueType.Numerical);
/* 334 */       this.dailyPriceIndCollection.put(scripName, dailyPriceIndicator);
/*     */     }
/*     */     
/*     */ 
/* 338 */     DailyIndColl Price1DColl = (DailyIndColl)this.dailyPriceIndCollection.get(scripList.get(0));
/* 339 */     DailyIndColl MTM1DColl = 
/* 340 */       (DailyIndColl)this.dailyMTMIndCollection.get(((Asset)((ArrayList)segmentWiseAssetUniverseMap.values().iterator().next()).get(0)).getAssetName());
/*     */     
/* 342 */     for (Factor factor : Price1DColl.getFactorList()) {
/* 343 */       this.priceIndNames.add(factor.getName());
/*     */     }
/*     */     
/* 346 */     for (Factor factor : MTM1DColl.getFactorList()) {
/* 347 */       this.mtmIndNames.add(factor.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   private void initDailyData(HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap)
/*     */   {
/* 353 */     this.dailyDataCollection = new HashMap();
/* 354 */     Iterator localIterator2; for (Iterator localIterator1 = segmentWiseAssetUniverseMap.values().iterator(); localIterator1.hasNext(); 
/* 355 */         localIterator2.hasNext())
/*     */     {
/* 354 */       ArrayList<Asset> segmentAssetUniverse = (ArrayList)localIterator1.next();
/* 355 */       localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
/* 356 */       this.dailyDataCollection.put(asset.getAssetName(), 
/* 357 */         new DailyData(asset.getScrip().scripID, asset.getScrip().segmentName));
/*     */     }
/*     */   }
/*     */   
/*     */   private void initCandleData(ArrayList<String> scripList)
/*     */   {
/* 363 */     this.candleDataCollection = new HashMap();
/* 364 */     for (String scripName : scripList) {
/* 365 */       String[] scripNameParts = scripName.split(" ");
/* 366 */       this.candleDataCollection.put(scripName, new CandleData(scripNameParts[3], scripNameParts[1]));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initwriters(String dataPath, Set<String> segmentSet, String sourcePath, String destPath)
/*     */     throws Exception
/*     */   {
/* 381 */     this.rInputWriterMap = new HashMap();
/* 382 */     for (String segmentName : segmentSet)
/*     */     {
/* 384 */       if (this.rInputWriterMap.get(segmentName) == null)
/*     */       {
/*     */ 
/* 387 */         CSVWriter inputWriter = new CSVWriter(destPath + "/ML/" + segmentName + " Input.csv", false, ",");
/*     */         
/*     */ 
/* 390 */         this.rInputWriterMap.put(segmentName, inputWriter);
/*     */         
/*     */ 
/* 393 */         inputWriter.write("Date,EndDate,Asset, TradeSide");
/*     */         
/* 395 */         for (int i = 0; i < this.priceIndNames.size(); i++) {
/* 396 */           inputWriter.write("," + (String)this.priceIndNames.get(i));
/*     */         }
/*     */         
/* 399 */         for (int i = 0; i < this.mtmIndNames.size(); i++)
/* 400 */           inputWriter.write("," + (String)this.mtmIndNames.get(i));
/* 401 */         inputWriter.write(",MTM\n");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void dailyUpdate(HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap, ArrayList<String> scripUniverse, Long date, TradeAndMTMDataProcessor stratTradePnL)
/*     */     throws IOException
/*     */   {
/*     */     Iterator localIterator2;
/*     */     
/*     */ 
/*     */ 
/* 417 */     for (Iterator localIterator1 = segmentWiseAssetUniverseMap.values().iterator(); localIterator1.hasNext(); 
/* 418 */         localIterator2.hasNext())
/*     */     {
/* 417 */       ArrayList<Asset> segmentAssetUniverse = (ArrayList)localIterator1.next();
/* 418 */       localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
/* 419 */       String assetName = asset.getAssetName();
/* 420 */       Long prevDate = ((DailyDataReader)this.dailyReaderCollection.get(asset.getScrip().scripID)).getPrevDate();
/*     */       try {
/* 422 */         ((DailyDataReader)this.dailyReaderCollection.get(asset.getScrip().scripID)).process(date, (CandleData)this.dailyDataCollection.get(assetName), 
/* 423 */           (Double)((TreeMap)stratTradePnL.getDailyMTMMap().get(asset.getAssetName())).get(prevDate));
/*     */       }
/*     */       catch (Exception e) {
/* 426 */         System.err.println("Error in fetching Daily Data Reader for " + assetName);
/* 427 */         e.printStackTrace();
/*     */       }
/*     */       
/* 430 */       getDailyMTMIndValues(date, asset.getAssetName());
/*     */     }
/*     */     
/* 433 */     for (String scripName : scripUniverse) {
/*     */       try
/*     */       {
/* 436 */         ((DailyDataReader)this.dailyReaderCollection.get(scripName)).process(date, (CandleData)this.candleDataCollection.get(scripName));
/*     */       }
/*     */       catch (Exception e) {
/* 439 */         System.err.println("Error in fetching Daily Data Reader for " + scripName);
/* 440 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 446 */     getDailyPriceIndValues(date, scripUniverse);
/* 447 */     updateCorrelMatrix(date, scripUniverse);
/*     */   }
/*     */   
/*     */   private void updateCorrelMatrix(Long date, ArrayList<String> scripList)
/*     */   {
/* 452 */     this.correlVals.put(date, new HashMap());
/* 453 */     for (int i = 0; i < scripList.size(); i++) {
/* 454 */       String scrip1 = (String)scripList.get(i);
/* 455 */       ((HashMap)this.correlVals.get(date)).put(scrip1, new HashMap());
/* 456 */       ((HashMap)((HashMap)this.correlVals.get(date)).get(scrip1)).put(scrip1, Double.valueOf(1.0D));
/* 457 */       for (int j = 0; j < i; j++) {
/* 458 */         String scrip2 = (String)scripList.get(j);
/* 459 */         ((Correl)((HashMap)this.correlMap.get(scrip1)).get(scrip2)).updateCorrel((CandleData)this.candleDataCollection.get(scrip1), 
/* 460 */           (CandleData)this.candleDataCollection.get(scrip2), date);
/* 461 */         Double correlVal = ((Correl)((HashMap)this.correlMap.get(scrip1)).get(scrip2)).getVal();
/* 462 */         ((HashMap)((HashMap)this.correlVals.get(date)).get(scrip1)).put(scrip2, correlVal);
/* 463 */         ((HashMap)((HashMap)this.correlVals.get(date)).get(scrip2)).put(scrip1, correlVal);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void getDailyMTMIndValues(Long date, String assetName)
/*     */     throws IOException
/*     */   {
/* 477 */     DailyData[] dailyDataArray = { (DailyData)this.dailyDataCollection.get(assetName) };
/* 478 */     this.dailyMTMIndVars.put(assetName, ((DailyIndColl)this.dailyMTMIndCollection.get(assetName)).process(dailyDataArray, date));
/*     */   }
/*     */   
/*     */   private void getDailyPriceIndValues(Long date, ArrayList<String> scripList) throws IOException
/*     */   {
/* 483 */     for (String scripName : scripList) {
/* 484 */       CandleData[] candleDataArray = { (CandleData)this.candleDataCollection.get(scripName) };
/* 485 */       this.dailyPriceIndVars.put(scripName, ((DailyIndColl)this.dailyPriceIndCollection.get(scripName)).process(candleDataArray, date));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void writeLineInInputFile(CSVWriter rInputWriter, Long date, String tradeEndDate, String assetName, Integer tradeSide, HashMap<String, ArrayList<Double[]>> priceIndVarsMap, HashMap<String, ArrayList<Double[]>> mtmIndVarsMap, String scripName, Double mtm, boolean predictionDateFlag)
/*     */   {
/* 493 */     String tradeSideAssetName = "";
/*     */     
/* 495 */     if (this.bias)
/*     */     {
/* 497 */       if (tradeSide.intValue() == 1) {
/* 498 */         tradeSideAssetName = "LON#" + assetName;
/* 499 */       } else if (tradeSide.intValue() == -1) {
/* 500 */         tradeSideAssetName = "SHO#" + assetName;
/*     */       } else {
/* 502 */         tradeSideAssetName = "NON#" + assetName;
/*     */       }
/*     */     }
/*     */     else {
/* 506 */       tradeSideAssetName = assetName;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 511 */       rInputWriter.write(date.toString());
/* 512 */       rInputWriter.write("," + tradeEndDate);
/* 513 */       rInputWriter.write("," + tradeSideAssetName);
/* 514 */       rInputWriter.write("," + tradeSide);
/*     */       
/* 516 */       if (priceIndVarsMap.get(scripName) != null) {
/* 517 */         for (int k = 0; k < ((ArrayList)priceIndVarsMap.get(scripName)).size(); k++) {
/* 518 */           if (this.bias) {
/* 519 */             rInputWriter.write("," + ((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[((1 - tradeSide.intValue()) / 2)]);
/*     */           }
/*     */           else {
/* 522 */             if (!((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[0].equals(((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[1]))
/*     */             {
/* 524 */               System.err.println("Getting different values in unbiased mode.");
/* 525 */               System.err.println("Check definition of :" + (String)this.priceIndNames.get(k));
/* 526 */               System.exit(0);
/*     */             }
/* 528 */             rInputWriter.write("," + ((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[0]);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 533 */       if (mtmIndVarsMap.get(assetName) != null) {
/* 534 */         for (int k = 0; k < ((ArrayList)mtmIndVarsMap.get(assetName)).size(); k++) {
/* 535 */           if (this.bias) {
/* 536 */             rInputWriter.write("," + ((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[((1 - tradeSide.intValue()) / 2)]);
/*     */           }
/*     */           else {
/* 539 */             if (!((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[0].equals(((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[1]))
/*     */             {
/* 541 */               System.err.println("Getting different values in unbiased mode.");
/* 542 */               System.err.println("Check definition of :" + (String)this.mtmIndNames.get(k));
/* 543 */               System.exit(0);
/*     */             }
/* 545 */             rInputWriter.write("," + ((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[0]);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 550 */       rInputWriter.write("," + mtm + "\n");
/* 551 */       rInputWriter.flush();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 555 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HashMap<String, DailyDataReader> getDailyReaderCollection()
/*     */   {
/* 566 */     return this.dailyReaderCollection;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, HashMap<String, HashMap<String, Double>>> getCorrelVals() {
/* 570 */     return this.correlVals;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/MLInputFileGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */