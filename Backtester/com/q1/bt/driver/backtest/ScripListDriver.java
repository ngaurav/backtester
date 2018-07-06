package com.q1.bt.driver.backtest;

import com.q1.bt.data.DataDriver;
import com.q1.bt.data.DataTypeHandler;
import com.q1.bt.data.ScripDataHandler;
import com.q1.bt.data.classes.Scrip;
import com.q1.bt.driver.backtest.enums.OutputMode;
import com.q1.bt.execution.ScripExecution;
import com.q1.bt.execution.Strategy;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.postprocess.PostProcess;
import com.q1.bt.process.backtest.PostProcessMode;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.parameter.BacktestParameter;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.csv.CSVReader;
import com.q1.csv.CSVWriter;
import com.q1.csv.ReverseCSVReader;
import com.q1.exception.bt.ExpiredContractException;
import com.q1.exception.bt.IllegalOrderTypeException;
import com.q1.exception.bt.IllegalQuantityException;
import com.q1.exception.bt.MissingExpiryException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.io.FileUtils;

public class ScripListDriver extends Thread
{
  BacktesterGlobal btGlobal;
  public DataDriver dataDriver;
  public HashMap<String, CSVWriter> scripMTMWriterMap = new HashMap();
  Long prevDate = null;
  
  PostProcess consolPP;
  
  Double capital;
  
  String scripListID;
  
  String strategyID;
  ArrayList<Scrip> scripSet;
  Backtest backtest;
  OutputMode outputMode;
  String strategyDataType;
  String backtestKey;
  Long startDate;
  Long endDate;
  BacktestDriver btDriver;
  
  public ScripListDriver(String scripListID, ArrayList<Scrip> scripSet, String strategyID, String strategyDataType, BacktesterGlobal btGlobal, Backtest backtest, String parameterKey, HashMap<String, HashMap<String, HashMap<String, String>>> outputMap)
    throws Exception
  {
    this.btGlobal = btGlobal;
    this.backtest = backtest;
    
    this.scripListID = scripListID;
    this.strategyID = strategyID;
    this.scripSet = scripSet;
    
    this.capital = btGlobal.loginParameter.getCapital();
    
    this.backtestKey = (strategyID + " " + scripListID);
    
    this.strategyDataType = strategyDataType;
    

    this.dataDriver = new DataDriver(btGlobal, Long.valueOf(backtest.backtestParameter.getStartDate()), backtest, scripListID, scripSet, strategyDataType);
    

    updateStartAndEndDate();
    

    checkOutput(parameterKey, outputMap);
  }
  


  public void run()
  {
    String mtmPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data/" + 
      this.backtestKey;
    createPath(mtmPath);
    for (Scrip scrip : this.scripSet) {
      try {
        CSVWriter mtmWriter = new CSVWriter(mtmPath + "/" + scrip.scripID + " MTM.csv", false, ",");
        this.scripMTMWriterMap.put(scrip.scripID, mtmWriter);
      }
      catch (IOException e) {
        e.printStackTrace();
        this.btGlobal.displayMessage("Error creating MTM Writer for: " + this.backtestKey + " " + scrip.scripID);
        return;
      }
    }
    
    try
    {
      while (this.dataDriver.updateData())
      {

        runDailyMTMRoutine(false);
        

        this.dataDriver.updateDataViewers();
        

        if (!runBacktestRoutine()) {
          return;
        }
      }
    } catch (Exception e1) {
      e1.printStackTrace();
      return;
    }
    

    runDailyMTMRoutine(true);
    

    closeFiles();
  }
  


  public void updateStartAndEndDate()
  {
    Collection<ScripDataHandler> sdHandlerList = 
      ((DataTypeHandler)this.dataDriver.dataTypeHandlerMap.get(this.strategyDataType)).scripDataHandlerMap.values();
    
    PostProcessMode ppMode = this.backtest.backtestParameter.getPostProcessMode();
    
    this.startDate = Long.valueOf(this.backtest.backtestParameter.getStartDate());
    this.endDate = Long.valueOf(this.backtest.backtestParameter.getEndDate());
    
    if (!ppMode.equals(PostProcessMode.Portfolio)) {
      for (ScripDataHandler sdHandler : sdHandlerList) {
        if (sdHandler.startDate.longValue() > this.startDate.longValue())
          this.startDate = sdHandler.startDate;
        if (sdHandler.endDate.longValue() < this.endDate.longValue()) {
          this.endDate = sdHandler.endDate;
        }
      }
    }
  }
  

  public void createBacktestDriver(Strategy strategy)
    throws IOException
  {
    this.btDriver = new BacktestDriver(this.scripListID, this.scripSet, strategy, this.dataDriver.dataTypeViewerMap, this.btGlobal, this.backtest, 
      this.startDate, this.endDate);
  }
  

  public void writeMTMToFile(CSVWriter writer, String key, Double mtm)
    throws IOException
  {
    String[] mtmVal = { this.prevDate.toString(), mtm.toString() };
    writer.writeLine(mtmVal);
  }
  


  public void closeFiles()
  {
    this.btDriver.closeFileStreams();
    try {
      this.btDriver.serializeObjects();
    } catch (IOException e) {
      e.printStackTrace();
      this.btGlobal.displayMessage("Error Serializing");
    }
    

    for (CSVWriter writer : this.scripMTMWriterMap.values()) {
      try {
        writer.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  public TreeMap<Long, Double> convertMap(TreeMap<Long, Double[]> mtmMap) {
    TreeMap<Long, Double> consolMTM = new TreeMap();
    
    Double cumMTM = Double.valueOf(0.0D);
    for (Map.Entry<Long, Double[]> entry : mtmMap.entrySet()) {
      Long dateTime = (Long)entry.getKey();
      Double[] mtmVal = (Double[])entry.getValue();
      cumMTM = Double.valueOf(cumMTM.doubleValue() + mtmVal[0].doubleValue() / mtmVal[1].doubleValue());
      if (com.q1.math.MathLib.doubleCompare(cumMTM, Double.valueOf(0.0D)).intValue() != 0) {
        consolMTM.put(dateTime, Double.valueOf(mtmVal[0].doubleValue() / mtmVal[1].doubleValue()));
      }
    }
    
    return consolMTM;
  }
  
  public TreeMap<Long, Double> getMTMMap(String key, String mtmPath) {
    String mtmFilePath = mtmPath + "/" + key + " MTM.csv";
    TreeMap<Long, Double> mtmMap = new TreeMap();
    try {
      CSVReader reader = new CSVReader(mtmFilePath, ',', 0);
      String[] mtmLine;
      while ((mtmLine = reader.getLine()) != null) { String[] mtmLine;
        Long date = Long.valueOf(Long.parseLong(mtmLine[0]));
        Double mtm = Double.valueOf(Double.parseDouble(mtmLine[1]));
        mtmMap.put(date, mtm);
      }
    }
    catch (IOException e1) {
      this.btGlobal.displayMessage("Could not find MTM File for: " + key);
      e1.printStackTrace();
      return null;
    }
    return mtmMap;
  }
  
  public static void createPath(String folder)
  {
    if (!new File(folder).exists()) {
      new File(folder).mkdirs();
    }
  }
  

  public void runDailyMTMRoutine(boolean forceWrite)
  {
    if (this.prevDate == null) {
      if (!this.dataDriver.strategyDate.equals(Long.valueOf(0L))) {
        this.prevDate = this.dataDriver.strategyDate;
      }
    }
    else if ((!this.prevDate.equals(this.dataDriver.strategyDate)) || (forceWrite))
    {
      for (Map.Entry<String, CSVWriter> entry : this.scripMTMWriterMap.entrySet())
      {
        String scripID = (String)entry.getKey();
        CSVWriter writer = (CSVWriter)entry.getValue();
        

        Double $mtm = Double.valueOf(0.0D);
        ScripExecution scripExec = (ScripExecution)this.btDriver.execution.executionMap.get(scripID);
        $mtm = Double.valueOf($mtm.doubleValue() + scripExec.$CumMTM.doubleValue() - scripExec.$PrevDayCumMTM.doubleValue());
        scripExec.$PrevDayCumMTM = scripExec.$CumMTM;
        

        Double percMTM = Double.valueOf($mtm.doubleValue() / this.capital.doubleValue());
        

        if ((this.prevDate.longValue() > this.startDate.longValue()) || (this.prevDate.equals(this.startDate))) {
          try {
            writeMTMToFile(writer, this.backtestKey, percMTM);
          } catch (IOException e) {
            e.printStackTrace();
            this.btGlobal.displayMessage("Error writing MTM to file for: " + this.backtestKey);
            return;
          }
        }
      }
      


      this.prevDate = this.dataDriver.strategyDate;
    }
  }
  
  public boolean runBacktestRoutine()
  {
    try {
      this.btDriver.runBacktest();
    } catch (IllegalQuantityException e) {
      this.btGlobal.displayMessage(
        this.strategyID + " " + this.scripListID + ": Order quantity exceeds maximum permissible quantity");
      e.printStackTrace();
      return false;
    } catch (MissingExpiryException e) {
      this.btGlobal.displayMessage(this.strategyID + " " + this.scripListID + ": Order expiry not found in current data point");
      e.printStackTrace();
      return false;
    } catch (IllegalOrderTypeException e) {
      this.btGlobal.displayMessage(this.strategyID + " " + this.scripListID + 
        ": Illegal Order Type. Order type should either be Buy (1.0) or Sell (2.0)");
      e.printStackTrace();
      return false;
    } catch (ExpiredContractException e) {
      this.btGlobal.displayMessage(this.strategyID + " " + this.scripListID + ": Position still exists in expired contract");
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      this.btGlobal.displayMessage(this.strategyID + " " + this.scripListID + ": Error writing orderBook");
      e.printStackTrace();
      return false;
    } catch (NullPointerException e) {
      this.btGlobal.displayMessage(this.strategyID + " " + this.scripListID + ": Strategy Error");
      e.printStackTrace();
      return false;
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  

  public String checkIfBacktestExists(String strategyParamKey, HashMap<String, HashMap<String, HashMap<String, String>>> outputMap)
    throws IOException
  {
    HashMap<String, HashMap<String, String>> scripListDateMap = (HashMap)outputMap.get(strategyParamKey);
    

    String scripKey = this.scripListID;
    TreeSet<String> scripIDSet = new TreeSet();
    for (Scrip scrip : this.scripSet)
      scripIDSet.add(scrip.scripID);
    for (String scripID : scripIDSet) {
      scripKey = scripKey + "|" + scripID;
    }
    
    if (scripListDateMap != null)
    {
      HashMap<String, String> dateMap = (HashMap)scripListDateMap.get(scripKey);
      
      if (dateMap == null) {
        return null;
      }
      String outputTS = (String)dateMap.get(this.startDate.toString() + " " + this.endDate.toString());
      

      if (outputTS != null) {
        this.outputMode = OutputMode.Existing;
        return outputTS;
      }
      



      String maxKey = "";
      Long maxEndDate = Long.valueOf(0L);
      for (String key : dateMap.keySet()) {
        String[] dateVal = key.split(" ");
        Long sDate = Long.valueOf(Long.parseLong(dateVal[0]));
        Long eDate = Long.valueOf(Long.parseLong(dateVal[1]));
        if ((eDate.longValue() > maxEndDate.longValue()) && (sDate.equals(this.startDate))) {
          maxEndDate = eDate;
          maxKey = key;
        }
      }
      
      if (maxEndDate.longValue() > this.endDate.longValue()) {
        this.outputMode = OutputMode.Chop;
        return (String)dateMap.get(maxKey);
      }
      return null;
    }
    

    return null;
  }
  



  public void checkOutput(String parameterKey, HashMap<String, HashMap<String, HashMap<String, String>>> outputMap)
    throws IOException
  {
    this.outputMode = OutputMode.Normal;
    




    String primaryKey = this.strategyID + " " + parameterKey;
    String existingTS = null;
    

    if (!this.backtest.backtestParameter.isSkipExistingBacktest())
      existingTS = checkIfBacktestExists(primaryKey, outputMap);
    CSVReader reader;
    ArrayList<String[]> scripListDates;
    String[] inData;
    if (this.outputMode.equals(OutputMode.Existing))
    {

      String currentPath = this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/MTM Data/" + this.strategyID + 
        " " + this.scripListID;
      String newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data/" + 
        this.strategyID + " " + this.scripListID;
      if (!new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data").exists())
        new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data").mkdirs();
      File currentFile = new File(currentPath);
      File newFile = new File(newPath);
      FileUtils.copyDirectory(currentFile, newFile);
      try {
        FileUtils.deleteDirectory(currentFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
      

      currentPath = 
        this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/Trade Data/" + this.strategyID + " " + this.scripListID;
      newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Trade Data/" + this.strategyID + 
        " " + this.scripListID;
      
      if (!new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Trade Data").exists())
        new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Trade Data").mkdirs();
      currentFile = new File(currentPath);
      newFile = new File(newPath);
      FileUtils.copyDirectory(currentFile, newFile);
      try {
        FileUtils.deleteDirectory(currentFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
      

      currentPath = 
        this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/Post Process Data/" + this.strategyID + " " + this.scripListID;
      newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Post Process Data/" + 
        this.strategyID + " " + this.scripListID;
      
      if (!new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Post Process Data").exists())
      {
        new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Post Process Data").mkdirs(); }
      currentFile = new File(currentPath);
      newFile = new File(newPath);
      FileUtils.copyDirectory(currentFile, newFile);
      try {
        FileUtils.deleteDirectory(currentFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
      

      currentPath = 
        this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/Parameters/" + this.scripListID + " ScripSet.csv";
      newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Parameters/" + this.scripListID + 
        " ScripSet.csv";
      currentFile = new File(currentPath);
      newFile = new File(newPath);
      FileUtils.copyFile(currentFile, newFile);
      

      String paramPath = this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/Parameters/" + this.strategyID + 
        " ScripListDateMap.csv";
      reader = new CSVReader(paramPath, ',', 0);
      
      scripListDates = new ArrayList();
      while ((inData = reader.getLine()) != null) { String[] inData;
        if (!inData[0].equals(this.scripListID))
          scripListDates.add(inData); }
      CSVWriter writer = new CSVWriter(paramPath, false, ",");
      for (String[] scrip : scripListDates)
        writer.writeLine(scrip);
      writer.close();


    }
    else if (this.outputMode.equals(OutputMode.Chop))
    {

      String currentPath = this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/MTM Data/" + this.strategyID + 
        " " + this.scripListID;
      String newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data/" + 
        this.strategyID + " " + this.scripListID;
      if (!new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data").exists())
        new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data").mkdirs();
      File currentFile = new File(currentPath);
      File newFile = new File(newPath);
      FileUtils.copyDirectory(currentFile, newFile);
      
      inData = (scripListDates = newFile.listFiles()).length; for (reader = 0; reader < inData; reader++) { File mtmFile = scripListDates[reader];
        String curPath = mtmFile.getAbsolutePath();
        chopFile(curPath, this.endDate);
      }
      

      currentPath = 
        this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/Trade Data/" + this.strategyID + " " + this.scripListID;
      newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Trade Data/" + this.strategyID + 
        " " + this.scripListID;
      
      if (!new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Trade Data").exists())
        new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Trade Data").mkdirs();
      currentFile = new File(currentPath);
      newFile = new File(newPath);
      FileUtils.copyDirectory(currentFile, newFile);
      
      inData = (scripListDates = newFile.listFiles()).length; for (reader = 0; reader < inData; reader++) { File tradeBookFile = scripListDates[reader];
        String curPath = tradeBookFile.getAbsolutePath();
        chopFileDateTime(curPath, this.endDate);
      }
      

      currentPath = 
        this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/Post Process Data/" + this.strategyID + " " + this.scripListID;
      newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Post Process Data/" + 
        this.strategyID + " " + this.scripListID;
      
      if (!new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Post Process Data").exists())
      {
        new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Post Process Data").mkdirs(); }
      currentFile = new File(currentPath);
      newFile = new File(newPath);
      FileUtils.copyDirectory(currentFile, newFile);
      
      inData = (scripListDates = newFile.listFiles()).length; for (reader = 0; reader < inData; reader++) { File postFile = scripListDates[reader];
        String curPath = postFile.getAbsolutePath();
        chopFile(curPath, this.endDate);
      }
    }
    


    String outPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Parameters";
    if (!new File(outPath).exists())
      new File(outPath).mkdirs();
    String scripListDatePath = outPath + "/" + this.strategyID + " ScripListDateMap.csv";
    CSVWriter writer = new CSVWriter(scripListDatePath, true, ",");
    

    String[] scripOutput = { this.scripListID, this.startDate.toString(), this.endDate.toString() };
    writer.writeLine(scripOutput);
    writer.close();
    

    String scripListScripPath = outPath + "/" + this.scripListID + " ScripSet.csv";
    if (!new File(scripListScripPath).exists()) {
      writer = new CSVWriter(scripListScripPath, false, ",");
      
      TreeSet<String> scripIDSet = new TreeSet();
      for (Scrip scrip : this.scripSet) {
        scripIDSet.add(scrip.scripID);
      }
      for (String scripID : scripIDSet)
        writer.writeLine(scripID);
      writer.close();
    }
  }
  
  public static void chopFile(String fileName, Long endDate)
    throws IOException
  {
    ReverseCSVReader reader = new ReverseCSVReader(fileName, ',', 0);
    

    Long deleteCount = Long.valueOf(0L);
    String[] line;
    while ((line = reader.readLineAsArray()) != null) {
      String[] line;
      Long curDate = Long.valueOf(Long.parseLong(line[0]));
      
      if (curDate.longValue() <= endDate.longValue()) break;
      String[] arrayOfString1; int j = (arrayOfString1 = line).length; for (int i = 0; i < j; i++) { String token = arrayOfString1[i];
        deleteCount = Long.valueOf(deleteCount.longValue() + token.length());
      }
      deleteCount = Long.valueOf(deleteCount.longValue() + line.length);
    }
    


    reader.close();
    

    RandomAccessFile f = new RandomAccessFile(fileName, "rw");
    long fileLength = f.length();
    f.setLength(fileLength - deleteCount.longValue());
    f.close();
  }
  
  public static void chopFileDateTime(String fileName, Long endDate) throws IOException
  {
    ReverseCSVReader reader = new ReverseCSVReader(fileName, ',', 0);
    

    Long deleteCount = Long.valueOf(0L);
    String[] line;
    while ((line = reader.readLineAsArray()) != null) {
      String[] line;
      Long curDate = Long.valueOf(Long.parseLong(line[0]) / 1000000L);
      
      if (curDate.longValue() <= endDate.longValue()) break;
      String[] arrayOfString1; int j = (arrayOfString1 = line).length; for (int i = 0; i < j; i++) { String token = arrayOfString1[i];
        deleteCount = Long.valueOf(deleteCount.longValue() + token.length());
      }
      deleteCount = Long.valueOf(deleteCount.longValue() + line.length);
    }
    


    reader.close();
    

    RandomAccessFile f = new RandomAccessFile(fileName, "rw");
    long fileLength = f.length();
    f.setLength(fileLength - deleteCount.longValue());
    f.close();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/backtest/ScripListDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */