/*    */ package com.q1.bt.execution;
/*    */ 
/*    */ import com.q1.bt.data.DataTypeViewer;
/*    */ import com.q1.bt.execution.order.Order;
/*    */ import com.q1.bt.execution.order.OrderSide;
/*    */ import com.q1.bt.execution.order.OrderType;
/*    */ import com.q1.bt.postprocess.PostProcessData;
/*    */ import com.q1.csv.CSVReader;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OrderbookStrategy
/*    */   extends Strategy
/*    */ {
/*    */   String strategyDataType;
/* 20 */   public transient HashMap<String, CSVReader> scripOrderBookReaderMap = new HashMap();
/*    */   
/*    */ 
/*    */   public OrderbookStrategy(String strategyDataType, String orderBookPath)
/*    */     throws IOException
/*    */   {
/* 26 */     this.strategyDataType = strategyDataType;
/*    */     
/*    */ 
/* 29 */     File orderBookFile = new File(orderBookPath);
/* 30 */     File[] arrayOfFile; int j = (arrayOfFile = orderBookFile.listFiles()).length; for (int i = 0; i < j; i++) { File orderBook = arrayOfFile[i];
/* 31 */       String scripID = orderBook.getName().substring(0, orderBook.getName().length() - 14);
/* 32 */       CSVReader orderBookReader = new CSVReader(orderBook.getAbsolutePath(), ',', 0);
/* 33 */       this.scripOrderBookReaderMap.put(scripID, orderBookReader);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ArrayList<Order> processStrategy(HashMap<String, DataTypeViewer> dataMap, HashMap<String, Long> positionMap, HashMap<String, Double> mtmMap, HashMap<String, Double> dayMTMMap, HashMap<String, ArrayList<String[]>> tradeBook, Double capital, Double riskPerTrade)
/*    */   {
/* 42 */     ArrayList<Order> orderBook = new ArrayList();
/* 43 */     DataTypeViewer data = (DataTypeViewer)dataMap.get(this.strategyDataType);
/* 44 */     Long curTimestamp = data.dateTime;
/*    */     
/*    */ 
/* 47 */     ArrayList<String> scripIDList = new ArrayList(data.scripDataViewerMap.keySet());
/*    */     
/*    */ 
/* 50 */     for (String scripID : scripIDList)
/*    */     {
/* 52 */       CSVReader orderBookReader = (CSVReader)this.scripOrderBookReaderMap.get(scripID);
/*    */       
/*    */       try
/*    */       {
/* 56 */         String[] dataLine = orderBookReader.getLastReadLine();
/*    */         
/*    */ 
/* 59 */         if (dataLine != null)
/*    */         {
/*    */ 
/*    */ 
/* 63 */           Long curOrderTimestamp = Long.valueOf(Long.parseLong(dataLine[0]));
/* 64 */           if (curOrderTimestamp.equals(curTimestamp))
/*    */           {
/*    */ 
/* 67 */             Order order = new Order(OrderSide.valueOf(dataLine[3]), OrderType.valueOf(dataLine[4]), 
/* 68 */               Double.parseDouble(dataLine[5]), Long.parseLong(dataLine[6]));
/* 69 */             orderBook.add(order);
/*    */             
/*    */ 
/* 72 */             while ((dataLine = orderBookReader.getNextLine()) != null) {
/* 73 */               curOrderTimestamp = Long.valueOf(Long.parseLong(dataLine[0]));
/* 74 */               if (!curOrderTimestamp.equals(curTimestamp))
/*    */                 break;
/* 76 */               order = new Order(OrderSide.valueOf(dataLine[3]), OrderType.valueOf(dataLine[4]), 
/* 77 */                 Double.parseDouble(dataLine[5]), Long.parseLong(dataLine[6]));
/* 78 */               orderBook.add(order);
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */       catch (IOException e) {
/* 84 */         e.printStackTrace();
/*    */       }
/*    */     }
/*    */     
/* 88 */     return orderBook;
/*    */   }
/*    */   
/*    */   public ArrayList<String[]> getParameterList()
/*    */   {
/* 93 */     return new ArrayList();
/*    */   }
/*    */   
/*    */   public ArrayList<PostProcessData> getPostProcessData()
/*    */   {
/* 98 */     return new ArrayList();
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/OrderbookStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */