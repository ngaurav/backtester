/*     */ package com.q1.bt.machineLearning.driver;
/*     */ 
/*     */ import com.q1.bt.data.classes.Scrip;
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
/*     */ public class MLInputFileGenerator
/*     */ {
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
/*     */   MachineLearningParameter mlParameter;
/*     */   
/*     */   public MLInputFileGenerator(MachineLearningParameter mlParameter)
/*     */   {
/*  68 */     this.mlParameter = mlParameter;
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
/*  80 */       MetadataReader mdMap = new MetadataReader(sourcePath, dataPath, scripUniverse);
/*  81 */       mdScripwiseMap = mdMap.getMetadataMap();
/*     */     } catch (Exception e) { HashMap<String, TreeMap<Long, ArrayList<Long>>> mdScripwiseMap;
/*  83 */       mdScripwiseMap = new HashMap();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  88 */     File destDir = new File(destPath);
/*  89 */     if ((destDir.exists()) && (nextLayer))
/*     */     {
/*  91 */       for (int i = 1;; i++) {
/*  92 */         File tempDir = new File(destPath + "/Temp/Temp" + i);
/*  93 */         if (!tempDir.exists()) {
/*  94 */           tempDir.mkdirs();
/*  95 */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 100 */       sourcePath = destPath + "/Temp/Temp" + i;
/*     */       String[] arrayOfString1;
/* 102 */       int j = (arrayOfString1 = destDir.list()).length; for (int i = 0; i < j; i++) { String dirName = arrayOfString1[i];
/* 103 */         if (!dirName.contains("Temp")) {
/* 104 */           File dir = new File(destPath + "/" + dirName);
/* 105 */           if (dir.isFile()) {
/* 106 */             if (dir.renameTo(new File(destPath + "/Temp/Temp" + i + "/" + dirName))) {
/* 107 */               System.out.println(dirName + " moved successfully");
/*     */             } else
/* 109 */               System.out.println(dirName + " could not be moved");
/*     */           } else {
/* 111 */             File newDir = new File(destPath + "/Temp/Temp" + i + "/" + dirName);
/* 112 */             newDir.mkdirs();
/* 113 */             String[] arrayOfString2; int m = (arrayOfString2 = dir.list()).length; for (int k = 0; k < m; k++) { String fileName = arrayOfString2[k];
/* 114 */               File file = new File(destPath + "/" + dirName + "/" + fileName);
/* 115 */               if (file.renameTo(new File(destPath + "/Temp/Temp" + i + "/" + dirName + "/" + fileName))) {
/* 116 */                 System.out.println(fileName + " moved successfully");
/*     */               } else {
/* 118 */                 System.out.println(fileName + " could not be moved");
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 125 */     initializeML(segmentWiseAssetUniverseMap, scripUniverse, mdScripwiseMap, sourcePath, destPath, dataPath, 
/* 126 */       dailyStartDate);
/*     */     
/*     */ 
/* 129 */     Calendar startDate = new GregorianCalendar();
/* 130 */     Calendar endDate = new GregorianCalendar();
/*     */     
/* 132 */     SimpleDateFormat numericDate = new SimpleDateFormat("yyyyMMdd");
/* 133 */     SimpleDateFormat numericYear = new SimpleDateFormat("yyyy");
/*     */     try {
/* 135 */       startDate.setTime(numericDate.parse(dailyStartDate.toString()));
/* 136 */       endDate.setTime(numericDate.parse(((Long)dateList.get(dateList.size() - 1)).toString()));
/*     */     } catch (ParseException e1) {
/* 138 */       System.out.println("Error in parsing Start and End daily date");
/* 139 */       e1.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 146 */     this.mlParameter.getVarList().getVarNames();
/* 147 */     Long curYear = Long.valueOf(1770L);
/* 148 */     HashMap<String, ArrayList<Double[]>> priceIndVarsMap = new HashMap();
/* 149 */     HashMap<String, ArrayList<Double[]>> mtmIndVarsMap = new HashMap();
/*     */     
/* 151 */     endDate.add(5, 1);
/*     */     
/* 153 */     for (Calendar dt = startDate; dt.compareTo(endDate) <= 0; dt.add(5, 1))
/*     */     {
/*     */ 
/*     */ 
/* 157 */       priceIndVarsMap.clear();
/* 158 */       mtmIndVarsMap.clear();
/*     */       Long year;
/* 160 */       Long year; if (dt.compareTo(endDate) == 0) {
/* 161 */         Long date = Long.valueOf(99999999L);
/* 162 */         year = Long.valueOf(9999L);
/*     */       } else {
/* 164 */         date = Long.valueOf(Long.parseLong(numericDate.format(dt.getTime())));
/* 165 */         year = Long.valueOf(Long.parseLong(numericYear.format(dt.getTime())));
/*     */       }
/*     */       
/* 168 */       if (!curYear.equals(year)) {
/* 169 */         System.out.println("Processing Year: " + year);
/* 170 */         curYear = year;
/*     */       }
/*     */       
/*     */ 
/* 174 */       dailyUpdate(segmentWiseAssetUniverseMap, scripUniverse, (Long)date, stratTradePnL);
/*     */       
/* 176 */       Object tradePnLList = (HashMap)stratTradePnL.getTradeMTMMat().get(date);
/*     */       
/* 178 */       priceIndVarsMap.putAll(this.dailyPriceIndVars);
/* 179 */       mtmIndVarsMap.putAll(this.dailyMTMIndVars);
/* 180 */       Iterator localIterator2; for (Iterator localIterator1 = segmentWiseAssetUniverseMap.entrySet().iterator(); localIterator1.hasNext(); 
/*     */           
/*     */ 
/*     */ 
/* 184 */           localIterator2.hasNext())
/*     */       {
/* 180 */         Map.Entry<String, ArrayList<Asset>> entry = (Map.Entry)localIterator1.next();
/* 181 */         String segmentName = (String)entry.getKey();
/* 182 */         ArrayList<Asset> segmentAssetUniverse = (ArrayList)entry.getValue();
/*     */         
/* 184 */         localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
/* 185 */         String assetName = asset.getAssetName();
/* 186 */         if (tradeStartEndMaps.get(assetName) != null)
/*     */         {
/* 188 */           Double mtm = tradePnLList == null ? null : (Double)((HashMap)tradePnLList).get(assetName);
/*     */           
/* 190 */           if (dt.compareTo(endDate) == 0) {
/* 191 */             mtm = Double.valueOf(0.0D);
/*     */           }
/*     */           
/* 194 */           if (mtm != null) {
/* 195 */             String scripName = asset.getScrip().scripID;
/*     */             
/* 197 */             if ((this.priceIndNames.size() == 0) || (priceIndVarsMap.get(scripName) != null))
/*     */             {
/*     */ 
/* 200 */               if ((this.mtmIndNames.size() == 0) || (mtmIndVarsMap.get(assetName) != null))
/*     */               {
/*     */ 
/* 203 */                 CSVWriter rInputWriter = (CSVWriter)this.rInputWriterMap.get(segmentName);
/* 204 */                 String tradeEndDate = "";
/*     */                 
/* 206 */                 if (((TreeMap)tradeStartEndMaps.get(assetName)).get(date) == null) {
/* 207 */                   tradeEndDate = "99999999";
/*     */                 } else {
/* 209 */                   tradeEndDate = ((Long)((TreeMap)tradeStartEndMaps.get(assetName)).get(date)).toString();
/*     */                 }
/*     */                 
/*     */ 
/* 213 */                 Integer tradeSide = (Integer)((TreeMap)tradeStartDateTradeSideMaps.get(assetName)).get(date);
/*     */                 
/* 215 */                 if (tradeSide == null) {
/* 216 */                   tradeSide = Integer.valueOf(0);
/*     */                 }
/* 218 */                 if (((Long)date).equals(Long.valueOf(99999999L)))
/*     */                 {
/* 220 */                   writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(1), priceIndVarsMap, mtmIndVarsMap, scripName, mtm, true);
/* 221 */                   writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(-1), priceIndVarsMap, mtmIndVarsMap, scripName, mtm, true);
/*     */                 }
/*     */                 else
/*     */                 {
/* 225 */                   writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, tradeSide, priceIndVarsMap, mtmIndVarsMap, scripName, mtm, false);
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
/* 240 */     for (Object date = this.rInputWriterMap.values().iterator(); ((Iterator)date).hasNext();) { CSVWriter rInputWriter = (CSVWriter)((Iterator)date).next();
/* 241 */       rInputWriter.flush();
/* 242 */       rInputWriter.close();
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
/* 256 */     this.dailyMTMIndVars = new HashMap();
/* 257 */     this.dailyPriceIndVars = new HashMap();
/*     */     
/* 259 */     initDailyData(segmentWiseAssetUniverseMap);
/* 260 */     initCandleData(scripUniverse);
/*     */     
/* 262 */     initIndicators(segmentWiseAssetUniverseMap, scripUniverse, this.mlParameter.getFactorList(), 
/* 263 */       this.mlParameter.getVarList(), dataPath);
/* 264 */     initCorelMap(scripUniverse, this.mlParameter.getCorrelPeriod().intValue());
/*     */     
/* 266 */     this.dailyReaderCollection = initDailyreader(scripUniverse, dataPath, dailyStartDate);
/*     */     
/*     */ 
/* 269 */     initwriters(dataPath, segmentWiseAssetUniverseMap.keySet(), sourcePath, destPath);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void initCorelMap(ArrayList<String> scripList, int correlPeriod)
/*     */   {
/* 276 */     this.correlMap = new HashMap();
/* 277 */     this.correlVals = new TreeMap();
/* 278 */     for (int i = 0; i < scripList.size(); i++) {
/* 279 */       String scrip1 = (String)scripList.get(i);
/* 280 */       this.correlMap.put(scrip1, new HashMap());
/* 281 */       for (int j = 0; j < i; j++) {
/* 282 */         String scrip2 = (String)scripList.get(j);
/* 283 */         Correl corEle = new Correl(correlPeriod);
/* 284 */         ((HashMap)this.correlMap.get(scrip1)).put(scrip2, corEle);
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
/* 295 */     this.dailyPriceIndCollection = new HashMap();
/* 296 */     this.dailyMTMIndCollection = new HashMap();
/*     */     
/* 298 */     this.priceIndNames = new ArrayList();
/* 299 */     this.mtmIndNames = new ArrayList();
/*     */     
/*     */     Iterator localIterator2;
/* 302 */     for (Iterator localIterator1 = segmentWiseAssetUniverseMap.values().iterator(); localIterator1.hasNext(); 
/* 303 */         localIterator2.hasNext())
/*     */     {
/* 302 */       ArrayList<Asset> segmentAssetUniverse = (ArrayList)localIterator1.next();
/* 303 */       localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
/*     */       
/*     */ 
/* 306 */       DailyIndColl dailyMTMIndicator = new DailyIndColl(factorList, varListType, FactorType.MTM, 
/* 307 */         ValueType.Numerical);
/*     */       
/*     */ 
/* 310 */       this.dailyMTMIndCollection.put(asset.getAssetName(), dailyMTMIndicator);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 315 */     for (String scripName : scripList) {
/* 316 */       DailyIndColl dailyPriceIndicator = new DailyIndColl(factorList, varListType, FactorType.Price, 
/* 317 */         ValueType.Numerical);
/* 318 */       this.dailyPriceIndCollection.put(scripName, dailyPriceIndicator);
/*     */     }
/*     */     
/*     */ 
/* 322 */     DailyIndColl Price1DColl = (DailyIndColl)this.dailyPriceIndCollection.get(scripList.get(0));
/* 323 */     DailyIndColl MTM1DColl = 
/* 324 */       (DailyIndColl)this.dailyMTMIndCollection.get(((Asset)((ArrayList)segmentWiseAssetUniverseMap.values().iterator().next()).get(0)).getAssetName());
/*     */     
/* 326 */     for (Factor factor : Price1DColl.getFactorList()) {
/* 327 */       this.priceIndNames.add(factor.getName());
/*     */     }
/*     */     
/* 330 */     for (Factor factor : MTM1DColl.getFactorList()) {
/* 331 */       this.mtmIndNames.add(factor.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   private void initDailyData(HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap)
/*     */   {
/* 337 */     this.dailyDataCollection = new HashMap();
/* 338 */     Iterator localIterator2; for (Iterator localIterator1 = segmentWiseAssetUniverseMap.values().iterator(); localIterator1.hasNext(); 
/* 339 */         localIterator2.hasNext())
/*     */     {
/* 338 */       ArrayList<Asset> segmentAssetUniverse = (ArrayList)localIterator1.next();
/* 339 */       localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
/* 340 */       this.dailyDataCollection.put(asset.getAssetName(), 
/* 341 */         new DailyData(asset.getScrip().scripID, asset.getScrip().segmentName));
/*     */     }
/*     */   }
/*     */   
/*     */   private void initCandleData(ArrayList<String> scripList)
/*     */   {
/* 347 */     this.candleDataCollection = new HashMap();
/* 348 */     for (String scripName : scripList) {
/* 349 */       String[] scripNameParts = scripName.split(" ");
/* 350 */       this.candleDataCollection.put(scripName, new DailyData(scripNameParts[3], scripNameParts[1]));
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
/* 365 */     this.rInputWriterMap = new HashMap();
/* 366 */     for (String segmentName : segmentSet)
/*     */     {
/* 368 */       if (this.rInputWriterMap.get(segmentName) == null)
/*     */       {
/*     */ 
/* 371 */         CSVWriter inputWriter = new CSVWriter(destPath + "/ML/" + segmentName + " Input.csv", false, ",");
/*     */         
/*     */ 
/* 374 */         this.rInputWriterMap.put(segmentName, inputWriter);
/*     */         
/*     */ 
/* 377 */         inputWriter.write("Date,EndDate,Asset, TradeSide");
/*     */         
/* 379 */         for (int i = 0; i < this.priceIndNames.size(); i++) {
/* 380 */           inputWriter.write("," + (String)this.priceIndNames.get(i));
/*     */         }
/*     */         
/* 383 */         for (int i = 0; i < this.mtmIndNames.size(); i++)
/* 384 */           inputWriter.write("," + (String)this.mtmIndNames.get(i));
/* 385 */         inputWriter.write(",MTM\n");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static HashMap<String, DailyDataReader> initDailyreader(ArrayList<String> scripList, String dataPath, Long dailyStartDate)
/*     */     throws Exception
/*     */   {
/* 393 */     HashMap<String, DailyDataReader> dailyReaderCollection = new HashMap();
/* 394 */     for (String scripName : scripList)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 401 */       String scripNameNew = scripName.substring(0, scripName.length() - 3);
/* 402 */       String filePath = dataPath + "/CC/" + scripNameNew + " " + "1D.csv";
/*     */       try {
/* 404 */         dailyReaderCollection.put(scripName, new DailyDataReader(filePath, dailyStartDate));
/*     */       } catch (IOException e) {
/* 406 */         System.out.println("Error in reading Daily file for " + scripName);
/* 407 */         throw new Exception();
/*     */       }
/*     */     }
/*     */     
/* 411 */     return dailyReaderCollection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void dailyUpdate(HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap, ArrayList<String> scripUniverse, Long date, TradeAndMTMDataProcessor stratTradePnL)
/*     */     throws IOException
/*     */   {
/*     */     Iterator localIterator2;
/*     */     
/*     */ 
/* 422 */     for (Iterator localIterator1 = segmentWiseAssetUniverseMap.values().iterator(); localIterator1.hasNext(); 
/* 423 */         localIterator2.hasNext())
/*     */     {
/* 422 */       ArrayList<Asset> segmentAssetUniverse = (ArrayList)localIterator1.next();
/* 423 */       localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
/* 424 */       String assetName = asset.getAssetName();
/* 425 */       Long prevDate = ((DailyDataReader)this.dailyReaderCollection.get(asset.getScrip().scripID)).getPrevDate();
/* 426 */       ((DailyDataReader)this.dailyReaderCollection.get(asset.getScrip().scripID)).process(date, (DailyData)this.dailyDataCollection.get(assetName), 
/* 427 */         (Double)((TreeMap)stratTradePnL.getDailyMTMMap().get(asset.getAssetName())).get(prevDate));
/*     */       
/* 429 */       getDailyMTMIndValues(date, asset.getAssetName());
/*     */     }
/*     */     
/* 432 */     for (String scripName : scripUniverse)
/*     */     {
/* 434 */       ((DailyDataReader)this.dailyReaderCollection.get(scripName)).process(date, (CandleData)this.candleDataCollection.get(scripName));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 439 */     getDailyPriceIndValues(date, scripUniverse);
/* 440 */     updateCorrelMatrix(date, scripUniverse);
/*     */   }
/*     */   
/*     */   private void updateCorrelMatrix(Long date, ArrayList<String> scripList)
/*     */   {
/* 445 */     this.correlVals.put(date, new HashMap());
/* 446 */     for (int i = 0; i < scripList.size(); i++) {
/* 447 */       String scrip1 = (String)scripList.get(i);
/* 448 */       ((HashMap)this.correlVals.get(date)).put(scrip1, new HashMap());
/* 449 */       ((HashMap)((HashMap)this.correlVals.get(date)).get(scrip1)).put(scrip1, Double.valueOf(1.0D));
/* 450 */       for (int j = 0; j < i; j++) {
/* 451 */         String scrip2 = (String)scripList.get(j);
/* 452 */         ((Correl)((HashMap)this.correlMap.get(scrip1)).get(scrip2)).updateCorrel((CandleData)this.candleDataCollection.get(scrip1), 
/* 453 */           (CandleData)this.candleDataCollection.get(scrip2), date);
/* 454 */         Double correlVal = ((Correl)((HashMap)this.correlMap.get(scrip1)).get(scrip2)).getVal();
/* 455 */         ((HashMap)((HashMap)this.correlVals.get(date)).get(scrip1)).put(scrip2, correlVal);
/* 456 */         ((HashMap)((HashMap)this.correlVals.get(date)).get(scrip2)).put(scrip1, correlVal);
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
/* 470 */     DailyData[] dailyDataArray = { (DailyData)this.dailyDataCollection.get(assetName) };
/* 471 */     this.dailyMTMIndVars.put(assetName, ((DailyIndColl)this.dailyMTMIndCollection.get(assetName)).process(dailyDataArray, date));
/*     */   }
/*     */   
/*     */   private void getDailyPriceIndValues(Long date, ArrayList<String> scripList) throws IOException
/*     */   {
/* 476 */     for (String scripName : scripList) {
/* 477 */       CandleData[] candleDataArray = { (CandleData)this.candleDataCollection.get(scripName) };
/* 478 */       this.dailyPriceIndVars.put(scripName, ((DailyIndColl)this.dailyPriceIndCollection.get(scripName)).process(candleDataArray, date));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void writeLineInInputFile(CSVWriter rInputWriter, Long date, String tradeEndDate, String assetName, Integer tradeSide, HashMap<String, ArrayList<Double[]>> priceIndVarsMap, HashMap<String, ArrayList<Double[]>> mtmIndVarsMap, String scripName, Double mtm, boolean predictionDateFlag)
/*     */   {
/* 486 */     String tradeSideAssetName = "";
/*     */     
/* 488 */     if (tradeSide.intValue() == 1) {
/* 489 */       tradeSideAssetName = "LON#" + assetName;
/* 490 */     } else if (tradeSide.intValue() == -1) {
/* 491 */       tradeSideAssetName = "SHO#" + assetName;
/*     */     } else {
/* 493 */       tradeSideAssetName = "NON#" + assetName;
/*     */     }
/*     */     try {
/* 496 */       rInputWriter.write(date.toString());
/* 497 */       rInputWriter.write("," + tradeEndDate);
/*     */       
/* 499 */       if (predictionDateFlag) {
/* 500 */         rInputWriter.write("," + tradeSideAssetName);
/*     */       } else {
/* 502 */         rInputWriter.write("," + assetName);
/*     */       }
/* 504 */       rInputWriter.write("," + tradeSide);
/*     */       
/* 506 */       if (priceIndVarsMap.get(scripName) != null) {
/* 507 */         for (int k = 0; k < ((ArrayList)priceIndVarsMap.get(scripName)).size(); k++) {
/* 508 */           rInputWriter.write("," + ((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[((1 - tradeSide.intValue()) / 2)]);
/*     */         }
/*     */       }
/*     */       
/* 512 */       if (mtmIndVarsMap.get(assetName) != null) {
/* 513 */         for (int k = 0; k < ((ArrayList)mtmIndVarsMap.get(assetName)).size(); k++) {
/* 514 */           rInputWriter.write("," + ((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[((1 - tradeSide.intValue()) / 2)]);
/*     */         }
/*     */       }
/*     */       
/* 518 */       rInputWriter.write("," + mtm + "\n");
/* 519 */       rInputWriter.flush();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 523 */       e.printStackTrace();
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
/* 534 */     return this.dailyReaderCollection;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, HashMap<String, HashMap<String, Double>>> getCorrelVals() {
/* 538 */     return this.correlVals;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/MLInputFileGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */