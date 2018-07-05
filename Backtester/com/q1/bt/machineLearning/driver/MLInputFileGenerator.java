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
/*     */ 
/*     */ 
/*     */                   }
/* 236 */                   else if (tradeSide.equals(Integer.valueOf(0)))
/*     */                   {
/* 238 */                     writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(1), priceIndVarsMap, mtmIndVarsMap, scripName, mtm, false);
/* 239 */                     writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(-1), priceIndVarsMap, mtmIndVarsMap, scripName, mtm, false);
/*     */                   }
/*     */                   else
/*     */                   {
/* 243 */                     writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, tradeSide, priceIndVarsMap, mtmIndVarsMap, scripName, mtm, false);
/* 244 */                     writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(-tradeSide.intValue()), priceIndVarsMap, mtmIndVarsMap, scripName, Double.valueOf(0.0D), false);
/*     */                   }
/*     */                   
/*     */ 
/*     */                 }
/*     */                 else
/*     */                 {
/* 251 */                   writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(0), priceIndVarsMap, mtmIndVarsMap, scripName, mtm, false);
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
/* 267 */     for (Object date = this.rInputWriterMap.values().iterator(); ((Iterator)date).hasNext();) { CSVWriter rInputWriter = (CSVWriter)((Iterator)date).next();
/* 268 */       rInputWriter.flush();
/* 269 */       rInputWriter.close();
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
/* 283 */     this.dailyMTMIndVars = new HashMap();
/* 284 */     this.dailyPriceIndVars = new HashMap();
/*     */     
/* 286 */     initDailyData(segmentWiseAssetUniverseMap);
/* 287 */     initCandleData(scripUniverse);
/*     */     
/* 289 */     initIndicators(segmentWiseAssetUniverseMap, scripUniverse, this.mlParameter.getFactorList(), 
/* 290 */       this.mlParameter.getVarList(), dataPath);
/* 291 */     initCorelMap(scripUniverse, this.mlParameter.getCorrelPeriod().intValue());
/*     */     
/*     */ 
/* 294 */     initwriters(dataPath, segmentWiseAssetUniverseMap.keySet(), sourcePath, destPath);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void initCorelMap(ArrayList<String> scripList, int correlPeriod)
/*     */   {
/* 301 */     this.correlMap = new HashMap();
/* 302 */     this.correlVals = new TreeMap();
/* 303 */     for (int i = 0; i < scripList.size(); i++) {
/* 304 */       String scrip1 = (String)scripList.get(i);
/* 305 */       this.correlMap.put(scrip1, new HashMap());
/* 306 */       for (int j = 0; j < i; j++) {
/* 307 */         String scrip2 = (String)scripList.get(j);
/* 308 */         Correl corEle = new Correl(correlPeriod);
/* 309 */         ((HashMap)this.correlMap.get(scrip1)).put(scrip2, corEle);
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
/* 320 */     this.dailyPriceIndCollection = new HashMap();
/* 321 */     this.dailyMTMIndCollection = new HashMap();
/*     */     
/* 323 */     this.priceIndNames = new ArrayList();
/* 324 */     this.mtmIndNames = new ArrayList();
/*     */     
/*     */     Iterator localIterator2;
/* 327 */     for (Iterator localIterator1 = segmentWiseAssetUniverseMap.values().iterator(); localIterator1.hasNext(); 
/* 328 */         localIterator2.hasNext())
/*     */     {
/* 327 */       ArrayList<Asset> segmentAssetUniverse = (ArrayList)localIterator1.next();
/* 328 */       localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
/*     */       
/*     */ 
/* 331 */       DailyIndColl dailyMTMIndicator = new DailyIndColl(factorList, varListType, FactorType.MTM, 
/* 332 */         ValueType.Numerical);
/*     */       
/*     */ 
/* 335 */       this.dailyMTMIndCollection.put(asset.getAssetName(), dailyMTMIndicator);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 340 */     for (String scripName : scripList) {
/* 341 */       DailyIndColl dailyPriceIndicator = new DailyIndColl(factorList, varListType, FactorType.Price, 
/* 342 */         ValueType.Numerical);
/* 343 */       this.dailyPriceIndCollection.put(scripName, dailyPriceIndicator);
/*     */     }
/*     */     
/*     */ 
/* 347 */     DailyIndColl Price1DColl = (DailyIndColl)this.dailyPriceIndCollection.get(scripList.get(0));
/* 348 */     DailyIndColl MTM1DColl = 
/* 349 */       (DailyIndColl)this.dailyMTMIndCollection.get(((Asset)((ArrayList)segmentWiseAssetUniverseMap.values().iterator().next()).get(0)).getAssetName());
/*     */     
/* 351 */     for (Factor factor : Price1DColl.getFactorList()) {
/* 352 */       this.priceIndNames.add(factor.getName());
/*     */     }
/*     */     
/* 355 */     for (Factor factor : MTM1DColl.getFactorList()) {
/* 356 */       this.mtmIndNames.add(factor.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   private void initDailyData(HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap)
/*     */   {
/* 362 */     this.dailyDataCollection = new HashMap();
/* 363 */     Iterator localIterator2; for (Iterator localIterator1 = segmentWiseAssetUniverseMap.values().iterator(); localIterator1.hasNext(); 
/* 364 */         localIterator2.hasNext())
/*     */     {
/* 363 */       ArrayList<Asset> segmentAssetUniverse = (ArrayList)localIterator1.next();
/* 364 */       localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
/* 365 */       this.dailyDataCollection.put(asset.getAssetName(), 
/* 366 */         new DailyData(asset.getScrip().scripID, asset.getScrip().segmentName));
/*     */     }
/*     */   }
/*     */   
/*     */   private void initCandleData(ArrayList<String> scripList)
/*     */   {
/* 372 */     this.candleDataCollection = new HashMap();
/* 373 */     for (String scripName : scripList) {
/* 374 */       String[] scripNameParts = scripName.split(" ");
/* 375 */       this.candleDataCollection.put(scripName, new CandleData(scripNameParts[3], scripNameParts[1]));
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
/* 390 */     this.rInputWriterMap = new HashMap();
/* 391 */     for (String segmentName : segmentSet)
/*     */     {
/* 393 */       if (this.rInputWriterMap.get(segmentName) == null)
/*     */       {
/*     */ 
/* 396 */         CSVWriter inputWriter = new CSVWriter(destPath + "/ML/" + segmentName + " Input.csv", false, ",");
/*     */         
/*     */ 
/* 399 */         this.rInputWriterMap.put(segmentName, inputWriter);
/*     */         
/*     */ 
/* 402 */         inputWriter.write("Date,EndDate,Asset, TradeSide");
/*     */         
/* 404 */         for (int i = 0; i < this.priceIndNames.size(); i++) {
/* 405 */           inputWriter.write("," + (String)this.priceIndNames.get(i));
/*     */         }
/*     */         
/* 408 */         for (int i = 0; i < this.mtmIndNames.size(); i++)
/* 409 */           inputWriter.write("," + (String)this.mtmIndNames.get(i));
/* 410 */         inputWriter.write(",MTM\n");
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
/* 426 */     for (Iterator localIterator1 = segmentWiseAssetUniverseMap.values().iterator(); localIterator1.hasNext(); 
/* 427 */         localIterator2.hasNext())
/*     */     {
/* 426 */       ArrayList<Asset> segmentAssetUniverse = (ArrayList)localIterator1.next();
/* 427 */       localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
/* 428 */       String assetName = asset.getAssetName();
/* 429 */       Long prevDate = ((DailyDataReader)this.dailyReaderCollection.get(asset.getScrip().scripID)).getPrevDate();
/*     */       try {
/* 431 */         ((DailyDataReader)this.dailyReaderCollection.get(asset.getScrip().scripID)).process(date, (CandleData)this.dailyDataCollection.get(assetName), 
/* 432 */           (Double)((TreeMap)stratTradePnL.getDailyMTMMap().get(asset.getAssetName())).get(prevDate));
/*     */       }
/*     */       catch (Exception e) {
/* 435 */         System.err.println("Error in fetching Daily Data Reader for " + assetName);
/* 436 */         e.printStackTrace();
/*     */       }
/*     */       
/* 439 */       getDailyMTMIndValues(date, asset.getAssetName());
/*     */     }
/*     */     
/* 442 */     for (String scripName : scripUniverse) {
/*     */       try
/*     */       {
/* 445 */         ((DailyDataReader)this.dailyReaderCollection.get(scripName)).process(date, (CandleData)this.candleDataCollection.get(scripName));
/*     */       }
/*     */       catch (Exception e) {
/* 448 */         System.err.println("Error in fetching Daily Data Reader for " + scripName);
/* 449 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 455 */     getDailyPriceIndValues(date, scripUniverse);
/* 456 */     updateCorrelMatrix(date, scripUniverse);
/*     */   }
/*     */   
/*     */   private void updateCorrelMatrix(Long date, ArrayList<String> scripList)
/*     */   {
/* 461 */     this.correlVals.put(date, new HashMap());
/* 462 */     for (int i = 0; i < scripList.size(); i++) {
/* 463 */       String scrip1 = (String)scripList.get(i);
/* 464 */       ((HashMap)this.correlVals.get(date)).put(scrip1, new HashMap());
/* 465 */       ((HashMap)((HashMap)this.correlVals.get(date)).get(scrip1)).put(scrip1, Double.valueOf(1.0D));
/* 466 */       for (int j = 0; j < i; j++) {
/* 467 */         String scrip2 = (String)scripList.get(j);
/* 468 */         ((Correl)((HashMap)this.correlMap.get(scrip1)).get(scrip2)).updateCorrel((CandleData)this.candleDataCollection.get(scrip1), 
/* 469 */           (CandleData)this.candleDataCollection.get(scrip2), date);
/* 470 */         Double correlVal = ((Correl)((HashMap)this.correlMap.get(scrip1)).get(scrip2)).getVal();
/* 471 */         ((HashMap)((HashMap)this.correlVals.get(date)).get(scrip1)).put(scrip2, correlVal);
/* 472 */         ((HashMap)((HashMap)this.correlVals.get(date)).get(scrip2)).put(scrip1, correlVal);
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
/* 486 */     DailyData[] dailyDataArray = { (DailyData)this.dailyDataCollection.get(assetName) };
/* 487 */     this.dailyMTMIndVars.put(assetName, ((DailyIndColl)this.dailyMTMIndCollection.get(assetName)).process(dailyDataArray, date));
/*     */   }
/*     */   
/*     */   private void getDailyPriceIndValues(Long date, ArrayList<String> scripList) throws IOException
/*     */   {
/* 492 */     for (String scripName : scripList) {
/* 493 */       CandleData[] candleDataArray = { (CandleData)this.candleDataCollection.get(scripName) };
/* 494 */       this.dailyPriceIndVars.put(scripName, ((DailyIndColl)this.dailyPriceIndCollection.get(scripName)).process(candleDataArray, date));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void writeLineInInputFile(CSVWriter rInputWriter, Long date, String tradeEndDate, String assetName, Integer tradeSide, HashMap<String, ArrayList<Double[]>> priceIndVarsMap, HashMap<String, ArrayList<Double[]>> mtmIndVarsMap, String scripName, Double mtm, boolean predictionDateFlag)
/*     */   {
/* 502 */     String tradeSideAssetName = "";
/*     */     
/* 504 */     if (this.bias)
/*     */     {
/* 506 */       if (tradeSide.intValue() == 1) {
/* 507 */         tradeSideAssetName = "LON#" + assetName;
/* 508 */       } else if (tradeSide.intValue() == -1) {
/* 509 */         tradeSideAssetName = "SHO#" + assetName;
/*     */       } else {
/* 511 */         tradeSideAssetName = "NON#" + assetName;
/*     */       }
/*     */     }
/*     */     else {
/* 515 */       tradeSideAssetName = assetName;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 520 */       rInputWriter.write(date.toString());
/* 521 */       rInputWriter.write("," + tradeEndDate);
/* 522 */       rInputWriter.write("," + tradeSideAssetName);
/* 523 */       rInputWriter.write("," + tradeSide);
/*     */       
/* 525 */       if (priceIndVarsMap.get(scripName) != null) {
/* 526 */         for (int k = 0; k < ((ArrayList)priceIndVarsMap.get(scripName)).size(); k++) {
/* 527 */           if (this.bias) {
/* 528 */             rInputWriter.write("," + ((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[((1 - tradeSide.intValue()) / 2)]);
/*     */           }
/*     */           else {
/* 531 */             if (!((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[0].equals(((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[1]))
/*     */             {
/* 533 */               System.err.println("Getting different values in unbiased mode.");
/* 534 */               System.err.println("Check definition of :" + (String)this.priceIndNames.get(k));
/* 535 */               System.exit(0);
/*     */             }
/* 537 */             rInputWriter.write("," + ((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[0]);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 542 */       if (mtmIndVarsMap.get(assetName) != null) {
/* 543 */         for (int k = 0; k < ((ArrayList)mtmIndVarsMap.get(assetName)).size(); k++) {
/* 544 */           if (this.bias) {
/* 545 */             rInputWriter.write("," + ((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[((1 - tradeSide.intValue()) / 2)]);
/*     */           }
/*     */           else {
/* 548 */             if (!((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[0].equals(((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[1]))
/*     */             {
/* 550 */               System.err.println("Getting different values in unbiased mode.");
/* 551 */               System.err.println("Check definition of :" + (String)this.mtmIndNames.get(k));
/* 552 */               System.exit(0);
/*     */             }
/* 554 */             rInputWriter.write("," + ((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[0]);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 559 */       rInputWriter.write("," + mtm + "\n");
/* 560 */       rInputWriter.flush();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 564 */       e.printStackTrace();
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
/* 575 */     return this.dailyReaderCollection;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, HashMap<String, HashMap<String, Double>>> getCorrelVals() {
/* 579 */     return this.correlVals;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/MLInputFileGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */