/*     */ package com.q1.bt.machineLearning.driver;
/*     */ 
/*     */ import com.q1.bt.machineLearning.absclasses.MLAlgo;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.BinaryGenerator;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.CorrelLogWriter;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.OutputProcessor;
/*     */ import com.q1.bt.machineLearning.driver.driverHelperClasses.ParameterWriter;
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
/*  41 */   HashMap<String, MLAlgo> algorithmMap = new HashMap();
/*     */   
/*     */   HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps;
/*     */   
/*     */   HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMap;
/*     */   
/*     */   String mlPath;
/*  48 */   Long rwriteTS = Long.valueOf(0L);
/*  49 */   Boolean rcompleteReading = Boolean.valueOf(false);
/*     */   
/*  51 */   HashMap<Long, String> tsTradedSelectedScripsMap = new HashMap();
/*  52 */   HashMap<Long, String> tsTradedNotSelectedScripsMap = new HashMap();
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
/*  89 */     this.backtest = backtest;
/*     */     
/*     */ 
/*  92 */     this.mlParameter = machineLearning.getMlParameter();
/*     */     
/*  94 */     this.dailyReaderCollection = dailyReaderCollection;
/*  95 */     this.correlVals = correlVals;
/*  96 */     this.algorithmMap = algorithmMap;
/*  97 */     this.tradeStartEndMaps = tradeStartEndMaps;
/*  98 */     this.tradeStartDateTradeSideMap = tradeStartDateTradeSideMap;
/*  99 */     this.tradeMTMMat = tradeMTMMat;
/* 100 */     this.modelSegmentWiseAssetUniverseMap = modelSegmentWiseAssetUniverseMap;
/* 101 */     this.postModelSelectionSegmentWiseAssetUniverseMap = postModelSelectionSegmentWiseAssetUniverseMap;
/* 102 */     this.scripUniverse = scripUniverse;
/* 103 */     this.bias = bias;
/*     */     
/* 105 */     this.mlPath = (destPath + "/ML");
/*     */     try
/*     */     {
/* 108 */       initMLLogWriter();
/* 109 */       this.mlFinalDecisionWriter = new MLFinalDecisionWriter(this.mlPath);
/*     */     } catch (Exception e) {
/* 111 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void readOutput(String sourcePath, String destPath, boolean postProcess, ArrayList<Long> dateList) throws Exception
/*     */   {
/* 117 */     HashMap<String, Double> modelOutput = new HashMap();
/* 118 */     HashMap<String, Boolean> result = new HashMap();
/* 119 */     HashMap<String, CSVReader> outputReaderCollector = new HashMap();
/*     */     
/* 121 */     if (this.correlVals == null) {
/* 122 */       this.correlVals = correlRead(destPath);
/*     */     }
/*     */     else {
/* 125 */       this.correlLogWriter = new CorrelLogWriter(this.mlPath);
/* 126 */       this.correlLogWriter.writeCorrelLog(this.correlVals, this.scripUniverse);
/*     */     }
/*     */     
/* 129 */     this.rcompleteReading = Boolean.valueOf(false);
/*     */     
/* 131 */     OutputProcessor outputProcessor = new OutputProcessor();
/*     */     
/* 133 */     for (String assetPart : this.algorithmMap.keySet()) {
/* 134 */       String fileName = destPath + "/ML" + "/" + assetPart + 
/* 135 */         " Output.csv";
/*     */       try {
/* 137 */         System.out.println("processing " + fileName);
/* 138 */         CSVReader outputReader = new CSVReader(fileName, ',', 0);
/* 139 */         outputReaderCollector.put(assetPart, outputReader);
/*     */       } catch (IOException e) {
/* 141 */         System.out.println(fileName + " not found");
/*     */       }
/*     */     }
/*     */     
/* 145 */     BinaryGenerator binaryGenerator = new BinaryGenerator(
/* 146 */       this.tsTradedSelectedScripsMap, this.tsTradedNotSelectedScripsMap, 
/* 147 */       this.correlVals, this.tradeStartEndMaps, this.tradeStartDateTradeSideMap, this.bias);
/* 148 */     boolean firstCall = true;
/*     */     
/* 150 */     while (!this.rcompleteReading.booleanValue()) {
/* 151 */       modelOutput = outputProcessor.processOutput(outputReaderCollector);
/* 152 */       this.rcompleteReading = Boolean.valueOf(outputProcessor.isRcompleteReading());
/* 153 */       this.rwriteTS = outputProcessor.getRwriteTS();
/*     */       
/*     */ 
/* 156 */       result = binaryGenerator.generateBinary(modelOutput, 
/* 157 */         this.mlParameter.getSegmentCount().intValue(), 
/* 158 */         this.mlParameter.getOverallCount().intValue(), 
/* 159 */         this.mlParameter.getSegmentCorrelThreshold().doubleValue(), 
/* 160 */         this.mlParameter.getOverallCorrelThreshold().doubleValue(), this.rwriteTS.longValue(), 
/* 161 */         dateList, this.postModelSelectionSegmentWiseAssetUniverseMap, firstCall);
/* 162 */       firstCall = false;
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 167 */         this.mlFinalDecisionWriter.writeAndSaveInMemoryMLDecisions(result, this.rwriteTS);
/* 168 */         HashMap<String, Double> mtmMap = (HashMap)this.tradeMTMMat.get(this.rwriteTS);
/*     */         
/* 170 */         writeMLTradeLog(this.rwriteTS, result, mtmMap, modelOutput);
/*     */       }
/*     */       catch (Exception e) {
/* 173 */         System.out.println("ML Error in writing Output files for " + this.rwriteTS);
/* 174 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 179 */     this.mlFinalDecisionWriter.closeWriter();
/* 180 */     ParameterWriter parameterWriter = new ParameterWriter();
/* 181 */     parameterWriter.createParamDir(sourcePath, destPath, this.mlParameter);
/* 182 */     HashMap<String, TreeMap<Long, Boolean>> assetTimeStampDecisionMap = this.mlFinalDecisionWriter.getAssetTimeStampDecisionMap();
/* 183 */     TradeBookToOrderBookGenerator tradeBookToOrderBookGenerator = 
/* 184 */       new TradeBookToOrderBookGenerator(sourcePath, destPath, assetTimeStampDecisionMap, this.bias);
/* 185 */     tradeBookToOrderBookGenerator.generateOrderBooks();
/*     */   }
/*     */   
/*     */   private TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlRead(String destPath)
/*     */     throws IOException
/*     */   {
/* 191 */     String fileName = destPath + "/ML/DailyCorrelLog.csv";
/*     */     
/* 193 */     TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals = new TreeMap();
/*     */     
/* 195 */     CSVReader reader = new CSVReader(fileName, ',', 0);
/*     */     
/*     */ 
/*     */ 
/* 199 */     String[] header1 = reader.getLine();
/* 200 */     String[] header2 = reader.getLine();
/*     */     String[] curLine;
/* 202 */     while ((curLine = reader.getLine()) != null) {
/*     */       String[] curLine;
/* 204 */       Long date = Long.valueOf(Long.parseLong(curLine[0]));
/* 205 */       HashMap<String, HashMap<String, Double>> correlMap = new HashMap();
/*     */       
/*     */ 
/* 208 */       for (int i = 1; i < curLine.length; i++)
/*     */       {
/* 210 */         String scrip1 = header1[i];
/* 211 */         String scrip2 = header2[i];
/* 212 */         Double value = Double.valueOf(Double.parseDouble(curLine[i]));
/*     */         
/* 214 */         HashMap<String, Double> curMap = (HashMap)correlMap.get(scrip1);
/* 215 */         if (curMap == null) {
/* 216 */           curMap = new HashMap();
/* 217 */           curMap.put(scrip2, value);
/* 218 */           correlMap.put(scrip1, curMap);
/*     */         } else {
/* 220 */           curMap.put(scrip2, value);
/* 221 */           correlMap.put(scrip1, curMap);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 226 */       correlVals.put(date, correlMap);
/*     */     }
/*     */     
/* 229 */     reader.close();
/*     */     
/* 231 */     return correlVals;
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
/* 243 */     String[] logData = new String[5];
/* 244 */     logData[0] = resultDate.toString();
/* 245 */     label258: for (Map.Entry<String, Boolean> entry : result.entrySet()) {
/* 246 */       String assetName = (String)entry.getKey();
/* 247 */       String originalAssetName = assetName;
/*     */       
/* 249 */       if (this.bias)
/* 250 */         originalAssetName = assetName.split("#")[1];
/*     */       Double mtm;
/*     */       Double mtm;
/* 253 */       if (mtmList == null) {
/* 254 */         mtm = Double.valueOf(0.0D);
/* 255 */       } else { if (mtmList.get(originalAssetName) == null) {
/*     */           continue;
/*     */         }
/* 258 */         mtm = (Double)mtmList.get(originalAssetName);
/*     */       }
/* 260 */       logData[1] = assetName;
/* 261 */       if (result.get(assetName) != null)
/*     */       {
/* 263 */         if (!((Boolean)result.get(assetName)).booleanValue()) {
/* 264 */           logData[4] = "Not Selected";
/* 265 */         } else if (((Double)modelOutput.get(assetName)).equals(Double.valueOf(Double.POSITIVE_INFINITY))) {
/* 266 */           logData[4] = "Running Trade";
/* 267 */         } else if (this.tsTradedSelectedScripsMap.get(resultDate) == null) {
/* 268 */           logData[4] = "Selected Not Traded";
/* 269 */         } else { if (MathLib.doubleCompare(mtm, Double.valueOf(0.0D)).intValue() == 0)
/*     */           {
/* 271 */             if (!((String)this.tsTradedSelectedScripsMap.get(resultDate)).contains(assetName)) {
/* 272 */               logData[4] = "Selected Not Traded";
/*     */               break label258; } }
/* 274 */           logData[4] = "Traded";
/*     */         }
/* 276 */         logData[2] = mtm.toString();
/* 277 */         logData[3] = ((Double)modelOutput.get(assetName)).toString();
/*     */         
/* 279 */         this.mlLogWriter.writeLine(logData);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void initMLLogWriter() throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 288 */       this.mlLogWriter = new CSVWriter(this.mlPath + "\\DailyFilterLog" + ".csv", false, ",");
/*     */     } catch (IOException e) {
/* 290 */       System.out.println("ML Error:Error in creating file for logging Daily filter results");
/* 291 */       throw new IOException();
/*     */     }
/*     */     
/* 294 */     this.mlLogWriter.write("Date,Asset,Initial MTM,Model Output, Decision\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HashMap<String, DailyDataReader> getDailyReaderCollection()
/*     */   {
/* 304 */     return this.dailyReaderCollection;
/*     */   }
/*     */   
/*     */   public CSVWriter getMlLogWriter() {
/* 308 */     return this.mlLogWriter;
/*     */   }
/*     */   
/*     */   public CSVWriter getCorrelLogWriter() {
/* 312 */     return this.correlLogWriter.getCorrelLogWriter();
/*     */   }
/*     */   
/*     */   public HashMap<String, MLAlgo> getAlgorithmMap() {
/* 316 */     return this.algorithmMap;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/MLPostProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */