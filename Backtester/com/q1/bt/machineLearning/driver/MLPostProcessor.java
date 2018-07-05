/*     */ package com.q1.bt.machineLearning.driver;
/*     */ 
/*     */ import com.q1.bt.machineLearning.absclasses.MLAlgo;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.BinaryGenerator;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.CorrelLogWriter;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.OutputProcessor;
/*     */ import com.q1.bt.machineLearning.utility.DailyDataReader;
/*     */ import com.q1.bt.machineLearning.utility.MLFinalDecisionWriter;
/*     */ import com.q1.bt.machineLearning.utility.TradeBookToOrderBookGenerator;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.objects.MachineLearning;
/*     */ import com.q1.bt.process.parameter.MachineLearningParameter;
/*     */ import com.q1.csv.CSVReader;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import com.q1.math.MathLib;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MLPostProcessor
/*     */ {
/*     */   HashMap<String, DailyDataReader> dailyReaderCollection;
/*     */   MLFinalDecisionWriter mlFinalDecisionWriter;
/*     */   TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals;
/*     */   CSVWriter mlLogWriter;
/*     */   CorrelLogWriter correlLogWriter;
/*  40 */   HashMap<String, MLAlgo> algorithmMap = new HashMap();
/*     */   
/*     */   HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps;
/*     */   
/*     */   HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMap;
/*     */   
/*     */   String mlPath;
/*  47 */   Long rwriteTS = Long.valueOf(0L);
/*  48 */   Boolean rcompleteReading = Boolean.valueOf(false);
/*     */   
/*  50 */   HashMap<Long, String> tsTradedSelectedScripsMap = new HashMap();
/*  51 */   HashMap<Long, String> tsTradedNotSelectedScripsMap = new HashMap();
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
/*     */   public boolean bias;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MLPostProcessor(String sourcePath, String destPath, HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap, HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap, ArrayList<String> scripUniverse, String dataPath, Backtest backtest, MachineLearning machineLearning, ArrayList<Long> dateList, HashMap<String, DailyDataReader> dailyReaderCollection, TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals, HashMap<String, MLAlgo> algorithmMap, HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps, HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMap, TreeMap<Long, HashMap<String, Double>> tradeMTMMat, String algoLastModifiedTimeStamp, boolean postProcess, boolean bias)
/*     */   {
/*  88 */     this.backtest = backtest;
/*     */     
/*     */ 
/*  91 */     this.mlParameter = machineLearning.getMlParameter();
/*     */     
/*  93 */     this.dailyReaderCollection = dailyReaderCollection;
/*  94 */     this.correlVals = correlVals;
/*  95 */     this.algorithmMap = algorithmMap;
/*  96 */     this.tradeStartEndMaps = tradeStartEndMaps;
/*  97 */     this.tradeStartDateTradeSideMap = tradeStartDateTradeSideMap;
/*  98 */     this.tradeMTMMat = tradeMTMMat;
/*  99 */     this.modelSegmentWiseAssetUniverseMap = modelSegmentWiseAssetUniverseMap;
/* 100 */     this.postModelSelectionSegmentWiseAssetUniverseMap = postModelSelectionSegmentWiseAssetUniverseMap;
/* 101 */     this.scripUniverse = scripUniverse;
/* 102 */     this.bias = bias;
/*     */     
/* 104 */     this.mlPath = (destPath + "/ML");
/*     */     try
/*     */     {
/* 107 */       initMLLogWriter();
/* 108 */       this.mlFinalDecisionWriter = new MLFinalDecisionWriter(this.mlPath);
/*     */     } catch (Exception e) {
/* 110 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void readOutput(String sourcePath, String destPath, boolean postProcess, ArrayList<Long> dateList) throws IOException
/*     */   {
/* 116 */     HashMap<String, Double> modelOutput = new HashMap();
/* 117 */     HashMap<String, Boolean> result = new HashMap();
/* 118 */     HashMap<String, CSVReader> outputReaderCollector = new HashMap();
/*     */     
/* 120 */     if (this.correlVals == null) {
/* 121 */       this.correlVals = correlRead(destPath);
/*     */     }
/*     */     else {
/* 124 */       this.correlLogWriter = new CorrelLogWriter(this.mlPath);
/* 125 */       this.correlLogWriter.writeCorrelLog(this.correlVals, this.scripUniverse);
/*     */     }
/*     */     
/* 128 */     this.rcompleteReading = Boolean.valueOf(false);
/*     */     
/* 130 */     OutputProcessor outputProcessor = new OutputProcessor();
/*     */     
/* 132 */     for (String assetPart : this.algorithmMap.keySet()) {
/* 133 */       String fileName = destPath + "/ML" + "/" + assetPart + 
/* 134 */         " Output.csv";
/*     */       try {
/* 136 */         System.out.println("processing " + fileName);
/* 137 */         CSVReader outputReader = new CSVReader(fileName, ',', 0);
/* 138 */         outputReaderCollector.put(assetPart, outputReader);
/*     */       } catch (IOException e) {
/* 140 */         System.out.println(fileName + " not found");
/*     */       }
/*     */     }
/*     */     
/* 144 */     BinaryGenerator binaryGenerator = new BinaryGenerator(
/* 145 */       this.tsTradedSelectedScripsMap, this.tsTradedNotSelectedScripsMap, 
/* 146 */       this.correlVals, this.tradeStartEndMaps, this.tradeStartDateTradeSideMap, this.bias);
/* 147 */     boolean firstCall = true;
/*     */     
/* 149 */     while (!this.rcompleteReading.booleanValue()) {
/* 150 */       modelOutput = outputProcessor.processOutput(outputReaderCollector);
/* 151 */       this.rcompleteReading = Boolean.valueOf(outputProcessor.isRcompleteReading());
/* 152 */       this.rwriteTS = outputProcessor.getRwriteTS();
/*     */       
/*     */ 
/* 155 */       result = binaryGenerator.generateBinary(modelOutput, 
/* 156 */         this.mlParameter.getSegmentCount().intValue(), 
/* 157 */         this.mlParameter.getOverallCount().intValue(), 
/* 158 */         this.mlParameter.getSegmentCorrelThreshold().doubleValue(), 
/* 159 */         this.mlParameter.getOverallCorrelThreshold().doubleValue(), this.rwriteTS.longValue(), 
/* 160 */         dateList, this.postModelSelectionSegmentWiseAssetUniverseMap, firstCall);
/* 161 */       firstCall = false;
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 166 */         this.mlFinalDecisionWriter.writeAndSaveInMemoryMLDecisions(result, this.rwriteTS);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 171 */         HashMap<String, Double> mtmMap = (HashMap)this.tradeMTMMat.get(this.rwriteTS);
/*     */         
/* 173 */         writeMLTradeLog(this.rwriteTS, result, mtmMap, modelOutput);
/*     */       }
/*     */       catch (Exception e) {
/* 176 */         System.out.println("ML Error in writing Output files for " + this.rwriteTS);
/* 177 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 182 */     this.mlFinalDecisionWriter.closeWriter();
/* 183 */     HashMap<String, TreeMap<Long, Boolean>> assetTimeStampDecisionMap = this.mlFinalDecisionWriter.getAssetTimeStampDecisionMap();
/* 184 */     TradeBookToOrderBookGenerator tradeBookToOrderBookGenerator = 
/* 185 */       new TradeBookToOrderBookGenerator(sourcePath, destPath, assetTimeStampDecisionMap, this.bias);
/* 186 */     tradeBookToOrderBookGenerator.generateOrderBooks();
/*     */   }
/*     */   
/*     */   private TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlRead(String destPath)
/*     */     throws IOException
/*     */   {
/* 192 */     String fileName = destPath + "/ML/DailyCorrelLog.csv";
/*     */     
/* 194 */     TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals = new TreeMap();
/*     */     
/* 196 */     CSVReader reader = new CSVReader(fileName, ',', 0);
/*     */     
/*     */ 
/*     */ 
/* 200 */     String[] header1 = reader.getLine();
/* 201 */     String[] header2 = reader.getLine();
/*     */     String[] curLine;
/* 203 */     while ((curLine = reader.getLine()) != null) {
/*     */       String[] curLine;
/* 205 */       Long date = Long.valueOf(Long.parseLong(curLine[0]));
/* 206 */       HashMap<String, HashMap<String, Double>> correlMap = new HashMap();
/*     */       
/*     */ 
/* 209 */       for (int i = 1; i < curLine.length; i++)
/*     */       {
/* 211 */         String scrip1 = header1[i];
/* 212 */         String scrip2 = header2[i];
/* 213 */         Double value = Double.valueOf(Double.parseDouble(curLine[i]));
/*     */         
/* 215 */         HashMap<String, Double> curMap = (HashMap)correlMap.get(scrip1);
/* 216 */         if (curMap == null) {
/* 217 */           curMap = new HashMap();
/* 218 */           curMap.put(scrip2, value);
/* 219 */           correlMap.put(scrip1, curMap);
/*     */         } else {
/* 221 */           curMap.put(scrip2, value);
/* 222 */           correlMap.put(scrip1, curMap);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 227 */       correlVals.put(date, correlMap);
/*     */     }
/*     */     
/* 230 */     reader.close();
/*     */     
/* 232 */     return correlVals;
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
/* 244 */     String[] logData = new String[5];
/* 245 */     logData[0] = resultDate.toString();
/* 246 */     label258: for (Map.Entry<String, Boolean> entry : result.entrySet()) {
/* 247 */       String assetName = (String)entry.getKey();
/* 248 */       String originalAssetName = assetName;
/*     */       
/* 250 */       if (this.bias)
/* 251 */         originalAssetName = assetName.split("#")[1];
/*     */       Double mtm;
/*     */       Double mtm;
/* 254 */       if (mtmList == null) {
/* 255 */         mtm = Double.valueOf(0.0D);
/* 256 */       } else { if (mtmList.get(originalAssetName) == null) {
/*     */           continue;
/*     */         }
/* 259 */         mtm = (Double)mtmList.get(originalAssetName);
/*     */       }
/* 261 */       logData[1] = assetName;
/* 262 */       if (result.get(assetName) != null)
/*     */       {
/* 264 */         if (!((Boolean)result.get(assetName)).booleanValue()) {
/* 265 */           logData[4] = "Not Selected";
/* 266 */         } else if (((Double)modelOutput.get(assetName)).equals(Double.valueOf(100.0D))) {
/* 267 */           logData[4] = "Running Trade";
/* 268 */         } else if (this.tsTradedSelectedScripsMap.get(resultDate) == null) {
/* 269 */           logData[4] = "Selected Not Traded";
/* 270 */         } else { if (MathLib.doubleCompare(mtm, Double.valueOf(0.0D)).intValue() == 0)
/*     */           {
/* 272 */             if (!((String)this.tsTradedSelectedScripsMap.get(resultDate)).contains(assetName)) {
/* 273 */               logData[4] = "Selected Not Traded";
/*     */               break label258; } }
/* 275 */           logData[4] = "Traded";
/*     */         }
/* 277 */         logData[2] = mtm.toString();
/* 278 */         logData[3] = ((Double)modelOutput.get(assetName)).toString();
/*     */         
/* 280 */         this.mlLogWriter.writeLine(logData);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initMLLogWriter()
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 321 */       this.mlLogWriter = new CSVWriter(this.mlPath + "\\DailyFilterLog" + ".csv", false, ",");
/*     */     } catch (IOException e) {
/* 323 */       System.out.println("ML Error:Error in creating file for logging Daily filter results");
/* 324 */       throw new IOException();
/*     */     }
/*     */     
/* 327 */     this.mlLogWriter.write("Date,Asset,Initial MTM,Model Output, Decision\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HashMap<String, DailyDataReader> getDailyReaderCollection()
/*     */   {
/* 337 */     return this.dailyReaderCollection;
/*     */   }
/*     */   
/*     */   public CSVWriter getMlLogWriter() {
/* 341 */     return this.mlLogWriter;
/*     */   }
/*     */   
/*     */   public CSVWriter getCorrelLogWriter() {
/* 345 */     return this.correlLogWriter.getCorrelLogWriter();
/*     */   }
/*     */   
/*     */   public HashMap<String, MLAlgo> getAlgorithmMap() {
/* 349 */     return this.algorithmMap;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/MLPostProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */