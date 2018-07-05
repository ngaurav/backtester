/*     */ package com.q1.bt.machineLearning.driver;
/*     */ 
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.machineLearning.absclasses.MLAlgo;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.BinaryGenerator;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.CorrelLogWriter;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.OutputProcessor;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.ParameterWriter;
/*     */ import com.q1.bt.machineLearning.utility.DailyDataReader;
/*     */ import com.q1.bt.machineLearning.utility.MLFinalDecisionWriter;
/*     */ import com.q1.bt.machineLearning.utility.PostProcessDataWriter;
/*     */ import com.q1.bt.machineLearning.utility.TradeBookToOrderBookGenerator;
/*     */ import com.q1.bt.machineLearning.utility.TradeFilteredMTMWriter;
/*     */ import com.q1.bt.machineLearning.utility.TradeFilteredTDWriter;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.objects.MachineLearning;
/*     */ import com.q1.bt.process.parameter.MachineLearningParameter;
/*     */ import com.q1.csv.CSVReader;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import com.q1.math.MathLib;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
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
/*     */ public class MLPostProcessor
/*     */ {
/*     */   HashMap<String, TradeFilteredMTMWriter> mtmWriterCollection;
/*     */   HashMap<String, TradeFilteredTDWriter> tdWriterCollection;
/*     */   HashMap<String, PostProcessDataWriter> postProcessWriterCollection;
/*     */   HashMap<String, DailyDataReader> dailyReaderCollection;
/*     */   MLFinalDecisionWriter mlFinalDecisionWriter;
/*     */   TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals;
/*     */   CSVWriter mlLogWriter;
/*     */   CSVWriter combinedMTM;
/*     */   CorrelLogWriter correlLogWriter;
/*  50 */   HashMap<String, MLAlgo> algorithmMap = new HashMap();
/*     */   
/*     */   HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps;
/*     */   
/*     */   String mlPath;
/*     */   
/*  56 */   Long rwriteTS = Long.valueOf(0L);
/*  57 */   Boolean rcompleteReading = Boolean.valueOf(false);
/*     */   
/*  59 */   HashMap<Long, String> tsTradedSelectedScripsMap = new HashMap();
/*  60 */   HashMap<Long, String> tsTradedNotSelectedScripsMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private TreeMap<Long, HashMap<String, Double>> tradeMTMMat;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap;
/*     */   
/*     */ 
/*     */ 
/*     */   ArrayList<String> scripUniverse;
/*     */   
/*     */ 
/*     */ 
/*     */   MachineLearningParameter mlParameter;
/*     */   
/*     */ 
/*     */ 
/*     */   Backtest backtest;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MLPostProcessor(String sourcePath, String destPath, HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap, HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap, ArrayList<String> scripUniverse, String dataPath, Backtest backtest, MachineLearning machineLearning, ArrayList<Long> dateList, HashMap<String, DailyDataReader> dailyReaderCollection, TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals, HashMap<String, MLAlgo> algorithmMap, HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps, TreeMap<Long, HashMap<String, Double>> tradeMTMMat, String algoLastModifiedTimeStamp, boolean postProcess)
/*     */   {
/*  94 */     this.backtest = backtest;
/*     */     
/*     */ 
/*  97 */     this.mlParameter = machineLearning.getMlParameter();
/*     */     
/*  99 */     this.dailyReaderCollection = dailyReaderCollection;
/* 100 */     this.correlVals = correlVals;
/* 101 */     this.algorithmMap = algorithmMap;
/* 102 */     this.tradeStartEndMaps = tradeStartEndMaps;
/* 103 */     this.tradeMTMMat = tradeMTMMat;
/* 104 */     this.modelSegmentWiseAssetUniverseMap = modelSegmentWiseAssetUniverseMap;
/* 105 */     this.postModelSelectionSegmentWiseAssetUniverseMap = postModelSelectionSegmentWiseAssetUniverseMap;
/* 106 */     this.scripUniverse = scripUniverse;
/*     */     
/* 108 */     this.mlPath = (destPath + "/ML");
/*     */     try
/*     */     {
/* 111 */       initWritersForOutput(dataPath, sourcePath, destPath, 
/* 112 */         algoLastModifiedTimeStamp, postProcess, dateList);
/* 113 */       this.mlFinalDecisionWriter = new MLFinalDecisionWriter(this.mlPath);
/*     */     } catch (Exception e) {
/* 115 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void readOutput(String sourcePath, String destPath, boolean postProcess, ArrayList<Long> dateList) throws IOException
/*     */   {
/* 121 */     HashMap<String, Double> modelOutput = new HashMap();
/* 122 */     HashMap<String, Boolean> result = new HashMap();
/* 123 */     HashMap<String, CSVReader> outputReaderCollector = new HashMap();
/*     */     
/* 125 */     if (this.correlVals == null) {
/* 126 */       this.correlVals = correlRead(destPath);
/*     */     }
/*     */     else {
/* 129 */       this.correlLogWriter = new CorrelLogWriter(this.mlPath);
/* 130 */       this.correlLogWriter.writeCorrelLog(this.correlVals, this.scripUniverse);
/*     */     }
/*     */     
/* 133 */     this.rcompleteReading = Boolean.valueOf(false);
/*     */     
/* 135 */     OutputProcessor outputProcessor = new OutputProcessor();
/*     */     
/* 137 */     for (String assetPart : this.algorithmMap.keySet()) {
/* 138 */       String fileName = destPath + "/ML" + "/" + assetPart + 
/* 139 */         " Output.csv";
/*     */       try {
/* 141 */         System.out.println("processing " + fileName);
/* 142 */         CSVReader outputReader = new CSVReader(fileName, ',', 0);
/* 143 */         outputReaderCollector.put(assetPart, outputReader);
/*     */       } catch (IOException e) {
/* 145 */         System.out.println(fileName + " not found");
/*     */       }
/*     */     }
/*     */     
/* 149 */     BinaryGenerator binaryGenerator = new BinaryGenerator(
/* 150 */       this.tsTradedSelectedScripsMap, this.tsTradedNotSelectedScripsMap, 
/* 151 */       this.correlVals, this.tradeStartEndMaps);
/* 152 */     boolean firstCall = true;
/*     */     
/* 154 */     while (!this.rcompleteReading.booleanValue()) {
/* 155 */       modelOutput = outputProcessor.processOutput(outputReaderCollector);
/* 156 */       this.rcompleteReading = Boolean.valueOf(outputProcessor.isRcompleteReading());
/* 157 */       this.rwriteTS = outputProcessor.getRwriteTS();
/*     */       
/*     */ 
/* 160 */       result = binaryGenerator.generateBinary(modelOutput, 
/* 161 */         this.mlParameter.getSegmentCount().intValue(), 
/* 162 */         this.mlParameter.getOverallCount().intValue(), 
/* 163 */         this.mlParameter.getSegmentCorrelThreshold().doubleValue(), 
/* 164 */         this.mlParameter.getOverallCorrelThreshold().doubleValue(), this.rwriteTS.longValue(), 
/* 165 */         dateList, this.postModelSelectionSegmentWiseAssetUniverseMap, firstCall);
/* 166 */       firstCall = false;
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 171 */         this.mlFinalDecisionWriter.writeAndSaveInMemoryMLDecisions(result, this.rwriteTS);
/*     */         
/* 173 */         if (!this.rwriteTS.equals(Long.valueOf(99999999L))) {
/* 174 */           writeToFiles(result, this.rwriteTS, postProcess);
/*     */         }
/* 176 */         HashMap<String, Double> mtmMap = (HashMap)this.tradeMTMMat.get(this.rwriteTS);
/*     */         
/* 178 */         writeMLTradeLog(this.rwriteTS, result, mtmMap, modelOutput);
/*     */       }
/*     */       catch (Exception e) {
/* 181 */         System.out.println("ML Error in writing Output files for " + this.rwriteTS);
/* 182 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 187 */     this.mlFinalDecisionWriter.closeWriter();
/* 188 */     HashMap<String, TreeMap<Long, Boolean>> assetTimeStampDecisionMap = this.mlFinalDecisionWriter
/* 189 */       .getAssetTimeStampDecisionMap();
/* 190 */     TradeBookToOrderBookGenerator tradeBookToOrderBookGenerator = new TradeBookToOrderBookGenerator(
/* 191 */       sourcePath, destPath, assetTimeStampDecisionMap);
/* 192 */     tradeBookToOrderBookGenerator.generateOrderBooks();
/*     */     
/*     */ 
/*     */ 
/* 196 */     finalizeMTM(outputProcessor);
/*     */   }
/*     */   
/*     */ 
/*     */   private TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlRead(String destPath)
/*     */     throws IOException
/*     */   {
/* 203 */     String fileName = destPath + "/ML/DailyCorrelLog.csv";
/*     */     
/* 205 */     TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals = new TreeMap();
/*     */     
/* 207 */     CSVReader reader = new CSVReader(fileName, ',', 0);
/*     */     
/*     */ 
/*     */ 
/* 211 */     String[] header1 = reader.getLine();
/* 212 */     String[] header2 = reader.getLine();
/*     */     String[] curLine;
/* 214 */     while ((curLine = reader.getLine()) != null) {
/*     */       String[] curLine;
/* 216 */       Long date = Long.valueOf(Long.parseLong(curLine[0]));
/* 217 */       HashMap<String, HashMap<String, Double>> correlMap = new HashMap();
/*     */       
/*     */ 
/* 220 */       for (int i = 1; i < curLine.length; i++)
/*     */       {
/* 222 */         String scrip1 = header1[i];
/* 223 */         String scrip2 = header2[i];
/* 224 */         Double value = Double.valueOf(Double.parseDouble(curLine[i]));
/*     */         
/* 226 */         HashMap<String, Double> curMap = (HashMap)correlMap.get(scrip1);
/* 227 */         if (curMap == null) {
/* 228 */           curMap = new HashMap();
/* 229 */           curMap.put(scrip2, value);
/* 230 */           correlMap.put(scrip1, curMap);
/*     */         } else {
/* 232 */           curMap.put(scrip2, value);
/* 233 */           correlMap.put(scrip1, curMap);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 238 */       correlVals.put(date, correlMap);
/*     */     }
/*     */     
/* 241 */     reader.close();
/*     */     
/* 243 */     return correlVals;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writeMLTradeLog(Long resultDate, HashMap<String, Boolean> result, HashMap<String, Double> mtmList, HashMap<String, Double> modelOutput)
/*     */     throws IOException
/*     */   {
/* 255 */     String[] logData = new String[5];
/* 256 */     logData[0] = resultDate.toString();
/* 257 */     label235: for (Map.Entry<String, Boolean> entry : result.entrySet()) {
/* 258 */       String assetName = (String)entry.getKey();
/*     */       Double mtm;
/*     */       Double mtm;
/* 261 */       if (mtmList == null) {
/* 262 */         mtm = Double.valueOf(0.0D);
/* 263 */       } else { if (mtmList.get(assetName) == null) {
/*     */           continue;
/*     */         }
/* 266 */         mtm = (Double)mtmList.get(assetName);
/*     */       }
/* 268 */       logData[1] = assetName;
/* 269 */       if (result.get(assetName) != null)
/*     */       {
/* 271 */         if (!((Boolean)result.get(assetName)).booleanValue()) {
/* 272 */           logData[4] = "Not Selected";
/* 273 */         } else if (((Double)modelOutput.get(assetName)).equals(Double.valueOf(100.0D))) {
/* 274 */           logData[4] = "Running Trade";
/* 275 */         } else if (this.tsTradedSelectedScripsMap.get(resultDate) == null) {
/* 276 */           logData[4] = "Selected Not Traded";
/* 277 */         } else { if (MathLib.doubleCompare(mtm, Double.valueOf(0.0D)).intValue() == 0)
/*     */           {
/* 279 */             if (!((String)this.tsTradedSelectedScripsMap.get(resultDate)).contains(assetName)) {
/* 280 */               logData[4] = "Selected Not Traded";
/*     */               break label235; } }
/* 282 */           logData[4] = "Traded";
/*     */         }
/* 284 */         logData[2] = mtm.toString();
/* 285 */         logData[3] = ((Double)modelOutput.get(assetName)).toString();
/*     */         
/* 287 */         this.mlLogWriter.writeLine(logData);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writeToFiles(HashMap<String, Boolean> result, Long tradeTS, boolean postProcess)
/*     */     throws IOException
/*     */   {
/* 298 */     for (String assetName : result.keySet()) {
/* 299 */       TreeMap<Long, Long> tradeStartEndMap = (TreeMap)this.tradeStartEndMaps.get(assetName);
/*     */       
/* 301 */       if (result.get(assetName) != null) {
/* 302 */         int write = ((Boolean)result.get(assetName)).booleanValue() ? 1 : 0;
/* 303 */         Long endTS = (Long)tradeStartEndMap.get(tradeTS);
/*     */         
/* 305 */         ((TradeFilteredMTMWriter)this.mtmWriterCollection.get(assetName))
/* 306 */           .process(tradeTS, write);
/*     */         
/* 308 */         if (endTS != null)
/*     */         {
/*     */ 
/* 311 */           ((TradeFilteredTDWriter)this.tdWriterCollection.get(assetName)).process(tradeTS, write, assetName);
/*     */         }
/*     */         
/* 314 */         if (postProcess)
/*     */         {
/* 316 */           ((PostProcessDataWriter)this.postProcessWriterCollection.get(assetName)).process(tradeTS, write);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void finalizeMTM(OutputProcessor outputProcessor) {
/* 323 */     TreeMap<Long, Double> combinedDailyMTMMap = new TreeMap();
/* 324 */     HashMap<Long, Integer> assetCount = new HashMap();
/*     */     
/* 326 */     Iterator localIterator1 = this.modelSegmentWiseAssetUniverseMap.entrySet().iterator();
/*     */     Iterator localIterator2;
/* 325 */     for (; localIterator1.hasNext(); 
/*     */         
/*     */ 
/* 328 */         localIterator2.hasNext())
/*     */     {
/* 326 */       Map.Entry<String, ArrayList<Asset>> entry = (Map.Entry)localIterator1.next();
/* 327 */       String segment = (String)entry.getKey();
/* 328 */       localIterator2 = ((ArrayList)entry.getValue()).iterator(); continue;Asset asset = (Asset)localIterator2.next();
/* 329 */       String assetName = asset.getAssetName();
/* 330 */       if (this.mtmWriterCollection.get(assetName) != null)
/*     */       {
/* 332 */         TreeMap<Long, Double> assetDailyMTMMap = new TreeMap();
/*     */         
/* 334 */         Long firstOutputDate = 
/* 335 */           (Long)outputProcessor.getFirstOutputDateCollector().get(segment);
/*     */         try {
/* 337 */           assetDailyMTMMap = 
/* 338 */             ((TradeFilteredMTMWriter)this.mtmWriterCollection.get(assetName)).writeInFile(firstOutputDate);
/*     */         } catch (IOException e1) {
/* 340 */           System.err.println("Error Writing daily MTM for " + 
/* 341 */             assetName);
/* 342 */           e1.printStackTrace();
/*     */         }
/* 344 */         if (assetDailyMTMMap != null) {
/* 345 */           for (Long date : assetDailyMTMMap.keySet()) {
/* 346 */             if (combinedDailyMTMMap.get(date) == null) {
/* 347 */               combinedDailyMTMMap.put(date, 
/* 348 */                 (Double)assetDailyMTMMap.get(date));
/* 349 */               assetCount.put(date, Integer.valueOf(1));
/*     */             } else {
/* 351 */               int count = ((Integer)assetCount.get(date)).intValue();
/* 352 */               combinedDailyMTMMap
/* 353 */                 .put(date, 
/* 354 */                 Double.valueOf((((Double)combinedDailyMTMMap.get(date)).doubleValue() * 
/* 355 */                 count + 
/* 356 */                 ((Double)assetDailyMTMMap.get(date)).doubleValue()) / (
/* 357 */                 count + 1)));
/* 358 */               assetCount.put(date, Integer.valueOf(count + 1));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 364 */     for (Long date : combinedDailyMTMMap.keySet()) {
/*     */       try {
/* 366 */         this.combinedMTM.write(date.toString() + "," + 
/* 367 */           ((Double)combinedDailyMTMMap.get(date)).toString() + "\n");
/*     */       } catch (IOException e) {
/* 369 */         System.err.println("Error writing combined MTM file for " + 
/* 370 */           date);
/* 371 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initWritersForOutput(String dataPath, String sourcePath, String destPath, String algoLastModifiedTimeStamp, boolean postProcess, ArrayList<Long> dateList)
/*     */     throws Exception
/*     */   {
/* 382 */     this.mtmWriterCollection = new HashMap();
/* 383 */     this.tdWriterCollection = new HashMap();
/* 384 */     String scripName; if (postProcess) {
/* 385 */       this.postProcessWriterCollection = new HashMap();
/* 386 */       String postProcessDataSourceFolder = sourcePath + 
/* 387 */         "/Post Process Data";
/* 388 */       String postProcessDataDestinationFolder = destPath + 
/* 389 */         "/Post Process Data";
/* 390 */       new File(postProcessDataDestinationFolder).mkdirs();
/* 391 */       Iterator localIterator2; for (Iterator localIterator1 = this.modelSegmentWiseAssetUniverseMap.entrySet().iterator(); localIterator1.hasNext(); 
/* 392 */           localIterator2.hasNext())
/*     */       {
/* 391 */         Map.Entry<String, ArrayList<Asset>> entry = (Map.Entry)localIterator1.next();
/* 392 */         localIterator2 = ((ArrayList)entry.getValue()).iterator(); continue;Asset asset = (Asset)localIterator2.next();
/* 393 */         assetName = asset.getAssetName();
/* 394 */         String strategyName = asset.getStrategyName();
/* 395 */         scripName = asset.getScrip().scripID;
/* 396 */         String scripListName = asset.getScripListName();
/* 397 */         String strategyScripListPPDFolder = strategyName + " " + 
/* 398 */           scripListName;
/*     */         try
/*     */         {
/* 401 */           new File(postProcessDataDestinationFolder + "/" + strategyScripListPPDFolder).mkdirs();
/* 402 */           String postProcessDataFile = scripName + " Output.csv";
/* 403 */           PostProcessDataWriter assetPPD = new PostProcessDataWriter(
/* 404 */             postProcessDataSourceFolder + "/" + 
/* 405 */             strategyScripListPPDFolder + "/" + 
/* 406 */             postProcessDataFile, 
/* 407 */             postProcessDataDestinationFolder + "/" + 
/* 408 */             strategyScripListPPDFolder + "/" + 
/* 409 */             postProcessDataFile);
/* 410 */           this.postProcessWriterCollection.put(assetName, assetPPD);
/*     */         }
/*     */         catch (IOException e) {
/* 413 */           System.out.println("Error in processing file for " + 
/* 414 */             assetName);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 421 */     String srcFolderMTM = sourcePath + "/MTM Data";
/* 422 */     String srcFolderTD = sourcePath + "/Trade Data";
/*     */     
/* 424 */     String destFolderMTM = destPath + "/MTM Data";
/* 425 */     String destFolderTD = destPath + "/Trade Data";
/*     */     
/*     */ 
/* 428 */     new File(destFolderMTM).mkdirs();
/* 429 */     new File(destFolderTD).mkdirs();
/*     */     
/* 431 */     new File(destPath + "/Results").mkdirs();
/*     */     
/* 433 */     ParameterWriter parameterWriter = new ParameterWriter();
/* 434 */     parameterWriter.createParamDir(sourcePath, destPath, this.mlPath, 
/* 435 */       this.mlParameter, algoLastModifiedTimeStamp);
/*     */     
/*     */ 
/* 438 */     String assetName = this.modelSegmentWiseAssetUniverseMap.entrySet().iterator();
/* 437 */     for (; assetName.hasNext(); 
/*     */         
/* 439 */         scripName.hasNext())
/*     */     {
/* 438 */       Object entry = (Map.Entry)assetName.next();
/* 439 */       scripName = ((ArrayList)((Map.Entry)entry).getValue()).iterator(); continue;Asset asset = (Asset)scripName.next();
/* 440 */       String assetName = asset.getAssetName();
/*     */       try
/*     */       {
/* 443 */         String strategyName = asset.getStrategyName();
/* 444 */         String scripListName = asset.getScripListName();
/* 445 */         String scripName = asset.getScrip().scripID;
/* 446 */         String dataFileName = scripName
/* 447 */           .substring(0, scripName.length() - 3) + 
/* 448 */           " 1D.csv";
/* 449 */         String tradeBookFile = scripName + " Tradebook.csv";
/* 450 */         String mtmFile = scripName + " MTM.csv";
/* 451 */         String strategyScripListFolder = strategyName + " " + 
/* 452 */           scripListName;
/*     */         
/* 454 */         if (new File(srcFolderTD + "/" + strategyScripListFolder + 
/* 455 */           "/" + tradeBookFile).length() != 0L)
/*     */         {
/*     */ 
/*     */ 
/* 459 */           new File(destFolderMTM + "/" + strategyScripListFolder).mkdirs();
/* 460 */           new File(destFolderTD + "/" + strategyScripListFolder)
/* 461 */             .mkdirs();
/*     */           
/* 463 */           TradeFilteredMTMWriter assetMTMWriter = new TradeFilteredMTMWriter(
/* 464 */             dataPath + "/CC/" + dataFileName, srcFolderMTM + 
/* 465 */             "/" + strategyScripListFolder + "/" + 
/* 466 */             mtmFile, srcFolderTD + "/" + 
/* 467 */             strategyScripListFolder + "/" + 
/* 468 */             tradeBookFile, destFolderMTM + "/" + 
/* 469 */             strategyScripListFolder + "/" + mtmFile, 
/* 470 */             dateList);
/* 471 */           TradeFilteredTDWriter assetTDWriter = new TradeFilteredTDWriter(
/* 472 */             srcFolderTD + "/" + strategyScripListFolder + "/" + 
/* 473 */             tradeBookFile, destFolderTD + "/" + 
/* 474 */             strategyScripListFolder + "/" + 
/* 475 */             tradeBookFile);
/*     */           
/* 477 */           this.mtmWriterCollection.put(assetName, assetMTMWriter);
/* 478 */           this.tdWriterCollection.put(assetName, assetTDWriter);
/*     */         }
/*     */       } catch (IOException e) {
/* 481 */         System.out.println("Error in processing file for " + 
/* 482 */           assetName);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 489 */       this.mlLogWriter = new CSVWriter(this.mlPath + "\\DailyFilterLog" + ".csv", 
/* 490 */         false, ",");
/* 491 */       this.combinedMTM = new CSVWriter(this.mlPath + "\\combinedMTM" + ".csv", 
/* 492 */         false, ",");
/*     */     }
/*     */     catch (IOException e) {
/* 495 */       System.out.println("ML Error:Error in creating file for logging Daily filter results");
/* 496 */       throw new IOException();
/*     */     }
/*     */     
/* 499 */     this.mlLogWriter.write("Date,Asset,Initial MTM,Model Output, Decision\n");
/* 500 */     this.combinedMTM.write("Date,Avg MTM(Avg on ML Count)\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HashMap<String, TradeFilteredMTMWriter> getMtmWriterCollection()
/*     */   {
/* 510 */     return this.mtmWriterCollection;
/*     */   }
/*     */   
/*     */   public HashMap<String, TradeFilteredTDWriter> getTdWriterCollection() {
/* 514 */     return this.tdWriterCollection;
/*     */   }
/*     */   
/*     */   public HashMap<String, PostProcessDataWriter> getPostProcessWriterCOllection() {
/* 518 */     return this.postProcessWriterCollection;
/*     */   }
/*     */   
/*     */   public HashMap<String, DailyDataReader> getDailyReaderCollection() {
/* 522 */     return this.dailyReaderCollection;
/*     */   }
/*     */   
/*     */   public CSVWriter getMlLogWriter() {
/* 526 */     return this.mlLogWriter;
/*     */   }
/*     */   
/*     */   public CSVWriter getCombinedMTM() {
/* 530 */     return this.combinedMTM;
/*     */   }
/*     */   
/*     */   public CSVWriter getCorrelLogWriter() {
/* 534 */     return this.correlLogWriter.getCorrelLogWriter();
/*     */   }
/*     */   
/*     */   public HashMap<String, MLAlgo> getAlgorithmMap() {
/* 538 */     return this.algorithmMap;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/MLPostProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */