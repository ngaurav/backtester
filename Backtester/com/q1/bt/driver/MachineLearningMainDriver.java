/*      */ package com.q1.bt.driver;
/*      */ 
/*      */ import com.q1.bt.global.BacktesterGlobal;
/*      */ import com.q1.bt.machineLearning.absclasses.MLAlgo;
/*      */ import com.q1.bt.machineLearning.absclasses.MLAlgoP;
/*      */ import com.q1.bt.machineLearning.absclasses.MLAlgoR;
/*      */ import com.q1.bt.machineLearning.absclasses.MLAlgoRA;
/*      */ import com.q1.bt.machineLearning.driver.MLInputFileGenerator;
/*      */ import com.q1.bt.machineLearning.driver.MLPostProcessor;
/*      */ import com.q1.bt.machineLearning.driver.MLPreProcessor;
/*      */ import com.q1.bt.machineLearning.driver.MLProcessFinisher;
/*      */ import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
/*      */ import com.q1.bt.machineLearning.utility.DailyDataReader;
/*      */ import com.q1.bt.machineLearning.utility.PostProcessDataWriter;
/*      */ import com.q1.bt.machineLearning.utility.TradeAndMTMDataProcessor;
/*      */ import com.q1.bt.machineLearning.utility.TradeFilteredMTMWriter;
/*      */ import com.q1.bt.machineLearning.utility.TradeFilteredTDWriter;
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
/*   42 */   public Long DAILY_START_DATE = Long.valueOf(19800101L);
/*      */   
/*      */   ArrayList<Long> dateList;
/*      */   
/*      */   MachineLearning machineLearning;
/*      */   
/*      */   MachineLearningParameter mlParameter;
/*      */   
/*      */   public Backtest backtest;
/*      */   
/*      */   public Backtest mlBacktest;
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
/*      */   private HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap;
/*      */   private HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap;
/*      */   
/*      */   public MachineLearningMainDriver(BacktesterGlobal btGlobal, MachineLearning machineLearning)
/*      */     throws IOException
/*      */   {
/*   69 */     this.btGlobal = btGlobal;
/*   70 */     this.backtest = machineLearning.getBacktest();
/*   71 */     this.dateList = btGlobal.getConsolDateList(this.backtest.timeStamp);
/*      */     
/*      */ 
/*   74 */     this.machineLearning = machineLearning;
/*   75 */     this.mlParameter = machineLearning.getMlParameter();
/*      */     
/*   77 */     this.postProcess = machineLearning.getBacktest().backtestParameter.isGenerateOutputCheck();
/*      */   }
/*      */   
/*      */   public void run()
/*      */   {
/*      */     try
/*      */     {
/*   84 */       execute();
/*      */     } catch (Exception e) {
/*   86 */       e.printStackTrace();
/*   87 */       return;
/*      */     }
/*      */     
/*   90 */     if (this.btGlobal.isGui)
/*      */     {
/*   92 */       BacktesterProcess[] choices = { BacktesterProcess.Backtest, BacktesterProcess.Results };
/*   93 */       BacktesterProcess input = (BacktesterProcess)JOptionPane.showInputDialog(null, 
/*   94 */         "Please choose the next Process", "Process Type", 3, null, 
/*      */         
/*      */ 
/*   97 */         choices, 
/*   98 */         choices[0]);
/*      */       
/*  100 */       this.btGlobal.processFlow.add(input);
/*      */       
/*      */ 
/*  103 */       this.btGlobal.displayMessage("Running Backtest on Updated Orderbook");
/*  104 */       this.btGlobal.processFlow.update();
/*  105 */       this.btGlobal.processFlow.add(BacktesterProcess.Results);
/*      */       try
/*      */       {
/*  108 */         Backtest mlBacktest = new Backtest(this.backtest.backtestParameter, 
/*  109 */           this.mlPreProcessor.getDestPath() + "/ML Order Data");
/*  110 */         mlBacktest.fileBacktest = true;
/*  111 */         mlBacktest.timeStamp = this.machineLearning.getTimeStamp();
/*  112 */         BacktestMainDriver backtestDriver = new BacktestMainDriver(this.btGlobal, mlBacktest);
/*  113 */         Thread t = new Thread(backtestDriver);
/*  114 */         t.start();
/*      */       }
/*      */       catch (Exception e) {
/*  117 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void execute()
/*      */     throws Exception
/*      */   {
/*  125 */     String backtestPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp;
/*  126 */     Integer newTimeStamp = Integer.valueOf(Integer.parseInt(this.backtest.timeStamp) + 1);
/*  127 */     this.backtest.timeStamp = newTimeStamp.toString();
/*  128 */     String newBacktestPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp;
/*  129 */     File currentFile = new File(backtestPath);
/*  130 */     File newFile = new File(newBacktestPath);
/*  131 */     FileUtils.copyDirectory(currentFile, newFile);
/*  132 */     FileUtils.deleteDirectory(currentFile);
/*      */     
/*      */ 
/*  135 */     this.machineLearning.setTimeStamp("ML" + this.backtest.timeStamp);
/*      */     
/*      */ 
/*  138 */     HashMap<String, String> mlOutputMap = new HashMap();
/*      */     try {
/*  140 */       mlOutputMap = createMLOutputMap(this.btGlobal, this.machineLearning);
/*      */     } catch (IOException e2) {
/*  142 */       e2.printStackTrace();
/*  143 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  148 */     boolean mlCheck = false;boolean mlOutputCheck = false;boolean mlInputCheck = false;
/*  149 */     mlCheck = checkMLOutput(this.btGlobal, this.machineLearning, mlOutputMap);
/*  150 */     if (!mlCheck)
/*      */     {
/*  152 */       mlOutputCheck = checkMLOOutput(this.btGlobal, this.machineLearning, mlOutputMap);
/*  153 */       if (!mlOutputCheck)
/*      */       {
/*  155 */         mlInputCheck = checkMLIOutput(this.btGlobal, this.machineLearning, mlOutputMap);
/*      */       }
/*      */     }
/*      */     
/*  159 */     if (!mlCheck)
/*      */     {
/*      */ 
/*  162 */       preProcess();
/*      */       
/*  164 */       if (!mlOutputCheck)
/*      */       {
/*  166 */         if (!mlInputCheck)
/*      */         {
/*  168 */           createInputFile();
/*      */         }
/*      */         
/*      */ 
/*  172 */         generateMLOutput();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  177 */       readOutput();
/*      */       
/*      */ 
/*  180 */       endProcess();
/*      */     }
/*      */   }
/*      */   
/*      */   public void preProcess() throws Exception
/*      */   {
/*  186 */     this.mlPreProcessor = new MLPreProcessor(this.btGlobal, this.backtest, this.machineLearning);
/*  187 */     this.mlPreProcessor.mlPreProcess();
/*      */   }
/*      */   
/*      */   public void createInputFile() throws Exception
/*      */   {
/*  192 */     this.mlInputFileGenerator = new MLInputFileGenerator(this.mlParameter);
/*      */     
/*  194 */     boolean nextLayer = false;
/*  195 */     String dataPath = this.btGlobal.loginParameter.getDataPath();
/*  196 */     TradeAndMTMDataProcessor stratTradePnL = this.mlPreProcessor.getStratTradePnL();
/*  197 */     ArrayList<String> scripUniverse = this.mlPreProcessor.getScripUniverse();
/*  198 */     HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap = this.mlPreProcessor
/*  199 */       .getModelSegmentWiseAssetUniverseMap();
/*  200 */     String sourcePath = this.mlPreProcessor.getSourcePath();
/*  201 */     String destPath = this.mlPreProcessor.getDestPath();
/*  202 */     HashMap<String, TreeMap<Long, Long>> tradeStratEndMap = this.mlPreProcessor.getTradeStartEndMaps();
/*  203 */     HashMap<String, TreeMap<Long, Integer>> tradeStratDateTradeSideMap = this.mlPreProcessor
/*  204 */       .getTradeStartDateTradeSideMaps();
/*      */     
/*  206 */     this.mlInputFileGenerator.createInputFile(nextLayer, sourcePath, destPath, dataPath, 
/*  207 */       modelSegmentWiseAssetUniverseMap, scripUniverse, stratTradePnL, this.dateList, tradeStratEndMap, 
/*  208 */       tradeStratDateTradeSideMap, this.DAILY_START_DATE);
/*      */   }
/*      */   
/*      */   public void readOutput() throws Exception
/*      */   {
/*  213 */     String sourcePath = this.mlPreProcessor.getSourcePath();
/*  214 */     String destPath = this.mlPreProcessor.getDestPath();
/*  215 */     String algoLastModifiedTimeStamp = this.mlPreProcessor.getAlgoLastModifiedTimeStamp();
/*  216 */     this.modelSegmentWiseAssetUniverseMap = this.mlPreProcessor.getModelSegmentWiseAssetUniverseMap();
/*  217 */     this.postModelSelectionSegmentWiseAssetUniverseMap = this.mlPreProcessor
/*  218 */       .getPostModelSelectionSegmentWiseAssetUniverseMap();
/*  219 */     ArrayList<String> scripUniverse = this.mlPreProcessor.getScripUniverse();
/*      */     
/*      */     HashMap<String, DailyDataReader> dailyReaderCollection;
/*      */     TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals;
/*      */     HashMap<String, DailyDataReader> dailyReaderCollection;
/*  224 */     if (this.mlInputFileGenerator == null) {
/*  225 */       String dataPath = this.btGlobal.loginParameter.getDataPath();
/*  226 */       TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals = readCorrelFromFile(destPath);
/*  227 */       dailyReaderCollection = MLInputFileGenerator.initDailyreader(scripUniverse, dataPath, this.DAILY_START_DATE);
/*      */     } else {
/*  229 */       correlVals = this.mlInputFileGenerator.getCorrelVals();
/*  230 */       dailyReaderCollection = this.mlInputFileGenerator.getDailyReaderCollection();
/*      */     }
/*      */     
/*  233 */     this.mlPostProcessor = new MLPostProcessor(sourcePath, destPath, this.modelSegmentWiseAssetUniverseMap, 
/*  234 */       this.postModelSelectionSegmentWiseAssetUniverseMap, scripUniverse, this.btGlobal.loginParameter.getDataPath(), 
/*  235 */       this.backtest, this.machineLearning, this.dateList, dailyReaderCollection, correlVals, 
/*  236 */       this.mlPreProcessor.getAlgorithmMap(), this.mlPreProcessor.getTradeStartEndMaps(), 
/*  237 */       this.mlPreProcessor.getTradeMTMMat(), algoLastModifiedTimeStamp, this.postProcess);
/*      */     
/*  239 */     this.mlPostProcessor.readOutput(sourcePath, destPath, this.postProcess, this.dateList);
/*      */   }
/*      */   
/*      */   public void endProcess() throws IOException
/*      */   {
/*  244 */     HashMap<String, MLAlgo> algorithmMap = this.mlPostProcessor.getAlgorithmMap();
/*  245 */     HashMap<String, TradeFilteredMTMWriter> mtmWriterCollection = this.mlPostProcessor.getMtmWriterCollection();
/*  246 */     HashMap<String, TradeFilteredTDWriter> tdWriterCollection = this.mlPostProcessor.getTdWriterCollection();
/*  247 */     HashMap<String, PostProcessDataWriter> postProcessWriterCollection = this.mlPostProcessor
/*  248 */       .getPostProcessWriterCOllection();
/*  249 */     HashMap<String, DailyDataReader> dailyReaderCollection = this.mlPostProcessor.getDailyReaderCollection();
/*  250 */     CSVWriter mlLogWriter = this.mlPostProcessor.getMlLogWriter();
/*  251 */     CSVWriter combinedMTM = this.mlPostProcessor.getCombinedMTM();
/*  252 */     CSVWriter correlLogWriter = this.mlPostProcessor.getCorrelLogWriter();
/*  253 */     String destPath = this.mlPreProcessor.getDestPath();
/*      */     
/*  255 */     MLProcessFinisher mlProcessFinisher = new MLProcessFinisher();
/*  256 */     mlProcessFinisher.endProcess(this.postProcess, algorithmMap, mtmWriterCollection, tdWriterCollection, 
/*  257 */       postProcessWriterCollection, dailyReaderCollection, mlLogWriter, combinedMTM, correlLogWriter, destPath, 
/*  258 */       this.modelSegmentWiseAssetUniverseMap);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static HashMap<String, String> createMLOutputMap(BacktesterGlobal btGlobal, MachineLearning machineLearning)
/*      */     throws IOException
/*      */   {
/*  267 */     HashMap<String, String> outputMap = new HashMap();
/*      */     
/*      */ 
/*  270 */     File outputFile = new File(btGlobal.loginParameter.getOutputPath());
/*  271 */     File[] folders = outputFile.listFiles();
/*  272 */     File[] arrayOfFile1; int j = (arrayOfFile1 = folders).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
/*      */       
/*  274 */       String folderTimeStamp = folder.getName();
/*  275 */       String folderPath = folder.getAbsolutePath();
/*      */       
/*      */ 
/*  278 */       if (folderTimeStamp.startsWith("ML"))
/*      */       {
/*      */ 
/*      */ 
/*  282 */         if (new File(folderPath + "/Parameters").exists())
/*      */         {
/*      */ 
/*      */ 
/*  286 */           File[] paramFiles = new File(folderPath + "/Parameters").listFiles();
/*  287 */           File[] arrayOfFile2; int m = (arrayOfFile2 = paramFiles).length; for (int k = 0; k < m; k++) { File paramFile = arrayOfFile2[k];
/*      */             
/*  289 */             String fileName = paramFile.getName();
/*  290 */             String[] fileVal = paramFile.getName().split(" ");
/*      */             
/*  292 */             if (fileName.endsWith("Parameters.csv"))
/*      */             {
/*      */ 
/*  295 */               String strategyID = fileVal[0];
/*  296 */               if (machineLearning.getBacktest().backtestMap.containsKey(strategyID))
/*      */               {
/*      */ 
/*      */ 
/*  300 */                 String paramPath = paramFile.getAbsolutePath();
/*  301 */                 String paramKey = "";
/*  302 */                 String paramKeyInput = "";
/*  303 */                 String paramKeyOutput = "";
/*      */                 
/*      */ 
/*  306 */                 CSVReader reader = new CSVReader(paramPath, ',', 0);
/*      */                 String[] parameterLine;
/*  308 */                 while ((parameterLine = reader.getLine()) != null)
/*      */                 {
/*      */                   String[] parameterLine;
/*  311 */                   String parameterName = parameterLine[0];
/*  312 */                   String parameterValue = parameterLine[1];
/*      */                   
/*      */ 
/*  315 */                   if (paramKey.equals("")) {
/*  316 */                     paramKey = parameterValue;
/*      */                   } else {
/*  318 */                     paramKey = paramKey + "$" + parameterValue;
/*      */                   }
/*      */                   
/*  321 */                   if (!parameterName.startsWith("ML ")) {
/*  322 */                     if (paramKeyInput.equals("")) {
/*  323 */                       paramKeyInput = parameterValue;
/*      */                     } else {
/*  325 */                       paramKeyInput = paramKeyInput + "$" + parameterValue;
/*      */                     }
/*      */                   }
/*      */                   
/*  329 */                   if ((!parameterLine[0].equals("ML Consolidation Function")) && 
/*  330 */                     (!parameterLine[0].equals("ML Segment Asset Count")) && 
/*  331 */                     (!parameterLine[0].equals("ML Overall Asset Count")) && 
/*  332 */                     (!parameterLine[0].equals("ML Correl Threshold"))) {
/*  333 */                     if (paramKeyOutput.equals("")) {
/*  334 */                       paramKeyOutput = parameterValue;
/*      */                     } else {
/*  336 */                       paramKeyOutput = paramKeyOutput + "$" + parameterValue;
/*      */                     }
/*      */                   }
/*      */                 }
/*  340 */                 reader.close();
/*      */                 
/*      */ 
/*  343 */                 String primaryKey = "ML " + strategyID + " " + paramKey;
/*  344 */                 String primaryKeyInput = "ML " + strategyID + " " + paramKeyInput;
/*  345 */                 String primaryKeyOutput = "ML " + strategyID + " " + paramKeyOutput;
/*      */                 
/*      */ 
/*  348 */                 TreeSet<String> factorSet = new TreeSet();
/*  349 */                 String factorPath = folderPath + "/Parameters/ML Factorlist.csv";
/*  350 */                 if (new File(factorPath).exists()) {
/*  351 */                   reader = new CSVReader(factorPath, ',', 0);
/*      */                   String[] tsLine;
/*  353 */                   while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/*  354 */                     factorSet.add(tsLine[0]); }
/*  355 */                   reader.close();
/*      */                 }
/*      */                 
/*      */ 
/*  359 */                 for (String factor : factorSet)
/*      */                 {
/*  361 */                   primaryKey = primaryKey + "$" + factor;
/*  362 */                   primaryKeyInput = primaryKeyInput + "$" + factor;
/*  363 */                   primaryKeyOutput = primaryKeyOutput + "$" + factor;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  372 */                 ArrayList<String> scripListDateList = new ArrayList();
/*  373 */                 String scripParameterPath = folderPath + "/Parameters/" + strategyID + " ScripListDateMap.csv";
/*  374 */                 if (new File(scripParameterPath).exists()) {
/*  375 */                   reader = new CSVReader(scripParameterPath, ',', 0);
/*      */                   String[] tsLine;
/*  377 */                   while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/*  378 */                     scripListDateList.add(tsLine[0] + "," + tsLine[1] + "," + tsLine[2]); }
/*  379 */                   reader.close();
/*      */                 }
/*      */                 
/*      */ 
/*  383 */                 boolean fileError = false;
/*      */                 
/*      */ 
/*  386 */                 for (String scripListDate : scripListDateList)
/*      */                 {
/*  388 */                   String[] scripListDateVal = scripListDate.split(",");
/*  389 */                   String scripListID = scripListDateVal[0];
/*      */                   
/*      */ 
/*  392 */                   String scripFileName = folderPath + "/Parameters/" + scripListID + " ScripSet.csv";
/*  393 */                   CSVReader scripReader = new CSVReader(scripFileName, ',', 0);
/*      */                   
/*      */ 
/*  396 */                   String scripKey = scripListDate;
/*  397 */                   String[] scripLine; while ((scripLine = scripReader.getLine()) != null) { String[] scripLine;
/*  398 */                     String scripID = scripLine[0];
/*      */                     
/*  400 */                     String mtmPath = folderPath + "/MTM Data/" + strategyID + " " + scripListID + "/" + scripID + 
/*  401 */                       " MTM.csv";
/*  402 */                     String tradePath = folderPath + "/Trade Data/" + strategyID + " " + scripListID + "/" + 
/*  403 */                       scripID + " Tradebook.csv";
/*      */                     
/*  405 */                     if ((!new File(mtmPath).exists()) || (!new File(tradePath).exists())) {
/*  406 */                       fileError = true;
/*  407 */                       break;
/*      */                     }
/*  409 */                     scripKey = scripKey + "|" + scripID;
/*      */                   }
/*  411 */                   scripReader.close();
/*      */                   
/*      */ 
/*  414 */                   if (fileError) {
/*      */                     break;
/*      */                   }
/*      */                   
/*  418 */                   primaryKey = primaryKey + "$" + scripKey;
/*  419 */                   primaryKeyInput = primaryKeyInput + "$" + scripKey;
/*  420 */                   primaryKeyOutput = primaryKeyOutput + "$" + scripKey;
/*      */                 }
/*      */                 
/*      */ 
/*  424 */                 if (!fileError)
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
/*  435 */                   outputMap.put(primaryKey, folderTimeStamp);
/*  436 */                   outputMap.put(primaryKeyInput, folderTimeStamp);
/*  437 */                   outputMap.put(primaryKeyOutput, folderTimeStamp);
/*      */                 }
/*      */               }
/*      */             } } } } }
/*  441 */     return outputMap;
/*      */   }
/*      */   
/*      */   public static boolean checkMLOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  447 */     boolean outputCheck = true;
/*      */     
/*  449 */     String backtestTimestamp = machineLearning.getBacktest().timeStamp;
/*      */     
/*  451 */     for (String strategyID : machineLearning.getBacktest().backtestMap.keySet())
/*      */     {
/*      */ 
/*  454 */       String backtestParameters = getBacktestParameters(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*      */ 
/*  457 */       String mlParameters = machineLearning.getMlParameter().getMLParametersAsKey();
/*      */       
/*      */ 
/*  460 */       String parameterString = backtestParameters + "$" + mlParameters;
/*      */       
/*      */ 
/*  463 */       String currentKey = "ML " + strategyID + " " + parameterString;
/*      */       
/*      */ 
/*  466 */       String scripListDate = getScripDateKey(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*  468 */       currentKey = currentKey + "$" + scripListDate;
/*      */       
/*      */ 
/*  471 */       boolean curOutputCheck = checkAndTransferML(btGlobal, machineLearning, strategyID, currentKey, mlOutputMap);
/*  472 */       if (outputCheck)
/*  473 */         outputCheck = curOutputCheck;
/*      */     }
/*  475 */     return outputCheck;
/*      */   }
/*      */   
/*      */   public static boolean checkMLIOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  481 */     boolean outputCheck = true;
/*      */     
/*  483 */     String backtestTimestamp = machineLearning.getBacktest().timeStamp;
/*      */     
/*  485 */     for (String strategyID : machineLearning.getBacktest().backtestMap.keySet())
/*      */     {
/*      */ 
/*  488 */       String backtestParameters = getBacktestParameters(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*      */ 
/*  491 */       String mlFactorParameters = machineLearning.getMlParameter().getFactorParameters();
/*      */       
/*      */ 
/*  494 */       String paramString = backtestParameters + "$" + mlFactorParameters;
/*      */       
/*      */ 
/*  497 */       String currentKey = "ML " + strategyID + " " + paramString;
/*      */       
/*      */ 
/*  500 */       String scripListDate = getScripDateKey(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*  502 */       currentKey = currentKey + "$" + scripListDate;
/*      */       
/*      */ 
/*  505 */       boolean curOutputCheck = checkAndTransferMLInput(btGlobal, machineLearning, strategyID, currentKey, 
/*  506 */         mlOutputMap);
/*  507 */       if (outputCheck)
/*  508 */         outputCheck = curOutputCheck;
/*      */     }
/*  510 */     return outputCheck;
/*      */   }
/*      */   
/*      */   public static boolean checkMLOOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  516 */     boolean outputCheck = true;
/*      */     
/*  518 */     String backtestTimestamp = machineLearning.getBacktest().timeStamp;
/*      */     
/*  520 */     for (String strategyID : machineLearning.getBacktest().backtestMap.keySet())
/*      */     {
/*      */ 
/*  523 */       String backtestParameters = getBacktestParameters(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*      */ 
/*  526 */       String mlParameters = machineLearning.getMlParameter().getMLParametersAsOutputKey();
/*      */       
/*      */ 
/*  529 */       String parameterString = backtestParameters + "$" + mlParameters;
/*      */       
/*      */ 
/*  532 */       String currentKey = "ML " + strategyID + " " + parameterString;
/*      */       
/*      */ 
/*  535 */       String scripListDate = getScripDateKey(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*  537 */       currentKey = currentKey + "$" + scripListDate;
/*      */       
/*      */ 
/*  540 */       boolean curOutputCheck = checkAndTransferMLOutput(btGlobal, machineLearning, strategyID, currentKey, 
/*  541 */         mlOutputMap);
/*  542 */       if (outputCheck)
/*  543 */         outputCheck = curOutputCheck;
/*      */     }
/*  545 */     return outputCheck;
/*      */   }
/*      */   
/*      */   public static boolean checkAndTransferML(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  551 */     String timeStamp = (String)mlOutputMap.get(currentKey);
/*  552 */     String mlTimeStamp = machineLearning.getTimeStamp();
/*      */     
/*  554 */     if (timeStamp == null) {
/*  555 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  560 */     String outputPath = btGlobal.loginParameter.getOutputPath();
/*      */     
/*  562 */     String sMTMPath = outputPath + "/" + timeStamp;
/*  563 */     String mtmPath = outputPath + "/" + mlTimeStamp;
/*  564 */     File sMTMFolder = new File(sMTMPath);
/*  565 */     File mtmFolder = new File(mtmPath);
/*  566 */     sMTMFolder.renameTo(mtmFolder);
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
/*  635 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean checkAndTransferMLOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  642 */     String timeStamp = (String)mlOutputMap.get(currentKey);
/*      */     
/*  644 */     String mlTimeStamp = machineLearning.getTimeStamp();
/*      */     
/*  646 */     if (timeStamp == null) {
/*  647 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  651 */     String outputPath = btGlobal.loginParameter.getOutputPath();
/*      */     
/*      */ 
/*  654 */     String sParamPath = outputPath + "/" + timeStamp + "/Parameters";
/*  655 */     String paramPath = outputPath + "/" + mlTimeStamp + "/Parameters";
/*  656 */     File sParamFolder = new File(sParamPath);
/*  657 */     File paramFolder = new File(paramPath);
/*  658 */     if (!paramFolder.exists())
/*  659 */       paramFolder.mkdirs();
/*  660 */     File[] paramFileList = sParamFolder.listFiles();
/*  661 */     File[] arrayOfFile1; int j = (arrayOfFile1 = paramFileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/*  662 */       String fStrategy = file.getName().split(" ")[0];
/*  663 */       if (fStrategy.equals(strategy)) {
/*  664 */         String newPath = paramPath + "/" + file.getName();
/*  665 */         btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*  667 */       if (fStrategy.equals("ML")) {
/*  668 */         String newPath = paramPath + "/" + file.getName();
/*  669 */         btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  674 */     String sMLPath = outputPath + "/" + timeStamp + "/ML";
/*  675 */     String mlPath = outputPath + "/" + mlTimeStamp + "/ML";
/*  676 */     File sMLFolder = new File(sMLPath);
/*  677 */     File mlFolder = new File(mlPath);
/*  678 */     if (!mlFolder.exists())
/*  679 */       mlFolder.mkdirs();
/*  680 */     File[] mlFileList = sMLFolder.listFiles();
/*  681 */     File[] arrayOfFile2; int m = (arrayOfFile2 = mlFileList).length; for (int k = 0; k < m; k++) { File file = arrayOfFile2[k];
/*  682 */       String newPath = mlPath + "/" + file.getName();
/*  683 */       if (!new File(newPath).exists())
/*  684 */         btGlobal.copyFile(file, new File(newPath));
/*      */     }
/*  686 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean checkAndTransferMLAOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  693 */     String timeStamp = (String)mlOutputMap.get(currentKey);
/*      */     
/*  695 */     String mlTimeStamp = machineLearning.getTimeStamp();
/*      */     
/*  697 */     if (timeStamp == null) {
/*  698 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  702 */     String outputPath = btGlobal.loginParameter.getOutputPath();
/*      */     
/*      */ 
/*  705 */     String sParamPath = outputPath + "/" + timeStamp + "/Parameters";
/*  706 */     String paramPath = outputPath + "/" + mlTimeStamp + "/Parameters";
/*  707 */     File sParamFolder = new File(sParamPath);
/*  708 */     File paramFolder = new File(paramPath);
/*  709 */     if (!paramFolder.exists())
/*  710 */       paramFolder.mkdirs();
/*  711 */     File[] paramFileList = sParamFolder.listFiles();
/*  712 */     File[] arrayOfFile1; int j = (arrayOfFile1 = paramFileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/*  713 */       String fStrategy = file.getName().split(" ")[0];
/*  714 */       if (fStrategy.equals(strategy)) {
/*  715 */         String newPath = paramPath + "/" + file.getName();
/*  716 */         btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*  718 */       if (fStrategy.equals("ML")) {
/*  719 */         String newPath = paramPath + "/" + file.getName();
/*  720 */         btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  725 */     String sMLPath = outputPath + "/" + timeStamp + "/ML";
/*  726 */     String mlPath = outputPath + "/" + mlTimeStamp + "/ML";
/*  727 */     File sMLFolder = new File(sMLPath);
/*  728 */     File mlFolder = new File(mlPath);
/*  729 */     if (!mlFolder.exists())
/*  730 */       mlFolder.mkdirs();
/*  731 */     File[] mlFileList = sMLFolder.listFiles();
/*  732 */     File[] arrayOfFile2; int m = (arrayOfFile2 = mlFileList).length; for (int k = 0; k < m; k++) { File file = arrayOfFile2[k];
/*  733 */       if (file.getName().contains(" RData")) {
/*  734 */         String newPath = mlPath + "/" + file.getName();
/*  735 */         if (!new File(newPath).exists())
/*  736 */           btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*      */     }
/*  739 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean checkAndTransferMLInput(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  746 */     String timeStamp = (String)mlOutputMap.get(currentKey);
/*      */     
/*  748 */     String mlTimeStamp = machineLearning.getTimeStamp();
/*      */     
/*  750 */     if (timeStamp == null) {
/*  751 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  755 */     String outputPath = btGlobal.loginParameter.getOutputPath();
/*      */     
/*      */ 
/*  758 */     String sMLPath = outputPath + "/" + timeStamp + "/ML";
/*  759 */     String mlPath = outputPath + "/" + mlTimeStamp + "/ML";
/*  760 */     File sMLFolder = new File(sMLPath);
/*  761 */     File mlFolder = new File(mlPath);
/*  762 */     if (!mlFolder.exists())
/*  763 */       mlFolder.mkdirs();
/*  764 */     File[] mlFileList = sMLFolder.listFiles();
/*  765 */     File[] arrayOfFile1; int j = (arrayOfFile1 = mlFileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/*  766 */       if (file.getName().contains("Input")) {
/*  767 */         String newPath = mlPath + "/" + file.getName();
/*  768 */         if (!new File(newPath).exists())
/*  769 */           btGlobal.copyFile(file, new File(newPath));
/*  770 */       } else if (file.getName().contains("DailyCorrelLog")) {
/*  771 */         String newPath = mlPath + "/" + file.getName();
/*  772 */         if (!new File(newPath).exists())
/*  773 */           btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*      */     }
/*  776 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String getBacktestParameters(BacktesterGlobal btGlobal, String strategyID, String backtestTimestamp)
/*      */     throws IOException
/*      */   {
/*  783 */     String basePath = btGlobal.loginParameter.getOutputPath() + "/" + backtestTimestamp;
/*      */     
/*  785 */     if (!new File(basePath + "/Parameters").exists()) {
/*  786 */       return null;
/*      */     }
/*      */     
/*  789 */     String paramPath = basePath + "/Parameters/" + strategyID + " Parameters.csv";
/*  790 */     String strategyParameters = "";
/*  791 */     if (new File(paramPath).exists())
/*      */     {
/*  793 */       CSVReader reader = new CSVReader(paramPath, ',', 0);
/*      */       String[] tsLine;
/*  795 */       while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/*  796 */         if (strategyParameters.equals("")) {
/*  797 */           strategyParameters = tsLine[1];
/*      */         } else
/*  799 */           strategyParameters = strategyParameters + "$" + tsLine[1];
/*      */       }
/*  801 */       reader.close();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  809 */     return strategyParameters;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String getScripDateKey(BacktesterGlobal btGlobal, String strategyID, String timestamp)
/*      */     throws IOException
/*      */   {
/*  816 */     String basePath = btGlobal.loginParameter.getOutputPath() + "/" + timestamp;
/*      */     
/*  818 */     if (!new File(basePath + "/Parameters").exists()) {
/*  819 */       return null;
/*      */     }
/*      */     
/*  822 */     ArrayList<String> scripListDateList = new ArrayList();
/*  823 */     String scripParameterPath = basePath + "/Parameters/" + strategyID + " ScripListDateMap.csv";
/*  824 */     if (new File(scripParameterPath).exists()) {
/*  825 */       CSVReader reader = new CSVReader(scripParameterPath, ',', 0);
/*      */       String[] tsLine;
/*  827 */       while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/*  828 */         scripListDateList.add(tsLine[0] + "," + tsLine[1] + "," + tsLine[2]);
/*      */       }
/*      */     }
/*      */     
/*  832 */     String scripDateKey = null;
/*  833 */     for (String scripListDate : scripListDateList)
/*      */     {
/*  835 */       String[] scripListDateVal = scripListDate.split(",");
/*  836 */       String scripListID = scripListDateVal[0];
/*      */       
/*      */ 
/*  839 */       String scripFileName = basePath + "/Parameters/" + scripListID + " ScripSet.csv";
/*  840 */       CSVReader scripReader = new CSVReader(scripFileName, ',', 0);
/*      */       
/*  842 */       boolean fileError = false;
/*      */       
/*  844 */       if (scripDateKey == null) {
/*  845 */         scripDateKey = scripListDate;
/*      */       } else
/*  847 */         scripDateKey = scripDateKey + "$" + scripListDate;
/*  848 */       String[] scripLine; while ((scripLine = scripReader.getLine()) != null) { String[] scripLine;
/*  849 */         String scripID = scripLine[0];
/*      */         
/*  851 */         String mtmPath = basePath + "/MTM Data/" + strategyID + " " + scripListID + "/" + scripID + " MTM.csv";
/*  852 */         String tradePath = basePath + "/Trade Data/" + strategyID + " " + scripListID + "/" + scripID + 
/*  853 */           " Tradebook.csv";
/*      */         
/*  855 */         if ((!new File(mtmPath).exists()) || (!new File(tradePath).exists())) {
/*  856 */           fileError = true;
/*  857 */           break;
/*      */         }
/*  859 */         scripDateKey = scripDateKey + "|" + scripID;
/*      */       }
/*      */       
/*      */ 
/*  863 */       if (!fileError) {}
/*      */     }
/*      */     
/*      */ 
/*  867 */     return scripDateKey;
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
/*  891 */     String destPath = this.mlPreProcessor.getDestPath();
/*      */     
/*      */ 
/*  894 */     if (!(this.mlParameter.getMlAlgorithm() instanceof MLAlgoRA))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  899 */       if ((this.mlParameter.getMlAlgorithm() instanceof MLAlgoR))
/*      */       {
/*  901 */         boolean rolling = true;
/*      */         
/*  903 */         if (this.mlParameter.getLookbackType().equals(LookbackType.Hinged)) {
/*  904 */           rolling = false;
/*      */         }
/*      */         
/*  907 */         boolean postProcess = this.machineLearning.getBacktest().backtestParameter.isGenerateOutputCheck();
/*  908 */         boolean append = false;
/*      */         
/*  910 */         runMLThroughR(postProcess, this.btGlobal.loginParameter.getMainPath(), destPath, rolling, append);
/*      */ 
/*      */ 
/*      */       }
/*  914 */       else if ((this.mlParameter.getMlAlgorithm() instanceof MLAlgoP)) {
/*  915 */         boolean rolling = true;
/*      */         
/*  917 */         if (this.mlParameter.getLookbackType().equals(LookbackType.Hinged)) {
/*  918 */           rolling = false;
/*      */         }
/*      */         
/*  921 */         boolean postProcess = this.machineLearning.getBacktest().backtestParameter.isGenerateOutputCheck();
/*  922 */         boolean append = false;
/*      */         
/*  924 */         runMLThroughPython(postProcess, this.btGlobal.loginParameter.getMainPath(), destPath, rolling, append);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*  930 */         throw new RuntimeException("Invalid Object Type mlAlgo not typeCasted");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void runMLThroughR(boolean postProcess, String mainPath, String outputPath, boolean rolling, boolean append)
/*      */   {
/*  939 */     String[] newargs1 = { "--no-save" };
/*  940 */     Rengine engine = Rengine.getMainEngine();
/*  941 */     if (engine == null) {
/*  942 */       engine = new Rengine(newargs1, false, null);
/*      */     }
/*      */     
/*  945 */     String mainEval = "c(";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  950 */     for (Map.Entry<String, MLAlgo> entry : this.mlPreProcessor.getAlgorithmMap().entrySet())
/*      */     {
/*      */ 
/*  953 */       String mainDriverPath = mainPath + "/lib/ML Filter/MLFilterMainDriver.R";
/*      */       
/*      */ 
/*  956 */       MLAlgoR Mlr = (MLAlgoR)entry.getValue();
/*  957 */       String packageLoc = Mlr.getModelPackage().replaceAll("\\.", "/");
/*  958 */       String packagePath = mainPath + "/src/" + packageLoc;
/*  959 */       String algoPath = packagePath + "/" + Mlr.getModelName();
/*  960 */       String mlAlgoName = Mlr.getModelName().split("\\.")[0];
/*      */       
/*      */ 
/*  963 */       String src1 = "source(\\\"" + algoPath + "\\\")";
/*  964 */       String src2 = "source(\\\"" + mainDriverPath + "\\\")";
/*      */       
/*  966 */       System.out.println("source(\\\"" + algoPath + "\\\")");
/*  967 */       System.out.println("source(\\\"" + mainDriverPath + "\\\")");
/*      */       
/*  969 */       String rollingVal = rolling ? "TRUE" : "FALSE";
/*      */       
/*  971 */       String appendVal = append ? "TRUE" : "FALSE";
/*      */       
/*  973 */       String inputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Input.csv" + "\\\"";
/*  974 */       String outputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Output.csv" + "\\\"";
/*      */       
/*  976 */       String algoCommand = "MLFilterMainDriver(\\\"" + packagePath + "\\\"" + ", " + inputFile + ", " + 
/*  977 */         outputFile + ", " + "\\\"" + (String)entry.getKey() + "\\\"" + "," + "\\\"" + outputPath + "\\\"" + ", " + 
/*  978 */         this.mlParameter.getBlackoutPeriod() + ", " + this.mlParameter.getWindowPeriod() + ", " + 
/*  979 */         this.mlParameter.getUpdatePeriod() + ", " + rollingVal + ", " + appendVal + ", " + mlAlgoName;
/*  980 */       ArrayList<String[]> paramList = Mlr.getParameterList();
/*  981 */       for (String[] param : paramList) {
/*  982 */         algoCommand = algoCommand + "," + param[1];
/*      */       }
/*  984 */       algoCommand = algoCommand + ")";
/*      */       
/*  986 */       System.out.println(algoCommand);
/*      */       
/*  988 */       System.out.println("\n");
/*      */       
/*      */ 
/*  991 */       if (mainEval.equals("c(")) {
/*  992 */         mainEval = mainEval + "\"" + src1 + "\\n" + src2 + "\\n" + algoCommand + "\"";
/*      */       }
/*      */       else {
/*  995 */         mainEval = mainEval + "," + "\"" + src1 + "\\n" + src2 + "\\n" + algoCommand + "\"";
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1000 */     String mlMainPath = mainPath + "/lib/ML Filter/MainDriver.R";
/* 1001 */     String src = "source(\"" + mlMainPath + "\")";
/* 1002 */     mainEval = mainEval + ")";
/* 1003 */     String finalCommand = "MainDriver(\"" + outputPath + "\"" + "," + mainEval + ")";
/*      */     
/* 1005 */     System.out.println(src);
/* 1006 */     System.out.println(finalCommand);
/* 1007 */     engine.eval(src);
/* 1008 */     engine.eval(finalCommand);
/* 1009 */     engine.end();
/*      */   }
/*      */   
/*      */ 
/*      */   private void runMLThroughPython(boolean postProcess, String mainPath, String outputPath, boolean rolling, boolean append)
/*      */   {
/* 1015 */     String mainDriverPath = mainPath + "/lib/ML Filter";
/*      */     
/* 1017 */     String mainEval = "python \"" + mainDriverPath + "/MainDriver.py" + "\"";
/*      */     
/*      */ 
/* 1020 */     for (Map.Entry<String, MLAlgo> entry : this.mlPreProcessor.getAlgorithmMap().entrySet())
/*      */     {
/*      */ 
/* 1023 */       String rollingVal = rolling ? "1" : "0";
/*      */       
/* 1025 */       String appendVal = append ? "1" : "0";
/*      */       
/*      */ 
/* 1028 */       MLAlgo MlAlgo = (MLAlgo)entry.getValue();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1033 */       String mlAlgoName = MlAlgo.getModelName().split("\\.")[0];
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1039 */       String inputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Input.csv" + "\\\"";
/* 1040 */       String outputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Output.csv" + "\\\"";
/*      */       
/* 1042 */       String algoCommand = "python \\\"" + mainDriverPath + "/MLFilterMainDriver.py" + "\\\"" + " \\\"" + mainPath + 
/* 1043 */         "\\\" " + inputFile + " " + outputFile + " " + "\\\"" + (String)entry.getKey() + "\\\"" + " " + "\\\"" + 
/* 1044 */         outputPath + "\\\"" + " " + this.mlParameter.getBlackoutPeriod().intValue() + " " + this.mlParameter.getWindowPeriod() + 
/* 1045 */         " " + this.mlParameter.getUpdatePeriod() + " " + rollingVal + " " + appendVal + " " + "\\\"" + 
/* 1046 */         mlAlgoName + "\\\"";
/* 1047 */       mainEval = mainEval + " \"" + algoCommand + "\"";
/*      */     }
/*      */     
/* 1050 */     System.out.println(mainEval);
/*      */   }
/*      */   
/*      */ 
/*      */   public TreeMap<Long, HashMap<String, HashMap<String, Double>>> readCorrelFromFile(String destPath)
/*      */     throws IOException
/*      */   {
/* 1057 */     String fileName = destPath + "/ML/DailyCorrelLog.csv";
/*      */     
/* 1059 */     TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals = new TreeMap();
/*      */     
/* 1061 */     CSVReader reader = new CSVReader(fileName, ',', 0);
/*      */     
/*      */ 
/*      */ 
/* 1065 */     String[] header1 = reader.getLine();
/* 1066 */     String[] header2 = reader.getLine();
/*      */     String[] curLine;
/* 1068 */     while ((curLine = reader.getLine()) != null) {
/*      */       String[] curLine;
/* 1070 */       Long date = Long.valueOf(Long.parseLong(curLine[0]));
/* 1071 */       HashMap<String, HashMap<String, Double>> correlMap = new HashMap();
/*      */       
/*      */ 
/* 1074 */       for (int i = 1; i < curLine.length; i++)
/*      */       {
/* 1076 */         String scrip1 = header1[i];
/* 1077 */         String scrip2 = header2[i];
/* 1078 */         Double value = Double.valueOf(Double.parseDouble(curLine[i]));
/*      */         
/* 1080 */         HashMap<String, Double> curMap = (HashMap)correlMap.get(scrip1);
/* 1081 */         if (curMap == null) {
/* 1082 */           curMap = new HashMap();
/* 1083 */           curMap.put(scrip2, value);
/* 1084 */           correlMap.put(scrip1, curMap);
/*      */         } else {
/* 1086 */           curMap.put(scrip2, value);
/* 1087 */           correlMap.put(scrip1, curMap);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1092 */       correlVals.put(date, correlMap);
/*      */     }
/*      */     
/* 1095 */     reader.close();
/*      */     
/* 1097 */     return correlVals;
/*      */   }
/*      */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/MachineLearningMainDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */