package com.q1.bt.machineLearning.driver;

import com.q1.bt.machineLearning.absclasses.MLAlgo;
import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
import com.q1.bt.machineLearning.driver.driverHelperClasses.BinaryGenerator;
import com.q1.bt.machineLearning.driver.driverHelperClasses.CorrelLogWriter;
import com.q1.bt.machineLearning.driver.driverHelperClasses.OutputProcessor;
import com.q1.bt.machineLearning.driver.driverHelperClasses.ParameterWriter;
import com.q1.bt.machineLearning.utility.DailyDataReader;
import com.q1.bt.machineLearning.utility.MLFinalDecisionWriter;
import com.q1.bt.machineLearning.utility.TradeBookToOrderBookGenerator;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.objects.MachineLearning;
import com.q1.bt.process.parameter.MachineLearningParameter;
import com.q1.csv.CSVReader;
import com.q1.csv.CSVWriter;
import com.q1.math.MathLib;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;










public class MLPostProcessor
{
  HashMap<String, DailyDataReader> dailyReaderCollection;
  MLFinalDecisionWriter mlFinalDecisionWriter;
  TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals;
  CSVWriter mlLogWriter;
  CorrelLogWriter correlLogWriter;
  HashMap<String, MLAlgo> algorithmMap = new HashMap();
  
  HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps;
  
  HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMap;
  
  String mlPath;
  Long rwriteTS = Long.valueOf(0L);
  Boolean rcompleteReading = Boolean.valueOf(false);
  
  HashMap<Long, String> tsTradedSelectedScripsMap = new HashMap();
  HashMap<Long, String> tsTradedNotSelectedScripsMap = new HashMap();
  



  private TreeMap<Long, HashMap<String, Double>> tradeMTMMat;
  



  HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap;
  


  HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap;
  


  ArrayList<String> scripUniverse;
  


  MachineLearningParameter mlParameter;
  


  Backtest backtest;
  


  public boolean bias;
  



  public MLPostProcessor(String sourcePath, String destPath, HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap, HashMap<String, ArrayList<Asset>> postModelSelectionSegmentWiseAssetUniverseMap, ArrayList<String> scripUniverse, String dataPath, Backtest backtest, MachineLearning machineLearning, ArrayList<Long> dateList, HashMap<String, DailyDataReader> dailyReaderCollection, TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals, HashMap<String, MLAlgo> algorithmMap, HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps, HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMap, TreeMap<Long, HashMap<String, Double>> tradeMTMMat, String algoLastModifiedTimeStamp, boolean postProcess, boolean bias)
  {
    this.backtest = backtest;
    

    this.mlParameter = machineLearning.getMlParameter();
    
    this.dailyReaderCollection = dailyReaderCollection;
    this.correlVals = correlVals;
    this.algorithmMap = algorithmMap;
    this.tradeStartEndMaps = tradeStartEndMaps;
    this.tradeStartDateTradeSideMap = tradeStartDateTradeSideMap;
    this.tradeMTMMat = tradeMTMMat;
    this.modelSegmentWiseAssetUniverseMap = modelSegmentWiseAssetUniverseMap;
    this.postModelSelectionSegmentWiseAssetUniverseMap = postModelSelectionSegmentWiseAssetUniverseMap;
    this.scripUniverse = scripUniverse;
    this.bias = bias;
    
    this.mlPath = (destPath + "/ML");
    try
    {
      initMLLogWriter();
      this.mlFinalDecisionWriter = new MLFinalDecisionWriter(this.mlPath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void readOutput(String sourcePath, String destPath, boolean postProcess, ArrayList<Long> dateList) throws Exception
  {
    HashMap<String, Double> modelOutput = new HashMap();
    HashMap<String, Boolean> result = new HashMap();
    HashMap<String, CSVReader> outputReaderCollector = new HashMap();
    
    if (this.correlVals == null) {
      this.correlVals = correlRead(destPath);
    }
    else {
      this.correlLogWriter = new CorrelLogWriter(this.mlPath);
      this.correlLogWriter.writeCorrelLog(this.correlVals, this.scripUniverse);
    }
    
    this.rcompleteReading = Boolean.valueOf(false);
    
    OutputProcessor outputProcessor = new OutputProcessor();
    
    for (String assetPart : this.algorithmMap.keySet()) {
      String fileName = destPath + "/ML" + "/" + assetPart + 
        " Output.csv";
      try {
        System.out.println("processing " + fileName);
        CSVReader outputReader = new CSVReader(fileName, ',', 0);
        outputReaderCollector.put(assetPart, outputReader);
      } catch (IOException e) {
        System.out.println(fileName + " not found");
      }
    }
    
    BinaryGenerator binaryGenerator = new BinaryGenerator(
      this.tsTradedSelectedScripsMap, this.tsTradedNotSelectedScripsMap, 
      this.correlVals, this.tradeStartEndMaps, this.tradeStartDateTradeSideMap, this.bias);
    boolean firstCall = true;
    
    while (!this.rcompleteReading.booleanValue()) {
      modelOutput = outputProcessor.processOutput(outputReaderCollector);
      this.rcompleteReading = Boolean.valueOf(outputProcessor.isRcompleteReading());
      this.rwriteTS = outputProcessor.getRwriteTS();
      

      result = binaryGenerator.generateBinary(modelOutput, 
        this.mlParameter.getSegmentCount().intValue(), 
        this.mlParameter.getOverallCount().intValue(), 
        this.mlParameter.getSegmentCorrelThreshold().doubleValue(), 
        this.mlParameter.getOverallCorrelThreshold().doubleValue(), this.rwriteTS.longValue(), 
        dateList, this.postModelSelectionSegmentWiseAssetUniverseMap, firstCall);
      firstCall = false;
      

      try
      {
        this.mlFinalDecisionWriter.writeAndSaveInMemoryMLDecisions(result, this.rwriteTS);
        HashMap<String, Double> mtmMap = (HashMap)this.tradeMTMMat.get(this.rwriteTS);
        
        writeMLTradeLog(this.rwriteTS, result, mtmMap, modelOutput);
      }
      catch (Exception e) {
        System.out.println("ML Error in writing Output files for " + this.rwriteTS);
        e.printStackTrace();
      }
    }
    

    this.mlFinalDecisionWriter.closeWriter();
    ParameterWriter parameterWriter = new ParameterWriter();
    parameterWriter.createParamDir(sourcePath, destPath, this.mlParameter);
    HashMap<String, TreeMap<Long, Boolean>> assetTimeStampDecisionMap = this.mlFinalDecisionWriter.getAssetTimeStampDecisionMap();
    TradeBookToOrderBookGenerator tradeBookToOrderBookGenerator = 
      new TradeBookToOrderBookGenerator(sourcePath, destPath, assetTimeStampDecisionMap, this.bias);
    tradeBookToOrderBookGenerator.generateOrderBooks();
  }
  
  private TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlRead(String destPath)
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
  






  private void writeMLTradeLog(Long resultDate, HashMap<String, Boolean> result, HashMap<String, Double> mtmList, HashMap<String, Double> modelOutput)
    throws IOException
  {
    String[] logData = new String[5];
    logData[0] = resultDate.toString();
    label258: for (Map.Entry<String, Boolean> entry : result.entrySet()) {
      String assetName = (String)entry.getKey();
      String originalAssetName = assetName;
      
      if (this.bias)
        originalAssetName = assetName.split("#")[1];
      Double mtm;
      Double mtm;
      if (mtmList == null) {
        mtm = Double.valueOf(0.0D);
      } else { if (mtmList.get(originalAssetName) == null) {
          continue;
        }
        mtm = (Double)mtmList.get(originalAssetName);
      }
      logData[1] = assetName;
      if (result.get(assetName) != null)
      {
        if (!((Boolean)result.get(assetName)).booleanValue()) {
          logData[4] = "Not Selected";
        } else if (((Double)modelOutput.get(assetName)).equals(Double.valueOf(Double.POSITIVE_INFINITY))) {
          logData[4] = "Running Trade";
        } else if (this.tsTradedSelectedScripsMap.get(resultDate) == null) {
          logData[4] = "Selected Not Traded";
        } else { if (MathLib.doubleCompare(mtm, Double.valueOf(0.0D)).intValue() == 0)
          {
            if (!((String)this.tsTradedSelectedScripsMap.get(resultDate)).contains(assetName)) {
              logData[4] = "Selected Not Traded";
              break label258; } }
          logData[4] = "Traded";
        }
        logData[2] = mtm.toString();
        logData[3] = ((Double)modelOutput.get(assetName)).toString();
        
        this.mlLogWriter.writeLine(logData);
      }
    }
  }
  
  private void initMLLogWriter() throws Exception
  {
    try
    {
      this.mlLogWriter = new CSVWriter(this.mlPath + "\\DailyFilterLog" + ".csv", false, ",");
    } catch (IOException e) {
      System.out.println("ML Error:Error in creating file for logging Daily filter results");
      throw new IOException();
    }
    
    this.mlLogWriter.write("Date,Asset,Initial MTM,Model Output, Decision\n");
  }
  





  public HashMap<String, DailyDataReader> getDailyReaderCollection()
  {
    return this.dailyReaderCollection;
  }
  
  public CSVWriter getMlLogWriter() {
    return this.mlLogWriter;
  }
  
  public CSVWriter getCorrelLogWriter() {
    return this.correlLogWriter.getCorrelLogWriter();
  }
  
  public HashMap<String, MLAlgo> getAlgorithmMap() {
    return this.algorithmMap;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/MLPostProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */