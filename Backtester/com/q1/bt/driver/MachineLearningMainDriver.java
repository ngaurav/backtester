package com.q1.bt.driver;

import com.q1.bt.data.classes.Scrip;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.machineLearning.absclasses.MLAlgo;
import com.q1.bt.machineLearning.absclasses.MLAlgoP;
import com.q1.bt.machineLearning.absclasses.MLAlgoR;
import com.q1.bt.machineLearning.absclasses.MLAlgoRA;
import com.q1.bt.machineLearning.driver.MLInputFileGenerator;
import com.q1.bt.machineLearning.driver.MLPostProcessor;
import com.q1.bt.machineLearning.driver.MLPreProcessor;
import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
import com.q1.bt.machineLearning.utility.DailyDataReader;
import com.q1.bt.machineLearning.utility.TradeAndMTMDataProcessor;
import com.q1.bt.process.BacktesterProcess;
import com.q1.bt.process.ProcessFlow;
import com.q1.bt.process.machinelearning.LookbackType;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.objects.MachineLearning;
import com.q1.bt.process.parameter.BacktestParameter;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.bt.process.parameter.MachineLearningParameter;
import com.q1.csv.CSVReader;
import com.q1.csv.CSVWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.rosuda.JRI.Rengine;

public class MachineLearningMainDriver implements Runnable
{
  private static String destPath;
  public Long DAILY_START_DATE = Long.valueOf(19800101L);
  
  ArrayList<Long> dateList;
  
  MachineLearning machineLearning;
  
  MachineLearningParameter mlParameter;
  
  public Backtest backtest;
  
  BacktesterGlobal btGlobal;
  
  public MLPreProcessor mlPreProcessor;
  
  MLInputFileGenerator mlInputFileGenerator;
  
  MLPostProcessor mlPostProcessor;
  
  boolean postProcess;
  
  private HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap;
  private HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap;
  
  public MachineLearningMainDriver(BacktesterGlobal btGlobal, MachineLearning machineLearning)
    throws IOException
  {
    this.btGlobal = btGlobal;
    this.backtest = machineLearning.getBacktest();
    this.dateList = btGlobal.getConsolDateList(this.backtest.timeStamp);
    

    this.machineLearning = machineLearning;
    this.mlParameter = machineLearning.getMlParameter();
    
    this.postProcess = machineLearning.getBacktest().backtestParameter.isGenerateOutputCheck();
  }
  
  public void run()
  {
    try
    {
      execute();
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    
    if (this.btGlobal.isGui)
    {
      BacktesterProcess[] choices = { BacktesterProcess.Backtest, BacktesterProcess.Results };
      BacktesterProcess input = (BacktesterProcess)JOptionPane.showInputDialog(null, 
        "Please choose the next Process", "Process Type", 3, null, 
        choices, 
        choices[0]);
      
      this.btGlobal.processFlow.add(input);
      

      if (input.equals(BacktesterProcess.Backtest))
      {

        this.btGlobal.displayMessage("Running Backtest on Updated Orderbook");
        this.btGlobal.processFlow.update();
        this.btGlobal.processFlow.add(BacktesterProcess.Results);
        try
        {
          if (this.mlPreProcessor != null) {
            destPath = this.mlPreProcessor.getDestPath();
          }
          Backtest mlBacktest = new Backtest(this.backtest.backtestParameter, destPath + "/ML Order Data");
          mlBacktest.fileBacktest = true;
          mlBacktest.timeStamp = this.machineLearning.getTimeStamp();
          BacktestMainDriver backtestDriver = new BacktestMainDriver(this.btGlobal, mlBacktest);
          Thread t = new Thread(backtestDriver);
          t.start();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        

      }
      else if (input.equals(BacktesterProcess.Results)) {
        this.btGlobal.processFlow.update();
        this.btGlobal.initializeProcess(this.machineLearning);
      }
    }
  }
  
  public void execute() throws Exception
  {
    String backtestPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp;
    Integer newTimeStamp = Integer.valueOf(Integer.parseInt(this.backtest.timeStamp) + 1);
    this.backtest.timeStamp = newTimeStamp.toString();
    String newBacktestPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp;
    File currentFile = new File(backtestPath);
    File newFile = new File(newBacktestPath);
    FileUtils.copyDirectory(currentFile, newFile);
    FileUtils.deleteDirectory(currentFile);
    

    this.machineLearning.setTimeStamp("ML" + this.backtest.timeStamp);
    

    HashMap<String, String> mlOutputMap = new HashMap();
    try {
      mlOutputMap = createMLOutputMap(this.btGlobal, this.machineLearning);
    } catch (IOException e2) {
      e2.printStackTrace();
      return;
    }
    


    boolean mlCheck = false;boolean mlOutputCheck = false;boolean mlInputCheck = false;
    mlCheck = checkMLOutput(this.btGlobal, this.machineLearning, mlOutputMap);
    if (!mlCheck)
    {
      mlOutputCheck = checkMLOOutput(this.btGlobal, this.machineLearning, mlOutputMap);
      if (!mlOutputCheck)
      {
        mlInputCheck = checkMLIOutput(this.btGlobal, this.machineLearning, mlOutputMap);
      }
    }
    
    if (!mlCheck)
    {

      preProcess();
      
      if (!mlOutputCheck)
      {
        if (!mlInputCheck)
        {
          createInputFile();
        }
        

        generateMLOutput();
      }
      


      readOutput();
      

      endProcess();
    }
  }
  
  public void preProcess() throws Exception
  {
    this.mlPreProcessor = new MLPreProcessor(this.btGlobal, this.backtest, this.machineLearning);
    this.mlPreProcessor.mlPreProcess();
  }
  
  public void createInputFile() throws Exception
  {
    ArrayList<String> scripUniverse = this.mlPreProcessor.getScripUniverse();
    String dataPath = this.btGlobal.loginParameter.getDataPath();
    
    HashMap<String, DailyDataReader> dailyReaderCollection = initDailyreader(scripUniverse, dataPath, this.DAILY_START_DATE);
    this.mlInputFileGenerator = new MLInputFileGenerator(this.btGlobal, this.backtest, this.mlParameter, dailyReaderCollection, this.mlParameter.getBias().booleanValue());
    
    boolean nextLayer = false;
    TradeAndMTMDataProcessor stratTradePnL = this.mlPreProcessor.getStratTradePnL();
    HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap = this.mlPreProcessor.getModelSegmentWiseAssetUniverseMap();
    String sourcePath = this.mlPreProcessor.getSourcePath();
    String destPath = this.mlPreProcessor.getDestPath();
    HashMap<String, TreeMap<Long, Long>> tradeStratEndMap = this.mlPreProcessor.getTradeStartEndMaps();
    HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMap = this.mlPreProcessor.getTradeStartDateTradeSideMaps();
    
    this.mlInputFileGenerator.createInputFile(nextLayer, sourcePath, destPath, dataPath, modelSegmentWiseAssetUniverseMap, 
      scripUniverse, stratTradePnL, this.dateList, tradeStratEndMap, tradeStartDateTradeSideMap, this.DAILY_START_DATE);
  }
  
  public void readOutput() throws Exception {
    String sourcePath = this.mlPreProcessor.getSourcePath();
    String destPath = this.mlPreProcessor.getDestPath();
    String algoLastModifiedTimeStamp = this.mlPreProcessor.getAlgoLastModifiedTimeStamp();
    this.modelSegmentWiseAssetUniverseMap = this.mlPreProcessor.getModelSegmentWiseAssetUniverseMap();
    this.postModelSelectionSegmentWiseAssetUniverseMap = this.mlPreProcessor.getPostModelSelectionSegmentWiseAssetUniverseMap();
    ArrayList<String> scripUniverse = this.mlPreProcessor.getScripUniverse();
    
    HashMap<String, DailyDataReader> dailyReaderCollection;
    TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals;
    HashMap<String, DailyDataReader> dailyReaderCollection;
    if (this.mlInputFileGenerator == null)
    {
      String dataPath = this.btGlobal.loginParameter.getDataPath();
      TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals = readCorrelFromFile(destPath);
      dailyReaderCollection = initDailyreader(scripUniverse, dataPath, this.DAILY_START_DATE);
    }
    else
    {
      correlVals = this.mlInputFileGenerator.getCorrelVals();
      dailyReaderCollection = this.mlInputFileGenerator.getDailyReaderCollection();
    }
    
    this.mlPostProcessor = new MLPostProcessor(sourcePath, destPath, this.modelSegmentWiseAssetUniverseMap, this.postModelSelectionSegmentWiseAssetUniverseMap, scripUniverse, 
      this.btGlobal.loginParameter.getDataPath(), this.backtest, this.machineLearning, this.dateList, 
      dailyReaderCollection, correlVals, 
      this.mlPreProcessor.getAlgorithmMap(), this.mlPreProcessor.getTradeStartEndMaps(), this.mlPreProcessor.getTradeStartDateTradeSideMaps(), 
      this.mlPreProcessor.getTradeMTMMat(), algoLastModifiedTimeStamp, this.postProcess, this.mlParameter.getBias().booleanValue());
    
    this.mlPostProcessor.readOutput(sourcePath, destPath, this.postProcess, this.dateList);
  }
  
  public void endProcess() throws IOException
  {
    HashMap<String, MLAlgo> algorithmMap = this.mlPostProcessor.getAlgorithmMap();
    CSVWriter mlLogWriter = this.mlPostProcessor.getMlLogWriter();
    CSVWriter correlLogWriter = this.mlPostProcessor.getCorrelLogWriter();
    try
    {
      for (MLAlgo mlalgo : algorithmMap.values()) {
        mlalgo.close();
      }
      
      mlLogWriter.close();
      correlLogWriter.close();
    }
    catch (Exception localException) {}
  }
  



  public static HashMap<String, String> createMLOutputMap(BacktesterGlobal btGlobal, MachineLearning machineLearning)
    throws IOException
  {
    HashMap<String, String> outputMap = new HashMap();
    

    File outputFile = new File(btGlobal.loginParameter.getOutputPath());
    File[] folders = outputFile.listFiles();
    File[] arrayOfFile1; int j = (arrayOfFile1 = folders).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
      
      String folderTimeStamp = folder.getName();
      String folderPath = folder.getAbsolutePath();
      

      if (folderTimeStamp.startsWith("ML"))
      {


        if (new File(folderPath + "/Parameters").exists())
        {


          File[] paramFiles = new File(folderPath + "/Parameters").listFiles();
          File[] arrayOfFile2; int m = (arrayOfFile2 = paramFiles).length; for (int k = 0; k < m; k++) { File paramFile = arrayOfFile2[k];
            
            String fileName = paramFile.getName();
            String[] fileVal = paramFile.getName().split(" ");
            
            if (fileName.endsWith("Parameters.csv"))
            {

              String strategyID = fileVal[0];
              if (machineLearning.getBacktest().backtestMap.containsKey(strategyID))
              {


                String paramPath = paramFile.getAbsolutePath();
                String paramKey = "";
                String paramKeyInput = "";
                String paramKeyOutput = "";
                

                CSVReader reader = new CSVReader(paramPath, ',', 0);
                String[] parameterLine;
                while ((parameterLine = reader.getLine()) != null)
                {
                  String[] parameterLine;
                  String parameterName = parameterLine[0];
                  String parameterValue = parameterLine[1];
                  

                  if (paramKey.equals("")) {
                    paramKey = parameterValue;
                  } else {
                    paramKey = paramKey + "$" + parameterValue;
                  }
                  
                  if ((!parameterName.startsWith("ML ")) || (parameterLine[0].equals("ML Model Merge Type"))) {
                    if (paramKeyInput.equals("")) {
                      paramKeyInput = parameterValue;
                    } else {
                      paramKeyInput = paramKeyInput + "$" + parameterValue;
                    }
                  }
                  
                  if ((!parameterLine[0].equals("ML Consolidation Function")) && 
                    (!parameterLine[0].equals("ML Segment Asset Count")) && 
                    (!parameterLine[0].equals("ML Overall Asset Count")) && 
                    (!parameterLine[0].equals("ML Segment Correl Threshold")) && 
                    (!parameterLine[0].equals("ML Overall Correl Threshold"))) {
                    if (paramKeyOutput.equals("")) {
                      paramKeyOutput = parameterValue;
                    } else {
                      paramKeyOutput = paramKeyOutput + "$" + parameterValue;
                    }
                  }
                }
                reader.close();
                

                String primaryKey = "ML " + strategyID + " " + paramKey;
                String primaryKeyInput = "ML " + strategyID + " " + paramKeyInput;
                String primaryKeyOutput = "ML " + strategyID + " " + paramKeyOutput;
                

                TreeSet<String> factorSet = new TreeSet();
                String factorPath = folderPath + "/Parameters/ML Factorlist.csv";
                if (new File(factorPath).exists()) {
                  reader = new CSVReader(factorPath, ',', 0);
                  String[] tsLine;
                  while ((tsLine = reader.getLine()) != null) { String[] tsLine;
                    factorSet.add(tsLine[0]); }
                  reader.close();
                }
                

                for (String factor : factorSet)
                {
                  primaryKey = primaryKey + "$" + factor;
                  primaryKeyInput = primaryKeyInput + "$" + factor;
                  primaryKeyOutput = primaryKeyOutput + "$" + factor;
                }
                






                TreeSet<String> scripListDateList = new TreeSet();
                String scripParameterPath = folderPath + "/Parameters/" + strategyID + " ScripListDateMap.csv";
                if (new File(scripParameterPath).exists()) {
                  reader = new CSVReader(scripParameterPath, ',', 0);
                  String[] tsLine;
                  while ((tsLine = reader.getLine()) != null) { String[] tsLine;
                    scripListDateList.add(tsLine[0] + "," + tsLine[1] + "," + tsLine[2]); }
                  reader.close();
                }
                

                boolean fileError = false;
                

                for (String scripListDate : scripListDateList)
                {
                  String[] scripListDateVal = scripListDate.split(",");
                  String scripListID = scripListDateVal[0];
                  

                  String scripFileName = folderPath + "/Parameters/" + scripListID + " ScripSet.csv";
                  CSVReader scripReader = new CSVReader(scripFileName, ',', 0);
                  

                  String scripKey = scripListDate;
                  String[] scripLine; while ((scripLine = scripReader.getLine()) != null) { String[] scripLine;
                    String scripID = scripLine[0];
                    
                    String mtmPath = folderPath + "/MTM Data/" + strategyID + " " + scripListID + "/" + scripID + 
                      " MTM.csv";
                    String tradePath = folderPath + "/Trade Data/" + strategyID + " " + scripListID + "/" + 
                      scripID + " Tradebook.csv";
                    
                    if ((!new File(mtmPath).exists()) || (!new File(tradePath).exists())) {
                      fileError = true;
                      break;
                    }
                    scripKey = scripKey + "|" + scripID;
                  }
                  scripReader.close();
                  

                  if (fileError) {
                    break;
                  }
                  
                  primaryKey = primaryKey + "$" + scripKey;
                  primaryKeyInput = primaryKeyInput + "$" + scripKey;
                  primaryKeyOutput = primaryKeyOutput + "$" + scripKey;
                }
                

                if (!fileError)
                {









                  outputMap.put(primaryKey, folderTimeStamp);
                  outputMap.put(primaryKeyInput, folderTimeStamp);
                  outputMap.put(primaryKeyOutput, folderTimeStamp);
                }
              }
            } } } } }
    return outputMap;
  }
  
  public static boolean checkMLOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, HashMap<String, String> mlOutputMap)
    throws IOException
  {
    boolean outputCheck = true;
    
    String backtestTimestamp = machineLearning.getBacktest().timeStamp;
    
    for (String strategyID : machineLearning.getBacktest().backtestMap.keySet())
    {

      String backtestParameters = getBacktestParameters(btGlobal, strategyID, backtestTimestamp);
      

      String mlParameters = machineLearning.getMlParameter().getMLParametersAsKey();
      

      String parameterString = backtestParameters + "$" + mlParameters;
      

      String currentKey = "ML " + strategyID + " " + parameterString;
      

      String scripListDate = getScripDateKey(btGlobal, strategyID, backtestTimestamp);
      
      currentKey = currentKey + "$" + scripListDate;
      

      boolean curOutputCheck = checkAndTransferML(btGlobal, machineLearning, strategyID, currentKey, mlOutputMap);
      if (outputCheck)
        outputCheck = curOutputCheck;
    }
    return outputCheck;
  }
  
  public static boolean checkMLIOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, HashMap<String, String> mlOutputMap)
    throws IOException
  {
    boolean outputCheck = true;
    
    String backtestTimestamp = machineLearning.getBacktest().timeStamp;
    
    for (String strategyID : machineLearning.getBacktest().backtestMap.keySet())
    {

      String backtestParameters = getBacktestParameters(btGlobal, strategyID, backtestTimestamp);
      

      String mlFactorParameters = machineLearning.getMlParameter().getMLParametersAsInputKey();
      

      String paramString = backtestParameters + "$" + mlFactorParameters;
      

      String currentKey = "ML " + strategyID + " " + paramString;
      

      String scripListDate = getScripDateKey(btGlobal, strategyID, backtestTimestamp);
      
      currentKey = currentKey + "$" + scripListDate;
      

      boolean curOutputCheck = checkAndTransferMLInput(btGlobal, machineLearning, strategyID, currentKey, 
        mlOutputMap);
      if (outputCheck)
        outputCheck = curOutputCheck;
    }
    return outputCheck;
  }
  
  public static boolean checkMLOOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, HashMap<String, String> mlOutputMap)
    throws IOException
  {
    boolean outputCheck = true;
    
    String backtestTimestamp = machineLearning.getBacktest().timeStamp;
    
    for (String strategyID : machineLearning.getBacktest().backtestMap.keySet())
    {

      String backtestParameters = getBacktestParameters(btGlobal, strategyID, backtestTimestamp);
      

      String mlParameters = machineLearning.getMlParameter().getMLParametersAsOutputKey();
      

      String parameterString = backtestParameters + "$" + mlParameters;
      

      String currentKey = "ML " + strategyID + " " + parameterString;
      

      String scripListDate = getScripDateKey(btGlobal, strategyID, backtestTimestamp);
      
      currentKey = currentKey + "$" + scripListDate;
      

      boolean curOutputCheck = checkAndTransferMLOutput(btGlobal, machineLearning, strategyID, currentKey, 
        mlOutputMap);
      if (outputCheck)
        outputCheck = curOutputCheck;
    }
    return outputCheck;
  }
  
  public static boolean checkAndTransferML(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
    throws IOException
  {
    String timeStamp = (String)mlOutputMap.get(currentKey);
    String mlTimeStamp = machineLearning.getTimeStamp();
    
    if (timeStamp == null) {
      return false;
    }
    


    String outputPath = btGlobal.loginParameter.getOutputPath();
    String sMTMPath = outputPath + "/" + timeStamp;
    String mtmPath = outputPath + "/" + mlTimeStamp;
    destPath = mtmPath;
    File sMTMFolder = new File(sMTMPath);
    File mtmFolder = new File(mtmPath);
    
    sMTMFolder.renameTo(mtmFolder);
    

    File paramFolder = new File(mtmPath + "/Parameters");
    File[] arrayOfFile;
    int j = (arrayOfFile = paramFolder.listFiles()).length; for (int i = 0; i < j; i++) { File file = arrayOfFile[i];
      
      if (file.getName().contains("ScripListDateMap")) {
        file.delete();
      }
    }
    



































































    return true;
  }
  

  public static boolean checkAndTransferMLOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
    throws IOException
  {
    String timeStamp = (String)mlOutputMap.get(currentKey);
    
    String mlTimeStamp = machineLearning.getTimeStamp();
    
    if (timeStamp == null) {
      return false;
    }
    

    String outputPath = btGlobal.loginParameter.getOutputPath();
    

    String sParamPath = outputPath + "/" + timeStamp + "/Parameters";
    String paramPath = outputPath + "/" + mlTimeStamp + "/Parameters";
    File sParamFolder = new File(sParamPath);
    File paramFolder = new File(paramPath);
    if (!paramFolder.exists())
      paramFolder.mkdirs();
    File[] paramFileList = sParamFolder.listFiles();
    File[] arrayOfFile1; int j = (arrayOfFile1 = paramFileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
      String fStrategy = file.getName().split(" ")[0];
      if (fStrategy.equals(strategy)) {
        String newPath = paramPath + "/" + file.getName();
        btGlobal.copyFile(file, new File(newPath));
      }
      if (fStrategy.equals("ML")) {
        String newPath = paramPath + "/" + file.getName();
        btGlobal.copyFile(file, new File(newPath));
      }
    }
    

    String sMLPath = outputPath + "/" + timeStamp + "/ML";
    String mlPath = outputPath + "/" + mlTimeStamp + "/ML";
    File sMLFolder = new File(sMLPath);
    File mlFolder = new File(mlPath);
    if (!mlFolder.exists())
      mlFolder.mkdirs();
    File[] mlFileList = sMLFolder.listFiles();
    File[] arrayOfFile2; int m = (arrayOfFile2 = mlFileList).length; for (int k = 0; k < m; k++) { File file = arrayOfFile2[k];
      String newPath = mlPath + "/" + file.getName();
      if (!new File(newPath).exists())
        btGlobal.copyFile(file, new File(newPath));
    }
    return true;
  }
  

  public static boolean checkAndTransferMLAOutput(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
    throws IOException
  {
    String timeStamp = (String)mlOutputMap.get(currentKey);
    
    String mlTimeStamp = machineLearning.getTimeStamp();
    
    if (timeStamp == null) {
      return false;
    }
    

    String outputPath = btGlobal.loginParameter.getOutputPath();
    

    String sParamPath = outputPath + "/" + timeStamp + "/Parameters";
    String paramPath = outputPath + "/" + mlTimeStamp + "/Parameters";
    File sParamFolder = new File(sParamPath);
    File paramFolder = new File(paramPath);
    if (!paramFolder.exists())
      paramFolder.mkdirs();
    File[] paramFileList = sParamFolder.listFiles();
    File[] arrayOfFile1; int j = (arrayOfFile1 = paramFileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
      String fStrategy = file.getName().split(" ")[0];
      if (fStrategy.equals(strategy)) {
        String newPath = paramPath + "/" + file.getName();
        btGlobal.copyFile(file, new File(newPath));
      }
      if (fStrategy.equals("ML")) {
        String newPath = paramPath + "/" + file.getName();
        btGlobal.copyFile(file, new File(newPath));
      }
    }
    

    String sMLPath = outputPath + "/" + timeStamp + "/ML";
    String mlPath = outputPath + "/" + mlTimeStamp + "/ML";
    File sMLFolder = new File(sMLPath);
    File mlFolder = new File(mlPath);
    if (!mlFolder.exists())
      mlFolder.mkdirs();
    File[] mlFileList = sMLFolder.listFiles();
    File[] arrayOfFile2; int m = (arrayOfFile2 = mlFileList).length; for (int k = 0; k < m; k++) { File file = arrayOfFile2[k];
      if (file.getName().contains(" RData")) {
        String newPath = mlPath + "/" + file.getName();
        if (!new File(newPath).exists())
          btGlobal.copyFile(file, new File(newPath));
      }
    }
    return true;
  }
  

  public static boolean checkAndTransferMLInput(BacktesterGlobal btGlobal, MachineLearning machineLearning, String strategy, String currentKey, HashMap<String, String> mlOutputMap)
    throws IOException
  {
    String timeStamp = (String)mlOutputMap.get(currentKey);
    
    String mlTimeStamp = machineLearning.getTimeStamp();
    
    if (timeStamp == null) {
      return false;
    }
    

    String outputPath = btGlobal.loginParameter.getOutputPath();
    

    String sMLPath = outputPath + "/" + timeStamp + "/ML";
    String mlPath = outputPath + "/" + mlTimeStamp + "/ML";
    File sMLFolder = new File(sMLPath);
    File mlFolder = new File(mlPath);
    if (!mlFolder.exists())
      mlFolder.mkdirs();
    File[] mlFileList = sMLFolder.listFiles();
    File[] arrayOfFile1; int j = (arrayOfFile1 = mlFileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
      if (file.getName().contains("Input")) {
        String newPath = mlPath + "/" + file.getName();
        if (!new File(newPath).exists())
          btGlobal.copyFile(file, new File(newPath));
      } else if (file.getName().contains("DailyCorrelLog")) {
        String newPath = mlPath + "/" + file.getName();
        if (!new File(newPath).exists())
          btGlobal.copyFile(file, new File(newPath));
      }
    }
    return true;
  }
  

  public static String getBacktestParameters(BacktesterGlobal btGlobal, String strategyID, String backtestTimestamp)
    throws IOException
  {
    String basePath = btGlobal.loginParameter.getOutputPath() + "/" + backtestTimestamp;
    
    if (!new File(basePath + "/Parameters").exists()) {
      return null;
    }
    
    String paramPath = basePath + "/Parameters/" + strategyID + " Parameters.csv";
    String strategyParameters = "";
    if (new File(paramPath).exists())
    {
      CSVReader reader = new CSVReader(paramPath, ',', 0);
      String[] tsLine;
      while ((tsLine = reader.getLine()) != null) { String[] tsLine;
        if (strategyParameters.equals("")) {
          strategyParameters = tsLine[1];
        } else
          strategyParameters = strategyParameters + "$" + tsLine[1];
      }
      reader.close();
    }
    





    return strategyParameters;
  }
  

  public static String getScripDateKey(BacktesterGlobal btGlobal, String strategyID, String timestamp)
    throws IOException
  {
    String basePath = btGlobal.loginParameter.getOutputPath() + "/" + timestamp;
    
    if (!new File(basePath + "/Parameters").exists()) {
      return null;
    }
    
    TreeSet<String> scripListDateList = new TreeSet();
    String scripParameterPath = basePath + "/Parameters/" + strategyID + " ScripListDateMap.csv";
    if (new File(scripParameterPath).exists()) {
      CSVReader reader = new CSVReader(scripParameterPath, ',', 0);
      String[] tsLine;
      while ((tsLine = reader.getLine()) != null) { String[] tsLine;
        scripListDateList.add(tsLine[0] + "," + tsLine[1] + "," + tsLine[2]);
      }
    }
    
    String scripDateKey = null;
    for (String scripListDate : scripListDateList)
    {
      String[] scripListDateVal = scripListDate.split(",");
      String scripListID = scripListDateVal[0];
      

      String scripFileName = basePath + "/Parameters/" + scripListID + " ScripSet.csv";
      CSVReader scripReader = new CSVReader(scripFileName, ',', 0);
      
      boolean fileError = false;
      
      if (scripDateKey == null) {
        scripDateKey = scripListDate;
      } else
        scripDateKey = scripDateKey + "$" + scripListDate;
      String[] scripLine; while ((scripLine = scripReader.getLine()) != null) { String[] scripLine;
        String scripID = scripLine[0];
        
        String mtmPath = basePath + "/MTM Data/" + strategyID + " " + scripListID + "/" + scripID + " MTM.csv";
        String tradePath = basePath + "/Trade Data/" + strategyID + " " + scripListID + "/" + scripID + 
          " Tradebook.csv";
        
        if ((!new File(mtmPath).exists()) || (!new File(tradePath).exists())) {
          fileError = true;
          break;
        }
        scripDateKey = scripDateKey + "|" + scripID;
      }
      

      if (!fileError) {}
    }
    

    return scripDateKey;
  }
  


















  public void generateMLOutput()
    throws Exception
  {
    String destPath = this.mlPreProcessor.getDestPath();
    

    if (!(this.mlParameter.getMlAlgorithm() instanceof MLAlgoRA))
    {



      if ((this.mlParameter.getMlAlgorithm() instanceof MLAlgoR))
      {
        boolean rolling = true;
        
        if (this.mlParameter.getLookbackType().equals(LookbackType.Hinged)) {
          rolling = false;
        }
        
        boolean postProcess = this.machineLearning.getBacktest().backtestParameter.isGenerateOutputCheck();
        boolean append = false;
        
        runMLThroughR(postProcess, this.btGlobal.loginParameter.getMainPath(), destPath, rolling, append);


      }
      else if ((this.mlParameter.getMlAlgorithm() instanceof MLAlgoP)) {
        boolean rolling = true;
        
        if (this.mlParameter.getLookbackType().equals(LookbackType.Hinged)) {
          rolling = false;
        }
        
        boolean postProcess = this.machineLearning.getBacktest().backtestParameter.isGenerateOutputCheck();
        boolean append = false;
        
        runMLThroughPython(postProcess, this.btGlobal.loginParameter.getMainPath(), destPath, rolling, append);

      }
      else
      {

        throw new RuntimeException("Invalid Object Type mlAlgo not typeCasted");
      }
    }
  }
  


  private void runMLThroughR(boolean postProcess, String mainPath, String outputPath, boolean rolling, boolean append)
  {
    String[] newargs1 = { "--no-save" };
    Rengine engine = Rengine.getMainEngine();
    if (engine == null) {
      engine = new Rengine(newargs1, false, null);
    }
    
    String mainEval = "c(";
    



    for (Map.Entry<String, MLAlgo> entry : this.mlPreProcessor.getAlgorithmMap().entrySet())
    {

      String mainDriverPath = mainPath + "/lib/ML Filter/MLFilterMainDriver.R";
      

      MLAlgoR Mlr = (MLAlgoR)entry.getValue();
      String packageLoc = Mlr.getModelPackage().replaceAll("\\.", "/");
      String packagePath = mainPath + "/src/" + packageLoc;
      String algoPath = packagePath + "/" + Mlr.getModelName();
      String mlAlgoName = Mlr.getModelName().split("\\.")[0];
      

      String src1 = "source(\\\"" + algoPath + "\\\")";
      String src2 = "source(\\\"" + mainDriverPath + "\\\")";
      
      System.out.println("source(\\\"" + algoPath + "\\\")");
      System.out.println("source(\\\"" + mainDriverPath + "\\\")");
      
      String rollingVal = rolling ? "TRUE" : "FALSE";
      
      String appendVal = append ? "TRUE" : "FALSE";
      
      String inputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Input.csv" + "\\\"";
      String outputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Output.csv" + "\\\"";
      
      String algoCommand = "MLFilterMainDriver(\\\"" + packagePath + "\\\"" + ", " + inputFile + ", " + 
        outputFile + ", " + "\\\"" + (String)entry.getKey() + "\\\"" + "," + "\\\"" + outputPath + "\\\"" + ", " + 
        this.mlParameter.getBlackoutPeriod() + ", " + this.mlParameter.getWindowPeriod() + ", " + 
        this.mlParameter.getUpdatePeriod() + ", " + rollingVal + ", " + appendVal + ", " + mlAlgoName;
      ArrayList<String[]> paramList = Mlr.getParameterList();
      for (String[] param : paramList) {
        algoCommand = algoCommand + "," + param[1];
      }
      algoCommand = algoCommand + ")";
      
      System.out.println(algoCommand);
      
      System.out.println("\n");
      

      if (mainEval.equals("c(")) {
        mainEval = mainEval + "\"" + src1 + "\\n" + src2 + "\\n" + algoCommand + "\"";
      }
      else {
        mainEval = mainEval + "," + "\"" + src1 + "\\n" + src2 + "\\n" + algoCommand + "\"";
      }
    }
    

    String mlMainPath = mainPath + "/lib/ML Filter/MainDriver.R";
    String src = "source(\"" + mlMainPath + "\")";
    mainEval = mainEval + ")";
    String finalCommand = "MainDriver(\"" + outputPath + "\"" + "," + mainEval + ")";
    
    System.out.println(src);
    System.out.println(finalCommand);
    engine.eval(src);
    engine.eval(finalCommand);
    engine.end();
  }
  

  private void runMLThroughPython(boolean postProcess, String mainPath, String outputPath, boolean rolling, boolean append)
  {
    String mainDriverPath = mainPath + "/lib/ML Filter";
    
    String mainEval = "python \"" + mainDriverPath + "/MainDriver.py" + "\"";
    

    for (Map.Entry<String, MLAlgo> entry : this.mlPreProcessor.getAlgorithmMap().entrySet())
    {

      String rollingVal = rolling ? "1" : "0";
      
      String appendVal = append ? "1" : "0";
      

      MLAlgo MlAlgo = (MLAlgo)entry.getValue();
      



      String mlAlgoName = MlAlgo.getModelName().split("\\.")[0];
      




      String inputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Input.csv" + "\\\"";
      String outputFile = "\\\"" + outputPath + "/ML/" + (String)entry.getKey() + " Output.csv" + "\\\"";
      
      String algoCommand = "python \\\"" + mainDriverPath + "/MLFilterMainDriver.py" + "\\\"" + " \\\"" + mainPath + 
        "\\\" " + inputFile + " " + outputFile + " " + "\\\"" + (String)entry.getKey() + "\\\"" + " " + "\\\"" + 
        outputPath + "\\\"" + " " + this.mlParameter.getBlackoutPeriod().intValue() + " " + this.mlParameter.getWindowPeriod() + 
        " " + this.mlParameter.getUpdatePeriod() + " " + rollingVal + " " + appendVal + " " + "\\\"" + 
        mlAlgoName + "\\\"";
      mainEval = mainEval + " \"" + algoCommand + "\"";
    }
    
    System.out.println(mainEval);
  }
  
  public TreeMap<Long, HashMap<String, HashMap<String, Double>>> readCorrelFromFile(String destPath)
    throws IOException
  {
    String fileName = destPath + "/ML/DailyCorrelLog.csv";
    
    TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals = new TreeMap();
    
    CSVReader reader = new CSVReader(fileName, ',', 0);
    


    String[] header1 = reader.getLine();
    String[] header2 = reader.getLine();
    String[] curLine;
    while ((curLine = reader.getLine()) != null) {
      String[] curLine;
      Long date = Long.valueOf(Long.parseLong(curLine[0]));
      HashMap<String, HashMap<String, Double>> correlMap = new HashMap();
      

      for (int i = 1; i < curLine.length; i++)
      {
        String scrip1 = header1[i];
        String scrip2 = header2[i];
        Double value = Double.valueOf(Double.parseDouble(curLine[i]));
        
        HashMap<String, Double> curMap = (HashMap)correlMap.get(scrip1);
        if (curMap == null) {
          curMap = new HashMap();
          curMap.put(scrip2, value);
          correlMap.put(scrip1, curMap);
        } else {
          curMap.put(scrip2, value);
          correlMap.put(scrip1, curMap);
        }
      }
      

      correlVals.put(date, correlMap);
    }
    
    reader.close();
    
    return correlVals;
  }
  

  public HashMap<String, DailyDataReader> initDailyreader(ArrayList<String> scripList, String dataPath, Long dailyStartDate)
    throws Exception
  {
    HashMap<String, DailyDataReader> dailyReaderCollection = new HashMap();
    
    for (String scripName : scripList) {
      Scrip scrip = new Scrip(scripName);
      String scripListName = scripName.replace(' ', '$');
      try {
        dailyReaderCollection.put(scripName, new DailyDataReader(dailyStartDate, this.btGlobal, this.backtest, scrip, scripListName));
      } catch (IOException e) {
        System.out.println("Error in reading Daily file for " + scripName);
        throw new Exception();
      }
    }
    
    return dailyReaderCollection;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/MachineLearningMainDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */