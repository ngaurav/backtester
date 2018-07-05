/*      */ package com.q1.bt.driver;
/*      */ 
/*      */ import com.q1.bt.data.classes.Scrip;
/*      */ import com.q1.bt.global.BacktesterGlobal;
/*      */ import com.q1.bt.machineLearning.absclasses.MLAlgo;
/*      */ import com.q1.bt.machineLearning.absclasses.MLAlgoP;
/*      */ import com.q1.bt.machineLearning.absclasses.MLAlgoR;
/*      */ import com.q1.bt.machineLearning.absclasses.MLAlgoRA;
/*      */ import com.q1.bt.machineLearning.driver.MLInputFileGenerator;
/*      */ import com.q1.bt.machineLearning.driver.MLPostProcessor;
/*      */ import com.q1.bt.machineLearning.driver.MLPreProcessor;
/*      */ import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
/*      */ import com.q1.bt.machineLearning.utility.DailyDataReader;
/*      */ import com.q1.bt.machineLearning.utility.TradeAndMTMDataProcessor;
/*      */ import com.q1.bt.process.BacktesterProcess;
/*      */ import com.q1.bt.process.ProcessFlow;
/*      */ import com.q1.bt.process.machinelearning.LookbackType;
/*      */ import com.q1.bt.process.objects.Backtest;
/*      */ import com.q1.bt.process.objects.MachineLearning;
/*      */ import com.q1.bt.process.parameter.BacktestParameter;
/*      */ import com.q1.bt.process.parameter.LoginParameter;
/*      */ import com.q1.bt.process.parameter.MachineLearningParameter;
/*      */ import com.q1.csv.CSVReader;
/*      */ import com.q1.csv.CSVWriter;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
/*      */ import javax.swing.JOptionPane;
/*      */ import org.apache.commons.io.FileUtils;
/*      */ import org.rosuda.JRI.Rengine;
/*      */ 
/*      */ public class MachineLearningMainDriver implements Runnable
/*      */ {
/*      */   private static String destPath;
/*   40 */   public Long DAILY_START_DATE = Long.valueOf(19800101L);
/*      */   
/*      */   ArrayList<Long> dateList;
/*      */   
/*      */   MachineLearning machineLearning;
/*      */   
/*      */   MachineLearningParameter mlParameter;
/*      */   
/*      */   public Backtest backtest;
/*      */   
/*      */   BacktesterGlobal btGlobal;
/*      */   
/*      */   public MLPreProcessor mlPreProcessor;
/*      */   
/*      */   MLInputFileGenerator mlInputFileGenerator;
/*      */   
/*      */   MLPostProcessor mlPostProcessor;
/*      */   
/*      */   boolean postProcess;
/*      */   
/*      */   private HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap;
/*      */   private HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap;
/*      */   
/*      */   public MachineLearningMainDriver(BacktesterGlobal btGlobal, MachineLearning machineLearning)
/*      */     throws IOException
/*      */   {
/*   66 */     this.btGlobal = btGlobal;
/*   67 */     this.backtest = machineLearning.getBacktest();
/*   68 */     this.dateList = btGlobal.getConsolDateList(this.backtest.timeStamp);
/*      */     
/*      */ 
/*   71 */     this.machineLearning = machineLearning;
/*   72 */     this.mlParameter = machineLearning.getMlParameter();
/*      */     
/*   74 */     this.postProcess = machineLearning.getBacktest().backtestParameter.isGenerateOutputCheck();
/*      */   }
/*      */   
/*      */   public void run()
/*      */   {
/*      */     try
/*      */     {
/*   81 */       execute();
/*      */     } catch (Exception e) {
/*   83 */       e.printStackTrace();
/*   84 */       return;
/*      */     }
/*      */     
/*   87 */     if (this.btGlobal.isGui)
/*      */     {
/*   89 */       BacktesterProcess[] choices = { BacktesterProcess.Backtest, BacktesterProcess.Results };
/*   90 */       BacktesterProcess input = (BacktesterProcess)JOptionPane.showInputDialog(null, 
/*   91 */         "Please choose the next Process", "Process Type", 3, null, 
/*   92 */         choices, 
/*   93 */         choices[0]);
/*      */       
/*   95 */       this.btGlobal.processFlow.add(input);
/*      */       
/*      */ 
/*   98 */       if (input.equals(BacktesterProcess.Backtest))
/*      */       {
/*      */ 
/*  101 */         this.btGlobal.displayMessage("Running Backtest on Updated Orderbook");
/*  102 */         this.btGlobal.processFlow.update();
/*  103 */         this.btGlobal.processFlow.add(BacktesterProcess.Results);
/*      */         try
/*      */         {
/*  106 */           if (this.mlPreProcessor != null) {
/*  107 */             destPath = this.mlPreProcessor.getDestPath();
/*      */           }
/*  109 */           Backtest mlBacktest = new Backtest(this.backtest.backtestParameter, destPath + "/ML Order Data");
/*  110 */           mlBacktest.fileBacktest = true;
/*  111 */           mlBacktest.timeStamp = this.machineLearning.getTimeStamp();
/*  112 */           BacktestMainDriver backtestDriver = new BacktestMainDriver(this.btGlobal, mlBacktest);
/*  113 */           Thread t = new Thread(backtestDriver);
/*  114 */           t.start();
/*      */         }
/*      */         catch (Exception e) {
/*  117 */           e.printStackTrace();
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*  122 */       else if (input.equals(BacktesterProcess.Results)) {
/*  123 */         this.btGlobal.processFlow.update();
/*  124 */         this.btGlobal.initializeProcess(this.machineLearning);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void execute() throws Exception
/*      */   {
/*  131 */     String backtestPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp;
/*  132 */     Integer newTimeStamp = Integer.valueOf(Integer.parseInt(this.backtest.timeStamp) + 1);
/*  133 */     this.backtest.timeStamp = newTimeStamp.toString();
/*  134 */     String newBacktestPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp;
/*  135 */     File currentFile = new File(backtestPath);
/*  136 */     File newFile = new File(newBacktestPath);
/*  137 */     FileUtils.copyDirectory(currentFile, newFile);
/*  138 */     FileUtils.deleteDirectory(currentFile);
/*      */     
/*      */ 
/*  141 */     this.machineLearning.setTimeStamp("ML" + this.backtest.timeStamp);
/*      */     
/*      */ 
/*  144 */     HashMap<String, String> mlOutputMap = new HashMap();
/*      */     try {
/*  146 */       mlOutputMap = createMLOutputMap(this.btGlobal, this.machineLearning);
/*      */     } catch (IOException e2) {
/*  148 */       e2.printStackTrace();
/*  149 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  154 */     boolean mlCheck = false;boolean mlOutputCheck = false;boolean mlInputCheck = false;
/*  155 */     mlCheck = checkMLOutput(this.btGlobal, this.machineLearning, mlOutputMap);
/*  156 */     if (!mlCheck)
/*      */     {
/*  158 */       mlOutputCheck = checkMLOOutput(this.btGlobal, this.machineLearning, mlOutputMap);
/*  159 */       if (!mlOutputCheck)
/*      */       {
/*  161 */         mlInputCheck = checkMLIOutput(this.btGlobal, this.machineLearning, mlOutputMap);
/*      */       }
/*      */     }
/*      */     
/*  165 */     if (!mlCheck)
/*      */     {
/*      */ 
/*  168 */       preProcess();
/*      */       
/*  170 */       if (!mlOutputCheck)
/*      */       {
/*  172 */         if (!mlInputCheck)
/*      */         {
/*  174 */           createInputFile();
/*      */         }
/*      */         
/*      */ 
/*  178 */         generateMLOutput();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  183 */       readOutput();
/*      */       
/*      */ 
/*  186 */       endProcess();
/*      */     }
/*      */   }
/*      */   
/*      */   public void preProcess() throws Exception
/*      */   {
/*  192 */     this.mlPreProcessor = new MLPreProcessor(this.btGlobal, this.backtest, this.machineLearning);
/*  193 */     this.mlPreProcessor.mlPreProcess();
/*      */   }
/*      */   
/*      */   public void createInputFile() throws Exception
/*      */   {
/*  198 */     ArrayList<String> scripUniverse = this.mlPreProcessor.getScripUniverse();
/*  199 */     String dataPath = this.btGlobal.loginParameter.getDataPath();
/*      */     
/*  201 */     HashMap<String, DailyDataReader> dailyReaderCollection = initDailyreader(scripUniverse, dataPath, this.DAILY_START_DATE);
/*  202 */     this.mlInputFileGenerator = new MLInputFileGenerator(this.btGlobal, this.backtest, this.mlParameter, dailyReaderCollection, this.mlParameter.getBias().booleanValue());
/*      */     
/*  204 */     boolean nextLayer = false;
/*  205 */     TradeAndMTMDataProcessor stratTradePnL = this.mlPreProcessor.getStratTradePnL();
/*  206 */     HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap = this.mlPreProcessor.getModelSegmentWiseAssetUniverseMap();
/*  207 */     String sourcePath = this.mlPreProcessor.getSourcePath();
/*  208 */     String destPath = this.mlPreProcessor.getDestPath();
/*  209 */     HashMap<String, TreeMap<Long, Long>> tradeStratEndMap = this.mlPreProcessor.getTradeStartEndMaps();
/*  210 */     HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMap = this.mlPreProcessor.getTradeStartDateTradeSideMaps();
/*      */     
/*  212 */     this.mlInputFileGenerator.createInputFile(nextLayer, sourcePath, destPath, dataPath, modelSegmentWiseAssetUniverseMap, 
/*  213 */       scripUniverse, stratTradePnL, this.dateList, tradeStratEndMap, tradeStartDateTradeSideMap, this.DAILY_START_DATE);
/*      */   }
/*      */   
/*      */   public void readOutput() throws Exception {
/*  217 */     String sourcePath = this.mlPreProcessor.getSourcePath();
/*  218 */     String destPath = this.mlPreProcessor.getDestPath();
/*  219 */     String algoLastModifiedTimeStamp = this.mlPreProcessor.getAlgoLastModifiedTimeStamp();
/*  220 */     this.modelSegmentWiseAssetUniverseMap = this.mlPreProcessor.getModelSegmentWiseAssetUniverseMap();
/*  221 */     this.postModelSelectionSegmentWiseAssetUniverseMap = this.mlPreProcessor.getPostModelSelectionSegmentWiseAssetUniverseMap();
/*  222 */     ArrayList<String> scripUniverse = this.mlPreProcessor.getScripUniverse();
/*      */     
/*      */     HashMap<String, DailyDataReader> dailyReaderCollection;
/*      */     TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals;
/*      */     HashMap<String, DailyDataReader> dailyReaderCollection;
/*  227 */     if (this.mlInputFileGenerator == null)
/*      */     {
/*  229 */       String dataPath = this.btGlobal.loginParameter.getDataPath();
/*  230 */       TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals = readCorrelFromFile(destPath);
/*  231 */       dailyReaderCollection = initDailyreader(scripUniverse, dataPath, this.DAILY_START_DATE);
/*      */     }
/*      */     else
/*      */     {
/*  235 */       correlVals = this.mlInputFileGenerator.getCorrelVals();
/*  236 */       dailyReaderCollection = this.mlInputFileGenerator.getDailyReaderCollection();
/*      */     }
/*      */     
/*  239 */     this.mlPostProcessor = new MLPostProcessor(sourcePath, destPath, this.modelSegmentWiseAssetUniverseMap, this.postModelSelectionSegmentWiseAssetUniverseMap, scripUniverse, 
/*  240 */       this.btGlobal.loginParameter.getDataPath(), this.backtest, this.machineLearning, this.dateList, 
/*  241 */       dailyReaderCollection, correlVals, 
/*  242 */       this.mlPreProcessor.getAlgorithmMap(), this.mlPreProcessor.getTradeStartEndMaps(), this.mlPreProcessor.getTradeStartDateTradeSideMaps(), 
/*  243 */       this.mlPreProcessor.getTradeMTMMat(), algoLastModifiedTimeStamp, this.postProcess, this.mlParameter.getBias().booleanValue());
/*      */     
/*  245 */     this.mlPostProcessor.readOutput(sourcePath, destPath, this.postProcess, this.dateList);
/*      */   }
/*      */   
/*      */   public void endProcess() throws IOException
/*      */   {
/*  250 */     HashMap<String, MLAlgo> algorithmMap = this.mlPostProcessor.getAlgorithmMap();
/*  251 */     CSVWriter mlLogWriter = this.mlPostProcessor.getMlLogWriter();
/*  252 */     CSVWriter correlLogWriter = this.mlPostProcessor.getCorrelLogWriter();
/*      */     try
/*      */     {
/*  255 */       for (MLAlgo mlalgo : algorithmMap.values()) {
/*  256 */         mlalgo.close();
/*      */       }
/*      */       
/*  259 */       mlLogWriter.close();
/*  260 */       correlLogWriter.close();
/*      */     }
/*      */     catch (Exception localException) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static HashMap<String, String> createMLOutputMap(BacktesterGlobal btGlobal, MachineLearning machineLearning)
/*      */     throws IOException
/*      */   {
/*  271 */     HashMap<String, String> outputMap = new HashMap();
/*      */     
/*      */ 
/*  274 */     File outputFile = new File(btGlobal.loginParameter.getOutputPath());
/*  275 */     File[] folders = outputFile.listFiles();
/*  276 */     File[] arrayOfFile1; int j = (arrayOfFile1 = folders).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
/*      */       
/*  278 */       String folderTimeStamp = folder.getName();
/*  279 */       String folderPath = folder.getAbsolutePath();
/*      */       
/*      */ 
/*  282 */       if (folderTimeStamp.startsWith("ML"))
/*      */       {
/*      */ 
/*      */ 
/*  286 */         if (new File(folderPath + "/Parameters").exists())
/*      */         {
/*      */ 
/*      */ 
/*  290 */           File[] paramFiles = new File(folderPath + "/Parameters").listFiles();
/*  291 */           File[] arrayOfFile2; int m = (arrayOfFile2 = paramFiles).length; for (int k = 0; k < m; k++) { File paramFile = arrayOfFile2[k];
/*      */             
/*  293 */             String fileName = paramFile.getName();
/*  294 */             String[] fileVal = paramFile.getName().split(" ");
/*      */             
/*  296 */             if (fileName.endsWith("Parameters.csv"))
/*      */             {
/*      */ 
/*  299 */               String strategyID = fileVal[0];
/*  300 */               if (machineLearning.getBacktest().backtestMap.containsKey(strategyID))
/*      */               {
/*      */ 
/*      */ 
/*  304 */                 String paramPath = paramFile.getAbsolutePath();
/*  305 */                 String paramKey = "";
/*  306 */                 String paramKeyInput = "";
/*  307 */                 String paramKeyOutput = "";
/*      */                 
/*      */ 
/*  310 */                 CSVReader reader = new CSVReader(paramPath, ',', 0);
/*      */                 String[] parameterLine;
/*  312 */                 while ((parameterLine = reader.getLine()) != null)
/*      */                 {
/*      */                   String[] parameterLine;
/*  315 */                   String parameterName = parameterLine[0];
/*  316 */                   String parameterValue = parameterLine[1];
/*      */                   
/*      */ 
/*  319 */                   if (paramKey.equals("")) {
/*  320 */                     paramKey = parameterValue;
/*      */                   } else {
/*  322 */                     paramKey = paramKey + "$" + parameterValue;
/*      */                   }
/*      */                   
/*  325 */                   if ((!parameterName.startsWith("ML ")) || (parameterLine[0].equals("ML Model Merge Type"))) {
/*  326 */                     if (paramKeyInput.equals("")) {
/*  327 */                       paramKeyInput = parameterValue;
/*      */                     } else {
/*  329 */                       paramKeyInput = paramKeyInput + "$" + parameterValue;
/*      */                     }
/*      */                   }
/*      */                   
/*  333 */                   if ((!parameterLine[0].equals("ML Consolidation Function")) && 
/*  334 */                     (!parameterLine[0].equals("ML Segment Asset Count")) && 
/*  335 */                     (!parameterLine[0].equals("ML Overall Asset Count")) && 
/*  336 */                     (!parameterLine[0].equals("ML Segment Correl Threshold")) && 
/*  337 */                     (!parameterLine[0].equals("ML Overall Correl Threshold"))) {
/*  338 */                     if (paramKeyOutput.equals("")) {
/*  339 */                       paramKeyOutput = parameterValue;
/*      */                     } else {
/*  341 */                       paramKeyOutput = paramKeyOutput + "$" + parameterValue;
/*      */                     }
/*      */                   }
/*      */                 }
/*  345 */                 reader.close();
/*      */                 
/*      */ 
/*  348 */                 String primaryKey = "ML " + strategyID + " " + paramKey;
/*  349 */                 String primaryKeyInput = "ML " + strategyID + " " + paramKeyInput;
/*  350 */                 String primaryKeyOutput = "ML " + strategyID + " " + paramKeyOutput;
/*      */                 
/*      */ 
/*  353 */                 TreeSet<String> factorSet = new TreeSet();
/*  354 */                 String factorPath = folderPath + "/Parameters/ML Factorlist.csv";
/*  355 */                 if (new File(factorPath).exists()) {
/*  356 */                   reader = new CSVReader(factorPath, ',', 0);
/*      */                   String[] tsLine;
/*  358 */                   while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/*  359 */                     factorSet.add(tsLine[0]); }
/*  360 */                   reader.close();
/*      */                 }
/*      */                 
/*      */ 
/*  364 */                 for (String factor : factorSet)
/*      */                 {
/*  366 */                   primaryKey = primaryKey + "$" + factor;
/*  367 */                   primaryKeyInput = primaryKeyInput + "$" + factor;
/*  368 */                   primaryKeyOutput = primaryKeyOutput + "$" + factor;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  377 */                 TreeSet<String> scripListDateList = new TreeSet();
/*  378 */                 String scripParameterPath = folderPath + "/Parameters/" + strategyID + " ScripListDateMap.csv";
/*  379 */                 if (new File(scripParameterPath).exists()) {
/*  380 */                   reader = new CSVReader(scripParameterPath, ',', 0);
/*      */                   String[] tsLine;
/*  382 */                   while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/*  383 */                     scripListDateList.add(tsLine[0] + "," + tsLine[1] + "," + tsLine[2]); }
/*  384 */                   reader.close();
/*      */                 }
/*      */                 
/*      */ 
/*  388 */                 boolean fileError = false;
/*      */                 
/*      */ 
/*  391 */                 for (String scripListDate : scripListDateList)
/*      */                 {
/*  393 */                   String[] scripListDateVal = scripListDate.split(",");
/*  394 */                   String scripListID = scripListDateVal[0];
/*      */                   
/*      */ 
/*  397 */                   String scripFileName = folderPath + "/Parameters/" + scripListID + " ScripSet.csv";
/*  398 */                   CSVReader scripReader = new CSVReader(scripFileName, ',', 0);
/*      */                   
/*      */ 
/*  401 */                   String scripKey = scripListDate;
/*  402 */                   String[] scripLine; while ((scripLine = scripReader.getLine()) != null) { String[] scripLine;
/*  403 */                     String scripID = scripLine[0];
/*      */                     
/*  405 */                     String mtmPath = folderPath + "/MTM Data/" + strategyID + " " + scripListID + "/" + scripID + 
/*  406 */                       " MTM.csv";
/*  407 */                     String tradePath = folderPath + "/Trade Data/" + strategyID + " " + scripListID + "/" + 
/*  408 */                       scripID + " Tradebook.csv";
/*      */                     
/*  410 */                     if ((!new File(mtmPath).exists()) || (!new File(tradePath).exists())) {
/*  411 */                       fileError = true;
/*  412 */                       break;
/*      */                     }
/*  414 */                     scripKey = scripKey + "|" + scripID;
/*      */                   }
/*  416 */                   scripReader.close();
/*      */                   
/*      */ 
/*  419 */                   if (fileError) {
/*      */                     break;
/*      */                   }
/*      */                   
/*  423 */                   primaryKey = primaryKey + "$" + scripKey;
/*  424 */                   primaryKeyInput = primaryKeyInput + "$" + scripKey;
/*  425 */                   primaryKeyOutput = primaryKeyOutput + "$" + scripKey;
/*      */                 }
/*      */                 
/*      */ 
/*  429 */                 if (!fileError)
/*      */                 {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  440 */                   outputMap.put(primaryKey, folderTimeStamp);
/*  441 */                   outputMap.put(primaryKeyInput, folderTimeStamp);
/*  442 */                   outputMap.put(primaryKeyOutput, folderTimeStamp);
/*      */                 }
/*      */               }
/*      */             } } } } }
/*  446 */     return outputMap;
/*      */   }
/*      */   
/*      */   public static boolean checkMLOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  452 */     boolean outputCheck = true;
/*      */     
/*  454 */     String backtestTimestamp = machineLearning.getBacktest().timeStamp;
/*      */     
/*  456 */     for (String strategyID : machineLearning.getBacktest().backtestMap.keySet())
/*      */     {
/*      */ 
/*  459 */       String backtestParameters = getBacktestParameters(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*      */ 
/*  462 */       String mlParameters = machineLearning.getMlParameter().getMLParametersAsKey();
/*      */       
/*      */ 
/*  465 */       String parameterString = backtestParameters + "$" + mlParameters;
/*      */       
/*      */ 
/*  468 */       String currentKey = "ML " + strategyID + " " + parameterString;
/*      */       
/*      */ 
/*  471 */       String scripListDate = getScripDateKey(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*  473 */       currentKey = currentKey + "$" + scripListDate;
/*      */       
/*      */ 
/*  476 */       boolean curOutputCheck = checkAndTransferML(btGlobal, machineLearning, strategyID, currentKey, mlOutputMap);
/*  477 */       if (outputCheck)
/*  478 */         outputCheck = curOutputCheck;
/*      */     }
/*  480 */     return outputCheck;
/*      */   }
/*      */   
/*      */   public static boolean checkMLIOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  486 */     boolean outputCheck = true;
/*      */     
/*  488 */     String backtestTimestamp = machineLearning.getBacktest().timeStamp;
/*      */     
/*  490 */     for (String strategyID : machineLearning.getBacktest().backtestMap.keySet())
/*      */     {
/*      */ 
/*  493 */       String backtestParameters = getBacktestParameters(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*      */ 
/*  496 */       String mlFactorParameters = machineLearning.getMlParameter().getMLParametersAsInputKey();
/*      */       
/*      */ 
/*  499 */       String paramString = backtestParameters + "$" + mlFactorParameters;
/*      */       
/*      */ 
/*  502 */       String currentKey = "ML " + strategyID + " " + paramString;
/*      */       
/*      */ 
/*  505 */       String scripListDate = getScripDateKey(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*  507 */       currentKey = currentKey + "$" + scripListDate;
/*      */       
/*      */ 
/*  510 */       boolean curOutputCheck = checkAndTransferMLInput(btGlobal, machineLearning, strategyID, currentKey, 
/*  511 */         mlOutputMap);
/*  512 */       if (outputCheck)
/*  513 */         outputCheck = curOutputCheck;
/*      */     }
/*  515 */     return outputCheck;
/*      */   }
/*      */   
/*      */   public static boolean checkMLOOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  521 */     boolean outputCheck = true;
/*      */     
/*  523 */     String backtestTimestamp = machineLearning.getBacktest().timeStamp;
/*      */     
/*  525 */     for (String strategyID : machineLearning.getBacktest().backtestMap.keySet())
/*      */     {
/*      */ 
/*  528 */       String backtestParameters = getBacktestParameters(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*      */ 
/*  531 */       String mlParameters = machineLearning.getMlParameter().getMLParametersAsOutputKey();
/*      */       
/*      */ 
/*  534 */       String parameterString = backtestParameters + "$" + mlParameters;
/*      */       
/*      */ 
/*  537 */       String currentKey = "ML " + strategyID + " " + parameterString;
/*      */       
/*      */ 
/*  540 */       String scripListDate = getScripDateKey(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*  542 */       currentKey = currentKey + "$" + scripListDate;
/*      */       
/*      */ 
/*  545 */       boolean curOutputCheck = checkAndTransferMLOutput(btGlobal, machineLearning, strategyID, currentKey, 
/*  546 */         mlOutputMap);
/*  547 */       if (outputCheck)
/*  548 */         outputCheck = curOutputCheck;
/*      */     }
/*  550 */     return outputCheck;
/*      */   }
/*      */   
/*      */   public static boolean checkAndTransferML(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  556 */     String timeStamp = (String)mlOutputMap.get(currentKey);
/*  557 */     String mlTimeStamp = machineLearning.getTimeStamp();
/*      */     
/*  559 */     if (timeStamp == null) {
/*  560 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  565 */     String outputPath = btGlobal.loginParameter.getOutputPath();
/*  566 */     String sMTMPath = outputPath + "/" + timeStamp;
/*  567 */     String mtmPath = outputPath + "/" + mlTimeStamp;
/*  568 */     destPath = mtmPath;
/*  569 */     File sMTMFolder = new File(sMTMPath);
/*  570 */     File mtmFolder = new File(mtmPath);
/*      */     
/*  572 */     sMTMFolder.renameTo(mtmFolder);
/*      */     
/*      */ 
/*  575 */     File paramFolder = new File(mtmPath + "/Parameters");
/*      */     File[] arrayOfFile;
/*  577 */     int j = (arrayOfFile = paramFolder.listFiles()).length; for (int i = 0; i < j; i++) { File file = arrayOfFile[i];
/*      */       
/*  579 */       if (file.getName().contains("ScripListDateMap")) {
/*  580 */         file.delete();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  651 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean checkAndTransferMLOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  658 */     String timeStamp = (String)mlOutputMap.get(currentKey);
/*      */     
/*  660 */     String mlTimeStamp = machineLearning.getTimeStamp();
/*      */     
/*  662 */     if (timeStamp == null) {
/*  663 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  667 */     String outputPath = btGlobal.loginParameter.getOutputPath();
/*      */     
/*      */ 
/*  670 */     String sParamPath = outputPath + "/" + timeStamp + "/Parameters";
/*  671 */     String paramPath = outputPath + "/" + mlTimeStamp + "/Parameters";
/*  672 */     File sParamFolder = new File(sParamPath);
/*  673 */     File paramFolder = new File(paramPath);
/*  674 */     if (!paramFolder.exists())
/*  675 */       paramFolder.mkdirs();
/*  676 */     File[] paramFileList = sParamFolder.listFiles();
/*  677 */     File[] arrayOfFile1; int j = (arrayOfFile1 = paramFileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/*  678 */       String fStrategy = file.getName().split(" ")[0];
/*  679 */       if (fStrategy.equals(strategy)) {
/*  680 */         String newPath = paramPath + "/" + file.getName();
/*  681 */         btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*  683 */       if (fStrategy.equals("ML")) {
/*  684 */         String newPath = paramPath + "/" + file.getName();
/*  685 */         btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  690 */     String sMLPath = outputPath + "/" + timeStamp + "/ML";
/*  691 */     String mlPath = outputPath + "/" + mlTimeStamp + "/ML";
/*  692 */     File sMLFolder = new File(sMLPath);
/*  693 */     File mlFolder = new File(mlPath);
/*  694 */     if (!mlFolder.exists())
/*  695 */       mlFolder.mkdirs();
/*  696 */     File[] mlFileList = sMLFolder.listFiles();
/*  697 */     File[] arrayOfFile2; int m = (arrayOfFile2 = mlFileList).length; for (int k = 0; k < m; k++) { File file = arrayOfFile2[k];
/*  698 */       String newPath = mlPath + "/" + file.getName();
/*  699 */       if (!new File(newPath).exists())
/*  700 */         btGlobal.copyFile(file, new File(newPath));
/*      */     }
/*  702 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean checkAndTransferMLAOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  709 */     String timeStamp = (String)mlOutputMap.get(currentKey);
/*      */     
/*  711 */     String mlTimeStamp = machineLearning.getTimeStamp();
/*      */     
/*  713 */     if (timeStamp == null) {
/*  714 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  718 */     String outputPath = btGlobal.loginParameter.getOutputPath();
/*      */     
/*      */ 
/*  721 */     String sParamPath = outputPath + "/" + timeStamp + "/Parameters";
/*  722 */     String paramPath = outputPath + "/" + mlTimeStamp + "/Parameters";
/*  723 */     File sParamFolder = new File(sParamPath);
/*  724 */     File paramFolder = new File(paramPath);
/*  725 */     if (!paramFolder.exists())
/*  726 */       paramFolder.mkdirs();
/*  727 */     File[] paramFileList = sParamFolder.listFiles();
/*  728 */     File[] arrayOfFile1; int j = (arrayOfFile1 = paramFileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/*  729 */       String fStrategy = file.getName().split(" ")[0];
/*  730 */       if (fStrategy.equals(strategy)) {
/*  731 */         String newPath = paramPath + "/" + file.getName();
/*  732 */         btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*  734 */       if (fStrategy.equals("ML")) {
/*  735 */         String newPath = paramPath + "/" + file.getName();
/*  736 */         btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  741 */     String sMLPath = outputPath + "/" + timeStamp + "/ML";
/*  742 */     String mlPath = outputPath + "/" + mlTimeStamp + "/ML";
/*  743 */     File sMLFolder = new File(sMLPath);
/*  744 */     File mlFolder = new File(mlPath);
/*  745 */     if (!mlFolder.exists())
/*  746 */       mlFolder.mkdirs();
/*  747 */     File[] mlFileList = sMLFolder.listFiles();
/*  748 */     File[] arrayOfFile2; int m = (arrayOfFile2 = mlFileList).length; for (int k = 0; k < m; k++) { File file = arrayOfFile2[k];
/*  749 */       if (file.getName().contains(" RData")) {
/*  750 */         String newPath = mlPath + "/" + file.getName();
/*  751 */         if (!new File(newPath).exists())
/*  752 */           btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*      */     }
/*  755 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean checkAndTransferMLInput(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  762 */     String timeStamp = (String)mlOutputMap.get(currentKey);
/*      */     
/*  764 */     String mlTimeStamp = machineLearning.getTimeStamp();
/*      */     
/*  766 */     if (timeStamp == null) {
/*  767 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  771 */     String outputPath = btGlobal.loginParameter.getOutputPath();
/*      */     
/*      */ 
/*  774 */     String sMLPath = outputPath + "/" + timeStamp + "/ML";
/*  775 */     String mlPath = outputPath + "/" + mlTimeStamp + "/ML";
/*  776 */     File sMLFolder = new File(sMLPath);
/*  777 */     File mlFolder = new File(mlPath);
/*  778 */     if (!mlFolder.exists())
/*  779 */       mlFolder.mkdirs();
/*  780 */     File[] mlFileList = sMLFolder.listFiles();
/*  781 */     File[] arrayOfFile1; int j = (arrayOfFile1 = mlFileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/*  782 */       if (file.getName().contains("Input")) {
/*  783 */         String newPath = mlPath + "/" + file.getName();
/*  784 */         if (!new File(newPath).exists())
/*  785 */           btGlobal.copyFile(file, new File(newPath));
/*  786 */       } else if (file.getName().contains("DailyCorrelLog")) {
/*  787 */         String newPath = mlPath + "/" + file.getName();
/*  788 */         if (!new File(newPath).exists())
/*  789 */           btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*      */     }
/*  792 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String getBacktestParameters(BacktesterGlobal btGlobal, String strategyID, String backtestTimestamp)
/*      */     throws IOException
/*      */   {
/*  799 */     String basePath = btGlobal.loginParameter.getOutputPath() + "/" + backtestTimestamp;
/*      */     
/*  801 */     if (!new File(basePath + "/Parameters").exists()) {
/*  802 */       return null;
/*      */     }
/*      */     
/*  805 */     String paramPath = basePath + "/Parameters/" + strategyID + " Parameters.csv";
/*  806 */     String strategyParameters = "";
/*  807 */     if (new File(paramPath).exists())
/*      */     {
/*  809 */       CSVReader reader = new CSVReader(paramPath, ',', 0);
/*      */       String[] tsLine;
/*  811 */       while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/*  812 */         if (strategyParameters.equals("")) {
/*  813 */           strategyParameters = tsLine[1];
/*      */         } else
/*  815 */           strategyParameters = strategyParameters + "$" + tsLine[1];
/*      */       }
/*  817 */       reader.close();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  825 */     return strategyParameters;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String getScripDateKey(BacktesterGlobal btGlobal, String strategyID, String timestamp)
/*      */     throws IOException
/*      */   {
/*  832 */     String basePath = btGlobal.loginParameter.getOutputPath() + "/" + timestamp;
/*      */     
/*  834 */     if (!new File(basePath + "/Parameters").exists()) {
/*  835 */       return null;
/*      */     }
/*      */     
/*  838 */     TreeSet<String> scripListDateList = new TreeSet();
/*  839 */     String scripParameterPath = basePath + "/Parameters/" + strategyID + " ScripListDateMap.csv";
/*  840 */     if (new File(scripParameterPath).exists()) {
/*  841 */       CSVReader reader = new CSVReader(scripParameterPath, ',', 0);
/*      */       String[] tsLine;
/*  843 */       while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/*  844 */         scripListDateList.add(tsLine[0] + "," + tsLine[1] + "," + tsLine[2]);
/*      */       }
/*      */     }
/*      */     
/*  848 */     String scripDateKey = null;
/*  849 */     for (String scripListDate : scripListDateList)
/*      */     {
/*  851 */       String[] scripListDateVal = scripListDate.split(",");
/*  852 */       String scripListID = scripListDateVal[0];
/*      */       
/*      */ 
/*  855 */       String scripFileName = basePath + "/Parameters/" + scripListID + " ScripSet.csv";
/*  856 */       CSVReader scripReader = new CSVReader(scripFileName, ',', 0);
/*      */       
/*  858 */       boolean fileError = false;
/*      */       
/*  860 */       if (scripDateKey == null) {
/*  861 */         scripDateKey = scripListDate;
/*      */       } else
/*  863 */         scripDateKey = scripDateKey + "$" + scripListDate;
/*  864 */       String[] scripLine; while ((scripLine = scripReader.getLine()) != null) { String[] scripLine;
/*  865 */         String scripID = scripLine[0];
/*      */         
/*  867 */         String mtmPath = basePath + "/MTM Data/" + strategyID + " " + scripListID + "/" + scripID + " MTM.csv";
/*  868 */         String tradePath = basePath + "/Trade Data/" + strategyID + " " + scripListID + "/" + scripID + 
/*  869 */           " Tradebook.csv";
/*      */         
/*  871 */         if ((!new File(mtmPath).exists()) || (!new File(tradePath).exists())) {
/*  872 */           fileError = true;
/*  873 */           break;
/*      */         }
/*  875 */         scripDateKey = scripDateKey + "|" + scripID;
/*      */       }
/*      */       
/*      */ 
/*  879 */       if (!fileError) {}
/*      */     }
/*      */     
/*      */ 
/*  883 */     return scripDateKey;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void generateMLOutput()
/*      */     throws Exception
/*      */   {
/*  907 */     String destPath = this.mlPreProcessor.getDestPath();
/*      */     
/*      */ 
/*  910 */     if (!(this.mlParameter.getMlAlgorithm() instanceof MLAlgoRA))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  915 */       if ((this.mlParameter.getMlAlgorithm() instanceof MLAlgoR))
/*      */       {
/*  917 */         boolean rolling = true;
/*      */         
/*  919 */         if (this.mlParameter.getLookbackType().equals(LookbackType.Hinged)) {
/*  920 */           rolling = false;
/*      */         }
/*      */         
/*  923 */         boolean postProcess = this.machineLearning.getBacktest().backtestParameter.isGenerateOutputCheck();
/*  924 */         boolean append = false;
/*      */         
/*  926 */         runMLThroughR(postProcess, this.btGlobal.loginParameter.getMainPath(), destPath, rolling, append);
/*      */ 
/*      */ 
/*      */       }
/*  930 */       else if ((this.mlParameter.getMlAlgorithm() instanceof MLAlgoP)) {
/*  931 */         boolean rolling = true;
/*      */         
/*  933 */         if (this.mlParameter.getLookbackType().equals(LookbackType.Hinged)) {
/*  934 */           rolling = false;
/*      */         }
/*      */         
/*  937 */         boolean postProcess = this.machineLearning.getBacktest().backtestParameter.isGenerateOutputCheck();
/*  938 */         boolean append = false;
/*      */         
/*  940 */         runMLThroughPython(postProcess, this.btGlobal.loginParameter.getMainPath(), destPath, rolling, append);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*  946 */         throw new RuntimeException("Invalid Object Type mlAlgo not typeCasted");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void runMLThroughR(boolean postProcess, String mainPath, String outputPath, boolean rolling, boolean append)
/*      */   {
/*  955 */     String[] newargs1 = { "--no-save" };
/*  956 */     Rengine engine = Rengine.getMainEngine();
/*  957 */     if (engine == null) {
/*  958 */       engine = new Rengine(newargs1, false, null);
/*      */     }
/*      */     
/*  961 */     String mainEval = "c(";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  966 */     for (Map.Entry<String, MLAlgo> entry : this.mlPreProcessor.getAlgorithmMap().entrySet())
/*      */     {
/*      */ 
/*  969 */       String mainDriverPath = mainPath + "/lib/ML Filter/MLFilterMainDriver.R";
/*      */       
/*      */ 
/*  972 */       MLAlgoR Mlr = (MLAlgoR)entry.getValue();
/*  973 */       String packageLoc = Mlr.getModelPackage().replaceAll("\\.", "/");
/*  974 */       String packagePath = mainPath + "/src/" + packageLoc;
/*  975 */       String algoPath = packagePath + "/" + Mlr.getModelName();
/*  976 */       String mlAlgoName = Mlr.getModelName().split("\\.")[0];
/*      */       
/*      */ 
/*  979 */       String src1 = "source(\\\"" + algoPath + "\\\")";
/*  980 */       String src2 = "source(\\\"" + mainDriverPath + "\\\")";
/*      */       
/*  982 */       System.out.println("source(\\\"" + algoPath + "\\\")");
/*  983 */       System.out.println("source(\\\"" + mainDriverPath + "\\\")");
/*      */       
/*  985 */       String rollingVal = rolling ? "TRUE" : "FALSE";
/*      */       
/*  987 */       String appendVal = append ? "TRUE" : "FALSE";
/*      */       
/*  989 */       String inputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Input.csv" + "\\\"";
/*  990 */       String outputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Output.csv" + "\\\"";
/*      */       
/*  992 */       String algoCommand = "MLFilterMainDriver(\\\"" + packagePath + "\\\"" + ", " + inputFile + ", " + 
/*  993 */         outputFile + ", " + "\\\"" + (String)entry.getKey() + "\\\"" + "," + "\\\"" + outputPath + "\\\"" + ", " + 
/*  994 */         this.mlParameter.getBlackoutPeriod() + ", " + this.mlParameter.getWindowPeriod() + ", " + 
/*  995 */         this.mlParameter.getUpdatePeriod() + ", " + rollingVal + ", " + appendVal + ", " + mlAlgoName;
/*  996 */       ArrayList<String[]> paramList = Mlr.getParameterList();
/*  997 */       for (String[] param : paramList) {
/*  998 */         algoCommand = algoCommand + "," + param[1];
/*      */       }
/* 1000 */       algoCommand = algoCommand + ")";
/*      */       
/* 1002 */       System.out.println(algoCommand);
/*      */       
/* 1004 */       System.out.println("\n");
/*      */       
/*      */ 
/* 1007 */       if (mainEval.equals("c(")) {
/* 1008 */         mainEval = mainEval + "\"" + src1 + "\\n" + src2 + "\\n" + algoCommand + "\"";
/*      */       }
/*      */       else {
/* 1011 */         mainEval = mainEval + "," + "\"" + src1 + "\\n" + src2 + "\\n" + algoCommand + "\"";
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1016 */     String mlMainPath = mainPath + "/lib/ML Filter/MainDriver.R";
/* 1017 */     String src = "source(\"" + mlMainPath + "\")";
/* 1018 */     mainEval = mainEval + ")";
/* 1019 */     String finalCommand = "MainDriver(\"" + outputPath + "\"" + "," + mainEval + ")";
/*      */     
/* 1021 */     System.out.println(src);
/* 1022 */     System.out.println(finalCommand);
/* 1023 */     engine.eval(src);
/* 1024 */     engine.eval(finalCommand);
/* 1025 */     engine.end();
/*      */   }
/*      */   
/*      */ 
/*      */   private void runMLThroughPython(boolean postProcess, String mainPath, String outputPath, boolean rolling, boolean append)
/*      */   {
/* 1031 */     String mainDriverPath = mainPath + "/lib/ML Filter";
/*      */     
/* 1033 */     String mainEval = "python \"" + mainDriverPath + "/MainDriver.py" + "\"";
/*      */     
/*      */ 
/* 1036 */     for (Map.Entry<String, MLAlgo> entry : this.mlPreProcessor.getAlgorithmMap().entrySet())
/*      */     {
/*      */ 
/* 1039 */       String rollingVal = rolling ? "1" : "0";
/*      */       
/* 1041 */       String appendVal = append ? "1" : "0";
/*      */       
/*      */ 
/* 1044 */       MLAlgo MlAlgo = (MLAlgo)entry.getValue();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1049 */       String mlAlgoName = MlAlgo.getModelName().split("\\.")[0];
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1055 */       String inputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Input.csv" + "\\\"";
/* 1056 */       String outputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Output.csv" + "\\\"";
/*      */       
/* 1058 */       String algoCommand = "python \\\"" + mainDriverPath + "/MLFilterMainDriver.py" + "\\\"" + " \\\"" + mainPath + 
/* 1059 */         "\\\" " + inputFile + " " + outputFile + " " + "\\\"" + (String)entry.getKey() + "\\\"" + " " + "\\\"" + 
/* 1060 */         outputPath + "\\\"" + " " + this.mlParameter.getBlackoutPeriod().intValue() + " " + this.mlParameter.getWindowPeriod() + 
/* 1061 */         " " + this.mlParameter.getUpdatePeriod() + " " + rollingVal + " " + appendVal + " " + "\\\"" + 
/* 1062 */         mlAlgoName + "\\\"";
/* 1063 */       mainEval = mainEval + " \"" + algoCommand + "\"";
/*      */     }
/*      */     
/* 1066 */     System.out.println(mainEval);
/*      */   }
/*      */   
/*      */   public TreeMap<Long, HashMap<String, HashMap<String, Double>>> readCorrelFromFile(String destPath)
/*      */     throws IOException
/*      */   {
/* 1072 */     String fileName = destPath + "/ML/DailyCorrelLog.csv";
/*      */     
/* 1074 */     TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals = new TreeMap();
/*      */     
/* 1076 */     CSVReader reader = new CSVReader(fileName, ',', 0);
/*      */     
/*      */ 
/*      */ 
/* 1080 */     String[] header1 = reader.getLine();
/* 1081 */     String[] header2 = reader.getLine();
/*      */     String[] curLine;
/* 1083 */     while ((curLine = reader.getLine()) != null) {
/*      */       String[] curLine;
/* 1085 */       Long date = Long.valueOf(Long.parseLong(curLine[0]));
/* 1086 */       HashMap<String, HashMap<String, Double>> correlMap = new HashMap();
/*      */       
/*      */ 
/* 1089 */       for (int i = 1; i < curLine.length; i++)
/*      */       {
/* 1091 */         String scrip1 = header1[i];
/* 1092 */         String scrip2 = header2[i];
/* 1093 */         Double value = Double.valueOf(Double.parseDouble(curLine[i]));
/*      */         
/* 1095 */         HashMap<String, Double> curMap = (HashMap)correlMap.get(scrip1);
/* 1096 */         if (curMap == null) {
/* 1097 */           curMap = new HashMap();
/* 1098 */           curMap.put(scrip2, value);
/* 1099 */           correlMap.put(scrip1, curMap);
/*      */         } else {
/* 1101 */           curMap.put(scrip2, value);
/* 1102 */           correlMap.put(scrip1, curMap);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1107 */       correlVals.put(date, correlMap);
/*      */     }
/*      */     
/* 1110 */     reader.close();
/*      */     
/* 1112 */     return correlVals;
/*      */   }
/*      */   
/*      */ 
/*      */   public HashMap<String, DailyDataReader> initDailyreader(ArrayList<String> scripList, String dataPath, Long dailyStartDate)
/*      */     throws Exception
/*      */   {
/* 1119 */     HashMap<String, DailyDataReader> dailyReaderCollection = new HashMap();
/*      */     
/* 1121 */     for (String scripName : scripList) {
/* 1122 */       Scrip scrip = new Scrip(scripName);
/* 1123 */       String scripListName = scripName.replace(' ', '$');
/*      */       try {
/* 1125 */         dailyReaderCollection.put(scripName, new DailyDataReader(dailyStartDate, this.btGlobal, this.backtest, scrip, scripListName));
/*      */       } catch (IOException e) {
/* 1127 */         System.out.println("Error in reading Daily file for " + scripName);
/* 1128 */         throw new Exception();
/*      */       }
/*      */     }
/*      */     
/* 1132 */     return dailyReaderCollection;
/*      */   }
/*      */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/MachineLearningMainDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */