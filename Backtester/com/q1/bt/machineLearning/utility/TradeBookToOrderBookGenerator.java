package com.q1.bt.machineLearning.utility;

import com.q1.bt.postprocess.TradebookProcessor;
import com.q1.csv.CSVWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TradeBookToOrderBookGenerator
{
  private String tradeBookPath;
  private String orderBookPath;
  private HashMap<String, TreeMap<Long, Boolean>> assetTimeStampDecisionMap;
  public boolean bias;
  
  public TradeBookToOrderBookGenerator(String sourcePath, String destPath, HashMap<String, TreeMap<Long, Boolean>> assetTimeStampDecisionMap, boolean bias)
  {
    this.tradeBookPath = (sourcePath + "/" + "Trade Data");
    this.orderBookPath = (destPath + "/" + "ML Order Data");
    this.assetTimeStampDecisionMap = assetTimeStampDecisionMap;
    this.bias = bias;
  }
  




  public void generateOrderBooks()
    throws IOException
  {
    File masterTadeBookFolder = new File(this.tradeBookPath);
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = masterTadeBookFolder.listFiles()).length; for (int i = 0; i < j; i++) { File strategyScripListTradeBookFolder = arrayOfFile1[i];
      
      String strategyScripListName = strategyScripListTradeBookFolder.getName();
      String orderBookFolder = this.orderBookPath + "/" + strategyScripListName;
      new File(orderBookFolder).mkdirs();
      

      File[] arrayOfFile2;
      

      int m = (arrayOfFile2 = strategyScripListTradeBookFolder.listFiles()).length; for (int k = 0; k < m; k++) { File strategyScripListScripTradeBook = arrayOfFile2[k];
        
        String scripName = strategyScripListScripTradeBook.getName();
        int scripNameLength = scripName.length() - 14;
        scripName = scripName.substring(0, scripNameLength);
        String assetName = strategyScripListName + " " + scripName;
        String orderBookPath = orderBookFolder + "/" + scripName + " OrderBook.csv";
        try
        {
          TradebookProcessor tradeBookProcessor = new TradebookProcessor(strategyScripListScripTradeBook.getPath().replace("\\", "/"));
          CSVWriter orderBookWriter = new CSVWriter(orderBookPath, false, ",");
          
          processTradeBook(tradeBookProcessor, orderBookWriter, assetName);
          
          orderBookWriter.close();
        }
        catch (java.text.ParseException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  private void processTradeBook(TradebookProcessor tradebookProcessor, CSVWriter orderBookWriter, String assetName)
    throws IOException
  {
    TreeMap<Long, Boolean> longTimeStampDecisionMap = null;TreeMap<Long, Boolean> shortTimeStampDecisionMap = null;TreeMap<Long, Boolean> timeStampDecisionMap = null;
    if (this.bias)
    {
      longTimeStampDecisionMap = (TreeMap)this.assetTimeStampDecisionMap.get("LON#" + assetName);
      shortTimeStampDecisionMap = (TreeMap)this.assetTimeStampDecisionMap.get("SHO#" + assetName);
    }
    else {
      timeStampDecisionMap = (TreeMap)this.assetTimeStampDecisionMap.get(assetName);
    }
    
    Boolean tradeStart = Boolean.valueOf(true);
    
    TreeMap<Long, ArrayList<Integer>> tradeDateIndexMap = tradebookProcessor.getTradeDateIndexMap();
    boolean buyBeforeSell;
    for (Iterator localIterator = tradeDateIndexMap.entrySet().iterator(); localIterator.hasNext(); 
        





























        buyBeforeSell.hasNext())
    {
      Map.Entry<Long, ArrayList<Integer>> tradeDateIndexMapEntry = (Map.Entry)localIterator.next();
      
      ArrayList<Integer> tradeBookIndex = (ArrayList)tradeDateIndexMapEntry.getValue();
      Long tradeTimeStamp = (Long)tradeDateIndexMapEntry.getKey();
      tradeTimeStamp = Long.valueOf(tradeTimeStamp.longValue() / 1000000L);
      ArrayList<String[]> listOfTrades = new ArrayList();
      
      int startIndex = ((Integer)tradeBookIndex.get(0)).intValue();
      int endIndex = ((Integer)tradeBookIndex.get(tradeBookIndex.size() - 1)).intValue();
      ArrayList<String[]> tempListOfTrades = tradebookProcessor.getTrades(startIndex, endIndex, true);
      


      if ((this.bias) && (tempListOfTrades.size() == 2))
      {
        boolean sellBeforeBuy = checkTradeOrderingMismatchCondition(longTimeStampDecisionMap, shortTimeStampDecisionMap, tradeTimeStamp, tempListOfTrades, "SELL");
        buyBeforeSell = checkTradeOrderingMismatchCondition(longTimeStampDecisionMap, shortTimeStampDecisionMap, tradeTimeStamp, tempListOfTrades, "BUY");
        
        if ((sellBeforeBuy) || (buyBeforeSell))
        {
          listOfTrades.add((String[])tempListOfTrades.get(1));
          listOfTrades.add((String[])tempListOfTrades.get(0));
        }
        else {
          listOfTrades = tempListOfTrades;
        }
      }
      else {
        listOfTrades = tempListOfTrades;
      }
      
      buyBeforeSell = listOfTrades.iterator(); continue;String[] tradeBookEntry = (String[])buyBeforeSell.next();
      
      String orderSide = tradeBookEntry[3];
      String orderType = tradeBookEntry[4];
      


      if (this.bias)
      {
        if (tradeStart.booleanValue())
        {
          if ((orderSide.equals("BUY")) && (longTimeStampDecisionMap.get(tradeTimeStamp) != null) && (((Boolean)longTimeStampDecisionMap.get(tradeTimeStamp)).booleanValue())) {
            writeOrder(tradeBookEntry, orderBookWriter);
          }
          else if ((orderSide.equals("SELL")) && (shortTimeStampDecisionMap.get(tradeTimeStamp) != null) && (((Boolean)shortTimeStampDecisionMap.get(tradeTimeStamp)).booleanValue())) {
            writeOrder(tradeBookEntry, orderBookWriter);
          }
          
        }
        else if ((longTimeStampDecisionMap.get(tradeTimeStamp) != null) && (((Boolean)longTimeStampDecisionMap.get(tradeTimeStamp)).booleanValue())) {
          writeOrder(tradeBookEntry, orderBookWriter);
        }
        else if ((shortTimeStampDecisionMap.get(tradeTimeStamp) != null) && (((Boolean)shortTimeStampDecisionMap.get(tradeTimeStamp)).booleanValue())) {
          writeOrder(tradeBookEntry, orderBookWriter);
        }
        

      }
      else if ((timeStampDecisionMap.get(tradeTimeStamp) != null) && (((Boolean)timeStampDecisionMap.get(tradeTimeStamp)).booleanValue())) {
        writeOrder(tradeBookEntry, orderBookWriter);
      }
      
      if (!orderType.equals("ROLLOVER")) {
        tradeStart = Boolean.valueOf(!tradeStart.booleanValue());
      }
    }
  }
  


  private boolean checkTradeOrderingMismatchCondition(TreeMap<Long, Boolean> longTimeStampDecisionMap, TreeMap<Long, Boolean> shortTimeStampDecisionMap, Long tradeTimeStamp, ArrayList<String[]> tempListOfTrades, String tradeSide)
  {
    boolean scripExistence = longTimeStampDecisionMap.get(tradeTimeStamp) != null;
    
    if (!scripExistence) {
      return false;
    }
    boolean longPrediction = ((Boolean)longTimeStampDecisionMap.get(tradeTimeStamp)).booleanValue();
    boolean shortPrediction = ((Boolean)shortTimeStampDecisionMap.get(tradeTimeStamp)).booleanValue();
    boolean tradeSideFlag = ((String[])tempListOfTrades.get(0))[3].equals(tradeSide);
    
    if (tradeSide.equals("SELL")) {
      shortPrediction = !shortPrediction;
    } else {
      longPrediction = !longPrediction;
    }
    boolean returnVal = (scripExistence) && (longPrediction) && (shortPrediction) && (tradeSideFlag);
    
    return returnVal;
  }
  
  private void writeOrder(String[] tradeBookEntry, CSVWriter orderBookWriter)
  {
    try
    {
      String orderSide = tradeBookEntry[3];
      String orderType = tradeBookEntry[4];
      String orderTime = tradeBookEntry[10];
      String scripList = tradeBookEntry[9];
      String scripName = tradeBookEntry[2];
      
      String price = tradeBookEntry[5];
      String qty = tradeBookEntry[7];
      
      if (orderType.equals("ROLLOVER")) {
        return;
      }
      String orderEntry = orderTime + "," + scripList + "," + scripName + "," + orderSide + "," + orderType + "," + price + "," + qty;
      orderBookWriter.writeLine(orderEntry);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/TradeBookToOrderBookGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */