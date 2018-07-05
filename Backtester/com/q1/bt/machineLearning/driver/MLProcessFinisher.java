/*     */ package com.q1.bt.machineLearning.driver;
/*     */ 
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.machineLearning.absclasses.MLAlgo;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
/*     */ import com.q1.bt.machineLearning.utility.DailyDataReader;
/*     */ import com.q1.bt.machineLearning.utility.PostProcessDataWriter;
/*     */ import com.q1.bt.machineLearning.utility.TradeFilteredMTMWriter;
/*     */ import com.q1.bt.machineLearning.utility.TradeFilteredTDWriter;
/*     */ import com.q1.csv.CSVReader;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MLProcessFinisher
/*     */ {
/*     */   public void endProcess(boolean postProcess, HashMap<String, MLAlgo> algorithmMap, HashMap<String, TradeFilteredMTMWriter> mtmWriterCollection, HashMap<String, TradeFilteredTDWriter> tdWriterCollection, HashMap<String, PostProcessDataWriter> postProcessWriterCollection, HashMap<String, DailyDataReader> dailyReaderCollection, CSVWriter mlLogWriter, CSVWriter combinedMTM, CSVWriter correlLogWriter, String destPath, HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap)
/*     */     throws IOException
/*     */   {
/*  34 */     closeFiles(postProcess, algorithmMap, mtmWriterCollection, tdWriterCollection, postProcessWriterCollection, 
/*  35 */       dailyReaderCollection, mlLogWriter, combinedMTM, correlLogWriter);
/*     */     
/*  37 */     ArrayList<String> assetList = null;
/*     */     
/*  39 */     filterForTradeData(destPath, assetList, tdWriterCollection, modelSegmentWiseAssetUniverseMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void closeFiles(boolean postProcess, HashMap<String, MLAlgo> algorithmMap, HashMap<String, TradeFilteredMTMWriter> mtmWriterCollection, HashMap<String, TradeFilteredTDWriter> tdWriterCollection, HashMap<String, PostProcessDataWriter> postProcessWriterCollection, HashMap<String, DailyDataReader> dailyReaderCollection, CSVWriter mlLogWriter, CSVWriter combinedMTM, CSVWriter correlLogWriter)
/*     */     throws IOException
/*     */   {
/*  48 */     for (MLAlgo mlalgo : algorithmMap.values()) {
/*  49 */       mlalgo.close();
/*     */     }
/*  51 */     for (String assetName : mtmWriterCollection.keySet())
/*     */     {
/*  53 */       ((TradeFilteredMTMWriter)mtmWriterCollection.get(assetName)).close();
/*  54 */       ((TradeFilteredTDWriter)tdWriterCollection.get(assetName)).close();
/*  55 */       if (postProcess) {
/*  56 */         ((PostProcessDataWriter)postProcessWriterCollection.get(assetName)).close();
/*     */       }
/*     */     }
/*  59 */     mlLogWriter.close();
/*     */     try {
/*  61 */       correlLogWriter.close();
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/*  65 */     combinedMTM.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void filterForTradeData(String destPath, ArrayList<String> assetList, HashMap<String, TradeFilteredTDWriter> tdWriterCollection, HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap)
/*     */   {
/*  73 */     tdWriterCollection = new HashMap();
/*  74 */     String destFolderTD = destPath + "/Trade Data";
/*     */     
/*  76 */     String destFolderMTM = destPath + "/MTM Data";
/*     */     Iterator localIterator2;
/*  78 */     for (Iterator localIterator1 = modelSegmentWiseAssetUniverseMap.entrySet().iterator(); localIterator1.hasNext(); 
/*  79 */         localIterator2.hasNext())
/*     */     {
/*  78 */       Map.Entry<String, ArrayList<Asset>> entry = (Map.Entry)localIterator1.next();
/*  79 */       localIterator2 = ((ArrayList)entry.getValue()).iterator(); continue;Asset asset = (Asset)localIterator2.next();
/*  80 */       String strategyName = asset.getStrategyName();
/*  81 */       String scripListName = asset.getScripListName();
/*  82 */       String scripName = asset.getScrip().scripID;
/*  83 */       String tradeBookFile = scripName + " Tradebook.csv";
/*  84 */       String mtmFile = scripName + " MTM.csv";
/*  85 */       String strategyScripListFolder = strategyName + " " + scripListName;
/*  86 */       String assetName = asset.getAssetName();
/*  87 */       CSVReader cread = null;
/*     */       try
/*     */       {
/*  90 */         cread = new CSVReader(destFolderTD + "/" + strategyScripListFolder + "/" + tradeBookFile, ',', 0);
/*     */       } catch (IOException e) {
/*  92 */         cread = null;
/*  93 */         File folder = new File(destFolderTD + "/" + strategyScripListFolder);
/*  94 */         if (!folder.exists()) {
/*  95 */           folder.mkdirs();
/*     */         }
/*     */         try {
/*  98 */           CSVWriter cwrite = new CSVWriter(destFolderTD + "/" + strategyScripListFolder + "/" + tradeBookFile, 
/*  99 */             false, ",");
/* 100 */           cwrite.close();
/*     */         }
/*     */         catch (IOException e1) {
/* 103 */           e1.printStackTrace();
/*     */         }
/*     */       }
/*     */       try
/*     */       {
/* 108 */         if ((cread == null) || (cread.getLine() == null)) {
/* 109 */           File folder = new File(destFolderMTM + "/" + strategyScripListFolder);
/* 110 */           if (!folder.exists())
/* 111 */             folder.mkdirs();
/* 112 */           CSVWriter cwrite = new CSVWriter(destFolderMTM + "/" + strategyScripListFolder + "/" + mtmFile, 
/* 113 */             false, ",");
/* 114 */           cwrite.close();
/*     */         }
/*     */       } catch (IOException e) {
/* 117 */         System.out.println("Error in Processing empty Tradebooks " + assetName);
/*     */       }
/* 119 */       if (cread != null) {
/*     */         try {
/* 121 */           cread.close();
/*     */         } catch (IOException e) {
/* 123 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/MLProcessFinisher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */