package com.q1.bt.machineLearning.driver.correl;

import com.q1.csv.CSVReader;
import com.q1.csv.CSVWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class CorrelatedAssets
{
  private HashMap<String, Integer> lotMap;
  
  public CorrelatedAssets()
  {
    this.lotMap = new HashMap();
    this.lotMap.put("CBOE IDX VX FUT CC", Integer.valueOf(1000));
    this.lotMap.put("CBOT AGRI BO FUT CC", Integer.valueOf(600));
    this.lotMap.put("CBOT AGRI C FUT CC", Integer.valueOf(50));
    this.lotMap.put("CBOT BONDS FV FUT CC", Integer.valueOf(1000));
    this.lotMap.put("CBOT AGRI S FUT CC", Integer.valueOf(50));
    this.lotMap.put("CBOT AGRI SM FUT CC", Integer.valueOf(100));
    this.lotMap.put("CBOT BONDS TU FUT CC", Integer.valueOf(1));
    this.lotMap.put("CBOT BONDS TY FUT CC", Integer.valueOf(1000));
    this.lotMap.put("CBOT BONDS US FUT CC", Integer.valueOf(1000));
    this.lotMap.put("CBOT AGRI W FUT CC", Integer.valueOf(50));
    this.lotMap.put("CBOT IDX YM FUT CC", Integer.valueOf(5));
    this.lotMap.put("CME FOREX AUD FUT CC", Integer.valueOf(100000));
    this.lotMap.put("CME FOREX CAD FUT CC", Integer.valueOf(100000));
    this.lotMap.put("CME FOREX CHF FUT CC", Integer.valueOf(125000));
    this.lotMap.put("CME BONDS ED FUT CC", Integer.valueOf(1));
    this.lotMap.put("CME IDX EMD FUT CC", Integer.valueOf(100));
    this.lotMap.put("CME IDX ES FUT CC", Integer.valueOf(50));
    this.lotMap.put("CME FOREX EUR FUT CC", Integer.valueOf(125000));
    this.lotMap.put("CME SOFTS FC FUT CC", Integer.valueOf(500));
    this.lotMap.put("CME FOREX GBP FUT CC", Integer.valueOf(62500));
    this.lotMap.put("CME FOREX JPY FUT CC", Integer.valueOf(125000));
    this.lotMap.put("CME SOFTS LC FUT CC", Integer.valueOf(400));
    this.lotMap.put("CME SOFTS LH FUT CC", Integer.valueOf(400));
    this.lotMap.put("CME FOREX MXP FUT CC", Integer.valueOf(500000));
    this.lotMap.put("CME IDX NK FUT CC", Integer.valueOf(5));
    this.lotMap.put("CME IDX NQ FUT CC", Integer.valueOf(20));
    this.lotMap.put("CME FOREX NZD FUT CC", Integer.valueOf(100000));
    this.lotMap.put("COMEX METALS GC FUT CC", Integer.valueOf(100));
    this.lotMap.put("COMEX METALS HG FUT CC", Integer.valueOf(250));
    this.lotMap.put("COMEX METALS SI FUT CC", Integer.valueOf(50));
    this.lotMap.put("ICE ENERGY BRN FUT CC", Integer.valueOf(1000));
    this.lotMap.put("ICE ENERGY GAS FUT CC", Integer.valueOf(100));
    this.lotMap.put("ICECA AGRI RS FUT CC", Integer.valueOf(20));
    this.lotMap.put("ICEUS SOFTS CC FUT CC", Integer.valueOf(10));
    this.lotMap.put("ICEUS SOFTS CT FUT CC", Integer.valueOf(500));
    this.lotMap.put("ICEUS IDX DX FUT CC", Integer.valueOf(1000));
    this.lotMap.put("ICEUS SOFTS KC FUT CC", Integer.valueOf(375));
    this.lotMap.put("ICEUS SOFTS SB FUT CC", Integer.valueOf(1120));
    this.lotMap.put("NYMEX ENERGY CL FUT CC", Integer.valueOf(1000));
    this.lotMap.put("NYMEX ENERGY HO FUT CC", Integer.valueOf(42000));
    this.lotMap.put("NYMEX ENERGY NG FUT CC", Integer.valueOf(10000));
    this.lotMap.put("NYMEX METALS PA FUT CC", Integer.valueOf(100));
    this.lotMap.put("NYMEX METALS PL FUT CC", Integer.valueOf(50));
    this.lotMap.put("NYMEX ENERGY RBG FUT CC", Integer.valueOf(42000));
    this.lotMap.put("KBOT AGRI HRW FUT CC", Integer.valueOf(50));
  }
  

  TreeMap<String, HashMap<String, TreeMap<Long, Long>>> updatePositionMap(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, Long date, HashMap<String, HashMap<String, Double>> dayLeverageMap)
  {
    Iterator localIterator2;
    for (Iterator localIterator1 = dayLeverageMap.keySet().iterator(); localIterator1.hasNext(); 
        

        localIterator2.hasNext())
    {
      String strategy = (String)localIterator1.next();
      HashMap<String, Double> dayStratLevMap = 
        (HashMap)dayLeverageMap.get(strategy);
      localIterator2 = dayStratLevMap.keySet().iterator(); continue;String scrip = (String)localIterator2.next();
      Double leverage = (Double)dayStratLevMap.get(scrip);
      Long initPosition = 
        (Long)((TreeMap)((HashMap)positionMap.get(strategy)).get(scrip)).get(date);
      Long finalPosition = Long.valueOf(com.q1.math.MathLib.roundTick(initPosition.longValue() * 
        leverage.doubleValue(), ((Integer)this.lotMap.get(scrip)).intValue()));
      ((TreeMap)((HashMap)positionMap.get(strategy)).get(scrip)).put(date, finalPosition);
    }
    
    return positionMap;
  }
  



  HashMap<String, HashMap<String, Long>> getDayPositionMap(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, Long date)
  {
    HashMap<String, HashMap<String, Long>> dayPositionMap = new HashMap();
    Iterator localIterator2;
    for (Iterator localIterator1 = positionMap.keySet().iterator(); localIterator1.hasNext(); 
        







        localIterator2.hasNext())
    {
      String strategy = (String)localIterator1.next();
      
      if (dayPositionMap.get(strategy) == null) {
        dayPositionMap.put(strategy, new HashMap());
      }
      HashMap<String, Long> scripPosition = (HashMap)dayPositionMap.get(strategy);
      HashMap<String, TreeMap<Long, Long>> dayStratLevMap = 
        (HashMap)positionMap.get(strategy);
      
      localIterator2 = dayStratLevMap.keySet().iterator(); continue;String scrip = (String)localIterator2.next();
      Long position = (Long)((TreeMap)dayStratLevMap.get(scrip)).get(date);
      scripPosition.put(scrip, position);
    }
    

    return dayPositionMap;
  }
  




  HashMap<String, HashMap<String, Double>> generateDayLeverageMap(HashMap<String, Double> dayCorrelationMap, HashMap<String, HashMap<String, Long>> dayPositionMap, HashMap<String, HashMap<String, Double>> dayPredictionMap)
  {
    Integer maxAssets = Integer.valueOf(2);
    HashMap<String, HashMap<String, Double>> levMap = new HashMap();
    HashMap<String, Integer> corGroup = getCorGroup(dayCorrelationMap);
    HashMap<Integer, ArrayList<String>> corGroupList = getCorGroupCount(
      dayPositionMap, corGroup);
    for (Integer grpNo : corGroupList.keySet()) {
      if (((ArrayList)corGroupList.get(grpNo)).size() > maxAssets.intValue()) {
        TreeMap<Double, String> predictionMap = new TreeMap();
        String[] stratScripPart; for (String stratScrip : (ArrayList)corGroupList.get(grpNo)) {
          stratScripPart = stratScrip.split("_");
          predictionMap.put(
            (Double)((HashMap)dayPredictionMap.get(stratScripPart[0])).get(stratScripPart[1]), stratScrip);
        }
        int i = 0;
        for (Object entry : predictionMap.entrySet()) {
          if (i >= maxAssets.intValue()) {
            addZeroLev(levMap, (String)((java.util.Map.Entry)entry).getValue());
          }
          i++;
        }
      }
    }
    return levMap;
  }
  

  private void addZeroLev(HashMap<String, HashMap<String, Double>> levMap, String stratScrip)
  {
    String[] stratScripPart = stratScrip.split("_");
    if (levMap.get(stratScripPart[0]) == null) {
      levMap.put(stratScripPart[0], new HashMap());
    }
    ((HashMap)levMap.get(stratScripPart[0])).put(stratScripPart[1], Double.valueOf(0.0D));
  }
  


  private HashMap<Integer, ArrayList<String>> getCorGroupCount(HashMap<String, HashMap<String, Long>> dayPositionMap, HashMap<String, Integer> corGroup)
  {
    HashMap<Integer, ArrayList<String>> corGroupList = new HashMap();
    Iterator localIterator2; for (Iterator localIterator1 = dayPositionMap.keySet().iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      String Strategy = (String)localIterator1.next();
      localIterator2 = ((HashMap)dayPositionMap.get(Strategy)).keySet().iterator(); continue;String scrip = (String)localIterator2.next();
      Integer group = (Integer)corGroup.get(scrip);
      if (corGroupList.get(group) == null) {
        corGroupList.put(group, new ArrayList());
      }
      ((ArrayList)corGroupList.get(group)).add(Strategy + "_" + scrip);
    }
    
    return corGroupList;
  }
  
  private HashMap<String, Integer> getCorGroup(HashMap<String, Double> dayCorrelationMap)
  {
    int corGrpCnt = 0;
    HashMap<String, Integer> corGroup = new HashMap();
    for (String assetPair : dayCorrelationMap.keySet()) {
      if (((Double)dayCorrelationMap.get(assetPair)).doubleValue() > 0.7D) {
        String[] assetPairVals = assetPair.split("_");
        
        Integer grp0 = (Integer)corGroup.get(assetPairVals[0]);
        Integer grp1 = (Integer)corGroup.get(assetPairVals[1]);
        if ((grp0 == null) && (grp1 == null))
        {
          corGroup.put(assetPairVals[0], Integer.valueOf(corGrpCnt));
          corGroup.put(assetPairVals[1], Integer.valueOf(corGrpCnt));
          corGrpCnt++;
        } else if ((grp0 == null) && (grp1 != null)) {
          corGroup.put(assetPairVals[0], grp1);
        } else if ((grp0 != null) && (grp1 == null)) {
          corGroup.put(assetPairVals[1], grp0);
        }
        else {
          for (String asset : corGroup.keySet()) {
            if (corGroup.get(asset) == grp1) {
              corGroup.put(asset, grp0);
            }
          }
        }
      }
    }
    
    return corGroup;
  }
  



  public void createMLOrderbooks(TreeMap<Long, HashMap<String, HashMap<String, Double>>> leverageMap, String outputOrderbookPath, String backtestOrderbookPath)
  {
    TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap = getPositionMap(backtestOrderbookPath);
    
    for (Long date : leverageMap.keySet()) {
      HashMap<String, HashMap<String, Double>> dayLevMap = (HashMap)leverageMap.get(date);
      updatePositionMap(positionMap, 
        date, dayLevMap);
    }
    writeOrderBook(positionMap, outputOrderbookPath);
  }
  

  private void writeOrderBook(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, String outputOrderbookPath)
  {
    Iterator localIterator2;
    for (Iterator localIterator1 = positionMap.keySet().iterator(); localIterator1.hasNext(); 
        
        localIterator2.hasNext())
    {
      String strategy = (String)localIterator1.next();
      HashMap<String, TreeMap<Long, Long>> scripMap = (HashMap)positionMap.get(strategy);
      localIterator2 = scripMap.keySet().iterator(); continue;String scrip = (String)localIterator2.next();
      String destPath = outputOrderbookPath + File.separator + strategy + File.separator + 
        scrip + " OrderBook.csv";
      String srcPath = outputOrderbookPath + File.separator + strategy + File.separator + 
        scrip + " OrderBook.csv";
      try
      {
        CSVWriter writer = new CSVWriter(destPath, false, ",");
        CSVReader reader = new CSVReader(srcPath, ',', 0);
        String[] line = null;
        while ((line = reader.getLine()) != null) {
          String position = Long.toString(((Long)((TreeMap)scripMap.get(scrip)).get(Long.valueOf(Long.parseLong(line[0]) / 1000000L))).longValue());
          writer.writeLine(new String[] { line[0], line[1], line[2], line[3], 
            line[4], line[5], 
            position });
          line = reader.getLine();
          writer.writeLine(new String[] { line[0], line[1], line[2], line[3], 
            line[4], line[5], 
            position });
        }
        reader.close();
        writer.close();
      }
      catch (java.io.IOException e) {
        e.printStackTrace();
      }
    }
  }
  



  private TreeMap<String, HashMap<String, TreeMap<Long, Long>>> getPositionMap(String backtestOrderbookPath)
  {
    TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap = new TreeMap();
    File sourceFolder = new File(backtestOrderbookPath);
    File[] listOfFiles = sourceFolder.listFiles();
    File[] arrayOfFile1; int j = (arrayOfFile1 = listOfFiles).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
      String strategy = folder.getName().split(" ")[0];
      File[] orderBooks = folder.listFiles();
      File[] arrayOfFile2; int m = (arrayOfFile2 = orderBooks).length; for (int k = 0; k < m; k++) { File orderBook = arrayOfFile2[k];
        String orderBookName = orderBook.getName();
        String scrip = orderBookName.substring(0, orderBookName.length() - 14);
        updatePositionMap(positionMap, orderBook.getAbsolutePath(), strategy, scrip);
      }
    }
    return positionMap;
  }
  

  private void updatePositionMap(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, String absolutePath, String strategy, String scrip)
  {
    try
    {
      CSVReader reader = new CSVReader(absolutePath, ',', 0);
      String[] line = null;
      while ((line = reader.getLine()) != null) {
        Long dateTime = Long.valueOf(Long.parseLong(line[0]));
        Long date = Long.valueOf(dateTime.longValue() / 1000000L);
        Long position = Long.valueOf(Long.parseLong(line[6]));
        HashMap<String, TreeMap<Long, Long>> scripMap = (HashMap)positionMap.get(strategy);
        if (scripMap == null) {
          positionMap.put(strategy, new HashMap());
          scripMap = (HashMap)positionMap.get(strategy);
        }
        TreeMap<Long, Long> dateMap = (TreeMap)scripMap.get(scrip);
        if (dateMap == null) {
          scripMap.put(scrip, new TreeMap());
          dateMap = (TreeMap)scripMap.get(scrip);
        }
        dateMap.put(date, position);
        reader.getLine();
      }
      reader.close();
    }
    catch (java.io.IOException localIOException) {}
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/correl/CorrelatedAssets.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */