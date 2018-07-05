/*    */ package com.q1.bt.machineLearning.utility;
/*    */ 
/*    */ import com.q1.csv.CSVReader;
/*    */ import com.q1.csv.CSVWriter;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.HashMap;
/*    */ import java.util.TreeMap;
/*    */ 
/*    */ public class TradeBookToOrderBookGenerator
/*    */ {
/*    */   private String tradeBookPath;
/*    */   private String orderBookPath;
/*    */   private HashMap<String, TreeMap<Long, Boolean>> assetTimeStampDecisionMap;
/*    */   
/*    */   public TradeBookToOrderBookGenerator(String sourcePath, String destPath, HashMap<String, TreeMap<Long, Boolean>> assetTimeStampDecisionMap)
/*    */   {
/* 18 */     this.tradeBookPath = (sourcePath + "/" + "Trade Data");
/* 19 */     this.orderBookPath = (destPath + "/" + "ML Order Data");
/* 20 */     this.assetTimeStampDecisionMap = assetTimeStampDecisionMap;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void generateOrderBooks()
/*    */     throws IOException
/*    */   {
/* 30 */     File masterTadeBookFolder = new File(this.tradeBookPath);
/*    */     File[] arrayOfFile1;
/* 32 */     int j = (arrayOfFile1 = masterTadeBookFolder.listFiles()).length; for (int i = 0; i < j; i++) { File strategyScripListTradeBookFolder = arrayOfFile1[i];
/*    */       
/* 34 */       String strategyScripListName = strategyScripListTradeBookFolder.getName();
/* 35 */       String orderBookFolder = this.orderBookPath + "/" + strategyScripListName;
/* 36 */       new File(orderBookFolder).mkdirs();
/*    */       
/*    */ 
/*    */       File[] arrayOfFile2;
/*    */       
/*    */ 
/* 42 */       int m = (arrayOfFile2 = strategyScripListTradeBookFolder.listFiles()).length; for (int k = 0; k < m; k++) { File strategyScripListScripTradeBook = arrayOfFile2[k];
/*    */         
/* 44 */         String scripName = strategyScripListScripTradeBook.getName();
/* 45 */         int scripNameLength = scripName.length() - 14;
/* 46 */         scripName = scripName.substring(0, scripNameLength);
/* 47 */         String assetName = strategyScripListName + " " + scripName;
/* 48 */         String orderBookPath = orderBookFolder + "/" + scripName + " OrderBook.csv";
/*    */         
/* 50 */         CSVReader tradeBookReader = new CSVReader(strategyScripListScripTradeBook.getPath(), ',', 0);
/* 51 */         CSVWriter orderBookWriter = new CSVWriter(orderBookPath, false, ",");
/*    */         
/* 53 */         processTradeBook(tradeBookReader, orderBookWriter, assetName);
/*    */         
/* 55 */         tradeBookReader.close();
/* 56 */         orderBookWriter.close();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private void processTradeBook(CSVReader tradeBookReader, CSVWriter orderBookWriter, String assetName) throws IOException
/*    */   {
/* 63 */     TreeMap<Long, Boolean> timeStampDecisionMap = (TreeMap)this.assetTimeStampDecisionMap.get(assetName);
/*    */     
/*    */     String[] tradeBookEntry;
/*    */     
/* 67 */     while ((tradeBookEntry = tradeBookReader.getLine()) != null) {
/*    */       String[] tradeBookEntry;
/* 69 */       Long tradeTimeStamp = Long.valueOf(Long.parseLong(tradeBookEntry[0]));
/* 70 */       tradeTimeStamp = Long.valueOf(tradeTimeStamp.longValue() / 1000000L);
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 76 */       if ((timeStampDecisionMap.get(tradeTimeStamp) != null) && (((Boolean)timeStampDecisionMap.get(tradeTimeStamp)).booleanValue()))
/*    */       {
/*    */ 
/*    */ 
/* 80 */         String orderTime = tradeBookEntry[10];
/* 81 */         String scripList = tradeBookEntry[9];
/* 82 */         String scripName = tradeBookEntry[2];
/* 83 */         String orderSide = tradeBookEntry[3];
/* 84 */         String orderType = tradeBookEntry[4];
/* 85 */         String price = tradeBookEntry[5];
/* 86 */         String qty = tradeBookEntry[7];
/*    */         
/* 88 */         if (!orderType.equals("ROLLOVER"))
/*    */         {
/*    */ 
/* 91 */           String orderEntry = orderTime + "," + scripList + "," + scripName + "," + orderSide + "," + orderType + "," + price + "," + qty;
/* 92 */           orderBookWriter.writeLine(orderEntry);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/TradeBookToOrderBookGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */