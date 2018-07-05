/*     */ package com.q1.bt.machineLearning.utility;
/*     */ 
/*     */ import com.q1.bt.postprocess.TradebookProcessor;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class TradeBookToOrderBookGenerator
/*     */ {
/*     */   private String tradeBookPath;
/*     */   private String orderBookPath;
/*     */   private HashMap<String, TreeMap<Long, Boolean>> assetTimeStampDecisionMap;
/*     */   public boolean bias;
/*     */   
/*     */   public TradeBookToOrderBookGenerator(String sourcePath, String destPath, HashMap<String, TreeMap<Long, Boolean>> assetTimeStampDecisionMap, boolean bias)
/*     */   {
/*  22 */     this.tradeBookPath = (sourcePath + "/" + "Trade Data");
/*  23 */     this.orderBookPath = (destPath + "/" + "ML Order Data");
/*  24 */     this.assetTimeStampDecisionMap = assetTimeStampDecisionMap;
/*  25 */     this.bias = bias;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void generateOrderBooks()
/*     */     throws IOException
/*     */   {
/*  35 */     File masterTadeBookFolder = new File(this.tradeBookPath);
/*     */     File[] arrayOfFile1;
/*  37 */     int j = (arrayOfFile1 = masterTadeBookFolder.listFiles()).length; for (int i = 0; i < j; i++) { File strategyScripListTradeBookFolder = arrayOfFile1[i];
/*     */       
/*  39 */       String strategyScripListName = strategyScripListTradeBookFolder.getName();
/*  40 */       String orderBookFolder = this.orderBookPath + "/" + strategyScripListName;
/*  41 */       new File(orderBookFolder).mkdirs();
/*     */       
/*     */ 
/*     */       File[] arrayOfFile2;
/*     */       
/*     */ 
/*  47 */       int m = (arrayOfFile2 = strategyScripListTradeBookFolder.listFiles()).length; for (int k = 0; k < m; k++) { File strategyScripListScripTradeBook = arrayOfFile2[k];
/*     */         
/*  49 */         String scripName = strategyScripListScripTradeBook.getName();
/*  50 */         int scripNameLength = scripName.length() - 14;
/*  51 */         scripName = scripName.substring(0, scripNameLength);
/*  52 */         String assetName = strategyScripListName + " " + scripName;
/*  53 */         String orderBookPath = orderBookFolder + "/" + scripName + " OrderBook.csv";
/*     */         try
/*     */         {
/*  56 */           TradebookProcessor tradeBookProcessor = new TradebookProcessor(strategyScripListScripTradeBook.getPath().replace("\\", "/"));
/*  57 */           CSVWriter orderBookWriter = new CSVWriter(orderBookPath, false, ",");
/*     */           
/*  59 */           processTradeBook(tradeBookProcessor, orderBookWriter, assetName);
/*     */           
/*  61 */           orderBookWriter.close();
/*     */         }
/*     */         catch (java.text.ParseException e) {
/*  64 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void processTradeBook(TradebookProcessor tradebookProcessor, CSVWriter orderBookWriter, String assetName)
/*     */     throws IOException
/*     */   {
/*  73 */     TreeMap<Long, Boolean> longTimeStampDecisionMap = null;TreeMap<Long, Boolean> shortTimeStampDecisionMap = null;TreeMap<Long, Boolean> timeStampDecisionMap = null;
/*  74 */     if (this.bias)
/*     */     {
/*  76 */       longTimeStampDecisionMap = (TreeMap)this.assetTimeStampDecisionMap.get("LON#" + assetName);
/*  77 */       shortTimeStampDecisionMap = (TreeMap)this.assetTimeStampDecisionMap.get("SHO#" + assetName);
/*     */     }
/*     */     else {
/*  80 */       timeStampDecisionMap = (TreeMap)this.assetTimeStampDecisionMap.get(assetName);
/*     */     }
/*     */     
/*  83 */     Boolean tradeStart = Boolean.valueOf(true);
/*     */     
/*  85 */     TreeMap<Long, ArrayList<Integer>> tradeDateIndexMap = tradebookProcessor.getTradeDateIndexMap();
/*     */     boolean buyBeforeSell;
/*  87 */     for (Iterator localIterator = tradeDateIndexMap.entrySet().iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */         buyBeforeSell.hasNext())
/*     */     {
/*  87 */       Map.Entry<Long, ArrayList<Integer>> tradeDateIndexMapEntry = (Map.Entry)localIterator.next();
/*     */       
/*  89 */       ArrayList<Integer> tradeBookIndex = (ArrayList)tradeDateIndexMapEntry.getValue();
/*  90 */       Long tradeTimeStamp = (Long)tradeDateIndexMapEntry.getKey();
/*  91 */       tradeTimeStamp = Long.valueOf(tradeTimeStamp.longValue() / 1000000L);
/*  92 */       ArrayList<String[]> listOfTrades = new ArrayList();
/*     */       
/*  94 */       int startIndex = ((Integer)tradeBookIndex.get(0)).intValue();
/*  95 */       int endIndex = ((Integer)tradeBookIndex.get(tradeBookIndex.size() - 1)).intValue();
/*  96 */       ArrayList<String[]> tempListOfTrades = tradebookProcessor.getTrades(startIndex, endIndex, true);
/*     */       
/*     */ 
/*     */ 
/* 100 */       if ((this.bias) && (tempListOfTrades.size() == 2))
/*     */       {
/* 102 */         boolean sellBeforeBuy = checkTradeOrderingMismatchCondition(longTimeStampDecisionMap, shortTimeStampDecisionMap, tradeTimeStamp, tempListOfTrades, "SELL");
/* 103 */         buyBeforeSell = checkTradeOrderingMismatchCondition(longTimeStampDecisionMap, shortTimeStampDecisionMap, tradeTimeStamp, tempListOfTrades, "BUY");
/*     */         
/* 105 */         if ((sellBeforeBuy) || (buyBeforeSell))
/*     */         {
/* 107 */           listOfTrades.add((String[])tempListOfTrades.get(1));
/* 108 */           listOfTrades.add((String[])tempListOfTrades.get(0));
/*     */         }
/*     */         else {
/* 111 */           listOfTrades = tempListOfTrades;
/*     */         }
/*     */       }
/*     */       else {
/* 115 */         listOfTrades = tempListOfTrades;
/*     */       }
/*     */       
/* 118 */       buyBeforeSell = listOfTrades.iterator(); continue;String[] tradeBookEntry = (String[])buyBeforeSell.next();
/*     */       
/* 120 */       String orderSide = tradeBookEntry[3];
/* 121 */       String orderType = tradeBookEntry[4];
/*     */       
/*     */ 
/*     */ 
/* 125 */       if (this.bias)
/*     */       {
/* 127 */         if (tradeStart.booleanValue())
/*     */         {
/* 129 */           if ((orderSide.equals("BUY")) && (longTimeStampDecisionMap.get(tradeTimeStamp) != null) && (((Boolean)longTimeStampDecisionMap.get(tradeTimeStamp)).booleanValue())) {
/* 130 */             writeOrder(tradeBookEntry, orderBookWriter);
/*     */           }
/* 132 */           else if ((orderSide.equals("SELL")) && (shortTimeStampDecisionMap.get(tradeTimeStamp) != null) && (((Boolean)shortTimeStampDecisionMap.get(tradeTimeStamp)).booleanValue())) {
/* 133 */             writeOrder(tradeBookEntry, orderBookWriter);
/*     */           }
/*     */           
/*     */         }
/* 137 */         else if ((longTimeStampDecisionMap.get(tradeTimeStamp) != null) && (((Boolean)longTimeStampDecisionMap.get(tradeTimeStamp)).booleanValue())) {
/* 138 */           writeOrder(tradeBookEntry, orderBookWriter);
/*     */         }
/* 140 */         else if ((shortTimeStampDecisionMap.get(tradeTimeStamp) != null) && (((Boolean)shortTimeStampDecisionMap.get(tradeTimeStamp)).booleanValue())) {
/* 141 */           writeOrder(tradeBookEntry, orderBookWriter);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 146 */       else if ((timeStampDecisionMap.get(tradeTimeStamp) != null) && (((Boolean)timeStampDecisionMap.get(tradeTimeStamp)).booleanValue())) {
/* 147 */         writeOrder(tradeBookEntry, orderBookWriter);
/*     */       }
/*     */       
/* 150 */       if (!orderType.equals("ROLLOVER")) {
/* 151 */         tradeStart = Boolean.valueOf(!tradeStart.booleanValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean checkTradeOrderingMismatchCondition(TreeMap<Long, Boolean> longTimeStampDecisionMap, TreeMap<Long, Boolean> shortTimeStampDecisionMap, Long tradeTimeStamp, ArrayList<String[]> tempListOfTrades, String tradeSide)
/*     */   {
/* 160 */     boolean scripExistence = longTimeStampDecisionMap.get(tradeTimeStamp) != null;
/*     */     
/* 162 */     if (!scripExistence) {
/* 163 */       return false;
/*     */     }
/* 165 */     boolean longPrediction = ((Boolean)longTimeStampDecisionMap.get(tradeTimeStamp)).booleanValue();
/* 166 */     boolean shortPrediction = ((Boolean)shortTimeStampDecisionMap.get(tradeTimeStamp)).booleanValue();
/* 167 */     boolean tradeSideFlag = ((String[])tempListOfTrades.get(0))[3].equals(tradeSide);
/*     */     
/* 169 */     if (tradeSide.equals("SELL")) {
/* 170 */       shortPrediction = !shortPrediction;
/*     */     } else {
/* 172 */       longPrediction = !longPrediction;
/*     */     }
/* 174 */     boolean returnVal = (scripExistence) && (longPrediction) && (shortPrediction) && (tradeSideFlag);
/*     */     
/* 176 */     return returnVal;
/*     */   }
/*     */   
/*     */   private void writeOrder(String[] tradeBookEntry, CSVWriter orderBookWriter)
/*     */   {
/*     */     try
/*     */     {
/* 183 */       String orderSide = tradeBookEntry[3];
/* 184 */       String orderType = tradeBookEntry[4];
/* 185 */       String orderTime = tradeBookEntry[10];
/* 186 */       String scripList = tradeBookEntry[9];
/* 187 */       String scripName = tradeBookEntry[2];
/*     */       
/* 189 */       String price = tradeBookEntry[5];
/* 190 */       String qty = tradeBookEntry[7];
/*     */       
/* 192 */       if (orderType.equals("ROLLOVER")) {
/* 193 */         return;
/*     */       }
/* 195 */       String orderEntry = orderTime + "," + scripList + "," + scripName + "," + orderSide + "," + orderType + "," + price + "," + qty;
/* 196 */       orderBookWriter.writeLine(orderEntry);
/*     */     }
/*     */     catch (IOException e) {
/* 199 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/TradeBookToOrderBookGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */