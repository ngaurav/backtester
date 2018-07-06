package com.q1.bt.machineLearning.utility;

import com.q1.bt.postprocess.TradebookProcessor;
import com.q1.csv.CSVReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TradeAndMTMDataProcessor
{
  String sourcePath;
  TreeMap<Long, HashMap<String, Double>> dateAssetTradePnLMap;
  HashMap<String, TreeMap<Long, Double>> dailyMTMMap;
  HashMap<String, TreeMap<Long, Long>> assetStartDateEndDateMaps;
  HashMap<String, TreeMap<Long, Integer>> assetStartDateTradeSideMaps;
  
  public TradeAndMTMDataProcessor(String sourcePath)
  {
    this.sourcePath = sourcePath;
    
    this.dateAssetTradePnLMap = new TreeMap();
    this.dailyMTMMap = new HashMap();
    this.assetStartDateEndDateMaps = new HashMap();
    this.assetStartDateTradeSideMaps = new HashMap();
  }
  
  public void processTradeBooksAndMTMs() throws Exception
  {
    HashMap<String, TreeMap<Long, Double>> assetDateTimeTradePnLMap = processTradeBooks();
    HashMap<String, ArrayList<Long>> assetDateListMap = processMTMFiles();
    
    HashMap<String, TreeMap<Long, Double>> assetDateTradePnLMaps = extractDailyTradePnLMapsSum(
      assetDateTimeTradePnLMap, assetDateListMap);
    

    this.dateAssetTradePnLMap = combineMTMmaps(assetDateTradePnLMaps);
  }
  
  private HashMap<String, TreeMap<Long, Double>> processTradeBooks()
    throws Exception
  {
    ArrayList<File> tradeFiles = generateTradeFileList(this.sourcePath);
    
    HashMap<String, TreeMap<Long, Double>> assetDateTimeTradePnLMap = new HashMap();
    

    for (File tradeFile : tradeFiles)
    {
      if (tradeFile.length() != 0L)
      {

        String assetName = getAssetNameFromFile(tradeFile);
        
        ArrayList<TreeMap> tradeFileProcessingOutput = getDateTimeTradePnLAndStartEndDateMap(tradeFile);
        
        TreeMap<Long, Double> dateTimeTradePnLMap = (TreeMap)tradeFileProcessingOutput.get(0);
        assetDateTimeTradePnLMap.put(assetName, dateTimeTradePnLMap);
        
        TreeMap<Long, Long> tradeStartEndMap = (TreeMap)tradeFileProcessingOutput.get(1);
        this.assetStartDateEndDateMaps.put(assetName, tradeStartEndMap);
        
        TreeMap<Long, Integer> tradeStartDateTradeSideMap = (TreeMap)tradeFileProcessingOutput.get(2);
        this.assetStartDateTradeSideMaps.put(assetName, tradeStartDateTradeSideMap);
      }
    }
    
    return assetDateTimeTradePnLMap;
  }
  
  private HashMap<String, ArrayList<Long>> processMTMFiles()
    throws IOException
  {
    ArrayList<File> mtmFiles = generateMTMFileList(this.sourcePath);
    HashMap<String, ArrayList<Long>> assetMTMDateListMap = new HashMap();
    
    for (File mtmFile : mtmFiles)
    {
      String assetName = getAssetNameFromFile(mtmFile);
      ArrayList<Long> mtmDateList = new ArrayList();
      try
      {
        CSVReader reader = new CSVReader(mtmFile.getAbsolutePath(), ',', 0);
        TreeMap<Long, Double> mtmMap = new TreeMap();
        
        String[] line;
        while ((line = reader.getLine()) != null) { String[] line;
          mtmMap.put(Long.valueOf(Long.parseLong(line[0])), Double.valueOf(Double.parseDouble(line[1])));
          mtmDateList.add(Long.valueOf(Long.parseLong(line[0])));
        }
        this.dailyMTMMap.put(assetName, mtmMap);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      assetMTMDateListMap.put(assetName, mtmDateList);
    }
    
    return assetMTMDateListMap;
  }
  
  private String getAssetNameFromFile(File file) throws IOException
  {
    String strategyScripListName = file.getParentFile().getName();
    String fileName = file.getName();
    
    String[] fileNameParts = fileName.split(" ");
    String scripName = fileNameParts[0] + " " + fileNameParts[1] + " " + fileNameParts[2] + " " + fileNameParts[3] + 
      " " + fileNameParts[4];
    
    return strategyScripListName + " " + scripName;
  }
  
  private ArrayList<File> generateTradeFileList(String sourcePath)
  {
    ArrayList<File> tradeFiles = new ArrayList();
    
    File tradebookDirectory = new File(sourcePath + "/Trade Data");
    File[] scripListTradeBookDirectories = tradebookDirectory.listFiles();
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = scripListTradeBookDirectories).length; for (int i = 0; i < j; i++) { File scripListTradeBookDirectory = arrayOfFile1[i];
      tradeFiles.addAll(Arrays.asList(scripListTradeBookDirectory.listFiles()));
    }
    
    return tradeFiles;
  }
  
  private ArrayList<File> generateMTMFileList(String sourcePath)
  {
    ArrayList<File> mtmFiles = new ArrayList();
    
    File mtmDirectory = new File(sourcePath + "/MTM Data");
    File[] scripListMTMDirectories = mtmDirectory.listFiles();
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = scripListMTMDirectories).length; for (int i = 0; i < j; i++) { File scripListMTMDirectory = arrayOfFile1[i];
      mtmFiles.addAll(Arrays.asList(scripListMTMDirectory.listFiles()));
    }
    
    return mtmFiles;
  }
  

  private void insertNonTradeDates(TreeMap<Long, Double> singleStratScripTradePnLTimeStampMap, ArrayList<Long> mtmTimeStampList)
  {
    for (Long timeStamp : mtmTimeStampList) {
      if (singleStratScripTradePnLTimeStampMap.get(timeStamp) == null) {
        singleStratScripTradePnLTimeStampMap.put(timeStamp, Double.valueOf(0.0D));
      }
    }
  }
  

  private HashMap<String, TreeMap<Long, Double>> extractDailyTradePnLMapsSum(HashMap<String, TreeMap<Long, Double>> tradePnLDateTimeMaps, HashMap<String, ArrayList<Long>> assetMTMDateListMap)
  {
    HashMap<String, TreeMap<Long, Double>> assetDateTradePnLMap = new HashMap();
    
    for (Map.Entry<String, TreeMap<Long, Double>> entry : tradePnLDateTimeMaps.entrySet())
    {
      String assetName = (String)entry.getKey();
      TreeMap<Long, Double> singleStratScripTradePnLDateTimeMap = (TreeMap)entry.getValue();
      
      TreeMap<Long, Double> singleStratScripTradePnLDateMap = new TreeMap();
      
      for (Long dateTime : singleStratScripTradePnLDateTimeMap.keySet())
      {
        Long timeStamp = Long.valueOf(dateTime.longValue() / 1000000L);
        
        if (singleStratScripTradePnLDateMap.get(timeStamp) == null) {
          singleStratScripTradePnLDateMap.put(timeStamp, (Double)singleStratScripTradePnLDateTimeMap.get(dateTime));
        } else {
          double prevVal = ((Double)singleStratScripTradePnLDateMap.get(timeStamp)).doubleValue();
          double newVal = prevVal + ((Double)singleStratScripTradePnLDateTimeMap.get(dateTime)).doubleValue();
          
          singleStratScripTradePnLDateMap.put(timeStamp, Double.valueOf(newVal));
        }
      }
      

      insertNonTradeDates(singleStratScripTradePnLDateMap, (ArrayList)assetMTMDateListMap.get(assetName));
      
      assetDateTradePnLMap.put(assetName, singleStratScripTradePnLDateMap);
    }
    
    return assetDateTradePnLMap;
  }
  
  private ArrayList<TreeMap> getDateTimeTradePnLAndStartEndDateMap(File tradeFile)
    throws Exception
  {
    TradebookProcessor tradebookProcessor = new TradebookProcessor(tradeFile.getCanonicalPath().replace("\\", "/"));
    TreeMap<Long, Double> tradePnLMap = tradebookProcessor.getTradeStartDateTimeMTMMap();
    TreeMap<Long, Long> startDateEndDateMap = tradebookProcessor.getTradeStartEndDateMap();
    TreeMap<Long, Integer> startDateTradeSideMap = tradebookProcessor.getTradeStartDateTradeSideMap();
    
    ArrayList<TreeMap> output = new ArrayList();
    output.add(tradePnLMap);
    output.add(startDateEndDateMap);
    output.add(startDateTradeSideMap);
    
    return output;
  }
  


  private TreeMap<Long, HashMap<String, Double>> combineMTMmaps(HashMap<String, TreeMap<Long, Double>> mtmMaps)
  {
    TreeMap<Long, HashMap<String, Double>> consolMTM = new TreeMap();
    
    for (Map.Entry<String, TreeMap<Long, Double>> entry : mtmMaps.entrySet())
    {
      TreeMap<Long, Double> mtmMap = (TreeMap)entry.getValue();
      

      if (mtmMap.size() != 0)
      {



        for (Map.Entry<Long, Double> mtmEntry : mtmMap.entrySet()) {
          Long dateTime = (Long)mtmEntry.getKey();
          Double mtm = (Double)mtmEntry.getValue();
          try
          {
            HashMap<String, Double> curMTM = (HashMap)consolMTM.get(dateTime);
            
            if (curMTM == null) {
              curMTM = new HashMap();
            }
            
            curMTM.put((String)entry.getKey(), mtm);
            consolMTM.put(dateTime, curMTM);
          }
          catch (Exception e) {
            System.out.println("Error while making consol MTM Array");
            e.printStackTrace();
          }
        }
      }
    }
    return consolMTM;
  }
  
  public TreeMap<Long, HashMap<String, Double>> getTradeMTMMat() {
    return this.dateAssetTradePnLMap;
  }
  
  public HashMap<String, TreeMap<Long, Long>> getTradeStartEndMaps() {
    return this.assetStartDateEndDateMaps;
  }
  
  public HashMap<String, TreeMap<Long, Double>> getDailyMTMMap() {
    return this.dailyMTMMap;
  }
  
  public HashMap<String, TreeMap<Long, Integer>> getAssetStartDateTradeSideMaps() {
    return this.assetStartDateTradeSideMaps;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/TradeAndMTMDataProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */