/*     */ package com.q1.bt.machineLearning.utility;
/*     */ 
/*     */ import com.q1.bt.postprocess.TradebookProcessor;
/*     */ import com.q1.csv.CSVReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class TradeAndMTMDataProcessor
/*     */ {
/*     */   String sourcePath;
/*     */   TreeMap<Long, HashMap<String, Double>> dateAssetTradePnLMap;
/*     */   HashMap<String, TreeMap<Long, Double>> dailyMTMMap;
/*     */   HashMap<String, TreeMap<Long, Long>> assetStartDateEndDateMaps;
/*     */   HashMap<String, TreeMap<Long, Integer>> assetStartDateTradeSideMaps;
/*     */   
/*     */   public TradeAndMTMDataProcessor(String sourcePath)
/*     */   {
/*  23 */     this.sourcePath = sourcePath;
/*     */     
/*  25 */     this.dateAssetTradePnLMap = new TreeMap();
/*  26 */     this.dailyMTMMap = new HashMap();
/*  27 */     this.assetStartDateEndDateMaps = new HashMap();
/*  28 */     this.assetStartDateTradeSideMaps = new HashMap();
/*     */   }
/*     */   
/*     */   public void processTradeBooksAndMTMs() throws Exception
/*     */   {
/*  33 */     HashMap<String, TreeMap<Long, Double>> assetDateTimeTradePnLMap = processTradeBooks();
/*  34 */     HashMap<String, ArrayList<Long>> assetDateListMap = processMTMFiles();
/*     */     
/*  36 */     HashMap<String, TreeMap<Long, Double>> assetDateTradePnLMaps = extractDailyTradePnLMapsSum(
/*  37 */       assetDateTimeTradePnLMap, assetDateListMap);
/*     */     
/*     */ 
/*  40 */     this.dateAssetTradePnLMap = combineMTMmaps(assetDateTradePnLMaps);
/*     */   }
/*     */   
/*     */   private HashMap<String, TreeMap<Long, Double>> processTradeBooks()
/*     */     throws Exception
/*     */   {
/*  46 */     ArrayList<File> tradeFiles = generateTradeFileList(this.sourcePath);
/*     */     
/*  48 */     HashMap<String, TreeMap<Long, Double>> assetDateTimeTradePnLMap = new HashMap();
/*     */     
/*     */ 
/*  51 */     for (File tradeFile : tradeFiles)
/*     */     {
/*  53 */       if (tradeFile.length() != 0L)
/*     */       {
/*     */ 
/*  56 */         String assetName = getAssetNameFromFile(tradeFile);
/*     */         
/*  58 */         ArrayList<TreeMap> tradeFileProcessingOutput = getDateTimeTradePnLAndStartEndDateMap(tradeFile);
/*     */         
/*  60 */         TreeMap<Long, Double> dateTimeTradePnLMap = (TreeMap)tradeFileProcessingOutput.get(0);
/*  61 */         assetDateTimeTradePnLMap.put(assetName, dateTimeTradePnLMap);
/*     */         
/*  63 */         TreeMap<Long, Long> tradeStartEndMap = (TreeMap)tradeFileProcessingOutput.get(1);
/*  64 */         this.assetStartDateEndDateMaps.put(assetName, tradeStartEndMap);
/*     */         
/*  66 */         TreeMap<Long, Integer> tradeStartDateTradeSideMap = (TreeMap)tradeFileProcessingOutput.get(2);
/*  67 */         this.assetStartDateTradeSideMaps.put(assetName, tradeStartDateTradeSideMap);
/*     */       }
/*     */     }
/*     */     
/*  71 */     return assetDateTimeTradePnLMap;
/*     */   }
/*     */   
/*     */   private HashMap<String, ArrayList<Long>> processMTMFiles()
/*     */     throws IOException
/*     */   {
/*  77 */     ArrayList<File> mtmFiles = generateMTMFileList(this.sourcePath);
/*  78 */     HashMap<String, ArrayList<Long>> assetMTMDateListMap = new HashMap();
/*     */     
/*  80 */     for (File mtmFile : mtmFiles)
/*     */     {
/*  82 */       String assetName = getAssetNameFromFile(mtmFile);
/*  83 */       ArrayList<Long> mtmDateList = new ArrayList();
/*     */       try
/*     */       {
/*  86 */         CSVReader reader = new CSVReader(mtmFile.getAbsolutePath(), ',', 0);
/*  87 */         TreeMap<Long, Double> mtmMap = new TreeMap();
/*     */         
/*     */         String[] line;
/*  90 */         while ((line = reader.getLine()) != null) { String[] line;
/*  91 */           mtmMap.put(Long.valueOf(Long.parseLong(line[0])), Double.valueOf(Double.parseDouble(line[1])));
/*  92 */           mtmDateList.add(Long.valueOf(Long.parseLong(line[0])));
/*     */         }
/*  94 */         this.dailyMTMMap.put(assetName, mtmMap);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/*  98 */         e.printStackTrace();
/*     */       }
/* 100 */       assetMTMDateListMap.put(assetName, mtmDateList);
/*     */     }
/*     */     
/* 103 */     return assetMTMDateListMap;
/*     */   }
/*     */   
/*     */   private String getAssetNameFromFile(File file) throws IOException
/*     */   {
/* 108 */     String strategyScripListName = file.getParentFile().getName();
/* 109 */     String fileName = file.getName();
/*     */     
/* 111 */     String[] fileNameParts = fileName.split(" ");
/* 112 */     String scripName = fileNameParts[0] + " " + fileNameParts[1] + " " + fileNameParts[2] + " " + fileNameParts[3] + 
/* 113 */       " " + fileNameParts[4];
/*     */     
/* 115 */     return strategyScripListName + " " + scripName;
/*     */   }
/*     */   
/*     */   private ArrayList<File> generateTradeFileList(String sourcePath)
/*     */   {
/* 120 */     ArrayList<File> tradeFiles = new ArrayList();
/*     */     
/* 122 */     File tradebookDirectory = new File(sourcePath + "/Trade Data");
/* 123 */     File[] scripListTradeBookDirectories = tradebookDirectory.listFiles();
/*     */     File[] arrayOfFile1;
/* 125 */     int j = (arrayOfFile1 = scripListTradeBookDirectories).length; for (int i = 0; i < j; i++) { File scripListTradeBookDirectory = arrayOfFile1[i];
/* 126 */       tradeFiles.addAll(Arrays.asList(scripListTradeBookDirectory.listFiles()));
/*     */     }
/*     */     
/* 129 */     return tradeFiles;
/*     */   }
/*     */   
/*     */   private ArrayList<File> generateMTMFileList(String sourcePath)
/*     */   {
/* 134 */     ArrayList<File> mtmFiles = new ArrayList();
/*     */     
/* 136 */     File mtmDirectory = new File(sourcePath + "/MTM Data");
/* 137 */     File[] scripListMTMDirectories = mtmDirectory.listFiles();
/*     */     File[] arrayOfFile1;
/* 139 */     int j = (arrayOfFile1 = scripListMTMDirectories).length; for (int i = 0; i < j; i++) { File scripListMTMDirectory = arrayOfFile1[i];
/* 140 */       mtmFiles.addAll(Arrays.asList(scripListMTMDirectory.listFiles()));
/*     */     }
/*     */     
/* 143 */     return mtmFiles;
/*     */   }
/*     */   
/*     */ 
/*     */   private void insertNonTradeDates(TreeMap<Long, Double> singleStratScripTradePnLTimeStampMap, ArrayList<Long> mtmTimeStampList)
/*     */   {
/* 149 */     for (Long timeStamp : mtmTimeStampList) {
/* 150 */       if (singleStratScripTradePnLTimeStampMap.get(timeStamp) == null) {
/* 151 */         singleStratScripTradePnLTimeStampMap.put(timeStamp, Double.valueOf(0.0D));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private HashMap<String, TreeMap<Long, Double>> extractDailyTradePnLMapsSum(HashMap<String, TreeMap<Long, Double>> tradePnLDateTimeMaps, HashMap<String, ArrayList<Long>> assetMTMDateListMap)
/*     */   {
/* 159 */     HashMap<String, TreeMap<Long, Double>> assetDateTradePnLMap = new HashMap();
/*     */     
/* 161 */     for (Map.Entry<String, TreeMap<Long, Double>> entry : tradePnLDateTimeMaps.entrySet())
/*     */     {
/* 163 */       String assetName = (String)entry.getKey();
/* 164 */       TreeMap<Long, Double> singleStratScripTradePnLDateTimeMap = (TreeMap)entry.getValue();
/*     */       
/* 166 */       TreeMap<Long, Double> singleStratScripTradePnLDateMap = new TreeMap();
/*     */       
/* 168 */       for (Long dateTime : singleStratScripTradePnLDateTimeMap.keySet())
/*     */       {
/* 170 */         Long timeStamp = Long.valueOf(dateTime.longValue() / 1000000L);
/*     */         
/* 172 */         if (singleStratScripTradePnLDateMap.get(timeStamp) == null) {
/* 173 */           singleStratScripTradePnLDateMap.put(timeStamp, (Double)singleStratScripTradePnLDateTimeMap.get(dateTime));
/*     */         } else {
/* 175 */           double prevVal = ((Double)singleStratScripTradePnLDateMap.get(timeStamp)).doubleValue();
/* 176 */           double newVal = prevVal + ((Double)singleStratScripTradePnLDateTimeMap.get(dateTime)).doubleValue();
/*     */           
/* 178 */           singleStratScripTradePnLDateMap.put(timeStamp, Double.valueOf(newVal));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 183 */       insertNonTradeDates(singleStratScripTradePnLDateMap, (ArrayList)assetMTMDateListMap.get(assetName));
/*     */       
/* 185 */       assetDateTradePnLMap.put(assetName, singleStratScripTradePnLDateMap);
/*     */     }
/*     */     
/* 188 */     return assetDateTradePnLMap;
/*     */   }
/*     */   
/*     */   private ArrayList<TreeMap> getDateTimeTradePnLAndStartEndDateMap(File tradeFile)
/*     */     throws Exception
/*     */   {
/* 194 */     TradebookProcessor tradebookProcessor = new TradebookProcessor(tradeFile.getCanonicalPath().replace("\\", "/"));
/* 195 */     TreeMap<Long, Double> tradePnLMap = tradebookProcessor.getTradeStartDateTimeMTMMap();
/* 196 */     TreeMap<Long, Long> startDateEndDateMap = tradebookProcessor.getTradeStartEndDateMap();
/* 197 */     TreeMap<Long, Integer> startDateTradeSideMap = tradebookProcessor.getTradeStartDateTradeSideMap();
/*     */     
/* 199 */     ArrayList<TreeMap> output = new ArrayList();
/* 200 */     output.add(tradePnLMap);
/* 201 */     output.add(startDateEndDateMap);
/* 202 */     output.add(startDateTradeSideMap);
/*     */     
/* 204 */     return output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private TreeMap<Long, HashMap<String, Double>> combineMTMmaps(HashMap<String, TreeMap<Long, Double>> mtmMaps)
/*     */   {
/* 211 */     TreeMap<Long, HashMap<String, Double>> consolMTM = new TreeMap();
/*     */     
/* 213 */     for (Map.Entry<String, TreeMap<Long, Double>> entry : mtmMaps.entrySet())
/*     */     {
/* 215 */       TreeMap<Long, Double> mtmMap = (TreeMap)entry.getValue();
/*     */       
/*     */ 
/* 218 */       if (mtmMap.size() != 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 223 */         for (Map.Entry<Long, Double> mtmEntry : mtmMap.entrySet()) {
/* 224 */           Long dateTime = (Long)mtmEntry.getKey();
/* 225 */           Double mtm = (Double)mtmEntry.getValue();
/*     */           try
/*     */           {
/* 228 */             HashMap<String, Double> curMTM = (HashMap)consolMTM.get(dateTime);
/*     */             
/* 230 */             if (curMTM == null) {
/* 231 */               curMTM = new HashMap();
/*     */             }
/*     */             
/* 234 */             curMTM.put((String)entry.getKey(), mtm);
/* 235 */             consolMTM.put(dateTime, curMTM);
/*     */           }
/*     */           catch (Exception e) {
/* 238 */             System.out.println("Error while making consol MTM Array");
/* 239 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 244 */     return consolMTM;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, HashMap<String, Double>> getTradeMTMMat() {
/* 248 */     return this.dateAssetTradePnLMap;
/*     */   }
/*     */   
/*     */   public HashMap<String, TreeMap<Long, Long>> getTradeStartEndMaps() {
/* 252 */     return this.assetStartDateEndDateMaps;
/*     */   }
/*     */   
/*     */   public HashMap<String, TreeMap<Long, Double>> getDailyMTMMap() {
/* 256 */     return this.dailyMTMMap;
/*     */   }
/*     */   
/*     */   public HashMap<String, TreeMap<Long, Integer>> getAssetStartDateTradeSideMaps() {
/* 260 */     return this.assetStartDateTradeSideMaps;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/TradeAndMTMDataProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */