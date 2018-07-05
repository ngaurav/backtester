/*    */ package com.q1.bt.machineLearning.utility;
/*    */ 
/*    */ import com.q1.bt.postprocess.TradebookProcessor;
/*    */ import com.q1.csv.CSVWriter;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.TreeMap;
/*    */ 
/*    */ public class TradeFilteredTDWriter
/*    */ {
/*    */   CSVWriter writerTD;
/*    */   Long currentTS;
/* 14 */   String[] inData = null;
/* 15 */   boolean inTrade = false;
/*    */   TreeMap<Long, Integer[]> tradeStartDateStartEndIndexMap;
/*    */   TradebookProcessor tradebookProcessor;
/*    */   
/*    */   public TradeFilteredTDWriter(String source, String destination)
/*    */     throws Exception
/*    */   {
/*    */     try
/*    */     {
/* 24 */       this.writerTD = new CSVWriter(destination, false, ",");
/*    */       
/* 26 */       this.tradebookProcessor = new TradebookProcessor(source);
/* 27 */       this.tradeStartDateStartEndIndexMap = this.tradebookProcessor.getTradeStartDateStartEndIndexMap();
/*    */     }
/*    */     catch (IOException e) {
/* 30 */       System.out.println(source + " not found");
/* 31 */       throw new IOException();
/*    */     }
/* 33 */     this.currentTS = Long.valueOf(0L);
/*    */   }
/*    */   
/*    */ 
/*    */   public void process(Long startTS, int write, String assetName)
/*    */     throws IOException
/*    */   {
/* 40 */     if (write == 1)
/*    */     {
/* 42 */       Integer[] startEndIndex = (Integer[])this.tradeStartDateStartEndIndexMap.get(startTS);
/*    */       
/* 44 */       if (startEndIndex == null) {
/* 45 */         return;
/*    */       }
/* 47 */       ArrayList<String[]> listOfTrades = this.tradebookProcessor.getTrades(startEndIndex[0].intValue(), startEndIndex[1].intValue());
/*    */       
/* 49 */       for (String[] trade : listOfTrades)
/*    */       {
/* 51 */         this.writerTD.writeLine(trade);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/* 59 */     this.writerTD.close();
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/TradeFilteredTDWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */