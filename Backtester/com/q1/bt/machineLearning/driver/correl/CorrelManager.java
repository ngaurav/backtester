package com.q1.bt.machineLearning.driver.correl;

import com.q1.csv.CSVReader;
import com.q1.csv.CSVWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class CorrelManager
{
  static TreeSet<Long> dateSet = new TreeSet();
  static TreeSet<String> scripSet = new TreeSet();
  static TreeSet<String> strategySet = new TreeSet();
  static HashMap<String, Integer> lotMap = new HashMap();
  



  public static void main(String[] args)
    throws IOException
  {
    String backtestOrderbookPath = "C:/Q1/ML Correl Management/BT Orderbook Path";
    String dailyPredictiontPath = "C:/Q1/ML Correl Management/ML Output Folder";
    String dailyCorrelFilePath = "C:/Q1/ML Correl Management/Daily Correlation/DailyCorrelLog.csv";
    String outputOrderbookPath = "C:/Q1/ML Correl Management/ML Orderbook Path";
    

    initializeLotMap();
    


    System.out.println("Creating Position Map");
    TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap = generatePositionMap(backtestOrderbookPath);
    


    System.out.println("Generating Correlation Map");
    TreeMap<Long, HashMap<String, Double>> correlationMap = generateCorrelationMap(dailyCorrelFilePath);
    


    System.out.println("Generating Prediction Map");
    TreeMap<Long, HashMap<String, HashMap<String, Double>>> predictionMap = generatePredictionMap(
      dailyPredictiontPath);
    


    System.out.println("Generating Leverage Map");
    TreeMap<Long, HashMap<String, HashMap<String, Double>>> leverageMap = generateLeverageMap(correlationMap, 
      predictionMap, positionMap);
    

    System.out.println("Create ML Orderbooks");
    createMLOrderbooks(leverageMap, outputOrderbookPath, backtestOrderbookPath);
  }
  

  public static void initializeLotMap()
  {
    lotMap = new HashMap();
    lotMap.put("CBOE IDX VX FUT CC", Integer.valueOf(1000));
    lotMap.put("CBOT AGRI BO FUT CC", Integer.valueOf(600));
    lotMap.put("CBOT AGRI C FUT CC", Integer.valueOf(50));
    lotMap.put("CBOT BONDS FV FUT CC", Integer.valueOf(1000));
    lotMap.put("CBOT AGRI S FUT CC", Integer.valueOf(50));
    lotMap.put("CBOT AGRI SM FUT CC", Integer.valueOf(100));
    lotMap.put("CBOT BONDS TU FUT CC", Integer.valueOf(1));
    lotMap.put("CBOT BONDS TY FUT CC", Integer.valueOf(1000));
    lotMap.put("CBOT BONDS US FUT CC", Integer.valueOf(1000));
    lotMap.put("CBOT AGRI W FUT CC", Integer.valueOf(50));
    lotMap.put("CBOT IDX YM FUT CC", Integer.valueOf(5));
    lotMap.put("CME FOREX AUD FUT CC", Integer.valueOf(100000));
    lotMap.put("CME FOREX CAD FUT CC", Integer.valueOf(100000));
    lotMap.put("CME FOREX CHF FUT CC", Integer.valueOf(125000));
    lotMap.put("CME BONDS ED FUT CC", Integer.valueOf(1));
    lotMap.put("CME IDX EMD FUT CC", Integer.valueOf(100));
    lotMap.put("CME IDX ES FUT CC", Integer.valueOf(50));
    lotMap.put("CME FOREX EUR FUT CC", Integer.valueOf(125000));
    lotMap.put("CME SOFTS FC FUT CC", Integer.valueOf(500));
    lotMap.put("CME FOREX GBP FUT CC", Integer.valueOf(62500));
    lotMap.put("CME FOREX JPY FUT CC", Integer.valueOf(125000));
    lotMap.put("CME SOFTS LC FUT CC", Integer.valueOf(400));
    lotMap.put("CME SOFTS LH FUT CC", Integer.valueOf(400));
    lotMap.put("CME FOREX MXP FUT CC", Integer.valueOf(500000));
    lotMap.put("CME IDX NK FUT CC", Integer.valueOf(5));
    lotMap.put("CME IDX NQ FUT CC", Integer.valueOf(20));
    lotMap.put("CME FOREX NZD FUT CC", Integer.valueOf(100000));
    lotMap.put("COMEX METALS GC FUT CC", Integer.valueOf(100));
    lotMap.put("COMEX METALS HG FUT CC", Integer.valueOf(250));
    lotMap.put("COMEX METALS SI FUT CC", Integer.valueOf(50));
    lotMap.put("ICE ENERGY BRN FUT CC", Integer.valueOf(1000));
    lotMap.put("ICE ENERGY GAS FUT CC", Integer.valueOf(100));
    lotMap.put("ICECA AGRI RS FUT CC", Integer.valueOf(20));
    lotMap.put("ICEUS SOFTS CC FUT CC", Integer.valueOf(10));
    lotMap.put("ICEUS SOFTS CT FUT CC", Integer.valueOf(500));
    lotMap.put("ICEUS IDX DX FUT CC", Integer.valueOf(1000));
    lotMap.put("ICEUS SOFTS KC FUT CC", Integer.valueOf(375));
    lotMap.put("ICEUS SOFTS SB FUT CC", Integer.valueOf(1120));
    lotMap.put("NYMEX ENERGY CL FUT CC", Integer.valueOf(1000));
    lotMap.put("NYMEX ENERGY HO FUT CC", Integer.valueOf(42000));
    lotMap.put("NYMEX ENERGY NG FUT CC", Integer.valueOf(10000));
    lotMap.put("NYMEX METALS PA FUT CC", Integer.valueOf(100));
    lotMap.put("NYMEX METALS PL FUT CC", Integer.valueOf(50));
    lotMap.put("NYMEX ENERGY RBG FUT CC", Integer.valueOf(42000));
    lotMap.put("KBOT AGRI HRW FUT CC", Integer.valueOf(50));
  }
  

  public static TreeMap<Long, HashMap<String, Double>> generateCorrelationMap(String dailyCorrelFilePath)
    throws IOException
  {
    TreeMap<Long, HashMap<String, Double>> correlationMap = new TreeMap();
    
    CSVReader correlReader = new CSVReader(dailyCorrelFilePath, ',', 0);
    String[] scrip1List = correlReader.getLine();
    String[] scrip2List = correlReader.getLine();
    
    String[] correlLine;
    while ((correlLine = correlReader.getLine()) != null) {
      String[] correlLine;
      Long date = Long.valueOf(Long.parseLong(correlLine[0]));
      

      HashMap<String, Double> dayCorrelationMap = new HashMap();
      for (int i = 1; i < correlLine.length - 1; i++) {
        String scripValue = "";
        if (scrip1List[i].compareTo(scrip2List[i]) < 0) {
          scripValue = scrip1List[i] + "_" + scrip2List[i];
        } else
          scripValue = scrip2List[i] + "_" + scrip1List[i];
        Double correlValue = Double.valueOf(Double.parseDouble(correlLine[i]));
        dayCorrelationMap.put(scripValue, correlValue);
      }
      

      correlationMap.put(date, dayCorrelationMap);
    }
    
    return correlationMap;
  }
  

  public static TreeMap<Long, HashMap<String, HashMap<String, Double>>> generatePredictionMap(String dailyPredictiontPath)
    throws IOException
  {
    TreeMap<Long, HashMap<String, HashMap<String, Double>>> predictionMap = new TreeMap();
    
    File predictionPathFile = new File(dailyPredictiontPath);
    
    HashMap<String, Double> tempPredicitonMap = new HashMap();
    
    File[] arrayOfFile;
    int j = (arrayOfFile = predictionPathFile.listFiles()).length; CSVReader fileReader; String strategyID; for (int i = 0; i < j; i++) { File strategyPredictionFile = arrayOfFile[i];
      

      fileReader = new CSVReader(strategyPredictionFile.getAbsolutePath(), ',', 0);
      String[] dataLine;
      while ((dataLine = fileReader.getLine()) != null) {
        String[] dataLine;
        String[] keyValue = dataLine[1].split(" ");
        strategyID = keyValue[0];
        String scripID = keyValue[1] + " " + keyValue[2] + " " + keyValue[3] + " " + keyValue[4] + " " + 
          keyValue[5];
        

        Double predictionValue = Double.valueOf(Double.parseDouble(dataLine[2]));
        String key = dataLine[0] + "$" + strategyID + "$" + scripID;
        tempPredicitonMap.put(key, predictionValue);
      }
    }
    



    for (Long date : dateSet)
    {
      Object scripPredictionMap = new HashMap();
      
      for (String scripID : scripSet)
      {
        HashMap<String, Double> strategyPredictionMap = new HashMap();
        
        for (String strategyID : strategySet)
        {
          String key = date.toString() + "$" + strategyID + "$" + scripID;
          Double predictionValue = Double.valueOf(tempPredicitonMap.get(key) == null ? -100.0D : ((Double)tempPredicitonMap.get(key)).doubleValue());
          
          strategyPredictionMap.put(strategyID, predictionValue);
        }
        
        ((HashMap)scripPredictionMap).put(scripID, strategyPredictionMap);
      }
      
      predictionMap.put(date, scripPredictionMap);
    }
    

    return predictionMap;
  }
  

  public static TreeMap<String, HashMap<String, TreeMap<Long, Long>>> generatePositionMap(String backtestOrderbookPath)
    throws IOException
  {
    TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap = new TreeMap();
    
    File orderbookPathFile = new File(backtestOrderbookPath);
    
    HashMap<String, Long> tradeMap = new HashMap();
    
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = orderbookPathFile.listFiles()).length; String[] strategyOrderbookVal; for (int i = 0; i < j; i++) { File strategyOrderbookFile = arrayOfFile1[i];
      
      strategyOrderbookVal = strategyOrderbookFile.getName().split(" ");
      
      String strategyID = strategyOrderbookVal[0];
      File[] arrayOfFile2;
      int m = (arrayOfFile2 = strategyOrderbookFile.listFiles()).length; for (int k = 0; k < m; k++) { File scripOrderbookFile = arrayOfFile2[k];
        
        String scripID = scripOrderbookFile.getName().substring(0, scripOrderbookFile.getName().length() - 14);
        

        CSVReader fileReader = new CSVReader(scripOrderbookFile.getAbsolutePath(), ',', 0);
        String[] dataLine;
        while ((dataLine = fileReader.getLine()) != null) {
          String[] dataLine;
          Long dateTime = Long.valueOf(Long.parseLong(dataLine[0]));
          Long date = Long.valueOf(dateTime.longValue() / 1000000L);
          Long side = Long.valueOf(dataLine[3].equals("BUY") ? 1L : -1L);
          Long trade = Long.valueOf(side.longValue() * Long.parseLong(dataLine[6]));
          

          dateSet.add(date);
          strategySet.add(strategyID);
          scripSet.add(scripID);
          
          String key = date.toString() + "$" + strategyID + "$" + scripID;
          Long currentPosition = Long.valueOf(tradeMap.get(key) == null ? 0L : ((Long)tradeMap.get(key)).longValue());
          tradeMap.put(key, Long.valueOf(currentPosition.longValue() + trade.longValue()));
        }
      }
    }
    



    for (String strategyID : strategySet)
    {
      Object scripPositionMap = new HashMap();
      
      for (String scripID : scripSet)
      {
        Long position = Long.valueOf(0L);
        TreeMap<Long, Long> datePositionMap = new TreeMap();
        
        for (Long date : dateSet)
        {
          String key = date.toString() + "$" + strategyID + "$" + scripID;
          Long trade = Long.valueOf(tradeMap.get(key) == null ? 0L : ((Long)tradeMap.get(key)).longValue());
          position = Long.valueOf(position.longValue() + trade.longValue());
          
          datePositionMap.put(date, position);
        }
        
        ((HashMap)scripPositionMap).put(scripID, datePositionMap);
      }
      
      positionMap.put(strategyID, scripPositionMap);
    }
    

    return positionMap;
  }
  




  public static TreeMap<Long, HashMap<String, HashMap<String, Double>>> generateLeverageMap(TreeMap<Long, HashMap<String, Double>> correlationMap, TreeMap<Long, HashMap<String, HashMap<String, Double>>> predictionMap, TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap)
  {
    TreeMap<Long, HashMap<String, HashMap<String, Double>>> leverageMap = new TreeMap();
    

    HashMap<String, HashMap<String, Double>> dayPredictionMap = new HashMap();
    HashMap<String, HashMap<String, Long>> dayPositionMap = new HashMap();
    for (Map.Entry<Long, HashMap<String, HashMap<String, Double>>> predictionEntry : predictionMap.entrySet())
    {

      Long date = (Long)predictionEntry.getKey();
      

      HashMap<String, Double> dayCorrelationMap = (HashMap)correlationMap.get(date);
      

      HashMap<String, HashMap<String, Double>> dayLeverageMap = generateDayLeverageMap(dayCorrelationMap, 
        dayPositionMap, dayPredictionMap);
      

      positionMap = updatePositionMap(positionMap, date, dayLeverageMap);
      

      dayPositionMap = getDayPositionMap(positionMap, date);
      

      dayPredictionMap = (HashMap)predictionEntry.getValue();
      

      leverageMap.put(date, dayLeverageMap);
    }
    

    return leverageMap;
  }
  




  public static HashMap<String, HashMap<String, Double>> generateDayLeverageMap(HashMap<String, Double> correlationMap, HashMap<String, HashMap<String, Long>> positionMap, HashMap<String, HashMap<String, Double>> dayPredictionMap)
  {
    HashMap<String, HashMap<String, Double>> dayLeverageMap = new HashMap();
    
    return dayLeverageMap;
  }
  

  public static TreeMap<String, HashMap<String, TreeMap<Long, Long>>> updatePositionMap(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, Long date, HashMap<String, HashMap<String, Double>> dayLeverageMap)
  {
    Iterator localIterator2;
    for (Iterator localIterator1 = dayLeverageMap.keySet().iterator(); localIterator1.hasNext(); 
        
        localIterator2.hasNext())
    {
      String strategy = (String)localIterator1.next();
      HashMap<String, Double> dayStratLevMap = (HashMap)dayLeverageMap.get(strategy);
      localIterator2 = dayStratLevMap.keySet().iterator(); continue;String scrip = (String)localIterator2.next();
      Double leverage = (Double)dayStratLevMap.get(scrip);
      Long initPosition = (Long)((TreeMap)((HashMap)positionMap.get(strategy)).get(scrip)).get(date);
      Long finalPosition = Long.valueOf(com.q1.math.MathLib.roundTick(initPosition.longValue() * leverage.doubleValue(), ((Integer)lotMap.get(scrip)).intValue()));
      ((TreeMap)((HashMap)positionMap.get(strategy)).get(scrip)).put(date, finalPosition);
    }
    
    return positionMap;
  }
  



  public static HashMap<String, HashMap<String, Long>> getDayPositionMap(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, Long date)
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
      HashMap<String, TreeMap<Long, Long>> dayStratLevMap = (HashMap)positionMap.get(strategy);
      

      localIterator2 = dayStratLevMap.keySet().iterator(); continue;String scrip = (String)localIterator2.next();
      Long position = (Long)((TreeMap)dayStratLevMap.get(scrip)).get(date);
      scripPosition.put(scrip, position);
    }
    

    return dayPositionMap;
  }
  


  public static void createMLOrderbooks(TreeMap<Long, HashMap<String, HashMap<String, Double>>> leverageMap, String outputOrderbookPath, String backtestOrderbookPath)
  {
    TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap = getPositionMap(backtestOrderbookPath);
    
    for (Long date : leverageMap.keySet()) {
      HashMap<String, HashMap<String, Double>> dayLevMap = (HashMap)leverageMap.get(date);
      updatePositionMap(positionMap, date, dayLevMap);
    }
    writeOrderBook(positionMap, backtestOrderbookPath, outputOrderbookPath);
  }
  
  private static void writeOrderBook(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, String backtestOrderbookPath, String outputOrderbookPath)
  {
    Iterator localIterator2;
    for (Iterator localIterator1 = positionMap.keySet().iterator(); localIterator1.hasNext(); 
        
        localIterator2.hasNext())
    {
      String strategy = (String)localIterator1.next();
      HashMap<String, TreeMap<Long, Long>> scripMap = (HashMap)positionMap.get(strategy);
      localIterator2 = scripMap.keySet().iterator(); continue;String scrip = (String)localIterator2.next();
      String destFolder = outputOrderbookPath + File.separator + strategy + " " + scrip.replace(" ", "$");
      if (!new File(destFolder).exists())
        new File(destFolder).mkdirs();
      String destPath = destFolder + File.separator + scrip + " OrderBook.csv";
      String srcPath = backtestOrderbookPath + File.separator + strategy + " " + scrip.replace(" ", "$") + 
        File.separator + scrip + " OrderBook.csv";
      try
      {
        CSVWriter writer = new CSVWriter(destPath, false, ",");
        CSVReader reader = new CSVReader(srcPath, ',', 0);
        String[] line = null;
        while ((line = reader.getLine()) != null) {
          String position = Long.toString(((Long)((TreeMap)scripMap.get(scrip)).get(Long.valueOf(Long.parseLong(line[0]) / 1000000L))).longValue());
          writer.writeLine(
            new String[] { line[0], line[1], line[2], line[3], line[4], line[5], position });
          line = reader.getLine();
          try {
            writer.writeLine(
              new String[] { line[0], line[1], line[2], line[3], line[4], line[5], position });
          } catch (Exception e) {
            writer.flush();
            break;
          }
        }
        reader.close();
        writer.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  

  private static TreeMap<String, HashMap<String, TreeMap<Long, Long>>> getPositionMap(String backtestOrderbookPath)
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
  
  private static void updatePositionMap(TreeMap<String, HashMap<String, TreeMap<Long, Long>>> positionMap, String absolutePath, String strategy, String scrip)
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
    catch (IOException localIOException) {}
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/correl/CorrelManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */