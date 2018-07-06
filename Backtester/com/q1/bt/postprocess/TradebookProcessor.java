package com.q1.bt.postprocess;

import com.q1.csv.CSVReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;




public class TradebookProcessor
{
  ArrayList<Double> tradePLs = new ArrayList();
  
  HashMap<String, Integer> tbIdxMap = new HashMap();
  TreeMap<Long, ArrayList<Integer>> tradeDateIndexMap = new TreeMap();
  ArrayList<String> startdTs = new ArrayList();
  ArrayList<Integer> tradeSides = new ArrayList();
  ArrayList<String> enddTs = new ArrayList();
  ArrayList<Integer[]> tradeStartEndIndex = new ArrayList();
  
  Long position;
  
  Double tradePnL;
  
  String assetName;
  String filename;
  public ArrayList<String[]> tradeBook = new ArrayList();
  
  public TradebookProcessor(String path) throws IOException, ParseException {
    this.tradeBook = getTradebook(path);
    
    this.tradePLs = new ArrayList();
    this.tbIdxMap = getTradeBookIndexMap();
    
    String[] tempsplit = path.split("/");
    String strategyScripList = tempsplit[(tempsplit.length - 2)];
    String scrip = tempsplit[(tempsplit.length - 1)];
    scrip = scrip.substring(0, scrip.length() - 14);
    this.assetName = (strategyScripList + " " + scrip);
    
    this.position = Long.valueOf(0L);
    
    runSingleTradePP();
  }
  
  private void runSingleTradePP()
    throws ParseException
  {
    boolean inPos = false;
    this.tradePnL = Double.valueOf(0.0D);
    
    Integer tradeStartIndex = Integer.valueOf(0);Integer tradeEndIndex = Integer.valueOf(0);
    
    int i = 0;
    while (i < this.tradeBook.size())
    {
      String[] trade = (String[])this.tradeBook.get(i);
      String tradeDate = trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()];
      Long longTradeDate = Long.valueOf(Long.parseLong(tradeDate));
      
      ArrayList<Integer> tradebookIndex;
      
      if ((tradebookIndex = (ArrayList)this.tradeDateIndexMap.get(longTradeDate)) == null) {
        tradebookIndex = new ArrayList();
      }
      tradebookIndex.add(Integer.valueOf(i));
      this.tradeDateIndexMap.put(longTradeDate, tradebookIndex);
      
      if (!inPos)
      {
        tradeStartIndex = Integer.valueOf(i);
        this.startdTs.add(tradeDate);
        this.tradeSides.add(Integer.valueOf(trade[((Integer)this.tbIdxMap.get("orderSide")).intValue()].equals("BUY") ? 1 : -1));
        
        trade = (String[])this.tradeBook.get(i);
        processTrade(trade);
        inPos = true;
      }
      else {
        boolean exit = processTrade(trade);
        

        if (exit)
        {

          tradeEndIndex = Integer.valueOf(i);
          Integer[] tradeIndex = { tradeStartIndex, tradeEndIndex };
          this.tradeStartEndIndex.add(tradeIndex);
          this.enddTs.add(tradeDate);
          this.tradePLs.add(this.tradePnL);
          this.tradePnL = Double.valueOf(0.0D);
          
          inPos = false;
        }
      }
      i++;
    }
    

    if (this.startdTs.size() > this.enddTs.size()) {
      tradeEndIndex = null;
      Integer[] tradeIndex = { tradeStartIndex, Integer.valueOf(this.tradeBook.size() - 1) };
      this.tradeStartEndIndex.add(tradeIndex);
      this.enddTs.add("99999999");
      this.tradePLs.add(Double.valueOf(0.0D));
    }
  }
  
  private boolean processTrade(String[] trade)
    throws ParseException
  {
    Long orderQty = Long.valueOf(Long.parseLong(trade[((Integer)this.tbIdxMap.get("qty")).intValue()]));
    Double execPrice = Double.valueOf(Double.parseDouble(trade[((Integer)this.tbIdxMap.get("execPrice")).intValue()]));
    Double capital = Double.valueOf(Double.parseDouble(trade[((Integer)this.tbIdxMap.get("capital")).intValue()]));
    String orderSide = trade[((Integer)this.tbIdxMap.get("orderSide")).intValue()];
    String orderType = trade[((Integer)this.tbIdxMap.get("orderType")).intValue()];
    
    int tradeSignal = 0;
    
    if (orderSide.equalsIgnoreCase("Buy")) {
      tradeSignal = 1;
    } else if (orderSide.equalsIgnoreCase("Sell")) {
      tradeSignal = -1;
    }
    this.tradePnL = Double.valueOf(this.tradePnL.doubleValue() + execPrice.doubleValue() * orderQty.longValue() * -tradeSignal / capital.doubleValue());
    

    this.position = Long.valueOf(this.position.longValue() + tradeSignal * orderQty.longValue());
    boolean rollover = (orderType.equals("ROLLOVER")) || (orderType.equals("ROL"));
    if ((this.position.longValue() == 0L) && (!rollover)) {
      return true;
    }
    return false;
  }
  
  private HashMap<String, Integer> getTradeBookIndexMap() {
    HashMap<String, Integer> tbIdxMap = new HashMap();
    
    tbIdxMap.put("dateTime", Integer.valueOf(0));
    tbIdxMap.put("capital", Integer.valueOf(1));
    tbIdxMap.put("expiry", Integer.valueOf(2));
    tbIdxMap.put("orderSide", Integer.valueOf(3));
    tbIdxMap.put("orderType", Integer.valueOf(4));
    tbIdxMap.put("triggerPrice", Integer.valueOf(5));
    tbIdxMap.put("execPrice", Integer.valueOf(6));
    tbIdxMap.put("qty", Integer.valueOf(7));
    tbIdxMap.put("execType", Integer.valueOf(8));
    tbIdxMap.put("ScripID", Integer.valueOf(9));
    
    return tbIdxMap;
  }
  
  private ArrayList<String[]> getTradebook(String path) throws IOException
  {
    ArrayList<String[]> tradeBook = new ArrayList();
    
    CSVReader reader = new CSVReader(path, ',', 0);
    String[] line;
    while ((line = reader.getLine()) != null) { String[] line;
      tradeBook.add(line);
    }
    reader.close();
    
    return tradeBook;
  }
  
  public ArrayList<String[]> getTradeTimestamps() throws Exception
  {
    ArrayList<String[]> timestamps = new ArrayList();
    if (this.startdTs.size() == 0) {
      throw new Exception("empty tradebook");
    }
    
    for (int i = 0; i < this.startdTs.size(); i++) {
      String[] timestamp = new String[2];
      timestamp[0] = ((String)this.startdTs.get(i));
      timestamp[1] = ((String)this.enddTs.get(i));
      timestamps.add(timestamp);
    }
    
    return timestamps;
  }
  
  public TreeMap<Long, Long> getTradeStartEndDateMap() throws Exception {
    TreeMap<Long, Long> startEndMap = new TreeMap();
    


    if (this.startdTs.size() == 0) {
      throw new Exception("empty tradebook");
    }
    
    for (int i = 0; i < this.startdTs.size(); i++)
    {
      Long startDate = Long.valueOf(Long.parseLong(((String)this.startdTs.get(i)).substring(0, 8)));
      Long endDate = Long.valueOf(Long.parseLong(((String)this.enddTs.get(i)).substring(0, 8)));
      
      Long existingEndDate = (Long)startEndMap.get(startDate);
      
      if ((existingEndDate == null) || (endDate.compareTo(existingEndDate) > 0)) {
        startEndMap.put(startDate, endDate);
      }
    }
    return startEndMap;
  }
  
  public TreeMap<Long, Integer[]> getTradeStartDateStartEndIndexMap() throws Exception {
    TreeMap<Long, Integer[]> startDateStartEndIndexMap = new TreeMap();
    

    if (this.startdTs.size() == 0) {
      throw new Exception("empty tradebook");
    }
    
    for (int i = 0; i < this.startdTs.size(); i++)
    {
      Long startDate = Long.valueOf(Long.parseLong(((String)this.startdTs.get(i)).substring(0, 8)));
      Integer[] startEndIndex = (Integer[])this.tradeStartEndIndex.get(i);
      
      Integer[] existingStartEndIndex = (Integer[])startDateStartEndIndexMap.get(startDate);
      
      if (existingStartEndIndex == null) {
        startDateStartEndIndexMap.put(startDate, startEndIndex);
      }
      else {
        Integer startIndex = Integer.valueOf(Math.min(startEndIndex[0].intValue(), existingStartEndIndex[0].intValue()));
        Integer endIndex = Integer.valueOf(Math.min(startEndIndex[1].intValue(), existingStartEndIndex[1].intValue()));
        Integer[] newStartEndIndex = { startIndex, endIndex };
        startDateStartEndIndexMap.put(startDate, newStartEndIndex);
      }
    }
    
    return startDateStartEndIndexMap;
  }
  
  public ArrayList<String[]> getTrades(int startIndex, int endIndex, boolean ignoreROTrades) {
    ArrayList<String[]> listOfTrades = new ArrayList();
    
    for (int i = startIndex; i <= endIndex; i++) {
      String[] tradeLine = (String[])this.tradeBook.get(i);
      String tradeType = tradeLine[((Integer)this.tbIdxMap.get("orderType")).intValue()];
      
      if ((!ignoreROTrades) || (!tradeType.equals("ROLLOVER"))) {
        listOfTrades.add((String[])this.tradeBook.get(i));
      }
    }
    return listOfTrades;
  }
  
  public ArrayList<String[]> getTrades(int startIndex, int endIndex) {
    return getTrades(startIndex, endIndex, false);
  }
  
  public ArrayList<String[]> getTradePnls() throws Exception {
    ArrayList<String[]> tradepnls = new ArrayList();
    if (this.startdTs.size() == 0) {
      throw new Exception("empty tradebook");
    }
    
    for (int i = 0; i < this.startdTs.size(); i++) {
      String[] tradepnl = new String[4];
      tradepnl[0] = ((String)this.startdTs.get(i));
      tradepnl[1] = ((String)this.enddTs.get(i));
      tradepnl[2] = ((Double)this.tradePLs.get(i)).toString();
      tradepnl[3] = this.assetName;
      tradepnls.add(tradepnl);
    }
    
    return tradepnls;
  }
  
  public TreeMap<Long, Double> getTradeStartDateTimeMTMMap() throws Exception
  {
    TreeMap<Long, Double> tradeStartDateTimeMTMMap = new TreeMap();
    
    if (this.startdTs.size() == 0) {
      throw new Exception("empty tradebook");
    }
    
    for (int i = 0; i < this.startdTs.size(); i++) {
      double tradePnL = ((Double)this.tradePLs.get(i)).doubleValue();
      
      Long startDateTime = Long.valueOf(Long.parseLong((String)this.startdTs.get(i)));
      tradeStartDateTimeMTMMap.put(startDateTime, Double.valueOf(tradePnL));
    }
    
    return tradeStartDateTimeMTMMap;
  }
  
  public TreeMap<Long, Integer> getTradeStartDateTradeSideMap() throws Exception {
    TreeMap<Long, Integer> tradeStartDateTradeSideMap = new TreeMap();
    
    if (this.startdTs.size() == 0) {
      throw new Exception("empty tradebook");
    }
    
    for (int i = 0; i < this.startdTs.size(); i++) {
      Integer tradeSide = (Integer)this.tradeSides.get(i);
      
      Long startDate = Long.valueOf(Long.parseLong((String)this.startdTs.get(i)) / 1000000L);
      if (((String)this.startdTs.get(i)).equals(this.enddTs.get(i))) {
        tradeStartDateTradeSideMap.put(startDate, Integer.valueOf(0));
      } else {
        tradeStartDateTradeSideMap.put(startDate, tradeSide);
      }
    }
    return tradeStartDateTradeSideMap;
  }
  
  public TreeMap<Long, ArrayList<Integer>> getTradeDateIndexMap() {
    return this.tradeDateIndexMap;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/TradebookProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */