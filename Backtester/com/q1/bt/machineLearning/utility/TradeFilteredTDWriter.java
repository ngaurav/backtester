package com.q1.bt.machineLearning.utility;

import com.q1.bt.postprocess.TradebookProcessor;
import com.q1.csv.CSVWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.TreeMap;

public class TradeFilteredTDWriter
{
  CSVWriter writerTD;
  Long currentTS;
  String[] inData = null;
  boolean inTrade = false;
  TreeMap<Long, Integer[]> tradeStartDateStartEndIndexMap;
  TradebookProcessor tradebookProcessor;
  
  public TradeFilteredTDWriter(String source, String destination)
    throws Exception
  {
    try
    {
      this.writerTD = new CSVWriter(destination, false, ",");
      
      this.tradebookProcessor = new TradebookProcessor(source);
      this.tradeStartDateStartEndIndexMap = this.tradebookProcessor.getTradeStartDateStartEndIndexMap();
    }
    catch (IOException e) {
      System.out.println(source + " not found");
      throw new IOException();
    }
    this.currentTS = Long.valueOf(0L);
  }
  

  public void process(Long startTS, int write, String assetName)
    throws IOException
  {
    if (write == 1)
    {
      Integer[] startEndIndex = (Integer[])this.tradeStartDateStartEndIndexMap.get(startTS);
      
      if (startEndIndex == null) {
        return;
      }
      ArrayList<String[]> listOfTrades = this.tradebookProcessor.getTrades(startEndIndex[0].intValue(), startEndIndex[1].intValue());
      
      for (String[] trade : listOfTrades)
      {
        this.writerTD.writeLine(trade);
      }
    }
  }
  
  public void close()
    throws IOException
  {
    this.writerTD.close();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/TradeFilteredTDWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */