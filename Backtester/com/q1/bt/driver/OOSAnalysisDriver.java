package com.q1.bt.driver;

import com.q1.bt.driver.backtest.enums.AggregationMode;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.postprocess.PostProcess;
import com.q1.bt.process.backtest.PostProcessMode;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.csv.CSVReader;
import com.q1.math.MathLib;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class OOSAnalysisDriver
{
  BacktesterGlobal btGlobal;
  String[] timeStamps;
  PostProcessMode postProcessMode;
  AggregationMode aggregationMode;
  TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, String>>>>> resultsFileMaps = new TreeMap();
  TreeSet<String> strategySet = new TreeSet();
  TreeSet<String> scripListSet = new TreeSet();
  TreeSet<String> assetClassSet = new TreeSet();
  TreeSet<String> scripSet = new TreeSet();
  TreeSet<Long> dateSet = new TreeSet();
  long startDate = 0L;
  long endDate = 20910101L;
  String outputKey;
  
  public OOSAnalysisDriver(BacktesterGlobal btGlobal, PostProcessMode postProcessMode, AggregationMode aggregationMode)
    throws Exception
  {
    this.btGlobal = btGlobal;
    
    File numberedFolders = new File(btGlobal.loginParameter.getOutputPath());
    this.timeStamps = numberedFolders.list();
    
    this.postProcessMode = postProcessMode;
    this.aggregationMode = aggregationMode;
    generateResultsFileMap();
  }
  

  public TreeSet<String> getSelectableStrategySet()
  {
    TreeSet<String> selectableStrategySet = new TreeSet();
    

    for (String strategyID : ((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).keySet()) {
      selectableStrategySet.add(strategyID);
    }
    return selectableStrategySet;
  }
  


  public TreeSet<String> getSelectableScripListSet(String strategyID)
  {
    TreeSet<String> selectableScripListSet = new TreeSet();
    this.strategySet = new TreeSet();
    
    TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap;
    if (strategyID.equals("All"))
    {
      Iterator localIterator1 = ((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).entrySet().iterator();
      while (localIterator1.hasNext()) {
        Map.Entry<String, TreeMap<String, TreeMap<String, TreeMap<String, String>>>> strategyEntry = (Map.Entry)localIterator1.next();
        

        scripListMap = (TreeMap)strategyEntry.getValue();
        for (String scripListID : scripListMap.keySet()) {
          selectableScripListSet.add(scripListID);
        }
        
        String curStrategyID = (String)strategyEntry.getKey();
        this.strategySet.add(curStrategyID);
      }
      

    }
    else
    {

      TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(strategyID);
      for (String scripListID : scripListMap.keySet()) {
        selectableScripListSet.add(scripListID);
      }
      
      this.strategySet.add(strategyID);
    }
    

    this.outputKey = strategyID;
    
    return selectableScripListSet;
  }
  


  public TreeSet<String> getSelectableAssetClassSet(String scripListID)
  {
    TreeSet<String> selectableAssetClassSet = new TreeSet();
    this.scripListSet = new TreeSet();
    Iterator localIterator1;
    TreeMap<String, TreeMap<String, String>> assetClassMap;
    if (scripListID.equals("All")) { Iterator localIterator2;
      for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
          localIterator2.hasNext())
      {
        String curStrategyID = (String)localIterator1.next();
        
        localIterator2 = ((TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(curStrategyID)).entrySet().iterator(); continue;Map.Entry<String, TreeMap<String, TreeMap<String, String>>> scripListEntry = (Map.Entry)localIterator2.next();
        

        assetClassMap = (TreeMap)scripListEntry.getValue();
        for (String assetClassID : assetClassMap.keySet()) {
          selectableAssetClassSet.add(assetClassID);
        }
        
        String curScripListID = (String)scripListEntry.getKey();
        this.scripListSet.add(curScripListID);
      }
    }
    else
    {
      for (String curStrategyID : this.strategySet)
      {

        TreeMap<String, TreeMap<String, String>> assetClassMap = 
          (TreeMap)((TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(curStrategyID)).get(scripListID);
        for (String assetClassID : assetClassMap.keySet()) {
          selectableAssetClassSet.add(assetClassID);
        }
        
        this.scripListSet.add(scripListID);
      }
    }
    

    this.outputKey = (this.outputKey + "|" + scripListID);
    
    return selectableAssetClassSet;
  }
  

  public TreeSet<String> getSelectableScripSet(String assetClassID)
  {
    TreeSet<String> selectableScripSet = new TreeSet();
    this.assetClassSet = new TreeSet();
    Iterator localIterator1;
    Iterator localIterator2;
    TreeMap<String, String> scripMap; if (assetClassID.equals("All")) {
      for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
          localIterator2.hasNext())
      {
        String curStrategyID = (String)localIterator1.next();
        localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
        
        Iterator localIterator3 = ((TreeMap)((TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(curStrategyID)).get(curScripListID)).entrySet().iterator();
        while (localIterator3.hasNext()) {
          Map.Entry<String, TreeMap<String, String>> assetClassEntry = (Map.Entry)localIterator3.next();
          

          scripMap = (TreeMap)assetClassEntry.getValue();
          for (String scripID : scripMap.keySet()) {
            selectableScripSet.add(scripID);
          }
          
          String curAssetClassID = (String)assetClassEntry.getKey();
          this.assetClassSet.add(curAssetClassID);
        }
        
      }
    } else {
      for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
          localIterator2.hasNext())
      {
        String curStrategyID = (String)localIterator1.next();
        localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
        

        TreeMap<String, String> scripMap = 
          (TreeMap)((TreeMap)((TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(curStrategyID)).get(curScripListID)).get(assetClassID);
        if (scripMap != null)
        {
          for (String scripID : scripMap.keySet()) {
            selectableScripSet.add(scripID);
          }
          
          this.assetClassSet.add(assetClassID);
        }
      }
    }
    


    return selectableScripSet;
  }
  

  public TreeSet<Long> getSelectableDateSet(String scripID)
  {
    TreeSet<Long> selectableDateSet = new TreeSet();
    this.scripSet = new TreeSet();
    Iterator localIterator1;
    Iterator localIterator2;
    if (scripID.equals("All")) {
      for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
          localIterator2.hasNext())
      {
        String curStrategyID = (String)localIterator1.next();
        localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
        for (String curAssetClassID : this.assetClassSet) {
          TreeMap<String, String> scripMap = 
            (TreeMap)((TreeMap)((TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(curStrategyID)).get(curScripListID)).get(curAssetClassID);
          if (scripMap != null)
          {
            for (Map.Entry<String, String> scripEntry : scripMap.entrySet())
            {

              String mtmFilePath = (String)scripEntry.getValue();
              CSVReader reader = null;
              try {
                reader = new CSVReader(mtmFilePath, ',', 0);
                String[] dataLine;
                while ((dataLine = reader.getLine()) != null) { String[] dataLine;
                  selectableDateSet.add(Long.valueOf(Long.parseLong(dataLine[0])));
                }
              }
              catch (Exception localException) {}
              

              String curScripID = (String)scripEntry.getKey();
              this.scripSet.add(curScripID);
            }
          }
        }
      }
    } else {
      for (localIterator1 = this.strategySet.iterator(); localIterator1.hasNext(); 
          localIterator2.hasNext())
      {
        String curStrategyID = (String)localIterator1.next();
        localIterator2 = this.scripListSet.iterator(); continue;String curScripListID = (String)localIterator2.next();
        
        String curAssetClassID = scripID.split(" ")[1];
        

        Object scripMap = 
          (TreeMap)((TreeMap)((TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(curStrategyID)).get(curScripListID)).get(curAssetClassID);
        if (scripMap != null)
        {
          String mtmFilePath = (String)((TreeMap)scripMap).get(scripID);
          try {
            CSVReader reader = new CSVReader(mtmFilePath, ',', 0);
            String[] dataLine;
            while ((dataLine = reader.getLine()) != null) { String[] dataLine;
              selectableDateSet.add(Long.valueOf(Long.parseLong(dataLine[0])));
            }
          }
          catch (Exception localException1) {}
          
          this.scripSet.add(scripID);
        }
      }
    }
    
    this.outputKey = (this.outputKey + "|" + scripID);
    
    return selectableDateSet;
  }
  


  public void updateSetsForKeys(String strategyKey, String scripListKey, String assetClassKey, String scripKey)
  {
    getSelectableScripListSet(strategyKey);
    getSelectableAssetClassSet(assetClassKey);
    getSelectableScripSet(scripListKey);
    getSelectableDateSet(scripKey);
    

    this.startDate = 0L;
    this.endDate = 20910101L;
  }
  


  public PostProcess createPostProcessObject(String timeStamp)
    throws Exception
  {
    TreeMap<Long, Double> mtmMap = generateMTMMap(timeStamp);
    

    ArrayList<String[]> tradeBook = generateTradebook(timeStamp);
    

    return new PostProcess(mtmMap, this.outputKey, tradeBook, this.btGlobal, this.postProcessMode);
  }
  
































  public void generateResults(long startDate, long endDate, int split)
    throws Exception
  {
    ArrayList<HashMap<String, Double>> resultMapsIS = new ArrayList();
    ArrayList<HashMap<String, Double>> resultMapsOS = new ArrayList();
    
    if (split < 10) { split = 10;
    } else if (split > 90) { split = 90;
    }
    long years = (0.01D * (endDate - startDate) * split);
    years /= 10000L;
    

    this.startDate = startDate;
    this.endDate = (endDate - years * 10000L);
    String[] arrayOfString;
    int j = (arrayOfString = this.timeStamps).length; for (int i = 0; i < j; i++) { String timeStamp = arrayOfString[i];
      
      PostProcess ppObj = createPostProcessObject(timeStamp);
      
      try
      {
        ppObj.runPostprocess();
      }
      catch (ParseException e) {
        this.btGlobal.displayMessage("Error running post process");
        e.printStackTrace();
      }
      
      resultMapsIS.add(ppObj.getResultsMap());
    }
    

    this.startDate = this.endDate;
    this.endDate = endDate;
    
    j = (arrayOfString = this.timeStamps).length; for (i = 0; i < j; i++) { String timeStamp = arrayOfString[i];
      
      PostProcess ppObj = createPostProcessObject(timeStamp);
      
      try
      {
        ppObj.runPostprocess();
      }
      catch (ParseException e) {
        this.btGlobal.displayMessage("Error running post process");
        e.printStackTrace();
      }
      
      resultMapsOS.add(ppObj.getResultsMap());
    }
    

    this.btGlobal.displayResults(resultMapsIS, resultMapsOS);
  }
  
















































































































  public ArrayList<String[]> generateTradebook(String timeStamp)
    throws Exception
  {
    ArrayList<String[]> consolTradebook = new ArrayList();
    

    for (String strategyID : this.strategySet)
    {

      ArrayList<String[]> strategyTradebook = new ArrayList();
      

      for (String scripListID : this.scripListSet)
      {

        ArrayList<String[]> scripListTradebook = new ArrayList();
        

        for (String scripID : this.scripSet)
        {

          if (checkIfResultExpected(strategyID, scripListID, scripID))
          {

            ArrayList<String[]> scripTradebook = getStrategyScripListScripTradebook(timeStamp, strategyID, scripListID, 
              scripID);
            
            if (!this.postProcessMode.equals(PostProcessMode.Portfolio)) {
              scripTradebook = cleanTradebookForRunningTrades(scripTradebook);
            }
            

            scripListTradebook.addAll(scripTradebook);
          }
        }
        
        if (this.postProcessMode.equals(PostProcessMode.Spread)) {
          scripListTradebook = timeSortTradebook(scripListTradebook);
        }
        

        strategyTradebook.addAll(scripListTradebook);
      }
      


      consolTradebook.addAll(strategyTradebook);
    }
    
    return consolTradebook;
  }
  
  private ArrayList<String[]> timeSortTradebook(ArrayList<String[]> scripListTradebook) {
    TreeMap<String, ArrayList<String[]>> tradeBookMap = new TreeMap();
    

    for (String[] line : scripListTradebook) {
      if (tradeBookMap.get(line[0]) == null) {
        ArrayList<String[]> trades = new ArrayList();
        trades.add(line);
        tradeBookMap.put(line[0], trades);
      } else {
        trades = (ArrayList)tradeBookMap.get(line[0]);
        trades.add(line);
        tradeBookMap.put(line[0], trades);
      }
    }
    

    ArrayList<String[]> sortedTradeBook = new ArrayList();
    
    Iterator localIterator2;
    for (ArrayList<String[]> trades = tradeBookMap.keySet().iterator(); trades.hasNext(); 
        
        localIterator2.hasNext())
    {
      String key = (String)trades.next();
      ArrayList<String[]> trades = (ArrayList)tradeBookMap.get(key);
      localIterator2 = trades.iterator(); continue;String[] line = (String[])localIterator2.next();
      sortedTradeBook.add(line);
    }
    
    return sortedTradeBook;
  }
  



































































  public ArrayList<String[]> getStrategyScripListScripTradebook(String timeStamp, String strategyID, String scripListID, String scripID)
  {
    ArrayList<String[]> tradeBook = new ArrayList();
    String tbPath = this.btGlobal.loginParameter.getOutputPath() + "/" + timeStamp + "/Trade Data";
    String tbFilePath = tbPath + "/" + strategyID + " " + scripListID + "/" + scripID + " Tradebook.csv";
    CSVReader reader = null;
    try {
      reader = new CSVReader(tbFilePath, ',', 0);
      String[] dataLine;
      while ((dataLine = reader.getLine()) != null) {
        String[] dataLine;
        long date = Long.parseLong(dataLine[0]) / 1000000L;
        

        if (date >= this.startDate)
        {


          if (date > this.endDate) {
            break;
          }
          
          tradeBook.add(dataLine);
        }
      }
    } catch (IOException e1) { this.btGlobal.displayMessage("Could not find Tradebook: " + strategyID + " " + scripListID + " " + scripID);
      e1.printStackTrace();
      return null;
    }
    
    return tradeBook;
  }
  



  public TreeMap<Long, Double> generateMTMMap(String timeStamp)
    throws Exception
  {
    TreeMap<Long, Double> consolMTMMap = new TreeMap();
    

    for (String strategyID : this.strategySet)
    {

      TreeMap<Long, Double> strategyMTMMap = new TreeMap();
      

      for (String scripListID : this.scripListSet)
      {

        TreeMap<Long, Double> scripListMTMMap = new TreeMap();
        

        for (String scripID : this.scripSet)
        {

          if (checkIfResultExpected(strategyID, scripListID, scripID))
          {

            TreeMap<Long, Double> scripMTMMap = getStrategyScripListScripMTM(timeStamp, strategyID, scripListID, scripID);
            

            appendMTMmap(scripListMTMMap, scripMTMMap);
          }
        }
        

        appendMTMmap(strategyMTMMap, scripListMTMMap);
      }
      


      TreeMap<Long, Integer> activeScripCountMap = new TreeMap();
      




      adjustMTMmap(strategyMTMMap, Integer.valueOf(this.scripListSet.size()), "Fixed", activeScripCountMap);
      

      appendMTMmap(consolMTMMap, strategyMTMMap);
    }
    

    adjustMTMmap(consolMTMMap, Integer.valueOf(this.strategySet.size()), "Fixed", new TreeMap());
    
    return consolMTMMap;
  }
  






















































































  public TreeMap<Long, Double> getStrategyScripListScripMTM(String timeStamp, String strategyID, String scripListID, String scripID)
  {
    TreeMap<Long, Double> mtmMap = new TreeMap();
    
    String mtmPath = this.btGlobal.loginParameter.getOutputPath() + "/" + timeStamp + "/MTM Data/" + strategyID + " " + 
      scripListID;
    
    String mtmFilePath = mtmPath + "/" + scripID + " MTM.csv";
    
    CSVReader reader = null;
    try {
      reader = new CSVReader(mtmFilePath, ',', 0);
      String[] inData;
      while ((inData = reader.getLine()) != null) {
        String[] inData;
        Long date = Long.valueOf(Long.parseLong(inData[0]));
        

        if (date.longValue() >= this.startDate)
        {


          if (date.longValue() > this.endDate) {
            break;
          }
          
          Double mtm = Double.valueOf(Double.parseDouble(inData[1]));
          mtmMap.put(date, mtm);
        }
      }
    } catch (IOException e1) { this.btGlobal.displayMessage("Could not find MTM File: " + strategyID + " " + scripListID + " " + scripID);
      e1.printStackTrace();
      return null;
    }
    
    return mtmMap;
  }
  





  public boolean checkIfResultExpected(String strategyID, String scripListID, String scripID)
  {
    TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListMap = (TreeMap)((TreeMap)this.resultsFileMaps.get(this.timeStamps[0])).get(strategyID);
    if (scripListMap == null) {
      return false;
    }
    
    TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListMap.get(scripListID);
    if (assetClassMap == null) {
      return false;
    }
    for (Map.Entry<String, TreeMap<String, String>> assetClassEntry : assetClassMap.entrySet()) {
      TreeMap<String, String> scripMap = (TreeMap)assetClassEntry.getValue();
      
      if (scripMap.keySet().contains(scripID)) {
        return true;
      }
    }
    return false;
  }
  


  public ArrayList<String[]> cleanTradebookForRunningTrades(ArrayList<String[]> tradeBook)
  {
    Double curPosition = Double.valueOf(0.0D);
    Double prevPosition = Double.valueOf(0.0D);
    int lastExitIndex = 0;
    for (int i = 0; i < tradeBook.size(); i++) {
      String[] trade = (String[])tradeBook.get(i);
      String side = trade[3];
      String type = trade[4];
      
      if (!type.equals("ROLLOVER"))
      {

        int signal = 0;
        if (side.equals("BUY")) {
          signal = 1;
        } else {
          signal = -1;
        }
        prevPosition = curPosition;
        curPosition = Double.valueOf(curPosition.doubleValue() + signal * Double.parseDouble(trade[7]));
        
        if (((MathLib.doubleCompare(curPosition, Double.valueOf(0.0D)).intValue() == 0 ? 1 : 0) & (MathLib.doubleCompare(prevPosition, Double.valueOf(0.0D)).intValue() != 0 ? 1 : 0)) != 0) {
          lastExitIndex = i;
        }
      }
    }
    

    while (tradeBook.size() > lastExitIndex + 1) {
      tradeBook.remove(lastExitIndex + 1);
    }
    
    return tradeBook;
  }
  

  public void appendMTMmap(TreeMap<Long, Double> currentMap, TreeMap<Long, Double> newMap)
  {
    if (newMap.size() == 0) {
      return;
    }
    for (Map.Entry<Long, Double> mtmEntry : newMap.entrySet()) {
      Long dateTime = (Long)mtmEntry.getKey();
      Double mtm = (Double)mtmEntry.getValue();
      Double curMTM = Double.valueOf(0.0D);
      try {
        curMTM = (Double)currentMap.get(dateTime);
        if (curMTM == null)
          curMTM = Double.valueOf(0.0D);
        currentMap.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
      } catch (Exception e) {
        curMTM = Double.valueOf(0.0D);
        currentMap.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
      }
    }
  }
  


  public void adjustMTMmap(TreeMap<Long, Double> currentMap, Integer count, String aggregationMode, TreeMap<Long, Integer> activeScripCountMap)
    throws Exception
  {
    if (aggregationMode.equalsIgnoreCase("Fixed")) {
      for (Map.Entry<Long, Double> mtmEntry : new TreeMap(currentMap).entrySet()) {
        Long dateTime = (Long)mtmEntry.getKey();
        Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / count.doubleValue());
        currentMap.put(dateTime, mtm);

      }
      
    }
    else if (aggregationMode.equalsIgnoreCase("Active")) {
      for (Map.Entry<Long, Double> mtmEntry : new TreeMap(currentMap).entrySet()) {
        Long dateTime = (Long)mtmEntry.getKey();
        Integer activeScripCount = (Integer)activeScripCountMap.get(dateTime);
        if (activeScripCount == null) {
          this.btGlobal.displayMessage("Scrip count for date: " + dateTime + " not available");
          throw new Exception("Scrip count for date: " + dateTime + " not available");
        }
        Double mtm = Double.valueOf(activeScripCount.intValue() < 1 ? 0.0D : ((Double)mtmEntry.getValue()).doubleValue() / activeScripCount.intValue());
        currentMap.put(dateTime, mtm);
      }
    }
  }
  

  public TreeMap<Long, Double> combineMTMmaps(HashMap<String, TreeMap<Long, Double>> mtmMaps)
  {
    Integer count = Integer.valueOf(mtmMaps.size());
    TreeMap<Long, Double> consolMTM = new TreeMap();
    for (Map.Entry<String, TreeMap<Long, Double>> entry : mtmMaps.entrySet()) {
      TreeMap<Long, Double> mtmMap = (TreeMap)entry.getValue();
      if (mtmMap.size() != 0)
      {
        for (Map.Entry<Long, Double> mtmEntry : mtmMap.entrySet()) {
          Long dateTime = (Long)mtmEntry.getKey();
          Double mtm = (Double)mtmEntry.getValue();
          Double curMTM = Double.valueOf(0.0D);
          try {
            curMTM = (Double)consolMTM.get(dateTime);
            if (curMTM == null)
              curMTM = Double.valueOf(0.0D);
            consolMTM.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
          } catch (Exception e) {
            curMTM = Double.valueOf(0.0D);
            consolMTM.put(dateTime, Double.valueOf(curMTM.doubleValue() + mtm.doubleValue()));
          }
        } }
    }
    for (Map.Entry<Long, Double> mtmEntry : new TreeMap(consolMTM).entrySet()) {
      Long dateTime = (Long)mtmEntry.getKey();
      Double mtm = Double.valueOf(((Double)mtmEntry.getValue()).doubleValue() / count.doubleValue());
      consolMTM.put(dateTime, mtm);
    }
    return consolMTM;
  }
  
  public void updateNumActiveScrips(TreeMap<Long, Integer> activeScripMap, String scripName, String auxFilesPath) {
    CSVReader reader = null;
    try {
      reader = new CSVReader(auxFilesPath + "\\" + scripName + ".csv", ',', 0);
      String[] line = reader.getLine();
      Long firstDate = Long.valueOf(Long.parseLong(line[0]));
      int isThisScripActive = Integer.parseInt(line[2]);
      activeScripMap.put(firstDate, Integer.valueOf(0));
      while ((line = reader.getLine()) != null) {
        Long date = Long.valueOf(Long.parseLong(line[0]));
        if (activeScripMap.containsKey(date)) {
          int currentNumActiveScrips = ((Integer)activeScripMap.get(date)).intValue();
          activeScripMap.put(date, Integer.valueOf(currentNumActiveScrips + isThisScripActive));
        } else {
          activeScripMap.put(date, Integer.valueOf(isThisScripActive));
        }
        isThisScripActive = Integer.parseInt(line[2]);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  


  String getMTMFilePath(String timeStamp, String strategyID, String scripListID, String assetClassID, String scripID)
  {
    return (String)((TreeMap)((TreeMap)((TreeMap)((TreeMap)this.resultsFileMaps.get(timeStamp)).get(strategyID)).get(scripListID)).get(assetClassID)).get(scripID);
  }
  
  public void generateResultsFileMap() throws Exception
  {
    String[] arrayOfString1;
    int j = (arrayOfString1 = this.timeStamps).length; for (int i = 0; i < j; i++) { String timeStamp = arrayOfString1[i];
      
      TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, String>>>> resultsFileMap = new TreeMap();
      
      String mtmFolderPath = this.btGlobal.loginParameter.getOutputPath() + "/" + timeStamp + "/MTM Data";
      
      File mtmFolderPathFile = new File(mtmFolderPath);
      File[] mtmFolders = mtmFolderPathFile.listFiles();
      
      File[] arrayOfFile1;
      int m = (arrayOfFile1 = mtmFolders).length; for (int k = 0; k < m; k++) { File mtmFolder = arrayOfFile1[k];
        

        String[] mtmFolderVal = mtmFolder.getName().split(" ");
        
        String strategyID = mtmFolderVal[0];
        

        TreeMap<String, TreeMap<String, TreeMap<String, String>>> scripListFileMap = (TreeMap)resultsFileMap.get(strategyID);
        if (scripListFileMap == null) {
          scripListFileMap = new TreeMap();
        }
        String scripListID = mtmFolderVal[1];
        if (mtmFolderVal.length > 2) {
          scripListID = 
            mtmFolderVal[1] + " " + mtmFolderVal[2] + " " + mtmFolderVal[3] + " " + mtmFolderVal[4] + " " + mtmFolderVal[5];
        }
        
        String[] splits = mtmFolderVal[1].split("\\$");
        String assetClassID = splits[1];
        TreeMap<String, TreeMap<String, String>> assetClassMap = (TreeMap)scripListFileMap.get(assetClassID);
        if (assetClassMap == null) {
          assetClassMap = new TreeMap();
        }
        
        TreeMap<String, String> scripFileMap = (TreeMap)assetClassMap.get(scripListID);
        if (scripFileMap == null) {
          scripFileMap = new TreeMap();
        }
        
        String mtmPath = mtmFolderPath + "/" + mtmFolder.getName();
        File mtmPathFile = new File(mtmPath);
        File[] mtmFiles = mtmPathFile.listFiles();
        
        File[] arrayOfFile2;
        int i1 = (arrayOfFile2 = mtmFiles).length; for (int n = 0; n < i1; n++) { File mtmFile = arrayOfFile2[n];
          String mtmFileName = mtmFile.getName();
          String scripID = mtmFileName.substring(0, mtmFileName.length() - 8);
          

          String mtmFilePath = mtmFile.getAbsolutePath();
          scripFileMap.put(scripID, mtmFilePath);
        }
        

        assetClassMap.put(assetClassID, scripFileMap);
        scripListFileMap.put(scripListID, assetClassMap);
        resultsFileMap.put(strategyID, scripListFileMap);
      }
      
      this.resultsFileMaps.put(timeStamp, resultsFileMap);
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/OOSAnalysisDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */