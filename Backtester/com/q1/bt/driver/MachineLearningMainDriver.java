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
/*      */ import org.apache.commons.io.FileUtils;
/*      */ import org.rosuda.JRI.Rengine;
/*      */ 
/*      */ public class MachineLearningMainDriver implements Runnable
/*      */ {
/*   38 */   public Long DAILY_START_DATE = Long.valueOf(19800101L);
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
/*   64 */     this.btGlobal = btGlobal;
/*   65 */     this.backtest = machineLearning.getBacktest();
/*   66 */     this.dateList = btGlobal.getConsolDateList(this.backtest.timeStamp);
/*      */     
/*      */ 
/*   69 */     this.machineLearning = machineLearning;
/*   70 */     this.mlParameter = machineLearning.getMlParameter();
/*      */     
/*   72 */     this.postProcess = machineLearning.getBacktest().backtestParameter.isGenerateOutputCheck();
/*      */   }
/*      */   
/*      */   public void run()
/*      */   {
/*      */     try
/*      */     {
/*   79 */       execute();
/*      */     } catch (Exception e) {
/*   81 */       e.printStackTrace();
/*   82 */       return;
/*      */     }
/*      */     
/*   85 */     if (this.btGlobal.isGui)
/*      */     {
/*   87 */       BacktesterProcess[] choices = { BacktesterProcess.Backtest, BacktesterProcess.Results };
/*   88 */       BacktesterProcess input = (BacktesterProcess)javax.swing.JOptionPane.showInputDialog(null, 
/*   89 */         "Please choose the next Process", "Process Type", 3, null, 
/*   90 */         choices, 
/*   91 */         choices[0]);
/*      */       
/*   93 */       this.btGlobal.processFlow.add(input);
/*      */       
/*      */ 
/*   96 */       if (input.equals(BacktesterProcess.Backtest))
/*      */       {
/*      */ 
/*   99 */         this.btGlobal.displayMessage("Running Backtest on Updated Orderbook");
/*  100 */         this.btGlobal.processFlow.update();
/*  101 */         this.btGlobal.processFlow.add(BacktesterProcess.Results);
/*      */         try
/*      */         {
/*  104 */           Backtest mlBacktest = new Backtest(this.backtest.backtestParameter, 
/*  105 */             this.mlPreProcessor.getDestPath() + "/ML Order Data");
/*  106 */           mlBacktest.fileBacktest = true;
/*  107 */           mlBacktest.timeStamp = this.machineLearning.getTimeStamp();
/*  108 */           BacktestMainDriver backtestDriver = new BacktestMainDriver(this.btGlobal, mlBacktest);
/*  109 */           Thread t = new Thread(backtestDriver);
/*  110 */           t.start();
/*      */         }
/*      */         catch (Exception e) {
/*  113 */           e.printStackTrace();
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*  118 */       else if (input.equals(BacktesterProcess.Results)) {
/*  119 */         this.btGlobal.processFlow.update();
/*  120 */         this.btGlobal.initializeProcess(this.machineLearning);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void execute() throws Exception
/*      */   {
/*  127 */     String backtestPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp;
/*  128 */     Integer newTimeStamp = Integer.valueOf(Integer.parseInt(this.backtest.timeStamp) + 1);
/*  129 */     this.backtest.timeStamp = newTimeStamp.toString();
/*  130 */     String newBacktestPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp;
/*  131 */     File currentFile = new File(backtestPath);
/*  132 */     File newFile = new File(newBacktestPath);
/*  133 */     FileUtils.copyDirectory(currentFile, newFile);
/*  134 */     FileUtils.deleteDirectory(currentFile);
/*      */     
/*      */ 
/*  137 */     this.machineLearning.setTimeStamp("ML" + this.backtest.timeStamp);
/*      */     
/*      */ 
/*  140 */     HashMap<String, String> mlOutputMap = new HashMap();
/*      */     try {
/*  142 */       mlOutputMap = createMLOutputMap(this.btGlobal, this.machineLearning);
/*      */     } catch (IOException e2) {
/*  144 */       e2.printStackTrace();
/*  145 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  150 */     boolean mlCheck = false;boolean mlOutputCheck = false;boolean mlInputCheck = false;
/*  151 */     mlCheck = checkMLOutput(this.btGlobal, this.machineLearning, mlOutputMap);
/*  152 */     if (!mlCheck)
/*      */     {
/*  154 */       mlOutputCheck = checkMLOOutput(this.btGlobal, this.machineLearning, mlOutputMap);
/*  155 */       if (!mlOutputCheck)
/*      */       {
/*  157 */         mlInputCheck = checkMLIOutput(this.btGlobal, this.machineLearning, mlOutputMap);
/*      */       }
/*      */     }
/*      */     
/*  161 */     if (!mlCheck)
/*      */     {
/*      */ 
/*  164 */       preProcess();
/*      */       
/*  166 */       if (!mlOutputCheck)
/*      */       {
/*  168 */         if (!mlInputCheck)
/*      */         {
/*  170 */           createInputFile();
/*      */         }
/*      */         
/*      */ 
/*  174 */         generateMLOutput();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  179 */       readOutput();
/*      */       
/*      */ 
/*  182 */       endProcess();
/*      */     }
/*      */   }
/*      */   
/*      */   public void preProcess() throws Exception
/*      */   {
/*  188 */     this.mlPreProcessor = new MLPreProcessor(this.btGlobal, this.backtest, this.machineLearning);
/*  189 */     this.mlPreProcessor.mlPreProcess();
/*      */   }
/*      */   
/*      */   public void createInputFile() throws Exception
/*      */   {
/*  194 */     ArrayList<String> scripUniverse = this.mlPreProcessor.getScripUniverse();
/*  195 */     String dataPath = this.btGlobal.loginParameter.getDataPath();
/*      */     
/*  197 */     HashMap<String, DailyDataReader> dailyReaderCollection = initDailyreader(scripUniverse, dataPath, this.DAILY_START_DATE);
/*  198 */     this.mlInputFileGenerator = new MLInputFileGenerator(this.btGlobal, this.backtest, this.mlParameter, dailyReaderCollection, this.mlParameter.getBias().booleanValue());
/*      */     
/*  200 */     boolean nextLayer = false;
/*  201 */     TradeAndMTMDataProcessor stratTradePnL = this.mlPreProcessor.getStratTradePnL();
/*  202 */     HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap = this.mlPreProcessor.getModelSegmentWiseAssetUniverseMap();
/*  203 */     String sourcePath = this.mlPreProcessor.getSourcePath();
/*  204 */     String destPath = this.mlPreProcessor.getDestPath();
/*  205 */     HashMap<String, TreeMap<Long, Long>> tradeStratEndMap = this.mlPreProcessor.getTradeStartEndMaps();
/*  206 */     HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMap = this.mlPreProcessor.getTradeStartDateTradeSideMaps();
/*      */     
/*  208 */     this.mlInputFileGenerator.createInputFile(nextLayer, sourcePath, destPath, dataPath, modelSegmentWiseAssetUniverseMap, 
/*  209 */       scripUniverse, stratTradePnL, this.dateList, tradeStratEndMap, tradeStartDateTradeSideMap, this.DAILY_START_DATE);
/*      */   }
/*      */   
/*      */   public void readOutput() throws Exception
/*      */   {
/*  214 */     String sourcePath = this.mlPreProcessor.getSourcePath();
/*  215 */     String destPath = this.mlPreProcessor.getDestPath();
/*  216 */     String algoLastModifiedTimeStamp = this.mlPreProcessor.getAlgoLastModifiedTimeStamp();
/*  217 */     this.modelSegmentWiseAssetUniverseMap = this.mlPreProcessor.getModelSegmentWiseAssetUniverseMap();
/*  218 */     this.postModelSelectionSegmentWiseAssetUniverseMap = this.mlPreProcessor.getPostModelSelectionSegmentWiseAssetUniverseMap();
/*  219 */     ArrayList<String> scripUniverse = this.mlPreProcessor.getScripUniverse();
/*      */     
/*      */     HashMap<String, DailyDataReader> dailyReaderCollection;
/*      */     TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals;
/*      */     HashMap<String, DailyDataReader> dailyReaderCollection;
/*  224 */     if (this.mlInputFileGenerator == null)
/*      */     {
/*  226 */       String dataPath = this.btGlobal.loginParameter.getDataPath();
/*  227 */       TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals = readCorrelFromFile(destPath);
/*  228 */       dailyReaderCollection = initDailyreader(scripUniverse, dataPath, this.DAILY_START_DATE);
/*      */     }
/*      */     else
/*      */     {
/*  232 */       correlVals = this.mlInputFileGenerator.getCorrelVals();
/*  233 */       dailyReaderCollection = this.mlInputFileGenerator.getDailyReaderCollection();
/*      */     }
/*      */     
/*  236 */     this.mlPostProcessor = new MLPostProcessor(sourcePath, destPath, this.modelSegmentWiseAssetUniverseMap, this.postModelSelectionSegmentWiseAssetUniverseMap, scripUniverse, 
/*  237 */       this.btGlobal.loginParameter.getDataPath(), this.backtest, this.machineLearning, this.dateList, 
/*  238 */       dailyReaderCollection, correlVals, 
/*  239 */       this.mlPreProcessor.getAlgorithmMap(), this.mlPreProcessor.getTradeStartEndMaps(), this.mlPreProcessor.getTradeStartDateTradeSideMaps(), 
/*  240 */       this.mlPreProcessor.getTradeMTMMat(), algoLastModifiedTimeStamp, this.postProcess, this.mlParameter.getBias().booleanValue());
/*      */     
/*  242 */     this.mlPostProcessor.readOutput(sourcePath, destPath, this.postProcess, this.dateList);
/*      */   }
/*      */   
/*      */   public void endProcess() throws IOException
/*      */   {
/*  247 */     HashMap<String, MLAlgo> algorithmMap = this.mlPostProcessor.getAlgorithmMap();
/*  248 */     CSVWriter mlLogWriter = this.mlPostProcessor.getMlLogWriter();
/*  249 */     CSVWriter correlLogWriter = this.mlPostProcessor.getCorrelLogWriter();
/*      */     try
/*      */     {
/*  252 */       for (MLAlgo mlalgo : algorithmMap.values()) {
/*  253 */         mlalgo.close();
/*      */       }
/*      */       
/*  256 */       mlLogWriter.close();
/*  257 */       correlLogWriter.close();
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
/*  268 */     HashMap<String, String> outputMap = new HashMap();
/*      */     
/*      */ 
/*  271 */     File outputFile = new File(btGlobal.loginParameter.getOutputPath());
/*  272 */     File[] folders = outputFile.listFiles();
/*  273 */     File[] arrayOfFile1; int j = (arrayOfFile1 = folders).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
/*      */       
/*  275 */       String folderTimeStamp = folder.getName();
/*  276 */       String folderPath = folder.getAbsolutePath();
/*      */       
/*      */ 
/*  279 */       if (folderTimeStamp.startsWith("ML"))
/*      */       {
/*      */ 
/*      */ 
/*  283 */         if (new File(folderPath + "/Parameters").exists())
/*      */         {
/*      */ 
/*      */ 
/*  287 */           File[] paramFiles = new File(folderPath + "/Parameters").listFiles();
/*  288 */           File[] arrayOfFile2; int m = (arrayOfFile2 = paramFiles).length; for (int k = 0; k < m; k++) { File paramFile = arrayOfFile2[k];
/*      */             
/*  290 */             String fileName = paramFile.getName();
/*  291 */             String[] fileVal = paramFile.getName().split(" ");
/*      */             
/*  293 */             if (fileName.endsWith("Parameters.csv"))
/*      */             {
/*      */ 
/*  296 */               String strategyID = fileVal[0];
/*  297 */               if (machineLearning.getBacktest().backtestMap.containsKey(strategyID))
/*      */               {
/*      */ 
/*      */ 
/*  301 */                 String paramPath = paramFile.getAbsolutePath();
/*  302 */                 String paramKey = "";
/*  303 */                 String paramKeyInput = "";
/*  304 */                 String paramKeyOutput = "";
/*      */                 
/*      */ 
/*  307 */                 CSVReader reader = new CSVReader(paramPath, ',', 0);
/*      */                 String[] parameterLine;
/*  309 */                 while ((parameterLine = reader.getLine()) != null)
/*      */                 {
/*      */                   String[] parameterLine;
/*  312 */                   String parameterName = parameterLine[0];
/*  313 */                   String parameterValue = parameterLine[1];
/*      */                   
/*      */ 
/*  316 */                   if (paramKey.equals("")) {
/*  317 */                     paramKey = parameterValue;
/*      */                   } else {
/*  319 */                     paramKey = paramKey + "$" + parameterValue;
/*      */                   }
/*      */                   
/*  322 */                   if (!parameterName.startsWith("ML ")) {
/*  323 */                     if (paramKeyInput.equals("")) {
/*  324 */                       paramKeyInput = parameterValue;
/*      */                     } else {
/*  326 */                       paramKeyInput = paramKeyInput + "$" + parameterValue;
/*      */                     }
/*      */                   }
/*      */                   
/*  330 */                   if ((!parameterLine[0].equals("ML Consolidation Function")) && 
/*  331 */                     (!parameterLine[0].equals("ML Segment Asset Count")) && 
/*  332 */                     (!parameterLine[0].equals("ML Overall Asset Count")) && 
/*  333 */                     (!parameterLine[0].equals("ML Correl Threshold"))) {
/*  334 */                     if (paramKeyOutput.equals("")) {
/*  335 */                       paramKeyOutput = parameterValue;
/*      */                     } else {
/*  337 */                       paramKeyOutput = paramKeyOutput + "$" + parameterValue;
/*      */                     }
/*      */                   }
/*      */                 }
/*  341 */                 reader.close();
/*      */                 
/*      */ 
/*  344 */                 String primaryKey = "ML " + strategyID + " " + paramKey;
/*  345 */                 String primaryKeyInput = "ML " + strategyID + " " + paramKeyInput;
/*  346 */                 String primaryKeyOutput = "ML " + strategyID + " " + paramKeyOutput;
/*      */                 
/*      */ 
/*  349 */                 TreeSet<String> factorSet = new TreeSet();
/*  350 */                 String factorPath = folderPath + "/Parameters/ML Factorlist.csv";
/*  351 */                 if (new File(factorPath).exists()) {
/*  352 */                   reader = new CSVReader(factorPath, ',', 0);
/*      */                   String[] tsLine;
/*  354 */                   while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/*  355 */                     factorSet.add(tsLine[0]); }
/*  356 */                   reader.close();
/*      */                 }
/*      */                 
/*      */ 
/*  360 */                 for (String factor : factorSet)
/*      */                 {
/*  362 */                   primaryKey = primaryKey + "$" + factor;
/*  363 */                   primaryKeyInput = primaryKeyInput + "$" + factor;
/*  364 */                   primaryKeyOutput = primaryKeyOutput + "$" + factor;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  373 */                 ArrayList<String> scripListDateList = new ArrayList();
/*  374 */                 String scripParameterPath = folderPath + "/Parameters/" + strategyID + " ScripListDateMap.csv";
/*  375 */                 if (new File(scripParameterPath).exists()) {
/*  376 */                   reader = new CSVReader(scripParameterPath, ',', 0);
/*      */                   String[] tsLine;
/*  378 */                   while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/*  379 */                     scripListDateList.add(tsLine[0] + "," + tsLine[1] + "," + tsLine[2]); }
/*  380 */                   reader.close();
/*      */                 }
/*      */                 
/*      */ 
/*  384 */                 boolean fileError = false;
/*      */                 
/*      */ 
/*  387 */                 for (String scripListDate : scripListDateList)
/*      */                 {
/*  389 */                   String[] scripListDateVal = scripListDate.split(",");
/*  390 */                   String scripListID = scripListDateVal[0];
/*      */                   
/*      */ 
/*  393 */                   String scripFileName = folderPath + "/Parameters/" + scripListID + " ScripSet.csv";
/*  394 */                   CSVReader scripReader = new CSVReader(scripFileName, ',', 0);
/*      */                   
/*      */ 
/*  397 */                   String scripKey = scripListDate;
/*  398 */                   String[] scripLine; while ((scripLine = scripReader.getLine()) != null) { String[] scripLine;
/*  399 */                     String scripID = scripLine[0];
/*      */                     
/*  401 */                     String mtmPath = folderPath + "/MTM Data/" + strategyID + " " + scripListID + "/" + scripID + 
/*  402 */                       " MTM.csv";
/*  403 */                     String tradePath = folderPath + "/Trade Data/" + strategyID + " " + scripListID + "/" + 
/*  404 */                       scripID + " Tradebook.csv";
/*      */                     
/*  406 */                     if ((!new File(mtmPath).exists()) || (!new File(tradePath).exists())) {
/*  407 */                       fileError = true;
/*  408 */                       break;
/*      */                     }
/*  410 */                     scripKey = scripKey + "|" + scripID;
/*      */                   }
/*  412 */                   scripReader.close();
/*      */                   
/*      */ 
/*  415 */                   if (fileError) {
/*      */                     break;
/*      */                   }
/*      */                   
/*  419 */                   primaryKey = primaryKey + "$" + scripKey;
/*  420 */                   primaryKeyInput = primaryKeyInput + "$" + scripKey;
/*  421 */                   primaryKeyOutput = primaryKeyOutput + "$" + scripKey;
/*      */                 }
/*      */                 
/*      */ 
/*  425 */                 if (!fileError)
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
/*  436 */                   outputMap.put(primaryKey, folderTimeStamp);
/*  437 */                   outputMap.put(primaryKeyInput, folderTimeStamp);
/*  438 */                   outputMap.put(primaryKeyOutput, folderTimeStamp);
/*      */                 }
/*      */               }
/*      */             } } } } }
/*  442 */     return outputMap;
/*      */   }
/*      */   
/*      */   public static boolean checkMLOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  448 */     boolean outputCheck = true;
/*      */     
/*  450 */     String backtestTimestamp = machineLearning.getBacktest().timeStamp;
/*      */     
/*  452 */     for (String strategyID : machineLearning.getBacktest().backtestMap.keySet())
/*      */     {
/*      */ 
/*  455 */       String backtestParameters = getBacktestParameters(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*      */ 
/*  458 */       String mlParameters = machineLearning.getMlParameter().getMLParametersAsKey();
/*      */       
/*      */ 
/*  461 */       String parameterString = backtestParameters + "$" + mlParameters;
/*      */       
/*      */ 
/*  464 */       String currentKey = "ML " + strategyID + " " + parameterString;
/*      */       
/*      */ 
/*  467 */       String scripListDate = getScripDateKey(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*  469 */       currentKey = currentKey + "$" + scripListDate;
/*      */       
/*      */ 
/*  472 */       boolean curOutputCheck = checkAndTransferML(btGlobal, machineLearning, strategyID, currentKey, mlOutputMap);
/*  473 */       if (outputCheck)
/*  474 */         outputCheck = curOutputCheck;
/*      */     }
/*  476 */     return outputCheck;
/*      */   }
/*      */   
/*      */   public static boolean checkMLIOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  482 */     boolean outputCheck = true;
/*      */     
/*  484 */     String backtestTimestamp = machineLearning.getBacktest().timeStamp;
/*      */     
/*  486 */     for (String strategyID : machineLearning.getBacktest().backtestMap.keySet())
/*      */     {
/*      */ 
/*  489 */       String backtestParameters = getBacktestParameters(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*      */ 
/*  492 */       String mlFactorParameters = machineLearning.getMlParameter().getFactorParameters();
/*      */       
/*      */ 
/*  495 */       String paramString = backtestParameters + "$" + mlFactorParameters;
/*      */       
/*      */ 
/*  498 */       String currentKey = "ML " + strategyID + " " + paramString;
/*      */       
/*      */ 
/*  501 */       String scripListDate = getScripDateKey(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*  503 */       currentKey = currentKey + "$" + scripListDate;
/*      */       
/*      */ 
/*  506 */       boolean curOutputCheck = checkAndTransferMLInput(btGlobal, machineLearning, strategyID, currentKey, 
/*  507 */         mlOutputMap);
/*  508 */       if (outputCheck)
/*  509 */         outputCheck = curOutputCheck;
/*      */     }
/*  511 */     return outputCheck;
/*      */   }
/*      */   
/*      */   public static boolean checkMLOOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  517 */     boolean outputCheck = true;
/*      */     
/*  519 */     String backtestTimestamp = machineLearning.getBacktest().timeStamp;
/*      */     
/*  521 */     for (String strategyID : machineLearning.getBacktest().backtestMap.keySet())
/*      */     {
/*      */ 
/*  524 */       String backtestParameters = getBacktestParameters(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*      */ 
/*  527 */       String mlParameters = machineLearning.getMlParameter().getMLParametersAsOutputKey();
/*      */       
/*      */ 
/*  530 */       String parameterString = backtestParameters + "$" + mlParameters;
/*      */       
/*      */ 
/*  533 */       String currentKey = "ML " + strategyID + " " + parameterString;
/*      */       
/*      */ 
/*  536 */       String scripListDate = getScripDateKey(btGlobal, strategyID, backtestTimestamp);
/*      */       
/*  538 */       currentKey = currentKey + "$" + scripListDate;
/*      */       
/*      */ 
/*  541 */       boolean curOutputCheck = checkAndTransferMLOutput(btGlobal, machineLearning, strategyID, currentKey, 
/*  542 */         mlOutputMap);
/*  543 */       if (outputCheck)
/*  544 */         outputCheck = curOutputCheck;
/*      */     }
/*  546 */     return outputCheck;
/*      */   }
/*      */   
/*      */   public static boolean checkAndTransferML(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  552 */     String timeStamp = (String)mlOutputMap.get(currentKey);
/*  553 */     String mlTimeStamp = machineLearning.getTimeStamp();
/*      */     
/*  555 */     if (timeStamp == null) {
/*  556 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  561 */     String outputPath = btGlobal.loginParameter.getOutputPath();
/*      */     
/*  563 */     String sMTMPath = outputPath + "/" + timeStamp;
/*  564 */     String mtmPath = outputPath + "/" + mlTimeStamp;
/*  565 */     File sMTMFolder = new File(sMTMPath);
/*  566 */     File mtmFolder = new File(mtmPath);
/*  567 */     sMTMFolder.renameTo(mtmFolder);
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
/*  636 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean checkAndTransferMLOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  643 */     String timeStamp = (String)mlOutputMap.get(currentKey);
/*      */     
/*  645 */     String mlTimeStamp = machineLearning.getTimeStamp();
/*      */     
/*  647 */     if (timeStamp == null) {
/*  648 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  652 */     String outputPath = btGlobal.loginParameter.getOutputPath();
/*      */     
/*      */ 
/*  655 */     String sParamPath = outputPath + "/" + timeStamp + "/Parameters";
/*  656 */     String paramPath = outputPath + "/" + mlTimeStamp + "/Parameters";
/*  657 */     File sParamFolder = new File(sParamPath);
/*  658 */     File paramFolder = new File(paramPath);
/*  659 */     if (!paramFolder.exists())
/*  660 */       paramFolder.mkdirs();
/*  661 */     File[] paramFileList = sParamFolder.listFiles();
/*  662 */     File[] arrayOfFile1; int j = (arrayOfFile1 = paramFileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/*  663 */       String fStrategy = file.getName().split(" ")[0];
/*  664 */       if (fStrategy.equals(strategy)) {
/*  665 */         String newPath = paramPath + "/" + file.getName();
/*  666 */         btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*  668 */       if (fStrategy.equals("ML")) {
/*  669 */         String newPath = paramPath + "/" + file.getName();
/*  670 */         btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  675 */     String sMLPath = outputPath + "/" + timeStamp + "/ML";
/*  676 */     String mlPath = outputPath + "/" + mlTimeStamp + "/ML";
/*  677 */     File sMLFolder = new File(sMLPath);
/*  678 */     File mlFolder = new File(mlPath);
/*  679 */     if (!mlFolder.exists())
/*  680 */       mlFolder.mkdirs();
/*  681 */     File[] mlFileList = sMLFolder.listFiles();
/*  682 */     File[] arrayOfFile2; int m = (arrayOfFile2 = mlFileList).length; for (int k = 0; k < m; k++) { File file = arrayOfFile2[k];
/*  683 */       String newPath = mlPath + "/" + file.getName();
/*  684 */       if (!new File(newPath).exists())
/*  685 */         btGlobal.copyFile(file, new File(newPath));
/*      */     }
/*  687 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean checkAndTransferMLAOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  694 */     String timeStamp = (String)mlOutputMap.get(currentKey);
/*      */     
/*  696 */     String mlTimeStamp = machineLearning.getTimeStamp();
/*      */     
/*  698 */     if (timeStamp == null) {
/*  699 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  703 */     String outputPath = btGlobal.loginParameter.getOutputPath();
/*      */     
/*      */ 
/*  706 */     String sParamPath = outputPath + "/" + timeStamp + "/Parameters";
/*  707 */     String paramPath = outputPath + "/" + mlTimeStamp + "/Parameters";
/*  708 */     File sParamFolder = new File(sParamPath);
/*  709 */     File paramFolder = new File(paramPath);
/*  710 */     if (!paramFolder.exists())
/*  711 */       paramFolder.mkdirs();
/*  712 */     File[] paramFileList = sParamFolder.listFiles();
/*  713 */     File[] arrayOfFile1; int j = (arrayOfFile1 = paramFileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/*  714 */       String fStrategy = file.getName().split(" ")[0];
/*  715 */       if (fStrategy.equals(strategy)) {
/*  716 */         String newPath = paramPath + "/" + file.getName();
/*  717 */         btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*  719 */       if (fStrategy.equals("ML")) {
/*  720 */         String newPath = paramPath + "/" + file.getName();
/*  721 */         btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  726 */     String sMLPath = outputPath + "/" + timeStamp + "/ML";
/*  727 */     String mlPath = outputPath + "/" + mlTimeStamp + "/ML";
/*  728 */     File sMLFolder = new File(sMLPath);
/*  729 */     File mlFolder = new File(mlPath);
/*  730 */     if (!mlFolder.exists())
/*  731 */       mlFolder.mkdirs();
/*  732 */     File[] mlFileList = sMLFolder.listFiles();
/*  733 */     File[] arrayOfFile2; int m = (arrayOfFile2 = mlFileList).length; for (int k = 0; k < m; k++) { File file = arrayOfFile2[k];
/*  734 */       if (file.getName().contains(" RData")) {
/*  735 */         String newPath = mlPath + "/" + file.getName();
/*  736 */         if (!new File(newPath).exists())
/*  737 */           btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*      */     }
/*  740 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean checkAndTransferMLInput(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
/*      */     throws IOException
/*      */   {
/*  747 */     String timeStamp = (String)mlOutputMap.get(currentKey);
/*      */     
/*  749 */     String mlTimeStamp = machineLearning.getTimeStamp();
/*      */     
/*  751 */     if (timeStamp == null) {
/*  752 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  756 */     String outputPath = btGlobal.loginParameter.getOutputPath();
/*      */     
/*      */ 
/*  759 */     String sMLPath = outputPath + "/" + timeStamp + "/ML";
/*  760 */     String mlPath = outputPath + "/" + mlTimeStamp + "/ML";
/*  761 */     File sMLFolder = new File(sMLPath);
/*  762 */     File mlFolder = new File(mlPath);
/*  763 */     if (!mlFolder.exists())
/*  764 */       mlFolder.mkdirs();
/*  765 */     File[] mlFileList = sMLFolder.listFiles();
/*  766 */     File[] arrayOfFile1; int j = (arrayOfFile1 = mlFileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/*  767 */       if (file.getName().contains("Input")) {
/*  768 */         String newPath = mlPath + "/" + file.getName();
/*  769 */         if (!new File(newPath).exists())
/*  770 */           btGlobal.copyFile(file, new File(newPath));
/*  771 */       } else if (file.getName().contains("DailyCorrelLog")) {
/*  772 */         String newPath = mlPath + "/" + file.getName();
/*  773 */         if (!new File(newPath).exists())
/*  774 */           btGlobal.copyFile(file, new File(newPath));
/*      */       }
/*      */     }
/*  777 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String getBacktestParameters(BacktesterGlobal btGlobal, String strategyID, String backtestTimestamp)
/*      */     throws IOException
/*      */   {
/*  784 */     String basePath = btGlobal.loginParameter.getOutputPath() + "/" + backtestTimestamp;
/*      */     
/*  786 */     if (!new File(basePath + "/Parameters").exists()) {
/*  787 */       return null;
/*      */     }
/*      */     
/*  790 */     String paramPath = basePath + "/Parameters/" + strategyID + " Parameters.csv";
/*  791 */     String strategyParameters = "";
/*  792 */     if (new File(paramPath).exists())
/*      */     {
/*  794 */       CSVReader reader = new CSVReader(paramPath, ',', 0);
/*      */       String[] tsLine;
/*  796 */       while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/*  797 */         if (strategyParameters.equals("")) {
/*  798 */           strategyParameters = tsLine[1];
/*      */         } else
/*  800 */           strategyParameters = strategyParameters + "$" + tsLine[1];
/*      */       }
/*  802 */       reader.close();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  810 */     return strategyParameters;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String getScripDateKey(BacktesterGlobal btGlobal, String strategyID, String timestamp)
/*      */     throws IOException
/*      */   {
/*  817 */     String basePath = btGlobal.loginParameter.getOutputPath() + "/" + timestamp;
/*      */     
/*  819 */     if (!new File(basePath + "/Parameters").exists()) {
/*  820 */       return null;
/*      */     }
/*      */     
/*  823 */     ArrayList<String> scripListDateList = new ArrayList();
/*  824 */     String scripParameterPath = basePath + "/Parameters/" + strategyID + " ScripListDateMap.csv";
/*  825 */     if (new File(scripParameterPath).exists()) {
/*  826 */       CSVReader reader = new CSVReader(scripParameterPath, ',', 0);
/*      */       String[] tsLine;
/*  828 */       while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/*  829 */         scripListDateList.add(tsLine[0] + "," + tsLine[1] + "," + tsLine[2]);
/*      */       }
/*      */     }
/*      */     
/*  833 */     String scripDateKey = null;
/*  834 */     for (String scripListDate : scripListDateList)
/*      */     {
/*  836 */       String[] scripListDateVal = scripListDate.split(",");
/*  837 */       String scripListID = scripListDateVal[0];
/*      */       
/*      */ 
/*  840 */       String scripFileName = basePath + "/Parameters/" + scripListID + " ScripSet.csv";
/*  841 */       CSVReader scripReader = new CSVReader(scripFileName, ',', 0);
/*      */       
/*  843 */       boolean fileError = false;
/*      */       
/*  845 */       if (scripDateKey == null) {
/*  846 */         scripDateKey = scripListDate;
/*      */       } else
/*  848 */         scripDateKey = scripDateKey + "$" + scripListDate;
/*  849 */       String[] scripLine; while ((scripLine = scripReader.getLine()) != null) { String[] scripLine;
/*  850 */         String scripID = scripLine[0];
/*      */         
/*  852 */         String mtmPath = basePath + "/MTM Data/" + strategyID + " " + scripListID + "/" + scripID + " MTM.csv";
/*  853 */         String tradePath = basePath + "/Trade Data/" + strategyID + " " + scripListID + "/" + scripID + 
/*  854 */           " Tradebook.csv";
/*      */         
/*  856 */         if ((!new File(mtmPath).exists()) || (!new File(tradePath).exists())) {
/*  857 */           fileError = true;
/*  858 */           break;
/*      */         }
/*  860 */         scripDateKey = scripDateKey + "|" + scripID;
/*      */       }
/*      */       
/*      */ 
/*  864 */       if (!fileError) {}
/*      */     }
/*      */     
/*      */ 
/*  868 */     return scripDateKey;
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
/*  892 */     String destPath = this.mlPreProcessor.getDestPath();
/*      */     
/*      */ 
/*  895 */     if (!(this.mlParameter.getMlAlgorithm() instanceof MLAlgoRA))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  900 */       if ((this.mlParameter.getMlAlgorithm() instanceof MLAlgoR))
/*      */       {
/*  902 */         boolean rolling = true;
/*      */         
/*  904 */         if (this.mlParameter.getLookbackType().equals(LookbackType.Hinged)) {
/*  905 */           rolling = false;
/*      */         }
/*      */         
/*  908 */         boolean postProcess = this.machineLearning.getBacktest().backtestParameter.isGenerateOutputCheck();
/*  909 */         boolean append = false;
/*      */         
/*  911 */         runMLThroughR(postProcess, this.btGlobal.loginParameter.getMainPath(), destPath, rolling, append);
/*      */ 
/*      */ 
/*      */       }
/*  915 */       else if ((this.mlParameter.getMlAlgorithm() instanceof MLAlgoP)) {
/*  916 */         boolean rolling = true;
/*      */         
/*  918 */         if (this.mlParameter.getLookbackType().equals(LookbackType.Hinged)) {
/*  919 */           rolling = false;
/*      */         }
/*      */         
/*  922 */         boolean postProcess = this.machineLearning.getBacktest().backtestParameter.isGenerateOutputCheck();
/*  923 */         boolean append = false;
/*      */         
/*  925 */         runMLThroughPython(postProcess, this.btGlobal.loginParameter.getMainPath(), destPath, rolling, append);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*  931 */         throw new RuntimeException("Invalid Object Type mlAlgo not typeCasted");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void runMLThroughR(boolean postProcess, String mainPath, String outputPath, boolean rolling, boolean append)
/*      */   {
/*  940 */     String[] newargs1 = { "--no-save" };
/*  941 */     Rengine engine = Rengine.getMainEngine();
/*  942 */     if (engine == null) {
/*  943 */       engine = new Rengine(newargs1, false, null);
/*      */     }
/*      */     
/*  946 */     String mainEval = "c(";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  951 */     for (Map.Entry<String, MLAlgo> entry : this.mlPreProcessor.getAlgorithmMap().entrySet())
/*      */     {
/*      */ 
/*  954 */       String mainDriverPath = mainPath + "/lib/ML Filter/MLFilterMainDriver.R";
/*      */       
/*      */ 
/*  957 */       MLAlgoR Mlr = (MLAlgoR)entry.getValue();
/*  958 */       String packageLoc = Mlr.getModelPackage().replaceAll("\\.", "/");
/*  959 */       String packagePath = mainPath + "/src/" + packageLoc;
/*  960 */       String algoPath = packagePath + "/" + Mlr.getModelName();
/*  961 */       String mlAlgoName = Mlr.getModelName().split("\\.")[0];
/*      */       
/*      */ 
/*  964 */       String src1 = "source(\\\"" + algoPath + "\\\")";
/*  965 */       String src2 = "source(\\\"" + mainDriverPath + "\\\")";
/*      */       
/*  967 */       System.out.println("source(\\\"" + algoPath + "\\\")");
/*  968 */       System.out.println("source(\\\"" + mainDriverPath + "\\\")");
/*      */       
/*  970 */       String rollingVal = rolling ? "TRUE" : "FALSE";
/*      */       
/*  972 */       String appendVal = append ? "TRUE" : "FALSE";
/*      */       
/*  974 */       String inputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Input.csv" + "\\\"";
/*  975 */       String outputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Output.csv" + "\\\"";
/*      */       
/*  977 */       String algoCommand = "MLFilterMainDriver(\\\"" + packagePath + "\\\"" + ", " + inputFile + ", " + 
/*  978 */         outputFile + ", " + "\\\"" + (String)entry.getKey() + "\\\"" + "," + "\\\"" + outputPath + "\\\"" + ", " + 
/*  979 */         this.mlParameter.getBlackoutPeriod() + ", " + this.mlParameter.getWindowPeriod() + ", " + 
/*  980 */         this.mlParameter.getUpdatePeriod() + ", " + rollingVal + ", " + appendVal + ", " + mlAlgoName;
/*  981 */       ArrayList<String[]> paramList = Mlr.getParameterList();
/*  982 */       for (String[] param : paramList) {
/*  983 */         algoCommand = algoCommand + "," + param[1];
/*      */       }
/*  985 */       algoCommand = algoCommand + ")";
/*      */       
/*  987 */       System.out.println(algoCommand);
/*      */       
/*  989 */       System.out.println("\n");
/*      */       
/*      */ 
/*  992 */       if (mainEval.equals("c(")) {
/*  993 */         mainEval = mainEval + "\"" + src1 + "\\n" + src2 + "\\n" + algoCommand + "\"";
/*      */       }
/*      */       else {
/*  996 */         mainEval = mainEval + "," + "\"" + src1 + "\\n" + src2 + "\\n" + algoCommand + "\"";
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1001 */     String mlMainPath = mainPath + "/lib/ML Filter/MainDriver.R";
/* 1002 */     String src = "source(\"" + mlMainPath + "\")";
/* 1003 */     mainEval = mainEval + ")";
/* 1004 */     String finalCommand = "MainDriver(\"" + outputPath + "\"" + "," + mainEval + ")";
/*      */     
/* 1006 */     System.out.println(src);
/* 1007 */     System.out.println(finalCommand);
/* 1008 */     engine.eval(src);
/* 1009 */     engine.eval(finalCommand);
/* 1010 */     engine.end();
/*      */   }
/*      */   
/*      */ 
/*      */   private void runMLThroughPython(boolean postProcess, String mainPath, String outputPath, boolean rolling, boolean append)
/*      */   {
/* 1016 */     String mainDriverPath = mainPath + "/lib/ML Filter";
/*      */     
/* 1018 */     String mainEval = "python \"" + mainDriverPath + "/MainDriver.py" + "\"";
/*      */     
/*      */ 
/* 1021 */     for (Map.Entry<String, MLAlgo> entry : this.mlPreProcessor.getAlgorithmMap().entrySet())
/*      */     {
/*      */ 
/* 1024 */       String rollingVal = rolling ? "1" : "0";
/*      */       
/* 1026 */       String appendVal = append ? "1" : "0";
/*      */       
/*      */ 
/* 1029 */       MLAlgo MlAlgo = (MLAlgo)entry.getValue();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1034 */       String mlAlgoName = MlAlgo.getModelName().split("\\.")[0];
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1040 */       String inputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Input.csv" + "\\\"";
/* 1041 */       String outputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Output.csv" + "\\\"";
/*      */       
/* 1043 */       String algoCommand = "python \\\"" + mainDriverPath + "/MLFilterMainDriver.py" + "\\\"" + " \\\"" + mainPath + 
/* 1044 */         "\\\" " + inputFile + " " + outputFile + " " + "\\\"" + (String)entry.getKey() + "\\\"" + " " + "\\\"" + 
/* 1045 */         outputPath + "\\\"" + " " + this.mlParameter.getBlackoutPeriod().intValue() + " " + this.mlParameter.getWindowPeriod() + 
/* 1046 */         " " + this.mlParameter.getUpdatePeriod() + " " + rollingVal + " " + appendVal + " " + "\\\"" + 
/* 1047 */         mlAlgoName + "\\\"";
/* 1048 */       mainEval = mainEval + " \"" + algoCommand + "\"";
/*      */     }
/*      */     
/* 1051 */     System.out.println(mainEval);
/*      */   }
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
/*      */   
/*      */ 
/*      */   public HashMap<String, DailyDataReader> initDailyreader(ArrayList<String> scripList, String dataPath, Long dailyStartDate)
/*      */     throws Exception
/*      */   {
/* 1104 */     HashMap<String, DailyDataReader> dailyReaderCollection = new HashMap();
/*      */     
/* 1106 */     for (String scripName : scripList) {
/* 1107 */       Scrip scrip = new Scrip(scripName);
/* 1108 */       String scripListName = scripName.replace(' ', '$');
/*      */       try {
/* 1110 */         dailyReaderCollection.put(scripName, new DailyDataReader(dailyStartDate, this.btGlobal, this.backtest, scrip, scripListName));
/*      */       } catch (IOException e) {
/* 1112 */         System.out.println("Error in reading Daily file for " + scripName);
/* 1113 */         throw new Exception();
/*      */       }
/*      */     }
/*      */     
/* 1117 */     return dailyReaderCollection;
/*      */   }
/*      */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/MachineLearningMainDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */